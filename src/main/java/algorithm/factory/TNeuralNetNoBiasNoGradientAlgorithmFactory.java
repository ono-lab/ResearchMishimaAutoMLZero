package algorithm.factory;

import java.util.ArrayList;

import algorithm.core.TAlgorithm;
import algorithm.spec.TNeuralNetNoBiasNoGradientAlgorithmSpec;
import instruction.TInstruction;
import instruction.TOp;
import memory.TMemory;
import utils.TRandomGenerator;

/**
 * A Algorithm with specific instruction.
 */
public class TNeuralNetNoBiasNoGradientAlgorithmFactory extends TAlgorithmFactory<TNeuralNetNoBiasNoGradientAlgorithmSpec> {
  private double fLearningRate;

  // A 2-layer neural network without bias and no learning.
  public static final int kUnitTestNeuralNetNoBiasNoGradientFinalLayerWeightsAddress = 1;
  public static final int kUnitTestNeuralNetNoBiasNoGradientFirstLayerWeightsAddress = 0;

  // Scalar addresses
  // 0 : label
  // 1 : prediction
  public static final int kLearningRateAddress = 2;
  public static final int kPredictionErrorAddress = 3;

  // Vector addresses.
  // 0 : feature
  public static final int kFinalLayerWeightsAddress =
      kUnitTestNeuralNetNoBiasNoGradientFinalLayerWeightsAddress;
  public static final int kFirstLayerOutputBeforeReluAddress = 2;
  public static final int kFirstLayerOutputAfterReluAddress = 3;
  public static final int kZerosAddress = 4;
  public static final int kGradientWrtFinalLayerWeightsAddress = 5;
  public static final int kGradientWrtActivationsAddress = 6;
  public static final int kGradientOfReluAddress = 7;

  // Matrix addresses.
  public static final int kFirstLayerWeightsAddress =
      kUnitTestNeuralNetNoBiasNoGradientFirstLayerWeightsAddress;
  public static final int kGradientWrtFirstLayerWeightsAddress = 1;

  public TNeuralNetNoBiasNoGradientAlgorithmFactory(TNeuralNetNoBiasNoGradientAlgorithmSpec spec) {
    super(spec);
    fLearningRate = spec.learningRate;
  }

  @Override
  public TAlgorithm create(final TMemory memory, TRandomGenerator rand) {
    TAlgorithm algorithm = new TAlgorithm();

    if (rand == null) {
      rand = new TRandomGenerator(0);
    }

    // setup
    ArrayList<TInstruction> setup = algorithm.getSetup();
    setup.add(new TInstruction(TOp.SCALAR_CONST_SET_OP, kLearningRateAddress,
        fLearningRate));

    // ReLUのデッドニューロン問題を回避
    for (int i = 0; i < memory.getDim(); i++) {
      setup.add(new TInstruction(TOp.VECTOR_CONST_SET_OP, kFinalLayerWeightsAddress, rand.nextDouble(), i));
      for (int j = 0; j < memory.getDim(); j++) {
        setup.add(new TInstruction(TOp.MATRIX_CONST_SET_OP, kFirstLayerWeightsAddress, rand.nextDouble(), i, j));
      }
    }

    // predict
    ArrayList<TInstruction> predict = algorithm.getPredict();
    // Multiply with first layer weight matrix.
    predict.add(new TInstruction(TOp.MATRIX_VECTOR_PRODUCT_OP, kFirstLayerWeightsAddress,
        memory.vectorInputAddress, kFirstLayerOutputBeforeReluAddress));
    // Apply RELU.
    predict.add(new TInstruction(TOp.VECTOR_MAX_OP, kFirstLayerOutputBeforeReluAddress,
        kZerosAddress, kFirstLayerOutputAfterReluAddress));
    // Dot product with final layer weight vector.
    predict.add(new TInstruction(TOp.VECTOR_INNER_PRODUCT_OP, kFirstLayerOutputAfterReluAddress,
        kFinalLayerWeightsAddress, memory.scalarPredictionLabelAddress));

    // learn
    ArrayList<TInstruction> learn = algorithm.getLearn();

    // 2層目の更新
    learn.add(new TInstruction(TOp.SCALAR_DIFF_OP, memory.scalarCorrectLabelAddress,
        memory.scalarPredictionLabelAddress, kPredictionErrorAddress));
    learn.add(new TInstruction(TOp.SCALAR_PRODUCT_OP, kLearningRateAddress, kPredictionErrorAddress,
        kPredictionErrorAddress));
    learn.add(new TInstruction(TOp.SCALAR_VECTOR_PRODUCT_OP, kPredictionErrorAddress, kFirstLayerOutputAfterReluAddress,
        kGradientWrtFinalLayerWeightsAddress));

    // 1層目の更新
    learn.add(new TInstruction(TOp.SCALAR_VECTOR_PRODUCT_OP, kPredictionErrorAddress, kFinalLayerWeightsAddress,
        kGradientWrtActivationsAddress));
    learn.add(new TInstruction(TOp.VECTOR_HEAVYSIDE_OP, kFirstLayerOutputBeforeReluAddress, kGradientOfReluAddress));
    learn.add(new TInstruction(TOp.VECTOR_PRODUCT_OP, kGradientOfReluAddress, kGradientWrtActivationsAddress, kGradientWrtActivationsAddress));
    learn.add(new TInstruction(TOp.VECTOR_OUTER_PRODUCT_OP, kGradientWrtActivationsAddress, memory.vectorInputAddress,
                    kGradientWrtFirstLayerWeightsAddress));

    learn.add(new TInstruction(TOp.VECTOR_SUM_OP, kFinalLayerWeightsAddress, kGradientWrtFinalLayerWeightsAddress,
        kFinalLayerWeightsAddress));
    learn.add(new TInstruction(TOp.MATRIX_SUM_OP, kFirstLayerWeightsAddress,
        kGradientWrtFirstLayerWeightsAddress, kFirstLayerWeightsAddress));

    return algorithm;
  }
}
