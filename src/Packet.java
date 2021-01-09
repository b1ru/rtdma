public class Packet {
    int timeslot;
    int destination;
    public Packet(int destination, int timeslot) {
        this.destination = destination;
        this.timeslot = timeslot;
    }
}
