package jfasta.model;

/**
 * This class is a DBSequence in fasta format. This class allow to retrieve
 * the information from fasta files (Swissprot, NCBI, etc).
 *

 */
public class DBFastaSequence {

    /**
     * The unique accession of this sequence.
     */
    private String accessionId = null;

    /**
     * protein accession version
     */
    private String header = null;

    /**
     * The length of the sequence as a number of bases or residues.
     */
    private int length = -1;

    /**
     * The actual sequence of amino acids or nucleic acid.
     */
    private String sequence = null;

    /**
     * If the sequence is nucleotide or aminoacid
     */

    private boolean aminoacidBased = true;

    /**
     * Taxonomy
     */

    private String taxonomy = null;

    public DBFastaSequence(String accessionId, String header, int length, String sequence, boolean aminoacidBased, String taxonomy) {
        this.accessionId = accessionId;
        this.header = header;
        this.length = length;
        this.sequence = sequence;
        this.aminoacidBased = aminoacidBased;
        this.taxonomy = taxonomy;
    }

    public String getAccessionId() {
        return accessionId;
    }

    public void setAccessionId(String accessionId) {
        this.accessionId = accessionId;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(String taxonomy) {
        this.taxonomy = taxonomy;
    }

    public boolean isAminoacidBased() {

        return aminoacidBased;
    }

     /**
     * The id is the part of the Fasta entry that identified the Sequence
     * Some fasta files have the concept of accession for each entry, but it mus be accessed
     * using different pattern.
     * @return
     */
    public String getId(){
        String[] headerLines = header.split(" ");
        return headerLines[0];
    }

    public String getDescription(){
        String[] headerLines = header.split(" ");
        String description = "";
        for(int i = 1; i < headerLines.length; i++){
            if(i+1 < headerLines.length)
                description = description + headerLines[i] + " ";
            else
                description = description + headerLines[i];
        }
        return description;
    }
}
