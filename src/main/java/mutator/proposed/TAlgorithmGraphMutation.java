package mutator.proposed;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;


public enum TAlgorithmGraphMutation {
  ALTER_CONST_VALUE(),
  ALTER_PARAMETER_INITIAL_VALUE(),
  REPLACE_CONST_NODE_WITH_CONNECTION(),
  REPLACE_CONNECTION_WITH_CONST_NODE(),
  CHANGE_CONNECTION(),
  RECONSTRUCT_SUB_GRAPH(),
  RECONSTRUCT_ROOT_NODE();

  @JsonCreator(mode = Mode.DELEGATING)
  public static TAlgorithmGraphMutation of(String name) {
    for (var instance : values()) {
      if (instance.name().contentEquals(name))
        return instance;
    }
    throw new IllegalArgumentException();
  }

  public boolean isDiscrete() {
    return this != ALTER_CONST_VALUE && this != ALTER_PARAMETER_INITIAL_VALUE;
  }
}
