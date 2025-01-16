package algorithm.core;

import java.util.ArrayList;

import memory.TMemory;
import memory.TVariable;
import node.TNode;
import node.TParameterNode;
import node.TParameterRootNode;
import node.TPredictRootNode;
import node.TVariableNode;

public class TAlgorithmGraphValidator {

  // 不適切な親子関係がないかの確認
  static private boolean validateParentChildrenRelations(TAlgorithmGraph graph, TMemory memory, boolean log) {
    ArrayList<TNode> allNodes = graph.getAllNodes();
    for (TNode node : allNodes) {
      ArrayList<TNode> ancestorNodes = node.getAncestorNodes();
      ArrayList<TNode> descendantNodes = node.getDescendantNodes();
      if (ancestorNodes.contains(node) || descendantNodes.contains(node)) {
        if (log) {
          System.out.println("Node is included in its ancestor or descendant nodes.");
        }
        return false;
      }
      for (TNode ancestorNode : ancestorNodes) {
        if (!allNodes.contains(ancestorNode)) {
          if (log) {
            System.out.println("Ancestor node is not included in all nodes.");
          }
          return false;
        }
      }
    }
    return true;
  }



  /**
   * 予測ラベルノードが正しい依存関係を持っているかを確認
   */
  static private boolean validatePredictNode(TAlgorithmGraph graph, TMemory memory, boolean log) {
    TPredictRootNode rootNode = graph.getPredictRootNode();
    ArrayList<TNode> descendantNodes = rootNode.getDescendantNodes();
    boolean existsInputVector = false;
    boolean existsLearningParameter = false;
    for (TNode node : descendantNodes) {
      if (node instanceof TVariableNode) {
        TVariableNode variableNode = (TVariableNode) node;
        TVariable variable = variableNode.getVariable();
        if (memory.isVectorInputVariable(variable)) {
          existsInputVector = true;
        }
      } else if (node instanceof TParameterNode) {
        existsLearningParameter = true;
      }
    }
    if (log) {
      if (!existsInputVector) {
        System.out.println("Input vector is not included in s1 descendant nodes.");
      }
      if (!existsLearningParameter) {
        System.out.println("Learning parameter is not included in s1 descendant nodes.");
      }
    }
    return existsInputVector && existsLearningParameter;
  }

  /**
   * 学習パラメータが正しい依存関係を持っているかを確認
   */
  static private boolean validateParameterNode(TAlgorithmGraph graph, TParameterRootNode node, TMemory memory, boolean log) {
    int depth = node.getDepth();

    // 最終的に全てs0の算出に利用されるかの確認
    boolean isUsedForS0 = false;
    ArrayList<TNode> ancestorNodes = node.getParameterNode().getAncestorNodes();
    for (TNode ancestorNode : ancestorNodes) {
      if (ancestorNode instanceof TPredictRootNode && depth == 1) {
        isUsedForS0 = true;
        break;
      }
      if (ancestorNode instanceof TParameterRootNode && ancestorNode.getDepth() == depth - 1) {
        isUsedForS0 = true;
        break;
      }
    }
    if (!isUsedForS0) {
      if (log) {
        System.out.println("Parameter node is not used for s0.");
        System.out.println(ancestorNodes);
        System.out.println(node.getParameterNode());
        System.out.println(node);
      }
      return false;
    }

    // 依存関係が正しいかの確認
    ArrayList<TNode> descendantNodes = node.getPredictLabelStoppedDescendantNodes();
    boolean existsSelf = false;
    boolean existsCorrectLabel = false;
    boolean existsPredictLabel = false;

    for (TNode descendantNode : descendantNodes) {
      if (descendantNode instanceof TPredictRootNode) {
        existsPredictLabel = true;
      } else if (descendantNode instanceof TParameterNode) {
        TParameterNode parameterNode = (TParameterNode) descendantNode;
        if (parameterNode == node.getParameterNode()) {
          existsSelf = true;
        }
      } else if (descendantNode instanceof TVariableNode) {
        TVariableNode variableNode = (TVariableNode) descendantNode;
        TVariable variable = variableNode.getVariable();
        if (memory.isScalarCorrectLabelVariable(variable)) {
          existsCorrectLabel = true;
        }
      }
    }
    if (depth <= 1) {
      if (log) {
        if (!existsSelf) {
          System.out.println("Self is not included in " + depth + " descendant nodes.");
        }
        if (!existsCorrectLabel) {
          System.out.println("Correct label is not included in " + depth + " descendant nodes.");
        }
        if (!existsPredictLabel) {
          System.out.println("Predict label is not included in " + depth + " descendant nodes.");
        }
      }
      return existsSelf && existsCorrectLabel && existsPredictLabel;
    } else {
      if (log) {
        if (!existsSelf) {
          System.out.println("Self is not included in " + depth + " descendant nodes.");
        }
      }
      return existsSelf;
    }
  }


  /**
   * 全ての学習パラメータが正しい依存関係を持っているかを確認
   */
  static private boolean validateParameterNodes(TAlgorithmGraph graph, TMemory memory, boolean log) {
    ArrayList<TParameterRootNode> parameterNodes = graph.getParameterRootNodes();
    for (TParameterRootNode node : parameterNodes) {
      if (!TAlgorithmGraphValidator.validateParameterNode(graph, node, memory, log)) {
        return false;
      }
    }
    return true;
  }

  static public boolean validate(TAlgorithmGraph graph, TMemory memory, boolean log) {
    if (!TAlgorithmGraphValidator.validateParentChildrenRelations(graph, memory, log)) {
      if (log) {
        System.out.println("Invalid parent-children relations.");
      }
      return false;
    }
    if (!TAlgorithmGraphValidator.validatePredictNode(graph, memory, log)) {
      if (log) {
        System.out.println("Invalid predict node.");
      }
      return false;
    }
    if (!TAlgorithmGraphValidator.validateParameterNodes(graph, memory, log)) {
      if (log) {
        System.out.println("Invalid parameter nodes.");
      }
      return false;
    }
    return true;
  }

  static public boolean validate(TAlgorithmGraph graph, TMemory memory) {
    return validate(graph, memory, false);
  }
}
