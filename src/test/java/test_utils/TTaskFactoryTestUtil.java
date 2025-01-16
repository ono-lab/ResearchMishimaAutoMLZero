package test_utils;

import task.core.TEvalMethod;
import task.spec.*;

public class TTaskFactoryTestUtil {
  static public final int kNumOfTrainExamples = 1000;
  static public final int kNumOfValidExamples = 100;
  static public final int kFeaturesSize = 4;

  static private void setCommonFiledOfTaskSpec(TTaskSpec spec) {
    spec.featuresSize = kFeaturesSize;
    spec.numOfTrainExamples = kNumOfTrainExamples;
    spec.numOfValidExamples = kNumOfValidExamples;
  }

  static public TScalar2LayerNNTaskSpec getScalar2LayerNNRegressionTaskSpec(
      int numOfTasks, long[] paramSeeds, long[] dataSeeds) {
    TScalar2LayerNNTaskSpec spec = new TScalar2LayerNNTaskSpec();
    setCommonFiledOfTaskSpec(spec);
    spec.numOfTasks = numOfTasks;
    spec.dataSeeds = dataSeeds;
    spec.paramSeeds = paramSeeds;
    spec.evalMethod = TEvalMethod.RMS_ERROR;
    return spec;
  }

  static public TZerosTaskSpec getZerosTaskSpec(TEvalMethod evalMethod) {
    TZerosTaskSpec spec = new TZerosTaskSpec();
    setCommonFiledOfTaskSpec(spec);
    spec.evalMethod = evalMethod;
    return spec;
  }

  static public TOnesTaskSpec getOnesTaskSpec(TEvalMethod evalMethod) {
    TOnesTaskSpec spec = new TOnesTaskSpec();
    setCommonFiledOfTaskSpec(spec);
    spec.evalMethod = evalMethod;
    return spec;
  }

  static public TIncrementTaskSpec getIncrementTaskSpec(TEvalMethod evalMethod) {
    TIncrementTaskSpec spec = new TIncrementTaskSpec();
    setCommonFiledOfTaskSpec(spec);
    spec.evalMethod = evalMethod;
    return spec;
  }
}
