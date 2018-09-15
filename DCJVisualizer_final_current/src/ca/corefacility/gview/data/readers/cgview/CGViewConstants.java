package ca.corefacility.gview.data.readers.cgview;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.SAXException;

import ca.corefacility.gview.layout.feature.FeatureShapeRealizer;
import ca.corefacility.gview.map.effects.ShapeEffectRenderer;
import ca.corefacility.gview.style.items.LegendAlignment;
import ca.corefacility.gview.style.items.LegendTextAlignment;

/**
 * A class defining constants and utility methods for CGView xml parsing.
 * @author aaron
 *
 */
class CGViewConstants
{
	// whole bunch of tables matching strings to values for the style info
	public final static Hashtable<String, Color> COLORS = new Hashtable<String, Color>();
	// private final static Hashtable LABEL_TYPES = new Hashtable();
	public final static Hashtable<String, Boolean> GLOBAL_LABEL_TYPES = new Hashtable<String, Boolean>();
	public final static Hashtable<String, FeatureShapeRealizer> DECORATIONS =
		new Hashtable<String, FeatureShapeRealizer>();
	// private final static Hashtable RULER_UNITS = new Hashtable();
	// private final static Hashtable USE_INNER_LABELS = new Hashtable();
	public final static Hashtable<String, Boolean> GIVE_FEATURE_POSITIONS = new Hashtable<String, Boolean>();
	public final static Hashtable<String, Double> FEATURE_THICKNESSES = new Hashtable<String, Double>();
	public final static Hashtable<String, Double> FEATURESLOT_SPACINGS = new Hashtable<String, Double>();
	public final static Hashtable<String, Double> BACKBONE_THICKNESSES = new Hashtable<String, Double>();
	public final static Hashtable<String, Double> ARROWHEAD_LENGTHS = new Hashtable<String, Double>();
	public final static Hashtable<String, Double> MINIMUM_FEATURE_LENGTHS = new Hashtable<String, Double>();
	public final static Hashtable<String, Double> ORIGINS = new Hashtable<String, Double>();
	public final static Hashtable<String, Double> TICK_THICKNESSES = new Hashtable<String, Double>();
	public final static Hashtable<String, Double> TICK_LENGTHS = new Hashtable<String, Double>();
	public final static Hashtable<String, Double> LABEL_LINE_THICKNESSES = new Hashtable<String, Double>();
	public final static Hashtable<String, Double> LABEL_LINE_LENGTHS = new Hashtable<String, Double>();
	public final static Hashtable<String, Integer> LABEL_PLACEMENT_QUALITIES = new Hashtable<String, Integer>();
	public final static Hashtable<String, Boolean> BOOLEANS = new Hashtable<String, Boolean>();
	// private final static Hashtable SWATCH_TYPES = new Hashtable();
	public final static Hashtable<String, LegendAlignment> LEGEND_POSITIONS = new Hashtable<String, LegendAlignment>();
	public final static Hashtable<String, LegendTextAlignment> LEGEND_ALIGNMENTS = new Hashtable<String, LegendTextAlignment>();
	// private final static Hashtable LEGEND_SHOW_ZOOM = new Hashtable();

	public final static int MAX_BASES = Integer.MAX_VALUE;
	public final static int MIN_BASES = 1;
	public final static double MIN_BACKBONE_RADIUS = 10.0d;
	public final static double MAX_BACKBONE_RADIUS = 12000.0d; //default is 190.0d
	public final static int MAX_IMAGE_WIDTH = 30000; // default is 700.0d
	public final static int MIN_IMAGE_WIDTH = 100;

	public final static int MAX_IMAGE_HEIGHT = 30000; // default is 700.0d
	public final static int MIN_IMAGE_HEIGHT = 100;

	public static final Font defaultLabelFont = new Font("SansSerif", Font.PLAIN, 10);

	private static final ShapeEffectRenderer shadedEffect = ShapeEffectRenderer.SHADING_RENDERER;
	private static final ShapeEffectRenderer normalEffect = ShapeEffectRenderer.BASIC_RENDERER;

