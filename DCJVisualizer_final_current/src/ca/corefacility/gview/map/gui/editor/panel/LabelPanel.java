package ca.corefacility.gview.map.gui.editor.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
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

import ca.corefacility.gview.map.controllers.FeatureHolderStyleToken;
import ca.corefacility.gview.map.controllers.GenomeDataController;
import ca.corefacility.gview.map.controllers.LabelStyleController;
import ca.corefacility.gview.map.controllers.LabelStyleToken;
import ca.corefacility.gview.map.controllers.SetStyleController;
import ca.corefacility.gview.map.controllers.selection.SelectionController;
import ca.corefacility.gview.map.gui.editor.AnnotationComboBox;
import ca.corefacility.gview.map.gui.editor.StyleColoredButton;
import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;
import ca.corefacility.gview.map.gui.editor.TriStateCheckBox;
import ca.corefacility.gview.map.gui.editor.communication.GUIEvent;
import ca.corefacility.gview.map.gui.editor.communication.SelectionEventBroadcaster;
import ca.corefacility.gview.map.gui.editor.communication.labelEvent.LabelBackgroundColorEvent;
import ca.corefacility.gview.map.gui.editor.communication.labelEvent.LabelFontFamilyEvent;
import ca.corefacility.gview.map.gui.editor.communication.labelEvent.LabelFontSizeEvent;
import ca.corefacility.gview.map.gui.editor.communication.labelEvent.LabelFontStyleEvent;
import ca.corefacility.gview.map.gui.editor.communication.labelEvent.LabelLockColorsEvent;
import ca.corefacility.gview.map.gui.editor.communication.labelEvent.LabelTextColorEvent;
import ca.corefacility.gview.map.gui.editor.communication.labelEvent.LabelTextEvent;
import ca.corefacility.gview.map.gui.editor.communication.labelEvent.LabelVisibleEvent;
import ca.corefacility.gview.map.gui.editor.panel.proxy.ProxiablePanel;
import ca.corefacility.gview.map.gui.editor.panel.proxy.ProxyLabelPanel;
import ca.corefacility.gview.map.gui.hint.HintLabel;
import ca.corefacility.gview.textextractor.AnnotationExtractor;
import ca.corefacility.gview.textextractor.BlankExtractor;
import ca.corefacility.gview.textextractor.FeatureTextExtractor;
import ca.corefacility.gview.textextractor.LocationExtractor;
import ca.corefacility.gview.textextractor.StringBuilder;
import ca.corefacility.gview.textextractor.SymbolsExtractor;
import ca.corefacility.gview.utils.Util;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * The panel for the label properties.
 * 
 * @author Eric Marinier
 * 
 */
public class LabelPanel extends StylePanel implements ActionListener, ItemListener, DocumentListener, ProxiablePanel
{
	private static final long serialVersionUID = 1L;

	private static final String TEXT_COLOR = "Text Color";
	private static final String BACKGROUND_COLOR = "Background Color";
	private static final String FONT_FAMILY = "Font Family";
	private static final String FONT_STYLE = "Font Style";
	private static final String LABEL_TEXT = "Label Text";
	private static final String ANNOTATION_TEXT = "Annotation Text";
	private static final String SHOW_LABELS = "Show Labels";
	private static final String LOCK_COLORS = "Lock Colors";

	private static final String LABEL_STYLE_TEXT = "Label Style";
	private static final String TEXT_COLOR_LABEL_TEXT = "Text Color:";
	private static final String BACKGROUND_COLOR_LABEL_TEXT = "Background Color:";
	private static final String FONT_FAMILY_LABEL_TEXT = "Font Family:";
	private static final String FONT_STYLE_LABEL_TEXT = "Font Style:";
	private static final String FONT_SIZE_LABEL_TEXT = "Font Size";
	private static final String LABEL_TEXT_LABEL_TEXT = "Label Text:"; // Yeah,
																		// I
																		// know..
	private static final String SHOW_LABELS_LABEL_TEXT = "Show Labels:";
	private static final String LOCK_COLORS_LABEL_TEXT = "Lock Colors:";

	private static final String TEXT_COLOR_HINT = "The color of the label text.";
	private static final String BACKGROUND_COLOR_HINT = "The background color of label boxes.";
	private static final String FONT_FAMILY_HINT = "The label text font family.";
	private static final String FONT_STYLE_HINT = "The label text font style.";
	private static final String FONT_SIZE_HINT = "The label text font size.";
	private static final String LABEL_TEXT_HINT = "The text to display in the label.";
	private static final String SHOW_LABELS_HINT = "Whether or not the labels are visible.";
	private static final String LOCK_COLORS_HINT = "Whether or not to lock the this label's color with its parent set's color.";

