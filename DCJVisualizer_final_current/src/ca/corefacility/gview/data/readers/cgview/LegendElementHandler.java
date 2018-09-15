package ca.corefacility.gview.data.readers.cgview;

import java.awt.Color;
import java.awt.Font;
import java.util.Stack;

import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import ca.corefacility.gview.style.items.LegendAlignment;
import ca.corefacility.gview.style.items.LegendItemStyle;
import ca.corefacility.gview.style.items.LegendStyle;
import ca.corefacility.gview.style.items.LegendTextAlignment;

/**
 * Handles the legend cgview xml element.
 * @author aaron
 *
 */
class LegendElementHandler
{
	private LegendStyle currentLegendStyle;
	private Locator locator;

	private Color backgroundColor;
	private float backgroundOpacity;
	private Font font;
	private LegendAlignment legendAlignment;
	private Color fontColor;
	private LegendTextAlignment legendTextAlignment;

	public LegendElementHandler(Color backgroundColor)
	{
		this.backgroundColor = backgroundColor;

		setDefaultProperties();
	}

	private void setDefaultProperties()
	{
		this.backgroundOpacity = 1.0f;
		this.font = new Font("SansSerif", Font.PLAIN, 8);
		this.legendAlignment = CGViewConstants.LEGEND_POSITIONS.get("upper-right");
		this.fontColor = CGViewConstants.COLORS.get("black");
		legendTextAlignment = LegendTextAlignment.LEFT;
	}

	private LegendStyle buildLegendStyle()
	{
		LegendStyle legendStyle = new LegendStyle();
		
		Color backgroundColorOpacity = CGViewConstants.colorToOpacity(this.backgroundColor, this.backgroundOpacity);

		legendStyle.setBackgroundPaint(backgroundColorOpacity);
		legendStyle.setDefaultFont(font);
		legendStyle.setDefaultFontPaint(fontColor);
		legendStyle.setAlignment(legendAlignment);
//		legendStyle.setTextAlignment(this.legendTextAlignment);

		return legendStyle;
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

	/**
     * Handles the legend element and its attributes.
     *
     * @throws SAXException
     */
    //required attributes: none.
    //optional attributes: font, fontColor, position, drawWhenZoomed, textAlignment, backgroundColor, backgroundOpacity, allowLabelClash.
    public void handleLegend(Stack<ElementDetails> context, Locator locator) throws SAXException
    {
    	this.locator = locator;
        for (int p = context.size() - 1; p >= 0; p--)
        {
            ElementDetails elem = context.elementAt(p);

            if (elem.name.equalsIgnoreCase("legend"))
            {
                if (currentLegendStyle != null)
                {
                	throw new SAXException(appendLocation("legend element encountered inside of another legend element"));
                }

                //optional tags
                //fontColor
                if (elem.attributes.getValue("fontColor") != null)
                {
                	fontColor = CGViewConstants.extractColorFor("fontColor", elem);
                }

                //font
                if (elem.attributes.getValue("font") != null)
                {
                	font = CGViewConstants.extractFontFor("font", elem);
                }

                //position
                if (elem.attributes.getValue("position") != null)
                {
                	legendAlignment = CGViewConstants.LEGEND_POSITIONS.get(((elem.attributes.getValue("position"))).toLowerCase());

                    if (legendAlignment == null)
                    {
                        throw new SAXException(appendLocation("value for 'position' attribute in legend element not understood"));
                    }
                }

                //backgroundColor
                if (elem.attributes.getValue("backgroundColor") != null)
                {
                	backgroundColor = CGViewConstants.extractColorFor("backgroundColor", elem);
                }
                
    			// backgroundOpacity
				if (elem.attributes.getValue("backgroundOpacity") != null)
				{
					try
					{
						backgroundOpacity = Float.parseFloat(elem.attributes.getValue("backgroundOpacity"));
					}
					catch (NumberFormatException nfe)
					{
						throw new SAXException(appendLocation("value for 'backgroundOpacity' attribute in legend element not understood"));
					}

					if (backgroundOpacity > 1.0)
					{
						throw new SAXException(appendLocation("value for 'backgroundOpacity' attribute in legend element must be between 0 and 1"));
					}

					if (backgroundOpacity < 0.0)
					{
						throw new SAXException(appendLocation("value for 'backgroundOpacity' attribute in legend element must be between 0 and 1"));
					}
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

                this.currentLegendStyle = buildLegendStyle();
            }
        }
    }

	public LegendStyle getLegendStyle()
	{
		return this.currentLegendStyle;
	}

	public void handleLegendItem(Stack<ElementDetails> context, Locator locator) throws SAXException
	{
		LegendItemElementHandler currentLegendItemHandler = new LegendItemElementHandler(this.legendTextAlignment);
		currentLegendItemHandler.setFont(this.font);
		currentLegendItemHandler.setFontColor(this.fontColor);

		currentLegendItemHandler.handleLegendItem(context, locator);

		LegendItemStyle legendItemStyle = currentLegendItemHandler.buildLegendItemStyle();

		currentLegendStyle.addLegendItem(legendItemStyle);
	}
}