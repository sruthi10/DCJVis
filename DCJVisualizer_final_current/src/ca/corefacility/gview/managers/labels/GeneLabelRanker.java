package ca.corefacility.gview.managers.labels;

import org.biojava.bio.seq.Feature;

public class GeneLabelRanker implements FeatureLabelRanker
{
	public LabelRank rankFeature(Feature feature)
	{
		if (feature.getAnnotation().containsProperty("gene"))
		{
			return LabelRank.ESSENTIAL;
		}
		
		return LabelRank.NON_ESSENTIAL;
	}
	
	public Object clone()
	{
		return new GeneLabelRanker();
	}
}
