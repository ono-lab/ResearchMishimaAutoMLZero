package evaluator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import algorithm.core.*;
import test_utils.TAlgorithmTestUtil;

public class TTrainBudgetTest {
  @Test
  void matchesBaselineExactly() {
    TAlgorithm baselineAlgorithm = TAlgorithmTestUtil.getSimpleNeuralNetAlgorithm();
    TTrainBudget trainBudget = new TTrainBudget(baselineAlgorithm, 2.0);
    TAlgorithm algorithm = baselineAlgorithm;
    assertEquals(trainBudget.getNumOfTrainExamplesInBudget(algorithm, 100), 100);
    assertEquals(trainBudget.getNumOfTrainExamplesInBudget(algorithm, 1000), 1000);
  }

  @Test
  void cheaperModelIsUnaffected() {
    TAlgorithm baselineAlgorithm = TAlgorithmTestUtil.getSimpleNeuralNetAlgorithm();
    TTrainBudget trainBudget = new TTrainBudget(baselineAlgorithm, 2.0);
    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleLinearAlgorithm();
    assertEquals(trainBudget.getNumOfTrainExamplesInBudget(algorithm, 100), 100);
    assertEquals(trainBudget.getNumOfTrainExamplesInBudget(algorithm, 1000), 1000);
  }

  @Test
  void moreExpensiveModelIsRejected() {
    TAlgorithm baselineAlgorithm = TAlgorithmTestUtil.getSimpleLinearAlgorithm();
    TTrainBudget trainBudget = new TTrainBudget(baselineAlgorithm, 2.0);
    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleNeuralNetAlgorithm();
    assertEquals(trainBudget.getNumOfTrainExamplesInBudget(algorithm, 100), 0);
    assertEquals(trainBudget.getNumOfTrainExamplesInBudget(algorithm, 1000), 0);
  }
}
