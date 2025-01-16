package instruction;

import jp.ac.titech.onolab.core.matrix.TCMatrix;
import memory.*;
import utils.*;

/**
 * 命令の実行を担うクラス、インスタンス化はされない
 */
class TInstructionExecutor {
  /**
   * 命令の実行をする関数
   *
   * @param instr 実行する命令
   * @param rand 処理に乱数を使う場合に用いる乱数生成器
   * @param memory 実行時に使うメモリー
   */
  static void execute(final TInstruction instr, final TRandomGenerator rand, final TMemory memory) {
    switch (instr.getOp()) {
      case NO_OP: // = 0
        executeNoOp(instr, rand, memory);
        break;
      case SCALAR_SUM_OP: // = 1
        executeScalarSumOp(instr, rand, memory);
        break;
      case SCALAR_DIFF_OP: // = 2
        executeScalarDiffOp(instr, rand, memory);
        break;
      case SCALAR_PRODUCT_OP: // = 3
        executeScalarProductOp(instr, rand, memory);
        break;
      case SCALAR_DIVISION_OP: // = 4
        executeScalarDivisionOp(instr, rand, memory);
        break;
      case SCALAR_ABS_OP: // = 5
        executeScalarAbsOp(instr, rand, memory);
        break;
      case SCALAR_RECIPROCAL_OP: // = 6
        executeScalarReciprocalOp(instr, rand, memory);
        break;
      case SCALAR_SIN_OP: // = 7
        executeScalarSinOp(instr, rand, memory);
        break;
      case SCALAR_COS_OP: // = 8
        executeScalarCosOp(instr, rand, memory);
        break;
      case SCALAR_TAN_OP: // = 9
        executeScalarTanOp(instr, rand, memory);
        break;
      case SCALAR_ARCSIN_OP: // = 10
        executeScalarArcSinOp(instr, rand, memory);
        break;
      case SCALAR_ARCCOS_OP: // = 11
        executeScalarArcCosOp(instr, rand, memory);
        break;
      case SCALAR_ARCTAN_OP: // = 12
        executeScalarArcTanOp(instr, rand, memory);
        break;
      case SCALAR_EXP_OP: // = 13
        executeScalarExpOp(instr, rand, memory);
        break;
      case SCALAR_LOG_OP: // = 14
        executeScalarLogOp(instr, rand, memory);
        break;
      case SCALAR_HEAVYSIDE_OP: // = 15
        executeScalarHeavisideOp(instr, rand, memory);
        break;
      case VECTOR_HEAVYSIDE_OP: // = 16
        executeVectorHeavisideOp(instr, rand, memory);
        break;
      case MATRIX_HEAVYSIDE_OP: // = 17
        executeMatrixHeavisideOp(instr, rand, memory);
        break;
      case SCALAR_VECTOR_PRODUCT_OP: // = 18
        executeScalarVectorProductOp(instr, rand, memory);
        break;
      case SCALAR_BROADCAST_OP: // = 19
        executeScalarBroadcastOp(instr, rand, memory);
        break;
      case VECTOR_RECIPROCAL_OP: // = 20
        executeVectorReciprocalOp(instr, rand, memory);
        break;
      case VECTOR_NORM_OP: // = 21
        executeVectorNormOp(instr, rand, memory);
        break;
      case VECTOR_ABS_OP: // = 22
        executeVectorAbsOp(instr, rand, memory);
        break;
      case VECTOR_SUM_OP: // = 23
        executeVectorSumOp(instr, rand, memory);
        break;
      case VECTOR_DIFF_OP: // = 24
        executeVectorDiffOp(instr, rand, memory);
        break;
      case VECTOR_PRODUCT_OP: // = 25
        executeVectorProductOp(instr, rand, memory);
        break;
      case VECTOR_DIVISION_OP: // = 26
        executeVectorDivisionOp(instr, rand, memory);
        break;
      case VECTOR_INNER_PRODUCT_OP: // = 27
        executeVectorInnerProductOp(instr, rand, memory);
        break;
      case VECTOR_OUTER_PRODUCT_OP: // = 28
        executeVectorOuterProductOp(instr, rand, memory);
        break;
      case SCALAR_MATRIX_PRODUCT_OP: // = 29
        executeScalarMatrixProductOp(instr, rand, memory);
        break;
      case MATRIX_RECIPROCAL_OP: // = 30
        executeMatrixReciprocalOp(instr, rand, memory);
        break;
      case MATRIX_VECTOR_PRODUCT_OP: // = 31
        executeMatrixVectorProductOp(instr, rand, memory);
        break;
      case VECTOR_COLUMN_BROADCAST_OP: // = 32
        executeVectorColumnBroadcastOp(instr, rand, memory);
        break;
      case VECTOR_ROW_BROADCAST_OP: // = 33
        executeVectorRowBroadcastOp(instr, rand, memory);
        break;
      case MATRIX_NORM_OP: // = 34
        executeMatrixNormOp(instr, rand, memory);
        break;
      case MATRIX_COLUMN_NORM_OP: // = 35
        executeMatrixColumnNormOp(instr, rand, memory);
        break;
      case MATRIX_ROW_NORM_OP: // = 36
        executeMatrixRowNormOp(instr, rand, memory);
        break;
      case MATRIX_TRANSPOSE_OP: // = 37
        executeMatrixTransposeOp(instr, rand, memory);
        break;
      case MATRIX_ABS_OP: // = 38
        executeMatrixAbsOp(instr, rand, memory);
        break;
      case MATRIX_SUM_OP: // = 39
        executeMatrixSumOp(instr, rand, memory);
        break;
      case MATRIX_DIFF_OP: // = 40
        executeMatrixDiffOp(instr, rand, memory);
        break;
      case MATRIX_PRODUCT_OP: // = 41
        executeMatrixProductOp(instr, rand, memory);
        break;
      case MATRIX_DIVISION_OP: // = 42
        executeMatrixDivisionOp(instr, rand, memory);
        break;
      case MATRIX_MATRIX_PRODUCT_OP: // = 43
        executeMatrixMatrixProductOp(instr, rand, memory);
        break;
      case SCALAR_MIN_OP: // = 44
        executeScalarMinOp(instr, rand, memory);
        break;
      case VECTOR_MIN_OP: // = 45
        executeVectorMinOp(instr, rand, memory);
        break;
      case MATRIX_MIN_OP: // = 46
        executeMatrixMinOp(instr, rand, memory);
        break;
      case SCALAR_MAX_OP: // = 47
        executeScalarMaxOp(instr, rand, memory);
        break;
      case VECTOR_MAX_OP: // = 48
        executeVectorMaxOp(instr, rand, memory);
        break;
      case MATRIX_MAX_OP: // = 49
        executeMatrixMaxOp(instr, rand, memory);
        break;
      case VECTOR_MEAN_OP: // = 50
        executeVectorMeanOp(instr, rand, memory);
        break;
      case MATRIX_MEAN_OP: // = 51
        executeMatrixMeanOp(instr, rand, memory);
        break;
      case MATRIX_ROW_MEAN_OP: // = 52
        executeMatrixRowMeanOp(instr, rand, memory);
        break;
      case MATRIX_ROW_ST_DEV_OP: // = 53
        executeMatrixRowStDevOp(instr, rand, memory);
        break;
      case VECTOR_ST_DEV_OP: // = 54
        executeVectorStDevOp(instr, rand, memory);
        break;
      case MATRIX_ST_DEV_OP: // = 55
        executeMatrixStDevOp(instr, rand, memory);
        break;
      case SCALAR_CONST_SET_OP: // = 56
        executeScalarConstSetOp(instr, rand, memory);
        break;
      case VECTOR_CONST_SET_OP: // = 57
        executeVectorConstSetOp(instr, rand, memory);
        break;
      case MATRIX_CONST_SET_OP: // = 58
        executeMatrixConstSetOp(instr, rand, memory);
        break;
      case SCALAR_UNIFORM_SET_OP: // = 59
        executeScalarUniformSetOp(instr, rand, memory);
        break;
      case VECTOR_UNIFORM_SET_OP: // = 60
        executeVectorUniformSetOp(instr, rand, memory);
        break;
      case MATRIX_UNIFORM_SET_OP: // = 61
        executeMatrixUniformSetOp(instr, rand, memory);
        break;
      case SCALAR_GAUSSIAN_SET_OP: // = 62
        executeScalarGaussianSetOp(instr, rand, memory);
        break;
      case VECTOR_GAUSSIAN_SET_OP: // = 63
        executeVectorGaussianSetOp(instr, rand, memory);
        break;
      case MATRIX_GAUSSIAN_SET_OP: // = 64
        executeMatrixGaussianSetOp(instr, rand, memory);
        break;
      default:
        executeUnsupportedOp(instr, rand, memory);
        break;
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Scalar arithmetic-related instructions.
  ////////////////////////////////////////////////////////////////////////////////

  static private void executeScalarSumOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    memory.scalar[instr.getOut()] = memory.scalar[instr.getIn1()] + memory.scalar[instr.getIn2()];
  }

  static private void executeScalarDiffOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    memory.scalar[instr.getOut()] = memory.scalar[instr.getIn1()] - memory.scalar[instr.getIn2()];
  }

