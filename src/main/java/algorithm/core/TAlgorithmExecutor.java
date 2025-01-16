package algorithm.core;

import java.util.ArrayList;
import jp.ac.titech.onolab.core.matrix.TCMatrix;
import utils.*;
import memory.*;
import task.core.*;
import instruction.*;

/**
 * アルゴリズムの実行を担うクラス
 */
public class TAlgorithmExecutor {

  /** 評価値の最小値 */
  static final double kMinFitness = 0.0;

  /** 評価値の最大値 */
  static final double kMaxFitness = 1.0;

  /** 実行中のアルゴリズム */
  private TAlgorithm fAlgorithm;

  /** 実行に利用するデータセット */
  private TTask fDataset;

  /**
   * 学習データの数
   * タスクに含まれているデータ数より少ない場合は自動調整される
   */
  private int fNumOfAllTrainExamples;

  /**
   * 検証データの数
   * タスクに含まれているデータ数より少ない場合は自動調整される
   */
  private int fNumOfAllValidExamples;

  /**
   * エラーの最大値
   * この値を超えるエラーが発生している場合は、アルゴリズムが暴走している可能性が高いため、早期終了する。
   * 早期停止は、損失が無限大、NaN、または非常に大きい場合に発生する。
   * 早期停止が発生した場合、実行の評価値は最小値に設定される。
   */
  private double fMaxAbsError;

  /** 乱数生成器 */
  private TRandomGenerator fRand;

  /** メモリ */
  private TMemory fMemory;

  /** 学習が完了したステップ数 */
  private long fNumOfTrainCompletedSteps = 0;

  /**
   * 標準的なアルゴリズムの実行を行う用のコンストラクタ
   */
  public TAlgorithmExecutor(final TAlgorithm algorithm, final TTask dataset,
      final int numOfAllTrainExamples, final int numOfAllValidExamples, final double maxAbsError,
       final TMemory memory, final TRandomGenerator rand) {
    assert dataset.getFeatureSize() == memory.getDim();
    fAlgorithm = algorithm;
    fDataset = dataset;
    fNumOfAllTrainExamples = numOfAllTrainExamples;
    fNumOfAllValidExamples = numOfAllValidExamples;
    fMemory = memory;
    fMaxAbsError = maxAbsError;
    fRand = rand;
    setup();
  };

  /**
   * 学習が完了したステップ数を取得する関数
   */
  public long getNumOfTrainCompletedSteps() {
    return fNumOfTrainCompletedSteps;
  }

  /**
   * Setup関数の実行
   */
  public static void setup(TAlgorithm algorithm, TMemory memory, TRandomGenerator rand) {
    memory.wipe();
    for (TInstruction instruction : algorithm.getSetup()) {
      instruction.execute(rand, memory);
    }
  }

  /**
   * Setup関数の実行
   */
  public void setup() {
    setup(fAlgorithm, fMemory, fRand);
  }

  public static double predictWithoutNormalize(TAlgorithm algorithm, TCMatrix feature, TMemory memory, TRandomGenerator rand) {
    memory.setInputVector(feature.clone());
    memory.resetLabel();
    for (TInstruction instruction : algorithm.getPredict()) {
      instruction.execute(rand, memory);
    }
    double prediction = memory.getPredictionLabel();
    return prediction;
  }

  /**
   * Predict関数の実行
   */
  public static double predict(TAlgorithm algorithm, TCMatrix feature, TEvalMethod evalMethod, TMemory memory,
      TRandomGenerator rand) {
    double prediction = predictWithoutNormalize(algorithm, feature, memory, rand);
    prediction = evalMethod.normalize(prediction);
    memory.setPredictionLabel(prediction);
    return prediction;
  }

  /**
   * Predict関数の実行
   */
  public double predict(TCMatrix feature) {
    return predict(fAlgorithm, feature, fDataset.getEvalType(), fMemory, fRand);
  }

  /**
   * Learn関数の実行
   */
  public static void learn(TAlgorithm algorithm, TCMatrix feature, double label, TMemory memory, TRandomGenerator rand) {
    memory.setInputVector(feature.clone());
    memory.setCorrectLabel(label);
    for (TInstruction instruction : algorithm.getLearn()) {
      instruction.execute(rand, memory);
    }
  }

  /**
   * Learn関数の実行
   */
  public void learn(TCMatrix feature, double label) {
    learn(fAlgorithm, feature, label, fMemory, fRand);
  }

  /**
   * 学習の実行
   */
  public boolean train(int maxSteps, TTaskIterator trainIterator, ArrayList<Double> errors) {
    TEvalMethod evalMethod = fDataset.getEvalType();
    for (int step = 0; step < maxSteps; step++) {
      fNumOfTrainCompletedSteps++;

      // Run predict component function for this example.
      TCMatrix feature = trainIterator.getFeature();
      double prediction = predict(feature);
      double label = trainIterator.getLabel();
      double error = evalMethod.error(prediction, label);

      // Check whether we should stop early.
      double absError = Math.abs(error);
      if (Double.isInfinite(absError) || Double.isNaN(absError) || absError >= fMaxAbsError) {
        return false;
      }
      if (errors != null) {
        errors.add(absError);
      }

      // Run learn component function for this example.
      learn(feature, label);

      // Check whether we are done.
      trainIterator.next();
      if (trainIterator.hasDone()) {
        break; // Reached the end of the dataset.
      }
    }
    return true;
  }

