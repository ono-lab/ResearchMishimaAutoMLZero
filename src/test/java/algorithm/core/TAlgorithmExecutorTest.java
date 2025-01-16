package algorithm.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

import instruction.TInstruction;
import instruction.TOp;
import jp.ac.titech.onolab.core.matrix.TCMatrix;
import memory.TMemory;
import task.core.*;
import task.spec.*;
import test_utils.*;
import utils.*;

public class TAlgorithmExecutorTest {
  static private final double kTestTolerance = 0.0001;
  static private final double kMaxAbsError = 100.0;
  static private final double kLargeMaxAbsError = 1000000000.0;

  static private final TMemory kMemory = new TMemory(TTaskFactoryTestUtil.kFeaturesSize);

  @Test
  void setupComponentIsExecutedCorrectly() {
    TOnesTaskSpec spec = TTaskFactoryTestUtil.getOnesTaskSpec(TEvalMethod.RMS_ERROR);
    TTask task = spec.createTasks().get(0);
    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    algorithm.getSetup().set(0, new TInstruction(TOp.SCALAR_CONST_SET_OP, 2, 1.0));
    algorithm.getSetup().set(2, new TInstruction(TOp.SCALAR_SUM_OP, 2, kMemory.scalarPredictionLabelAddress, kMemory.scalarPredictionLabelAddress));
    algorithm.execute(task, TTaskFactoryTestUtil.kNumOfTrainExamples, TTaskFactoryTestUtil.kNumOfValidExamples,
        kLargeMaxAbsError, kMemory,new TRandomGenerator());
    assertEquals(1.0, kMemory.getPredictionLabel());
  }

  @Test
  void predictComponentIsExecutedCorrectly() {
    TOnesTaskSpec spec = TTaskFactoryTestUtil.getOnesTaskSpec(TEvalMethod.RMS_ERROR);
    TTask task = spec.createTasks().get(0);
    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    algorithm.getPredict().set(0,
        new TInstruction(TOp.SCALAR_CONST_SET_OP, 2, 1.0));
    algorithm.getPredict().set(2, new TInstruction(TOp.SCALAR_SUM_OP, 2,
        kMemory.scalarPredictionLabelAddress, kMemory.scalarPredictionLabelAddress));
    algorithm.execute(task, TTaskFactoryTestUtil.kNumOfTrainExamples,
            TTaskFactoryTestUtil.kNumOfValidExamples, kLargeMaxAbsError, kMemory, new TRandomGenerator());
    assertEquals(
        TTaskFactoryTestUtil.kNumOfTrainExamples + TTaskFactoryTestUtil.kNumOfValidExamples,
        kMemory.getPredictionLabel());
  }

  @Test
  void learnComponentIsExecutedCorrectly() {
    TOnesTaskSpec spec = TTaskFactoryTestUtil.getOnesTaskSpec(TEvalMethod.RMS_ERROR);
    TTask task = spec.createTasks().get(0);
    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    algorithm.getLearn().set(0,
        new TInstruction(TOp.SCALAR_CONST_SET_OP, 2, 1.0));
    algorithm.getLearn().set(2, new TInstruction(TOp.SCALAR_SUM_OP, 2,
        kMemory.scalarPredictionLabelAddress, kMemory.scalarPredictionLabelAddress));
    algorithm.execute(task, TTaskFactoryTestUtil.kNumOfTrainExamples,
            TTaskFactoryTestUtil.kNumOfValidExamples, kLargeMaxAbsError, kMemory, new TRandomGenerator());
    assertEquals(TTaskFactoryTestUtil.kNumOfTrainExamples, kMemory.getPredictionLabel());
  }

  @Test
  void computesLossCorrectly() {
    TOnesTaskSpec spec = TTaskFactoryTestUtil.getOnesTaskSpec(TEvalMethod.RMS_ERROR);
    TTask task = spec.createTasks().get(0);
    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    algorithm.getPredict().set(0, new TInstruction(TOp.SCALAR_CONST_SET_OP,
        kMemory.scalarPredictionLabelAddress, 0.9));
    double fitness = algorithm.execute(task, TTaskFactoryTestUtil.kNumOfTrainExamples,
            TTaskFactoryTestUtil.kNumOfValidExamples, kLargeMaxAbsError, kMemory, new TRandomGenerator());
    assertEquals(1.0 - TMathUtility.squash(0.1), fitness);
  }