	// initialize hash tables
	static
	{
		COLORS.put("black", new Color(0, 0, 0));
		COLORS.put("silver", new Color(192, 192, 192));
		COLORS.put("gray", new Color(128, 128, 128)); // backbone default
		COLORS.put("grey", new Color(128, 128, 128));
		COLORS.put("white", new Color(255, 255, 255)); // background default
		COLORS.put("maroon", new Color(128, 0, 0));
		COLORS.put("red", new Color(255, 0, 0));
		COLORS.put("purple", new Color(128, 0, 128));
		COLORS.put("fuchsia", new Color(255, 0, 255));
		COLORS.put("green", new Color(0, 128, 0));
		COLORS.put("lime", new Color(0, 255, 0));
		COLORS.put("olive", new Color(128, 128, 0));
		COLORS.put("yellow", new Color(255, 255, 0));
		COLORS.put("orange", new Color(255, 153, 0));
		COLORS.put("navy", new Color(0, 0, 128));
		COLORS.put("blue", new Color(0, 0, 255)); // feature and featureRange default
		COLORS.put("teal", new Color(0, 128, 128));
		COLORS.put("aqua", new Color(0, 255, 255));

		// LABEL_TYPES.put("true", new Integer(LABEL));
		// LABEL_TYPES.put("false", new Integer(LABEL_NONE)); //default for feature and featureRange
		// LABEL_TYPES.put("force", new Integer(LABEL_FORCE));
		//
		GLOBAL_LABEL_TYPES.put("true", true); //default for Cgview
		GLOBAL_LABEL_TYPES.put("false", false);
		GLOBAL_LABEL_TYPES.put("auto", true); // auto stops labels on full resolution, which doesn't apply for gview
		//
		DECORATIONS.put("arc", FeatureShapeRealizer.NO_ARROW); //default for feature and featureRange
		DECORATIONS.put("hidden", FeatureShapeRealizer.HIDDEN);
		DECORATIONS.put("counterclockwise-arrow", FeatureShapeRealizer.COUNTERCLOCKWISE_ARROW);
		DECORATIONS.put("clockwise-arrow", FeatureShapeRealizer.CLOCKWISE_ARROW);
		//
		// RULER_UNITS.put("bases", new Integer(BASES)); //default for rulerUnits
		// RULER_UNITS.put("centisomes", new Integer(CENTISOMES));
		//
		// USE_INNER_LABELS.put("true", new Integer(INNER_LABELS_SHOW)); //should have true, false,
		// auto
		// USE_INNER_LABELS.put("false", new Integer(INNER_LABELS_NO_SHOW));
		// USE_INNER_LABELS.put("auto", new Integer(INNER_LABELS_AUTO));
		//
		 GIVE_FEATURE_POSITIONS.put("true", true);
		 GIVE_FEATURE_POSITIONS.put("false", false); //default
		 GIVE_FEATURE_POSITIONS.put("auto", true);

		FEATURE_THICKNESSES.put("xxx-small", Double.valueOf(1.0));
		FEATURE_THICKNESSES.put("xx-small", Double.valueOf(2.0));
		FEATURE_THICKNESSES.put("x-small", Double.valueOf(4.0));
		FEATURE_THICKNESSES.put("small", Double.valueOf(6.0));
		FEATURE_THICKNESSES.put("medium", Double.valueOf(8.0)); // default for featureThickness
		FEATURE_THICKNESSES.put("large", Double.valueOf(10.0));
		FEATURE_THICKNESSES.put("x-large", Double.valueOf(12.0));
		FEATURE_THICKNESSES.put("xx-large", Double.valueOf(14.0));
		FEATURE_THICKNESSES.put("xxx-large", Double.valueOf(16.0));

		FEATURESLOT_SPACINGS.put("xxx-small", Double.valueOf(0.0));
		FEATURESLOT_SPACINGS.put("xx-small", Double.valueOf(1.0));
		FEATURESLOT_SPACINGS.put("x-small", Double.valueOf(2.0));
		FEATURESLOT_SPACINGS.put("small", Double.valueOf(3.0));
		FEATURESLOT_SPACINGS.put("medium", Double.valueOf(4.0)); // default for featureSlotSpacing
		FEATURESLOT_SPACINGS.put("large", Double.valueOf(5.0));
		FEATURESLOT_SPACINGS.put("x-large", Double.valueOf(6.0));
		FEATURESLOT_SPACINGS.put("xx-large", Double.valueOf(7.0));
		FEATURESLOT_SPACINGS.put("xxx-large", Double.valueOf(8.0));

		BACKBONE_THICKNESSES.put("xxx-small", Double.valueOf(1.0));
		BACKBONE_THICKNESSES.put("xx-small", Double.valueOf(2.0));
		BACKBONE_THICKNESSES.put("x-small", Double.valueOf(3.0));
		BACKBONE_THICKNESSES.put("small", Double.valueOf(4.0));
		BACKBONE_THICKNESSES.put("medium", Double.valueOf(5.0)); // default for backboneThickness
		BACKBONE_THICKNESSES.put("large", Double.valueOf(6.0));
		BACKBONE_THICKNESSES.put("x-large", Double.valueOf(7.0));
		BACKBONE_THICKNESSES.put("xx-large", Double.valueOf(8.0));
		BACKBONE_THICKNESSES.put("xxx-large", Double.valueOf(9.0));

		ARROWHEAD_LENGTHS.put("xxx-small", Double.valueOf(1.0d));
		ARROWHEAD_LENGTHS.put("xx-small", Double.valueOf(2.0d));
		ARROWHEAD_LENGTHS.put("x-small", Double.valueOf(3.0d));
		ARROWHEAD_LENGTHS.put("small", Double.valueOf(4.0d));
		ARROWHEAD_LENGTHS.put("medium", Double.valueOf(5.0d)); // default for arrowheadLength
		ARROWHEAD_LENGTHS.put("large", Double.valueOf(6.0d));
		ARROWHEAD_LENGTHS.put("x-large", Double.valueOf(7.0d));
		ARROWHEAD_LENGTHS.put("xx-large", Double.valueOf(8.0d));
		ARROWHEAD_LENGTHS.put("xxx-large", Double.valueOf(9.0d));

		TICK_LENGTHS.put("xxx-small", Double.valueOf(3.0));
		TICK_LENGTHS.put("xx-small", Double.valueOf(4.0));
		TICK_LENGTHS.put("x-small", Double.valueOf(5.0));
		TICK_LENGTHS.put("small", Double.valueOf(6.0));
		TICK_LENGTHS.put("medium", Double.valueOf(7.0)); // default for ticks
		TICK_LENGTHS.put("large", Double.valueOf(8.0));
		TICK_LENGTHS.put("x-large", Double.valueOf(9.0));
		TICK_LENGTHS.put("xx-large", Double.valueOf(10.0));
		TICK_LENGTHS.put("xxx-large", Double.valueOf(11.0));

		MINIMUM_FEATURE_LENGTHS.put("xxx-small", Double.valueOf(0.02d)); // default for
		// minimumFeatureLength
		MINIMUM_FEATURE_LENGTHS.put("xx-small", Double.valueOf(0.05d));
		MINIMUM_FEATURE_LENGTHS.put("x-small", Double.valueOf(0.1d));
		MINIMUM_FEATURE_LENGTHS.put("small", Double.valueOf(0.5d));
		MINIMUM_FEATURE_LENGTHS.put("medium", Double.valueOf(1.0d));
		MINIMUM_FEATURE_LENGTHS.put("large", Double.valueOf(1.5d));
		MINIMUM_FEATURE_LENGTHS.put("x-large", Double.valueOf(2.0d));
		MINIMUM_FEATURE_LENGTHS.put("xx-large", Double.valueOf(2.5d));
		MINIMUM_FEATURE_LENGTHS.put("xxx-large", Double.valueOf(3.0d));

		ORIGINS.put("1", Double.valueOf(60.0d));
		ORIGINS.put("2", Double.valueOf(30.0d));
		ORIGINS.put("3", Double.valueOf(0.0d));
		ORIGINS.put("4", Double.valueOf(-30.0d));
		ORIGINS.put("5", Double.valueOf(-60.0d));
		ORIGINS.put("6", Double.valueOf(-90.0d));
		ORIGINS.put("7", Double.valueOf(-120.0d));
		ORIGINS.put("8", Double.valueOf(-150.0d));
		ORIGINS.put("9", Double.valueOf(-180.0d));
		ORIGINS.put("10", Double.valueOf(-210.0d));
		ORIGINS.put("11", Double.valueOf(-240.0d));
		ORIGINS.put("12", Double.valueOf(90.0d)); // default for origin

		TICK_THICKNESSES.put("xxx-small", Double.valueOf(0.02));
		TICK_THICKNESSES.put("xx-small", Double.valueOf(0.5));
		TICK_THICKNESSES.put("x-small", Double.valueOf(1.0));
		TICK_THICKNESSES.put("small", Double.valueOf(1.5));
		TICK_THICKNESSES.put("medium", Double.valueOf(2.0)); // default for tickThickness
		TICK_THICKNESSES.put("large", Double.valueOf(2.5));
		TICK_THICKNESSES.put("x-large", Double.valueOf(3.0));
		TICK_THICKNESSES.put("xx-large", Double.valueOf(3.5));
		TICK_THICKNESSES.put("xxx-large", Double.valueOf(4.0));

		LABEL_LINE_THICKNESSES.put("xxx-small", Double.valueOf(0.02));
		LABEL_LINE_THICKNESSES.put("xx-small", Double.valueOf(0.25));
		LABEL_LINE_THICKNESSES.put("x-small", Double.valueOf(0.50));
		LABEL_LINE_THICKNESSES.put("small", Double.valueOf(0.75));
		LABEL_LINE_THICKNESSES.put("medium", Double.valueOf(1.0)); // default for labelLineThickness
		LABEL_LINE_THICKNESSES.put("large", Double.valueOf(1.25));
		LABEL_LINE_THICKNESSES.put("x-large", Double.valueOf(1.5));
		LABEL_LINE_THICKNESSES.put("xx-large", Double.valueOf(1.75));
		LABEL_LINE_THICKNESSES.put("xxx-large", Double.valueOf(2.0));

		LABEL_LINE_LENGTHS.put("xxx-small", Double.valueOf(10.0d));
		LABEL_LINE_LENGTHS.put("xx-small", Double.valueOf(20.0d));
		LABEL_LINE_LENGTHS.put("x-small", Double.valueOf(30.0d));
		LABEL_LINE_LENGTHS.put("small", Double.valueOf(40.0d));
		LABEL_LINE_LENGTHS.put("medium", Double.valueOf(50.0d)); // default for labelLineLength
		LABEL_LINE_LENGTHS.put("large", Double.valueOf(60.0d));
		LABEL_LINE_LENGTHS.put("x-large", Double.valueOf(70.0d));
		LABEL_LINE_LENGTHS.put("xx-large", Double.valueOf(80.0d));
		LABEL_LINE_LENGTHS.put("xxx-large", Double.valueOf(90.0d));

		LABEL_PLACEMENT_QUALITIES.put("good", Integer.valueOf(5));
		LABEL_PLACEMENT_QUALITIES.put("better", Integer.valueOf(8)); // default for
		// labelPlacementQuality
		LABEL_PLACEMENT_QUALITIES.put("best", Integer.valueOf(10));

		BOOLEANS.put("true", true); // default for showShading //default for
		// moveInnerLabelsToOuter
		BOOLEANS.put("false", false); // default for allowLabelClash //default for
		// showWarning

		 LEGEND_POSITIONS.put("upper-left", LegendAlignment.UPPER_LEFT);
		 LEGEND_POSITIONS.put("upper-center", LegendAlignment.UPPER_CENTER);
		 LEGEND_POSITIONS.put("upper-right", LegendAlignment.UPPER_RIGHT);
		 LEGEND_POSITIONS.put("middle-left", LegendAlignment.MIDDLE_LEFT);
		 //LEGEND_POSITIONS.put("middle-left-of-center", LegendAlignment.);
		 LEGEND_POSITIONS.put("middle-center", LegendAlignment.MIDDLE_CENTER);
		 //LEGEND_POSITIONS.put("middle-right-of-center", new
		 LEGEND_POSITIONS.put("middle-right", LegendAlignment.MIDDLE_RIGHT); //default for
		 LEGEND_POSITIONS.put("lower-left", LegendAlignment.LOWER_LEFT);
		 LEGEND_POSITIONS.put("lower-center", LegendAlignment.LOWER_CENTER);
		 LEGEND_POSITIONS.put("lower-right", LegendAlignment.LOWER_RIGHT);
		//
		// LEGEND_SHOW_ZOOM.put("true", new Integer(LEGEND_DRAW_ZOOMED)); //default for legend
		// LEGEND_SHOW_ZOOM.put("false", new Integer(LEGEND_NO_DRAW_ZOOMED));
		//
		 LEGEND_ALIGNMENTS.put("left", LegendTextAlignment.LEFT); //default for legend
		 LEGEND_ALIGNMENTS.put("center", LegendTextAlignment.CENTER);
		 LEGEND_ALIGNMENTS.put("right", LegendTextAlignment.RIGHT);
		//
		// SWATCH_TYPES.put("true", new Integer(SWATCH_SHOW));
		// SWATCH_TYPES.put("false", new Integer(SWATCH_NO_SHOW)); //default for legendItem
	}

