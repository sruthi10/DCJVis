package ca.corefacility.gview.map.inputHandler;


import java.awt.geom.Point2D;

import ca.corefacility.gview.map.items.MapItem;

/**
 * Handles MouseOver events for items on the GView map.
 * @author aaron
 *
 */
public class MouseOverHandler
{
	private MapItem currentMouseOverItem;
	private ToolTipHandler toolTipHandler;
	
	/**
	 * Creates a new MouseOverHandler, displaying text using the passed tooltip handler.
	 * @param toolTipHandler
	 */
	public MouseOverHandler(ToolTipHandler toolTipHandler)
	{
		currentMouseOverItem = null;
		this.toolTipHandler = toolTipHandler;
	}
	
	/**
	 * Sets the passed item's state as mouse over.
	 * @param item  The item to change the state, or null if no item has the mouse over it.
	 */
	public void mouseOver(MapItem item, Point2D cursorPosition)
	{
		if (currentMouseOverItem != null)
		{
			currentMouseOverItem.removeMouseOver();
		}
		
		currentMouseOverItem = item;
		
		if (item != null)
		{
			String label = item.getToolTip();
			if (null != label && cursorPosition != null)
			{
				toolTipHandler.updateToolTip(cursorPosition, label);
			}
			else
			{
				toolTipHandler.hideToolTip();
			}
			
			item.setMouseOver();
		}
		else
		{
			toolTipHandler.hideToolTip();
		}
	}
}
