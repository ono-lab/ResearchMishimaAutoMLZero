package task.factory;

import task.spec.*;
import utils.TRandomGenerator;
import jp.ac.titech.onolab.core.matrix.TCMatrix;

public class TIncrementTaskFactory extends TTaskFactory<TIncrementTaskSpec> {
  private double fIncrement;

  public TIncrementTaskFactory(TIncrementTaskSpec spec) {
    super(spec);
    fIncrement = spec.increment;
  }


  protected TCMatrix getFeatureVector(TDataType type, int index, int featureSize, TRandomGenerator dataRand) {
    double incremented = fIncrement * index;
    return new TCMatrix(featureSize).fill(incremented);
  };

  protected double getLabel(TDataType type, int index, TCMatrix feature, TRandomGenerator dataRand) {
    return fIncrement * index;
  };
}
