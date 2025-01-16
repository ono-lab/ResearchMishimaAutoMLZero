package task.factory;

import algorithm.core.TAlgorithm;
import jp.ac.titech.onolab.core.matrix.TCMatrix;
import memory.TMemory;
import task.spec.TAlgorithmTaskSpec;
import utils.TRandomGenerator;

public abstract class TAlgorithmTaskFactory<T extends TAlgorithmTaskSpec> extends TTaskFactory<T> {
  private TMemory fMemory;
  private double fGaussianNoiseStdev;
  private TTaskType fTaskType;

  public TAlgorithmTaskFactory(T spec) {
    super(spec);
    fGaussianNoiseStdev = spec.gaussianNoiseStdev;
    fTaskType = spec.taskType;
  }

  public abstract TAlgorithm getAlgorithm(TMemory memory);

  public abstract TMemory setupMemory(int featureSize, TRandomGenerator paramRand);

  @Override
  protected void initialize(TRandomGenerator paramRand) {
    int featureSize = getSpec().featuresSize;
    fMemory = setupMemory(featureSize, paramRand);
  }

  @Override
  protected double getLabel(TDataType type, int index, TCMatrix feature, TRandomGenerator dataRand) {
    TAlgorithm algorithm = getAlgorithm(fMemory);
    double prediction = algorithm.executePredictWithoutNormalize(feature, fMemory, dataRand);
    double predictionWithNoise = prediction + dataRand.nextGaussian(0.0, fGaussianNoiseStdev);
    switch (fTaskType) {
      case REGRESSION:
        return predictionWithNoise;
      case CLASSIFICATION:
        return predictionWithNoise > 0 ? 1.0 : 0.0;
      default:
        throw new IllegalArgumentException("Unknown task type: " + fTaskType);
    }
  }
}