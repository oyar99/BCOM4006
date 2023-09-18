package uniandes.algorithms.readsanalyzer;

import java.io.IOException;

import java.io.FileWriter;

import java.nio.file.*;

import java.util.Iterator;
import java.util.Set;

import ngsep.sequences.RawRead;
import ngsep.sequences.io.FastqFileReader;
/**
 * Example program to build simple overlap graphs or to build k-mer tables from raw reads
 * @author Jorge Duitama
 *
 */
public class ReadsAnalyzerExample {
	public static final String COMMAND_OVERLAP = "Overlap";
	public static final String COMMAND_KMERS = "Kmers";
	/**
	 * Main class that executes the program
	 * @param args Array of arguments:
	 * args[0]: Command to execute. It can be "Overlap" or "Kmers"
	 * args[1]: Fastq file with the reads to be processed
	 * args[2]: Optional. For "Overlap" is the minimum overlap length. For "Kmers" is the k-mer length
	 * @throws Exception If the reads can not be loaded
	 */
	public static void main(String[] args) throws Exception {
		if(args.length<2) {
			System.err.println("Command and input file are mandatory parameters");
			return;
		}
		int kmerSize = 75;
		int minOverlap = 1;
		String command = args[0];

		String fastqFilename = args[1];

		String basePath = args[3];
		
		if(COMMAND_OVERLAP.equals(command)&& args.length>2) {
			minOverlap = Integer.parseInt(args[2]);
			
	
		} else if(COMMAND_KMERS.equals(command) && args.length>2) {
			kmerSize = Integer.parseInt(args[2]);
			
		}
		
		if(COMMAND_OVERLAP.equals(command)) {
			//Build overlap graph
			long time = System.currentTimeMillis();
			OverlapGraph graph = new OverlapGraph (minOverlap);
			processFastq(fastqFilename, graph);
			time = System.currentTimeMillis()-time;
			
			//Calculate statistics
			Set<String> sequencesList = graph.getDistinctSequences();
			
			for(String sequence:sequencesList) System.out.println("The sequence: "+sequence+" is present "+graph.getSequenceAbundance(sequence)+" times");
			System.out.println("Abundances distribution");

			Path fullPath_abundance = Paths.get(basePath, "abundance.txt");
			Path fullPath_overlapping = Paths.get(basePath, "overlapping.txt");
			
			try {
			    FileWriter fileWriter = new FileWriter(fullPath_abundance);
			    
			    int [] distribution = graph.calculateAbundancesDistribution();
				for(int i=1;i<distribution.length;i++) {
					
					String line = i + "\t" + distribution[i];
					
					//System.out.println(line);
					
					fileWriter.write(line + "\n");
				}
				
			    fileWriter.close(); 
			} catch (IOException e) {
			    e.printStackTrace(); 
			}
			
			
			try {
			    FileWriter fileWriter = new FileWriter(fullPath_overlapping);
			    
			    System.out.println("Overlap distribution");
				int [] ovdist = graph.calculateOverlapDistribution();
				for(int i=0;i<ovdist.length;i++) {
					String line = i + "\t" + ovdist[i];
					//System.out.println(line);
					fileWriter.write(line + "\n");
				}
				
			    fileWriter.close(); 
			} catch (IOException e) {
			    e.printStackTrace(); 
			}
				
			
			//Assemble sequence
			
			System.out.println("Time building overlap graph(ms): "+time);
			System.out.println("Number of distinct sequences: "+sequencesList.size());
			
			time = System.currentTimeMillis();
			String assembly = graph.getAssembly();
			time = System.currentTimeMillis()-time;
			System.out.println("Time assembling sequence(ms): "+time);
			
			System.out.println("Assembly:");
			System.out.println(assembly);
			
		} else if(COMMAND_KMERS.equals(command)) {
			//Build k-mers table
			long time = System.currentTimeMillis();
			KmersTable kmersTable = new KmersTable(kmerSize);
			processFastq(fastqFilename, kmersTable);
			time = System.currentTimeMillis()-time;
			System.out.println("Time building k-mers table(ms): "+time);
			
			//Calculate statistics
			Set<String> kmers = kmersTable.getDistinctKmers();
			System.out.println("Total number of k-mers: "+kmers.size());
			
			
			try {
			    FileWriter fileWriter = new FileWriter("/Users/juanitapuentes/Desktop/Decimo Semestre/Algoritmos Biologia Computacional/Tareas/Tarea2/ISIS4006/Tarea2/ReadsAnalyzer/Results/kmers/100x/kmer_data_75.txt");
			    
			    System.out.println("Abundance distribution");
				
			    
				for(String kmer:kmers) {
					
					
					int abundance = kmersTable.getAbundance(kmer);
					
					String line = kmer + "\t" + abundance;
					
					//System.out.println("The k-mer "+kmer+" is present "+abundance+" times");
					
					fileWriter.write(line + "\n");
				}
				
			    fileWriter.close(); 
			} catch (IOException e) {
			    e.printStackTrace(); 
			}
			
			
			try {
			    FileWriter fileWriter = new FileWriter("/Users/juanitapuentes/Desktop/Decimo Semestre/Algoritmos Biologia Computacional/Tareas/Tarea2/ISIS4006/Tarea2/ReadsAnalyzer/Results/kmers/100x/kmer_distribution_75.txt");
			    
			    System.out.println("Distribution");
				
			    int [] distribution = kmersTable.calculateAbundancesDistribution();
			    
				for(int i=1;i<distribution.length;i++) {
					
					
					String line = i + "\t" + distribution[i];
					
					//System.out.println(line);
					
	                fileWriter.write(line + "\n");
					

				}
				
			    fileWriter.close(); 
			} catch (IOException e) {
			    e.printStackTrace(); 
			}

		}
	}
	/**
	 * Process the reads stored in the given fastq file
	 * @param filename Name of the file to load. The file can be gzip compressed but then the
	 * extension must finish with ".gz"
	 * @param processor Object able to operate with raw reads
	 * @throws IOException If there is an error reading the file
	 */
	public static void processFastq(String filename, RawReadProcessor processor) throws IOException {
		try (FastqFileReader reader = new FastqFileReader(filename)) {
			reader.setLoadMode(FastqFileReader.LOAD_MODE_MINIMAL);
			Iterator<RawRead> it = reader.iterator();
			while(it.hasNext()) {
				RawRead read = it.next();
				processor.processRead(read);
			}
		}
	}
}
