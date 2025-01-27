package method.re_vag;

import algorithm.core.*;
import algorithm.spec.TAlgorithmGraphSpec;
import method.TAutoMLZero;
import method.TMethodType;
import mutator.proposed.TAlgorithmGraphMutator;
import mutator.proposed.TAlgorithmGraphMutatorSpec;
import population.TPopulation;
import utils.TRandomGenerator;

public class TREAutoMLZeroVAG extends TAutoMLZero<TIndividual> {
  private int fPopulationSize;
  private int fTournamentSize;
  private TAlgorithmGraphSpec fInitialAlgorithmSpec;
  private TAlgorithmGraphMutator fMutator;
  private TAlgorithmGraphMutatorSpec fMutatorSpec;
  private double fMutateProb;

  @Override
  public TMethodType getType() {
    return TMethodType.MGG_AUTO_ML_ZERO_VAG;
  }

  public TREAutoMLZeroVAG(TREAutoMLZeroVAGSpec spec) {
    fPopulationSize = spec.populationSize;
    fTournamentSize = spec.tournamentSize;
    fInitialAlgorithmSpec = spec.initialPopulationAlgorithmSpec;
    fMutatorSpec = spec.mutatorSpec;
    fMutateProb = spec.mutateProb;
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

  private void nextIndividual(TIndividual individual) {
    assert individual != null;
    TRandomGenerator rand = getEvolutionRand();
    TPopulation<TIndividual> population = getPopulation();
    TIndividual baseIndividual = population.getTournamentSelected(fTournamentSize, rand);
    TAlgorithmGraph baseAlgorithmGraph = baseIndividual.getAlgorithmGraph();
    if (rand.nextDouble() < fMutateProb) {
      TAlgorithmGraph algorithmGraph = fMutator.mutate(baseAlgorithmGraph, getMemory(), rand);
      individual.setAlgorithmGraph(algorithmGraph, getMemory());
    } else {
      individual.setAlgorithmGraph(baseAlgorithmGraph.copy(), getMemory());
    }
    evaluate(individual);
  }

  @Override
  protected void nextGeneration() {
    TPopulation<TIndividual> population = getPopulation();
    for (TIndividual individual : population.getAll()) {
      nextIndividual(individual);
    }
  }
}
