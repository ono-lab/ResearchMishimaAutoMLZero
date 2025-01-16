package memory;

import jp.ac.titech.onolab.core.matrix.TCMatrix;

/**
 * アルゴリズムの実行に用いるメモリ
 */
public class TMemory {
  private static final int kMaxNumOfScalarAddresses = 50;
  private static final int kMaxNumOfVectorAddresses = 50;
  private static final int kMaxNumOfMatrixAddresses = 50;

  private int fNumOfScalarAddresses = kMaxNumOfScalarAddresses;
  private int fNumOfVectorAddresses = kMaxNumOfVectorAddresses;
  private int fNumOfMatrixAddresses = kMaxNumOfMatrixAddresses;

  /**
   * 正解ラベル（スカラー）のアドレス
   */
  public final int scalarCorrectLabelAddress = 0;

  /**
   * 予測ラベル（スカラー）のアドレス
   */
  public final int scalarPredictionLabelAddress = 1;

  /**
   * 入力ベクトルのアドレス
   */
  public final int vectorInputAddress = 0;

  /**
   * 代入可能な最初のアドレス（スカラー）
   */
  public final int scalarFirstFreeAddress = 2;

  /**
   * 代入可能な最初のアドレス（ベクトル）
   */
  public final int vectorFirstFreeAddress = 1;

  /**
   * 代入可能な最初のアドレス（行列）
   */
  public final int matrixFirstFreeAddress = 0;

  /**
   * 入力として設定可能な最初のアドレス（Setup関数のスカラー）
   */
  public final int scalarFirstInAddressOfSetup = 2;

  /**
   * 出力として設定可能な最初のアドレス（Setup関数のスカラー）
   */
  public final int scalarFirstOutAddressOfSetup = 2;

  /**
   * 入力として設定可能な最初のアドレス（Predict関数のスカラー）
   */
  public final int scalarFirstInAddressOfPredict = 2;

  /**
   * 出力として設定可能な最初のアドレス（Predict関数のスカラー）
   */
  public final int scalarFirstOutAddressOfPredict = 1;

  /**
   * 入力として設定可能な最初のアドレス（Learn関数のスカラー）
   */
  public final int scalarFirstInAddressOfLearn = 0;

  /**
   * 出力として設定可能な最初のアドレス（Learn関数のスカラー）
   */
  public final int scalarFirstOutAddressOfLearn = 1;

  /**
   * 入力として設定可能な最初のアドレス（ベクトル）
   */
  public final int vectorFirstInAddress = 0;

  /**
   * 出力として設定可能な最初のアドレス（ベクトル）
   */
  public final int vectorFirstOutAddress = 1;

  /**
   * 入力として設定可能な最初のアドレス（行列）
   */
  public final int matrixFirstInAddress = 0;

  /**
   * 出力として設定可能な最初のアドレス（行列）
   */
  public final int matrixFirstOutAddress = 0;

  /**
   * スカラーの格納先
   */
  public double[] scalar;

  /**
   * ベクトルの格納先
   */
  public TCMatrix[] vector;

  /**
   * 行列の格納先
   */
  public TCMatrix[] matrix;

  /**
   * ベクトル、行列の列ベクトルおよび行ベクトルの次元
   * initializeによって初期化するまでは-1
   */
  private int fDim = -1;

  /**
   * スカラー、ベクトル、行列のアドレスの数を指定してメモリを初期化するコンストラクタ
   */
  public TMemory(int numOfScalarAddresses, int numOfVectorAddresses, int numOfMatrixAddresses) {
    scalar = new double[kMaxNumOfScalarAddresses];
    vector = new TCMatrix[kMaxNumOfVectorAddresses];
    matrix = new TCMatrix[kMaxNumOfMatrixAddresses];
    fNumOfScalarAddresses = numOfScalarAddresses;
    fNumOfVectorAddresses = numOfVectorAddresses;
    fNumOfMatrixAddresses = numOfMatrixAddresses;
  }

  /**
   * 次元とスカラー、ベクトル、行列のアドレスの数を指定してメモリを初期化するコンストラクタ
   */
  public TMemory(int dim, int numOfScalarAddresses, int numOfVectorAddresses, int numOfMatrixAddresses) {
    this(numOfScalarAddresses, numOfVectorAddresses, numOfMatrixAddresses);
    initialize(dim);
  }

  /**
   * デフォルトの命令数でメモリを初期化するコンストラクタ
   */
  public TMemory() {
    this(kMaxNumOfScalarAddresses, kMaxNumOfVectorAddresses, kMaxNumOfMatrixAddresses);
  }

