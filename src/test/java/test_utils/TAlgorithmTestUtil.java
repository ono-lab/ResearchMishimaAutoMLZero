package test_utils;

import java.util.ArrayList;
import java.util.Objects;

import algorithm.core.*;
import algorithm.factory.*;
import algorithm.spec.*;
import instruction.*;
import memory.TMemory;
import utils.TArrayUtility;
import utils.TRandomGenerator;

public class TAlgorithmTestUtil {
  static public TMemory kMemory = new TMemory(4);
  static public TRandomGenerator kRand = new TRandomGenerator(0);

  static public TAlgorithm getSimpleNoOpAlgorithm() {
    TNoOpAlgorithmSpec spec = new TNoOpAlgorithmSpec(6, 3, 9);
    return new TNoOpAlgorithmFactory(spec).create(kMemory, kRand);
  }

  static public TAlgorithm getSimpleRandomAlgorithm() {
    TOp[] setupOps = new TOp[] { TOp.SCALAR_SUM_OP, TOp.MATRIX_VECTOR_PRODUCT_OP, TOp.VECTOR_MEAN_OP };
    TOp[] predictOps = new TOp[] { TOp.SCALAR_SUM_OP, TOp.MATRIX_VECTOR_PRODUCT_OP, TOp.VECTOR_MEAN_OP };
    TOp[] learnOps = new TOp[] { TOp.SCALAR_SUM_OP, TOp.MATRIX_VECTOR_PRODUCT_OP, TOp.VECTOR_MEAN_OP };
    TRandomAlgorithmSpec spec = new TRandomAlgorithmSpec(6, 3, 9,
        setupOps, predictOps, learnOps);
    return new TRandomAlgorithmFactory(spec).create(kMemory, kRand);
  }

  static public TAlgorithm getSimpleLinearAlgorithm() {
    TLinearAlgorithmSpec spec = new TLinearAlgorithmSpec();
    return new TLinearAlgorithmFactory(spec).create(kMemory, kRand);
  }

  static public TAlgorithm getSimpleNeuralNetAlgorithm() {
    TNeuralNetAlgorithmSpec spec = new TNeuralNetAlgorithmSpec();
    return new TNeuralNetAlgorithmFactory(spec).create(kMemory, kRand);
  }

  static public TAlgorithm getSimpleNeuralNetAlgorithm(double learningRate, float firstInitScale,
      float finalInitScale) {
    TNeuralNetAlgorithmSpec spec = new TNeuralNetAlgorithmSpec(learningRate, firstInitScale, finalInitScale);
    return new TNeuralNetAlgorithmFactory(spec).create(kMemory, kRand);
  }

  static public TAlgorithm getOriginalAutoMLZeroDemoHighFitnessAlgorithm() {
    TAlgorithm algorithm = new TAlgorithm();
    ArrayList<TInstruction> setup = algorithm.getSetup();
    setup.add(new TInstruction(TOp.SCALAR_PRODUCT_OP, 3, 1, 1));
    setup.add(new TInstruction(TOp.SCALAR_PRODUCT_OP, 3, 2, 1));
    setup.add(new TInstruction(TOp.VECTOR_SUM_OP, 2, 1, 2));
    setup.add(new TInstruction(TOp.SCALAR_DIFF_OP, 3, 3, 2));
    setup.add(new TInstruction(TOp.SCALAR_CONST_SET_OP, 1, 0.0951716));
    setup.add(new TInstruction(TOp.SCALAR_PRODUCT_OP, 3, 3, 3));
    setup.add(new TInstruction(TOp.SCALAR_CONST_SET_OP, 2, -0.166948));
    setup.add(new TInstruction(TOp.VECTOR_SUM_OP, 0, 0, 2));
    setup.add(new TInstruction(TOp.SCALAR_PRODUCT_OP, 0, 1, 1));

    ArrayList<TInstruction> predict = algorithm.getPredict();
    predict.add(new TInstruction(TOp.SCALAR_PRODUCT_OP, 3, 0, 3));
    predict.add(new TInstruction(TOp.VECTOR_INNER_PRODUCT_OP, 2, 0, 1));

    ArrayList<TInstruction> learn = algorithm.getLearn();
    learn.add(new TInstruction(TOp.SCALAR_DIFF_OP, 1, 0, 3));
    learn.add(new TInstruction(TOp.VECTOR_SUM_OP, 1, 2, 2));
    learn.add(new TInstruction(TOp.SCALAR_VECTOR_PRODUCT_OP, 2, 0, 1));
    learn.add(new TInstruction(TOp.SCALAR_PRODUCT_OP, 3, 3, 2));
    learn.add(new TInstruction(TOp.SCALAR_CONST_SET_OP, 2, 0.528608));
    learn.add(new TInstruction(TOp.SCALAR_VECTOR_PRODUCT_OP, 3, 1, 1));
    learn.add(new TInstruction(TOp.SCALAR_CONST_SET_OP, 2, -0.0702741));
    learn.add(new TInstruction(TOp.VECTOR_SUM_OP, 2, 1, 2));
    return algorithm;
  }

