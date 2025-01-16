package node;

import java.util.ArrayList;
import java.util.HashMap;

import instruction.*;
import jp.ac.titech.onolab.core.matrix.TCMatrix;
import memory.TMemoryType;
import memory.TVariable;
import utils.TRandomGenerator;

public class TVectorParameterNode extends TParameterNode {
  private TCMatrix fInitialValue;
  private int fDim;

  public TVectorParameterNode(int dim) {
    super(TMemoryType.VECTOR);
    fDim = dim;
    fInitialValue = new TCMatrix(dim);
  }

  public TVectorParameterNode(int dim, TRandomGenerator rand) {
    super(TMemoryType.VECTOR);
    if (dim <= 0) {
      throw new RuntimeException("Invalid dimension");
    }
    fInitialValue = new TCMatrix(dim);
    fDim = dim;
    rand.fillGaussian(0, 1, fInitialValue);
  }

  public TVectorParameterNode(TCMatrix initialValue) {
    super(TMemoryType.VECTOR);
    if (initialValue.getColumnDimension() != 1) {
      throw new RuntimeException("Invalid initial value for vector");
    }
    fInitialValue = initialValue;
    fDim = initialValue.getColumnDimension();
  }

  public TVectorParameterNode(TVectorParameterNode node) {
    super(node);
    fInitialValue = node.fInitialValue.clone();
    fDim = node.fDim;
  }

  @Override
  public TVectorParameterNode copyImpl(HashMap<Integer, TNode> copied) {
    return new TVectorParameterNode(this);
  };

  @Override
  public TVectorParameterNode copy(HashMap<Integer, TNode> copied) {
    return (TVectorParameterNode) super.copy(copied);
  }

  public String getCodeImpl() {
    return getDiscreteCodeImpl() + "-" + fInitialValue;
  }

  public void randomizeInitialValue(TRandomGenerator rand, double flipProb) {
    if (rand.nextProbability() < flipProb) {
      fInitialValue.times(-1);
    } else {
      TCMatrix diff = new TCMatrix(fDim);
      rand.fillGaussian(0, 1, diff);
      fInitialValue.timesElement(diff.exp());
    }
  };

  public ArrayList<TInstruction> getSetupInstructions(TVariablesManager variablesManager) {
    TVariable variable = getVariable(variablesManager);
    ArrayList<TInstruction> instructions = new ArrayList<TInstruction>();
    int dim = fInitialValue.getRowDimension();
    for (int i = 0; i < dim; i++) {
      TInstruction instruction = new TInstruction(TOp.VECTOR_CONST_SET_OP, variable.getAddress(), fInitialValue.getValue(i), i);
      instructions.add(instruction);
    }
    return instructions;
 };
}
