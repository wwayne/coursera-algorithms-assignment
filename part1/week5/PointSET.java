import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class PointSET {
  private SET<Point2D> set;
  
  public PointSET() {
    set = new SET<Point2D>();
  }
  
  public boolean isEmpty() {
    return set.isEmpty();
  }
  
  public int size() {
    return set.size();
  }
  
  public void insert(Point2D p) {
    if (p == null) throw new IllegalArgumentException();
    set.add(p);
  }
  
  public boolean contains(Point2D p) {
    if (p == null) throw new IllegalArgumentException();
    return set.contains(p);
  }
  
  public void draw() {
    for (Point2D point: set) {
      point.draw();
    }
  }
  
  public Iterable<Point2D> range(RectHV rect) {
    if (rect == null) throw new IllegalArgumentException();
    return new Iterable<Point2D>() {
      public Iterator<Point2D> iterator() {
        return new RangeIterator(rect);
      }
    };
  }
  
  private class RangeIterator implements Iterator<Point2D> {
    private int index = 0;
    private Point2D[] points;
      
    public RangeIterator(RectHV rect) {
      List<Point2D> pointList = new ArrayList<>();
      for (Point2D point: set) {
        if (rect.contains(point)) pointList.add(point);
      }
      points = pointList.toArray(new Point2D[pointList.size()]);
    }
      
    public boolean hasNext() {
      return index != points.length;
    }
      
    public Point2D next() {
      if (!hasNext()) throw new NoSuchElementException();
      return points[index++];
    }
      
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
  
  public Point2D nearest(Point2D p) {
    if (p == null) throw new IllegalArgumentException();
    if (isEmpty()) return null;
    double minDistance = 0.0;
    Point2D minPoint = null;
    for (Point2D point: set) {
      double distance = point.distanceTo(p);
      if (minPoint == null || distance < minDistance) {
        minDistance = distance;
        minPoint = point;
      }
    }
    return minPoint;
  }

  public static void main(String[] args) {
    PointSET ps = new PointSET();
    Point2D p1 = new Point2D(1.0, 2.0);
    Point2D p2 = new Point2D(2.0, 2.0);
    Point2D p3 = new Point2D(1.0, 3.0);
    Point2D p4 = new Point2D(-1.0, -1.0);
    ps.insert(p1);
    ps.insert(p2);
    ps.insert(p3);
    System.out.println(ps.size());
    System.out.println(ps.contains(p1));
    System.out.println(ps.contains(p4));
    System.out.println(ps.nearest(p4));
    
    System.out.println("Test randge ----");
    RectHV rHV = new RectHV(1.0, 1.0, 2.5, 2.5);
    for (Point2D p: ps.range(rHV)) {
      System.out.println(p);
    }
  }
}