package ca.corefacility.gview.textextractor;

import org.biojava.bio.seq.Feature;

/**
 * Text extractor which returns a blank string.
 * @author Aaron Petkau
 *
 */
public class BlankExtractor implements FeatureTextExtractor
{
	public String extractText(Feature feature)
	{
		return "";
	}
	
	public Object clone()
	{
		return new BlankExtractor();
	}
	
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		else if (o == null)
			return false;
		else if (this.getClass() != o.getClass())
			return false;
		return true;
	}
}
