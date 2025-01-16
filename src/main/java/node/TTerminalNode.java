package node;

import java.util.HashMap;

import memory.TMemoryType;

abstract public class TTerminalNode extends TNode {
  protected TTerminalNode(TMemoryType outMemoryType) {
    super(outMemoryType, 0);
  }


  protected TTerminalNode(TTerminalNode node) {
    super(node);
  }

  @Override
  abstract protected TTerminalNode copyImpl(HashMap<Integer, TNode> copied);
}
