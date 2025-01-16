package evaluator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import utils.TMathUtility;

public enum TFitnessCombinationMode {
  MEAN(), MEDIAN();

  @JsonCreator(mode = Mode.DELEGATING)
  public static TFitnessCombinationMode of(String name) {
    for (var instance : values()) {
      if (instance.name().contentEquals(name))
        return instance;
    }
    throw new IllegalArgumentException();
  }

  public double combine(double[] fitnesses) {
    switch (this) {
      case MEAN:
        return TMathUtility.mean(fitnesses);
      case MEDIAN:
        return TMathUtility.median(fitnesses);
      default:
        throw new RuntimeException("Unsupported fitness combination.");
    }
  }
}
