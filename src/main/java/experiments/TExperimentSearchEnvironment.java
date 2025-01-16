package experiments;

import evaluator.TEvaluator;
import logger.TEliteIndividualsLogger;
import logger.TImprovementLogger;
import logger.TProgressLogger;
import memory.TMemory;
import population.TIndividualBase;
import population.TPopulation;
import population.TPopulationStats;
import task.spec.TTaskSpec;
import utils.TRandomGenerator;

public class TExperimentSearchEnvironment<I extends TIndividualBase> {
  static private final long kEvaluatorSeedOffset = 1024l;
  static private final long kInitSeedOffset = 2048l;
  static private final long kEvolutionSeedOffset = 3072l;
  static private final long kMillisPerSecond = 1000;

  private TMemory fMemory;
  private TRandomGenerator fEvaluatorRand;
  private TRandomGenerator fInitRand;
  private TRandomGenerator fEvolutionRand;
  private TEvaluator fEvaluator;
  private TProgressLogger fProgressLogger;
  private TEliteIndividualsLogger<I> fEliteIndividualsLogger;
  private TImprovementLogger<I> fImprovementLogger;

  private int fTrialNo;
  private long fMaxNumOfEvaluations;
  private double fMaxFitness;
  private int fProgressEvery;
  private int fOutputEliteEvery;

  private int fGeneration = 0;

  private int fNumOfEvaluations = 0;

  private int fNumOfEvaluationsLastProgress;
  private int fNumOfEvaluationsLastEliteOutput;

  private long fStartSecs;
  private long fEpochSecs;

  public TExperimentSearchEnvironment(TExperimentInstance instance, int trialNo) {
    TExperimentSpec spec = instance.getExperimentSpec();

    if (spec.changeSearchTasks) {
      TTaskSpec.randomizeSeeds(spec.searchEvaluatorSpec.taskSpecs, trialNo);
    }

    fMemory = new TMemory();

    long evaluatorSeed = spec.changeSearchEvaluator ? trialNo + spec.searchSeed : spec.searchSeed;
    long initSeed = spec.changeInitialization ? trialNo + spec.searchSeed : spec.searchSeed;
    long evolutionSeed = spec.changeEvolution ? trialNo + spec.searchSeed : spec.searchSeed;

    fInitRand = new TRandomGenerator(initSeed + kInitSeedOffset);
    fEvolutionRand = new TRandomGenerator(evolutionSeed + kEvolutionSeedOffset);
    fEvaluatorRand = new TRandomGenerator(evaluatorSeed + kEvaluatorSeedOffset);

    fEvaluator = new TEvaluator(spec.searchEvaluatorSpec, fMemory, fEvaluatorRand);

    if (spec.progressLoggerSpec != null) {
      fProgressLogger = new TProgressLogger(spec.progressLoggerSpec, instance);
    }
    if (spec.eliteLoggerSpec != null) {
      fEliteIndividualsLogger = new TEliteIndividualsLogger<I>(spec.eliteLoggerSpec, instance);
    }
    if (spec.improvementLoggerSpec != null) {
      fImprovementLogger = new TImprovementLogger<I>(spec.improvementLoggerSpec, instance);
    }

    fTrialNo = trialNo;
    fMaxNumOfEvaluations = spec.maxNumOfEvaluations;
    fMaxFitness = spec.sufficientFitness;
    fProgressEvery = spec.progressEvery;
    fOutputEliteEvery = spec.outputEliteEvery;
  }

  public TMemory getMemory() {
    return fMemory;
  }

  public TEvaluator getEvaluator() {
    return fEvaluator;
  }

  public TRandomGenerator getInitRand() {
    return fInitRand;
  }

  public TRandomGenerator getEvolutionRand() {
    return fEvolutionRand;
  }

  public long getNumOfEvaluatedIndividuals() {
    return fNumOfEvaluations;
  }

