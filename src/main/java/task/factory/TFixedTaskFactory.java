package task.factory;

import task.spec.*;
import utils.TRandomGenerator;
import jp.ac.titech.onolab.core.matrix.TCMatrix;

public class TFixedTaskFactory extends TTaskFactory<TFixedTaskSpec> {
  private  TCMatrix[] fTrainFeatures;
  private TCMatrix[] fValidFeatures;
  private double[] fTrainLabels;
  private double[] fValidLabels;

  public TFixedTaskFactory(TFixedTaskSpec spec) {
    super(spec);
    fTrainFeatures = spec.trainFeatures;
    fValidFeatures = spec.validFeatures;
    fTrainLabels = spec.trainLabels;
    fValidLabels = spec.validLabels;
  }

  protected TCMatrix getFeatureVector(TDataType type, int index, int featureSize, TRandomGenerator dataRand) {
    switch (type) {
      case TRAIN:
        return fTrainFeatures[index];
      case VALID:
        return fValidFeatures[index];
      default:
        throw new IllegalArgumentException("Invalid type: " + type);
    }
  };

  protected double getLabel(TDataType type, int index, TCMatrix feature, TRandomGenerator dataRand) {
    switch (type) {
      case TRAIN:
        return fTrainLabels[index];
      case VALID:
        return fValidLabels[index];
      default:
        throw new IllegalArgumentException("Invalid type: " + type);
    }
  };
}
