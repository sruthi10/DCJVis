package ca.corefacility.gview.map.items;

import ca.corefacility.gview.layout.prototype.SequencePoint;
import ca.corefacility.gview.layout.sequence.Backbone;
import edu.umd.cs.piccolo.util.PPickPath;

// this is made so that if we select a Backbone Label, the associated feature will be selected.
public class BackboneLabelItem extends AbstractBackboneTextItem
{
	private static final long serialVersionUID = -9012348993194170647L;
	private FeatureItem featureItem;

	public BackboneLabelItem(Backbone backbone, SequencePoint pinnedPoint, FeatureItem featureItem)
	{
		super(backbone, pinnedPoint);

		assert (featureItem != null);
		this.featureItem = featureItem;
	}

	public BackboneLabelItem(Backbone backbone, FeatureItem featureItem)
	{
		super(backbone, null);

		assert (featureItem != null);
		this.featureItem = featureItem;
	}

	public FeatureItem getLabeledFeature()
	{
		return featureItem;
	}

	// says that this item should be picked before any children
	// do I even need this anymore? Maybe when I add in label lines.
	@Override
	protected boolean pick(PPickPath pickPath)
	{
		return true;
	}
}