  public void incrementNumOfEvaluatedIndividuals() {
    fNumOfEvaluations++;
  }

  public void incrementGeneration() {
    fGeneration++;
    fEvaluator.incrementFECGeneration();
  }

  public void updateEpochSecs() {
    fEpochSecs = System.currentTimeMillis() / kMillisPerSecond;
  }

  public long getElapsedSecs() {
    return fEpochSecs - fStartSecs;
  }

  public void start() {
    if (fProgressLogger != null) {
      fProgressLogger.open(fTrialNo);
    }
    if (fEliteIndividualsLogger != null) {
      fEliteIndividualsLogger.open(fTrialNo);
    }
    if (fImprovementLogger != null) {
      fImprovementLogger.open(fTrialNo);
    }
    fStartSecs = System.currentTimeMillis() / kMillisPerSecond;
  }

  public void stop() {
    if (fProgressLogger != null) {
      fProgressLogger.close();
    }
    if (fEliteIndividualsLogger != null) {
      fEliteIndividualsLogger.close();
    }
    if (fImprovementLogger != null) {
      fImprovementLogger.close();
    }
  }

  public void maybeWriteProgress(String mean, String stdev, String bestFit) {
    if (fProgressLogger == null) {
      return;
    }
    fProgressLogger.write(fTrialNo, fGeneration, fNumOfEvaluations, getElapsedSecs(), mean, stdev, bestFit);
  }

  public void putProgress(TPopulation<I> population, I best) {
    TPopulationStats<I> stats = population.getStats();
    String mean = String.format("%05f", stats.getMean());
    String stdev = String.format("%05f", stats.getStdev());
    String bestFit = String.format("%05f", best.getFitness());
    String progress = "";
    progress += "trial_no=" + fTrialNo + ", ";
    progress += "generation=" + fGeneration + ", ";
    progress += "evaluations_num=" + fNumOfEvaluations + ", ";
    progress += "elapsed_secs=" + getElapsedSecs() + ", ";
    progress += "mean=" + mean + ", ";
    progress += "stdev=" + stdev + ", ";
    progress += "best_fit=" + bestFit + ", ";
    System.out.println(progress);
    maybeWriteProgress(mean, stdev, bestFit);
    fNumOfEvaluationsLastProgress = fNumOfEvaluations;
  }

  public void maybePutProgress(TPopulation<I> population, I best) {
    if (fNumOfEvaluations >= fNumOfEvaluationsLastProgress + fProgressEvery
        || fNumOfEvaluations >= fMaxNumOfEvaluations
        || best.getFitness() >= fMaxFitness) {
      putProgress(population, best);
    }
  }

  public void writeEliteIndividuals(TPopulation<I> population) {
    if (fEliteIndividualsLogger == null) {
      return;
    }
    fEliteIndividualsLogger.write(fTrialNo, fGeneration, population);
    fNumOfEvaluationsLastEliteOutput = fNumOfEvaluations;
  }

  public void maybeWriteEliteIndividuals(TPopulation<I> population) {
    if (fOutputEliteEvery > 0
        && (fNumOfEvaluations >= fNumOfEvaluationsLastEliteOutput + fOutputEliteEvery
            || fNumOfEvaluations >= fMaxNumOfEvaluations)) {
      writeEliteIndividuals(population);
    }
  }

  public void maybeWriteImprovement(I currentBest, I oldBest) {
    if (fImprovementLogger == null) {
      return;
    }
    if (currentBest.getFitness() < oldBest.getFitness()) {
      return;
    }
    if (currentBest == oldBest) {
      return;
    }
    fImprovementLogger.write(fTrialNo, fGeneration, fNumOfEvaluations, currentBest);
  }

  public boolean shouldContinue(I best) {
    return fNumOfEvaluations < fMaxNumOfEvaluations
        && (best.getFitness() < fMaxFitness || fMaxFitness == 0.0);
  }
}