  /**
   * 次元数とデフォルトの命令数でメモリを初期化するコンストラクタ
   */
  public TMemory(int dim) {
    this(dim, kMaxNumOfScalarAddresses, kMaxNumOfVectorAddresses, kMaxNumOfMatrixAddresses);
  }

  /**
   * ベクトル、行列の列ベクトルおよび行ベクトルの次元
   * initializeによって初期化するまでは-1
   */
  public int getDim() {
    return fDim;
  }

  /**
   * 指定された次元でメモリを初期化する関数
   */
  public void initialize(int dim) {
    assert dim > 0;
    if (dim == fDim) {
      return;
    }
    for (int index = 0; index < vector.length; index++) {
      if (dim != fDim) {
        vector[index] = new TCMatrix(dim);
      }
    }
    for (int index = 0; index < matrix.length; index++) {
      matrix[index] = new TCMatrix(dim, dim);
    }
    wipe();
    fDim = dim;
  }


  /**
   * メモリー内をすべて0.0で初期化する関数
   */
  public void wipe() {
    for (int index = 0; index < scalar.length; index++) {
      scalar[index] = 0.0;
    }
    for (TCMatrix vector : vector) {
      vector.fill(0.0);
    }
    for (TCMatrix matrix : matrix) {
      matrix.fill(0.0);
    }
  }

  public void setUsedNumOfAddresses(int numOfScalarAddresses, int numOfVectorAddresses, int numOfMatrixAddresses) {
    fNumOfScalarAddresses = numOfScalarAddresses;
    fNumOfVectorAddresses = numOfVectorAddresses;
    fNumOfMatrixAddresses = numOfMatrixAddresses;
  }

  public boolean isScalarPredictionLabelVariable(TVariable variable) {
    return variable.getAddress() == scalarPredictionLabelAddress && variable.getMemoryType() == TMemoryType.SCALAR;
  }

  public boolean isScalarCorrectLabelVariable(TVariable variable) {
    return variable.getAddress() == scalarCorrectLabelAddress && variable.getMemoryType() == TMemoryType.SCALAR;
  }

  public boolean isVectorInputVariable(TVariable variable) {
    return variable.getAddress() == vectorInputAddress && variable.getMemoryType() == TMemoryType.VECTOR;
  }

  /**
   * 入力ベクトルをメモリーにセットする関数
   */
  public void setInputVector(TCMatrix feature) {
    assert feature.getRowDimension() == fDim;
    assert feature.getColumnDimension() == 1;
    vector[this.vectorInputAddress] = feature;
  }

  /**
   * （アルゴリズムによってセットされた）予測ラベルを取得する関数
   */
  public double getPredictionLabel() {
    return scalar[this.scalarPredictionLabelAddress];
  }


  /**
   * 予測ラベルをセットする関数
   */
  public double setPredictionLabel(double prediction) {
    return scalar[this.scalarPredictionLabelAddress] = prediction;
  }

  /**
   * 教師ラベルをセットする関数
   */
  public void setCorrectLabel(double label) {
    scalar[this.scalarCorrectLabelAddress] = label;
  }

  /**
   * 教師ラベルを初期化する関数
   */
  public void resetLabel() {
    setCorrectLabel(0.0);
  }

  /**
   * スカラーのアドレスの個数を取得する関数
   */
  public int getNumOfScalarAddresses() {
    return fNumOfScalarAddresses;
  }

  /**
   * ベクトルのアドレスの個数を取得する関数
   */
  public int getNumOfVectorAddresses() {
    return fNumOfVectorAddresses;
  }

  /**
   * 行列のアドレスの個数を取得する関数
   */
  public int getNumOfMatrixAddresses() {
    return fNumOfMatrixAddresses;
  }

  @Override
  public String toString() {
    String str = "Scalar\n";
    for (int index = 0; index < scalar.length; index++) {
      str += index;
      if (index == this.scalarCorrectLabelAddress) {
        str += "(correct label)";
      }
      if (index == this.scalarPredictionLabelAddress) {
        str += "(prediction label)";
      }
      str += ":\n" + scalar[index] + "\n";
    }
    str += "\n\nVector\n";
    for (int index = 0; index < vector.length; index++) {
      str += index;
      if (index == this.vectorInputAddress) {
        str += "(feature)";
      }
      str += ":\n" + vector[index];

    }
    str += "\n\nMatrix\n";
    for (int index = 0; index < matrix.length; index++) {
      str += index;
      str += ":\n" + matrix[index];
    }
    return str;
  }
}