  static private void setIncreasingDataInComponent(ArrayList<TInstruction> component) {
    for (int position = 0; position < component.size(); position++) {
      component.get(position).setDoubleData1(position);
    }
  }

  static public TAlgorithm getSimpleIncreasingDataAlgorithm() {
    TAlgorithm algorithm = getSimpleNoOpAlgorithm();
    setIncreasingDataInComponent(algorithm.getSetup());
    setIncreasingDataInComponent(algorithm.getPredict());
    setIncreasingDataInComponent(algorithm.getLearn());
    return algorithm;
  }

  static public int getDifferentNumOfSetupInstructions(TAlgorithm algorithm1,
      TAlgorithm algorithm2) {
    int numOfDiff = 0;
    assert algorithm1.getSetup().size() == algorithm2.getSetup().size();
    int size = algorithm1.getSetup().size();
    for (int i = 0; i < size; i++) {
      TInstruction instr1 = algorithm1.getSetup().get(i);
      TInstruction instr2 = algorithm2.getSetup().get(i);
      if (!Objects.equals(instr1, instr2)) {
        numOfDiff += 1;
      }
    }
    return numOfDiff;
  }

  static public int getDifferentNumOfPredictInstructions(TAlgorithm algorithm1,
      TAlgorithm algorithm2) {
    int numOfDiff = 0;
    assert algorithm1.getPredict().size() == algorithm2.getPredict().size();
    int size = algorithm1.getPredict().size();
    for (int i = 0; i < size; i++) {
      TInstruction instr1 = algorithm1.getPredict().get(i);
      TInstruction instr2 = algorithm2.getPredict().get(i);
      if (!Objects.equals(instr1, instr2)) {
        numOfDiff += 1;
      }
    }
    return numOfDiff;
  }

  static public int getDifferentNumOfLearnInstructions(TAlgorithm algorithm1,
      TAlgorithm algorithm2) {
    int numOfDiff = 0;
    assert algorithm1.getLearn().size() == algorithm2.getLearn().size();
    int size = algorithm1.getLearn().size();
    for (int i = 0; i < size; i++) {
      TInstruction instr1 = algorithm1.getLearn().get(i);
      TInstruction instr2 = algorithm2.getLearn().get(i);
      if (!Objects.equals(instr1, instr2)) {
        numOfDiff += 1;
      }
    }
    return numOfDiff;
  }

  static public int getDifferentNumOfInstructions(TAlgorithm algorithm1, TAlgorithm algorithm2) {
    return getDifferentNumOfSetupInstructions(algorithm1, algorithm2)
        + getDifferentNumOfPredictInstructions(algorithm1, algorithm2)
        + getDifferentNumOfLearnInstructions(algorithm1, algorithm2);
  }

  static public TAlgorithmComponentType[] getDifferentAlgorithmComponentTypes(TAlgorithm algorithm1,
      TAlgorithm algorithm2) {
    ArrayList<TAlgorithmComponentType> componentTypes = new ArrayList<TAlgorithmComponentType>();
    if (algorithm1.getSetup().size() != algorithm2.getSetup().size()
        || getDifferentNumOfSetupInstructions(algorithm1, algorithm2) > 0) {
      componentTypes.add(TAlgorithmComponentType.SETUP);
    }
    if (algorithm1.getPredict().size() != algorithm2.getPredict().size()
        || getDifferentNumOfPredictInstructions(algorithm1, algorithm2) > 0) {
      componentTypes.add(TAlgorithmComponentType.PREDICT);
    }
    if (algorithm1.getLearn().size() != algorithm2.getLearn().size()
        || getDifferentNumOfLearnInstructions(algorithm1, algorithm2) > 0) {
      componentTypes.add(TAlgorithmComponentType.LEARN);
    }
    TAlgorithmComponentType[] componentTypeArray = new TAlgorithmComponentType[componentTypes.size()];
    for (int i = 0; i < componentTypes.size(); i++) {
      componentTypeArray[i] = componentTypes.get(i);
    }
    return componentTypeArray;
  }

  static public int getScalarSumOpPosition(ArrayList<TInstruction> component) {
    int result = -1;
    for (int position = 0; position < component.size(); position++) {
      if (component.get(position).getOp() == TOp.SCALAR_SUM_OP) {
        if (result != -1) {
          return -2; // 2つ以上存在した場合
        } else {
          result = position;
        }
      }
    }
    return result;
  }

  static public double getMissingDataInComponent(ArrayList<TInstruction> component1,
      ArrayList<TInstruction> component2) {
    ArrayList<Double> data2 = new ArrayList<Double>();
    for (TInstruction instruction : component2) {
      data2.add(instruction.getDoubleData1());
    }
    ArrayList<Double> missing = new ArrayList<Double>();
    for (TInstruction instruction : component1) {
      double data1Value = instruction.getDoubleData1();
      if (TArrayUtility.find(data2, data1Value) == null) {
        missing.add(data1Value);
      }
    }
    if (missing.isEmpty()) {
      return -1;
    } else if (missing.size() == 1) {
      return missing.get(0);
    } else {
      return -2;
    }
  }

}
