Para ejecutar el script de SimpleReadsSimulator.java, el cual genera ejemplos de lectura de un archivo en formato fasta, se puede ejecutar el siguiente comando.

## Windows.

java -Xmx4g -cp lib\NGSEPcore_3.2.0.jar;bin uniandes.algorithms.readsanalyzer.SimpleReadsSimulator "data\HS_MIT.fasta" 100 20 "<Ruta de archivo de salida>" 1.0

## Mac.

java -Xmx4g -cp lib/NGSEPcore_3.2.0.jar:bin uniandes.algorithms.readsanalyzer.SimpleReadsSimulator "data\HS_MIT.fasta" 100 20 "<Ruta de archivo de salida>" 1.0

Donde el primer argumento corresponde al archivo de entrada en formato fasta, el siguiente al tamano de las secuencias de lectura a simular, luego el
numero de lecturas a simular, y finalmente el porcentaje de error.