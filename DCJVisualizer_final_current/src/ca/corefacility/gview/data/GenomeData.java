package ca.corefacility.gview.data;


import org.biojava.bio.seq.FeatureFilter;
import org.biojava.bio.seq.FeatureHolder;
import org.biojava.bio.seq.Sequence;

import ca.corefacility.gview.style.datastyle.FeatureHolderStyle;

/**
 * Manages the data of the genome sequence.
 */
public interface GenomeData
{
	/**
	 * @return The length of the sequence.
	 */
	public int getSequenceLength();
	
	/**
	 * @return  The initial base of this sequence.
	 */
	public int getInitialBase();
	
	/**
	 * Obtains a FeatureHolder from an associated FeatureHolderStyle.
	 * 
	 * @param holderStyle  The style of the set of features we want to obtain.
	 * @return  A feature holder containing the full set of features (including features styled by sub-styles).
	 */
	public FeatureHolder getAllFeatures(FeatureHolderStyle holderStyle);
	
	/**
	 * Obtains the set of all features which are styled according to the passed style.
	 * 
	 * @param holderStyle The style from which we want to get the features.
	 * 
	 * @return  A set of all the features styled by this style, minus any features styled by sub-styles.
	 */
	public FeatureHolder getOnlyFeatures(FeatureHolderStyle holderStyle);
	
	/**
	 * Gives all features filtered out by the passed filter.
	 *   Note: this performs a linear search of all features on the sequence.
	 * @param filter
	 * @return  A FeatureHolder containing the specific features.
	 */
	public FeatureHolder getFeatures(FeatureFilter filter);
	
	/**
	 * Determines if the passed base is on the sequence.
	 * @param base
	 * @return  True if the passed base is on the sequence, false otherwise.
	 */
	public boolean baseOnSequence(int base);

	public Sequence getSequence();
	
	/**
	 * Determines if the sequence data is circular.
	 * @return  True if circular, false otherwise.
	 */
	public boolean isCircular();

	/**
	 * 
	 * @return Whether or not the genomic data is derived from an XML file.
	 */
	boolean isXML();
}
