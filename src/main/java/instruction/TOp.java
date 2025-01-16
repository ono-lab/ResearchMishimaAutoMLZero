package instruction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import memory.TMemoryType;

/**
 * 命令の種類
 */
public enum TOp {
  NO_OP(),

  SCALAR_SUM_OP(),

  SCALAR_DIFF_OP(),

  SCALAR_PRODUCT_OP(),

  SCALAR_DIVISION_OP(),

  SCALAR_ABS_OP(),

  SCALAR_RECIPROCAL_OP(),

  SCALAR_SIN_OP(),

  SCALAR_COS_OP(),

  SCALAR_TAN_OP(),

  SCALAR_ARCSIN_OP(),

  SCALAR_ARCCOS_OP(),

  SCALAR_ARCTAN_OP(),

  SCALAR_EXP_OP(),

  SCALAR_LOG_OP(),

  SCALAR_HEAVYSIDE_OP(),

  VECTOR_HEAVYSIDE_OP(),

  MATRIX_HEAVYSIDE_OP(),

  SCALAR_VECTOR_PRODUCT_OP(),

  SCALAR_BROADCAST_OP(),

  VECTOR_RECIPROCAL_OP(),

  VECTOR_NORM_OP(),

  VECTOR_ABS_OP(),

  VECTOR_SUM_OP(),

  VECTOR_DIFF_OP(),

  VECTOR_PRODUCT_OP(),

  VECTOR_DIVISION_OP(),

  VECTOR_INNER_PRODUCT_OP(),

  VECTOR_OUTER_PRODUCT_OP(),

  SCALAR_MATRIX_PRODUCT_OP(),

  MATRIX_RECIPROCAL_OP(),

  MATRIX_VECTOR_PRODUCT_OP(),

  VECTOR_COLUMN_BROADCAST_OP(),

  VECTOR_ROW_BROADCAST_OP(),

  MATRIX_NORM_OP(),

  MATRIX_COLUMN_NORM_OP(),

  MATRIX_ROW_NORM_OP(),

  MATRIX_TRANSPOSE_OP(),

  MATRIX_ABS_OP(),

  MATRIX_SUM_OP(),

  MATRIX_DIFF_OP(),

  MATRIX_PRODUCT_OP(),

  MATRIX_DIVISION_OP(),

  MATRIX_MATRIX_PRODUCT_OP(),

  SCALAR_MIN_OP(),

  VECTOR_MIN_OP(),

  MATRIX_MIN_OP(),

  SCALAR_MAX_OP(),

  VECTOR_MAX_OP(),

  MATRIX_MAX_OP(),

  VECTOR_MEAN_OP(),

  MATRIX_MEAN_OP(),

  MATRIX_ROW_MEAN_OP(),

  MATRIX_ROW_ST_DEV_OP(),

  VECTOR_ST_DEV_OP(),

  MATRIX_ST_DEV_OP(),

  SCALAR_CONST_SET_OP(),

  VECTOR_CONST_SET_OP(),

  MATRIX_CONST_SET_OP(),

  SCALAR_UNIFORM_SET_OP(),

  VECTOR_UNIFORM_SET_OP(),

  MATRIX_UNIFORM_SET_OP(),

  SCALAR_GAUSSIAN_SET_OP(),

  VECTOR_GAUSSIAN_SET_OP(),

  MATRIX_GAUSSIAN_SET_OP();

  @JsonCreator(mode = Mode.DELEGATING)
  public static TOp of(String name) {
    for (var instance : values()) {
      if (instance.name().contentEquals(name))
        return instance;
    }
    throw new IllegalArgumentException();
  }

  /**
   * 入力数の取得
   */
  public int getInNum() {
    if (this.getIn1MemoryType() == null)
      return 0;
    if (this.getIn2MemoryType() == null)
      return 1;
    return 2;
  }

