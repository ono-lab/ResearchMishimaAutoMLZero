package task.spec;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import task.core.*;
import task.factory.TScalarAffineTaskFactory;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class TScalarAffineTaskSpec extends TAlgorithmTaskSpec {
  @Override
  public void addTasksTo(ArrayList<TTask> tasks) {
    TScalarAffineTaskFactory factory = new TScalarAffineTaskFactory(this);
    factory.addTasksTo(tasks);
  }
}
