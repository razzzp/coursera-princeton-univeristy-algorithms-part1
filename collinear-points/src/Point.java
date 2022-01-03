/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param  x the <em>x</em>-coordinate of the point
     * @param  y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        if (this.x == that.x && this.y == that.y) return Double.NEGATIVE_INFINITY;
        if (this.x == that.x) return Double.POSITIVE_INFINITY;

        return (double)(that.y - this.y) / (double)(that.x - this.x);
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param  that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Point that) {
        int xdiff = this.x - that.x;
        int ydiff = this.y - that.x;

        if (ydiff == 0) return xdiff;
        else return ydiff;

        // if (this.y == that.y && this.x == that.x) return 0;
        // else if (this.y == that.y && this.x < that.x) return -1;
        // else if (this.y < that.y) return -1;
        // else return 1;
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return new PointComparator(this);
    }

    private class PointComparator implements Comparator<Point>{
        Point refPoint;

        public PointComparator(Point p) {
            refPoint = p;
        }

        @Override
        public int compare(Point p1, Point p2) {
            double p1Slope = refPoint.slopeTo(p1);
            double p2Slope = refPoint.slopeTo(p2);

            if (p1Slope == p2Slope) return 0;
            else if (p1Slope < p2Slope) return -1;
            else return 1;
        }
        
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    private static Point[] _makeSquarePoints(int width) {
        Point[] result = new Point[4];
        result[0] = new Point(width, width);
        result[1] = new Point(-width, width);
        result[2] = new Point(-width, -width);
        result[3] = new Point(width, -width);
        return result;
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        final Point p0 = new Point(0, 0);
        Point[] squarePoints1 = _makeSquarePoints(1);
        Point[] squarePoints10 = _makeSquarePoints(10);
        Comparator<Point> p0Comparator = p0.slopeOrder();

        StdOut.println("horizontal slope:");
        StdOut.println(squarePoints10[0].toString() + squarePoints10[1].toString() + " = " + squarePoints10[0].slopeTo(squarePoints10[1]));

        
        StdOut.println("vertical slope:");
        StdOut.println(squarePoints10[1].toString() + squarePoints10[2].toString() + " = " + squarePoints10[1].slopeTo(squarePoints10[2]));

        StdOut.println("degenerate slope:");
        StdOut.println(squarePoints10[3].toString() + squarePoints10[3].toString() + " = " + squarePoints10[3].slopeTo(squarePoints10[3]));

        StdOut.println("origin slopes 1:");
        for (Point point : squarePoints1) {    
            StdOut.println(point.toString() + " = " + p0.slopeTo(point));
        }

        StdOut.println("origin slopes 10:");
        for (Point point : squarePoints10) {    
            StdOut.println(point.toString() + " = " + p0.slopeTo(point));
        }

        StdOut.println("decimal slope:");
        Point p912 = new Point(9, 12);
        StdOut.println(p912.toString() + " = " + p0.slopeTo(p912));

        StdOut.println("compare equal decimal slope:");
        Point p1823 = new Point(18, 24);
        StdOut.println(p912.toString() +  p1823.toString() + " = " + p0Comparator.compare(p912,p1823));

        StdOut.println("compare slopes 10:");
        for (int i=0;i<squarePoints10.length;i++) {  
            Point curPoint = squarePoints10[i];
            Point prevPoint;
            if (i ==0) prevPoint = squarePoints10[3];
            else prevPoint = squarePoints10[i-1];
            StdOut.println(prevPoint.toString() + curPoint.toString() + " = " + p0Comparator.compare(prevPoint, curPoint));
        }

        StdOut.println("compare equal int slope:");
        StdOut.println(squarePoints10[0].toString() + squarePoints10[2].toString() + " = " + p0Comparator.compare(squarePoints10[0], squarePoints10[2]));
    }
}