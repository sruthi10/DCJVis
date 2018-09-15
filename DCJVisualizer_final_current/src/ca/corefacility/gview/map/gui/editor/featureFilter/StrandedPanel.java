package ca.corefacility.gview.map.gui.editor.featureFilter;

import java.awt.BorderLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * This class is intended to be used on the FeatureFilterChooserDialog class.
 * 
 * @author Eric Marinier
 *
 */
public class StrandedPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private final JComboBox strandedComboBox;

	/**
	 * Create the panel.
	 */
	public StrandedPanel()
	{
		setBorder(new TitledBorder(null, "Stranded", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
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
		
		JLabel lblAnnotation = new JLabel("Stranded:");
		panel_1.add(lblAnnotation, BorderLayout.EAST);
		
		this.strandedComboBox = new JComboBox(FeatureFilterChooserDialog.STRANDED);
		add(this.strandedComboBox, "4, 2, fill, default");
	}

	/**
	 * 
	 * @return The stranded combo box's current selection.
	 */
	public String getStranded()
	{
		if(!(this.strandedComboBox.getSelectedItem() instanceof String))
			throw new ClassCastException("StrandedComboBox should contain only Strings.");
		
		return (String)this.strandedComboBox.getSelectedItem();
	}
}
