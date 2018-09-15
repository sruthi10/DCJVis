package jfasta.utils;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Regular expression pattern and url for protein accessions.
 * <p/>
 * User: ypriverol
 * Date: June 2014
 * Time: 10:12:32
 */
public enum ProteinAccessionPattern {
    /**
     * Ensembl accession
     */
    ENSEMBL("ensemble", Pattern.compile("sp\\|([O,P,Q][0-9][A-Z, 0-9][A-Z, 0-9][A-Z, 0-9][0-9])\\|.*"),Pattern.compile("ENSP[A-Z,0-9]+"),new MessageFormat("http://www.ensembl.org/common/psychic?site=&species=&q={0}")),
    /**
     * FlyBase identifier numbers have the general form FBxxnnnnnnn where xx is an
     * alphabetical code for the identifier class and nnnnnnn is a 7 digit number, padded with leading zeros.
     */
    FLYBASE("flybase",Pattern.compile("sp\\|([O,P,Q][0-9][A-Z, 0-9][A-Z, 0-9][A-Z, 0-9][0-9])\\|.*"),Pattern.compile("FB[a-z,A-Z]{2}[\\d]{7}"),
            new MessageFormat("http://flybase.bio.indiana.edu/cgi-bin/uniq.html?species=Dmel&field=all&db=fbgn&caller=quicksearch&context={0}")),
    /**
     * IPI accession without version
     */
    IPI_WITHOUT_VERSION("ipi_no_version", Pattern.compile("sp\\|([O,P,Q][0-9][A-Z, 0-9][A-Z, 0-9][A-Z, 0-9][0-9])\\|.*"),Pattern.compile("IPI[^\\.]+"),
            new MessageFormat("http://srs.ebi.ac.uk/srsbin/cgi-bin/wgetz?-e+[IPI-acc:{0}]")),
    /**
     * IPI accession with version
     */
    IPI_WITH_VERSION("ipi_version",Pattern.compile("sp\\|([O,P,Q][0-9][A-Z, 0-9][A-Z, 0-9][A-Z, 0-9][0-9])\\|.*"),Pattern.compile("IPI[\\d]+.[\\d]?"),
            new MessageFormat("http://srs.ebi.ac.uk/srsbin/cgi-bin/wgetz?-e+[IPI:{0}]")),
    /**
     * RefSeq accession numbers can be distinguished from GenBank accessions by their distinct prefix format of
     * 2 characters followed by an underscore character ('_'). For example, a RefSeq protein accession is NP_015325.
     */
    REFSEQ("refseq",Pattern.compile("sp\\|([O,P,Q][0-9][A-Z, 0-9][A-Z, 0-9][A-Z, 0-9][0-9])\\|.*"),Pattern.compile("[A-Z]{2}_[\\d]+"),
            new MessageFormat("http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=protein&val={0}")),
    /**
     * GI number should be all numbers
      */
    GI("gi",Pattern.compile("sp\\|([O,P,Q][0-9][A-Z, 0-9][A-Z, 0-9][A-Z, 0-9][0-9])\\|.*"),Pattern.compile("[\\d]+"),
            new MessageFormat("http://www.ncbi.nlm.nih.gov/protein/{0}")),
    /**
     * SGD for example: S000005574
     */
    SGD("sgd", Pattern.compile("sp\\|([O,P,Q][0-9][A-Z, 0-9][A-Z, 0-9][A-Z, 0-9][0-9])\\|.*"),Pattern.compile("S[\\d]{9}"),
            new MessageFormat("http://db.yeastgenome.org/cgi-bin/locus.pl?locus={0}")),
    /**
     * This will match both uniprot accession and uniprot isoform accession
     */
    SWISSPROT_ID("swissprot",Pattern.compile("sp\\|([O,P,Q,A-G][0-9][A-Z, 0-9][A-Z, 0-9][A-Z, 0-9][0-9])\\|.*"),Pattern.compile("(?:[OPQ][0-9][A-Z0-9]{3}[0-9]|[A-NR-Z][0-9][A-Z][A-Z0-9]{2}[0-9])(-[0-9]+)?"),
            new MessageFormat("http://www.uniprot.org/entry/{0}")),

