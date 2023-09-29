package uniandes.algorithms.fmindex;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class FMIndex {
	private SuffixArray suffixArray;
	private Map<Character, Integer> alphabet; // alphabet[c] is the position of c in the alphabet
	private Map<Integer, Character> reverseAlphabetMap; // reverseAlphabetMap[i] is the character at position i in the alphabet
	private int [][] tallyIndices;

	public FMIndex(String sequence, boolean optimizeMem) {
		int n = sequence.length();
		// Create suffix array for sequence: sequence$
		String seq = sequence + "$";
		this.suffixArray = new SuffixArray(seq, optimizeMem);
		int [] sa = suffixArray.getSuffixArray();

		StringBuilder bawBuilder = new StringBuilder();

		for (int i = 0; i < sa.length; ++i) {
			bawBuilder.append(seq.charAt(sa[i] - 1 < 0 ? sa.length - 1 : sa[i] - 1));
		}

		String baw = bawBuilder.toString();

		SortedSet<Character> alphabetSet = new TreeSet<>();

		for (int i = 0; i < baw.length(); ++i) {
			alphabetSet.add(baw.charAt(i));
		}

		this.alphabet = new HashMap<>();
		this.reverseAlphabetMap = new HashMap<>();

		int posInAlphabet = 0;

		// The alphabet is iterated by lexicographical order
		for (Character c: alphabetSet) {
			this.alphabet.put(c, posInAlphabet);
			this.reverseAlphabetMap.put(posInAlphabet, c);
			posInAlphabet++;
		}

		// Matrix where the cell (i, j) corresponds to the
		// the number of occurrences of the jth symbol of the alphabet in the
		// substring baw[0: i].
		this.tallyIndices = new int[n + 1][this.alphabet.size()];

		for (int i = 0; i < baw.length(); ++i) {
			char currentChar = baw.charAt(i);
			int k = this.alphabet.get(currentChar);

			for (int j = 0; j < this.alphabet.size(); ++j) {
				this.tallyIndices[i][j] = (i - 1 >= 0 ? this.tallyIndices[i-1][j] : 0);

				if (j == k) {
					this.tallyIndices[i][j]++;
				}
			}
		}
	}

	/**
     * Find occurrences of query in the sequence
     * 
     * @param query String that we will search in the sequence
     * 
     * @return the index of an occurrence of query in the sequence. -1, if no match
     * is found.
     */
	public int[] search(String query) {
		if (query.length() <= 0) {
			return new int[0];
		}

		int [] characterCount = this.tallyIndices[this.tallyIndices.length - 1];

		// Find starting range for last character of query
		int fl = L2F(query.charAt(query.length() - 1), 1);

		if (fl == -1) {
			// The last character of query doesn't even appear in the sequence
			return new int[0];
		}

		// Find end of range - determined by the count of character query[query.length - 1]
		int fr = fl + characterCount[this.alphabet.get(query.charAt(query.length() - 1))] - 1;

		for (int i = query.length() - 2; i >= 0; --i) {
			char c = query.charAt(i);
			int j = this.alphabet.get(c);

			int rangeLength = this.tallyIndices[fr][j] - (fl - 1 > 0 ? this.tallyIndices[fl - 1][j] : 0);
			
			if (rangeLength == 0) {
				// We weren't able to find a preceding character that matched the query in the sequence
				return new int[0];
			}

			int l = this.tallyIndices[fr][j] - rangeLength + 1;

			// Last to First Mapping
			fl = L2F(c, l);
			fr = fl + rangeLength - 1;
		}

		// We have found matches for query in the sequence starting at position fl and ending at position
		// fr in the suffix array.
		int[] indices = new int[fr - fl + 1]; 
		int[] sa = this.suffixArray.getSuffixArray();

		for (int i = fl; i <= fr; ++i) {
			indices[i - fl] = sa[i];
		}

		return indices;
	}

	private int L2F(char c, int rank) {
		// The last row in the tally indices matrix corresponds to the count of symbols 
		// for the whole sequence which can be used to map from last column to first column in the
		// baw matrix
		int f = -1;
		int runningPos = 0;

		int [] characterCount = this.tallyIndices[this.tallyIndices.length - 1];

		for (int j = 0; j < characterCount.length; ++j) {
			Character ch = this.reverseAlphabetMap.get(j);
			if (ch.charValue() == c) {
				f = runningPos + rank - 1; // Ranks are 1-based
				break;
			}

			runningPos += characterCount[j];
		}

		return f;
	}
}
