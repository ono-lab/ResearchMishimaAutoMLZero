package instruction;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import jp.ac.titech.onolab.core.matrix.TCMatrix;
import memory.TMemory;
import utils.TRandomGenerator;

class TInstructionExecutorTest {
  static private final int kIn1 = 1;
  static private final int kIn2 = 0;
  static private final int kOut = 1;
  static private final TRandomGenerator kRand = new TRandomGenerator();
  static private final TMemory kMemory = new TMemory(4);
  static private final double kTestTolerance = 0.0001;

  static private TInstruction makeZeroInputsInstruction(TOp op, double data1) {
    return new TInstruction(op, kOut, data1);
  }

  static private TInstruction makeZeroInputsInstruction(TOp op, double data1, double data2) {
    return new TInstruction(op, kOut, data1, data2);
  }

  static private TInstruction makeZeroInputsInstruction(TOp op, int index1, double data) {
    return new TInstruction(op, kOut, data, index1);
  }

  static private TInstruction makeZeroInputsInstruction(TOp op, int index1, int index2, double data) {
    return new TInstruction(op, kOut, data, index1, index2);
  }

  static private TInstruction makeOneInputInstruction(TOp op) {
    return new TInstruction(op, kIn1, kOut);
  }

  static private TInstruction makeTwoInputsInstruction(TOp op) {
    return new TInstruction(op, kIn1, kIn2, kOut);
  }

  static private void verifyNothingToScalarEquals(TInstruction instruction, double expectedOut) {
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    assertTrue(Math.abs(kMemory.scalar[kOut] - expectedOut) <= kTestTolerance);
  }

  static private void verifyNothingToScalarIsRandomized(TInstruction instruction) {
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    double out1 = kMemory.scalar[kOut];
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    double out2 = kMemory.scalar[kOut];
    assertTrue(Math.abs(out1 - out2) > kTestTolerance);
  }

  static private void verifyNothingToVectorEquals(TInstruction instruction, double[] expectedOut) {
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    TCMatrix expectedOutVector = new TCMatrix(expectedOut);
    assertTrue(expectedOutVector.sub(kMemory.vector[kOut]).normL2() <= kTestTolerance);
  }

  static private void verifyNothingToVectorIsRandomized(TInstruction instruction) {
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    TCMatrix out1 = kMemory.vector[kOut].clone();
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    TCMatrix out2 = kMemory.vector[kOut].clone();
    assertTrue(out1.sub(out2).normL2() > kTestTolerance);
  }

  static private void verifyNothingToMatrixEquals(TInstruction instruction,
      double[][] expectedOut) {
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    TCMatrix expectedOutVector = new TCMatrix(expectedOut);
    assertTrue(expectedOutVector.sub(kMemory.matrix[kOut]).normF() <= kTestTolerance);
  }

  static private void verifyNothingToMatrixIsRandomized(TInstruction instruction) {
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    TCMatrix out1 = kMemory.matrix[kOut].clone();
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    TCMatrix out2 = kMemory.matrix[kOut].clone();
    assertTrue(out1.sub(out2).normF() > kTestTolerance);
  }

  static private void verifyScalarToScalarEquals(TInstruction instruction, double in1,
      double expectedOut) {
    kMemory.scalar[kIn1] = in1;
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    assertTrue(Math.abs(kMemory.scalar[kOut] - expectedOut) <= kTestTolerance);
  }

  static private void verifyScalarToScalarIsGreater(TInstruction instruction, double in1,
      double than) {
    kMemory.scalar[kIn1] = in1;
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    assertTrue(kMemory.scalar[kOut] > than);
  }

  static private void verifyScalarToScalarIsLess(TInstruction instruction, double in1,
      double than) {
    kMemory.scalar[kIn1] = in1;
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    assertTrue(kMemory.scalar[kOut] < than);
  }

  static private void verifyScalarToScalarIsNan(TInstruction instruction, double in1) {
    kMemory.scalar[kIn1] = in1;
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    assertTrue(Double.isNaN(kMemory.scalar[kOut]));
  }

  static private void verifyScalarToScalarIsInf(TInstruction instruction, double in1) {
    kMemory.scalar[kIn1] = in1;
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    assertTrue(Double.isInfinite(kMemory.scalar[kOut]));
  }

  static private void verifyVectorToScalarEquals(TInstruction instruction, double[] in1,
      double expectedOut) {
    kMemory.vector[kIn1] = new TCMatrix(in1);
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    assertTrue(Math.abs(kMemory.scalar[kOut] - expectedOut) <= kTestTolerance);
  }

  static private void verifyVectorToVectorEquals(TInstruction instruction, double[] in1,
      double[] expectedOut) {
    kMemory.vector[kIn1] = new TCMatrix(in1);
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    TCMatrix expectedOutVector = new TCMatrix(expectedOut);
    assertTrue(expectedOutVector.sub(kMemory.vector[kOut]).normL2() <= kTestTolerance);
  }

