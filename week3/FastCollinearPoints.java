import java.util.Comparator;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;

public class FastCollinearPoints {
    private LineSegment[] segments;
    private int segmentAmount = 0;
    private Point[] endPoints;
    private double[] endPointSlope ;
    
    public FastCollinearPoints(Point[] pointList) {
        if (pointList == null) throw new IllegalArgumentException();
        checkNullAndDuplicate(pointList);

        Point[] points = Arrays.copyOf(pointList, pointList.length);
        segments = new LineSegment[(int) Math.pow(points.length, 2)];
        endPoints = new Point[2];
        endPointSlope = new double[2];
        
        Arrays.sort(points);

        for (int i = 0; i < points.length; i++) {
            Point originP = points[i];
            double[] slopes = new double[points.length - i - 1];
            Point[] restPoints = new Point[points.length - i - 1];
            for (int j = 0; j < slopes.length; j++) {
                slopes[j] = originP.slopeTo(points[i + j + 1]);
                restPoints[j] = points[i + j + 1];
            }
            sortStart(originP, restPoints, slopes);
            findCollinear(originP, restPoints, slopes);
        }

        LineSegment[] segmentCopy = new LineSegment[segmentAmount];
        for (int i = 0; i < segmentAmount; i++) {
            segmentCopy[i] = segments[i];
        }
        segments = segmentCopy;
        endPoints = null;
        endPointSlope = null;
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
    
    private static void sortStart(Point originP, Point[] restPoints
                                      , double[] slopes) 
    {
        Point[] aux = new Point[restPoints.length];
        double[] slopesAux = new double[restPoints.length];
        sort(originP.slopeOrder(), restPoints, aux, slopes, slopesAux, 0
                 , restPoints.length - 1);
    }
    
    private static void sort(Comparator<Point> c, Point[] restPoints
                                 , Point[] aux, double[] slopes
                                 , double[] slopesAux, int lo, int hi) {
        if (hi <= lo) return;
        int mid = lo + (hi - lo) / 2;
        sort(c, restPoints, aux, slopes, slopesAux, lo, mid);
        sort(c, restPoints, aux, slopes, slopesAux, mid + 1, hi);
        merge(c, restPoints, aux, slopes, slopesAux, lo, mid, hi);
    }
    
    private static void merge(Comparator<Point> c, Point[] restPoints
                                  , Point[] aux, double[] slopes
                                  , double[] slopesAux, int lo, int mid, int hi)
    {
        for (int i = lo; i <= hi; i++) {
            aux[i] = restPoints[i];
            slopesAux[i] = slopes[i];
        }
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k ++) {
            if (i > mid) {
                restPoints[k] = aux[j];
                slopes[k] = slopesAux[j];
                j++;
            } else if (j > hi) {
                restPoints[k] = aux[i];
                slopes[k] = slopesAux[i];
                i++;
            } else if (less(c, aux[i], aux[j])) {
                restPoints[k] = aux[i];
                slopes[k] = slopesAux[i];
                i++;
            } else {
                restPoints[k] = aux[j];
                slopes[k] = slopesAux[j];
                j++;
            }   
        }
    }
    
    private static boolean less(Comparator<Point> c, Point a, Point b) {
        return c.compare(a, b) <= 0;
    }
    
    private void findCollinear(Point originP, Point[] points, double[] slopes)
    {
        int start = 0, end = 2;
        while (end < points.length) {
            if (slopes[start] == slopes[end]) {
                if (points.length == ++end) {
                    Point endPoint = points[end - 1];
                    Boolean alreadyHasEndpoint = false;
                    for (int i = 0; i < endPoints.length; i ++) {
                        if (endPoints[i] == endPoint &&
                            endPointSlope[i] == slopes[start]
                           )
                        {
                            alreadyHasEndpoint = true;
                            return;
                        }
                    }
                    if (!alreadyHasEndpoint) {
                        if (segmentAmount == endPoints.length) {
                            resizeEndPoint(endPoints.length * 2);
                        }
                        endPoints[segmentAmount] = endPoint;
                        endPointSlope[segmentAmount] = slopes[start];
                        segments[segmentAmount++] = 
                            new LineSegment(originP, endPoint);
                    }
                }
                continue;
            }
            if (end - start >= 3) {
                Point endPoint = points[end - 1];
                Boolean alreadyHasEndpoint = false;
                for (int i = 0; i < endPoints.length; i ++) {
                    if (endPoints[i] == endPoint &&
                        endPointSlope[i] == slopes[start]
                       )
                    {
                        alreadyHasEndpoint = true;
                        return;
                    }
                }        
                if (!alreadyHasEndpoint) {
                    if (segmentAmount == endPoints.length) {
                        resizeEndPoint(endPoints.length * 2);
                    }
                    
                    endPoints[segmentAmount] = endPoint;
                    endPointSlope[segmentAmount] = slopes[start];
                    segments[segmentAmount++] = 
                        new LineSegment(originP, endPoint);
                }
                start = end;
                end = start + 2;
            } else {
                start++;
                end++;
            }
            
        }
    }
    
    private void resizeEndPoint(int capacity) {
        Point[] endPointsCopy = new Point[capacity];
        double[] endPointSlopeCopy = new double[capacity];
        for (int i = 0; i < endPoints.length; i++) {
            endPointsCopy[i] = endPoints[i];
            endPointSlopeCopy[i] = endPointSlope[i];
        }
        endPoints = endPointsCopy;
        endPointSlope = endPointSlopeCopy;
    }
    
    public int numberOfSegments() {
        return segmentAmount;
    }
    
    public LineSegment[] segments() {
        return Arrays.copyOf(segments, segmentAmount);
    }
    
    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
        }
    }
}