package ca.corefacility.gview.layout.sequence.linear;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import ca.corefacility.gview.layout.sequence.AbstractSlotRegion;
import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.layout.sequence.SequencePath;
import ca.corefacility.gview.layout.sequence.SlotTranslator;

public class SlotRegionLinear extends AbstractSlotRegion
{
	public SlotRegionLinear(Backbone backbone, SlotTranslator slots)
	{
		super(backbone, slots);
		
		sequencePath = new SequencePathLinear(backbone);
	}
	
	public SequencePath getSequencePath()
	{
		return new SequencePathLinear(backbone);
	}

	@Override
	public Point2D getCenter()
	{
		Point2D centerPoint = null;
		
		float top = (float)slots.getTopMostHeight();
		float bottom = (float)slots.getBottomMostHeight();
		
		float centerHeight = (top+bottom)/2;
		centerPoint = new Point2D.Float(0, -centerHeight);

		return centerPoint;
	}

	@Override
	public Rectangle2D getCurrentBounds()
	{		
		Point2D maxLeftPoint = backbone.translate(0,0);
		float xLeft = (float)maxLeftPoint.getX();
		float xRight = (float)(xLeft + backbone.getBackboneLength());
		
		Point2D top = backbone.translate(0, slots.getTopMostHeight());
		Point2D bottom = backbone.translate(0, slots.getBottomMostHeight());
		
		return new Rectangle2D.Float(xLeft, (float)top.getY(), (xRight-xLeft), (float)(bottom.getY() - top.getY()));
	}
}
