package ca.corefacility.gview.map.items;

import java.awt.geom.Rectangle2D;

import ca.corefacility.gview.style.items.LegendItemStyle;
import ca.corefacility.gview.style.items.LegendTextAlignment;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

public class LegendEntryItem extends Layer
{
    private static final long serialVersionUID = 1L;
    
    private final LegendItemStyle style;
	
    public LegendEntryItem(LegendItemStyle style)
    {
    	this.style = style;
    	
    	PText legendTextNode = new PText(style.getText());
    	legendTextNode.setFont(style.getTextFont());
    	legendTextNode.setTextPaint(style.getTextPaint());
    	addChild(legendTextNode);
    	
    	Rectangle2D bounds = legendTextNode.getFullBounds();
    	float swatchHeight = (float)bounds.getHeight();
    	float swatchWidth = swatchHeight;
    	float swatchTextSpacing = swatchWidth/2;
    	
    	if (style.isShowSwatch() && style.getSwatchPaint() != null)
    	{
	    	PPath swatch = PPath.createRectangle(0, 0, swatchWidth, swatchHeight);
	    	
	    	swatch.setPaint(style.getSwatchPaint());
	    	swatch.setStrokePaint(null);
	    	swatch.setStroke(null);
	    	addChild(swatch);
	    	
	    	float legendTextHeight = (float)legendTextNode.getFullBounds().getHeight();
	    	float swatchBoxHeight = (float)swatch.getFullBounds().getHeight();
	    	
	    	if (legendTextHeight < swatchBoxHeight)
	    	{
		    	// offset center of text to center of legend item
		    	float newCenterY = (float)getFullBounds().getHeight()/2;
		    	float currCenterY = legendTextHeight/2;
		    	
		    	legendTextNode.offset(0, newCenterY-currCenterY);
	    	}
	    	else
	    	{
		    	// offset center of swatch to center legend item
		    	float newCenterY = (float)getFullBounds().getHeight()/2;
		    	float currCenterY = swatchBoxHeight/2;
	
		    	swatch.offset(0, (newCenterY-currCenterY));
	    	}
	    	
	    	legendTextNode.offset(swatchWidth+swatchTextSpacing, 0);
    	}
    }
    
    public LegendTextAlignment getAlignment()
    {
    	return this.style.getTextAlignment();
    }
    
    public LegendItemStyle getLegendItemStyle()
    {
    	return this.style;
    }
}