  /**
   * 入力1のメモリの種類の取得
   */
  public TMemoryType getIn1MemoryType() {
    switch (this) {
      case NO_OP:
      case SCALAR_CONST_SET_OP:
      case VECTOR_CONST_SET_OP:
      case MATRIX_CONST_SET_OP:
      case SCALAR_GAUSSIAN_SET_OP:
      case VECTOR_GAUSSIAN_SET_OP:
      case MATRIX_GAUSSIAN_SET_OP:
      case SCALAR_UNIFORM_SET_OP:
      case VECTOR_UNIFORM_SET_OP:
      case MATRIX_UNIFORM_SET_OP:
        return null;
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
      case SCALAR_VECTOR_PRODUCT_OP:
      case SCALAR_MATRIX_PRODUCT_OP:
        return TMemoryType.SCALAR;
      case VECTOR_SUM_OP:
      case VECTOR_DIFF_OP:
      case VECTOR_PRODUCT_OP:
      case VECTOR_DIVISION_OP:
      case VECTOR_MIN_OP:
      case VECTOR_MAX_OP:
      case VECTOR_ABS_OP:
      case VECTOR_HEAVYSIDE_OP:
      case VECTOR_INNER_PRODUCT_OP:
      case VECTOR_OUTER_PRODUCT_OP:
      case VECTOR_NORM_OP:
      case VECTOR_MEAN_OP:
      case VECTOR_ST_DEV_OP:
      case VECTOR_RECIPROCAL_OP:
      case VECTOR_COLUMN_BROADCAST_OP:
      case VECTOR_ROW_BROADCAST_OP:
        return TMemoryType.VECTOR;
      case MATRIX_SUM_OP:
      case MATRIX_DIFF_OP:
      case MATRIX_PRODUCT_OP:
      case MATRIX_DIVISION_OP:
      case MATRIX_MIN_OP:
      case MATRIX_MAX_OP:
      case MATRIX_ABS_OP:
      case MATRIX_HEAVYSIDE_OP:
      case MATRIX_VECTOR_PRODUCT_OP:
      case MATRIX_NORM_OP:
      case MATRIX_TRANSPOSE_OP:
      case MATRIX_MATRIX_PRODUCT_OP:
      case MATRIX_MEAN_OP:
      case MATRIX_ST_DEV_OP:
      case MATRIX_ROW_MEAN_OP:
      case MATRIX_ROW_ST_DEV_OP:
      case MATRIX_RECIPROCAL_OP:
      case MATRIX_ROW_NORM_OP:
      case MATRIX_COLUMN_NORM_OP:
        return TMemoryType.MATRIX;
      default:
        throw new RuntimeException("invalid op.");
    }
  }

  /**
   * 入力2のメモリの種類の取得
   */
  public TMemoryType getIn2MemoryType() {
    switch (this) {
      case NO_OP:
      case SCALAR_ABS_OP:
      case SCALAR_HEAVYSIDE_OP:
      case SCALAR_CONST_SET_OP:
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
      case VECTOR_CONST_SET_OP:
      case MATRIX_ABS_OP:
      case MATRIX_HEAVYSIDE_OP:
      case MATRIX_CONST_SET_OP:
      case VECTOR_NORM_OP:
      case MATRIX_NORM_OP:
      case MATRIX_TRANSPOSE_OP:
      case VECTOR_MEAN_OP:
      case VECTOR_ST_DEV_OP:
      case MATRIX_MEAN_OP:
      case MATRIX_ST_DEV_OP:
      case MATRIX_ROW_MEAN_OP:
      case MATRIX_ROW_ST_DEV_OP:
      case SCALAR_GAUSSIAN_SET_OP:
      case VECTOR_GAUSSIAN_SET_OP:
      case MATRIX_GAUSSIAN_SET_OP:
      case SCALAR_UNIFORM_SET_OP:
      case VECTOR_UNIFORM_SET_OP:
      case MATRIX_UNIFORM_SET_OP:
        return null;
      case SCALAR_SUM_OP:
      case SCALAR_DIFF_OP:
      case SCALAR_PRODUCT_OP:
      case SCALAR_DIVISION_OP:
      case SCALAR_MIN_OP:
      case SCALAR_MAX_OP:
        return TMemoryType.SCALAR;
      case VECTOR_SUM_OP:
      case VECTOR_DIFF_OP:
      case VECTOR_PRODUCT_OP:
      case VECTOR_DIVISION_OP:
      case VECTOR_MIN_OP:
      case VECTOR_MAX_OP:
      case SCALAR_VECTOR_PRODUCT_OP:
      case VECTOR_INNER_PRODUCT_OP:
      case VECTOR_OUTER_PRODUCT_OP:
      case MATRIX_VECTOR_PRODUCT_OP:
        return TMemoryType.VECTOR;
      case MATRIX_SUM_OP:
      case MATRIX_DIFF_OP:
      case MATRIX_PRODUCT_OP:
      case MATRIX_DIVISION_OP:
      case MATRIX_MIN_OP:
      case MATRIX_MAX_OP:
      case SCALAR_MATRIX_PRODUCT_OP:
      case MATRIX_MATRIX_PRODUCT_OP:
        return TMemoryType.MATRIX;
      default:
        throw new RuntimeException("invalid op.");
    }
  }

