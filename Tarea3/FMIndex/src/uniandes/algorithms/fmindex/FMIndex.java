package uniandes.algorithms.fmindex;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class FMIndex {
	private String baw;
	private int [][] tallyIndices;

	public FMIndex(String sequence) {
		int n = sequence.length();
		// Create suffix array for sequence: sequence$
		SuffixArray suffixArray = new SuffixArray(sequence + "$");
		int [] sa = suffixArray.getSuffixArray();

		StringBuilder bawBuilder = new StringBuilder();

		for (int i = 0; i < sa.length; ++i) {
			bawBuilder.append(sequence.charAt(sa[i] - 1 < 0 ? sa.length - 1 : sa[i] - 1));
		}

		String baw = bawBuilder.toString();

		SortedSet<Character> alphabetSet = new TreeSet<>();

		for (int i = 0; i < baw.length(); ++i) {
			alphabetSet.add(baw.charAt(i));
		}

		Map<Character, Integer> alphabet = new HashMap<>();

		int posInAlphabet = 0;
		for (Character c: alphabetSet) {
			alphabet.put(c, posInAlphabet++);
		}

		// Matrix where the cell (i, j) corresponds to the
		// the number of occurrences of the jth symbol of the alphabet in the
		// substring baw[0: i].
		this.tallyIndices = new int[n + 1][alphabet.size()];

		for (int i = 0; i < baw.length(); ++i) {
			char currentChar = baw.charAt(i);
			int k = alphabet.get(currentChar);

			for (int j = 0; j < alphabet.size(); ++i) {
				this.tallyIndices[i][j] = (i - 1 >= 0 ? this.tallyIndices[i-1][j] : 0);

				if (j == k) {
					this.tallyIndices[i][j]++;
				}
			}
		}

		this.baw = baw;
	}

	/**
     * Find an occurrence of query in the sequence
     * 
     * @param query String that we will search in the sequence
     * 
     * @return the index of an occurrence of query in the sequence. -1, if no match
     * is found.
     */
	public int search(String query) {

	}

	private int lastToFirst(char c, int rank) {
		
	}
}
