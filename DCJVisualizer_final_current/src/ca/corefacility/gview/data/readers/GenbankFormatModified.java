package ca.corefacility.gview.data.readers;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.RNATools;
import org.biojava.bio.seq.io.ParseException;
import org.biojava.bio.seq.io.SymbolTokenization;
import org.biojava.bio.symbol.Alphabet;
import org.biojava.bio.symbol.AlphabetManager;
import org.biojava.bio.symbol.IllegalAlphabetException;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.biojava.bio.symbol.SimpleSymbolList;
import org.biojava.bio.symbol.Symbol;
import org.biojava.bio.symbol.SymbolList;
import org.biojava.utils.ChangeVetoException;
import org.biojavax.CrossRef;
import org.biojavax.DocRef;
import org.biojavax.DocRefAuthor;
import org.biojavax.Namespace;
import org.biojavax.RankedCrossRef;
import org.biojavax.RankedDocRef;
import org.biojavax.RichObjectFactory;
import org.biojavax.SimpleCrossRef;
import org.biojavax.SimpleDocRef;
import org.biojavax.SimpleRankedCrossRef;
import org.biojavax.SimpleRankedDocRef;
import org.biojavax.SimpleRichAnnotation;
import org.biojavax.bio.seq.CompoundRichLocation;
import org.biojavax.bio.seq.RichFeature;
import org.biojavax.bio.seq.RichLocation;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.SimplePosition;
import org.biojavax.bio.seq.SimpleRichLocation;
import org.biojavax.bio.seq.io.GenbankFormat;
import org.biojavax.bio.seq.io.GenbankLocationParser;
import org.biojavax.bio.seq.io.RichSeqIOListener;
import org.biojavax.bio.taxa.NCBITaxon;
import org.biojavax.bio.taxa.SimpleNCBITaxon;

/**
 * Code from biojava 1.7.1 org.biojavax.bio.seq.io.GenbankFormat
 * Enables parsing of genbank files which have no sequence data into a sequence object.
 * Fills all sequence data with ambiguity characters (N).
 * Only works for DNA/RNA genbank files. 
 * @author aaron
 *
 */
public class GenbankFormatModified extends GenbankFormat
{
    protected static final Pattern lp = Pattern.compile("^(\\S+)\\s+(\\d+)\\s+(bp|aa)\\s{1,4}([dms]s-)?(\\S+)?\\s+(circular|linear)?\\s*(\\S+)?\\s*(\\S+)?$");
    
    /**
     * 
     * Returns an dna parser if the letters DNA or RNA appear in the first line of the file.
     * Otherwise returns a DNA tokenizer.
     */
    public SymbolTokenization guessSymbolTokenization(File file) throws IOException {
        return guessSymbolTokenization(new BufferedInputStream(new FileInputStream(file)));
    }
    
    /**
     * 
     * A stream is in GenBank format if the first line of the stream starts with the word LOCUS
     */
    public boolean canRead(BufferedInputStream stream) throws IOException {
        stream.mark(2000); // some streams may not support this
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        final String firstLine = br.readLine();
        boolean readable = firstLine!=null && headerLine.matcher(firstLine).matches();
        // don't close the reader as it'll close the stream too.
        // br.close();
        stream.reset();
        return readable;
    }
    
