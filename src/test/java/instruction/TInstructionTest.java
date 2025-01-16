package instruction;

import utils.TRandomGenerator;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

public class TInstructionTest {
  static private final TRandomGenerator kRand = new TRandomGenerator();

  @Test
  void constructorDefaultTest() {
    TInstruction instruction = new TInstruction();
    assertEquals(TOp.NO_OP, instruction.getOp());
    assertEquals(0, instruction.getIn1());
    assertEquals(0, instruction.getIn2());
    assertEquals(0, instruction.getOut());
    assertEquals(0.0, instruction.getDoubleData1());
    assertEquals(0.0, instruction.getDoubleData2());
    assertEquals(0, instruction.getIndex1());
    assertEquals(0, instruction.getIndex2());
  }

  @Test
  void constructorOpAddressAddress() {
    TInstruction instruction = new TInstruction(TOp.SCALAR_ABS_OP, 10, 20);
    assertEquals(TOp.SCALAR_ABS_OP, instruction.getOp());
    assertEquals(10, instruction.getIn1());
    assertEquals(0, instruction.getIn2());
    assertEquals(20, instruction.getOut());
    assertEquals(0.0, instruction.getDoubleData1());
    assertEquals(0.0, instruction.getDoubleData2());
    assertEquals(0, instruction.getIndex1());
    assertEquals(0, instruction.getIndex2());
  }

  @Test
  void constructorOpAddressAddressAddressTest() {
    TInstruction instruction = new TInstruction(TOp.SCALAR_SUM_OP, 10, 20, 30);
    assertEquals(TOp.SCALAR_SUM_OP, instruction.getOp());
    assertEquals(10, instruction.getIn1());
    assertEquals(20, instruction.getIn2());
    assertEquals(30, instruction.getOut());
    assertEquals(0.0, instruction.getDoubleData1());
    assertEquals(0.0, instruction.getDoubleData2());
    assertEquals(0, instruction.getIndex1());
    assertEquals(0, instruction.getIndex2());
  }

  @Test
  void constructorOpAddressDoubleDataTest() {
    TInstruction instruction =
        new TInstruction(TOp.SCALAR_CONST_SET_OP, 10, 2.2);
    assertEquals(TOp.SCALAR_CONST_SET_OP, instruction.getOp());
    assertEquals(0, instruction.getIn1());
    assertEquals(0, instruction.getIn2());
    assertEquals(10, instruction.getOut());
    assertTrue(
        Math.abs(instruction.getDoubleData1() - 2.2) <= TInstruction.kDoubleDataTolerance);
    assertEquals(0.0, instruction.getDoubleData2());
    assertEquals(0, instruction.getIndex1());
    assertEquals(0, instruction.getIndex2());
  }

  @Test
  void constructorOpAddressDoubleDataDoubleDataTest() {
    TInstruction instruction = new TInstruction(TOp.SCALAR_GAUSSIAN_SET_OP, 10,
        2.2, 3.3);
    assertEquals(TOp.SCALAR_GAUSSIAN_SET_OP, instruction.getOp());
    assertEquals(0, instruction.getIn1());
    assertEquals(0, instruction.getIn2());
    assertEquals(0, instruction.getIn1());
    assertEquals(0, instruction.getIn2());
    assertEquals(10, instruction.getOut());
    assertTrue(
        Math.abs(instruction.getDoubleData1() - 2.2) <= TInstruction.kDoubleDataTolerance);
    assertTrue(
        Math.abs(instruction.getDoubleData2() - 3.3) <= TInstruction.kDoubleDataTolerance);
    assertEquals(0, instruction.getIndex1());
    assertEquals(0, instruction.getIndex2());
  }


  @Test
  void constructorOpAddressDoubleDataDoubleDataIndexTest() {
    TInstruction instruction = new TInstruction(TOp.VECTOR_CONST_SET_OP, 10, 0.5, 3);
    assertEquals(TOp.VECTOR_CONST_SET_OP, instruction.getOp());
    assertEquals(0, instruction.getIn1());
    assertEquals(0, instruction.getIn2());
    assertEquals(10, instruction.getOut());
    assertEquals(0.5, instruction.getDoubleData1());
    assertEquals(0.0, instruction.getDoubleData2());
    assertEquals(3, instruction.getIndex1());
    assertEquals(0, instruction.getIndex2());

  }

  @Test
  void constructorOpAddressDoubleDataDoubleDataIndexIndexTest() {
    TInstruction instruction = new TInstruction(TOp.MATRIX_CONST_SET_OP, 10, 0.5, 3, 2);
    assertEquals(TOp.MATRIX_CONST_SET_OP, instruction.getOp());
    assertEquals(0, instruction.getIn1());
    assertEquals(0, instruction.getIn2());
    assertEquals(10, instruction.getOut());
    assertEquals(0.5, instruction.getDoubleData1());
    assertEquals(0.0, instruction.getDoubleData2());
    assertEquals(3, instruction.getIndex1());
    assertEquals(2, instruction.getIndex2());
  }

