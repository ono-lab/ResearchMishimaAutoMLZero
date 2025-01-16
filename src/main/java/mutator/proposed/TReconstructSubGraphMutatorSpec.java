package mutator.proposed;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
class TReconstructSubGraphMutatorSpec {
  public int numOfRetries;
  public double[] selectSizeProbabilities;

  TReconstructSubGraphMutatorSpec() {

  }
}
