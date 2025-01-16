package algorithm.core;

import instruction.*;
import memory.*;
import utils.TRandomGenerator;

public class TAlgorithmOpsSet {
  private TOpsSet fSetupOpsSet;
  private TOpsSet fPredictOpsSet;
  private TOpsSet fLearnOpsSet;

  public TAlgorithmOpsSet(TOp[] setupOps, TOp[] predictOps, TOp[] learnOps) {
    fSetupOpsSet = new TOpsSet(setupOps);
    fPredictOpsSet = new TOpsSet(predictOps);
    fLearnOpsSet = new TOpsSet(learnOps);
  }

  public TAlgorithmOpsSet(TOpsSet setupOpsSet, TOpsSet predictOpsSet, TOpsSet learnOpsSet) {
    fSetupOpsSet = setupOpsSet;
    fPredictOpsSet = predictOpsSet;
    fLearnOpsSet = learnOpsSet;
  }

  public boolean hasOps(TAlgorithmComponentType componentType) {
    switch (componentType) {
      case SETUP:
        return fSetupOpsSet.hasOps();
      case PREDICT:
        return fPredictOpsSet.hasOps();
      case LEARN:
        return fLearnOpsSet.hasOps();
      default:
        throw new RuntimeException("Invalid component type.");
    }
  }

  public boolean hasOps(TAlgorithmComponentType componentType, TMemoryType outMemoryType) {
    switch (componentType) {
      case SETUP:
        return fSetupOpsSet.hasOps(outMemoryType);
      case PREDICT:
        return fPredictOpsSet.hasOps(outMemoryType);
      case LEARN:
        return fLearnOpsSet.hasOps(outMemoryType);
      default:
        throw new RuntimeException("Invalid component type.");
    }
  }

  /**
  * 指定されたコンポーネントに対して許可される命令群の中からランダムに1つ選択して返却する関数
  */
  public TOp getRandomOp(TAlgorithmComponentType componentType, TRandomGenerator rand) {
    switch (componentType) {
      case SETUP:
        return fSetupOpsSet.getRandomOp(rand);
      case PREDICT:
        return fPredictOpsSet.getRandomOp(rand);
      case LEARN:
        return fLearnOpsSet.getRandomOp(rand);
      default:
        throw new RuntimeException("Invalid component type.");
    }
  }
}