    // modified from Biojava org.biojavax.bio.seq.io.GenbankFormat.guessSymbolokenization
    //  to support upper/lower case strings
    @Override
    public SymbolTokenization guessSymbolTokenization(BufferedInputStream stream) throws IOException
    {
        SymbolTokenization st = RichSequence.IOTools.getProteinParser();
        
        stream.mark(2000);
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String firstLine = br.readLine().toLowerCase();
        if(firstLine.indexOf("dna") >0)
        {
            st = RichSequence.IOTools.getDNAParser();
        }
        else if (firstLine.indexOf("rna") > 0)
        {
            st = RichSequence.IOTools.getRNAParser();
        }

        stream.reset();
        
        return st;
    }
    
    
    private String sectionKey = null;
    private NCBITaxon tax = null;
    private String organism = null;
    private String accession = null;
    private String identifier = null;
    /**
     * 
     */
    public boolean readRichSequence(BufferedReader reader,
            SymbolTokenization symParser,
            RichSeqIOListener rlistener,
            Namespace ns)
            throws IllegalSymbolException, IOException, ParseException {
        
        sectionKey = null;
        tax = null;
        organism = null;
        accession = null;
        identifier = null;
        boolean hasAnotherSequence = true;
        int seqLength = -1;
        String alphabet = null;
        //boolean hasInternalWhitespace = false;
        
        rlistener.startSequence();
        
        if (ns==null) ns=RichObjectFactory.getDefaultNamespace();
        rlistener.setNamespace(ns);
        
        // Get an ordered list of key->value pairs in array-tuples
        List section = null;
        try{
            do {
                section = this.readSection(reader);
                sectionKey = ((String[])section.get(0))[0];
                if(sectionKey == null){
                    String message = ParseException.newMessage(this.getClass(), accession, identifier, "Section key was null", sectionToString(section));
                    throw new ParseException(message);
                }
                // process section-by-section
                if (sectionKey.equals(LOCUS_TAG)) {
                    String loc = ((String[])section.get(0))[1];
                    Matcher m = lp.matcher(loc);
                    if (m.matches()) {
                        rlistener.setName(m.group(1));
                        accession = m.group(1); // default if no accession found
                        rlistener.setAccession(accession);
                        
                        if (m.group(2) != null)
                        {
                            try
                            {
                                seqLength = Integer.parseInt(m.group(2));
                            }
                            catch (NumberFormatException e)
                            {
                                System.err.println("Warning: sequence length from line \"" + loc + "\" could not be parsed");
                            }
                        }
                        
                        if (m.group(5)!=null)
                        {
                            rlistener.addSequenceProperty(Terms.getMolTypeTerm(),m.group(5));
                            alphabet = m.group(5).toUpperCase();
                        }
                        
                        // Optional extras
                        String stranded = m.group(4);
                        if(stranded!=null && stranded.equals("ss-"))
                            stranded = "single";
                        else if(stranded!=null && stranded.equals("ms-"))
                            stranded = "mixed";
                        else if(stranded!=null && stranded.equals("ds-"))
                            stranded = "double";
                        String circular = m.group(6);
                        String fifth = m.group(7);
                        String sixth = m.group(8);
                        if (stranded!=null) rlistener.addSequenceProperty(Terms.getStrandedTerm(),stranded);
                        if (circular!=null && circular.equalsIgnoreCase("circular")) rlistener.setCircular(true);
                        if (sixth != null) {
                            rlistener.setDivision(fifth);
                            rlistener.addSequenceProperty(Terms.getDateUpdatedTerm(),sixth);
                        } else if (fifth!=null) {
                            rlistener.addSequenceProperty(Terms.getDateUpdatedTerm(),fifth);
                        }
                    } else {
                        String message = ParseException.newMessage(this.getClass(), accession, identifier, "Bad locus line", sectionToString(section));
                        throw new ParseException(message);
                    }
                } else if (sectionKey.equals(DEFINITION_TAG)) {
                    rlistener.setDescription(((String[])section.get(0))[1]);
                } else if (sectionKey.equals(ACCESSION_TAG)) {
                    // if multiple accessions, store only first as accession,
                    // and store rest in annotation
                    String[] accs = ((String[])section.get(0))[1].split("\\s+");
                    accession = accs[0].trim();
                    rlistener.setAccession(accession);
                    for (int i = 1; i < accs.length; i++) {
                        rlistener.addSequenceProperty(Terms.getAdditionalAccessionTerm(),accs[i].trim());
                    }
                } else if (sectionKey.equals(VERSION_TAG)) {
                    String ver = ((String[])section.get(0))[1];
                    Matcher m = vp.matcher(ver);
                    if (m.matches()) {
                        String verAcc = m.group(1);
                        if (!accession.equals(verAcc)) {
                            // the version refers to a different accession!
                            // believe the version line, and store the original
                            // accession away in the additional accession set
                            rlistener.addSequenceProperty(Terms.getAdditionalAccessionTerm(),accession);
                            accession = verAcc;
                            rlistener.setAccession(accession);
                        }
                        if (m.group(3)!=null) rlistener.setVersion(Integer.parseInt(m.group(3)));
                        if (m.group(5)!=null) {
                            identifier = m.group(5);
                            rlistener.setIdentifier(identifier);
                        }
                    } else {
                        String message = ParseException.newMessage(this.getClass(), accession, identifier, "Bad version line", sectionToString(section));
                        throw new ParseException(message);
                    }
                } else if (sectionKey.equals(KEYWORDS_TAG)) {
                    String val = ((String[])section.get(0))[1];
                    if (val.endsWith(".")) val = val.substring(0, val.length()-1); // chomp dot
                    val = val.replace('\n',' '); //remove newline
                    String[] kws = val.split(";");
                    
                    for (int i = 0; i < kws.length; i++) {
                        String kw = kws[i].trim();
                        if (kw.length()==0) continue;
                        rlistener.addSequenceProperty(Terms.getKeywordTerm(), kw);
                    }
                } else if (sectionKey.equals(SOURCE_TAG)) {
                    // ignore - can get all this from the first feature
                } else if (sectionKey.equals(REFERENCE_TAG) && !this.getElideReferences()) {
                    // first line of section has rank and location
                    int ref_rank;
                    List baseRangeList=null;
                    String ref = ((String[])section.get(0))[1];
                    Matcher m = refp.matcher(ref);
                    if (m.matches()) {
                        ref_rank = Integer.parseInt(m.group(1));
                        if (m.group(3) != null) baseRangeList=buildBaseRanges(m.group(3));
                    } else {
                        String message = ParseException.newMessage(this.getClass(), accession, identifier, "Bad reference line", sectionToString(section));
                        throw new ParseException(message);
                    }
                    // rest can be in any order
                    String authors = null;
                    String consortium = null;
                    String title = null;
                    String journal = null;
                    String medline = null;
                    String pubmed = null;
                    String remark = null;
                    for (int i = 1; i < section.size(); i++) {
                        String key = ((String[])section.get(i))[0];
                        String val = ((String[])section.get(i))[1];
                        if (key.equals(AUTHORS_TAG)) authors = val.replace('\n',' '); //see #2276
                        else if (key.equals(CONSORTIUM_TAG)) consortium = val.replace('\n',' '); //see #2276
                        else if (key.equals(TITLE_TAG)) title = val.replace('\n',' '); //see #2276
                        else if (key.equals(JOURNAL_TAG)) journal = val.replace('\n',' '); //see #2276
                        else if (key.equals(MEDLINE_TAG)) medline = val;
                        else if (key.equals(PUBMED_TAG)) pubmed = val;
                        else if (key.equals(REMARK_TAG)) remark = val.replace('\n',' '); //see #2276
                    }
                    
                    // create the docref object
                    try {
                        // Use consortium as well if present.
                        if (authors==null) authors = consortium + " (consortium)";
                        else if (consortium!=null) authors = authors + ", " + consortium + " (consortium)";
                        // Create docref.
                        DocRef dr = (DocRef)RichObjectFactory.getObject(SimpleDocRef.class,new Object[]{DocRefAuthor.Tools.parseAuthorString(authors),journal,title});
                        // assign either the pubmed or medline to the docref - medline gets priority
                        if (medline!=null) dr.setCrossref((CrossRef)RichObjectFactory.getObject(SimpleCrossRef.class,new Object[]{Terms.MEDLINE_KEY, medline, new Integer(0)}));
                        else if (pubmed!=null) dr.setCrossref((CrossRef)RichObjectFactory.getObject(SimpleCrossRef.class,new Object[]{Terms.PUBMED_KEY, pubmed, new Integer(0)}));
                        // assign the remarks
                        if (!this.getElideComments()) dr.setRemark(remark);
                        // assign the docref to the bioentry: null if no base ranges, Integers if 1 base range - the normal case, joined RichLocation if more than 1
                        RankedDocRef rdr = baseRangeList == null?new SimpleRankedDocRef(dr, null, null, ref_rank):(baseRangeList.size()==1?new SimpleRankedDocRef(dr, new Integer(((RichLocation)baseRangeList.get(0)).getMin()), new Integer(((RichLocation)baseRangeList.get(0)).getMax()), ref_rank):new SimpleRankedDocRef(dr, new CompoundRichLocation(baseRangeList), ref_rank));
                        rlistener.setRankedDocRef(rdr);
                    } catch (ChangeVetoException e) {
                        throw new ParseException(e+", accession:"+accession);
                    }
                } else if (sectionKey.equals(COMMENT_TAG) && !this.getElideComments()) {
                    // Set up some comments
                    rlistener.setComment(((String[])section.get(0))[1]);
                } else if (sectionKey.equals(FEATURE_TAG) && !this.getElideFeatures()) {
                    // starting from second line of input, start a new feature whenever we come across
                    // a key that does not start with /
                    boolean seenAFeature = false;
                    int rcrossrefCount = 0;
                    boolean skippingBond = false;
                    for (int i = 1 ; i < section.size(); i++) {
                        String key = ((String[])section.get(i))[0];
                        String val = ((String[])section.get(i))[1];
                        if (key.startsWith("/")) {
                              if(!skippingBond)
                              {
                                key = key.substring(1); // strip leading slash
                                val = val.replaceAll("\\s*[\\n\\r]+\\s*"," ").trim();
                                if (val.endsWith("\"")) val = val.substring(1,val.length()-1); // strip quotes
                                // parameter on old feature
                                if (key.equals("db_xref")) {
                                    Matcher m = dbxp.matcher(val);
                                    if (m.matches()) {
                                        String dbname = m.group(1);
                                        String raccession = m.group(2);
                                        if (dbname.equalsIgnoreCase("taxon")) {
                                            // Set the Taxon instead of a dbxref
                                            tax = (NCBITaxon)RichObjectFactory.getObject(SimpleNCBITaxon.class, new Object[]{Integer.valueOf(raccession)});
                                            rlistener.setTaxon(tax);
                                            try {
                                                if (organism!=null) tax.addName(NCBITaxon.SCIENTIFIC,organism.replace('\n', ' '));// readSection can embed new lines
                                            } catch (ChangeVetoException e) {
                                                throw new ParseException(e+", accession:"+accession);
                                            }
                                        } else {
                                            try {
                                                CrossRef cr = (CrossRef)RichObjectFactory.getObject(SimpleCrossRef.class,new Object[]{dbname, raccession, new Integer(0)});
                                                RankedCrossRef rcr = new SimpleRankedCrossRef(cr, ++rcrossrefCount);
                                                rlistener.getCurrentFeature().addRankedCrossRef(rcr);
                                            } catch (ChangeVetoException e) {
                                                throw new ParseException(e+", accession:"+accession);
                                            }
                                        }
                                    } else {
                                        String message = ParseException.newMessage(this.getClass(), accession, identifier, "Bad dbxref", sectionToString(section));
                                        throw new ParseException(message);
                                    }
                                } else if (key.equalsIgnoreCase("organism")) {
                                    try {
                                        organism = val;
                                        if (tax!=null) tax.addName(NCBITaxon.SCIENTIFIC,organism.replace('\n', ' '));// readSection can embed new lines
                                    } catch (ChangeVetoException e) {
                                        throw new ParseException(e+", accession:"+accession);
                                    }
                                } else {
                                    if (key.equalsIgnoreCase("translation")) {
                                        // strip spaces from sequence
                                        val = val.replaceAll("\\s+","");
                                    }
                                    rlistener.addFeatureProperty(RichObjectFactory.getDefaultOntology().getOrCreateTerm(key),val);
                                }
                            }
                        } else {
                            // new feature!
                            // end previous feature
                            if(key.equalsIgnoreCase("bond"))
                            {
                                skippingBond = true;
                            }
                            else
                            {
                                skippingBond = false;
                                if (seenAFeature) {
                                    rlistener.endFeature();
                                }
                                // start next one, with lots of lovely info in it
                                RichFeature.Template templ = new RichFeature.Template();
                                templ.annotation = new SimpleRichAnnotation();
                                templ.sourceTerm = Terms.getGenBankTerm();
                                templ.typeTerm = RichObjectFactory.getDefaultOntology().getOrCreateTerm(key);
                                templ.featureRelationshipSet = new TreeSet();
                                templ.rankedCrossRefs = new TreeSet();
                                String tidyLocStr = val.replaceAll("\\s+","");
                                templ.location = GenbankLocationParser.parseLocation(ns, accession, tidyLocStr);
                                rlistener.startFeature(templ);
                                seenAFeature = true;
                                rcrossrefCount = 0;
                            }
                            
                        }
                    }
                    
                    if (seenAFeature) {
                        rlistener.endFeature();
                    }
                } else if (sectionKey.equals(BASE_COUNT_TAG)) {
                    // ignore - can calculate from sequence content later if needed
                } else if (sectionKey.equals(START_SEQUENCE_TAG) && !this.getElideSymbols()) {
                    // our first line is ignorable as it is the ORIGIN tag
                    // the second line onwards conveniently have the number as
                    // the [0] tuple, and sequence string as [1] so all we have
                    // to do is concat the [1] parts and then strip out spaces,
                    // and replace '.' and '~' with '-' for our parser.
                    StringBuffer seq = new StringBuffer();
                    for (int i = 1 ; i < section.size(); i++) seq.append(((String[])section.get(i))[1]);
                    try {
                        constructSequenceSymbols(seqLength, alphabet, seq, rlistener, symParser);
                    } catch (IllegalAlphabetException e) {
                        String message = ParseException.newMessage(this.getClass(), accession, identifier, "Bad sequence section", sectionToString(section));
                        throw new ParseException(e, message);
                    }
                }
            } while (!sectionKey.equals(END_SEQUENCE_TAG));
        }catch(RuntimeException e){
            String message = ParseException.newMessage(this.getClass(), accession, identifier, "Bad sequence section", sectionToString(section));
            throw new ParseException(e, message);
        }
        
        // Allows us to tolerate trailing whitespace without
        // thinking that there is another Sequence to follow
        while (true) {
            reader.mark(1);
            int c = reader.read();
            if (c == -1) {
                hasAnotherSequence = false;
                break;
            }
            if (Character.isWhitespace((char) c)) {
                //hasInternalWhitespace = true;
                continue;
            }
            //if (hasInternalWhitespace)
            //    System.err.println("Warning: whitespace found between sequence entries");
            reader.reset();
            break;
        }
        
        // Finish up.
        rlistener.endSequence();
        return hasAnotherSequence;
    }
    
