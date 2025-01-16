package fun.xiaorang.study.java.core.nestedclass;

/**
 * @author xiaorang
 * @description <p style = " font-weight:bold ; "><p/>
 * @github <a href="https://github.com/xihuanxiaorang/java-study">java-study</a>
 * @Copyright 博客：<a href="https://docs.xiaorang.fun">小让的糖果屋</a>  - show me the code
 * @date 2025/01/15 22:51
 */
public class DataStructure {
  // Create an array
  private final static int SIZE = 15;
  private final int[] arrayOfInts = new int[SIZE];

  public DataStructure() {
    // fill the array with ascending integer values
    for (int i = 0; i < SIZE; i++) {
      arrayOfInts[i] = i;
    }
  }

  public void printEven() {
    // Print out values of even indices of the array
    DataStructureIterator iterator = this.new EvenIterator();
    while (iterator.hasNext()) {
      System.out.print(iterator.next() + " ");
    }
    System.out.println();
  }

  public static void main(String[] s) {
    // Fill the array with integer values and print out only
    // values of even indices
    DataStructure ds = new DataStructure();
    ds.printEven();
  }

  // Inner class implements the DataStructureIterator interface,
  // which extends the Iterator<Integer> interface
  interface DataStructureIterator extends java.util.Iterator<Integer> {
  }

  private class EvenIterator implements DataStructureIterator {
    // Start stepping through the array from the beginning
    private int nextIndex = 0;

    @Override
    public boolean hasNext() {
      // Check if the current element is the last in the array
      return (nextIndex <= SIZE - 1);
    }

    @Override
    public Integer next() {
      // Record a value of an even index of the array
      Integer retValue = arrayOfInts[nextIndex];
      // Get the next even element
      nextIndex += 2;
      return retValue;
    }
  }
}
