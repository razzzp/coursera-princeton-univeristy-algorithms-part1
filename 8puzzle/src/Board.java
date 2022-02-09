import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Board {
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    private int n, hamming, manhatan;
    private int[] tiles;
    private ArrayList<Board> neighbours;

    public Board(int[][] tiles){
        this.n = tiles.length;
        this.tiles = new int[n*n];
        for(int i=0;i<n*n;i++){
            this.tiles[i] = tiles[getRow(i)][getColumn(i)];
        }
        calculateHammingAndManhattan();
    }

    private Board(Board b, int i1, int i2){
        // ctor for copying tiles and swapping values at i1, & i2
        this.n = b.n;
        this.tiles = Arrays.copyOf(b.tiles, b.tiles.length);
        this.tiles[i1] = b.tiles[i2];
        this.tiles[i2] = b.tiles[i1];
        calculateHammingAndManhattan();
    }


    private void calculateHammingAndManhattan(){
        this.hamming = calculateHamming();
        this.manhatan = calculateManhattan();
    }

    private int getRow(int index){
        return index/n;
    }
    
    private int getColumn(int index){
        return index%n;
    }
                                           
    // string representation of this board
    public String toString(){
        StringBuilder result = new StringBuilder();
        result.append(n);
        result.append("\n");
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                result.append(" ");
                result.append(tiles[i*n+j]);
            }
            result.append("\n");
        }
        return result.toString();
    }

    // board dimension n
    public int dimension(){
        return n;
    }

    // number of tiles out of place
    public int hamming(){
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan(){
        return manhatan;
    }

    private int getGoalValueForIndex(int i){
        // last index is 0
        if(i == n*n -1) return 0;
        // all others
        else return i+1;
    }

    // number of tiles out of place
    private int calculateHamming(){
        int diffCount = 0;
        // skip last index
        for(int i=0;i<n*n-1;i++){
            if(tiles[i] != getGoalValueForIndex(i)) diffCount++;
        }
        return diffCount;
    }

    private int getGoalRow(int value){
        // returns Row the value is supposed to be in, 0-based
        if (value == 0) return n-1;
        else return (value-1)/n;
    }
    private int getGoalColumn(int value){
        // returns Column the value is supposed to be in, 0-based
        if (value == 0) return n-1;
        else return (value-1)%n;
    }

    // sum of Manhattan distances between tiles and goal
    private int calculateManhattan(){
        int result = 0;
        // all 0-based index
        int curRow, curColumn, curGoalRow, curGoalColumn, curValue;
        for(int i=0;i<n*n;i++){
            curValue = tiles[i];
            // no manhattan for 0
            if (curValue == 0) continue;
            curRow = getRow(i);
            curColumn = getColumn(i);
            curGoalRow = getGoalRow(curValue);
            curGoalColumn = getGoalColumn(curValue);
            // add current manhatan distance
            result += Math.abs(curGoalRow-curRow) + Math.abs(curGoalColumn-curColumn);
        }
        return result;
    }

    // is this board the goal board?
    public boolean isGoal(){
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y){
        // is this ok? can't access y tiles to compare, zz
        if (y== null) return false;
        return (y instanceof Board)
         && (this.toString().equals(y.toString()));
    }

    private int getEmptySlotIndex(){
        for(int i=0;i<n*n;i++){
            if(tiles[i] == 0) return i;
        }
        return -1;
    }

    private boolean isRowColOk(int row, int col){
        if(
            row < 0 || row >= n
            || col < 0 || col >= n
        ) return false;
        else return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors(){
        if (neighbours == null){
            neighbours = new ArrayList<>();
            int emptyIdx = getEmptySlotIndex();
            int row = getRow(emptyIdx);
            int col = getColumn(emptyIdx);

            // left neighbour
            int nrow = row;
            int ncol = col-1;
            if (isRowColOk(nrow, ncol)){
                // get index of neighbouring empty idx
                int nEmptyIdx = nrow * n + ncol;
                // append new board with current empty and neighbouring empty swapped
                neighbours.add(new Board(this, emptyIdx, nEmptyIdx));
            }

            // right neighbour
            nrow = row;
            ncol = col+1;
            if (isRowColOk(nrow, ncol)){
                int nEmptyIdx = nrow * n + ncol;
                neighbours.add(new Board(this, emptyIdx, nEmptyIdx));
            }

            
            // top neighbour
            nrow = row-1;
            ncol = col;
            if (isRowColOk(nrow, ncol)){
                int nEmptyIdx = nrow * n + ncol;
                neighbours.add(new Board(this, emptyIdx, nEmptyIdx));
            }

            // bottom neighbour
            nrow = row+1;
            ncol = col;
            if (isRowColOk(nrow, ncol)){
                int nEmptyIdx = nrow * n + ncol;
                neighbours.add(new Board(this, emptyIdx, nEmptyIdx));
            }
        }
        return neighbours;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin(){
        // need to be consistent
        // don't use random, get first 2 non-0 tiles
        int rIdx1 = 0;
        while(tiles[rIdx1] == 0) rIdx1++;
        int rIdx2 = 0;
        while (rIdx2 <= rIdx1 || tiles[rIdx2] == 0 ) rIdx2++;

        return new Board(this, rIdx1, rIdx2);
    }
    
    private static int[] generateGoalArray(int n){
        int[] result = new int[n*n];
        for(int i=0;i<n*n;i++){
            if(i == n*n-1) result[i]= 0;
            else result[i] = i+1;
        }
        return result;
    }

    private static int[][] generate2DTilesFrom1D(int n,int[] flatTiles){
        int[][] result = new int[n][n];
        for(int i=0;i<n*n;i++){
            result[i/n][i%n] = flatTiles[i];
        }
        return result;
    }

    private static int[][] generateRandomTiles(int n){
        int[] rndArr = generateGoalArray(n);
        StdRandom.shuffle(rndArr);
        return generate2DTilesFrom1D(n, rndArr);
    }

    private static int[][] generateGoalTiles(int n){
        int[] orderedArr = generateGoalArray(n);
        return generate2DTilesFrom1D(n, orderedArr);
    }

    private static void printNeighbours(Board b){
        StdOut.println("neighbours");
        for (Board curBoard : b.neighbors()) {
            StdOut.print(curBoard);
        }
        StdOut.println("-----");
    }

    // unit testing (not graded)
    public static void main(String[] args){
        int n = 3;
        int[][] tiles = generateRandomTiles(n);
        int[][] tilesTestManhattan = {{6,2,7},{8,5,1},{4,3,0}};
        int[][] tiles2 = {{3,2},{1,0}};

        Board board;

        board = new Board(tilesTestManhattan);
        StdOut.print(board);
        // test hamming goal
        StdOut.printf("hamming: %d\n", board.hamming());
        // test manhattan
        StdOut.printf("manhattan: %d\n", board.manhattan()); 
        // neighbours
        printNeighbours(board);

        board= new Board(tiles);
        StdOut.print(board);
        // test hamming
        StdOut.printf("hamming: %d\n", board.hamming());
        // test manhattan
        StdOut.printf("manhattan: %d\n", board.manhattan());
        printNeighbours(board);

        Board goalBoard = new Board(generateGoalTiles(n));
        StdOut.print(goalBoard);
        // test hamming goal
        StdOut.printf("hamming: %d\n", goalBoard.hamming());
        // test manhattan
        StdOut.printf("manhattan: %d\n", goalBoard.manhattan()); 
        printNeighbours(goalBoard);

        board = new Board(tiles2);
        Board board2 = new Board(tiles2);
        StdOut.println(board.equals(board2));

        tiles = generateRandomTiles(n);
        board = new Board(tiles);
        // test twin
        StdOut.println("test twin");
        StdOut.println(board);
        for(int i=0;i<10;i++){
            StdOut.printf("iter %d\n", i);
            Board twinBoard = board.twin();
            StdOut.print(twinBoard);
        }
    }
}
