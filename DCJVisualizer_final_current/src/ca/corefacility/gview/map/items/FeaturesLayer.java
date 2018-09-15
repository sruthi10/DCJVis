package ca.corefacility.gview.map.items;

import java.util.Collection;
import java.util.Iterator;

/**
 * This class is a custom implementation of the Layer class.
 * 
 * It is intended to responsible for managing the layer containing features.
 * 
 * @author Eric Marinier
 *
 */
public class FeaturesLayer extends Layer
{
	private static final long serialVersionUID = 1L;

	public MapItem[] getMapItems()
	{
		@SuppressWarnings("unchecked")
		Collection<Object> collection = getAllNodes(new MapItemFilter(), null);
		
		Object current;
		
		for(Iterator<Object> iter = collection.iterator(); iter.hasNext();)
		{
			current = iter.next();
			
			if(!(current instanceof MapItem))
			{
				throw new IllegalArgumentException("Invalid type: " + current.getClass());
			}
		}
		
		return collection.toArray(new MapItem[1]);
	}
}
