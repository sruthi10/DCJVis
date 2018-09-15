package ca.corefacility.gview.map.gui.editor.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import ca.corefacility.gview.map.controllers.LegendItemStyleToken;
import ca.corefacility.gview.map.controllers.LegendStyleController;
import ca.corefacility.gview.map.controllers.LegendStyleToken;
import ca.corefacility.gview.map.controllers.link.Link;
import ca.corefacility.gview.map.controllers.link.LinkController;
import ca.corefacility.gview.map.controllers.link.Linkable;
import ca.corefacility.gview.map.controllers.selection.SelectionController;
import ca.corefacility.gview.map.gui.editor.StyleColoredButton;
import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;
import ca.corefacility.gview.map.gui.editor.TriStateCheckBox;
import ca.corefacility.gview.map.gui.editor.communication.GUIEvent;
import ca.corefacility.gview.map.gui.editor.communication.LinkEventBroadcaster;
import ca.corefacility.gview.map.gui.editor.communication.SelectionEventBroadcaster;
import ca.corefacility.gview.map.gui.editor.communication.legendEvent.LegendItemFontFamilyEvent;
import ca.corefacility.gview.map.gui.editor.communication.legendEvent.LegendItemFontSizeEvent;
import ca.corefacility.gview.map.gui.editor.communication.legendEvent.LegendItemFontStyleEvent;
import ca.corefacility.gview.map.gui.editor.communication.legendEvent.LegendItemShowSwatchEvent;
import ca.corefacility.gview.map.gui.editor.communication.legendEvent.LegendItemSwatchColorEvent;
import ca.corefacility.gview.map.gui.editor.communication.legendEvent.LegendItemTextColorEvent;
import ca.corefacility.gview.map.gui.editor.communication.legendEvent.LegendItemTextEvent;
import ca.corefacility.gview.map.gui.editor.communication.setEvent.SetColorEvent;
import ca.corefacility.gview.map.gui.editor.panel.proxy.ProxiablePanel;
import ca.corefacility.gview.map.gui.editor.panel.proxy.ProxyLegendItemPanel;
import ca.corefacility.gview.map.gui.hint.HintLabel;
import ca.corefacility.gview.utils.Util;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * The panel for the legend item style properties.
 * 
 * @author Eric Marinier
 * 
 */
public class LegendItemPanel extends StylePanel implements ActionListener, DocumentListener, Linkable, ProxiablePanel
{
	private static final long serialVersionUID = 1L; // requested by java

	private static final String SWATCH_COLOR = "SWATCH COLOR";
	private static final String TEXT_COLOR = "TEXT COLOR";
	private static final String SHOW_SWATCH = "SHOW SWATCH";
	private static final String FONT_FAMILY = "FONT FAMILY";
	private static final String FONT_STYLE = "FONT STYLE";

	private static final String LEGEND_ITEM_STYLE_TEXT = "Legend Text Style";

	private static final String LEGEND_STYLE_CONTROLLER_NULL = "LegendStyleController is null.";
	private static final String LEGEND_ITEM_STYLE_TOKEN_NULL = "LegendItemStyleToken is null.";
	private static final String LEGEND_STYLE_TOKEN_NULL = "LegendStyleToken is null.";

	private static final String SHOW_SWATCH_LABEL_TEXT = "Show Swatch:";
	private static final String SWATCH_COLOR_LABEL_TEXT = "Swatch Color:";
	private static final String FONT_FAMILY_LABEL_TEXT = "Font Family:";
	private static final String FONT_STYLE_LABEL_TEXT = "Font Style:";
	private static final String FONT_SIZE_LABEL_TEXT = "Font Size:";
	private static final String TEXT_LABEL_TEXT = "Text:";
	private static final String TEXT_COLOR_LABEL_TEXT = "Text Color:";

	private static final String FONT_FAMILY_HINT = "The legend text font family.";
	private static final String FONT_STYLE_HINT = "The legend text font style.";
	private static final String FONT_SIZE_HINT = "The legend text font size.";
	private static final String LEGEND_ITEM_TEXT_HINT = "The text of the legend item.";
	private static final String SHOW_SWATCH_HINT = "Whether or not the swatch is visible.";
	private static final String SWATCH_COLOR_HINT = "The color of the swatch, typically used for matching this legend text item to any features of the same color.";
	private static final String TEXT_COLOR_HINT = "The color of the legend text.";

