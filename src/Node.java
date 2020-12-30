import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Node extends Thread {
    private LinkedList<Packet> queue;               // the node's buffer
    private Set<Integer> T;                         // channels the node can transmit to
    private Set<Integer> R;                         // channels the node can receive from
    //private int id;                                 // the unique id of the node

    public Node(int id, int configuration){
        queue = new LinkedList<>();
        T = new HashSet<>();
        R = new HashSet<>();
        configure(id, configuration);
    }

    public void run(){
        // run the slot transmission algorithm

        // initialize the set of all the channels
        Set<Integer> channels = new HashSet<>();
        for (int channel=1; channel<=Main.getNumberOfChannels(); channel++){
            channels.add(channel);
        }
        // make the transmission

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
}
