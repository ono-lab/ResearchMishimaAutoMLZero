package evaluator;

import java.util.ArrayList;
import utils.*;
import memory.*;
import task.core.TTask;
import task.spec.TTaskSpec;
import algorithm.core.TAlgorithm;
import algorithm.core.TAlgorithmExecutor;

public class TEvaluator {
  private static final int kMinNumOfTrainExamples = 10;
  private static final long kFECCacheRandomSeed = 235732282;

  private TFitnessCombinationMode fFitnessCombinationMode;

  private TTrainBudget fTrainBudget;

  private TMemory fMemory;

  private TRandomGenerator fRand;

  private ArrayList<TTask> fTasks = new ArrayList<TTask>();

  private TFECCache fFECCache;
  private TRandomGenerator fFECRand = new TRandomGenerator(kFECCacheRandomSeed);
  private ArrayList<Double> fFECTrainErrors = new ArrayList<Double>();
  private ArrayList<Double> fFECValidErrors = new ArrayList<Double>();

  private double fMaxAbsError;
  private long fNumOfTrainCompletedSteps = 0;

  public TEvaluator(final TFitnessCombinationMode fitnessCombinationMode,
      final TTaskSpec[] taskSpecs, final TFECCache FECCache, TTrainBudget trainBudget,
        final double maxAbsError, final TMemory memory, final TRandomGenerator rand) {
    fFitnessCombinationMode = fitnessCombinationMode;
    fFECCache = FECCache;
    fTrainBudget = trainBudget;
    fRand = rand;
    fMaxAbsError = maxAbsError;
    for (TTaskSpec spec : taskSpecs) {
      spec.addTasksTo(fTasks);
    }
    assert fTasks.size() > 0;
    fMemory = memory;
    fMemory.initialize(fTasks.get(0).getFeatureSize());
  }

  public TEvaluator(TEvaluatorSpec spec, final TMemory memory, TRandomGenerator rand) {
    this(
      spec.fitnessCombinationMode,
      spec.taskSpecs, spec.FECSpec == null ? null : new TFECCache(spec.FECSpec),
      spec.trainBudgetSpec == null ? null : new TTrainBudget(spec.trainBudgetSpec, memory),
      spec.maxAbsError,
      memory,
      rand
    );
  }


  public double execute(TAlgorithm algorithm) {
    double[] fitnesses = new double[fTasks.size()];
    for (int taskIndex = 0; taskIndex < fTasks.size(); taskIndex++) {
      TTask task = fTasks.get(taskIndex);
      int numOfMaxTrainExamples = task.getNumOfMaxTrainExamples();
      assert numOfMaxTrainExamples >= kMinNumOfTrainExamples;
      // アルゴリズムがbudgetを超えてたら学習しない（numOfTrainExamples = 0）
      int numOfTrainExamples = fTrainBudget == null ? numOfMaxTrainExamples
          : fTrainBudget.getNumOfTrainExamplesInBudget(algorithm, numOfMaxTrainExamples);
      fitnesses[taskIndex] = executeForOneTask(algorithm, task, numOfTrainExamples);
    }
    double combinedFitnesses = fFitnessCombinationMode.combine(fitnesses);
    return combinedFitnesses;
  }

  private double executeForOneTaskWithCache(TAlgorithm algorithm, TTask task, int numOfTrainExamples) {
    // キャッシュが指定されている場合
    assert fFECCache.getNumOfTrainExamples() <= task.getNumOfMaxTrainExamples();
    assert fFECCache.getNumOfValidExamples() <= task.getNumOfValidExamples();

    final TAlgorithmExecutor FECExecutor = new TAlgorithmExecutor(algorithm, task, fFECCache.getNumOfTrainExamples(),
        fFECCache.getNumOfValidExamples(), fMaxAbsError, fMemory, fFECRand);
    fFECTrainErrors.clear();
    fFECValidErrors.clear();
    FECExecutor.execute(fFECTrainErrors, fFECValidErrors);
    fNumOfTrainCompletedSteps += FECExecutor.getNumOfTrainCompletedSteps();
    final long hash = THashGenerator.getWellMixedHash(fFECTrainErrors, fFECValidErrors,
        task.getIndex(), numOfTrainExamples);
    final double cachedFitness = fFECCache.getCachedFitness(hash);

    // キャッシュがヒットした場合
    if (!Double.isNaN(cachedFitness)) {
      return cachedFitness;
    }
    // キャッシュがヒットしていない場合
    else {
      final double fitness = executeForOneTaskWithoutCache(algorithm, task, numOfTrainExamples);
      fFECCache.put(hash, fitness); // キャッシュに書き込む
      return fitness;
    }
  }

  private double executeForOneTaskWithoutCache(TAlgorithm algorithm, TTask task, int numOfTrainExamples) {
    final TAlgorithmExecutor executor = new TAlgorithmExecutor(algorithm, task,
        numOfTrainExamples, task.getNumOfValidExamples(), fMaxAbsError, fMemory, fRand);
    final double fitness = executor.execute();
    fNumOfTrainCompletedSteps += executor.getNumOfTrainCompletedSteps();
    return fitness;
  }

  private double executeForOneTask(TAlgorithm algorithm, TTask task, int numOfTrainExamples) {
    if (fMemory.getDim() != task.getFeatureSize()) {
      fMemory.initialize(task.getFeatureSize());
    }
    if (fFECCache != null) {
      return executeForOneTaskWithCache(algorithm, task, numOfTrainExamples);
    } else {
      return executeForOneTaskWithoutCache(algorithm, task, numOfTrainExamples);
    }
  }

  public long getNumOfTrainCompletedSteps() {
    return fNumOfTrainCompletedSteps;
  }

  public void incrementFECGeneration() {
    if (fFECCache != null)
      fFECCache.incrementGeneration();
  }
}
