package mutator.proposed;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
class TReconstructRootNodeMutatorSpec {
  public double[] selectDepthProbabilities;
  public int numOfRetries;

  TReconstructRootNodeMutatorSpec() {

  }
}
