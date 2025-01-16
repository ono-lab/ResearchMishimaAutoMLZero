package algorithm.core;

import java.util.ArrayList;
import java.util.Objects;

import instruction.TInstruction;
import jp.ac.titech.onolab.core.matrix.TCMatrix;
import memory.TMemory;
import task.core.TEvalMethod;
import task.core.TTask;
import utils.TRandomGenerator;

/**
 * アルゴリズム
 */
public class TAlgorithm {
  /** Setup関数 */
  private ArrayList<TInstruction> fSetup = new ArrayList<TInstruction>();

  /** Predict関数 */
  private ArrayList<TInstruction> fPredict = new ArrayList<TInstruction>();

  /** Learn関数 */
  private ArrayList<TInstruction> fLearn = new ArrayList<TInstruction>();

  /**
   * 空のアルゴリズムを生成するコンストラクタ
   */
  public TAlgorithm() {
  }

  /**
   * setup, predict, learnを指定してアルゴリズムを生成するコンストラクタ
   */
  public TAlgorithm( ArrayList<TInstruction> setup, ArrayList<TInstruction> predict, ArrayList<TInstruction> learn) {
    fSetup = setup;
    fPredict = predict;
    fLearn = learn;
  }


  /**
   * 他のアルゴリズムからコピーする関数
   */
  public TAlgorithm copyFrom(TAlgorithm other) {
    if (other != this) {
      TInstruction.copy(other.fSetup, fSetup);
      TInstruction.copy(other.fPredict, fPredict);
      TInstruction.copy(other.fLearn, fLearn);
    }
    return this;
  }

  public TAlgorithm(TAlgorithm other) {
    copyFrom(other);
  }

  /**
   * setup関数の取得
   */
  public ArrayList<TInstruction> getSetup() {
    return fSetup;
  }

  /**
   * predict関数の取得
   */
  public ArrayList<TInstruction> getPredict() {
    return fPredict;
  }

  /**
   * learn関数の取得
   */
  public ArrayList<TInstruction> getLearn() {
    return fLearn;
  }

  /**
   * 指定されたコンポーネント（setup, predict, learnのいずれか）を返却する関数
   */
  public ArrayList<TInstruction> getComponent(TAlgorithmComponentType componentType) {
    switch (componentType) {
      case SETUP:
        return fSetup;
      case PREDICT:
        return fPredict;
      case LEARN:
        return fLearn;
      default:
        throw new RuntimeException("Should not reach here.");
    }
  }

  /**
   * 特定の命令でsetup, predict, learnのいずれかを埋める関数
   */
  public void fillComponentWith(TAlgorithmComponentType componentType, TInstruction instruction,
      int numOfInstructions) {
    ArrayList<TInstruction> component = getComponent(componentType);
    component.clear();
    for (int index = 0; index < numOfInstructions; index++) {
      component.add(instruction.clone());
    }
  }

  /**
   * NO_OPでsetup, predict, learnのいずれかを埋める関数
   */
  public void fillComponentWithNoOp(TAlgorithmComponentType componentType, int numOfInstructions) {
    fillComponentWith(componentType, new TInstruction(), numOfInstructions);
  }

  public void executeSetup(TMemory memory, TRandomGenerator rand) {
    TAlgorithmExecutor.setup(this, memory, rand);
  }


  public double executePredict(TCMatrix feature, TEvalMethod evalMethod,
      TMemory memory, TRandomGenerator rand) {
    return TAlgorithmExecutor.predict(this, feature, evalMethod, memory, rand);
  }

  public double executePredictWithoutNormalize(TCMatrix feature, TMemory memory, TRandomGenerator rand) {
    return TAlgorithmExecutor.predictWithoutNormalize(this, feature, memory, rand);
  }

  public void executeLearn(TCMatrix feature, double label, TEvalMethod evalMethod,
      TMemory memory, TRandomGenerator rand) {
    TAlgorithmExecutor.learn(this, feature, label, memory, rand);
  }

  public double execute(final TTask dataset, final int numOfAllTrainExamples, final int numOfAllValidExamples,
      final double maxAbsError, final TMemory memory, final TRandomGenerator rand,
          ArrayList<Double> trainErrors, ArrayList<Double> validErrors) {
    return TAlgorithmExecutor.execute(this, dataset, numOfAllTrainExamples, numOfAllValidExamples,
        maxAbsError, memory, rand, trainErrors, validErrors);
  }

  public double execute(final TTask dataset, final int numOfAllTrainExamples, final int numOfAllValidExamples,
      final double maxAbsError, final TMemory memory, final TRandomGenerator rand) {
    return TAlgorithmExecutor.execute(this, dataset, numOfAllTrainExamples, numOfAllValidExamples,
        maxAbsError, memory, rand);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this)
      return true;
    if (!(other instanceof TAlgorithm))
      return false;
    TAlgorithm otherAlgorithm = (TAlgorithm) other;
    if (!Objects.equals(fSetup, otherAlgorithm.fSetup))
      return false;
    if (!Objects.equals(fPredict, otherAlgorithm.fPredict))
      return false;
    if (!Objects.equals(fLearn, otherAlgorithm.fLearn))
      return false;
    return true;
  }

  @Override
  public TAlgorithm clone() {
    return new TAlgorithm(this);
  }

  @Override
  public String toString() {
    String str = "";
    str += "def Setup():\n";
    for (TInstruction instr : fSetup)
      str += instr.toString(2) + "\n";
    str += "\ndef Predict():\n";
    for (TInstruction instr : fPredict)
      str += instr.toString(2) + "\n";
    str += "\ndef Learn():\n";
    for (TInstruction instr : fLearn)
      str += instr.toString(2) + "\n";
    return str;
  }
}
