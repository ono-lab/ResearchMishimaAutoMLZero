package mutator.proposed;

import java.util.ArrayList;

import algorithm.core.TAlgorithmGraph;
import node.TConstSetNode;
import utils.TRandomGenerator;

class TAlterConstValueMutator {
  private double fSignFlipProb = 0.1;

  TAlterConstValueMutator(TAlterConstValueMutatorSpec spec) {
    fSignFlipProb = spec.signFlipProb;
  }

  TAlgorithmGraph mutate(TAlgorithmGraph graph, TRandomGenerator rand) {
    TAlgorithmGraph copy = graph.copy();
    ArrayList<TConstSetNode> nodes = copy.getConstSetNodes();
    if (nodes.size() == 0) {
      throw new RuntimeException("No const nodes found");
    }
    TConstSetNode node = nodes.get(rand.nextInt(nodes.size()));
    node.randomizeValue(rand,fSignFlipProb);
    return copy;
  }
}