	// used for extracting text
	private static final Pattern fontDescriptionPattern = Pattern.compile("\\s*(\\S+)\\s*,\\s*(\\S+)\\s*,\\s*(\\d+)\\s*,*\\s*");
	private static final Pattern colorDescriptionPattern = Pattern.compile("\\s*(\\d+)\\s*,\\s*(\\d+)\\s*,\\s*(\\d+)\\s*,*\\s*");

	/**
	 * Extracts a font for the given property name, from the passed element details.
	 *
	 * @param propertyName
	 *            The name of the property to extract the font for.
	 * @param elem
	 *            The element details to extract the font for.
	 * @return The extracted font, or null if no font.
	 */
	public static Font extractFontFor(String propertyName, ElementDetails elem) throws SAXException
	{
		Font font = null;

		if (elem.attributes.getValue(propertyName) != null)
		{
			Matcher m = fontDescriptionPattern.matcher(elem.attributes.getValue(propertyName));
			if (m.find())
			{
				try
				{

					String name = m.group(1);
					String style = m.group(2);
					int size = Integer.parseInt(m.group(3));
					int intStyle = Font.PLAIN;

					if (style.equalsIgnoreCase("bold"))
					{
						intStyle = Font.BOLD;
					}
					else if (style.equalsIgnoreCase("italic") || style.equalsIgnoreCase("italics"))
					{
						intStyle = Font.ITALIC;
					}
					else if (style.equalsIgnoreCase("bold-italic") || style.equalsIgnoreCase("italic-bold"))
					{
						intStyle = Font.ITALIC + Font.BOLD;
					}
					else if (style.equalsIgnoreCase("plain"))
					{
						intStyle = Font.PLAIN;
					}
					else
					{
						throw new SAXException("[warning] value for style in '" + propertyName + "' attribute in " + elem.name + " element not understood");
					}

					font = new Font(name, intStyle, size);

				}
				catch (NumberFormatException e)
				{
					throw new SAXException("[warning] value for '" + propertyName + "' attribute in " + elem.name + " element not understood");
				}
			}
			else
			{
				throw new SAXException("[warning] value for '" + propertyName + "' attribute in " + elem.name + " element not understood");
			}
		}

		return font;
	}

