package ca.corefacility.gview.map.gui.editor.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import ca.corefacility.gview.map.controllers.RulerStyleController;
import ca.corefacility.gview.map.effects.ShapeEffectRenderer;
import ca.corefacility.gview.map.gui.editor.StyleColoredButton;
import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;
import ca.corefacility.gview.map.gui.editor.StyleEditorUtility.ConversionException;
import ca.corefacility.gview.map.gui.hint.HintLabel;
import ca.corefacility.gview.style.items.LabelLocation;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * The ruler style panel.
 * 
 * @author Eric Marinier
 * 
 */
public class RulerPanel extends StylePanel implements ActionListener
{
	private static final long serialVersionUID = 1L; // requested by java

	private static final int THICKNESS_PROPORTION_SCALE = 100;
	private static final double THICKNESS_PROPORTION_MIN = 0.0;
	private static final double THICKNESS_PROPORTION_MAX = 1.0;

	private static final String MAJOR_TICK_COLOR = "Major Tick Color";
	private static final String MINOR_TICK_COLOR = "Minor Tick Color";
	private static final String LABEL_COLOR = "Label Color";
	private static final String LABEL_BACKGROUND_COLOR = "Label Background Color";

	private static final String RULER_STYLE_TEXT = "Ruler Style";

	private static final String MAJOR_TICK_COLOR_LABEL_TEXT = "Major Tick Color:";
	private static final String MINOR_TICK_COLOR_LABEL_TEXT = "Minor Tick Color:";
	private static final String LABEL_COLOR_LABEL_TEXT = "Label Color:";
	private static final String LABEL_BACKGROUND_COLOR_LABEL_TEXT = "Label Background Color:";
	private static final String LABEL_FONT_FAMILY_LABEL_TEXT = "Label Font Family:";
	private static final String LABEL_FONT_STYLE_LABEL_TEXT = "Label Font Style:";
	private static final String LABEL_FONT_SIZE_LABEL_TEXT = "Label Font Size:";
	private static final String MAJOR_TICK_LENGTH_LABEL_TEXT = "Major Tick Length:";
	private static final String MINOR_TICK_LENGTH_LABEL_TEXT = "Minor Tick Length:";
	private static final String TICK_DENSITY_LABEL_TEXT = "Tick Density:";
	private static final String TICK_THICKNESS_LABEL_TEXT = "Tick Thickness:";
	private static final String TICK_EFFECT = "Tick Effect:";
	private static final String RULER_PLACEMENT = "Label Placement:";

	private static final String MAJOR_TICK_COLOR_HINT = "The color of the major tick marks.";
	private static final String MINOR_TICK_COLOR_HINT = "The color of the minor tick marks.";
	private static final String LABEL_COLOR_HINT = "The color of the ruler label text.";
	private static final String LABEL_BACKGROUND_COLOR_HINT = "The background color of ruler labels.";
	private static final String LABEL_FONT_FAMILY_HINT = "The ruler label text font family.";
	private static final String LABEL_FONT_STYLE_HINT = "The ruler label text font style.";
	private static final String LABEL_FONT_SIZE_HINT = "The ruler label text font size.";
	private static final String MAJOR_TICK_LENGTH_HINT = "The length of the major tick marks.";
	private static final String MINOR_TICK_LENGTH_HINT = "The length of the minor tick marks.";
	private static final String TICK_DENSITY_HINT = "The density of the tick marks; how sparse the tick marks are.";
	private static final String TICK_THICKNESS_HINT = "The thickness of the ruler tick marks.";
	private static final String TICK_EFFECT_HINT = "The visual effect to apply to the tick marks.";
	private static final String LABEL_PLACEMENT_HINT = "The placement of the ruler labels relative to the ruler.";

	private static final String INVALID_TICK_THICKNESS = "Invalid tick thickness.";
	private static final String INVALID_TICK_DENSITY = "Invalid tick density.";
	private static final String INVALID_MINOR_TICK_LENGTH = "Invalid minor tick length.";
	private static final String INVALID_MAJOR_TICK_LENGTH = "Invalid major tick length.";
	private static final String INVALID_FONT_SIZE = "Invalid font size.";
	private static final String RULER_STYLE_CONTROLLER_NULL = "RulerStyleController is null.";

	private final StyleColoredButton majorTickColor;
	private final StyleColoredButton minorTickColor;
	private final StyleColoredButton labelColor;
	private final StyleColoredButton labelBackgroundColor;