  /**
   * 検証の実行
   */
  public double validate(ArrayList<Double> errors) {
    double loss = 0.0;
    int numOfSteps = Math.min(fNumOfAllValidExamples, fDataset.getNumOfValidExamples());
    TTaskIterator validIterator = fDataset.getValidIterator();
    TEvalMethod evalMethod = fDataset.getEvalType();
    for (int step = 0; step < numOfSteps; step++) {
      TCMatrix feature = validIterator.getFeature();
      double prediction = predict(feature);
      double label = validIterator.getLabel();

      double error = evalMethod.error(prediction, label);
      loss += evalMethod.loss(prediction, label);

      double absError = Math.abs(error);
      if (Double.isInfinite(absError) || Double.isNaN(absError) || absError >= fMaxAbsError) {
        // Stop early. Return infinite loss.
        return kMinFitness;
      }

      if (errors != null) {
        errors.add(error);
      }

      // Check whether we are done.
      validIterator.next();
      if (validIterator.hasDone()) {
        break; // Reached the end of the dataset.
      }
    }

    loss = fDataset.getEvalType().rescale(loss / fDataset.getNumOfValidExamples());
    double fitness = 1 - loss;

    assert fitness <= kMaxFitness;
    assert fitness >= kMinFitness;
    return fitness;
  }


  /**
   * アルゴリズムを実行し、最も良かったエポックのFitnessを返却する。
   * 学習中のErrorはtrainErrorsに、検証中のエラーはvalidErrorsに格納される。
   * ただし，格納は第一エポックのみである。
   */
  public double execute(ArrayList<Double> trainErrors, ArrayList<Double> validErrors) {
    assert fDataset.getNumOfTrainEpochs() >= 1;
    assert trainErrors == null || trainErrors.size() == 0;
    assert validErrors == null || validErrors.size() == 0;

    // Iterators that track the progress of training.
    TTaskIterator trainIterator = fDataset.getTrainIterator();

    // Train for multiple epochs, evaluate on validation set
    // after each epoch and take the best validation result as fitness.
    int numOfAllTrainExamples =
        Math.min(fNumOfAllTrainExamples, fDataset.getNumOfMaxTrainExamples());
    int numOfTrainExamplesPerEpoch =
        Math.min(numOfAllTrainExamples, fDataset.getNumOfTrainExamplesPerEpoch());
    int numOfRemaining = numOfAllTrainExamples;

    double bestFitness = kMinFitness;
    while (numOfRemaining > 0) {
      boolean isSuccess =
          train(Math.min(numOfTrainExamplesPerEpoch, numOfRemaining), trainIterator, trainErrors);
      if (!isSuccess) {
        if (numOfRemaining == numOfAllTrainExamples)
          return kMinFitness;
        else
          break;
      }
      numOfRemaining -= numOfTrainExamplesPerEpoch;
      double currentFitness = validate(validErrors);
      bestFitness = Math.max(currentFitness, bestFitness);
      // Only save the errors of the first epoch.
      if (trainErrors != null) {
        trainErrors = null;
        validErrors = null;
      }
    }
    return bestFitness;
  }

  /**
   * アルゴリズムを実行し、最も良かったエポックのFitnessを返却する。
   */
  public double execute() {
    return execute(null, null);
  }

  /**
   * アルゴリズムを実行し、最も良かったエポックのFitnessを返却する。
   */
  public static double execute(final TAlgorithm algorithm, final TTask dataset,
      final int numOfAllTrainExamples, final int numOfAllValidExamples, final double maxAbsError,
      final TMemory memory, final TRandomGenerator rand, ArrayList<Double> trainErrors, ArrayList<Double> validErrors) {
    TAlgorithmExecutor executor = new TAlgorithmExecutor(algorithm, dataset, numOfAllTrainExamples,
        numOfAllValidExamples, maxAbsError, memory, rand);
    return executor.execute(trainErrors, validErrors);
  }

  /**
   * アルゴリズムを実行し、最も良かったエポックのFitnessを返却する。
   */
  public static double execute(final TAlgorithm algorithm, final TTask dataset,
      final int numOfAllTrainExamples, final int numOfAllValidExamples, final double maxAbsError,
      final TMemory memory, final TRandomGenerator rand) {
    TAlgorithmExecutor executor = new TAlgorithmExecutor(algorithm, dataset, numOfAllTrainExamples,
        numOfAllValidExamples, maxAbsError, memory, rand);
    return executor.execute();
  }
}
