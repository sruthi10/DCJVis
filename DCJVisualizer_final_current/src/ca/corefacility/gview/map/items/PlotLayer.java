package ca.corefacility.gview.map.items;

import java.awt.Color;

import ca.corefacility.gview.map.controllers.PlotStyleController;
import ca.corefacility.gview.map.controllers.PlotStyleToken;
import ca.corefacility.gview.style.datastyle.PlotStyle;

/**
 * This class is designed to allow an override of the fullPaint() method.
 * 
 * The only items that should ever be added to this layer are plot items.
 * 
 * @author Eric Marinier
 *
 */
public class PlotLayer extends OptimizedLayer
{
	private static final long serialVersionUID = 1L;
	
	private final PlotStyleToken plotStyleToken;
	
	/**
	 * 
	 * @param plotStyle The associated plot style for the plot.
	 */
	public PlotLayer(PlotStyle plotStyle)
	{
		this.plotStyleToken = new PlotStyleToken(plotStyle);
	}

	@Override
	public Color getConsensusColor()
	{
		return PlotStyleController.getConsensusColor(this.plotStyleToken);
	}
}
