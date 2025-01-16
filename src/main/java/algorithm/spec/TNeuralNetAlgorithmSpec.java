package algorithm.spec;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import algorithm.core.TAlgorithm;
import algorithm.factory.TNeuralNetAlgorithmFactory;
import memory.TMemory;
import utils.TRandomGenerator;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class TNeuralNetAlgorithmSpec extends TAlgorithmSpec {

  private static final double kDefaultLearningRate = 0.01;
  private static final float kDefaultInitScale = 0.1f;

  public double learningRate = kDefaultLearningRate;
  public float firstInitScale = kDefaultInitScale;
  public float finalInitScale = kDefaultInitScale;

  public TNeuralNetAlgorithmSpec() {}

  public TNeuralNetAlgorithmSpec(double learningRate, float firstInitScale, float finalInitScale) {
    this.learningRate = learningRate;
    this.firstInitScale = firstInitScale;
    this.finalInitScale = finalInitScale;
  }

  @Override
  public TAlgorithm create(TMemory memory, TRandomGenerator rand) {
    TNeuralNetAlgorithmFactory factory = new TNeuralNetAlgorithmFactory(this);
    return factory.create(memory, rand);
  }
}
