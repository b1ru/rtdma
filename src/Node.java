import java.util.*;

public class Node {
    private LinkedList<Packet> queue;               // the node's buffer
    private Set<Integer> T;                         // channels the node can transmit to
    private Set<Integer> R;                         // channels the node can receive from
    public ArrayList<ArrayList<Integer>> A;
    public ArrayList<ArrayList<Integer>> B;
    private Random rand;                            // random number generator
    private int bufferSize;                         // node's capacity of packets
    private double l;                               // packet generation probability
    private double[] d;                             // destination probabilities
    private int id;
    private int transmitted = 0;
    private int buffered = 0;
    private int slotsWaited = 0;

    public Node(int id, int configuration, long seed, int bufferSize){
        queue = new LinkedList<>();
        T = new HashSet<>();
        R = new HashSet<>();
        rand = new Random(seed);
        this.bufferSize = bufferSize;
        this.id = id;
        int N = Main.getNumberOfNodes();
        d = new double[N+1];
        for (int m=1; m<=N; m++){
            if (m==id){
                d[m] = 0;
            } else {
                d[m] = (double) m / (N*(N+1)/2 - id);
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
                if (id==1 || id==2 || id==3){
                    T.add(1);
                    T.add(2);
                } else if (id==4 || id==5 || id==6){
                    T.add(2);
                    T.add(3);
                } else if (id==7 || id==8){
                    T.add(3);
                    T.add(4);
                }

                // configure the receivers
                if (id==1){
                    R.add(2);
                    R.add(3);
                } else if (id==2 || id==3 || id==5 || id==7){
                    R.add(2);
                    R.add(4);
                } else if (id==4 || id==6 || id==8){
                    R.add(1);
                    R.add(3);
                }
                break;
            case 2:
                // system configuration 2
                //   each transmitter can be tuned to one wavelength
                //   each node has four receivers, one for each wavelength

                // configure the transmission range
                if (id==1 || id==2){
                    T.add(1);
                } else if (id==3 || id==4){
                    T.add(2);
                } else if (id==5 || id ==6){
                    T.add(3);
                } else if (id==7 || id==8){
                    T.add(4);
                }

                // configure the receivers
                R.add(1);
                R.add(2);
                R.add(3);
                R.add(4);
                break;
            case 3:
                // system configuration 3
                //   each transmitter can be tuned to all four wavelengths
                //   each node has one receiver

                // configure the transmission range
                T.add(1);
                T.add(2);
                T.add(3);
                T.add(4);

                // configure the receivers
                if (id==1 || id==2){
                    R.add(1);
                } else if (id==3 || id==4){
                    R.add(2);
                } else if (id==5 || id==6){
                    R.add(3);
                } else if (id==7 || id==8){
                    R.add(4);
                }
                break;
        }
    }

    public void slotAction(int slot){
        ////////////////////
        // PACKET ARRIVAL //
        ////////////////////
        boolean arrives = rand.nextDouble() < l;
        if (arrives && queue.size() < bufferSize){
            int destination = findDestination();
            if (destination==-1){
                System.exit(5);
            }
            queue.add(new Packet(destination, slot));
            //System.out.println("+");
        } else if (arrives && queue.size() == bufferSize){
            //System.out.println("-");
        }
        //////////////////////////
        // Creating trans table //
        //////////////////////////

        // Initialize the trans table
        int[] trans = new int[Main.getNumberOfNodes() + 1];
        for (int i = 1; i <= Main.getNumberOfNodes(); i++) {
            trans[i] = 0;
        }

        // initialize channels ( Ω )
        ArrayList<Integer> channels = new ArrayList<>();
        for (int channel = 1; channel <= Main.getNumberOfChannels(); channel++) {
            channels.add(channel);
        }

        // get a copy of A
        ArrayList<ArrayList<Integer>> _A = new ArrayList<>();
        for (int i = 0; i < A.size(); i++) {
            _A.add((ArrayList<Integer>) A.get(i).clone());
        }
//        System.out.println("A=" + A);
//        System.out.println("_A="+_A);
        // create trans table
        while (!channels.isEmpty()) {
            int k = channels.get(rand.nextInt(channels.size()));        // get a random channel, say channel k
            int i = _A.get(k).get(rand.nextInt(_A.get(k).size()));      // get a random node from _A[k], say node i
            trans[i] = k;

            // remove i from _A
            for (int j = 1; j < _A.size(); j++) {
                _A.get(j).remove((Integer) i);
            }

            // remove k from channels (Ω)
            channels.remove((Integer) k);
        }


//        System.out.print("d = [");
//        for (int i=1; i < d.length ; i++){
//            System.out.print(d[i] + " ");
//        }
//        System.out.println("]");

        buffered += queue.size();
        //////////////////
        // TRANSMISSION //
        //////////////////

//        // print trans table
//        System.out.print("trans=[");
//        for (int i=0; i<trans.length; i++){
//            System.out.print(trans[i]);
//            System.out.print(" ");
//        }
        //System.out.println("]");
        if (trans[id] != 0) {

            int channel = trans[id];
            for (Packet packet : queue) {
                // an o komvos tou destination tou <packet> exei receiver sto <channel> kane to transmission
                int destination = packet.destination;
                if (B.get(channel).contains(destination)){
                    // do the transmission
                    slotsWaited += slot - packet.timeslot;
                    queue.remove(packet);
                    transmitted++;
                    break;
                }
            }
        }

    }

    private int findDestination() {
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

    public int getTransmitted(){
        return transmitted;
    }

    public int getBuffered(){ return buffered; }

    public int getSlotsWaited(){ return slotsWaited; }

    public void changeSystemLoad(double systemLoad){
        l = id * systemLoad / 36;
        transmitted = 0;
        buffered = 0;
        slotsWaited = 0;
        queue.clear();
    }
}
