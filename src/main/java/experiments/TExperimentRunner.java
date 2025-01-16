package experiments;

import java.util.ArrayList;

import method.*;
import population.TIndividualBase;

public class TExperimentRunner {
  private TExperimentInstance fInstance;
  private TMethod fMethod;
  private TExperimentSpec fSpec;

  public TExperimentRunner(TExperimentInstance instance) {
    fInstance = instance;
    fMethod = instance.getMethod();
    fSpec = instance.getExperimentSpec();
  }


  private TIndividualBase search(int trialNo) {
    return fMethod.run(trialNo, fInstance);
  }

  private double evaluate(int trialNo, TIndividualBase individual) {
    final TExperimentEvaluatingEnvironment env = new TExperimentEvaluatingEnvironment(fSpec, trialNo);
    return env.getEvaluator().execute(individual.getAlgorithm());
  }

  private double executeOneExperiment(int trialNo) {
    System.out.println("Experiment " + trialNo);
    System.out.println("Running evolution experiment (on the T_search tasks)...");
    TIndividualBase individual = search(trialNo);
    System.out.println("Evaluating algorithm (on the T_eval tasks)...");
    double fitness = evaluate(trialNo, individual);
    System.out.println("Found algorithm:");
    System.out.println(individual);
    System.out.println("Fitness: " + fitness);
    System.out.println("Experiment done.");
    return fitness;
  }

  public void execute() {
    fInstance.copyConfigFiles();
    ArrayList<Double> fitnesses = new ArrayList<Double>();
    for (int numOfExperiments = 1; numOfExperiments <= fSpec.maxNumOfExperiments; numOfExperiments++) {
      double fitness = executeOneExperiment(numOfExperiments);
      fitnesses.add(fitness);
    }
    System.out.println("Fitnesses: " + fitnesses);
  }

  public static void main(String[] args) {
    TExperimentInstance instance = new TExperimentInstance(
        TExperimentType.LINEAR_REGRESSION,
      "noisy_0.2",
        TMethodType.DEDUPLICATED_MGG_AUTO_ML_ZERO_VAG
    );
    TExperimentRunner runner = new TExperimentRunner(instance);
    runner.execute();
  }
}