  @Test
  void probAccuracyComputesLossCorrectly() {
    TOnesTaskSpec spec = TTaskFactoryTestUtil.getOnesTaskSpec(TEvalMethod.ACCURACY);
    TTask task = spec.createTasks().get(0);

    // Create a Algorithm in which the accuracy is always 0.0.
    TAlgorithm algorithm1 = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    algorithm1.getPredict().set(0, new TInstruction(TOp.SCALAR_CONST_SET_OP,
        kMemory.scalarPredictionLabelAddress, -3.0));
    double fitness1 = algorithm1.execute(task, TTaskFactoryTestUtil.kNumOfTrainExamples,
            TTaskFactoryTestUtil.kNumOfValidExamples, kLargeMaxAbsError, kMemory, new TRandomGenerator());
    assertEquals(0.0, fitness1);

    // Create a Algorithm in which the accuracy is always 1.0.
    TAlgorithm algorithm2 = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    algorithm2.getPredict().set(0, new TInstruction(TOp.SCALAR_CONST_SET_OP,
        kMemory.scalarPredictionLabelAddress, 3.0));
    double fitness2 = algorithm2.execute(task, TTaskFactoryTestUtil.kNumOfTrainExamples,
            TTaskFactoryTestUtil.kNumOfValidExamples, kLargeMaxAbsError, kMemory, new TRandomGenerator());
    assertEquals(1.0, fitness2);

    // Create a Algorithm, whose logit is infinity.
    TAlgorithm algorithm3 = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    algorithm3.getPredict().set(0, new TInstruction(TOp.SCALAR_CONST_SET_OP,
        kMemory.scalarPredictionLabelAddress, Double.POSITIVE_INFINITY));
    double fitness3 = algorithm3.execute(task, TTaskFactoryTestUtil.kNumOfTrainExamples,
            TTaskFactoryTestUtil.kNumOfValidExamples, kLargeMaxAbsError, kMemory, new TRandomGenerator());
    assertEquals(TAlgorithmExecutor.kMaxFitness, fitness3);

    // Create a Algorithm, whose logit is negative infinity.
    TAlgorithm algorithm4 = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    algorithm4.getPredict().set(0, new TInstruction(TOp.SCALAR_CONST_SET_OP,
        kMemory.scalarPredictionLabelAddress, Double.NEGATIVE_INFINITY));
    double fitness4 = algorithm4.execute(task, TTaskFactoryTestUtil.kNumOfTrainExamples,
            TTaskFactoryTestUtil.kNumOfValidExamples, kLargeMaxAbsError, kMemory, new TRandomGenerator());
    assertEquals(TAlgorithmExecutor.kMinFitness, fitness4);
  }

  @Test
  void executeReportsErrorsCorrectly() {
    TIncrementTaskSpec spec = TTaskFactoryTestUtil.getIncrementTaskSpec(TEvalMethod.RMS_ERROR);
    TTask task = spec.createTasks().get(0);
    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    TAlgorithmExecutor executor =
        new TAlgorithmExecutor(algorithm, task, TTaskFactoryTestUtil.kNumOfTrainExamples,
            TTaskFactoryTestUtil.kNumOfValidExamples, kLargeMaxAbsError, kMemory, new TRandomGenerator());
    ArrayList<Double> trainErrors = new ArrayList<Double>();
    ArrayList<Double> validErrors = new ArrayList<Double>();
    executor.execute(trainErrors, validErrors);
    assertEquals(TTaskFactoryTestUtil.kNumOfTrainExamples, trainErrors.size());
    assertEquals(TTaskFactoryTestUtil.kNumOfValidExamples, validErrors.size());
    for (int i = 0; i < trainErrors.size(); i++) {
      assertNotNull(TArrayUtility.find(trainErrors, (double) i));
    }
    for (int i = 0; i < validErrors.size(); i++) {
      assertNotNull(TArrayUtility.find(validErrors, (double) i));
    }
  }

