package ca.corefacility.gview.textextractor;

import org.biojava.bio.seq.Feature;

/**
 * Doesn't extract text from a feature, but instead displays whatever string it is constructed with.
 * 
 * @author Aaron Petkau
 *
 */
public class StringExtractor implements FeatureTextExtractor
{
	private String displayText;
		
	/**
	 * This constructor can be used to "decorate" a feature text extractor.
	 * @param displayText  The text to display before the text from the passed FeatureTextExtractor.
	 */
	public StringExtractor(String displayText)
	{
		if (displayText != null)
		{
			this.displayText = displayText;
		}
		else
		{
			this.displayText = "";
		}
	}

	public String extractText(Feature feature)
	{
		return displayText;
	}
	
	public String getDisplayText()
	{
		return displayText;
	}
	
	public Object clone()
	{
		return new StringExtractor(displayText);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((displayText == null) ? 0 : displayText.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StringExtractor other = (StringExtractor) obj;
		if (displayText == null)
		{
			if (other.displayText != null)
				return false;
		} else if (!displayText.equals(other.displayText))
			return false;
		return true;
	}
}
