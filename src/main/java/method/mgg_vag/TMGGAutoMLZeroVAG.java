package method.mgg_vag;

import algorithm.core.*;
import algorithm.spec.TAlgorithmGraphSpec;
import method.TAutoMLZero;
import method.TMethodType;
import mutator.proposed.TAlgorithmGraphMutator;
import mutator.proposed.TAlgorithmGraphMutatorSpec;
import population.TPopulation;
import utils.TRandomGenerator;

public class TMGGAutoMLZeroVAG extends TAutoMLZero<TIndividual> {
  private int fPopulationSize;
  private int fNumOfChildren;
  private TAlgorithmGraphSpec fInitialAlgorithmSpec;
  private TAlgorithmGraphMutator fMutator;
  private TAlgorithmGraphMutatorSpec fMutatorSpec;

  @Override
  public TMethodType getType() {
    return TMethodType.MGG_AUTO_ML_ZERO_VAG;
  }

  public TMGGAutoMLZeroVAG(TMGGAutoMLZeroVAGSpec spec) {
    fPopulationSize = spec.populationSize;
    fNumOfChildren = spec.numOfChildren;
    fInitialAlgorithmSpec = spec.initialPopulationAlgorithmSpec;
    fMutatorSpec = spec.mutatorSpec;

  }

  @Override
  protected void prepare() {
    fMutator = new TAlgorithmGraphMutator(fMutatorSpec);
  }

  @Override
  protected void initialize() {
    TRandomGenerator rand = getInitRand();
    TPopulation<TIndividual> population = getPopulation();
    for (int i = 0; i < fPopulationSize; i++) {
      TAlgorithmGraph graph = fInitialAlgorithmSpec.create(getMemory(), rand);
      TIndividual individual = new TIndividual(graph, getMemory());
      evaluate(individual);
      population.add(individual);
    }
    assert population.size() == fPopulationSize;
  }

  @Override
  protected void nextGeneration() {
    TPopulation<TIndividual> population = getPopulation();
    TRandomGenerator rand = getEvolutionRand();
    TIndividual individual1 = population.randomRemove(rand);
    TIndividual individual2 = population.randomRemove(rand);
    TIndividual best1 = individual1.getFitness() > individual2.getFitness() ? individual1 : individual2;
    TIndividual best2 = individual1.getFitness() > individual2.getFitness() ? individual2 : individual1;
    for (int i = 0; i < fNumOfChildren; i++) {
      TIndividual parent = i % 2 == 0 ? individual1 : individual2;
      TAlgorithmGraph childGraph = fMutator.mutate(parent.getAlgorithmGraph(), getMemory(), rand);
      TIndividual child = new TIndividual(childGraph, getMemory());
      evaluate(child);
      if (child.getFitness() >= best1.getFitness()) {
        best2 = best1;
        best1 = child;
      } else if (child.getFitness() >= best2.getFitness()) {
        best2 = child;
      }
    }
    population.add(best1);
    population.add(best2);
  }
}