    private void constructSequenceSymbols(int seqLength, String alphabet, StringBuffer seq,
            RichSeqIOListener rlistener, SymbolTokenization symParser)
            throws IllegalSymbolException, IllegalAlphabetException, ParseException
    {
        if (seq.length() > 0)
        {
            SymbolList sl = new SimpleSymbolList(symParser,
                    seq.toString().replaceAll("\\s+","").replaceAll("[\\.|~]","-"));
            rlistener.addSymbols(symParser.getAlphabet(),
                    (Symbol[])(sl.toList().toArray(new Symbol[0])),
                    0, sl.length());
        }
        else
        {
            System.err.println("[warning] - no sequence symbols found in file, assuming all "+
                    "ambiguity symbols and sequence is of type " + alphabet + " with length " + seqLength);
            
            if (seqLength <= 0)
            {
                throw new ParseException("Error: no sequence symbols found in file, and sequence length could not be determined");
            }
            else
            {
                Alphabet alphabetObj = AlphabetManager.alphabetForName(alphabet);
                if (alphabetObj != null && (alphabetObj.equals(DNATools.getDNA()) || alphabetObj.equals(RNATools.getRNA())))
                {
                    Symbol[] symbols = new Symbol[seqLength];
                    Symbol N = null;
                    if (alphabetObj.equals(DNATools.getDNA()))
                    {
                        N = DNATools.n();
                    }
                    else if (alphabetObj.equals(RNATools.getRNA()))
                    {
                        N = RNATools.n();
                    }
                    
                    for (int i = 0; i < seqLength; i++)
                    {
                        symbols[i] = N;
                    }
                    rlistener.addSymbols(alphabetObj, symbols,
                            0, seqLength);
                }
                else
                {
                    throw new ParseException("Error: no sequence symbols and alphabet not DNA");
                }
            }
        }
    }
    
