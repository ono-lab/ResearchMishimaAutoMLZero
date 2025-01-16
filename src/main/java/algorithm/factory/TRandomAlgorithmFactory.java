package algorithm.factory;

import algorithm.core.*;
import algorithm.spec.TRandomAlgorithmSpec;
import memory.TMemory;
import randomizer.TAlgorithmRandomizer;
import utils.TRandomGenerator;

public class TRandomAlgorithmFactory extends TAlgorithmFactory<TRandomAlgorithmSpec> {
  private TAlgorithmOpsSet fOpsSet;
  private int fSetupSize;
  private int fPredictSize;
  private int fLearnSize;

  public TRandomAlgorithmFactory(TRandomAlgorithmSpec spec) {
    super(spec);
    fOpsSet = new TAlgorithmOpsSet(spec.setupOps, spec.predictOps, spec.learnOps);
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
    TAlgorithmRandomizer.executeAll(algorithm, fOpsSet, memory, rand);
    return algorithm;
  }
}
