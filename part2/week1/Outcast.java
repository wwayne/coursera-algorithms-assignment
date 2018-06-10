import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
  private WordNet wn;
  
  public Outcast(WordNet wordnet) {
    wn = wordnet;
  }
  
  // given an array of WordNet nouns, return an outcast
  public String outcast(String[] nouns) {
    int[] nounLenArray = new int[nouns.length];
    int maxDistance = 0;
    String maxNoun = "";
    for (int i = 0; i < nouns.length - 1; i++) {
      for (int j = i + 1; j < nouns.length; j ++) {
        int distance = wn.distance(nouns[i], nouns[j]);
        nounLenArray[i] += distance;
        nounLenArray[j] += distance;
      }
      if (nounLenArray[i] > maxDistance) {
        maxDistance = nounLenArray[i];
        maxNoun = nouns[i];
      }
    }
    if (nounLenArray[nouns.length - 1] > maxDistance) {
      maxNoun = nouns[nouns.length - 1];
    }
    return maxNoun;
  }
  
  public static void main(String[] args) {
    WordNet wordnet = new WordNet(args[0], args[1]);
    Outcast outcast = new Outcast(wordnet);
    for (int t = 2; t < args.length; t++) {
        In in = new In(args[t]);
        String[] nouns = in.readAllStrings();
        StdOut.println(args[t] + ": " + outcast.outcast(nouns));
    }
  }
}