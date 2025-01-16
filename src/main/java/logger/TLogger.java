package logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import experiments.TExperimentInstance;
abstract public class TLogger {

  private String fDir;
  private File fFile;
  private FileWriter fWriter;

  protected TLogger(TLoggerSpec spec, TExperimentInstance instance) {
    fDir = instance.getLogsDir();
  }

  abstract String getLogType();

  abstract String getExtension();

  protected String getStartText() {
    return "";
  }

  protected String getEndText() {
    return "";
  }

  public void open(int id) {
    try {
      String dirName = fDir + "/" + id + "/";
      File dir = new File(dirName);
      if (!dir.exists()) {
        dir.mkdirs();
      }
      fFile = new File(dirName + getLogType() + getExtension());
      fWriter = new FileWriter(fFile);
      fWriter.write(getStartText());
    } catch (IOException e) {
      System.out.println(e);
      throw new RuntimeException("Error: logger could not be opened.");
    }
  }

  protected void write(String line) {
    if(fWriter == null) {
      throw new RuntimeException("Error: logger is not opened.");
    }
    try {
      fWriter.write(line);
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  public void close() {
    try {
      fWriter.write(getEndText());
      fWriter.close();
      fWriter = null;
    } catch (IOException e) {
      System.out.println(e);
    }
  };
}
