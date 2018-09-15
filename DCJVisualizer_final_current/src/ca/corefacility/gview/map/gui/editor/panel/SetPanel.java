package ca.corefacility.gview.map.gui.editor.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import ca.corefacility.gview.layout.feature.FeatureShapeRealizer;
import ca.corefacility.gview.layout.feature.ForwardArrow2ShapeRealizer;
import ca.corefacility.gview.layout.feature.ForwardArrowShapeRealizer;
import ca.corefacility.gview.layout.feature.NoArrowShapeRealizer;
import ca.corefacility.gview.layout.feature.ReverseArrow2ShapeRealizer;
import ca.corefacility.gview.layout.feature.ReverseArrowShapeRealizer;
import ca.corefacility.gview.map.controllers.FeatureHolderStyleToken;
import ca.corefacility.gview.map.controllers.GenomeDataController;
import ca.corefacility.gview.map.controllers.SetStyleController;
import ca.corefacility.gview.map.controllers.link.Link;
import ca.corefacility.gview.map.controllers.link.LinkController;
import ca.corefacility.gview.map.controllers.link.Linkable;
import ca.corefacility.gview.map.controllers.selection.SelectionController;
import ca.corefacility.gview.map.effects.ShapeEffectRenderer;
import ca.corefacility.gview.map.gui.editor.AnnotationComboBox;
import ca.corefacility.gview.map.gui.editor.ProxiableDoubleSpinner;
import ca.corefacility.gview.map.gui.editor.StyleColoredButton;
import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;
import ca.corefacility.gview.map.gui.editor.StyleEditorUtility.ConversionException;
import ca.corefacility.gview.map.gui.editor.communication.GUIEvent;
import ca.corefacility.gview.map.gui.editor.communication.LinkEventBroadcaster;
import ca.corefacility.gview.map.gui.editor.communication.SelectionEventBroadcaster;
import ca.corefacility.gview.map.gui.editor.communication.legendEvent.LegendItemSwatchColorEvent;
import ca.corefacility.gview.map.gui.editor.communication.setEvent.SetColorEvent;
import ca.corefacility.gview.map.gui.editor.communication.setEvent.SetFeatureEffectEvent;
import ca.corefacility.gview.map.gui.editor.communication.setEvent.SetFeatureShapeEvent;
import ca.corefacility.gview.map.gui.editor.communication.setEvent.SetThicknessEvent;
import ca.corefacility.gview.map.gui.editor.communication.setEvent.SetTooltipEvent;
import ca.corefacility.gview.map.gui.editor.panel.proxy.ProxiablePanel;
import ca.corefacility.gview.map.gui.editor.panel.proxy.ProxySetPanel;
import ca.corefacility.gview.map.gui.hint.HintLabel;
import ca.corefacility.gview.map.gui.hint.WarningLabel;
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
 * The panel for set styles.
 * 
 * @author Eric Marinier
 * 
 */
