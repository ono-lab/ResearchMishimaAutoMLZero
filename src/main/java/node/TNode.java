package node;

import java.util.ArrayList;
import java.util.HashMap;

import memory.*;
import utils.THashGenerator;
import utils.TRandomGenerator;

public abstract class TNode {
  private TMemoryType fOutMemoryType;
  private ArrayList<TNode> fParentNodes;
  private TNode[] fChildNodes;

  protected TNode(TMemoryType outMemoryType, int numChildNodes) {
    fParentNodes = new ArrayList<TNode>();
    fOutMemoryType = outMemoryType;
    fChildNodes = new TNode[numChildNodes];
  }

  protected TNode(TNode node) {
    fParentNodes = new ArrayList<TNode>();
    fOutMemoryType = node.fOutMemoryType;
    fChildNodes = new TNode[node.fChildNodes.length];
  }

  abstract protected TNode copyImpl(HashMap<Integer, TNode> copied);

  public TNode copy(HashMap<Integer, TNode> copied) {
    if (copied.containsKey(hashCode())) {
      return copied.get(hashCode());
    }
    TNode newNode = copyImpl(copied);
    copied.put(hashCode(), newNode);
    for (int i = 0; i < fChildNodes.length; i++) {
      if (fChildNodes[i] != null) {
        TNode childNode = fChildNodes[i].copy(copied);
        if (!childNode.fParentNodes.contains(newNode)) {
          childNode.fParentNodes.add(newNode);
        }
        newNode.fChildNodes[i] = childNode;
      }
    }
    return newNode;
  }

  public String getCodeImpl() {
    return getDiscreteCodeImpl();
  };

  public String getDiscreteCodeImpl() {
    String code = "";
    code += this.getClass().getSimpleName();
    code += "-";
    code += fOutMemoryType;
    code += "-";
    int parentSize = fParentNodes.size();
    code += parentSize;
    return code;
  }

  public long getCode() {
    String code = "";
    code += "(";
    code += getCodeImpl();
    for (TNode childNode : fChildNodes) {
      code += "#";
      if (childNode != null) {
        code += childNode.getCode();
      } else {
        code += "null";
      }
    }
    code += ")";
    return THashGenerator.getStringHash(code);
  }

  public long getDiscreteCode() {
    String code = "";
    code += "(";
    code += getDiscreteCodeImpl();
    for (TNode childNode : fChildNodes) {
      code += "#";
      if (childNode != null) {
        code += childNode.getDiscreteCode();
      } else {
        code += "null";
      }
    }
    code += ")";
    return THashGenerator.getStringHash(code);
  }

  /**
   * 出力先の変数のメモリの型（スカラー、ベクトル、行列）を取得
   */
  public void setOutMemoryType(TMemoryType outMemoryType) {
    fOutMemoryType = outMemoryType;
  }

  /**
   * 出力先の変数のメモリの型（スカラー、ベクトル、行列）を取得
   */
  public TMemoryType getOutMemoryType() {
    return fOutMemoryType;
  }

  /**
   * 直接の親ノードを取得
   */
  public ArrayList<TNode> getParentNodes() {
    return fParentNodes;
  }

  public int getNumOfParentNodes() {
    return fParentNodes.size();
  }

  public void resetChildNodes(int length) {
    fChildNodes = new TNode[length];
  }

  /**
   * 直接の子ノードを取得
   */
  public TNode[] getChildNodes() {
    return fChildNodes;
  }

  public int getNotNullChildNodeCount() {
    int count = 0;
    for (TNode childNode : fChildNodes) {
      if (childNode != null) {
        count++;
      }
    }
    return count;
  }

  /**
   * i番目の子ノードを取得
   */
  public TNode getChildNode(int index) {
    return fChildNodes[index];
  }

  public int getChildNodeIndex(TNode childNode) {
    for (int i = 0; i < fChildNodes.length; i++) {
      if (fChildNodes[i] == childNode) {
        return i;
      }
    }
    return -1;
  }

  public ArrayList<Integer> getAllChildNodeIndexes(TNode childNode) {
    ArrayList<Integer> indexes = new ArrayList<Integer>();
    for (int i = 0; i < fChildNodes.length; i++) {
      if (fChildNodes[i] == childNode) {
        indexes.add(i);
      }
    }
    return indexes;
  }

  public int getRandomChildNodeIndexOf(TNode childNode, TRandomGenerator rand) {
    ArrayList<Integer> indexes = getAllChildNodeIndexes(childNode);
    if (indexes.size() == 0) {
      return -1;
    }
    return indexes.get(rand.nextInt(indexes.size()));
  }

  /**
   * i番目の子ノードを設定
   */
  public void setChildNode(int index, TNode childNode) {
    if (fChildNodes[index] != null) {
      throw new RuntimeException("Child node already exists at index " + index);
    }
    // 親ノードは重複しないようにする
    if (!childNode.fParentNodes.contains(this)) {
      childNode.fParentNodes.add(this);
    }
    fChildNodes[index] = childNode;
  }

