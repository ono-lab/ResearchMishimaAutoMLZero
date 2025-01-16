package utils;

import jp.ac.titech.onolab.core.matrix.TCMatrix;
import org.apache.commons.math3.random.JDKRandomGenerator;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.math3.distribution.BetaDistribution;

/**
 * java.util.Random（JDKRandomGenerator）を拡張した乱数生成器．
 */
public class TRandomGenerator extends JDKRandomGenerator {
  /**
   * seedを時刻からランダムに作る関数
   */
  static public long getRandomSeed() {
    long seed = 0;
    while (seed == 0) {
      seed = System.nanoTime() % Long.MAX_VALUE;
    }
    return seed;
  }

  /**
   * seedを時刻からランダムに作りセットする関数
   */
  public void setRandomSeed() {
    long seed = getRandomSeed();
    setSeed(seed);
  }

  /**
   * 乱数生成器を生成するコンストラクタ
   */
  public TRandomGenerator() {
    setRandomSeed();
  };

  /**
   * 指定されたseedで乱数生成器を生成するコンストラクタ
   */
  @JsonCreator
  public TRandomGenerator(@JsonProperty("seed") long seed) {
    setSeed(seed);
  }

  /**
   * 区間[low, high)内の整数を一様ランダムに選択して返却する関数
   */
  public int nextInt(final int low, final int high) {
    return nextInt(high - low) + low;
  };

  /**
   * 区間[low, high)内の少数(double)を一様ランダムに選択して返却する関数
   */
  public double nextDouble(final double low, final double high) {
    return nextDouble() * (high - low) + low;
  };

  /**
   * 区間[low, high)内の少数(float)を一様ランダムに選択して返却する関数
   */
  public float nextFloat(final float low, final float high) {
    return nextFloat() * (high - low) + low;
  };

  /**
   * 0から1の少数(double)を一様に選択して返却する関数
   */
  public double nextProbability() {
    return nextDouble(0.0, 1.0);
  };

  /**
   * 平均mean，標準偏差stdevの正規分布に従う乱数を返却する関数
   */
  public double nextGaussian(final double mean, final double stdev) {
    return nextGaussian() * stdev + mean;
  };

  /**
   * ベータ分布B(alpha, beta)に従う乱数を返却する関数
   */
  public double nextBeta(final double alpha, final double beta) {
    return new BetaDistribution(this, alpha, beta).sample();
  }

  /**
   * 与えられた配列の確率に基づいてindexを返却する関数
   */
  public int nextIndex(final double[] probabilities) {
    double random = nextDouble(0.0, 1.0);
    double sum = 0.0;
    for (int i = 0; i < probabilities.length; i++) {
      sum += probabilities[i];
      if (random < sum) {
        return i;
      }
    }
    return probabilities.length - 1;
  }

  /**
   * size個の文字を一様独立でランダムに選択して，それらを繋げて文字列として返却する関数．候補は'a'-'z', 'A'-'Z', '0'-'9', '_' および '~'である．
   */
  public String nextString(final long size) {
    String randomString = "";
    for (int i = 0; i < size; i++) {
      char randomChar;
      int charIndex = nextInt(0, 64);
      if (charIndex < 26) {
        randomChar = (char) (charIndex + 97); // Maps 0-25 to 'a'-'z'.
      } else if (charIndex < 52) {
        randomChar = (char) (charIndex - 26 + 65); // Maps 26-51 to 'A'-'Z'.
      } else if (charIndex < 62) {
        randomChar = (char) (charIndex - 52 + 48); // Maps 52-61 to '0'-'9'.
      } else if (charIndex == 62) {
        randomChar = '_';
      } else if (charIndex == 63) {
        randomChar = '~';
      } else {
        throw new RuntimeException("Code should not get here.");
      }
      randomString += randomChar;
    }
    return randomString;
  };

  /**
   * 2択(0 or 1)を一様ランダムに選んで返却する関数
   */
  public int nextChoice2() {
    return nextInt(0, 2);
  };

  /**
   * 3択(0, 1 or 2)を一様ランダムに選んで返却する関数
   */
  public int nextChoice3() {
    return nextInt(0, 3);
  };

  /**
   * 与えられたmatrixの各要素に[low, high)内の少数(double)を一様ランダムに選択して代入する関数
   */
  public void fillDouble(final double low, final double high, final TCMatrix matrix) {
    for (int i = 0; i < matrix.getDimension(); i++) {
      matrix.setValue(i, nextDouble(low, high));
    }
  }

  /**
   * 与えられたmatrixの各要素に平均mean，分散stdevの正規乱数を代入する関数
   */
  public void fillGaussian(final double mean, final double stdev, final TCMatrix matrix) {
    for (int i = 0; i < matrix.getDimension(); i++) {
      matrix.setValue(i, nextGaussian(mean, stdev));
    }
  }

  /**
   * 与えられたmatrixの各要素にベータ分布B(alpha, beta)で生成された乱数を代入する関数
   */
  public void fillBeta(final double alpha, final double beta, final TCMatrix matrix) {
    for (int i = 0; i < matrix.getDimension(); i++) {
      matrix.setValue(i, nextBeta(alpha, beta));
    }
  }

  /**
   * {0, 1, , ..., size - 1}をランダムに並び替えた配列を返却する関数
   */
  public int[] nextUniqueIntegerArray(final int size) {
    final int[] result = new int[size];
    final ArrayList<Integer> candidates =
        new ArrayList<Integer>(Arrays.asList(TArrayUtility.range(size)));
    for (int i = 0; i < size; i++) {
      int random = nextInt(size - i);
      result[i] = candidates.remove(random);
    }
    assert candidates.isEmpty();
    return result;
  }
}
