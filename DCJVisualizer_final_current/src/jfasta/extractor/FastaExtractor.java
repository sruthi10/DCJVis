package jfasta.extractor;

import jfasta.fastaindex.IndexElement;

import jfasta.fastaindex.IndexElementImpl;
import jfasta.model.DBFastaSequence;
import jfasta.utils.FastaAccessionPattern;
import jfasta.utils.ProteinAccessionPattern;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FastaExtractor {

    private static final Logger logger = Logger.getLogger(FastaExtractor.class.getName());

    /**
     * A fasta Accession List of Elements
     */

    FastaAccessionPattern fastaAccessionPattern = null;

    /**
     * DBSequence Map
     */

    private Map<String, IndexElement> dbSequenceMap = null;

    /**
     * An array of keys to iterate thought all the elements
     */
    private ArrayList<String> keys = null;

    /**
     * Input Fasta file
     */

    private File fastaFile = null;

    /**
     * For File with redundant sequences (Sequences with equal id)
     */
    private int idSequence = 0;

    /**
     * The default mode to open a file is read only
     */
    private String fileMode = "r";


    public void setFastaAccessionPattern(FastaAccessionPattern fastaAccessionPattern) {
        this.fastaAccessionPattern = fastaAccessionPattern;
    }

    /**
     * Create a Fasta Extractor File with a Fasta File and the read mode ("r" or "rw");
     * @param fastaFile
     * @param mode
     */
    public FastaExtractor(File fastaFile, String mode) {

        if (fastaFile == null) {
            throw new IllegalArgumentException("Fasta file to be indexed must not be null");
        } else if (!fastaFile.exists() && mode == "r") {
            try{
                boolean fileCreation = fastaFile.createNewFile();
            }catch (IOException e){
                throw new IllegalArgumentException("Error creating the Fasta File: " + fastaFile.getAbsolutePath() + "\n");
            }

        }else if(!fastaFile.exists()){
            throw new IllegalArgumentException("Fasta File do not exist: " + fastaFile.getAbsolutePath() + "\n");

        }
        this.fastaFile = fastaFile;
        this.fileMode = mode;
    }

    /**
     * This file create the index for the fasta file, it creates a braf file and
     * initialize the cache system with all the fasta entries.
     */
    public void readFastaFile(){
        try{
            BufferedRandomAccessFile braf = new BufferedRandomAccessFile(fastaFile.getAbsolutePath(), fileMode, 1024 * 100);
            initializeCaches(braf);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error indexing the Fasta File: " + fastaFile.getAbsolutePath() + "\n");

        }
    }

    /**
     * Get Source File

     *  @return
     */
    public File getSourceFile() {
        return fastaFile;
    }

    /**
     * Initialize the Fasta File Index
     * @param braf A File
     *      * @param idmod
     * @throws java.io.IOException
     */
    private void initializeCaches(BufferedRandomAccessFile braf) throws IOException {
        String line = null;
        dbSequenceMap = new HashMap<String, IndexElement>();
        long startPosition = 0;
        long lastPosition  = 0;
        String currentId   = null;
        while ((line = braf.getNextLine()) != null) {
            if(line.charAt(0) == '>'){
                String header = line.replaceFirst(">","");
                if(lastPosition != 0){
                    int size  = (int) (lastPosition - startPosition);
                    dbSequenceMap.put(currentId,new IndexElementImpl(startPosition,size));
                    startPosition = lastPosition;
                }
                if(fastaAccessionPattern != null){
                    currentId = getSequenceAccession(header);
                    if(currentId == null){
                        throw new IllegalArgumentException("Current header: " + header + " do not match the any patterns\n");
                    }
                }else{
                    currentId = Integer.toString(idSequence);
                    idSequence++;
                }
            }
            if(line.length() != 0){
                lastPosition = braf.getFilePointer();
            }
        }
        int size = (int) (lastPosition - startPosition);
        dbSequenceMap.put(currentId,new IndexElementImpl(startPosition,size));
        keys = (dbSequenceMap != null)? new ArrayList<String>(dbSequenceMap.keySet()):null;
    }

    /**
     * This function get for an Accession string for a header that mach an
     * specific pattern
     * @param header
     * @return
     */
    private String getSequenceAccession(String header) {
        if(fastaAccessionPattern != null){
            for (ProteinAccessionPattern proteinAccessionPattern: this.fastaAccessionPattern.getPatternIDMap()){
                if(proteinAccessionPattern.getIdPatternFasta() != null){
                    return getPatternMatch(header, proteinAccessionPattern.getIdPatternFasta());
                }
            }
        }
        return null;
    }

    /**
     * Get the number of Sequence in the Fasta File
     * @return
     */
    public int size(){
        return (fastaFile != null)? dbSequenceMap.size():-1;
    }

    /**
     * Get a Sequence for the specific Id
     * @param key
     * @return
     */
    public DBFastaSequence getSequence(String key){
        if(dbSequenceMap != null){
            IndexElement indexElement = dbSequenceMap.get(key);
            DBFastaSequence sequence = loadSequenceFromFile(indexElement);
            return sequence;
        }
        return null;

    }

    /**
     * Read from the File the indexElement a retrieve the DBSequence, we create a Random Access File
     * in read mode to retrieve the specific Sequence
     * @param indexElement
     * @return
     */
    private DBFastaSequence loadSequenceFromFile(IndexElement indexElement) {
        try {

            RandomAccessFile accFile = new RandomAccessFile(fastaFile, "r");

			// read the indexed element
			byte[] byteBuffer = new byte[indexElement.getSize()];

			// read the file from there
			accFile.seek(indexElement.getStart());
			accFile.read(byteBuffer);

			String DBBuffer = new String(byteBuffer);

			// create the query
			DBFastaSequence query = readDBSequence(DBBuffer);

			accFile.close();

			return query;
		}
		catch (FileNotFoundException e) {

		}
		catch (IOException e) {
		}
        return null;
	}

    /**
     * Create a DBFastaSequence from an String Buffer
     * @param dbBuffer
     * @return DBFastaSequence Sequence in Fasta Format
     */
    private DBFastaSequence readDBSequence(String dbBuffer) {
        // process the mgf section line by line
		String[] lines = dbBuffer.trim().split("\n");

        //Remove the First > if exits
        String header = lines[0].replaceFirst(">","");

        String accession = getSequenceAccession(lines[0]);

        String sequence  = "";

        for(int i = 1; i < lines.length;i++) sequence = sequence+lines[i];

        sequence.replaceAll("\\s", "");

        return new DBFastaSequence(accession, header, sequence.length(),sequence,true,null);

    }

    /**
     * Get the information from an String line using a patter, if the pattern is null the result will
     * be null. If the string do not match the pattern the program will show an error message.
     * @param header line that contains the information
     * @param pattern the pattern
     * @return
     * @throws IllegalStateException
     */
    private String getPatternMatch(String header, Pattern pattern) throws IllegalStateException{
        if(pattern != null){
            Matcher m = pattern.matcher(header);
            if (m.find()) {
                return m.group(1);
            }
        }
        return null;
    }

    /**
     * Get the DBSequence with the nIndex in the file
     * @param nIndex
     */
    public DBFastaSequence get(int nIndex) throws ArrayIndexOutOfBoundsException{
        if(keys != null && nIndex < keys.size()){
            loadSequenceFromFile(dbSequenceMap.get(keys.get(nIndex)));
        }else{
            throw new ArrayIndexOutOfBoundsException("The index must be less or equal than the number of Sequence in the File");
        }
        return null;
    }

    /**
     * Retrieve a set of Sequence from one index (start) to another (end).
     * @param start The initial index to retrieve a list of sequence
     * @param end   The final index to retrieve a list of sequence
     * @return
     */
    public List<DBFastaSequence> get(int start, int end){
        if(keys !=null && ((start != 0) && (start < keys.size()) && (end < keys.size()))){
            ArrayList<DBFastaSequence> dbFastaSequenceList = new ArrayList<DBFastaSequence>(end - start);
            for (int i = start; i == end; i++){
                dbFastaSequenceList.add(loadSequenceFromFile(dbSequenceMap.get(keys.get(i))));
            }
            return dbFastaSequenceList;
        }
        return null;
    }


    /**
     * Return the keys of the DBFastaSequence in the Map
     * @return
     */
    public List<String> getDBFastaSequenceIds(){
        return keys;
    }

    /**
     *
     * @param sequence
     */
    public void write(DBFastaSequence sequence){
        try{
            RandomAccessFile accFile = new RandomAccessFile(fastaFile, "rw");
            accFile.seek(accFile.length());
            accFile.writeBytes(">" + sequence.getHeader() + "\n");
            accFile.writeBytes(sequence.getSequence() + "\n");//writes 1-byte char
            accFile.close();
        }catch (FileNotFoundException e){

        }catch (IOException e){

        }
    }


}
