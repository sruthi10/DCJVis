package ca.corefacility.gview.managers.labels;


import java.awt.geom.Dimension2D;

import ca.corefacility.gview.layout.prototype.SequencePoint;

/**
 * Represents the position (in a dimension and seq point) as well as the range where this position is active.
 * @author aaron
 *
 */
public class SequenceLabelBounds
{
	private SequencePoint pinnedPoint;
	private Dimension2D dim;
	private double lowerScale; // the scale beyond which this label is being turned on
	private double upperScale;
	
	public SequenceLabelBounds(SequencePoint pinnedPoint, Dimension2D dim, double lowerScale, double upperScale)
	{
		super();
		this.pinnedPoint = pinnedPoint;
		this.dim = dim;
		
		this.lowerScale = lowerScale;
		this.upperScale = upperScale;
	}
	public SequencePoint getPinnedPoint()
	{
		return pinnedPoint;
	}
	public Dimension2D getDimension()
	{
		return dim;
	}
	
	public boolean inRange(double scale)
	{
		return (lowerScale <= scale && scale <= upperScale);
	}
	public double getLowerScale()
	{
		return lowerScale;
	}
	public double getUpperScale()
	{
		return upperScale;
	}
}
