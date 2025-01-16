package algorithm.factory;

import java.util.ArrayList;

import algorithm.core.TAlgorithm;
import algorithm.spec.TAffineAlgorithmSpec;
import instruction.TInstruction;
import instruction.TOp;
import memory.TMemory;
import utils.TRandomGenerator;

/**
 * A Algorithm with specific instruction.
 */
public class TAffineAlgorithmFactory extends TAlgorithmFactory<TAffineAlgorithmSpec> {
  private double fLearningRate;

  // Scalar addresses
  // 0 : label
  // 1 : prediction
  public static final int kLearningRateAddress = 2;
  public static final int kPredictionErrorAddress = 3;
  public static final int kInterceptAddress = 4;
  public static final int kTmpPredictionAddress = 3;

  // Vector addresses.
  // 0 : feature
  public static final int kWeightsAddress = 1;
  public static final int kCorrectionAddress = 2;

  public TAffineAlgorithmFactory(TAffineAlgorithmSpec spec) {
    super(spec);
    fLearningRate = spec.learningRate;
  }


  @Override
  public TAlgorithm create(final TMemory memory, TRandomGenerator rand) {
    TAlgorithm algorithm = new TAlgorithm();

    // setup
    ArrayList<TInstruction> setup = algorithm.getSetup();
    setup.add(new TInstruction(TOp.SCALAR_CONST_SET_OP, kLearningRateAddress, fLearningRate));

    // predict
    ArrayList<TInstruction> predict = algorithm.getPredict();
    predict.add(new TInstruction(TOp.VECTOR_INNER_PRODUCT_OP, kWeightsAddress,
        memory.vectorInputAddress, kTmpPredictionAddress));
    predict.add(new TInstruction(TOp.SCALAR_SUM_OP, kTmpPredictionAddress,
        kInterceptAddress, memory.scalarPredictionLabelAddress));

    // learn
    ArrayList<TInstruction> learn = algorithm.getLearn();
    learn.add(new TInstruction(TOp.SCALAR_DIFF_OP, memory.scalarCorrectLabelAddress,
        memory.scalarPredictionLabelAddress, kPredictionErrorAddress));
    learn.add(new TInstruction(TOp.SCALAR_PRODUCT_OP, kLearningRateAddress, kPredictionErrorAddress,
        kPredictionErrorAddress));
    learn.add(new TInstruction(TOp.SCALAR_VECTOR_PRODUCT_OP, kPredictionErrorAddress,
        memory.vectorInputAddress, kCorrectionAddress));
    learn.add(
        new TInstruction(TOp.VECTOR_SUM_OP, kWeightsAddress, kCorrectionAddress, kWeightsAddress));
    learn.add(
        new TInstruction(TOp.SCALAR_SUM_OP, kInterceptAddress, kPredictionErrorAddress, kInterceptAddress));

    return algorithm;
  }
}
