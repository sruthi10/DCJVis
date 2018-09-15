package ca.corefacility.gview.map.items;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import org.biojava.bio.symbol.Location;

import ca.corefacility.gview.layout.prototype.SequencePoint;
import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.map.event.BackboneZoomEvent;
import ca.corefacility.gview.map.event.GViewEvent;
import ca.corefacility.gview.utils.Dimension2DDouble;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PPaintContext;

public abstract class AbstractBackboneTextItem extends PText implements BackboneTextItem
{
	private static final long serialVersionUID = -6753987016003059634L;
	protected Paint normalPaint;
	protected Paint outlinePaint;
	protected Paint backgroundPaint;
	protected Backbone backbone;

	private Location spanningLocation;
	private SequencePoint pinnedPoint;

	private MapItemState state = MapItemState.NORMAL;

	public AbstractBackboneTextItem(Backbone backbone, SequencePoint pinnedPoint)
	{
		// assert (backbone != null);

		this.backbone = backbone;

		pinTo(pinnedPoint);
	}

	public AbstractBackboneTextItem(String text)
	{
		super(text);
	}

	public String getToolTip()
	{
		return null;
	}

	@Override
	public void setFont(Font font)
	{
		super.setFont(font);

		// does this in order to re-calculate bounds/pin to center
		pinTo(pinnedPoint);
	}
	
	@Override
	public void setPaint(Paint paint)
	{
		this.backgroundPaint = paint;
		super.setPaint(paint);
	}

	public void setOutlinePaint(Paint paint)
	{
		this.outlinePaint = paint;
	}

	@Override
	public void setTextPaint(Paint textPaint)
	{
		normalPaint = textPaint;
		super.setTextPaint(textPaint);
	}

	@Override
	public void setText(String text)
	{
		super.setText(text);

		pinTo(pinnedPoint);
	}
	
	@Override
	public Dimension2D getDimension()
	{
		Rectangle2D bounds = getBounds();
		return new Dimension2DDouble(bounds.getWidth(), bounds.getHeight()); // TODO should I create a new object here every time?
	}

	public void setPosition(Point2D position)
	{
		// // set the position of the center of the text
		Rectangle2D bounds = getTextBounds();
		double halfWidth = bounds.getWidth() / 2;
		double halfHeight = bounds.getHeight() / 2;

		// TODO determine how to set it so that pinned point specifies center of text item
		setOffset(position.getX() - halfWidth, position.getY() - halfHeight);

		// spanningLocation = backbone.getSpannedLocation(getBounds());
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
		System.err.println("Tried to do itemsIterator() on a BackboneTextItem");
		return null;
	}

	public void clear()
	{
		removeAllChildren();
	}

	public void layout()
	{
		System.err.println("Tried to do layout to a TextItem");
	}

	public void eventOccured(GViewEvent event)
	{
		if (event instanceof BackboneZoomEvent)
		{
			BackboneZoomEvent zoomEvent = (BackboneZoomEvent) event;

			Backbone backbone = zoomEvent.getBackbone();

			this.backbone = backbone; // should I reset this here?

			setPosition(backbone.translate(pinnedPoint));
			invalidatePaint();
		}
	}

	public void pinTo(SequencePoint pinnedPoint)
	{
		if (pinnedPoint != null)
		{
			this.pinnedPoint = pinnedPoint;

			setPosition(backbone.translate(pinnedPoint));
		}
	}

	@Override
	public void translate(double base, double heightFromBackbone)
	{
		pinnedPoint.translate(base, heightFromBackbone);
		pinTo(pinnedPoint);
	}

	public SequencePoint getPinnedPoint()
	{
		return pinnedPoint;
	}

	public Rectangle2D getTextBounds()
	{
		return getGlobalFullBounds();
	}

	// override paint to support outlines for text box
	@Override
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

	public Location getLocation()
	{
		return spanningLocation;
	}

	private void changeState(MapItemState state)
	{
		if (this.state != state)
		{
			this.state = state;

			// change this later
			if (state == MapItemState.NORMAL)
			{
				super.setPaint(backgroundPaint);
				super.setTextPaint(normalPaint);
			}
			else if (state == MapItemState.SELECTED || state == MapItemState.MOUSE_OVER_SELECTED)
			{
				super.setPaint(normalPaint); // color when it is selected
				
				
				if (backgroundPaint != null)
				{
					if(backgroundPaint instanceof Color)
					{
						Color color = new Color(((Color) backgroundPaint).getRGB());
						
						super.setTextPaint(color);
					}
					else
					{
						super.setTextPaint(backgroundPaint);
					}
				}
				else
				{
					super.setTextPaint(Color.white);
				}
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

	public void setBackbone(Backbone backbone)
	{
		this.backbone = backbone;
	}

	public void updateClippedLocation(Location clipLocation)
	{
		// maybe don't need this here, not as much calculations to update positions
		// if (spanningLocation.overlaps(clipLocation))
		// {
		//			
		// }
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