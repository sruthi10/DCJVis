package ca.corefacility.gview.map.items;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Area;

import edu.umd.cs.piccolo.util.PPaintContext;

public class LabelLineItem extends AbstractShapeItem
{
	private static final long serialVersionUID = -6259774007382746508L;
	private BackboneLabelItem labelItem;
	
	public LabelLineItem(BackboneLabelItem labelItem)
	{
		this.labelItem = labelItem;
	}

	public String getToolTip()
	{
		return null;
	}
	
	// this added here only to account for line overlapping label
	@Override
	public void paint(PPaintContext paintContext)
	{	
		Paint paint = getPaint();
		Graphics2D g2 = paintContext.getGraphics();
		
		Area lineArea = new Area(getShape());
		Area labelArea = new Area(labelItem.getFullBounds());
		lineArea.subtract(labelArea);
		
		if (paint != null)
		{
			g2.setPaint(paint);
			g2.fill(lineArea);
		}
	}
}
