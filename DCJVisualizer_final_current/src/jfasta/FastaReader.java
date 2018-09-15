package jfasta;



import jfasta.extractor.FastaExtractor;
import jfasta.model.DBFastaSequence;
import jfasta.utils.FastaAccessionPattern;
import jfasta.utils.FileUtils;

import java.io.File;
import java.net.URL;
import java.util.List;


public class FastaReader {

    /**
     * Source File in Fasta Format.
     */
    private File sourceFile = null;

    /**
     * The extractor File Reader
     */
    private FastaExtractor extractor  = null;

    /**
     * Default Constructor
     */
    public FastaReader(URL url) {
        sourceFile = FileUtils.getFileFromURL(url);
    }

    /**
     * This constructor should be called to access Pride XML on the Internet.
     *
     * @param url URL of the PRIDE xml file.
     */

    /**
     * Create a fasta file from URL
     * @param url
     * @param mode
     */
    public FastaReader(URL url, String mode){
        this(FileUtils.getFileFromURL(url), mode);
    }

    /**
     * Create a Fasta File from File
     * @param fastaFile
     * @param mode
     */
    public FastaReader(File fastaFile, String mode) {
        // create extractor
        this.extractor = new FastaExtractor(fastaFile,mode);
    }

    /**
     * Get the original input xml source file.
     *
     * @return File input file object
     */
    public File getSourceFile() {
        return extractor.getSourceFile();
    }

    public int size(){
        return extractor.size();
    }

    public DBFastaSequence get(String index){
        return extractor.getSequence(index);
    }


    public void write(DBFastaSequence sequence) {
        extractor.write(sequence);
    }

    public List<DBFastaSequence> get(int start, int end){
        return extractor.get(start,end);
    }

    public void readFastaFile(){
        extractor.readFastaFile();
    }

    public void setHeaderPattern(FastaAccessionPattern fastaAccessionPattern){
        extractor.setFastaAccessionPattern(fastaAccessionPattern);
    }

    public List<String> getDBFastaSequenceIds(){
        return extractor.getDBFastaSequenceIds();
    }


}
