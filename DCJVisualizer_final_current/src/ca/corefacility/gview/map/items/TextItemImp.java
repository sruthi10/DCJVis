package ca.corefacility.gview.map.items;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import ca.corefacility.gview.map.event.GViewEvent;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PPaintContext;

public class TextItemImp extends PText implements TextItem
{
	private static final long serialVersionUID = -7195140821762578175L;
//	private Paint normalPaint;
	private Paint outlinePaint;
	private Paint textPaint;
	
	private MapItemState state = MapItemState.NORMAL;
	
	private float maxWidth = Float.MAX_VALUE;
	private boolean unconstrained = true;
	
	public String getToolTip()
	{
		return null;
	}
	
	public TextItemImp()
	{}
	
	public TextItemImp(String text)
	{
		setText(text);
	}
	
	/**
	 * Constrains the width of this text item to the given width.
	 * @param width
	 */
	public void constrainWidthTo(float width)
	{
	    if (width > 0)
	    {
	        this.maxWidth = width;
	        unconstrained = false;
	    }
	}
	
	@Override
	public void setText(String text)
	{
	    super.setText(text);
	    
	    if (!unconstrained)
	    {
    	    Rectangle2D bounds;
    	    float unconstrainedWidth,constrainedWidth;
    	    
    	    this.setConstrainWidthToTextWidth(true);
    	    bounds = getBounds();
    	    unconstrainedWidth = (float)bounds.getWidth();
    	    
            this.setConstrainWidthToTextWidth(false);
            this.setWidth(maxWidth);
    	    bounds = getBounds();
    	    constrainedWidth = (float)bounds.getWidth();
    	    
    	    if (constrainedWidth > unconstrainedWidth)
    	    {
    	           this.setConstrainWidthToTextWidth(true);
    	    }
	    }
	}
	
	public void setPaint(Paint paint)
	{
//		normalPaint = paint;
		super.setPaint(paint);
	}
	
	public void setOutlinePaint(Paint paint)
	{
		this.outlinePaint = paint;
	}

	public void setPosition(Point2D position)
	{
		setOffset(position.getX(), position.getY());
	}
	
	public void setTextPaint(Paint paint)
	{
		this.textPaint = paint;
		super.setTextPaint(paint);
	}
	
	private void changeState(MapItemState state)
	{
		if (this.state != state)
		{
			this.state = state;
			
			// change this later
			if (state == MapItemState.NORMAL)
			{
				super.setTextPaint(textPaint);
			}
			else if (state == MapItemState.SELECTED)
			{
				super.setTextPaint(Color.YELLOW);
			}
			
			invalidatePaint();
		}
	}
	
	public MapItemState getState()
	{
		return state;
	}
	
	public boolean isMouseOver()
	{
		return (state == MapItemState.MOUSE_OVER || state == MapItemState.MOUSE_OVER_SELECTED);
	}

	public boolean isSelected()
	{
		return (state == MapItemState.SELECTED || state == MapItemState.MOUSE_OVER_SELECTED);
	}
	
	public void removeMouseOver()
	{
		if (state == MapItemState.MOUSE_OVER_SELECTED)
		{
			changeState(MapItemState.SELECTED);
		}
		else
		{
			changeState(MapItemState.NORMAL);
		}
	}
	
	// override paint to support outlines for text box
	protected void paint(PPaintContext paintContext)
	{
		super.paint(paintContext);
		
		if (outlinePaint != null)
		{
			Graphics2D g = paintContext.getGraphics();
			
			g.setPaint(outlinePaint);
			g.draw(getBoundsReference());
		}
	}

	public void removeSelect()
	{
		if (state == MapItemState.MOUSE_OVER_SELECTED)
		{
			changeState(MapItemState.MOUSE_OVER);
		}
		else
		{
			changeState(MapItemState.NORMAL);
		}
	}

	public void setMouseOver()
	{
		if (state == MapItemState.SELECTED)
		{
			changeState(MapItemState.MOUSE_OVER_SELECTED);
		}
		else
		{
			changeState(MapItemState.MOUSE_OVER);
		}
	}

	public void setSelect()
	{
		if (state == MapItemState.MOUSE_OVER)
		{
			changeState(MapItemState.MOUSE_OVER_SELECTED);
		}
		else
		{
			changeState(MapItemState.SELECTED);
		}
	}
	
	public void add(MapComponent component)
	{
		System.err.println("Tried to add to a TextItem");
	}
	
	public void clear()
	{
		System.err.println("Tried to do a clear on a ShapeItem");
	}
	
	public Iterator<MapItem> itemsIterator()
	{
		System.err.println("Tried to do iterator() on a TextItem");

		return null;
	}

	
	public void layout()
	{
		System.err.println("Tried to do layout to a TextItem");
	}

	public void eventOccured(GViewEvent event)
	{
		// TODO Auto-generated method stub
		
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