    // reads an indented section, combining split lines and creating a list of key->value tuples
    private List readSection(BufferedReader br) throws ParseException {
        List section = new ArrayList();
        String line;
        String currKey = null;
        StringBuffer currVal = new StringBuffer();
        boolean done = false;
        int linecount = 0;
        
        try {
            while (!done) {
                br.mark(320);
                line = br.readLine();
                String firstSecKey = section.size() == 0 ? "" : ((String[])section.get(0))[0];
                if (line==null || line.length()==0 || (!line.startsWith(" ") && linecount++>0 && ( !firstSecKey.equals(START_SEQUENCE_TAG)  || line.startsWith(END_SEQUENCE_TAG)))) {
                    // dump out last part of section
                    section.add(new String[]{currKey,currVal.toString()});
                    br.reset();
                    done = true;
                } else {
                    Matcher m = sectp.matcher(line);
                    if (m.matches()) {
                        // new key
                        if (currKey!=null) section.add(new String[]{currKey,currVal.toString()});
                        // key = group(2) or group(4) or group(6) - whichever is not null
                        currKey = m.group(2)==null?(m.group(4)==null?m.group(6):m.group(4)):m.group(2);
                        currVal = new StringBuffer();
                        // val = group(3) if group(2) not null, group(5) if group(4) not null, "" otherwise, trimmed
                        currVal.append((m.group(2)==null?(m.group(4)==null?"":m.group(5)):m.group(3)).trim());
                    } else {
                        // concatted line or SEQ START/END line?
                        if (line.startsWith(START_SEQUENCE_TAG) || line.startsWith(END_SEQUENCE_TAG)) currKey = line;
                        else {
                            currVal.append("\n"); // newline in between lines - can be removed later
                            currVal.append(currKey.charAt(0)=='/'?line.substring(21):line.substring(12));
                        }
                    }
                }
            }
        } catch (IOException e) {
            String message = ParseException.newMessage(this.getClass(), accession, identifier, "", sectionToString(section));
            throw new ParseException(e, message);
        } catch (RuntimeException e){
            String message = ParseException.newMessage(this.getClass(), accession, identifier, "Bad section", sectionToString(section));
            throw new ParseException(e, message);
        }
        return section;
    }
    
