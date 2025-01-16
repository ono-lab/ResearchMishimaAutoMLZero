package node;

import java.util.ArrayList;
import java.util.HashMap;

import instruction.*;
import memory.*;
import utils.TRandomGenerator;

public class TOpNode extends TNode {
  private TOp fOp;
  private TMemoryType[] fChildNodeMemoryTypes;

  public TOpNode(TOp op, TMemoryType outMemoryType) {
    super(outMemoryType, op.getInNum());
    fChildNodeMemoryTypes = new TMemoryType[op.getInNum()];
    for (int i = 0; i < op.getInNum(); i++) {
      switch (i) {
        case 0:
          fChildNodeMemoryTypes[i] = op.getIn1MemoryType();
          break;
        case 1:
          fChildNodeMemoryTypes[i] = op.getIn2MemoryType();
          break;
        default:
          throw new RuntimeException("Invalid input number");
      }
    }
    if (op.isAssignment()) {
      throw new RuntimeException("Assignment operator not allowed");
    }
    if (op.getOutMemoryType() != outMemoryType) {
      throw new RuntimeException("Output memory type mismatch");
    }
    fOp = op;
  }

  public TOpNode(TOpNode node) {
    super(node);
    fOp = node.fOp;
    fChildNodeMemoryTypes = new TMemoryType[node.fChildNodeMemoryTypes.length];
    for (int i = 0; i < node.fChildNodeMemoryTypes.length; i++) {
      fChildNodeMemoryTypes[i] = node.fChildNodeMemoryTypes[i];
    }
  }

  @Override
  public TOpNode copyImpl(HashMap<Integer, TNode> copied) {
    return new TOpNode(this);
  };

  @Override
  public TOpNode copy(HashMap<Integer, TNode> copied) {
    return (TOpNode) super.copy(copied);
  }

  @Override
  public String getDiscreteCodeImpl(){
    String code = super.getDiscreteCodeImpl();
    code += "-";
    code += fOp;
    return code;
  }

  /**
   * 命令の取得
   */
  public TOp getOp() {
    return fOp;
  }

  /**
   * 命令のリセット
   */
  public void resetOp(TOp op) {
    if(getOutMemoryType() != op.getOutMemoryType()){
      throw new RuntimeException("Output memory type mismatch");
    }
    fOp = op;
    fChildNodeMemoryTypes = new TMemoryType[op.getInNum()];
    for (int i = 0; i < op.getInNum(); i++) {
      switch (i) {
        case 0:
          fChildNodeMemoryTypes[i] = op.getIn1MemoryType();
          break;
        case 1:
          fChildNodeMemoryTypes[i] = op.getIn2MemoryType();
          break;
        default:
          throw new RuntimeException("Invalid input number");
      }
    }
    for (TNode child : getChildNodes()) {
      if (child != null) {
        throw new RuntimeException("Input memory type mismatch");
      }
    }
    resetChildNodes(op.getInNum());
    if (op.isAssignment()) {
      throw new RuntimeException("Assignment operator not allowed");
    }
    if (op.getOutMemoryType() != getOutMemoryType()) {
      throw new RuntimeException("Output memory type mismatch");
    }
  }


  /**
   * 命令のリセット
   */
  public void resetOp(TOp op, TMemoryType outMemoryType) {
    setOutMemoryType(outMemoryType);
    resetOp(op);
  }

  /**
   * 子ノードに設定可能なメモリの型を取得
   */
  public TMemoryType[] getChildNodeMemoryTypes() {
    return fChildNodeMemoryTypes;
  }

  /**
   * 特定の型の未設定の子ノードを持つ子孫ノード（直接の子ノードを含む）を取得し、与えらたArrayListに追加
   */
  private void addAvailableDescendantNodesTo(ArrayList<TOpNode> nodes, TMemoryType memoryType,
      ArrayList<TOpNode> processedNodes) {
    TNode[] childNodes = getChildNodes();
    boolean isAvailable = false;
    for (int i = 0; i < childNodes.length; i++) {
      TNode childNode = childNodes[i];
      if (childNode == null &&
          (memoryType == fChildNodeMemoryTypes[i] || memoryType == null)) {
        isAvailable = true;
      } else if (childNode instanceof TOpNode) {
        TOpNode opNode = (TOpNode) childNode;
        if (!processedNodes.contains(opNode)) {
          processedNodes.add(opNode);
          opNode.addAvailableDescendantNodesTo(nodes, memoryType, processedNodes);
        }
      }
    }
    if (isAvailable && nodes.contains(this) == false) {
      nodes.add(this);
    }
  }

