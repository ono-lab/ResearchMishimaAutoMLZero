package experiments;

import java.util.ArrayList;

import algorithm.core.TAlgorithm;
import algorithm.spec.TAlgorithmSpec;
import algorithm.spec.TLinearAlgorithmSpec;
import algorithm.spec.TNeuralNetNoBiasNoGradientAlgorithmSpec;

public class THandmadeExperimentRunner {
  private TExperimentSpec fSpec;

  public THandmadeExperimentRunner(TExperimentSpec spec) {
    fSpec = spec;
  }


  private double calculateFitness(int trialNo, TAlgorithmSpec algorithmSpec) {
    final TExperimentEvaluatingEnvironment env = new TExperimentEvaluatingEnvironment(fSpec, trialNo);
    TAlgorithm algorithm = algorithmSpec.create(env.getMemory(), env.getEvolutionRand());
    System.out.println(algorithm);
    return env.getEvaluator().execute(algorithm);
  }

  private double executeOneExperiment(int trialNo, TAlgorithmSpec algorithmSpec) {
    System.out.println("Experiment " + trialNo);
    double fitness = calculateFitness(trialNo, algorithmSpec);
    System.out.println("Fitness: " + fitness);
    System.out.println("Experiment done.");
    return fitness;
  }

  public ArrayList<Double> execute(TAlgorithmSpec algorithmSpec) {
    ArrayList<Double> fitnesses = new ArrayList<Double>();
    for (int numOfExperiments = 1; numOfExperiments <= fSpec.maxNumOfExperiments; numOfExperiments++) {
      double fitness = executeOneExperiment(numOfExperiments, algorithmSpec);
      fitnesses.add(fitness);
    }
    System.out.println("Fitnesses: " + fitnesses);
    return fitnesses;
  }

  public static void main(String[] args) {
    TExperimentInstance instance = new TExperimentInstance(TExperimentType.TWO_LAYERS_NN_CLASSIFICATION);
    TAlgorithmSpec algorithmSpec = new TNeuralNetNoBiasNoGradientAlgorithmSpec(0.02);
    THandmadeExperimentRunner runner = new THandmadeExperimentRunner(instance.getExperimentSpec());
    runner.execute(algorithmSpec);
  }
}
