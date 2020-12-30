public class Stats {
    int packetsSent=0;
    int packetsCreated=0;
    int packetsLost=0;
    int totalDelay=0;

    double getAverageDelay(){
        return (double) totalDelay / packetsSent;
    }

    double getThroughput() {
        return (double) packetsSent / Main.getNumberOfSlots();
    }

    double getPacketLossRate() {
        return (double) packetsLost / packetsCreated;
    }
}
