import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
  private static final int SizeForInsertionSort = 30;
  private static final int R = 256;
  private int[] sortedSuffixIndex;
  
  public CircularSuffixArray(String s) {
    if (s == null) throw new IllegalArgumentException();
    int len = s.length();
    sortedSuffixIndex = new int[len];
    for (int i = 0; i < len; i++)
      sortedSuffixIndex[i] = i;
    
    sort(0, len - 1, 0, s);
  }
  
//  MSD implementation, stackoverflow and have problem for hex input...
//
//  private void sort(int lo, int hi, int d, int[] aux, String s) {
//    if (hi - lo <= SizeForInsertionSort) {
//      insertionSort(lo, hi, s);
//      return;
//    }
//
//    int[] count = new int[R + 1]; // no need for saving shorter string
//    
//    for (int i = lo; i <= hi; i++) {
//      int targetChar = charAt(sortedSuffixIndex[i], d, s);
//      count[targetChar + 1]++;
//    }
//
//    for (int r = 0; r < R; r++)
//      count[r + 1] += count[r];
//
//    for (int i = lo; i <= hi; i++) {
//      int targetChar = charAt(sortedSuffixIndex[i], d, s);
//      aux[count[targetChar]++] = sortedSuffixIndex[i];
//    }
//    
//    for (int i = lo; i <= hi; i++)
//      sortedSuffixIndex[i] = aux[i - lo];
//
//    for (int r = 0; r < R; r++)
//      sort(lo + count[r], lo + count[r + 1] - 1, d + 1, aux, s);
//  }
  
//  3-way quicksort implementaion, reported as stackoverflow...
  private void sort(int lo, int hi, int d, String s) {
    if (hi - lo <= SizeForInsertionSort) {
      insertionSort(lo, hi, s);
      return;
    }
    
    int tmpLo = lo;
    int tmpHi = hi;
    int tmpEq = lo + 1;
    int valueForCompare = charAt(sortedSuffixIndex[lo], d, s);
    while (tmpEq <= tmpHi) {
      int currentValue = charAt(sortedSuffixIndex[tmpEq], d, s);
      int diff = currentValue - valueForCompare;
      if (diff < 0) exchange(tmpEq++, tmpLo++);
      else if (diff > 0) exchange(tmpEq, tmpHi--);
      else tmpEq++;
    }
    
    sort(lo, tmpLo - 1, d, s);
    sort(tmpLo, tmpHi, d + 1, s);
    sort(tmpHi + 1, hi, d, s);
  }
  
  private int charAt(int index, int d, String s) {
    int nexIndexInString = (index + d) % s.length();
    return s.charAt(nexIndexInString);
  }
  
  private void exchange(int i, int j) {
    int tmp = sortedSuffixIndex[i];
    sortedSuffixIndex[i] = sortedSuffixIndex[j];
    sortedSuffixIndex[j] = tmp;
  }
  
  private void insertionSort (int lo, int hi, String s) {
    for (int i = lo; i <= hi; i++)
      for (int j = i; j > lo; j--) {
        if (less(j, j - 1, s)) exchange(j, j - 1);
      }
  }
  
  // if ith sorted suffix < jth sorted suffix
  private boolean less(int i, int j, String s) {
    int indexI = sortedSuffixIndex[i];
    int indexJ = sortedSuffixIndex[j];
    for (int d = 0; d < s.length(); d++) {
      int diff = charAt(indexJ, d, s) - charAt(indexI, d, s);
      if (diff > 0) return true;
      else if (diff < 0) return false;
    }
    return false;
  }
  
  public int length() {
    return sortedSuffixIndex.length;
  }
  
  public int index(int i) {
    if (i < 0 || i >= sortedSuffixIndex.length) throw new IllegalArgumentException();
    return sortedSuffixIndex[i];
  }
  
  public static void main(String[] args) {
    String s = "ABRACADABRA!";
    CircularSuffixArray csa = new CircularSuffixArray(s);
    StdOut.println("length = " + csa.length());
    for (int i = 0; i < csa.length(); i++) {
      StdOut.println("index: " + i + " output: " + csa.index(i));
    }
  }
}