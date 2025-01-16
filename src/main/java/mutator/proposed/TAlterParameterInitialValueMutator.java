package mutator.proposed;

import java.util.ArrayList;

import algorithm.core.TAlgorithmGraph;
import node.TParameterNode;
import utils.TRandomGenerator;

class TAlterParameterInitialValueMutator {
  private double fSignFlipProb;

  TAlterParameterInitialValueMutator(TAlterParameterInitialValueMutatorSpec spec) {
    fSignFlipProb = spec.signFlipProb;
  }

  TAlgorithmGraph mutate(TAlgorithmGraph graph, TRandomGenerator rand) {
    TAlgorithmGraph copy = graph.copy();
    ArrayList<TParameterNode> nodes = copy.getParameterNodes();
    if (nodes.size() == 0) {
      throw new RuntimeException("No parameter nodes found");
    }
    TParameterNode node = nodes.get(rand.nextInt(nodes.size()));
    node.randomizeInitialValue(rand, fSignFlipProb);
    return copy;
  }
}
