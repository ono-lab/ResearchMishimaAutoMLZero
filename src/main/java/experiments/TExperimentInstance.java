package experiments;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import method.TMethod;
import method.TMethodSpec;
import method.TMethodType;

public class TExperimentInstance {
  private TExperimentType fExperimentType;
  private String fExperimentName;

  private TMethodType fMethodType;
  private String fMethodName;

  private String fNowText;

  public TExperimentInstance(TExperimentType experimentType, String experimentName, TMethodType methodType,
      String methodName) {
    fExperimentType = experimentType;
    fExperimentName = experimentName;
    fMethodType = methodType;
    fMethodName = methodName;

    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS");
    fNowText = now.format(formatter);
  }

  public TExperimentInstance(TExperimentType experimentType, TMethodType methodType) {
    this(experimentType, "default", methodType, "default");
  }

  public TExperimentInstance(TExperimentType experimentType, String experimentName, TMethodType methodType) {
    this(experimentType, experimentName, methodType, "default");
  }

  public TExperimentInstance(TExperimentType experimentType, String experimentName) {
    this(experimentType, experimentName, null, null);
  }

  public TExperimentInstance(TExperimentType experimentType) {
    this(experimentType, "default");
  }

  private String getExperimentID() {
    return fExperimentType.getName() + "/" + fExperimentName;
  }

  private String getMethodID() {
    return fMethodType.getName() + "/" + fMethodName;
  }

  private String getExperimentMethodID() {
    return getExperimentID() + "/" + getMethodID();
  }

  public String getLogsDir() {
    return "logs/" + getExperimentMethodID() + "/" + fNowText + "/";
  }

  private String getExperimentDir() {
    return "experiments/" + getExperimentID() + "/";
  }

  public String getExperimentFilePath() {
    return getExperimentDir() + "experiment.json";
  }

  public String getMethodFilePath() {
    return getExperimentDir() + getMethodID() + ".json";
  }

  public TExperimentSpec getExperimentSpec() {
    return fExperimentType.getSpec(this);
  }

  public TMethodSpec getMethodSpec() {
    return fMethodType.getSpec(this);
  }

  public TMethod getMethod() {
    return getMethodSpec().toMethod();
  }

  public void copyConfigFiles() {
    File dir = new File(getLogsDir());
    if (!dir.exists()) {
      dir.mkdirs();
    }
    Path methodFilePath = Paths.get(getMethodFilePath());
    Path methodFileDestinationPath = Paths.get(getLogsDir() + "method.json");
    Path experimentFilePath = Paths.get(getExperimentFilePath());
    Path experimentFileDestinationPath = Paths.get(getLogsDir() + "experiment.json");
    try {
      Files.copy(methodFilePath,methodFileDestinationPath);
      Files.copy(experimentFilePath,experimentFileDestinationPath);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