	private final JTextField majorTickLength;
	private final JTextField minorTickLength;
	private final JTextField tickThickness;
	private final JTextField fontSize;

	private final JSlider tickDensity;

	private final JComboBox labelFontFamily;
	private final JComboBox labelFontStyle;
	private final JComboBox featureEffect;
	private final JComboBox rulerPlacement;

	private final RulerStyleController controller;

	/**
	 * Create the panel.
	 */
	public RulerPanel(RulerStyleController controller)
	{
		super();

		if (controller == null)
			throw new IllegalArgumentException(RULER_STYLE_CONTROLLER_NULL);

		this.controller = controller;

		// Layout
		setBorder(new EmptyBorder(10, 10, 10, 10));
		FormLayout formLayout = new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("center:default"), FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(150dlu;min)"), FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("15dlu"), FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, });
		formLayout.setRowGroups(new int[][] { new int[] { 2, 4, 6, 8, 10, 12, 14, 16, 18, 22, 24, 26 } });

		JPanel inner = new JPanel();
		inner.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), RULER_STYLE_TEXT,
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		add(inner, BorderLayout.NORTH);

		inner.setLayout(formLayout);

		// MAJOR TICK COLOR
		JPanel panel_4 = new JPanel();
		inner.add(panel_4, "2, 2, fill, fill");
		panel_4.setLayout(new BorderLayout(0, 0));

		JLabel label_1 = new JLabel(MAJOR_TICK_COLOR_LABEL_TEXT);
		panel_4.add(label_1, BorderLayout.EAST);

		JPanel panel = new JPanel();
		inner.add(panel, "6, 2, fill, fill");
		panel.setLayout(new BorderLayout(0, 0));

		// major tick color button
		this.majorTickColor = new StyleColoredButton();
		panel.add(this.majorTickColor, BorderLayout.WEST);
		this.majorTickColor.setActionCommand(RulerPanel.MAJOR_TICK_COLOR);
		this.majorTickColor.setToolTipText(RulerPanel.MAJOR_TICK_COLOR);
		this.majorTickColor.addActionListener(this);

		// hint
		inner.add(new HintLabel(MAJOR_TICK_COLOR_HINT), "10, 2");

		// MINOR TICK COLOR
		JPanel panel_5 = new JPanel();
		inner.add(panel_5, "2, 4, fill, fill");
		panel_5.setLayout(new BorderLayout(0, 0));

		JLabel label_2 = new JLabel(MINOR_TICK_COLOR_LABEL_TEXT);
		panel_5.add(label_2, BorderLayout.EAST);

		JPanel panel_1 = new JPanel();
		inner.add(panel_1, "6, 4, fill, fill");
		panel_1.setLayout(new BorderLayout(0, 0));

		// minor tick color button
		this.minorTickColor = new StyleColoredButton();
		panel_1.add(this.minorTickColor, BorderLayout.WEST);
		this.minorTickColor.setActionCommand(RulerPanel.MINOR_TICK_COLOR);
		this.minorTickColor.setToolTipText(RulerPanel.MINOR_TICK_COLOR);
		this.minorTickColor.addActionListener(this);

		// hint
		inner.add(new HintLabel(MINOR_TICK_COLOR_HINT), "10, 4");

		// LABEL COLOR
		JPanel panel_6 = new JPanel();
		inner.add(panel_6, "2, 6, fill, fill");
		panel_6.setLayout(new BorderLayout(0, 0));

		JLabel label = new JLabel(LABEL_COLOR_LABEL_TEXT);
		panel_6.add(label, BorderLayout.EAST);

		JPanel panel_2 = new JPanel();
		inner.add(panel_2, "6, 6, fill, fill");
		panel_2.setLayout(new BorderLayout(0, 0));

		// labal color button
		this.labelColor = new StyleColoredButton();
		panel_2.add(this.labelColor, BorderLayout.WEST);
		this.labelColor.setActionCommand(RulerPanel.LABEL_COLOR);
		this.labelColor.setToolTipText(RulerPanel.LABEL_COLOR);
		this.labelColor.addActionListener(this);

		// hint
		inner.add(new HintLabel(LABEL_COLOR_HINT), "10, 6");

