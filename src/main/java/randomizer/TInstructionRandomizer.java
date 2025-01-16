package randomizer;

import algorithm.core.*;
import instruction.*;
import memory.*;
import utils.TRandomGenerator;

/**
 * 命令のパラメータを書き換える際に用いるクラス．インスタンス化はされない．
 */
public class TInstructionRandomizer {

  static int randomInAddress(final TMemory memory, final TAlgorithmComponentType componentType,
      final TMemoryType memoryType, final TRandomGenerator rand) {
    switch (memoryType) {
      case SCALAR: {
        switch (componentType) {
          case SETUP:
            return rand.nextInt(memory.scalarFirstInAddressOfSetup, memory.getNumOfScalarAddresses());
          case PREDICT:
            return rand.nextInt(memory.scalarFirstInAddressOfPredict, memory.getNumOfScalarAddresses());
          case LEARN:
            return rand.nextInt(memory.scalarFirstInAddressOfLearn, memory.getNumOfScalarAddresses());
          default:
            throw new RuntimeException("Invalid component type");
        }
      }
      case VECTOR:
        return rand.nextInt(memory.vectorFirstInAddress, memory.getNumOfVectorAddresses());
      case MATRIX:
        return rand.nextInt(memory.matrixFirstInAddress, memory.getNumOfMatrixAddresses());
      default:
        throw new RuntimeException("Invalid memory type");
    }
  }

  static int randomOutAddress(final TMemory memory, final TAlgorithmComponentType componentType, final TMemoryType memoryType, final TRandomGenerator rand) {
    switch (memoryType) {
      case SCALAR: {
        switch (componentType) {
          case SETUP:
            return rand.nextInt(memory.scalarFirstOutAddressOfSetup, memory.getNumOfScalarAddresses());
          case PREDICT:
            return rand.nextInt(memory.scalarFirstOutAddressOfPredict, memory.getNumOfScalarAddresses());
          case LEARN:
            return rand.nextInt(memory.scalarFirstOutAddressOfLearn, memory.getNumOfScalarAddresses());
          default:
            throw new RuntimeException("Invalid component type");
        }
      }
      case VECTOR:
        return rand.nextInt(memory.vectorFirstOutAddress, memory.getNumOfVectorAddresses());
      case MATRIX:
        return rand.nextInt(memory.matrixFirstOutAddress, memory.getNumOfMatrixAddresses());
      default:
        throw new RuntimeException("Invalid memory type");
    }
  }

  /**
   * 命令をコピーしてパラメータをランダムに書き換える関数
   */
  static public TInstruction copyInstructionAndAlterParam(final TInstruction other, final TAlgorithmComponentType componentType,
      final TMemory memory, final TRandomGenerator rand) {
    final TInstruction instruction = new TInstruction();
    instruction.copyFrom(other);
    alterParam(instruction, componentType, memory, rand);
    return instruction;
  }

  /**
   * 指定された命令でパラメータをランダムに初期化した命令を生成する関数
   */
  static public TInstruction makeInstructionAndRandomize(final TOp op, final TAlgorithmComponentType componentType,
      final TMemory memory, final TRandomGenerator rand) {
    final TInstruction instruction = new TInstruction();
    setOpAndRandomize(instruction, op, componentType, memory, rand);
    return instruction;
  }


