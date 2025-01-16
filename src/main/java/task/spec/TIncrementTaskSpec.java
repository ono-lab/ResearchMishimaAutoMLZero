package task.spec;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import task.core.TTask;
import task.factory.TIncrementTaskFactory;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class TIncrementTaskSpec extends TTaskSpec {
  public double increment = 1.0;

  @Override
  public void addTasksTo(ArrayList<TTask> tasks) {
    TIncrementTaskFactory factory = new TIncrementTaskFactory(this);
    factory.addTasksTo(tasks);
  }
}