  @Test
  void executeIteratesThroughFeatures() {
    TIncrementTaskSpec spec = TTaskFactoryTestUtil.getIncrementTaskSpec(TEvalMethod.RMS_ERROR);
    TTask task = spec.createTasks().get(0);
    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    algorithm.getPredict().set(0,
        new TInstruction(TOp.VECTOR_MEAN_OP, kMemory.vectorInputAddress, 2));
    algorithm.getPredict().set(2, new TInstruction(TOp.SCALAR_SUM_OP, 2,
        kMemory.scalarPredictionLabelAddress, kMemory.scalarPredictionLabelAddress));
    TAlgorithmExecutor executor =
        new TAlgorithmExecutor(algorithm, task, TTaskFactoryTestUtil.kNumOfTrainExamples,
            TTaskFactoryTestUtil.kNumOfValidExamples, kLargeMaxAbsError, kMemory, new TRandomGenerator());
    executor.execute();
    assertEquals(504450.0, kMemory.getPredictionLabel());
  }

  @Test
  void executeIteratesThroughLabelsDuringTraining() {
    TIncrementTaskSpec spec = TTaskFactoryTestUtil.getIncrementTaskSpec(TEvalMethod.RMS_ERROR);
    TTask task = spec.createTasks().get(0);
    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    algorithm.getLearn().set(0, new TInstruction(TOp.SCALAR_SUM_OP, kMemory.scalarCorrectLabelAddress,
        kMemory.scalarPredictionLabelAddress, kMemory.scalarPredictionLabelAddress));
    TAlgorithmExecutor executor =
        new TAlgorithmExecutor(algorithm, task, TTaskFactoryTestUtil.kNumOfTrainExamples,
            TTaskFactoryTestUtil.kNumOfValidExamples, kLargeMaxAbsError, kMemory, new TRandomGenerator());
    executor.execute();
    assertEquals(499500.0, kMemory.getPredictionLabel());
  }

  @Test
  void executeIteratesThroughLabelsDuringValidation() {
    TIncrementTaskSpec spec = TTaskFactoryTestUtil.getIncrementTaskSpec(TEvalMethod.RMS_ERROR);
    spec.increment = 0.1;
    TTask task = spec.createTasks().get(0);
    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    TAlgorithmExecutor executor =
        new TAlgorithmExecutor(algorithm, task, TTaskFactoryTestUtil.kNumOfTrainExamples,
            TTaskFactoryTestUtil.kNumOfValidExamples, kLargeMaxAbsError, kMemory,  new TRandomGenerator());
    double fitness = executor.execute();
    assertTrue(Math.abs(1.0 - TMathUtility.squash(5.7301812) - fitness) < kTestTolerance);
  }

  @Test
  void executeStopsEarlyIfLargeErrorInSetupComponent() {
    TIncrementTaskSpec spec = TTaskFactoryTestUtil.getIncrementTaskSpec(TEvalMethod.RMS_ERROR);
    TTask task = spec.createTasks().get(0);
    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    algorithm.getSetup().set(0, new TInstruction(TOp.SCALAR_CONST_SET_OP,
        kMemory.scalarPredictionLabelAddress, kMaxAbsError + 10.0));
    TAlgorithmExecutor executor =
        new TAlgorithmExecutor(algorithm, task, TTaskFactoryTestUtil.kNumOfTrainExamples,
            TTaskFactoryTestUtil.kNumOfValidExamples, kMaxAbsError, kMemory, new TRandomGenerator());
    double fitness = executor.execute();
    assertEquals(TAlgorithmExecutor.kMinFitness, fitness);
  }

  @Test
  void executeStopsEarlyIfLargeErrorInPredictComponent() {
    TIncrementTaskSpec spec = TTaskFactoryTestUtil.getIncrementTaskSpec(TEvalMethod.RMS_ERROR);
    TTask task = spec.createTasks().get(0);
    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    algorithm.getPredict().set(0, new TInstruction(TOp.SCALAR_CONST_SET_OP,
        kMemory.scalarPredictionLabelAddress, kMaxAbsError + 10.0));
    TAlgorithmExecutor executor =
        new TAlgorithmExecutor(algorithm, task, TTaskFactoryTestUtil.kNumOfTrainExamples,
            TTaskFactoryTestUtil.kNumOfValidExamples, kMaxAbsError, kMemory, new TRandomGenerator());
    double fitness = executor.execute();
    assertEquals(TAlgorithmExecutor.kMinFitness, fitness);
  }

