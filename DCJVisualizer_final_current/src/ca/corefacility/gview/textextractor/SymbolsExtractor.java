package ca.corefacility.gview.textextractor;

import org.biojava.bio.seq.Feature;

public class SymbolsExtractor implements FeatureTextExtractor
{
	public SymbolsExtractor()
	{
	}

	public String extractText(Feature feature)
	{
		return feature.getSymbols().seqString();
	}
	
	public Object clone()
	{
		return new SymbolsExtractor();
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
