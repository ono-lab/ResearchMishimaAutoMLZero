package mutator.proposed;

import node.TNode;
import node.TOpNode;

class TMutableConnection {
  TOpNode fParent;
  int fIndex;
  TNode fNode;

  TMutableConnection(TOpNode parent, int index, TNode node) {
    fParent = parent;
    fIndex = index;
    fNode = node;
  }

  TOpNode getParent() {
    return fParent;
  }

  int getIndex() {
    return fIndex;
  }

  TNode getNode() {
    return fNode;
  }
}
