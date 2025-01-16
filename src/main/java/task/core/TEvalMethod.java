package task.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import utils.TMathUtility;

public enum TEvalMethod {
  RMS_ERROR(), ACCURACY();

  @JsonCreator(mode = Mode.DELEGATING)
  public static TEvalMethod of(String name) {
    for (var instance : values()) {
      if (instance.name().contentEquals(name))
        return instance;
    }
    throw new IllegalArgumentException();
  }

  /**
   * 予測ラベルの出力を評価方法に合わせてリスケールする関数
   * @param memory
   */
  public double normalize(double prediction) {
    switch (this) {
      case RMS_ERROR:
        return prediction;
      case ACCURACY:
        return TMathUtility.sigmoid(prediction);
      default:
        throw new RuntimeException("Invalid eval type.");
    }
  }

  public double error(double predicted, double actual) {
    switch (this) {
      case RMS_ERROR: {
        return actual - predicted;
      }
      case ACCURACY: {
        if (predicted > 1.0 || predicted < 0.0) {
          return Double.MAX_VALUE;
        } else {
          boolean isCorrect = (predicted > 0.5) == (actual > 0.5);
          return isCorrect ? 0.0 : 1.0;
        }
      }
      default:
        throw new RuntimeException("Invalid eval type.");
    }
  }

  public double loss(double predicted, double actual) {
    switch (this) {
      case RMS_ERROR: {
        double error = error(predicted, actual);
        return error * error;
      }
      case ACCURACY: {
        return error(predicted, actual);
      }
      default:
        throw new RuntimeException("Invalid eval type.");
    }
  }

  public double rescale(double lossMean) {
    switch (this) {
      case RMS_ERROR:
        return TMathUtility.squash(Math.sqrt(lossMean));
      case ACCURACY:
        return lossMean;
    }
    throw new RuntimeException("Invalid eval type.");
  }
}
