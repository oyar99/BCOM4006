#include <iostream>
#include <unordered_set>
#include <string>
#include <vector>

using namespace std;

const int MAX_LENGTH_LINE_OUTPUT = 60;

int main() {
    unordered_set<string> sequences; // Mapa para indexar secuencias biológicas
    vector<string> output;
    string currentSequence;
    string currentLine;

    auto appendCurrentSequence = [&](const string& newLine = "") {
        if (currentSequence.size() > 0 && sequences.count(currentSequence) <= 0) {
            output.push_back(currentLine);
            output.push_back(currentSequence);

            sequences.insert(currentSequence);
        }

        currentSequence = "";
        currentLine = newLine;
    };

    auto isHeader = [](const string& line) {
        return line.size() > 0 && line[0] == '>';
    };

    while (!cin.eof()) {
        string line; cin >> line;

        if (isHeader(line)) {
            appendCurrentSequence(line);
        } else {
            currentSequence += line;
        }
    }

    appendCurrentSequence();

    for (const auto& line: output) {
        if (!isHeader(line)) {
            for (int i = 0; i < line.size(); ++i) {
                if (i % MAX_LENGTH_LINE_OUTPUT == 0 || i == line.size() - 1) {
                    // Para el formato fasta es recomendable no tener mas de 80 caracteres por linea
                    cout << endl;
                }

                cout << line[i];
            }
        } else {
            cout << line;
        }
    }
}