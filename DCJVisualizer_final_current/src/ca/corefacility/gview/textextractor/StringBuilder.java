package ca.corefacility.gview.textextractor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;

import org.biojava.bio.seq.Feature;

/**
 * Concatenates the strings from two or more text-extractors.
 * 
 * @author Aaron Petkau
 *
 */
public class StringBuilder implements FeatureTextExtractor
{
	private String format = "";
	
	private List<FeatureTextExtractor> textExtractors = null;	

	public StringBuilder(String format, List<FeatureTextExtractor> textExtractors)
		throws IllegalFormatException
	{
		if (format == null)
		{
			throw new IllegalArgumentException("passed format is null");
		}
		
		String[] testStrings = new String[textExtractors.size()];
		for (int i = 0; i < testStrings.length; i++)
		{
			testStrings[i] = "";
		}
		
		String.format(format, (Object[])testStrings);
		
		this.format = format;
		this.textExtractors = textExtractors;
	}
	
	public StringBuilder(String format, FeatureTextExtractor...textExtractors)
	{
		this(format, Arrays.asList(textExtractors));
	}

	public String extractText(Feature feature)
	{
		if (format != null)
		{
			ArrayList<String> strings = new ArrayList<String>();
			for (FeatureTextExtractor f : textExtractors)
			{
			    String extractedString = f.extractText(feature);
				if (extractedString == null)
				{
				    extractedString = "";
				}
                strings.add(extractedString);
			}
			
			String s = String.format(format, strings.toArray());
			return s;
		}
		
		return "";
	}
	
	public Object clone()
	{
		StringBuilder c = new StringBuilder(format, textExtractors);
		
		return c;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((format == null) ? 0 : format.hashCode());
		result = prime * result
				+ ((textExtractors == null) ? 0 : textExtractors.hashCode());
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
		StringBuilder other = (StringBuilder) obj;
		if (format == null)
		{
			if (other.format != null)
				return false;
		} else if (!format.equals(other.format))
			return false;
		if (textExtractors == null)
		{
			if (other.textExtractors != null)
				return false;
		} else if (!textExtractors.equals(other.textExtractors))
			return false;
		return true;
	}
	
	public String getFormat()
	{
		return this.format;
	}
	
	public Iterator<FeatureTextExtractor> getFeatureTextExtractors()
	{
		return this.textExtractors.iterator();
	}
}
