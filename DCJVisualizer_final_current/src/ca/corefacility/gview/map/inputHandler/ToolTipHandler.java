package ca.corefacility.gview.map.inputHandler;

import edu.umd.cs.piccolo.PNode;

import java.awt.geom.Point2D;

import ca.corefacility.gview.map.items.MapItem;
import ca.corefacility.gview.map.items.TextItem;
import ca.corefacility.gview.map.items.TextItemImp;
import ca.corefacility.gview.style.items.TooltipStyle;

/**
 * Used to handle the tool-tip item that is displayed.
 * @author aaron
 *
 */
public class ToolTipHandler
{
	private final TextItem toolTipItem = new TextItemImp();
	private final Point2D toolTipPosition;
	private final static double OFFSET = 8;
	private final static float maxWidth = 300;

	private String previousLabel;

	private boolean isHidden = true;

	/**
	 * Creates a new ToolTipHandler based on the passed style information.
	 * @param tooltipStyle  The style information for the tooltip.
	 */
	public ToolTipHandler(TooltipStyle tooltipStyle)
	{
		// setup defaults for toolTipItem
		toolTipItem.setFont(tooltipStyle.getFont());
		toolTipItem.setText(null);
		toolTipItem.setPaint(tooltipStyle.getBackgroundPaint());
		toolTipItem.setTextPaint(tooltipStyle.getTextPaint());
		toolTipItem.setOutlinePaint(tooltipStyle.getOutlinePaint());

		((PNode) toolTipItem).setPickable(false); // TODO change this, shouldn't need to cast
		((TextItemImp)toolTipItem).constrainWidthTo(maxWidth);

		toolTipPosition = new Point2D.Double();
		previousLabel = null;
	}

	/**
	 * @return  The tooltip item on the GView map that is displayed.
	 */
	public MapItem getToolTipItem()
	{
		return toolTipItem;
	}

	/**
	 * Updates the tooltip to the passed cursor position, with the passed label.
	 * @param itemCursorPosition  The position on the tooltip on the map.
	 * @param label  The label to display for the tooltip.
	 */
	public void updateToolTip(Point2D itemCursorPosition, String label)
	{
		if (label == null || itemCursorPosition == null)
		{
			hideToolTip();
		}
		else
		{
			toolTipPosition.setLocation(itemCursorPosition.getX() + OFFSET, itemCursorPosition.getY() - OFFSET);

			// if the current label is no different from previous label then don't updated the text
			if (!label.equals(previousLabel))
			{
				toolTipItem.setText(label);
				toolTipItem.setPosition(toolTipPosition);

				previousLabel = label;
			}
			else
			{
				toolTipItem.setPosition(toolTipPosition);
			}

			isHidden = false;
		}
	}

	/**
	 * Hides the tool tip item if necessary.
	 */
	public void hideToolTip()
	{
		if (!isHidden)
		{
			toolTipItem.setText(null);

			isHidden = true;
			previousLabel = null;
		}
	}

	public void setToolTipStyle(TooltipStyle tooltipStyle)
	{	
		toolTipItem.setFont(tooltipStyle.getFont());
		toolTipItem.setText(null);
		toolTipItem.setPaint(tooltipStyle.getBackgroundPaint());
		toolTipItem.setTextPaint(tooltipStyle.getTextPaint());
		toolTipItem.setOutlinePaint(tooltipStyle.getOutlinePaint());
	}
}
