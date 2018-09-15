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

import ca.corefacility.gview.map.controllers.GlobalStyleController;
import ca.corefacility.gview.map.gui.editor.StyleColoredButton;
import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;
import ca.corefacility.gview.map.gui.hint.HintLabel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * The panel for the global properties.
 * 
 * @author Eric Marinier
 * 
 */
public class GlobalPanel extends StylePanel implements ActionListener
{
	private static final long serialVersionUID = 1L; // requested by java

	private static final String BACKGROUND_COLOR = "Background Color";
	private static final String BACKGROUND_COLOR_LABEL_TEXT = "Background Color:";
	private static final String SLOT_SPACING_LABEL_TEXT = "Slot Spacing:";
	private static final String GLOBAL_STYLE_TEXT = "Global Style";

	private static final String INVALID_SPACING = "Invalid slot spacing.";
	private static final String GLOBAL_STYLE_CONTROLLER_NULL = "GlobalStyleController is null.";

	private static final String BACKGROUND_COLOR_HINT = "The color of the background.";
	private static final String SLOT_SPACING_HINT = "The spacing between slots.";

	private final StyleColoredButton backgroundColor;
	private final JTextField slotSpacing;

	private final GlobalStyleController controller;

	/**
	 * Create the panel.
	 */
	public GlobalPanel(GlobalStyleController controller)
	{
		super();

		if (controller == null)
			throw new IllegalArgumentException(GLOBAL_STYLE_CONTROLLER_NULL);

		this.controller = controller;

		// Layout
		setBorder(new EmptyBorder(10, 10, 10, 10));
		FormLayout formLayout = new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("center:default"), FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(165dlu;min)"), FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("15dlu"), FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, });
		formLayout.setRowGroups(new int[][] { new int[] { 4, 2 } });

		JPanel inner = new JPanel();
		inner.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), GLOBAL_STYLE_TEXT,
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
		panel.add(this.backgroundColor, BorderLayout.WEST);
		this.backgroundColor.setActionCommand(GlobalPanel.BACKGROUND_COLOR);
		this.backgroundColor.addActionListener(this);
		this.backgroundColor.setToolTipText(GlobalPanel.BACKGROUND_COLOR);

		// hint
		inner.add(new HintLabel(BACKGROUND_COLOR_HINT), "10, 2");

		// SLOT SPACING
		JPanel panel_5 = new JPanel();
		inner.add(panel_5, "2, 4, fill, fill");
		panel_5.setLayout(new BorderLayout(0, 0));

		JLabel label_2 = new JLabel(SLOT_SPACING_LABEL_TEXT);
		panel_5.add(label_2, BorderLayout.EAST);

		JPanel panel_1 = new JPanel();
		inner.add(panel_1, "6, 4, fill, fill");
		panel_1.setLayout(new GridLayout());

		// slot spacing text field
		this.slotSpacing = new JTextField();
		this.slotSpacing.setColumns(10);
		panel_1.add(this.slotSpacing, BorderLayout.WEST);

		// hint
		inner.add(new HintLabel(SLOT_SPACING_HINT), "10, 4");

		this.update();
	}

	/**
	 * 
	 * @return The color of the background.
	 */
	private Paint getBackgroundPaint()
	{
		Paint p = this.backgroundColor.getPaint();

		return p;
	}

	/**
	 * Sets the color of the background.
	 * 
	 * @param color
	 */
	private void setBackgroundPaint(Paint p)
	{
		this.backgroundColor.setPaint(p);
	}

	/**
	 * 
	 * @return The TEXT in the slot spacing text field.
	 */
	private String getSlotSpacingText()
	{
		return this.slotSpacing.getText();
	}

	private double getSlotSpacing()
	{
		return Double.parseDouble(getSlotSpacingText());
	}

	/**
	 * Sets the slot spacing text.
	 * 
	 * @param d
	 *            The slot spacing.
	 */
	private void setSlotSpacingText(double d)
	{
		this.slotSpacing.setText(Double.toString(d));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Color color;

		if (e.getActionCommand().equals(GlobalPanel.BACKGROUND_COLOR))
		{
			color = StyleEditorUtility.showColorPicker(this, (Color) this.backgroundColor.getPaint());

			if (color != null)
				this.backgroundColor.setPaint(color);
		}
	}

	/**
	 * Updates the panel according its' style information.
	 */
	public void update()
	{
		updateBackgroundColor();
		updateSlotSpacing();
	}

	/**
	 * Updates the background color.
	 */
	private void updateBackgroundColor()
	{
		Paint tempPaint;

		tempPaint = this.controller.getBackgroundColor();

		setBackgroundPaint(tempPaint);
	}

	/**
	 * Updates the slot spacing.
	 */
	private void updateSlotSpacing()
	{
		setSlotSpacingText(this.controller.getSlotSpacing());
	}

	@Override
	/**
	 * Applies the style to the globalStyle.
	 */
	protected void doApply()
	{
		applyBackgroundColor();
		applySlotSpacing();
	}

	/**
	 * Applies the background color.
	 */
	private void applyBackgroundColor()
	{
		this.controller.setBackgroundColor(getBackgroundPaint());
	}

	/**
	 * Applies the slot spacing.
	 */
	private void applySlotSpacing()
	{
		double slotSpacing;

		try
		{
			slotSpacing = getSlotSpacing();

			if (slotSpacing < 0)
			{
				JOptionPane.showMessageDialog(this, "Slot Spacing value must be non-negative.");
			}
			else
			{
				this.controller.setSlotSpacing(slotSpacing);
			}
		}
		catch (NumberFormatException e)
		{
			JOptionPane.showMessageDialog(this, INVALID_SPACING);
		}
	}
}