	private final Document legendTextDocument;
	private final Document fontSizeDocument;
	private boolean isDocumentUpdating = false;

	private final StyleColoredButton swatchColor;
	private final StyleColoredButton textColor;

	private final JComboBox fontFamily;
	private final JComboBox fontStyle;

	private final JTextField fontSize;
	private final JTextField legendText;

	private final TriStateCheckBox showSwatch;

	private final LegendStyleController legendController;
	private final LinkController linkController;
	private final SelectionController selectionController;

	private final LegendItemStyleToken legendItemStyle;
	private final LegendStyleToken legendStyle;

	/**
	 * Create the panel.
	 */
	public LegendItemPanel(LegendStyleController legendController, LegendStyleToken legendStyle,
			LegendItemStyleToken legendItemStyle, LinkController linkController, SelectionController selectionController)
	{
		if (legendController == null)
		{
			throw new IllegalArgumentException(LEGEND_STYLE_CONTROLLER_NULL);
		}

		if (legendItemStyle == null)
		{
			throw new IllegalArgumentException(LEGEND_ITEM_STYLE_TOKEN_NULL);
		}

		if (legendStyle == null)
		{
			throw new IllegalArgumentException(LEGEND_STYLE_TOKEN_NULL);
		}

		this.legendController = legendController;
		this.linkController = linkController;
		this.selectionController = selectionController;

		this.legendItemStyle = legendItemStyle;
		this.legendStyle = legendStyle;

		this.addGUIEventBroadcaster(new LinkEventBroadcaster(linkController, this));
		this.addGUIEventBroadcaster(new SelectionEventBroadcaster(selectionController));

		// Layout
		setBorder(new EmptyBorder(10, 10, 10, 10));
		FormLayout formLayout = new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("center:default"), FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(165dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, },
				new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"),
						FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"),
						FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"),
						FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"),
						FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, });
		formLayout.setRowGroups(new int[][] { new int[] { 10, 8, 6, 12, 14, 2, 4 } });

		JPanel inner = new JPanel();
		inner.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), LEGEND_ITEM_STYLE_TEXT,
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		add(inner, BorderLayout.NORTH);

		inner.setLayout(formLayout);

		// LEGEND ITEM TEXT
		JPanel panel_14 = new JPanel();
		inner.add(panel_14, "2, 2, fill, fill");
		panel_14.setLayout(new BorderLayout(0, 0));

		JLabel lblText = new JLabel(TEXT_LABEL_TEXT);
		panel_14.add(lblText, BorderLayout.EAST);

		JPanel panel_15 = new JPanel();
		inner.add(panel_15, "6, 2, fill, fill");
		panel_15.setLayout(new GridLayout());

		// Legend Item text
		this.legendText = new JTextField();
		panel_15.add(this.legendText);
		this.legendText.setColumns(10);
		this.legendText.getDocument().addDocumentListener(this);
		this.legendTextDocument = this.legendText.getDocument();

		// hint
		inner.add(new HintLabel(LEGEND_ITEM_TEXT_HINT), "10, 2");

		// SHOW SWATCH
		JPanel panel = new JPanel();
		inner.add(panel, "2, 4, fill, fill");
		panel.setLayout(new BorderLayout(0, 0));

		JLabel lblShowSwatch = new JLabel(SHOW_SWATCH_LABEL_TEXT);
		panel.add(lblShowSwatch, BorderLayout.EAST);

		JPanel panel_4 = new JPanel();
		inner.add(panel_4, "6, 4, fill, fill");
		panel_4.setLayout(new BorderLayout(0, 0));

		// show swatch check box
		this.showSwatch = new TriStateCheckBox();
		this.showSwatch.addActionListener(this);
		panel_4.add(this.showSwatch, BorderLayout.WEST);
		this.showSwatch.setActionCommand(SHOW_SWATCH);

		// hint
		inner.add(new HintLabel(SHOW_SWATCH_HINT), "10, 4");

		// SWATCH COLOR
		JPanel panel_5 = new JPanel();
		inner.add(panel_5, "2, 6, fill, fill");
		panel_5.setLayout(new BorderLayout(0, 0));

		JLabel lblSwatchColor = new JLabel(SWATCH_COLOR_LABEL_TEXT);
		panel_5.add(lblSwatchColor, BorderLayout.EAST);

		JPanel panel_1 = new JPanel();
		inner.add(panel_1, "6, 6, fill, fill");
		panel_1.setLayout(new BorderLayout(0, 0));

		// swatch color button
		this.swatchColor = new StyleColoredButton();
		this.swatchColor.setActionCommand(LegendItemPanel.SWATCH_COLOR);
		panel_1.add(this.swatchColor, BorderLayout.WEST);
		this.swatchColor.addActionListener(this);

		// hint
		inner.add(new HintLabel(SWATCH_COLOR_HINT), "10, 6");

		// TEXT COLOR
		JPanel panel_6 = new JPanel();
		inner.add(panel_6, "2, 8, fill, fill");
		panel_6.setLayout(new BorderLayout(0, 0));

		JLabel label = new JLabel(TEXT_COLOR_LABEL_TEXT);
		panel_6.add(label, BorderLayout.EAST);

		JPanel panel_2 = new JPanel();
		inner.add(panel_2, "6, 8, fill, fill");
		panel_2.setLayout(new BorderLayout(0, 0));

		// text color button
		this.textColor = new StyleColoredButton();
		this.textColor.setActionCommand(LegendItemPanel.TEXT_COLOR);
		panel_2.add(this.textColor, BorderLayout.WEST);
		this.textColor.addActionListener(this);

		// hint
		inner.add(new HintLabel(TEXT_COLOR_HINT), "10, 8");

		// FONT FAMILY
		JPanel panel_7 = new JPanel();
		inner.add(panel_7, "2, 10, fill, fill");
		panel_7.setLayout(new BorderLayout(0, 0));

		JLabel label_3 = new JLabel(FONT_FAMILY_LABEL_TEXT);
		panel_7.add(label_3, BorderLayout.EAST);

		JPanel panel_3 = new JPanel();
		inner.add(panel_3, "6, 10, fill, fill");
		panel_3.setLayout(new GridLayout());

		// font family combo box
		this.fontFamily = new JComboBox(StyleEditorUtility.FONT_NAMES);
		this.fontFamily.addActionListener(this);
		panel_3.add(this.fontFamily);
		this.fontFamily.setActionCommand(FONT_FAMILY);

		// hint
		inner.add(new HintLabel(FONT_FAMILY_HINT), "10, 10");

		// FONT STYLE
		JPanel panel_10 = new JPanel();
		inner.add(panel_10, "2, 12, fill, fill");
		panel_10.setLayout(new BorderLayout(0, 0));

		JLabel lblFontStyle = new JLabel(FONT_STYLE_LABEL_TEXT);
		panel_10.add(lblFontStyle, BorderLayout.EAST);

		JPanel panel_12 = new JPanel();
		inner.add(panel_12, "6, 12, fill, fill");
		panel_12.setLayout(new GridLayout());

		// font style combo box
		this.fontStyle = new JComboBox(StyleEditorUtility.FONT_STYLES);
		this.fontStyle.addActionListener(this);
		panel_12.add(this.fontStyle);
		this.fontStyle.setActionCommand(FONT_STYLE);

		// hint
		inner.add(new HintLabel(FONT_STYLE_HINT), "10, 12");

		// FONT SIZE
		JPanel panel_11 = new JPanel();
		inner.add(panel_11, "2, 14, fill, fill");
		panel_11.setLayout(new BorderLayout(0, 0));

		JLabel lblFontSize = new JLabel(FONT_SIZE_LABEL_TEXT);
		panel_11.add(lblFontSize, BorderLayout.EAST);

		JPanel panel_13 = new JPanel();
		inner.add(panel_13, "6, 14, fill, fill");
		panel_13.setLayout(new GridLayout());

		// font size text field
		this.fontSize = new JTextField();
		panel_13.add(this.fontSize);
		this.fontSize.setColumns(10);
		this.fontSize.getDocument().addDocumentListener(this);
		this.fontSizeDocument = this.fontSize.getDocument();

		// hint
		inner.add(new HintLabel(FONT_SIZE_HINT), "10, 14");

		this.update();
	}

	/**
	 * Applies GUI changes to create the proxy version of the panel.
	 */
	private void proxy()
	{
		this.setUpdating(true);

		this.legendText.setText(null);

		this.showSwatch.setSelected(false);
		this.showSwatch.setMixed(true);

		this.swatchColor.setBackground(StyleEditorUtility.DEFAULT_COLOR);
		this.swatchColor.setNotify(true);

		this.textColor.setBackground(StyleEditorUtility.DEFAULT_COLOR);
		this.textColor.setNotify(true);

		this.fontFamily.addItem(StyleEditorUtility.PROXY_VALUE);
		this.fontFamily.setSelectedItem(StyleEditorUtility.PROXY_VALUE);

		this.fontStyle.addItem(StyleEditorUtility.PROXY_VALUE);
		this.fontStyle.setSelectedItem(StyleEditorUtility.PROXY_VALUE);

		this.fontSize.setText(null);

		this.setUpdating(false);
	}

	@Override
	public LegendItemPanel createProxy()
	{
		ProxyLegendItemPanel panel = new ProxyLegendItemPanel(legendController, legendStyle, legendItemStyle,
				linkController, selectionController);

		((LegendItemPanel) panel).proxy();

		return panel;
	}

	/**
	 * 
	 * @return The text color.
	 */
	private Paint getTextPaint()
	{
		return this.textColor.getPaint();
	}

	/**
	 * Sets the text color.
	 * 
	 * @param color
	 */
	private void setTextPaint(Paint p)
	{
		this.textColor.setPaint(p);
	}

	/**
	 * 
	 * @return The swatch color.
	 */
	private Paint getSwatchPaint()
	{
		return this.swatchColor.getPaint();
	}

	/**
	 * Sets the swatch color.
	 * 
	 * @param p
	 */
	public void setSwatchPaint(Paint p)
	{
		this.swatchColor.setPaint(p);
	}

	/**
	 * 
	 * @return The font family as a string.
	 */
	private String getFontFamily()
	{
		Object temp;
		String result;

		temp = this.fontFamily.getSelectedItem();

		if (temp instanceof String)
		{
			result = (String) temp;
		}
		else
		{
			throw new IllegalArgumentException("Font Family is not a String");
		}

		return result;
	}

	/**
	 * Sets the font family.
	 * 
	 * @param s
	 */
	private void setFontFamily(String s)
	{
		this.fontFamily.setSelectedItem(s);
	}

	/**
	 * 
	 * @return The font style as a string.
	 */
	private String getFontStyle()
	{
		Object temp;
		String result;

		temp = this.fontStyle.getSelectedItem();

		if (temp instanceof String)
		{
			result = (String) temp;
		}
		else
		{
			throw new IllegalArgumentException("Font Style is not a String");
		}

		return result;
	}

	/**
	 * Sets the font style.
	 * 
	 * @param fStyle
	 */
	private void setFontStyle(String fStyle)
	{
		this.fontStyle.setSelectedItem(fStyle);
	}

	/**
	 * 
	 * @return The font size as a string.
	 */
	private String getFontSizeText()
	{
		return this.fontSize.getText();
	}

	/**
	 * Sets the field of the font size.
	 * 
	 * @param text
	 */
	private void setFontSizeText(final int size)
	{
		if (this.isDocumentUpdating)
		{
			return;
		}

		fontSize.setText(size + "");
	}

	/**
	 * Sets the GUI elements of the panel to match the passed font.
	 * 
	 * @param font
	 */
	private void setLegendItemFont(Font font)
	{
		if (font != null)
		{
			setFontFamily(font.getFamily());

			if (font.isBold() && font.isItalic())
			{
				setFontStyle(StyleEditorUtility.BOLD_ITALIC);
			}
			else if (font.isPlain())
			{
				setFontStyle(StyleEditorUtility.PLAIN);
			}
			else if (font.isBold())
			{
				setFontStyle(StyleEditorUtility.BOLD);
			}
			else if (font.isItalic())
			{
				setFontStyle(StyleEditorUtility.ITALIC);
			}

			setFontSizeText(font.getSize());
		}
	}

	/**
	 * 
	 * @return The legend text.
	 */
	public String getLegendText()
	{
		return this.legendText.getText();
	}

	/**
	 * Sets the legend text.
	 * 
	 * @param text
	 */
	private void setLegendText(final String text)
	{
		if (this.isDocumentUpdating)
		{
			return;
		}

		this.legendText.setText(text);
	}

	/**
	 * 
	 * @return A boolean of whether or not the swatch is show.
	 */
	private boolean getShowSwatch()
	{
		return this.showSwatch.isSelected();
	}

	/**
	 * Sets whether or now the swatch is show.
	 * 
	 * @param b
	 */
	private void setShowSwatch(boolean b)
	{
		this.showSwatch.setSelected(b);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (this.isUpdating())
		{
			return;
		}

		if (e.getActionCommand().equals(SWATCH_COLOR))
		{
			swatchColorAction();
		}
		else if (e.getActionCommand().equals(LegendItemPanel.TEXT_COLOR))
		{
			textColorAction();
		}
		else if (e.getActionCommand().equals(SHOW_SWATCH))
		{
			showSwatchAction();
		}
		else if (e.getActionCommand().equals(FONT_FAMILY))
		{
			fontFamilyAction();
		}
		else if (e.getActionCommand().equals(FONT_STYLE))
		{
			fontStyleAction();
		}
	}

	/**
	 * The response to the font family being changed.
	 */
	private void fontFamilyAction()
	{
		this.broadcastEvent(new LegendItemFontFamilyEvent(getFontFamily()));
	}

	/**
	 * The response to the font style being changed.
	 */
	private void fontStyleAction()
	{
		this.broadcastEvent(new LegendItemFontStyleEvent(getFontStyle()));
	}

	/**
	 * The response to the font size being changed.
	 */
	private void fontSizeAction()
	{
		if (Util.isInteger(getFontSizeText()))
		{
			this.broadcastEvent(new LegendItemFontSizeEvent(Integer.parseInt(getFontSizeText())));
		}
	}

	/**
	 * The response to the swatch visibility being changed.
	 */
	private void showSwatchAction()
	{
		this.broadcastEvent(new LegendItemShowSwatchEvent(getShowSwatch()));
	}

	/**
	 * The response to the text being changed.
	 */
	private void textAction()
	{
		this.broadcastEvent(new LegendItemTextEvent(this.getLegendText()));
	}

	/**
	 * The response to the text color being changed.
	 */
	private void textColorAction()
	{
		Color color = StyleEditorUtility.showColorPicker(this, this.textColor.getBackground());

		if (color != null)
		{
			this.textColor.setBackground(color);
			this.broadcastEvent(new LegendItemTextColorEvent(color));
		}
	}

	/**
	 * The response to the swatch color being changed.
	 */
	private void swatchColorAction()
	{
		Color color = StyleEditorUtility.showColorPicker(this, this.swatchColor.getBackground());

		if (color != null)
		{
			this.swatchColor.setBackground(color);
			this.broadcastEvent(new LegendItemSwatchColorEvent(color));
		}
	}

	@Override
	/**
	 * Updates the panel.
	 */
	public void update()
	{
		setUpdating(true);

		updateTextColor();
		updateSwatchColor();
		updateShowSwatch();
		updateFont();
		updateLegendText();

		setUpdating(false);
	}

	/**
	 * Updates the text color.
	 */
	private void updateTextColor()
	{
		Paint tempPaint = this.legendController.getFontColor(legendItemStyle);

		setTextPaint(tempPaint);
	}

	/**
	 * Updates the swatch color.
	 */
	private void updateSwatchColor()
	{
		Paint tempPaint = this.legendController.getSwatchColor(legendItemStyle);

		setSwatchPaint(tempPaint);
	}

	/**
	 * Updates the show swatch component.
	 */
	private void updateShowSwatch()
	{
		if (this.legendController.getSwatchVisible(legendItemStyle))
		{
			setShowSwatch(true);
		}
		else
		{
			setShowSwatch(false);
		}
	}

	/**
	 * Updates the font.
	 */
	private void updateFont()
	{
		Font font = this.legendController.getFont(legendItemStyle);

		if (font == null)
		{
			font = this.legendController.getFont(legendStyle);
		}

		setLegendItemFont(font);
	}

	/**
	 * Updates the legend text.
	 */
	private void updateLegendText()
	{
		setLegendText(LegendStyleController.getText(legendItemStyle));
	}

	@Override
	/**
	 * Applies the style.
	 */
	protected void doApply()
	{
		applyTextColor();
		applySwatchColor();
		applyShowSwatch();
		applyFont();
		applyLegendText();
	}

	/**
	 * Applies the text color.
	 */
	private void applyTextColor()
	{
		this.legendController.setFontColor(legendItemStyle, getTextPaint());
	}

	/**
	 * Applies the swatch color.
	 */
	private void applySwatchColor()
	{
		this.legendController.setSwatchColor(legendItemStyle, getSwatchPaint());
	}

	/**
	 * Applies the show swatch.
	 */
	private void applyShowSwatch()
	{
		this.legendController.setSwatchVisible(legendItemStyle, getShowSwatch());
	}

	/**
	 * Applies the font.
	 */
	private void applyFont()
	{
		try
		{
			Font font = StyleEditorUtility.createFont(getFontFamily(), getFontStyle(), getFontSizeText());
			this.legendController.setFont(legendItemStyle, font);
		}
		catch (StyleEditorUtility.ConversionException e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage());

			this.fontSize.setText(this.legendController.getFont(this.legendItemStyle).getSize() + "");
		}
	}

	/**
	 * Applies the legend text.
	 */
	private void applyLegendText()
	{
		this.legendController.setText(legendItemStyle, getLegendText());
	}

	/**
	 * 
	 * @return The legend item style as a token.
	 */
	public LegendItemStyleToken getLegendItemSyle()
	{
		return this.legendItemStyle;
	}

	/**
	 * 
	 * @return The legend style controller.
	 */
	public LegendStyleController getLegendStyleController()
	{
		return this.legendController;
	}

	@Override
	public Link getLink()
	{
		return this.legendController.getLink(this.legendItemStyle);
	}

	@Override
	public void setLink(Link link)
	{
		this.legendController.setLink(this.legendItemStyle, link);
	}

	@Override
	public void executeGUIEvent(GUIEvent event)
	{
		if (event instanceof LegendItemShowSwatchEvent)
		{
			this.setShowSwatch(((LegendItemShowSwatchEvent) event).getData());
		}
		else if (event instanceof LegendItemSwatchColorEvent)
		{
			this.setSwatchPaint(((LegendItemSwatchColorEvent) event).getData());
		}
		else if (event instanceof LegendItemTextColorEvent)
		{
			this.setTextPaint(((LegendItemTextColorEvent) event).getData());
		}
		else if (event instanceof LegendItemTextEvent)
		{
			this.setLegendText(((LegendItemTextEvent) event).getData());
		}
		else if (event instanceof SetColorEvent)
		{
			this.setSwatchPaint(((SetColorEvent) event).getData());
		}
		else if (event instanceof LegendItemFontFamilyEvent)
		{
			this.setFontFamily(((LegendItemFontFamilyEvent) event).getData());
		}
		else if (event instanceof LegendItemFontStyleEvent)
		{
			this.setFontStyle(((LegendItemFontStyleEvent) event).getData());
		}
		else if (event instanceof LegendItemFontSizeEvent)
		{
			this.setFontSizeText(((LegendItemFontSizeEvent) event).getData());
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e)
	{
		textChanged(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e)
	{
		textChanged(e);
	}

	@Override
	public void changedUpdate(DocumentEvent e)
	{
		textChanged(e);
	}

	/**
	 * A single method for responding to a JComboBox being changed.
	 * 
	 * @param e
	 */
	private void textChanged(DocumentEvent e)
	{
		if (e == null || e.getDocument() == null || this.isUpdating())
		{
			return;
		}

		this.isDocumentUpdating = true;

		if (e.getDocument().equals(this.legendTextDocument))
		{
			textAction();
		}
		else if (e.getDocument().equals(this.fontSizeDocument))
		{
			fontSizeAction();
		}

		this.isDocumentUpdating = false;
	}
}