		// LABEL BACKGROUND COLOR
		JPanel panel_7 = new JPanel();
		inner.add(panel_7, "2, 8, fill, fill");
		panel_7.setLayout(new BorderLayout(0, 0));

		JLabel label_3 = new JLabel(LABEL_BACKGROUND_COLOR_LABEL_TEXT);
		panel_7.add(label_3, BorderLayout.EAST);

		JPanel panel_3 = new JPanel();
		inner.add(panel_3, "6, 8, fill, fill");
		panel_3.setLayout(new BorderLayout(0, 0));

		// label background color button
		this.labelBackgroundColor = new StyleColoredButton();
		panel_3.add(this.labelBackgroundColor, BorderLayout.WEST);
		this.labelBackgroundColor.setActionCommand(RulerPanel.LABEL_BACKGROUND_COLOR);
		this.labelBackgroundColor.setToolTipText(RulerPanel.LABEL_BACKGROUND_COLOR);
		this.labelBackgroundColor.addActionListener(this);

		// hint
		inner.add(new HintLabel(LABEL_BACKGROUND_COLOR_HINT), "10, 8");

		// LABEL FONT FAMILY
		JPanel panel_8 = new JPanel();
		inner.add(panel_8, "2, 10, fill, fill");
		panel_8.setLayout(new BorderLayout(0, 0));

		JLabel label_4 = new JLabel(LABEL_FONT_FAMILY_LABEL_TEXT);
		panel_8.add(label_4, BorderLayout.EAST);

		JPanel panel_9 = new JPanel();
		inner.add(panel_9, "6, 10, fill, fill");
		panel_9.setLayout(new GridLayout());

		// label font family combo box
		this.labelFontFamily = new JComboBox(StyleEditorUtility.FONT_NAMES);
		this.labelFontFamily.addActionListener(this);
		panel_9.add(this.labelFontFamily);

		// hint
		inner.add(new HintLabel(LABEL_FONT_FAMILY_HINT), "10, 10");

		// LABEL FONT STYLE
		JPanel panel_23 = new JPanel();
		inner.add(panel_23, "2, 12, fill, fill");
		panel_23.setLayout(new BorderLayout(0, 0));

		JLabel lblLabelFontStyle = new JLabel(LABEL_FONT_STYLE_LABEL_TEXT);
		panel_23.add(lblLabelFontStyle, BorderLayout.EAST);

		JPanel panel_24 = new JPanel();
		inner.add(panel_24, "6, 12, fill, fill");
		panel_24.setLayout(new GridLayout());

		// label font style combo box
		this.labelFontStyle = new JComboBox(StyleEditorUtility.FONT_STYLES);
		this.labelFontStyle.addActionListener(this);
		panel_24.add(this.labelFontStyle);

		// hint
		inner.add(new HintLabel(LABEL_FONT_STYLE_HINT), "10, 12");

		// LABEL FONT SIZE
		JPanel panel_16 = new JPanel();
		inner.add(panel_16, "2, 14, fill, fill");
		panel_16.setLayout(new BorderLayout(0, 0));

		JLabel lblLabelFontSize = new JLabel(LABEL_FONT_SIZE_LABEL_TEXT);
		panel_16.add(lblLabelFontSize, BorderLayout.EAST);

		JPanel panel_25 = new JPanel();
		inner.add(panel_25, "6, 14, fill, fill");
		panel_25.setLayout(new GridLayout());

		// font size text field
		this.fontSize = new JTextField();
		this.fontSize.setColumns(10);
		panel_25.add(this.fontSize);

		// hint
		inner.add(new HintLabel(LABEL_FONT_SIZE_HINT), "10, 14");

		// MAJOR TICK LENGTH
		JPanel panel_17 = new JPanel();
		inner.add(panel_17, "2, 16, fill, fill");
		panel_17.setLayout(new BorderLayout(0, 0));

		JLabel label_5 = new JLabel(MAJOR_TICK_LENGTH_LABEL_TEXT);
		panel_17.add(label_5, BorderLayout.EAST);

		JPanel panel_10 = new JPanel();
		inner.add(panel_10, "6, 16, fill, fill");
		panel_10.setLayout(new GridLayout());

		// major tick length text field
		this.majorTickLength = new JTextField();
		this.majorTickLength.setColumns(10);
		panel_10.add(this.majorTickLength);

		// hint
		inner.add(new HintLabel(MAJOR_TICK_LENGTH_HINT), "10, 16");

