'''import os
import pandas as pd
import matplotlib.pyplot as plt


script_directory = os.path.dirname(os.path.abspath(__file__))
generalpath =  os.path.dirname(script_directory)

folder_path = os.path.join(generalpath, "Results", "kmers","100x")

data = []

# Iterate through the files in the folder
for filename in os.listdir(folder_path):
    if filename.startswith("kmer_distribution_") and filename.endswith(".txt"):
        # Extract the distribution number from the filename (e.g., 5, 10, 20)
        distribution_number = int(filename.split("_")[2].split(".")[0])
        
        # Read the data from the file into a DataFrame
        file_path = os.path.join(folder_path, filename)
        df = pd.read_csv(file_path, delim_whitespace=True, names=["Value", "Frequency"])
        
        # Store the data along with the distribution number
        data.append((distribution_number, df))

data.sort(key=lambda x: x[0])

# Create a plot with different colors for each distribution
plt.figure(figsize=(10, 6))

bar_width = 0.8  # Adjust the bar width as needed

for i, (distribution_number, df) in enumerate(data):
    x = df["Value"] + i * bar_width  # Shift the x-coordinates for each group
    plt.bar(x, df["Frequency"], alpha=0.7,width=bar_width, label=f"Tamaño K-mer: {distribution_number}")

# Customize the plot
plt.title("K-mer Distribution 100x")
plt.xlabel("K-mer Frecuency")
plt.ylabel("Number of K-mers")
plt.legend()
plt.grid(True, axis="y", linestyle="--", alpha=0.9)
path =os.path.join(generalpath, "Results", "kmer_distributions_100x.png")
#plt.savefig(path)
#plt.show()


script_directory = os.path.dirname(os.path.abspath(__file__))
generalpath =  os.path.dirname(script_directory)

folder_path = os.path.join(generalpath, "Results", "kmers")

data = []
num = 75
# Iterate through the files in the folder

for depth in os.listdir(folder_path):

    if depth.endswith("x"):

        for tamanio in os.listdir(os.path.join(folder_path, depth)):

            if tamanio.startswith(f"kmer_distribution_{num}.txt"):

                file_path = os.path.join(folder_path, depth, tamanio)
                df = pd.read_csv(file_path, delim_whitespace=True, names=["Value", "Frequency"])
                
                # Store the data along with the distribution number
                data.append((depth, df))


custom_order = {'10x': 0, '20x': 1, '50x': 2,'100x': 2}

# Sort the data based on the custom sorting order
data.sort(key=lambda x: custom_order.get(x[0], float('inf')))


# Create a plot with different colors for each distribution
plt.figure(figsize=(10, 6))

bar_width = 0.8  # Adjust the bar width as needed

for i, (depth, df) in enumerate(data):
    x = df["Value"] + i * bar_width  # Shift the x-coordinates for each group
    plt.bar(x, df["Frequency"], alpha=0.7,width=bar_width, label=f"Profundidad: {depth}")

# Customize the plot

plt.xlabel("K-mer Frecuency")
plt.ylabel("Number of K-mers")
plt.legend()
plt.grid(True, axis="y", linestyle="--", alpha=0.9)
path =os.path.join(generalpath, "Results", "Plots","kmer_distributions_tam_75.png")
plt.savefig(path)
plt.show()

'''

'''



script_directory = os.path.dirname(os.path.abspath(__file__))
generalpath =  os.path.dirname(script_directory)

folder_path = os.path.join(generalpath, "Results", "Overlap","50x")

data = []

# Iterate through the files in the folder
for filename in os.listdir(folder_path):
    if filename.startswith("overlap_") and filename.endswith(".txt"):
        # Extract the distribution number from the filename (e.g., 5, 10, 20)
        distribution_number = int(filename.split("_")[1].split(".")[0])
        
        # Read the data from the file into a DataFrame
        file_path = os.path.join(folder_path, filename)
        df = pd.read_csv(file_path, delim_whitespace=True, names=["Value", "Frequency"])
        
        # Store the data along with the distribution number
        data.append((distribution_number, df))

data.sort(key=lambda x: x[0])

# Create a plot with different colors for each distribution
plt.figure(figsize=(10, 6))

bar_width = 0.6  # Adjust the bar width as needed

for i, (distribution_number, df) in enumerate(data):
    x = df["Value"] + i * bar_width  # Shift the x-coordinates for each group
    plt.bar(x, df["Frequency"], alpha=0.7,width=bar_width, label=f"Overlapping: {distribution_number}")

# Customize the plot
plt.xlabel("Sucesores")
plt.ylabel("Conteo (n)")
plt.legend()
plt.grid(True, axis="y", linestyle="--", alpha=0.9)
path =os.path.join(generalpath, "Results", "Plots", "overlapping_50x.png")
#plt.savefig(path)
#plt.show()'''


import os
import pandas as pd
import matplotlib.pyplot as plt

script_directory = os.path.dirname(os.path.abspath(__file__))
generalpath =  os.path.dirname(script_directory)

folder_path = os.path.join(generalpath, "Results", "Overlap")

data = []
num = 50
# Iterate through the files in the folder

for depth in os.listdir(folder_path):

    if depth.endswith("x"):

        for tamanio in os.listdir(os.path.join(folder_path, depth)):

            if tamanio.startswith(f"abundance_{num}.txt"):

                file_path = os.path.join(folder_path, depth, tamanio)
                df = pd.read_csv(file_path, delim_whitespace=True, names=["Value", "Frequency"])
                
                # Store the data along with the distribution number
                data.append((depth, df))


custom_order = {'10x': 0, '20x': 1, '50x': 2,'100x': 2}

# Sort the data based on the custom sorting order
data.sort(key=lambda x: custom_order.get(x[0], float('inf')))


# Create a plot with different colors for each distribution
plt.figure(figsize=(10, 6))

bar_width = 0.1  # Adjust the bar width as needed

for i, (depth, df) in enumerate(data):
    x = df["Value"] + i * bar_width  # Shift the x-coordinates for each group
    plt.bar(x, df["Frequency"], alpha=0.7,width=bar_width, label=f"Profundidad: {depth}")

# Customize the plot

plt.xlabel("Abundancia")
plt.ylabel("Secuencia")
plt.legend()
plt.grid(True, axis="y", linestyle="--", alpha=0.9)
path =os.path.join(generalpath, "Results", "Plots","overlap_size_50.png")
plt.savefig(path)
plt.show()