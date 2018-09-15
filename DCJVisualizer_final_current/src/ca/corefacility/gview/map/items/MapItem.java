package ca.corefacility.gview.map.items;


import java.awt.Paint;

import ca.corefacility.gview.map.event.GViewEventListener;

/**
 * An item that is stored on a map.
 */
public interface MapItem extends MapComponent, GViewEventListener
{
	// should these be public?  maybe protected since no code outside of package should need these
	
	/**
	 * Turns on/off the visibility of this item.
	 * 
	 * @param isVisible  True if this item is visible, false if invisible.
	 */
	public void setVisible(boolean isVisible);
	
	public void setPaint(Paint paint);
	
	public void setTransparency(float transparency);
	
	/**
	 * Changes the state of this map item.
	 * @param state
	 */
//	public void changeState(MapItemState state);
	
	/**
	 * @return  The current state this MapItem is in.
	 */
	public MapItemState getState();
	
	public boolean isMouseOver();
	public boolean isSelected();
	
	public void setSelect();
	public void setMouseOver();
	public void removeSelect();
	public void removeMouseOver();
	
	// obtains a string that it should display as a tooltip
	public String getToolTip();
}
