package logger;

import java.util.ArrayList;

import experiments.TExperimentInstance;
import population.TIndividualBase;
import population.TPopulation;

public class TEliteIndividualsLogger<T extends TIndividualBase> extends TLogger {
  private int fEliteSize;

  public TEliteIndividualsLogger(TEliteIndividualsLoggerSpec spec, TExperimentInstance instance) {
    super(spec, instance);
    fEliteSize = spec.eliteSize;
  }

  @Override
  protected String getLogType() {
    return "elites";
  }

  @Override
  protected String getExtension() {
    return ".txt";
  }

  public void write(int trialNo, int generation, TPopulation<T> population) {
    ArrayList<T> elites = population.getElites(fEliteSize);
    for (int i = 0; i < elites.size(); i++) {
      int rank = i + 1;
      String line = "";
      line += "Trial No " + trialNo + ", ";
      line += "Generation " + generation + ", ";
      line += "Rank " + rank + "\n";
      line += elites.get(i).toString() + "\n";
      super.write(line);
    }
  }
}
