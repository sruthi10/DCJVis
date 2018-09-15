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

import ca.corefacility.gview.map.controllers.TooltipStyleController;
import ca.corefacility.gview.map.gui.editor.StyleColoredButton;
import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;
import ca.corefacility.gview.map.gui.hint.HintLabel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * The panel for the tool tip style.
 * 
 * @author Eric Marinier
 * 
 */
public class TooltipPanel extends StylePanel implements ActionListener
{
	private static final long serialVersionUID = 1L; // requested by java

	private static final String TEXT_COLOR = "Text Color";
	private static final String OUTLINE_COLOR = "Outline Color";
	private static final String BACKGROUND_COLOR = "Background Color";
	private static final String TOOLTIP_STYLE_TEXT = "Tooltip Style";

	private static final String TEXT_COLOR_LABEL_TEXT = "Text Color:";
	private static final String OUTLINE_COLOR_LABEL_TEXT = "Outline Color:";
	private static final String BACKGROUND_COLOR_LABEL_TEXT = "Background Color:";
	private static final String FONT_FAMILY_LABEL_TEXT = "Font Family:";
	private static final String FONT_STYLE_LABEL_TEXT = "Font Style:";
	private static final String FONT_SIZE_LABEL_TEXT = "Font Size:";

	private static final String TEXT_COLOR_HINT = "The color of the text in the tooltip.";
	private static final String OUTLINE_COLOR_HINT = "The color of the outlining border of the tooltip.";
	private static final String BACKGROUND_COLOR_HINT = "The color of the background of the tooltip.";
	private static final String FONT_FAMILY_HINT = "The tooltip text font family.";
	private static final String FONT_STYLE_HINT = "The tooltip text font style.";
	private static final String FONT_SIZE_HINT = "The tooltip text font size.";

	private static final String TOOLTIP_STYLE_CONTROLLER_NULL = "TooltipStyleController is null.";

	private final StyleColoredButton textColor;
	private final StyleColoredButton backgroundColor;
	private final StyleColoredButton outlineColor;

	private final JComboBox fontFamily;
	private final JComboBox fontStyle;

	private final JTextField fontSize;

	private final TooltipStyleController controller;

