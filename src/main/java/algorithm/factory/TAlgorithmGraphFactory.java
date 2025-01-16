package algorithm.factory;

import java.util.ArrayList;

import algorithm.core.TAlgorithmGraph;
import algorithm.spec.TAlgorithmGraphSpec;
import instruction.*;
import memory.*;
import node.*;
import utils.TRandomGenerator;
import java.util.Collections;
import java.util.HashMap;

public class TAlgorithmGraphFactory {
  private int fDim;
  private int[] fOpsNum;
  private TOpsSet[] fOpsSet;
  private int[] fParametersNum;
  private float[] fOpNodeConnectProbability;
  private float[] fTerminalNodeConnectProbability;
  private TAlgorithmGraphSpec fSpec;

  private void initialize(TAlgorithmGraphSpec spec) {
    TOpsSet[] opsSet = new TOpsSet[spec.opsSet.length];
    for (int i = 0; i < spec.opsSet.length; i++) {
      opsSet[i] = new TOpsSet(spec.opsSet[i]);
    }
    if (spec.opsNum.length < 2) {
      throw new RuntimeException("Invalid opsNum and opsSet");
    }
    if (opsSet.length != spec.opsNum.length) {
      throw new RuntimeException("Invalid opsSet and opsSet");
    }
    if (spec.parametersNum.length != spec.opsNum.length) {
      throw new RuntimeException("Invalid parametersNum");
    }
    if (spec.opNodeConnectProbability.length != spec.opsNum.length) {
      throw new RuntimeException("Invalid opNodeConnectProbability");
    }
    if (spec.terminalNodeConnectProbability.length != spec.opsNum.length) {
      throw new RuntimeException("Invalid terminalNodeConnectProbability");
    }
    fDim = spec.dim;
    fOpsNum = spec.opsNum;
    fOpsSet = opsSet;
    fParametersNum = spec.parametersNum;
    fOpNodeConnectProbability = spec.opNodeConnectProbability;
    fTerminalNodeConnectProbability = spec.terminalNodeConnectProbability;
    fSpec = spec;
  }

  private void initialize(int dim, int[] opsNum, TOp[][] opsSet, int[] parametersNum,
      float[] opNodeConnectProbability, float[] terminalNodeConnectProbability) {
    TAlgorithmGraphSpec spec = new TAlgorithmGraphSpec(dim, opsNum, opsSet, parametersNum, opNodeConnectProbability,
        terminalNodeConnectProbability);
    initialize(spec);
  }

  public TAlgorithmGraphFactory(int dim, int[] opsNum, TOp[][] opsSet, int[] parametersNum,
      float[] opNodeConnectProbability, float[] terminalNodeConnectProbability) {
    initialize(dim, opsNum, opsSet, parametersNum, opNodeConnectProbability, terminalNodeConnectProbability);
  }

  public TAlgorithmGraphFactory(TAlgorithmGraphSpec spec) {
    initialize(spec);
  }

  private void createPredictNode(TAlgorithmGraphBuffer buffer, TRandomGenerator rand) {
    TOpsSet predictOpsSet = fOpsSet[0];

    // ルートノードの命令を作成
    TVariable predictionLabelVariable = buffer.makePredictionLabelVariable();
    TPredictRootNode predictRootNode = TPredictRootNode.randomCreate(predictOpsSet, predictionLabelVariable, rand);
    buffer.addNode(predictRootNode);
    predictRootNode.maybeConnect(fOpNodeConnectProbability[0], buffer.getNodes(), rand);

    // 命令数が規定の数になるまで命令ノードを追加
    for (int predictOpsNum = 1; predictOpsNum < fOpsNum[0]; predictOpsNum++) {
      TOpNode opNode = predictRootNode.grow(predictOpsSet, buffer.getNodes(), fOpNodeConnectProbability[0], rand);
      buffer.addNode(opNode);
    }

    // 存在しない場合はv0の依存関係を追加
    TVariableNode inputVectorVariableNode = buffer.getInputVectorVariableNode();
    predictRootNode.setDescendantNodeRandomlyIfNotDepended(inputVectorVariableNode, rand);

    // パラメータの追加
    for (int parametersCount = 0; parametersCount < fParametersNum[0]; parametersCount++) {
      TParameterNode parameterNode = predictRootNode.setDescendantParameterNodeRandomly(fDim, rand);
      buffer.addNode(parameterNode);
    }

    // 末端ノードとの接続処理
    for (TOpNode availableNode : predictRootNode.getAvailableDescendantNodes()) {
      TNode[] childNodes = availableNode.getChildNodes();
      for (int i = 0; i < childNodes.length; i++) {
        if (childNodes[i] != null)
          continue;
        availableNode.maybeConnect(fTerminalNodeConnectProbability[0], buffer.getTerminalNodes(), rand);
      }
    }

    // 余っている末端ノードを定数ノードで埋める
    ArrayList<TConstSetNode> constNodes = predictRootNode.fillAvailableDescendantNodesBySetNodes(fDim, rand);
    buffer.addNodes(constNodes);
  }

