package algorithm.spec;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import algorithm.core.TAlgorithm;
import algorithm.factory.TRandomAlgorithmFactory;
import instruction.TOp;
import memory.TMemory;
import utils.TRandomGenerator;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class TRandomAlgorithmSpec extends TAlgorithmSpec {
  public int setupSize;
  public int predictSize;
  public int learnSize;
  public TOp[] setupOps;
  public TOp[] predictOps;
  public TOp[] learnOps;

  @Override
  public TAlgorithm create(TMemory memory, TRandomGenerator rand) {
    TRandomAlgorithmFactory factory = new TRandomAlgorithmFactory(this);
    return factory.create(memory, rand);
  }

  public TRandomAlgorithmSpec() {
  }

  public TRandomAlgorithmSpec(int setupSize, int predictSize, int learnSize,
      TOp[] setupOps, TOp[] predictOps, TOp[] learnOps) {
    this.setupSize = setupSize;
    this.predictSize = predictSize;
    this.learnSize = learnSize;
    this.setupOps = setupOps;
    this.predictOps = predictOps;
    this.learnOps = learnOps;
  }
}
