### Bases de datos de ADN

Solución a la tarea #1 de BCOM4006 - Algoritmos en Biología Computacional, sobre bases de datos de ADN.

Junto a este readme, se encuentra un informe "Informe_Tarea_BiologiaMolecular" donde se explica a detalle
el proceso llevado a cabo en cada uno de los puntos, asi como descripción y aclaración de conceptos
claves.

En cuanto a los programas desarrollados para apoyar el análisis de las secuencias biológicas, estos
fueron elaborados en C++, un lenguaje multiplataforma en el sentido que se puede distribuir el código
fuente y compilar en distintos sistemas. Aunque los binarios no necesariamente se puedan
distribuir y ejecutar de la misma manera.

Las siguientes instrucciones explican como configurar el compilador GCC en distintas plataformas:

    - Windows: https://code.visualstudio.com/docs/cpp/config-mingw.
    - Linux: https://code.visualstudio.com/docs/cpp/config-linux
    - Mac: https://code.visualstudio.com/docs/cpp/config-clang-mac

## Como ejecutar los programas

1. Compilar el código fuente siguiendo las instrucciones anteriores y generar un ejecutable. 

2. Ejecutar el ejecutable (ejemplo: .exe) usando redirección de archivos, para indicar tanto la entrada como la salida del 
programa.

    Ejemplo:
        remover-secuencias-repetidas.exe < GBSSI_nucl.fa > GBSSI_nucl-out.fa

    Notar que powershell no soporta el operador <, por lo que se recomienda usar el CMD en Windows.