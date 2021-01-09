import java.io.IOException;
import java.util.*;

public class Main {
    private static final int numberOfNodes = 8;
    private static final int numberOfChannels = 4;
    private static final int numberOfSlots = 100000;
    private static long slotDuration = 0;

    public static void main(String[] args){
        // choose configuration
        //   1: transmitter can be tuned to 2 channels, 2 receivers
        //   2: transmitter can be tuned to 1 channel,  4 receivers
        //   3: transmitter can be tuned to 4 channels, 1 receiver
        int configuration = 2;
        long seed = new Random().nextLong();

        // create nodes
        Node[] nodes = new Node[numberOfNodes + 1];
        for (int i = 1; i <= numberOfNodes; i++) {
            nodes[i] = new Node(i, configuration, seed, i);
        }

        // create A and B
        // A[i] is the List of nodes that can transmit to channel i
        // B[i] is the List of nodes that can receive from channel i
        ArrayList<ArrayList<Integer>> A = new ArrayList<>();
        ArrayList<ArrayList<Integer>> B = new ArrayList<>();

        // Initialize A and B
        for (int i = 0; i <= numberOfChannels; i++) {
            A.add(new ArrayList<>());
            B.add(new ArrayList<>());
        }

        // find A and B
        for (int i = 1; i <= numberOfChannels; i++) {
            for (int j = 1; j <= numberOfNodes; j++) {
                if (nodes[j].getT().contains(i)) {
                    A.get(i).add(j);
                }
                if (nodes[j].getR().contains(i)) {
                    B.get(i).add(j);
                }
            }
        }

        // pass A and B to all Nodes
        for (int i = 1; i <= numberOfNodes; i++) {
            nodes[i].setA(A);
            nodes[i].setB(B);
        }

        // start the simulation
        for (double b=0.2 ; b<=1; b+=0.2) {
            System.out.println("---------------- b = " + b + " --------------------");

            for (int i=1; i <= numberOfNodes; i++){
                nodes[i].changeSystemLoad(b);
            }

            for (int i = 0; i < numberOfSlots; i++) {
                for (int j = 1; j <= numberOfNodes; j++) {
                    nodes[j].slotAction(i);
                }
            }

            double systemThroughput = 0;
            for (int i = 1; i <= numberOfNodes; i++) {
                double nodeThroughput = (double) nodes[i].getTransmitted() / numberOfSlots;
                System.out.println("Throughput of node " + i + ": " + nodeThroughput);
                systemThroughput += nodeThroughput;
                System.out.println("Buffered packets on average of node " + i + ": " +
                        ((double) nodes[i].getBuffered() / numberOfSlots));
                System.out.println("Average packet delay of node " + i + ": " +
                        (double) nodes[i].getSlotsWaited() / nodes[i].getTransmitted());
            }
            System.out.println("---------------------------------------------");
            System.out.println("System's throughput: " + systemThroughput);
        }
    }

    public static int getNumberOfNodes(){
        return numberOfNodes;
    }

    public static int getNumberOfChannels() { return numberOfChannels; }

    public static int getNumberOfSlots() { return numberOfSlots; }
}
