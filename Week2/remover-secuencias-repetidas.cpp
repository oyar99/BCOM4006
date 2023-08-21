#include <iostream>
#include <vector>
#include <unordered_set>
#include <queue>
#include <string>
#include <unordered_map>
#include <assert.h>
#include <cstdint>
#include <limits>

using namespace std;

/*
 * 
 * Ocultar información de actividad
 * Instrucciones
 * Implementar en grupos de 3 personas el algoritmo de Edmonds-Karp para determinar cuanta agua se puede transportar en una red de flujo desde un nodo fuente a un nodo destino. El programa debe recibir un archivo de entrada con las siguientes especificaciones:
 *
 * La primera linea debe tener la cantidad de nodos de la red N. Se asume que el primer nodo (el cero) es la fuente y el último (el N-1) el destino.
 *
 * Las siguientes lineas tienen tres números que representan la información de un tubo de la red. El primero es el id de nodo origen del tubo (de 0 a N-1), el segundo es el id de nodo destino del tubo (de 0 a N-1). El tercero es la capacidad del tubo.
 *
 * El programa debe imprimir un archivo que indiqué cuanto flujo máximo se puede transportar en cada tubo, cumpliendo las restricciones de capacidad y de balance de flujo, y el flujo total. El programa debe validar que no haya más de un tubo entre dos nodos de la red.
 * 
*/

int main() {
    int N; cin >> N;
    
    // Lista adyacente para representar un grafo de flujo.
    // 
    // adj[u] mapea a una pareja de datos (v, f/c) que indica
    // que u tiene un eje hacia v con capacidad c y flujo f.
    vector<unordered_map<int, pair<int, int>>> adj(N);

    while (!cin.eof()) {
        int u, v, c; cin >> u >> v >> c;

        assert(u < N);
        assert(v < N);

        if (adj[u].count(v) > 0) {
            cout << "Error de entrada: La red de flujo contiene mas de un tubo entre dos nodos de la red" << endl;
            return -1;
        }

        adj[u][v] = make_pair(0, c);
        adj[v][u] = make_pair(0, 0);
    }

    // Usaremos BFS para encontrar un camino entre source y sink para 
    // mandar flujo por ese camino
    auto bfs = [&]() {
        queue<int> q;
        unordered_set<int> visited;

        // Almacenaremos el camino para llegar al nodo sink
        vector<int> path(N, -1);

        q.push(0);
        visited.insert(0);

        while (!q.empty()) {
            int u = q.front();
            q.pop();

            // Cuando lleguemos al nodo sink, podemos terminar la búsqueda BFS
            if (u == N - 1) {
                break;
            }

            for (const auto& v: adj[u]) {
                int capacity = v.second.second - v.second.first;
                assert(capacity >= 0);

                if (visited.count(v.first) <= 0 && capacity > 0) {
                    q.push(v.first);
                    visited.insert(v.first);

                    path[v.first] = u;
                } 
            }
        }

        if (path[N -1] == -1) {
            // Esto indica que no encontramos un camino entre source y sink
            return 0;
        }

        int flow = numeric_limits<int32_t>::max();

        {
            // Calcular el flujo máximo que podemos mandar en el camino encontrado
            int u = N - 1;

            while (u != 0) {
                // El flujo máximo corresponderá a la menor capacidad de los ejes
                // que hacen parte del camino entre source y sink
                assert(path[u] != -1);

                flow = min(flow, adj[path[u]][u].second - adj[path[u]][u].first);
                u = path[u];
            }
        }

        {
            // Actualizar el flujo que pasa en el camino del grafo residual
            int u = N - 1;

            while (u != 0) {
                assert(path[u] != -1);

                adj[path[u]][u].first += flow;
                adj[u][path[u]].first -= flow;
                u = path[u];
            }
        }

        return flow;
    };

    int max_flow = 0;
    int flow = 0;

    // Vamos a encontrar caminos de flujo iterativamente con BFS hasta que no podamos
    // mandar mas flujo por la red
    do {
        flow = bfs();

        max_flow += flow;
    } while (flow != 0);

    // Por cada eje, vamos a registrar el flujo máximo que se puede enviar
    for (int u = 0; u < N; ++u) {
        for (const auto& v: adj[u]) {
            if (v.second.second == 0) {
                // Saltarse los ejes residuales ya que no hacen parte del grafo original
                // para la respuesta
                continue;
            }
            cout << "El eje (" << u << ", " << v.first << ") tiene flujo de " 
                << v.second.first << "/" << v.second.second << endl;
        }
    }

    // Reportar el flujo máximo total
    cout << "Flujo Máximo: " << max_flow << endl;
}