  private void createParameterNodes(TAlgorithmGraphBuffer buffer, int depth, TRandomGenerator rand) {
    createParameterNodes(buffer, depth, null, rand);
  }

  private void createParameterNodes(TAlgorithmGraphBuffer buffer, int depth, TParameterRootNode target, TRandomGenerator rand) {
    TOpsSet opsSet = fOpsSet[depth];

    ArrayList<TParameterNode> parameterNodes = buffer.getParameterNodes(depth - 1);
    if (parameterNodes == null || parameterNodes.size() == 0) {
      return;
    }

    ArrayList<TParameterRootNode> parameterRootNodes = new ArrayList<TParameterRootNode>();
    ArrayList<TParameterRootNode> notSortedParameterRootNodes = buffer.getParameterRootNodes(depth);
    for (TParameterNode parameterNode : parameterNodes) {
      boolean found = false;
      for (TParameterRootNode parameterRootNode : notSortedParameterRootNodes) {
        if (parameterRootNode.getParameterNode() == parameterNode) {
          parameterRootNodes.add(parameterRootNode);
          found = true;
          break;
        }
      }
      if (!found) {
        parameterRootNodes.add(null);
      }
    }

    // 規定の命令数になるまで命令ノードを追加
    for (int opsNum = buffer.getOpsCount(depth); opsNum < fOpsNum[depth]; opsNum++) {
      int index = rand.nextInt(parameterRootNodes.size());
      TParameterRootNode nullableRootNode = target != null ? target : parameterRootNodes.get(index);
      // ルートノードが構築されている場合
      if (nullableRootNode != null) {
        TParameterRootNode rootNode = nullableRootNode;
        TOpNode opNode = rootNode.grow(opsSet, buffer.getNodes(), fOpNodeConnectProbability[depth], rand);
        buffer.addNode(opNode);
      }
      // ルートノードが構築されていない場合
      else {
        TParameterNode parameterNode = parameterNodes.get(index);
        TParameterRootNode rootNode = TParameterRootNode.randomCreate(depth, opsSet, parameterNode, rand);
        rootNode.maybeConnect(fOpNodeConnectProbability[depth], buffer.getNodes(), rand);
        parameterRootNodes.set(index, rootNode);
        buffer.addNode(rootNode);
      }
    }

    // 自分自身の依存関係を追加
    Collections.shuffle(parameterRootNodes, rand);
    for (TParameterRootNode rootNode : parameterRootNodes) {
      rootNode.addSelfParameterNodeRandomlyIfNotDepended(rand);
    }

    // s1, s0,
    if (depth == 1) {
      Collections.shuffle(parameterRootNodes, rand);
      for (TParameterRootNode rootNode : parameterRootNodes) {
        rootNode.setDescendantNodeRandomlyIfNotDepended(buffer.getPredictRootNode(), rand);
        rootNode.setDescendantNodeRandomlyIfNotDepended(buffer.getCorrectLabelVariableNode(), rand);
      }
    }

    // 規定の数のパラメータを追加
    for (int parameterNum = buffer.getParametersCount(depth); parameterNum < fParametersNum[depth]; parameterNum++) {
      int randomIndex = rand.nextInt(parameterRootNodes.size());
      TOpNode rootNode = target != null ? target : parameterRootNodes.get(randomIndex);
      TParameterNode parameterNode = rootNode.setChildParameterNodeRandomly(fDim, rand);
      buffer.addNode(parameterNode);
    }

    // 末端ノードとの接続処理
    for (TOpNode node : parameterRootNodes) {
      for (TOpNode availableNode : node.getAvailableDescendantNodes()) {
        TNode[] childNodes = availableNode.getChildNodes();
        for (int i = 0; i < childNodes.length; i++) {
          if (childNodes[i] != null)
            continue;
          availableNode.maybeConnect(fTerminalNodeConnectProbability[depth], buffer.getTerminalNodes(), rand);
        }
      }
    }

    // 定数ノードで埋める
    for (TOpNode node : parameterRootNodes) {
      ArrayList<TConstSetNode> constNodes = node.fillAvailableDescendantNodesBySetNodes(fDim, rand);
      buffer.addNodes(constNodes);
    }
  }

