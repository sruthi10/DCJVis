package ca.corefacility.gview.map.gui.editor.featureFilter;

import javax.swing.JPanel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.border.TitledBorder;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JTextField;

public class SequenceNamePanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private JTextField sequenceNameField;

	/**
	 * Create the panel.
	 */
	public SequenceNamePanel()
	{
		setBorder(new TitledBorder(null, "Sequence Name", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
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
		
		JLabel lblAnnotation = new JLabel("Name:");
		panel_1.add(lblAnnotation, BorderLayout.EAST);
		
		this.sequenceNameField = new JTextField();
		add(this.sequenceNameField, "4, 2, fill, default");
		this.sequenceNameField.setColumns(10);
	}

	/**
	 * 
	 * @return The sequence name field text.
	 */
	public String getSequenceName()
	{
		return this.sequenceNameField.getText();
	}
}
