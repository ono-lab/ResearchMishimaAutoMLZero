package algorithm.spec;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import algorithm.core.TAlgorithm;
import algorithm.factory.TNoOpAlgorithmFactory;
import memory.TMemory;
import utils.TRandomGenerator;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class TNoOpAlgorithmSpec  extends TAlgorithmSpec {
  public int setupSize;
  public int predictSize;
  public int learnSize;

  @Override
  public TAlgorithm create(TMemory memory, TRandomGenerator rand) {
    TNoOpAlgorithmFactory factory = new TNoOpAlgorithmFactory(this);
    return factory.create(memory, rand);
  }

  public TNoOpAlgorithmSpec() {
  }

  public TNoOpAlgorithmSpec(int setupSize, int predictSize, int learnSize) {
    this.setupSize = setupSize;
    this.predictSize = predictSize;
    this.learnSize = learnSize;
  }
}
