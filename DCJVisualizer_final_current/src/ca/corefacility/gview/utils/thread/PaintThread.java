package ca.corefacility.gview.utils.thread;

import java.awt.Graphics2D;
import java.util.concurrent.Callable;

import ca.corefacility.gview.map.items.AbstractShapeItem;
import edu.umd.cs.piccolo.PNode;

/**
 * This class is responsible for executing a portion of the multithreaded paint on the Graphics2D object.
 * It is implemented as both a Thread and a Callable. The call() method will execute the run() method.
 * 
 * Graphics2D and Swing are not designed to be thread-safe. This is NOT garunteed to work, but should function
 * correctly so long as the multithreaded interaction to the Graphics2D object is VERY, VERY minimal. We cannot 
 * change the clip, the transform, the color or the composite.
 * 
 * @author Eric Marinier
 *
 */
public class PaintThread extends Thread implements Callable<Object>
{
	private final PNode node;
	
	private final int start;
	private final int end;
	
	private final Graphics2D graphics;
	
	/**
	 * 
	 * @param start The start index of the range of child nodes to be painted.
	 * @param end The end index of the range of child nodes to be painted.
	 * @param node The parent node of the children to paint.
	 * @param graphics The graphics object to paint on.
	 */
	public PaintThread(int start, int end, PNode node, Graphics2D graphics)
	{
		this.node = node;
		this.start = start;
		this.end = end;
		
		this.graphics = graphics;
	}

	@Override
	public void run()
	{
        for (int i = this.start; i <= this.end; i++) 
        {
            final PNode each = (PNode) this.node.getChild(i);
            this.graphics.fill(((AbstractShapeItem)each).getShape());
        }
	}

	@Override
	public Boolean call() throws Exception
	{
		this.run();
		
		return true;
	}
}
