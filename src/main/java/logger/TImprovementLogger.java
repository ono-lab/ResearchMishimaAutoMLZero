package logger;

import experiments.TExperimentInstance;
import population.TIndividualBase;

public class TImprovementLogger<T extends TIndividualBase> extends TLogger {
  public TImprovementLogger(TImprovementLoggerSpec spec, TExperimentInstance instance) {
    super(spec, instance);
  }

  @Override
  public String getLogType() {
    return "improvement";
  };

  @Override
  protected String getExtension() {
    return ".txt";
  }

  public void write(int trialNo, int generation, int evaluationsNum, T newBestIndividual) {
    String line = "";
    line += "Trial No " + trialNo + ", ";
    line += "Generation " + generation + ", ";
    line += "Evaluation Num " + evaluationsNum + "\n";
    line += newBestIndividual.toString() + "\n";
    super.write(line);
  }
}
