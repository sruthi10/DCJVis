package ca.corefacility.gview.map.items;

import java.awt.Color;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import ca.corefacility.gview.map.gui.GUISettings;
import ca.corefacility.gview.utils.thread.PaintThread;
import ca.corefacility.gview.utils.thread.ThreadRange;
import ca.corefacility.gview.utils.thread.ThreadService;
import edu.umd.cs.piccolo.util.PPaintContext;

/**
 * This class is designed to allow an override of the fullPaint() method.
 * 
 * The ONLY items that should ever be added to this layer are (shape) map items.
 * 
 * @author Eric Marinier
 * 
 */
public abstract class OptimizedLayer extends Layer
{
	private static final long serialVersionUID = 1L;

	@Override
	public void fullPaint(final PPaintContext paintContext)
	{
		if (GUISettings.getRenderingQuality() == GUISettings.RenderingQuality.LOW && getVisible()
				&& fullIntersects(paintContext.getLocalClip()))
		{
			paintContext.pushTransform(super.getTransform());
			paintContext.pushTransparency(super.getTransparency());

			if (!getOccluded())
			{
				paint(paintContext);
			}

			final int count = getChildrenCount();

			ArrayList<ThreadRange> ranges = ThreadService.getRanges(count);
			ArrayList<Callable<Object>> instances = new ArrayList<Callable<Object>>();

			paintContext.getGraphics().setColor(getConsensusColor());

			// CREATE THREADS:
			for (ThreadRange range : ranges)
			{
				instances.add(new PaintThread(range.getStart(), range.getEnd(), this, paintContext.getGraphics()));
			}

			// WAIT
			ThreadService.invokeAll(instances);

			paintContext.popTransparency(super.getTransparency());
			paintContext.popTransform(super.getTransform());
		}
		else
		{
			super.fullPaint(paintContext);
		}
	}

	@Override
	public void add(MapComponent component)
	{
		if (component instanceof AbstractShapeItem)
		{
			super.add(component);
		}
		else
		{
			throw new IllegalArgumentException("Invalid class type: " + component.getClass());
		}
	}

	public abstract Color getConsensusColor();
}
