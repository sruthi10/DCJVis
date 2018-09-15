package ca.corefacility.gview.map.gui.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Window;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Parser;

import ca.corefacility.gview.layout.feature.FeatureShapeRealizer;
import ca.corefacility.gview.layout.feature.ForwardArrow2ShapeRealizer;
import ca.corefacility.gview.layout.feature.ForwardArrowShapeRealizer;
import ca.corefacility.gview.layout.feature.NoArrowShapeRealizer;
import ca.corefacility.gview.layout.feature.ReverseArrow2ShapeRealizer;
import ca.corefacility.gview.layout.feature.ReverseArrowShapeRealizer;
import ca.corefacility.gview.layout.sequence.LayoutFactory;
import ca.corefacility.gview.layout.sequence.circular.LayoutFactoryCircular;
import ca.corefacility.gview.map.effects.BasicEffect;
import ca.corefacility.gview.map.effects.ShapeEffectRenderer;
import ca.corefacility.gview.map.effects.StandardEffect;
import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.style.io.gss.TextExtractorHandler;
import ca.corefacility.gview.style.io.gss.exceptions.ParseException;
import ca.corefacility.gview.style.items.LegendAlignment;
import ca.corefacility.gview.textextractor.FeatureTextExtractor;

import com.bric.swing.ColorPicker;

/**
 * Contains many constants, methods and other utility for the style editor.
 * 
 * @author Eric Marinier
 * 
 */
public class StyleEditorUtility
{
	public static final String GLOBAL_TEXT = "Global";
	public static final String RULER_TEXT = "Ruler";
	public static final String BACKBONE_TEXT = "Backbone";
	public static final String LEGEND_TEXT = "Legend";
	public static final String TOOLTIP_TEXT = "Tooltip";
	public static final String STYLE_TEXT = "Style";
	public static final String LEGEND_STYLE_TEXT = "Legend Style";
	public static final String LEGEND_ITEM_TEXT = "Legend Item";

	public static final String[] FONT_NAMES;

	static
	{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		FONT_NAMES = ge.getAvailableFontFamilyNames(Locale.getDefault());
	}

	public static final String PLAIN = "Plain";
	public static final String BOLD = "Bold";
	public static final String ITALIC = "Italic";
	public static final String BOLD_ITALIC = "Bold-Italic";

	public static final String[] FONT_STYLES = { PLAIN, BOLD, ITALIC, BOLD_ITALIC };

	public static final Font DEFAULT_FONT = new Font(Font.DIALOG, Font.PLAIN, 12);

	public static final HashMap<String, LegendAlignment> ALIGNMENT_MAP_STRINGS_TO_ALIGN = new HashMap<String, LegendAlignment>();
	public static final HashMap<LegendAlignment, String> ALIGNMENT_MAP_ALIGN_TO_STRINGS = new HashMap<LegendAlignment, String>();

	public static final String UPPER_LEFT = "Upper Left";
	public static final String UPPER_CENTER = "Upper Center";
	public static final String UPPER_RIGHT = "Upper Right";
	public static final String MIDDLE_LEFT = "Middle Left";
	public static final String MIDDLE_CENTER = "Middle Center";
	public static final String MIDDLE_RIGHT = "Middle Right";
	public static final String LOWER_LEFT = "Lower Left";
	public static final String LOWER_CENTER = "Lower Center";
	public static final String LOWER_RIGHT = "Lower Right";

	public static final String[] LEGEND_ALIGNMENT_STRINGS = { UPPER_LEFT, UPPER_CENTER, UPPER_RIGHT, MIDDLE_LEFT,
			MIDDLE_CENTER, MIDDLE_RIGHT, LOWER_LEFT, LOWER_CENTER, LOWER_RIGHT };

	public static final LegendAlignment[] LEGEND_ALIGNMENTS = { LegendAlignment.UPPER_LEFT,
			LegendAlignment.UPPER_CENTER, LegendAlignment.UPPER_RIGHT, LegendAlignment.MIDDLE_LEFT,
			LegendAlignment.MIDDLE_CENTER, LegendAlignment.MIDDLE_RIGHT, LegendAlignment.LOWER_LEFT,
			LegendAlignment.LOWER_CENTER, LegendAlignment.LOWER_RIGHT };

