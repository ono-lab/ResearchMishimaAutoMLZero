package algorithm.spec;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import algorithm.core.TAlgorithm;
import algorithm.factory.TLinearAlgorithmFactory;
import memory.TMemory;
import utils.TRandomGenerator;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class TLinearAlgorithmSpec extends TAlgorithmSpec {
  private static final double kDefaultLearningRate = 0.01;

  public double learningRate = kDefaultLearningRate;

  public TLinearAlgorithmSpec() {}

  public TLinearAlgorithmSpec(double learningRate) {
    this.learningRate = learningRate;
  }

  public TLinearAlgorithmSpec(double learningRate, double noiseStdev) {
    this.learningRate = learningRate;
  }

  @Override
  public TAlgorithm create(TMemory memory, TRandomGenerator rand) {
    TLinearAlgorithmFactory factory = new TLinearAlgorithmFactory(this);
    return factory.create(memory, rand);
  }
}
