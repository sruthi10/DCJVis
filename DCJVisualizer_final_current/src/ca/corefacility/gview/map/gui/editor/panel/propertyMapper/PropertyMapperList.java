package ca.corefacility.gview.map.gui.editor.panel.propertyMapper;

import java.awt.Color;
import java.awt.Component;
import java.awt.Paint;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListCellRenderer;

import ca.corefacility.gview.map.gui.editor.ScoreToOpacityMapperListItem;

/**
 * This class is a customization of JList that is intended to contain only property mapper list items
 * and render them appropriately.
 * 
 * This is intended to be used on a property mapper panel.
 * 
 * @author Eric Marinier
 *
 */
public class PropertyMapperList extends JList
{
	private static final long serialVersionUID = 1L;	//requested by java
	private static final int NUM_ROWS_VISIBLE = 8;	//the number of rows to have the list show
	
	private final DefaultListModel listModel;	//the list model associated with the propertyMapperList

	public PropertyMapperList()
	{
		this.listModel = new DefaultListModel();
		this.setModel(this.listModel);
		this.setCellRenderer(new PropertyMapperRenderer());
		
		this.setVisibleRowCount(NUM_ROWS_VISIBLE);	
	}
	
	/**
	 * Removes all the list items from the list.
	 */
	public void removeAllElements()
	{
		this.listModel.removeAllElements();
	}
	
	/**
	 * Adds the passed element to the list.
	 * 
	 * @param element The element to add.
	 * @return Whether or not it was successful.
	 */
	public boolean addElement(PropertyMapperListItem element)
	{
		return this.addElement(this.listModel.getSize(), element);
	}
	
	/**
	 * Adds the passed element to the list.
	 * 
	 * @param index The location to insert the item into the list.
	 * @param element The element to insert.
	 * @return Whether or not the add was successful.
	 */
	public boolean addElement(int index, PropertyMapperListItem element)
	{
		boolean result = false;
		
		if(index < 0 || index > this.listModel.size())
			throw new ArrayIndexOutOfBoundsException("Index out of bounds.");
		
		if(!this.containsDuplicate(element))
		{			
			//is the list consistent?
			if(this.getListConsistency(element.getClass()))
			{
				this.listModel.add(index, element);
				result = true;
			}
			else
			{
				JOptionPane.showMessageDialog(this, "Cannot add property mapper.");
				result = false;
			}
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Duplicate property mapper exists.");
			result = false;
		}
		
		return result;
	}
	
	/**
	 * Determines whether or not the list is empty.
	 * 
	 * @return Whether or not the list is empty.
	 */
	public boolean isEmpty()
	{
		return this.listModel.isEmpty();
	}
	
	/**
	 * Returns the PropertyMapperListItem at the specific index.
	 * 
	 * @param index The index of the item to return. Expected to a valid index.
	 * 
	 * @return The PropertyMapperListItem located at the specified index.
	 */
	public PropertyMapperListItem get(int index)
	{
		Object result = this.listModel.get(index);
		
		if(!(result instanceof PropertyMapperListItem))
			throw new ClassCastException("Non-PropertyMapperListItem inside of PropertyMapperList.");
		
		return (PropertyMapperListItem)result;
	}
	
	/**
	 * 
	 * @return The number of elements that are in the list.
	 */
	public int getListSize()
	{
		return this.listModel.getSize();
	}
	
	/**
	 * Removes the passed element from this list.
	 * 
	 * @param element The element to remove.
	 */
	public void removeElement(PropertyMapperListItem element)
	{
		this.listModel.removeElement(element);
	}
	
	/**
	 * Determines whether or not this list is consistent. 
	 * More specifically, that every item in this list are of the same class type.
	 * 
	 * If the list has no elements, it will return true. If the list contains only elements that 
	 * are all the same as the passed listClassType, it will return true. If the list contains
	 * any elements that have a different class type, it will return false.
	 * 
	 * @param listClassType The class type to check consistency for.
	 * @return Whether or not the list is consistent.
	 */
	public boolean getListConsistency(Class<?> listClassType)
	{
		boolean result = true;
		Object current;
		
		for(int i = 0; i < this.getListSize() && result == true; i++)
		{
			current = this.get(i);
			
			if(!listClassType.equals(current.getClass()))
			{
				result = false;
			}
		}
		
		return result;
	}
	
	/**
	 * Determines whether or not adding the list item would make the list contain a 'duplicate', which is dependent on the type of list item.
	 * 
	 * Example: Two score to opacity list items. Two COGs of the same letter.
	 * 
	 * @return Whether or not there's a duplicate.
	 */
	private boolean containsDuplicate(PropertyMapperListItem listItem)
	{
		boolean result = false;
		
		if(listItem instanceof ScoreToOpacityMapperListItem)
		{
			result = containsDuplicate((ScoreToOpacityMapperListItem) listItem);
		}
		else if(listItem instanceof COGToColorMapperListItem)
		{
			result = containsDuplicate((COGToColorMapperListItem) listItem);
		}
		
		return result;
	}
	
