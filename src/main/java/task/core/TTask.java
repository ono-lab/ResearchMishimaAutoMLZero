package task.core;

import java.util.ArrayList;
import java.util.Objects;
import jp.ac.titech.onolab.core.matrix.TCMatrix;
import utils.TRandomGenerator;

public class TTask {
  private int fFeatureSize;
  private TEvalMethod fEvalMethod;

  // タスクのインデックス、キャッシュする際にタスクを区別するために必要
  private int fIndex;

  private ArrayList<TCMatrix> fTrainFeatures;
  private ArrayList<TCMatrix> fValidFeatures;
  private ArrayList<Double> fTrainLabels;
  private ArrayList<Double> fValidLabels;
  private int[][] fTrainEpochs;
  private int[][] fValidEpochs;

  static int[][] generateEpochs(int numOfExamples, int numOfEpochs, TRandomGenerator rand) {
    int[][] epochs = new int[numOfEpochs][numOfExamples];
    for (int epochIndex = 0; epochIndex < numOfEpochs; epochIndex++) {
      epochs[epochIndex] = rand.nextUniqueIntegerArray(numOfExamples);
    }
    return epochs;
  }

  public TTask(int index, int numOfTrainEpochs, TTaskBuffer buffer, TRandomGenerator rand) {
    fIndex = index;
    assert !buffer.isConsumed();
    fEvalMethod = buffer.getEvalMethod();
    fFeatureSize = buffer.getFeatureSize();
    fTrainFeatures = buffer.getTrainFeatures();
    fTrainLabels = buffer.getTrainLabels();
    fValidFeatures = buffer.getValidFeatures();
    fValidLabels = buffer.getValidLabels();
    buffer.consume();
    assert fTrainFeatures.size() == fTrainLabels.size();
    assert fValidFeatures.size() == fValidLabels.size();
    fTrainEpochs = generateEpochs(fTrainFeatures.size(), numOfTrainEpochs, rand);
    fValidEpochs = generateEpochs(fValidFeatures.size(), 1, rand);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof TTask)) {
      return false;
    }
    TTask otherTask = (TTask) other;

    // train data
    assert fTrainFeatures.size() == fTrainLabels.size();
    assert otherTask.fTrainFeatures.size() == otherTask.fTrainLabels.size();
    if (!Objects.equals(fTrainFeatures, otherTask.fTrainFeatures)) {
      return false;
    }
    if (!Objects.equals(fTrainLabels, otherTask.fTrainLabels)) {
      return false;
    }
    if (fTrainEpochs.length != otherTask.fTrainEpochs.length) {
      return false;
    }

    // valid data
    assert fValidFeatures.size() == fValidLabels.size();
    assert otherTask.fValidFeatures.size() == otherTask.fValidLabels.size();
    if (!Objects.equals(fValidFeatures, otherTask.fValidFeatures)) {
      return false;
    }
    if (!Objects.equals(fValidLabels, otherTask.fValidLabels)) {
      return false;
    }
    assert fValidEpochs.length == 1;
    assert otherTask.fValidEpochs.length == 1;

    return true;
  }

  public int getIndex() {
    return fIndex;
  }

  public int getFeatureSize() {
    return fFeatureSize;
  }

  public ArrayList<TCMatrix> getTrainFeatures() {
    return fTrainFeatures;
  }

  public TCMatrix getTrainFeature(int index) {
    return fTrainFeatures.get(index);
  }

  public ArrayList<Double> getTrainLabels() {
    return fTrainLabels;
  }

  public double getTrainLabel(int index) {
    return fTrainLabels.get(index);
  }

  public ArrayList<TCMatrix> getValidFeatures() {
    return fValidFeatures;
  }

  public TCMatrix getValidFeature(int index) {
    return fValidFeatures.get(index);
  }

  public ArrayList<Double> getValidLabels() {
    return fValidLabels;
  }

  public double getValidLabel(int index) {
    return fValidLabels.get(index);
  }

  public TEvalMethod getEvalType() {
    return fEvalMethod;
  }

  public int getNumOfTrainExamplesPerEpoch() {
    return fTrainFeatures.size();
  }

  public int getNumOfTrainEpochs() {
    return fTrainEpochs.length;
  }

  public int getNumOfMaxTrainExamples() {
    return getNumOfTrainExamplesPerEpoch() * getNumOfTrainEpochs();
  }

  public int getNumOfValidExamples() {
    return fValidFeatures.size();
  }

  // Iterate.
  public TTaskIterator getTrainIterator() {
    return new TTaskIterator(fTrainFeatures, fTrainLabels, fTrainEpochs);
  }

  public TTaskIterator getValidIterator() {
    return new TTaskIterator(fValidFeatures, fValidLabels, fValidEpochs);
  }

  @Override
  public String toString() {
    String str = "Train Data (Size: " + fTrainFeatures.size() + ")\n\n";
    for (int i = 0; i < fTrainFeatures.size(); i++) {
      str += "Train Data No. " + i + "\n";
      str += "Feature:\n";
      str += fTrainFeatures.get(i);
      str += "Label:\n";
      str += fTrainLabels.get(i);
      str += "\n";
    }
    str += "Validation Data (Size: " + fValidFeatures.size() + ")\n";
    for (int i = 0; i < fValidFeatures.size(); i++) {
      str += "Valid Data No. " + i + "\n";
      str += "Feature:\n";
      str += fValidFeatures.get(i);
      str += "Label:\n";
      str += fValidLabels.get(i);
      str += "\n";
    }
    return str;
  }
}
