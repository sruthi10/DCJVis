package ca.corefacility.gview.map.items;

import java.util.Iterator;

/**
 * Defines something that can go onto a map.
 * 
 * @author Aaron Petkau
 *
 */
//  TODO do we need this?  Tried to use Composite pattern, but I don't know if I really need it.
public interface MapComponent
{
	/**
	 * Adds a new MapComponent to this component.
	 * 
	 * @param component  The component to add.
	 */
	public void add(MapComponent component);
	
	/**
	 * Removes all children from this MapComponent
	 */
	public void clear();
	
	/**
	 * @return An iterator to iterate over the children of this MapComponent.
	 */
	public Iterator<MapItem> itemsIterator();

	/**
	 * Removes the given component from this layer.
	 * @param component  The component to remove.
	 */
	public void remove(MapComponent component);
}