  /**
   * 出力のメモリの種類の取得
   */
  public TMemoryType getOutMemoryType() {
    switch (this) {
      case NO_OP:
        return null;
      case SCALAR_SUM_OP:
      case SCALAR_DIFF_OP:
      case SCALAR_PRODUCT_OP:
      case SCALAR_DIVISION_OP:
      case SCALAR_MIN_OP:
      case SCALAR_MAX_OP:
      case SCALAR_ABS_OP:
      case SCALAR_HEAVYSIDE_OP:
      case SCALAR_CONST_SET_OP:
      case SCALAR_SIN_OP:
      case SCALAR_COS_OP:
      case SCALAR_TAN_OP:
      case SCALAR_ARCSIN_OP:
      case SCALAR_ARCCOS_OP:
      case SCALAR_ARCTAN_OP:
      case SCALAR_EXP_OP:
      case SCALAR_LOG_OP:
      case SCALAR_RECIPROCAL_OP:
      case VECTOR_INNER_PRODUCT_OP:
      case VECTOR_NORM_OP:
      case MATRIX_NORM_OP:
      case VECTOR_MEAN_OP:
      case VECTOR_ST_DEV_OP:
      case MATRIX_MEAN_OP:
      case MATRIX_ST_DEV_OP:
      case SCALAR_GAUSSIAN_SET_OP:
      case SCALAR_UNIFORM_SET_OP:
        return TMemoryType.SCALAR;
      case VECTOR_SUM_OP:
      case VECTOR_DIFF_OP:
      case VECTOR_PRODUCT_OP:
      case VECTOR_DIVISION_OP:
      case VECTOR_MIN_OP:
      case VECTOR_MAX_OP:
      case VECTOR_ABS_OP:
      case VECTOR_HEAVYSIDE_OP:
      case SCALAR_VECTOR_PRODUCT_OP:
      case MATRIX_VECTOR_PRODUCT_OP:
      case MATRIX_ROW_MEAN_OP:
      case MATRIX_ROW_ST_DEV_OP:
      case VECTOR_GAUSSIAN_SET_OP:
      case VECTOR_UNIFORM_SET_OP:
      case SCALAR_BROADCAST_OP:
      case VECTOR_RECIPROCAL_OP:
      case MATRIX_ROW_NORM_OP:
      case MATRIX_COLUMN_NORM_OP:
      case VECTOR_CONST_SET_OP:
        return TMemoryType.VECTOR;
      case MATRIX_SUM_OP:
      case MATRIX_DIFF_OP:
      case MATRIX_PRODUCT_OP:
      case MATRIX_DIVISION_OP:
      case MATRIX_MIN_OP:
      case MATRIX_MAX_OP:
      case MATRIX_ABS_OP:
      case MATRIX_HEAVYSIDE_OP:
      case VECTOR_OUTER_PRODUCT_OP:
      case SCALAR_MATRIX_PRODUCT_OP:
      case MATRIX_TRANSPOSE_OP:
      case MATRIX_MATRIX_PRODUCT_OP:
      case MATRIX_GAUSSIAN_SET_OP:
      case MATRIX_UNIFORM_SET_OP:
      case MATRIX_RECIPROCAL_OP:
      case VECTOR_COLUMN_BROADCAST_OP:
      case VECTOR_ROW_BROADCAST_OP:
      case MATRIX_CONST_SET_OP:
        return TMemoryType.MATRIX;
      default:
        System.out.println(this);
        throw new RuntimeException("invalid op.");
    }
  }

  /**
   * 代入命令（入力を持たない命令）であるかどうかの判定
   */
  public boolean isAssignment() {
    switch (this) {
      case SCALAR_CONST_SET_OP:
      case VECTOR_CONST_SET_OP:
      case MATRIX_CONST_SET_OP:
      case SCALAR_GAUSSIAN_SET_OP:
      case VECTOR_GAUSSIAN_SET_OP:
      case MATRIX_GAUSSIAN_SET_OP:
      case SCALAR_UNIFORM_SET_OP:
      case VECTOR_UNIFORM_SET_OP:
      case MATRIX_UNIFORM_SET_OP:
        return true;
      default:
        return false;
    }
  }
}
