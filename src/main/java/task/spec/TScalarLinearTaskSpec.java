package task.spec;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import task.core.*;
import task.factory.TScalarLinearTaskFactory;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class TScalarLinearTaskSpec extends TAlgorithmTaskSpec {
  @Override
  public void addTasksTo(ArrayList<TTask> tasks) {
    TScalarLinearTaskFactory factory = new TScalarLinearTaskFactory(this);
    factory.addTasksTo(tasks);
  }
}