	public static final String LEGEND_PANEL_TEXT = "The Legend Panel";

	public static final String APPLY_CHANGES = "Apply Changes";
	public static final String REVERT_CHANGES = "Revert Changes";
	public static final String SAVE = "Save";
	public static final String FILE_TEXT = "File";
	public static final String SELECTION_TEXT = "Selection";
	public static final String HELP_TEXT = "Help";
	public static final String HELP_PAGE_TEXT = "Website";
	public static final String EXIT_TEXT = "Exit";
	public static final String SLOT_TEXT = "Slot";
	public static final String PLOT_TEXT = "Plot";
	public static final String SET_TEXT = "Set";
	public static final String LABEL_TEXT = "Label Style";
	public static final String SLOT_DETAIL_TEXT = "Slot Node Labels";

	public static final String FULL_TEXT = "Basic";
	public static final String MINIMAL_TEXT = "Minimal";
	public static final String LINK_TEXT = "Legend Links";
	public static final String NONE_TEXT = "None";

	public static final String LAYOUT_TEXT = "Layout";
	public static final LayoutFactory DEFAULT_LAYOUT = new LayoutFactoryCircular();

	public static final String LINEAR_LAYOUT = "Linear";
	public static final String CIRCULAR_LAYOUT = "Circular";

	public static final String DEFAULT_STYLE_NODE_NAME = "Node";

	public static final String COG_TO_COLOR_MAPPER = "COG Category to Color";
	public static final String AUTO_POPULATE_COG_CATEGORIES = "Auto Populate COG Categories";
	public static final String SCORE_TO_OPACITY = "Score to Opacity";

	public static final String[] PROPERTY_MAPPERS = { SCORE_TO_OPACITY, COG_TO_COLOR_MAPPER,
			AUTO_POPULATE_COG_CATEGORIES };

	public static String[] COG_CATEGORIES = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
			"O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	public static final String BASIC = "basic";
	public static final String OUTLINE = "outline";
	public static final String SHADED = "shaded";
	public static final String STANDARD = "standard";
	public static final String[] featureEffects = { BASIC, OUTLINE, SHADED, STANDARD };

	public static final String LABELS_ABOVE = "above";
	public static final String LABELS_BELOW = "below";
	public static final String LABELS_BOTH = "both";
	public static final String LABELS_NONE = "none";
	public static final String[] rulerPlacements = { LABELS_ABOVE, LABELS_BELOW, LABELS_BOTH, LABELS_NONE };

	public static final String LOCATION = "location";
	public static final String SYMBOLS = "symbols";
	public static final String ANNOTATION = "annotation";
	public static final String BLANK = "blank";
	public static final String STRING_BUILDER = "string builder";
	public static final String[] textExtractorTexts = { ANNOTATION, LOCATION, BLANK };

	public static final String CLOCKWISE_ARROW = "clockwise-arrow";
	public static final String COUNTERCLOCKWISE_ARROW = "counterclockwise-arrow";
	public static final String CLOCKWISE_ARROW_2 = "clockwise-arrow2";
	public static final String COUNTERCLOCKWISE_ARROW_2 = "counterclockwise-arrow2";
	public static final String BLOCK = "block";
	public static final String[] featureShapes = { CLOCKWISE_ARROW, COUNTERCLOCKWISE_ARROW, CLOCKWISE_ARROW_2,
			COUNTERCLOCKWISE_ARROW_2, BLOCK };

	public static final String PROXY_VALUE = "*";

	public static final Color DEFAULT_COLOR = new Color(200, 200, 200);
	public static final Color DEFAULT_FONT_COLOR = Color.black;

	public static final Image CHECKBOARD = GUIUtility.loadImage("images/icons/checkerboard.png");

