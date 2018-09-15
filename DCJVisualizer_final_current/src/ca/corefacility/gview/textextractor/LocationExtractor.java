package ca.corefacility.gview.textextractor;

import org.biojava.bio.seq.Feature;

public class LocationExtractor implements FeatureTextExtractor
{	
	public LocationExtractor()
	{
	}
	
	public String extractText(Feature feature)
	{
            //System.out.println("Location Extractor.java: "+ feature.toString()); // Feature Target BUD3 [4349,9259] +   Feature source/target genename coordinates orientation
            //System.out.println(feature.getSource()); // prints the gene name
            return " "+ feature.getSource().toString() + " \n" + feature.getLocation().toString();// this is how we getting location coordinates
             //return "TEST -Tool tip";
	}
	
	public Object clone()
	{
		return new LocationExtractor();
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
