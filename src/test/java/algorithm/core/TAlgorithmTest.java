package algorithm.core;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import instruction.*;
import test_utils.TAlgorithmTestUtil;

public class TAlgorithmTest {

  @Test
  void constructorDefault() {
    TAlgorithm algorithm = new TAlgorithm();
    assertTrue(algorithm.getSetup().size() == 0);
    assertTrue(algorithm.getPredict().size() == 0);
    assertTrue(algorithm.getLearn().size() == 0);
  }

  @Test
  void copyFromOtherCorrectly() {
    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleRandomAlgorithm();
    TAlgorithm otherAlgorithm = new TAlgorithm();
    otherAlgorithm.copyFrom(algorithm);
    assertEquals(otherAlgorithm, algorithm);
  }

  @Test
  void copyConstructor() {
    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleRandomAlgorithm();
    TAlgorithm otherAlgorithm = new TAlgorithm(algorithm);
    assertEquals(otherAlgorithm, algorithm);
  }

  @Test
  void getComponentCorrectly() {
    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleRandomAlgorithm();
    assertTrue(algorithm.getSetup() == algorithm.getComponent(TAlgorithmComponentType.SETUP));
    assertTrue(algorithm.getPredict() == algorithm.getComponent(TAlgorithmComponentType.PREDICT));
    assertTrue(algorithm.getLearn() == algorithm.getComponent(TAlgorithmComponentType.LEARN));
  }

  @Test
  void equals() {
    TAlgorithm algorithm = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    algorithm.getPredict().set(1, new TInstruction(TOp.VECTOR_SUM_OP, 1, 2, 3));

    TAlgorithm algorithmSame = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    algorithmSame.getPredict().set(1, new TInstruction(TOp.VECTOR_SUM_OP, 1, 2, 3));

    TAlgorithm algorithmDifferentInstruction = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    algorithmDifferentInstruction.getPredict().set(1, new TInstruction(TOp.VECTOR_SUM_OP, 1, 1, 3));

    TAlgorithm algorithmDifferentPosition = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    algorithmDifferentPosition.getPredict().set(0, new TInstruction(TOp.VECTOR_SUM_OP, 1, 1, 3));

    TAlgorithm algorithmDifferentComponent = TAlgorithmTestUtil.getSimpleNoOpAlgorithm();
    algorithmDifferentComponent.getLearn().set(1, new TInstruction(TOp.VECTOR_SUM_OP, 1, 2, 3));

    assertEquals(algorithm, algorithmSame);
    assertNotEquals(algorithm, algorithmDifferentInstruction);
    assertNotEquals(algorithm, algorithmDifferentPosition);
    assertNotEquals(algorithm, algorithmDifferentComponent);

    TAlgorithm randomAlgorithm = TAlgorithmTestUtil.getSimpleRandomAlgorithm();
    TAlgorithm sameRandomAlgorithm = randomAlgorithm;
    assertTrue(randomAlgorithm == sameRandomAlgorithm);
  }
}