  /**
   * 指定された命令をセットして，パラメータをランダムに初期化する関数
   */
  static public void setOpAndRandomize(final TInstruction instruction, final TOp op,
      final TAlgorithmComponentType componentType, final TMemory memory, final TRandomGenerator rand) {
    instruction.fillWithNoOp();
    instruction.setOp(op);
    switch (op) {
      case NO_OP:
        return;
      case SCALAR_CONST_SET_OP:
      case VECTOR_CONST_SET_OP:
      case MATRIX_CONST_SET_OP:
      case SCALAR_GAUSSIAN_SET_OP:
      case VECTOR_GAUSSIAN_SET_OP:
      case MATRIX_GAUSSIAN_SET_OP:
      case SCALAR_UNIFORM_SET_OP:
      case VECTOR_UNIFORM_SET_OP:
      case MATRIX_UNIFORM_SET_OP:
        randomizeOut(instruction, componentType, memory, rand);
        randomizeData(instruction, memory, rand);
        return;
      case SCALAR_ABS_OP:
      case SCALAR_HEAVYSIDE_OP:
      case SCALAR_SIN_OP:
      case SCALAR_COS_OP:
      case SCALAR_TAN_OP:
      case SCALAR_ARCSIN_OP:
      case SCALAR_ARCCOS_OP:
      case SCALAR_ARCTAN_OP:
      case SCALAR_EXP_OP:
      case SCALAR_LOG_OP:
      case SCALAR_RECIPROCAL_OP:
      case SCALAR_BROADCAST_OP:
      case VECTOR_ABS_OP:
      case VECTOR_HEAVYSIDE_OP:
      case VECTOR_RECIPROCAL_OP:
      case MATRIX_RECIPROCAL_OP:
      case MATRIX_ROW_NORM_OP:
      case MATRIX_COLUMN_NORM_OP:
      case VECTOR_COLUMN_BROADCAST_OP:
      case VECTOR_ROW_BROADCAST_OP:
      case MATRIX_ABS_OP:
      case MATRIX_HEAVYSIDE_OP:
      case VECTOR_NORM_OP:
      case MATRIX_NORM_OP:
      case MATRIX_TRANSPOSE_OP:
      case VECTOR_MEAN_OP:
      case VECTOR_ST_DEV_OP:
      case MATRIX_MEAN_OP:
      case MATRIX_ST_DEV_OP:
      case MATRIX_ROW_MEAN_OP:
      case MATRIX_ROW_ST_DEV_OP:
        randomizeIn1(instruction, componentType, memory, rand);
        randomizeOut(instruction, componentType, memory, rand);
        return;
      case SCALAR_SUM_OP:
      case SCALAR_DIFF_OP:
      case SCALAR_PRODUCT_OP:
      case SCALAR_DIVISION_OP:
      case SCALAR_MIN_OP:
      case SCALAR_MAX_OP:
      case VECTOR_SUM_OP:
      case VECTOR_DIFF_OP:
      case VECTOR_PRODUCT_OP:
      case VECTOR_DIVISION_OP:
      case VECTOR_MIN_OP:
      case VECTOR_MAX_OP:
      case MATRIX_SUM_OP:
      case MATRIX_DIFF_OP:
      case MATRIX_PRODUCT_OP:
      case MATRIX_DIVISION_OP:
      case MATRIX_MIN_OP:
      case MATRIX_MAX_OP:
      case SCALAR_VECTOR_PRODUCT_OP:
      case VECTOR_INNER_PRODUCT_OP:
      case VECTOR_OUTER_PRODUCT_OP:
      case SCALAR_MATRIX_PRODUCT_OP:
      case MATRIX_VECTOR_PRODUCT_OP:
      case MATRIX_MATRIX_PRODUCT_OP:
        randomizeIn1(instruction, componentType, memory, rand);
        randomizeIn2(instruction, componentType, memory, rand);
        randomizeOut(instruction, componentType, memory, rand);
        return;
      default:
        throw new RuntimeException("invalid op.");
    }
  }