	private static final String[] LABEL_TEXTS = { StyleEditorUtility.ANNOTATION, StyleEditorUtility.LOCATION };

	private static final String LABEL_STYLE_TOKEN_NULL = "LabelStyleToken is null.";

	private final StyleColoredButton textColor = new StyleColoredButton();
	private final StyleColoredButton backgroundColor = new StyleColoredButton();;

	private final JComboBox fontFamily;
	private final JComboBox fontStyle;
	private final JComboBox labelText;
	private final AnnotationComboBox annotations;

	private final Box.Filler labelTextFiller;

	private final JTextField fontSize;

	private final TriStateCheckBox showLabels;
	private final TriStateCheckBox lockedColors;

	private final JLabel textColorLabel;

	private final Document fontSizeDocument;
	private boolean isDocumentUpdating = false;

	private final LabelStyleController labelStyleController;
	private final SetStyleController setStyleController;
	private final GenomeDataController genomeDataController;
	private final SelectionController selectionController;

	private final LabelStyleToken labelStyle;
	private final FeatureHolderStyleToken setStyle;

	private StringBuilder stringBuilder; // The string builder text extractor
											// that would be loaded from a GSS
											// file.

	/**
	 * Create the panel.
	 */
	public LabelPanel(LabelStyleToken labelStyle, FeatureHolderStyleToken setStyle,
			LabelStyleController labelStyleController, SetStyleController setStyleController,
			GenomeDataController genomeDataController, SelectionController selectionController)
	{
		super();

		if (labelStyle == null)
			throw new IllegalArgumentException(LABEL_STYLE_TOKEN_NULL);

		this.labelStyleController = labelStyleController;
		this.setStyleController = setStyleController;
		this.genomeDataController = genomeDataController;
		this.selectionController = selectionController;

		this.labelStyle = labelStyle;
		this.setStyle = setStyle;

		this.addGUIEventBroadcaster(new SelectionEventBroadcaster(selectionController));

		// Layout
		setBorder(new EmptyBorder(10, 10, 10, 10));
		FormLayout formLayout = new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("center:default"), FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(165dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, },
				new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("15dlu"),
						FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"),
						FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"),
						FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, });
		formLayout.setRowGroups(new int[][] { new int[] { 2, 4, 6, 8, 10, 12, 14, 16 } });

		JPanel inner = new JPanel();
		inner.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), LABEL_STYLE_TEXT,
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		add(inner, BorderLayout.NORTH);

		inner.setLayout(formLayout);

		// SHOW LABELS
		JPanel panel_8 = new JPanel();
		inner.add(panel_8, "2, 2, fill, fill");
		panel_8.setLayout(new BorderLayout(0, 0));

		JLabel lblColor = new JLabel(SHOW_LABELS_LABEL_TEXT);
		panel_8.add(lblColor, BorderLayout.EAST);

		JPanel panel_9 = new JPanel();
		inner.add(panel_9, "6, 2, fill, fill");
		panel_9.setLayout(new BorderLayout(0, 0));

		// Show Labels Check Box
		this.showLabels = new TriStateCheckBox();
		this.showLabels.setActionCommand(SHOW_LABELS);
		this.showLabels.addItemListener(this);
		panel_9.add(this.showLabels, BorderLayout.WEST);

		// hint
		inner.add(new HintLabel(SHOW_LABELS_HINT), "10, 2");

		// LOCK COLORS:
		JPanel panel_14 = new JPanel();
		inner.add(panel_14, "2, 4, fill, fill");
		panel_14.setLayout(new BorderLayout(0, 0));

		JLabel lblLockedColors = new JLabel(LOCK_COLORS_LABEL_TEXT);
		panel_14.add(lblLockedColors, BorderLayout.EAST);

		JPanel panel_15 = new JPanel();
		inner.add(panel_15, "6, 4, fill, fill");
		panel_15.setLayout(new BorderLayout(0, 0));

		// Check box:
		this.lockedColors = new TriStateCheckBox();
		panel_15.add(this.lockedColors, BorderLayout.WEST);
		this.lockedColors.setActionCommand(LOCK_COLORS);
		this.lockedColors.addItemListener(this);

		// hint
		inner.add(new HintLabel(LOCK_COLORS_HINT), "10, 4");

		// TEXT COLOR
		JPanel panel_4 = new JPanel();
		inner.add(panel_4, "2, 6, fill, fill");
		panel_4.setLayout(new BorderLayout(0, 0));

		this.textColorLabel = new JLabel(TEXT_COLOR_LABEL_TEXT);
		panel_4.add(this.textColorLabel, BorderLayout.EAST);

		JPanel panel = new JPanel();
		inner.add(panel, "6, 6, fill, fill");
		panel.setLayout(new BorderLayout(0, 0));

		// Text Color Button
		panel.add(this.textColor, BorderLayout.WEST);
		this.textColor.setActionCommand(TEXT_COLOR);
		this.textColor.addActionListener(this);

		// hint
		inner.add(new HintLabel(TEXT_COLOR_HINT), "10, 6");

		// BACKGROUND COLOR
		JPanel panel_5 = new JPanel();
		inner.add(panel_5, "2, 8, fill, fill");
		panel_5.setLayout(new BorderLayout(0, 0));

		JLabel lblBackgroundColorText = new JLabel(BACKGROUND_COLOR_LABEL_TEXT);
		panel_5.add(lblBackgroundColorText, BorderLayout.EAST);

		JPanel panel_1 = new JPanel();
		inner.add(panel_1, "6, 8, fill, fill");
		panel_1.setLayout(new BorderLayout(0, 0));

		// Background Color Button
		panel_1.add(backgroundColor, BorderLayout.WEST);
		this.backgroundColor.setActionCommand(BACKGROUND_COLOR);
		this.backgroundColor.addActionListener(this);

		// hint
		inner.add(new HintLabel(BACKGROUND_COLOR_HINT), "10, 8");

		// LABEL TEXT EXTRACTOR
		JPanel panel_7 = new JPanel();
		inner.add(panel_7, "2, 10, fill, fill");
		panel_7.setLayout(new BorderLayout(0, 0));

		JLabel lblFeatureEffect = new JLabel(LABEL_TEXT_LABEL_TEXT);
		panel_7.add(lblFeatureEffect, BorderLayout.EAST);

		JPanel panel_3 = new JPanel();
		inner.add(panel_3, "6, 10, fill, fill");
		panel_3.setLayout(new GridLayout());

		// Label Text (Extractor)
		JPanel labelTextPanel = new JPanel();
		labelTextPanel.setLayout(new BoxLayout(labelTextPanel, BoxLayout.LINE_AXIS));
		panel_3.add(labelTextPanel, BorderLayout.WEST);

		// Combo Box
		this.labelText = new JComboBox(LABEL_TEXTS);
		this.labelText.addActionListener(this);
		this.labelText.setActionCommand(LABEL_TEXT);
		labelTextPanel.add(this.labelText);

		this.labelTextFiller = new Box.Filler(new Dimension(2, 0), new Dimension(2, 0), new Dimension(2, 0));
		labelTextPanel.add(this.labelTextFiller);

		// Text Field
		this.annotations = new AnnotationComboBox(this.genomeDataController);
		this.annotations.setVisible(false);
		this.annotations.setActionCommand(ANNOTATION_TEXT);
		this.annotations.addActionListener(this);
		labelTextPanel.add(this.annotations);

		// hint
		inner.add(new HintLabel(LABEL_TEXT_HINT), "10, 10");

		// FONT FAMILY
		JPanel panel_6 = new JPanel();
		inner.add(panel_6, "2, 12, fill, fill");
		panel_6.setLayout(new BorderLayout(0, 0));

		JLabel lblFeatureShape = new JLabel(FONT_FAMILY_LABEL_TEXT);
		panel_6.add(lblFeatureShape, BorderLayout.EAST);

		JPanel panel_2 = new JPanel();
		inner.add(panel_2, "6, 12, fill, fill");
		panel_2.setLayout(new GridLayout());

		// Font Family Combo Box
		this.fontFamily = new JComboBox(StyleEditorUtility.FONT_NAMES);
		this.fontFamily.setActionCommand(FONT_FAMILY);
		this.fontFamily.addActionListener(this);
		panel_2.add(fontFamily);

		// hint
		inner.add(new HintLabel(FONT_FAMILY_HINT), "10, 12");

		// FONT STYLE
		JPanel panel_10 = new JPanel();
		inner.add(panel_10, "2, 14, fill, fill");
		panel_10.setLayout(new BorderLayout(0, 0));

		JLabel lblFontStyle = new JLabel(FONT_STYLE_LABEL_TEXT);
		panel_10.add(lblFontStyle, BorderLayout.EAST);

		JPanel panel_11 = new JPanel();
		inner.add(panel_11, "6, 14, fill, fill");
		panel_11.setLayout(new GridLayout());

		// Font Style Combo Box
		this.fontStyle = new JComboBox(StyleEditorUtility.FONT_STYLES);
		this.fontStyle.setActionCommand(FONT_STYLE);
		this.fontStyle.addActionListener(this);
		panel_11.add(fontStyle);

		// hint
		inner.add(new HintLabel(FONT_STYLE_HINT), "10, 14");

		// FONT SIZE
		JPanel panel_12 = new JPanel();
		inner.add(panel_12, "2, 16, fill, fill");
		panel_12.setLayout(new BorderLayout(0, 0));

		JLabel lblFontSize = new JLabel(FONT_SIZE_LABEL_TEXT);
		panel_12.add(lblFontSize, BorderLayout.EAST);

		JPanel panel_13 = new JPanel();
		inner.add(panel_13, "6, 16, fill, fill");
		panel_13.setLayout(new GridLayout());

		// font size text field
		this.fontSize = new JTextField();
		this.fontSize.setColumns(10);
		this.fontSize.getDocument().addDocumentListener(this);
		this.fontSizeDocument = this.fontSize.getDocument();
		panel_13.add(fontSize);

		// hint
		inner.add(new HintLabel(FONT_SIZE_HINT), "10, 16");

		this.update();
	}

	/**
	 * Applies GUI changes to create the proxy version of the panel.
	 */
	private void proxy()
	{
		this.setUpdating(true);

		this.showLabels.setSelected(false);
		this.showLabels.setMixed(true);

		this.lockedColors.setSelected(false);
		this.lockedColors.setMixed(true);

		this.textColor.setBackground(StyleEditorUtility.DEFAULT_COLOR);
		this.textColor.setNotify(true);
		this.textColor.setEnabled(false);

		this.backgroundColor.setBackground(StyleEditorUtility.DEFAULT_COLOR);
		this.backgroundColor.setNotify(true);

		this.labelText.addItem(StyleEditorUtility.PROXY_VALUE);
		this.labelText.setSelectedItem(StyleEditorUtility.PROXY_VALUE);

		this.fontFamily.addItem(StyleEditorUtility.PROXY_VALUE);
		this.fontFamily.setSelectedItem(StyleEditorUtility.PROXY_VALUE);

		this.fontStyle.addItem(StyleEditorUtility.PROXY_VALUE);
		this.fontStyle.setSelectedItem(StyleEditorUtility.PROXY_VALUE);

		this.fontSize.setText(null);

		validateLabelTextGUI();

		this.setUpdating(false);
	}

	@Override
	public LabelPanel createProxy()
	{
		ProxyLabelPanel panel = new ProxyLabelPanel(labelStyle, setStyle, labelStyleController, setStyleController,
				genomeDataController, selectionController);

		((LabelPanel) panel).proxy();

		return panel;
	}

	/**
	 * Gets the color of the label text.
	 * 
	 */
	private Paint getTextPaint()
	{
		Paint p = this.textColor.getPaint();

		return p;
	}

	/**
	 * Gets the color of the label background.
	 * 
	 */
	private Paint getBackgroundPaint()
	{
		Paint p = this.backgroundColor.getPaint();

		return p;
	}

	/**
	 * Gets the font family.
	 * 
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
	 * 
	 * @return The selected label font style as a string.
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
	 * 
	 * @return The text in the font size field.
	 */
	private String getFontSizeText()
	{
		return this.fontSize.getText();
	}

	/**
	 * Gets the text of the annotation combo box.
	 * 
	 */
	private String getLabelTextField()
	{
		return this.annotations.getSelectedString();
	}

	/**
	 * Gets the selection in the label text combo box.
	 */
	private String getLabelText()
	{
		Object temp;
		String result;

		temp = this.labelText.getSelectedItem();

		if (temp instanceof String)
		{
			result = (String) temp;
		}
		else
		{
			throw new IllegalArgumentException("Label Text is not a String");
		}

		return result;
	}

	/**
	 * 
	 * @return Whether or not show labels is selected.
	 */
	private boolean getShowLabels()
	{
		return this.showLabels.isSelected();
	}

	/**
	 * 
	 * @return Whether or not the lock colors option is selected.
	 */
	private boolean getLockedColors()
	{
		return this.lockedColors.isSelected();
	}

	/**
	 * Sets the color of the label text.
	 * 
	 * @param c
	 */
	private void setTextPaint(Paint p)
	{
		this.textColor.setPaint(p);
	}

	/**
	 * Sets the color of the label background.
	 * 
	 * @param c
	 */
	private void setBackgroundPaint(Paint p)
	{
		this.backgroundColor.setPaint(p);
	}

	/**
	 * Sets the font family.
	 * 
	 * @param font
	 */
	private void setFontFamily(String font)
	{
		this.fontFamily.setSelectedItem(font);
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
	 * Sets the text field of the font size.
	 * 
	 * @param i
	 */
	private void setFontSizeText(int i)
	{
		if (this.isDocumentUpdating)
		{
			return;
		}

		this.fontSize.setText(Integer.toString(i));
	}

	/**
	 * Sets the annotation.
	 * 
	 * @param annotation
	 */
	private void setLabelAnnotation(String annotation)
	{
		this.annotations.setSelectedItem(annotation);
	}

	/**
	 * Sets the selection in the label text combo box.
	 * 
	 * @param text
	 */
	private void setLabelText(FeatureTextExtractor textExtractor)
	{
		String text = null;

		if (textExtractor instanceof AnnotationExtractor)
		{
			text = StyleEditorUtility.ANNOTATION;

			// If the annotation doesn't already exist (for whatever reason):
			if (!this.annotations.containsItem(((AnnotationExtractor) textExtractor).getAnnotation()))
			{
				this.annotations.addAnnotation(((AnnotationExtractor) textExtractor).getAnnotation());
			}

			setLabelAnnotation(((AnnotationExtractor) textExtractor).getAnnotation());
		}
		else if (textExtractor instanceof LocationExtractor)
		{
			text = StyleEditorUtility.LOCATION;
		}
		else if (textExtractor instanceof SymbolsExtractor)
		{
			text = StyleEditorUtility.SYMBOLS;
		}
		else if (textExtractor instanceof BlankExtractor)
		{
			text = StyleEditorUtility.BLANK;
		}
		else if (textExtractor instanceof StringBuilder)
		{
			this.stringBuilder = (StringBuilder) textExtractor;

			// Do we have the string builder item in the combo box already?
			if (!StyleEditorUtility.hasElement(this.labelText, StyleEditorUtility.STRING_BUILDER))
			{
				this.labelText.addItem(StyleEditorUtility.STRING_BUILDER);
			}

			text = StyleEditorUtility.STRING_BUILDER;
		}

		if (text != null)
		{
			this.labelText.setSelectedItem(text);
		}

		validateLabelTextGUI();
	}

	/**
	 * Sets the show labels check box.
	 * 
	 * @param show
	 *            Whether or not to show the labels.
	 */
	private void setShowLabels(boolean show)
	{
		this.showLabels.setSelected(show);
	}

	/**
	 * Sets the lock colors option.
	 * 
	 * @param lock
	 *            Whether or not to lock the colors.
	 */
	private void setLockedColors(boolean lock)
	{
		this.lockedColors.setSelected(lock);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (this.isUpdating())
		{
			return;
		}

		if (e.getActionCommand().equals(TEXT_COLOR))
		{
			textColorAction();
		}
		else if (e.getActionCommand().equals(BACKGROUND_COLOR))
		{
			backgroundColorAction();
		}
		else if (e.getActionCommand().equals(FONT_FAMILY))
		{
			fontFamilyAction();
		}
		else if (e.getActionCommand().equals(FONT_STYLE))
		{
			fontStyleAction();
		}
		else if (e.getActionCommand().equals(LABEL_TEXT) || e.getActionCommand().equals(ANNOTATION_TEXT))
		{
			labelTextAction();
		}
	}

	/**
	 * The response to the text color being changed.
	 */
	private void textColorAction()
	{
		Color c = StyleEditorUtility.showColorPicker(this, this.textColor.getBackground());

		if (c != null)
		{
			this.textColor.setBackground(c);
			this.broadcastEvent(new LabelTextColorEvent(c));
		}
	}

	/**
	 * The response to the background color being changed.
	 */
	private void backgroundColorAction()
	{
		Color c = StyleEditorUtility.showColorPicker(this, this.backgroundColor.getBackground());

		if (c != null)
		{
			this.backgroundColor.setBackground(c);
			this.broadcastEvent(new LabelBackgroundColorEvent(c));
		}
	}

	/**
	 * The response to the font family being changed.
	 */
	private void fontFamilyAction()
	{
		this.broadcastEvent(new LabelFontFamilyEvent(getFontFamily()));
	}

	/**
	 * The response to the font style being changed.
	 */
	private void fontStyleAction()
	{
		this.broadcastEvent(new LabelFontStyleEvent(getFontStyle()));
	}

	/**
	 * The response to the font size being changed.
	 */
	private void fontSizeAction()
	{
		if (Util.isInteger(getFontSizeText()))
		{
			this.broadcastEvent(new LabelFontSizeEvent(Integer.parseInt(getFontSizeText())));
		}
	}

	/**
	 * The response to the label text being changed.
	 */
	private void labelTextAction()
	{
		validateLabelTextGUI();

		this.broadcastEvent(new LabelTextEvent(getLabelTextExtractor()));
	}

	/**
	 * The response to the labels' visibility being changed.
	 */
	private void showLabelsAction()
	{
		this.broadcastEvent(new LabelVisibleEvent(getShowLabels()));
	}

	/**
	 * The response to the state of the locked colors being changed.
	 */
	private void lockColorsAction()
	{
		if (this.lockedColors.isSelected())
		{
			this.textColor.setEnabled(false);
			this.textColorLabel.setEnabled(false);
		}
		else
		{
			this.textColor.setEnabled(true);
			this.textColorLabel.setEnabled(true);
		}

		this.broadcastEvent(new LabelLockColorsEvent(getLockedColors()));
	}

	@Override
	/**
	 * Updates the panel.
	 */
	public void update()
	{
		updateLockedColors();
		updateTextColor();
		updateBackgroundColor();
		updateFont();
		updateLabelText();
		updateShowLabels();
	}

	/**
	 * Updates the lock colors option.
	 */
	private void updateLockedColors()
	{
		setLockedColors(this.setStyleController.getLockedLabelColors(this.setStyle));
	}

	/**
	 * Updates the text color button.
	 */
	private void updateTextColor()
	{
		Paint tempPaint = this.labelStyleController.getTextColor(this.labelStyle);

		setTextPaint(tempPaint);
	}

	/**
	 * Updates the background color button.
	 */
	private void updateBackgroundColor()
	{
		Paint tempPaint = this.labelStyleController.getBackgroundColor(this.labelStyle);

		setBackgroundPaint(tempPaint);
	}

	/**
	 * Updates the font components.
	 */
	private void updateFont()
	{
		Font tempFont = this.labelStyleController.getFont(this.labelStyle);

		if (tempFont != null)
		{
			setFontFamily(tempFont.getFamily());

			if (tempFont.isBold() && tempFont.isItalic())
			{
				setFontStyle(StyleEditorUtility.BOLD_ITALIC);
			}
			else if (tempFont.isBold())
			{
				setFontStyle(StyleEditorUtility.BOLD);
			}
			else if (tempFont.isItalic())
			{
				setFontStyle(StyleEditorUtility.ITALIC);
			}
			else if (tempFont.isPlain())
			{
				setFontStyle(StyleEditorUtility.PLAIN);
			}

			setFontSizeText(tempFont.getSize());
		}
	}

	/**
	 * Updates the label text components.
	 */
	private void updateLabelText()
	{
		FeatureTextExtractor textExtractor = this.labelStyleController.getLabelExtractor(this.labelStyle);

		this.setLabelText(textExtractor);
		validateLabelTextGUI();
	}

	/**
	 * Updates the show labels check box.
	 */
	private void updateShowLabels()
	{
		setShowLabels(this.labelStyleController.getLabelsVisible(this.labelStyle));
	}

	@Override
	/**
	 * Applies the style to the labelStyle.
	 */
	protected void doApply()
	{
		applyLockColors();
		applyTextColor();
		applyBackgroundColor();
		applyFont();
		applyLabelText();
		applyShowLabels();
	}

	/**
	 * Applies the lock colors option.
	 */
	private void applyLockColors()
	{
		this.setStyleController.setLockedLabelColors(this.setStyle, this.getLockedColors());
	}

	/**
	 * Applies the text color.
	 */
	private void applyTextColor()
	{
		if (this.lockedColors.isSelected())
		{
			this.labelStyleController.setTextColor(this.labelStyle, this.setStyleController.getColor(this.setStyle));
		}
		else
		{
			this.labelStyleController.setTextColor(this.labelStyle, getTextPaint());
		}
	}

	/**
	 * Applies the background color.
	 */
	private void applyBackgroundColor()
	{
		this.labelStyleController.setBackgroundColor(this.labelStyle, getBackgroundPaint());
	}

	/**
	 * Applies the font.
	 */
	private void applyFont()
	{
		// font size
		try
		{
			Font font = StyleEditorUtility.createFont(getFontFamily(), getFontStyle(), getFontSizeText());
			this.labelStyleController.setFont(this.labelStyle, font);
		}
		catch (StyleEditorUtility.ConversionException e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	/**
	 * 
	 * @return The label text extractor.
	 */
	private FeatureTextExtractor getLabelTextExtractor()
	{
		String labelTextSelected = getLabelText();
		FeatureTextExtractor textExtractor = null;

		if (labelTextSelected.equals(StyleEditorUtility.ANNOTATION))
		{
			textExtractor = new AnnotationExtractor(getLabelTextField());
		}
		else if (labelTextSelected.equals(StyleEditorUtility.LOCATION))
		{
			textExtractor = new LocationExtractor();
		}
		else if (labelTextSelected.equals(StyleEditorUtility.SYMBOLS))
		{
			textExtractor = new SymbolsExtractor();
		}
		else if (labelTextSelected.equals(StyleEditorUtility.BLANK))
		{
			textExtractor = new BlankExtractor();
		}
		else if (labelTextSelected.equals(StyleEditorUtility.STRING_BUILDER))
		{
			textExtractor = this.stringBuilder;
		}

		return textExtractor;
	}

	/**
	 * Applies the label text.
	 */
	private void applyLabelText()
	{
		FeatureTextExtractor textExtractor = getLabelTextExtractor();

		if (textExtractor != null)
		{
			this.labelStyleController.setLabelExtractor(this.labelStyle, textExtractor);
		}
	}

	/**
	 * Applies the show labels check box.
	 */
	private void applyShowLabels()
	{
		this.labelStyleController.setLabelsVisible(this.labelStyle, getShowLabels());
	}

	@Override
	public void itemStateChanged(ItemEvent e)
	{
		if (e.getSource().equals(this.showLabels))
		{
			showLabelsAction();
		}
		else if (e.getSource().equals(this.lockedColors))
		{
			lockColorsAction();
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

		if (e.getDocument().equals(this.fontSizeDocument))
		{
			fontSizeAction();
		}

		this.isDocumentUpdating = false;
	}

	@Override
	public void executeGUIEvent(GUIEvent event)
	{
		if (event instanceof LabelBackgroundColorEvent)
		{
			this.setBackgroundPaint(((LabelBackgroundColorEvent) event).getData());
		}
		else if (event instanceof LabelFontFamilyEvent)
		{
			this.setFontFamily(((LabelFontFamilyEvent) event).getData());
		}
		else if (event instanceof LabelFontStyleEvent)
		{
			this.setFontStyle(((LabelFontStyleEvent) event).getData());
		}
		else if (event instanceof LabelFontSizeEvent)
		{
			this.setFontSizeText(((LabelFontSizeEvent) event).getData());
		}
		else if (event instanceof LabelLockColorsEvent)
		{
			this.setLockedColors(((LabelLockColorsEvent) event).getData());
		}
		else if (event instanceof LabelTextColorEvent)
		{
			this.setTextPaint(((LabelTextColorEvent) event).getData());
		}
		else if (event instanceof LabelTextEvent)
		{
			this.setLabelText(((LabelTextEvent) event).getData());
		}
		else if (event instanceof LabelVisibleEvent)
		{
			this.setShowLabels(((LabelVisibleEvent) event).getData());
		}
	}

	/**
	 * Validates the GUI elements associated with the tooltip text.
	 */
	private void validateLabelTextGUI()
	{
		if (this.labelText.getSelectedItem().equals(StyleEditorUtility.ANNOTATION))
		{
			this.annotations.setVisible(true);
			this.labelTextFiller.setVisible(true);
			this.revalidate();
		}
		else
		{
			this.annotations.setVisible(false);
			this.labelTextFiller.setVisible(false);
			this.revalidate();
		}
	}
}
