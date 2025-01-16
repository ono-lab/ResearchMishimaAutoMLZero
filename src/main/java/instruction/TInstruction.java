package instruction;

import java.util.ArrayList;
import memory.*;
import utils.TRandomGenerator;

/**
 * アルゴリズムの各関数内で使用する命令のクラス
 */
public class TInstruction {
  /** 命令の種類 */
  private TOp fOp = TOp.NO_OP;

  /** 1つ目の引数のアドレス */
  private int fIn1 = 0;

  /** 2つ目の引数のアドレス */
  private int fIn2 = 0;

  /** 出力先の引数のアドレス */
  private int fOut = 0;

  /** ベクトルや行列の特定の要素に値を設定する命令で使用するindex */
  private int fIndex1 = -1;

  /** 行列の特定の要素に値を設定する命令で使用するindex */
  private int fIndex2 = -1;

  /**
   * 定数が必要な入力に利用する変数1
   * 代入命令、乱数の生成命令など
  */
  private double fDoubleData1 = 0.0;

  /**
   * 定数が必要な入力に利用する変数2
   * 代入命令、乱数の生成命令など
   */
  private double fDoubleData2 = 0.0;

  /**
   * NO_OP命令で初期化するコンストラクタ
   */
  public TInstruction() {}

  /**
   * 1入力の命令で初期化するコンストラクタ
   */
  public TInstruction(final TOp op, final int in, final int out) {
    fOp = op;
    fIn1 = in;
    fOut = out;
  }

  /**
   * 2入力の命令で初期化するコンストラクタ
   */
  public TInstruction(final TOp op, final int in1, final int in2, final int out) {
    fOp = op;
    fIn1 = in1;
    fIn2 = in2;
    fOut = out;
  }

  /**
   * 1つの定数を使った命令で初期化するコンストラクタ
   * 例: SCALAR_CONST_SET_OP
   */
  public TInstruction(final TOp op, final int out, final double doubleData) {
    fOp = op;
    fOut = out;
    fDoubleData1 = doubleData;
  }

  /**
   * 2つの定数を使った命令で初期化するコンストラクタ
   * 例: SCALAR_GAUSSIAN_SET_OP, SCALAR_UNIFORM_SET_OP
   */
  public TInstruction(final TOp op, final int out,
      final double doubleData1, final double doubleData2) {
    fOp = op;
    fOut = out;
    fDoubleData1 = doubleData1;
    fDoubleData2 = doubleData2;
  }

  /**
   * 1つの定数と1つのインデックスを使った命令で初期化するコンストラクタ
   * 例: VECTOR_CONST_SET_OP
   */
  public TInstruction(final TOp op, final int out,
      final double data, final int index) {
    fOp = op;
    fOut = out;
    fDoubleData1 = data;
    fIndex1 = index;
  }

  /**
   * 1つの定数と2つのインデックスを使った命令で初期化するコンストラクタ
   * 例: MATRIX_CONST_SET_OP
   */
  public TInstruction(final TOp op, final int out,
      final double data, final int index1, final int index2) {
    fOp = op;
    fOut = out;
    fDoubleData1 = data;
    fIndex1 = index1;
    fIndex2 = index2;
  }

  /**
   * 他の命令から内容をコピーする関数
   */
  public TInstruction copyFrom(final TInstruction other) {
    fOp = other.fOp;
    fIn1 = other.fIn1;
    fIn2 = other.fIn2;
    fOut = other.fOut;
    fDoubleData1 = other.fDoubleData1;
    fDoubleData2 = other.fDoubleData2;
    fIndex1 = other.fIndex1;
    fIndex2 = other.fIndex2;
    return this;
  }

  /**
   * 他の命令からコピーするコンストラクタ
   */
  public TInstruction(final TInstruction other) {
    copyFrom(other);
  }

  /**
   * 命令の種類の取得
   */
  public TOp getOp() {
    return fOp;
  }

  /**
   * 命令の種類のセット
   */
  public void setOp(final TOp op) {
    fOp = op;
  }

  /**
   * 1つ目の入力のアドレスの取得
   */
  public int getIn1() {
    return fIn1;
  }

  /**
   * 1つ目の入力のアドレスのセット
   */
  public void setIn1(final int in1) {
    fIn1 = in1;
  }

  /**
   * 2つ目の入力のアドレスの取得
   */
  public int getIn2() {
    return fIn2;
  }

