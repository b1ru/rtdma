## Εκτέλεση του προγράμματος
- Ο προσομοιωτής είναι γραμμένος σε java.
- Τρέχουμε τη Main κλάση.
- Δημιουργούνται .csv files με τα αποτελέσματα.
- Ένα python script παίρνει τα .csv και τα μετατρέπει σε tables και γραφήματα
που απεικονίζουν τα δεδομένα.

## Κλάσεις
### Packet

- Υλοποιεί ένα πακέτο.

Μεταβλητές

| Μεταβλητή   | Περιγραφή |
| ---------   | --------- |
| destination | ο προορισμός του πακέτου |
| timeslot    | τo timeslot δημιουργήθηκε το πακέτο |

Μέθοδοι

| Mέθοδος | Περιγραφή |
| ------- | --------- |
| Packet(int destination, int timeslot) | Constructor<br> Αρχικοποιεί τις 2 μεταβλητές|


### Node

- Υλοποιεί έναν κόμβο.
