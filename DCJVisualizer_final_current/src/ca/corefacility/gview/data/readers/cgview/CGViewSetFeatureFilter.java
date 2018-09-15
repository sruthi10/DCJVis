package ca.corefacility.gview.data.readers.cgview;

import org.biojava.bio.seq.Feature;
import org.biojava.bio.seq.FeatureFilter;
import org.biojava.bio.seq.FeatureHolder;
import org.biojava.bio.seq.SimpleFeatureHolder;

/**
 * A class which simple stores a set of features to filter out
 * Used by CGView so we don't have to waste so much time filtering out all the features of a particular style.
 * @author aaron
 *
 */
public class CGViewSetFeatureFilter implements FeatureFilter
{
    private static final long serialVersionUID = 1L;
    
    private SimpleFeatureHolder features;
	
	public CGViewSetFeatureFilter()
	{
		features = new SimpleFeatureHolder();
	}
	
	public void addFeature(Feature f)
	{
		features.addFeature(f);
	}
	
	public FeatureHolder getFeatures()
	{
		return features;
	}

	@Override
	public boolean accept(Feature f)
	{
		return features.containsFeature(f);
	}

}