  /**
   * １つのパラメータをランダムに選択して，ランダムに値をセットする関数
   */
  static public void alterParam(final TInstruction instruction, final TAlgorithmComponentType componentType,
      final TMemory memory, final TRandomGenerator rand) {
    switch (instruction.getOp()) {
      case NO_OP:
        return;
      case SCALAR_CONST_SET_OP:
      case VECTOR_CONST_SET_OP:
      case MATRIX_CONST_SET_OP:
      case SCALAR_GAUSSIAN_SET_OP:
      case VECTOR_GAUSSIAN_SET_OP:
      case MATRIX_GAUSSIAN_SET_OP:
      case SCALAR_UNIFORM_SET_OP:
      case VECTOR_UNIFORM_SET_OP:
      case MATRIX_UNIFORM_SET_OP:
        switch (rand.nextChoice2()) {
          case 0:
            randomizeOut(instruction, componentType, memory, rand);
            return;
          case 1:
            alterData(instruction, memory, rand);
            return;
          default:
            throw new RuntimeException("invalid choice.");
        }
      case SCALAR_ABS_OP:
      case SCALAR_HEAVYSIDE_OP:
      case SCALAR_SIN_OP:
      case SCALAR_COS_OP:
      case SCALAR_TAN_OP:
      case SCALAR_ARCSIN_OP:
      case SCALAR_ARCCOS_OP:
      case SCALAR_ARCTAN_OP:
      case SCALAR_EXP_OP:
      case SCALAR_LOG_OP:
      case SCALAR_RECIPROCAL_OP:
      case SCALAR_BROADCAST_OP:
      case VECTOR_RECIPROCAL_OP:
      case MATRIX_RECIPROCAL_OP:
      case MATRIX_ROW_NORM_OP:
      case MATRIX_COLUMN_NORM_OP:
      case VECTOR_COLUMN_BROADCAST_OP:
      case VECTOR_ROW_BROADCAST_OP:
      case VECTOR_ABS_OP:
      case VECTOR_HEAVYSIDE_OP:
      case MATRIX_ABS_OP:
      case MATRIX_HEAVYSIDE_OP:
      case VECTOR_NORM_OP:
      case MATRIX_NORM_OP:
      case MATRIX_TRANSPOSE_OP:
      case VECTOR_MEAN_OP:
      case VECTOR_ST_DEV_OP:
      case MATRIX_MEAN_OP:
      case MATRIX_ST_DEV_OP:
      case MATRIX_ROW_MEAN_OP:
      case MATRIX_ROW_ST_DEV_OP:
        switch (rand.nextChoice2()) {
          case 0:
            randomizeIn1(instruction, componentType, memory, rand);
            return;
          case 1:
            randomizeOut(instruction, componentType, memory, rand);
            return;
          default:
            throw new RuntimeException("invalid choice.");
        }
      case SCALAR_SUM_OP:
      case SCALAR_DIFF_OP:
      case SCALAR_PRODUCT_OP:
      case SCALAR_DIVISION_OP:
      case SCALAR_MIN_OP:
      case SCALAR_MAX_OP:
      case VECTOR_SUM_OP:
      case VECTOR_DIFF_OP:
      case VECTOR_PRODUCT_OP:
      case VECTOR_DIVISION_OP:
      case VECTOR_MIN_OP:
      case VECTOR_MAX_OP:
      case MATRIX_SUM_OP:
      case MATRIX_DIFF_OP:
      case MATRIX_PRODUCT_OP:
      case MATRIX_DIVISION_OP:
      case MATRIX_MIN_OP:
      case MATRIX_MAX_OP:
      case SCALAR_VECTOR_PRODUCT_OP:
      case VECTOR_INNER_PRODUCT_OP:
      case VECTOR_OUTER_PRODUCT_OP:
      case SCALAR_MATRIX_PRODUCT_OP:
      case MATRIX_VECTOR_PRODUCT_OP:
      case MATRIX_MATRIX_PRODUCT_OP:
        switch (rand.nextChoice3()) {
          case 0:
            randomizeIn1(instruction, componentType, memory, rand);
            return;
          case 1:
            randomizeIn2(instruction, componentType, memory, rand);
            return;
          case 2:
            randomizeOut(instruction, componentType, memory, rand);
            return;
          default:
            throw new RuntimeException("invalid choice.");
        }
      default:
        throw new RuntimeException("invalid op.");
    }
  }

  /**
   * In1をランダムに初期化する関数
   */
  static public void randomizeIn1(final TInstruction instruction, final TAlgorithmComponentType componentType,
      final TMemory memory, final TRandomGenerator rand) {
    TOp op = instruction.getOp();
    TMemoryType memoryType = op.getIn1MemoryType();
    instruction.setIn1(randomInAddress(memory, componentType, memoryType, rand));
  }

  /**
   * In2をランダムに初期化する関数
   */
  static public void randomizeIn2(final TInstruction instruction, final TAlgorithmComponentType componentType,
      final TMemory memory, final TRandomGenerator rand) {
    TOp op = instruction.getOp();
    TMemoryType memoryType = op.getIn2MemoryType();
    instruction.setIn2(randomInAddress(memory, componentType, memoryType, rand));
  }

