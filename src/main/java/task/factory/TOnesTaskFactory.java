package task.factory;

import task.spec.*;
import utils.TRandomGenerator;
import jp.ac.titech.onolab.core.matrix.TCMatrix;

public class TOnesTaskFactory extends TTaskFactory<TOnesTaskSpec> {
  public TOnesTaskFactory(TOnesTaskSpec spec) {
    super(spec);
  }

  protected TCMatrix getFeatureVector(TDataType type, int index, int featureSize, TRandomGenerator dataRand) {
    return new TCMatrix(featureSize).fill(1.0);
  };

  protected double getLabel(TDataType type, int index, TCMatrix feature, TRandomGenerator dataRand) {
    return 1.0;
  };
}
