package ca.corefacility.gview.map.gui.editor.panel.propertyMapper;

import java.awt.Paint;

/**
 * This class represents a single COG category to color as a list item.
 * 
 * @author Eric Marinier
 */
public class COGToColorMapperListItem extends DiscreteMapperListItem
{
	private static final String COG = "COG Category";
	private static final String COLOR = "Color";
	
	private String cogCategory;	//the COG category string itself
	private Paint paint;	//the color associated with the COG category
	
	/**
	 * 
	 * @param cogCategory The COG category.
	 * @param paint The paint or color to associate with the COG category.
	 */
	public COGToColorMapperListItem(String cogCategory, Paint paint)
	{
		super();
		
		if(cogCategory == null)
			throw new IllegalArgumentException("COG Category is null.");
		
		if(paint == null)
			throw new IllegalArgumentException("Paint is null.");
		
		this.cogCategory = cogCategory;
		this.paint = paint;
		
		updateName();
	}
	
	/**
	 * 
	 * @return The paint or color associated with the COG category.
	 */
	public Paint getPaint()
	{
		if(paint == null)
			throw new IllegalArgumentException("Paint is null.");
		
		return this.paint;
	}
	
	/**
	 * 
	 * @return A representing the COG category. This is different than the name of the list item.
	 */
	public String getCOGCategory()
	{
		if(cogCategory == null)
			throw new IllegalArgumentException("COG Category is null.");
		
		return this.cogCategory;
	}
	
	/**
	 * Updates the name of the list item.
	 */
	private void updateName()
	{
		super.setName(COG + " ['" + cogCategory + "'] -> " + COLOR);
	}
	
	/**
	 * Sets the COG paint.
	 * 
	 * @param paint  The paint to set.
	 */
	public void setPaint(Paint paint)
	{
		this.paint = paint;
		
		updateName();
	}
	
	/**
	 * Sets the COG Category.
	 * 
	 * @param COG  The COG category.
	 */
	public void setCOGCategory(String COG)
	{
		this.cogCategory = COG;
		
		updateName();
	}
}

