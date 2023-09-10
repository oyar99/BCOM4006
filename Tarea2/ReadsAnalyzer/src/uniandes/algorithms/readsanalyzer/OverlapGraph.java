package uniandes.algorithms.readsanalyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import ngsep.sequences.RawRead;

/**
 * Represents an overlap graph for a set of reads taken from a sequence to
 * assemble
 * 
 * @author Jorge Duitama
 *
 */
public class OverlapGraph implements RawReadProcessor {

	private int minOverlap;
	private Map<String, Integer> readCounts = new HashMap<>();
	private Map<String, ArrayList<ReadOverlap>> overlaps = new HashMap<>();

	/**
	 * Creates a new overlap graph with the given minimum overlap
	 * 
	 * @param minOverlap Minimum overlap
	 */
	public OverlapGraph(int minOverlap) {
		this.minOverlap = minOverlap;
	}

	/**
	 * Adds a new read to the overlap graph
	 * 
	 * @param read object with the new read
	 */
	public void processRead(RawRead read) {
		String sequence = read.getSequenceString();
		// TODO: Paso 1. Agregar la secuencia al mapa de conteos si no existe.
		// Si ya existe, solo se le suma 1 a su conteo correspondiente y no se deben
		// ejecutar los pasos 2 y 3
		this.readCounts.put(sequence, this.readCounts.getOrDefault(sequence, 0) + 1);

		if (this.readCounts.get(sequence) > 1) {
			return;
		}

		// TODO: Paso 2. Actualizar el mapa de sobrelapes con los sobrelapes en los que
		// la secuencia nueva sea predecesora de una secuencia existente
		// 2.1 Crear un ArrayList para guardar las secuencias que tengan como prefijo un
		// sufijo de la nueva secuencia
		// 2.2 Recorrer las secuencias existentes para llenar este ArrayList creando los
		// nuevos sobrelapes que se encuentren.
		// 2.3 Después del recorrido para llenar la lista, agregar la nueva secuencia
		// con su lista de sucesores al mapa de sobrelapes
		ArrayList<ReadOverlap> newOverlaps = new ArrayList<>();

		for (String other : this.readCounts.keySet()) {
			int overlapLength = this.getOverlapLength(sequence, other);

			if (overlapLength >= minOverlap) {
				newOverlaps.add(new ReadOverlap(sequence, other, overlapLength));
			}
		}

		this.overlaps.put(sequence, newOverlaps);

		// TODO: Paso 3. Actualizar el mapa de sobrelapes con los sobrelapes en los que
		// la secuencia nueva sea sucesora de una secuencia existente
		// Recorrer el mapa de sobrelapes. Para cada secuencia existente que tenga como
		// sufijo un prefijo de la nueva secuencia
		// se agrega un nuevo sobrelape a la lista de sobrelapes de la secuencia
		// existente

		for (Entry<String, ArrayList<ReadOverlap>> entry : this.overlaps.entrySet()) {
			String other = entry.getKey();

			int overlapLength = this.getOverlapLength(other, sequence);

			if (overlapLength >= minOverlap) {
				entry.getValue().add(new ReadOverlap(other, sequence, overlapLength));
			}
		}
	}

	/**
	 * Returns the length of the maximum overlap between a suffix of sequence 1 and
	 * a prefix of sequence 2
	 * 
	 * @param sequence1 Sequence to evaluate suffixes
	 * @param sequence2 Sequence to evaluate prefixes
	 * @return int Maximum overlap between a prefix of sequence2 and a suffix of
	 *         sequence 1
	 */
	private int getOverlapLength(String sequence1, String sequence2) {
		for (int i = Math.max(0, sequence1.length() - sequence2.length()); i < sequence1.length(); ++i) {
			if (sequence1.substring(i).equals(sequence2.substring(0, sequence1.length() - i))) {
				return sequence1.length() - i;
			}
		}

		return 0;
	}

	/**
	 * Returns a set of the sequences that have been added to this graph
	 * 
	 * @return Set<String> of the different sequences
	 */
	public Set<String> getDistinctSequences() {
		// Returns a new copy of the hash keys - so changes in the returned data
		// structure are not reflected in the actual overlap graph.
		return new HashSet<>(this.readCounts.keySet());
	}

	/**
	 * Calculates the abundance of the given sequence
	 * 
	 * @param sequence to search
	 * @return int Times that the given sequence has been added to this graph
	 */
	public int getSequenceAbundance(String sequence) {
		return this.readCounts.getOrDefault(sequence, 0);
	}

	/**
	 * Calculates the distribution of abundances
	 * 
	 * @return int [] array where the indexes are abundances and the values are the
	 *         number of sequences
	 *         observed as many times as the corresponding array index. Position
	 *         zero should be equal to zero
	 */
	public int[] calculateAbundancesDistribution() {
		if (this.readCounts.isEmpty()) {
			return new int[1];
		}

		int maxAbundance = Integer.MIN_VALUE;
		Map<Integer, Integer> abundancesTable = new HashMap<>();

		for (int abundance : this.readCounts.values()) {
			abundancesTable.put(abundance, abundancesTable.getOrDefault(abundance, 0) + 1);
			maxAbundance = Math.max(abundance, maxAbundance);
		}

		int[] abundances = new int[maxAbundance + 1];

		for (int i = 0; i < abundances.length; ++i) {
			abundances[i] = abundancesTable.getOrDefault(i, 0);
		}

		return abundances;
	}

