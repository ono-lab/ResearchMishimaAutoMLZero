package mutator.proposed;

import java.util.ArrayList;

import algorithm.core.TAlgorithmGraph;
import memory.TMemory;
import node.TNode;
import node.TOpNode;
import utils.TRandomGenerator;

class TChangeConnectionMutator {
  TChangeConnectionMutator(TChangeConnectionMutatorSpec spec) {
  }

  ArrayList<TMutableConnection> getMutableConnections(TAlgorithmGraph graph, TMemory memory) {
    ArrayList<TMutableConnection> newConnections = new ArrayList<TMutableConnection>();

    // 親を複数持つノードの接続関係を変更できるかを調べる
    for (TNode connectedNode : graph.getConnectedNodes()) {
      int connectedNodeDepth = connectedNode.getDepth();
      ArrayList<TNode> parents = new ArrayList<TNode>();
      parents.addAll(connectedNode.getParentNodes());
      for (TNode parent : parents) {
        if (!(parent instanceof TOpNode)) {
          continue;
        }
        TOpNode parentOpNode = (TOpNode) parent;
        for (int connectedIndex : parentOpNode.getAllChildNodeIndexes(connectedNode)) {
          // 接続先の変更候補は深さが自分と同じかそれよりも浅いノードのみ
          // 深いノードに接続してしまうと、深さの制約が破られてしまうため
          ArrayList<TNode> candidates = graph.getAllNodesWithMaxDepth(parent.getDepth());
          // 循環参照にならないようにフィルターする
          ArrayList<TNode> connectableNodes = parentOpNode.filterConnectableNodes(connectedIndex, candidates);

          for (TNode connectableNode : connectableNodes) {

            // 試しに接続関係を変更してみて妥当性が保たれるかを確認し
            // 保たれる場合は突然変異の候補として追加
            parentOpNode.detachFromChildNode(connectedIndex);
            parentOpNode.setChildNode(connectedIndex, connectableNode);

            int currentConnectedNodeDepth = connectedNode.getDepth();

            if (graph.validate(memory) && currentConnectedNodeDepth == connectedNodeDepth) {
              newConnections.add(new TMutableConnection(parentOpNode, connectedIndex, connectableNode));
            }
          }

          // 接続関係を元に戻しておく
          parentOpNode.detachFromChildNode(connectedIndex);
          parentOpNode.setChildNode(connectedIndex, connectedNode);

          if (!graph.validate(memory)) {
            throw new RuntimeException("Invalid connection found.");
          }
        }
      }
    }

    if (newConnections.size() == 0) {
      throw new RuntimeException("No mutable connections found.");
    }

    return newConnections;
  }

  void updateConnection(TAlgorithmGraph graph, TMutableConnection connection) {
    TOpNode validConnectedNodeParent = connection.getParent();
    TNode validConnectableNode = connection.getNode();
    int index = connection.getIndex();
    validConnectedNodeParent.detachFromChildNode(index);
    validConnectedNodeParent.setChildNode(index, validConnectableNode);
  }

  TAlgorithmGraph mutate(TAlgorithmGraph graph, TMemory memory, TRandomGenerator rand) {
    TAlgorithmGraph copy = graph.copy();
    ArrayList<TMutableConnection> newConnections = getMutableConnections(copy, memory);
    TMutableConnection newConnection = newConnections.get(rand.nextInt(newConnections.size()));
    updateConnection(copy, newConnection);
    return copy;
  }
}
