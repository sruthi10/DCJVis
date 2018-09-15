package ca.corefacility.gview.map.gui.editor;

import javax.swing.JPanel;
import javax.swing.JLabel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * The score to opacity panel.
 * 
 * This panel is intended to exist on the property mapper chooser dialog.
 * 
 * @author Eric Marinier
 *
 */
public class ScoreToOpacityPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private JTextField minScore;
	private JTextField maxScore;

	/**
	 * Create the panel.
	 */
	public ScoreToOpacityPanel()
	{
		setBorder(new TitledBorder(null, "Score to Opacity", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));		
		
		setLayout(createLayout());
		
		JLabel lblMinScore = new JLabel("Min. Score:");
		add(lblMinScore, "2, 2, right, default");
		
		minScore = new JTextField();
		add(minScore, "4, 2, fill, default");
		minScore.setColumns(10);
		
		JLabel lblMaxScore = new JLabel("Max. Score:");
		add(lblMaxScore, "2, 4, right, default");
		
		maxScore = new JTextField();
		add(maxScore, "4, 4, fill, default");
		maxScore.setColumns(10);
	}
	
	/**
	 * 
	 * @return The specific form layout for this panel.
	 */
	private FormLayout createLayout()
	{
		FormLayout formLayout = new FormLayout(new ColumnSpec[] {
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
				FormFactory.DEFAULT_ROWSPEC,});
		
		return formLayout;
	}
	
	/**
	 * 
	 * @return A string containing the contents of the min text tfield.
	 */
	public String getMin()
	{
		return this.minScore.getText();
	}
	
	/**
	 * 
	 * @return A string containing the contents of the max text field.
	 */
	public String getMax()
	{
		return this.maxScore.getText();
	}
	
	/**
	 * Sets the min text field.
	 * 
	 * @param min
	 */
	public void setMin(float min)
	{
		this.minScore.setText(Float.toString(min));
	}
	
	/**
	 * Sets the max text field.
	 * @param max
	 */
	public void setMax(float max)
	{
		this.maxScore.setText(Float.toString(max));
	}
}
