package ca.corefacility.gview.map.items;

import java.awt.Image;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.Collection;
import java.util.Iterator;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolox.nodes.PNodeCache;

public class FeatureItemsCache extends PNodeCache implements MapComponent
{
	private static final long serialVersionUID = -1631307590687704916L;

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
			System.err.println("Error:  item not a PNode.");
		}
	}
	
	@Override
	public void remove(MapComponent component)
	{
		if (component instanceof PNode)
		{
			removeChild((PNode) component);
		}
	}

	@Override
	public Image createImageCache(Dimension2D cacheOffsetRef)
	{
		BufferedImage image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
		toImage(image, null);

		float[] matrix = { 0.111f, 0.111f, 0.111f, 0.111f, 0.111f, 0.9f, 0.9f, 0.111f, 0.111f, };

		BufferedImageOp op = new ConvolveOp(new Kernel(3, 3, matrix));
		// return image;
		return op.filter(image, null);
	}

	public Iterator<MapItem> itemsIterator()
	{
		return new ItemsIterator(getAllNodes(new MapItemFilter(), null));
	}

	public void clear()
	{
		removeAllChildren();
	}

	public void layout()
	{

	}
}
