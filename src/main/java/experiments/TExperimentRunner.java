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

  public void execute(Integer startTrialNo, Integer endTrialNo) {
    fInstance.copyConfigFiles();
    ArrayList<Double> fitnesses = new ArrayList<Double>();
    for (int numOfExperiments = startTrialNo == null ? 1 : startTrialNo; numOfExperiments <= (endTrialNo == null ? fSpec.maxNumOfExperiments : endTrialNo); numOfExperiments++) {
      double fitness = executeOneExperiment(numOfExperiments);
      fitnesses.add(fitness);
    }
    System.out.println("Fitnesses: " + fitnesses);
  }

  public static void main(String[] args) {
    TExperimentType experimentType = null;
    String experimentName = "default";
    TMethodType methodType = null;
    String methodName = "default";
    Integer startTrialNo = null;
    Integer endTrialNo = null;

    if (args.length < 2) {
      System.err.println("Usage: java Program <experimentType> <datasetName> <methodType> <methodName>");
      System.exit(1);
    } else if (args.length == 2) {
      experimentType = TExperimentType.valueOf(args[0]);
      methodType = TMethodType.valueOf(args[1]);
    } else if (args.length == 3) {
      experimentType = TExperimentType.valueOf(args[0]);
      experimentName = args[1];
      methodType = TMethodType.valueOf(args[2]);
    } else if (args.length == 4) {
      experimentType = TExperimentType.valueOf(args[0]);
      experimentName = args[1];
      methodType = TMethodType.valueOf(args[2]);
      methodName = args[3];
    } else if (args.length == 6) {
      experimentType = TExperimentType.valueOf(args[0]);
      experimentName = args[1];
      methodType = TMethodType.valueOf(args[2]);
      methodName = args[3];
      startTrialNo = Integer.parseInt(args[4]);
      endTrialNo = Integer.parseInt(args[5]);
    } else {
      System.err.println("Usage: java Program <experimentType> <datasetName> <methodType> <methodName>");
      System.exit(1);
    }

    try {
      // インスタンスの生成
      TExperimentInstance instance = new TExperimentInstance(experimentType, experimentName, methodType, methodName);

      // 実験の実行
      TExperimentRunner runner = new TExperimentRunner(instance);
      runner.execute(startTrialNo, endTrialNo);

      System.out.println("Experiment done.");
      System.out.println("Experiment Logs are here: " + instance.getLogsDir());

    } catch (IllegalArgumentException e) {
      System.err.println("Error: Invalid argument provided.");
      System.err.println("Ensure that <experimentType> and <methodType> match the defined enum values.");
      System.exit(1);
    }
  }
}
