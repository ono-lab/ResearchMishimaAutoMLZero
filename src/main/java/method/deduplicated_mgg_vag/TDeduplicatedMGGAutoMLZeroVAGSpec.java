package method.deduplicated_mgg_vag;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import algorithm.spec.TAlgorithmGraphSpec;
import method.TMethod;
import method.TMethodSpec;
import mutator.proposed.TAlgorithmGraphMutatorSpec;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class TDeduplicatedMGGAutoMLZeroVAGSpec extends TMethodSpec {
  // 集団サイズ
  public int populationSize;

  // 子個体の生成数
  public int numOfChildren;

  // 初期個体の設定
  public TAlgorithmGraphSpec initialPopulationAlgorithmSpec;

  // 突然変異の設定
  public TAlgorithmGraphMutatorSpec mutatorSpec;

  // 世代交代の2つ目に追加する個体でどの程度多様性を優先するか
  public double weightOfDiversity = 0.0;

  @Override
  public TMethod toMethod() {
    return new TDeduplicatedMGGAutoMLZeroVAG(this);
  }
}
