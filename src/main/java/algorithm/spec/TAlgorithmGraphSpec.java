package algorithm.spec;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import algorithm.core.TAlgorithmGraph;
import algorithm.factory.TAlgorithmGraphFactory;
import instruction.TOp;
import memory.TMemory;
import utils.TRandomGenerator;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class TAlgorithmGraphSpec {
  // アルゴリズムのベクトルの次元
  public int dim;

  // 命令の数
  // 0: Predictの命令数
  // 1以上: パラメータに関する命令数、indexが1以上の命令数の合計がLearn関数の命令数
  public int[] opsNum;

  // 命令セット
  // 0: Predictの命令セット
  // 1以上: Learn関数の命令セット
  public TOp[][] opsSet;

  // 新規で割り当てるパラメータの数
  // 0: Predictの命令セット
  // 1以上: Learn関数の命令セット
  public int[] parametersNum;

  // 命令ノードの接続確率
  // 0: Predictにおける命令ノードの接続確率
  // 1以上: Learn関数における命令ノードの接続確率
  public float[] opNodeConnectProbability;

  // 末端ノードの接続確率
  // 0: Predictにおける末端ノードの接続確率
  // 1以上: Learn関数における末端ノードの接続確率
  public float[] terminalNodeConnectProbability;

  public TAlgorithmGraphSpec() {
  }

  public TAlgorithmGraphSpec(int dim, int[] opsNum, TOp[][] opsSet, int[] parametersNum,
      float[] opNodeConnectProbability, float[] terminalNodeConnectProbability) {
    this.dim = dim;
    this.opsNum = opsNum;
    this.opsSet = opsSet;
    this.parametersNum = parametersNum;
    this.opNodeConnectProbability = opNodeConnectProbability;
    this.terminalNodeConnectProbability = terminalNodeConnectProbability;
  }

  public TAlgorithmGraph create(TMemory memory, TRandomGenerator rand) {
    while (true) {
      TAlgorithmGraphFactory factory = new TAlgorithmGraphFactory(this);
      try {
        return factory.create(memory, rand);
      } catch (Exception e) {
        continue;
      }
    }
  };
}
