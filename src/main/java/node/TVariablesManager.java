package node;

import java.util.ArrayList;

import memory.TMemory;
import memory.TMemoryType;
import memory.TVariable;

/**
 * 利用中の命令のアドレスを管理するクラス
 */
public class TVariablesManager {
  /**
   * メモリ
   */
  private TMemory fMemory;

  /**
   * 次にアサインするスカラーのアドレス
   */
  private int fNextScalarAddress;

  /**
   * 次にアサインするベクトルのアドレス
   */
  private int fNextVectorAddress;

  /**
   * 次にアサインする行列のアドレス
   */
  private int fNextMatrixAddress;

  /**
   * 既に変数をアサインしたノード
   */
  private ArrayList<TNode> assignedNodes = new ArrayList<TNode>();

  /**
   * 既に変数をアサインしたノードの変数
   */
  private ArrayList<TVariable> assignedVariables = new ArrayList<TVariable>();


  public TVariablesManager(TMemory memory) {
    fMemory = memory;
    fNextScalarAddress = memory.scalarFirstFreeAddress;
    fNextVectorAddress = memory.vectorFirstFreeAddress;
    fNextMatrixAddress = memory.matrixFirstFreeAddress;
  }

  /**
   * メモリの種類に応じた次のアドレスを取得する関数
   */
  private int getNextAddress(TMemoryType memoryType) {
    switch (memoryType) {
      case SCALAR:
        return getNextScalarAddress();
      case VECTOR:
        return getNextVectorAddress();
      case MATRIX:
        return getNextMatrixAddress();
      default:
        throw new RuntimeException("Invalid memory type");
    }
  }

  /**
   * 次にアサイン可能なスカラーのアドレスを取得する関数
   */
  private int getNextScalarAddress() {
    if(fNextScalarAddress == fMemory.scalar.length){
      throw new RuntimeException("Scalar address is full");
    }
    return fNextScalarAddress++;
  }

  /**
   * 次にアサイン可能なベクトルのアドレスを取得する関数
   */
  private int getNextVectorAddress() {
    if (fNextScalarAddress == fMemory.vector.length) {
      throw new RuntimeException("Vector address is full");
    }
    return fNextVectorAddress++;
  }

  /**
   * 次にアサイン可能な行列のアドレスを取得する関数
   */
  private int getNextMatrixAddress() {
    if (fNextScalarAddress == fMemory.matrix.length) {
      throw new RuntimeException("Matrix address is full");
    }
    return fNextMatrixAddress++;
  }

  public TVariable getVariable(TNode node) {
    for (int assignedIndex = 0; assignedIndex < assignedNodes.size(); assignedIndex++) {
      TNode assignedNode = assignedNodes.get(assignedIndex);
      if (assignedNode == node)
        return assignedVariables.get(assignedIndex);
    }
    TMemoryType memoryType = node.getOutMemoryType();
    int address = getNextAddress(memoryType);
    TVariable variable = new TVariable(memoryType, address);
    assignedNodes.add(node);
    assignedVariables.add(variable);
    return variable;
  }
}
