package task.core;

import java.util.ArrayList;
import jp.ac.titech.onolab.core.matrix.TCMatrix;

public class TTaskIterator {
  private ArrayList<TCMatrix> fFeatures;
  private ArrayList<Double> fLabels;
  private int[][] fEpochs;
  private int fCurrentExample = 0;
  private int fCurrentEpoch = 0;

  public TTaskIterator(ArrayList<TCMatrix> features, ArrayList<Double> labels, int[][] epochs) {
    fFeatures = features;
    fLabels = labels;
    fEpochs = epochs;
  }

  public boolean hasDone() {
    return fCurrentEpoch >= fEpochs.length;
  }

  public void next() {
    assert fCurrentEpoch <= fEpochs.length;
    fCurrentExample++;
    if (fCurrentExample >= fFeatures.size()) {
      fCurrentEpoch++;
      fCurrentExample = 0;
    }
  }

  public TCMatrix getFeature() {
    return fFeatures.get(fEpochs[fCurrentEpoch][fCurrentExample]);
  }

  public double getLabel() {
    return fLabels.get(fEpochs[fCurrentEpoch][fCurrentExample]);
  }
}