  private void addAvailableDescendantNodesTo(ArrayList<TOpNode> nodes, TMemoryType memoryType) {
    ArrayList<TOpNode> processedNodes = new ArrayList<TOpNode>();
    addAvailableDescendantNodesTo(nodes, memoryType, processedNodes);
  }

  /**
   * 特定の型の未設定の子ノードを持つ子孫ノード（直接の子ノードを含む）を取得
   */
  public ArrayList<TOpNode> getAvailableDescendantNodes(TMemoryType memoryType) {
    ArrayList<TOpNode> nodes = new ArrayList<TOpNode>();
    addAvailableDescendantNodesTo(nodes, memoryType);
    return nodes;
  }

  /**
   * 未設定の子ノードを持つ子孫ノード（直接の子ノードを含む）を取得
   */
  public ArrayList<TOpNode> getAvailableDescendantNodes() {
    ArrayList<TOpNode> nodes = new ArrayList<TOpNode>();
    addAvailableDescendantNodesTo(nodes, null);
    return nodes;
  }

  /**
   * 特定の型の未設定の子ノードの個数を取得
   */
  public int getAvailableChildrenNum(TMemoryType memoryType) {
    int count = 0;
    TNode[] childNodes = getChildNodes();
    for (int i = 0; i < childNodes.length; i++) {
      TNode childNode = childNodes[i];
      if (childNode == null &&
          (memoryType == fChildNodeMemoryTypes[i] || memoryType == null)) {
        count++;
      }
    }
    return count;
  }

  /**
   * 未設定の子ノードの個数を取得
   */
  public int getAvailableChildrenNum() {
    return getAvailableChildrenNum(null);
  }

  /**
   * 特定の型の未設定の子孫ノード（直接の子ノードを含む）の個数を取得
   */
  public int getAvailableDescendantNodesNum(TMemoryType memoryType) {
    ArrayList<TOpNode> nodes = getAvailableDescendantNodes(memoryType);
    int count = 0;
    for (TOpNode node : nodes) {
      count += node.getAvailableChildrenNum(memoryType);
    }
    return count;
  }

  /**
   * 未設定の子孫ノード（直接の子ノードを含む）の個数を取得
   */
  public int getAvailableDescendantNodesNumNum() {
    return getAvailableDescendantNodesNum(null);
  }

  /**
   * 未設定の子孫ノードを持つ子ノードのインデックスをランダムに取得
   */
  private int randomChildIndexHasAvailableDescendant(TRandomGenerator rand) {
    int growableNum = getAvailableDescendantNodesNumNum();
    if (growableNum < 1) {
      throw new RuntimeException("No nodes are available for grow.");
    }
    int random = rand.nextInt(growableNum);
    TNode[] childNodes = getChildNodes();
    int current = 0;
    int childIndex = 0;
    for (childIndex = 0; childIndex < childNodes.length; childIndex++) {
      TNode childNode = childNodes[childIndex];
      if (childNode == null) {
        current += 1;
      } else if (childNode instanceof TOpNode) {
        TOpNode childOpNode = (TOpNode) childNode;
        current += childOpNode.getAvailableDescendantNodesNumNum();
      }
      if (current > random)
        break;
    }
    return childIndex;
  }

  /**
   * i番目の子ノードを設定
   */
  @Override
  public void setChildNode(int index, TNode childNode) {
    if (childNode.getOutMemoryType() != fChildNodeMemoryTypes[index]) {
      throw new RuntimeException("Input memory type mismatch");
    }
    super.setChildNode(index, childNode);
  }

  /**
   * 未設定の子ノードにノードを設定
   */
  public void setChildNodeRandomly(TNode childNode, TRandomGenerator rand) {
    TMemoryType childNodeMemoryType = childNode.getOutMemoryType();
    int availableNum = getAvailableChildrenNum(childNodeMemoryType);
    if (availableNum == 0)
      throw new RuntimeException("No available child node");
    int random = rand.nextInt(availableNum);
    int index = 0;
    for (index = 0; index < fChildNodeMemoryTypes.length; index++) {
      if (fChildNodeMemoryTypes[index] != childNodeMemoryType || getChildNode(index) != null) {
        continue;
      }
      if (random == 0)
        break;
      random--;
    }
    setChildNode(index, childNode);
  }

  /**
   * 未設定の子ノードをランダムに選択し、学習パラメータを設定
   */
  public TParameterNode setChildParameterNodeRandomly(int dim, TRandomGenerator rand) {
    int availableNum = getAvailableChildrenNum();
    if (availableNum == 0)
      throw new RuntimeException("No available child node");
    int random = rand.nextInt(availableNum);
    int index = 0;
    for (index = 0; index < fChildNodeMemoryTypes.length; index++) {
      if (getChildNode(index) != null) {
        continue;
      }
      if (random == 0)
        break;
      random--;
    }
    TMemoryType memoryType = fChildNodeMemoryTypes[index];
    TParameterNode lpNode = TParameterNode.create(memoryType, dim, rand);
    setChildNode(index, lpNode);
    return lpNode;
  }

