package mutator.proposed;

import java.util.ArrayList;

import algorithm.core.TAlgorithmGraph;
import algorithm.spec.TAlgorithmGraphSpec;
import instruction.TOp;
import instruction.TOpsSet;
import memory.TMemory;
import node.TNode;
import node.TOpNode;
import node.TParameterRootNode;
import node.TPredictRootNode;
import utils.TRandomGenerator;

class TReconstructSubGraphMutator {
  private int fNumOfRetries;
  private boolean fPrintError = false;
  private double[] fSelectSizeProbabilities;

  TReconstructSubGraphMutator(TReconstructSubGraphMutatorSpec spec) {
    fNumOfRetries = spec.numOfRetries;
    fSelectSizeProbabilities = spec.selectSizeProbabilities;
  }

  private TOpNode selectSubGraphRootNode(TAlgorithmGraph graph, TRandomGenerator rand) {
    ArrayList<TOpNode> subtreeRootNodeCandidates = graph.getOpNodes();
    return subtreeRootNodeCandidates.get(rand.nextInt(subtreeRootNodeCandidates.size()));
  }


  private ArrayList<TNode> removeSubGraph(TOpNode rootNode, int size, TRandomGenerator rand) {
    ArrayList<TNode> leafNodes = new ArrayList<TNode>();

    ArrayList<TOpNode> selectableNodes = new ArrayList<TOpNode>();
    selectableNodes.add(rootNode);

    for (int i = 0; i < size; i++) {
      if (selectableNodes.size() == 0) {
        throw new RuntimeException("Not found node in selectableNodes.");
      }
      int selectedOpNodeIndex = rand.nextInt(selectableNodes.size());
      TOpNode selectedOpNode = selectableNodes.get(selectedOpNodeIndex);
      selectableNodes.remove(selectedOpNode);
      while (leafNodes.contains(selectedOpNode)) {
        leafNodes.remove(selectedOpNode);
      }
      for (TNode child : selectedOpNode.getChildNodes()) {
        if (!leafNodes.contains(child)) {
          leafNodes.add(child);
        }
        if (!(child instanceof TOpNode)) {
          continue;
        }
        if (child.hasMultipleParents()) {
          continue;
        }
        if (child instanceof TPredictRootNode) {
          continue;
        }
        if (child instanceof TParameterRootNode) {
          continue;
        }
        if (selectableNodes.contains((TOpNode) child)) {
          continue;
        }
        selectableNodes.add((TOpNode) child);
      }
      if (rootNode == selectedOpNode) {
        selectedOpNode.detachFromChildNodes();
      } else {
        selectedOpNode.detach();
      }
    }

    return leafNodes;
  }

  private void reconstructSubGraph(TAlgorithmGraph graph, TOpNode rootNode,
      ArrayList<TNode> leafNodes, int size, TMemory memory, TRandomGenerator rand) {
    int depth = rootNode.getDepth();

    TAlgorithmGraphSpec spec = graph.getSpec();
    TOpsSet opsSet = new TOpsSet(spec.opsSet[depth]);

    // 部分グラフのルートノードをランダムに命令を設定
    TOp op = opsSet.getRandomOp(rootNode.getOutMemoryType(), rand);
    rootNode.resetOp(op);

    // 元々の大きさと同じになるまで拡張操作を繰り返す
    for (int i = 1; i < size; i++) {
      rootNode.extendDescendant(opsSet, rand);
    }

    // 変更前の部分グラフの葉ノードを割り当てる
    for (TNode leafNode : leafNodes) {
      rootNode.setDescendantNodeRandomly(leafNode, rand);
    }

    // 空いているノードがある場合は他のノードと一定確率で接続する
    for (TOpNode availableNode : rootNode.getAvailableDescendantNodes()) {
      TNode[] childNodes = availableNode.getChildNodes();
      for (int i = 0; i < childNodes.length; i++) {
        if (childNodes[i] != null)
          continue;
        availableNode.maybeConnect(spec.opNodeConnectProbability[depth], graph.getAllNodesWithMaxDepth(depth), rand);
      }
    }

    // 空いているノードがある場合は定数ノードで埋める
    rootNode.fillAvailableDescendantNodesBySetNodes(memory.getDim(), rand);
  }

  private TAlgorithmGraph mutateOnce(TAlgorithmGraph graph, TMemory memory, TRandomGenerator rand) {
    int size = rand.nextIndex(fSelectSizeProbabilities) + 1;
    TAlgorithmGraph copy = graph.copy();
    TOpNode rootNode = selectSubGraphRootNode(copy, rand);
    ArrayList<TNode> leafNodes = removeSubGraph(rootNode, size, rand);
    reconstructSubGraph(copy, rootNode, leafNodes, size, memory, rand);
    return copy;
  }

  TAlgorithmGraph mutate(TAlgorithmGraph graph, TMemory memory, TRandomGenerator rand) {
    for (int i = 0; i < fNumOfRetries; i++) {
      try {
        return mutateOnce(graph, memory, rand);
      } catch (RuntimeException e) {
        if (fPrintError) {
          System.out.println("Failed to reconstruct a subtree of graph (" + (i + 1) + " / " + fNumOfRetries + ")");
          e.printStackTrace();
        }
      }
    }
    throw new RuntimeException("Failed to reconstruct a subtree of graph");
  }
}
