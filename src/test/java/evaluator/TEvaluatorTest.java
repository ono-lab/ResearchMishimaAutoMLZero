package evaluator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;
import org.junit.jupiter.api.Test;

import algorithm.core.*;
import instruction.TOp;
import memory.TMemory;
import method.re.*;
import mutator.esteban.*;
import population.*;
import task.core.*;
import task.spec.*;
import test_utils.*;
import utils.TRandomGenerator;

public class TEvaluatorTest {
  static private final double kMaxAbsError = 100.0;

  @Test
  void evaluateCalculateFitnessAveragesOverTasks() {
    TScalar2LayerNNTaskSpec spec1 = TTaskFactoryTestUtil.getScalar2LayerNNRegressionTaskSpec(1, new long[] {1001},
        new long[] {11001});
    TScalar2LayerNNTaskSpec spec2 = TTaskFactoryTestUtil.getScalar2LayerNNRegressionTaskSpec(1, new long[] {1002},
        new long[] {11012});
    TTask task1 = spec1.createTasks().get(0);
    TTask task2 = spec2.createTasks().get(0);
    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleLinearAlgorithm();

    TMemory memory = new TMemory(TTaskFactoryTestUtil.kFeaturesSize);
    TRandomGenerator rand = new TRandomGenerator();

    // Run with tasks independently.
    TAlgorithmExecutor executor1 =
        new TAlgorithmExecutor(algorithm, task1, TTaskFactoryTestUtil.kNumOfTrainExamples,
            TTaskFactoryTestUtil.kNumOfValidExamples, kMaxAbsError, memory, rand);
    double fitness1 = executor1.execute();
    TAlgorithmExecutor executor2 =
        new TAlgorithmExecutor(algorithm, task2, TTaskFactoryTestUtil.kNumOfTrainExamples,
            TTaskFactoryTestUtil.kNumOfValidExamples, kMaxAbsError, memory, rand);
    double fitness2 = executor2.execute();
    double expectedFitness = (fitness1 + fitness2) / 2.0;
    TScalar2LayerNNTaskSpec[] specs = new TScalar2LayerNNTaskSpec[] { spec1, spec2 };

    TEvaluator evaluator = new TEvaluator(TFitnessCombinationMode.MEAN,
        specs, null, null, kMaxAbsError, memory, rand);
    double fitness = evaluator.execute(algorithm);

    assertEquals(expectedFitness, fitness);
  }

  @Test
  void linearAlgorithmHasHighFitnessForLinearRegressionTask() {
    TRandomGenerator rand = new TRandomGenerator(1000);
    TMemory memory = new TMemory(TTaskFactoryTestUtil.kFeaturesSize);
    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleLinearAlgorithm();
    TScalarLinearTaskSpec spec = new TScalarLinearTaskSpec();
    spec.featuresSize = 4;
    spec.numOfTrainExamples = 1000;
    spec.numOfValidExamples = 100;
    spec.numOfTasks = 10;
    spec.evalMethod = TEvalMethod.RMS_ERROR;
    TScalarLinearTaskSpec[] specs = new TScalarLinearTaskSpec[] { spec };
    TEvaluator evaluator = new TEvaluator(TFitnessCombinationMode.MEAN, specs,
        null, null, kMaxAbsError, memory, rand);
    double fitness = evaluator.execute(algorithm);
    assertTrue(fitness > 0.9999 && fitness <= 1.0);
  }

  @Test
  void producesSameValueForSameAlgorithm() {
    TMemory memory = new TMemory(TTaskFactoryTestUtil.kFeaturesSize);
    TRandomGenerator rand = new TRandomGenerator();

    TOp[] ops = new TOp[] {TOp.SCALAR_CONST_SET_OP, TOp.VECTOR_INNER_PRODUCT_OP,
        TOp.SCALAR_DIFF_OP, TOp.SCALAR_PRODUCT_OP, TOp.SCALAR_VECTOR_PRODUCT_OP, TOp.VECTOR_SUM_OP};
    TAlgorithmOpsSet opsSet = new TAlgorithmOpsSet(ops, ops, ops);

    TScalarLinearTaskSpec searchTaskSpec = new TScalarLinearTaskSpec();
    searchTaskSpec.featuresSize = 4;
    searchTaskSpec.numOfTrainExamples = 1000;
    searchTaskSpec.numOfValidExamples = 100;
    searchTaskSpec.numOfTasks = 10;
    searchTaskSpec.evalMethod = TEvalMethod.RMS_ERROR;
    TScalarLinearTaskSpec[] specs = new TScalarLinearTaskSpec[] {searchTaskSpec};

    TAlgorithmMutation[] actions = new TAlgorithmMutation[] {TAlgorithmMutation.ALTER_PARAM,
        TAlgorithmMutation.RANDOMIZE_INSTRUCTION, TAlgorithmMutation.RANDOMIZE_COMPONENT };


    TAlgorithmMutator mutator = new TAlgorithmMutator(actions,
        0.1, 10, 10, 2, 2, 8, 8, opsSet, memory, rand);
    TEvaluator evaluator = new TEvaluator(TFitnessCombinationMode.MEAN,
        specs, null, null, 100.0, memory, rand);

    // 初期個体の生成
    TPopulation<TIndividualBase> pop = new TPopulation<TIndividualBase>();
    for (int i = 0; i < 10; i++) {
      TAlgorithm algorithm = i == 9 ? TAlgorithmTestUtil.getSimpleLinearAlgorithm() : TAlgorithmTestUtil.getSimpleRandomAlgorithm();
      double fitness = evaluator.execute(algorithm);
      pop.add(new TIndividual(algorithm, fitness));
    }

    // 突然変異
    for (int g = 0; g < 100; g++) {
      for (TIndividualBase individual : pop.getAll()) {
        TAlgorithm algorithm = individual.getAlgorithm();
        TIndividualBase nextBaseIndividual = pop.getTournamentSelected(10, rand);
        TAlgorithm nextBaseAlgorithm = nextBaseIndividual.getAlgorithm();
        double nextBaseFitness = nextBaseIndividual.getFitness();
        algorithm.copyFrom(nextBaseAlgorithm);
        TAlgorithm nextAlgorithm = mutator.mutate(1, algorithm);
        double nextFitness = evaluator.execute(nextAlgorithm);
        individual.setFitness(nextFitness);
        assertFalse(nextBaseAlgorithm != nextAlgorithm
            && Objects.equals(nextBaseAlgorithm, nextAlgorithm) && nextBaseFitness != nextFitness);
      }
    }
  }

  @Test
  void meanFitnessWorksCorrectly() {
    assertEquals(0.5, TFitnessCombinationMode.MEAN.combine(new double[] {0.4, 0.2, 0.6, 0.8}));
  }

  @Test
  void medianFitnessWorksCorrectly() {
    assertEquals(0.5, TFitnessCombinationMode.MEDIAN.combine(new double[] {0.4, 0.2, 0.6, 0.8}));
  }
}
