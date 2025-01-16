package algorithm.spec;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import algorithm.core.TAlgorithm;
import algorithm.factory.TNeuralNetNoBiasNoGradientAlgorithmFactory;
import memory.TMemory;
import utils.TRandomGenerator;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class TNeuralNetNoBiasNoGradientAlgorithmSpec extends TAlgorithmSpec {
  private static final double kDefaultLearningRate = 0.01;

  public double learningRate = kDefaultLearningRate;

  public TNeuralNetNoBiasNoGradientAlgorithmSpec() {}

  public TNeuralNetNoBiasNoGradientAlgorithmSpec(double learningRate) {
    this.learningRate = learningRate;
  }

  @Override
  public TAlgorithm create(TMemory memory, TRandomGenerator rand) {
    TNeuralNetNoBiasNoGradientAlgorithmFactory factory = new TNeuralNetNoBiasNoGradientAlgorithmFactory(this);
    return factory.create(memory, rand);
  }
}
