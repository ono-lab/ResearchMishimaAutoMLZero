package algorithm.core;

import java.util.ArrayList;
import java.util.HashMap;

import algorithm.spec.TAlgorithmGraphSpec;
import memory.TMemory;
import mutator.proposed.TAlgorithmGraphMutation;
import node.*;
import utils.THashGenerator;
import utils.TRandomGenerator;

public class TAlgorithmGraph {
  private TAlgorithmGraphSpec fSpec;
  private TPredictRootNode fPredictRootNode;
  private ArrayList<TParameterRootNode> fParameterRootNodes = new ArrayList<TParameterRootNode>();
  private TAlgorithmGraphMutation fMutation;

  public TAlgorithmGraph(TPredictRootNode predictRootNode, ArrayList<TParameterRootNode> lpRootNodes) {
    fPredictRootNode = predictRootNode;
    fParameterRootNodes = lpRootNodes;
  }

  public TAlgorithmGraph(TPredictRootNode predictRootNode, ArrayList<TParameterRootNode> parameterRootNodes,
      TAlgorithmGraphSpec spec) {
    this(predictRootNode, parameterRootNodes);
    fSpec = spec;
  }

  public TAlgorithmGraph(TAlgorithmGraph graph) {
    fSpec = graph.fSpec;
    HashMap<Integer, TNode> copied = new HashMap<Integer, TNode>();
    fPredictRootNode = graph.copyPredictRootNode(copied);
    fParameterRootNodes = graph.copyParameterRootNodes(copied);
  }

  public TAlgorithmGraphMutation getMutation() {
    return fMutation;
  }

  public void setMutation(TAlgorithmGraphMutation mutation) {
    fMutation = mutation;
  }

  public TAlgorithmGraph copy() {
    return new TAlgorithmGraph(this);
  }

  public long getCode() {
    String code = "";
    code += fPredictRootNode.getCode();
    code += "-";
    for (TOpNode node : fParameterRootNodes) {
      code += node.getCode();
    }
    return THashGenerator.getStringHash(code);
  }

  public long getDiscreteCode() {
    String code = "";
    code += fPredictRootNode.getDiscreteCode();
    code += "-";
    for (TOpNode node : fParameterRootNodes) {
      code += node.getDiscreteCode();
    }
    return THashGenerator.getStringHash(code);
  }

  public TPredictRootNode getPredictRootNode() {
    return fPredictRootNode;
  }

  public ArrayList<TParameterRootNode> getParameterRootNodes() {
    return fParameterRootNodes;
  }

  public ArrayList<TParameterRootNode> getParameterRootNodes(int depth) {
    ArrayList<TParameterRootNode> nodes = new ArrayList<TParameterRootNode>();
    for (TParameterRootNode node : fParameterRootNodes) {
      if (node.getDepth() == depth) {
        nodes.add(node);
      }
    }
    return nodes;
  }

  public TPredictRootNode copyPredictRootNode(HashMap<Integer, TNode> copied) {
    return fPredictRootNode.copy(copied);
  }

  public ArrayList<TParameterRootNode> copyParameterRootNodes(HashMap<Integer, TNode> copied) {
    ArrayList<TParameterRootNode> nodes = new ArrayList<TParameterRootNode>();
    for (TParameterRootNode node : fParameterRootNodes) {
      nodes.add(node.copy(copied));
    }
    return nodes;
  }

  public ArrayList<TConstSetNode> getConstSetNodes() {
    ArrayList<TConstSetNode> constSetNodes = new ArrayList<TConstSetNode>();
    fPredictRootNode.addDescendantConstSetNodes(constSetNodes);
    for (TOpNode node : fParameterRootNodes) {
      node.addDescendantConstSetNodes(constSetNodes);
    }
    return constSetNodes;
  }

  public ArrayList<TParameterNode> getParameterNodes() {
    ArrayList<TParameterNode> parameterNodes = new ArrayList<TParameterNode>();
    fPredictRootNode.addDescendantParameterNodes(parameterNodes);
    for (TOpNode node : fParameterRootNodes) {
      node.addDescendantParameterNodes(parameterNodes);
    }
    return parameterNodes;
  }

  public TOpNode getRandomRootNode(TRandomGenerator rand) {
    int random = rand.nextInt(fParameterRootNodes.size() + 1);
    if (random == fParameterRootNodes.size()) {
      return fPredictRootNode;
    } else {
      return fParameterRootNodes.get(random);
    }
  }

