package ca.corefacility.gview.data.readers.cgview;

import java.awt.Color;
import java.awt.Font;
import java.util.Stack;

import org.biojava.bio.seq.Sequence;
import org.biojava.bio.seq.impl.SimpleSequenceFactory;
import org.biojava.bio.symbol.SymbolList;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import ca.corefacility.gview.data.BlankSymbolList;
import ca.corefacility.gview.style.GlobalStyle;
import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.style.items.BackboneStyle;
import ca.corefacility.gview.style.items.LegendStyle;
import ca.corefacility.gview.style.items.RulerStyle;
import ca.corefacility.gview.style.items.TooltipStyle;

/**
 * Handles the cgview element.
 * @author aaron
 *
 */
class CGViewElementHandler
{
	private Locator locator;

	private Sequence currentSequence;
	private MapStyle currentMapStyle;
	private FeatureStyleManager featureStyleManager;
	private SlotNumberManager slotNumberManager;
	private SlotElementHandler currentFeatureSlot;
	private LegendElementHandler currentLegendElementHandler;

	private int cgviewLength;
	private Color backboneColor;
	private double backboneRadius;
	private double backboneThickness;
	private Color backgroundColor;
	private double featureSlotSpacing;
	private double featureThickness;
	private boolean giveFeaturePositions;
	private Boolean globalLabel;
	private Color globalLabelColor;
	private int height;
	private Font labelFont;
	private float labelLineLength;
	private double labelLineThickness;
	private Color longTickColor;
//	private double minimumFeatureLength;
	private Font rulerFont;
	private Color rulerFontColor;
//	private double rulerPadding;
	private Color shortTickColor;
//	private double shortTickThickness;
	private boolean showShading;
	private float tickDensity;
	private double tickLength;
	private double tickThickness;
	private boolean useColoredLabelBackgrounds;
	private int width;

	public CGViewElementHandler()
	{
		slotNumberManager = new SlotNumberManager();
		currentMapStyle = new MapStyle();

		setCGViewDefaults();
	}

	private void setCGViewDefaults()
	{
//		arrowheadLength =
		backboneColor = CGViewConstants.COLORS.get("gray");
		backboneRadius = 190.0;
		backboneThickness = CGViewConstants.BACKBONE_THICKNESSES.get("medium");
		backgroundColor = CGViewConstants.COLORS.get("white");
//		borderColor =
		featureSlotSpacing = CGViewConstants.FEATURESLOT_SPACINGS.get("medium");
		featureThickness = CGViewConstants.FEATURE_THICKNESSES.get("medium");
		giveFeaturePositions = false;
		globalLabel = null; // globalLabel = true;
		globalLabelColor = null;
		height = 700;
//		isLinear = false;
		labelFont = new Font("SansSerif", Font.PLAIN, 10);
		labelLineLength = CGViewConstants.LABEL_LINE_LENGTHS.get("medium").floatValue();
		labelLineThickness = CGViewConstants.LABEL_LINE_THICKNESSES.get("medium");
//		labelPlacementQuality =
//		labelsToKeep =
		longTickColor = CGViewConstants.COLORS.get("black");
//		minimumFeatureLength = CGViewConstants.MINIMUM_FEATURE_LENGTHS.get("xxx-small");
//		moveInnerLabelsToOuter = true;
//		origin = 12;
		rulerFont = new Font("SansSerif", Font.PLAIN, 8);
		rulerFontColor = CGViewConstants.COLORS.get("black");
//		rulerPadding = 10.0;
//		rulerUnits =
		shortTickColor = CGViewConstants.COLORS.get("black");
//		shortTickThickness = CGViewConstants.TICK_THICKNESSES.get("medium");
//		showBorder = true;
		showShading = true;
//		showWarning = false;
		tickDensity = 1.0f;
		tickLength = CGViewConstants.TICK_LENGTHS.get("medium");
		tickThickness = CGViewConstants.TICK_THICKNESSES.get("medium");
//		title = null;
//		titleFontColor = CGViewConstants.COLORS.get("black");
//		titleFont = new Font("SansSerif", Font.PLAIN, 12);
		useColoredLabelBackgrounds = false;
//		useInnerLabels =
//		warningFont = new Font("SansSerif", Font.PLAIN, 8);
//		warningFontColor = CGViewConstants.COLORS.get("black");
		width = 700;
//		zeroTickColor = CGViewConstants.COLORS.get("black");
	}

