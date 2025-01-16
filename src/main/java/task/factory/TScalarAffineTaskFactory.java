package task.factory;

import task.spec.*;
import utils.TRandomGenerator;
import algorithm.core.TAlgorithm;
import algorithm.factory.TAffineAlgorithmFactory;
import algorithm.spec.TAffineAlgorithmSpec;
import jp.ac.titech.onolab.core.matrix.TCMatrix;
import memory.TMemory;

public class TScalarAffineTaskFactory extends TAlgorithmTaskFactory<TScalarAffineTaskSpec> {

  public TScalarAffineTaskFactory(TScalarAffineTaskSpec spec) {
    super(spec);
  }

  @Override
  public TAlgorithm getAlgorithm(TMemory memory) {
    return new TAffineAlgorithmSpec(0.0).create(memory);
  }

  @Override
  public TMemory setupMemory(int featureSize, TRandomGenerator paramRand) {
    TMemory memory = new TMemory();
    memory.initialize(featureSize);
    paramRand.fillGaussian(0.0, 1.0, memory.vector[TAffineAlgorithmFactory.kWeightsAddress]);
    memory.scalar[TAffineAlgorithmFactory.kInterceptAddress] = paramRand.nextGaussian(0.0, 1.0);
    return memory;
  }

  @Override
  protected TCMatrix getFeatureVector(TDataType type, int index, int featureSize, TRandomGenerator dataRand) {
    TCMatrix feature = new TCMatrix(featureSize);
    dataRand.fillGaussian(0.0, 1.0, feature);
    return feature;
  }
}
