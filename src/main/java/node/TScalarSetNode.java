package node;

import java.util.ArrayList;
import java.util.HashMap;

import instruction.*;
import memory.*;
import utils.TRandomGenerator;

public class TScalarSetNode extends TConstSetNode {
  private double fValue;

  public TScalarSetNode() {
    super(TMemoryType.SCALAR);
    fValue = 0.0;
  }

  public TScalarSetNode(double value) {
    super(TMemoryType.SCALAR);
    fValue = value;
  }

  public TScalarSetNode(TRandomGenerator rand) {
    super(TMemoryType.SCALAR);
    fValue = rand.nextGaussian();
  }

  public TScalarSetNode(TScalarSetNode node) {
    super(node);
    fValue = node.fValue;
  }

  @Override
  public TScalarSetNode copyImpl(HashMap<Integer, TNode> copied) {
    return new TScalarSetNode(this);
  };

  @Override
  public TScalarSetNode copy(HashMap<Integer, TNode> copied) {
    return (TScalarSetNode) super.copy(copied);
  }

  public String getCodeImpl() {
    return getDiscreteCodeImpl() + "-" + fValue;
  }

  @Override
  public void randomizeValue(TRandomGenerator rand, double flipProb) {
    if (rand.nextProbability() < flipProb) {
      fValue = -fValue;
    } else {
      fValue = fValue * Math.exp(rand.nextGaussian());
    }
  }

  @Override
  public ArrayList<TInstruction> toInstructions(TVariablesManager variablesManager) {
    TVariable variable = getVariable(variablesManager);
    ArrayList<TInstruction> instructions = new ArrayList<TInstruction>();
    instructions.add(new TInstruction(TOp.SCALAR_CONST_SET_OP, variable.getAddress(), fValue));
    return instructions;
  }
}