		// MINOR TICK LENGTH
		JPanel panel_18 = new JPanel();
		inner.add(panel_18, "2, 18, fill, fill");
		panel_18.setLayout(new BorderLayout(0, 0));

		JLabel label_6 = new JLabel(MINOR_TICK_LENGTH_LABEL_TEXT);
		panel_18.add(label_6, BorderLayout.EAST);

		JPanel panel_11 = new JPanel();
		inner.add(panel_11, "6, 18, fill, fill");
		panel_11.setLayout(new GridLayout());

		// minor tick length text field
		this.minorTickLength = new JTextField();
		this.minorTickLength.setColumns(10);
		panel_11.add(this.minorTickLength);

		// hint
		inner.add(new HintLabel(MINOR_TICK_LENGTH_HINT), "10, 18");

		// TICK DENSITY
		JPanel panel_19 = new JPanel();
		inner.add(panel_19, "2, 20, fill, fill");
		panel_19.setLayout(new BorderLayout(0, 0));

		JLabel label_7 = new JLabel(TICK_DENSITY_LABEL_TEXT);
		panel_19.add(label_7, BorderLayout.EAST);

		JPanel panel_12 = new JPanel();
		inner.add(panel_12, "6, 20, fill, fill");
		panel_12.setLayout(new GridLayout());

		// tick density slider
		this.tickDensity = new JSlider(JSlider.HORIZONTAL, (int) THICKNESS_PROPORTION_MIN * THICKNESS_PROPORTION_SCALE,
				(int) (THICKNESS_PROPORTION_MAX * THICKNESS_PROPORTION_SCALE),
				(int) (THICKNESS_PROPORTION_MAX * THICKNESS_PROPORTION_SCALE));
		this.tickDensity
				.setMajorTickSpacing((int) ((THICKNESS_PROPORTION_MAX * THICKNESS_PROPORTION_SCALE - THICKNESS_PROPORTION_MIN
						* THICKNESS_PROPORTION_SCALE) / 10));
		this.tickDensity.setPaintTicks(true);
		this.tickDensity.setSnapToTicks(true);
		panel_12.add(this.tickDensity);

		// Mouse wheel listener for slider:
		this.tickDensity.addMouseWheelListener(new MouseWheelListener()
		{
			@Override
			public void mouseWheelMoved(MouseWheelEvent e)
			{
				tickDensity.setValue(tickDensity.getValue() - e.getWheelRotation() * tickDensity.getMajorTickSpacing());
			}
		});

		// hint
		inner.add(new HintLabel(TICK_DENSITY_HINT), "10, 20");

		// TICK THICKNESS
		JPanel panel_20 = new JPanel();
		inner.add(panel_20, "2, 22, fill, fill");
		panel_20.setLayout(new BorderLayout(0, 0));

		JLabel label_8 = new JLabel(TICK_THICKNESS_LABEL_TEXT);
		panel_20.add(label_8, BorderLayout.EAST);

		JPanel panel_13 = new JPanel();
		inner.add(panel_13, "6, 22, fill, fill");
		panel_13.setLayout(new GridLayout());

		// tick thickness text field
		this.tickThickness = new JTextField();
		this.tickThickness.setColumns(10);
		panel_13.add(this.tickThickness);

		// hint
		inner.add(new HintLabel(TICK_THICKNESS_HINT), "10, 22");

		// TICK EFFECT
		// Feature Effect.. (ShapeEffectRenderer)
		JPanel panel_22 = new JPanel();
		inner.add(panel_22, "2, 24, fill, fill");
		panel_22.setLayout(new BorderLayout(0, 0));

		JLabel label_10 = new JLabel(TICK_EFFECT);
		panel_22.add(label_10, BorderLayout.EAST);

		JPanel panel_15 = new JPanel();
		inner.add(panel_15, "6, 24, fill, fill");
		panel_15.setLayout(new GridLayout());

		// feature effect combo box
		this.featureEffect = new JComboBox(StyleEditorUtility.featureEffects);
		this.featureEffect.addActionListener(this);
		panel_15.add(this.featureEffect);

		// hint
		inner.add(new HintLabel(TICK_EFFECT_HINT), "10, 24");

		// RULER LABEL PLACEMENT
		JPanel panel_26 = new JPanel();
		inner.add(panel_26, "2, 26, fill, fill");
		panel_26.setLayout(new BorderLayout(0, 0));

