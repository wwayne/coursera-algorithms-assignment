import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;

public class WordNet {
  private String[] synsetMap;
  private HashMap<String, HashSet<Integer>> nounMap;
  private SAP sap;
  
  public WordNet(String synsets, String hypernyms) {
    if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
    List<String> synsetMapList = new ArrayList<>();
    nounMap = new HashMap<String, HashSet<Integer>>();
    int synAmount = 1;
    
    In inSyn = new In(synsets);
    String line = inSyn.readLine();
    while (line != null) {
      String[] split = line.split(",");
      int synsetId = Integer.parseInt(split[0], 10);
      synsetMapList.add(synsetId, split[1]);
      for (String noun: split[1].split(" ")) {
        HashSet<Integer> idSet = new HashSet<Integer>();
        if (nounMap.containsKey(noun))
          idSet = nounMap.get(noun);
        idSet.add(synsetId);
        nounMap.put(noun, idSet); 
      }
      line = inSyn.readLine();
      synAmount++;
    }
    synsetMap = synsetMapList.toArray(new String[synsetMapList.size()]);
    
    Digraph G = new Digraph(synAmount);
    In inHyp = new In(hypernyms);
    String lineHyp = inHyp.readLine();
    while (lineHyp != null) {
      String[] split = lineHyp.split(",");
      int synsetId = Integer.parseInt(split[0], 10);
      for (int i = 1; i < split.length; i++) {
        G.addEdge(synsetId, Integer.parseInt(split[i], 10));
      }
      lineHyp = inHyp.readLine();
    }
    sap = new SAP(G);
  }

  // returns all WordNet nouns
  public Iterable<String> nouns() {
    return nounMap.keySet();
  }

  // is the word a WordNet noun?
  public boolean isNoun(String word) {
    if (word == null) throw new IllegalArgumentException();
    return nounMap.containsKey(word);
  }

  // distance between nounA and nounB (defined below)
  public int distance(String nounA, String nounB) {
    if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
    HashSet<Integer> synsetIdsA = nounMap.get(nounA);
    HashSet<Integer> synsetIdsB = nounMap.get(nounB);
    return sap.length(synsetIdsA, synsetIdsB);
  }

   // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
   // in a shortest ancestral path (defined below)
  public String sap(String nounA, String nounB) {
    if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
    HashSet<Integer> synsetIdsA = nounMap.get(nounA);
    HashSet<Integer> synsetIdsB = nounMap.get(nounB);
    int ancestor = sap.ancestor(synsetIdsA, synsetIdsB);
    return synsetMap[ancestor];
  }

  public static void main(String[] args) {
    WordNet wn = new WordNet(args[0], args[1]);
    System.out.println(wn.distance("worm", "bird"));
    System.out.println(wn.sap("white_marlin", "mileage"));
  }
}