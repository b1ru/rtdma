import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    private static final int numberOfNodes = 8;
    private static final int numberOfChannels = 4;
    private static final int numberOfSlots = 100000;
    private static Node[] nodes;
    private static boolean validation;
    private static FileWriter out1;
    private static FileWriter out2;

    public static void main(String[] args) throws IOException {
        // choose configuration
        //   1: transmitter can be tuned to 2 channels, 2 receivers
        //   2: transmitter can be tuned to 1 channel,  4 receivers
        //   3: transmitter can be tuned to 4 channels, 1 receiver
        int configuration = 2;
        //long seed = new Random().nextLong();
        long seed = 5;

        // create nodes
        nodes = new Node[numberOfNodes + 1];
        for (int i = 1; i <= numberOfNodes; i++) {
            nodes[i] = new Node(i, configuration, seed);
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


        if (configuration==1){
            out1 = new FileWriter("validation1.csv");
            out2 = new FileWriter("performance1.csv");
        } else if (configuration==2){
            out1 = new FileWriter("validation2.csv");
            out2 = new FileWriter("performance2.csv");
        } else if(configuration==3) {
            out1 = new FileWriter("validation3.csv");
            out2 = new FileWriter("performance3.csv");
        }
        out1.write("TP,Q,D\n");
        out2.write("TP,D\n");

        validation=true;
        for (int i = 1; i <= numberOfNodes; i++) {
            nodes[i].setD(validation);
            nodes[i].setBufferSize(validation);
        }
        simulate();

        validation=false;
        for (int i = 1; i <= numberOfNodes; i++) {
            nodes[i].setD(validation);
            nodes[i].setBufferSize(validation);
        }
        simulate();

        out1.close();
        out2.close();
    }

    private static void simulate() throws IOException {
        double from, to, step;
        if (validation){
            from = 0.2;
            to = 1;
            step = 0.2;
            System.out.println("##############\n# VALIDATION #\n##############");
        } else {
            from = 0.5;
            to = 8;
            step = 0.5;
            System.out.println("###############\n# PERFORMANCE #\n###############");
        }
        System.out.println(from + " ≤ b ≤ " + to);

        for (double b=from ; b<=to; b+=step) {
            System.out.printf("b=%.1f\n",b);

            // reset nodes for each b
            for (int i=1; i <= numberOfNodes; i++){
                nodes[i].reset(b);
            }

            // actual simulation
            for (int i = 0; i < numberOfSlots; i++) {
                for (int j = 1; j <= numberOfNodes; j++) {
                    nodes[j].slotAction(i);
                }
            }

            // get stats
            double systemThroughput = 0;
            double averageDelay = 0;
            for (int i = 1; i <= numberOfNodes; i++) {
                double nodeThroughput = (double) nodes[i].getTransmitted() / numberOfSlots;
                systemThroughput += nodeThroughput;

                double nodeDelay = (double) nodes[i].getSlotsWaited() / nodes[i].getTransmitted();
                averageDelay += nodeDelay;

                double nodeBuffered = (double) nodes[i].getBuffered() / numberOfSlots;

                if (validation){
                    out1.write(String.valueOf(nodeThroughput));
                    out1.write(',');
                    out1.write(String.valueOf(nodeBuffered));
                    out1.write(',');
                    out1.write(String.valueOf(nodeDelay));
                    out1.write('\n');
                }
            }
            averageDelay /= numberOfNodes;
            if (!validation){
                out2.write(String.valueOf(systemThroughput));
                out2.write(',');
                out2.write(String.valueOf(averageDelay));
                out2.write('\n');
            }
        }
    }

    public static int getNumberOfNodes(){
        return numberOfNodes;
    }

    public static int getNumberOfChannels() { return numberOfChannels; }

    public static boolean getValidation(){ return validation; };
}
