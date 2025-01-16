package task.spec;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import task.factory.TTaskType;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
abstract public class TAlgorithmTaskSpec extends TTaskSpec {
  /**
   * ガウシアンノイズの標準偏差
   */
  public double gaussianNoiseStdev = 0.0;

  /**
   * タスクの種類
   */
  public TTaskType taskType;
}
