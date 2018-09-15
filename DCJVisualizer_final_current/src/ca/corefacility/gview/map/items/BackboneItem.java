package ca.corefacility.gview.map.items;

import org.biojava.bio.symbol.Location;

/**
 * An item which has a particular location on the backbone.
 * 
 * @author Aaron Petkau
 *
 */
public interface BackboneItem extends MapItem
{
	public Location getLocation();
	
	/**
	 * Tests/updates this item if it intersects the passed clipping location.
	 * @param clipLocation  The clipping location to check against.
	 */
	public void updateClippedLocation(Location clipLocation);
}
