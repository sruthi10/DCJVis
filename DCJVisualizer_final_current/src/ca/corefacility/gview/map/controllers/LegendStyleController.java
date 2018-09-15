package ca.corefacility.gview.map.controllers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Iterator;

import ca.corefacility.gview.map.controllers.link.Link;
import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;
import ca.corefacility.gview.style.GlobalStyle;
import ca.corefacility.gview.style.items.LegendAlignment;
import ca.corefacility.gview.style.items.LegendItemStyle;
import ca.corefacility.gview.style.items.LegendStyle;
import ca.corefacility.gview.utils.Util;

/**
 * This class is responsible for controlling access to the legend styles.
 * 
 * @author Eric Marinier
 * 
 */
public class LegendStyleController extends Controller
{
	public static final Color DEFAULT_FONT_COLOR = StyleEditorUtility.DEFAULT_FONT_COLOR;
	public static final Color DEFAULT_SWATCH_COLOR = new Color(200, 200, 200);
	public static final Color DEFAULT_BACKGROUND_COLOR = new Color(0, 0, 0, 0);
	public static final Color DEFAULT_OUTLINE_COLOR = new Color(0, 0, 0, 0);

	public static final Font DEFAULT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);

	public static final LegendAlignment DEFAULT_LEGEND_ALIGNMENT = LegendAlignment.UPPER_RIGHT;

	public static final String LEGEND_STYLE = "Legend Box";
	public final StyleController styleController;

	private static final String LEGEND_ITEM_STYLE = "Legend Text";

	// IDs for naming new legends.
	private int legendStyleID = 0;
	private int legendItemStyleID = 0;

	private final GlobalStyle globalStyle;

	public LegendStyleController(StyleController styleController, GlobalStyle globalStyle)
	{
		this.styleController = styleController;
		this.globalStyle = globalStyle;
	}

	/**
	 * 
	 * @return The number of legends in the style.
	 */
	public int getNumLegends()
	{
		ArrayList<LegendStyleToken> tokens = getLegends();

		return tokens.size();
	}

	/**
	 * 
	 * @return The legends in the style.
	 */
	public ArrayList<LegendStyleToken> getLegends()
	{
		ArrayList<LegendStyleToken> tokens = new ArrayList<LegendStyleToken>();
		Iterator<LegendStyle> legends = this.globalStyle.legendStyles();

		while (legends.hasNext())
		{
			tokens.add(new LegendStyleToken(legends.next()));
		}

		return tokens;
	}

	/**
	 * 
	 * @param parent
	 *            The parent legend style.
	 * @return The number of legend items under the parent legend.
	 */
	public int getNumLegendItems(LegendStyleToken parent)
	{
		ArrayList<LegendItemStyleToken> tokens = getLegendItems(parent);

		return tokens.size();
	}

	/**
	 * 
	 * @param parent
	 *            The parent legend style.
	 * @return The legend items under the parent legend.
	 */
	public ArrayList<LegendItemStyleToken> getLegendItems(LegendStyleToken parent)
	{
		ArrayList<LegendItemStyleToken> tokens = new ArrayList<LegendItemStyleToken>();
		LegendStyle legendStyle = parent.getStyle();
		Iterator<LegendItemStyle> legendItems = legendStyle.legendItems();

		while (legendItems.hasNext())
		{
			tokens.add(new LegendItemStyleToken(legendItems.next()));
		}

		return tokens;
	}

	/**
	 * 
	 * @param token
	 *            The associated legend style.
	 * @return The font color or the default font color if NULL.
	 */
	public Paint getFontColor(LegendStyleToken token)
	{
		LegendStyle style = token.getStyle();
		Paint color = style.getDefaultFontPaint();

		if (color == null)
		{
			color = DEFAULT_FONT_COLOR;
		}

		return color;
	}

	/**
	 * 
	 * @param token
	 *            The associated legend style.
	 * @return The outline color or the default font color if NULL.
	 */
	public Paint getOutlineColor(LegendStyleToken token)
	{
		LegendStyle style = token.getStyle();
		Paint color = style.getOutlinePaint();

		if (color == null)
		{
			color = DEFAULT_OUTLINE_COLOR;
		}

		return color;
	}

	/**
	 * 
	 * @param token
	 *            The associated legend style.
	 * @return The background color or the default color if NULL.
	 */
	public Paint getBackgroundColor(LegendStyleToken token)
	{
		LegendStyle style = token.getStyle();
		Paint color = style.getBackgroundPaint();

		if (color == null)
		{
			color = DEFAULT_BACKGROUND_COLOR;
		}

		return color;
	}

	/**
	 * 
	 * @param token
	 *            The associated legend style.
	 * @return The font or the default font if NULL.
	 */
	public Font getFont(LegendStyleToken token)
	{
		LegendStyle style = token.getStyle();
		Font font = style.getDefaultFont();

		if (font == null)
		{
			font = DEFAULT_FONT;
		}

		return font;
	}

	/**
	 * 
	 * @param token
	 *            The associated legend style.
	 * @param color
	 *            The color to set the font.
	 */
	public void setFontColor(LegendStyleToken token, Paint color)
	{
		LegendStyle style = token.getStyle();

		if (color == null)
		{
			color = DEFAULT_FONT_COLOR;
		}

		if (!Util.isEqual(style.getDefaultFontPaint(), color))
		{
			style.setDefaultFontPaint(color);

			this.notifyRebuildRequired();
		}
	}

	/**
	 * 
	 * @param token
	 *            The associated legend style.
	 * @param color
	 *            The new color of the outline.
	 */
	public void setOutlineColor(LegendStyleToken token, Paint color)
	{
		LegendStyle style = token.getStyle();

		if (color == null)
		{
			color = DEFAULT_OUTLINE_COLOR;
		}

		if (!Util.isEqual(style.getOutlinePaint(), color))
		{
			style.setOutlinePaint(color);

			this.notifyRebuildRequired();
		}
	}

	/**
	 * 
	 * @param token
	 *            The associated legend style.
	 * @param color
	 *            The new color of the background.
	 */
	public void setBackgroundColor(LegendStyleToken token, Paint color)
	{
		LegendStyle style = token.getStyle();

		if (color == null)
		{
			color = DEFAULT_BACKGROUND_COLOR;
		}

		if (!Util.isEqual(style.getBackgroundPaint(), color))
		{
			style.setBackgroundPaint(color);

			this.notifyRebuildRequired();
		}
	}

	/**
	 * 
	 * @param token
	 *            The associated legend style.
	 * @param font
	 *            The new legend font.
	 */
	public void setFont(LegendStyleToken token, Font font)
	{
		LegendStyle style = token.getStyle();

		if (font == null)
		{
			font = DEFAULT_FONT;
		}

		if (!Util.isEqual(style.getDefaultFont(), font))
		{
			style.setDefaultFont(font);

			this.notifyRebuildRequired();
		}
	}

	/**
	 * 
	 * @param token
	 *            The associated legend style.
	 * @return The alignment of the legend or the default alignment if NULL.
	 */
	public LegendAlignment getAlignment(LegendStyleToken token)
	{
		LegendStyle style = token.getStyle();
		LegendAlignment alignment = style.getAlignment();

		if (alignment == null)
		{
			alignment = DEFAULT_LEGEND_ALIGNMENT;
		}

		return alignment;
	}

	/**
	 * 
	 * @param token
	 *            The associated legend style.
	 * @param alignment
	 *            The new alignment of the legend.
	 */
	public void setAlignment(LegendStyleToken token, LegendAlignment alignment)
	{
		LegendStyle style = token.getStyle();

		if (alignment == null)
		{
			alignment = DEFAULT_LEGEND_ALIGNMENT;
		}

		if (!Util.isEqual(style.getAlignment(), alignment))
		{
			style.setAlignment(alignment);

			this.notifyRebuildRequired();
		}
	}

	/**
	 * 
	 * @return The next available legend style ID.
	 */
	public int giveLegendStyleID()
	{
		this.legendStyleID++;
		return this.legendStyleID;
	}

	/**
	 * 
	 * @return The next available legend item style ID.
	 */
	public int giveLegendItemStyleID()
	{
		this.legendItemStyleID++;
		return this.legendItemStyleID;
	}

	/**
	 * 
	 * @param token
	 *            The associated legend item style.
	 * @return The legend item style font color or the default font color if
	 *         NULL.
	 */
	public Paint getFontColor(LegendItemStyleToken token)
	{
		LegendItemStyle style = token.getStyle();
		Paint color = style.getTextPaint();

		if (color == null)
		{
			color = DEFAULT_FONT_COLOR;
		}

		return color;
	}

	/**
	 * 
	 * @param token
	 *            The associated legend item style.
	 * @return The swatch color or the default swatch color if NULL.
	 */
	public Paint getSwatchColor(LegendItemStyleToken token)
	{
		LegendItemStyle style = token.getStyle();
		Paint color = style.getSwatchPaint();

		if (color == null)
		{
			color = DEFAULT_SWATCH_COLOR;
		}

		return color;
	}

	/**
	 * 
	 * @param token
	 *            The associated legend item style.
	 * @return Whether or not the swatch is visible.
	 */
	public boolean getSwatchVisible(LegendItemStyleToken token)
	{
		LegendItemStyle style = token.getStyle();

		return style.isShowSwatch();
	}

	/**
	 * 
	 * @param token
	 *            The associated legend item style.
	 * @return The legend item style font or the default font if NULL.
	 */
	public Font getFont(LegendItemStyleToken token)
	{
		LegendItemStyle style = token.getStyle();
		Font font = style.getTextFont();

		if (font == null)
		{
			font = DEFAULT_FONT;
		}

		return font;
	}

	/**
	 * 
	 * @param token
	 *            The associated legend item style.
	 * @return The text of the legend item an empty String if NULL.
	 */
	public static String getText(LegendItemStyleToken token)
	{
		LegendItemStyle style = token.getStyle();

		String text = style.getText();

		if (text == null)
		{
			text = "";
		}

		return text;
	}

	/**
	 * 
	 * @param token
	 *            The associated legend item style.
	 * @param color
	 *            The new color of the legend item text.
	 */
	public void setFontColor(LegendItemStyleToken token, Paint color)
	{
		LegendItemStyle style = token.getStyle();

		if (color == null)
		{
			color = DEFAULT_FONT_COLOR;
		}

		if (!Util.isEqual(style.getTextPaint(), color))
		{
			style.setTextPaint(color);

			this.notifyRebuildRequired();
		}
	}

	/**
	 * 
	 * @param token
	 *            The associated legend item style.
	 * @param color
	 *            The new color of the legend item swatch.
	 */
	public void setSwatchColor(LegendItemStyleToken token, Paint color)
	{
		LegendItemStyle style = token.getStyle();

		if (color == null)
		{
			color = DEFAULT_SWATCH_COLOR;
		}

		if (!Util.isEqual(style.getSwatchPaint(), color))
		{
			style.setSwatchPaint(color);

			this.notifyRebuildRequired();
		}
	}

	/**
	 * 
	 * @param token
	 *            The associated legend item style.
	 * @param visible
	 *            Whether or not to display the legend item swatch.
	 */
	public void setSwatchVisible(LegendItemStyleToken token, boolean visible)
	{
		LegendItemStyle style = token.getStyle();

		if (style.isShowSwatch() != visible)
		{
			style.setShowSwatch(visible);

			this.notifyRebuildRequired();
		}
	}

	/**
	 * 
	 * @param token
	 *            The associated legend item style.
	 * @param font
	 *            The new font for the legend item.
	 */
	public void setFont(LegendItemStyleToken token, Font font)
	{
		LegendItemStyle style = token.getStyle();

		if (font == null)
		{
			font = DEFAULT_FONT;
		}

		if (!Util.isEqual(style.getTextFont(), font))
		{
			style.setTextFont(font);

			this.notifyRebuildRequired();
		}
	}

	/**
	 * 
	 * @param token
	 *            The associated legend item style.
	 * @param text
	 *            The new text for the legend item.
	 */
	public void setText(LegendItemStyleToken token, String text)
	{
		LegendItemStyle style = token.getStyle();

		if (text == null)
		{
			text = "";
		}

		if (!Util.isEqual(style.getText(), text))
		{
			style.setText(text);

			this.notifyRebuildRequired();
		}
	}

	/**
	 * Creates a new legend.
	 * 
	 * @return The associated legend style token.
	 */
	public LegendStyleToken createLegend()
	{
		LegendStyle style = new LegendStyle();
		LegendStyleToken token = new LegendStyleToken(style);

		this.globalStyle.addLegendStyle(style);

		this.applyDefaults(style);

		this.notifyRebuildRequired();

		return token;
	}

	/**
	 * Creates a new legend item.
	 * 
	 * @return The associated legend item style token.
	 */
	public LegendItemStyleToken createLegendItem(LegendStyleToken token)
	{
		LegendStyle legendStyle = token.getStyle();

		LegendItemStyle legendItemStyle = new LegendItemStyle();
		LegendItemStyleToken legendItemStyleToken = new LegendItemStyleToken(legendItemStyle);

		legendItemStyle.setText(LEGEND_ITEM_STYLE + " " + giveLegendItemStyleID());
		legendStyle.addLegendItem(legendItemStyle);

		this.applyDefaults(legendItemStyle);

		this.notifyRebuildRequired();

		return legendItemStyleToken;
	}

	/**
	 * 
	 * @param token
	 *            The associated legend style.
	 * @return Whether or not the removal was successful.
	 */
	public boolean removeLegend(LegendStyleToken token)
	{
		LegendStyle style = token.getStyle();

		this.notifyRebuildRequired();

		return this.globalStyle.removeLegendStyle(style);
	}

	/**
	 * 
	 * @param legendToken
	 *            The associated parent legend style. Will not be removed.
	 * @param legendItemToken
	 *            The associated legend item style to remove.
	 * @return Whether or not the removal was successful.
	 */
	public boolean removeLegendItem(LegendStyleToken legendToken, LegendItemStyleToken legendItemToken)
	{
		LegendStyle legendStyle = legendToken.getStyle();
		LegendItemStyle legendItemStyle = legendItemToken.getStyle();

		this.notifyRebuildRequired();

		return legendStyle.removeLegendItem(legendItemStyle);
	}

	public void moveLegendItem(int sourceLegendIndex, int sourceLegendItemIndex, int destinationLegendIndex,
			int destinationLegendItemIndex)
	{
		this.globalStyle.moveLegendItemStyle(sourceLegendIndex, sourceLegendItemIndex, destinationLegendIndex,
				destinationLegendItemIndex);

		this.notifyRebuildRequired();
	}

	/**
	 * Applies the default styling attributes as appropriate.
	 * 
	 * @param style
	 *            The legend item style to modify.
	 */
	public void applyDefaults(LegendItemStyle style)
	{
		style.setSwatchPaint(DEFAULT_SWATCH_COLOR);
		style.setTextFont(DEFAULT_FONT);
		style.setTextPaint(DEFAULT_FONT_COLOR);
	}

	/**
	 * Applies the default styling attributes as appropriate.
	 * 
	 * @param style
	 *            The legend style to modify.
	 */
	public void applyDefaults(LegendStyle style)
	{
		style.setBackgroundPaint(DEFAULT_BACKGROUND_COLOR);
		style.setOutlinePaint(DEFAULT_OUTLINE_COLOR);
		style.setDefaultFontPaint(DEFAULT_FONT_COLOR);
	}

	/**
	 * 
	 * @param token
	 * @return The link associated of the passed legend item style.
	 */
	public Link getLink(LegendItemStyleToken token)
	{
		LegendItemStyle LIS = token.getStyle();

		return LIS.getLink();
	}

	/**
	 * Sets the link for the passed legend item style.
	 * 
	 * @param token
	 * @param link
	 */
	public void setLink(LegendItemStyleToken token, Link link)
	{
		LegendItemStyle LIS = token.getStyle();

		LIS.setLink(link);
	}
}
