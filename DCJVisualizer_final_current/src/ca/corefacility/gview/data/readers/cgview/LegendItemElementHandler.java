package ca.corefacility.gview.data.readers.cgview;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.util.Stack;

import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import ca.corefacility.gview.style.items.LegendItemStyle;
import ca.corefacility.gview.style.items.LegendTextAlignment;

/**
 * Handles the legendItem cgview xml element.
 * @author aaron
 *
 */
class LegendItemElementHandler
{
	private Locator locator;

	private String label;
	private boolean drawSwatch = false;
	private Color swatchColor = Color.black;
	private float swatchOpacity;
	private Font font;
	private Paint fontColor;
	private LegendTextAlignment legendTextAlignment;

	private LegendItemStyle currentLegendItemStyle = null;

	public LegendItemElementHandler(LegendTextAlignment legendTextAlignment)
	{
		drawSwatch = false;
		swatchOpacity = 1.0f;
		swatchColor = CGViewConstants.COLORS.get("black");
		this.legendTextAlignment = legendTextAlignment;
	}

	public void setFont(Font font)
	{
		this.font = font;
	}

	public void setFontColor(Paint fontColor)
	{
		this.fontColor = fontColor;
	}

	public LegendItemStyle buildLegendItemStyle()
	{
		LegendItemStyle legendItemStyle = new LegendItemStyle(this.label);
		
		Color swatchColorOpacity = CGViewConstants.colorToOpacity(this.swatchColor, this.swatchOpacity);

		legendItemStyle.setShowSwatch(this.drawSwatch);
		legendItemStyle.setTextFont(this.font);
		legendItemStyle.setTextPaint(this.fontColor);
		legendItemStyle.setSwatchPaint(swatchColorOpacity);
		legendItemStyle.setTextAlignment(this.legendTextAlignment);

		return legendItemStyle;
	}

	/**
	 * Appends the location onto the passed error string.
	 *
	 * @param error
	 *            The string which to append a location on.
	 * @return The error string + the location.
	 */
	private String appendLocation(String error)
	{
		String errorLocation = error;

		if (this.locator != null)
		{
			errorLocation = " in " + this.locator.getSystemId() + " at line " + this.locator.getLineNumber() + " column " + this.locator.getColumnNumber();
			if (error != null)
			{
				errorLocation = error + errorLocation;
			}
		}
		return errorLocation;
	}

	public void handleLegendItem(Stack<ElementDetails> context, Locator locator) throws SAXException
	{
		this.locator = locator;
        for (int p = context.size() - 1; p >= 0; p--)
        {
            ElementDetails elem = context.elementAt(p);

            if (elem.name.equalsIgnoreCase("legendItem"))
            {
                if (currentLegendItemStyle != null)
                {
                    throw new SAXException(appendLocation("legendItem element encountered inside of another legendItem element"));
                }
                else if (elem.attributes.getValue("text") == null)
                {
                    throw new SAXException(appendLocation("legendItem element is missing 'text' attribute"));
                }
                else
                {
	                label = elem.attributes.getValue("text");

	                //fontColor
	                if (elem.attributes.getValue("fontColor") != null)
	                {
	                	fontColor = CGViewConstants.extractColorFor("fontColor", elem);
	                }

	                //swatchColor
	                if (elem.attributes.getValue("swatchColor") != null)
	                {
	                	swatchColor = CGViewConstants.extractColorFor("swatchColor", elem);
	                }
	                
					// swatchOpacity
					if (elem.attributes.getValue("swatchOpacity") != null)
					{
						try
						{
							swatchOpacity = Float.parseFloat(elem.attributes.getValue("swatchOpacity"));
						}
						catch (NumberFormatException nfe)
						{
							throw new SAXException(appendLocation("value for 'swatchOpacity' attribute in legendItem element not understood"));
						}

						if (swatchOpacity > 1.0)
						{
							throw new SAXException(appendLocation("value for 'swatchOpacity' attribute in legendItem element must be between 0 and 1"));
						}

						if (swatchOpacity < 0.0)
						{
							throw new SAXException(appendLocation("value for 'swatchOpacity' attribute in legendItem element must be between 0 and 1"));
						}
					}

	                //drawSwatch
	                if (elem.attributes.getValue("drawSwatch") != null)
	                {
	                	Boolean drawSwatch = CGViewConstants.BOOLEANS.get(elem.attributes.getValue("drawSwatch"));

	                	if (drawSwatch == null)
	                	{
	                		throw new SAXException(appendLocation("value for 'drawSwatch' attribute in legendItem element not understood"));
	                	}
	                	else
	                	{
	                		this.drawSwatch = drawSwatch;
	                	}
	                }

	                //font
	                if (elem.attributes.getValue("font") != null)
	                {
	                	font = CGViewConstants.extractFontFor("font", elem);
	                }
	                
	                //textAlignment
	                if (elem.attributes.getValue("textAlignment") != null)
	                {
	                	LegendTextAlignment localTextAlignment = CGViewConstants.LEGEND_ALIGNMENTS.get(elem.attributes.getValue("textAlignment"));

	                	if (localTextAlignment == null)
	                	{
	                		throw new SAXException(appendLocation("value for 'textAlignment' attribute in legendItem element not understood"));
	                	}
	                	else
	                	{
	                		this.legendTextAlignment = localTextAlignment;
	                	}
	                }
                }
            }
		}
	}
}