  /**
   * i番目の子ノードをnullにリセット
   */
  public void detachFromChildNode(int index) {
    if (fChildNodes[index] == null) {
      throw new RuntimeException("Child reset child node " + index);
    }

    int nodeCount = 0;
    for (TNode childNode : fChildNodes) {
      if (childNode == fChildNodes[index]) {
        nodeCount++;
      }
    }

    // 親ノードは重複しないので、他のChildNodeで利用されている場合は消さないようにする
    if (nodeCount == 1) {
      fChildNodes[index].fParentNodes.remove(this);
    }

    fChildNodes[index] = null;
  }

  public void detachFromChildNodes() {
    for (int childNodeIndex = 0; childNodeIndex < getChildNodes().length; childNodeIndex++) {
      TNode child = getChildNode(childNodeIndex);
      if (child != null) {
        detachFromChildNode(childNodeIndex);
      }
    }
  }

  public void detachFromParentNodes() {
    for (TNode parentNode : getParentNodes()) {
      for (int parentChildIndex: parentNode.getAllChildNodeIndexes(this)) {
        parentNode.detachFromChildNode(parentChildIndex);
      }
    }
  }

  /**
   * 祖先ノード（直接の親ノードを含む）を与えられたArrayListに追加
   */
  public void addAncestorNodesTo(ArrayList<TNode> ancestorNodes) {
    for (TNode parentNode : fParentNodes) {
      if (!ancestorNodes.contains(parentNode)) {
        ancestorNodes.add(parentNode);
        parentNode.addAncestorNodesTo(ancestorNodes);
      }
    }
  }

  /**
   * 祖先ノード（直接の親ノードを含む）の取得
   */
  public ArrayList<TNode> getAncestorNodes() {
    ArrayList<TNode> ancestorNodes = new ArrayList<TNode>();
    addAncestorNodesTo(ancestorNodes);
    return ancestorNodes;
  }

  /**
   * 子孫ノード（直接の子ノードを含む）を与えられたArrayListに追加
   */
  public void addDescendantNodesTo(ArrayList<TNode> descendantNodes) {
    for (TNode childNode : fChildNodes) {
      if (childNode == null)
        continue;
      if (!descendantNodes.contains(childNode)) {
        descendantNodes.add(childNode);
        childNode.addDescendantNodesTo(descendantNodes);
      }
    }
  }

  /**
   * 子孫ノード（直接の子ノードを含む）を与えられたArrayListに追加
   */
  public ArrayList<TNode> getDescendantNodes() {
    ArrayList<TNode> descendantNodes = new ArrayList<TNode>();
    addDescendantNodesTo(descendantNodes);
    return descendantNodes;
  }

  /**
   * 子孫ノード（直接の子ノードを含む）を与えられたArrayListに追加
   */
  public void addPredictLabelStoppedDescendantNodesTo(ArrayList<TNode> descendantNodes) {
    for (TNode childNode : fChildNodes) {
      if (childNode == null)
        continue;
      if (descendantNodes.contains(childNode)) {
        continue;
      }
      descendantNodes.add(childNode);
      if (childNode instanceof TPredictRootNode) {
        continue;
      }
      childNode.addPredictLabelStoppedDescendantNodesTo(descendantNodes);
    }
  }

  /**
   * 子孫ノード（直接の子ノードを含む）を与えられたArrayListに追加
   */
  public ArrayList<TNode> getPredictLabelStoppedDescendantNodes() {
    ArrayList<TNode> descendantNodes = new ArrayList<TNode>();
    addPredictLabelStoppedDescendantNodesTo(descendantNodes);
    return descendantNodes;
  }

  /**
   * 子孫ノード（直接の子ノードを含む）の中から終端ノードを取得
   * 終端ノードとは、最終的にグラフが完成した際に子ノードを持たないノードであり、TOpNodeを除いたノードを意味する。
   * TOpNodeは構成中の時に、一時的に子ノードを持たない状況は存在するが、終端ノードとは扱わないことに注意。
   */
  public ArrayList<TNode> getTerminalNodes() {
    ArrayList<TNode> terminalNodes = new ArrayList<TNode>();
    for (TNode node : getDescendantNodes()) {
      if (!(node instanceof TOpNode))
        terminalNodes.add(node);
    }
    return terminalNodes;
  }

  /**
   * 子孫ノード（直接の子ノードを含む）の中から定数ノードを取得して、与えられたArrayListに追加
   */
  public void addDescendantConstSetNodes(ArrayList<TConstSetNode> nodes) {
    for (TNode node : getDescendantNodes()) {
      if (node instanceof TConstSetNode) {
        nodes.add((TConstSetNode) node);
      }
    }
  }

  /**
   * 子孫ノード（直接の子ノードを含む）の中から定数ノードを取得
   */
  public ArrayList<TConstSetNode> getDescendantConstSetNodes() {
    ArrayList<TConstSetNode> constSetNodes = new ArrayList<TConstSetNode>();
    addDescendantConstSetNodes(constSetNodes);
    return constSetNodes;
  }