  public TAlgorithmGraph create(TMemory memory, TRandomGenerator rand) {
    TAlgorithmGraphBuffer buffer = new TAlgorithmGraphBuffer(memory);

    // v0のノードを追加
    buffer.addInputVectorVariableNode();

    // Predict用のノードを作成
    createPredictNode(buffer, rand);

    // s0のノードを追加
    buffer.addCorrectLabelVariableNode();

    // Learn用のノードを作成
    for (int depth = 1; depth < fParametersNum.length; depth++) {
      createParameterNodes(buffer, depth, rand);
    }

    return buffer.toAlgorithmGraph(fSpec);
  }

  public TAlgorithmGraph createWithRetries(TMemory memory, TRandomGenerator rand, int numOfRetries,
      boolean printError) {
    for (int i = 0; i < numOfRetries; i++) {
      try {
        return create(memory, rand);
      } catch (Exception e) {
        if (printError) {
          System.out.println("Failed to create a graph (" + (i + 1) + " / " + numOfRetries + ")");
          e.printStackTrace();
        }
      }
    }
    throw new RuntimeException("Failed to create a graph");
  }

  public static TAlgorithmGraph reconstructPredictRootNode(TAlgorithmGraph graph, TMemory memory,
      TRandomGenerator rand) {
    TAlgorithmGraphFactory factory = new TAlgorithmGraphFactory(graph.getSpec());
    return factory.create(memory, rand);
  }

  public static TAlgorithmGraph reconstructPredictRootNodeWithRetries(TAlgorithmGraph graph, TMemory memory,
      TRandomGenerator rand, int numOfRetries, boolean printError) {
    TAlgorithmGraphFactory factory = new TAlgorithmGraphFactory(graph.getSpec());
    return factory.createWithRetries(memory, rand, numOfRetries, printError);
  }

  public void createParameterNodesFromMidpoint(TAlgorithmGraphBuffer buffer, TParameterRootNode rootNode, TRandomGenerator rand) {
    int rootNodeDepth = rootNode.getDepth();
    buffer.addCorrectLabelVariableNodeIfNotExists();
    TOp op = fOpsSet[rootNodeDepth].getRandomOp(rootNode.getOutMemoryType(), rand);
    rootNode.resetOp(op);
    rootNode.maybeConnect(fOpNodeConnectProbability[rootNodeDepth], buffer.getNodes(), rand);
    for (int depth = rootNodeDepth; depth < fParametersNum.length; depth++) {
      if (depth == rootNodeDepth) {
        // はじめに指定されたノードをターゲットとして同じ深さを再構築
        createParameterNodes(buffer, depth, rootNode, rand);
      } else {
        // 以降は通常通り再構築
        createParameterNodes(buffer, depth, rand);
      }
    }
  }

  public static TAlgorithmGraph reconstructParameterRootNode(TAlgorithmGraph graph, TParameterRootNode selectedRootNode, TMemory memory,
      TRandomGenerator rand) {

    HashMap<Integer, TNode> copied = new HashMap<Integer, TNode>();
    TPredictRootNode copiedPredictRootNode = graph.copyPredictRootNode(copied);
    ArrayList<TParameterRootNode> copiedParameterRootNodes = new ArrayList<TParameterRootNode>();
    TParameterRootNode copiedSelectedRootNode = null;
    for (TParameterRootNode node : graph.getParameterRootNodes()) {
      if (node.getDepth() > selectedRootNode.getDepth()) {
        continue;
      }
      TParameterRootNode copiedRootNode = node.copy(copied);
      if (node == selectedRootNode) {
        copiedSelectedRootNode = copiedRootNode;
      }
      copiedParameterRootNodes.add(copiedRootNode);
    }
    if (copiedSelectedRootNode == null) {
      throw new RuntimeException("Failed to copy selected root node");
    }

    // ルートノードの再構築
    TAlgorithmGraphBuffer buffer = new TAlgorithmGraphBuffer(memory, copiedPredictRootNode, copiedParameterRootNodes);
    TAlgorithmGraphFactory factory = new TAlgorithmGraphFactory(graph.getSpec());
    factory.createParameterNodesFromMidpoint(buffer, copiedSelectedRootNode, rand);
    return buffer.toAlgorithmGraph(factory.fSpec);
  }

