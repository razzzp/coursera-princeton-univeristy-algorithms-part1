import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        String curChamp = "";
        int count = 0;
        while (!StdIn.isEmpty()) {      
            String cuWord = StdIn.readString();
            count++;
            // StdOut.println(1d/(double)count);
            if (StdRandom.bernoulli(1d/(double)count)) {
                curChamp = cuWord;
            }
        }
        StdOut.println(curChamp);
    }
}