  @Test
  void executeStopsEarlyIfLargeErrorInLearnComponent() {
    TIncrementTaskSpec spec = TTaskFactoryTestUtil.getIncrementTaskSpec(TEvalMethod.RMS_ERROR);
    TTask task = spec.createTasks().get(0);
    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    algorithm.getLearn().set(0, new TInstruction(TOp.SCALAR_CONST_SET_OP,
        kMemory.scalarPredictionLabelAddress, kMaxAbsError + 10.0));
    TAlgorithmExecutor executor =
        new TAlgorithmExecutor(algorithm, task, TTaskFactoryTestUtil.kNumOfTrainExamples,
            TTaskFactoryTestUtil.kNumOfValidExamples, kMaxAbsError, kMemory, new TRandomGenerator());
    double fitness = executor.execute();
    assertEquals(TAlgorithmExecutor.kMinFitness, fitness);
  }

  @Test
  void executeStopsEarlyIfInfinityInSetupComponent() {
    TIncrementTaskSpec spec = TTaskFactoryTestUtil.getIncrementTaskSpec(TEvalMethod.RMS_ERROR);
    TTask task = spec.createTasks().get(0);
    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    int oneAddress = 2;
    int zeroAddress = 3;
    algorithm.getSetup().set(0,
        new TInstruction(TOp.SCALAR_CONST_SET_OP, oneAddress, 1.0));
    algorithm.getSetup().set(1, new TInstruction(TOp.SCALAR_DIVISION_OP, oneAddress, zeroAddress,
        kMemory.scalarPredictionLabelAddress));
    TAlgorithmExecutor executor =
        new TAlgorithmExecutor(algorithm, task, TTaskFactoryTestUtil.kNumOfTrainExamples,
            TTaskFactoryTestUtil.kNumOfValidExamples, kMaxAbsError, kMemory, new TRandomGenerator());
    double fitness = executor.execute();
    assertEquals(TAlgorithmExecutor.kMinFitness, fitness);
  }

  @Test
  void executeStopsEarlyIfInfinityInPredictComponent() {
    TIncrementTaskSpec spec = TTaskFactoryTestUtil.getIncrementTaskSpec(TEvalMethod.RMS_ERROR);
    TTask task = spec.createTasks().get(0);
    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    int oneAddress = 2;
    int zeroAddress = 3;
    algorithm.getSetup().set(0,
        new TInstruction(TOp.SCALAR_CONST_SET_OP, oneAddress, 1.0));
    algorithm.getPredict().set(0, new TInstruction(TOp.SCALAR_DIVISION_OP, oneAddress, zeroAddress,
        kMemory.scalarPredictionLabelAddress));
    TAlgorithmExecutor executor =
        new TAlgorithmExecutor(algorithm, task, TTaskFactoryTestUtil.kNumOfTrainExamples,
            TTaskFactoryTestUtil.kNumOfValidExamples, kMaxAbsError, kMemory, new TRandomGenerator());
    double fitness = executor.execute();
    assertEquals(TAlgorithmExecutor.kMinFitness, fitness);
  }

  @Test
  void executeStopsEarlyIfInfinityInLearnComponent() {
    TIncrementTaskSpec spec = TTaskFactoryTestUtil.getIncrementTaskSpec(TEvalMethod.RMS_ERROR);
    TTask task = spec.createTasks().get(0);
    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    int oneAddress = 2;
    int zeroAddress = 3;
    algorithm.getSetup().set(0,
        new TInstruction(TOp.SCALAR_CONST_SET_OP, oneAddress, 1.0));
    algorithm.getLearn().set(0, new TInstruction(TOp.SCALAR_DIVISION_OP, oneAddress, zeroAddress,
        kMemory.scalarPredictionLabelAddress));
    TAlgorithmExecutor executor =
        new TAlgorithmExecutor(algorithm, task, TTaskFactoryTestUtil.kNumOfTrainExamples,
            TTaskFactoryTestUtil.kNumOfValidExamples, kMaxAbsError, kMemory, new TRandomGenerator());
    double fitness = executor.execute();
    assertEquals(TAlgorithmExecutor.kMinFitness, fitness);
  }