  /**
   * Outをランダムに初期化する関数
   */
  static public void randomizeOut(final TInstruction instruction, final TAlgorithmComponentType componentType,
      final TMemory memory, final TRandomGenerator rand) {
    TOp op = instruction.getOp();
    TMemoryType memoryType = op.getOutMemoryType();
    instruction.setOut(randomOutAddress(memory, componentType, memoryType, rand));
  }

  /**
   * Dataをランダムに初期化する関数
   */
  static public void randomizeData(final TInstruction instruction,
      final TMemory memory, final TRandomGenerator rand) {
    randomizeData(instruction, memory, rand, true);
  }

  /**
   * 対応可能な命令の場合にDataをランダムに初期化する関数
   */
  static public void maybeRandomizeData(final TInstruction instruction,
      final TMemory memory, final TRandomGenerator rand) {
    randomizeData(instruction, memory, rand, false);
  }

  /**
   * Dataをランダムに初期化する関数, throwErrorをfalseに設定すると対応していない命令はスルーする
   */
  static private void randomizeData(final TInstruction instruction,
      final TMemory memory, final TRandomGenerator rand, boolean throwError) {
    switch (instruction.getOp()) {
      case NO_OP:
      case SCALAR_SUM_OP:
      case SCALAR_DIFF_OP:
      case SCALAR_PRODUCT_OP:
      case SCALAR_DIVISION_OP:
      case SCALAR_MIN_OP:
      case SCALAR_MAX_OP:
      case SCALAR_ABS_OP:
      case SCALAR_HEAVYSIDE_OP:
      case SCALAR_SIN_OP:
      case SCALAR_COS_OP:
      case SCALAR_TAN_OP:
      case SCALAR_ARCSIN_OP:
      case SCALAR_ARCCOS_OP:
      case SCALAR_ARCTAN_OP:
      case SCALAR_EXP_OP:
      case SCALAR_LOG_OP:
      case SCALAR_RECIPROCAL_OP:
      case SCALAR_BROADCAST_OP:
      case VECTOR_RECIPROCAL_OP:
      case MATRIX_RECIPROCAL_OP:
      case MATRIX_ROW_NORM_OP:
      case MATRIX_COLUMN_NORM_OP:
      case VECTOR_COLUMN_BROADCAST_OP:
      case VECTOR_ROW_BROADCAST_OP:
      case VECTOR_SUM_OP:
      case VECTOR_DIFF_OP:
      case VECTOR_PRODUCT_OP:
      case VECTOR_DIVISION_OP:
      case VECTOR_MIN_OP:
      case VECTOR_MAX_OP:
      case VECTOR_ABS_OP:
      case VECTOR_HEAVYSIDE_OP:
      case MATRIX_SUM_OP:
      case MATRIX_DIFF_OP:
      case MATRIX_PRODUCT_OP:
      case MATRIX_DIVISION_OP:
      case MATRIX_MIN_OP:
      case MATRIX_MAX_OP:
      case MATRIX_ABS_OP:
      case MATRIX_HEAVYSIDE_OP:
      case SCALAR_VECTOR_PRODUCT_OP:
      case VECTOR_INNER_PRODUCT_OP:
      case VECTOR_OUTER_PRODUCT_OP:
      case SCALAR_MATRIX_PRODUCT_OP:
      case MATRIX_VECTOR_PRODUCT_OP:
      case VECTOR_NORM_OP:
      case MATRIX_NORM_OP:
      case MATRIX_TRANSPOSE_OP:
      case MATRIX_MATRIX_PRODUCT_OP:
      case VECTOR_MEAN_OP:
      case VECTOR_ST_DEV_OP:
      case MATRIX_MEAN_OP:
      case MATRIX_ST_DEV_OP:
      case MATRIX_ROW_MEAN_OP:
      case MATRIX_ROW_ST_DEV_OP:
        if (throwError)
          throw new RuntimeException("Invalid TOp.");
        else
          return;
      case SCALAR_CONST_SET_OP: {
        instruction.setDoubleData1(rand.nextDouble(-1.0, 1.0));
        return;
      }
      case VECTOR_CONST_SET_OP: {
        instruction.setIndex(rand.nextInt(memory.getDim()));
        instruction.setDoubleData1(rand.nextDouble(-1.0, 1.0));
        return;
      }
      case MATRIX_CONST_SET_OP: {
        instruction.setIndex(rand.nextInt(memory.getDim()), rand.nextInt(memory.getDim()));
        instruction.setDoubleData1(rand.nextDouble(-1.0, 1.0));
        return;
      }
      case SCALAR_GAUSSIAN_SET_OP:
      case VECTOR_GAUSSIAN_SET_OP:
      case MATRIX_GAUSSIAN_SET_OP: {
        instruction.setDoubleData1(rand.nextDouble(-1.0, 1.0)); // Mean.
        instruction.setDoubleData2(rand.nextDouble(0.0, 1.0));// St. dev.
        return;
      }
      case SCALAR_UNIFORM_SET_OP:
      case VECTOR_UNIFORM_SET_OP:
      case MATRIX_UNIFORM_SET_OP: {
        instruction.setDoubleData1(rand.nextDouble(-1.0, 1.0)); // Mean.
        instruction.setDoubleData2(rand.nextDouble(-1.0, 1.0)); // St. dev
        return;
      }
      default:
        if (throwError)
          throw new RuntimeException("Invalid TOp.");
        else
          return;
    }
  }

