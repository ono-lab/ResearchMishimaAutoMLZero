package method.deduplicated_mgg_vag;

import java.util.ArrayList;

import algorithm.core.*;
import algorithm.spec.TAlgorithmGraphSpec;
import method.TAutoMLZero;
import method.TMethodType;
import mutator.proposed.TAlgorithmGraphMutator;
import mutator.proposed.TAlgorithmGraphMutatorSpec;
import population.TPopulation;
import utils.TRandomGenerator;

public class TDeduplicatedMGGAutoMLZeroVAG extends TAutoMLZero<TIndividual> {
  private int fPopulationSize;
  private int fNumOfChildren;
  private TAlgorithmGraphSpec fInitialAlgorithmSpec;
  private TAlgorithmGraphMutator fMutator;
  private TAlgorithmGraphMutatorSpec fMutatorSpec;
  private double fWeightOfDiversity;

  private TAlgorithmGraphDuplication fDuplication = new TAlgorithmGraphDuplication();

  @Override
  public TMethodType getType() {
    return TMethodType.DEDUPLICATED_MGG_AUTO_ML_ZERO_VAG;
  }

  public TDeduplicatedMGGAutoMLZeroVAG(TDeduplicatedMGGAutoMLZeroVAGSpec spec) {
    fPopulationSize = spec.populationSize;
    fNumOfChildren = spec.numOfChildren;
    fInitialAlgorithmSpec = spec.initialPopulationAlgorithmSpec;
    fMutatorSpec = spec.mutatorSpec;
    fWeightOfDiversity = spec.weightOfDiversity;
  }

  @Override
  protected void prepare() {
    fMutator = new TAlgorithmGraphMutator(fMutatorSpec);
  }

  private void addIndividualToPopulation(TIndividual individual) {
    TPopulation<TIndividual> population = getPopulation();
    TAlgorithmGraph algorithmGraph = individual.getAlgorithmGraph();
    population.add(individual);
    fDuplication.add(algorithmGraph);
  }

  private TIndividual randomRemoveIndividualFromPopulation() {
    TRandomGenerator rand = getEvolutionRand();
    TIndividual individual = getPopulation().randomRemove(rand);
    TAlgorithmGraph algorithmGraph = individual.getAlgorithmGraph();
    fDuplication.remove(algorithmGraph);
    return individual;
  }

  @Override
  protected void initialize() {
    TRandomGenerator rand = getInitRand();
    for (int i = 0; i < fPopulationSize; i++) {
      TAlgorithmGraph graph;
      int numOfRetries = 0;
      while (true) {
        if (numOfRetries != 0 && numOfRetries % 100000 == 0) {
          System.out.println(i + "th individual is retrying " + numOfRetries + " times.");
        }
        graph = fInitialAlgorithmSpec.create(getMemory(), rand);
        if (fDuplication.existsDiscreteDuplication(graph)) {
          numOfRetries++;
          continue;
        }
        TIndividual individual = new TIndividual(graph, getMemory());
        evaluate(individual);
        addIndividualToPopulation(individual);
        break;
      }
    }
    assert getPopulation().size() == fPopulationSize;
  }

  private double calcDiversity(TIndividual individual) {
    return 1.0 / (1.0 + fDuplication.countDiscreteDuplication(individual.getAlgorithmGraph()));
  }

  private double calcScore(TIndividual individual) {
    return (1 - fWeightOfDiversity) * individual.getFitness() + fWeightOfDiversity * calcDiversity(individual);
  }



  @Override
  protected void nextGeneration() {
    TRandomGenerator rand = getEvolutionRand();
    TIndividual individual1 = randomRemoveIndividualFromPopulation();
    TIndividual individual2 = randomRemoveIndividualFromPopulation();

    TIndividual bestFitnessIndividual = individual1.getFitness() > individual2.getFitness() ? individual1 : individual2;

    ArrayList<TIndividual> family = new ArrayList<TIndividual>();
    family.add(individual1);
    family.add(individual2);

    TAlgorithmGraphDuplication familyDuplication = new TAlgorithmGraphDuplication();
    familyDuplication.add(individual1.getAlgorithmGraph());
    familyDuplication.add(individual2.getAlgorithmGraph());

    for (int i = 0; i < fNumOfChildren; i++) {
      TIndividual parent = i % 2 == 0 ? individual1 : individual2;
      int numOfRetries = 0;
      while (true) {
        if (numOfRetries != 0 && numOfRetries % 100000 == 0) {
          System.out.println(i + "th child individual is retrying " + numOfRetries + " times.");
        }
        TAlgorithmGraph childGraph = fMutator.mutate(parent.getAlgorithmGraph(), getMemory(), rand);
        if (childGraph.getMutation().isDiscrete()
            && familyDuplication.existsDiscreteDuplication(childGraph)
            && fDuplication.existsDiscreteDuplication(childGraph)) {
          numOfRetries++;
          continue;
        }
        if (fDuplication.existsDuplication(childGraph)
          || familyDuplication.existsDuplication(childGraph)) {
          numOfRetries++;
          continue;
        }
        TIndividual child = new TIndividual(childGraph, getMemory());
        double fitness = evaluate(child);
        if (fitness >= bestFitnessIndividual.getFitness()) {
          bestFitnessIndividual = child;
        }
        family.add(child);
        familyDuplication.add(childGraph);
        break;
      }
    }
    addIndividualToPopulation(bestFitnessIndividual);

    double bestScore = 0.0;
    TIndividual bestScoreIndividual = null;
    for (TIndividual individual : family) {
      double score = calcScore(individual);
      if (individual == bestFitnessIndividual) {
        continue;
      }
      if (score >= bestScore) {
        bestScore = score;
        bestScoreIndividual = individual;
      }
    }
    assert bestScoreIndividual != null;
    addIndividualToPopulation(bestScoreIndividual);
  }
}
