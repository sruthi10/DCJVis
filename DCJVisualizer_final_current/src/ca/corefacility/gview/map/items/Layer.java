package ca.corefacility.gview.map.items;

import java.util.Collection;
import java.util.Iterator;

import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;

public class Layer extends PLayer implements MapComponent
{
	private static final long serialVersionUID = 3627879068962021947L;

	private static class ItemsIterator implements Iterator<MapItem>
	{
		private Iterator children;

		public ItemsIterator(Collection children)
		{
			this.children = children.iterator();
		}

		public boolean hasNext()
		{
			return children.hasNext();
		}

		public MapItem next()
		{
			return (MapItem) children.next();
		}

		public void remove()
		{
			children.remove();
		}
	}

	public void add(MapComponent component)
	{
		// only add a component that is implemented as a piccolo PNode
		if (component instanceof PNode)
		{
			addChild((PNode) component);
		}
		else
		{
			throw new IllegalArgumentException("item not a PNode");
		}
	}

	public Iterator<MapItem> itemsIterator()
	{
		return new ItemsIterator(getAllNodes(new MapItemFilter(), null));
	}

	public void clear()
	{
		removeAllChildren();
	}
	
	@Override
	public void remove(MapComponent component)
	{
		if (component instanceof PNode)
		{
			removeChild((PNode) component);
		}
	}
}