		JLabel label_11 = new JLabel(RULER_PLACEMENT);
		panel_26.add(label_11, BorderLayout.EAST);

		JPanel panel_27 = new JPanel();
		inner.add(panel_27, "6, 26, fill, fill");
		panel_27.setLayout(new GridLayout());

		// Ruler placement combo box
		this.rulerPlacement = new JComboBox(StyleEditorUtility.rulerPlacements);
		this.rulerPlacement.addActionListener(this);
		panel_27.add(this.rulerPlacement);

		// hint
		inner.add(new HintLabel(LABEL_PLACEMENT_HINT), "10, 26");

		this.update();
	}

	/**
	 * 
	 * @return The color of the major ticks.
	 */
	private Paint getMajorTickPaint()
	{
		Paint p = this.majorTickColor.getPaint();

		return p;
	}

	/**
	 * Sets the color of the major ticks.
	 * 
	 * @param color
	 */
	private void setMajorTickPaint(Paint p)
	{
		this.majorTickColor.setPaint(p);
	}

	/**
	 * 
	 * @return The color of the minor ticks.
	 */
	private Paint getMinorTickPaint()
	{
		Paint p = this.minorTickColor.getPaint();

		return p;
	}

	/**
	 * Sets the color of the minor ticks.
	 * 
	 * @param color
	 */
	private void setMinorTickPaint(Paint p)
	{
		this.minorTickColor.setPaint(p);
	}

	/**
	 * 
	 * @return The color of the labels.
	 */
	private Paint getLabelPaint()
	{
		Paint p = this.labelColor.getPaint();

		return p;
	}

	/**
	 * Sets the color of the labels.
	 * 
	 * @param color
	 */
	private void setLabelPaint(Paint p)
	{
		this.labelColor.setPaint(p);
	}

	/**
	 * 
	 * @return The label background color.
	 */
	private Paint getLabelBackgroundPaint()
	{
		Paint p = this.labelBackgroundColor.getPaint();

		return p;
	}

	/**
	 * Sets the color of the labels' background.
	 * 
	 * @param color
	 */
	private void setLabelBackgroundPaint(Paint p)
	{
		this.labelBackgroundColor.setPaint(p);
	}

	/**
	 * 
	 * @return The text in the font size field.
	 */
	private String getLabelFontSizeText()
	{
		return this.fontSize.getText();
	}

	/**
	 * Sets the text field of the font size.
	 * 
	 * @param i
	 */
	private void setLabelFontSizeText(int i)
	{
		this.fontSize.setText(Integer.toString(i));
	}

	/**
	 * 
	 * @return The text of the major tick length field.
	 */
	private String getMajorTickLengthText()
	{
		return this.majorTickLength.getText();
	}

	private double getMajorTickLength()
	{
		return Double.parseDouble(getMajorTickLengthText());
	}

	/**
	 * Sets the text of the major tick length field.
	 * 
	 * @param d
	 */
	private void setMajorTickLengthText(double d)
	{
		this.majorTickLength.setText(d + "");
	}

	/**
	 * 
	 * @return The text of the minor tick length field.
	 */
	private String getMinorTickLengthText()
	{
		return this.minorTickLength.getText();
	}

	/**
	 * Sets the text of the minor tick length field.
	 * 
	 * @param d
	 */
	private void setMinorTickLengthText(double d)
	{
		this.minorTickLength.setText(d + "");
	}

	private double getMinorTickLength()
	{
		return Double.parseDouble(getMinorTickLengthText());
	}

	/**
	 * 
	 * @return The text of the tick density field.
	 */
	private double getTickDensityText()
	{
		return ((double) this.tickDensity.getValue() / (double) THICKNESS_PROPORTION_SCALE);
	}

	private float getTickDensity()
	{
		return (float) (getTickDensityText());
	}

	/**
	 * Sets the tick density GUI element.
	 * 
	 * @param f
	 */
	private void setTickDensityText(float f)
	{
		this.tickDensity.setValue((int) (f * THICKNESS_PROPORTION_SCALE));
	}

	/**
	 * 
	 * @return The text in the tick thickness field.
	 */
	private String getTickThicknessText()
	{
		return this.tickThickness.getText();
	}

	private double getTickThickness()
	{
		return Double.parseDouble(getTickThicknessText());
	}

	/**
	 * Sets the text in the tick thickness field.
	 * 
	 * @param d
	 */
	private void setTickThicknessText(double d)
	{
		this.tickThickness.setText(d + "");
	}

	/**
	 * 
	 * @return The label font family selected in the combo box as a string.
	 */
	private String getLabelFontFamily()
	{
		Object temp;
		String result;

		temp = this.labelFontFamily.getSelectedItem();

		if (temp instanceof String)
		{
			result = (String) temp;
		}
		else
		{
			throw new IllegalArgumentException("Label Font is not a String");
		}

		return result;
	}

	/**
	 * Sets the font family of the labels.
	 * 
	 * @param font
	 */
	private void setLabelFontFamily(String font)
	{
		this.labelFontFamily.setSelectedItem(font);
	}

	/**
	 * 
	 * @return The selected label font style as a string.
	 */
	private String getLabelFontStyle()
	{
		Object temp;
		String result;

		temp = this.labelFontStyle.getSelectedItem();

		if (temp instanceof String)
		{
			result = (String) temp;
		}
		else
		{
			throw new IllegalArgumentException("Label Font Style is not a String");
		}

		return result;
	}

	/**
	 * Sets the label font style.
	 * 
	 * @param font
	 */
	private void setLabelFontStyle(String font)
	{
		this.labelFontStyle.setSelectedItem(font);
	}

	/**
	 * 
	 * @return The selection of the feature effect combo box.
	 */
	private String getFeatureEffect()
	{
		Object temp;
		String result;

		temp = this.featureEffect.getSelectedItem();

		if (temp instanceof String)
		{
			result = (String) temp;
		}
		else
		{
			throw new IllegalArgumentException("Feature Effect is not a String");
		}

		return result;
	}

	/**
	 * Sets the selection of the feature effect combo box.
	 * 
	 * @param effect
	 */
	private void setFeatureEffect(String effect)
	{
		this.featureEffect.setSelectedItem(effect);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Color color;

		if (e.getActionCommand().equals(RulerPanel.MAJOR_TICK_COLOR))
		{
			color = StyleEditorUtility.showColorPicker(this, this.majorTickColor.getBackground());

			if (color != null)
				this.majorTickColor.setPaint(color);
		}
		else if (e.getActionCommand().equals(RulerPanel.MINOR_TICK_COLOR))
		{
			color = StyleEditorUtility.showColorPicker(this, this.minorTickColor.getBackground());

			if (color != null)
				this.minorTickColor.setPaint(color);
		}
		else if (e.getActionCommand().equals(RulerPanel.LABEL_COLOR))
		{
			color = StyleEditorUtility.showColorPicker(this, this.labelColor.getBackground());

			if (color != null)
				this.labelColor.setPaint(color);
		}
		else if (e.getActionCommand().equals(RulerPanel.LABEL_BACKGROUND_COLOR))
		{
			color = StyleEditorUtility.showColorPicker(this, this.labelBackgroundColor.getBackground());

			if (color != null)
				this.labelBackgroundColor.setPaint(color);
		}
	}

	@Override
	/**
	 * Updates the panel.
	 */
	public void update()
	{
		updateMajorTickPaint();
		updateMinorTickPaint();
		updateLabelPaint();
		updateLabelBackgroundPaint();
		updateLabelFont();
		updateMajorTickLength();
		updateMinorTickLength();
		updateTickDensity();
		updateTickThickness();
		updateFeatureEffect();
		updateLabelPlacement();
	}

	/**
	 * Updates the major tick color.
	 */
	private void updateMajorTickPaint()
	{
		Paint tempPaint = this.controller.getMajorTickColor();

		setMajorTickPaint(tempPaint);
	}

	/**
	 * Updates the minor tick color.
	 */
	private void updateMinorTickPaint()
	{
		Paint tempPaint = this.controller.getMinorTickColor();

		setMinorTickPaint(tempPaint);
	}

	/**
	 * Updates the label color.
	 */
	private void updateLabelPaint()
	{
		Paint tempPaint = this.controller.getTextColor();

		setLabelPaint(tempPaint);

	}

	/**
	 * Updates the label background color.
	 */
	private void updateLabelBackgroundPaint()
	{
		Paint tempPaint = this.controller.getTextBackgroundColor();

		setLabelBackgroundPaint(tempPaint);
	}

	/**
	 * Upates the label font.
	 */
	private void updateLabelFont()
	{
		Font font = this.controller.getFont();

		if (font != null)
		{
			setLabelFontFamily(font.getFamily());

			if (font.isBold() && font.isItalic())
			{
				setLabelFontStyle(StyleEditorUtility.BOLD_ITALIC);
			}
			else if (font.isBold())
			{
				setLabelFontStyle(StyleEditorUtility.BOLD);
			}
			else if (font.isItalic())
			{
				setLabelFontStyle(StyleEditorUtility.ITALIC);
			}
			else if (font.isPlain())
			{
				setLabelFontStyle(StyleEditorUtility.PLAIN);
			}

			setLabelFontSizeText(font.getSize());
		}
	}

	/**
	 * Updates the major tick length.
	 */
	private void updateMajorTickLength()
	{
		setMajorTickLengthText(this.controller.getMajorTickLength());
	}

	/**
	 * Updates the minor tick length.
	 */
	private void updateMinorTickLength()
	{
		setMinorTickLengthText(this.controller.getMinorTickLength());
	}

	/**
	 * Updates the tick density.
	 */
	private void updateTickDensity()
	{
		setTickDensityText(this.controller.getTickDensity());
	}

	/**
	 * Updates the tick thickness.
	 */
	private void updateTickThickness()
	{
		setTickThicknessText(this.controller.getTickThickness());
	}

	/**
	 * Updates the feature effect.
	 */
	private void updateFeatureEffect()
	{
		ShapeEffectRenderer effectRenderer = this.controller.getShapeEffectRenderer();

		String effect = StyleEditorUtility.getFeatureEffectRenderer(effectRenderer);
		setFeatureEffect(effect);
	}

	private void updateLabelPlacement()
	{
		LabelLocation location = this.controller.getRulerLabelLocation();

		if (location == LabelLocation.ABOVE_BACKBONE)
		{
			this.rulerPlacement.setSelectedItem(StyleEditorUtility.LABELS_ABOVE);
		}
		else if (location == LabelLocation.BELOW_BACKBONE)
		{
			this.rulerPlacement.setSelectedItem(StyleEditorUtility.LABELS_BELOW);
		}
		else if (location == LabelLocation.BOTH)
		{
			this.rulerPlacement.setSelectedItem(StyleEditorUtility.LABELS_BOTH);
		}
		else if (location == LabelLocation.NONE)
		{
			this.rulerPlacement.setSelectedItem(StyleEditorUtility.LABELS_NONE);
		}
		else
		{
			System.out.println("WARNING: UNKNOWN LABEL PLACEMENT LOCATION!");
		}
	}

	@Override
	/**
	 * Applies the style.
	 */
	protected void doApply()
	{
		applyMajorTickColor();
		applyMinorTickColor();
		applyLabelColor();
		applyLabelBackgroundColor();
		applyLabelFont();
		applyMajorTickLength();
		applyMinorTickLength();
		applyTickDensity();
		applyTickThickness();
		applyFeatureEffect();
		applyLabelPlacement();
	}

	/**
	 * Applies the major tick color.
	 */
	private void applyMajorTickColor()
	{
		this.controller.setMajorTickColor(getMajorTickPaint());
	}

	/**
	 * Applies the minor tick color.
	 */
	private void applyMinorTickColor()
	{
		this.controller.setMinorTickColor(getMinorTickPaint());
	}

	/**
	 * Applies the label color.
	 */
	private void applyLabelColor()
	{
		this.controller.setTextColor(getLabelPaint());
	}

	/**
	 * Applies the label background color.
	 */
	private void applyLabelBackgroundColor()
	{
		this.controller.setTextBackgroundColor(getLabelBackgroundPaint());
	}

	/**
	 * Applies the label font.
	 */
	private void applyLabelFont()
	{
		int tempFontStyle = 0;
		int tempInt;

		if (StyleEditorUtility.BOLD_ITALIC.equals(getLabelFontStyle()))
		{
			tempFontStyle = Font.BOLD | Font.ITALIC;
		}
		else if (StyleEditorUtility.BOLD.equals(getLabelFontStyle()))
		{
			tempFontStyle = Font.BOLD;
		}
		else if (StyleEditorUtility.ITALIC.equals(getLabelFontStyle()))
		{
			tempFontStyle = Font.ITALIC;
		}
		else if (StyleEditorUtility.PLAIN.equals(getLabelFontStyle()))
		{
			tempFontStyle = Font.PLAIN;
		}

		// font size
		try
		{
			tempInt = Integer.parseInt(getLabelFontSizeText());

			if (tempInt <= 0)
			{
				JOptionPane.showMessageDialog(this, "Font Size value must be greater than zero.");
			}
			else
			{
				this.controller.setFont(new Font(getLabelFontFamily(), tempFontStyle, tempInt));
			}
		}
		catch (NumberFormatException e)
		{
			JOptionPane.showMessageDialog(this, INVALID_FONT_SIZE);
		}
	}

	/**
	 * Applies the major tick length.
	 */
	private void applyMajorTickLength()
	{
		double tempDouble;

		try
		{
			tempDouble = getMajorTickLength();

			if (tempDouble < 0)
			{
				JOptionPane.showMessageDialog(this, "Major Tick Length value must be non-negative.");
			}
			else
			{
				this.controller.setMajorTickLength(tempDouble);
			}
		}
		catch (NumberFormatException e)
		{
			JOptionPane.showMessageDialog(this, INVALID_MAJOR_TICK_LENGTH);
		}
	}

	/**
	 * Applies the minor tick length.
	 */
	private void applyMinorTickLength()
	{
		double tempDouble;

		try
		{
			tempDouble = getMinorTickLength();

			if (tempDouble < 0)
			{
				JOptionPane.showMessageDialog(this, "Minor Tick Length value must be non-negative.");
			}
			else
			{
				this.controller.setMinorTickLength(tempDouble);
			}
		}
		catch (NumberFormatException e)
		{
			JOptionPane.showMessageDialog(this, INVALID_MINOR_TICK_LENGTH);
		}
	}

	/**
	 * Applies the tick density.
	 */
	private void applyTickDensity()
	{
		float tempFloat;

		try
		{
			tempFloat = getTickDensity();

			if (tempFloat < 0)
			{
				JOptionPane.showMessageDialog(this, "Tick Density value must be non-negative.");
			}
			else
			{
				this.controller.setTickDensity(tempFloat);
			}
		}
		catch (NumberFormatException e)
		{
			JOptionPane.showMessageDialog(this, INVALID_TICK_DENSITY);
		}
	}

	/**
	 * Applies the tick thickness.
	 */
	private void applyTickThickness()
	{
		double tempDouble;

		try
		{
			tempDouble = getTickThickness();

			if (tempDouble < 0)
			{
				JOptionPane.showMessageDialog(this, "Tick Thickness value must be non-negative.");
			}
			else
			{
				this.controller.setTickThickness(tempDouble);
			}
		}
		catch (NumberFormatException e)
		{
			JOptionPane.showMessageDialog(this, INVALID_TICK_THICKNESS);
		}
	}

	/**
	 * Applies the ruler placement.
	 */
	private void applyLabelPlacement()
	{
		LabelLocation location;

		if (this.rulerPlacement.getSelectedItem().equals(StyleEditorUtility.LABELS_ABOVE))
		{
			location = LabelLocation.ABOVE_BACKBONE;

			this.controller.setRulerLabelLocation(location);
		}
		else if (this.rulerPlacement.getSelectedItem().equals(StyleEditorUtility.LABELS_BELOW))
		{
			location = LabelLocation.BELOW_BACKBONE;

			this.controller.setRulerLabelLocation(location);
		}
		else if (this.rulerPlacement.getSelectedItem().equals(StyleEditorUtility.LABELS_BOTH))
		{
			location = LabelLocation.BOTH;

			this.controller.setRulerLabelLocation(location);
		}
		else if (this.rulerPlacement.getSelectedItem().equals(StyleEditorUtility.LABELS_NONE))
		{
			location = LabelLocation.NONE;

			this.controller.setRulerLabelLocation(location);
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Invalid ruler placement location.");

			this.updateLabelPlacement();
		}
	}

	/**
	 * Applies the feature effect.
	 */
	private void applyFeatureEffect()
	{
		ShapeEffectRenderer shapeEffect;

		try
		{
			shapeEffect = StyleEditorUtility.getFeatureEffectRenderer(getFeatureEffect());
			this.controller.setShapeEffectRenderer(shapeEffect);
		}
		catch (ConversionException e)
		{
			e.printStackTrace();
		}
	}
}
