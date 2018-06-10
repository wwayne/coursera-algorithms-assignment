import java.util.NoSuchElementException;
import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private int size = 0;
    private Node first, last;
    
    private class Node {
        private Item item;
        private Node next;
        private Node previous;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public int size() {
        return size;
    }
    
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        first.previous = null;
        if (oldFirst != null) oldFirst.previous = first;
        if (size == 0) last = first;
        size++;
    }
    
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.previous = oldLast;
        last.next = null;
        if (oldLast != null) oldLast.next = last;
        if (size == 0) first = last;
        size++;
    }
    
    public Item removeFirst() {
        if (size == 0) throw new NoSuchElementException();
        Item oldFirstItem = first.item;
        if (size == 1) {
            first = null;
            last = null;
        }
        else {
            first = first.next;
            first.previous = null;
        }
        size--;
        return oldFirstItem;
    }
    
    public Item removeLast() {
        if (size == 0) throw new NoSuchElementException();
        Item oldLastItem = last.item;
        if (size == 1) {
            first = null;
            last = null;
        }
        else {
            last = last.previous;
            last.next = null;
        }
        size--;
        return oldLastItem;
    }
    
    public Iterator<Item> iterator() {
        return new ListIterator();
    }
    
    private class ListIterator implements Iterator<Item> {
        private Node current = first;
        
        public boolean hasNext() {
            return current != null;
        }
        
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item currentItem = current.item;
            current = current.next;
            return currentItem;
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();
        System.out.println(deque.size());
        deque.addFirst("wayne");
        deque.addLast("cjt");
        deque.addFirst("wzx");
        System.out.println(deque.size());
        
        System.out.println(deque.removeLast());
        System.out.println(deque.size());
        
        System.out.println(deque.removeFirst());
        System.out.println(deque.size());
        
        deque.addFirst("2");
        deque.addFirst("1");
        Iterator<String> i = deque.iterator();
        while (i.hasNext()) {
            System.out.println("iterator: " + i.next());
        }
    }
}