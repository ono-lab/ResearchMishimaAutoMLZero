package task.core;

import java.util.ArrayList;
import jp.ac.titech.onolab.core.matrix.TCMatrix;

public class TTaskBuffer {
  private int fFeatureSize;
  private TEvalMethod fEvalMethod;
  private boolean fIsConsumed = false;
  private ArrayList<TCMatrix> fTrainFeatures = new ArrayList<TCMatrix>();
  private ArrayList<TCMatrix> fValidFeatures = new ArrayList<TCMatrix>();
  private ArrayList<Double> fTrainLabels = new ArrayList<Double>();
  private ArrayList<Double> fValidLabels = new ArrayList<Double>();

  public TTaskBuffer(int featureSize) {
    fFeatureSize = featureSize;
  }

  public TEvalMethod getEvalMethod() {
    return fEvalMethod;
  }

  public void setEvalMethod(TEvalMethod evalMethod) {
    fEvalMethod = evalMethod;
  }

  public void clear() {
    fTrainFeatures.clear();
    fValidFeatures.clear();
    fTrainLabels.clear();
    fValidLabels.clear();
    fIsConsumed = false;
  }

  public boolean isConsumed() {
    return fIsConsumed;
  }

  public void verifyFeatureShape(TCMatrix feature) {
    assert feature.getRowDimension() == fFeatureSize;
    assert feature.getColumnDimension() == 1;
  }

  public void addTrainData(TCMatrix feature, Double label) {
    verifyFeatureShape(feature);
    fTrainFeatures.add(feature);
    fTrainLabels.add(label);
  }

  public void addTrainData(TCMatrix feature) {
    addTrainData(feature, Double.NaN);
  }

  public void setTrainLabel(int index, double label) {
    fTrainLabels.set(index, label);
  }

  public int getTrainDataSize() {
    return fTrainFeatures.size();
  }

  public ArrayList<TCMatrix> getTrainFeatures() {
    return fTrainFeatures;
  }

  public ArrayList<Double> getTrainLabels() {
    return fTrainLabels;
  }

  public void addValidData(TCMatrix feature, Double label) {
    verifyFeatureShape(feature);
    fValidFeatures.add(feature);
    fValidLabels.add(label);
  }

  public void addValidData(TCMatrix feature) {
    addValidData(feature, Double.NaN);
  }

  public void setValidLabel(int index, double label) {
    fValidLabels.set(index, label);
  }

  public int getValidDataSize() {
    return fValidFeatures.size();
  }

  public int getFeatureSize() {
    return fFeatureSize;
  }

  public ArrayList<TCMatrix> getValidFeatures() {
    return fValidFeatures;
  }

  public ArrayList<Double> getValidLabels() {
    return fValidLabels;
  }

  public void consume() {
    fIsConsumed = true;
  }
}
