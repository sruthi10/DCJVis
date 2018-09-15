package ca.corefacility.gview.managers.labels;

import org.biojava.bio.seq.Feature;

/**
 * Defines how to assign rankings to the labels of Features.  This is used to divide up labels into categories of importance.
 * @author Aaron Petkau
 *
 */
public interface FeatureLabelRanker
{
	public final static FeatureLabelRanker DEFAULT = new AllLabelRanker();
	
	/**
	 * Gives a specific ranking for a label on the passed feature.
	 * @param feature  The feature to be labeled
	 * @return  A LabelRank value giving the ranking.
	 */
	public LabelRank rankFeature(Feature feature);
	
	public Object clone();
}
