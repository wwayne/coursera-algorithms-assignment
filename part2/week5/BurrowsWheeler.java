import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
  private static final int R = 256; 
  
  // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
  public static void transform() {
    String s = BinaryStdIn.readString();
    CircularSuffixArray csa = new CircularSuffixArray(s);
      
    int len = s.length();
    int targetIndex = 0;
    for (int i = 0; i < len; i++) {
      if (csa.index(i) == 0) {
        BinaryStdOut.write(i);
        break;
      }
    }
    for (int i = 0; i < len; i++) {
      int indexInOriginalString = csa.index(i) - 1;
      if (indexInOriginalString == -1) indexInOriginalString = len - 1;
      BinaryStdOut.write(s.charAt(indexInOriginalString));
    }
    BinaryStdOut.close();
  }

  // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
  public static void inverseTransform() { 
    int first = BinaryStdIn.readInt();
    String s = BinaryStdIn.readString();
    int len = s.length();
    int[] count = new int[R + 1];
    int[] next = new int[len];
    char[] sorted = new char[len];
    
    for (int i = 0; i < len; i++)
      count[s.charAt(i) + 1]++;
    for (int r = 0; r < R; r++)
      count[r + 1] += count[r];
    for (int i = 0; i < len; i++) {
      next[count[s.charAt(i)]] = i;
      sorted[count[s.charAt(i)]++] = s.charAt(i);
    }
    for (int i = 0, j = first; i < len; i++) {
      BinaryStdOut.write(sorted[j]);
      j = next[j];
    }
    BinaryStdOut.close();
  }

  // if args[0] is '-', apply Burrows-Wheeler transform
  // if args[0] is '+', apply Burrows-Wheeler inverse transform
  public static void main(String[] args) {
    if (args[0].equals("-")) transform();
    else if (args[0].equals("+")) inverseTransform();
  }
}