	public static final ImageIcon GLOBE_IMAGE = new ImageIcon(GUIUtility.loadImage("images/icons/globe.png"));
	public static final ImageIcon RULER_IMAGE = new ImageIcon(GUIUtility.loadImage("images/icons/ruler-triangle.png"));
	public static final ImageIcon SLOTS_IMAGE = new ImageIcon(GUIUtility.loadImage("images/icons/category.png"));
	public static final ImageIcon TOOLTIP_IMAGE = new ImageIcon(GUIUtility.loadImage("images/icons/cursor.png"));
	public static final ImageIcon BACKBONE_IMAGE = new ImageIcon(GUIUtility.loadImage("images/icons/molecule.png"));
	public static final ImageIcon LABEL_IMAGE = new ImageIcon(GUIUtility.loadImage("images/icons/tags.png"));
	public static final ImageIcon OPEN_FOLDER = new ImageIcon(
			GUIUtility.loadImage("images/icons/blue-folder-horizontal-open.png"));
	public static final ImageIcon CLOSED_FOLDER = new ImageIcon(
			GUIUtility.loadImage("images/icons/blue-folder-horizontal.png"));
	public static final ImageIcon LEAF = new ImageIcon(GUIUtility.loadImage("images/icons/blue-document.png"));
	public static final ImageIcon QUESTION = new ImageIcon(GUIUtility.loadImage("images/icons/question.png"));
	public static final ImageIcon EXCLAMATION = new ImageIcon(GUIUtility.loadImage("images/icons/exclamation-dark.png"));

	public static Color[] COG_COLORS = { new Color(255, 0, 0, 255), new Color(255, 98, 71, 255),
			new Color(0, 255, 255, 255), new Color(240, 230, 140, 255), new Color(70, 130, 180, 255),
			new Color(0, 191, 255, 255), new Color(0, 206, 209, 255), new Color(0, 0, 255, 255),
			new Color(105, 90, 205, 255), new Color(240, 128, 128, 255), new Color(255, 136, 0, 255),
			new Color(255, 20, 147, 255), new Color(108, 142, 35, 255), new Color(33, 139, 33, 255),
			new Color(189, 182, 107, 255), new Color(154, 205, 50, 255), new Color(0, 0, 128, 255),
			new Color(190, 190, 190, 255), new Color(105, 105, 105, 255), new Color(50, 205, 50, 255),
			new Color(175, 255, 47, 255), new Color(0, 250, 150, 255), new Color(143, 188, 143, 255),
			new Color(255, 0, 0, 255), new Color(59, 179, 111, 255), new Color(255, 255, 0, 255) };

	static
	{
		ALIGNMENT_MAP_STRINGS_TO_ALIGN.put(UPPER_LEFT, LegendAlignment.UPPER_LEFT);
		ALIGNMENT_MAP_STRINGS_TO_ALIGN.put(UPPER_CENTER, LegendAlignment.UPPER_CENTER);
		ALIGNMENT_MAP_STRINGS_TO_ALIGN.put(UPPER_RIGHT, LegendAlignment.UPPER_RIGHT);
		ALIGNMENT_MAP_STRINGS_TO_ALIGN.put(MIDDLE_LEFT, LegendAlignment.MIDDLE_LEFT);
		ALIGNMENT_MAP_STRINGS_TO_ALIGN.put(MIDDLE_CENTER, LegendAlignment.MIDDLE_CENTER);
		ALIGNMENT_MAP_STRINGS_TO_ALIGN.put(MIDDLE_RIGHT, LegendAlignment.MIDDLE_RIGHT);
		ALIGNMENT_MAP_STRINGS_TO_ALIGN.put(LOWER_LEFT, LegendAlignment.LOWER_LEFT);
		ALIGNMENT_MAP_STRINGS_TO_ALIGN.put(LOWER_CENTER, LegendAlignment.LOWER_CENTER);
		ALIGNMENT_MAP_STRINGS_TO_ALIGN.put(LOWER_RIGHT, LegendAlignment.LOWER_RIGHT);

		ALIGNMENT_MAP_ALIGN_TO_STRINGS.put(LegendAlignment.UPPER_LEFT, UPPER_LEFT);
		ALIGNMENT_MAP_ALIGN_TO_STRINGS.put(LegendAlignment.UPPER_CENTER, UPPER_CENTER);
		ALIGNMENT_MAP_ALIGN_TO_STRINGS.put(LegendAlignment.UPPER_RIGHT, UPPER_RIGHT);
		ALIGNMENT_MAP_ALIGN_TO_STRINGS.put(LegendAlignment.MIDDLE_LEFT, MIDDLE_LEFT);
		ALIGNMENT_MAP_ALIGN_TO_STRINGS.put(LegendAlignment.MIDDLE_CENTER, MIDDLE_CENTER);
		ALIGNMENT_MAP_ALIGN_TO_STRINGS.put(LegendAlignment.MIDDLE_RIGHT, MIDDLE_RIGHT);
		ALIGNMENT_MAP_ALIGN_TO_STRINGS.put(LegendAlignment.LOWER_LEFT, LOWER_LEFT);
		ALIGNMENT_MAP_ALIGN_TO_STRINGS.put(LegendAlignment.LOWER_CENTER, LOWER_CENTER);
		ALIGNMENT_MAP_ALIGN_TO_STRINGS.put(LegendAlignment.LOWER_RIGHT, LOWER_RIGHT);
	}

