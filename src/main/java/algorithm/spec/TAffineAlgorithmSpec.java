package algorithm.spec;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import algorithm.core.TAlgorithm;
import algorithm.factory.TAffineAlgorithmFactory;
import memory.TMemory;
import utils.TRandomGenerator;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class TAffineAlgorithmSpec extends TAlgorithmSpec {
  private static final double kDefaultLearningRate = 0.01;
  public double learningRate = kDefaultLearningRate;

  public TAffineAlgorithmSpec() {
  }

  public TAffineAlgorithmSpec(double learningRate) {
    this.learningRate = learningRate;
  }

  @Override
  public TAlgorithm create(TMemory memory, TRandomGenerator rand) {
    TAffineAlgorithmFactory factory = new TAffineAlgorithmFactory(this);
    return factory.create(memory, rand);
  }
}
