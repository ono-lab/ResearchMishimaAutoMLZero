package node;

import java.util.ArrayList;
import java.util.HashMap;

import instruction.*;
import memory.*;
import utils.TRandomGenerator;

public abstract class TConstSetNode extends TTerminalNode {
  protected TConstSetNode(TMemoryType outMemoryType) {
    super(outMemoryType);
  }

  protected TConstSetNode(TConstSetNode node) {
    super(node);
  }

  @Override
  abstract protected TConstSetNode copyImpl(HashMap<Integer, TNode> copied);

  @Override
  public TConstSetNode copy(HashMap<Integer, TNode> copied) {
    return (TConstSetNode) super.copy(copied);
  }

  abstract public void randomizeValue(TRandomGenerator rand, double flipProb);

  abstract public ArrayList<TInstruction> toInstructions(TVariablesManager variablesManager);

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
    str += " = constant";
    if (!processedNodes.contains(this)) {
      processedNodes.add(this);
    } else {
      str += " (connected)";
    }
    return str;
  }
}
