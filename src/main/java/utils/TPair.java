package utils;

import java.util.Objects;

/**
 * 2つの値の対を管理する用のクラス
 */
public class TPair<T extends Object, U extends Object> {
  public T first;
  public U second;

  /**
   * 2つの値(first, second)を生成
   */
  public TPair(final T first, final U second) {
    this.first = first;
    this.second = second;
  }

  /**
   * 1つの目の値と2つめの値が同じ時にtrueを返却する関数．ただし，比較は==だけではあく，equalsでも行われる．
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof TPair)) {
      return true;
    }
    final TPair<Object, Object> otherPair = (TPair<Object, Object>) other;
    return Objects.equals(this.first, otherPair.first)
        && Objects.equals(this.second, otherPair.second);
  }

  /**
   * this.firstが第一引数とthis.secondが第二引数と同じ時にtrueを返却する関数．ただし，比較は==だけではあく，equalsでも行われる．
   */
  public boolean equals(final T otherFirst, final U otherSecond) {
    return Objects.equals(this.first, otherFirst) && Objects.equals(this.second, otherSecond);
  }
}
