package memory;

/**
 * メモリの型とアドレスの組
 */
public class TVariable {
  /**
   * メモリ内のアドレス
   */
  private int fAddress;

  /**
   * メモリの型
   */
  private TMemoryType fMemoryType;

  /**
   * メモリの型とアドレスの組によるコンストラクタ
   */
  public TVariable(TMemoryType memoryType, int address) {
    fAddress = address;
    fMemoryType = memoryType;
  }

  public TVariable(TVariable variable) {
    fAddress = variable.fAddress;
    fMemoryType = variable.fMemoryType;
  }

  public TVariable copy() {
    return new TVariable(this);
  }

  /**
   * メモリの型の取得
   */
  public TMemoryType getMemoryType() {
    return fMemoryType;
  }

  /**
   * メモリのアドレスの取得
   */
  public int getAddress() {
    return fAddress;
  }

  /**
   * メモリ内の入力ベクトルに対応する変数を取得する関数
   */
  static public TVariable getVectorInputVariable(TMemory memory) {
    return new TVariable(TMemoryType.VECTOR, memory.vectorInputAddress);
  }

  /**
   * メモリ内の予測ラベルに対応する変数を取得する関数
   */
  static public TVariable getPredictionLabelVariable(TMemory memory) {
    return new TVariable(TMemoryType.SCALAR, memory.scalarPredictionLabelAddress);
  }

  /**
   * メモリ内の正解ラベルに対応する変数を取得する関数
   */
  static public TVariable getCorrectLabelVariable(TMemory memory) {
    return new TVariable(TMemoryType.SCALAR, memory.scalarCorrectLabelAddress);
  }

  @Override
  public boolean equals(final Object other) {
    if (other == this)
      return true;
    if (!(other instanceof TVariable))
      return false;
    TVariable otherVariable = (TVariable) other;
    return fAddress == otherVariable.fAddress && fMemoryType == otherVariable.fMemoryType;
  }

  @Override
  public String toString() {
    switch(fMemoryType) {
      case SCALAR:
        return "s" + getAddress();
      case VECTOR:
        return "v" + getAddress();
      case MATRIX:
        return "m" + getAddress();
      default:
        throw new RuntimeException("Should not reach here.");
    }
  }
}
