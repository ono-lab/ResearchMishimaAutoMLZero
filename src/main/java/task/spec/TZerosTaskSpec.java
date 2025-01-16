package task.spec;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import task.core.TTask;
import task.factory.TZerosTaskFactory;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class TZerosTaskSpec extends TTaskSpec {
  @Override
  public void addTasksTo(ArrayList<TTask> tasks) {
    TZerosTaskFactory factory = new TZerosTaskFactory(this);
    factory.addTasksTo(tasks);
  }
}
