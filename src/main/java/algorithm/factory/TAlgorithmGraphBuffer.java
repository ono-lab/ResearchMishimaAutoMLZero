package algorithm.factory;

import java.util.ArrayList;
import java.util.HashMap;

import algorithm.core.TAlgorithmGraph;
import algorithm.spec.TAlgorithmGraphSpec;
import memory.TMemory;
import memory.TVariable;
import node.*;

public class TAlgorithmGraphBuffer {
  private ArrayList<TNode> fAllNodes = new ArrayList<TNode>();;
  private ArrayList<TNode> fAllTerminalNodes = new ArrayList<TNode>();;

  // Predictのルートに存在する命令のノード
  private TPredictRootNode fPredictRootNode;

  // ルートに存在するパラメータのノード
  private ArrayList<TParameterRootNode> fParameterRootNodes = new ArrayList<TParameterRootNode>();

  private TVariableNode fInputVectorVariableNode;
  private TVariableNode fCorrectLabelVariableNode;

  // パラメータノード
  private HashMap<Integer, ArrayList<TParameterNode>> fParameterNodes = new HashMap<Integer, ArrayList<TParameterNode>>();

  private TMemory fMemory;

  TAlgorithmGraphBuffer(TMemory memory) {
    fMemory = memory;
  }

  TAlgorithmGraphBuffer(TMemory memory, TPredictRootNode predictRootNode,
      ArrayList<TParameterRootNode> parameterRootNodes) {
    this(memory);
    ArrayList<TNode> predictRootNodeWithDescendantNodes = predictRootNode.getSelfAndDescendantNodes();
    addNodesIfNotExists(predictRootNodeWithDescendantNodes);
    for (TNode parameterRootNode : parameterRootNodes) {
      ArrayList<TNode> parameterRootNodeWithDescendantNodes = parameterRootNode.getSelfAndDescendantNodes();
      addNodesIfNotExists(parameterRootNodeWithDescendantNodes);
    }
  }

  ArrayList<TNode> getNodes() {
    return fAllNodes;
  }

  private void addToAllNode(TNode node) {
    fAllNodes.add(node);
  }

  ArrayList<TNode> getTerminalNodes() {
    return fAllTerminalNodes;
  }

  private void addToTerminalNodes(TTerminalNode node) {
    fAllTerminalNodes.add(node);
  }

  TPredictRootNode getPredictRootNode() {
    return fPredictRootNode;
  }

  void setPredictRootNode(TPredictRootNode predictRootNode) {
    if (fPredictRootNode != null) {
      throw new RuntimeException("PredictRootNode is already set");
    }
    fPredictRootNode = predictRootNode;
  }

  ArrayList<TParameterRootNode> getParameterRootNodes() {
    return fParameterRootNodes;
  }

  void addToParameterRootNode(TParameterRootNode parameterRootNode) {
    fParameterRootNodes.add(parameterRootNode);
  }

  ArrayList<TParameterNode> getParameterNodes(int depth) {
    return fParameterNodes.get(depth);
  }

  ArrayList<TParameterRootNode> getParameterRootNodes(int depth) {
    ArrayList<TParameterRootNode> nodes = new ArrayList<TParameterRootNode>();
    for (TParameterRootNode node : fParameterRootNodes) {
      if (node.getDepth() == depth) {
        nodes.add(node);
      }
    }
    return nodes;
  }

  private void addToParameterNode(TParameterNode node) {
    int depth = node.getDepth();
    if (fParameterNodes.containsKey(depth)) {
      fParameterNodes.get(depth).add(node);
    } else {
      ArrayList<TParameterNode> nodes = new ArrayList<TParameterNode>();
      nodes.add(node);
      fParameterNodes.put(depth, nodes);
    }
  }

  TVariable makePredictionLabelVariable() {
    return TVariable.getPredictionLabelVariable(fMemory);
  }

  TVariableNode getInputVectorVariableNode() {
    return fInputVectorVariableNode;
  }

  private void setInputVectorVariableNode(TVariableNode inputVectorVariableNode) {
    fInputVectorVariableNode = inputVectorVariableNode;
  }

  TVariableNode addInputVectorVariableNode() {
    if (fInputVectorVariableNode != null) {
      throw new RuntimeException("InputVectorVariableNode is already set");
    }
    addNode(new TVariableNode(TVariable.getVectorInputVariable(fMemory)));
    return fInputVectorVariableNode;
  }

  TVariableNode getCorrectLabelVariableNode() {
    return fCorrectLabelVariableNode;
  }

  private void setCorrectLabelVariableNode(TVariableNode correctLabelVariableNode) {
    fCorrectLabelVariableNode = correctLabelVariableNode;
  }

  TVariableNode addCorrectLabelVariableNode() {
    if (fCorrectLabelVariableNode != null) {
      throw new RuntimeException("CorrectLabelVariableNode is already set");
    }
    addNode(new TVariableNode(TVariable.getCorrectLabelVariable(fMemory)));
    return fCorrectLabelVariableNode;
  }

  TVariableNode addCorrectLabelVariableNodeIfNotExists() {
    if (fCorrectLabelVariableNode != null) {
      return fCorrectLabelVariableNode;
    }
    return addCorrectLabelVariableNode();
  }

  void addNodeIfNotExists(TNode node) {
    if (fAllNodes.contains(node)) {
      return;
    }
    addNode(node);
  }

  <T extends TNode> void addNodesIfNotExists(ArrayList<T> nodes) {
    for (TNode node : nodes) {
      addNodeIfNotExists(node);
    }
  }

  void addNode(TNode node) {
    addToAllNode(node);
    if (node instanceof TTerminalNode) {
      addToTerminalNodes((TTerminalNode) node);
    }
    if (node instanceof TParameterNode) {
      addToParameterNode((TParameterNode) node);
    }
    if (node instanceof TParameterRootNode) {
      addToParameterRootNode((TParameterRootNode) node);
    }
    if (node instanceof TPredictRootNode) {
      setPredictRootNode((TPredictRootNode) node);
    }
    if (node instanceof TVariableNode) {
      TVariableNode variableNode = (TVariableNode) node;
      if (fMemory.isScalarCorrectLabelVariable(variableNode.getVariable())) {
        setCorrectLabelVariableNode(variableNode);
      }
      if (fMemory.isVectorInputVariable(variableNode.getVariable())) {
        setInputVectorVariableNode(variableNode);
      }
    }
  }

  <T extends TNode> void addNodes(ArrayList<T> nodes) {
    for (TNode node : nodes) {
      addNode(node);
    }
  }

  TAlgorithmGraph toAlgorithmGraph(TAlgorithmGraphSpec spec) {
    return new TAlgorithmGraph(fPredictRootNode, fParameterRootNodes, spec);
  }

  int getOpsCount(int depth) {
    int count = 0;
    for (TNode node : fAllNodes) {
      if (node instanceof TOpNode && node.getDepth() == depth) {
        count++;
      }
    }
    return count;
  }

  int getParametersCount(int depth) {
    ArrayList<TParameterNode> parameterNodes = fParameterNodes.get(depth);
    if (parameterNodes == null) {
      return 0;
    }
    return parameterNodes.size();
  }
}
