import java.util.*;

public class Node {
    private LinkedList<Packet> queue;               // the node's buffer
    private Set<Integer> T;                         // channels the node can transmit to
    private Set<Integer> R;                         // channels the node can receive from
    private ArrayList<ArrayList<Integer>> A;
    private ArrayList<ArrayList<Integer>> B;
    private Random rand;                            // random number generator
    private long slotDuration;                      // timeslot duration
    private int bufferSize;
    private double l;                               // packet generation probability
    private double[] d;                             // destination probabilities
    private int id;

    public Node(int id, int configuration, long seed, int bufferSize, double systemLoad){
        queue = new LinkedList<>();
        T = new HashSet<>();
        R = new HashSet<>();
        rand = new Random(seed);
        this.slotDuration = slotDuration;
        this.bufferSize = bufferSize;
        this.id = id;
        l = id * systemLoad / 36;
        d = new double[Main.getNumberOfNodes()];
        int N = Main.getNumberOfNodes();
        for (int m=0; m<N; m++){
            if (m==id){
                d[m] = 0;
            } else {
                d[m] = m / (N * (N+1) / (2-id));
            }

        }
        configure(id, configuration);
    }

    public void setA(ArrayList<ArrayList<Integer>> A) {
        this.A = A;
    }

    public void setB(ArrayList<ArrayList<Integer>> B) {
        this.B = B;
    }

    public Set<Integer> getT() {
        return T;
    }

    public Set<Integer> getR() {
        return R;
    }

    private void configure(int id, int configuration){
        // Configure Node's transmission range and receivers.
        switch (configuration){
            case 1:
                // system configuration 1
                //   each transmitter can be tuned to two wavelengths
                //   each node has two receivers

                // configure the transmission range
                if (id==0 || id==1 || id==2){
                    T.add(0);
                    T.add(1);
                } else if (id==3 || id==4 || id==5){
                    T.add(1);
                    T.add(2);
                } else if (id==6 || id==7){
                    T.add(2);
                    T.add(3);
                }

                // configure the receivers
                if (id==0){
                    R.add(1);
                    R.add(2);
                } else if (id==1 || id==2 || id==4 || id==6){
                    R.add(1);
                    R.add(3);
                } else if (id==3 || id==5 || id==7){
                    R.add(0);
                    R.add(2);
                }
                break;
            case 2:
                // system configuration 2
                //   each transmitter can be tuned to one wavelength
                //   each node has four receivers, one for each wavelength

                // configure the transmission range
                if (id==0 || id==1){
                    T.add(0);
                } else if (id==2 || id==3){
                    T.add(1);
                } else if (id==4 || id ==5){
                    T.add(2);
                } else if (id==6 || id==7){
                    T.add(3);
                }

                // configure the receivers
                R.add(0);
                R.add(1);
                R.add(2);
                R.add(3);
                break;
            case 3:
                // system configuration 3
                //   each transmitter can be tuned to all four wavelengths
                //   each node has one receiver

                // configure the transmission range
                T.add(0);
                T.add(1);
                T.add(2);
                T.add(3);

                // configure the receivers
                if (id==0 || id==1){
                    R.add(0);
                } else if (id==2 || id==3){
                    R.add(2);
                } else if (id==4 || id==5){
                    R.add(3);
                } else if (id==6 || id==7){
                    R.add(3);
                }
                break;
        }
    }

    private void slotAction(){

        ////////////////////
        // PACKET ARRIVAL //
        ////////////////////
        boolean arrives = rand.nextDouble() < l;
        if (arrives && queue.size() < bufferSize){
            int destination = findDestination();
            queue.add(new Packet(destination));
        }
        //////////////////////////
        // Creating trans table //
        //////////////////////////

        // Initialize the trans table
        int[] trans = new int[Main.getNumberOfNodes()];
        for (int i = 0; i < trans.length; i++) {
            trans[i] = -1;
        }

        // initialize channels ( Ω )
        ArrayList<Integer> channels = new ArrayList<>();
        for (int channel = 0; channel < Main.getNumberOfChannels(); channel++) {
            channels.add(channel);
        }

        // get a copy of A
        ArrayList<ArrayList<Integer>> _A = new ArrayList<>();
        for (int i = 0; i < A.size(); i++) {
            _A.add((ArrayList<Integer>) A.get(i).clone());
        }

        // create trans table
        while (!channels.isEmpty()) {
            int k = channels.get(rand.nextInt(channels.size()));        // get a random channel, say channel k
            int i = _A.get(k).get(rand.nextInt(_A.get(k).size()));      // get a random node from _A[k], say node i
            trans[i] = k;

            // remove i from _A
            for (int j = 0; j < _A.size(); j++) {
                _A.get(j).remove((Integer) i);
            }

            // remove k from channels (Ω)
            channels.remove((Integer) k);
        }

        //////////////////
        // TRANSMISSION //
        //////////////////


        if (trans[id] != -1) {
            int channel = trans[id];
            for (Packet packet : queue) {
                // an o komvos tou destination tou <packet> exei receiver sto <channel> kane to transmission
                int destination = packet.destination;
                if (B.get(channel).contains(destination)){
                    // do the transmission
                    queue.remove(packet);
                    break;
                }
            }
        }

    }

    private int findDestination(){
        int destination;
        double p = Math.random();
        double cumulativeProbability = 0.0;
        for (int m=0; m<d.length; m++){
            cumulativeProbability += d[m];
            if (p <= cumulativeProbability){
                return m;
            }
        }
        return -1;
    }
}
