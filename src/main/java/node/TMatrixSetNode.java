package node;

import java.util.ArrayList;
import java.util.HashMap;

import jp.ac.titech.onolab.core.matrix.TCMatrix;
import instruction.*;
import memory.*;
import utils.TRandomGenerator;

public class TMatrixSetNode extends TConstSetNode {
  private TCMatrix fValue;
  private int fDim;

  public TMatrixSetNode(int dim) {
    super(TMemoryType.MATRIX);
    fDim = dim;
    fValue = new TCMatrix(dim, dim);
  }

  public TMatrixSetNode(int dim, TCMatrix value) {
    super(TMemoryType.MATRIX);
    if (value.getColumnDimension() != dim || value.getRowDimension() != dim) {
      throw new RuntimeException("Invalid value dimension");
    }
    fDim = dim;
    fValue = value;
  }

  public TMatrixSetNode(int dim, TRandomGenerator rand) {
    super(TMemoryType.MATRIX);
    fDim = dim;
    if (dim <= 0) {
      throw new RuntimeException("Invalid dimension");
    }
    fValue = new TCMatrix(dim, dim);
    rand.fillGaussian(0, 1, fValue);
  }

  public TMatrixSetNode(TMatrixSetNode node) {
    super(node);
    fValue = node.fValue.clone();
    fDim = node.fDim;
  }

  @Override
  public TMatrixSetNode copyImpl(HashMap<Integer, TNode> copied) {
    return new TMatrixSetNode(this);
  };

  @Override
  public TMatrixSetNode copy(HashMap<Integer, TNode> copied) {
    return (TMatrixSetNode) super.copy(copied);
  }

  public String getCodeImpl() {
    return getDiscreteCodeImpl() + "-" + fValue;
  }

  @Override
  public void randomizeValue(TRandomGenerator rand, double flipProb) {
    if (rand.nextProbability() < flipProb) {
      fValue.times(-1);
    } else {
      TCMatrix diff = new TCMatrix(fDim, fDim);
      rand.fillGaussian(0, 1, diff);
      fValue.timesElement(diff.exp());
    }
  }

  @Override
  public ArrayList<TInstruction> toInstructions(TVariablesManager variablesManager) {
    TVariable variable = getVariable(variablesManager);
    ArrayList<TInstruction> instructions = new ArrayList<TInstruction>();
    for (int row = 0; row < fDim; row++) {
      for (int col = 0; col < fDim; col++) {
        float value = (float) fValue.getValue(row);
        TInstruction instr = new TInstruction(TOp.MATRIX_CONST_SET_OP, variable.getAddress(), value, row, col);
        instructions.add(instr);
      }
    }
    return instructions;
  }
}