	/**
	 * Extracts a color for the given property name, from the passed element details.
	 *
	 * @param propertyName
	 *            The name of the property to extract the color for.
	 * @param elem
	 *            The element details to extract the color for.
	 * @return The extracted color, or null if no color.
	 */
	public static Color extractColorFor(String propertyName, ElementDetails elem) throws SAXException
	{
		Color color = null;

		if (elem.attributes.getValue(propertyName) != null)
		{
			if (CGViewConstants.COLORS.get(elem.attributes.getValue(propertyName).toLowerCase()) != null)
			{
				color = CGViewConstants.COLORS.get(elem.attributes.getValue(propertyName).toLowerCase());
			}
			else
			{
				Matcher m = colorDescriptionPattern.matcher(elem.attributes.getValue(propertyName));
				if (m.find())
				{
					try
					{
						int r = Integer.parseInt(m.group(1));
						int g = Integer.parseInt(m.group(2));
						int b = Integer.parseInt(m.group(3));

						color = new Color(r, g, b);

					}
					catch (NumberFormatException e)
					{
						// throw new SAXException (error);
						throw new SAXException("[warning] value for '" + propertyName + "' attribute in " + elem.name + " element not understood");
					}
				}
				else
				{
					throw new SAXException("[warning] value for '" + propertyName + "' attribute in " + elem.name + " element not understood");
				}
			}
		}

		return color;
	}

