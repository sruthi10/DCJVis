package ca.corefacility.gview.map.gui.editor.featureFilter;

import javax.swing.JPanel;

import ca.corefacility.gview.map.controllers.GenomeDataController;
import ca.corefacility.gview.map.gui.editor.AnnotationComboBox;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.border.TitledBorder;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JTextField;

public class AnnotationValueContainsPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private AnnotationComboBox annotations;
	private JTextField valueField;

	/**
	 * Create the panel.
	 */
	public AnnotationValueContainsPanel(GenomeDataController controller)
	{
		setBorder(new TitledBorder(null, "Annotation Value Contains", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
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
		
		JLabel lblAnnotation = new JLabel("Annotation:");
		panel_1.add(lblAnnotation, BorderLayout.EAST);
		
		this.annotations = new AnnotationComboBox(controller);
		add(this.annotations, "4, 2, fill, default");
		
		JPanel panel = new JPanel();
		add(panel, "2, 4, fill, fill");
		panel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblValue = new JLabel("Value:");
		panel.add(lblValue, BorderLayout.EAST);
		
		this.valueField = new JTextField();
		add(this.valueField, "4, 4, fill, default");
		this.valueField.setColumns(10);

	}
	
	/**
	 * 
	 * @return The annotation.
	 */
	public String getAnnotation()
	{
		return this.annotations.getSelectedString();
	}
	
	/**
	 * 
	 * @return The value text field.
	 */
	public String getValue()
	{
		return this.valueField.getText();
	}
}
