package ca.corefacility.gview.map.items;

import java.awt.Font;
import java.awt.Paint;
import java.awt.geom.Point2D;

/**
 * Describes an item which consists of text.
 * @author Aaron Petkau
 *
 */
public interface TextItem extends MapItem
{
	public void setFont(Font font);
	public void setText(String text);
	
	public void setTextPaint(Paint paint);
	
	public void setOutlinePaint(Paint paint);
	
	public void setPosition(Point2D position); // TODO should I have a setPosition here,
	// since BackboneTextItem is supposed to be pinned to a location (and we now have setPosition available).
}
