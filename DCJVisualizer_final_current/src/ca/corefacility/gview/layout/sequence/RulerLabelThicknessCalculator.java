package ca.corefacility.gview.layout.sequence;

/**
 * Determines the maximum thickness of any given ruler label.
 * @author aaron
 *
 */
public abstract class RulerLabelThicknessCalculator
{
	protected float maxTopThickness = 0.0f;
	protected float maxBottomThickness = 0.0f;
	
	public float getMaxTopThickness()
	{
		return maxTopThickness;
	}
	
	public float getMaxBottomThickness()
	{
		return maxBottomThickness;
	}
}
