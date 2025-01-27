package method.re_vag;

import java.util.Objects;

import algorithm.core.TAlgorithm;
import algorithm.core.TAlgorithmGraph;
import memory.TMemory;
import node.TVariablesManager;
import population.TIndividualBase;

public class TIndividual extends TIndividualBase {
  private TAlgorithmGraph fAlgorithmGraph;
  private TVariablesManager fVariablesManager;

  public TIndividual(TAlgorithmGraph algorithmGraph, TMemory memory) {
    super();
    fAlgorithmGraph = algorithmGraph;
    fVariablesManager = new TVariablesManager(memory);
  }

  public TIndividual(TAlgorithmGraph algorithmGraph, TMemory memory, double fitness) {
    super(fitness);
    fAlgorithmGraph = algorithmGraph;
    fVariablesManager = new TVariablesManager(memory);
  }

  public TIndividual(TIndividual other) {
    super(other);
  }

  @Override
  public TIndividual clone() {
    return new TIndividual(this);
  }

  public TAlgorithmGraph getAlgorithmGraph() {
    return fAlgorithmGraph;
  }

  public void setAlgorithmGraph(TAlgorithmGraph graph, TMemory memory) {
    fAlgorithmGraph = graph;
    fVariablesManager = new TVariablesManager(memory);
  }

  @Override
  public TAlgorithm getAlgorithm() {
    return fAlgorithmGraph.toAlgorithm(fVariablesManager);
  }

  @Override
  public TIndividualBase copyFrom(TIndividualBase other) {
    if (!(other instanceof TIndividual)) {
      throw new Error("Cloud not copy from different type individual.");
    }
    TIndividual otherIndividual = (TIndividual) other;
    fAlgorithmGraph = otherIndividual.fAlgorithmGraph;
    fVariablesManager = otherIndividual.fVariablesManager;
    setFitness(otherIndividual.getFitness());
    return this;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof TIndividual)) {
      return false;
    }
    TIndividual otherIndividual = (TIndividual) other;
    return Objects.equals(otherIndividual.fAlgorithmGraph.toAlgorithm(otherIndividual.fVariablesManager), fAlgorithmGraph.toAlgorithm(fVariablesManager))
        && Objects.equals(otherIndividual.getFitness(), getFitness());
  }


  @Override
  public String toString() {
    return "fitness:" + getFitness() + "\n" + fAlgorithmGraph.toString(fVariablesManager) + "\n\n" + getAlgorithm().toString() + "\n";
  }
}
