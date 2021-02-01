Υλοποίηση του Random Time-Division Multiple Access δικτύου που παρουσιάζεται στο [ανεβασμένο paper](./RTDMA.pdf).

## Εκτέλεση του προγράμματος
- Ο προσομοιωτής είναι γραμμένος σε java.
- Τρέχουμε τη Main κλάση.
- Δημιουργούνται .csv files με τα αποτελέσματα.
- Ένα python script παίρνει τα .csv και τα μετατρέπει σε tables και γραφήματα
που απεικονίζουν τα δεδομένα.

## Κλάσεις
### Packet

- Υλοποιεί ένα πακέτο.

##### Μεταβλητές

| Μεταβλητή   | Περιγραφή |
| ---------   | --------- |
| *int* destination | ο προορισμός του πακέτου |
| *int* timeslot    | τo timeslot δημιουργήθηκε το πακέτο |

##### Μέθοδοι

| Mέθοδος | Περιγραφή |
| ------- | --------- |
| Packet(*int* destination, *int* timeslot) | Constructor<br> Αρχικοποιεί τις 2 μεταβλητές|

### Node
- Υλοποιεί έναν κόμβο.

##### Μεταβλητές

| Μεταβλητή   | Περιγραφή |
| ---------   | --------- |
| *LinkedList<Packet>* queue | ο buffer των πακέτων του κόμβου |
| *Set<Integer>* T | Τα κανάλια στα οποία μπορεί να μεταδώσει ο κόμβος |
| *Set<Integer>* R | Τα κανάλια από τα οποία μπορεί να λάβει πακέτα ο κόμβος |
| *ArrayList<ArrayList<Integer>>* A | **Α~i~** οι κόμβοι που μπορούν να μεταδώσουν στο κανάλι i |
| *ArrayList<ArrayList<Integer>>* B | **Β~i~** οι κόμβοι που μπορούν να λάβουν πακέτα από το κανάλι i |
| *Random* rand | γεννήτρια τυχαίων αιρθμών |
| *int* bufferSize | H χωρητικότητα του buffer του κόμβου |
| *double* l | H πιθανότητα δημιουργίας πακέτα σε ένα timeslot |
| *double[]* d | **d~i~** η πιθανότητα το πακέτα να έχει ως προορισμό τον κόμβο i |
| *int* id | Το id του κόμβου |
| *int* transmitted | Ο αριθμός των πακέτων που μεταδόθηκαν από αυτόν τον κόμβο |
| *int* buffered | Μετράεω πόσα πακέτα είναι στον buffer σε κάθε timeslot <br> Το άθροισμα για όλα τα timeslots είναι το **buffered** |
| *int* slotsWaited | Μετράω πόσα timeslots περιμένει ένα πακέτο στον buffer <br> Το άθροισμα για όλα τα πακέτα που μεταδώθηκαν είναι το **buffered**|



### Main
- Τρέχει το πρόγραμμα και γίνεται η προσομοίωση.

##### Μεταβλητές
| Μεταβλητή | Περιγραφή |
| --------- | --------- |
| *int* numberOfNodes| Ο αριθμός των κόμβων |
| *int* numberOfChannels | Ο αριθμός των καναλιών |
| *int* numberOfSlots | Πόσα timeslots θα χρησιμοποιηθούν στην προσομοίωση |
| *Node[]* nodes | Ο πίνακας με τους κόμβους |
| *boolean* validation | Αν θέλουμε να τρέξουμε την 'validation' προσομοίωση ή την 'performance' προσομοίωση όπως αναγράφονται στο paper |
| *FileWriter* out1 | Το stream για τα csv αρχεία της validation προσομοίωσης |
| *FileWriter* out1 | Το stream για τα csv αρχεία της performance προσομοίωσης |

##### Μέθοδοι
| Mέθοδος | Περιγραφή |
| ------- | --------- |
| *void* main(*String[]* args) | Διαλέγουμε ποιο από τα 3 συστήματα θέλουμε να προσομοιώσουμε <br> Αφού γίνουν οι κατάλληλες αρχικοποιήσεις γίνεται η προσομοίωση για το επιλεγμένο σύστημα μέσω της simulate()|
| *void* simulate() | Γίνονται οι 'validation' και 'performance' προσομοιώσεις χρησμιοποιώντας την Node.slotAction() και γράφονται τα δεδομένα στα .csv αρχεία |
| *int* getNumberOfNodes() | numberOfNodes getter |
| *int* getNumberOfChannels() | numberOfChannels getter |
| *boolean* getValidation() | validation getter |