  static private void verifyMatrixToScalarEquals(TInstruction instruction, double[][] in1,
      double expectedOut) {
    kMemory.matrix[kIn1] = new TCMatrix(in1);
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    assertTrue(Math.abs(kMemory.scalar[kOut] - expectedOut) <= kTestTolerance);
  }

  static private void verifyMatrixToVectorEquals(TInstruction instruction, double[][] in1,
      double[] expectedOut) {
    kMemory.matrix[kIn1] = new TCMatrix(in1);
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    TCMatrix expectedOutVector = new TCMatrix(expectedOut);
    assertTrue(expectedOutVector.sub(kMemory.vector[kOut]).normL2() <= kTestTolerance);
  }

  static private void verifyScalarScalarToScalarEquals(TInstruction instruction, double in1,
      double in2, double expectedOut) {
    kMemory.scalar[kIn1] = in1;
    kMemory.scalar[kIn2] = in2;
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    assertTrue(Math.abs(kMemory.scalar[kOut] - expectedOut) <= kTestTolerance);
  }

  static private void verifyScalarScalarToScalarIsNan(TInstruction instruction, double in1,
      double in2) {
    kMemory.scalar[kIn1] = in1;
    kMemory.scalar[kIn2] = in2;
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    assertTrue(Double.isNaN(kMemory.scalar[kOut]));
  }

  static private void verifyScalarScalarToScalarIsInf(TInstruction instruction, double in1,
      double in2) {
    kMemory.scalar[kIn1] = in1;
    kMemory.scalar[kIn2] = in2;
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    assertTrue(Double.isInfinite(kMemory.scalar[kOut]));
  }

  static private void verifyScalarVectorToVectorEquals(TInstruction instruction, double in1,
      double[] in2, double[] expectedOut) {
    kMemory.scalar[kIn1] = in1;
    kMemory.vector[kIn2] = new TCMatrix(in2);
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    TCMatrix expectedOutVector = new TCMatrix(expectedOut);
    assertTrue(expectedOutVector.sub(kMemory.vector[kOut]).normL2() <= kTestTolerance);
  }

  static private void verifyScalarMatrixToMatrixEquals(TInstruction instruction, double in1,
      double[][] in2, double[][] expectedOut) {
    kMemory.scalar[kIn1] = in1;
    kMemory.matrix[kIn2] = new TCMatrix(in2);
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    TCMatrix expectedOutMatrix = new TCMatrix(expectedOut);
    assertTrue(expectedOutMatrix.sub(kMemory.matrix[kOut]).normF() <= kTestTolerance);
  }

  static private void verifyVectorVectorToScalarEquals(TInstruction instruction, double[] in1,
      double[] in2, double expectedOut) {
    kMemory.vector[kIn1] = new TCMatrix(in1);
    kMemory.vector[kIn2] = new TCMatrix(in2);
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    assertTrue(Math.abs(kMemory.scalar[kOut] - expectedOut) <= kTestTolerance);
  }

  static private void verifyVectorVectorToVectorEquals(TInstruction instruction, double[] in1,
      double[] in2, double[] expectedOut) {
    kMemory.vector[kIn1] = new TCMatrix(in1);
    kMemory.vector[kIn2] = new TCMatrix(in2);
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    TCMatrix expectedOutVector = new TCMatrix(expectedOut);
    assertTrue(expectedOutVector.sub(kMemory.vector[kOut]).normL2() <= kTestTolerance);
  }

  static private void verifyVectorVectorToVectorIsNan(TInstruction instruction, double[] in1,
      double[] in2) {
    kMemory.vector[kIn1] = new TCMatrix(in1);
    kMemory.vector[kIn2] = new TCMatrix(in2);
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    assertTrue(Double.isNaN(kMemory.vector[kOut].normL2()));
  }

  static private void verifyVectorVectorToVectorIsInf(TInstruction instruction, double[] in1,
      double[] in2) {
    kMemory.vector[kIn1] = new TCMatrix(in1);
    kMemory.vector[kIn2] = new TCMatrix(in2);
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    assertTrue(Double.isInfinite(kMemory.vector[kOut].normL2()));
  }

  static private void verifyVectorVectorToMatrixEquals(TInstruction instruction, double[] in1,
      double[] in2, double[][] expectedOut) {
    kMemory.vector[kIn1] = new TCMatrix(in1);
    kMemory.vector[kIn2] = new TCMatrix(in2);
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    TCMatrix expectedOutMatrix = new TCMatrix(expectedOut);
    assertTrue(expectedOutMatrix.sub(kMemory.matrix[kOut]).normF() <= kTestTolerance);
  }

  static private void verifyMatrixVectorToVectorEquals(TInstruction instruction, double[][] in1,
      double[] in2, double[] expectedOut) {
    kMemory.matrix[kIn1] = new TCMatrix(in1);
    kMemory.vector[kIn2] = new TCMatrix(in2);
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    TCMatrix expectedOutVector = new TCMatrix(expectedOut);
    assertTrue(expectedOutVector.sub(kMemory.vector[kOut]).normL2() <= kTestTolerance);
  }

