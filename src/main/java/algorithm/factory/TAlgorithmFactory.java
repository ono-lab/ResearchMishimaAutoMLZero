package algorithm.factory;

import algorithm.core.TAlgorithm;
import algorithm.spec.TAlgorithmSpec;
import memory.TMemory;
import utils.TRandomGenerator;

public abstract class TAlgorithmFactory<T extends TAlgorithmSpec> {
  private T fSpec;

  protected TAlgorithmFactory(T spec) {
    fSpec = spec;
  }

  protected T getSpec() {
    return fSpec;
  }

  public abstract TAlgorithm create(final TMemory memory, TRandomGenerator rand);

  public TAlgorithm create(final TMemory memory) {
    return create(memory);
  };
}
