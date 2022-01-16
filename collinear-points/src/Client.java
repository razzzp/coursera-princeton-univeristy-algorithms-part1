import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

public class Client {
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
    
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.setPenRadius(0.003);
    
        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        // BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        StdDraw.setPenColor(StdDraw.BLUE);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            if (segment != null) segment.draw();
        }
        StdDraw.show();

        StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(StdDraw.RED);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
    }
}
