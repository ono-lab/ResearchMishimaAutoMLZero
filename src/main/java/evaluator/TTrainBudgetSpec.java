package evaluator;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import algorithm.spec.TAlgorithmSpec;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class TTrainBudgetSpec {
  /**
   * 基準となるアルゴリズムを生成するモデル
   */
  public TAlgorithmSpec baseAlgorithmSpec;

  /**
   * 基準アルゴリズムの何倍まで許すかの値
   */
  public double trainBudgetThresholdFactor = 2.0;

}
