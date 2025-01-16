package node;

import java.util.ArrayList;
import java.util.HashMap;

import instruction.TInstruction;
import memory.TMemoryType;
import memory.TVariable;
import utils.TRandomGenerator;

public abstract class TParameterNode extends TTerminalNode {
  protected TParameterNode(TMemoryType outMemoryType) {
    super(outMemoryType);
  }


  protected TParameterNode(TParameterNode node) {
    super(node);
  }

  @Override
  abstract protected TParameterNode copyImpl(HashMap<Integer, TNode> copied);

  @Override
  public TParameterNode copy(HashMap<Integer, TNode> copied) {
    return (TParameterNode) super.copy(copied);
  }

  abstract public ArrayList<TInstruction> getSetupInstructions(TVariablesManager variablesManager);

  abstract public void randomizeInitialValue(TRandomGenerator rand, double flipProb);

  static public TParameterNode create(TMemoryType memoryType, int dim, TRandomGenerator rand) {
    switch (memoryType) {
      case SCALAR:
        return new TScalarParameterNode(rand);
      case VECTOR:
        return new TVectorParameterNode(dim, rand);
      case MATRIX:
        return new TMatrixParameterNode(dim, rand);
      default:
        throw new RuntimeException("Invalid memory type");
    }
  }

  @Override
  public String toString(int indent, ArrayList<TNode> processedNodes, TVariablesManager variablesManager) {
    TVariable variable = getVariable(variablesManager);
    String str = "";
    for (int i = 0; i < indent; i++) {
      str += "  ";
    }
    if (indent != 0) {
      str += "L ";
    }
    str += variable;
    if (!processedNodes.contains(this)) {
      processedNodes.add(this);
    }
    return str;
  }
}
