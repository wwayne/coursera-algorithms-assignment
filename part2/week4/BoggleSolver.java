import edu.princeton.cs.algs4.TrieSET;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver{
  private TrieSET dic;
  private BoggleBoard b;
  private int rowAmount;
  private int colAmount;
  
  // Initializes the data structure using the given array of strings as the dictionary.
  // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
  public BoggleSolver(String[] dictionary) {
    dic = new TrieSET();
    for (String str: dictionary) {
      dic.add(str);
    }
  }

  // Returns the set of all valid words in the given Boggle board, as an Iterable.
  public Iterable<String> getAllValidWords(BoggleBoard board) {
    b = board;
    rowAmount = b.rows();
    colAmount = b.cols();
    TrieSET validWords = new TrieSET();
    boolean[][] isVisited = new boolean[rowAmount][colAmount];
    for (int row = 0; row < rowAmount; row++) {
      for (int col = 0; col < colAmount; col++) {
        searchWord("", row, col, isVisited, dic, validWords);
      }
    }
    return validWords;
  }
  
  private void searchWord(String prefix, int row, int col, boolean[][] isVisited, TrieSET samePrefixWords, TrieSET validWords) {
    if (row < 0 || row >= rowAmount || col < 0 || col >= colAmount || isVisited[row][col]) return;
    String newLetter = "" + b.getLetter(row, col);
    if (newLetter.equals("Q")) newLetter += "U";
    String currentWord = prefix + newLetter;
    isVisited[row][col] = true;
    
    TrieSET newSamePrefixWords = new TrieSET();
    for (String word: samePrefixWords.keysWithPrefix(currentWord)) {
      newSamePrefixWords.add(word);
    }
    if (newSamePrefixWords.isEmpty())  {
      isVisited[row][col] = false;
      return;
    }
    
    if (currentWord.length() >= 3 && newSamePrefixWords.contains(currentWord)) {
      validWords.add(currentWord);
    }
    
    searchWord(currentWord, row + 1, col, isVisited, newSamePrefixWords, validWords);
    searchWord(currentWord, row - 1, col, isVisited, newSamePrefixWords, validWords);
    searchWord(currentWord, row, col + 1, isVisited, newSamePrefixWords, validWords);
    searchWord(currentWord, row, col - 1, isVisited, newSamePrefixWords, validWords);
    searchWord(currentWord, row + 1, col + 1, isVisited, newSamePrefixWords, validWords);
    searchWord(currentWord, row + 1, col - 1, isVisited, newSamePrefixWords, validWords);
    searchWord(currentWord, row - 1, col + 1, isVisited, newSamePrefixWords, validWords);
    searchWord(currentWord, row - 1, col - 1, isVisited, newSamePrefixWords, validWords);
    
    isVisited[row][col] = false;
  }

  // Returns the score of the given word if it is in the dictionary, zero otherwise.
  // (You can assume the word contains only the uppercase letters A through Z.)
  public int scoreOf(String word) {
    if (!dic.contains(word)) return 0;
    int len = word.length();
    if (len <= 2) return 0;
    else if (len <= 4) return 1;
    else if (len == 5) return 2;
    else if (len == 6) return 3;
    else if (len == 7) return 5;
    else return 11;
  }
  
  public static void main(String[] args) {
    In in = new In(args[0]);
    String[] dictionary = in.readAllStrings();
    BoggleSolver solver = new BoggleSolver(dictionary);
    BoggleBoard board = new BoggleBoard(args[1]);
    int score = 0;
    for (String word : solver.getAllValidWords(board)) {
      StdOut.println(word);
      score += solver.scoreOf(word);
    }
    StdOut.println("Score = " + score);
  }
}