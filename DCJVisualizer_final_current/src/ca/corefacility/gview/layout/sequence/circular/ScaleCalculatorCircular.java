package ca.corefacility.gview.layout.sequence.circular;

import java.awt.geom.Dimension2D;

import ca.corefacility.gview.layout.prototype.SequencePoint;
import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.layout.sequence.ScaleCalculator;
import ca.corefacility.gview.layout.sequence.SlotRegion;
import ca.corefacility.gview.managers.labels.Label;

public class ScaleCalculatorCircular extends ScaleCalculator
{	
	private static final float innerRadius = 75;
	
	@Override
	public double intersectsAtScale(Label label, SlotRegion slotRegion)
	{
		double intersectScale = 0;
		
		Backbone backbone = slotRegion.getBackbone();
		
		if (backbone instanceof BackboneCircular)
		{
			BackboneCircular backboneCircular = (BackboneCircular)backbone;
			Dimension2D labelDimension = label.getDimension();
			SequencePoint pinnedPoint = label.getPinnedPoint();
			
			intersectScale = backboneCircular.calculateScaleOverlap(labelDimension, pinnedPoint, innerRadius);
		}
		
		return intersectScale;
	}
}
