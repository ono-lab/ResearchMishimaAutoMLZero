package evaluator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class TFECCacheTest {
  static private final int kCacheSize = 10;
  static private final int kNumOfTrainExamples = 10;
  static private final int kNumOfValidExamples = 10;

  static private TFECCacheSpec makeSpec(int forgetEvery) {
    return new TFECCacheSpec(kNumOfTrainExamples, kNumOfValidExamples, kCacheSize, forgetEvery);
  }

  @Test
  void numOfTrainExamplesWorks() {
    TFECCache cache = new TFECCache(makeSpec(0));
    assertEquals(cache.getNumOfTrainExamples(), kNumOfTrainExamples);
  }

  @Test
  void numOfValidExamplesWorks() {
    TFECCache cache = new TFECCache(makeSpec(0));
    assertEquals(cache.getNumOfValidExamples(), kNumOfValidExamples);
  }

  @Test
  void getCachedFitnessReturnsFitnessCorrectly() {
    TFECCache cache = new TFECCache(makeSpec(0));
    cache.put(100L, 0.1);
    cache.put(200L, 0.2);
    assertEquals(0.1, cache.getCachedFitness(100L));
    assertEquals(0.2, cache.getCachedFitness(200L));
  }

  @Test
  void getCachedFitnessReturnsNaNIfNotFound() {
    TFECCache cache = new TFECCache(makeSpec(0));
    cache.put(100L, 0.1);
    cache.put(200L, 0.2);
    assertEquals(Double.NaN, cache.getCachedFitness(300L));
  }

  @Test
  void forgetCorrectly() {
    TFECCache cache = new TFECCache(makeSpec(3));
    cache.put(100L, 0.1);
    cache.put(200L, 0.2);
    cache.put(300L, 0.3);
    assertEquals(0.1, cache.getCachedFitness(100L));
    assertEquals(0.1, cache.getCachedFitness(100L));
    assertEquals(0.1, cache.getCachedFitness(100L));
    assertEquals(Double.NaN, cache.getCachedFitness(100L));
  }

  @Test
  void clearWorksCorrectly() {
    TFECCache cache = new TFECCache(makeSpec(0));
    cache.put(100L, 0.1);
    cache.put(200L, 0.2);
    cache.clear();
    assertEquals(Double.NaN, cache.getCachedFitness(100L));
    assertEquals(Double.NaN, cache.getCachedFitness(200L));
  }
}
