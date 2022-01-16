import java.util.Arrays;

import javax.lang.model.util.ElementScanner14;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class FastCollinearPoints {

    private final LineSegment[] _segments;
    private int _numberOfSegments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points){
        // check null, null points and duplicate points, sorts array
        _checkPoints(points);

        // segments
        _segments = new LineSegment[points.length];
        
        for (int i = 0; i < points.length; i++){
            Point refPoint = points[i];

            // array for sorting other points, by slope
            Point[] auxPoints = new Point[points.length];

            // O(n)
            // TODO: do we need to check with previous points?
            for(int j=0; j<points.length; j++){
                // if (j != i) 
                auxPoints[j] = points[j];
            }   

            // order by slope to elem in outer loop
            // O(nlog n)
            Arrays.sort(auxPoints, refPoint.slopeOrder());

            // check for 3 or more equal slopes, to get line with 4 or more points
            int equalSlopeAccum = 0;
            double prevSlope = Double.NaN;
            int startPointIndex = 0;
            // O(n)
            for (int j=0;j<auxPoints.length;j++){
                double curSlope = refPoint.slopeTo(auxPoints[j]);

                if (curSlope == prevSlope){
                    // set start of line, sort is stable so, it should be sorted first
                    //  by slope order and then by normal compareTo
                    //  so prev point is lowest point on line
                    if (equalSlopeAccum == 0) startPointIndex = j-1;
                    // if equal accumulate and continue
                    equalSlopeAccum++;
                }
                else {
                    if (equalSlopeAccum >= 3){
                        // if current slope different that prev
                        //  and has been the same for 3 or more points
                        //  create line segment, from lowest point to highest
                        

                        // lowest can either be refPoint or point at startindex
                        Point lowestPointInLine;
                        if (refPoint.compareTo(auxPoints[startPointIndex]) < 0) lowestPointInLine = refPoint;
                        else lowestPointInLine = auxPoints[startPointIndex];

                        // highest pointin line can either be ref point or
                        //  elem at j-1 
                        Point highestPointInLine;
                        if (refPoint.compareTo(auxPoints[j-1]) > 0) highestPointInLine = refPoint;
                        else highestPointInLine = auxPoints[j-1];

                        _segments[_numberOfSegments++] = new LineSegment(lowestPointInLine, highestPointInLine);
                    }
                    // reset accumulator
                    equalSlopeAccum=0;
                }
                // prepare next loop
                prevSlope = curSlope;
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
    private static double penWidth = 0.01;

    public static void main(String[] args) {
        _initDraw();
        //
        _testCollinear();
    }

    private static void _initDraw() {
        StdDraw.setPenRadius(0.005);
        StdDraw.setScale(XSCALE_MIN, XSCALE_MAX);
        StdDraw.point(45, -45);
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
        _makeStraightLine(points, 0, -10, 4, 0, 10);
        _makeStraightLine(points, 10, -10, 1, 4, 10);
        // _addRandomPoints(points, 4, XSCALE_MIN, XSCALE_MAX);
        _addUniqueRandomPoints(points, 20, XSCALE_MIN, XSCALE_MAX);
        // StdRandom.shuffle(points);
        
        FastCollinearPoints fcp = new FastCollinearPoints(points);
        StdOut.println("total segments: " + fcp.numberOfSegments());

        // draw lines
        StdDraw.setPenColor(StdDraw.BLUE);
        int r = 0, g = 100, b = 0;
        for(LineSegment ls : fcp.segments()){
            StdOut.println(ls);
            StdDraw.setPenColor(r, g, b);
            b += 20;
            if (b>255) b= 0;
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