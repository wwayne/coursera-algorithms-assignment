import edu.princeton.cs.algs4.StdIn;
import java.util.Iterator;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0], 10);
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        
        do {
            String readString = StdIn.readString();
            queue.enqueue(readString);
        } while (!StdIn.isEmpty());
        
        Iterator<String> i = queue.iterator();
        while (k != 0) {
            System.out.println(i.next());
            k--;
        }
    } 
}