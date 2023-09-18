# Tarea 2 - Ensamblaje de genomas


- Juanita Puentes Mozo, j.puentes@uniandes.edu.co, 201814823
- Laura Valentina Acosta Corredor, lv.acostac@uniandes.edu.co, 201911225
- Jhon Stewar Rayo Mosquera, j.rayom@uniandes.edu.co, 201720166


# Objetivos 
1. Realizar una implementación sencilla del algoritmo OLC para ensamblaje de
genomas
2. Diferenciar conteos de lecturas de conteos de k-mers
3. Practicar una implementación de grafos dirigidos

# ¿Cómo correr el código?

Para ejecutar el script de *ReadsAnalyzerExample.java*, el cual tiene dos funcionalidades.

1. Ensamblar un texto/secuencia dado construyendo el grafo de solapes.
2. Calcular k-mers de un texto/secuencia dado

Se debe ejecutar el siguiente comando.

## Windows

`java -Xmx4g -cp lib\NGSEPcore_3.2.0.jar;bin uniandes.algorithms.readsanalyzer.ReadsAnalyzerExample "Overlap" "<Ruta de archivo de entrada>" 10`

## Mac

`java -Xmx4g -cp lib/NGSEPcore_3.2.0.jar:bin uniandes.algorithms.readsanalyzer.ReadsAnalyzerExample "Overlap" "<Ruta de archivo de entrada>" 10`

Donde los argumentos son:

`args[0]` **->** Comando a ejecutar. Puede ser **"Overlap"** o **"Kmers"**. 

`args[1]` **->** Path al archivo Fastq con las lecturas a procesar. 

`args[2]`  **->**  _Opcional_. Para **"Overlap"** es la longitud mínima de solapamiento. Para **"Kmers"** es la longitud de k-meros.

`args[3]` **->**  Path de base donde se guardaran los archivos .txt de abundancias y distribuciones. **Nota: Asegurese que esta carpeta ya exista en su máquina local**


---

Para ejecutar el script de *SimpleReadsSimulator.java*, el cual genera ejemplos de lectura de un archivo en formato fasta, se puede ejecutar el siguiente comando.

## Windows

`java -Xmx4g -cp lib\NGSEPcore_3.2.0.jar;bin uniandes.algorithms.readsanalyzer.SimpleReadsSimulator "data\HS_MIT.fasta" 100 20 "<Ruta de archivo de salida>" 1.0`

## Mac

`java -Xmx4g -cp lib/NGSEPcore_3.2.0.jar:bin uniandes.algorithms.readsanalyzer.SimpleReadsSimulator "data\HS_MIT.fasta" 100 20 "<Ruta de archivo de salida>" 1.0`

Donde los argumentos son:

`args[0]` -> Archivo de entrada en formato fasta

`args[1]` -> Tamano de las secuencias de lectura a simular

`args[2]` -> Numero de lecturas a simular

`args[3]` -> Porcentaje de error para generar lecturas con ciertos errors mas alineadas a lo que se obtiene con secuencacion de ADN

Para ambos casos se deben ejecutar estos comandos desde la raiz del proyecto.



## ADICIONALES

La carpeta [Results](https://github.com/oyar99/ISIS4006/tree/main/Tarea2/ReadsAnalyzer/Results) incluye todos los archivos .txt de las distribuciones y abundancias. La carpeta [Plots](https://github.com/oyar99/ISIS4006/tree/main/Tarea2/ReadsAnalyzer/Results/Plots) incluye las graficas generadas. El script [plots.py]([http://157.253.243.19/PLA-Net/](https://github.com/oyar99/ISIS4006/blob/main/Tarea2/ReadsAnalyzer/plots/plots_kmers.py)https://github.com/oyar99/ISIS4006/blob/main/Tarea2/ReadsAnalyzer/plots) contiene el código empleado para realizar las graficas en Python.
