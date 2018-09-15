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

public class OverlapsLocationPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private JTextField startField;
	private JTextField endField;

	/**
	 * Create the panel.
	 */
	public OverlapsLocationPanel()
	{
		setBorder(new TitledBorder(null, "Overlaps Location", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
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
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JPanel panel_1 = new JPanel();
		add(panel_1, "2, 2, fill, fill");
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JLabel lblAnnotation = new JLabel("Start:");
		panel_1.add(lblAnnotation, BorderLayout.EAST);
		
		this.startField = new JTextField();
		add(this.startField, "4, 2, fill, default");
		this.startField.setColumns(10);
		
		JPanel panel = new JPanel();
		add(panel, "2, 4, fill, fill");
		panel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblValue = new JLabel("End:");
		panel.add(lblValue, BorderLayout.EAST);
		
		this.endField = new JTextField();
		add(this.endField, "4, 4, fill, default");
		this.endField.setColumns(10);
	}
	
	/**
	 * 
	 * @return The start field text.
	 */
	public String getStart()
	{
		return this.startField.getText();
	}
	
	/**
	 * 
	 * @return The end field text.
	 */
	public String getEnd()
	{
		return this.endField.getText();
	}
}
