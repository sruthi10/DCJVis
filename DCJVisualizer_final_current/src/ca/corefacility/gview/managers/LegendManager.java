package ca.corefacility.gview.managers;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ca.corefacility.gview.map.event.DisplayUpdated;
import ca.corefacility.gview.map.event.GViewEvent;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.items.Layer;
import ca.corefacility.gview.map.items.LegendItem;
import ca.corefacility.gview.map.items.MapComponent;
import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.style.items.LegendStyle;
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PNode;


public class LegendManager implements GViewEventListener
{
	private final PCamera mainView;
	
	private MapComponent legendLayer;
	private boolean legendDisplayed = false;	
	private List<LegendItem> legends;
	
	// used so, if we turn legends on, we can send the DisplayUpdated event
	private DisplayUpdated previousDisplayUpdatedEvent = null;

	public LegendManager(PCamera mainView, MapStyle mapStyle)
	{
		this.mainView = mainView;
		
		rebuild(mapStyle);
	}
	
	public void rebuild(MapStyle mapStyle)
	{
		legendLayer = new Layer();
		legends = new LinkedList<LegendItem>();

		Iterator<LegendStyle> lStyles = mapStyle.getGlobalStyle().legendStyles();

		while (lStyles.hasNext())
		{
			LegendStyle currLegendStyle = lStyles.next();

			if (currLegendStyle.isDisplayLegend())
			{
				LegendItem currLegend = LegendItem.buildLegend(currLegendStyle);

				legendLayer.add(currLegend);
				legends.add(currLegend);
			}
		}
	}
	
	/**
	 * Updates the legends on the map.
	 * 
	 * @param mapStyle The map style the appropriate legend styles are contained under.
	 */
	public void update(MapStyle mapStyle)
	{		
		if (legendDisplayed)
		{
			if(legendLayer != null)
			{
				mainView.removeChild((PNode)legendLayer);
			}
			
			rebuild(mapStyle);
			
			eventOccured(previousDisplayUpdatedEvent);
			mainView.addChild((PNode)legendLayer);
		}
		else
		{
			rebuild(mapStyle);
		}
	}

	public MapComponent getLegendLayer()
	{
		return legendLayer;
	}
	
	public void setLegendDisplayed(boolean display)
	{
		this.legendDisplayed = display;
		
		if (display)
		{
			eventOccured(previousDisplayUpdatedEvent);
			mainView.addChild((PNode)legendLayer);
		}
		else
		{
			mainView.removeChild((PNode)legendLayer);
		}
	}
	
	public Rectangle2D expanedForLegends(Rectangle2D bounds, boolean inside)
	{
		if (bounds == null)
		{
			throw new IllegalArgumentException("bounds is null");
		}
		
		Rectangle2D expandedBounds = new Rectangle2D.Double();
		expandedBounds.setFrame(bounds);
		Point2D alignmentPoint = new Point2D.Double();
		
		for (LegendItem legend : legends)
		{
			legend.determineAlignment(alignmentPoint, bounds, inside);
			Rectangle2D boundsRef = legend.computeFullBounds(null);
			Rectangle2D legendBoundsRelativeToBounds = new Rectangle2D.Double(alignmentPoint.getX(), alignmentPoint.getY(),
					boundsRef.getWidth(), boundsRef.getHeight());
			
			expandedBounds = expandedBounds.createUnion(legendBoundsRelativeToBounds);
		}
		
		return expandedBounds;
	}

	@Override
	public void eventOccured(GViewEvent event)
	{
		if (event instanceof DisplayUpdated)
		{
			DisplayUpdated displayUpdatedEvent = (DisplayUpdated) event;
			
			if (displayUpdatedEvent.isDisplayBoundsChanged())
			{
				previousDisplayUpdatedEvent = displayUpdatedEvent;
				
				if (legendDisplayed)
				{
					for (LegendItem legend : legends)
					{
						legend.alignLegend(displayUpdatedEvent.getCameraBounds(), true);
					}
				}
			}
		}
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (legendDisplayed ? 1231 : 1237);
		result = prime * result
				+ ((legendLayer == null) ? 0 : legendLayer.hashCode());
		result = prime * result + ((legends == null) ? 0 : legends.hashCode());
		result = prime
				* result
				+ ((previousDisplayUpdatedEvent == null) ? 0
						: previousDisplayUpdatedEvent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LegendManager other = (LegendManager) obj;
		if (legendDisplayed != other.legendDisplayed)
			return false;
		if (legendLayer == null)
		{
			if (other.legendLayer != null)
				return false;
		} else if (!legendLayer.equals(other.legendLayer))
			return false;
		if (legends == null)
		{
			if (other.legends != null)
				return false;
		} else if (!legends.equals(other.legends))
			return false;
		if (previousDisplayUpdatedEvent == null)
		{
			if (other.previousDisplayUpdatedEvent != null)
				return false;
		} else if (!previousDisplayUpdatedEvent
				.equals(other.previousDisplayUpdatedEvent))
			return false;
		return true;
	}

	public Rectangle2D getLegendBounds()
	{
		return ((PNode)legendLayer).computeFullBounds(null);
	}

	public boolean getDisplayed()
	{
		return legendDisplayed;
	}
}
