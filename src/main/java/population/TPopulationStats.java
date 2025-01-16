package population;

import java.util.ArrayList;

public class TPopulationStats<T extends TIndividualBase> {
  private TPopulation<T> fPopulation;
  private Double fMean = null;
  private Double fStdev = null;

  public TPopulationStats(TPopulation<T> population) {
    fPopulation = population;
  }

  private void calculate(){
    double total = 0.0;
    double totalSquares = 0.0;
    int size = fPopulation.size();
    ArrayList<T> individuals = fPopulation.getAll();
    T best = individuals.get(0);
    for (T individual : individuals) {
      double fitness = individual.getFitness();
      if (fitness > best.getFitness()) {
        best = individual;
      }
      total += fitness;
      totalSquares += fitness * fitness;
    }
    double mean = total / (double) size;
    double var = totalSquares / (double) size - mean * mean;
    if (var < 0.0)
      var = 0.0;
    double stdev = Math.sqrt(var);
    fMean = mean;
    fStdev = stdev;
  }

  public double getMean() {
    if (fMean == null) {
      calculate();
    }
    assert fMean != null;
    return fMean;
  }

  public double getStdev() {
    if (fStdev == null) {
      calculate();
    }
    assert fStdev != null;
    return fStdev;
  }
}
