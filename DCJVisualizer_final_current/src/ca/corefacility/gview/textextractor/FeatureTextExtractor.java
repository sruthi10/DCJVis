package ca.corefacility.gview.textextractor;

import org.biojava.bio.seq.Feature;

/**
 * Extracts text from a feature to be displayed in a tooltip or label.
 * 
 * @author Aaron Petkau
 *
 */
public interface FeatureTextExtractor extends Cloneable
{
	/**
	 * A FeatureTextExtractor which returns a blank string always.
	 */
	public static final FeatureTextExtractor BLANK = new BlankExtractor();
	
	/**
	 * Extracts information from the passed feature.
	 * @param feature  The feature to extract the information from.
	 * @return A String containing any relevant information.
	 */
	public String extractText(Feature feature);
	
	public Object clone();
}
