package ca.corefacility.gview.managers.labels;

import org.biojava.bio.seq.Feature;

public class HiddenLabelRanker implements FeatureLabelRanker
{
	public LabelRank rankFeature(Feature feature)
	{
		return LabelRank.HIDDEN;
	}
	
	public Object clone()
	{
		return new HiddenLabelRanker();
	}
}
