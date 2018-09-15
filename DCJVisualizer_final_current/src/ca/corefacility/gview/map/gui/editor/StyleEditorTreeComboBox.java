package ca.corefacility.gview.map.gui.editor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;

/**
 * A customized combo box for style editor trees. Intended to contain only style editor tree objects 
 * and to be used within a style editor frame.
 * 
 * @author Eric Marinier
 *
 */
public class StyleEditorTreeComboBox extends JComboBox implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private static final String STYLE_TREE_COMBO_BOX = "Style Tree Combo Box";
	
	private final StyleEditorFrame styleEditorFrame;
	
	private boolean updating = false;
	
	/**
	 * 
	 * @param styleEditorFrame The related style editor frame.
	 */
	StyleEditorTreeComboBox(StyleEditorFrame styleEditorFrame)
	{
		this.styleEditorFrame = styleEditorFrame;
		
		this.addActionListener(this);
		this.setActionCommand(STYLE_TREE_COMBO_BOX);
		
		this.setRenderer(new ComboBoxRenderer());
	}
	
	@Override
	/**
	 * Allows only StyleEditorTree objects to be added.
	 */
	public void addItem(Object anObject)
	{
		if(anObject instanceof StyleEditorTree)
		{
			super.addItem(anObject);
		}
		else
		{
			throw new IllegalArgumentException("You may only add StyleEditorTree objects to a StyleEditorTreeComboBox.");
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object selectedItem = this.getSelectedItem();
			
		if(e.getActionCommand().equals(STYLE_TREE_COMBO_BOX) && !updating)
		{
			if(selectedItem instanceof StyleEditorTree)
				this.styleEditorFrame.setCurrentStyle((StyleEditorTree)selectedItem);
		}
	}
	
	/**
	 * Updates the selection of the combo box without firing an action event.
	 * 
	 * @param styleEditorTree The new selection.
	 */
	public void updateSelection(StyleEditorTree styleEditorTree)
	{
		this.updating = true;
		this.setSelectedItem(styleEditorTree);
		this.updating = false;
	}
	
	/**
	 * Updates the items in the combo box.
	 * Pulls styles from the style editor frame.
	 */
	public void update()
	{
		Iterator<StyleEditorTree> styles = this.styleEditorFrame.getStyleTrees();		
		ArrayList<StyleEditorTree> list = new ArrayList<StyleEditorTree>();
		
		this.updating = true;
		
		while(styles.hasNext())
		{
			list.add(styles.next());
		}
		
		Collections.sort(list);
		
		this.removeAllItems();	
		
		for(int i = 0; i < list.size(); i++)
		{
			this.addItem(list.get(i));
		}			
		
		this.updating = false;
	}
	
	/**
	 * An alternative to using toString()'s to display the name within the combo box.
	 * 
	 * @author Eric Marinier
	 *
	 */
	private class ComboBoxRenderer extends DefaultListCellRenderer
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus)
		{			
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			if(value instanceof StyleEditorTree)
			{
				this.setText(((StyleEditorTree)value).getStyleName());
			}
			
			revalidate();
			repaint();
	        
			return this;	
		}		
	}
}
