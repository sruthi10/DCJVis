package ca.corefacility.gview.data;

import org.biojava.bio.seq.Sequence;
import org.biojavax.SimpleNamespace;
import org.biojavax.bio.seq.SimpleRichSequence;

/**
 * A factory used to create a genome data object from the passed sequence.
 * @author Aaron Petkau
 *
 */
public class GenomeDataFactory
{
	/**
	 * Creates a genome data object from the passed sequence.
	 * 
	 * @param sequence  The sequence to use.
	 * @return  A GenomeData object, or null if sequence was null.
	 */
	public static GenomeData createGenomeData(Sequence sequence)
	{
		GenomeData genomeData = null;
		
		if (sequence != null)
		{
			genomeData = new GenomeDataImp(sequence);
		}
		
		return genomeData;
	}
	
	/**
	 * Creates a genome data object from the passed sequence.
	 * 
	 * @param sequence  The sequence to use.
	 * @param XML Whether or not the genomic data will be derived from an XML file.
	 * 
	 * @return  A GenomeData object, or null if sequence was null.
	 */
	public static GenomeData createGenomeData(Sequence sequence, boolean XML)
	{
		GenomeData genomeData = null;
		
		if (sequence != null)
		{
			genomeData = new GenomeDataImp(sequence, XML);
		}
		
		return genomeData;
	}
	
	/**
	 * Creates a blank GenomeData object which has a sequenceLength, but no associated symbols/features.
	 * @param sequenceLength  The length of the sequence.
	 * @return  A blank GenomeData object.
	 */
	public static GenomeData createBlankGenomeData(int sequenceLength)
	{	
		SimpleRichSequence richSequence = new SimpleRichSequence(new SimpleNamespace("gview"), "gview", "accession", 0, 
				new BlankSymbolList(sequenceLength), null);
	
		return new GenomeDataImp(richSequence);		
	}
}