  static private void executeScalarProductOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    memory.scalar[instr.getOut()] = memory.scalar[instr.getIn1()] * memory.scalar[instr.getIn2()];
  }

  static private void executeScalarDivisionOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    memory.scalar[instr.getOut()] = memory.scalar[instr.getIn1()] / memory.scalar[instr.getIn2()];
  }

  static private void executeScalarMinOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    memory.scalar[instr.getOut()] =
        Math.min(memory.scalar[instr.getIn1()], memory.scalar[instr.getIn2()]);
  }

  static private void executeScalarMaxOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    memory.scalar[instr.getOut()] =
        Math.max(memory.scalar[instr.getIn1()], memory.scalar[instr.getIn2()]);
  }

  static private void executeScalarAbsOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    memory.scalar[instr.getOut()] = Math.abs(memory.scalar[instr.getIn1()]);
  }

  static private void executeScalarHeavisideOp(final TInstruction instr,
      final TRandomGenerator rand, final TMemory memory) {
    memory.scalar[instr.getOut()] = memory.scalar[instr.getIn1()] > 0.0 ? 1.0 : 0.0;
  }

  static private void executeScalarConstSetOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    memory.scalar[instr.getOut()] = instr.getDoubleData1();
  }

  static private void executeScalarReciprocalOp(final TInstruction instr,
      final TRandomGenerator rand, final TMemory memory) {
    memory.scalar[instr.getOut()] = 1.0 / memory.scalar[instr.getIn1()];
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Trigonometry-related instructions.
  ////////////////////////////////////////////////////////////////////////////////

  static private void executeScalarSinOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    memory.scalar[instr.getOut()] = Math.sin(memory.scalar[instr.getIn1()]);
  }

  static private void executeScalarCosOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    memory.scalar[instr.getOut()] = Math.cos(memory.scalar[instr.getIn1()]);
  }

  static private void executeScalarTanOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    memory.scalar[instr.getOut()] = Math.tan(memory.scalar[instr.getIn1()]);
  }

  static private void executeScalarArcSinOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    memory.scalar[instr.getOut()] = Math.asin(memory.scalar[instr.getIn1()]);
  }

  static private void executeScalarArcCosOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    memory.scalar[instr.getOut()] = Math.acos(memory.scalar[instr.getIn1()]);
  }

  static private void executeScalarArcTanOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    memory.scalar[instr.getOut()] = Math.atan(memory.scalar[instr.getIn1()]);
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Calculus-related instructions.
  ////////////////////////////////////////////////////////////////////////////////

  static private void executeScalarExpOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    memory.scalar[instr.getOut()] = Math.exp(memory.scalar[instr.getIn1()]);
  }

  static private void executeScalarLogOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    memory.scalar[instr.getOut()] = Math.log(memory.scalar[instr.getIn1()]);
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Vector arithmetic-related instructions.
  ////////////////////////////////////////////////////////////////////////////////

  static private void executeVectorSumOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    TCMatrix out = memory.vector[instr.getOut()];
    out.add(memory.vector[instr.getIn1()], memory.vector[instr.getIn2()]);
  }

  static private void executeVectorDiffOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    TCMatrix out = memory.vector[instr.getOut()];
    out.sub(memory.vector[instr.getIn1()], memory.vector[instr.getIn2()]);
  }

  static private void executeVectorProductOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    TCMatrix out = memory.vector[instr.getOut()];
    out.timesElement(memory.vector[instr.getIn1()], memory.vector[instr.getIn2()]);
  }

  static private void executeVectorDivisionOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    TCMatrix out = memory.vector[instr.getOut()];
    out.divElement(memory.vector[instr.getIn1()], memory.vector[instr.getIn2()]);
  }

  static private void executeVectorMinOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    TCMatrix out = memory.vector[instr.getOut()];
    for (int i = 0; i < out.getDimension(); i++) {
      double in1 = memory.vector[instr.getIn1()].getValue(i);
      double in2 = memory.vector[instr.getIn2()].getValue(i);
      out.setValue(i, Math.min(in1, in2));
    }
  }

  static private void executeVectorMaxOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    TCMatrix out = memory.vector[instr.getOut()];
    for (int i = 0; i < out.getDimension(); i++) {
      double in1 = memory.vector[instr.getIn1()].getValue(i);
      double in2 = memory.vector[instr.getIn2()].getValue(i);
      out.setValue(i, Math.max(in1, in2));
    }
  }

  static private void executeVectorAbsOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    TCMatrix out = memory.vector[instr.getOut()];
    for (int i = 0; i < out.getDimension(); i++) {
      double in1 = memory.vector[instr.getIn1()].getValue(i);
      out.setValue(i, Math.abs(in1));
    }
  }

  static private void executeVectorHeavisideOp(final TInstruction instr,
      final TRandomGenerator rand, final TMemory memory) {
    TCMatrix out = memory.vector[instr.getOut()];
    for (int i = 0; i < out.getDimension(); i++) {
      double in1 = memory.vector[instr.getIn1()].getValue(i);
      out.setValue(i, in1 > 0.0 ? 1.0 : 0.0);
    }
  }

  static private void executeVectorConstSetOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    TCMatrix out = memory.vector[instr.getOut()];
    int index = instr.getIndex1();
    if (out.getRowDimension() <= index) {
      return;
    }
    out.setValue(index, instr.getDoubleData1());
  }

  static private void executeVectorReciprocalOp(final TInstruction instr,
      final TRandomGenerator rand, final TMemory memory) {
    TCMatrix out = memory.vector[instr.getOut()];
    for (int i = 0; i < out.getDimension(); i++) {
      double in1 = memory.vector[instr.getIn1()].getValue(i);
      out.setValue(i, 1.0 / in1);
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Matrix arithmetic-related instructions.
  ////////////////////////////////////////////////////////////////////////////////

  static private void executeMatrixSumOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    TCMatrix out = memory.matrix[instr.getOut()];
    out.add(memory.matrix[instr.getIn1()], memory.matrix[instr.getIn2()]);
  }

  static private void executeMatrixDiffOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    TCMatrix out = memory.matrix[instr.getOut()];
    out.sub(memory.matrix[instr.getIn1()], memory.matrix[instr.getIn2()]);
  }

  static private void executeMatrixProductOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    TCMatrix out = memory.matrix[instr.getOut()];
    out.timesElement(memory.matrix[instr.getIn1()], memory.matrix[instr.getIn2()]);
  }

  static private void executeMatrixDivisionOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    TCMatrix out = memory.matrix[instr.getOut()];
    out.divElement(memory.matrix[instr.getIn1()], memory.matrix[instr.getIn2()]);
  }

  static private void executeMatrixMinOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    TCMatrix out = memory.matrix[instr.getOut()];
    for (int i = 0; i < out.getDimension(); i++) {
      double in1 = memory.matrix[instr.getIn1()].getValue(i);
      double in2 = memory.matrix[instr.getIn2()].getValue(i);
      out.setValue(i, Math.min(in1, in2));
    }
  }

  static private void executeMatrixMaxOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    TCMatrix out = memory.matrix[instr.getOut()];
    for (int i = 0; i < out.getDimension(); i++) {
      double in1 = memory.matrix[instr.getIn1()].getValue(i);
      double in2 = memory.matrix[instr.getIn2()].getValue(i);
      out.setValue(i, Math.max(in1, in2));
    }
  }

  static private void executeMatrixAbsOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    TCMatrix out = memory.matrix[instr.getOut()];
    for (int i = 0; i < out.getDimension(); i++) {
      double in1 = memory.matrix[instr.getIn1()].getValue(i);
      out.setValue(i, Math.abs(in1));
    }
  }

  static private void executeMatrixHeavisideOp(final TInstruction instr,
      final TRandomGenerator rand, final TMemory memory) {
    TCMatrix out = memory.matrix[instr.getOut()];
    for (int i = 0; i < out.getDimension(); i++) {
      double in1 = memory.matrix[instr.getIn1()].getValue(i);
      out.setValue(i, in1 > 0.0 ? 1.0 : 0.0);
    }
  }

  static private void executeMatrixConstSetOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    TCMatrix out = memory.matrix[instr.getOut()];
    int row = instr.getIndex1();
    int col = instr.getIndex2();
    if(out.getRowDimension() <= row || out.getColumnDimension() <= col) {
      return;
    }
    out.setValue(row, col, instr.getDoubleData1());
  }

  static private void executeMatrixReciprocalOp(final TInstruction instr,
      final TRandomGenerator rand, final TMemory memory) {
    TCMatrix out = memory.matrix[instr.getOut()];
    for (int i = 0; i < out.getDimension(); i++) {
      double in1 = memory.matrix[instr.getIn1()].getValue(i);
      out.setValue(i, 1.0 / in1);
    }
  }

  ////////////////////////////////////////////////////////////////////////////
  // Linear algebra-related instructions.
  ////////////////////////////////////////////////////////////////////////////

  static private void executeScalarVectorProductOp(final TInstruction instr,
      final TRandomGenerator rand, final TMemory memory) {
    TCMatrix out = memory.vector[instr.getOut()];
    if (memory.scalar[instr.getIn1()] == 0.0) {
      out.fill(0.0);
    } else {
      out.times(memory.vector[instr.getIn2()], memory.scalar[instr.getIn1()]);
    }
  }

  static private void executeVectorInnerProductOp(final TInstruction instr,
      final TRandomGenerator rand, final TMemory memory) {
    memory.scalar[instr.getOut()] =
        memory.vector[instr.getIn1()].innerProduct(memory.vector[instr.getIn2()]);
  }

  static private void executeVectorOuterProductOp(final TInstruction instr,
      final TRandomGenerator rand, final TMemory memory) {
    memory.matrix[instr.getOut()].times(memory.vector[instr.getIn1()],
        memory.vector[instr.getIn2()].tclone());
  }

  static private void executeScalarMatrixProductOp(final TInstruction instr,
      final TRandomGenerator rand, final TMemory memory) {
    TCMatrix out = memory.matrix[instr.getOut()];
    out.times(memory.matrix[instr.getIn2()], memory.scalar[instr.getIn1()]);
  }

  static private void executeMatrixVectorProductOp(final TInstruction instr,
      final TRandomGenerator rand, final TMemory memory) {
    int in1 = instr.getIn1();
    int in2 = instr.getIn2();
    int out = instr.getOut();
    if (out != in2) {
      memory.vector[out].times(memory.matrix[in1], memory.vector[in2]);
    } else {
      memory.vector[out] =
          new TCMatrix(memory.getDim()).times(memory.matrix[in1], memory.vector[in2]);
    }
  }

  static private void executeVectorNormOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    memory.scalar[instr.getOut()] = memory.vector[instr.getIn1()].normL2();
  }

  static private void executeMatrixNormOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    memory.scalar[instr.getOut()] = memory.matrix[instr.getIn1()].normF();
  }

  static private void executeMatrixRowNormOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    TCMatrix out = memory.vector[instr.getOut()];
    for (int i = 0; i < out.getDimension(); i++) {
      TCMatrix row = memory.matrix[instr.getIn1()].cloneRowVector(i);
      out.setValue(i, row.normL2());
    }
  }

  static private void executeMatrixColumnNormOp(final TInstruction instr,
      final TRandomGenerator rand, final TMemory memory) {
    TCMatrix out = memory.vector[instr.getOut()];
    for (int i = 0; i < out.getDimension(); i++) {
      TCMatrix row = memory.matrix[instr.getIn1()].cloneColumnVector(i);
      out.setValue(i, row.normL2());
    }
  }

  static private void executeMatrixTransposeOp(final TInstruction instr,
      final TRandomGenerator rand, final TMemory memory) {
    int in1 = instr.getIn1();
    int out = instr.getOut();
    if (in1 != out) {
      memory.matrix[out].tcopyFrom(memory.matrix[in1]);
    } else {
      memory.matrix[out] =
          new TCMatrix(memory.getDim(), memory.getDim()).tcopyFrom(memory.matrix[in1]);
    }
  }

  static private void executeMatrixMatrixProductOp(final TInstruction instr,
      final TRandomGenerator rand, final TMemory memory) {
    int in1 = instr.getIn1();
    int in2 = instr.getIn2();
    int out = instr.getOut();
    if (out != in1 && out != in2) {
      memory.matrix[out].times(memory.matrix[in1], memory.matrix[in2]);
    } else {
      memory.matrix[out] = new TCMatrix(memory.getDim(), memory.getDim()).times(memory.matrix[in1],
          memory.matrix[in2]);
    }
  }

  static private void executeScalarBroadcastOp(final TInstruction instr,
      final TRandomGenerator rand, final TMemory memory) {
    memory.vector[instr.getOut()].fill(memory.scalar[instr.getIn1()]);
  }

  static private void executeVectorColumnBroadcastOp(final TInstruction instr,
      final TRandomGenerator rand, final TMemory memory) {
    TCMatrix out = memory.matrix[instr.getOut()];
    for (int row = 0; row < memory.getDim(); row++) {
      for (int col = 0; col < memory.getDim(); col++) {
        double val = memory.vector[instr.getIn1()].getValue(row);
        out.setValue(row, col, val);
      }
    }
  }

  static private void executeVectorRowBroadcastOp(final TInstruction instr,
      final TRandomGenerator rand, final TMemory memory) {
    TCMatrix out = memory.matrix[instr.getOut()];
    for (int row = 0; row < memory.getDim(); row++) {
      for (int col = 0; col < memory.getDim(); col++) {
        double val = memory.vector[instr.getIn1()].getValue(col);
        out.setValue(row, col, val);
      }
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Probability-related instructions.
  ////////////////////////////////////////////////////////////////////////////////

  static private void executeVectorMeanOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    TCMatrix vector = memory.vector[instr.getIn1()];
    memory.scalar[instr.getOut()] = TMathUtility.mean(vector);
  }

  static private void executeVectorStDevOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    TCMatrix vector = memory.vector[instr.getIn1()];
    memory.scalar[instr.getOut()] = TMathUtility.stdev(vector);
  }

  static private void executeMatrixMeanOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    TCMatrix matrix = memory.matrix[instr.getIn1()];
    memory.scalar[instr.getOut()] = TMathUtility.mean(matrix);
  }

  static private void executeMatrixStDevOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    TCMatrix matrix = memory.matrix[instr.getIn1()];
    memory.scalar[instr.getOut()] = TMathUtility.stdev(matrix);
  }

  static private void executeMatrixRowMeanOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    TCMatrix out = memory.vector[instr.getOut()];
    for (int i = 0; i < out.getDimension(); i++) {
      TCMatrix row = memory.matrix[instr.getIn1()].cloneRowVector(i);
      double mean = TMathUtility.mean(row);
      out.setValue(i, mean);
    }
  }

  static private void executeMatrixRowStDevOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    TCMatrix out = memory.vector[instr.getOut()];
    for (int i = 0; i < out.getDimension(); i++) {
      TCMatrix row = memory.matrix[instr.getIn1()].cloneRowVector(i);
      double stdev = TMathUtility.stdev(row);
      out.setValue(i, stdev);
    }
  }

  static private void executeScalarGaussianSetOp(final TInstruction instr,
      final TRandomGenerator rand, final TMemory memory) {
    memory.scalar[instr.getOut()] = rand.nextGaussian(instr.getDoubleData1(), instr.getDoubleData2());
  }

  static private void executeVectorGaussianSetOp(final TInstruction instr,
      final TRandomGenerator rand, final TMemory memory) {
    TCMatrix out = memory.vector[instr.getOut()];
    rand.fillGaussian(instr.getDoubleData1(), instr.getDoubleData2(), out);
  }

  static private void executeMatrixGaussianSetOp(final TInstruction instr,
      final TRandomGenerator rand, final TMemory memory) {
    TCMatrix out = memory.matrix[instr.getOut()];
    rand.fillGaussian(instr.getDoubleData1(), instr.getDoubleData2(), out);
  }

  static private void executeScalarUniformSetOp(final TInstruction instr,
      final TRandomGenerator rand, final TMemory memory) {
    memory.scalar[instr.getOut()] = rand.nextDouble(instr.getDoubleData1(), instr.getDoubleData2());
  }

  static private void executeVectorUniformSetOp(final TInstruction instr,
      final TRandomGenerator rand, final TMemory memory) {
    TCMatrix out = memory.vector[instr.getOut()];
    rand.fillDouble(instr.getDoubleData1(), instr.getDoubleData2(), out);
  }

  static private void executeMatrixUniformSetOp(final TInstruction instr,
      final TRandomGenerator rand, final TMemory memory) {
    TCMatrix out = memory.matrix[instr.getOut()];
    rand.fillDouble(instr.getDoubleData1(), instr.getDoubleData2(), out);
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Other instructions.
  ////////////////////////////////////////////////////////////////////////////////

  static private void executeNoOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {}

  static private void executeUnsupportedOp(final TInstruction instr, final TRandomGenerator rand,
      final TMemory memory) {
    throw new RuntimeException("Unsupported op.");
  }
}