  /**
   * 2つ目の入力のアドレスのセット
   */
  public void setIn2(final int in2) {
    fIn2 = in2;
  }

  /**
   * 出力先アドレスの取得
   */
  public int getOut() {
    return fOut;
  }

  /**
   * 出力先アドレスのセット
   */
  public void setOut(final int out) {
    fOut = out;
  }

  /**
   * 定数データ1（double）の取得
   */
  public double getDoubleData1() {
    return fDoubleData1;
  }

  /**
   * 定数データ2（double）の取得
   */
  public double getDoubleData2() {
    return fDoubleData2;
  }

  /**
   * 定数データ1（double）のセット
   */
  public void setDoubleData1(final double data) {
    fDoubleData1 = data;
  }

  /**
   * 定数データ2（double）のセット
   */
  public void setDoubleData2(final double data) {
    fDoubleData2 = data;
  }

  /**
   * 2つの定数データを同時にセット
   */
  public void setDoubleData(final double data1, final double data2) {
    fDoubleData1 = data1;
    fDoubleData2 = data2;
  }

  /**
   * 1つ目のindexの取得
   */
  public int getIndex1() {
    return fIndex1;
  }

  /**
   * 2つ目のindexの取得
   */
  public int getIndex2() {
    return fIndex2;
  }

  /**
   * 1つ目のindexのセット
   */
  public void setIndex1(int index) {
    fIndex1 = index;
  }

  /**
   * 2つ目のindexのセット
   */
  public void setIndex2(int index) {
    fIndex2 = index;
  }

  /**
   * 1つ目のindexのセット
   */
  public void setIndex(int index) {
    fIndex1 = index;
  }

  /**
   * 2つのindexを同時にセット
   */
  public void setIndex(int index1, int index2) {
    fIndex1 = index1;
    fIndex2 = index2;
  }

  /**
   * 1つ目の入力を変数として取得
   */
  public TVariable getInput1Variable() {
    TMemoryType memoryType = getOp().getIn1MemoryType();
    if (memoryType == null)
      return null;
    return new TVariable(memoryType, getIn1());
  }

  /**
   * 2つ目の入力を変数として取得
   */
  public TVariable getInput2Variable() {
    TMemoryType memoryType = getOp().getIn2MemoryType();
    if (memoryType == null)
      return null;
    return new TVariable(memoryType, getIn2());
  }

  /**
   * 出力先の変数を取得
   */
  public TVariable getOutVariable() {
    TOp op = getOp();
    TMemoryType memoryType = op.getOutMemoryType();
    if (memoryType == null)
      return null;
    return new TVariable(memoryType, getOut());
  }

  /**
   * 命令にNO_OPをセットして初期化する関数
   */
  public void fillWithNoOp() {
    fOp = TOp.NO_OP;
    fIn1 = 0;
    fIn2 = 0;
    fOut = 0;
    fDoubleData1 = 0.0;
    fDoubleData2 = 0.0;
    fIndex1 = -1;
    fIndex2 = -1;
  }

  /**
   * 命令の実行を行う関数
   */
  public void execute(final TRandomGenerator rand, final TMemory memory) {
    TInstructionExecutor.execute(this, rand, memory);
  }

  /**
   * 命令列の複製
   * srcの内容をdestにコピーする関数
   */
  public static void copy(ArrayList<TInstruction> src, ArrayList<TInstruction> dest){
    dest.clear();
    for (TInstruction instr : src) {
      dest.add(instr.clone());
    }
  }

  @Override
  public TInstruction clone() {
    return new TInstruction(this);
  }

  static public final double kDoubleDataTolerance = 0.00001;

  @Override
  public boolean equals(final Object other) {
    if (other == this)
      return true;
    if (!(other instanceof TInstruction))
      return false;
    TInstruction otherInstr = (TInstruction) other;
    return fOp == otherInstr.fOp && fIn1 == otherInstr.fIn1 && fIn2 == otherInstr.fIn2
        && fOut == otherInstr.fOut
        && fIndex1 == otherInstr.fIndex1 && fIndex2 == otherInstr.fIndex2
        && Math.abs(fDoubleData1 - otherInstr.fDoubleData1) < kDoubleDataTolerance
        && Math.abs(fDoubleData2 - otherInstr.fDoubleData2) < kDoubleDataTolerance;
  }