  public ArrayList<TOpNode> getOpNodes() {
    ArrayList<TOpNode> nodes = new ArrayList<TOpNode>();
    for (TOpNode opNode : getPredictRootNode().getSelfAndDescendantOpNodes()) {
      if (!nodes.contains(opNode)) {
        nodes.add(opNode);
      }
    }
    for (TOpNode rootNode : getParameterRootNodes()) {
      for (TOpNode opNode : rootNode.getSelfAndDescendantOpNodes()) {
        if (!nodes.contains(opNode)) {
          nodes.add(opNode);
        }
      }
    }
    return nodes;
  }

  public ArrayList<TNode> getTerminalNodes() {
    ArrayList<TNode> nodes = new ArrayList<TNode>();
    for (TNode node : getPredictRootNode().getTerminalNodes()) {
      if (!nodes.contains(node)) {
        nodes.add(node);
      }
    }
    for (TOpNode rootNode : getParameterRootNodes()) {
      for (TNode node : rootNode.getTerminalNodes()) {
        if (!nodes.contains(node)) {
          nodes.add(node);
        }
      }
    }
    return nodes;
  }

  public ArrayList<TNode> getAllNodes() {
    ArrayList<TNode> nodes = new ArrayList<TNode>();
    nodes.add(fPredictRootNode);
    for (TNode node : fPredictRootNode.getDescendantNodes()) {
      if (!nodes.contains(node)) {
        nodes.add(node);
      }
    }
    for (TOpNode rootNode : getParameterRootNodes()) {
      if (!nodes.contains(rootNode)) {
        nodes.add(rootNode);
      }
      for (TNode node : rootNode.getDescendantNodes()) {
        if (!nodes.contains(node)) {
          nodes.add(node);
        }
      }
    }
    return nodes;
  }

  public ArrayList<TNode> getAllNodesWithMaxDepth(int maxDepth) {
    ArrayList<TNode> nodes = new ArrayList<TNode>();
    for (TNode node : getAllNodes()) {
      if (node.getDepth() <= maxDepth) {
        nodes.add(node);
      }
    }
    return nodes;
  }

  public ArrayList<TNode> getConnectedNodes() {
    ArrayList<TNode> nodes = new ArrayList<TNode>();
    for (TNode node : getAllNodes()) {
      if (node.hasMultipleParents()) {
        nodes.add(node);
      } else if (node instanceof TPredictRootNode && node.getParentNodes().size() > 0) {
        nodes.add(node);
      } else if (node instanceof TParameterRootNode && node.getParentNodes().size() > 0) {
        nodes.add(node);
      }
    }
    return nodes;
  }

  public boolean validate(TMemory memory) {
    return TAlgorithmGraphValidator.validate(this, memory);
  }

  public boolean validate(TMemory memory, boolean log) {
    return TAlgorithmGraphValidator.validate(this, memory, log);
  }

  public TAlgorithmGraphSpec getSpec() {
    return fSpec;
  }

  public TAlgorithm toAlgorithm(TVariablesManager variablesManager) {
    return TAlgorithmGraphConverter.convert(this, variablesManager);
  }

  public String toString(TVariablesManager variablesManager) {
    ArrayList<TNode> processedNodes = new ArrayList<TNode>();
    String str = "";
    str += "previous_mutation: " + fMutation + "\n";
    str += fPredictRootNode.getVariable(variablesManager).toString();
    str += ": ";
    str += fPredictRootNode.toString(0, processedNodes, variablesManager);
    for (TOpNode node : fParameterRootNodes) {
      str += "\n";
      str += node.getVariable(variablesManager).toString();
      str += ": ";
      str += node.toString(0, processedNodes, variablesManager);
    }
    return str;
  }

  public boolean completelyEquals(Object obj) {
    if (obj == this)
      return true;
    if (!(obj instanceof TAlgorithmGraph))
      return false;
    TAlgorithmGraph graph = (TAlgorithmGraph) obj;
    return getCode() == graph.getCode();
  }

  public boolean discreteEquals(Object obj) {
    if (obj == this)
      return true;
    if (!(obj instanceof TAlgorithmGraph))
      return false;
    TAlgorithmGraph graph = (TAlgorithmGraph) obj;
    return getDiscreteCode() == graph.getDiscreteCode();
  }
}
