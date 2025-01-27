package mutator.proposed;

import algorithm.core.TAlgorithmGraph;
import algorithm.factory.TAlgorithmGraphFactory;
import instruction.TOp;
import memory.TMemory;
import node.TVariablesManager;
import utils.TRandomGenerator;

public class TAlgorithmGraphMutator {
  private TAlgorithmGraphMutation[] fMutations;
  private double[] fProbabilities;

  private boolean kSafeMode = true;

  private TAlterConstValueMutator fAlterConstValueMutator;
  private TAlterParameterInitialValueMutator fAlterParameterInitialValueMutator;
  private TReplaceConnectionWithConstNodeMutator fReplaceConnectionWithConstNodeMutator;
  private TReplaceConstNodeWithConnectionMutator fReplaceConstNodeWithConnectionMutator;
  private TChangeConnectionMutator fChangeConnectionMutator;
  private TReconstructSubGraphMutator fReconstructSubtreeMutator;
  private TReconstructRootNodeMutator fReconstructRootNodeMutator;

  public TAlgorithmGraphMutator(TAlgorithmGraphMutatorSpec spec) {
    fMutations = spec.mutations;
    fProbabilities = spec.probabilities;
    fAlterConstValueMutator = new TAlterConstValueMutator(spec.alterConstValueMutatorSpec);
    fAlterParameterInitialValueMutator = new TAlterParameterInitialValueMutator(
        spec.alterParameterInitialValueMutatorSpec);
    fReplaceConnectionWithConstNodeMutator = new TReplaceConnectionWithConstNodeMutator(
        spec.replaceConnectionWithConstNodeMutatorSpec);
    fReplaceConstNodeWithConnectionMutator = new TReplaceConstNodeWithConnectionMutator(
        spec.replaceConstNodeWithConnectionMutatorSpec);
    fChangeConnectionMutator = new TChangeConnectionMutator(spec.changeConnectionMutatorSpec);
    fReconstructSubtreeMutator = new TReconstructSubGraphMutator(spec.reconstructSubGraphMutatorSpec);
    fReconstructRootNodeMutator = new TReconstructRootNodeMutator(
        spec.reconstructRootNodeMutatorSpec);
  }

  public TAlgorithmGraph mutate(TAlgorithmGraphMutation mutation, TAlgorithmGraph graph, TMemory memory,
      TRandomGenerator rand) {
    switch (mutation) {
      case ALTER_CONST_VALUE:
        return fAlterConstValueMutator.mutate(graph, rand);
      case ALTER_PARAMETER_INITIAL_VALUE:
        return fAlterParameterInitialValueMutator.mutate(graph, rand);
      case REPLACE_CONNECTION_WITH_CONST_NODE:
        return fReplaceConnectionWithConstNodeMutator.mutate(graph, memory, rand);
      case REPLACE_CONST_NODE_WITH_CONNECTION:
        return fReplaceConstNodeWithConnectionMutator.mutate(graph, memory, rand);
      case CHANGE_CONNECTION:
        return fChangeConnectionMutator.mutate(graph, memory, rand);
      case RECONSTRUCT_SUB_GRAPH:
        return fReconstructSubtreeMutator.mutate(graph, memory, rand);
      case RECONSTRUCT_ROOT_NODE:
        return fReconstructRootNodeMutator.mutate(graph, memory, rand);
      default:
        throw new IllegalArgumentException();
    }
  }

  public TAlgorithmGraph mutate(TAlgorithmGraph graph, TMemory memory, TRandomGenerator rand) {
    TAlgorithmGraphMutation mutation;
    TAlgorithmGraph mutated;
    if (!graph.validate(memory)) {
      System.out.println("original invalid before mutation");
    }
    while (true) {
      try {
        int mutationIndex = rand.nextIndex(fProbabilities);
        mutation = fMutations[mutationIndex];
        mutated = mutate(mutation, graph, memory, rand);
        mutated.setMutation(mutation);
        if (kSafeMode && !mutated.validate(memory)) {
          continue;
        }
        break;
      } catch (Exception e) {
        continue;
      }
    }
    if (!mutated.validate(memory)) {
      System.out.println("====================================");
      System.out.println("Invalid mutated graph: " + mutation);
      System.out.println("====================================");

      mutated.validate(memory, true);

      TVariablesManager manager = new TVariablesManager(memory);
      System.out.println(graph.toString(manager));
      if (!graph.validate(memory)) {
        System.out.println("original invalid");
      }
      System.out.println("------------------------------------");
      System.out.println(mutated.toString(manager));
      throw new RuntimeException("Invalid mutated graph");
    }
    return mutated;
  }

  public static void main(String[] args) {
    TRandomGenerator rand = new TRandomGenerator();

    TMemory memory = new TMemory();

    int dim = 4;
    int predictOpsNum = 2;
    int learnOpsNum = 5;
    TOp[] predictOpsSet = new TOp[] { TOp.VECTOR_INNER_PRODUCT_OP, TOp.SCALAR_SUM_OP };
    TOp[] learnOpsSet = new TOp[] { TOp.SCALAR_DIFF_OP, TOp.SCALAR_PRODUCT_OP, TOp.SCALAR_VECTOR_PRODUCT_OP,
        TOp.VECTOR_SUM_OP };
    int[] opsNum = new int[] { predictOpsNum, learnOpsNum };
    TOp[][] opsSet = new TOp[][] { predictOpsSet, learnOpsSet };
    int[] parametersNum = new int[] { 2, 0 };
    float[] opNodeConnectProbability = new float[] { 0.5f, 0.5f };
    float[] terminalNodeConnectProbability = new float[] { 0.5f, 0.5f };

    System.out.println("--------------- result -------------");
    TAlgorithmGraph graph = TAlgorithmGraphFactory.createWithRetries(dim, opsNum, opsSet, parametersNum,
        opNodeConnectProbability, terminalNodeConnectProbability, memory, rand, 1000, true);

    TAlgorithmGraphMutatorSpec spec = new TAlgorithmGraphMutatorSpec();
    spec.reconstructRootNodeMutatorSpec = new TReconstructRootNodeMutatorSpec();
    spec.reconstructRootNodeMutatorSpec.selectDepthProbabilities = new double[] { 0, 1.0 };
    spec.reconstructRootNodeMutatorSpec.numOfRetries = 1000;
    spec.reconstructSubGraphMutatorSpec = new TReconstructSubGraphMutatorSpec();
    spec.reconstructSubGraphMutatorSpec.selectSizeProbabilities = new double[] { 0, 1 };
    spec.reconstructSubGraphMutatorSpec.numOfRetries = 1000;
    spec.alterConstValueMutatorSpec = new TAlterConstValueMutatorSpec();
    spec.alterConstValueMutatorSpec.signFlipProb = 0.1;
    spec.alterParameterInitialValueMutatorSpec = new TAlterParameterInitialValueMutatorSpec();
    spec.alterParameterInitialValueMutatorSpec.signFlipProb = 0.1;

    System.out.println(graph.toString(new TVariablesManager(memory)));
    if (!graph.validate(memory)) {
      throw new RuntimeException("Invalid graph");
    }
    System.out.println("===================================");
    TAlgorithmGraphMutator mutator = new TAlgorithmGraphMutator(spec);
    TAlgorithmGraph mutated = mutator.mutate(TAlgorithmGraphMutation.RECONSTRUCT_SUB_GRAPH, graph, memory, rand);
    System.out.println(mutated.toString(new TVariablesManager(memory)));
    mutated.toAlgorithm(new TVariablesManager(memory));
    if (!mutated.validate(memory)) {
      throw new RuntimeException("Invalid graph");
    }
  }
}
