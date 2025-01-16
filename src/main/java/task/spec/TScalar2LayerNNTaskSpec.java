package task.spec;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import task.core.*;
import task.factory.TScalar2LayerNNTaskFactory;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class TScalar2LayerNNTaskSpec extends TAlgorithmTaskSpec {
  @Override
  public void addTasksTo(ArrayList<TTask> tasks) {
    TScalar2LayerNNTaskFactory factory = new TScalar2LayerNNTaskFactory(this);
    factory.addTasksTo(tasks);
  }
}
