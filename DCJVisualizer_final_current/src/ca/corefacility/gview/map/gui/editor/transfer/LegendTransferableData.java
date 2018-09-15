package ca.corefacility.gview.map.gui.editor.transfer;

import java.io.Serializable;

/**
 * This class is a representation of the data required in a legend transfer.
 * 
 * @author Eric Marinier
 *
 */
public class LegendTransferableData implements Serializable
{
	private static final long serialVersionUID = 1L;	//requested by java
	
	private final int legendStyleIndex;	//the legend style index; the legend style the encases the legend item style
	private final int legendItemStyleIndex;	//the index of the legend item style, within a legend style
	
	/**
	 * 
	 * @param legendStyleIndex The encasing legend style's index. Must be >= 0.
	 * @param legendItemStyleIndex The index of the legend item style with its legend style. Must be >= 0.
	 */
	public LegendTransferableData(int legendStyleIndex, int legendItemStyleIndex)
	{
		if(legendStyleIndex < 0)
			throw new IllegalArgumentException("Legend style index is < 0.");
		
		if(legendItemStyleIndex < 0)
			throw new IllegalArgumentException("Legend item style index is < 0.");
		
		this.legendStyleIndex = legendStyleIndex;
		this.legendItemStyleIndex = legendItemStyleIndex;
	}
	
	/**
	 * 
	 * @return The legend style index.
	 */
	public int getLegendStyleIndex()
	{
		if(legendStyleIndex < 0)
			throw new IllegalArgumentException("Legend style index is < 0.");
		
		return this.legendStyleIndex;
	}
	
	/**
	 * 
	 * @return The legend item style index.
	 */
	public int getLegendItemStyleIndex()
	{
		if(legendItemStyleIndex < 0)
			throw new IllegalArgumentException("Legend item style index is < 0.");
		
		return this.legendItemStyleIndex;
	}
}
