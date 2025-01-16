package utils;

import java.util.Map;
import java.util.LinkedHashMap;

/**
 * LRUキャッシュ
 */
public class TLRUCache<K, V> extends LinkedHashMap<K, V> {
  private final int maxSize;

  /**
   * 最大数maxSizeのLRU Cacheを作成
   */
  public TLRUCache(int maxSize) {
    super(15, 0.75f, true); // 第一引数と第二引数はデフォルト値
    assert maxSize > 1;
    this.maxSize = maxSize;
  }

  /** エントリの削除要否を判断 */
  @Override
  protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
    return size() > maxSize;
  }
}
