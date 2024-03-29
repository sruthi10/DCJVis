package ca.corefacility.gview.map.gui.editor.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import ca.corefacility.gview.map.controllers.BackboneStyleController;
import ca.corefacility.gview.map.gui.editor.StyleColoredButton;
import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;
import ca.corefacility.gview.map.gui.hint.HintLabel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * The backbone panel.
 * 
 * Displays the editable properties of the backbone.
 * 
 * @author Eric Marinier
 * 
 */
public class BackbonePanel extends StylePanel implements ActionListener
{
	private static final long serialVersionUID = 1L; // requested by java

	private static final String BACKBONE_STYLE_TEXT = "Backbone Style";
	private static final String COLOR_LABEL_TEXT = "Color:";
	private static final String THICKNESS_LABEL_TEXT = "Thickness:";

	private static final String BACKBONE_COLOR = "Backbone Color";
	private static final String INVALID_THICKNESS = "Invalid backbone thickness.";
	private static final String BACKBONE_STYLE_CONTROLLER_NULL = "BackboneStyleController is null.";

	private static final String COLOR_HINT = "The color of the backbone.";
	private static final String THICKNESS_HINT = "The thickness of the backbone.";

	private final JTextField backboneThickness; // the thickness text field
	private final StyleColoredButton backboneColor; // colored button displaying
													// the backbone color

	private final BackboneStyleController controller; // the related backbone
														// style controller for
														// the panel

	/**
	 * Create the panel.
	 * 
	 * Much of the code generated by WindowBuilderPro.
	 */
	public BackbonePanel(BackboneStyleController backboneStyleController)
	{
		super();

		if (backboneStyleController == null)
			throw new IllegalArgumentException(BACKBONE_STYLE_CONTROLLER_NULL);

		this.controller = backboneStyleController;

		// Layout
		setBorder(new EmptyBorder(10, 10, 10, 10));
		FormLayout formLayout = new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("center:default"), FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(165dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, },
				new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("15dlu"),
						FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, });
		formLayout.setRowGroups(new int[][] { new int[] { 4, 2 } });

		JPanel inner = new JPanel();
		inner.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), BACKBONE_STYLE_TEXT,
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		add(inner, BorderLayout.NORTH);

		inner.setLayout(formLayout);

		// COLOR
		JPanel panel_4 = new JPanel();
		inner.add(panel_4, "2, 2, fill, fill");
		panel_4.setLayout(new BorderLayout(0, 0));

		JLabel label_1 = new JLabel(COLOR_LABEL_TEXT);
		panel_4.add(label_1, BorderLayout.EAST);

		JPanel panel = new JPanel();
		inner.add(panel, "6, 2, fill, fill");
		panel.setLayout(new BorderLayout(0, 0));

		// Backbone Color Button
		this.backboneColor = new StyleColoredButton();
		panel.add(this.backboneColor, BorderLayout.WEST);
		this.backboneColor.setActionCommand(BackbonePanel.BACKBONE_COLOR);
		this.backboneColor.addActionListener(this);
		this.backboneColor.setToolTipText(BackbonePanel.BACKBONE_COLOR);

		// hint
		inner.add(new HintLabel(COLOR_HINT), "10, 2");

		// THICKNESS
		JPanel panel_5 = new JPanel();
		inner.add(panel_5, "2, 4, fill, fill");
		panel_5.setLayout(new BorderLayout(0, 0));

		JLabel label_2 = new JLabel(THICKNESS_LABEL_TEXT);
		panel_5.add(label_2, BorderLayout.EAST);

		JPanel panel_1 = new JPanel();
		inner.add(panel_1, "6, 4, fill, fill");
		panel_1.setLayout(new GridLayout());

		// Thickness Text Field
		this.backboneThickness = new JTextField();
		this.backboneThickness.setColumns(10);
		panel_1.add(this.backboneThickness);

		// hint
		inner.add(new HintLabel(THICKNESS_HINT), "10, 4");

		this.update();
	}

	/**
	 * 
	 * @return The color of the backbone.
	 */
	private Paint getBackbonePaint()
	{
		return this.backboneColor.getPaint();
	}

	/**
	 * Sets the color of the backbone.
	 * 
	 * @param color
	 *            The color to set the backbone to.
	 */
	private void setBackbonePaint(Paint p)
	{
		this.backboneColor.setPaint(p);
	}

	/**
	 * @return The text within the thickness field.
	 */
	private String getBackboneThicknessText()
	{
		return this.backboneThickness.getText();
	}

	private double getBackboneThickness()
	{
		return Double.parseDouble(getBackboneThicknessText());
	}

	/**
	 * Sets the text of the thickness field, forcing a double as that value.
	 * 
	 * @param value
	 *            The value to set in the text field.
	 */
	private void setBackboneThicknessText(double value)
	{
		this.backboneThickness.setText(value + "");
	}

	@Override
	/**
	 * Action Event listener for BackbonePanel.
	 */
	public void actionPerformed(ActionEvent e)
	{
		Color color;

		if (e.getActionCommand().equals(BackbonePanel.BACKBONE_COLOR))
		{
			// show the color dialog
			color = StyleEditorUtility.showColorPicker(this, this.backboneColor.getBackground());

			if (color != null)
				this.backboneColor.setPaint(color);
		}
	}

	@Override
	/**
	 * Updates the BackbonePanel.
	 */
	public void update()
	{
		updateBackboneColor();
		updateBackboneThickness();
	}

	/**
	 * Updates the backboneColor button.
	 */
	private void updateBackboneColor()
	{
		Paint tempPaint = this.controller.getColor();

		setBackbonePaint(tempPaint);
	}

	/**
	 * Updates the backboneThickness text field.
	 */
	private void updateBackboneThickness()
	{
		setBackboneThicknessText(this.controller.getThickness());
	}

	@Override
	/**
	 * Applies the style to the BackboneStyle.
	 */
	protected void doApply()
	{
		applyBackboneColor();
		applyBackboneThickness();
	}

	/**
	 * Applies the backbone color.
	 */
	private void applyBackboneColor()
	{
		this.controller.setColor(getBackbonePaint());
	}

	/**
	 * Applies the backbone thickness.
	 */
	private void applyBackboneThickness()
	{
		double backboneThickness;

		try
		{
			backboneThickness = getBackboneThickness();

			if (backboneThickness < 0)
			{
				JOptionPane.showMessageDialog(this, "Backbone Thickness value must be non-negative.");

				this.updateBackboneThickness();
			}
			else
			{
				this.controller.setThickness(backboneThickness);
			}
		}
		catch (NumberFormatException e)
		{
			JOptionPane.showMessageDialog(this, INVALID_THICKNESS);

			this.updateBackboneThickness();
		}
	}

	/**
	 * 
	 * @return The backbone style controller.
	 */
	public BackboneStyleController getBackboneStyleController()
	{
		return this.controller;
	}
}
