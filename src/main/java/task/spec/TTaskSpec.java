package task.spec;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import task.core.*;
import utils.THashGenerator;
import utils.TRandomGenerator;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
abstract public class TTaskSpec {
  /**
   * Size of each features vector. This also sets the size of all vectors and matrices in the
   * memory.
   */
  public int featuresSize;

  /**
   * Number of unique training examples.
   */
  public int numOfTrainExamples;

  /**
   * Number of unique valid examples.
   */
  public int numOfValidExamples;

  /*
   * Number of times the training examples will be repeated to mimic multiple epochs over a fixed
   * training set.
   */
  public int numOfTrainEpochs = 1;

  /**
   * Number of tasks with this specification.
   */
  public int numOfTasks = 1;

  /**
   * Seeds for the features. If data_seeds have n elements (n > 0), they will be used as the seeds
   * for the first n tasks, the seeds for the rest will be incrementing from the last seed in
   * data_seeds. If data_seeds is empty, the default seeds will be used. See FillTasks function in
   * task_util.cc for more details.
   */
  public long[] dataSeeds;

  /*
   * Seeds for the parameters that determine the labels function. Same rules as for data_seeds
   * apply.
   */
  public long[] paramSeeds;

  /*
   * See task_type case for allowed EvalType values.
   */
  public TEvalMethod evalMethod;

  abstract public void addTasksTo(ArrayList<TTask> tasks);

  public ArrayList<TTask> createTasks() {
    ArrayList<TTask> tasks = new ArrayList<TTask>();
    addTasksTo(tasks);
    return tasks;
  }

  public static void randomizeSeeds(TTaskSpec[] specs, final long seed) {
    long baseParamSeed = THashGenerator.getCombinedHash(seed, 85652777L);
    long baseDataSeed = THashGenerator.getCombinedHash(seed, 38272328L);
    TRandomGenerator paramSeedGen = new TRandomGenerator(baseParamSeed);
    TRandomGenerator dataSeedGen = new TRandomGenerator(baseDataSeed);
    for (TTaskSpec spec : specs) {
      if (spec.paramSeeds == null || spec.paramSeeds.length != spec.numOfTasks) {
        spec.paramSeeds = new long[spec.numOfTasks];
      }
      if (spec.dataSeeds == null || spec.dataSeeds.length != spec.numOfTasks) {
        spec.dataSeeds = new long[spec.numOfTasks];
      }
      for (int i = 0; i < spec.numOfTasks; i++) {
        spec.paramSeeds[i] = paramSeedGen.nextLong();
        spec.dataSeeds[i] = dataSeedGen.nextLong();
      }
    }
  }
}