  @Test
  void copyConstructor() {
    TOp op = TOp.SCALAR_SUM_OP;
    TInstruction instruction1 = new TInstruction(op, 3, 5, 10);
    TInstruction instruction2 = new TInstruction(instruction1);
    assertTrue(instruction1.equals(instruction2));
  }

  @Test
  void copyFromOtherCorrectly() {
    TOp op = TOp.SCALAR_SUM_OP;
    TInstruction instruction1 = new TInstruction(op, 3, 5, 10);
    TInstruction instruction2 = new TInstruction();
    instruction2.copyFrom(instruction1);
    assertTrue(instruction1.equals(instruction2));
  }

  @Test
  void equalsThroughSomeExamples() {
    TInstruction instruction = new TInstruction(TOp.VECTOR_SUM_OP, 1, 2, 3);
    TInstruction instructionSame = new TInstruction(TOp.VECTOR_SUM_OP, 1, 2, 3);
    TInstruction instructionDiffOp = new TInstruction(TOp.SCALAR_DIFF_OP, 1, 2, 3);
    TInstruction instructionDiffIn1 = new TInstruction(TOp.VECTOR_SUM_OP, 2, 2, 3);
    TInstruction instructionDiffIn2 = new TInstruction(TOp.VECTOR_SUM_OP, 1, 0, 3);
    TInstruction instructionDiffOut = new TInstruction(TOp.VECTOR_SUM_OP, 1, 2, 2);
    assertTrue(instruction.equals(instructionSame));
    assertFalse(instruction.equals(instructionDiffOp));
    assertFalse(instruction.equals(instructionDiffIn1));
    assertFalse(instruction.equals(instructionDiffIn2));
    assertFalse(instruction.equals(instructionDiffOut));
  }

  @Test
  void equalsConsidersOp() {
    TOp[] ops = TOp.values();
    for (TOp op : ops) {
      TInstruction instr1 = new TInstruction(op, 0, 0, 0);
      TInstruction instr2 = new TInstruction(op, 0, 0, 0);
      assertTrue(instr1.equals(instr2));
      assertFalse(!instr1.equals(instr2));
    }

    for (int i = 0; i < ops.length; i++) {
      for (int j = i + 1; j < ops.length; j++) {
        TInstruction instr1 = new TInstruction(ops[i], 0, 0, 0);
        TInstruction instr2 = new TInstruction(ops[j], 0, 0, 0);
        assertFalse(instr1.equals(instr2));
        assertTrue(!instr1.equals(instr2));
      }
    }
  }

  @RepeatedTest(30)
  void equalsConsidersStuffOtherThanOp() {
    for (TOp op : TOp.values()) {
      TInstruction instr = new TInstruction();
      instr.setOp(op);
      TInstruction sameInstr = new TInstruction(instr);
      assertTrue(instr.equals(sameInstr));
      assertFalse(!instr.equals(sameInstr));
      TInstruction otherInstr = new TInstruction(instr);
      switch (kRand.nextInt(0, 7)) {
        case 0: {
          otherInstr.setIn1(kRand.nextInt());
          if (otherInstr.getIn1() == instr.getIn1()) {
            assertTrue(instr.equals(otherInstr));
          } else {
            assertFalse(instr.equals(otherInstr));
          }
          break;
        }
        case 1: {
          otherInstr.setIn2(kRand.nextInt());
          if (otherInstr.getIn2() == instr.getIn2()) {
            assertTrue(instr.equals(otherInstr));
          } else {
            assertFalse(instr.equals(otherInstr));
          }
          break;
        }
        case 2: {
          otherInstr.setOut(kRand.nextInt());
          if (otherInstr.getOut() == instr.getOut()) {
            assertTrue(instr.equals(otherInstr));
          } else {
            assertFalse(instr.equals(otherInstr));
          }
          break;
        }
        case 3: {
          otherInstr.setDoubleData1(kRand.nextDouble());
          if (otherInstr.getDoubleData1() == instr.getDoubleData1()) {
            assertTrue(instr.equals(otherInstr));
          } else {
            assertFalse(instr.equals(otherInstr));
          }
          break;
        }
        case 4: {
          otherInstr.setDoubleData2(kRand.nextFloat());
          if (otherInstr.getDoubleData2() == instr.getDoubleData2()) {
            assertTrue(instr.equals(otherInstr));
          } else {
            assertFalse(instr.equals(otherInstr));
          }
          break;
        }
        case 5: {
          otherInstr.setIndex1(kRand.nextInt());
          if (otherInstr.getIndex1() == instr.getIndex1()) {
            assertTrue(instr.equals(otherInstr));
          } else {
            assertFalse(instr.equals(otherInstr));
          }
          break;
        }
        case 6: {
          otherInstr.setIndex2(kRand.nextInt());
          if (otherInstr.getIndex2() == instr.getIndex2()) {
            assertTrue(instr.equals(otherInstr));
          } else {
            assertFalse(instr.equals(otherInstr));
          }
          break;
        }
      }
    }
  }
}
