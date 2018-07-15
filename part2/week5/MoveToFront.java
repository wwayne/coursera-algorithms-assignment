import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
  private static final int R = 256; 
  
  // apply move-to-front encoding, reading from standard input and writing to standard output
  public static void encode() {
    char[] charArray = genCharArray();
    char targetChar;
    char tmpCharOut;
    char tmpCharIn;
    char r;
    while(!BinaryStdIn.isEmpty()) {
      targetChar = BinaryStdIn.readChar();
      tmpCharOut = charArray[0];
      for (r = 0; charArray[r] != targetChar; r++) {
        tmpCharIn = charArray[r];
        charArray[r] = tmpCharOut;
        tmpCharOut = tmpCharIn;
      }
      charArray[r] = tmpCharOut;
      charArray[0] = targetChar;
      BinaryStdOut.write(r);
    }
    BinaryStdOut.close();
  }

  // apply move-to-front decoding, reading from standard input and writing to standard output
  public static void decode() {
    char[] charArray = genCharArray();
    while(!BinaryStdIn.isEmpty()) {
      int targetCharIndex = BinaryStdIn.readInt(8);
      char targetChar = charArray[targetCharIndex];
      for (int i = targetCharIndex; i > 0; i--)
        charArray[i] = charArray[i - 1];
      charArray[0] = targetChar;
      BinaryStdOut.write(targetChar);
    }
    BinaryStdOut.close();
  }
  
  private static char[] genCharArray() {
    char[] charArray = new char[R];
    for (char i = 0; i < R; i++)
      charArray[i] = i;
    return charArray;
  }

  // if args[0] is '-', apply move-to-front encoding
  // if args[0] is '+', apply move-to-front decoding
  public static void main(String[] args) {
    String sign = args[0];
    if (sign.equals("-")) encode();
    else if (sign.equals("+")) decode();
  }
}