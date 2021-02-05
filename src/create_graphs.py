import pandas

##############
# Validation #
##############
#
# Για κάθε b θα δείχνουμε κόμβο και τα TP, Q, D
#

data = pandas.read_csv('../validation1.csv')
TP = list(data['TP'])
Q = list(data['Q'])
D = list(data['D'])

throughput = list()
throughput.append(TP[0:8])
throughput.append(TP[8:16])
throughput.append(TP[16:24])
throughput.append(TP[24:32])
throughput.append(TP[32:40])

for item in throughput:
    print(item)
