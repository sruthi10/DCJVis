package ca.corefacility.gview.map.gui.editor.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import ca.corefacility.gview.map.controllers.LegendStyleController;
import ca.corefacility.gview.map.controllers.LegendStyleToken;
import ca.corefacility.gview.map.controllers.selection.SelectionController;
import ca.corefacility.gview.map.gui.editor.StyleColoredButton;
import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;
import ca.corefacility.gview.map.gui.editor.communication.GUIEvent;
import ca.corefacility.gview.map.gui.editor.communication.SelectionEventBroadcaster;
import ca.corefacility.gview.map.gui.editor.communication.legendEvent.LegendAlignmentEvent;
import ca.corefacility.gview.map.gui.editor.communication.legendEvent.LegendBackgroundColorEvent;
import ca.corefacility.gview.map.gui.editor.communication.legendEvent.LegendBorderColorEvent;
import ca.corefacility.gview.map.gui.editor.panel.proxy.ProxiablePanel;
import ca.corefacility.gview.map.gui.editor.panel.proxy.ProxyLegendPanel;
import ca.corefacility.gview.map.gui.hint.HintLabel;
import ca.corefacility.gview.style.items.LegendAlignment;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * The legend style panel.
 * 
 * @author Eric Marinier
 * 
 */
public class LegendPanel extends StylePanel implements ActionListener, ProxiablePanel
{
	private static final long serialVersionUID = 1L; // requested by java

	private static final String ALIGNMENT = "Alignment";
	private static final String BACKGROUND_COLOR = "Background Color";
	private static final String BORDER_COLOR = "Border Color";

	private static final String LEGEND_STYLE_TEXT = "Legend Box Style";

	private static final String LEGEND_STYLE_CONTROLLER_NULL = "LegendStyleController is null.";
	private static final String LEGEND_STYLE_TOKEN_NULL = "LegendStyleToken is null.";

	private static final String ALIGNMENT_LABEL_TEXT = "Alignment:";
	private static final String BACKGROUND_COLOR_LABEL_TEXT = "Background Color:";
	private static final String BORDER_COLOR_LABEL_TEXT = "Border Color:";

	private static final String BACKGROUND_COLOR_HINT = "The background color of the legend.";
	private static final String BORDER_COLOR_HINT = "The color of the legend's border.";
	private static final String ALIGNMENT_HINT = "The alignment of the legend.";

	private final StyleColoredButton backgroundColor;
	private final StyleColoredButton borderColor;

	private final JComboBox alignment;

	private final LegendStyleController legendController;
	private final SelectionController selectionController;
	private final LegendStyleToken legendStyle;

