package utils;

import java.util.Collection;
import java.util.Objects;

/**
 * 汎用的に使えるJavaのUtilityをまとめているクラス
 */
public class TArrayUtility {
  /**
   * {0, 1, ... , end - 1}というint型の配列を返却する．
   */
  public static Integer[] range(final int end) {
    return range(0, end);
  }

  /**
   * {start, start + 1, ... , end - 1}というint型の配列を返却する．
   */
  public static Integer[] range(final int start, final int end) {
    assert start < end;
    Integer[] range = new Integer[end - start];
    for (int i = 0; i < end - start; i++) {
      range[i] = start + i;
    }
    return range;
  }

  public static <T extends Object> T find(T[] array, T searchElement) {
    for (T element : array) {
      if (Objects.equals(element, searchElement)) {
        return element;
      }
    }
    return null;
  }

  public static <T extends Object> T find(Collection<T> list, T searchElement) {
    for (T element : list) {
      if (Objects.equals(element, searchElement)) {
        return element;
      }
    }
    return null;
  }

  public static <T extends Object> boolean include(Collection<T> list, T searchElement) {
    for (T element : list) {
      if (Objects.equals(element, searchElement)) {
        return true;
      }
    }
    return false;
  }
}
