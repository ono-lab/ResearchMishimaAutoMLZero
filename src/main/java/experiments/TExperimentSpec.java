package experiments;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import evaluator.TEvaluatorSpec;
import logger.TEliteIndividualsLoggerSpec;
import logger.TImprovementLoggerSpec;
import logger.TProgressLoggerSpec;

/**
 * Stores the entire configuration of an experiment.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class TExperimentSpec {
  // 評価機の設定
  public TEvaluatorSpec searchEvaluatorSpec;
  public TEvaluatorSpec evalEvaluatorSpec;

  // 打ち切り評価値、この値を超えた場合は探索を打ち切る、0.0の場合は打ち切りなし
  public double sufficientFitness = 0.0;

  // 評価回数の上限
  public long maxNumOfEvaluations = Long.MAX_VALUE;

  // ログをターミナルに出力する頻度、指定した値の世代ごとに出力される
  public int progressEvery = 1;

  // ログのCSV出力の設定
  public TProgressLoggerSpec progressLoggerSpec = null;

  // エリート個体の出力設定
  public int outputEliteEvery = 30;

  // エリート個体の出力設定
  public TEliteIndividualsLoggerSpec eliteLoggerSpec = null;

  // 改善推移の出力設定
  public TImprovementLoggerSpec improvementLoggerSpec = null;

  // 実験回数
  public int maxNumOfExperiments = 1;

  // 乱数のシード
  public long searchSeed = 0l;
  public long evalSeed = 32768l;

  // 探索ごとに乱数の設定を変更するか
  public boolean changeSearchTasks = true;
  public boolean changeEvalTasks = true;
  public boolean changeInitialization = true;
  public boolean changeEvolution = true;
  public boolean changeSearchEvaluator = true;
  public boolean changeEvalEvaluator = true;
}