public class SetPanel extends StylePanel implements ActionListener, ChangeListener, Linkable, ProxiablePanel,
		DocumentListener
{
	private static final long serialVersionUID = 1L; // requested by java

	private static final int THICKNESS_PROPORTION_SCALE = 100;
	private static final double THICKNESS_PROPORTION_MIN = 0.0;
	private static final double THICKNESS_PROPORTION_MAX = 1.0;
	private double thickness;

	private static final String COLOR = "Color";
	private static final String TOOLTIP_TEXT = "Tooltip Text";
	private static final String FEATURE_SHAPE = "Feature Text";
	private static final String FEATURE_EFFECT = "Feature Effect";

	private static final String SET_STYLE_TEXT = "Set Style";

	private static final String THICKNESS_PROPORTION_LABEL_TEXT = "Thickness Proportion:";
	private static final String TOOLTIP_TEXT_LABEL_TEXT = "Tooltip Text:";
	private static final String FEATURE_SHAPE_LABEL_TEXT = "Feature Shape:";
	private static final String FEATURE_EFFECT_LABEL_TEXT = "Feature Effect:";
	private static final String ANNOTATIONS = "Annotations";
	private static final String COLOR_LABEL_TEXT = "Color:";

	private static final String BASIC = "basic";
	private static final String OUTLINE = "outline";
	private static final String SHADED = "shaded";
	private static final String STANDARD = "standard";

	private static final String THICKNESS_PROPORTION_HINT = "The thickness of the features in this set, as a proportion of the thickness of the parent slot.";
	private static final String TOOLTIP_TEXT_HINT = "The text to display in the tooltip of the feature.";
	private static final String FEATURE_SHAPE_HINT = "The shape of the features.";
	private static final String FEATURE_EFFECT_HINT = "The visual effect to be applied when drawing the features. \"Basic\" is a minimal visual effect, \"outline\" consists only the feature's outline, \"shaded\" adds a shading effect, and \"standard\" is the basic effect with a minor outline effect added.";
	private static final String COLOR_HINT = "The color of the features in this set.";

	private static final String[] featureEffects = { BASIC, OUTLINE, SHADED, STANDARD };

	private static final String FEATURE_HOLDER_STYLE_TOKEN_NULL = "FeatureHolderStyleToken is null.";

	private final StyleColoredButton color;

	private final JSlider thicknessProportion;
	private final ProxiableDoubleSpinner thicknessProportionSpinner;
	private final Document thicknessProportionDocument;

	private final JComboBox tooltipText;
	private final JComboBox featureShape;
	private final JComboBox featureEffect;
	private final AnnotationComboBox annotations;

	private final Box.Filler tooltipTextFiller;

	private final WarningLabel colorWarning = new WarningLabel(
			"Any changes to the color will propagate to all items that are linked to any selected sets!");

	private boolean isDocumentUpdating = false;

	private final SetStyleController setStyleController;
	private final GenomeDataController genomeDataController;
	private final LinkController linkController;
	private final SelectionController selectionController;

	private final FeatureHolderStyleToken setStyle;

	private StringBuilder stringBuilder; // The string builder text extractor
											// that would be loaded from a GSS
											// file.

	/**
	 * Create the panel.
	 */
	public SetPanel(SetStyleController setStyleController, FeatureHolderStyleToken setStyle,
			GenomeDataController genomeDataController, LinkController linkController,
			SelectionController selectionController)
	{
		if (setStyle == null)
			throw new IllegalArgumentException(FEATURE_HOLDER_STYLE_TOKEN_NULL);

		this.setStyleController = setStyleController;
		this.genomeDataController = genomeDataController;
		this.linkController = linkController;
		this.selectionController = selectionController;

		this.setStyle = setStyle;

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
						FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("15dlu"), FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, });
		formLayout.setRowGroups(new int[][] { new int[] { 8, 6, 2, 10 } }); // leave
																			// sliders
																			// off
																			// this

		JPanel inner = new JPanel();
		inner.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), SET_STYLE_TEXT,
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		add(inner, BorderLayout.NORTH);

		inner.setLayout(formLayout);

		// COLOR
		JPanel panel_8 = new JPanel();
		inner.add(panel_8, "2, 2, fill, fill");
		panel_8.setLayout(new BorderLayout(0, 0));

		JLabel lblColor = new JLabel(COLOR_LABEL_TEXT);
		panel_8.add(lblColor, BorderLayout.EAST);

		JPanel panel_9 = new JPanel();
		inner.add(panel_9, "6, 2, fill, fill");
		panel_9.setLayout(new BorderLayout(0, 0));

		JPanel colorPanel = new JPanel();
		colorPanel.setLayout(new BorderLayout());
		panel_9.add(colorPanel, BorderLayout.WEST);

		JPanel colorButtonPanel = new JPanel();
		colorButtonPanel.setLayout(new BorderLayout());
		colorPanel.add(colorButtonPanel, BorderLayout.WEST);

		// color button
		this.color = new StyleColoredButton();
		colorButtonPanel.add(this.color, BorderLayout.WEST);
		this.color.setActionCommand(COLOR);
		this.color.addActionListener(this);

		// color button warning
		colorPanel.add(this.colorWarning, BorderLayout.EAST);
		this.colorWarning.setVisible(false);

		// spacing
		colorPanel.add(new Box.Filler(new Dimension(2, 0), new Dimension(2, 0), new Dimension(2, 0)),
				BorderLayout.CENTER);

		// hint
		inner.add(new HintLabel(COLOR_HINT), "10, 2");

		// THICKNESS PROPORTION
		JPanel panel_4 = new JPanel();
		inner.add(panel_4, "2, 4, fill, fill");
		panel_4.setLayout(new BorderLayout(0, 0));

		JLabel lblThickness = new JLabel(THICKNESS_PROPORTION_LABEL_TEXT);
		panel_4.add(lblThickness, BorderLayout.EAST);

		JPanel panel = new JPanel();
		inner.add(panel, "6, 4, fill, fill");
		panel.setLayout(new GridLayout());

		JPanel thicknessProportionPanel = new JPanel();
		thicknessProportionPanel.setLayout(new BoxLayout(thicknessProportionPanel, BoxLayout.LINE_AXIS));
		panel.add(thicknessProportionPanel);

		// thickness proportion slider
		this.thicknessProportion = new JSlider(JSlider.HORIZONTAL, (int) THICKNESS_PROPORTION_MIN
				* THICKNESS_PROPORTION_SCALE, (int) (THICKNESS_PROPORTION_MAX * THICKNESS_PROPORTION_SCALE),
				(int) (THICKNESS_PROPORTION_MAX * THICKNESS_PROPORTION_SCALE));
		this.thicknessProportion
				.setMajorTickSpacing((int) ((THICKNESS_PROPORTION_MAX * THICKNESS_PROPORTION_SCALE - THICKNESS_PROPORTION_MIN
						* THICKNESS_PROPORTION_SCALE) / 10));
		this.thicknessProportion.setPaintTicks(true);
		thicknessProportionPanel.add(this.thicknessProportion);

		// Filler
		thicknessProportionPanel.add(new Box.Filler(new Dimension(2, 0), new Dimension(2, 0), new Dimension(2, 0)));

		// Mouse wheel listener for slider:
		this.thicknessProportion.addMouseWheelListener(new MouseWheelListener()
		{
			@Override
			public void mouseWheelMoved(MouseWheelEvent e)
			{
				thicknessProportion.setValue(thicknessProportion.getValue() - e.getWheelRotation()
						* thicknessProportion.getMajorTickSpacing());
				thickness = getSliderThicknessProportion();
				thicknessProportionSpinner.setValue(thickness);
			}
		});

		// Change listener for slider:
		this.thicknessProportion.addChangeListener(this);

		// thickness proportion text
		this.thicknessProportionSpinner = ProxiableDoubleSpinner.createSpinner(THICKNESS_PROPORTION_MAX,
				THICKNESS_PROPORTION_MIN, THICKNESS_PROPORTION_MAX,
				(THICKNESS_PROPORTION_MAX - THICKNESS_PROPORTION_MIN) / THICKNESS_PROPORTION_SCALE, 2);
		((JSpinner.DefaultEditor) this.thicknessProportionSpinner.getEditor()).getTextField().setColumns(5);

		thicknessProportionPanel.add(this.thicknessProportionSpinner);

		// Listener for spinner:
		this.thicknessProportionSpinner.addChangeListener(this);
		this.thicknessProportionDocument = ((JSpinner.DefaultEditor) this.thicknessProportionSpinner.getEditor())
				.getTextField().getDocument();
		this.thicknessProportionDocument.addDocumentListener(this);

		// hint
		inner.add(new HintLabel(THICKNESS_PROPORTION_HINT), "10, 4");

		// TOOLTIP TEXT
		JPanel panel_5 = new JPanel();
		inner.add(panel_5, "2, 6, fill, fill");
		panel_5.setLayout(new BorderLayout(0, 0));

		JLabel lblTooltipText = new JLabel(TOOLTIP_TEXT_LABEL_TEXT);
		panel_5.add(lblTooltipText, BorderLayout.EAST);

		JPanel panel_1 = new JPanel();
		inner.add(panel_1, "6, 6, fill, fill");
		panel_1.setLayout(new GridLayout());

		JPanel tooltipPanel = new JPanel();
		tooltipPanel.setLayout(new BoxLayout(tooltipPanel, BoxLayout.LINE_AXIS));
		panel_1.add(tooltipPanel);

		// tooltip text combo box
		this.tooltipText = new JComboBox(StyleEditorUtility.textExtractorTexts);
		this.tooltipText.addActionListener(this);
		this.tooltipText.setActionCommand(TOOLTIP_TEXT);
		tooltipPanel.add(this.tooltipText);

		this.tooltipTextFiller = new Box.Filler(new Dimension(2, 0), new Dimension(2, 0), new Dimension(2, 0));
		tooltipPanel.add(this.tooltipTextFiller);

		// annotations combo box
		this.annotations = new AnnotationComboBox(this.genomeDataController);
		this.annotations.setVisible(false);
		this.annotations.setActionCommand(ANNOTATIONS);
		this.annotations.addActionListener(this);
		tooltipPanel.add(this.annotations);

		// hint
		inner.add(new HintLabel(TOOLTIP_TEXT_HINT), "10, 6");

		// FEATURE SHAPE
		JPanel panel_6 = new JPanel();
		inner.add(panel_6, "2, 8, fill, fill");
		panel_6.setLayout(new BorderLayout(0, 0));

		JLabel lblFeatureShape = new JLabel(FEATURE_SHAPE_LABEL_TEXT);
		panel_6.add(lblFeatureShape, BorderLayout.EAST);

		JPanel panel_2 = new JPanel();
		inner.add(panel_2, "6, 8, fill, fill");
		panel_2.setLayout(new GridLayout());

		// feature shape combo box
		this.featureShape = new JComboBox(StyleEditorUtility.featureShapes);
		this.featureShape.setActionCommand(FEATURE_SHAPE);
		this.featureShape.addActionListener(this);
		panel_2.add(this.featureShape);

		// hint
		inner.add(new HintLabel(FEATURE_SHAPE_HINT), "10, 8");

		// FEATURE EFFECT
		JPanel panel_7 = new JPanel();
		inner.add(panel_7, "2, 10, fill, fill");
		panel_7.setLayout(new BorderLayout(0, 0));

		JLabel lblFeatureEffect = new JLabel(FEATURE_EFFECT_LABEL_TEXT);
		panel_7.add(lblFeatureEffect, BorderLayout.EAST);

		JPanel panel_3 = new JPanel();
		inner.add(panel_3, "6, 10, fill, fill");
		panel_3.setLayout(new GridLayout());

		// feature effect combo box
		this.featureEffect = new JComboBox(featureEffects);
		this.featureEffect.setActionCommand(FEATURE_EFFECT);
		this.featureEffect.addActionListener(this);
		panel_3.add(this.featureEffect);

		// hint
		inner.add(new HintLabel(FEATURE_EFFECT_HINT), "10, 10");

		this.update();
	}

	/**
	 * Applies GUI changes to create the proxy version of the panel.
	 */
	private void proxy()
	{
		this.setUpdating(true);

		this.color.setPaint(StyleEditorUtility.DEFAULT_COLOR);
		this.color.setNotify(true);

		if (!this.selectionController.isLinkConsistent())
		{
			// this.color.setEnabled(false);
			this.colorWarning.setVisible(true);
		}

		this.thicknessProportion.setValue((int) (THICKNESS_PROPORTION_MIN * THICKNESS_PROPORTION_SCALE));

		this.thicknessProportionSpinner.setProxy();

		this.tooltipText.addItem(StyleEditorUtility.PROXY_VALUE);
		this.tooltipText.setSelectedItem(StyleEditorUtility.PROXY_VALUE);

		this.annotations.addItem(StyleEditorUtility.PROXY_VALUE);
		this.annotations.setSelectedItem(StyleEditorUtility.PROXY_VALUE);

		this.featureShape.addItem(StyleEditorUtility.PROXY_VALUE);
		this.featureShape.setSelectedItem(StyleEditorUtility.PROXY_VALUE);

		this.featureEffect.addItem(StyleEditorUtility.PROXY_VALUE);
		this.featureEffect.setSelectedItem(StyleEditorUtility.PROXY_VALUE);

		validateTooltipTextGUI();

		this.setUpdating(false);
	}

	@Override
	public SetPanel createProxy()
	{

		ProxySetPanel panel = new ProxySetPanel(setStyleController, setStyle, genomeDataController, linkController,
				selectionController);

		((SetPanel) panel).proxy();

		return panel;
	}

	/**
	 * 
	 * @return The color of the set.
	 */
	private Paint getPaint()
	{
		Paint p = this.color.getPaint();

		return p;
	}

	/**
	 * Sets the color of the set.
	 * 
	 * @param c
	 */
	private void setPaint(Paint p)
	{
		this.color.setPaint(p);
	}

	/**
	 * 
	 * @return The thickness proportion.
	 */
	private double getThicknessProportion()
	{
		return this.thickness;
	}

	private double getSpinnerThicknessProportion()
	{
		Object value = this.thicknessProportionSpinner.getValue();

		if (value instanceof Integer)
		{
			return ((Integer) value).doubleValue();
		}
		else if (value instanceof Double)
		{
			return (Double) value;
		}
		else if (value != null && value.equals(ProxiableDoubleSpinner.PROXY))
		{
			return THICKNESS_PROPORTION_MIN;
		}
		else
		{
			throw new IllegalArgumentException("Invalid spinner value!");
		}
	}

	private double getSliderThicknessProportion()
	{
		return ((double) this.thicknessProportion.getValue() / (double) THICKNESS_PROPORTION_SCALE);
	}

	protected void updateThicknessProportionGUI()
	{
		this.thicknessProportion.setValue((int) (this.thickness * THICKNESS_PROPORTION_SCALE));

		if (!this.isDocumentUpdating)
		{
			this.thicknessProportionSpinner.setValue(this.thickness);
		}
	}

	/**
	 * Sets the thickness proportion gui value.
	 * 
	 * @param d
	 */
	private void setThicknessProportion(double d)
	{
		this.thickness = d;

		updateThicknessProportionGUI();
	}

	/**
	 * 
	 * @return The item selected in the tooltip combo box.
	 */
	private String getTooltip()
	{
		Object temp;
		String result;

		temp = this.tooltipText.getSelectedItem();

		if (temp instanceof String)
		{
			result = (String) temp;
		}
		else
		{
			throw new IllegalArgumentException("Tooltip Text is not a String");
		}

		return result;
	}

	/**
	 * Sets the selection in the tooltip combo box.
	 * 
	 * @param text
	 */
	private void setTooltip(FeatureTextExtractor textExtractor)
	{
		String text = null;

		if (textExtractor instanceof AnnotationExtractor)
		{
			text = (StyleEditorUtility.ANNOTATION);

			// If the annotation doesn't already exist (sometimes happens with
			// CGView XML files):
			if (!this.annotations.containsItem(((AnnotationExtractor) textExtractor).getAnnotation())
					&& !((AnnotationExtractor) textExtractor).getAnnotation().equals(StyleEditorUtility.PROXY_VALUE))
			{
				this.annotations.addAnnotation(((AnnotationExtractor) textExtractor).getAnnotation());
			}

			setAnnotation(((AnnotationExtractor) textExtractor).getAnnotation());
		}
		else if (textExtractor instanceof LocationExtractor)
		{
			text = (StyleEditorUtility.LOCATION);
		}
		else if (textExtractor instanceof SymbolsExtractor)
		{
			text = (StyleEditorUtility.SYMBOLS);
		}
		else if (textExtractor instanceof BlankExtractor)
		{
			text = (StyleEditorUtility.BLANK);
		}
		else if (textExtractor instanceof StringBuilder)
		{
			this.stringBuilder = (StringBuilder) textExtractor;

			// Do we have the string builder item in the combo box already?
			if (!StyleEditorUtility.hasElement(this.tooltipText, StyleEditorUtility.STRING_BUILDER))
			{
				this.tooltipText.addItem(StyleEditorUtility.STRING_BUILDER);
			}

			text = (StyleEditorUtility.STRING_BUILDER);
		}

		if (text != null)
		{
			this.tooltipText.setSelectedItem(text);
		}

		validateTooltipTextGUI();
	}

	/**
	 * 
	 * @return The text of the annotation combo box.
	 */
	private String getAnnotation()
	{
		return this.annotations.getSelectedString();
	}

	/**
	 * Sets the annotation.
	 * 
	 * @param annotation
	 */
	private void setAnnotation(String annotation)
	{
		this.annotations.setSelectedItem(annotation);
	}

	/**
	 * 
	 * @return The selection in the feature shape combo box.
	 */
	private String getFeatureShape()
	{
		Object temp;
		String result;

		temp = this.featureShape.getSelectedItem();

		if (temp instanceof String)
		{
			result = (String) temp;
		}
		else
		{
			throw new IllegalArgumentException("Feature Shape is not a String");
		}

		return result;
	}

	/**
	 * Sets selection of the feature shape combo box.
	 * 
	 * @param shape
	 */
	private void setFeatureShape(String shape)
	{
		this.featureShape.setSelectedItem(shape);
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
		if (this.isUpdating())
		{
			return;
		}

		if (e.getActionCommand().equals(COLOR))
		{
			colorAction();
		}
		else if (e.getActionCommand().equals(TOOLTIP_TEXT) || e.getActionCommand().equals(ANNOTATIONS))
		{
			tooltipAction();
		}
		else if (e.getActionCommand().equals(FEATURE_SHAPE))
		{
			featureShapeAction();
		}
		else if (e.getActionCommand().equals(FEATURE_EFFECT))
		{
			featureEffectAction();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		if (this.isUpdating())
		{
			return;
		}

		if (e.getSource() == this.thicknessProportion)
		{
			// Order is important (need to update the value first):
			this.thickness = getSliderThicknessProportion();
			thicknessAction();
			updateThicknessProportionGUI();
		}
		else if (e.getSource() == this.thicknessProportionSpinner)
		{
			// Order is important (need to update the value first):
			this.thickness = getSpinnerThicknessProportion();
			thicknessAction();
			updateThicknessProportionGUI();
		}
	}

	/**
	 * Validates the GUI elements associated with the tooltip text.
	 */
	private void validateTooltipTextGUI()
	{
		if (this.tooltipText.getSelectedItem().equals(StyleEditorUtility.ANNOTATION))
		{
			this.annotations.setVisible(true);
			this.tooltipTextFiller.setVisible(true);
			this.revalidate();
		}
		else
		{
			this.annotations.setVisible(false);
			this.tooltipTextFiller.setVisible(false);
			this.revalidate();
		}
	}

	/**
	 * The tooltip action event.
	 */
	private void tooltipAction()
	{
		validateTooltipTextGUI();

		this.broadcastEvent(new SetTooltipEvent(this.getTooltipTextExtractor()));
	}

	/**
	 * The color action event.
	 */
	private void colorAction()
	{
		Color c = StyleEditorUtility.showColorPicker(this, this.color.getBackground());

		if (c != null)
		{
			this.color.setBackground(c);
			this.broadcastEvent(new SetColorEvent(c));
		}
	}

	/**
	 * The thickness event.
	 */
	private void thicknessAction()
	{
		this.broadcastEvent(new SetThicknessEvent(this.getThicknessProportion()));
	}

	/**
	 * The feature shape action event.
	 */
	private void featureShapeAction()
	{
		this.broadcastEvent(new SetFeatureShapeEvent(this.getFeatureShape()));
	}

	/**
	 * The feature effect action event.
	 */
	private void featureEffectAction()
	{
		this.broadcastEvent(new SetFeatureEffectEvent(this.getFeatureEffect()));
	}

	@Override
	/**
	 * Updates the panel.
	 */
	public void update()
	{
		setUpdating(true);

		updateThicknessProportion();
		updateTooltipText();
		updateFeatureShape();
		updateFeatureEffect();
		updateColor();

		setUpdating(false);
	}

	/**
	 * Updates the thickness proportion.
	 */
	private void updateThicknessProportion()
	{
		setThicknessProportion(this.setStyleController.getThickness(this.setStyle));
	}

	/**
	 * Updates the tool tip text.
	 */
	private void updateTooltipText()
	{
		FeatureTextExtractor textExtractor;

		textExtractor = this.setStyleController.getTextExtractor(this.setStyle);

		setTooltip(textExtractor);
		validateTooltipTextGUI();
	}

	/**
	 * Updates the feature shape.
	 */
	private void updateFeatureShape()
	{
		FeatureShapeRealizer shapeRealizer = this.setStyleController.getFeatureShapeRealizer(this.setStyle);

		if (shapeRealizer instanceof ForwardArrowShapeRealizer)
		{
			setFeatureShape(StyleEditorUtility.CLOCKWISE_ARROW);
		}
		else if (shapeRealizer instanceof ForwardArrow2ShapeRealizer)
		{
			setFeatureShape(StyleEditorUtility.CLOCKWISE_ARROW_2);
		}
		else if (shapeRealizer instanceof ReverseArrowShapeRealizer)
		{
			setFeatureShape(StyleEditorUtility.COUNTERCLOCKWISE_ARROW);
		}
		else if (shapeRealizer instanceof ReverseArrow2ShapeRealizer)
		{
			setFeatureShape(StyleEditorUtility.COUNTERCLOCKWISE_ARROW_2);
		}
		else if (shapeRealizer instanceof NoArrowShapeRealizer)
		{
			setFeatureShape(StyleEditorUtility.BLOCK);
		}
	}

	/**
	 * Updates the feature effect.
	 */
	private void updateFeatureEffect()
	{
		ShapeEffectRenderer effectRenderer = this.setStyleController.getShapeEffectRenderer(this.setStyle);

		String effect = StyleEditorUtility.getFeatureEffectRenderer(effectRenderer);
		setFeatureEffect(effect);
	}

	/**
	 * Updates the color.
	 */
	private void updateColor()
	{
		setPaint(this.setStyleController.getColor(this.setStyle));
	}

	@Override
	/**
	 * Applies the style.
	 */
	protected void doApply()
	{
		applyThicknessProportion();
		applyTooltipText();
		applyFeatureShape();
		applyFeatureEffect();
		applyColor();
	}

	/**
	 * Applies the thickness proportion.
	 */
	private void applyThicknessProportion()
	{
		if (this.thickness > 1.0 || this.thickness < 0.0)
		{
			JOptionPane.showMessageDialog(this, "Thickness proportion needs to be within 0.0 and 1.0 inclusive.");

			this.updateThicknessProportion();
		}
		else
		{
			this.setStyleController.setThickness(this.setStyle, this.thickness);
		}

	}

	/**
	 * 
	 * @return The tool tip text extractor.
	 */
	private FeatureTextExtractor getTooltipTextExtractor()
	{
		String labelTextSelected = getTooltip();
		FeatureTextExtractor textExtractor = null;

		if (labelTextSelected.equals(StyleEditorUtility.ANNOTATION))
		{
			textExtractor = new AnnotationExtractor(this.getAnnotation());
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
	 * Applies the tool tip text.
	 */
	private void applyTooltipText()
	{
		FeatureTextExtractor featureTextExtractor = getTooltipTextExtractor();

		if (featureTextExtractor != null)
		{
			this.setStyleController.setTextExtractor(this.setStyle, featureTextExtractor);
		}
	}

	/**
	 * Applies the feature shape.
	 */
	private void applyFeatureShape()
	{
		FeatureShapeRealizer shapeRealizer;
		try
		{
			shapeRealizer = StyleEditorUtility.getShapeRealizer(getFeatureShape());
			this.setStyleController.setFeatureShapeRealizer(this.setStyle, shapeRealizer);
		}
		catch (ConversionException e)
		{
			e.printStackTrace();

			this.updateFeatureShape();
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
			this.setStyleController.setShapeEffectRenderer(this.setStyle, shapeEffect);
		}
		catch (ConversionException e)
		{
			e.printStackTrace();

			this.updateFeatureEffect();
		}
	}

	/**
	 * Applies the color.
	 */
	private void applyColor()
	{
		Paint color = getPaint();

		this.setStyleController.setColor(this.setStyle, color);
	}

	/**
	 * 
	 * @return The set style as a token.
	 */
	public FeatureHolderStyleToken getSetStyle()
	{
		return this.setStyle;
	}

	/**
	 * 
	 * @return The set style controller.
	 */
	public SetStyleController getController()
	{
		return this.setStyleController;
	}

	@Override
	public void executeGUIEvent(GUIEvent event)
	{
		if (event instanceof SetColorEvent)
		{
			this.setPaint(((SetColorEvent) event).getData());
		}
		else if (event instanceof SetThicknessEvent)
		{
			this.setThicknessProportion(((SetThicknessEvent) event).getData());
		}
		else if (event instanceof SetTooltipEvent)
		{
			this.setTooltip(((SetTooltipEvent) event).getData());
		}
		else if (event instanceof SetFeatureShapeEvent)
		{
			this.setFeatureShape(((SetFeatureShapeEvent) event).getData());
		}
		else if (event instanceof SetFeatureEffectEvent)
		{
			this.setFeatureEffect(((SetFeatureEffectEvent) event).getData());
		}
		else if (event instanceof LegendItemSwatchColorEvent)
		{
			this.setPaint(((LegendItemSwatchColorEvent) event).getData());
		}
	}

	@Override
	public Link getLink()
	{
		return this.setStyleController.getLink(this.setStyle);
	}

	@Override
	public void setLink(Link link)
	{
		this.setStyleController.setLink(this.setStyle, link);
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
	protected void textChanged(DocumentEvent e)
	{
		if (e == null || e.getDocument() == null || this.isUpdating())
		{
			return;
		}

		this.isDocumentUpdating = true;

		if (e.getDocument().equals(this.thicknessProportionDocument))
		{
			thicknessProportionTextAction();
		}

		this.isDocumentUpdating = false;
	}

	private void thicknessProportionTextAction()
	{
		String text = ((JSpinner.DefaultEditor) (this.thicknessProportionSpinner.getEditor())).getTextField().getText();

		// Is the text valid?
		if (Util.isDouble(text))
		{
			double d = Double.parseDouble(text);

			if (d >= THICKNESS_PROPORTION_MIN && d <= THICKNESS_PROPORTION_MAX)
			{
				this.thickness = d;
				this.broadcastEvent(new SetThicknessEvent(d));
				updateThicknessProportionGUI();
			}
		}

	}
}
