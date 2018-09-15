package ca.corefacility.gview.managers.labels;

import org.biojava.bio.seq.Feature;

public class AllLabelRanker implements FeatureLabelRanker
{
	public LabelRank rankFeature(Feature feature)
	{
		return LabelRank.ESSENTIAL;
	}
	
	public boolean equals(Object o)
	{
		return o instanceof AllLabelRanker;
	}
	
	public Object clone()
	{
		return new AllLabelRanker();
	}
}
