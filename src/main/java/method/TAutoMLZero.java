package method;

import evaluator.TEvaluator;
import experiments.TExperimentInstance;
import experiments.TExperimentSearchEnvironment;
import memory.TMemory;
import population.*;
import utils.TRandomGenerator;

public abstract class TAutoMLZero<T extends TIndividualBase> extends TMethod {
  private TExperimentSearchEnvironment<T> fEnv;

  private TPopulation<T> fPopulation;

  abstract public TMethodType getType();

  public TPopulation<T> getPopulation() {
    return fPopulation;
  }

  protected TEvaluator getEvaluator() {
    return fEnv.getEvaluator();
  }

  protected TMemory getMemory() {
    return fEnv.getMemory();
  }

  protected TRandomGenerator getInitRand() {
    return fEnv.getInitRand();
  }

  protected TRandomGenerator getEvolutionRand() {
    return fEnv.getEvolutionRand();
  }

  public long getNumOfEvaluatedIndividuals() {
    return fEnv.getNumOfEvaluatedIndividuals();
  }

  protected double evaluate(T individual) {
    try {
      fEnv.incrementNumOfEvaluatedIndividuals();
      fEnv.updateEpochSecs();
      double fitness = getEvaluator().execute(individual.getAlgorithm());
      individual.setFitness(fitness);
      return fitness;
    } catch (Exception e) {
      System.err.println("Error evaluating individual:");
      System.err.println(individual);
      throw e;
    }
  }

  protected abstract void initialize();

  protected abstract void prepare();

  protected abstract void nextGeneration();

  @Override
  public T run(int trialNo, TExperimentInstance instance) {
    fEnv = new TExperimentSearchEnvironment<T>(instance, trialNo);
    prepare();
    fPopulation = new TPopulation<T>();
    fEnv.start();
    initialize();
    assert fPopulation != null;
    T best = fPopulation.getBest();
    T currentBest = fPopulation.getBest();
    fEnv.putProgress(fPopulation, best);
    fEnv.writeEliteIndividuals(fPopulation);
    while (fEnv.shouldContinue(currentBest)) {
      fEnv.incrementGeneration();
      nextGeneration();
      currentBest = fPopulation.getBest();
      fEnv.maybeWriteImprovement(currentBest, best);
      fEnv.maybePutProgress(fPopulation, currentBest);
      fEnv.maybeWriteEliteIndividuals(fPopulation);
      best = currentBest;
    }
    fEnv.stop();
    return fPopulation.getBest();
  }
}
