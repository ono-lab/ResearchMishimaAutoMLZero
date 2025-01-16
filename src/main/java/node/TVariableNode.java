package node;

import java.util.ArrayList;
import java.util.HashMap;

import memory.*;

public class TVariableNode extends TTerminalNode {
  private TVariable fVariable;

  public TVariableNode(TVariable variable) {
    super(variable.getMemoryType());
    fVariable = variable;
  }

  public TVariableNode(TVariableNode node) {
    super(node);
    fVariable = node.fVariable.copy();
  }

  @Override
  public TVariableNode copyImpl(HashMap<Integer, TNode> copied) {
    return new TVariableNode(this);
  };

  @Override
  public TVariableNode copy(HashMap<Integer, TNode> copied) {
    return (TVariableNode) super.copy(copied);
  }

  public String getCodeImpl() {
    return getDiscreteCodeImpl() + "-" + getVariable();
  }


  @Override
  public TVariable getVariable(TVariablesManager variablesManager) {
    return fVariable;
  }

  public TVariable getVariable() {
    return fVariable;
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
