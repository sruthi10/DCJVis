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

public class GSSStringPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private JTextField gssField;

	/**
	 * Create the panel.
	 */
	public GSSStringPanel()
	{
		setBorder(new TitledBorder(null, "GSS String", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
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
		
		JLabel lblAnnotation = new JLabel("GSS String:");
		panel_1.add(lblAnnotation, BorderLayout.EAST);
		
		this.gssField = new JTextField();
		add(this.gssField, "4, 2, fill, default");
		this.gssField.setColumns(10);
	}

	/**
	 * 
	 * @return The gss field text.
	 */
	public String getGSS()
	{
		return this.gssField.getText();
	}
}
