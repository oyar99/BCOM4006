package uniandes.algorithms.readsanalyzer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ngsep.sequences.RawRead;

/**
 * Stores abundances information on a list of subsequences of a fixed length k
 * (k-mers)
 * 
 * @author Jorge Duitama
 */
public class KmersTable implements RawReadProcessor {

	private Map<String, Integer> kmersTable;
	private int kmerSize;

	/**
	 * Creates a new table with the given k-mer size
	 * 
	 * @param kmerSize length of k-mers stored in this table
	 */
	public KmersTable(int kmerSize) {
		this.kmerSize = kmerSize;
		this.kmersTable = new HashMap<>();
	}

	/**
	 * Identifies k-mers in the given read
	 * 
	 * @param read object to extract new k-mers
	 */
	public void processRead(RawRead read) {
		String sequence = read.getSequenceString();

		// Use a Map to store the count of each k-mer. That is,
		// kmersTable[kmer] will correspond to the number of times
		// kmer appears in the sequence S.

		// For a sequence of length S. There are S - K + 1 k-mers
		for (int i = 0; i < sequence.length() - this.kmerSize + 1; ++i) {
			String kmer = sequence.substring(i, i + this.kmerSize);

			this.kmersTable.put(kmer, this.kmersTable.getOrDefault(kmer, 0) + 1);
		}
	}

	/**
	 * List with the different k-mers found up to this point
	 * 
	 * @return Set<String> set of k-mers
	 */
	public Set<String> getDistinctKmers() {
		// Returns a new copy of the hash keys - so changes in the returned data
		// structure are not reflected in the actual kmers-table.
		return new HashSet<>(this.kmersTable.keySet());
	}

	/**
	 * Calculates the current abundance of the given k-mer
	 * 
	 * @param kmer sequence of length k
	 * @return int times that the given k-mer have been extracted from given reads
	 */
	public int getAbundance(String kmer) {
		return this.kmersTable.getOrDefault(kmer, 0);
	}

	/**
	 * Calculates the distribution of abundances
	 * 
	 * @return int [] array where the indexes are abundances and the values are the
	 *         number of k-mers
	 *         observed as many times as the corresponding array index. Position
	 *         zero should be equal to zero
	 */
	public int[] calculateAbundancesDistribution() {
		// If the k-mers table is empty, then we return a 1-length array filled with
		// zero.
		if (this.kmersTable.isEmpty()) {
			return new int[1];
		}
		// First, let us determine the maximum abundance for a k-mer in the k-mers table
		// so we can then create a fixed-length array
		int maxAbundance = Integer.MIN_VALUE;
		
		int sum = 0;

		// We need a reverse hash table to determine for a given abundance the number of
		// k-mers that appear that many times in the original sequence.
		Map<Integer, Integer> abundancesTable = new HashMap<>();

		for (int abundance : this.kmersTable.values()) {
			abundancesTable.put(abundance, abundancesTable.getOrDefault(abundance, 0) + 1);
			maxAbundance = Math.max(abundance, maxAbundance);
			sum += abundance;
		}
		
		double mean = (double) sum / kmersTable.size();
		
		double redondearMedia = Double.parseDouble(String.format("%.3f", mean));
		
		System.out.println("La abundancia media es:"+ redondearMedia);
		
		int[] abundances = new int[maxAbundance + 1];

		for (int i = 0; i < abundances.length; ++i) {
			abundances[i] = abundancesTable.getOrDefault(i, 0);
		}

		return abundances;
	}
}