  static private void verifyMatrixMatrixToMatrixEquals(TInstruction instruction, double[][] in1,
      double[][] in2, double[][] expectedOut) {
    kMemory.matrix[kIn1] = new TCMatrix(in1);
    kMemory.matrix[kIn2] = new TCMatrix(in2);
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    TCMatrix expectedOutMatrix = new TCMatrix(expectedOut);
    assertTrue(expectedOutMatrix.sub(kMemory.matrix[kOut]).normF() <= kTestTolerance);
  }

  static private void verifyMatrixMatrixToMatrixIsNan(TInstruction instruction, double[][] in1,
      double[][] in2) {
    kMemory.matrix[kIn1] = new TCMatrix(in1);
    kMemory.matrix[kIn2] = new TCMatrix(in2);
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    assertTrue(Double.isNaN(kMemory.matrix[kOut].normF()));
  }

  static private void verifyMatrixMatrixToMatrixIsInf(TInstruction instruction, double[][] in1,
      double[][] in2) {
    kMemory.matrix[kIn1] = new TCMatrix(in1);
    kMemory.matrix[kIn2] = new TCMatrix(in2);
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    assertTrue(Double.isInfinite(kMemory.matrix[kOut].normF()));
  }

  static private void verifyMatrixToMatrixEquals(TInstruction instruction, double[][] in1,
      double[][] expectedOut) {
    kMemory.matrix[kIn1] = new TCMatrix(in1);
    TInstructionExecutor.execute(instruction, kRand, kMemory);
    TCMatrix expectedOutMatrix = new TCMatrix(expectedOut);
    assertTrue(expectedOutMatrix.sub(kMemory.matrix[kOut]).normF() <= kTestTolerance);
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Scalar arithmetic-related instructions.
  ////////////////////////////////////////////////////////////////////////////////

  @Test
  void scalarSumOpExecuteCorrectly() {
    verifyScalarScalarToScalarEquals(makeTwoInputsInstruction(TOp.SCALAR_SUM_OP), 0.5, 2.0, 2.5);
  }

  @Test
  void scalarDiffOpExecuteCorrectly() {
    verifyScalarScalarToScalarEquals(makeTwoInputsInstruction(TOp.SCALAR_DIFF_OP), -0.2, 2.3, -2.5);
  }

  @Test
  void scalarProductOpExecuteCorrectly() {
    verifyScalarScalarToScalarEquals(makeTwoInputsInstruction(TOp.SCALAR_PRODUCT_OP), -0.2, 2.3,
        -0.46);
  }

  @Test
  void scalarDivisionOpExecuteCorrectly() {
    verifyScalarScalarToScalarEquals(makeTwoInputsInstruction(TOp.SCALAR_DIVISION_OP), 8.8, -2.0,
        -4.4);
    verifyScalarScalarToScalarIsNan(makeTwoInputsInstruction(TOp.SCALAR_DIVISION_OP), 0.0, 0.0);
    verifyScalarScalarToScalarIsInf(makeTwoInputsInstruction(TOp.SCALAR_DIVISION_OP), 1.0, 0.0);
  }

  @Test
  void scalarMinOpExecuteCorrectly() {
    verifyScalarScalarToScalarEquals(makeTwoInputsInstruction(TOp.SCALAR_MIN_OP), 0.5, 2.2, 0.5);
    verifyScalarScalarToScalarEquals(makeTwoInputsInstruction(TOp.SCALAR_MIN_OP), 2.2, 0.5, 0.5);
    verifyScalarScalarToScalarEquals(makeTwoInputsInstruction(TOp.SCALAR_MIN_OP), -2.2, -0.5, -2.2);
    verifyScalarScalarToScalarEquals(makeTwoInputsInstruction(TOp.SCALAR_MIN_OP), 2.2, -0.5, -0.5);
  }

  @Test
  void scalarMaxOpExecuteCorrectly() {
    verifyScalarScalarToScalarEquals(makeTwoInputsInstruction(TOp.SCALAR_MAX_OP), 0.5, 2.2, 2.2);
    verifyScalarScalarToScalarEquals(makeTwoInputsInstruction(TOp.SCALAR_MAX_OP), 2.2, 0.5, 2.2);
    verifyScalarScalarToScalarEquals(makeTwoInputsInstruction(TOp.SCALAR_MAX_OP), -2.2, -0.5, -0.5);
    verifyScalarScalarToScalarEquals(makeTwoInputsInstruction(TOp.SCALAR_MAX_OP), 0.5, -2.2, 0.5);
  }

  @Test
  void scalarAbsOpExecuteCorrectly() {
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_ABS_OP), 2.5, 2.5);
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_ABS_OP), -2.5, 2.5);
  }

  @Test
  void scalarHeavisideOpExecuteCorrectly() {
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_HEAVYSIDE_OP), -2.5, 0.0);
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_HEAVYSIDE_OP), -0.5, 0.0);
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_HEAVYSIDE_OP), 0.5, 1.0);
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_HEAVYSIDE_OP), 2.5, 1.0);
  }

  @Test
  void scalarConstSetOpExecuteCorrectly() {
    verifyNothingToScalarEquals(makeZeroInputsInstruction(TOp.SCALAR_CONST_SET_OP, -0.5), -0.5);
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Trigonometry-related instructions.
  ////////////////////////////////////////////////////////////////////////////////

  @Test
  void scalarSinOpExecuteCorrectly() {
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_SIN_OP), 0.0, 0.0);
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_SIN_OP), Math.PI / 6.0, 0.5);
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_SIN_OP), Math.PI / 2.0, 1.0);
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_SIN_OP), 3 * Math.PI / 2, -1.0);
  }

  @Test
  void scalarCosOpExecuteCorrectly() {
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_COS_OP), 0.0, 1.0);
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_COS_OP), Math.PI / 3.0, 0.5);
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_COS_OP), Math.PI / 2.0, 0.0);
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_COS_OP), Math.PI, -1.0);
  }

  @Test
  void scalarTanOpExecuteCorrectly() {
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_TAN_OP), 0.0, 0.0);
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_TAN_OP), Math.PI / 4.0, 1.0);
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_TAN_OP), 3 * Math.PI / 4.0, -1.0);
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_TAN_OP), 5 * Math.PI / 4.0, 1.0);
    verifyScalarToScalarIsGreater(makeOneInputInstruction(TOp.SCALAR_TAN_OP),
        Math.PI / 2.0 - 0.000000001, 1000000.0);
    verifyScalarToScalarIsLess(makeOneInputInstruction(TOp.SCALAR_TAN_OP),
        Math.PI / 2.0 + 0.000000001, -1000000.0);
  }

  @Test
  void scalarArcSinOpExecuteCorrectly() {
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_ARCSIN_OP), 0.0, 0.0);
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_ARCSIN_OP), 0.5, Math.PI / 6.0);
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_ARCSIN_OP), 1.0, Math.PI / 2.0);
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_ARCSIN_OP), -1.0, -Math.PI / 2);
  }

  @Test
  void scalarArcCosOpExecuteCorrectly() {
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_ARCCOS_OP), 1.0, 0.0);
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_ARCCOS_OP), 0.5, Math.PI / 3.0);
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_ARCCOS_OP), 0.0, Math.PI / 2.0);
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_ARCCOS_OP), -1.0, Math.PI);
  }

  @Test
  void scalarArcTanOpExecuteCorrectly() {
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_ARCTAN_OP), 0.0, 0.0);
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_ARCTAN_OP), 1.0, Math.PI / 4.0);
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_ARCTAN_OP), -1.0, -Math.PI / 4.0);
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_ARCTAN_OP), 1000000000.0,
        Math.PI / 2.0);
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_ARCTAN_OP), -1000000000.0,
        -Math.PI / 2.0);
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Calculus-related instructions.
  ////////////////////////////////////////////////////////////////////////////////

  @Test
  void scalarExpOpExecuteCorrectly() {
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_EXP_OP), 0.0, 1.0);
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_EXP_OP), 1.0, Math.E);
  }

  @Test
  void scalarLogOpExecuteCorrectly() {
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_LOG_OP), 1.0, 0.0);
    verifyScalarToScalarEquals(makeOneInputInstruction(TOp.SCALAR_LOG_OP), Math.E, 1.0);
    verifyScalarToScalarIsInf(makeOneInputInstruction(TOp.SCALAR_LOG_OP), 0.0);
    verifyScalarToScalarIsNan(makeOneInputInstruction(TOp.SCALAR_LOG_OP), -0.1);
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Vector arithmetic-related instructions.
  ////////////////////////////////////////////////////////////////////////////////
  @Test
  void vectorSumOpExecuteCorrectly() {
    verifyVectorVectorToVectorEquals(makeTwoInputsInstruction(TOp.VECTOR_SUM_OP),
        new double[] {0.1, -0.1, 1.2, -10.0}, new double[] {2.3, -0.3, 0.5, -0.001},
        new double[] {2.4, -0.4, 1.7, -10.001});
  }

  @Test
  void vectorDiffOpExecuteCorrectly() {
    verifyVectorVectorToVectorEquals(makeTwoInputsInstruction(TOp.VECTOR_DIFF_OP),
        new double[] {0.1, -0.1, 1.2, -10.0}, new double[] {2.3, -0.3, 0.5, -0.001},
        new double[] {-2.2, 0.2, 0.7, -9.999});
  }

  @Test
  void vectorProductOpExecuteCorrectly() {
    verifyVectorVectorToVectorEquals(makeTwoInputsInstruction(TOp.VECTOR_PRODUCT_OP),
        new double[] {0.1, -0.1, 1.2, -10.0}, new double[] {2.3, -0.3, 0.5, -0.001},
        new double[] {0.23, 0.03, 0.6, 0.01});
  }

  @Test
  void vectorDivisionOpExecuteCorrectly() {
    verifyVectorVectorToVectorEquals(makeTwoInputsInstruction(TOp.VECTOR_DIVISION_OP),
        new double[] {7.0, -18.18, 1.0, 0.0}, new double[] {2.0, 3.0, 0.5, -0.5},
        new double[] {3.5, -6.06, 2.0, 0.0});
    verifyVectorVectorToVectorIsNan(makeTwoInputsInstruction(TOp.VECTOR_DIVISION_OP),
        new double[] {7.0, -18.18, 0.0, -10.0}, new double[] {2.0, 3.0, 0.0, -0.5});
    verifyVectorVectorToVectorIsInf(makeTwoInputsInstruction(TOp.VECTOR_DIVISION_OP),
        new double[] {7.0, -18.18, 1.0, -10.0}, new double[] {2.0, 3.0, 0.0, -0.5});
  }

  @Test
  void vectorMinOpExecuteCorrectly() {
    verifyVectorVectorToVectorEquals(makeTwoInputsInstruction(TOp.VECTOR_MIN_OP),
        new double[] {0.5, 2.2, -2.2, 2.2}, new double[] {2.2, 0.5, -0.5, -0.5},
        new double[] {0.5, 0.5, -2.2, -0.5});
  }

  @Test
  void vectorMaxOpExecuteCorrectly() {
    verifyVectorVectorToVectorEquals(makeTwoInputsInstruction(TOp.VECTOR_MAX_OP),
        new double[] {0.5, 2.2, -2.2, 0.5}, new double[] {2.2, 0.5, -0.5, -2.2},
        new double[] {2.2, 2.2, -0.5, 0.5});
  }

  @Test
  void vectorAbsOpExecuteCorrectly() {
    verifyVectorToVectorEquals(makeOneInputInstruction(TOp.VECTOR_ABS_OP),
        new double[] {0.5, 0.0, -2.2, 100.5}, new double[] {0.5, 0.0, 2.2, 100.5});
  }

  @Test
  void vectorHeavisideOpExecuteCorrectly() {
    verifyVectorToVectorEquals(makeOneInputInstruction(TOp.VECTOR_HEAVYSIDE_OP),
        new double[] {-0.01, 0.001, 1.3, -0.001}, new double[] {0.0, 1.0, 1.0, 0.0});
  }

  @Test
  void vectorConstSetOpExecuteCorrectly() {
    double[] expected = new double[4];
    for (int i = 0; i < 4; i++) {
      expected[i] = i == 2 ? -1.5 : kMemory.vector[kOut].getValue(i);
    }
    verifyNothingToVectorEquals(makeZeroInputsInstruction(TOp.VECTOR_CONST_SET_OP, 2, -1.5), expected);
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Matrix arithmetic-related instructions.
  ////////////////////////////////////////////////////////////////////////////////

  @Test
  void matrixSumOpExecuteCorrectly() {
    verifyMatrixMatrixToMatrixEquals(makeTwoInputsInstruction(TOp.MATRIX_SUM_OP),
        new double[][] {{-2.0, 10.0, 0.3, 0.0}, {0.0, 8.0, -0.1, 0.0}, {20.0, 0.0, 20.0, 50.0},
            {-0.01, -1.0, 25.0, -32.0}},
        new double[][] {{-0.2, 1.0, 0.03, 0.0}, {0.0, 0.8, -0.01, 0.0}, {2.0, 0.0, 2.0, 5.0},
            {-0.001, -0.1, 2.5, -3.2}},
        new double[][] {{-2.2, 11.0, 0.33, 0.0}, {0.0, 8.8, -0.11, 0.0}, {22.0, 0.0, 22.0, 55.0},
            {-0.011, -1.1, 27.5, -35.2}});
  }

  @Test
  void matrixDiffOpExecuteCorrectly() {
    verifyMatrixMatrixToMatrixEquals(makeTwoInputsInstruction(TOp.MATRIX_DIFF_OP),
        new double[][] {{-2.0, 10.0, 0.3, 0.0}, {0.0, 8.0, -0.1, 0.0}, {20.0, 0.0, 20.0, 50.0},
            {-0.01, -1.0, 25.0, -32.0}},
        new double[][] {{-0.2, 1.0, 0.03, 0.0}, {0.0, 0.8, -0.01, 0.0}, {2.0, 0.0, 2.0, 5.0},
            {-0.001, -0.1, 2.5, -3.2}},
        new double[][] {{-1.8, 9.0, 0.27, 0.0}, {0.0, 7.2, -0.09, 0.0}, {18.0, 0.0, 18.0, 45.0},
            {-0.009, -0.9, 22.5, -28.8}});
  }

  @Test
  void matrixProductOpExecuteCorrectly() {
    verifyMatrixMatrixToMatrixEquals(makeTwoInputsInstruction(TOp.MATRIX_PRODUCT_OP),
        new double[][] {{0.1, -0.1, 1.2, -10.0}, {0.1, 1.2, -0.1, -10.0}, {1.0, -1.0, 12.0, -100.0},
            {0.01, -0.01, 0.12, -1.00}},
        new double[][] {{2.3, -0.3, 0.5, -0.001}, {2.3, 0.5, -0.3, -0.001}, {23, -3.0, 5.0, -0.01},
            {0.23, -0.03, 0.05, -0.0001}},
        new double[][] {{0.23, 0.03, 0.6, 0.01}, {0.23, 0.6, 0.03, 0.01}, {23.0, 3.0, 60.0, 1.0},
            {0.0023, 0.0003, 0.006, 0.0001}});
  }

  @Test
  void matrixDivisionOpExecuteCorrectly() {
    verifyMatrixMatrixToMatrixEquals(makeTwoInputsInstruction(TOp.MATRIX_DIVISION_OP),
        new double[][] {{7.0, -18.18, 1.0, 0.0}, {7.0, 1.0, -18.18, 0.0}, {70.0, -181.8, 10.0, 0.0},
            {70.0, -181.8, 0.0, 10.0}},
        new double[][] {{2.0, 3.0, 0.5, -0.5}, {2.0, 0.5, 3.0, -0.5}, {20.0, 30.0, 5.0, -5.0},
            {2.0, 3.0, -0.5, 0.5}},
        new double[][] {{3.5, -6.06, 2.0, 0.0}, {3.5, 2.0, -6.06, 0.0}, {3.5, -6.06, 2.0, 0.0},
            {35.0, -60.6, 0.0, 20.0}});
    verifyMatrixMatrixToMatrixIsNan(makeTwoInputsInstruction(TOp.MATRIX_DIVISION_OP),
        new double[][] {{7.0, -18.18, 1.0, 0.0}, {7.0, 1.0, 0.0, 0.0}, {70.0, -181.8, 10.0, 0.0},
            {70.0, -181.8, 0.0, 10.0}},
        new double[][] {{2.0, 3.0, 0.5, -0.5}, {2.0, 0.5, 0.0, -0.5}, {20.0, 30.0, 5.0, -5.0},
            {2.0, 3.0, -0.5, 0.5}});

    verifyMatrixMatrixToMatrixIsInf(makeTwoInputsInstruction(TOp.MATRIX_DIVISION_OP),
        new double[][] {{7.0, -18.18, 1.0, 0.0}, {7.0, 1.0, 1.0, 0.0}, {70.0, -181.8, 10.0, 0.0},
            {70.0, -181.8, 0.0, 10.0}},
        new double[][] {{2.0, 3.0, 0.5, -0.5}, {2.0, 0.5, 0.0, -0.5}, {20.0, 30.0, 5.0, -5.0},
            {2.0, 3.0, -0.5, 0.5}});
  }

  @Test
  void matrixMinOpExecuteCorrectly() {
    verifyMatrixMatrixToMatrixEquals(makeTwoInputsInstruction(TOp.MATRIX_MIN_OP),
        new double[][] {{0.5, 2.2, -2.2, 2.2}, {0.5, -2.2, 2.2, 2.2}, {5.0, 22.0, -22.0, 22.0},
            {0.05, 0.22, -0.22, 0.22}},
        new double[][] {{2.2, 0.5, -0.5, -0.5}, {2.2, -0.5, 0.5, -0.5}, {22.0, 5.0, -5.0, -5.0},
            {0.22, 0.05, -0.05, -0.05}},
        new double[][] {{0.5, 0.5, -2.2, -0.5}, {0.5, -2.2, 0.5, -0.5}, {5.0, 5.0, -22.0, -5.0},
            {0.05, 0.05, -0.22, -0.05}});
  }

  @Test
  void matrixMaxOpExecuteCorrectly() {
    verifyMatrixMatrixToMatrixEquals(makeTwoInputsInstruction(TOp.MATRIX_MAX_OP),
        new double[][] {{0.5, 2.2, -2.2, 0.5}, {0.5, -2.2, 2.2, 0.5}, {5.0, 22.0, -22.0, 5.0},
            {0.05, 0.22, -0.22, 0.05}},
        new double[][] {{2.2, 0.5, -0.5, -2.2}, {2.2, -0.5, 0.5, -2.2}, {22.0, 5.0, -5.0, -22.0},
            {0.22, 0.05, -0.05, -0.22}},
        new double[][] {{2.2, 2.2, -0.5, 0.5}, {2.2, -0.5, 2.2, 0.5}, {22.0, 22.0, -5.0, 5.0},
            {0.22, 0.22, -0.05, 0.05}});
  }

  @Test
  void matrixAbsOpExecuteCorrectly() {
    verifyMatrixToMatrixEquals(makeOneInputInstruction(TOp.MATRIX_ABS_OP),
        new double[][] {{0.5, 0.0, -2.2, 100.5}, {0.5, -2.2, 0.0, 100.5}, {5.0, 0.0, -22.0, 1005.0},
            {0.05, 0.0, -0.22, 10.05}},
        new double[][] {{0.5, 0.0, 2.2, 100.5}, {0.5, 2.2, 0.0, 100.5}, {5.0, 0.0, 22.0, 1005.0},
            {0.05, 0.0, 0.22, 10.05}});
  }

  @Test
  void matrixHeavisideOpExecuteCorrectly() {
    verifyMatrixToMatrixEquals(makeOneInputInstruction(TOp.MATRIX_HEAVYSIDE_OP),
        new double[][] {{0.5, 0.1, -2.2, 100.5}, {0.5, -2.2, 0.0, 100.5}, {5.0, 1.0, -22.0, 1005.0},
            {0.05, 0.01, -0.22, 10.05}},
        new double[][] {{1.0, 1.0, 0.0, 1.0}, {1.0, 0.0, 0.0, 1.0}, {1.0, 1.0, 0.0, 1.0},
            {1.0, 1.0, 0.0, 1.0}});
  }

  @Test
  void matrixConstSetOpExecuteCorrectly() {
    double[][] expected = new double[4][4];
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        expected[i][j] = i == 2 && j == 1 ? -1.5 : kMemory.matrix[kOut].getValue(i, j);
      }
    }
    verifyNothingToMatrixEquals(makeZeroInputsInstruction(TOp.MATRIX_CONST_SET_OP, 2, 1, -1.5), expected);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Linear algebra-related instructions.
  ////////////////////////////////////////////////////////////////////////////

  @Test
  void scalarVectorProductOpExecuteCorrectly() {
    verifyScalarVectorToVectorEquals(makeTwoInputsInstruction(TOp.SCALAR_VECTOR_PRODUCT_OP), -0.2,
        new double[] {2.3, -0.3, 0.5, -0.001}, new double[] {-0.46, 0.06, -0.1, 0.0002});
  }

  @Test
  void vectorInnerProductOpExecuteCorrectly() {
    verifyVectorVectorToScalarEquals(makeTwoInputsInstruction(TOp.VECTOR_INNER_PRODUCT_OP),
        new double[] {-0.01, 0.0, 1.3, -0.001}, new double[] {2.3, 0.3, 0.5, -0.001}, 0.627001);
  }

  @Test
  void vectorOuterProductOpExecuteCorrectly() {
    verifyVectorVectorToMatrixEquals(makeTwoInputsInstruction(TOp.VECTOR_OUTER_PRODUCT_OP),
        new double[] {0.1, -0.1, 1.2, -10.0}, new double[] {2.3, -0.3, 0.5, -0.001},
        new double[][] {{0.23, -0.03, 0.05, -0.0001}, {-0.23, 0.03, -0.05, 0.0001},
            {2.76, -0.36, 0.6, -0.0012}, {-23, 3.0, -5.0, 0.01}});
  }

  @Test
  void scalarMatrixProductOpExecuteCorrectly() {
    verifyScalarMatrixToMatrixEquals(makeTwoInputsInstruction(TOp.SCALAR_MATRIX_PRODUCT_OP), 0.5,
        new double[][] {{2.2, 0.5, -0.5, -2.2}, {2.2, -0.5, 0.5, -2.2}, {22.0, 5.0, -5.0, -22.0},
            {0.22, 0.05, -0.05, -0.22}},
        new double[][] {{1.1, 0.25, -0.25, -1.1}, {1.1, -0.25, 0.25, -1.1},
            {11.0, 2.5, -2.5, -11.0}, {0.11, 0.025, -0.025, -0.11}});
  }

  @Test
  void matrixVectorProductOpExecuteCorrectly() {
    verifyMatrixVectorToVectorEquals(makeTwoInputsInstruction(TOp.MATRIX_VECTOR_PRODUCT_OP),
        new double[][] {{-0.2, 1.0, 0.03, 0.0}, {0.0, 0.8, -0.01, 0.0}, {2.0, 0.0, 2.0, 5.0},
            {-0.001, -0.1, 2.5, -3.2}},
        new double[] {0.1, -2.2, 10.0, 0.0}, new double[] {-1.92, -1.86, 20.2, 25.2199});
  }

  @Test
  void vectorNormOpExecuteCorrectly() {
    verifyVectorToScalarEquals(makeOneInputInstruction(TOp.VECTOR_NORM_OP),
        new double[] {2.2, -0.5, 0.0, 0.01}, 2.25612499654);
  }

  @Test
  void matrixNormOpExecuteCorrectly() {
    verifyMatrixToScalarEquals(
        makeOneInputInstruction(TOp.MATRIX_NORM_OP), new double[][] {{0.0, 0.5, -0.5, -2.2},
            {2.2, -0.5, 0.5, -2.2}, {22.0, 5.0, -5.0, -22.0}, {0.22, 0.05, -0.05, -0.22}},
        32.149989113528484);
  }

  @Test
  void matrixTransposeOpExecuteCorrectly() {
    verifyMatrixToMatrixEquals(makeOneInputInstruction(TOp.MATRIX_TRANSPOSE_OP),
        new double[][] {{0.0, 0.5, -0.5, -2.2}, {2.2, -0.5, 0.5, -2.2}, {22.0, 5.0, -5.0, -22.0},
            {0.22, 0.05, -0.05, -0.22}},
        new double[][] {{0.0, 2.2, 22.0, 0.22}, {0.5, -0.5, 5.0, 0.05}, {-0.5, 0.5, -5.0, -0.05},
            {-2.2, -2.2, -22.0, -0.22}});
  }

  @Test
  void matrixMatrixProductOpExecuteCorrectly() {
    verifyMatrixMatrixToMatrixEquals(makeTwoInputsInstruction(TOp.MATRIX_MATRIX_PRODUCT_OP),
        new double[][] {{0.1, 2.5, -0.5, -2.2}, {10.3, -0.06, 0.7, -2.1}, {22.0, 5.0, -5.0, -22.0},
            {0.4, 19.05, -0.05, -0.22}},
        new double[][] {{10.0, 2.0, -0.5, 0.003}, {10.3, 0.06, -7.3, -8.0},
            {-28.0, 0.076, 3.0, -32.0}, {0.4, -2.0, 0.08, -0.7}},
        new double[][] {{39.87, 4.712, -19.976, -2.4597}, {81.942, 24.8496, -2.78, -20.4191},
            {402.7, 87.92, -64.26, 135.466}, {201.527, 2.3792, -139.4326, -150.6448}});
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Probability-related instructions.
  ////////////////////////////////////////////////////////////////////////////////

  @Test
  void vectorMeanOpExecuteCorrectly() {
    verifyVectorToScalarEquals(makeOneInputInstruction(TOp.VECTOR_MEAN_OP),
        new double[] {-0.01, -0.2, 1.3, -0.001}, 0.27225);
  }

  @Test
  void vectorStDevOpExecuteCorrectly() {
    verifyVectorToScalarEquals(makeOneInputInstruction(TOp.VECTOR_ST_DEV_OP),
        new double[] {2.2, -0.5, 0.0, 0.01}, 1.04391989635);
  }

  @Test
  void matrixMeanOpExecuteCorrectly() {
    verifyMatrixToScalarEquals(makeOneInputInstruction(TOp.MATRIX_MEAN_OP),
        new double[][] {{0.1, 2.5, -0.5, -2.2}, {10.3, -0.06, 0.7, -2.1}, {22.0, 5.0, -5.0, -22.0},
            {0.4, 19.05, -0.05, -0.22}},
        1.745);
  }

  @Test
  void matrixStDevOpExecuteCorrectly() {
    verifyMatrixToScalarEquals(
        makeOneInputInstruction(TOp.MATRIX_ST_DEV_OP), new double[][] {{0.1, 2.5, -0.5, -2.2},
            {10.3, -0.06, 0.7, -2.1}, {22.0, 5.0, -5.0, -22.0}, {0.4, 19.05, -0.05, -0.22}},
        9.5352523563878488);
  }

  @Test
  void matrixRowMeanOpExecuteCorrectly() {
    verifyMatrixToVectorEquals(
        makeOneInputInstruction(TOp.MATRIX_ROW_MEAN_OP), new double[][] {{0.1, 2.5, -0.5, -2.2},
            {10.3, -0.06, 0.7, -2.1}, {22.0, 5.0, -5.0, -22.0}, {0.4, 19.05, -0.05, -0.22}},
        new double[] {-0.025, 2.21, 0.0, 4.795});
  }

  @Test
  void matrixRowStDevOpExecuteCorrectly() {
    verifyMatrixToVectorEquals(makeOneInputInstruction(TOp.MATRIX_ROW_ST_DEV_OP),
        new double[][] {{0.1, 2.5, -0.5, -2.2}, {10.3, -0.06, 0.7, -2.1}, {22.0, 5.0, -5.0, -22.0},
            {0.4, 19.05, -0.05, -0.22}},
        new double[] {1.68430252627, 4.78166289067, 15.9530561335, 8.23324510749});
  }

  @Test
  void scalarGaussianSetOpExecuteCorrectly() {
    verifyNothingToScalarIsRandomized(makeZeroInputsInstruction(TOp.SCALAR_GAUSSIAN_SET_OP, 20.0, 10.0));
  }

  @Test
  void vectorGaussianSetOpExecuteCorrectly() {
    verifyNothingToVectorIsRandomized(makeZeroInputsInstruction(TOp.VECTOR_GAUSSIAN_SET_OP, 20.0, 10.0));
  }

  @Test
  void matrixGaussianSetOpExecuteCorrectly() {
    verifyNothingToMatrixIsRandomized(makeZeroInputsInstruction(TOp.MATRIX_GAUSSIAN_SET_OP, 0.0, 0.1));
  }

  @Test
  void scalarUniformSetOpExecuteCorrectly() {
    verifyNothingToScalarIsRandomized(makeZeroInputsInstruction(TOp.SCALAR_UNIFORM_SET_OP, -2.5, 2.0));
  }

  @Test
  void vectorUniformSetOpExecuteCorrectly() {
    verifyNothingToVectorIsRandomized(makeZeroInputsInstruction(TOp.VECTOR_UNIFORM_SET_OP, -2.5, 2.0));
  }

  @Test
  void matrixUniformSetOpExecuteCorrectly() {
    verifyNothingToMatrixIsRandomized(makeZeroInputsInstruction(TOp.MATRIX_UNIFORM_SET_OP, -2.5, 2.0));
  }
}
