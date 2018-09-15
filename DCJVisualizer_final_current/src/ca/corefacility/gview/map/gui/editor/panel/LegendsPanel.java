package ca.corefacility.gview.map.gui.editor.panel;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * The panel for the legends node.
 * 
 * The legends node is intended to group together the individual legend style nodes.
 * 
 * @author Eric Marinier
 *
 */
public class LegendsPanel extends StylePanel
{
	private static final long serialVersionUID = 1L;	//requested by java
	
	private static final String LEGENDS_TEXT = "Legends";
	
	private static final String MESSAGE = 
			"<html>" +
			"<u>Overview</u>" +
			"<br>" +
			"<br>" +
			"There are two legend components: the <i>Legend Boxes</i> and their <i>Legend Texts</i>. <i>Legend Boxes</i> represent the legend as a whole and determine where the legend will appear and what overall appearance it will have. <i>Legend Texts</i> represent the items that exist within the legend." +
			"<br>" +
			"<br>" +
			"<u>Creating Legend Boxes and Legend Texts</u>" +
			"<br>" +
			"<br>" +
			"Legend Boxes may be created by right clicking the \"Legends\" node in the style tree and selecting \"New Legend Box\". Legend Texts may be created within a Legend Box by right clicking the appropriate Legend Box node in the style tree and selecting \"New Legend Text\"." +
			"<br>" + 
			"<br>" + 
			"<u>Removing Legend Boxes and Legend Texts</u>" +
			"<br>" +
			"<br>" +
			"Legend Boxes may be removed by right clicking them and selecting \"Remove Legend Box\". Legend Texts may be removed by right clicking them and selecting \"Remove Legend Text\"." +
			"<br>" + 
			"<br>" + 
			"<u>Reordering Legend Texts</u>" + 
			"<br>" + 
			"<br>" + 
			"Legend Texts may be moved by dragging and dropping the individual Legend Text nodes. Legend Texts may be moved from one Legend Box to an entirely different Legend Box." +
			"<br>" + 
			"<br>" + 
			"<u>Recoloring Legend Text Swatches</u>" + 
			"<br>" + 
			"<br>" + 
			"A Legend Text swatch may be automatically colored to match any set. This can be done by first applying all changes to the set and then dragging and dropping the set onto the appropriate Legend Text." +
			"</html>";
	
	public LegendsPanel()
	{
		setBorder(new EmptyBorder(10, 10, 10, 10));
		
		JPanel inner = new JPanel();
		inner.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)),  LEGENDS_TEXT, TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		add(inner, BorderLayout.NORTH);
		inner.setLayout(new BoxLayout(inner, BoxLayout.X_AXIS));
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		inner.add(panel);
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("1dlu:grow"),},
			new RowSpec[] {
				RowSpec.decode("fill:pref:grow"),}));
	
		JLabel label = new JLabel(MESSAGE);
		panel.add(label, "1, 1, left, top");
	}

	@Override
	public void update()
	{
	}

	@Override
	protected void doApply()
	{
	}
}
