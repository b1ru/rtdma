import pandas
import plotly.graph_objects as go
from plotly.subplots import make_subplots
import matplotlib.pyplot as plt

##############
# Validation #
##############

# Για κάθε b θα δείχνουμε κόμβο και τα TP, Q, D


def validation():
    for i in range(1, 4):
        validation_single(i)


def validation_single(conf):
    data = pandas.read_csv('../validation' + str(conf) + '.csv')
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
            columnwidth=[1, 2, 2, 2],
            header=dict(values=['Node', 'TP', 'Q', 'D']),
            cells=dict(values=[list(range(1, 9)), tp[i], q[i], d[i]])),
            row=i+1, col=1
        )

    fig.update_layout(
        title_text="Validation, system " + str(conf),
        height=2000
    )

    fig.show()

###############
# PERFORMANCE #
###############

# Δείχνουμε ένα γράφημα με το througput και το delay του συστήματος για κάθε
#   ρύθμιση.


def performance():
    TP = list()
    D = list()

    for i in range(1, 4):
        data = pandas.read_csv('../performance' + str(i) + '.csv')
        TP.append(list(data['TP']))
        D.append(list(data['D']))

    fig, ax = plt.subplots()
    lines = list()
    for i in range(3):
        lines.append(ax.plot(TP[i], D[i])[0])

    ax.set(xlabel="Throughput", ylabel="Delay", title="Average packet delay"
        + " versus system throughput,\nN=8, W=4, Lᵢ=4, RTDMA protocol.")
    ax.legend(lines, ('System 1', 'System 2', 'System 3'))
    plt.show()


validation()
performance()
