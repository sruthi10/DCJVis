package ca.corefacility.gview.map.items;

import java.awt.Paint;

import org.biojava.bio.symbol.Location;

/**
 * Stores the actual shape of the backbone.
 * @author aaron
 *
 */
public class BasicBackboneItem extends AbstractBackboneShapeItem
{
	private static final long serialVersionUID = 3028438471220220740L;

	private Location location;

	private String tooltip;

	//private BackboneStyle style;

	public BasicBackboneItem(Location location, String tooltip)
	{
		this.location = location;
		this.tooltip = tooltip;

		setPickable(false);

		//		setStroke(null);
	}

	public BasicBackboneItem(Location location)
	{
		this(location, null);
	}

	public BasicBackboneItem(String tooltip)
	{
		this.tooltip = tooltip;
	}

	public void select()
	{
		// TODO Auto-generated method stub

	}

	public void unSelect()
	{
		// TODO Auto-generated method stub

	}


	@Override
	public String getToolTip()
	{
		return this.tooltip;
	}

	// done only to draw backbone for now
	public Location getLocation()
	{
		return this.location;
	}


	public void updateClippedLocation(Location clipLocation)
	{
		if (this.location.overlaps(clipLocation))
		{
			updateBoundsFromShape(getState());
		}
	}
	
	@Override
	public void setPaint(Paint paint)
	{
		super.setPaint(paint);
		this.invalidatePaint();
	}
}
