import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.MinPQ;

public class Solver {
    // find a solution to the initial board (using the A* algorithm)
    private Board initialBoard;
    private List<Board> solution;

    public Solver(Board initial){
        if (initial == null) throw new IllegalArgumentException("board cannot be null");
        initialBoard = initial;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable(){
        return moves() != -1;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves(){
        if (solution() == null) return -1;
        else {
            // exclude initial board
            return solution.size()-1;
        }
    }

    private boolean hasBoardExisted(Board b, Node inNode){
        Node curNode = inNode;
        while(curNode != null){
            if (curNode.board.equals(b)) return true;
            curNode= curNode.prevNode;
        }
        return false;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution(){
        if (solution != null) return solution;

        Comparator<Node> comp = new ManhattanComparator();
        MinPQ<Node> pQueue = new MinPQ<Node>(comp);
        MinPQ<Node> pQueueTwin = new MinPQ<Node>(comp);
        Board twinBoard = initialBoard.twin();
        
        // enqueue initial node
        pQueue.insert(new Node(initialBoard, null, 0));
        pQueueTwin.insert(new Node(twinBoard, null, 0));
        //
        Node curNode = pQueue.delMin();
        Node curNodeTwin = pQueueTwin.delMin();
        while(!curNode.board.isGoal()){
            // twin solved means given board is unsolveable
            if (curNodeTwin.board.isGoal()) 
                return null;

            // enqueue neighbours
            for (Board nBoard : curNode.board.neighbors()) {
                // only insert to PQ, if board never seen before
                if (!hasBoardExisted(nBoard, curNode))
                    pQueue.insert(new Node(nBoard, curNode, curNode.move+1));
            }

            // enqueue neighbours twin
            for (Board nBoard : curNodeTwin.board.neighbors()) {
                // only insert to PQ, if board never seen before
                if (!hasBoardExisted(nBoard, curNodeTwin))
                    pQueueTwin.insert(new Node(nBoard, curNodeTwin, curNodeTwin.move+1));
            }

            // pop next min
            curNode = pQueue.delMin();
            curNodeTwin = pQueueTwin.delMin();
        }
        // stack to reverse order of moveds
        solution = new Stack<Board>();
        // curNode has the solved board
        while(curNode != null){
            solution.add(curNode.board);
            curNode = curNode.prevNode;
        }
        Collections.reverse(solution);

        return solution;
    }

    private class Node{
        Board board;
        Node prevNode;
        int move;
        Node(Board board, Node prevNode, int move){
            this.board = board;
            this.prevNode = prevNode;
            this.move = move;
        }
    }

    private class ManhattanComparator implements Comparator<Node>{

        @Override
        public int compare(Node n1, Node n2) {
            return (n1.move + n1.board.manhattan()) - (n2.move + n2.board.manhattan());
        }
          
    }

    // test client (see below) 
    public static void main(String[] args){
        // solveable
        // int[][] tilesTestManhattan = {{0,1,3},{4,2,5},{7,8,6}};
        // unsolveable
        int[][] tilesTestManhattan = {{0,3,1},{4,2,5},{7,8,6}};
        // solveable
        // int[][] tilesTestManhattan = {{6,2,7},{8,5,1},{4,3,0}};
        // unsolveable
        // int[][] tilesTestManhattan = {{6,3,7},{8,5,1},{4,2,0}};
        // solved
        //int[][] tilesTestManhattan = {{1,2,3},{4,5,6},{7,8,0}};

        Board board;

        board = new Board(tilesTestManhattan);
        Solver solver = new Solver(board);

        if (solver.isSolvable()){
            StdOut.printf("Minimum number of moves = %d\n", solver.moves());
            for (Board b : solver.solution()) {
                StdOut.println(b);
            }
        } else {
            StdOut.printf("Unsolvevable puzzle");
        }
    }
}