	private void buildSequenceStyle()
	{
		SimpleSequenceFactory seqFactory = new SimpleSequenceFactory();
		SymbolList blankList = new BlankSymbolList(this.cgviewLength);
		this.currentSequence = seqFactory.createSequence(blankList, null, null, null);

		this.currentMapStyle = new MapStyle();
		GlobalStyle globalStyle = currentMapStyle.getGlobalStyle();
		RulerStyle rulerStyle = globalStyle.getRulerStyle();

		BackboneStyle backboneStyle = globalStyle.getBackboneStyle();
		backboneStyle.setPaint(this.backboneColor);
		backboneStyle.setInitialBackboneLength(2*Math.PI*this.backboneRadius);
		backboneStyle.setThickness(this.backboneThickness);
		
		if (showShading)
		{
		    backboneStyle.setShapeEffectRenderer(CGViewConstants.getDefaultShadedEffect());
		}
		else
		{
		    backboneStyle.setShapeEffectRenderer(CGViewConstants.getDefaultEffect());
		}

		globalStyle.setBackgroundPaint(this.backgroundColor);
		globalStyle.setSlotSpacing(this.featureSlotSpacing);

		// feature thickness
		// globalLabel
		// globalLabelColor

		globalStyle.setDefaultHeight(height);

		// labelFont
		// labelLineLength
		// labelLineThickness

		rulerStyle.setMajorTickPaint(this.longTickColor);
		rulerStyle.setFont(this.rulerFont);
		rulerStyle.setTextPaint(this.rulerFontColor);

		// rulerPadding?

		rulerStyle.setMinorTickPaint(this.shortTickColor);
		rulerStyle.setMinorTickThickness(this.tickThickness);

		// showShading
        if (showShading)
        {
            rulerStyle.setShapeEffectRenderer(CGViewConstants.getDefaultShadedEffect());
        }
        else
        {
            rulerStyle.setShapeEffectRenderer(CGViewConstants.getDefaultEffect());
        }

		rulerStyle.setTickDensity(this.tickDensity);
		rulerStyle.setMajorTickLength(this.tickLength);
		rulerStyle.setTickThickness(this.tickThickness);

		// useColoredLabelBackgrounds

		globalStyle.setDefaultWidth(this.width);



		/** extra settings **/

		// by default, set the ruler text background colour to a translucent version of the background colour
		rulerStyle.setTextBackgroundPaint(new Color(backgroundColor.getRed(),
				backgroundColor.getGreen(), backgroundColor.getBlue(), 100));

		// sets style for tooltips
		TooltipStyle tooltip = globalStyle.getTooltipStyle();
		tooltip.setFont(new Font("SansSerif", Font.PLAIN, 12));
		tooltip.setBackgroundPaint(new Color(0xCCCCFF));
		tooltip.setOutlinePaint(new Color(0x333399));
		tooltip.setTextPaint(Color.black);

		featureStyleManager = new FeatureStyleManager(currentSequence, currentMapStyle);
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
	 * Prints out the error, handling location.
	 *
	 * @param error
	 *            The error message to print.
	 */
	private void printError(String error)
	{
		System.err.println(appendLocation(error));
	}

	private SlotElementHandler createFeatureSlot()
	{
		SlotElementHandler handler = new SlotElementHandler(slotNumberManager, featureStyleManager, cgviewLength);

		// set defaults for slot
		handler.setFeatureThickness(this.featureThickness);
		handler.setGiveFeaturePositions(this.giveFeaturePositions);
		handler.setGlobalShowLabels(this.globalLabel);
		handler.setGlobalLabelColor(this.globalLabelColor);
		handler.setLabelFont(this.labelFont);
		handler.setLabelLineLength(this.labelLineLength);
		handler.setLabelLineThickness((float)this.labelLineThickness);
		handler.setShowShading(this.showShading);
		handler.setUseColoredLabelBackgrounds(this.useColoredLabelBackgrounds);

		return handler;
	}

	public void endSlot()
	{
		currentFeatureSlot.endSlot();
		currentFeatureSlot = null;
	}

	/**
	 * Handles the cgview element and its attributes.
	 *
	 * @throws SAXException
	 */
	// required attributes: sequenceLength.
	// optional attributes: title, rulerUnits, rulerFont, rulerPadding, labelFont, useInnerLabels,
	// featureThickness, featureSlotSpacing, backboneThickness, arrowheadLength,
	// minimumFeatureLength, titleFont, titleFontColor, rulerFontColor, origin, tickThickness,
	// tickLength, labelLineThickness, labelLineLength, backboneColor, backgroundColor,
	// longTickColor, giveFeaturePositions, shortTickColor, zeroTickColor, width, height,
	// backboneRadius, labelPlacementQuality, useColoredLabelBackgrounds, showWarning, warningFont,
	// info, warningFontColor, showShading, showBorder, borderColor, moveInnerLabelsToOuter,
	// globalLabel, labelsToKeep, globalLabelColor, tickDensity.
	public void handleCgview(Stack<ElementDetails> context, Locator locator) throws SAXException
	{
		this.locator = locator;

		for (int p = context.size() - 1; p >= 0; p--)
		{
			ElementDetails elem = (ElementDetails) context.elementAt(p);
			if (elem.name.equalsIgnoreCase("cgview"))
			{
				if (elem.attributes.getValue("sequenceLength") == null)
				{
					throw new SAXException(appendLocation("cgview element is missing 'sequenceLength' attribute"));
				} else
				{
					try
					{
						this.cgviewLength = Integer.parseInt(elem.attributes.getValue("sequenceLength"));
					}
					catch (NumberFormatException nfe)
					{
						throw new SAXException(appendLocation("value for 'sequenceLength' attribute in cgview element not understood"));
					}

					if (this.cgviewLength > CGViewConstants.MAX_BASES)
					{
						throw new SAXException(
								appendLocation("value for 'sequenceLength' attribute in cgview element must be less than " + CGViewConstants.MAX_BASES));
					}

					if (this.cgviewLength < CGViewConstants.MIN_BASES)
					{
						throw new SAXException(appendLocation("value for 'sequenceLength' attribute in cgview element must be greater than "
								+ CGViewConstants.MIN_BASES));
					}
				}

				// useColoredLabelBackgrounds
				if (elem.attributes.getValue("useColoredLabelBackgrounds") != null)
				{
					Boolean useColoredLabelBackgrounds = CGViewConstants.BOOLEANS.get(elem.attributes.getValue("useColoredLabelBackgrounds"));

					if (useColoredLabelBackgrounds == null)
					{
						throw new SAXException(appendLocation("value for 'useColoredLabelBackgrounds' attribute in cgview element not understood"));
					} else
					{
						this.useColoredLabelBackgrounds = useColoredLabelBackgrounds.booleanValue();
					}
				}

				// rulerFont
				if (elem.attributes.getValue("rulerFont") != null)
				{
					this.rulerFont = CGViewConstants.extractFontFor("rulerFont", elem);
				}

				// labelFont
				if (elem.attributes.getValue("labelFont") != null)
				{
					this.labelFont = CGViewConstants.extractFontFor("labelFont", elem);
				}

				// //titleFontColor
				// if (elem.attributes.getValue("titleFontColor") != null) {
				// // set title font color
				// }

				// globalLabelColor
				if (elem.attributes.getValue("globalLabelColor") != null)
				{
					this.globalLabelColor = CGViewConstants.extractColorFor("globalLabelColor", elem);
				}

				// rulerFontColor
				if (elem.attributes.getValue("rulerFontColor") != null)
				{
					this.rulerFontColor = CGViewConstants.extractColorFor("rulerFontColor", elem);
				}

				// backboneColor
				if (elem.attributes.getValue("backboneColor") != null)
				{
					this.backboneColor = CGViewConstants.extractColorFor("backboneColor", elem);
				}

				// backgroundColor
				if (elem.attributes.getValue("backgroundColor") != null)
				{
					this.backgroundColor = CGViewConstants.extractColorFor("backgroundColor", elem);
				}

				// longTickColor
				if (elem.attributes.getValue("longTickColor") != null)
				{
					this.longTickColor = CGViewConstants.extractColorFor("longTickColor", elem);
				}

				// shortTickColor
				if (elem.attributes.getValue("shortTickColor") != null)
				{
					this.shortTickColor = CGViewConstants.extractColorFor("shortTickColor", elem);
				}

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
						throw new SAXException(appendLocation("value for 'featureThickness' attribute in cgview element not understood"));
					}
				}
				
				// giveFeaturePositions
				if (elem.attributes.getValue("giveFeaturePositions") != null)
				{
					Boolean giveFeaturePositionsValue = 
						CGViewConstants.GIVE_FEATURE_POSITIONS.get(elem.attributes.getValue("giveFeaturePositions"));
					
					if (giveFeaturePositionsValue == null)
					{
						throw new SAXException(appendLocation("value for 'giveFeaturePositions' attribute in cgview element not understood"));
					}
					else
					{
						this.giveFeaturePositions = giveFeaturePositionsValue.booleanValue();
					}
				}

				if (elem.attributes.getValue("featureSlotSpacing") != null)
				{
					try
					{
						this.featureSlotSpacing = CGViewConstants.extractRealFor("featureSlotSpacing", elem, CGViewConstants.FEATURESLOT_SPACINGS);
					}
					catch (NumberFormatException nfe)
					{
						throw new SAXException(appendLocation("value for 'featureSlotSpacing' attribute in cgview element not understood"));
					}

					if (this.featureSlotSpacing < 0)
					{
						throw new SAXException(appendLocation("value for 'featureSlotSpacing' attribute in cgview element must be non-negative"));
					}
				}

				// show shading
				if (elem.attributes.getValue("showShading") != null)
				{
					Boolean showShadingValue = CGViewConstants.BOOLEANS.get(elem.attributes.getValue("showShading"));

					if (showShadingValue == null)
					{
						throw new SAXException(appendLocation("value for 'showShading' attribute in cgview element not understood"));
					}
					else
					{
						this.showShading = showShadingValue.booleanValue();
					}
				}

				// backboneThickness
				if (elem.attributes.getValue("backboneThickness") != null)
				{
					try
					{
						this.backboneThickness = CGViewConstants.extractRealFor("backboneThickness", elem, CGViewConstants.BACKBONE_THICKNESSES);
					}
					catch (NumberFormatException e)
					{
						// throw new SAXException (error);
						printError("[warning] value for 'backboneThickness' attribute in cgview element not understood");
					}
				}

				// tickDensity
				if (elem.attributes.getValue("tickDensity") != null)
				{
					try
					{
						tickDensity = (float) CGViewConstants.extractRealFor("tickDensity", elem, null);
					}
					catch (NumberFormatException nfe)
					{
						throw new SAXException(appendLocation("value for 'tickDensity' attribute in cgview element not understood"));
					}

					if (tickDensity < 0)
					{
						throw new SAXException(appendLocation("value for 'tickDensity' attribute in cgview element must be within [0,1]"));
					}
					else if (tickDensity > 1.0)
					{
						throw new SAXException(appendLocation("value for 'tickDensity' attribute in cgview element must be within [0,1]"));
					}
				}

				// tickLength
				if (elem.attributes.getValue("tickLength") != null)
				{
					try
					{
						this.tickLength = CGViewConstants.extractRealFor("tickLength", elem, CGViewConstants.TICK_LENGTHS);
					}
					catch (Exception e)
					{
						// throw new SAXException (error);
						printError("[warning] value for 'tickLength' attribute in cgview element not understood");
					}
				}

				// tickThickness
				if (elem.attributes.getValue("tickThickness") != null)
				{
					try
					{
						this.tickThickness = CGViewConstants.extractRealFor("tickThickness", elem, CGViewConstants.TICK_THICKNESSES);
					}
					catch (Exception e)
					{
						// throw new SAXException (error);
						printError("[warning] value for 'tickThickness' attribute in cgview element not understood");
					}
				}

				// labelLineThickness
				if (elem.attributes.getValue("labelLineThickness") != null)
				{
					try
					{
						this.labelLineThickness = CGViewConstants.extractRealFor("labelLineThickness", elem, CGViewConstants.LABEL_LINE_THICKNESSES);
					}
					catch (NumberFormatException nfe)
					{
						throw new SAXException(appendLocation("value for 'labelLineThickness' attribute in cgview element not understood"));
					}

					if (this.labelLineThickness < 0.0f)
					{
						throw new SAXException(
								appendLocation("value for 'labelLineThickness' attribute in cgview element must not be negative"));
					}
				}

				// width
				if (elem.attributes.getValue("width") != null)
				{
					try
					{
						width = Integer.parseInt(elem.attributes.getValue("width"));
					}
					catch (NumberFormatException nfe)
					{
						throw new SAXException(appendLocation("value for 'width' attribute in cgview element not understood"));
					}

					if (width < CGViewConstants.MIN_IMAGE_WIDTH)
					{
						throw new SAXException(appendLocation("value for 'width' attribute in cgview element must be greater than or equal to "
								+ CGViewConstants.MIN_IMAGE_WIDTH));
					}

					if (width > CGViewConstants.MAX_IMAGE_WIDTH)
					{
						throw new SAXException(appendLocation("value for 'width' attribute in cgview element must be less than or equal to "
								+ CGViewConstants.MAX_IMAGE_WIDTH));
					}
				}

				// height
				if (elem.attributes.getValue("height") != null)
				{
					try
					{
						height = Integer.parseInt(elem.attributes.getValue("height"));
					}
					catch (NumberFormatException nfe)
					{
						throw new SAXException(appendLocation("value for 'height' attribute in cgview element not understood"));
					}

					if (height < CGViewConstants.MIN_IMAGE_HEIGHT)
					{
						throw new SAXException(appendLocation("value for 'height' attribute in cgview element must be greater than or equal to "
								+ CGViewConstants.MIN_IMAGE_HEIGHT));
					}

					if (height > CGViewConstants.MAX_IMAGE_HEIGHT)
					{
						throw new SAXException(appendLocation("value for 'height' attribute in cgview element must be less than or equal to "
								+ CGViewConstants.MAX_IMAGE_HEIGHT));
					}
				}

				// backboneRadius
				if (elem.attributes.getValue("backboneRadius") != null)
				{
					try
					{
						this.backboneRadius = Double.parseDouble(elem.attributes.getValue("backboneRadius"));
					}
					catch (NumberFormatException nfe)
					{
						throw new SAXException(appendLocation("value for 'backboneRadius' attribute in cgview element not understood"));
					}

					if (backboneRadius < CGViewConstants.MIN_BACKBONE_RADIUS)
					{
						throw new SAXException(appendLocation("value for 'backboneRadius' attribute in cgview element must be greater than or equal to "
								+ CGViewConstants.MIN_BACKBONE_RADIUS));
					}
					else if (backboneRadius > CGViewConstants.MAX_BACKBONE_RADIUS)
					{
						throw new SAXException(appendLocation("value for 'backboneRadius' attribute in cgview element must be less than or equal to "
								+ CGViewConstants.MAX_BACKBONE_RADIUS));
					}
				}

				// globalLabel
				if (elem.attributes.getValue("globalLabel") != null)
				{
					Boolean globalLabelValue = CGViewConstants.GLOBAL_LABEL_TYPES.get(elem.attributes.getValue("globalLabel"));

					if (globalLabelValue == null)
					{
						throw new SAXException(appendLocation("value for 'globalLabel' attribute in cgview element not understood"));
					}
					else
					{
						this.globalLabel = globalLabelValue;
					}
				}

				buildSequenceStyle();
			}
		}
	}

	public Sequence getSequence()
	{
		return currentSequence;
	}

	public MapStyle getMapStyle()
	{
		return currentMapStyle;
	}

	public LegendElementHandler getLegendElementHandler()
	{
		return currentLegendElementHandler;
	}

	public LegendElementHandler createLegendElementHandler()
	{
		this.currentLegendElementHandler = new LegendElementHandler(this.backgroundColor);

		return this.currentLegendElementHandler;
	}

	public void endLegend()
	{
		if (currentLegendElementHandler != null)
		{
			LegendStyle legendStyle = currentLegendElementHandler.getLegendStyle();

			currentMapStyle.getGlobalStyle().addLegendStyle(legendStyle);
		}

		this.currentLegendElementHandler = null;
	}

	public void handleFeatureSlot(Stack<ElementDetails> context,
			Locator locator) throws SAXException
	{
		this.currentFeatureSlot = createFeatureSlot();
		this.currentFeatureSlot.handleFeatureSlot(context, locator);
	}
	
	public void handleFeature(Stack<ElementDetails> context,
			Locator locator) throws SAXException
	{
		if (currentFeatureSlot != null)
		{
			currentFeatureSlot.handleFeature(context, locator);
		}
		else
		{
			throw new SAXException(appendLocation("\'feature\' element encountered outside of a \'featureSlot\' element"));
		}
	}

	public void handleFeatureRange(Stack<ElementDetails> context,
			Locator locator2) throws SAXException
	{
		if (this.currentFeatureSlot != null)
		{
			this.currentFeatureSlot.handleFeatureRange(context, locator2);
		}
		else
		{
			throw new SAXException(appendLocation("\'featureRange\' element encountered outside of a \'featureSlot\' element"));
		}
	}

	public void endFeatureElement() throws SAXException
	{
		if (this.currentFeatureSlot != null)
		{
			this.currentFeatureSlot.endFeatureElement();
		}
		else
		{
			throw new SAXException(appendLocation("\'feature\' element encountered outside of a \'featureSlot\' element"));
		}
	}

	public void endFeatureRange() throws SAXException
	{
		if (this.currentFeatureSlot != null)
		{
			this.currentFeatureSlot.endFeatureRange();
		}
		else
		{
			throw new SAXException(appendLocation("\'featureRange\' element encountered outside of a \'featureSlot\' element"));
		}
	}
}
