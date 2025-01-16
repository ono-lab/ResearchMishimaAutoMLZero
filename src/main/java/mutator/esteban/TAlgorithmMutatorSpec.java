package mutator.esteban;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import instruction.TOp;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class TAlgorithmMutatorSpec {
  public TAlgorithmMutation[] actions;
  public double mutateProb;

  public int minSetupSize;
  public int maxSetupSize;

  public int minPredictSize;
  public int maxPredictSize;

  public int minLearnSize;
  public int maxLearnSize;

  public TOp[] setupOps;
  public TOp[] predictOps;
  public TOp[] learnOps;
}
