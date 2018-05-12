import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment[] segments;
    private int segmentAmount = 0;
    
    public BruteCollinearPoints(Point[] pointList) {
        if (pointList == null) throw new IllegalArgumentException();
        checkNullAndDuplicate(pointList);

        Point[] points = Arrays.copyOf(pointList, pointList.length);
        segments = new LineSegment[(int) Math.pow(points.length, 2)];
        
        Arrays.sort(points);
        
        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i + 1; j < points.length - 2; j++) {
                for (int k = j + 1; k < points.length - 1; k++) {
                    for (int w = k + 1; w < points.length; w++) {
                        Point p1 = points[i];
                        Point p2 = points[j];
                        Point p3 = points[k];
                        Point p4 = points[w];
                        Point[] linear = {p1, p2, p3, p4};
                        if (isCollinear(linear)) {
                            segments[segmentAmount++] = new LineSegment(p1, p4);
                        }
                    }
                }
            }
        }

        LineSegment[] copySegments = new LineSegment[segmentAmount];
        for (int i = 0; i < segmentAmount; i++) {
            copySegments[i] = segments[i];
        }
        segments = copySegments;
    }
    
    private void checkNullAndDuplicate(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
            for (int j = i + 1; j < points.length; j++) {
                if (points[j] == null) throw new IllegalArgumentException();
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }        
        }
    }
    
    private boolean isCollinear(Point[] points) {
        double slope0To1 = points[0].slopeTo(points[1]);
        double slope1To2 = points[1].slopeTo(points[2]);
        double slope2To3 = points[2].slopeTo(points[3]);
 
        if (slope0To1 == Double.NEGATIVE_INFINITY ||
            slope1To2 == Double.NEGATIVE_INFINITY ||
            slope2To3 == Double.NEGATIVE_INFINITY
        ) throw new IllegalArgumentException(); 
        
        return slope0To1 == slope1To2
            && slope1To2 == slope2To3;
    }
    
    public int numberOfSegments() {
        return segmentAmount;
    }
    
    public LineSegment[] segments() {
        return Arrays.copyOf(segments, segmentAmount);
    }
    
    public static void main (String[] args) {
        Point a = new Point(3, 3);
        Point b = new Point(5, 5);
        Point c = new Point(6, 6);
        Point d = new Point(7, 7);
        Point e = new Point(-1, 6);
        Point f = new Point(-3, 7);
        Point[] points = {a,b,c,d};
        BruteCollinearPoints bcp = new BruteCollinearPoints(points);
//        System.out.println("numberOfSegments: " + bcp.numberOfSegments());
//        System.out.println("segments: " + bcp.segments()[0].toString());
        
        LineSegment[] segments = bcp.segments();
        System.out.println("first segments: " + segments);
        segments[0] = new LineSegment(e, f);
        points[0] = e;
        System.out.println("last segments: " + bcp.segments());
    }
}