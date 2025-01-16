package algorithm.factory;

import algorithm.core.*;
import algorithm.spec.TNoOpAlgorithmSpec;
import memory.TMemory;
import utils.TRandomGenerator;

public class TNoOpAlgorithmFactory extends TAlgorithmFactory<TNoOpAlgorithmSpec> {
  private int fSetupSize;
  private int fPredictSize;
  private int fLearnSize;

  public TNoOpAlgorithmFactory(TNoOpAlgorithmSpec spec) {
    super(spec);
    fSetupSize = spec.setupSize;
    fPredictSize = spec.predictSize;
    fLearnSize = spec.learnSize;
  }

  @Override
  public TAlgorithm create(final TMemory memory, TRandomGenerator rand) {
    TAlgorithm algorithm = new TAlgorithm();
    algorithm.fillComponentWithNoOp(TAlgorithmComponentType.SETUP, fSetupSize);
    algorithm.fillComponentWithNoOp(TAlgorithmComponentType.PREDICT,fPredictSize);
    algorithm.fillComponentWithNoOp(TAlgorithmComponentType.LEARN,fLearnSize);
    return algorithm;
  }
}
