package ca.corefacility.gview.map.items;

import java.awt.BasicStroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ca.corefacility.gview.map.effects.ShapeEffectRenderer;
import ca.corefacility.gview.map.effects.StandardEffect;
import ca.corefacility.gview.style.items.LegendAlignment;
import ca.corefacility.gview.style.items.LegendItemStyle;
import ca.corefacility.gview.style.items.LegendStyle;
import edu.umd.cs.piccolo.util.PBounds;

public class LegendItem extends Layer
{
    private static final long serialVersionUID = 1L;
    
    private LegendAlignment alignment;
	private boolean legendDisplayed;
	
	private Point2D alignmentPoint;
	
	private LegendItem()
	{
		alignmentPoint = new Point2D.Double();
	}	
	
	public LegendAlignment getAlignment()
	{
		return alignment;
	}

	public void setAlignment(LegendAlignment alignment)
	{
		this.alignment = alignment;
	}

	public void determineAlignment(Point2D alignmentPoint, Rectangle2D displayBounds, boolean inside)
	{
		Rectangle2D legendBounds = getFullBounds();
		
		double newLeftX = displayBounds.getX();
		double newLeftY = displayBounds.getY();
		
		if (inside)
		{
			switch (alignment)
			{
				case UPPER_LEFT:
					newLeftX += 0;
					newLeftY += 0;
				break;
				
				case UPPER_CENTER:
					newLeftX += (displayBounds.getWidth() - legendBounds.getWidth())/2;
					newLeftY += 0;
				break;
				
				case UPPER_RIGHT:
					newLeftX += displayBounds.getWidth() - legendBounds.getWidth();
					newLeftY += 0;
				break;
				
				case MIDDLE_LEFT:
					newLeftX += 0;
					newLeftY += (displayBounds.getHeight() - legendBounds.getHeight())/2;
				break;
				
				case MIDDLE_CENTER:
					newLeftX += (displayBounds.getWidth() - legendBounds.getWidth())/2;
					newLeftY += (displayBounds.getHeight() - legendBounds.getHeight())/2;
				break;
				
				case MIDDLE_RIGHT:
					newLeftX += displayBounds.getWidth() - legendBounds.getWidth();
					newLeftY += (displayBounds.getHeight() - legendBounds.getHeight())/2;
				break;
				
				case LOWER_LEFT:
					newLeftX +=  0;
					newLeftY +=  displayBounds.getHeight() - legendBounds.getHeight();
				break;
				
				case LOWER_CENTER:
					newLeftX +=  (displayBounds.getWidth() - legendBounds.getWidth())/2;
					newLeftY +=  displayBounds.getHeight() - legendBounds.getHeight();
				break;
				
				case LOWER_RIGHT:
					newLeftX +=  displayBounds.getWidth() - legendBounds.getWidth();
					newLeftY +=  displayBounds.getHeight() - legendBounds.getHeight();
				break;
			}
		}
		else
		{
			switch (alignment)
			{
				case UPPER_LEFT:
					newLeftX += -legendBounds.getWidth();
					newLeftY += -legendBounds.getHeight();
				break;
				
				case UPPER_CENTER:
					newLeftX += (displayBounds.getWidth() - legendBounds.getWidth())/2;
					newLeftY += -legendBounds.getHeight();
				break;
				
				case UPPER_RIGHT:
					newLeftX +=  displayBounds.getWidth();
					newLeftY +=  -legendBounds.getHeight();
				break;
				
				case MIDDLE_LEFT:
					newLeftX += -legendBounds.getWidth();
					newLeftY += (displayBounds.getHeight() - legendBounds.getHeight())/2;
				break;
				
				case MIDDLE_CENTER:
					newLeftX += (displayBounds.getWidth() - legendBounds.getWidth())/2;
					newLeftY += (displayBounds.getHeight() - legendBounds.getHeight())/2;
				break;
				
				case MIDDLE_RIGHT:
					newLeftX +=  displayBounds.getWidth();
					newLeftY +=  (displayBounds.getHeight() - legendBounds.getHeight())/2;
				break;
				
				case LOWER_LEFT:
					newLeftX +=  -legendBounds.getWidth();
					newLeftY +=  displayBounds.getHeight();
				break;
				
				case LOWER_CENTER:
					newLeftX +=  (displayBounds.getWidth() - legendBounds.getWidth())/2;
					newLeftY +=  displayBounds.getHeight();
				break;
				
				case LOWER_RIGHT:
					newLeftX +=  displayBounds.getWidth();
					newLeftY +=  displayBounds.getHeight();
				break;
			}
		}
			
		alignmentPoint.setLocation(newLeftX, newLeftY);
	}
    
