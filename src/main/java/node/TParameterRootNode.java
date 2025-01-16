package node;

import java.util.HashMap;

import instruction.TOp;
import instruction.TOpsSet;
import memory.TVariable;
import utils.TRandomGenerator;

public class TParameterRootNode extends TOpNode {
  private TParameterNode fParameterNode;
  private int fDepth;

  public TParameterRootNode(int depth, TOp op, TParameterNode parameterNode) {
    super(op, parameterNode.getOutMemoryType());
    fParameterNode = parameterNode;
    fDepth = depth;
  }

  public TParameterRootNode(TParameterRootNode node, HashMap<Integer, TNode> copied) {
    super(node);
    fDepth = node.fDepth;
    fParameterNode = node.fParameterNode.copy(copied);
  }

  @Override
  public TParameterRootNode copyImpl(HashMap<Integer, TNode> copied) {
    return new TParameterRootNode(this, copied);
  };

  @Override
  public TParameterRootNode copy(HashMap<Integer, TNode> copied) {
    return (TParameterRootNode) super.copy(copied);
  }

  public static TParameterRootNode randomCreate(int depth,
      TOpsSet opsSet, TParameterNode parameterNode,  TRandomGenerator rand) {
    TOp op = opsSet.getRandomOp(parameterNode.getOutMemoryType(), rand);
    return new TParameterRootNode(depth, op, parameterNode);
  }

  public TParameterNode getParameterNode() {
    return fParameterNode;
  }

  @Override
  public int getDepth() {
    return fDepth;
  }

  @Override
  public TVariable getVariable(TVariablesManager variablesManager){
    return fParameterNode.getVariable(variablesManager);
  }

  public void addSelfParameterNodeRandomly(TRandomGenerator rand) {
    this.setDescendantNodeRandomly(this.getParameterNode(), rand);
  }

  public void addSelfParameterNodeRandomlyIfNotDepended(TRandomGenerator rand) {
    this.setDescendantNodeRandomlyIfNotDepended(this.getParameterNode(), rand);
  }
}
