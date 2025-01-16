package memory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class TMemoryTest {
  @Test
  void dimensionControlTest() {
    TMemory memory = new TMemory();
    assertEquals(-1, memory.getDim());

    memory.initialize(3);
    assertEquals(3, memory.getDim());
    assertEquals(3, memory.vector[0].getRowDimension());
    assertEquals(3, memory.matrix[0].getRowDimension());
    assertEquals(3, memory.matrix[0].getColumnDimension());

    memory.initialize(5);
    assertEquals(5, memory.getDim());
    assertEquals(5, memory.vector[0].getRowDimension());
    assertEquals(5, memory.matrix[0].getRowDimension());
    assertEquals(5, memory.matrix[0].getColumnDimension());
  }

  @Test
  void wipeTest() {
    TMemory memory = new TMemory();
    memory.initialize(3);
    memory.scalar[0] = 1.0;
    memory.vector[1].setValue(0, 1.0);
    memory.matrix[2].setValue(1, 1, 1.0);
    memory.wipe();

    assertEquals(0.0, memory.scalar[0]);
    assertEquals(0.0, memory.vector[1].getValue(0));
    assertEquals(0.0, memory.matrix[2].getValue(1, 1));
  }

  @Test
  void numOfAddressesControl() {
    TMemory memory = new TMemory(1, 3, 6);
    assertEquals(1, memory.getNumOfScalarAddresses());
    assertEquals(3, memory.getNumOfVectorAddresses());
    assertEquals(6, memory.getNumOfMatrixAddresses());
  }
}
