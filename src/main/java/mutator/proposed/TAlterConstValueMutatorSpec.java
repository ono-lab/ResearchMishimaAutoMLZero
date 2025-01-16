package mutator.proposed;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
class TAlterConstValueMutatorSpec {
  public double signFlipProb = 0.1;

  TAlterConstValueMutatorSpec() {

  }
}
