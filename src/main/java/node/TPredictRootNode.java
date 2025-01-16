package node;

import java.util.HashMap;

import instruction.TOp;
import instruction.TOpsSet;
import memory.TVariable;
import utils.TRandomGenerator;

public class TPredictRootNode extends TOpNode {
  private TVariable fPredictionLabelVariable;

  public TPredictRootNode(TOp op, TVariable predictionLabelVariable) {
    super(op, predictionLabelVariable.getMemoryType());
    fPredictionLabelVariable = predictionLabelVariable;
  }

  public TPredictRootNode(TPredictRootNode node) {
    super(node);
    fPredictionLabelVariable = node.fPredictionLabelVariable.copy();
  }

  @Override
  public int getDepth() {
    return 0;
  }

  @Override
  public TPredictRootNode copyImpl(HashMap<Integer, TNode> copied) {
    return new TPredictRootNode(this);
  };

  @Override
  public TPredictRootNode copy(HashMap<Integer, TNode> copied) {
    return (TPredictRootNode) super.copy(copied);
  }

  public static TPredictRootNode randomCreate(TOpsSet opsSet, TVariable outVariable, TRandomGenerator rand) {
    TOp op = opsSet.getRandomOp(outVariable.getMemoryType(), rand);
    return new TPredictRootNode(op, outVariable);
  }

  @Override
  public TVariable getVariable(TVariablesManager variablesManager) {
    return fPredictionLabelVariable;
  }

  public TVariable getVariable() {
    return fPredictionLabelVariable;
  }
}
