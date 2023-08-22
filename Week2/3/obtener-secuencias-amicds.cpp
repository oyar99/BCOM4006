#include <iostream>
#include <unordered_set>
#include <unordered_map>
#include <string>
#include <vector>
#include <assert.h>

using namespace std;

unordered_map<string, string> GENETIC_CODON = {
    { "UUU", "F" },
    { "UUC", "F" },
    { "UUA", "L" },
    { "UUG", "L" },
    { "CUU", "L" },
    { "CUC", "L" },
    { "CUA", "L" },
    { "CUG", "L" },
    { "AUU", "I" },
    { "AUC", "I" },
    { "AUA", "I" },
    { "AUG", "M" },
    { "GUU", "V" },
    { "GUC", "V" },
    { "GUA", "V" },
    { "GUG", "V" },
    { "UCU", "S" },
    { "UCC", "S" },
    { "UCA", "S" },
    { "UCG", "S" },
    { "CCU", "P" },
    { "CCC", "P" },
    { "CCA", "P" },
    { "CCG", "P" },
    { "ACU", "T" },
    { "ACC", "T" },
    { "ACA", "T" },
    { "ACG", "T" },
    { "GCU", "A" },
    { "GCC", "A" },
    { "GCA", "A" },
    { "GCG", "A" },
    { "UAU", "Y" },
    { "UAC", "Y" },
    { "CAU", "H" },
    { "CAC", "H" },
    { "CAA", "Q" },
    { "CAG", "Q" },
    { "AAU", "N" },
    { "AAC", "N" },
    { "AAA", "K" },
    { "AAG", "K" },
    { "GAU", "D" },
    { "GAC", "D" },
    { "GAA", "E" },
    { "GAG", "E" },
    { "UGU", "C" },
    { "UGC", "C" },
    { "UGG", "W" },
    { "CGU", "R" },
    { "CGC", "R" },
    { "CGA", "R" },
    { "CGG", "R" },
    { "AGU", "S" },
    { "AGC", "S" },
    { "AGA", "R" },
    { "AGG", "R" },
    { "GGU", "G" },
    { "GGC", "G" },
    { "GGA", "G" },
    { "GGG", "G" },
};

unordered_set<string> TERMINATION_CODON = {
    { "UAA" }, { "UAG" }, { "UGA" }
};

const string START_CODON = "AUG";

const int MAX_LENGTH_LINE_OUTPUT = 60;

/*
 * Este programa recibe un archivo FASTA con secuencias de nucleótidos y retorna otro archivo fasta
 * con las secuencias de aminoácidos que representan la traducción a proteína de la entrada de datos.
 */
int main() {
    vector<string> sequences;
    string currentSequence;
    string currentLine;

    auto appendCurrentSequence = [&](const string& newLine = "") {
        if (currentSequence.size() > 0) {
            sequences.push_back(currentLine);
            sequences.push_back(currentSequence);
        }

        currentSequence = "";
        currentLine = newLine;
    };

    auto isHeader = [](const string& line) {
        return line.size() > 0 && line[0] == '>';
    };

    while (!cin.eof()) {
        string line; getline(cin, line);

        if (isHeader(line)) {
            appendCurrentSequence(line);
        } else {
            currentSequence += line;
        }
    }

    appendCurrentSequence();

    // Ya construimos un arreglo donde el elemento con indice 2n corresponde a la descripción de la secuencia, 
    // y el elemento 2n+1 corresponde a la secuencia para todos los n tales que n < N donde N es el numero de secuencias en
    // el archivo FASTA

    vector<string> output;

    for (int i = 0; i < sequences.size(); ++i) {
        if (!(i & 1)) {
            output.push_back(sequences[i]);
            continue;
        }

        // Proceso de transcripción para generar secuencia de RNA
        string rna = "";
        for (int j = 0; j < sequences[i].size(); ++j) {
            if (sequences[i][j] == 'T') {
                rna += "U";
            } else if (sequences[i][j] == 'A' || sequences[i][j] == 'C' || sequences[i][j] == 'G') {
                rna += sequences[i][j];
            }
        }

        bool isTranslationStarted = false;
        bool isTranslationFinished = false;
        string translation = "";

        for (int j = 0; j < rna.size() - 2;) {
            string codon = rna.substr(j, 3);
            if (!isTranslationStarted) {
                // Buscar codon de inicio AUG
                if (codon == START_CODON) {
                   isTranslationStarted = true;
                   translation += GENETIC_CODON[codon];
                   j += 3;
                   continue;
                }

                ++j;
            } else {
                if (TERMINATION_CODON.count(codon) > 0) {
                    isTranslationFinished = true;
                    break;
                }

                assert(GENETIC_CODON.count(codon) > 0);
            
                translation += GENETIC_CODON[codon];
                j += 3;
            }
        }

        output.push_back(translation);
    }

    for (const auto& line: output) {
        if (!isHeader(line)) {
            for (int i = 0; i < line.size(); ++i) {
                cout << line[i];

                if ((i+1) % MAX_LENGTH_LINE_OUTPUT == 0 || i == line.size() - 1) {
                    // Para el formato fasta es recomendable no tener mas de 80 caracteres por linea
                    cout << endl;
                }
            }
        } else {
            cout << line << endl;
        }
    }
}