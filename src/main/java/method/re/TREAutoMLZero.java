package method.re;

import algorithm.core.*;
import algorithm.spec.TAlgorithmSpec;
import method.TAutoMLZero;
import method.TMethodType;
import mutator.esteban.TAlgorithmMutator;
import mutator.esteban.TAlgorithmMutatorSpec;
import population.TPopulation;
import utils.TRandomGenerator;

public class TREAutoMLZero extends TAutoMLZero<TIndividual> {
  private int fPopulationSize;
  private int fTournamentSize;
  private TAlgorithmSpec fInitialAlgorithmSpec;
  private TAlgorithmMutatorSpec fMutatorSpec;
  private TAlgorithmMutator fMutator;
  private int fNumOfScalarAddresses;
  private int fNumOfVectorAddresses;
  private int fNumOfMatrixAddresses;

  @Override
  public TMethodType getType() {
    return TMethodType.RE_AUTO_ML_ZERO;
  }

  public TREAutoMLZero(TREAutoMLZeroSpec spec) {
    fPopulationSize = spec.populationSize;
    fTournamentSize = spec.tournamentSize;
    fInitialAlgorithmSpec = spec.initialPopulationAlgorithmSpec;
    fMutatorSpec = spec.mutatorSpec;
    fNumOfScalarAddresses = spec.numOfScalarAddresses;
    fNumOfVectorAddresses = spec.numOfVectorAddresses;
    fNumOfMatrixAddresses = spec.numOfMatrixAddresses;
  }

  @Override
  protected void prepare() {
    getMemory().setUsedNumOfAddresses(fNumOfScalarAddresses, fNumOfVectorAddresses, fNumOfMatrixAddresses);
    fMutator = new TAlgorithmMutator(fMutatorSpec, getMemory(), getEvolutionRand());
  }

  @Override
  protected void initialize() {
    TRandomGenerator rand = getInitRand();
    TPopulation<TIndividual> population = getPopulation();
    for (int i = 0; i < fPopulationSize; i++) {
      TAlgorithm algorithm = fInitialAlgorithmSpec.create(getMemory(), rand);
      TIndividual individual = new TIndividual(algorithm);
      evaluate(individual);
      population.add(individual);
    }
    assert population.size() == fPopulationSize;
  }

  private void nextIndividual(TIndividual individual) {
    assert individual != null;
    TRandomGenerator rand = getEvolutionRand();
    TPopulation<TIndividual> population = getPopulation();
    TAlgorithm algorithm = individual.getAlgorithm();
    TIndividual baseIndividual = population.getTournamentSelected(fTournamentSize, rand);
    TAlgorithm baseAlgorithm = baseIndividual.getAlgorithm();
    algorithm.copyFrom(baseAlgorithm);
    fMutator.mutate(algorithm);
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
