package algorithm.core;

import java.util.ArrayList;

import instruction.TInstruction;
import node.*;

public class TAlgorithmGraphConverter {
  private ArrayList<TNode> fLearnNodes = new ArrayList<TNode>();
  private ArrayList<TNode> fPredictNodes = new ArrayList<TNode>();

  private TAlgorithmGraph fGraph;
  private ArrayList<TNode> fCompletedNodes = new ArrayList<TNode>();
  private ArrayList<TInstruction> fLearnInstructions = new ArrayList<TInstruction>();
  private ArrayList<TInstruction> fPredictInstructions = new ArrayList<TInstruction>();
  private ArrayList<TInstruction> fSetupInstructions = new ArrayList<TInstruction>();

  TAlgorithmGraphConverter(TAlgorithmGraph graph) {
    TOpNode predictRootNode = graph.getPredictRootNode();
    predictRootNode.addDescendantNodesTo(fPredictNodes);
    fPredictNodes.add(predictRootNode);

    for (TNode parameterRootNode : graph.getParameterRootNodes()) {
      parameterRootNode.addDescendantNodesTo(fLearnNodes);
      fLearnNodes.add(parameterRootNode);
    }

    for (int index = 0; index < fLearnNodes.size(); index++) {
      TNode learnNode = fLearnNodes.get(index);
      if (fPredictNodes.contains(learnNode)) {
        fLearnNodes.remove(index);
      }
    }

    fGraph = graph;
  }

  private boolean isProcessable(TNode node) {
    if (fCompletedNodes.contains(node))
      return true;
    for (TNode parentNode : node.getParentNodes()) {
      if (!fCompletedNodes.contains(parentNode)) {
        return false;
      }
    }
    return true;
  }

  static public TAlgorithm convert(TAlgorithmGraph graph, TVariablesManager variablesManager) {
    TAlgorithmGraphConverter converter = new TAlgorithmGraphConverter(graph);
    return converter.convert(variablesManager);
  }

  private TAlgorithm convert(TVariablesManager variablesManager) {
    int size = fLearnNodes.size() + fPredictNodes.size();
    while (size > 0) {
      for (int i = 0; i < fLearnNodes.size(); i++) {
        TNode node = fLearnNodes.get(i);
        if (!isProcessable(node) || fPredictNodes.contains(node))
          continue;
        if (!fCompletedNodes.contains(node)) {
          if (node instanceof TOpNode) {
            TOpNode opNode = (TOpNode) node;
            fLearnInstructions.add(0, opNode.toInstruction(variablesManager));
          } else if (node instanceof TConstSetNode) {
            TConstSetNode constNode = (TConstSetNode) node;
            fSetupInstructions.addAll(0, constNode.toInstructions(variablesManager));
          } else if (node instanceof TParameterNode) {
            TParameterNode lpNode = (TParameterNode) node;
            fSetupInstructions.addAll(lpNode.getSetupInstructions(variablesManager));
          }
        }
        fCompletedNodes.add(node);
        fLearnNodes.remove(i);
      }
      for (int i = 0; i < fPredictNodes.size(); i++) {
        TNode node = fPredictNodes.get(i);
        if (!isProcessable(node))
          continue;
        if (!fCompletedNodes.contains(node)) {
          if (node instanceof TOpNode) {
            TOpNode opNode = (TOpNode) node;
            fPredictInstructions.add(0, opNode.toInstruction(variablesManager));
          } else if (node instanceof TConstSetNode) {
            TConstSetNode constNode = (TConstSetNode) node;
            fSetupInstructions.addAll(0, constNode.toInstructions(variablesManager));
          } else if (node instanceof TParameterNode) {
            TParameterNode lpNode = (TParameterNode) node;
            fSetupInstructions.addAll(lpNode.getSetupInstructions(variablesManager));
          }
        }
        fCompletedNodes.add(node);
        fPredictNodes.remove(i);
      }

      int nextSize = fLearnNodes.size() + fPredictNodes.size();
      if (nextSize == size) {
        System.err.println("========================================");
        System.err.println("Failed to convert algorithm graph");
        System.err.println("========================================");
        System.err.println(fGraph.toString(variablesManager));
        System.err.println("========================================");
        System.err.println("Leave predict nodes");
        for (TNode node : fPredictNodes) {
          System.err.println("========================================");
          System.err.println(node.toString(variablesManager));
          System.err.println("----------------------------------------");
          for (TNode parent : node.getParentNodes()) {
            if (fCompletedNodes.contains(parent)) {
              continue;
            }
            System.err.println(parent.toString(variablesManager));
          }
        }
        System.err.println("========================================");
        System.err.println("Leave learn nodes");
        for (TNode node : fLearnNodes) {
          System.err.println("========================================");
          System.err.println(node.toString(variablesManager));
          System.err.println("----------------------------------------");
          for (TNode parent : node.getParentNodes()) {
            if (fCompletedNodes.contains(parent)) {
              continue;
            }
            System.err.println(parent.toString(variablesManager));
          }
        }
        System.err.println("========================================");
        throw new RuntimeException("Failed to convert the graph to an algorithm");
      }
      size = nextSize;
    }
    return new TAlgorithm(fSetupInstructions, fPredictInstructions, fLearnInstructions);
  }
}
