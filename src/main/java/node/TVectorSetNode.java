package node;

import java.util.ArrayList;
import java.util.HashMap;

import instruction.*;
import jp.ac.titech.onolab.core.matrix.TCMatrix;
import memory.*;
import utils.TRandomGenerator;

public class TVectorSetNode extends TConstSetNode {
  private TCMatrix fValue;
  private int fDim;

  public TVectorSetNode(int dim) {
    super(TMemoryType.VECTOR);
    fDim = dim;
    fValue = new TCMatrix(dim);
  }

  public TVectorSetNode(int dim, TCMatrix value) {
    super(TMemoryType.VECTOR);
    if (value.getColumnDimension() != 1 || value.getRowDimension() != dim) {
      throw new RuntimeException("Invalid value dimension");
    }
    fDim = dim;
    fValue = value.clone();
  }

  public TVectorSetNode(int dim, TRandomGenerator rand) {
    super(TMemoryType.VECTOR);
    if (dim <= 0) {
      throw new RuntimeException("Invalid dimension");
    }
    fDim = dim;
    fValue = new TCMatrix(dim);
    rand.fillGaussian(0, 1, fValue);
  }

  public TVectorSetNode(TVectorSetNode node) {
    super(node);
    fValue = node.fValue.clone();
    fDim = node.fDim;
  }

  @Override
  public TVectorSetNode copyImpl(HashMap<Integer, TNode> copied) {
    return new TVectorSetNode(this);
  };

  @Override
  public TVectorSetNode copy(HashMap<Integer, TNode> copied) {
    return (TVectorSetNode) super.copy(copied);
  }

  public String getCodeImpl() {
    return getDiscreteCodeImpl() + "-" + fValue;
  }

  @Override
  public void randomizeValue(TRandomGenerator rand, double flipProb) {
    if (rand.nextProbability() < flipProb) {
      fValue.times(-1);
    } else {
      TCMatrix diff = new TCMatrix(fDim);
      rand.fillGaussian(0, 1, diff);
      fValue.timesElement(diff.exp());
    }
  }

  @Override
  public ArrayList<TInstruction> toInstructions(TVariablesManager variablesManager) {
    TVariable variable = getVariable(variablesManager);
    ArrayList<TInstruction> instructions = new ArrayList<TInstruction>();
    for (int i = 0; i < fDim; i++) {
      double value = fValue.getValue(i);
      TInstruction instr = new TInstruction(TOp.VECTOR_CONST_SET_OP, variable.getAddress(), value, i);
      instructions.add(instr);
    }
    return instructions;
  }
}