	/**
	 * Create the panel.
	 */
	public LegendPanel(LegendStyleController controller, LegendStyleToken legendStyle,
			SelectionController selectionController)
	{
		super();

		if (controller == null)
		{
			throw new IllegalArgumentException(LEGEND_STYLE_CONTROLLER_NULL);
		}

		if (legendStyle == null)
		{
			throw new IllegalArgumentException(LEGEND_STYLE_TOKEN_NULL);
		}

		this.legendController = controller;
		this.selectionController = selectionController;
		this.legendStyle = legendStyle;

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
						RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC, });
		formLayout.setRowGroups(new int[][] { new int[] { 6, 4, 2 } });

		JPanel inner = new JPanel();
		inner.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), LEGEND_STYLE_TEXT,
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		add(inner, BorderLayout.NORTH);

		inner.setLayout(formLayout);

		// BACKGROUND COLOR
		JPanel panel_4 = new JPanel();
		inner.add(panel_4, "2, 2, fill, fill");
		panel_4.setLayout(new BorderLayout(0, 0));

		JLabel label_1 = new JLabel(BACKGROUND_COLOR_LABEL_TEXT);
		panel_4.add(label_1, BorderLayout.EAST);

		JPanel panel = new JPanel();
		inner.add(panel, "6, 2, fill, fill");
		panel.setLayout(new BorderLayout(0, 0));

		// background color button
		this.backgroundColor = new StyleColoredButton();
		this.backgroundColor.setActionCommand(LegendPanel.BACKGROUND_COLOR);
		panel.add(this.backgroundColor, BorderLayout.WEST);
		this.backgroundColor.addActionListener(this);
		this.backgroundColor.setToolTipText(LegendPanel.BACKGROUND_COLOR);

		// hint
		inner.add(new HintLabel(BACKGROUND_COLOR_HINT), "10, 2");

		// BORDER COLOR
		JPanel panel_5 = new JPanel();
		inner.add(panel_5, "2, 4, fill, fill");
		panel_5.setLayout(new BorderLayout(0, 0));

		JLabel label_2 = new JLabel(BORDER_COLOR_LABEL_TEXT);
		panel_5.add(label_2, BorderLayout.EAST);

		JPanel panel_1 = new JPanel();
		inner.add(panel_1, "6, 4, fill, fill");
		panel_1.setLayout(new BorderLayout(0, 0));

		// border color button
		this.borderColor = new StyleColoredButton();
		this.borderColor.setActionCommand(LegendPanel.BORDER_COLOR);
		panel_1.add(this.borderColor, BorderLayout.WEST);
		this.borderColor.addActionListener(this);
		this.borderColor.setToolTipText(LegendPanel.BORDER_COLOR);

		// hint
		inner.add(new HintLabel(BORDER_COLOR_HINT), "10, 4");

		// ALIGNMENT
		JPanel panel_8 = new JPanel();
		inner.add(panel_8, "2, 6, fill, fill");
		panel_8.setLayout(new BorderLayout(0, 0));

		JLabel label_4 = new JLabel(ALIGNMENT_LABEL_TEXT);
		panel_8.add(label_4, BorderLayout.EAST);

		JPanel panel_9 = new JPanel();
		inner.add(panel_9, "6, 6, fill, fill");
		panel_9.setLayout(new GridLayout());

		// alignment combo box
		this.alignment = new JComboBox(StyleEditorUtility.LEGEND_ALIGNMENT_STRINGS);
		this.alignment.addActionListener(this);
		this.alignment.setActionCommand(ALIGNMENT);
		panel_9.add(this.alignment);

		// hint
		inner.add(new HintLabel(ALIGNMENT_HINT), "10, 6");

		this.update();
	}

	/**
	 * Applies GUI changes to create the proxy version of the panel.
	 */
	private void proxy()
	{
		this.setUpdating(true);

		this.backgroundColor.setBackground(StyleEditorUtility.DEFAULT_COLOR);
		this.backgroundColor.setNotify(true);

		this.borderColor.setBackground(StyleEditorUtility.DEFAULT_COLOR);
		this.borderColor.setNotify(true);

		this.alignment.addItem(StyleEditorUtility.PROXY_VALUE);
		this.alignment.setSelectedItem(StyleEditorUtility.PROXY_VALUE);

		this.setUpdating(false);
	}

	@Override
	public LegendPanel createProxy()
	{
		ProxyLegendPanel panel = new ProxyLegendPanel(legendController, legendStyle, selectionController);

		((LegendPanel) panel).proxy();

		return panel;
	}

	/**
	 * 
	 * @return The color of the border.
	 */
	private Paint getBorderPaint()
	{
		Paint p = this.borderColor.getPaint();

		return p;
	}

	/**
	 * Sets the border color.
	 * 
	 * @param color
	 */
	private void setBorderPaint(Paint p)
	{
		this.borderColor.setPaint(p);
	}

	/**
	 * 
	 * @return The background color.
	 */
	private Paint getBackgroundPaint()
	{
		Paint p = this.backgroundColor.getPaint();

		return p;
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
	 * @return The alignment of the legend as a string.
	 */
	private String getAlignment()
	{
		Object temp;
		String result;

		temp = this.alignment.getSelectedItem();

		if (temp instanceof String)
		{
			result = (String) temp;
		}
		else
		{
			throw new IllegalArgumentException("Alignment value is not a String");
		}

		return result;
	}

	/**
	 * Sets the alignment text field.
	 * 
	 * @param align
	 */
	private void setAlignment(String align)
	{
		if (align != null)
			this.alignment.setSelectedItem(align);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (this.isUpdating())
		{
			return;
		}

		if (e.getActionCommand().equals(LegendPanel.BACKGROUND_COLOR))
		{
			backgroundColorAction();
		}
		else if (e.getActionCommand().equals(LegendPanel.BORDER_COLOR))
		{
			borderColorAction();
		}
		else if (e.getActionCommand().equals(LegendPanel.ALIGNMENT))
		{
			alignmentAction();
		}
	}

	@Override
	/**
	 * Updates the panel.
	 */
	public void update()
	{
		updateBorderColor();
		updateBackgroundColor();
		updateAlignment();
	}

	/**
	 * Updates the border color.
	 */
	private void updateBorderColor()
	{
		Paint tempPaint = this.legendController.getOutlineColor(this.legendStyle);

		setBorderPaint(tempPaint);
	}

	/**
	 * Updates the background color.
	 */
	private void updateBackgroundColor()
	{
		Paint tempPaint = this.legendController.getBackgroundColor(this.legendStyle);

		setBackgroundPaint(tempPaint);
	}

	/**
	 * Updates the alignment.
	 */
	private void updateAlignment()
	{
		if (StyleEditorUtility.ALIGNMENT_MAP_ALIGN_TO_STRINGS.containsKey(this.legendController
				.getAlignment(this.legendStyle)))
		{
			setAlignment(StyleEditorUtility.ALIGNMENT_MAP_ALIGN_TO_STRINGS.get(this.legendController
					.getAlignment(this.legendStyle)));
		}
	}

	@Override
	/**
	 * Applies the style.
	 */
	protected void doApply()
	{
		applyBorderColor();
		applyBackgroundColor();
		applyAlignment();
	}

	/**
	 * Applies the border color.
	 */
	private void applyBorderColor()
	{
		this.legendController.setOutlineColor(this.legendStyle, getBorderPaint());
	}

	/**
	 * Applies the background color.
	 */
	private void applyBackgroundColor()
	{
		this.legendController.setBackgroundColor(this.legendStyle, getBackgroundPaint());
	}

	/**
	 * Applies the alignment.
	 */
	private void applyAlignment()
	{
		if (StyleEditorUtility.ALIGNMENT_MAP_STRINGS_TO_ALIGN.containsKey(getAlignment()))
		{
			this.legendController.setAlignment(this.legendStyle, getAlignmentObject());
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Invalid alignment.");

			this.updateAlignment();
		}
	}

	/**
	 * 
	 * @return The alignment of the legend.
	 */
	private LegendAlignment getAlignmentObject()
	{
		return StyleEditorUtility.ALIGNMENT_MAP_STRINGS_TO_ALIGN.get(getAlignment());
	}

	/**
	 * 
	 * @return The legend style as a token.
	 */
	public LegendStyleToken getLegendStyle()
	{
		return this.legendStyle;
	}

	/**
	 * 
	 * @return The legend style controller.
	 */
	public LegendStyleController getLegendStyleController()
	{
		return this.legendController;
	}

	/**
	 * A response to the background colour being changed.
	 */
	private void backgroundColorAction()
	{
		Color color = StyleEditorUtility.showColorPicker(this, this.backgroundColor.getBackground());

		if (color != null)
		{
			this.backgroundColor.setBackground(color);
			this.broadcastEvent(new LegendBackgroundColorEvent(color));
		}
	}

	/**
	 * A response to the border colour being changed.
	 */
	private void borderColorAction()
	{
		Color color = StyleEditorUtility.showColorPicker(this, this.borderColor.getBackground());

		if (color != null)
		{
			this.borderColor.setBackground(color);
			this.broadcastEvent(new LegendBorderColorEvent(color));
		}
	}

	/**
	 * A response to the alignment being changed.
	 */
	private void alignmentAction()
	{
		this.broadcastEvent(new LegendAlignmentEvent(getAlignment()));
	}

	@Override
	public void executeGUIEvent(GUIEvent event)
	{
		if (event instanceof LegendBackgroundColorEvent)
		{
			this.setBackgroundPaint(((LegendBackgroundColorEvent) event).getData());
		}
		else if (event instanceof LegendBorderColorEvent)
		{
			this.setBorderPaint(((LegendBorderColorEvent) event).getData());
		}
		else if (event instanceof LegendAlignmentEvent)
		{
			this.setAlignment(((LegendAlignmentEvent) event).getData());
		}
	}
}
