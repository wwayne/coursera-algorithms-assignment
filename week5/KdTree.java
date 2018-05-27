import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class KdTree {
  private Node root;
  private int size;
  
  private static class Node {
    private Point2D p;
    private RectHV rect;
    private Node leftBottom;
    private Node rightTop;
    
    public Node(Point2D p, RectHV rect) {
       this.p = p;
       this.rect = rect;
       this.leftBottom = null;
       this.rightTop = null;
    }
  }
  
  public KdTree() {
    size = 0;
  }
  
  public boolean isEmpty() {
    return size == 0;
  }
  
  public int size() {
    return size;
  }
  
  public void insert(Point2D p) {
    if (p == null) throw new IllegalArgumentException();
    if (root == null) {
      root = new Node(p, new RectHV(0.0, 0.0, 1.0, 1.0));
      size++;
      return;
    }
    int orientaion = 1; // 1:vertical; -1:horizontal
    Node currentNode = root;
    while(currentNode != null) {
      if (currentNode.p.equals(p)) return;
      if (orientaion > 0) {
        if (p.x() >= currentNode.p.x()) {
          if (currentNode.rightTop == null) {
            RectHV rect = new RectHV(currentNode.p.x(), currentNode.rect.ymin(), currentNode.rect.xmax(), currentNode.rect.ymax());
            currentNode.rightTop = new Node(p, rect);
            size++;
            return;
          }
          currentNode = currentNode.rightTop;
        } 
        else {
          if (currentNode.leftBottom == null) {
            RectHV rect = new RectHV(currentNode.rect.xmin(), currentNode.rect.ymin(), currentNode.p.x(), currentNode.rect.ymax());
            currentNode.leftBottom = new Node(p, rect);
            size++;
            return;
          }
          currentNode = currentNode.leftBottom;
        }
      } else {
        if (p.y() >= currentNode.p.y()) {
          if (currentNode.rightTop == null) {
            RectHV rect = new RectHV(currentNode.rect.xmin(), currentNode.p.y(), currentNode.rect.xmax(), currentNode.rect.ymax());
            currentNode.rightTop = new Node(p, rect);
            size++;
            return;
          }
          currentNode = currentNode.rightTop;
        }
        else {
          if (currentNode.leftBottom == null) {
            RectHV rect = new RectHV(currentNode.rect.xmin(), currentNode.rect.ymin(), currentNode.rect.xmax(), currentNode.p.y());
            currentNode.leftBottom = new Node(p, rect);
            size++;
            return;
          }
          currentNode = currentNode.leftBottom;
        }
      }
      
      orientaion = orientaion * -1;
    }
  }
  
  public boolean contains(Point2D p) {
    if (p == null) throw new IllegalArgumentException();
    int orientaion = 1; // 1:vertical; -1:horizontal
    Node currentNode = root;
    while(currentNode != null) {
      if (currentNode.p.equals(p)) return true;
      if (orientaion > 0) {
        if (p.x() >= currentNode.p.x()) currentNode = currentNode.rightTop;
        else currentNode = currentNode.leftBottom;
      } else {
        if (p.y() >= currentNode.p.y()) currentNode = currentNode.rightTop;
        else currentNode = currentNode.leftBottom;
      }
      orientaion = orientaion * -1;
    }
    return false;
  }
  
  public void draw() {
    draw(root);
  }
  
  private void draw(Node node) {
    if (node == null) return;
    node.p.draw();
    node.rect.draw();
    draw(node.leftBottom);
    draw(node.rightTop);
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
      searchPointInRange(root, rect, pointList);
      points = pointList.toArray(new Point2D[pointList.size()]);
    }
    
    private void searchPointInRange(Node node, RectHV rect, List<Point2D> pointList) {
      if (node == null || !node.rect.intersects(rect)) return;
      if (rect.contains(node.p)) pointList.add(node.p);
      searchPointInRange(node.leftBottom, rect, pointList);
      searchPointInRange(node.rightTop, rect, pointList);
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
    Nearest nearest = new Nearest(p);
    return nearest.getNearest();
  }

  private class Nearest {
    private Point2D nearestP;
    private double minDistance;
    
    public Nearest (Point2D targetP) {
      nearestP = root.p;
      minDistance = root.p.distanceTo(targetP);
      findNearest(root, targetP);
    }
    
    private void findNearest(Node node, Point2D targetP) {
      if (node == null || node.rect.distanceTo(targetP) > minDistance) return;
      
      double myMinDistance = node.p.distanceTo(targetP);
      if (myMinDistance < minDistance) {
        nearestP = node.p;
        minDistance = myMinDistance;
      }
      findNearest(node.leftBottom, targetP);
      findNearest(node.rightTop, targetP);
    }
    
    public Point2D getNearest () {
      return nearestP;
    }
  }

  public static void main(String[] args) {
    KdTree kd = new KdTree();
//    Point2D p1 = new Point2D(0.1, 0.2);
//    Point2D p2 = new Point2D(0.2, 0.2);
//    Point2D p3 = new Point2D(0.1, 0.3);
//    Point2D p4 = new Point2D(0.1, 0.3);
//    Point2D p5 = new Point2D(0.25, 0.2);
//    kd.insert(p1);
//    kd.insert(p2);
//    kd.insert(p3);
//    kd.insert(p4);
//    System.out.println(kd.size());
//    System.out.println(kd.contains(p1));
//    System.out.println(kd.contains(p5));
//    
//    System.out.println("Test randge ----");
//    RectHV rHV = new RectHV(0.1, 0.1, 0.21, 0.2);
//    for (Point2D p: kd.range(rHV)) {
//      System.out.println(p);
//    }
    
    Point2D pointA = new Point2D(0.7, 0.2);
    Point2D pointB = new Point2D(0.5, 0.4);
    Point2D pointC = new Point2D(0.2, 0.3);
    Point2D pointD = new Point2D(0.4, 0.7);
    Point2D pointE = new Point2D(0.9, 0.6);
    kd.insert(pointA);
    kd.insert(pointB);
    kd.insert(pointC);
    kd.insert(pointD);
    kd.insert(pointE);
    
    System.out.println("Test nearest ----");
    System.out.println(kd.nearest(new Point2D(0.203, 0.764)));
  }
}