  /**
   * 子孫ノード（直接の子ノードを含む）の中から学習パラメータを取得して、与えられたArrayListに追加
   */
  public void addDescendantParameterNodes(ArrayList<TParameterNode> nodes) {
    for (TNode node : getDescendantNodes()) {
      if (node instanceof TParameterNode) {
        nodes.add((TParameterNode) node);
      }
    }
  }

  /**
   * 子孫ノード（直接の子ノードを含む）の中から命令ノードを取得して、与えられたArrayListに追加
   */
  public void addDescendantOpNodesTo(ArrayList<TOpNode> nodes) {
    for (TNode node : getDescendantNodes()) {
      if (node instanceof TOpNode) {
        nodes.add((TOpNode) node);
      }
    }
  }

  /**
   * 子孫ノード（直接の子ノードを含む）の中から命令ノードを取得
   */
  public ArrayList<TOpNode> getChildOpNodes() {
    ArrayList<TOpNode> nodes = new ArrayList<TOpNode>();
    for (TNode node : fChildNodes) {
      if (node instanceof TOpNode) {
        nodes.add((TOpNode)node);
      }
    }
    return nodes;
  }

  /**
   * 子孫ノード（直接の子ノードを含む）の中から命令ノードを取得
   */
  public ArrayList<TOpNode> getDescendantOpNodes() {
    ArrayList<TOpNode> nodes = new ArrayList<TOpNode>();
    addDescendantOpNodesTo(nodes);
    return nodes;
  }

  /**
   * 自身と子孫ノード（直接の子ノードを含む）の中からノードを取得
   */
  public ArrayList<TNode> getSelfAndDescendantNodes() {
    ArrayList<TNode> nodes = new ArrayList<TNode>();
    nodes.add(this);
    addDescendantNodesTo(nodes);
    return nodes;
  }

  /**
   * 自身と子孫ノード（直接の子ノードを含む）の中から命令ノードを取得
   */
  public ArrayList<TOpNode> getSelfAndDescendantOpNodes() {
    ArrayList<TOpNode> nodes = new ArrayList<TOpNode>();
    if(this instanceof TOpNode){
      nodes.add((TOpNode)this);
    }
    addDescendantOpNodesTo(nodes);
    return nodes;
  }

  /**
   * 子孫ノード（直接の子ノードを含む）の中から学習パラメータのノードを取得
   */
  public ArrayList<TParameterNode> getDescendantParameterNodes() {
    ArrayList<TParameterNode> parameterSetNodes = new ArrayList<TParameterNode>();
    addDescendantParameterNodes(parameterSetNodes);
    return parameterSetNodes;
  }

  /**
   * 複数の親ノードを持つかどうか
   */
  public boolean hasMultipleParents() {
    return fParentNodes.size() > 1;
  }

  /**
   * 出力先の変数を取得
   */
  public TVariable getVariable(TVariablesManager variablesManager) {
    return variablesManager.getVariable(this);
  }

  public int getDepth() {
    int depth = Integer.MAX_VALUE;
    ArrayList<TNode> nodes = getAncestorNodes();
    for (TNode node : nodes) {
      if (node instanceof TParameterRootNode) {
        TParameterRootNode parameterRootNode = (TParameterRootNode) node;
        if (depth < parameterRootNode.getDepth()) {
          depth = parameterRootNode.getDepth();
        }
        return parameterRootNode.getDepth();
      } else if (node instanceof TPredictRootNode) {
        return 0;
      }
    }
    return depth;
  }

  /**
   * グラフからノードを除去する
   * 親ノードの子ノードから、子ノードの親ノードから自身を切り離す
   */
  public void detach() {
    detachFromParentNodes();
    detachFromChildNodes();
  }

  abstract public String toString(int indent, ArrayList<TNode> processedNodes, TVariablesManager variablesManager);

  public String toString(TMemory memory) {
    TVariablesManager variablesManager = new TVariablesManager(memory);
    return toString(0, new ArrayList<TNode>(), variablesManager);
  };

  public String toString(TVariablesManager variablesManager) {
    return toString(0, new ArrayList<TNode>(), variablesManager);
  };

  public String toString(ArrayList<TNode> stopNodes, TVariablesManager variablesManager) {
    ArrayList<TNode> processedNodes = new ArrayList<TNode>();
    processedNodes.addAll(stopNodes);
    return toString(0, processedNodes, variablesManager);
  };

  public boolean completelyEquals(Object obj) {
    if (obj == this)
      return true;
    if (!(obj instanceof TNode))
      return false;
    TNode node = (TNode) obj;
    return getCode() == node.getCode();
  }

  public boolean discreteEquals(Object obj) {
    if (obj == this)
      return true;
    if (!(obj instanceof TNode))
      return false;
    TNode node = (TNode) obj;
    return getDiscreteCode() == node.getDiscreteCode();
  }

}
