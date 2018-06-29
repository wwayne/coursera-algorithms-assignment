import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class BaseballElimination {
  private String[] teams;
  private int[] wins;
  private int[] loses;
  private int[] remains;
  private int[][] matches;
  private String[] certificates;
  private String[][] cache;
  private boolean[] cached;
  
  public BaseballElimination(String filename) {
    In inFile = new In(filename);
    int verticesAmount = Integer.parseInt(inFile.readLine(), 10);
    teams = new String[verticesAmount];
    wins = new int[verticesAmount];
    loses = new int[verticesAmount];
    remains = new int[verticesAmount];
    matches = new int[verticesAmount][];
    cache = new String[verticesAmount][];
    cached = new boolean[verticesAmount];
    
    String line = inFile.readLine();
    int index = 0;
    while (line != null) {
      String[] res = line.split("\\s+");
      if (res[0].isEmpty()) res = Arrays.copyOfRange(res, 1, res.length);
      teams[index] = res[0];
      wins[index] = Integer.parseInt(res[1], 10);
      loses[index] = Integer.parseInt(res[2], 10);
      remains[index] = Integer.parseInt(res[3], 10);
      matches[index] = new int[verticesAmount];
      for (int i = 4; i < res.length; i++) {
        matches[index][i - 4] = Integer.parseInt(res[i], 10);
      }
      
      line = inFile.readLine();
      index++;
    }
  }
  
  private int findTeamIndexByName (String teamName) {
    int index = Arrays.asList(teams).indexOf(teamName);
    if (index == -1) throw new IllegalArgumentException();
    return index;
  }
  
  public int numberOfTeams() {
    return teams.length;
  }
  
  public Iterable<String> teams() {
    return Arrays.asList(teams);
  }
  
  public int wins(String team) {
    int index = findTeamIndexByName(team);
    return wins[index];
  }
  
  public int losses(String team) {
    int index = findTeamIndexByName(team);
    return loses[index];
  }
  
  public int remaining(String team) {
    int index = findTeamIndexByName(team);
    return remains[index];
  }
  
  // number of remaining games between team1 and team2
  public int against(String team1, String team2) {
    int index1 = findTeamIndexByName(team1);
    int index2 = findTeamIndexByName(team2);
    return matches[index1][index2];
  }
  
  public boolean isEliminated(String team) {
    int teamIndex = findTeamIndexByName(team);
    if (cached[teamIndex]) return cache[teamIndex] != null;
    
    if (isAnyOtherTeamHasMoreWins(team)) return true;
    
    int teamAmount = numberOfTeams() - 1;
    int matchVerticesAmount = teamAmount * (teamAmount - 1) / 2;
    FlowNetwork network = setupFlowNetwork(team);
    FordFulkerson ff = new FordFulkerson(network, 0, matchVerticesAmount + teamAmount + 2 - 1);
    List<String> certificateList = new ArrayList<>();
    for (int i = 0; i < numberOfTeams(); i++) {
      if (i == teamIndex) continue;
      int teamVertex = 1 + matchVerticesAmount + i;
      if (i > teamIndex) teamVertex -= 1;
      if (ff.inCut(teamVertex)) certificateList.add(teams[i]);
    }
    
    certificates = certificateList.toArray(new String[certificateList.size()]);
    boolean result = certificates.length != 0;
    cached[teamIndex] = true;
    if (result) cache[teamIndex] = certificates;
    return result;
  }
  
  public Iterable<String> certificateOfElimination(String team) {
    int teamIndex = findTeamIndexByName(team);
    if (cached[teamIndex]) {
      if (cache[teamIndex] == null) return null;
      return Arrays.asList(cache[teamIndex]);
    };
    
    if (!isEliminated(team)) return null;
    return Arrays.asList(certificates);
  }
  
  private boolean isAnyOtherTeamHasMoreWins(String team) {
    int index = findTeamIndexByName(team);
    int myPossibleAllWins = wins[index] + remains[index];
    for (int i = 0; i < numberOfTeams(); i++) {
      if (i == index) continue;
      if (wins[i] > myPossibleAllWins) {
        certificates = new String[1];
        certificates[0] = teams[i];
        cached[index] = true;
        cache[index] = certificates;
        return true;
      }
    }
    return false;
  }
  
  private FlowNetwork setupFlowNetwork(String team) {
    int teamAmount = numberOfTeams() - 1;
    int matchVerticesAmount = teamAmount * (teamAmount - 1) / 2;
    int verticesAmount = matchVerticesAmount + teamAmount + 2;
    int teamIndex = findTeamIndexByName(team);
    
    FlowNetwork network = new FlowNetwork(verticesAmount);
    int targetVertex = 1;
    // network between source with match vertex and match vertex with team vertex
    for (int i = 0; i < numberOfTeams() - 1; i++) {
      if (i == teamIndex) continue;
      for (int j = i + 1; j < numberOfTeams(); j++) {
        if (j == teamIndex) continue;
        network.addEdge(new FlowEdge(0, targetVertex, matches[i][j]));
        int iVertex = findTeamVertex(teamIndex, 1 + matchVerticesAmount, i);
        int jVertex = findTeamVertex(teamIndex, 1 + matchVerticesAmount, j);
        network.addEdge(new FlowEdge(targetVertex, iVertex, Double.POSITIVE_INFINITY));
        network.addEdge(new FlowEdge(targetVertex, jVertex, Double.POSITIVE_INFINITY));
        targetVertex++;
      }
    }
    
    // network between team vertext with sink
    int myPossibleAllWins = wins[teamIndex] + remains[teamIndex];
    for (int i = 0; i < numberOfTeams(); i++) {
      if (i == teamIndex) continue;
      int teamVertex = findTeamVertex(teamIndex, 1 + matchVerticesAmount, i);
      network.addEdge(new FlowEdge(teamVertex, verticesAmount - 1, myPossibleAllWins - wins[i]));
    }
    
    return network;
  }
  
  private int findTeamVertex (int teamIndex, int startVertex, int currentTeamIndex) {
    if (currentTeamIndex < teamIndex) return startVertex + currentTeamIndex;
    return startVertex + currentTeamIndex - 1;
  }
  
  public static void main(String[] args) {
    BaseballElimination division = new BaseballElimination(args[0]);
    for (String team : division.teams()) {
        if (division.isEliminated(team)) {
            StdOut.print(team + " is eliminated by the subset R = { ");
            for (String t : division.certificateOfElimination(team)) {
                StdOut.print(t + " ");
            }
            StdOut.println("}");
        }
        else {
            StdOut.println(team + " is not eliminated");
        }
    }
  }
}