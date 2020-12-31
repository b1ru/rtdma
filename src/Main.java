import java.io.IOException;
import java.util.*;

public class Main {
    private static final int numberOfNodes = 8;
    private static final int numberOfChannels = 4;
    private static final int numberOfSlots = 100000;
    private static long slotDuration = 0;

    public static void main(String[] args) throws InterruptedException {
        simulation();   // run simulation one time to find the slot duration
        simulation();   // run simulation a second time, for the actual simulation
    }

    public static int getNumberOfNodes(){
        return numberOfNodes;
    }

    public static int getNumberOfChannels() { return numberOfChannels; }

    public static int getNumberOfSlots() { return numberOfSlots; }

    public static void setSlotDuration(long slotDuration) {
        Main.slotDuration = slotDuration;
    }

    public static long getSlotDuration(){ return slotDuration; }

    private static void simulation() throws InterruptedException {
        // choose configuration
        //   1: transmitter can be tuned to 2 channels, 2 receivers
        //   2: transmitter can be tuned to 1 channel,  4 receivers
        //   3: transmitter can be tuned to 4 channels, 1 receiver
        int configuration = 1;

        // create nodes
        Node[] nodes = new Node[numberOfNodes];
        for (int i=0; i<nodes.length; i++){
            nodes[i] = new Node(i, configuration, 5, slotDuration);
        }
        // A and B are Lists of Lists
        // A[i] is the List of nodes that can transmit to channel i
        // B[i] is the List of nodes that can receive from channel i
        ArrayList<ArrayList<Integer>> A = new ArrayList<>();
        ArrayList<ArrayList<Integer>> B = new ArrayList<>();

        // Initialize A and B
        for (int i=0; i<numberOfChannels; i++){
            A.add(new ArrayList<>());
            B.add(new ArrayList<>());
        }

        // find A and B
        for (int i=0; i<A.size(); i++){
            for (int j=0; j<nodes.length; j++){
                if (nodes[j].getT().contains(i)){
                    A.get(i).add(j);
                }
                if (nodes[j].getR().contains(i)){
                    B.get(i).add(j);
                }
            }
        }

        // pass A and B to all Nodes
        for (int i=0; i<nodes.length; i++){
            nodes[i].setA(A);
            nodes[i].setB(B);
        }

        // start all threads
        for (int i=0; i<nodes.length; i++){
            nodes[i].start();
        }
        // wait all threads to finish
        for (int i=0; i<nodes.length; i++){
            nodes[i].join();
        }
    }
}
