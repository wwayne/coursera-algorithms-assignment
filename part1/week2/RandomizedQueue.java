import edu.princeton.cs.algs4.StdRandom;
import java.util.NoSuchElementException;
import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue;
    private int n = 0;
    
    public RandomizedQueue() {
        queue = (Item[]) new Object[2];
    }
    
    public boolean isEmpty() {
        return n == 0;
    }
    
    public int size() {
        return n;
    }
    
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (queue.length == n) resize(queue.length * 2);
        queue[n++] = item;
    }
    
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        int index = StdRandom.uniform(n);
        Item popItem = queue[index];
        queue[index] = queue[n - 1];
        queue[--n] = null;
        if (n > 0 && n == queue.length / 4) resize(queue.length / 2);
        return popItem;
    }
       
    private void resize(int capacity) {
        Item[] copyQueue = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            copyQueue[i] = queue[i];
        }
        queue = copyQueue;
    }
    
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        int index = StdRandom.uniform(n);
        return queue[index];
    }
    
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }
    
    private class ArrayIterator implements Iterator<Item> {
        private int index = n;
        private Item[] myQueue;
        
        public ArrayIterator() {
            myQueue = (Item[]) new Object[n];
            for (int i = 0; i < index; i++) {
                myQueue[i] = queue[i];
            }
            StdRandom.shuffle(myQueue);
        }
        
        public boolean hasNext() {
            return index > 0;
        }
        
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return myQueue[--index];
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
    public static void main(String[] args) {
        RandomizedQueue<Integer> randomQueue = new RandomizedQueue<Integer>();
        randomQueue.enqueue(1);
        randomQueue.enqueue(2);
        randomQueue.enqueue(3);
//        System.out.println(randomQueue.dequeue());
        randomQueue.enqueue(4);
        randomQueue.enqueue(5);
        randomQueue.enqueue(6);
        randomQueue.enqueue(4);
        randomQueue.enqueue(5);
        randomQueue.enqueue(6);
        randomQueue.dequeue();
        randomQueue.dequeue();
        randomQueue.dequeue();
        randomQueue.dequeue();
        randomQueue.dequeue();
        randomQueue.dequeue();
        randomQueue.dequeue();
        randomQueue.dequeue();
        randomQueue.dequeue();
//        System.out.println(randomQueue.sample()); 
//        Iterator<Integer> i = randomQueue.iterator();
//        Iterator<Integer> j = randomQueue.iterator();
//        while (i.hasNext()) {
//            System.out.println("first iterator: " + i.next());
//            System.out.println("second iterator: " + j.next());
//        }
    }
}