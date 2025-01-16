package method;

import experiments.TExperimentInstance;
import population.*;

public abstract class TMethod {
  public abstract TIndividualBase run(int trialNo, TExperimentInstance instance);
}
