package node;

import java.util.ArrayList;
import java.util.HashMap;

import jp.ac.titech.onolab.core.matrix.TCMatrix;
import memory.TMemoryType;
import memory.TVariable;
import instruction.*;
import utils.TRandomGenerator;

public class TMatrixParameterNode extends TParameterNode {
  private TCMatrix fInitialValue;
  private int fDim;

  public TMatrixParameterNode(int dim) {
    super(TMemoryType.MATRIX);
    fDim = dim;
    fInitialValue = new TCMatrix(dim, dim);
  }

  public TMatrixParameterNode(int dim, TRandomGenerator rand) {
    super(TMemoryType.MATRIX);
    if (dim <= 0) {
      throw new RuntimeException("Invalid dimension");
    }
    fDim = dim;
    fInitialValue = new TCMatrix(dim, dim);
    rand.fillGaussian(0, 1, fInitialValue);
  }

  public TMatrixParameterNode(TCMatrix initialValue) {
    super(TMemoryType.MATRIX);
    if (initialValue.getColumnDimension() != initialValue.getRowDimension()) {
      throw new RuntimeException("Invalid initial value for matrix");
    }
    fInitialValue = initialValue;
    fDim = initialValue.getRowDimension();
  }

  public TMatrixParameterNode(TMatrixParameterNode node) {
    super(node);
    fInitialValue = node.fInitialValue.clone();
    fDim = node.fDim;
  }

  @Override
  public TMatrixParameterNode copyImpl(HashMap<Integer, TNode> copied) {
    return new TMatrixParameterNode(this);
  };

  @Override
  public TMatrixParameterNode copy(HashMap<Integer, TNode> copied) {
    return (TMatrixParameterNode) super.copy(copied);
  }

  public String getCodeImpl() {
    return getDiscreteCodeImpl() + "-" + fInitialValue;
  }

  public void randomizeInitialValue(TRandomGenerator rand, double flipProb) {
    if (rand.nextProbability() < flipProb) {
      fInitialValue.times(-1);
    } else {
      TCMatrix diff = new TCMatrix(fDim, fDim);
      rand.fillGaussian(0, 1, diff);
      fInitialValue.timesElement(diff.exp());
    }
  };

  @Override
  public ArrayList<TInstruction> getSetupInstructions(TVariablesManager variablesManager) {
    TVariable variable = getVariable(variablesManager);
    ArrayList<TInstruction> instructions = new ArrayList<TInstruction>();
    int dim = fInitialValue.getRowDimension();
    for (int row = 0; row < dim; row++) {
      for (int column = 0; column < dim; column++) {
        double value = fInitialValue.getValue(row, column);
        TInstruction instr = new TInstruction(TOp.MATRIX_CONST_SET_OP, variable.getAddress(), value, row, column);
        instructions.add(instr);
      }
    }
    return instructions;
 };
}
