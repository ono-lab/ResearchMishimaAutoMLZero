package algorithm.factory;

import algorithm.core.TAlgorithm;
import algorithm.core.TAlgorithmComponentType;
import algorithm.spec.TFilledAlgorithmSpec;
import instruction.TInstruction;
import memory.TMemory;
import utils.TRandomGenerator;

/**
 * A Algorithm with specific instruction.
 */
public class TFilledAlgorithmFactory extends TAlgorithmFactory<TFilledAlgorithmSpec> {
  private TInstruction fInstruction;
  private int fSetupSize;
  private int fPredictSize;
  private int fLearnSize;

  public TFilledAlgorithmFactory(TFilledAlgorithmSpec spec) {
    super(spec);
    fInstruction = spec.instruction;
    fSetupSize = spec.setupSize;
    fPredictSize = spec.predictSize;
    fLearnSize = spec.learnSize;
  }

  @Override
  public TAlgorithm create(final TMemory memory, TRandomGenerator rand) {
    TAlgorithm algorithm = new TAlgorithm();
    algorithm.fillComponentWith(TAlgorithmComponentType.SETUP, fInstruction, fSetupSize);
    algorithm.fillComponentWith(TAlgorithmComponentType.PREDICT, fInstruction, fPredictSize);
    algorithm.fillComponentWith(TAlgorithmComponentType.LEARN, fInstruction, fLearnSize);
    return algorithm;
  }
}
