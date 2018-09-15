package ca.corefacility.gview.map;

import ca.corefacility.gview.managers.DisplayManager;

/**
 * Allows for controlling of which elements are to be displayed on the map.
 * @author aaron
 *
 */
public class ElementControl
{
	private DisplayManager displayManager;
	
	public ElementControl(DisplayManager displayManager)
	{
		if (displayManager == null)
		{
			throw new IllegalArgumentException("displayManager is null");
		}	

		this.displayManager = displayManager;
	}
	
	public void setLegendDisplayed(boolean displayed)
	{
		displayManager.setLegendDisplayed(displayed);		
	}
	
	public void setLabelsDisplayed(boolean displayed)
	{
		displayManager.setLabelsDisplayed(displayed);
	}
	
	public void setRulerDisplayed(boolean displayed)
	{
		displayManager.setRulerDisplayed(displayed);
	}
	
	public void setAllDisplayed(boolean displayed)
	{
		setLegendDisplayed(displayed);
		setLabelsDisplayed(displayed);
		setRulerDisplayed(displayed);
	}
	
	public boolean getAllDisplayed()
	{
		return getLegendDisplayed() && getLabelsDisplayed() &&
			   getRulerDisplayed();
	}
	
	public boolean getRulerDisplayed()
	{
		return displayManager.getRulerDisplayed();
	}
	
	public boolean getLabelsDisplayed()
	{
		return displayManager.getLabelsDisplayed();
	}
	
	public boolean getLegendDisplayed()
	{
		return displayManager.getLegendDisplayed();
	}
}