	public static final String gViewWebsite = "https://www.gview.ca/";

	/**
	 * Extracts the feature text extractor from the passed string.
	 * 
	 * @param text
	 *            The text to extract the filter from.
	 */
	public static FeatureTextExtractor decodeFeatureTextExtractor(String text)
	{
		Parser parser = new com.steadystate.css.parser.SACParserCSS2();
		InputSource currEncodedString = new InputSource(new StringReader(text));
		LexicalUnit textExtractor;
		FeatureTextExtractor featureTextExtractor = null;

		if (text != null)
		{
			try
			{
				textExtractor = parser.parsePropertyValue(currEncodedString);
				featureTextExtractor = TextExtractorHandler.decode(textExtractor);
			}
			catch (CSSException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}
		}

		return featureTextExtractor;
	}

	/**
	 * This method is a utility to turn the font parameters into a Font object.
	 * 
	 * @param fontFamily
	 *            The name of the font family as a string. Will be passed
	 *            directly through to the Font class.
	 * @param fontStyle
	 *            The style of the font, matching those in this class:
	 *            BOLD_ITALIC, BOLD, ITALIC or PLAIN.
	 * @param fontSize
	 *            An integer as a string that will be parsed.
	 * @return The created Font object based on the parameters.
	 * @throws ConversionException
	 */
	public static Font createFont(String fontFamily, String fontStyle, String fontSize) throws ConversionException
	{
		Font font = null;

		int tempFontStyle = 0;
		int tempInt;

		if (StyleEditorUtility.BOLD_ITALIC.equals(fontStyle))
		{
			tempFontStyle = Font.BOLD | Font.ITALIC;
		}
		else if (StyleEditorUtility.BOLD.equals(fontStyle))
		{
			tempFontStyle = Font.BOLD;
		}
		else if (StyleEditorUtility.ITALIC.equals(fontStyle))
		{
			tempFontStyle = Font.ITALIC;
		}
		else
		{
			tempFontStyle = Font.PLAIN;
		}

		try
		{
			tempInt = Integer.parseInt(fontSize);

			if (tempInt <= 0)
			{
				throw new ConversionException("Font Size value " + tempInt + " must be greater than zero.");
			}
			else
			{
				font = new Font(fontFamily, tempFontStyle, tempInt);
			}
		}
		catch (NumberFormatException e)
		{
			throw new ConversionException("Font size value " + fontSize + " is not a valid integer");
		}

		return font;
	}

	/**
	 * Converts the string to the associated ShapeEffectRender object.
	 * 
	 * @param effectText
	 *            Either: OUTLINE, SHADED, STANDARD or BASIC.
	 * @return The associated ShapeEffectRender object.
	 * @throws ConversionException
	 */
	public static ShapeEffectRenderer getFeatureEffectRenderer(String effectText) throws ConversionException
	{
		if (effectText.equals(OUTLINE))
		{
			return ShapeEffectRenderer.OUTLINE_RENDERER;
		}
		else if (effectText.equals(SHADED))
		{
			return ShapeEffectRenderer.SHADING_RENDERER;
		}
		else if (effectText.equals(STANDARD))
		{
			return ShapeEffectRenderer.STANDARD_RENDERER;
		}
		else if (effectText.equals(BASIC))
		{
			return new BasicEffect();
		}
		else
		{
			throw new ConversionException("invalid ShapeEffectRenderer " + effectText);
		}
	}

