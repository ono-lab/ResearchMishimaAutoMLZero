package node;

import java.util.ArrayList;
import java.util.HashMap;

import instruction.*;
import memory.TMemoryType;
import memory.TVariable;
import utils.TRandomGenerator;

public class TScalarParameterNode extends TParameterNode {
  private double fInitialValue;

  public TScalarParameterNode() {
    super(TMemoryType.SCALAR);
    fInitialValue = 0.0;
  }

  public TScalarParameterNode(TRandomGenerator rand) {
    super(TMemoryType.SCALAR);
    fInitialValue = rand.nextGaussian();
  }

  public TScalarParameterNode(double initialValue) {
    super(TMemoryType.SCALAR);
    fInitialValue = initialValue;
  }

  public TScalarParameterNode(TScalarParameterNode node) {
    super(node);
    fInitialValue = node.fInitialValue;
  }

  @Override
  public TScalarParameterNode copyImpl(HashMap<Integer, TNode> copied) {
    return new TScalarParameterNode(this);
  };

  @Override
  public TScalarParameterNode copy(HashMap<Integer, TNode> copied) {
    return (TScalarParameterNode) super.copy(copied);
  }

  public String getCodeImpl() {
    return getDiscreteCode() + "-" + fInitialValue;
  }

  public void randomizeInitialValue(TRandomGenerator rand, double flipProb) {
    if (rand.nextProbability() < flipProb) {
      fInitialValue = -fInitialValue;
    } else {
      fInitialValue = fInitialValue * Math.exp(rand.nextGaussian());
    }
  };

  public ArrayList<TInstruction> getSetupInstructions(TVariablesManager variablesManager) {
    TVariable variable = getVariable(variablesManager);
    ArrayList<TInstruction> instructions = new ArrayList<TInstruction>();
    instructions.add(new TInstruction(TOp.SCALAR_CONST_SET_OP, variable.getAddress(), fInitialValue));
    return instructions;
  };
}
