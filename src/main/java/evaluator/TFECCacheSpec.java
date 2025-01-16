package evaluator;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class TFECCacheSpec {
  /**
   * Number of examples to train a model in order to evaluate cache hashes. Required to be smaller
   * than or equal to the number of training examples. However, for the cache to provide an
   * advantage, should be kept much smaller than the actual training examples.
   */
  public int numOfTrainExamples;

  /**
   * Number of examples to validate a model in order to evaluate cache hashes. Required to be
   * smaller than or equal to the number of validation examples. However, for the cache to provide
   * an advantage, should be kept much smaller than the actual validation examples.
   */
  public int numOfValidExamples;

  /** Number of values to keep in the cache. */
  public int cacheSize = 100000;

  /**
   * If a hash is seen this many times, it will be forcibly removed from the cache immediately. If
   * set to 0, hashes are never forcibly removed (but will still be removed due to LRU policy)
   */
  public int forgetEvery = 0;

  public TFECCacheSpec() {
  }

  public TFECCacheSpec(int numOfTrainExamples, int numOfValidExamples, int cacheSize, int forgetEvery) {
    this.numOfTrainExamples = numOfTrainExamples;
    this.numOfValidExamples = numOfValidExamples;
    this.cacheSize = cacheSize;
    this.forgetEvery = forgetEvery;
  }
}
