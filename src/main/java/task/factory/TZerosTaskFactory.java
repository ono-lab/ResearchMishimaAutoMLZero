package task.factory;

import task.spec.*;
import utils.TRandomGenerator;
import jp.ac.titech.onolab.core.matrix.TCMatrix;

public class TZerosTaskFactory extends TTaskFactory<TZerosTaskSpec> {

  public TZerosTaskFactory(TZerosTaskSpec spec) {
    super(spec);
  }

  protected TCMatrix getFeatureVector(TDataType type, int index, int featureSize, TRandomGenerator dataRand) {
    return new TCMatrix(featureSize);
  };

  protected double getLabel(TDataType type, int index, TCMatrix feature, TRandomGenerator dataRand) {
    return 0.0;
  };
}
