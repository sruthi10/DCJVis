package ca.corefacility.gview.data.readers.cgview;

import java.awt.Color;
import java.util.Stack;

import org.biojava.bio.seq.Feature;
import org.biojava.bio.symbol.LocationTools;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * A class used to handle FeatureRange tags in cgview format.
 *
 * @author Aaron Petkau
 *
 */
class FeatureRangeHandler
{
	private Locator locator;

	private FeatureStyleManager featureBuilder;
	private Feature.Template featureTemplate;

	private FeatureStyleTemplate styleTemplate;
	private AnnotationTemplate annotationTemplate;

	private Boolean globalShowLabels;
	
	private int sequenceLength;

	public FeatureRangeHandler(Feature.Template featureTemplate, FeatureStyleTemplate styleTemplate,
			AnnotationTemplate annotationTemplate ,FeatureStyleManager featureBuilder,
			Boolean globalShowLabels, int sequenceLength)
	{
		this.styleTemplate = styleTemplate;
		this.featureBuilder = featureBuilder;
		this.featureTemplate = featureTemplate;
		this.annotationTemplate = annotationTemplate;

		this.globalShowLabels = globalShowLabels;
		this.sequenceLength = sequenceLength;
	}

	/**
	 * Extracts a base number on the sequence, performs error checking on value.
	 *
	 * @param propertyType
	 *            The property type to extract.
	 * @param elem
	 *            The element details.
	 * @return The extracted base.
	 * @throws SAXException
	 *             if there was any problem with extracted value.
	 */
	private int extractSequenceValue(String propertyType, ElementDetails elem) throws SAXException
	{
		int base = -1;

		try
		{
			base = Integer.parseInt(elem.attributes.getValue(propertyType));
		}
		catch (NumberFormatException nfe)
		{
			throw new SAXException(appendLocation("value for '" + propertyType + "' attribute in " + elem.name + " element not understood"));
		}

		if (base > featureBuilder.getCgviewLength())
		{
			throw new SAXException(appendLocation("value for '" + propertyType + "' attribute in " + elem.name
					+ " element must be less than or equal to the length of the plasmid"));
		}

		if (base < 1)
		{
			throw new SAXException(appendLocation("value for '" + propertyType + "' attribute in " + elem.name
					+ " element must be greater than or equal to 1"));
		}

		return base;
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
	 * Handles the featureRange element and its attributes.
	 * @param locator
	 * @param context
	 *
	 * @throws SAXException
	 */
	// required attributes: start, stop
	// optional attributes: color, opacity, proportionOfThickness, radiusAdjustment, decoration,
	// showLabel, font, label, showShading, hyperlink, mouseover
	public void handleFeatureRange(Stack<ElementDetails> context, Locator locator) throws SAXException
	{
		int start, stop;

		this.locator = locator;
		for (int p = context.size() - 1; p >= 0; p--)
		{
			ElementDetails elem = context.elementAt(p);
			if (elem.name.equalsIgnoreCase("featureRange"))
			{
				if (elem.attributes.getValue("start") == null)
				{
					// an error because no length
					throw new SAXException(appendLocation("featureRange element is missing 'start' attribute"));
				}
				else if (elem.attributes.getValue("stop") == null)
				{
					// an error because no length
					throw new SAXException(appendLocation("featureRange element is missing 'stop' attribute"));
				}
				else
				{
					start = extractSequenceValue("start", elem);
					stop = extractSequenceValue("stop", elem);

					if (stop >= start)
					{
					    featureTemplate.location = LocationTools.makeLocation(start, stop);
					}
					else if (start > stop)
					{
					    featureTemplate.location = LocationTools.makeCircularLocation(start, stop, sequenceLength);
					}
					
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
							throw new SAXException(appendLocation("value for 'decoration' attribute in featureRange element not understood"));
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
							throw new SAXException(appendLocation("value for 'opacity' attribute in featureRange element not understood"));
						}

						if (styleTemplate.opacity > 1.0)
						{
							throw new SAXException(appendLocation("value for 'opacity' attribute in featureRange element must be between 0 and 1"));
						}

						if (styleTemplate.opacity < 0.0)
						{
							throw new SAXException(appendLocation("value for 'opacity' attribute in featureRange element must be between 0 and 1"));
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
							throw new SAXException(appendLocation("value for 'proportionOfThickness' attribute in featureRange element not understood"));
						}

						if (styleTemplate.proportionOfThickness > 1.0)
						{
							throw new SAXException(
									appendLocation("value for 'proportionOfThickness' attribute in featureRange element must be between 0 and 1"));
						}

						if (styleTemplate.proportionOfThickness < 0.0f)
						{
							throw new SAXException(
									appendLocation("value for 'proportionOfThickness' attribute in featureRange element must be between 0 and 1"));
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
							throw new SAXException(appendLocation("value for 'radiusAdjustment' attribute in featureRange element not understood"));
						}

						if (styleTemplate.radiusAdjustment > 1.0)
						{
							throw new SAXException(appendLocation("value for 'radiusAdjustment' attribute in featureRange element must be between 0 and 1"));
						}

						if (styleTemplate.radiusAdjustment < 0.0f)
						{
							throw new SAXException(appendLocation("value for 'radiusAdjustment' attribute in featureRange element must be between 0 and 1"));
						}
					}

					// showlabel
					if (elem.attributes.getValue("showLabel") != null)
					{
						Boolean showLabel = CGViewConstants.BOOLEANS.get(elem.attributes.getValue("showLabel"));

						if (showLabel == null)
						{
							throw new SAXException(appendLocation("value for 'showLabel' attribute in featureRange element not understood"));
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
							throw new SAXException(appendLocation("value for 'showShading' attribute in featureRange element not understood"));
						} else
						{
							styleTemplate.showShading = showShading;
						}
					}

					featureBuilder.buildFeature(featureTemplate, styleTemplate, annotationTemplate, globalShowLabels);
				}
			}
		}
	}
}