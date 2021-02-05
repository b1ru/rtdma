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

tp = [TP[0:8], TP[8:16], TP[16:24], TP[24:32], TP[32:40]]
q = [Q[0:8], Q[8:16], Q[16:24], Q[24:32], Q[32:40]]
d = [D[0:8], D[8:16], D[16:24], D[24:32], D[32:40]]
