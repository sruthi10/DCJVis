package ca.corefacility.gview.map.items;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import ca.corefacility.gview.layout.prototype.BackboneShape;
import ca.corefacility.gview.map.effects.ShapeEffectRenderer;
import ca.corefacility.gview.map.effects.StandardEffect;
import ca.corefacility.gview.map.event.BackboneZoomEvent;
import ca.corefacility.gview.map.event.GViewEvent;
import ca.corefacility.gview.map.event.GViewEventListener;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PPaintContext;

public class AbstractShapeItem extends PNode implements GViewEventListener, ShapeItem
{
	private static final long serialVersionUID = 1176534279755636257L;

	private Shape shape;

	protected Paint normalPaint;

	private ShapeEffectRenderer shapeEffectRenderer;

	private MapItemState state = MapItemState.NORMAL;

	public AbstractShapeItem()
	{
		this.shape = null;
		this.normalPaint = null;

		this.shapeEffectRenderer = new StandardEffect();
	}

	@Override
	public void setPaint(Paint paint)
	{
		this.normalPaint = paint;
		super.setPaint(paint);
	}

	public void setShape(Shape shape)
	{
		if (this.shape instanceof BackboneShape)
		{
			// if old shape existed, remove this item from listening to it
			((BackboneShape) this.shape).removeEventListener(this);
		}

		this.shape = shape;

		// TODO should I add event listeners like this? I only need to listen if my shape will
		// change it's bounds
		if (shape instanceof BackboneShape)
		{
			((BackboneShape) shape).addEventListener(this);
		}

		// firePropertyChange(PROPERTY_CODE_PATH, PROPERTY_PATH, null, path); //used to report bound
		// updates to any listeners
		// not sure if I need this

		updateBoundsFromShape(this.state);

		invalidatePaint(); // no need to change this
	}

	public Shape getShape()
	{
		return this.shape;
	}

	public void updateBoundsFromShape(MapItemState state)
	{
		// TODO add some way to check if we are in view, so that we can limit calculations
		if (this.shape == null)
		{
			setBounds(0, 0, 0, 0);
		}
		else
		{
			Rectangle2D shapeRect = this.shapeEffectRenderer.getDrawnShape(this.shape, state).getBounds2D();
			setBounds(shapeRect);
		}
	}

	@Override
	public boolean intersects(Rectangle2D aBounds)
	{
		boolean result = false;

		if (super.intersects(aBounds))
		{
			if (getPaint() != null && this.shape != null && this.shape.intersects(aBounds))
			{
				result = true;
			}
		}

		return result;
	}

	@Override
	public void paint(PPaintContext paintContext)
	{
		Paint paint = getPaint();
		Graphics2D g2 = paintContext.getGraphics();
		
		this.shapeEffectRenderer.paint(this.shape, g2, paint, this.state);
	}

	private void changeState(MapItemState state)
	{
		if (state != this.state)
		{
			if (this.shapeEffectRenderer.paintChanged(this.state, state))
			{
				invalidatePaint();
				
				updateBoundsFromShape(this.state);
			}

			this.state = state;

			// note: since piccolo uses an arrayList for storing children, moveToFront each time
			// would be slow
			// if (isMouseOver())
			// {
			// moveToFront();
			// }
		}
	}

	public MapItemState getState()
	{
		return this.state;
	}

	public boolean isMouseOver()
	{
		return this.state == MapItemState.MOUSE_OVER || this.state == MapItemState.MOUSE_OVER_SELECTED;
	}

	public boolean isSelected()
	{
		return this.state == MapItemState.SELECTED || this.state == MapItemState.MOUSE_OVER_SELECTED;
	}

	public void removeMouseOver()
	{
		if (this.state == MapItemState.MOUSE_OVER_SELECTED)
		{
			changeState(MapItemState.SELECTED);
		}
		else
		{
			changeState(MapItemState.NORMAL);
		}
	}

	public void removeSelect()
	{
		if (this.state == MapItemState.MOUSE_OVER_SELECTED)
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
		if (this.state == MapItemState.SELECTED)
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
		if (this.state == MapItemState.MOUSE_OVER)
		{
			changeState(MapItemState.MOUSE_OVER_SELECTED);
		}
		else
		{
			changeState(MapItemState.SELECTED);
		}
	}

	protected void setStyled()
	{
		if (this.state == MapItemState.MOUSE_OVER_STYLED)
		{
			changeState(MapItemState.MOUSE_OVER_STYLED);
		}
		else
		{
			changeState(MapItemState.STYLED);
		}
	}

	public void add(MapComponent component)
	{
		// System.err.println("Tried to add to a ShapeItem");
		if (component instanceof PNode)
		{
			addChild((PNode) component);
		}
	}

	public void clear()
	{
		System.err.println("Tried to do a clear on a ShapeItem");
	}

	public Iterator<MapItem> itemsIterator()
	{
		System.err.println("Tried to do itemsIterator() on a ShapeItem");
		return null;
	}

	public void layout()
	{
		System.err.println("Tried to do layout to a ShapeItem");
	}

	public String getToolTip()
	{
		return null;
	}

	public void setShapeEffectRenderer(ShapeEffectRenderer shapeEffectRenderer)
	{
		if (shapeEffectRenderer != null)
		{
			this.shapeEffectRenderer = shapeEffectRenderer;
		}
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.normalPaint == null ? 0 : this.normalPaint.hashCode());
		result = prime * result + (this.shape == null ? 0 : this.shape.hashCode());
		result = prime * result + (this.shapeEffectRenderer == null ? 0 : this.shapeEffectRenderer.hashCode());
		result = prime * result + (this.state == null ? 0 : this.state.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractShapeItem other = (AbstractShapeItem) obj;
		if (this.normalPaint == null)
		{
			if (other.normalPaint != null)
				return false;
		}
		else if (!this.normalPaint.equals(other.normalPaint))
			return false;
		if (this.shape == null)
		{
			if (other.shape != null)
				return false;
		}
		else if (!this.shape.equals(other.shape))
			return false;
		if (this.shapeEffectRenderer == null)
		{
			if (other.shapeEffectRenderer != null)
				return false;
		}
		else if (!this.shapeEffectRenderer.equals(other.shapeEffectRenderer))
			return false;
		if (this.state == null)
		{
			if (other.state != null)
				return false;
		}
		else if (!this.state.equals(other.state))
			return false;
		return true;
	}

	public void eventOccured(GViewEvent event)
	{
		if (event instanceof BackboneZoomEvent)
		{
			if (this.shape instanceof BackboneShape)
			{
				((BackboneShape) this.shape).eventOccured(event);
			}

			updateBoundsFromShape(this.state);
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
}