	/**
	 * Determines whether or not this list contains another score to opacity mapper list item.
	 * 
	 * @param listItem
	 * @return True if the list contains another score to opacity mapper item, false otherwise.
	 */
	private boolean containsDuplicate(ScoreToOpacityMapperListItem listItem)
	{
		boolean result = false;
		
		for(int i = 0; i < this.listModel.getSize() && result == false; i++)
		{
			if(this.listModel.get(i) instanceof ScoreToOpacityMapperListItem)
			{
				result = true;
			}
		}
		
		return result;
	}
	
	/**
	 * Determines whether or not any items in the list are COG to color mapper list items
	 * and share the same COG category.
	 * 
	 * @param listItem
	 * @return  True if there was a duplicate item, false otherwise.
	 */
	private boolean containsDuplicate(COGToColorMapperListItem listItem)
	{
		boolean result = false;
		
		String cogCategory;
		
		for(int i = 0; i < this.listModel.getSize() && result == false; i++)
		{
			if(this.listModel.get(i) instanceof COGToColorMapperListItem)
			{
				cogCategory = ((COGToColorMapperListItem)this.listModel.get(i)).getCOGCategory();
				
				if(cogCategory.equals(listItem.getCOGCategory()))
				{
					result = true;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Attempts to replace the target list item with the source list item.
	 * Both are expected to be non-null.
	 * 
	 * @param source The source list item that is being inserted into the list.
	 * @param target The destination list item that is being removed from the list, but its position taken.
	 * @return The success of the replacement.
	 */
	public boolean replace(PropertyMapperListItem source, PropertyMapperListItem target)
	{
		boolean result = false;
		int targetIndex;
		
		//check for null
		if(source == null || target == null)
			return false;
		
		//is the target in the list? && is the item to add consistent?
		if(this.listModel.contains(target) && getListConsistency(source.getClass()))
		{
			targetIndex = this.listModel.indexOf(target);
			this.listModel.removeElement(target);			
			
			//will adding it create a duplicate?
			if(!containsDuplicate(source))
			{
				result = addElement(targetIndex, source);	//add the source
			}
			else
			{
				this.listModel.add(targetIndex, target);	//re-add the removed target
				result = false;
			}
		}
		else
		{
			result = false;
		}
		
		return result;
	}

	/**
	 * A custom renderer class for displaying the list items.
	 * 
	 * @author Eric Marinier
	 *
	 */
	private class PropertyMapperRenderer extends JLabel implements ListCellRenderer
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus)
		{
			if(!(value instanceof PropertyMapperListItem))
				throw new IllegalArgumentException("PropertyMapperList contains non-PropertyMapperListItem.");
			
			String s = ((PropertyMapperListItem)value).getName();
	        setText(s);
	        setEnabled(list.isEnabled());	       
	        setOpaque(true);
	        
	        if(value instanceof COGToColorMapperListItem)
	        //cog to color item
	        {
	        	Paint cogColor = (Paint) ((COGToColorMapperListItem)value).getPaint();
	        	boolean brighterForeground = false;
	        	
	        	//a 'somewhat' arbitrary way of deciding whether the text should be default or white
	        	if(cogColor instanceof Color && ((Color)cogColor).getRed() + ((Color)cogColor).getBlue() +
	        	        ((Color)cogColor).getGreen() < 255 && ((Color)cogColor).getAlpha() == 255)
	        	{
	        		brighterForeground = true;
	        	}
	        	
	        	if (isSelected)
	        	//selected
	        	{
	        	    if (cogColor instanceof Color)
	        	    {
	        	        setBackground(((Color)cogColor).darker());
	        	    }
	        	    else
	        	    {
	        	        setBackground(Color.darkGray);
	        	    }
	        		
	        		if(brighterForeground)
	        		{
	        			setForeground(Color.GRAY);
	        		}
	        		else
	        		{
	        			setForeground(list.getSelectionForeground());
	        		}
	 	        } 
	        	else 
	        	//not selected
	        	{
	        	    if (cogColor instanceof Color)
	        	    {
	        	        setBackground((Color)cogColor);
	        	    }
	        		
	        		if(brighterForeground)
	        		{
	        			setForeground(Color.WHITE);
	        		}
	        		else
	        		{
	        			setForeground(list.getForeground());
	        		}
	 	        }	        	
	        }
	        else
	        //'normal' item
	        {
	        	setFont(list.getFont());
	        	
	        	if (isSelected) 
	        	{
	 	            setBackground(list.getSelectionBackground());
	 	            setForeground(list.getSelectionForeground());
	 	        } 
	        	else 
	 	        {
	 	            setBackground(list.getBackground());
	 	            setForeground(list.getForeground());
	 	        }
	        }
	        
			return this;	
		}		
	}
}
