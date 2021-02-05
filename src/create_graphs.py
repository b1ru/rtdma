import pandas
import plotly.graph_objects as go
from plotly.subplots import make_subplots

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

fig = make_subplots(
    rows=5, cols=1,
    shared_xaxes=True,
    vertical_spacing=0.03,
    specs=[[{"type": "table"}],
           [{"type": "table"}],
           [{"type": "table"}],
           [{"type": "table"}],
           [{"type": "table"}]],
   subplot_titles=("b=0.2", "b=0.4", "b=0.6", "b=0.8", "b=1")
)

for i in range(5):
    fig.add_trace(go.Table(
        columnwidth = [1,2,2,2],
        header=dict(values=['Node', 'TP', 'Q', 'D']),
        cells=dict(values=[list(range(1,9)), tp[i], q[i], d[i]])),
        row=i+1, col=1
    )


fig.update_layout(
    title_text="Validation",
    height=2000
)

fig.show()
