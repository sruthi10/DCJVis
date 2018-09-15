package ca.corefacility.gview.map.gui.editor.featureFilter;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import ca.corefacility.gview.map.controllers.GenomeDataController;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class ByTypePanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private JComboBox types;

	/**
	 * Create the panel.
	 */
	public ByTypePanel(GenomeDataController genomeDataController)
	{
		setBorder(new TitledBorder(null, "By Type", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JPanel panel_1 = new JPanel();
		add(panel_1, "2, 2, fill, fill");
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JLabel lblAnnotation = new JLabel("Type:");
		panel_1.add(lblAnnotation, BorderLayout.EAST);
		
		ArrayList<String> types = genomeDataController.getTypes();
		Collections.sort(types);
		
		this.types = new JComboBox(types.toArray());
		add(this.types, "4, 2, fill, default");
	}

	/**
	 * 
	 * @return Returns the type field text.
	 */
	public String getType()
	{
		return this.types.getSelectedItem().toString();
	}
}
