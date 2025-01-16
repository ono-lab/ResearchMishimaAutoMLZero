package utils;

import java.util.Arrays;
import jp.ac.titech.onolab.core.matrix.TCMatrix;

/**
 * 汎用的に使える数学的処理をまとめているクラス
 */
public class TMathUtility {

  /**
   * matの各要素の合計値を返却する関数
   */
  public static double sum(final TCMatrix mat) {
    double sum = 0.0;
    for (int i = 0; i < mat.getDimension(); i++) {
      sum += mat.getValue(i);
    }
    return sum;
  }

  /**
   * matの各要素の平均値を返却する関数
   */
  public static double mean(final TCMatrix mat) {
    return sum(mat) / mat.getDimension();
  }

  /**
   * matの各要素の分散を返却する関数
   */
  public static double variance(final TCMatrix mat) {
    final double mean = mean(mat);
    double sum = 0.0;
    for (int i = 0; i < mat.getDimension(); i++) {
      sum += Math.pow(mat.getValue(i) - mean, 2);
    }
    return sum / mat.getDimension();
  }

  /**
   * matの各要素の標準偏差を返却する関数
   */
  public static double stdev(final TCMatrix mat) {
    return Math.sqrt(variance(mat));
  }

  /**
   * xにsigmoid関数を適用した結果を返却する関数
   */
  public static double sigmoid(final double x) {
    return 1.0 / (1.0 + Math.exp(-x));
  }

  /**
   * xにsquash関数を適用した結果を返却する関数
   */
  public static double squash(final double x) {
    return 2 / Math.PI * Math.atan(x);
  }

  /**
   * 2のexp乗を返却する関数
   */
  public static int pow2(final int exp) {
    return 1 << exp;
  }

  /**
   * 与えられた配列の平均値を返却する関数
   */
  public static double mean(final double[] values) {
    if (values.length == 0) {
      return Double.NaN;
    }
    double sum = 0.0;
    for (double value : values) {
      sum += value;
    }
    return sum / (double) (values.length);
  }

  /**
   * 与えられた配列の中央値を返却する関数
   */
  public static double median(final double[] values) {
    if (values.length == 0) {
      return Double.NaN;
    }
    Arrays.sort(values);
    final int center = values.length / 2;
    if (values.length % 2 == 0) {
      return (values[center] + values[center - 1]) / 2.0;
    } else {
      return values[center];
    }
  }
}
