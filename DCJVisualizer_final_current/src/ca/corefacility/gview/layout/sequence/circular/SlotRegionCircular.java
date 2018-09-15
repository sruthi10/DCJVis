package ca.corefacility.gview.layout.sequence.circular;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import ca.corefacility.gview.layout.sequence.AbstractSlotRegion;
import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.layout.sequence.SequencePath;
import ca.corefacility.gview.layout.sequence.SlotTranslator;

public class SlotRegionCircular extends AbstractSlotRegion
{		
	public SlotRegionCircular(Backbone backbone, SlotTranslator slots)
	{
		super(backbone, slots);
		
		sequencePath = new SequencePathCircular(backbone);
	}
	
	public SequencePath getSequencePath()
	{
		return new SequencePathCircular(backbone);
	}

	@Override
	public Point2D getCenter()
	{
		return new Point2D.Float(0,0);
	}

	@Override
	public Rectangle2D getCurrentBounds()
	{
		float topHeight = (float)slots.getTopMostHeight();
		
		Point2D topPoint = backbone.translate(0, topHeight);
		
		float maxRadius = (float)Polar.createPolar(topPoint).getRadius();
		
		return new Rectangle2D.Float(-maxRadius, -maxRadius, 2*maxRadius, 2*maxRadius);
	}
}
