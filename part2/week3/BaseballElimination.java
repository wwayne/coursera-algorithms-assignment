import edu.princeton.cs.algs4.In;
import java.util.Arrays;

public class BaseballElimination {
  private String[][] data;
  private String[] teams; 
  
  public BaseballElimination(String filename) {
    In inFile = new In(filename);
    int verticesAmount = Integer.parseInt(inFile.readLine(), 10);
    data = new String[verticesAmount][];
    teams = new String[verticesAmount];
    
    String line = inFile.readLine();
    int index = 0;
    while (line != null) {
      String[] resArray = line.split("\\s+");
      data[index] = resArray;
      teams[index] = resArray[0];
      line = inFile.readLine();
      index++;
    }
  }
  
  public int numberOfTeams() {
    return data.length;
  }
  
  public Iterable<String> teams() {
    return Arrays.asList(teams);
  }
  
  public int wins(String team) {
    int index = Arrays.asList(teams).indexOf(team);
    return Integer.parseInt(data[index][1], 10);
  }
  
  public int losses(String team) {
    int index = Arrays.asList(teams).indexOf(team);
    return Integer.parseInt(data[index][2], 10);
  }
  
  public int remaining(String team) {
    int index = Arrays.asList(teams).indexOf(team);
    return Integer.parseInt(data[index][3], 10);
  }
  
  // number of remaining games between team1 and team2
  public int against(String team1, String team2) {
    int index1 = Arrays.asList(teams).indexOf(team1);
    int index2 = Arrays.asList(teams).indexOf(team2);
    return Integer.parseInt(data[index1][4 + index2], 10);
  }
  
//public          boolean isEliminated(String team)              // is given team eliminated?
//public Iterable<String> certificateOfElimination(String team)  // subset R of teams that eliminates given team; null if not eliminated
  
  public static void main(String[] args) {
    BaseballElimination be = new BaseballElimination(args[0]);
    
  }
}