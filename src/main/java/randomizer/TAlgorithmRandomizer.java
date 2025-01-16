package randomizer;

import algorithm.core.*;
import instruction.*;
import memory.TMemory;
import utils.TRandomGenerator;

public class TAlgorithmRandomizer {
  /**
   * Randomizes all the instructions in the component function. Does not change the component
   * function size.
   */
  public static TAlgorithm execute(TAlgorithm algorithm, TAlgorithmComponentType componentType,
      TAlgorithmOpsSet opsSet, TMemory memory, TRandomGenerator rand) {
    for (TInstruction instr : algorithm.getComponent(componentType)) {
      TOp op = opsSet.getRandomOp(componentType, rand);
      TInstructionRandomizer.setOpAndRandomize(instr, op, componentType, memory, rand);
    }
    return algorithm;
  }

  public static TAlgorithm executeAll(TAlgorithm algorithm, TAlgorithmOpsSet opsSet,
      TMemory memory, TRandomGenerator rand) {
    execute(algorithm, TAlgorithmComponentType.SETUP, opsSet, memory, rand);
    execute(algorithm, TAlgorithmComponentType.PREDICT, opsSet, memory, rand);
    execute(algorithm, TAlgorithmComponentType.LEARN, opsSet, memory, rand);
    return algorithm;
  }
}
