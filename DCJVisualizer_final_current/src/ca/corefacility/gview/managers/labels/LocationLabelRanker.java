package ca.corefacility.gview.managers.labels;

import org.biojava.bio.seq.Feature;
import org.biojava.bio.symbol.Location;
import org.biojava.bio.symbol.RangeLocation;

public class LocationLabelRanker implements FeatureLabelRanker
{
	private Location overlapLocation = new RangeLocation(0,100);
	
	public LocationLabelRanker()
	{}
	
	protected LocationLabelRanker(LocationLabelRanker labelRanker)
	{
		labelRanker.overlapLocation = this.overlapLocation;
	}
	
	public LabelRank rankFeature(Feature feature)
	{
		if (feature.getLocation().overlaps(overlapLocation))
		{
			return LabelRank.ESSENTIAL;
		}
		
		return LabelRank.NON_ESSENTIAL;
	}
	
	public Object clone()
	{
		return new LocationLabelRanker(this);
	}
}
