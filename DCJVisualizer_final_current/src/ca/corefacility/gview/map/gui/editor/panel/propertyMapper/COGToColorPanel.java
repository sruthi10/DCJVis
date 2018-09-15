package ca.corefacility.gview.map.gui.editor.panel.propertyMapper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import ca.corefacility.gview.map.gui.editor.StyleColoredButton;
import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * The COG to color panel.
 * 
 * This panel is intended to exist on the property mapper chooser dialog.
 * 
 * @author Eric Marinier
 * 
 */
public class COGToColorPanel extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L; // requested by java
	private static final String COG_COLOR_BUTTON = "COG Color Button";

	private final StyleColoredButton coloredButton = new StyleColoredButton();
	private final JComboBox cogCategories = new JComboBox(StyleEditorUtility.COG_CATEGORIES);

	/**
	 * Creates a new COGToColorPanel.
	 */
	public COGToColorPanel()
	{
		setBorder(new TitledBorder(null, "COG to Color", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));

		// Layout
		FormLayout formLayout = createFormLayout();

		setLayout(formLayout);

		this.coloredButton.setActionCommand(COG_COLOR_BUTTON);
		this.coloredButton.addActionListener(this);

		JPanel panel_4 = new JPanel();
		add(panel_4, "2, 2, fill, fill");
		panel_4.setLayout(new BorderLayout(0, 0));

		JLabel lblCogCategory = new JLabel("COG Category");
		panel_4.add(lblCogCategory, BorderLayout.EAST);

		JPanel panel = new JPanel();
		add(panel, "6, 2, fill, fill");
		panel.setLayout(new BorderLayout(0, 0));

		panel.add(this.cogCategories, BorderLayout.WEST);

		JPanel panel_5 = new JPanel();
		add(panel_5, "2, 4, fill, fill");
		panel_5.setLayout(new BorderLayout(0, 0));

		JLabel lblCogColor = new JLabel("COG Color:");
		panel_5.add(lblCogColor, BorderLayout.EAST);

		JPanel panel_1 = new JPanel();
		add(panel_1, "6, 4, fill, fill");
		panel_1.setLayout(new BorderLayout(0, 0));

		panel_1.add(this.coloredButton, BorderLayout.WEST);
	}

	/**
	 * 
	 * @return The specific form layout for this panel.
	 */
	private FormLayout createFormLayout()
	{
		FormLayout formLayout = new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("center:default"), FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("left:min:grow"), FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("15dlu"), FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, });
		formLayout.setRowGroups(new int[][] { new int[] { 4, 2 } });

		return formLayout;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Color c;

		// COG color button clicked
		if (e.getActionCommand().equals(COG_COLOR_BUTTON))
		{
			c = StyleEditorUtility.showColorPicker(this, this.coloredButton.getBackground());

			if (c != null)
				this.coloredButton.setBackground(c);
		}
	}

	/**
	 * 
	 * @return The color of the COG 'colored button'.
	 */
	public Paint getPaint()
	{
		return this.coloredButton.getPaint();
	}

	/**
	 * 
	 * @return The selected COG category.
	 */
	public String getCOGCategory()
	{
		return this.cogCategories.getSelectedItem().toString();
	}

	/**
	 * Sets the default color of the colored button.
	 * 
	 * @param p
	 *            The color to default the button to.
	 */
	public void setPaint(Paint p)
	{
		if (p instanceof Color)
		{
			this.coloredButton.setBackground((Color) p);
		}
		else
		{
			throw new IllegalArgumentException(p + " is not of type " + Color.class);
		}
	}

	/**
	 * Sets the COG category.
	 * 
	 * @param COG
	 *            The COG category to default to.
	 */
	public void setCOGCategory(String COG)
	{
		this.cogCategories.setSelectedItem(COG);
	}
}
