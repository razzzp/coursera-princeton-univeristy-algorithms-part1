import java.util.Arrays;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class BruteCollinearPoints {
    private LineSegment[] _segments;
    private int _numberOfSegments =0;
    private int _segArraySize = 4;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {

        // check null, null points and duplicate points, sorts array
        _checkPoints(points);

        // segments
        _segments = new LineSegment[_segArraySize];
        double slope1,slope2,slope3;

        for (int i = 0; i < points.length-3; i++){
            for (int j = i+1; j < points.length-2; j++){
                for (int k = j+1; k < points.length-1; k++){
                    for (int l = k+1; l < points.length; l++){
                        slope1 = points[i].slopeTo(points[j]);
                        slope2 = points[i].slopeTo(points[k]);
                        slope3 = points[i].slopeTo(points[l]);
                        
                        if (slope1 == slope2 && slope1 == slope3){
                            // not needed anymore cause array is already sorted
                            // _segments[_numberOfSegments++] = new LineSegment(_minYPoint(curPoints), _maxYPoint(curPoints));
                            _addSegment(points[i], points[l]);
                        }
                    }   
                }   
            }   
        }   
    }

    private void _checkPoints(Point[] points){
        if(points == null) throw new IllegalArgumentException("points is null");
        // sort to check for dupes
        Arrays.sort(points);
        for(int i = 1;i < points.length; i++){
            if (points[i].compareTo(points[i-1]) == 0) throw new IllegalArgumentException("duplicate points found");
            if (points[i] == null || points[i-1] == null) throw new IllegalArgumentException("points contain a null point");
        }
    }

    private void _addSegment(Point p1, Point p2){
        if (_numberOfSegments == _segArraySize){
            // resize
            _segArraySize *= 2;
            LineSegment[] newArr = new LineSegment[_segArraySize];
            for (int i = 0; i<_segments.length;i++){
                newArr[i] = _segments[i];
            }
            _segments = newArr;
        }
        _segments[_numberOfSegments++] = new LineSegment(p1, p2);
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
        StdDraw.setPenRadius(0.005);
        StdDraw.setScale(XSCALE_MIN, XSCALE_MAX);
        StdDraw.setPenColor(StdDraw.BLACK);
        // x axis
        StdDraw.line(-50, 0, 50, 0);
        // y axis
        StdDraw.line(0, -50, 0, 50);
        //
        StdDraw.setPenRadius(0.01);
    }

    private static void _testCollinear(){
        Point[] points = new Point[100];
        _makeStraightLine(points, 0, -10, 4, 0, 4);
        _makeStraightLine(points, 4, -10, 1, 4, 4);
        // _addRandomPoints(points, 4, XSCALE_MIN, XSCALE_MAX);
        _addUniqueRandomPoints(points, 8, XSCALE_MIN, XSCALE_MAX);
        // StdRandom.shuffle(points);
        
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
        StdOut.println("end");
    }

    private static void _makeStraightLine(Point[] inArray, int startIndex, int iX, int gradient, int c, int num){
        int step = 2;
        for(int i=0;i<num;i++){
            int x = iX + i*step;
            int y= c + gradient * x;
            inArray[startIndex+i] = new Point(x,y);
        }   
    }

    private static void _addRandomPoints(Point[] inArray, int startIndex, int min, int max){
        for(int i=0;i<inArray.length-startIndex;i++){
            inArray[startIndex+i] = new Point(StdRandom.uniform(min,max),StdRandom.uniform(min, max));
        }  
    }
    
    private static void _addUniqueRandomPoints(Point[] inArray, int startIndex, int min, int max){
        for(int i=0;i<inArray.length-startIndex;i++){
            Point newPoint;
            do{
                newPoint = new Point(StdRandom.uniform(min,max),StdRandom.uniform(min, max));
            } while(_pointExists(newPoint, inArray));

            inArray[startIndex+i] = newPoint;
        }  
    }


    private static boolean _pointExists(Point refPoint, Point[] inArray){
        for(Point p : inArray){
            if (p == null) continue;
            if (p.compareTo(refPoint) == 0) return true;
        }
        return false;
    }
}