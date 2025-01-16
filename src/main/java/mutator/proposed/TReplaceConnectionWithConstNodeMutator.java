package mutator.proposed;

import java.util.ArrayList;

import algorithm.core.TAlgorithmGraph;
import memory.TMemory;
import node.TNode;
import node.TOpNode;
import utils.TRandomGenerator;

class TReplaceConnectionWithConstNodeMutator {
  TReplaceConnectionWithConstNodeMutator(TReplaceConnectionWithConstNodeMutatorSpec spec) {
  }

  ArrayList<TMutableConnection> getMutableConnections(TAlgorithmGraph graph, TMemory memory) {
    ArrayList<TMutableConnection> newConnections = new ArrayList<TMutableConnection>();

    // 親を複数持つノードの接続関係を変更できるかを調べる
    for (TNode connectedNode : graph.getConnectedNodes()) {
      ArrayList<TNode> parents = new ArrayList<TNode>();
      parents.addAll(connectedNode.getParentNodes());
      for (TNode parent : parents) {
        if (!(parent instanceof TOpNode)) {
          continue;
        }
        TOpNode parentOpNode = (TOpNode) parent;
        for (int connectedIndex : parentOpNode.getAllChildNodeIndexes(connectedNode)) {
          // 接続関係を除去しても妥当性を維持できるノードが定数に置き換え可能なノード
          parentOpNode.detachFromChildNode(connectedIndex);
          if (graph.validate(memory)) {
            newConnections.add(new TMutableConnection(parentOpNode, connectedIndex, null));
          }
          parentOpNode.setChildNode(connectedIndex, connectedNode);
        }
      }
    }

    if (newConnections.size() == 0) {
      throw new RuntimeException("No mutable connections found.");
    }

    return newConnections;
  }

  void replaceConnectionWithConstNode(TAlgorithmGraph graph, TMutableConnection connection, TMemory memory, TRandomGenerator rand) {
    TOpNode validConnectedNodeParent = connection.getParent();
    int index = connection.getIndex();
    validConnectedNodeParent.detachFromChildNode(index);
    validConnectedNodeParent.fillAvailableDescendantNodesBySetNodes(memory.getDim(), rand);
  }

  TAlgorithmGraph mutate(TAlgorithmGraph graph, TMemory memory, TRandomGenerator rand) {
    TAlgorithmGraph copy = graph.copy();
    ArrayList<TMutableConnection> mutableConnections = getMutableConnections(copy, memory);
    TMutableConnection newConnection = mutableConnections.get(rand.nextInt(mutableConnections.size()));
    replaceConnectionWithConstNode(copy, newConnection, memory, rand);
    return copy;
  }
}