  static private double mutateActivationLogScale(final TRandomGenerator rand, final double value) {
    if (value > 0) {
      return Math.exp(Math.log(value) + rand.nextGaussian(0.0, 1.0));
    } else {
      return -Math.exp(Math.log(-value) + rand.nextGaussian(0.0, 1.0));
    }
  }

  static private double mutateFloatLogScale(final TRandomGenerator rand, final double value) {
    if (value > 0) {
      return Math.exp(Math.log(value) + rand.nextGaussian(0.0, 1.0));
    } else {
      return -Math.exp(Math.log(-value) + rand.nextGaussian(0.0, 1.0));
    }
  }

  static private final double kSignFlipProb = 0.1;

  static private double mutateActivationLogScaleOrFlip(final TRandomGenerator rand, double value) {
    if (rand.nextProbability() < kSignFlipProb) {
      value = -value;
    } else {
      value = mutateActivationLogScale(rand, value);
    }
    return value;
  }

 static private double mutateFloatLogScaleOrFlip(final TRandomGenerator rand, double value) {
    if (rand.nextProbability() < kSignFlipProb) {
      value = -value;
    } else {
      value = mutateFloatLogScale(rand, value);
    }
    return value;
  }

  /**
   * Dataの一部をランダムに変更する関数
   */
  static public void alterData(final TInstruction instruction,
      final TMemory memory, final TRandomGenerator rand) {
    switch (instruction.getOp()) {
      case NO_OP:
      case SCALAR_SUM_OP:
      case SCALAR_DIFF_OP:
      case SCALAR_PRODUCT_OP:
      case SCALAR_DIVISION_OP:
      case SCALAR_MIN_OP:
      case SCALAR_MAX_OP:
      case SCALAR_ABS_OP:
      case SCALAR_HEAVYSIDE_OP:
      case SCALAR_SIN_OP:
      case SCALAR_COS_OP:
      case SCALAR_TAN_OP:
      case SCALAR_ARCSIN_OP:
      case SCALAR_ARCCOS_OP:
      case SCALAR_ARCTAN_OP:
      case SCALAR_EXP_OP:
      case SCALAR_LOG_OP:
      case VECTOR_SUM_OP:
      case VECTOR_DIFF_OP:
      case VECTOR_PRODUCT_OP:
      case VECTOR_DIVISION_OP:
      case VECTOR_MIN_OP:
      case VECTOR_MAX_OP:
      case VECTOR_ABS_OP:
      case VECTOR_HEAVYSIDE_OP:
      case MATRIX_SUM_OP:
      case MATRIX_DIFF_OP:
      case MATRIX_PRODUCT_OP:
      case MATRIX_DIVISION_OP:
      case MATRIX_MIN_OP:
      case MATRIX_MAX_OP:
      case MATRIX_ABS_OP:
      case MATRIX_HEAVYSIDE_OP:
      case SCALAR_VECTOR_PRODUCT_OP:
      case VECTOR_INNER_PRODUCT_OP:
      case VECTOR_OUTER_PRODUCT_OP:
      case SCALAR_MATRIX_PRODUCT_OP:
      case MATRIX_VECTOR_PRODUCT_OP:
      case VECTOR_NORM_OP:
      case MATRIX_NORM_OP:
      case MATRIX_TRANSPOSE_OP:
      case MATRIX_MATRIX_PRODUCT_OP:
      case VECTOR_MEAN_OP:
      case VECTOR_ST_DEV_OP:
      case MATRIX_MEAN_OP:
      case MATRIX_ST_DEV_OP:
      case MATRIX_ROW_MEAN_OP:
      case MATRIX_ROW_ST_DEV_OP:
      case SCALAR_RECIPROCAL_OP:
      case SCALAR_BROADCAST_OP:
      case VECTOR_RECIPROCAL_OP:
      case MATRIX_RECIPROCAL_OP:
      case MATRIX_ROW_NORM_OP:
      case MATRIX_COLUMN_NORM_OP:
      case VECTOR_COLUMN_BROADCAST_OP:
      case VECTOR_ROW_BROADCAST_OP:
        throw new RuntimeException("Invalid TOp.");
      case SCALAR_CONST_SET_OP:
        instruction.setDoubleData1(
            mutateActivationLogScaleOrFlip(rand, instruction.getDoubleData1()));
        return;
      case VECTOR_CONST_SET_OP:
        switch (rand.nextChoice2()) {
          case 0:
            instruction.setIndex(rand.nextInt(memory.getDim()));
            break;
          case 1:
            instruction.setDoubleData1(
                mutateActivationLogScaleOrFlip(rand, instruction.getDoubleData1()));
            break;
        }
        return;
      case MATRIX_CONST_SET_OP:
        switch (rand.nextChoice3()) {
          case 0:
            instruction.setIndex1(rand.nextInt(memory.getDim()));
            break;
          case 1:
            // Mutate second index.
            instruction.setIndex2(rand.nextInt(memory.getDim()));
            break;
          case 2:
            // Mutate value.
            instruction.setDoubleData1(mutateFloatLogScaleOrFlip(rand, instruction.getDoubleData1()));
            break;
        }
        return;

      case SCALAR_GAUSSIAN_SET_OP:
      case VECTOR_GAUSSIAN_SET_OP:
      case MATRIX_GAUSSIAN_SET_OP:
        switch (rand.nextChoice2()) {
          case 0:
            // Mutate mean.
            instruction.setDoubleData1(mutateFloatLogScaleOrFlip(rand, instruction.getDoubleData1()));
            break;
          case 1:
            // Mutate stdev.
            instruction.setDoubleData2(mutateFloatLogScale(rand, instruction.getDoubleData2()));
            break;
        }
        return;
      case SCALAR_UNIFORM_SET_OP:
      case VECTOR_UNIFORM_SET_OP:
      case MATRIX_UNIFORM_SET_OP:
        double value1 = instruction.getDoubleData1();
        double value2 = instruction.getDoubleData2();
        switch (rand.nextChoice2()) {
          case 0:
            // Mutate low.
            value1 = mutateFloatLogScaleOrFlip(rand, value1);
            break;
          case 1:
            // Mutate high.
            value2 = mutateFloatLogScaleOrFlip(rand, value2);
            break;
        }
        instruction.setDoubleData1(Math.min(value1, value2));
        instruction.setDoubleData2(Math.max(value1, value2));
        return;
      default:
        throw new RuntimeException("invalid op.");
    }
  }

