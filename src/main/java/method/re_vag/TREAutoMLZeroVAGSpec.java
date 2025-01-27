package method.re_vag;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import algorithm.spec.TAlgorithmGraphSpec;
import method.TMethod;
import method.TMethodSpec;
import mutator.proposed.TAlgorithmGraphMutatorSpec;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class TREAutoMLZeroVAGSpec extends TMethodSpec {
  public int populationSize;
  public int tournamentSize;

  // 初期個体の設定
  public TAlgorithmGraphSpec initialPopulationAlgorithmSpec;

  // 突然変異の設定
  public double mutateProb;
  public TAlgorithmGraphMutatorSpec mutatorSpec;

  @Override
  public TMethod toMethod() {
    return new TREAutoMLZeroVAG(this);
  }
}
