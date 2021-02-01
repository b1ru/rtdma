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
| *LinkedList\<Packet>* queue | ο buffer των πακέτων του κόμβου |
| *Set\<Integer>* T | Τα κανάλια στα οποία μπορεί να μεταδώσει ο κόμβος |
| *Set\<Integer>* R | Τα κανάλια από τα οποία μπορεί να λάβει πακέτα ο κόμβος |
| *ArrayList\<ArrayList\<Integer>>* A | **Α<sub>i</sub>** οι κόμβοι που μπορούν να μεταδώσουν στο κανάλι i |
| *ArrayList\<ArrayList\<Integer>>* B | **Β<sub>i</sub>** οι κόμβοι που μπορούν να λάβουν πακέτα από το κανάλι i |
| *Random* rand | γεννήτρια τυχαίων αιρθμών |
| *int* bufferSize | H χωρητικότητα του buffer του κόμβου |
| *double* l | H πιθανότητα δημιουργίας πακέτα σε ένα timeslot |
| *double[]* d | **d<sub>i</sub>** η πιθανότητα το πακέτα να έχει ως προορισμό τον κόμβο i |
| *int* id | Το id του κόμβου |
| *int* transmitted | Ο αριθμός των πακέτων που μεταδόθηκαν από αυτόν τον κόμβο |
| *int* buffered | Μετράεω πόσα πακέτα είναι στον buffer σε κάθε timeslot <br> Το άθροισμα για όλα τα timeslots είναι το **buffered** |
| *int* slotsWaited | Μετράω πόσα timeslots περιμένει ένα πακέτο στον buffer <br> Το άθροισμα για όλα τα πακέτα που μεταδώθηκαν είναι το **slotsWaited**|

##### Μέθοδοι

| Mέθοδος | Περιγραφή |
| ------- | --------- |
| Node(*int* id, *int* configuration, *long* seed) | Constructor <br> Αρχικοποιεί τιμές και ρυθμίζει τον κόμβο ανάλογα το επιλεγμένο σύστημα μέσω της configure()|
| *void* configure(*int* id, *int* configuration) | Δίνει τιμές στο transmission range **T** και στο receiving range **R** του κόμβου |
| *void* slotAction(*int* slot) | Ο αλγόριθμος που εκτελεί ο κόμβος σε ένα timeslot |
| *int* findDestination() | Επιστρέφει έναν τυχαίο κόμβο για προορισμό |
| *void* reset(double systemLoad) | Δίνει τιμή στο l ανάλογα το είδος της προσομοίωσης <br> Μηδενίζει τους counters <br> Αδειάζει τον buffer <br> Καλείται από την Main.main() για κάθε τιμή του b |
| *void* setD(*boolean* validation) | Δίνει τιμές στον πίνακα d ανάλογα το είδος της προσομοίωσης |
| *void* setBufferSize(*boolean* validation) | Δίνει τιμή στο bufferSize ανάλογα το είδος της προσομοίωσης|
| *void* setA(*ArrayList\<ArrayList\<Integer>>* A) | setter του Α |
| *void* setB(*ArrayList\<ArrayList\<Integer>>* B) | setter του Β |
| *Set\<Integer>* getT() | getter του Τ |
| *Set\<Integer>* getR() | getter του R |
| *int* getTransmitted() | getter του transmitted |
| *int* getBuffered() | getter του buffered |
| *int* getSlotsWaited() | getter του slotsWaited |




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
| *int* getNumberOfNodes() | getter του numberOfNodes |
| *int* getNumberOfChannels() | getter του numberOfChannels |
| *boolean* getValidation() | getter του validation |
