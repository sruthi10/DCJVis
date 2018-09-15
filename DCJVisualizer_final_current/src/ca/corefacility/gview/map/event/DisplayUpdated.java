package ca.corefacility.gview.map.event;

import java.awt.geom.Rectangle2D;

public class DisplayUpdated extends GViewEvent
{
	private static final long serialVersionUID = 1107720499369373959L;
	private Rectangle2D viewBounds;
	private boolean displayBoundsChanged;
	
	private Rectangle2D cameraBounds;
	
	/**
	 * An event summarising information about the current display bounds.
	 * @param source  The source of this event.
	 * @param viewBounds  The current view bounds.
	 * @param cameraBounds  The current camera bounds.
	 * @param displayBoundsChanged  If the bounds of the display changed, or not.
	 */
	public DisplayUpdated(Object source, Rectangle2D viewBounds, Rectangle2D cameraBounds, boolean displayBoundsChanged)
	{
		super(source);
		this.viewBounds = viewBounds;
		this.cameraBounds = cameraBounds;
		
		this.displayBoundsChanged = displayBoundsChanged;
	}
	
	public Rectangle2D getViewBounds()
	{
		return viewBounds;
	}
	
	public boolean isDisplayBoundsChanged()
	{
		return displayBoundsChanged;
	}
	
	public Rectangle2D getCameraBounds()
	{
		return cameraBounds;
	}
}
