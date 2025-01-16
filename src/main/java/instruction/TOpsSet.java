package instruction;

import java.util.ArrayList;

import memory.TMemoryType;
import utils.TRandomGenerator;

/**
 * 命令セット
 */
public class TOpsSet {
  private TOp[] fOps;
  private ArrayList<TOp> fScalarOutOps = new ArrayList<TOp>();
  private ArrayList<TOp> fVectorOutOps = new ArrayList<TOp>();
  private ArrayList<TOp> fMatrixOutOps = new ArrayList<TOp>();

  /**
   * 指定された命令の配列から命令セットを生成するコンストラクタ
   */
  public TOpsSet(TOp[] ops) {
    fOps = ops;
    for (TOp op : ops) {
      TMemoryType outMemoryType = op.getOutMemoryType();
      if (outMemoryType != null) {
        switch (outMemoryType) {
          case SCALAR:
            fScalarOutOps.add(op);
            break;
          case VECTOR:
            fVectorOutOps.add(op);
            break;
          case MATRIX:
            fMatrixOutOps.add(op);
            break;
        }
      }
    }
  }

  /**
   * 命令が1つ以上含まれているかどうか
   */
  public boolean hasOps() {
    return fOps.length > 0;
  }

  /**
   * 特定の型の出力を持つ命令が1つ以上含まれているかどうか
   */
  public boolean hasOps(TMemoryType outMemoryType) {
    switch (outMemoryType) {
      case SCALAR: {
        return fScalarOutOps.size() > 0;
      }
      case VECTOR: {
        return fVectorOutOps.size() > 0;
      }
      case MATRIX: {
        return fMatrixOutOps.size() > 0;
      }
      default:
        throw new RuntimeException("Invalid out memory type");
    }
  }

  /**
   * 命令をランダムに1つ取得
   */
  public TOp getRandomOp(TRandomGenerator rand){
    int index = rand.nextInt(fOps.length);
    return fOps[index];
  }

  /**
   * 指定された出力のメモリの種類に対応する命令をランダムに1つ取得
   */
  public TOp getRandomOp(TMemoryType outMemoryType, TRandomGenerator rand){
    switch (outMemoryType) {
      case SCALAR: {
        if (fScalarOutOps.size() == 0) {
          throw new RuntimeException("Operation for scalar output is unavailable.");
        }
        int index = rand.nextInt(fScalarOutOps.size());
        return fScalarOutOps.get(index);
      }
      case VECTOR: {
        if (fVectorOutOps.size() == 0) {
          throw new RuntimeException("Operation for vector output is unavailable.");
        }
        int index = rand.nextInt(fVectorOutOps.size());
        return fVectorOutOps.get(index);
      }
      case MATRIX: {
        if (fMatrixOutOps.size() == 0) {
          throw new RuntimeException("Operation for matrix output is unavailable.");
        }
        int index = rand.nextInt(fMatrixOutOps.size());
        return fMatrixOutOps.get(index);
      }
      default:
        throw new RuntimeException("Invalid out memory type");
    }
  }
}
