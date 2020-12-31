import java.util.*;

public class Node extends Thread {
    private LinkedList<Packet> queue;               // the node's buffer
    private Set<Integer> T;                         // channels the node can transmit to
    private Set<Integer> R;                         // channels the node can receive from
    private ArrayList<ArrayList<Integer>> A;
    private ArrayList<ArrayList<Integer>> B;
    private Random rand;                            // random number generator
    private long slotDuration;                      // timeslot duration

    public Node(int id, int configuration, long seed, long slotDuration){
        queue = new LinkedList<>();
        T = new HashSet<>();
        R = new HashSet<>();
        rand = new Random(seed);
        this.slotDuration = slotDuration;
        configure(id, configuration);
    }

    public void run(){
        if (slotDuration==0){           // find slot duration and set it to main
            long maxTime=0;
            double factor=2;          // timeslot duration = ceil(factor * maximum slot time)
            for (int slot=0; slot<Main.getNumberOfSlots(); slot++) {
                long startTime = System.currentTimeMillis();
                slotAction();
                long time = System.currentTimeMillis() - startTime;
                if (time > maxTime) {
                    maxTime = time;
                }
            }
            synchronized(this){
                if (maxTime*factor > Main.getSlotDuration()){
                    Main.setSlotDuration((long) Math.ceil(maxTime*factor));
                }
            }
        } else{                     // do the actual simulation
            for (int slot=0; slot<Main.getNumberOfSlots(); slot++){
                slotAction();
            }
        }
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
    }
}
