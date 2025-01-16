package task.factory;

import java.util.ArrayList;

import jp.ac.titech.onolab.core.matrix.TCMatrix;
import task.core.*;
import task.spec.TTaskSpec;
import utils.*;

public abstract class TTaskFactory<T extends TTaskSpec> {
  private T fSpec;

  TTaskFactory(T spec) {
    fSpec = spec;
  }

  protected T getSpec() {
    return fSpec;
  }

  // The values of the seeds below.
  private static final long[] kDefaultFirstParamSeeds =
      new long[] {1001, 1012, 1010, 1000, 1006, 1008, 1007, 1003};

  private static final long[] kDefaultFirstDataSeeds =
      new long[] { 11001, 11012, 11010, 11000, 11006, 11008, 11007, 11003 };

  private static final long kEpochSeedOffset = 3274582109L;

  protected abstract TCMatrix getFeatureVector(TDataType type, int index, int featureSize, TRandomGenerator dataRand);

  protected abstract double getLabel(TDataType type, int index, TCMatrix feature, TRandomGenerator dataRand);

  protected void initialize(TRandomGenerator paramRand) {}

  private TTask createTask(int taskIndex, long paramSeed, long dataSeed) {
    TRandomGenerator paramRand = new TRandomGenerator(paramSeed);
    TRandomGenerator dataRand = new TRandomGenerator(dataSeed);
    TTaskBuffer buffer = new TTaskBuffer(fSpec.featuresSize);
    buffer.setEvalMethod(fSpec.evalMethod);
    initialize(paramRand);
    for (int numOfTrainExamples = 0; numOfTrainExamples < fSpec.numOfTrainExamples; numOfTrainExamples++) {
      TCMatrix feature = getFeatureVector(TDataType.TRAIN, numOfTrainExamples, fSpec.featuresSize, dataRand);
      double label = getLabel(TDataType.TRAIN, numOfTrainExamples, feature, dataRand);
      buffer.addTrainData(feature, label);
    }
    for (int numOfValidExamples = 0; numOfValidExamples < fSpec.numOfValidExamples; numOfValidExamples++) {
      TCMatrix feature = getFeatureVector(TDataType.VALID, numOfValidExamples, fSpec.featuresSize, dataRand);
      double label = getLabel(TDataType.VALID, numOfValidExamples, feature, dataRand);
      buffer.addValidData(feature, label);
    }
    TRandomGenerator epochRand = new TRandomGenerator(dataSeed + kEpochSeedOffset);
    assert buffer.getTrainDataSize() == fSpec.numOfTrainExamples;
    assert buffer.getValidDataSize() == fSpec.numOfValidExamples;
    return new TTask(taskIndex, fSpec.numOfTrainEpochs, buffer, epochRand);
  }

  public void addTasksTo(ArrayList<TTask> tasks) {
    int numOfTasks = fSpec.numOfTasks;
    assert numOfTasks > 0;

    long[] firstParamSeeds = fSpec.paramSeeds != null && fSpec.paramSeeds.length > 0 ? fSpec.paramSeeds
        : kDefaultFirstParamSeeds;
    long[] firstDataSeeds = fSpec.dataSeeds != null && fSpec.dataSeeds.length > 0 ? fSpec.dataSeeds
        : kDefaultFirstDataSeeds;

    long paramSeed = firstParamSeeds[0], dataSeed = firstDataSeeds[0];
    for (int i = 0; i < numOfTasks; i++) {
      paramSeed = i < firstParamSeeds.length ? firstParamSeeds[i] : paramSeed + 1;
      dataSeed = i < firstDataSeeds.length ? firstDataSeeds[i] : dataSeed + 1;
      int taskIndex = tasks.size();
      TTask task = createTask(taskIndex, paramSeed, dataSeed);
      tasks.add(task);
    }
  }


  public ArrayList<TTask> createTasks() {
    ArrayList<TTask> tasks = new ArrayList<TTask>();
    addTasksTo(tasks);
    return tasks;
  }
}
