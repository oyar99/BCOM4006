package uniandes.algorithms.fmindex;

import java.util.Arrays;
import java.util.Comparator;

public class SuffixArray {
    private class Suffix {
        String suffix;
        int pos;

        public Suffix(String suffix, int pos) {
            this.suffix = suffix;
            this.pos = pos;
        }
    }

    private int sa[];
    private String sequence;

    public SuffixArray(String sequence, boolean optimizeMem) {
        if (optimizeMem) {
            initV2(sequence);
        } else {
            init(sequence);
        }
    }

    /*
     * This init version will use O(n^2) space to store all suffixes of the given sequence
     */
    private void init(String sequence) {
        this.sequence = sequence;

        int n = sequence.length();
        Suffix[] suffixes = new Suffix[n];

        // Create all suffixes of string sequence
        for (int i = 0; i < n; ++i) {
            suffixes[i] = new Suffix(sequence.substring(i), i);
        }

        // Sort suffixes
        Arrays.sort(suffixes, (a, b) -> a.suffix.compareTo(b.suffix));

        // Create suffix array
        this.sa = new int[n];

        for (int i = 0; i < n; ++i) {
            this.sa[i] = suffixes[i].pos;
        }
    }

    /*
     * BONUS: This init version will not use additional space to explicitly store suffixes
     * of the given sequence. 
     */
    private void initV2(String sequence) {
        this.sequence = sequence;

        int n = sequence.length();

        // Create array with indices of starting position of all suffixes of sequence
        Integer[] intArray = new Integer[n];

        for (int i = 0; i < n; ++i) {
            intArray[i] = i;
        }

        // Sort suffix array indices without storing additional info
        // We use a custom comparator to determine if suffix starting
        // at position i comes before suffix starting at position j by
        // looking at the characters in the given sequence.
        Arrays.sort(intArray, new Comparator<Integer>() {
            public int compare(Integer a, Integer b) {
                int suffixALength = n - a;
                int suffixBLength = n - b;

                for (int i = 0; i < Math.min(suffixALength, suffixBLength); ++i) {
                    Character ca = sequence.charAt(i + a);
                    Character cb = sequence.charAt(i + b);

                    int comparisonValue = ca.compareTo(cb);

                    if (comparisonValue == 0) {
                        continue;
                    }

                    return comparisonValue;
                }

                // This will return a negative value if suffix starting at position b comes after 
                // the other suffix. Otherwise it will return a positive value.
                return suffixALength - suffixBLength;
            }
        });

        // Convert from Integer array to array of primitives
        this.sa = Arrays.stream(intArray).mapToInt(Integer::intValue).toArray();
    }

    /**
     * 
     * @return Suffix array corresponding to this sequence
     */
    public int[] getSuffixArray() {
        return this.sa;
    }

    /**
     * Uses binary search to find an occurrence of query in the sequence
     * 
     * @param query String that we will search in the sequence
     * 
     * @return the index of an occurrence of query in the sequence. -1, if no match
     *         is found.
     */
    public int search(String query) {
        int l = 0;
        int r = this.sa.length;

        while (l < r) {
            int m = (l + r) / 2;

            String suffix = this.sequence.substring(
                    this.sa[m], Math.min(query.length() + this.sa[m], this.sequence.length()));

            boolean foundQuery = true;

            for (int i = 0; i < query.length(); ++i) {
                if (i >= suffix.length()
                        || suffix.charAt(i) < query.charAt(i)) {
                    // This suffix is lexicographically smaller than query
                    l = m + 1;
                    foundQuery = false;
                    break;
                }

                if (suffix.charAt(i) > query.charAt(i)) {
                    // This suffix is lexicographically greater than query
                    r = m;
                    foundQuery = false;
                    break;
                }
            }

            if (foundQuery) {
                return this.sa[m];
            }
        }

        return -1;
    }
}
