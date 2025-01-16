package task.spec;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import task.core.TTask;
import task.factory.TOnesTaskFactory;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class TOnesTaskSpec extends TTaskSpec {
  @Override
  public void addTasksTo(ArrayList<TTask> tasks) {
    TOnesTaskFactory factory = new TOnesTaskFactory(this);
    factory.addTasksTo(tasks);
  }
}
