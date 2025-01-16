package task.spec;

import task.core.*;
import task.factory.TFixedTaskFactory;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jp.ac.titech.onolab.core.matrix.TCMatrix;

/*
 * A task where the data is specified explicitly during construction. Useful for unit tests.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class TFixedTaskSpec extends TTaskSpec {
  public TCMatrix[] trainFeatures;
  public double[] trainLabels;
  public TCMatrix[] validFeatures;
  public double[] validLabels;

  @Override
  public void addTasksTo(ArrayList<TTask> tasks) {
    TFixedTaskFactory factory = new TFixedTaskFactory(this);
    factory.addTasksTo(tasks);
  }
}
