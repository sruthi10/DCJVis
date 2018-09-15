package ca.corefacility.gview.map.controllers;

/**
 * The abstract class for style controllers.
 * 
 * @author Eric Marinier
 *
 */
public abstract class Controller
{
	private boolean rebuildRequired = false;
	
	/**
	 * 
	 * @return Whether or not this controller requires the gViewMap map to be rebuilt.
	 */
	public boolean isRebuildRequired()
	{
		return this.rebuildRequired;
	}
	
	/**
	 * Resets the rebuild flag to FALSE.
	 */
	public void resetRebuildRequired()
	{
		this.rebuildRequired = false;
	}
	
	/**
	 * Sets the rebuild flag to TRUE.
	 */
	public void notifyRebuildRequired()
	{
		this.rebuildRequired = true;
	}
}
