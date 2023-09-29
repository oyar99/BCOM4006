package uniandes.algorithms.fmindex;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import ngsep.sequences.QualifiedSequence;
import ngsep.sequences.QualifiedSequenceList;
import ngsep.sequences.RawRead;
import ngsep.sequences.io.FastaSequencesHandler;
import ngsep.sequences.io.FastqFileReader;

public class EntryPoint {
    /**
     * Main class that executes the program
     * 
     * @param args Array of arguments:
     *             args[0]: Sequence in FASTA format. If many sequences are
     *             present, it only considers the first sequence.
     *             args[1]: Reads in FASTQ format that we will search in the
     *             sequence.
     *             args[2]: Command to execute: "SuffixArray" or "FM"
     *             args[3]: Path to the output file where it lists the matches
     *             for every read in the FASTQ file.
     * 
     * @throws Exception If it fails to load either the FASTA or FASTQ file, or they
     *                   cannot be correctly parsed.
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 4) {
            throw new Exception(
                    "The number arguments is not correct. We expect 3 arguments." +
                            "Please review the documentation");
        }
        String fastaFile = args[0];
        String fastQFile = args[1];
        String command = args[2];
        String outputPath = args[3];
        boolean optimizeSuffixArray = args.length > 4 ? Boolean.parseBoolean(args[4]) : false;

        // Read Fasta file and extract first sequence to string variable
        FastaSequencesHandler handler = new FastaSequencesHandler();
        handler.setSequenceType(StringBuilder.class);
        QualifiedSequenceList sequences = handler.loadSequences(fastaFile);
        if (sequences.size() == 0) {
            throw new Exception("No sequences found in file: " + fastaFile);
        }

        QualifiedSequence seq = sequences.get(0);
        String sequence = seq.getCharacters().toString();

        // Read FASTQ file with reads information
        ArrayList<RawRead> reads = processFastq(fastQFile);

        // For each read, we will search it in the original sequence and write the index
        // of the starting position of the matches to the output file
        FileWriter fileWriter = new FileWriter(new File(outputPath));

        if (command.equals("SuffixArray")) {
            // Construct suffix array
            SuffixArray sa = new SuffixArray(sequence, optimizeSuffixArray);

            for (RawRead read: reads) {
                int startIndex = sa.search(read.getSequenceString());

                if (startIndex == -1) {
                    fileWriter.append("Read:" + read.getSequenceString() +
                        " not found.\n");
                    continue;
                }

                fileWriter.append("Read:" + read.getSequenceString() +
                        " found at position: " + startIndex + "\n");
            }

            fileWriter.close();

        } else if (command.equals("FM")) {
            // Construct FM-Index
            FMIndex fmIndex = new FMIndex(sequence, optimizeSuffixArray);

            for (RawRead read: reads) {
                int[] indices = fmIndex.search(read.getSequenceString());

                if (indices.length == 0) {
                    fileWriter.append("Read:" + read.getSequenceString() +
                        " not found.\n");
                    continue;
                }

                fileWriter.append("Read:" + read.getSequenceString() +
                        " found at position: ");

                int count = 0;
                for (int i: indices) {
                    ++count;
                    fileWriter.append(String.valueOf(i));

                    if (count != indices.length) {
                        fileWriter.append(" - ");
                    }
                }

                fileWriter.append("\n");
            }

            fileWriter.close();
        } else {
            throw new Exception("The command provided is not supported.");
        }
    }

    /**
     * Process the reads stored in the given fastq file
     * 
     * @param filename Name of the file to load. The file can be gzip compressed
     *                 but then the
     *                 extension must finish with ".gz"
     * 
     * @return Array of reads processed from the fastq file
     * 
     * @throws IOException If there is an error reading the file
     */
    public static ArrayList<RawRead> processFastq(String filename) throws IOException {
        ArrayList<RawRead> reads = new ArrayList<>();

        try (FastqFileReader reader = new FastqFileReader(filename)) {
            reader.setLoadMode(FastqFileReader.LOAD_MODE_MINIMAL);
            Iterator<RawRead> it = reader.iterator();
            while (it.hasNext()) {
                RawRead read = it.next();
                reads.add(read);
            }
        }

        return reads;
    }
}
