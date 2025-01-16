package algorithm.factory;

import java.util.ArrayList;

import algorithm.core.TAlgorithm;
import algorithm.spec.TNeuralNetAlgorithmSpec;
import instruction.TInstruction;
import instruction.TOp;
import memory.TMemory;
import utils.TRandomGenerator;

/**
 * A Algorithm with specific instruction.
 */
public class TNeuralNetAlgorithmFactory extends TAlgorithmFactory<TNeuralNetAlgorithmSpec> {
  private double fLearningRate;
  private float fFirstInitScale;
  private float fFinalInitScale;

  // Scalar addresses
  // 0 : label
  // 1 : prediction
  public static final int kFinalLayerBiasAddress = 2;
  public static final int kLearningRateAddress = 3;
  public static final int kPredictionErrorAddress = 4;

  // Vector addresses.
  // 0 : feature
  public static final int kFirstLayerBiasAddress = 1;
  public static final int kFinalLayerWeightsAddress = 2;
  public static final int kFirstLayerOutputBeforeReluAddress = 3;
  public static final int kFirstLayerOutputAfterReluAddress = 4;
  public static final int kZerosAddress = 5;
  public static final int kGradientWrtFinalLayerWeightsAddress = 6;
  public static final int kGradientWrtActivationsAddress = 7;
  public static final int kGradientOfReluAddress = 8;

  // Matrix addresses.
  public static final int kFirstLayerWeightsAddress = 0;
  public static final int kGradientWrtFirstLayerWeightsAddress = 1;

  public TNeuralNetAlgorithmFactory(TNeuralNetAlgorithmSpec spec) {
    super(spec);
    fLearningRate = spec.learningRate;
    fFirstInitScale = spec.firstInitScale;
    fFinalInitScale = spec.finalInitScale;
  }

  @Override
  public TAlgorithm create(final TMemory memory, TRandomGenerator rand) {
    TAlgorithm algorithm = new TAlgorithm();

    // setup
    ArrayList<TInstruction> setup = algorithm.getSetup();
    setup.add(new TInstruction(TOp.VECTOR_GAUSSIAN_SET_OP, kFinalLayerWeightsAddress,
        0.0f, fFinalInitScale));
    setup.add(new TInstruction(TOp.MATRIX_GAUSSIAN_SET_OP, kFirstLayerWeightsAddress,
        0.0f, fFirstInitScale));
    setup.add(new TInstruction(TOp.SCALAR_CONST_SET_OP, kLearningRateAddress,
       fLearningRate));

    // predict
    ArrayList<TInstruction> predict = algorithm.getPredict();
    // Multiply with first layer weight matrix.
    predict.add(new TInstruction(TOp.MATRIX_VECTOR_PRODUCT_OP, kFirstLayerWeightsAddress,
        memory.vectorInputAddress, kFirstLayerOutputBeforeReluAddress));
    // Add first layer bias.
    predict.add(new TInstruction(TOp.VECTOR_SUM_OP, kFirstLayerOutputBeforeReluAddress,
        kFirstLayerBiasAddress, kFirstLayerOutputBeforeReluAddress));
    // Apply RELU.
    predict.add(new TInstruction(TOp.VECTOR_MAX_OP, kFirstLayerOutputBeforeReluAddress,
        kZerosAddress, kFirstLayerOutputAfterReluAddress));
    // Dot product with final layer weight vector.
    predict.add(new TInstruction(TOp.VECTOR_INNER_PRODUCT_OP, kFirstLayerOutputAfterReluAddress,
        kFinalLayerWeightsAddress, memory.scalarPredictionLabelAddress));
    // Add final layer bias.
    predict.add(new TInstruction(TOp.SCALAR_SUM_OP, memory.scalarPredictionLabelAddress,
        kFinalLayerBiasAddress, memory.scalarPredictionLabelAddress));

    // learn
    ArrayList<TInstruction> learn = algorithm.getLearn();
    learn.add(new TInstruction(TOp.SCALAR_DIFF_OP, memory.scalarCorrectLabelAddress,
        memory.scalarPredictionLabelAddress, kPredictionErrorAddress));
    learn.add(new TInstruction(TOp.SCALAR_PRODUCT_OP, kLearningRateAddress, kPredictionErrorAddress,
        kPredictionErrorAddress));

    // Update final layer bias.
    learn.add(new TInstruction(TOp.SCALAR_SUM_OP, kFinalLayerBiasAddress, kPredictionErrorAddress,
        kFinalLayerBiasAddress));
    learn.add(new TInstruction(TOp.SCALAR_VECTOR_PRODUCT_OP, kPredictionErrorAddress,
        kFirstLayerOutputAfterReluAddress, kGradientWrtFinalLayerWeightsAddress));
    learn.add(new TInstruction(TOp.VECTOR_SUM_OP, kFinalLayerWeightsAddress,
        kGradientWrtFinalLayerWeightsAddress, kFinalLayerWeightsAddress));
    learn.add(new TInstruction(TOp.SCALAR_VECTOR_PRODUCT_OP, kPredictionErrorAddress,
        kFinalLayerWeightsAddress, kGradientWrtActivationsAddress));
    learn.add(new TInstruction(TOp.VECTOR_HEAVYSIDE_OP, kFirstLayerOutputBeforeReluAddress, 0,
        kGradientOfReluAddress));
    learn.add(new TInstruction(TOp.VECTOR_PRODUCT_OP, kGradientOfReluAddress,
        kGradientWrtActivationsAddress, kGradientWrtActivationsAddress));

    // Update first layer bias.
    learn.add(new TInstruction(TOp.VECTOR_SUM_OP, kFirstLayerBiasAddress,
        kGradientWrtActivationsAddress, kFirstLayerBiasAddress));
    learn.add(new TInstruction(TOp.VECTOR_OUTER_PRODUCT_OP, kGradientWrtActivationsAddress,
        memory.vectorInputAddress, kGradientWrtFirstLayerWeightsAddress));
    learn.add(new TInstruction(TOp.MATRIX_SUM_OP, kFirstLayerWeightsAddress,
        kGradientWrtFirstLayerWeightsAddress, kFirstLayerWeightsAddress));

    return algorithm;
  }
}
