package task.factory;

import algorithm.spec.TNeuralNetNoBiasNoGradientAlgorithmSpec;
import task.spec.*;
import utils.TRandomGenerator;
import algorithm.core.*;
import algorithm.factory.TNeuralNetNoBiasNoGradientAlgorithmFactory;
import jp.ac.titech.onolab.core.matrix.TCMatrix;
import memory.TMemory;

public class TScalar2LayerNNTaskFactory extends TAlgorithmTaskFactory<TScalar2LayerNNTaskSpec> {

  public TScalar2LayerNNTaskFactory(TScalar2LayerNNTaskSpec spec) {
    super(spec);
  }

  @Override
  public TAlgorithm getAlgorithm(TMemory memory) {
    return new TNeuralNetNoBiasNoGradientAlgorithmSpec(0.0).create(memory);
  }

  @Override
  public TMemory setupMemory(int featureSize, TRandomGenerator paramRand) {
    TMemory memory = new TMemory();
    memory.initialize(featureSize);
    paramRand.fillGaussian(0.0, 1.0,
        memory.matrix[TNeuralNetNoBiasNoGradientAlgorithmFactory.kFirstLayerWeightsAddress]);
    paramRand.fillGaussian(0.0, 1.0,
        memory.vector[TNeuralNetNoBiasNoGradientAlgorithmFactory.kFinalLayerWeightsAddress]);
    return memory;
  }

  @Override
  protected TCMatrix getFeatureVector(TDataType type, int index, int featureSize, TRandomGenerator dataRand) {
    TCMatrix feature = new TCMatrix(featureSize);
    dataRand.fillGaussian(0.0, 1.0, feature);
    return feature;
  }
}
