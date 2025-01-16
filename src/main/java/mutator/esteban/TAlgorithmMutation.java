package mutator.esteban;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;

public enum TAlgorithmMutation {
  /**
   * Modifies a single parameter within one instruction. Does not change the op.
   */
  ALTER_PARAM(),
  /**
   * Randomizes an instruction, including its op.
   */
  RANDOMIZE_INSTRUCTION(),
  /**
   * Randomizes a whole component function, preserving its size.
   */
  RANDOMIZE_COMPONENT(),
  /**
   * Does nothing. Useful for debugging.
   */
  IDENTITY(),
  /**
   * Inserts an instruction in a component function.
   */
  INSERT_INSTRUCTION(),
  /**
   * Removes a instruction.
   */
  REMOVE_INSTRUCTION(),
  /**
   * Removes a instruction and inserts another.
   */
  TRADE_INSTRUCTION(),
  /**
   * Randomizes all component functions.
   */
  RANDOMIZE_ALGORITHM();


  @JsonCreator(mode = Mode.DELEGATING)
  public static TAlgorithmMutation of(String name) {
    for (var instance : values()) {
      if (instance.name().contentEquals(name))
        return instance;
    }
    throw new IllegalArgumentException();
  }
}
