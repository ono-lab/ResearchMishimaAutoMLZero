package method.mgg_vag;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import algorithm.spec.TAlgorithmGraphSpec;
import method.TMethod;
import method.TMethodSpec;
import mutator.proposed.TAlgorithmGraphMutatorSpec;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class TMGGAutoMLZeroVAGSpec extends TMethodSpec {
  // 集団サイズ
  public int populationSize;

  // 子個体の生成数
  public int numOfChildren;

  // 初期個体の設定
  public TAlgorithmGraphSpec initialPopulationAlgorithmSpec;

  // 突然変異の設定
  public TAlgorithmGraphMutatorSpec mutatorSpec;

  @Override
  public TMethod toMethod() {
    return new TMGGAutoMLZeroVAG(this);
  }
}
