package method.re;

import java.util.Objects;

import algorithm.core.TAlgorithm;
import population.TIndividualBase;

public class TIndividual extends TIndividualBase {
  private TAlgorithm fAlgorithm;

  public TIndividual(TAlgorithm algorithm) {
    super();
    fAlgorithm = algorithm;
  }

  public TIndividual(TAlgorithm algorithm, double fitness) {
    super(fitness);
    fAlgorithm = algorithm;
  }

  public TIndividual(TIndividual other) {
    super(other);
  }

  @Override
  public TIndividual clone() {
    return new TIndividual(this);
  }

  @Override
  public TAlgorithm getAlgorithm() {
    return fAlgorithm;
  }

  @Override
  public TIndividualBase copyFrom(TIndividualBase other) {
    if (!(other instanceof TIndividual)) {
      throw new Error("Cloud not copy from different type individual.");
    }
    TIndividualBase otherIndividualBase = (TIndividual) other;
    fAlgorithm = new TAlgorithm(otherIndividualBase.getAlgorithm());
    setFitness(otherIndividualBase.getFitness());
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
    TIndividual otherIndividualBase = (TIndividual) other;
    return Objects.equals(otherIndividualBase.fAlgorithm, fAlgorithm)
        && Objects.equals(otherIndividualBase.getFitness(), getFitness());
  }

  @Override
  public String toString() {
    return "fitness:" + getFitness() + "\n" + fAlgorithm + "\n";
  }
}
