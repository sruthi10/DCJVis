package ca.corefacility.gview.map.gui.editor.featureFilter;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import ca.corefacility.gview.map.controllers.GenomeDataController;
import ca.corefacility.gview.map.gui.editor.AnnotationComboBox;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class HasAnnotationPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private final AnnotationComboBox annotations;

	/**
	 * Create the panel.
	 */
	public HasAnnotationPanel(GenomeDataController controller)
	{
		setBorder(new TitledBorder(null, "Has Annotation", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
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
		
		JLabel lblAnnotation = new JLabel("Annotation:");
		panel_1.add(lblAnnotation, BorderLayout.EAST);
		
		this.annotations = new AnnotationComboBox(controller);
		add(this.annotations, "4, 2, fill, default");
	}

	/**
	 * 
	 * @return The annotation.
	 */
	public String getAnnotation()
	{
		return this.annotations.getSelectedString();
	}
}