  @Test
  void executeStopsEarlyIfNaNInSetupComponent() {
    TIncrementTaskSpec spec = TTaskFactoryTestUtil.getIncrementTaskSpec(TEvalMethod.RMS_ERROR);
    TTask task = spec.createTasks().get(0);
    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    int zeroAddress = 2;
    algorithm.getSetup().set(0, new TInstruction(TOp.SCALAR_DIVISION_OP, zeroAddress, zeroAddress,
        kMemory.scalarPredictionLabelAddress));
    TAlgorithmExecutor executor =
        new TAlgorithmExecutor(algorithm, task, TTaskFactoryTestUtil.kNumOfTrainExamples,
            TTaskFactoryTestUtil.kNumOfValidExamples, kMaxAbsError, kMemory, new TRandomGenerator());
    double fitness = executor.execute();
    assertEquals(TAlgorithmExecutor.kMinFitness, fitness);
  }

  @Test
  void executeStopsEarlyIfNaNInPredictComponent() {
    TIncrementTaskSpec spec = TTaskFactoryTestUtil.getIncrementTaskSpec(TEvalMethod.RMS_ERROR);
    TTask task = spec.createTasks().get(0);
    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    int zeroAddress = 2;
    algorithm.getPredict().set(0, new TInstruction(TOp.SCALAR_DIVISION_OP, zeroAddress, zeroAddress,
        kMemory.scalarPredictionLabelAddress));
    TAlgorithmExecutor executor =
        new TAlgorithmExecutor(algorithm, task, TTaskFactoryTestUtil.kNumOfTrainExamples,
            TTaskFactoryTestUtil.kNumOfValidExamples, kMaxAbsError, kMemory, new TRandomGenerator());
    double fitness = executor.execute();
    assertEquals(TAlgorithmExecutor.kMinFitness, fitness);
  }

  @Test
  void executeStopsEarlyIfNaNInLearnComponent() {
    TIncrementTaskSpec spec = TTaskFactoryTestUtil.getIncrementTaskSpec(TEvalMethod.RMS_ERROR);
    TTask task = spec.createTasks().get(0);
    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    int zeroAddress = 2;
    algorithm.getLearn().set(0, new TInstruction(TOp.SCALAR_DIVISION_OP, zeroAddress, zeroAddress,
        kMemory.scalarPredictionLabelAddress));
    TAlgorithmExecutor executor =
        new TAlgorithmExecutor(algorithm, task, TTaskFactoryTestUtil.kNumOfTrainExamples,
            TTaskFactoryTestUtil.kNumOfValidExamples, kMaxAbsError, kMemory, new TRandomGenerator());
    double fitness = executor.execute();
    assertEquals(TAlgorithmExecutor.kMinFitness, fitness);
  }

