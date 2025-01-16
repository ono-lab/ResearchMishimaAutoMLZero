package algorithm.spec;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import algorithm.core.TAlgorithm;
import algorithm.factory.TFilledAlgorithmFactory;
import instruction.TInstruction;
import memory.TMemory;
import utils.TRandomGenerator;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class TFilledAlgorithmSpec extends TAlgorithmSpec {
  public TInstruction instruction;
  public int setupSize;
  public int predictSize;
  public int learnSize;

  @Override
  public TAlgorithm create(TMemory memory, TRandomGenerator rand) {
    TFilledAlgorithmFactory factory = new TFilledAlgorithmFactory(this);
    return factory.create(memory, rand);
  }
}
