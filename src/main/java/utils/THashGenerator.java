package utils;

import java.util.ArrayList;

/**
 * オブジェクトのハッシュを生成する用のクラス
 */
public class THashGenerator {

  /**
   * seedを使ってvalueをハッシュ化する関数． 与えるseedとvalueが同じ場合は常に同じハッシュを返す．
   * 参考：https://burtleburtle.net/bob/hash/evahash.html.
   */
  static public long getCombinedHash(long seed, Object value) {
    long a = 0x9e3779b9;
    long b = seed;
    long c = value.hashCode();
    a = a - b;
    a = a - c;
    a = a ^ (c >> 43);
    b = b - c;
    b = b - a;
    b = b ^ (a << 9);
    c = c - a;
    c = c - b;
    c = c ^ (b >> 8);
    a = a - b;
    a = a - c;
    a = a ^ (c >> 38);
    b = b - c;
    b = b - a;
    b = b ^ (a << 23);
    c = c - a;
    c = c - b;
    c = c ^ (b >> 5);
    a = a - b;
    a = a - c;
    a = a ^ (c >> 35);
    b = b - c;
    b = b - a;
    b = b ^ (a << 49);
    c = c - a;
    c = c - b;
    c = c ^ (b >> 11);
    a = a - b;
    a = a - c;
    a = a ^ (c >> 12);
    b = b - c;
    b = b - a;
    b = b ^ (a << 18);
    c = c - a;
    c = c - b;
    c = c ^ (b >> 22);
    seed = c;
    return seed;
  }

  /**
   * オブジェクトの配列を統合したハッシュ値を返却する関数．
   */
  static public long getMixedHash(Object[] objects) {
    long seed = 42;
    for (Object object : objects) {
      seed = getCombinedHash(seed, object);
    }
    return seed;
  }

  /**
   * ２つのオブジェクトを統合したハッシュ値を返却する関数．
   */
  static public long getMixedHash(Object first, Object second) {
    return getMixedHash(new Object[] {first, second});
  }

  /**
   * 学習および検証の結果からハッシュを作成して返却する関数．
   */
  public static long getWellMixedHash(ArrayList<Double> trainErrors, ArrayList<Double> validErrors,
      int datasetIndex, int numTrainExamples) {
    long seed = 42;
    for (Double trainError : trainErrors) {
      seed = getCombinedHash(seed, trainError);
    }
    for (Double validError : validErrors) {
      seed = getCombinedHash(seed, validError);
    }
    seed = getCombinedHash(seed, datasetIndex);
    seed = getCombinedHash(seed, numTrainExamples);
    return seed;
  }

  /**
   * 文字列の内容からハッシュを作成して返却する関数．
   * 各文字を処理してより衝突を回避する設計．
   */
  public static long getStringHash(String input) {
    if (input == null) {
      return 0;
    }

    long seed = 42; // 初期値
    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);

      // 文字位置による重み付け
      seed = getCombinedHash(seed, c + i * 31);
    }
    return seed;
  }
}