	/**
	 * Converts the shape effect renderer to a string.
	 * 
	 * @param effect
	 * @return The associated String.
	 */
	public static String getFeatureEffectRenderer(ShapeEffectRenderer effect)
	{
		if (effect.equals(ShapeEffectRenderer.OUTLINE_RENDERER))
		{
			return OUTLINE;
		}
		else if (effect.equals(ShapeEffectRenderer.SHADING_RENDERER))
		{
			return SHADED;
		}
		else if (effect.equals(ShapeEffectRenderer.STANDARD_RENDERER))
		{
			return STANDARD;
		}
		else if (effect.equals(new StandardEffect()))
		{
			return BASIC;
		}
		else
		{
			return "Unknown";
		}
	}

	/**
	 * Converts the string to an associated feature shape realizer.
	 * 
	 * @param effectText
	 *            Either: CLOCKWISE_ARROW, CLOCKWISE_ARROW_2,
	 *            COUNTERCLOCKWISE_ARROW, COUNTERCLOCKWISE_ARROW_2 or BLOCK.
	 * @return The associated FeatureShapeRealizer object.
	 * @throws ConversionException
	 */
	public static FeatureShapeRealizer getShapeRealizer(String effectText) throws ConversionException
	{
		FeatureShapeRealizer featureShapeRealizer = null;

		if (effectText.equals(CLOCKWISE_ARROW))
		{
			featureShapeRealizer = new ForwardArrowShapeRealizer();
		}
		else if (effectText.equals(CLOCKWISE_ARROW_2))
		{
			featureShapeRealizer = new ForwardArrow2ShapeRealizer();
		}
		else if (effectText.equals(COUNTERCLOCKWISE_ARROW))
		{
			featureShapeRealizer = new ReverseArrowShapeRealizer();
		}
		else if (effectText.equals(COUNTERCLOCKWISE_ARROW_2))
		{
			featureShapeRealizer = new ReverseArrow2ShapeRealizer();
		}
		else if (effectText.equals(BLOCK))
		{
			featureShapeRealizer = new NoArrowShapeRealizer();
		}
		else
		{
			throw new ConversionException("Unknown shape effect renderer " + effectText);
		}

		return featureShapeRealizer;
	}

	/**
	 * Custom exception class.
	 * 
	 */
	public static class ConversionException extends Exception
	{
		private static final long serialVersionUID = 1L;

		public ConversionException()
		{
			super();
		}

		public ConversionException(String message, Throwable cause)
		{
			super(message, cause);
		}

		public ConversionException(String message)
		{
			super(message);
		}

		public ConversionException(Throwable cause)
		{
			super(cause);
		}
	}

	/**
	 * Shows the ColorPicker above the passed component's closest parent window
	 * with the starting color of the color picker being the passed color.
	 * 
	 * This will show an option for editing the color's alpha value.
	 * 
	 * @param component
	 *            The component to show the color picker over.
	 * @param color
	 *            The starting color of the color picker. *
	 * @return The user selected color. May be null.
	 */
	public static Color showColorPicker(Component component, Color color)
	{
		Window parentWindow = null;
		Component current = component;

		// while current is not an instance of window and current isn't null
		while (!(current instanceof Window) && current != null)
		{
			current = current.getParent();
		}

		if (current instanceof Window)
		{
			parentWindow = (Window) current;
		}

		// parent window will either be a window or be null
		return ColorPicker.showDialog(parentWindow, color, true);
	}

	/**
	 * Shows the ColorPicker above the passed component's closest parent window.
	 * 
	 * This will show an option for editing the color's alpha value.
	 * 
	 * @param component
	 *            The component to show the color picker over. *
	 * @return The user selected color. May be null.
	 */
	public static Color showColorPicker(Component component)
	{
		return showColorPicker(component, null);
	}

	/**
	 * Determines whether a JComboBox contains the passed object.
	 * 
	 * @param combo
	 * @param element
	 * @return Whether or not the passed combo box contains the passed element.
	 */
	public static boolean hasElement(JComboBox combo, Object element)
	{
		for (int i = 0; i < combo.getItemCount(); i++)
		{
			if (combo.getItemAt(i).equals(element))
			{
				return true;
			}
		}

		return false;
	}
}
