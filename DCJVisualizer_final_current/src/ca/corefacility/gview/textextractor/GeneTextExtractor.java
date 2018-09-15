package ca.corefacility.gview.textextractor;

import org.biojava.bio.seq.Feature;
import org.biojava.bio.symbol.Location;

import java.util.NoSuchElementException;

public class GeneTextExtractor implements FeatureTextExtractor
{
	public GeneTextExtractor()
	{
	}
	
	public String extractText(Feature feature)
	{
		Location location = feature.getLocation();
		//int length = location.getMax()-location.getMin();
		String text = "[" + location.getMin() + ", " + location.getMax() + "]";
		
		try
		{
			text += "\n" + (String)feature.getAnnotation().getProperty("gene");
		}
		catch (NoSuchElementException e)
		{
		}
		
		return text;
	}
	
	public Object clone()
	{
		return new GeneTextExtractor();
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
