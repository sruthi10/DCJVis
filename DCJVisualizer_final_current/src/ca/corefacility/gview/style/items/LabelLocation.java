package ca.corefacility.gview.style.items;

/**
 * Defines the location of labels on the ruler.
 * @author aaron
 *
 */
public enum LabelLocation
{
	/**
	 * Labels should be located below the backbone only.
	 */
	BELOW_BACKBONE,
	
	/**
	 * Labels should be located above the backbone only.
	 */
	ABOVE_BACKBONE,
	
	/**
	 * Labels should be located both above and below the backbone.
	 */
	BOTH,
	
	/**
	 * No ruler labels should be drawn.
	 */
	NONE;
	
	/**
	 * Determines the state is such that we should place labels above backbone.
	 * @return  True if we should place labels above backbone, false otherwise.
	 */
	public boolean buildAbove()
	{
		return this.equals(ABOVE_BACKBONE) || this.equals(BOTH);
	}
	
	/**
	 * Determines the state is such that we should place labels below backbone.
	 * @return  True if we should place labels below backbone, false otherwise.
	 */
	public boolean buildBelow()
	{
		return this.equals(BELOW_BACKBONE) || this.equals(BOTH);
	}
}
