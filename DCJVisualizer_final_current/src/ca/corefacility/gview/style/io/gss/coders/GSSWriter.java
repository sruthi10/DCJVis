package ca.corefacility.gview.style.io.gss.coders;


import java.awt.Paint;
import java.io.PrintWriter;
import java.io.Writer;

import ca.corefacility.gview.style.io.gss.PaintHandler;
import ca.corefacility.gview.style.io.gss.PaintHandler.UnknownPaintException;

public class GSSWriter
{
	private PrintWriter writer;
	
	public GSSWriter(PrintWriter writer)
	{
		this.writer = writer;
	}
	
	public GSSWriter(Writer writer)
	{
		this(new PrintWriter(writer));
	}
	
	public void writeProperty(String propertyName, String propertyValue)
	{
		writer.print("\t" + propertyName + " : ");
		
		writer.print(propertyValue);
		
		writer.println(";");
	}
	
	/**
	 * Gives the selector name for the passed "element" name, with the passed id
	 * @param elementName  The name of the element for this selector.
	 * @param id  The id of the selector.
	 * @return  The concatenated string describing the name of this selector.
	 */
	public String getSelectorName(String elementName, String id)
	{
		return elementName + "#" + id;
	}
	
	/**
	 * Returns the selector name for the passed "base" selector name, and the passed "descendant" selector name
	 * @param baseSelectorName  The name of the base selector.
	 * @param descendant  The name of the selector that is descendant from this base selector.
	 * @return  A String describing the concatenated name.
	 */
	public String getDescendantName(String baseSelectorName, String descendant)
	{
		return baseSelectorName + " " + descendant;
	}
	
	public void writePropertyPaint(String propertyName, Paint propertyValue)
	{
		writer.print("\t" + propertyName + " : ");
		
		try
		{
			writer.print(PaintHandler.encode(propertyValue));
		}
		catch (UnknownPaintException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writer.println(";");
	}
	
	public void endSelector()
	{
		writer.println("}");
	}
	
	public void startSelector(String selectorName)
	{
		writer.println();
		writer.println(selectorName);
		writer.println("{");
	}
	
	public void startSelector(String selectorName, String id)
	{
		writer.println();
		writer.println(getSelectorName(selectorName, id));
		writer.println("{");
	}
}