  /**
   * nodeの出力と同じメモリの型を持つ子ノードが未設定の子孫ノードをランダムに選択し、当該子孫ノードの子ノードにnodeを設定
   */
  public void setDescendantNodeRandomly(TNode node, TRandomGenerator rand) {
    ArrayList<TOpNode> availableNodes = getAvailableDescendantNodes(node.getOutMemoryType());
    if (availableNodes.size() == 0) {
      throw new RuntimeException("No available nodes for " + node.getOutMemoryType());
    }
    TOpNode targetNode = availableNodes.get(rand.nextInt(availableNodes.size()));
    targetNode.setChildNodeRandomly(node, rand);
  }

  /**
   * nodeの出力と同じメモリの型を持つ子ノードが未設定の子孫ノードをランダムに選択し、当該子孫ノードの子ノードにnodeを設定
   * ただし、nodeが子孫ノードに依存している場合は何もしない
   */
  public void setDescendantNodeRandomlyIfNotDepended(TNode node, TRandomGenerator rand) {
    ArrayList<TNode> dependantNodes = getPredictLabelStoppedDescendantNodes();
    if(dependantNodes.contains(node))
      return;
    setDescendantNodeRandomly(node, rand);
  }

  /**
   * 未設定の子ノードを持つ子孫ノードをランダムに選択し、当該子孫ノードの子ノードに学習パラメータを設定
   */
  public TParameterNode setDescendantParameterNodeRandomly(int dim, TRandomGenerator rand) {
    ArrayList<TOpNode> availableNodes = getAvailableDescendantNodes();
    if (availableNodes.size() == 0) {
      throw new RuntimeException("No available nodes for learning parameter");
    }
    TOpNode targetNode = availableNodes.get(rand.nextInt(availableNodes.size()));
    return targetNode.setChildParameterNodeRandomly(dim, rand);
  }

  /**
   * 子孫ノードをランダムに追加し、追加したノードを返す
   */
  public TOpNode extendDescendant(TOpsSet opsSet, TRandomGenerator rand) {
    int childIndex = randomChildIndexHasAvailableDescendant(rand);
    TNode targetNode = getChildNode(childIndex);
    if(targetNode == null) {
      return extendChild(childIndex, opsSet, rand);
    } else if (targetNode instanceof TOpNode) {
      TOpNode targetOpNode = (TOpNode) targetNode;
      return targetOpNode.extendDescendant(opsSet, rand);
    } else {
      throw new RuntimeException("Invalid child node type");
    }
  }

  /**
   * i番目の子ノードをランダムに追加し、追加したノードを返す
   * ただし、既にi番目の子ノードが存在する場合は例外をスロー
   */
  private TOpNode extendChild(int childIndex, TOpsSet opsSet, TRandomGenerator rand) {
    TNode childNode = getChildNode(childIndex);
    if(childNode != null){
      throw new RuntimeException("Child node already exists at index " + childIndex);
    }
    TMemoryType memoryType = fChildNodeMemoryTypes[childIndex];
    TOp op = opsSet.getRandomOp(memoryType, rand);
    TOpNode newNode = new TOpNode(op, memoryType);
    setChildNode(childIndex, newNode);
    return newNode;
  }

  /**
   * 未設定の子ノードを指定されたノードのいずれかと接続
   */
  public void connect(ArrayList<TNode> nodes, TRandomGenerator rand) {
    for(int i = 0; i < getChildNodes().length; i++){
      if (getChildNode(i) == null) {
        connect(i, nodes, rand);
      }
    }
  }

  /**
   * i番目の子ノードを指定されたノードのいずれかと接続
   */
  public TNode connect(int childIndex, ArrayList<TNode> nodes, TRandomGenerator rand) {
    TNode childNode = getChildNode(childIndex);
    if (childNode != null) {
      throw new RuntimeException("Child node already exists at index " + childIndex);
    }
    ArrayList<TNode> connectableNodes = filterConnectableNodes(childIndex, nodes);
    if (connectableNodes.size() == 0)
      return null;
    int index = rand.nextInt(connectableNodes.size());
    TNode node = connectableNodes.get(index);
    setChildNode(childIndex, node);
    return node;
  }