	/**
	 * Extracts a real value for the given property name, from the passed element details.
	 *
	 * @param propertyName
	 *            The name of the property to extract the real value for.
	 * @param elem
	 *            The element details to extract the real value for.
	 * @param table
	 *            A Hashtable mapping strings to double values.
	 * @return The extracted real value.
	 *
	 * @throws NumberFormatException
	 *             if the corresponding value was not formatted correctly
	 */
	public static double extractRealFor(String propertyName, ElementDetails elem, Hashtable<String, Double> table) throws NumberFormatException
	{
		double realValue = 0.0;

		if (elem == null || propertyName == null || elem.attributes.getValue(propertyName) == null)
		{
			throw new IllegalArgumentException("null parameters");
		}

		if (elem.attributes.getValue(propertyName) != null)
		{
			if (table != null && table.get(elem.attributes.getValue(propertyName).toLowerCase()) != null)
			{
				realValue = table.get(elem.attributes.getValue(propertyName).toLowerCase()).doubleValue();
			}
			else
			{
				realValue = Double.parseDouble(elem.attributes.getValue(propertyName));
			}
		}

		return realValue;
	}

	public static ShapeEffectRenderer getDefaultEffect()
	{
		return normalEffect;
	}

	public static ShapeEffectRenderer getDefaultShadedEffect()
	{
		return shadedEffect;
	}

	public static Color colorToOpacity(Color color, float opacity)
	{
		if (opacity < 0.0f || opacity > 1.0f)
		{
			throw new IllegalArgumentException("opacity must be in [0,1]");
		}
		
		if (color == null)
		{
			throw new IllegalArgumentException("color is null");
		}
		
		return new Color(color.getRed(), color.getGreen(), color.getBlue(),
						(int)(255*opacity));
	}
}
