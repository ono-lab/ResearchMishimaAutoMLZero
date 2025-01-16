package evaluator;

import algorithm.core.TAlgorithm;
import memory.TMemory;

/**
 * アルゴリズムの実行コストの上限を決めるためのクラス
 */
public class TTrainBudget {

  // 基準となるアルゴリズムのsetupおよびtrainにかかるコスト
  private double fBaselineSetupCost;
  private double fBaselineTrainCost;

  /// 基準の何倍まで許容するかを決める変数
  private double fThresholdFactor;

  /**
   * 指定されたbaselineAlgorithmのthresholdFactor倍までの実行コストを許容するTrainBudgetを作成．
   */
  public TTrainBudget(final TAlgorithm baselineAlgorithm, final double thresholdFactor) {
    fBaselineSetupCost = TCostComputer.execute(baselineAlgorithm.getSetup());
    fBaselineTrainCost = TCostComputer.execute(baselineAlgorithm.getPredict())
        + TCostComputer.execute(baselineAlgorithm.getLearn());
    fThresholdFactor = thresholdFactor;
  }

  /**
   * スペックファイルからTrainBudgetを作成．
   */
  public TTrainBudget(final TTrainBudgetSpec spec, TMemory memory) {
    this(spec.baseAlgorithmSpec.create(memory), spec.trainBudgetThresholdFactor);
  }

  /**
   * 与えられたalgorithmでsuggestedNumOfTrainExamples回学習を行うときのコストが許容範囲内ならば，suggestedNumOfTrainExamplesを，それ以外の場合は0を返却する関数．
   */
  public int getNumOfTrainExamplesInBudget(final TAlgorithm algorithm,
      final int suggestedNumOfTrainExamples) {
    double setupCost = TCostComputer.execute(algorithm.getSetup());
    double trainCost =
        TCostComputer.execute(algorithm.getPredict()) + TCostComputer.execute(algorithm.getLearn());
    double baselineCost = fBaselineSetupCost + suggestedNumOfTrainExamples * fBaselineTrainCost;
    double suggestedCost = setupCost + suggestedNumOfTrainExamples * trainCost;
    if (suggestedCost <= baselineCost * fThresholdFactor) {
      return suggestedNumOfTrainExamples;
    } else {
      return 0;
    }
  }
}