    /**
     * This will match SWISSPROT entry name
     */
    SWISSPROT_ENTRY_NAME("swissprot_name", Pattern.compile("sp\\|([O,P,Q][0-9][A-Z, 0-9][A-Z, 0-9][A-Z, 0-9][0-9])\\|.*"),Pattern.compile("[a-zA-Z0-9]{1,5}_[a-zA-Z0-9]{1,5}"),
            new MessageFormat("http://www.uniprot.org/entry/{0}")),
    /**
     * TAIR's accession is normally in the format of AT5G25950.1
     */
    TAIR_ARABIDOPSIS("tair_arabidosis", Pattern.compile("sp\\|([O,P,Q][0-9][A-Z, 0-9][A-Z, 0-9][A-Z, 0-9][0-9])\\|.*"),Pattern.compile("AT[1-9,A-Z]G.*"),
            new MessageFormat("http://www.arabidopsis.org/servlets/Search?sub_type=protein&type=general&search_action=detail&method=1&name={0}")),
    /**
     * Wormbase accession, for example: WP:CE28239
     */
    WORMBASE("wormbase", Pattern.compile("sp\\|([O,P,Q][0-9][A-Z, 0-9][A-Z, 0-9][A-Z, 0-9][0-9])\\|.*"),Pattern.compile("WP:.+"),
            new MessageFormat("http://www.wormbase.org/db/seq/protein?name={0};class=Protein")),
    /**
     * UNIPARC
     */
    UNIPARC("uniparc", Pattern.compile("sp\\|([O,P,Q][0-9][A-Z, 0-9][A-Z, 0-9][A-Z, 0-9][0-9])\\|.*"),Pattern.compile("UPI.+"),
            new MessageFormat("http://www.uniprot.org/uniparc/{0}")),
    /**
     * EMBL
     */
    EMBL("embl", Pattern.compile("sp\\|([O,P,Q][0-9][A-Z, 0-9][A-Z, 0-9][A-Z, 0-9][0-9])\\|.*"),Pattern.compile("[A-Z]{3}[0-9]{5}\\.[0-9]+"),
            new MessageFormat("http://srs.ebi.ac.uk/srsbin/cgi-bin/wgetz?-e+[emblcds-ID:''{0}'']"));

    private String  name;
    private Pattern idPatternFasta;
    private Pattern idPatternService;
    private MessageFormat urlPattern;

    private ProteinAccessionPattern(String name, Pattern idPatternFasta, Pattern idPattern, MessageFormat urlPattern) {
        this.idPatternFasta = idPatternFasta;
        this.idPatternService = idPattern;
        this.urlPattern = urlPattern;
        this.name = name;

    }

    public Pattern getIdPatternFasta() {
        return idPatternFasta;
    }

    public Pattern getIdPatternService() {
        return idPatternService;
    }

    public MessageFormat getUrlPattern() {
        return urlPattern;
    }

    public String getName() {
        return name;
    }

    /**
     * Check whether it is a swissprot accession
     *
     * @param acc protein accession
     * @return boolean true if it's a swissprot accession
     */
    public static boolean isSwissprotFastaAccession(String acc) {
        return isMatchAccession(SWISSPROT_ID.getIdPatternFasta(), acc);
    }

    /**
     * Check whether it is a swissprot entry name
     * @param name  entry name
     * @return boolean  true if tis a swissprot entry name
     */
    public static boolean isSwissprotFastaEntryName(String name) {
        return isMatchAccession(SWISSPROT_ENTRY_NAME.getIdPatternFasta(), name);
    }

    /**
     * Check whether it is a Uniparc accession
     *
     * @param acc protein accession
     * @return boolean true if it's a swissprot accession
     */
    public static boolean isUniparcFastaAccession(String acc) {
        return isMatchAccession(UNIPARC.getIdPatternFasta(), acc);
    }

    /**
     * Check whether it is a REFSEQ accession
     *
     * @param acc protein accession
     * @return boolean true if it's a REFSEQ accession
     */
    public static boolean isRefseqFastaAccession(String acc) {
        return isMatchAccession(REFSEQ.getIdPatternFasta(), acc);
    }

    /**
     * Check whether it is a GI accession.
     *
     * @param acc   protein accession
     * @return  boolean true if it is a GI accession.
     */
    public static boolean isGIFastaAccession(String acc) {
        return isMatchAccession(GI.getIdPatternFasta(), acc);
    }

    /**
     * Check whether it is a IPI accession without version
     *
     * @param acc protein accession
     * @return boolean true if it's a IPI accession
     */
    public static boolean isIPIFastaAccessionWithoutVersion(String acc) {
        return isMatchAccession(IPI_WITHOUT_VERSION.getIdPatternFasta(), acc);
    }

    /**
     * check whether it is an IPI accession with version number
     * @param acc   protein accession
     * @return  boolean true if it's a IPI accession
     */
    public static boolean isIPIFastaAccessionWithVersion(String acc) {
        return isMatchAccession(IPI_WITH_VERSION.getIdPatternFasta(), acc);
    }

    /**
     * Check whether it is a ensembl accession
     *
     * @param acc protein accession
     * @return boolean true if it's a swissprot accession
     */
    public static boolean isEnsemblFastaAccession(String acc) {
        return isMatchAccession(ENSEMBL.getIdPatternFasta(), acc);
    }

    /**
     * Check whether a protein accession matches the given pattern
     *
     * @param pattern protein accession pattern
     * @param acc     protein accession
     * @return boolean  true if matched
     */
    private static boolean isMatchAccession(Pattern pattern, String acc) {
        Matcher matcher = pattern.matcher(acc);
        return matcher.matches();
    }
}
