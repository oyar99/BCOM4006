package uniandes.algorithms.fmindex;

import java.util.Arrays;

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

    public SuffixArray(String sequence) {
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

    /**
     * Uses binary search to find an occurrence of query in the sequence
     * 
     * @param query String that we will search in the sequence
     * 
     * @return the index of an occurrence of query in the sequence. -1, if no match
     * is found.
     */
    public int search(String query) {
        int l = 0;
        int r = this.sa.length;

        while (l < r) {
            int m = (l + r) / 2;

            String suffix = this.sequence.substring(this.sa[m]);

            boolean foundQuery = true;

            for (int i = 0; i < query.length(); ++i) {
                if (i >= suffix.length() 
                    || suffix.charAt(i) < query.charAt(i)
                ) {
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
