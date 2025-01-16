package experiments;

import evaluator.TEvaluator;
import memory.TMemory;
import task.spec.TTaskSpec;
import utils.TRandomGenerator;

public class TExperimentEvaluatingEnvironment {
  static private long kEvaluatorSeedOffset = 1024l;

  private TMemory fMemory;
  private TRandomGenerator fEvaluatorRand;
  private TEvaluator fEvaluator;

  public TExperimentEvaluatingEnvironment(TExperimentSpec spec, int trialNo) {
    if (spec.changeEvalTasks) {
      TTaskSpec.randomizeSeeds(spec.evalEvaluatorSpec.taskSpecs, trialNo);
    }

    fMemory = new TMemory();

    long evaluatorSeed = spec.changeSearchEvaluator ? trialNo + spec.evalSeed : spec.evalSeed;
    fEvaluatorRand = new TRandomGenerator(evaluatorSeed + kEvaluatorSeedOffset);

    fEvaluator = new TEvaluator(spec.evalEvaluatorSpec, fMemory, fEvaluatorRand);
  }

  public TEvaluator getEvaluator() {
    return fEvaluator;
  }

  public TMemory getMemory() {
    return fMemory;
  }

  public TRandomGenerator getEvolutionRand() {
    return fEvaluatorRand;
  }
}