	/**
	 * Aligns this legend item within the display bounds.
	 * @param displayBounds  The bounds to align the legend in.
	 * @param inside  Whether to align the legend inside the bounds, or outside.
	 */
    public void alignLegend(Rectangle2D displayBounds, boolean inside)
    {
    	if (legendDisplayed)
    	{		
			determineAlignment(alignmentPoint, displayBounds, inside);
			
			this.setOffset(alignmentPoint.getX(), alignmentPoint.getY());
    	}
    }
    
	/**
	 * Builds up a list of LegendItems from the appropriate data needing a legend.
	 * @param dataStyle
	 * @return  A list of LegendItems.
	 */
	private static List<LegendEntryItem> generateLegendItems(LegendStyle legendStyle)
	{
		List<LegendEntryItem> legendItems = new LinkedList<LegendEntryItem>();
		
		Iterator<LegendItemStyle> legendStylesIter = legendStyle.legendItems();
		while (legendStylesIter.hasNext())
		{
			LegendItemStyle itemStyle = legendStylesIter.next();
			
			LegendEntryItem legendItem = new LegendEntryItem(itemStyle);
			
			legendItems.add(legendItem);
		}
		
		return legendItems;
	}
    
	private static BasicItem buildLegend(LegendStyle lStyle, List<LegendEntryItem> legendItems)
	{	
		BasicItem mainLegendArea = new BasicItem();		
		float legendBorderSpacing = 5;
		
		// only display legend if there are items to display
		if (legendItems.size() > 0)
		{	
			mainLegendArea.setPaint(lStyle.getBackgroundPaint());
			
			// handles outline for legend area
			if (lStyle.getOutlinePaint() != null)
			{
				ShapeEffectRenderer outlineRenderer = new StandardEffect(lStyle.getOutlinePaint(), new BasicStroke());
				mainLegendArea.setShapeEffectRenderer(outlineRenderer);
			}
			else
			{
				mainLegendArea.setShapeEffectRenderer(new StandardEffect(null, null));
			}
			
			float currOffsetY = legendBorderSpacing;		
			for (LegendEntryItem item : legendItems)
			{
				mainLegendArea.add(item);
				item.offset(legendBorderSpacing, currOffsetY);
				
				float trueLegendItemSpacing = (float)item.getFullBounds().getHeight()/2;
				
				currOffsetY += item.getFullBounds().getHeight() + trueLegendItemSpacing;
			}
			
			PBounds legendBoundsWithoutPadding = mainLegendArea.getFullBounds();
			
			Rectangle2D mainLegendBoundsWithPadding = new Rectangle2D.Float(0,0, (float)legendBoundsWithoutPadding.getWidth() + 2*legendBorderSpacing
					, (float)legendBoundsWithoutPadding.getHeight() + 2*legendBorderSpacing);
			
			// add in legendBoxPadding to account for padding at bottom of the box
			// multiply by 2 since there are two sides to add the padding to
			mainLegendArea.setShape(mainLegendBoundsWithPadding);
			
			// redo all offsets for legend items to align them in this legend box
			for (LegendEntryItem item : legendItems)
			{
				Rectangle2D itemBounds = item.getFullBounds();
				
				float offset = 0;
				switch (item.getAlignment())
				{
					case LEFT: // do nothing
					break;
				
					// shift text item to align in the center
					case CENTER:
						offset = (float)(Math.abs(legendBoundsWithoutPadding.getWidth() - itemBounds.getWidth())/2.0);
					break;
					
					// shift text item to align on the right
					case RIGHT:
						offset = (float)(Math.abs(legendBoundsWithoutPadding.getWidth() - itemBounds.getWidth()));
					break;
				}
				
				item.offset(offset, 0);
			}
		}
		
		return mainLegendArea;
	}
	
    public static LegendItem buildLegend(LegendStyle lStyle)
    {   	
    	LegendItem legendItem = new LegendItem();
    	
		legendItem.alignment = lStyle.getAlignment();
		legendItem.legendDisplayed = lStyle.isDisplayLegend();
		
		List<LegendEntryItem> legendItems = generateLegendItems(lStyle);
		BasicItem legendArea = buildLegend(lStyle, legendItems);
		legendItem.add(legendArea);
		
		
		return legendItem;
    }
}
