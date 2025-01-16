package task.factory;

import task.spec.*;
import utils.TRandomGenerator;
import algorithm.core.TAlgorithm;
import algorithm.factory.TLinearAlgorithmFactory;
import algorithm.spec.TLinearAlgorithmSpec;
import jp.ac.titech.onolab.core.matrix.TCMatrix;
import memory.TMemory;

public class TScalarLinearTaskFactory extends TAlgorithmTaskFactory<TScalarLinearTaskSpec> {
  public TScalarLinearTaskFactory(TScalarLinearTaskSpec spec) {
    super(spec);
  }


  @Override
  public TAlgorithm getAlgorithm(TMemory memory) {
    return new TLinearAlgorithmSpec(0.0).create(memory);
  }

  @Override
  public TMemory setupMemory(int featureSize, TRandomGenerator paramRand) {
    TMemory memory = new TMemory();
    memory.initialize(featureSize);
    paramRand.fillGaussian(0.0, 1.0, memory.vector[TLinearAlgorithmFactory.kWeightsAddress]);
    return memory;
  }

  @Override
  protected TCMatrix getFeatureVector(TDataType type, int index, int featureSize, TRandomGenerator dataRand) {
    TCMatrix feature = new TCMatrix(featureSize);
    dataRand.fillGaussian(0.0, 1.0, feature);
    return feature;
  }
}
