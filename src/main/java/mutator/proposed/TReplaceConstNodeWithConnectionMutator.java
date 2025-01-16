package mutator.proposed;

import java.util.ArrayList;

import algorithm.core.TAlgorithmGraph;
import memory.TMemory;
import node.TConstSetNode;
import node.TNode;
import node.TOpNode;
import utils.TRandomGenerator;

class TReplaceConstNodeWithConnectionMutator {
  TReplaceConstNodeWithConnectionMutator(TReplaceConstNodeWithConnectionMutatorSpec spec) {
  }

  TConstSetNode selectNode(TAlgorithmGraph graph, TRandomGenerator rand) {
    ArrayList<TConstSetNode> constSetNodes = graph.getConstSetNodes();
    if (constSetNodes.size() == 0) {
      throw new RuntimeException("No const set nodes found");
    }
    TConstSetNode node = constSetNodes.get(rand.nextInt(constSetNodes.size()));
    return node;
  }

  TOpNode selectParent(TConstSetNode node, TRandomGenerator rand) {
    ArrayList<TNode> parents = node.getParentNodes();
    if (parents.size() == 0) {
      throw new RuntimeException("No parents found");
    }
    TNode parent = parents.get(rand.nextInt(parents.size()));
    if (parent instanceof TOpNode) {
      return (TOpNode) parent;
    } else {
      throw new RuntimeException("Parent is not TOpNode");
    }
  }


  TAlgorithmGraph mutate(TAlgorithmGraph graph, TMemory memory, TRandomGenerator rand) {
    TAlgorithmGraph copy = graph.copy();
    TConstSetNode node = selectNode(copy, rand);
    TOpNode parent = selectParent(node, rand);
    int index = parent.getRandomChildNodeIndexOf(node, rand);
    parent.detachFromChildNode(index);
    ArrayList<TNode> nodes = copy.getAllNodesWithMaxDepth(parent.getDepth());
    if (parent.connect(index, nodes, rand) == null) {
      throw new RuntimeException("Failed to connect");
    }
    if (!copy.validate(memory)) {
      throw new RuntimeException("Invalid graph");
    }
    return copy;
  }
}
