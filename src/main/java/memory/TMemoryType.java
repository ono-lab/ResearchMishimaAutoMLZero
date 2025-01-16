package memory;

import java.util.ArrayList;

/**
 * メモリに格納される値の型
 */
public enum TMemoryType {
  MATRIX(), VECTOR(), SCALAR();

  /**
   * メモリに格納される値の種類をArrayListで取得する関数
   */
  public static ArrayList<TMemoryType> getAllArrayList(){
    ArrayList<TMemoryType> allMemoryTypes = new ArrayList<TMemoryType>();
    for(TMemoryType memoryType : values()){
      allMemoryTypes.add(memoryType);
    }
    return allMemoryTypes;
  }
}