  /**
   * 接続可能ノードの取得
   */
  public ArrayList<TNode> filterConnectableNodes(int childIndex, ArrayList<TNode> nodes) {
    TNode child = getChildNode(childIndex);
    TMemoryType memoryType = fChildNodeMemoryTypes[childIndex];
    ArrayList<TNode> ancestorNodes = getAncestorNodes();
    ArrayList<TNode> connectableNodes = new ArrayList<TNode>();
    for (TNode node : nodes) {
      if (node instanceof TParameterRootNode) {
        continue;
      }
      if (node.getOutMemoryType() != memoryType) {
        continue;
      }
      if (node == this || node == child || ancestorNodes.contains(node)) {
        continue;
      }
      connectableNodes.add(node);
    }
    return connectableNodes;
  }

  /**
   * 未設定の各子ノードを一定確率で指定されたノードのいずれかと接続
   */
  public void maybeConnect(double probability, ArrayList<TNode> nodes, TRandomGenerator rand){
    for (int i = 0; i < getChildNodes().length; i++) {
      if (getChildNode(i) == null) {
        maybeConnect(probability, i, nodes, rand);
      }
    }
  }

  /**
   * i番目の子ノードを一定確率で指定されたノードのいずれかと接続
   */
  public TNode maybeConnect(double probability, int childIndex, ArrayList<TNode> nodes, TRandomGenerator rand){
    return rand.nextFloat() < probability ? connect(childIndex, nodes, rand) : null;
  }

  /**
   * 子孫ノードをランダムに追加し、追加された子ノードに対して接続操作を適用し、追加したノードを返す
   */
  public TOpNode grow(TOpsSet opsSet, ArrayList<TNode> connectableNodes,
      double connectProbability, TRandomGenerator rand) {
    TOpNode opNode = extendDescendant(opsSet, rand);
    opNode.maybeConnect(connectProbability, connectableNodes, rand);
    return opNode;
  }

  /**
   * 未設定ノードを定数ノードで埋める
   */
  public ArrayList<TConstSetNode> fillAvailableDescendantNodesBySetNodes(int dim, TRandomGenerator rand) {
    ArrayList<TConstSetNode> setNodes = new ArrayList<TConstSetNode>();
    for (TOpNode availableNode : getAvailableDescendantNodes()) {
      TNode[] childNodes = availableNode.getChildNodes();
      TMemoryType[] childNodeMemoryTypes = availableNode.getChildNodeMemoryTypes();
      for (int i = 0; i < childNodes.length; i++) {
        TNode childNode = childNodes[i];
        if (childNode != null)
          continue;
        TMemoryType memoryType = childNodeMemoryTypes[i];
        switch (memoryType) {
          case SCALAR: {
            TConstSetNode setNode = new TScalarSetNode(rand);
            availableNode.setChildNode(i, setNode);
            setNodes.add(setNode);
            break;
          }
          case VECTOR: {
            TConstSetNode setNode = new TVectorSetNode(dim, rand);
            availableNode.setChildNode(i, setNode);
            setNodes.add(setNode);
            break;
          }
          case MATRIX: {
            TConstSetNode setNode = new TMatrixSetNode(dim, rand);
            availableNode.setChildNode(i, setNode);
            setNodes.add(setNode);
            break;
          }
        }
      }
    }
    return setNodes;
  }

  @Override
  public String toString(int indent, ArrayList<TNode> processedNodes, TVariablesManager variablesManager) {
    TVariable variable = getVariable(variablesManager);
    String space = "";
    for (int i = 0; i < indent; i++) {
      space += "  ";
    }
    String str = space;
    if (indent != 0) {
      str += "L ";
    }
    if (processedNodes.contains(this)) {
      str += variable + " (connected)";
      return str;
    }
    str += toInstruction(variablesManager);
    processedNodes.add(this);
    for (TNode childNode : getChildNodes()) {
      str += "\n";
      if (childNode != null) {
        str += childNode.toString(indent + 1, processedNodes, variablesManager);
      } else {
        str += space + "  L null";
      }
    }
    return str;
  };


  /**
   * 旧アルゴリズム表現の命令に変換する
   */
  public TInstruction toInstruction(TVariablesManager variablesManager) {
    TVariable variable = getVariable(variablesManager);
    switch (fOp.getInNum()) {
      case 1: {
        TNode in1 = getChildNode(0);
        return new TInstruction(fOp, in1 != null ? in1.getVariable(variablesManager).getAddress() : -1, variable.getAddress());
      }
      case 2: {
        TNode in1 = getChildNode(0);
        TNode in2 = getChildNode(1);
        return new TInstruction(
            fOp,
            in1 != null ? in1.getVariable(variablesManager).getAddress() : -1,
            in2 != null ? in2.getVariable(variablesManager).getAddress() : -1,
            variable.getAddress());
      }
      default:
        throw new RuntimeException("Invalid number of input nodes");
    }
  }

}
