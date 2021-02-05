import pandas

##############
# Validation #
##############
#
# Για κάθε b θα δείχνουμε κόμβο και τα TP, Q, D
#

data = pandas.read_csv('../validation1.csv')
TP = data['TP']
Q = data['Q']
D = data['D']
