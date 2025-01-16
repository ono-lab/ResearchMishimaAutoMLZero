package algorithm.spec;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import algorithm.core.TAlgorithm;
import memory.TMemory;
import utils.TRandomGenerator;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public abstract class TAlgorithmSpec {
  public abstract TAlgorithm create(TMemory memory, TRandomGenerator rand);

  public TAlgorithm create(TMemory memory) {
    return create(memory, null);
  };
}
