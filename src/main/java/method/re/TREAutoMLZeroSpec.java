package method.re;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import algorithm.spec.TAlgorithmSpec;
import method.TMethod;
import method.TMethodSpec;
import mutator.esteban.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class TREAutoMLZeroSpec extends TMethodSpec {
  public int populationSize;
  public int tournamentSize;
  public TAlgorithmMutatorSpec mutatorSpec;
  public TAlgorithmSpec initialPopulationAlgorithmSpec;

  public int numOfScalarAddresses;
  public int numOfVectorAddresses;
  public int numOfMatrixAddresses;

  @Override
  public TMethod toMethod() {
    return new TREAutoMLZero(this);
  }
}