    private final List buildBaseRanges(final String theBaseRangeList) throws ParseException {
        if (theBaseRangeList == null) return null;
        final List baseRangeList = new ArrayList();
        final String[] baseRange = theBaseRangeList.split(";");
        try{
        for (int r=0; r<baseRange.length; r++) {
            final Matcher rangeMatch = refRange.matcher(baseRange[r]);
            if (rangeMatch.matches()) {
                final int rangeStart = Integer.parseInt(rangeMatch.group(1));
                final int rangeEnd = Integer.parseInt(rangeMatch.group(2));
                baseRangeList.add(new SimpleRichLocation(new SimplePosition(rangeStart), new SimplePosition(rangeEnd), r));
            } else {
                String message = ParseException.newMessage(this.getClass(), accession, identifier, "Bad reference range found", theBaseRangeList);
                throw new ParseException(message);
            }
        }
        return baseRangeList;
        }catch(RuntimeException e){
            String message = ParseException.newMessage(this.getClass(), accession, identifier, "Bad base range", theBaseRangeList);
            throw new ParseException(e, message);
        }
    }
    
    /**
     * Converts the current parse section to a String. Useful for debugging.
     */
    String sectionToString(List section){
        StringBuffer parseBlock = new StringBuffer();
        for(Iterator i = section.listIterator(); i.hasNext();){
            String[] part = (String[])i.next();
            for(int x = 0; x < part.length; x++){
                parseBlock.append(part[x]);
                if(x == 0){
                    parseBlock.append("   "); //the gap will have been trimmed
                }
            }
        }
        return parseBlock.toString();
    }
}