  public String toString(int indent) {
    String space = "";
    for (int i = 0; i < indent; i++) {
      space += " ";
    }
    String in1 = fIn1 >= 0 ? fIn1 + "" : "[none]";
    String in2 = fIn2 >= 0 ? fIn2 + "" : "[none]";
    switch (fOp) {
      case NO_OP:
        return space + "NoOp()";
      case SCALAR_SUM_OP:
        return space + "s" + fOut + " = s" + in1 + " + s" + in2;
      case SCALAR_DIFF_OP:
        return space + "s" + fOut + " = s" + in1 + " - s" + in2;
      case SCALAR_PRODUCT_OP:
        return space + "s" + fOut + " = s" + in1 + " * s" + in2;
      case SCALAR_DIVISION_OP:
        return space + "s" + fOut + " = s" + in1 + " / s" + in2;
      case SCALAR_MIN_OP:
        return space + "s" + fOut + " = minimum(s" + in1 + ", s" + in2 + ")";
      case SCALAR_MAX_OP:
        return space + "s" + fOut + " = maximum(s" + in1 + ", s" + in2 + ")";
      case SCALAR_ABS_OP:
        return space + "s" + fOut + " = abs(s" + in1 + ")";
      case SCALAR_HEAVYSIDE_OP:
        return space + "s" + fOut + " = heaviside(s" + in1 + ", 1.0)";
      case SCALAR_CONST_SET_OP:
        return space + "s" + fOut + " = " + fDoubleData1;
      case SCALAR_SIN_OP:
        return space + "s" + fOut + " = sin(s" + in1 + ")";
      case SCALAR_COS_OP:
        return space + "s" + fOut + " = cos(s" + in1 + ")";
      case SCALAR_TAN_OP:
        return space + "s" + fOut + " = tan(s" + in1 + ")";
      case SCALAR_ARCSIN_OP:
        return space + "s" + fOut + " = arcsin(s" + in1 + ")";
      case SCALAR_ARCCOS_OP:
        return space + "s" + fOut + " = arccos(s" + in1 + ")";
      case SCALAR_ARCTAN_OP:
        return space + "s" + fOut + " = arctan(s" + in1 + ")";
      case SCALAR_EXP_OP:
        return space + "s" + fOut + " = exp(s" + in1 + ")";
      case SCALAR_LOG_OP:
        return space + "s" + fOut + " = log(s" + in1 + ")";
      case SCALAR_RECIPROCAL_OP:
        return space + "s" + fOut + " = 1 / s" + in1;
      case SCALAR_BROADCAST_OP:
        return space + "v" + fOut + " = bcast(s" + in1 + ")";
      case VECTOR_RECIPROCAL_OP:
        return space + "v" + fOut + " = 1 / v" + in1;
      case MATRIX_RECIPROCAL_OP:
        return space + "m" + fOut + " = 1 / m" + in1;
      case MATRIX_ROW_NORM_OP:
        return space + "v" + fOut + " = norm(m" + in1 + ", axis=1)";
      case MATRIX_COLUMN_NORM_OP:
        return space + "v" + fOut + " = norm(m" + in1 + ", axis=0)";
      case VECTOR_COLUMN_BROADCAST_OP:
        return space + "m" + fOut + " = bcast(v" + in1 + ", axis=0)";
      case VECTOR_ROW_BROADCAST_OP:
        return space + "m" + fOut + " = bcast(v" + in1 + ", axis=1)";
      case VECTOR_SUM_OP:
        return space + "v" + fOut + " = v" + in1 + " + v" + in2;
      case VECTOR_DIFF_OP:
        return space + "v" + fOut + " = v" + in1 + " - v" + in2;
      case VECTOR_PRODUCT_OP:
        return space + "v" + fOut + " = v" + in1 + " * v" + in2;
      case VECTOR_DIVISION_OP:
        return space + "v" + fOut + " = v" + in1 + " / v" + in2;
      case VECTOR_MIN_OP:
        return space + "v" + fOut + " = minimum(v" + in1 + ", v" + in2 + ")";
      case VECTOR_MAX_OP:
        return space + "v" + fOut + " = maximum(v" + in1 + ", v" + in2 + ")";
      case VECTOR_ABS_OP:
        return space + "v" + fOut + " = abs(v" + in1 + ")";
      case VECTOR_HEAVYSIDE_OP:
        return space + "v" + fOut + " = heaviside(v" + in1 + ", 1.0)";
      case VECTOR_CONST_SET_OP:
        return space + "v" + fOut + "[" + fIndex1 + "]" + " = " + fDoubleData1;
      case MATRIX_SUM_OP:
        return space + "m" + fOut + " = m" + in1 + " + m" + in2;
      case MATRIX_DIFF_OP:
        return space + "m" + fOut + " = m" + in1 + " - m" + in2;
      case MATRIX_PRODUCT_OP:
        return space + "m" + fOut + " = m" + in1 + " * m" + in2;
      case MATRIX_DIVISION_OP:
        return space + "m" + fOut + " = m" + in1 + " / m" + in2;
      case MATRIX_MIN_OP:
        return space + "m" + fOut + " = minimum(m" + in1 + ", m" + in2 + ")";
      case MATRIX_MAX_OP:
        return space + "m" + fOut + " = maximum(m" + in1 + ", m" + in2 + ")";
      case MATRIX_ABS_OP:
        return space + "m" + fOut + " = abs(m" + in1 + ")";
      case MATRIX_HEAVYSIDE_OP:
        return space + "m" + fOut + " = heaviside(m" + in1 + ", 1.0)";
      case MATRIX_CONST_SET_OP:
        return space + "m" + fOut + "[" + fIndex1 + ", " + fIndex2 + "]" + " = " + fDoubleData1;
      case SCALAR_VECTOR_PRODUCT_OP:
        return space + "v" + fOut + " = s" + in1 + " * v" + in2;
      case VECTOR_INNER_PRODUCT_OP:
        return space + "s" + fOut + " = " + "dot(v" + in1 + ", v" + in2 + ")";
      case VECTOR_OUTER_PRODUCT_OP:
        return space + "m" + fOut + " = " + "outer(v" + in1 + ", v" + in2 + ")";
      case SCALAR_MATRIX_PRODUCT_OP:
        return space + "m" + fOut + " = s" + in1 + " * m" + in2;
      case MATRIX_VECTOR_PRODUCT_OP:
        return space + "v" + fOut + " = dot(m" + in1 + ", v" + in2 + ")";
      case VECTOR_NORM_OP:
        return space + "s" + fOut + " = norm(v" + in1 + ")";
      case MATRIX_NORM_OP:
        return space + "s" + fOut + " = norm(m" + in1 + ")";
      case MATRIX_TRANSPOSE_OP:
        return space + "m" + fOut + " = transpose(m" + in1 + ")";
      case MATRIX_MATRIX_PRODUCT_OP:
        return space + "m" + fOut + " = matmul(m" + in1 + ", m" + in2 + ")";
      case VECTOR_MEAN_OP:
        return space + "s" + fOut + " = mean(v" + in1 + ")";
      case VECTOR_ST_DEV_OP:
        return space + "s" + fOut + " = std(v" + in1 + ")";
      case MATRIX_MEAN_OP:
        return space + "s" + fOut + " = mean(m" + in1 + ")";
      case MATRIX_ST_DEV_OP:
        return space + "s" + fOut + " = std(m" + in1 + ")";
      case MATRIX_ROW_MEAN_OP:
        return space + "v" + fOut + " = mean(m" + in1 + ", axis=1)";
      case MATRIX_ROW_ST_DEV_OP:
        return space + "v" + fOut + " = std(m" + in1 + ", axis=1)";
      case SCALAR_GAUSSIAN_SET_OP:
        return space + "s" + fOut + " = gaussian(" + fDoubleData1 + ", " + fDoubleData2 + ")";
      case VECTOR_GAUSSIAN_SET_OP:
        return space + "v" + fOut + " = gaussian(" + fDoubleData1 + ", " + fDoubleData2 + ", n_features)";
      case MATRIX_GAUSSIAN_SET_OP:
        return space + "m" + fOut + " = gaussian(" + fDoubleData1 + ", " + fDoubleData2
            + ", (n_features, n_features))";
      case SCALAR_UNIFORM_SET_OP:
        return space + "s" + fOut + " = uniform(" + fDoubleData1 + ", " + fDoubleData2 + ")";
      case VECTOR_UNIFORM_SET_OP:
        return space + "v" + fOut + " = uniform(" + fDoubleData1 + ", " + fDoubleData2 + ", n_features)";
      case MATRIX_UNIFORM_SET_OP:
        return space + "m" + fOut + " = uniform(" + fDoubleData1 + ", " + fDoubleData2
            + ", (n_features, n_features))";
      default:
        throw new RuntimeException("invalid op.");
    }
  }

  @Override
  public String toString() {
    return toString(0);
  }
}
