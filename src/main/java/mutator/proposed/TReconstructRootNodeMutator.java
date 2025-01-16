package mutator.proposed;

import java.util.ArrayList;

import algorithm.core.TAlgorithmGraph;
import algorithm.factory.TAlgorithmGraphFactory;
import memory.TMemory;
import node.TParameterRootNode;
import utils.TRandomGenerator;

class TReconstructRootNodeMutator {
  private int fNumOfRetries;
  private boolean fPrintError = false;

  private double[] fSelectDepthProbabilities;

  TReconstructRootNodeMutator(TReconstructRootNodeMutatorSpec spec) {
    fSelectDepthProbabilities = spec.selectDepthProbabilities;
    fNumOfRetries = spec.numOfRetries;
  }

  TAlgorithmGraph mutateOnce(TAlgorithmGraph graph, TMemory memory, TRandomGenerator rand) {
    int selectedDepth = rand.nextIndex(fSelectDepthProbabilities);

    if (selectedDepth == 0) {
      return TAlgorithmGraphFactory.reconstructPredictRootNode(graph, memory, rand);
    } else {
      ArrayList<TParameterRootNode> parameterRootNodeCandidates = graph.getParameterRootNodes(selectedDepth);
      if (parameterRootNodeCandidates.size() == 0) {
        return graph;
      }
      int randomIndex = rand.nextInt(parameterRootNodeCandidates.size());
      TParameterRootNode selectedRootNode = parameterRootNodeCandidates.get(randomIndex);
      return TAlgorithmGraphFactory.reconstructParameterRootNode(graph, selectedRootNode, memory, rand);
    }
  }

  TAlgorithmGraph mutate(TAlgorithmGraph graph, TMemory memory, TRandomGenerator rand) {
    for (int i = 0; i < fNumOfRetries; i++) {
      try {
        return mutateOnce(graph, memory, rand);
      } catch (RuntimeException e) {
        if (fPrintError) {
          System.out.println("Failed to reconstruct a root node of graph (" + (i + 1) + " / " + fNumOfRetries + ")");
          e.printStackTrace();
        }
      }
    }
    throw new RuntimeException("Failed to reconstruct a root node of graph");
  }
}