  public static TAlgorithmGraph reconstructParameterRootNodeWithRetries(TAlgorithmGraph graph, TParameterRootNode rootNode,
      TMemory memory, TRandomGenerator rand, int numOfRetries, boolean printError) {
    for (int i = 0; i < numOfRetries; i++) {
      try {
        return reconstructParameterRootNode(graph, rootNode, memory, rand);
      } catch (Exception e) {
        if (printError) {
          System.out.println("Failed to create a graph (" + (i + 1) + " / " + numOfRetries + ")");
          e.printStackTrace();
        }
      }
    }
    throw new RuntimeException("Failed to reconstruct a graph");
  }

  public static TAlgorithmGraph create(int dim, int[] opsNum, TOp[][] opsSet, int[] parametersNum,
      float[] opNodeConnectProbability, float[] terminalNodeConnectProbability, TMemory memory, TRandomGenerator rand) {
    TAlgorithmGraphFactory creator = new TAlgorithmGraphFactory(dim, opsNum, opsSet, parametersNum, opNodeConnectProbability, terminalNodeConnectProbability);
    return creator.create(memory, rand);
  }

  public static TAlgorithmGraph create(TAlgorithmGraphSpec spec, TMemory memory, TRandomGenerator rand) {
    TAlgorithmGraphFactory creator = new TAlgorithmGraphFactory(spec);
    return creator.create(memory, rand);
  }

  public static TAlgorithmGraph createWithRetries(int dim, int[] opsNum, TOp[][] opsSet, int[] parametersNum,
      float[] opNodeConnectProbability, float[] terminalNodeConnectProbability, TMemory memory,
      TRandomGenerator rand, int numOfRetries, boolean printError) {
    TAlgorithmGraphFactory creator = new TAlgorithmGraphFactory(dim, opsNum, opsSet, parametersNum,
        opNodeConnectProbability, terminalNodeConnectProbability);
    return creator.createWithRetries(memory, rand, numOfRetries, printError);
  }

  public static TAlgorithmGraph createWithRetries(TAlgorithmGraphSpec spec, TMemory memory, TRandomGenerator rand, int numOfRetries, boolean printError) {
    TAlgorithmGraphFactory creator = new TAlgorithmGraphFactory(spec);
    return creator.createWithRetries(memory, rand, numOfRetries, printError);
  }

  public static void main(String[] args) {
    TRandomGenerator rand = new TRandomGenerator();

    TMemory memory = new TMemory();
    int dim = 4;
    int predictOpsNum = 2;
    int learnOpsNum = 5;
    TOp[] predictOpsSet = new TOp[] { TOp.VECTOR_INNER_PRODUCT_OP, TOp.SCALAR_SUM_OP };
    TOp[] learnOpsSet =
        new TOp[] { TOp.SCALAR_DIFF_OP, TOp.SCALAR_PRODUCT_OP, TOp.SCALAR_VECTOR_PRODUCT_OP, TOp.VECTOR_SUM_OP };
    int[] opsNum = new int[] { predictOpsNum, learnOpsNum };
    TOp[][] opsSet = new TOp[][] { predictOpsSet, learnOpsSet };
    int[] parametersNum = new int[] { 2, 0 };
    float[] opNodeConnectProbability = new float[] { 0.5f, 0.5f };
    float[] terminalNodeConnectProbability = new float[] { 0.5f, 0.5f };

    TAlgorithmGraph graph = TAlgorithmGraphFactory.createWithRetries(dim, opsNum, opsSet, parametersNum,
        opNodeConnectProbability, terminalNodeConnectProbability, memory, rand, 1000, true);
    System.out.println(graph.toString(new TVariablesManager(memory)));
  }
}
