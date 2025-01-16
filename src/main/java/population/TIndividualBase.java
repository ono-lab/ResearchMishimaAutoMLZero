package population;

import algorithm.core.TAlgorithm;

abstract public class TIndividualBase {
  private double fFitness = Double.NaN;

  protected TIndividualBase() {
  }

  protected TIndividualBase(double fitness) {
    fFitness = fitness;
  }

  public double getFitness() {
    return fFitness;
  }

  public void setFitness(double fitness) {
    fFitness = fitness;
  }

  public TIndividualBase(TIndividualBase other) {
    copyFrom(other);
  }

  abstract public TAlgorithm getAlgorithm();

  abstract public TIndividualBase copyFrom(TIndividualBase other);

  abstract public TIndividualBase clone();

  @Override
  abstract public boolean equals(Object other);

  @Override
  abstract public String toString();
}
