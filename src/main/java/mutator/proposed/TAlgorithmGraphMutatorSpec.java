package mutator.proposed;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class TAlgorithmGraphMutatorSpec {
  /**
   * 実行可能な突然変異の種類
   */
  public TAlgorithmGraphMutation[] mutations;

  /**
   * 突然変異の選択確率
   * probabilities[i]は突然変異mutations[i]が選択される確率
   */
  public double[] probabilities;

  /**
   * 定数値の変更の突然変異の仕様
   */
  public TAlterConstValueMutatorSpec alterConstValueMutatorSpec;

  /**
   * 学習パラメータの初期値を変更する突然変異の仕様
   */
  public TAlterParameterInitialValueMutatorSpec alterParameterInitialValueMutatorSpec;

  /**
   * 定数ノードを接続に置き換える突然変異の仕様
   */
  public TReplaceConnectionWithConstNodeMutatorSpec replaceConnectionWithConstNodeMutatorSpec;

  /**
   * 接続を定数ノードに置き換える突然変異の仕様
   */
  public TReplaceConstNodeWithConnectionMutatorSpec replaceConstNodeWithConnectionMutatorSpec;

  /**
   * 接続関係を変更する突然変異の仕様
   */
  public TChangeConnectionMutatorSpec changeConnectionMutatorSpec;

  /**
   * 部分木を再構築する突然変異の仕様
   */
  public TReconstructSubGraphMutatorSpec reconstructSubGraphMutatorSpec;

  /**
   * ルートノードを再構築する突然変異の仕様
   */
  public TReconstructRootNodeMutatorSpec reconstructRootNodeMutatorSpec;

  public TAlgorithmGraphMutatorSpec() {
  }
}
