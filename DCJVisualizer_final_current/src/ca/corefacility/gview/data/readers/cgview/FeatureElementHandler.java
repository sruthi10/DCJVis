package ca.corefacility.gview.data.readers.cgview;

import java.awt.Color;
import java.util.Stack;

import org.biojava.bio.seq.StrandedFeature;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Used to handle Feature tags in cgview format.
 *
 * @author Aaron Petkau
 *
 */
class FeatureElementHandler
{
	private FeatureStyleTemplate styleTemplate;
	private AnnotationTemplate annotationTemplate;

	private FeatureStyleManager featureBuilder;

	private StrandedFeature.Template featureTemplate; // the biojava feature this Feature class defines

	private Boolean globalShowLabels;

	private Locator locator;

	private FeatureRangeHandler currentFeatureRangeHandler = null;
	
	private int sequenceLength;

	// showlabel
	// showshading

	/**
	 * Creates a new Feature from a featureTemplate within the passed slot.
	 * @param globalShowLabels
	 * @param labelLineThickness
	 */
	public FeatureElementHandler(FeatureStyleManager featureBuilder, StrandedFeature.Template featureTemplate,
			FeatureStyleTemplate styleTemplate, Boolean globalShowLabels, int sequenceLength)
	{
		if (featureTemplate == null)
		{
			throw new NullPointerException("featureTemplate is null");
		} else
		{
			this.styleTemplate = styleTemplate;

			this.featureTemplate = featureTemplate;

			this.featureBuilder = featureBuilder;

			this.annotationTemplate = new AnnotationTemplate();

			this.globalShowLabels = globalShowLabels;
			
			this.sequenceLength = sequenceLength;
		}
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
	 * Handles the feature element and its attributes.
	 * @param locator
	 * @param context
	 *
	 * @throws SAXException
	 */
	// required attributes:
	// optional attributes: color, opacity, proportionOfThickness, radiusAdjustment, decoration,
	// showLabel, font, label, showShading, hyperlink, mouseover
	public void handleFeature(Stack<ElementDetails> context, Locator locator) throws SAXException
	{
		this.locator = locator;
		for (int p = context.size() - 1; p >= 0; p--)
		{
			ElementDetails elem = context.elementAt(p);
			if (elem.name.equalsIgnoreCase("feature"))
			{
				{
					// optional tags
					// color
					if (elem.attributes.getValue("color") != null)
					{
						Color color = CGViewConstants.extractColorFor("color", elem);
						if (color != null)
						{
							styleTemplate.color = color;
						}
					}

					// decoration
					if (elem.attributes.getValue("decoration") != null)
					{
						styleTemplate.decoration = CGViewConstants.DECORATIONS.get(elem.attributes.getValue("decoration").toLowerCase());

						if (styleTemplate.decoration == null)
						{
							throw new SAXException(appendLocation("value for 'decoration' attribute in feature element not understood"));
						}
					}
					
					// font for label
					if (elem.attributes.getValue("font") != null)
					{
						styleTemplate.labelFont = CGViewConstants.extractFontFor("font", elem);
					}
					
					// hyperlink
					if (elem.attributes.getValue("hyperlink") != null)
					{
						annotationTemplate.hyperlink = elem.attributes.getValue("hyperlink");
					}
					
					// label
					if (elem.attributes.getValue("label") != null)
					{
						annotationTemplate.label = elem.attributes.getValue("label");
					}
					
					// mouseover
					if (elem.attributes.getValue("mouseover") != null)
					{
						annotationTemplate.mouseover = elem.attributes.getValue("mouseover");
					}

					// opacity
					if (elem.attributes.getValue("opacity") != null)
					{
						try
						{
							styleTemplate.opacity = Float.parseFloat(elem.attributes.getValue("opacity"));
						}
						catch (NumberFormatException nfe)
						{
							throw new SAXException(appendLocation("value for 'opacity' attribute in feature element not understood"));
						}

						if (styleTemplate.opacity > 1.0)
						{
							throw new SAXException(appendLocation("value for 'opacity' attribute in feature element must be between 0 and 1"));
						}

						if (styleTemplate.opacity < 0.0)
						{
							throw new SAXException(appendLocation("value for 'opacity' attribute in feature element must be between 0 and 1"));
						}
					}

					// proportionOfThickness
					if (elem.attributes.getValue("proportionOfThickness") != null)
					{
						try
						{
							styleTemplate.proportionOfThickness = Float.parseFloat(elem.attributes.getValue("proportionOfThickness"));
						}
						catch (NumberFormatException nfe)
						{
							throw new SAXException(appendLocation("value for 'proportionOfThickness' attribute in feature element not understood"));
						}

						if (styleTemplate.proportionOfThickness > 1.0)
						{
							throw new SAXException(
									appendLocation("value for 'proportionOfThickness' attribute in feature element must be between 0 and 1"));
						}

						if (styleTemplate.proportionOfThickness < 0.0f)
						{
							throw new SAXException(
									appendLocation("value for 'proportionOfThickness' attribute in feature element must be between 0 and 1"));
						}
					}

					// radiusAdjustment
					if (elem.attributes.getValue("radiusAdjustment") != null)
					{
						try
						{
							styleTemplate.radiusAdjustment = Float.parseFloat(elem.attributes.getValue("radiusAdjustment"));
						}
						catch (NumberFormatException nfe)
						{
							throw new SAXException(appendLocation("value for 'radiusAdjustment' attribute in feature element not understood"));
						}

						if (styleTemplate.radiusAdjustment > 1.0)
						{
							throw new SAXException(appendLocation("value for 'radiusAdjustment' attribute in feature element must be between 0 and 1"));
						}

						if (styleTemplate.radiusAdjustment < 0.0f)
						{
							throw new SAXException(appendLocation("value for 'radiusAdjustment' attribute in feature element must be between 0 and 1"));
						}
					}

					// showlabel
					if (elem.attributes.getValue("showLabel") != null)
					{
						Boolean showLabel = CGViewConstants.BOOLEANS.get(elem.attributes.getValue("showLabel"));

						if (showLabel == null)
						{
							throw new SAXException(appendLocation("value for 'showLabel' attribute in feature element not understood"));
						} else
						{
							styleTemplate.showLabel = showLabel;
						}
					}

					// show shading
					if (elem.attributes.getValue("showShading") != null)
					{
						Boolean showShading = CGViewConstants.BOOLEANS.get(elem.attributes.getValue("showShading"));

						if (showShading == null)
						{
							throw new SAXException(appendLocation("value for 'showShading' attribute in feature element not understood"));
						} else
						{
							styleTemplate.showShading = showShading;
						}
					}
				}
			}
		}
	}

	public FeatureRangeHandler createFeatureRangeHandler()
	{
		FeatureRangeHandler handler = new FeatureRangeHandler(featureTemplate, new FeatureStyleTemplate(styleTemplate),
				new AnnotationTemplate(annotationTemplate), featureBuilder,
				globalShowLabels, sequenceLength);
		return handler;
	}

	public void endFeatureRange()
	{
		currentFeatureRangeHandler = null;
	}

	public void handleFeatureRange(Stack<ElementDetails> context,
			Locator locator2) throws SAXException
	{
		this.currentFeatureRangeHandler = createFeatureRangeHandler();
		this.currentFeatureRangeHandler.handleFeatureRange(context, locator2);
	}
}
