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
 * The panel for the slots node.
 * The slots node is intended to group together the individual slot nodes.
 * 
 * @author Eric Marinier
 *
 */
public class SlotsPanel extends StylePanel
{
	private static final long serialVersionUID = 1L;	//requested by java
	
	private static final String SLOTS_TEXT = "Slots";
	
	private static final String MESSAGE = 
			"<html>" +
			"<u>Overview</u>" +
			"<br>" +
			"<br>" +
			"A slot defines a track region along the backbone. These track regions are stacked on top of each other and are used as containers for information such as sequence feature sets or plots." +
			"<br>" +
			"<br>" +
			"<u>Creating Slots</u>" +
			"<br>" +
			"<br>" +
			"Slots may be created either by right clicking the \"Slots\" node in the style tree and selecting \"New Slot\" or by selecting \"Style\" > \"New\" > \"Slot\" from within the Style Editor. You will be prompted for a slot number. A valid slot number is any non-zero integer within the range of the current slots extended by one, on both sides." +
			"<br>" + 
			"<br>" + 
			"<u>Removing Slots</u>" +
			"<br>" +
			"<br>" +
			"Slots may be removed by right clicking them and selecting \"Remove Slot\". If the removed slot is not one of the outer slots, then the surrounding slots with collapse inwards towards the backbone." +
			"<br>" + 
			"<br>" + 
			"<u>Moving Slots</u>" + 
			"<br>" + 
			"<br>" + 
			"Slots may be moved by dragging and dropping the individual slot nodes." + 
			"</html>";
	
	public SlotsPanel()
	{
		setBorder(new EmptyBorder(10, 10, 10, 10));
		
		JPanel inner = new JPanel();
		inner.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)),  SLOTS_TEXT, TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
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
