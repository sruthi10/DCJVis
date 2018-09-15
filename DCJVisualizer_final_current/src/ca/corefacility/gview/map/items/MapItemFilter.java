package ca.corefacility.gview.map.items;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PNodeFilter;

/**
 * Used so that we can iterate over children of layers, and only accept MapItems.
 * 
 * @author Aaron Petkau
 *
 */
public class MapItemFilter implements PNodeFilter
{
	
	public boolean accept(PNode node)
	{
		return (node instanceof MapItem);
	}

	
	public boolean acceptChildrenOf(PNode node)
	{
		return (node instanceof MapComponent);
	}
}
