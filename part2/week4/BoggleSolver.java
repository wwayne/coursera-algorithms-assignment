import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Set;
import java.util.TreeSet;

public class BoggleSolver{
  private final int R = 26;
  private Trie dic;
  private BoggleBoard b;
  private int rowAmount;
  private int colAmount;
  
  // Initializes the data structure using the given array of strings as the dictionary.
  // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
  public BoggleSolver(String[] dictionary) {
    dic = new Trie();
    for (String str: dictionary) {
      dic.add(str);
    }
  }
  
  private class Node {
    private Node[] next = new Node[R];
    private boolean isLast = false;
  }
  
  private class Trie {
    private Node root = new Node();
    
    public Node root() {
      return root;
    }
    
    // add new word
    public void add(String word) {
      root = addWord(root, word, 0);
    }
    
    private Node addWord(Node n, String word, int index) {
      if (n == null) n = new Node();
      if (index == word.length()) {
        n.isLast = true;
        return n;
      }
      
      char currentChar = word.charAt(index);
      n.next[currentChar - 'A'] = addWord(n.next[currentChar - 'A'], word, index + 1);
      return n;
    }
    
    // if the word contains
    public boolean contains(String word) {
      return isCharContained(root, word, 0);
    }
    
    private boolean isCharContained(Node n, String word, int index) {
      if (n == null) return false;
      if (index == word.length()) {
        if (n.isLast) return true;
        return false;
      }
      char currentChar = word.charAt(index);
      Node nextNode = n.next[currentChar - 'A'];
      return isCharContained(nextNode, word, index + 1);
    }
  }

  // Returns the set of all valid words in the given Boggle board, as an Iterable.
  public Iterable<String> getAllValidWords(BoggleBoard board) {
    b = board;
    rowAmount = b.rows();
    colAmount = b.cols();
    Set<String> validWords = new TreeSet<String>();
    boolean[][] isVisited = new boolean[rowAmount][colAmount];
    for (int row = 0; row < rowAmount; row++) {
      for (int col = 0; col < colAmount; col++) {
        searchWord("", row, col, isVisited, validWords, dic.root());
      }
    }
    return validWords;
  }
  
  private void searchWord(String prefix, int row, int col, boolean[][] isVisited, Set<String> validWords, Node node) {
    if (row < 0 || row >= rowAmount || col < 0 || col >= colAmount || isVisited[row][col] || node == null) return;
    char newChar = b.getLetter(row, col);
    String currentWord = prefix + newChar;
    if (newChar == 'Q') currentWord += "U";

    if (currentWord.length() >= 3 && dic.contains(currentWord)) {
      validWords.add(currentWord);
    }
    
    isVisited[row][col] = true;
    Node newNode = node.next[newChar - 'A'];
    if (newChar == 'Q' && newNode != null) newNode = newNode.next['U' - 'A'];
    
    searchWord(currentWord, row + 1, col, isVisited, validWords, newNode);
    searchWord(currentWord, row - 1, col, isVisited, validWords, newNode);
    searchWord(currentWord, row, col + 1, isVisited, validWords, newNode);
    searchWord(currentWord, row, col - 1, isVisited, validWords, newNode);
    searchWord(currentWord, row + 1, col + 1, isVisited, validWords, newNode);
    searchWord(currentWord, row + 1, col - 1, isVisited, validWords, newNode);
    searchWord(currentWord, row - 1, col + 1, isVisited, validWords, newNode);
    searchWord(currentWord, row - 1, col - 1, isVisited, validWords, newNode);
    
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