	/**
	 * Create the panel.
	 */
	public TooltipPanel(TooltipStyleController controller)
	{
		super();

		if (controller == null)
			throw new IllegalArgumentException(TOOLTIP_STYLE_CONTROLLER_NULL);

		this.controller = controller;

		// Layout
		setBorder(new EmptyBorder(10, 10, 10, 10));
		FormLayout formLayout = new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("center:default"), FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(165dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, },
				new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("15dlu"),
						FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"),
						FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"),
						FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"),
						FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, });
		formLayout.setRowGroups(new int[][] { new int[] { 8, 6, 4, 2, 12, 10 } });

		JPanel inner = new JPanel();
		inner.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), TOOLTIP_STYLE_TEXT,
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		add(inner, BorderLayout.NORTH);

		inner.setLayout(formLayout);

		// TEXT COLOR
		JPanel panel_4 = new JPanel();
		inner.add(panel_4, "2, 2, fill, fill");
		panel_4.setLayout(new BorderLayout(0, 0));

		JLabel label_1 = new JLabel(TEXT_COLOR_LABEL_TEXT);
		panel_4.add(label_1, BorderLayout.EAST);

		JPanel panel = new JPanel();
		inner.add(panel, "6, 2, fill, fill");
		panel.setLayout(new BorderLayout(0, 0));

		// text color button
		this.textColor = new StyleColoredButton();
		panel.add(this.textColor, BorderLayout.WEST);
		this.textColor.setActionCommand(TooltipPanel.TEXT_COLOR);
		this.textColor.setToolTipText(TooltipPanel.TEXT_COLOR);
		this.textColor.addActionListener(this);

		// hint
		inner.add(new HintLabel(TEXT_COLOR_HINT), "10, 2");

		// OUTLINE COLOR
		JPanel panel_5 = new JPanel();
		inner.add(panel_5, "2, 4, fill, fill");
		panel_5.setLayout(new BorderLayout(0, 0));

		JLabel label_2 = new JLabel(OUTLINE_COLOR_LABEL_TEXT);
		panel_5.add(label_2, BorderLayout.EAST);

		JPanel panel_1 = new JPanel();
		inner.add(panel_1, "6, 4, fill, fill");
		panel_1.setLayout(new BorderLayout(0, 0));

		// outline color button
		this.outlineColor = new StyleColoredButton();
		panel_1.add(this.outlineColor, BorderLayout.WEST);
		this.outlineColor.setActionCommand(TooltipPanel.OUTLINE_COLOR);
		this.outlineColor.setToolTipText(TooltipPanel.OUTLINE_COLOR);
		this.outlineColor.addActionListener(this);

		// hint
		inner.add(new HintLabel(OUTLINE_COLOR_HINT), "10, 4");

		// BACKGROUND COLOR
		JPanel panel_6 = new JPanel();
		inner.add(panel_6, "2, 6, fill, fill");
		panel_6.setLayout(new BorderLayout(0, 0));

		JLabel label = new JLabel(BACKGROUND_COLOR_LABEL_TEXT);
		panel_6.add(label, BorderLayout.EAST);

		JPanel panel_2 = new JPanel();
		inner.add(panel_2, "6, 6, fill, fill");
		panel_2.setLayout(new BorderLayout(0, 0));

		// background color button
		this.backgroundColor = new StyleColoredButton();
		panel_2.add(this.backgroundColor, BorderLayout.WEST);
		this.backgroundColor.setActionCommand(TooltipPanel.BACKGROUND_COLOR);
		this.backgroundColor.setToolTipText(TooltipPanel.BACKGROUND_COLOR);
		this.backgroundColor.addActionListener(this);

		// hint
		inner.add(new HintLabel(BACKGROUND_COLOR_HINT), "10, 6");

		// FONT_FAMILY
		JPanel panel_7 = new JPanel();
		inner.add(panel_7, "2, 8, fill, fill");
		panel_7.setLayout(new BorderLayout(0, 0));

		JLabel label_3 = new JLabel(FONT_FAMILY_LABEL_TEXT);
		panel_7.add(label_3, BorderLayout.EAST);

		JPanel panel_3 = new JPanel();
		inner.add(panel_3, "6, 8, fill, fill");
		panel_3.setLayout(new GridLayout());

		// font family combo box
		this.fontFamily = new JComboBox(StyleEditorUtility.FONT_NAMES);
		this.fontFamily.addActionListener(this);
		panel_3.add(this.fontFamily);

		// hint
		inner.add(new HintLabel(FONT_FAMILY_HINT), "10, 8");

		// FONT STYLE
		JPanel panel_8 = new JPanel();
		inner.add(panel_8, "2, 10, fill, fill");
		panel_8.setLayout(new BorderLayout(0, 0));

		JLabel lblFontStyle = new JLabel(FONT_STYLE_LABEL_TEXT);
		panel_8.add(lblFontStyle, BorderLayout.EAST);

		JPanel panel_10 = new JPanel();
		inner.add(panel_10, "6, 10, fill, fill");
		panel_10.setLayout(new GridLayout());

		// font style combo box
		this.fontStyle = new JComboBox(StyleEditorUtility.FONT_STYLES);
		this.fontStyle.addActionListener(this);
		panel_10.add(this.fontStyle);

		// hint
		inner.add(new HintLabel(FONT_STYLE_HINT), "10, 10");

		// FONT SIZE
		JPanel panel_9 = new JPanel();
		inner.add(panel_9, "2, 12, fill, fill");
		panel_9.setLayout(new BorderLayout(0, 0));

		JLabel label_5 = new JLabel(FONT_SIZE_LABEL_TEXT);
		panel_9.add(label_5, BorderLayout.EAST);

		JPanel panel_11 = new JPanel();
		inner.add(panel_11, "6, 12, fill, fill");
		panel_11.setLayout(new GridLayout());

		// font size text field
		this.fontSize = new JTextField();
		this.fontSize.setColumns(10);
		panel_11.add(this.fontSize);

		// hint
		inner.add(new HintLabel(FONT_SIZE_HINT), "10, 12");

		this.update();
	}

	/**
	 * 
	 * @return The color of the text.
	 */
	private Paint getTextPaint()
	{
		return this.textColor.getPaint();
	}

	/**
	 * Sets the color of the text.
	 * 
	 * @param color
	 */
	private void setTextPaint(Paint p)
	{
		this.textColor.setPaint(p);
	}

	/**
	 * 
	 * @return The color of the outline.
	 */
	private Paint getOutlinePaint()
	{
		return this.outlineColor.getPaint();
	}

	/**
	 * Sets the color of the outline.
	 * 
	 * @param color
	 */
	private void setOutlinePaint(Paint p)
	{
		this.outlineColor.setPaint(p);
	}

	/**
	 * 
	 * @return The background color.
	 */
	private Paint getBackgroundPaint()
	{
		return this.backgroundColor.getPaint();
	}

	/**
	 * Sets the background color.
	 * 
	 * @param color
	 */
	private void setBackgroundPaint(Paint p)
	{
		this.backgroundColor.setPaint(p);
	}

	/**
	 * 
	 * @return A string of the font family.
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
	 * @return A string of the font style.
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
			throw new IllegalArgumentException("Tooltip Font Style is not a String");
		}

		return result;
	}

	/**
	 * Sets the font style.
	 * 
	 * @param font
	 */
	private void setFontStyle(String font)
	{
		this.fontStyle.setSelectedItem(font);
	}

	/**
	 * 
	 * @return The text of the font size field.
	 */
	private String getFontSizeText()
	{
		return this.fontSize.getText();
	}

	/**
	 * Sets the text of the font size field.
	 * 
	 * @param i
	 */
	private void setFontSizeText(int i)
	{
		this.fontSize.setText(i + "");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Color color;

		if (e.getActionCommand().equals(TooltipPanel.TEXT_COLOR))
		{
			color = StyleEditorUtility.showColorPicker(this, this.textColor.getBackground());

			if (color != null)
				this.textColor.setPaint(color);
		}
		else if (e.getActionCommand().equals(TooltipPanel.OUTLINE_COLOR))
		{
			color = StyleEditorUtility.showColorPicker(this, this.outlineColor.getBackground());

			if (color != null)
				this.outlineColor.setPaint(color);
		}
		else if (e.getActionCommand().equals(TooltipPanel.BACKGROUND_COLOR))
		{
			color = StyleEditorUtility.showColorPicker(this, this.backgroundColor.getBackground());

			if (color != null)
				this.backgroundColor.setPaint(color);
		}
	}

	@Override
	/**
	 * Updates the panel.
	 */
	public void update()
	{
		updateTextColor();
		updateOutlineColor();
		updateBackgroundColor();
		updateFont();
	}

	/**
	 * Updates the text color.
	 */
	private void updateTextColor()
	{
		Paint tempPaint = this.controller.getTextColor();

		setTextPaint(tempPaint);
	}

	/**
	 * Updates the outline color.
	 */
	private void updateOutlineColor()
	{
		Paint tempPaint = this.controller.getOutlineColor();

		setOutlinePaint(tempPaint);
	}

	/**
	 * Updates the background color.
	 */
	private void updateBackgroundColor()
	{
		Paint tempPaint = this.controller.getBackgroundColor();

		setBackgroundPaint(tempPaint);
	}

	/**
	 * Updates the font.
	 */
	private void updateFont()
	{
		Font font = this.controller.getFont();

		if (font != null)
		{
			setFontFamily(font.getFamily());

			if (font.isBold() && font.isItalic())
			{
				setFontStyle(StyleEditorUtility.BOLD_ITALIC);
			}
			else if (font.isBold())
			{
				setFontStyle(StyleEditorUtility.BOLD);
			}
			else if (font.isItalic())
			{
				setFontStyle(StyleEditorUtility.ITALIC);
			}
			else if (font.isPlain())
			{
				setFontStyle(StyleEditorUtility.PLAIN);
			}

			setFontSizeText(font.getSize());
		}

	}

	@Override
	/**
	 * Applies the style.
	 */
	protected void doApply()
	{
		applyTextColor();
		applyOutlineColor();
		applyBackgroundColor();
		applyFont();
	}

	/**
	 * Applies the text color.
	 */
	private void applyTextColor()
	{
		this.controller.setTextColor(getTextPaint());
	}

	/**
	 * Applies the outline color.
	 */
	private void applyOutlineColor()
	{
		this.controller.setOutlineColor(getOutlinePaint());
	}

	/**
	 * Applies the background color.
	 */
	private void applyBackgroundColor()
	{
		this.controller.setBackgroundColor(getBackgroundPaint());
	}

	/**
	 * Applies the font.
	 */
	private void applyFont()
	{
		try
		{
			Font f = StyleEditorUtility.createFont(getFontFamily(), getFontStyle(), getFontSizeText());
			this.controller.setFont(f);
		}
		catch (StyleEditorUtility.ConversionException e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage());

			this.fontSize.setText(this.controller.getFont().getSize() + "");
		}
	}
}
