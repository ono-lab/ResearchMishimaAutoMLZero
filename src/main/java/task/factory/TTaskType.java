package task.factory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;

public enum TTaskType {
  CLASSIFICATION, REGRESSION;

  @JsonCreator(mode = Mode.DELEGATING)
  public static TTaskType of(String name) {
    for (var instance : values()) {
      if (instance.name().contentEquals(name))
        return instance;
    }
    throw new IllegalArgumentException();
  }
}
