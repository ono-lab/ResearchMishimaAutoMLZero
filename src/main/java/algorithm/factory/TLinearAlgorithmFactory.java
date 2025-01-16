package algorithm.factory;

import java.util.ArrayList;

import algorithm.core.TAlgorithm;
import algorithm.spec.TLinearAlgorithmSpec;
import instruction.TInstruction;
import instruction.TOp;
import memory.TMemory;
import utils.TRandomGenerator;

/**
 * A Algorithm with specific instruction.
 */
public class TLinearAlgorithmFactory extends TAlgorithmFactory<TLinearAlgorithmSpec> {
  private double fLearningRate;

  // Scalar addresses
  // 0 : label
  // 1 : prediction
  public static final int kLearningRateAddress = 2;
  public static final int kPredictionErrorAddress = 3;

  // Vector addresses.
  // 0 : feature
  public static final int kWeightsAddress = 1;
  public static final int kCorrectionAddress = 2;

  public TLinearAlgorithmFactory(TLinearAlgorithmSpec spec) {
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
        memory.vectorInputAddress, memory.scalarPredictionLabelAddress));

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

    return algorithm;
  }
}
