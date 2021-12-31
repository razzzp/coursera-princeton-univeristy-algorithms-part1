import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        if (args.length != 1) return;

        int k =Integer.parseInt(args[0]);
        RandomizedQueue<String> rndQueue = new RandomizedQueue<>();

        while (!StdIn.isEmpty()){
            rndQueue.enqueue(StdIn.readString());
        }

        for (int i = 0; i < k; i++){
            StdOut.println(rndQueue.dequeue());
        }
    }
}
