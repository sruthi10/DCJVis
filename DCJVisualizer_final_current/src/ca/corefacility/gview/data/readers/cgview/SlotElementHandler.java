package ca.corefacility.gview.data.readers.cgview;

import java.awt.Color;
import java.awt.Font;
import java.util.Stack;

import org.biojava.bio.seq.StrandedFeature;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import ca.corefacility.gview.layout.feature.FeatureShapeRealizer;
import ca.corefacility.gview.style.datastyle.SlotStyle;

/**
 * Handles the featureSlot cgview xml element.
 * @author aaron
 *
 */
class SlotElementHandler
{
	private SlotStyle currentSlotStyle;
	private SlotNumberManager slotNumberManager;
			
	private StrandedFeature.Strand strand = StrandedFeature.UNKNOWN;
	private FeatureStyleTemplate styleTemplate;
	private FeatureStyleManager featureBuilder;
	
	private double featureThickness;
	private Boolean globalShowLabels = null;
		
	private Locator locator;

	private FeatureElementHandler currentFeatureElementHandler;
	
	private int sequenceLength;
	
	/**
	 * A class used to deal with CGView feature slots.
	 * 
	 * @author Aaron Petkau
	 * @param annotationHolder 
	 * 
	 */
	public SlotElementHandler(SlotNumberManager slotNumberManager, FeatureStyleManager featureBuilder, int sequenceLength)
	{
		this.featureBuilder = featureBuilder;
		this.slotNumberManager = slotNumberManager;
		
		styleTemplate = new FeatureStyleTemplate();
		
		this.sequenceLength = sequenceLength;
	}
	
	public void setUseColoredLabelBackgrounds(boolean useColoredLabelBackgrounds)
	{
		styleTemplate.useColoredLabelBackgrounds  = useColoredLabelBackgrounds;
	}
	
	public void setGlobalLabelColor(Color globalLabelColor)
	{
		styleTemplate.globalLabelColor = globalLabelColor;
	}
	
	public void setLabelLineThickness(float labelLineThickness)
	{
		styleTemplate.labelLineThickness = labelLineThickness;
	}
	
	public void setGlobalShowLabels(Boolean globalShowLabels)
	{
		this.globalShowLabels = globalShowLabels;
	}
	
	public void setFeatureThickness(double featureThickness)
	{
		this.featureThickness = featureThickness;
	}
	
	/**
	 * Creates a feature within this slot.
	 * 
	 * @return The newly created feature.
	 */
	private FeatureElementHandler createFeatureElementHandler()
	{
		StrandedFeature.Template featureTemplate = new StrandedFeature.Template();
		featureTemplate.strand = this.strand;
	
		FeatureElementHandler handler = new FeatureElementHandler(featureBuilder , featureTemplate,
				new FeatureStyleTemplate(styleTemplate), globalShowLabels, sequenceLength);
	
		return handler;
	}
	
	public void endFeatureElement()
	{
		currentFeatureElementHandler = null;
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
	
	public void handleFeatureSlot(Stack<ElementDetails> context, Locator locator) throws SAXException
	{
		this.locator = locator;
		for (int p = context.size() - 1; p >= 0; p--)
		{
			ElementDetails elem = (ElementDetails) context.elementAt(p);
			if (elem.name.equalsIgnoreCase("featureSlot") || elem.name.equalsIgnoreCase("plotSlot"))
			{
				if (currentSlotStyle != null)
				{
					throw new SAXException(appendLocation("featureSlot element encountered in another 'featureSlot' element"));
				}
				if (elem.attributes.getValue("strand") == null)
					// an error because no strand given
					throw new SAXException(appendLocation("featureSlot element is missing 'strand' attribute"));
				else
				{
					if (elem.attributes.getValue("strand").equalsIgnoreCase("direct"))
					{
						this.strand = StrandedFeature.POSITIVE;
					}
					else if (elem.attributes.getValue("strand").equalsIgnoreCase("reverse"))
					{
						this.strand = StrandedFeature.NEGATIVE;
					}
					else
					{
						this.strand = StrandedFeature.UNKNOWN;
						throw new SAXException(appendLocation("value for 'strand' attribute in featureSlot element not understood"));
					}
				}

				// optional tags
				// featureThickness
				if (elem.attributes.getValue("featureThickness") != null)
				{
					try
					{
						this.featureThickness = CGViewConstants.extractRealFor("featureThickness", elem, CGViewConstants.FEATURE_THICKNESSES);
					}
					catch (Exception e)
					{
						// throw new SAXException (error);
						throw new SAXException(appendLocation("[warning] value for 'featureThickness' attribute in cgview element not understood"));
					}
				}


				// show shading
				if (elem.attributes.getValue("showShading") != null)
				{
					Boolean showShadingValue = CGViewConstants.BOOLEANS.get(elem.attributes.getValue("showShading"));

					if (showShadingValue == null)
					{
						throw new SAXException(appendLocation("value for 'showShading' attribute in feature element not understood"));
					}
					else
					{
						styleTemplate.showShading = showShadingValue;
					}
				}
				
				this.currentSlotStyle = buildSlotStyle();
			}
		}
	}

	private SlotStyle buildSlotStyle()
	{
		SlotStyle slotStyle = null;
		FeatureShapeRealizer shapeRealizer;
		
		if (strand == StrandedFeature.POSITIVE)
		{
			shapeRealizer = FeatureShapeRealizer.CLOCKWISE_ARROW;
		}
		else if (strand == StrandedFeature.NEGATIVE)
		{
			shapeRealizer = FeatureShapeRealizer.COUNTERCLOCKWISE_ARROW;
		}
		else
		{
			shapeRealizer = FeatureShapeRealizer.NO_ARROW;
		}
		
		slotStyle = featureBuilder.createNewSlotStyle(slotNumberManager, strand, shapeRealizer,
				styleTemplate, featureThickness, globalShowLabels);
		
		return slotStyle;
	}

	public void setLabelFont(Font labelFont)
	{
		styleTemplate.labelFont = labelFont;
	}

	public void setLabelLineLength(float labelLineLength)
	{
		styleTemplate.labelLineLength = labelLineLength;
	}

	public void setShowShading(boolean showShading)
	{
		styleTemplate.showShading = showShading;
	}

	public void setGiveFeaturePositions(boolean giveFeaturePositions)
	{
		styleTemplate.giveFeaturePositions = giveFeaturePositions;
	}

	public void handleFeature(Stack<ElementDetails> context, Locator locator2) throws SAXException
	{
		this.currentFeatureElementHandler = createFeatureElementHandler();
		this.currentFeatureElementHandler.handleFeature(context, locator2);
	}

	public void handleFeatureRange(Stack<ElementDetails> context,
			Locator locator2) throws SAXException
	{
		if (this.currentFeatureElementHandler != null)
		{
			this.currentFeatureElementHandler.handleFeatureRange(context, locator2);
		}
		else
		{
			throw new SAXException(appendLocation("featureRange element encountered outside of a feature element"));
		}
	}

	public void endFeatureRange() throws SAXException
	{
		if (this.currentFeatureElementHandler != null)
		{
			this.currentFeatureElementHandler.endFeatureRange();
		}
		else
		{
			throw new SAXException(appendLocation("featureRange element encountered outside of a featureSlot element"));
		}
	}

	public void endSlot()
	{
		featureBuilder.endSlot();
	}
}