	/**
	 * Calculates the distribution of number of successors
	 * 
	 * @return int [] array where the indexes are number of successors and the
	 *         values are the number of
	 *         sequences having as many successors as the corresponding array index.
	 */
	public int[] calculateOverlapDistribution() {
		if (this.overlaps.isEmpty()) {
			return new int[1];
		}

		int maxSuccessors = Integer.MIN_VALUE;
		Map<Integer, Integer> successorsTable = new HashMap<>();

		for (ArrayList<ReadOverlap> overlap : this.overlaps.values()) {
			successorsTable.put(overlap.size(), successorsTable.getOrDefault(overlap.size(), 0) + 1);
			maxSuccessors = Math.max(overlap.size(), maxSuccessors);
		}

		int[] successors = new int[maxSuccessors + 1];

		for (int i = 0; i < successors.length; ++i) {
			successors[i] = successorsTable.getOrDefault(i, 0);
		}

		return successors;
	}

	/**
	 * Predicts the leftmost sequence of the final assembly for this overlap graph
	 * 
	 * @return String Source sequence for the layout path that will be the left most
	 *         subsequence in the assembly
	 */
	public String getSourceSequence() {
		// TODO Implementar metodo recorriendo las secuencias existentes y buscando una
		// secuencia que no tenga predecesores
		
		// We need to look for a node with in-degree equal to zero
		// Let us compute the in-degree for all sequences
		Map<String, Integer> inDegree = new HashMap<>();

		for (String sequence: this.overlaps.keySet()) {
			inDegree.put(sequence, 0);
		}

		for (ArrayList<ReadOverlap> edges: this.overlaps.values()) {
			for (ReadOverlap overlap: edges) {
				inDegree.put(overlap.getDestSequence(), inDegree.get(overlap.getDestSequence()) + 1);
			}
		}

		int minIndegree = Integer.MAX_VALUE;
		String source = null;

		for (Entry<String, Integer> entry: inDegree.entrySet()) {
			if (entry.getValue() < minIndegree) {
				minIndegree = entry.getValue();
				source = entry.getKey();
			}
		}

		return source;
	}

	/**
	 * Calculates a layout path for this overlap graph
	 * 
	 * @return ArrayList<ReadOverlap> List of adjacent overlaps. The destination
	 *         sequence of the overlap in
	 *         position i must be the source sequence of the overlap in position
	 *         i+1.
	 */
	public ArrayList<ReadOverlap> getLayoutPath() {
		ArrayList<ReadOverlap> layout = new ArrayList<>();
		HashSet<String> visitedSequences = new HashSet<>();
		// TODO Implementar metodo. Comenzar por la secuencia fuente que calcula el
		// método anterior
		// Luego, hacer un ciclo en el que en cada paso se busca la secuencia no
		// visitada que tenga mayor sobrelape con la secuencia actual.
		// Agregar el sobrelape a la lista de respuesta y la secuencia destino al
		// conjunto de secuencias visitadas. Parar cuando no se encuentre una secuencia
		// nueva
		String sourceSequence = this.getSourceSequence();

		String curSequence = sourceSequence;

		while (curSequence != null) {
			// Pick the adjacent sequence with highest overlap
			int maxOverlap = Integer.MIN_VALUE;
			ReadOverlap nextOverlap = null;

			for (ReadOverlap overlap: this.overlaps.get(curSequence)) {
				if (overlap.getOverlap() > maxOverlap && !visitedSequences.contains(overlap.getDestSequence())) {
					maxOverlap = overlap.getOverlap();
					nextOverlap = overlap;
				}
			}

			if (nextOverlap != null) {
				layout.add(nextOverlap);
				visitedSequences.add(nextOverlap.getDestSequence());

				curSequence = nextOverlap.getDestSequence();
			} else {
				curSequence = null;
			}
		}

		return layout;
	}

	/**
	 * Predicts an assembly consistent with this overlap graph
	 * 
	 * @return String assembly explaining the reads and the overlaps in this graph
	 */
	public String getAssembly() {
		ArrayList<ReadOverlap> layout = getLayoutPath();
		StringBuilder assembly = new StringBuilder();
		// TODO Recorrer el layout y ensamblar la secuencia agregando al objeto assembly
		// las bases adicionales que aporta la región de cada secuencia destino que está
		// a la derecha del sobrelape

		if (layout.size() > 0) {
			assembly.append(layout.get(0).getSourceSequence());
		}

		for (ReadOverlap overlap: layout) {
			assembly.append(overlap.getDestSequence().substring(overlap.getOverlap()));
		}

		return assembly.toString();
	}

}
