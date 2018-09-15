package ca.corefacility.gview.map.items;


import org.biojava.bio.symbol.Location;

import ca.corefacility.gview.style.datastyle.PlotStyle;

public class PlotItem extends AbstractBackboneShapeItem
{
    private static final long serialVersionUID = 1L;
    
    private Location location;

	public PlotItem(PlotStyle plotStyle, Location location)
	{
		this.location = location;

		setStyle(plotStyle);
	}

	private void setStyle(PlotStyle style)
	{
		setPaint(style.getPaint()[0]);
		setShapeEffectRenderer(style.getShapeEffectRenderer());
	}

	@Override
	public Location getLocation()
	{
		return this.location;
	}

	@Override
	public void updateClippedLocation(Location clipLocation)
	{
		if (getLocation().overlaps(clipLocation))
		{
			updateBoundsFromShape(getState());
		}
	}
}
