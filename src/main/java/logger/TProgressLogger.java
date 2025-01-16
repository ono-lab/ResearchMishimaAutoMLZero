package logger;

import experiments.TExperimentInstance;

public class TProgressLogger extends TLogger {
  public TProgressLogger(TProgressLoggerSpec spec, TExperimentInstance instance) {
    super(spec, instance);
  }

  @Override
  protected String getLogType() {
    return "progress";
  }

  @Override
  protected String getExtension() {
    return ".csv";
  }

  @Override
  protected String getStartText() {
    return "trial_no,generation,evaluations_num,elapsed_secs,mean,stdev,best_fit,\n";
  }

  public void write(int trialNo, int generation, long evaluationsNum, long elapsedSecs, String mean, String stdev, String bestFit) {
    String line = "";
    line += trialNo + ",";
    line += generation + ",";
    line += evaluationsNum + ",";
    line += elapsedSecs + ",";
    line += mean + ",";
    line += stdev + ",";
    line += bestFit + ",\n";
    super.write(line);
  }
}
