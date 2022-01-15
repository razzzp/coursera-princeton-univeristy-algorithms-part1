import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class BruteCollinearPoints {
    private final LineSegment[] _segments;
    private int _numberOfSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if(points == null) throw new IllegalArgumentException("points is null");
        for (Point point : points) {
            if (point == null) throw new IllegalArgumentException("points contain a null point");
        }
        // how to check for duplicates?

        // divide by 3 in case segments share 1 point
        _segments = new LineSegment[points.length];
        double slope1,slope2,slope3;

        for (int i = 0; i < points.length-3; i++){
            for (int j = i+1; j < points.length-2; j++){
                for (int k = j+1; k < points.length-1; k++){
                    for (int l = k+1; l < points.length; l++){
                        Point[] curPoints = new Point[4];
                        curPoints[0] = points[i];
                        curPoints[1] = points[j];
                        curPoints[2] = points[k];
                        curPoints[3] = points[l]; 
                        slope1 = points[i].slopeTo(points[j]);
                        slope2 = points[i].slopeTo(points[k]);
                        slope3 = points[i].slopeTo(points[l]);
                        
                        if (slope1 == slope2 && slope1 == slope3){
                            _segments[_numberOfSegments++] = new LineSegment(_minYPoint(curPoints), _maxYPoint(curPoints));
                        }
                    }   
                }   
            }   
        }   
    }

    private Point _minYPoint(Point[] points){
        Point minYPoint = points[0];
        for (Point point : points) {
            if(point.compareTo(minYPoint) < 0)
                minYPoint = point;
        }
        return minYPoint;
    }

    private Point _maxYPoint(Point[] points){
        Point maxYPoint = points[0];
        for (Point point : points) {
            if(point.compareTo(maxYPoint) > 0)
                maxYPoint = point;
        }
        return maxYPoint;
    }

    // the number of line segments
    public int numberOfSegments() {
        return _numberOfSegments;
    }

    // the line segments
    public LineSegment[] segments() {
        return _segments;
    }

    private static final int XSCALE_MIN = -50;
    private static final int XSCALE_MAX = 50;
    private static final int YSCALE_MIN = -50;
    private static final int YSCALE_MAX = -50;
    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;

    public static void main(String[] args) {
        _initDraw();
        //
        _testCollinear();
    }

    private static void _initDraw() {
        StdDraw.setPenRadius(0.01);
        StdDraw.setScale(XSCALE_MIN, XSCALE_MAX);
        StdDraw.point(45, -45);
    }

    private static void _testCollinear(){
        Point[] points = new Point[100];
        _makeStraightLine(points, 0, 4, 0, 4);
        _addRandomPoints(points, 4, XSCALE_MIN, XSCALE_MAX);
        // StdRandom.shuffle(points);
        

        StdOut.println("1 straight line grad 2:");
        BruteCollinearPoints bcp = new BruteCollinearPoints(points);
        StdOut.println("total segments: " + bcp.numberOfSegments());

        // draw lines
        StdDraw.setPenColor(StdDraw.BLUE);
        for(LineSegment ls : bcp.segments()){
            StdOut.println(ls);
            if (ls!= null) ls.draw();
        }

        // draw points
        StdDraw.setPenColor(StdDraw.RED);
        for(Point p : points){
            StdOut.println(p);
            if(p != null) p.draw();
        }
    }

    private static void _makeStraightLine(Point[] inArray, int startIndex, int gradient, int c, int num){
        int ix = -4;
        int step = 2;
        for(int i=0;i<num;i++){
            int x = ix + i*step;
            int y= c + gradient * x;
            inArray[startIndex+i] = new Point(x,y);
        }   
    }

    private static void _addRandomPoints(Point[] inArray, int startIndex, int min, int max){
        for(int i=0;i<inArray.length-startIndex;i++){
            inArray[startIndex+i] = new Point(StdRandom.uniform(min,max),StdRandom.uniform(min, max));
        }  
    }
 }