package mutator.proposed;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
class TAlterParameterInitialValueMutatorSpec {
  public double signFlipProb = 0.1;

  TAlterParameterInitialValueMutatorSpec() {

  }
}