  /**
   * Dataの一部をランダムに変更する関数
   */
  static public void randomizeDataExceptIndex(final TInstruction instruction, final TRandomGenerator rand, boolean initialize) {
    switch (instruction.getOp()) {
      case NO_OP:
      case SCALAR_SUM_OP:
      case SCALAR_DIFF_OP:
      case SCALAR_PRODUCT_OP:
      case SCALAR_DIVISION_OP:
      case SCALAR_MIN_OP:
      case SCALAR_MAX_OP:
      case SCALAR_ABS_OP:
      case SCALAR_HEAVYSIDE_OP:
      case SCALAR_SIN_OP:
      case SCALAR_COS_OP:
      case SCALAR_TAN_OP:
      case SCALAR_ARCSIN_OP:
      case SCALAR_ARCCOS_OP:
      case SCALAR_ARCTAN_OP:
      case SCALAR_EXP_OP:
      case SCALAR_LOG_OP:
      case VECTOR_SUM_OP:
      case VECTOR_DIFF_OP:
      case VECTOR_PRODUCT_OP:
      case VECTOR_DIVISION_OP:
      case VECTOR_MIN_OP:
      case VECTOR_MAX_OP:
      case VECTOR_ABS_OP:
      case VECTOR_HEAVYSIDE_OP:
      case MATRIX_SUM_OP:
      case MATRIX_DIFF_OP:
      case MATRIX_PRODUCT_OP:
      case MATRIX_DIVISION_OP:
      case MATRIX_MIN_OP:
      case MATRIX_MAX_OP:
      case MATRIX_ABS_OP:
      case MATRIX_HEAVYSIDE_OP:
      case SCALAR_VECTOR_PRODUCT_OP:
      case VECTOR_INNER_PRODUCT_OP:
      case VECTOR_OUTER_PRODUCT_OP:
      case SCALAR_MATRIX_PRODUCT_OP:
      case MATRIX_VECTOR_PRODUCT_OP:
      case VECTOR_NORM_OP:
      case MATRIX_NORM_OP:
      case MATRIX_TRANSPOSE_OP:
      case MATRIX_MATRIX_PRODUCT_OP:
      case VECTOR_MEAN_OP:
      case VECTOR_ST_DEV_OP:
      case MATRIX_MEAN_OP:
      case MATRIX_ST_DEV_OP:
      case MATRIX_ROW_MEAN_OP:
      case MATRIX_ROW_ST_DEV_OP:
      case SCALAR_RECIPROCAL_OP:
      case SCALAR_BROADCAST_OP:
      case VECTOR_RECIPROCAL_OP:
      case MATRIX_RECIPROCAL_OP:
      case MATRIX_ROW_NORM_OP:
      case MATRIX_COLUMN_NORM_OP:
      case VECTOR_COLUMN_BROADCAST_OP:
      case VECTOR_ROW_BROADCAST_OP:
        throw new RuntimeException("Invalid TOp.");
      case SCALAR_CONST_SET_OP:
        instruction.setDoubleData1(
            initialize ? rand.nextDouble(-1.0, 1.0) : mutateActivationLogScaleOrFlip(rand, instruction.getDoubleData1()));
        return;
      case VECTOR_CONST_SET_OP:
        // Mutate value.
        instruction.setDoubleData1(initialize ? rand.nextFloat(-1.0f, 1.0f) : mutateFloatLogScaleOrFlip(rand, instruction.getDoubleData1()));
        return;
      case MATRIX_CONST_SET_OP:
        instruction.setDoubleData1(initialize ? rand.nextFloat(-1.0f, 1.0f) : mutateFloatLogScaleOrFlip(rand, instruction.getDoubleData1()));
        return;

      case SCALAR_GAUSSIAN_SET_OP:
      case VECTOR_GAUSSIAN_SET_OP:
      case MATRIX_GAUSSIAN_SET_OP:
        switch (rand.nextChoice2()) {
          case 0:
            // Mutate mean.
            instruction.setDoubleData1(initialize ? rand.nextDouble(-1.0, 1.0)
                : mutateFloatLogScaleOrFlip(rand, instruction.getDoubleData1()));
            break;
          case 1:
            // Mutate stdev.
            instruction.setDoubleData2(initialize ? rand.nextDouble(-1.0, 1.0)
                : mutateFloatLogScale(rand, instruction.getDoubleData2()));
            break;
        }
        return;
      case SCALAR_UNIFORM_SET_OP:
      case VECTOR_UNIFORM_SET_OP:
      case MATRIX_UNIFORM_SET_OP:
        double value1 = instruction.getDoubleData1();
        double value2 = instruction.getDoubleData2();
        switch (rand.nextChoice2()) {
          case 0:
            // Mutate low.
            value1 = mutateFloatLogScaleOrFlip(rand, value1);
            break;
          case 1:
            // Mutate high.
            value2 = mutateFloatLogScaleOrFlip(rand, value2);
            break;
        }
        instruction.setDoubleData(
            Math.min(value1, value2),
            Math.max(value1, value2)
        );
        return;
      default:
        throw new RuntimeException("invalid op.");
    }
  }
}
