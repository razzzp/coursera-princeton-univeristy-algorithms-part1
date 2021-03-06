import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    
    private final int _width;
    private int _openSites = 0;
    private boolean[] _grid;
    private final WeightedQuickUnionUF _myUF;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <=0)
            throw new IllegalArgumentException("invalid width");
        _width = n;
        int _length = n*n;
        //
        _grid = new boolean[_length];
        //
        // create UF structure, add 2 extra points for virtual top & bottom points
        _myUF = new WeightedQuickUnionUF(_length);
        // connect first row together
        for(int i=0;i<n;i++){
            _myUF.union(0, i);
        }
        // connect bottom row together
        for(int i=(_width-1)*_width;i<_length;i++){
            _myUF.union(_length-1, i);
        }
    }

    private boolean _isValidPoint(int row, int col){
        if (row <= 0 || row > _width || col <= 0 || col > _width)
            return false;
        else
            return true;
    }

    // throws exception if coords not valid
    private void _validate(int row, int col){
        if (!_isValidPoint(row, col))
            throw new IllegalArgumentException("index out of range");
    }

    // get index for array
    private int _calculateIndex(int row, int col) {
        _validate(row,col);
        return (row-1)*_width + (col-1);
    }

    private void _printRowPoints(int row){
        StringBuilder s = new StringBuilder();
        for (int i=0;i<_width;i++){
            s.append('|');
            if (_grid[_calculateIndex(row, i+1)])
                s.append(' ');
            else
                s.append('X');
        }
        s.append('|');
        StdOut.println(s);
    }

    private void printGrid(){
        for(int i=0;i<_width;i++){
            // _printRowBorder();
            _printRowPoints(i+1);
        }
        // _printRowBorder();
        StdOut.printf("Number of sets: %d\n", _myUF.count());
        StdOut.printf("percolates: %b\nopen sites: %d\n", percolates(), numberOfOpenSites());
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        int index= _calculateIndex(row, col);
        if (_grid[index] != true){
            _grid[index] = true;
            _openSites++;
        }
            
        // union coresponding point in UF
        //  with top, right, bottom, left point
        //  if they are open
        // top
        if (_isValidPoint(row-1, col) && isOpen(row-1, col)) _myUF.union(_calculateIndex(row, col), _calculateIndex(row-1, col));    
        // right
        if (_isValidPoint(row, col+1) && isOpen(row, col+1)) _myUF.union(_calculateIndex(row, col), _calculateIndex(row, col+1));
        // bottom
        if (_isValidPoint(row+1, col) && isOpen(row+1, col)) _myUF.union(_calculateIndex(row, col), _calculateIndex(row+1, col));    
        // left
        if (_isValidPoint(row, col-1) && isOpen(row, col-1)) _myUF.union(_calculateIndex(row, col), _calculateIndex(row, col-1));    

        // StdOut.printf("Number of sets: %d\n", _uf.count());
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return _grid[_calculateIndex(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        // check is open to treat first and last row
        return isOpen(row, col) && _myUF.find(_calculateIndex(row, col)) == _myUF.find(0);
    }

    // returns the number of open sites
    public int numberOfOpenSites(){
        return _openSites;
    }

    // does the system percolate?
    public boolean percolates(){
        // just check last node, because entire bottom row is connected
        return _myUF.find(_calculateIndex(_width, _width)) == _myUF.find(0);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation p = new Percolation(5);
        p.printGrid();
        p.open(3, 3);
        p.open(1, 1);
        p.open(1, 2);
        p.open(2, 3);
        p.open(3, 4);
        p.open(4, 3);
        p.open(3, 2);
        p.open(5, 5);
        p.open(4, 4);
        p.open(4, 5);
        p.open(1, 3);
        p.printGrid();
        StdOut.printf("percolates: %b\nopen sites: %d\n", p.percolates(), p.numberOfOpenSites());
    }
}
