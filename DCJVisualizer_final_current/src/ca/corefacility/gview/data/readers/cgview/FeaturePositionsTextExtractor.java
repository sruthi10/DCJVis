package ca.corefacility.gview.data.readers.cgview;

import org.biojava.bio.seq.Feature;
import org.biojava.bio.symbol.Location;

import ca.corefacility.gview.textextractor.FeatureTextExtractor;

/**
 * A text extractor used to add feature positions to cgview labels.
 * @author aaron
 *
 */
public class FeaturePositionsTextExtractor implements FeatureTextExtractor
{

	public Object clone()
	{
		return new FeaturePositionsTextExtractor();
	}

	@Override
	public String extractText(Feature feature)
	{
		String text = "";
		
		Location location = feature.getLocation();
		
		if (location != null)
		{
			return "" + location.getMin() + "-" + location.getMax();
		}
		
		return text;
	}
}
