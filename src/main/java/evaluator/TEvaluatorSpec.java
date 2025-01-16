package evaluator;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import task.spec.TTaskSpec;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class TEvaluatorSpec {
  public TTaskSpec[] taskSpecs;
  public TFitnessCombinationMode fitnessCombinationMode;
  public TTrainBudgetSpec trainBudgetSpec = null;
  public TFECCacheSpec FECSpec = null;
  public double maxAbsError = Double.MAX_VALUE;
}
