import java.io.IOException;
import java.util.*;

public class Main {
    private static final int numberOfNodes = 8;
    private static final int numberOfChannels = 4;
    private static final int numberOfSlots = 100000;

    private static Random rand = new Random();              // random number generator

    public static void main(String[] args) throws IOException {
        // choose configuration
        //   1: transmitter can be tuned to 2 channels, 2 receivers
        //   2: transmitter can be tuned to 1 channel,  4 receivers
        //   3: transmitter can be tuned to 4 channels, 1 receiver
        int configuration = 1;

        // create nodes
        Node[] nodes = new Node[numberOfNodes];
        for (int i=0; i<nodes.length; i++){
            nodes[i] = new Node(id(i),configuration);
        }
        // A and B are Lists of Sets
        // A[i] is the set of nodes that can transmit to channel i
        // B[i] is the set of nodes that can receive from channel i
        List<Set<Integer>> A = new ArrayList<>();
        List<Set<Integer>> B = new ArrayList<>();

        // Initialize A and B
        for (int i=0; i<numberOfChannels; i++){
            A.add(new HashSet<>());
            B.add(new HashSet<>());
        }

        // find A and B
        for (int i=0; i<A.size(); i++){
            for (int j=0; j<nodes.length; j++){
                if (nodes[j].getT().contains(id(i))){
                    A.get(i).add(id(j));
                }
                if (nodes[j].getR().contains(id(i))){
                    B.get(i).add(id(j));
                }
            }
        }

//        // print A and B
//        System.out.println("A: " + A);
//        System.out.println("B: " + B);


        // start the simulation
        for (int slot=1; slot<=numberOfSlots; slot++){
            // start all threads
            // wait all threads to finish
        }
    }

    public static int getNumberOfSlots(){
        return numberOfSlots;
    }
    public static int getNumberOfChannels() { return numberOfChannels; }
    private static int id(int pos){
        // Node IDs are numbered starting from 1
        // Arrays indexing starts from 0
        // Given an array index, return the relevant ID
        return pos + 1;
    }

}
