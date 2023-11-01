##
import pandas as pd
import matplotlib.pyplot as plt


df = pd.read_csv('LiverFemale3600_degree_dist.csv', sep='\t', header=None)

df = df[df.iloc[:, 1] != 0]

x_values = df.iloc[:, 0]
y_values = df.iloc[:, 1]


plt.bar(x_values, y_values, color='mediumorchid')

plt.style.use('ggplot')
plt.grid(1)
plt.xlabel('Grado')
plt.ylabel('Frecuencia')
plt.show()