  @Test
  void executeStopsEarlyIfDetectTooLargeErrorDuringTraining() {
    TFixedTaskSpec spec = new TFixedTaskSpec();
    spec.evalMethod = TEvalMethod.RMS_ERROR;
    spec.numOfTrainExamples = 10;
    spec.numOfValidExamples = 10;
    spec.featuresSize = TTaskFactoryTestUtil.kFeaturesSize;
    spec.trainFeatures = new TCMatrix[] {new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0})};
    spec.trainLabels = new double[] {0.0, 0.0, 100.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    spec.validFeatures = new TCMatrix[] {new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0})};
    spec.validLabels = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    TTask task = spec.createTasks().get(0);
    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    TAlgorithmExecutor executor =
        new TAlgorithmExecutor(algorithm, task, TTaskFactoryTestUtil.kNumOfTrainExamples,
            TTaskFactoryTestUtil.kNumOfValidExamples, kMaxAbsError, kMemory, new TRandomGenerator());
    double fitness = executor.execute();
    assertEquals(TAlgorithmExecutor.kMinFitness, fitness);
  }

  @Test
  void executeDoesNotStopEarlyUnlessDetectTooLargeErrorDuringTraining() {
    TFixedTaskSpec spec = new TFixedTaskSpec();
    spec.evalMethod = TEvalMethod.RMS_ERROR;
    spec.numOfTrainExamples = 10;
    spec.numOfValidExamples = 10;
    spec.featuresSize = TTaskFactoryTestUtil.kFeaturesSize;
    spec.trainFeatures = new TCMatrix[] {new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0})};
    spec.trainLabels = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    spec.validFeatures = new TCMatrix[] {new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0})};
    spec.validLabels = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    TTask task = spec.createTasks().get(0);
    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    TAlgorithmExecutor executor =
        new TAlgorithmExecutor(algorithm, task, TTaskFactoryTestUtil.kNumOfTrainExamples,
            TTaskFactoryTestUtil.kNumOfValidExamples, kMaxAbsError, kMemory, new TRandomGenerator());
    double fitness = executor.execute();
    assertNotEquals(TAlgorithmExecutor.kMinFitness, fitness);
  }

  @Test
  void executeStopsEarlyIfDetectTooLargeErrorDuringValidation() {
    TFixedTaskSpec spec = new TFixedTaskSpec();
    spec.evalMethod = TEvalMethod.RMS_ERROR;
    spec.numOfTrainExamples = 10;
    spec.numOfValidExamples = 10;
    spec.featuresSize = TTaskFactoryTestUtil.kFeaturesSize;
    spec.trainFeatures = new TCMatrix[] {new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0})};
    spec.trainLabels = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    spec.validFeatures = new TCMatrix[] {new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0}),
        new TCMatrix(new double[] {0.0, 0.0, 0.0, 0.0})};
    spec.validLabels = new double[] {0.0, 0.0, 100.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    TTask task = spec.createTasks().get(0);
    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    TAlgorithmExecutor executor =
        new TAlgorithmExecutor(algorithm, task, TTaskFactoryTestUtil.kNumOfTrainExamples,
            TTaskFactoryTestUtil.kNumOfValidExamples, kMaxAbsError, kMemory, new TRandomGenerator());
    double fitness = executor.execute();
    assertEquals(TAlgorithmExecutor.kMinFitness, fitness);
  }

  @Test
  void executeDoesNotStopEarlyIfEverythingIsFine() {
    TZerosTaskSpec spec = TTaskFactoryTestUtil.getZerosTaskSpec(TEvalMethod.RMS_ERROR);
    TTask task = spec.createTasks().get(0);
    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    TAlgorithmExecutor executor =
        new TAlgorithmExecutor(algorithm, task, TTaskFactoryTestUtil.kNumOfTrainExamples,
            TTaskFactoryTestUtil.kNumOfValidExamples, kMaxAbsError, kMemory, new TRandomGenerator());
    double fitness = executor.execute();
    assertEquals(1.0 - TMathUtility.squash(0.0), fitness);
  }

  @Test
  void producesSameFitnessForSameAlgorithm() {
    TScalarLinearTaskSpec spec = new TScalarLinearTaskSpec();
    spec.featuresSize = 4;
    spec.numOfTrainExamples = 1000;
    spec.numOfValidExamples = 100;
    spec.numOfTasks = 1;
    spec.evalMethod = TEvalMethod.RMS_ERROR;
    TTask task = spec.createTasks().get(0);

    TAlgorithm algorithm = TAlgorithmTestUtil.getOriginalAutoMLZeroDemoHighFitnessAlgorithm();

    double fitness = -1;

    for (int i = 0; i < 100; i++) {
      TAlgorithm copyAlgorithm = new TAlgorithm();
      copyAlgorithm.copyFrom(algorithm);
      TAlgorithmExecutor executor = new TAlgorithmExecutor(copyAlgorithm, task, 1000, 100,
          kMaxAbsError, kMemory, new TRandomGenerator(1000));
      if (fitness == -1) {
        fitness = executor.execute();
      } else {
        assertEquals(fitness, executor.execute());
      }
    }
  }

  @Test
  void produceHighFitnessForOptimalAlgorithm() {
    TScalarLinearTaskSpec spec = new TScalarLinearTaskSpec();
    spec.featuresSize = 4;
    spec.numOfTrainExamples = 1000;
    spec.numOfValidExamples = 100;
    spec.numOfTasks = 1;
    spec.evalMethod = TEvalMethod.RMS_ERROR;
    TTask task = spec.createTasks().get(0);

    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleLinearAlgorithm();
    TAlgorithmExecutor executor = new TAlgorithmExecutor(algorithm, task, 1000, 100, kMaxAbsError,
        kMemory, new TRandomGenerator(1000));
    double fitness = executor.execute();

    assertTrue(fitness > 0.999);
  }
}
