import java.io.IOException;
import java.util.Random;

public class Main {
    private static final int N = 8;                         // number of nodes
    private static final int W = 4;                         // number of channels
    private static final int S = 100000;                    // number of slots

    private static Random rand = new Random();              // random number generator

    public static void main(String[] args) throws IOException {
        // system configuration

        for (int slot=1; slot<=S; slot++){
            // run the slot transmission algorithm on every node (1 node->1 thread)
            // make the transmission
            // wait all threads to finish
        }
    }

    public static int getNumberOfSlots(){
        return S;
    }


}
