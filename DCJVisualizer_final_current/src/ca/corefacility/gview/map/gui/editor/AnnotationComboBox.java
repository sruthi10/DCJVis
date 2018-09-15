package ca.corefacility.gview.map.gui.editor;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;

import ca.corefacility.gview.map.controllers.GenomeDataController;

/**
 * A customized combo box for displaying annotations. Intended to contain only
 * annotation keys.
 * 
 * @author Eric Marinier
 * 
 */
public class AnnotationComboBox extends JComboBox
{
	private static final long serialVersionUID = 1L;

	private final DefaultComboBoxModel model = new DefaultComboBoxModel();

	private final GenomeDataController genomeDataController;

	public AnnotationComboBox(GenomeDataController genomeDataController)
	{
		this.setModel(model);
		this.setRenderer(new AnnotationRenderer());

		this.genomeDataController = genomeDataController;

		this.initialize();
	}

	/**
	 * Initializes the combo box.
	 */
	private void initialize()
	{
		ArrayList<String> annotations = this.genomeDataController.getAnnotationsAsStrings();

		this.updateAnnotations(annotations);
	}

	/**
	 * Adds an annotation in sorted order.
	 * 
	 * Implementation note: This uses a theta(n) algorithm, where n is the
	 * number of items in the list; because of limitations.
	 * 
	 * @param annotation
	 */
	public void addAnnotation(String annotation)
	{
		Object current;
		ArrayList<String> annotations = new ArrayList<String>();

		for (int i = 0; i < this.getItemCount(); i++)
		{
			current = this.getItemAt(i);

			if (current instanceof String)
			{
				annotations.add((String) current);
			}
		}

		annotations.add(annotation);

		this.updateAnnotations(annotations);
	}

	/**
	 * Updates the annotations.
	 * 
	 * This will remove all items in the combo box and add the passed
	 * annotations in sorted order.
	 * 
	 * @param annotations
	 */
	private void updateAnnotations(ArrayList<String> annotations)
	{
		this.removeAllItems();

		Collections.sort(annotations);

		for (String annotation : annotations)
		{
			this.addItem(annotation);
		}
	}

	@Override
	public void setSelectedItem(Object item)
	{
		if (this.containsItem(item))
		{
			super.setSelectedItem(item);
		}
	}

	/**
	 * 
	 * @param item
	 * @return Whether or not the combo box contains the item.
	 */
	public boolean containsItem(Object item)
	{
		boolean result = false;

		if (this.model.getIndexOf(item) >= 0)
		{
			result = true;
		}

		return result;
	}

	/**
	 * 
	 * @return The selected item as a String.
	 */
	public String getSelectedString()
	{
		String result;

		Object item = super.getSelectedItem();

		if (item instanceof String)
		{
			result = ((String) item);
		}
		else
		{
			result = "";
		}

		return result;
	}

	private class AnnotationRenderer extends DefaultListCellRenderer
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus)
		{
			String text = value.toString();

			Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			if (component instanceof JLabel)
			{
				if (text != null && text.length() > 20)
				{
					((JLabel) component).setText(text.substring(0, 17) + "...");
				}
			}

			return component;
		}

	}
}
