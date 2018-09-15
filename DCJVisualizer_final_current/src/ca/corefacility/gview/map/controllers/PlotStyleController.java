package ca.corefacility.gview.map.controllers;

import java.awt.Color;
import java.awt.Paint;
import java.util.Arrays;

import ca.corefacility.gview.style.datastyle.PlotBuilderType;
import ca.corefacility.gview.style.datastyle.PlotDrawerType;
import ca.corefacility.gview.style.datastyle.PlotStyle;
import ca.corefacility.gview.style.datastyle.SlotStyle;
import ca.corefacility.gview.utils.Util;

/**
 * This class is responsible for controlling all access to the plot style by GUI elements.
 * 
 * @author Eric Marinier
 *
 */
public class PlotStyleController extends Controller
{
	public static final Color DEFAULT_GRID_COLOR = Color.BLACK;
    public static final Color DEFAULT_UPPER_COLOR = new Color(44, 46, 92);
    public static final Color DEFAULT_LOWER_COLOR = new Color(35, 74, 34);
	public static final Color DEFAULT_PAINT[] = {DEFAULT_UPPER_COLOR, DEFAULT_LOWER_COLOR};
	
	public static final int DEFAULT_MINIMUM = -1;
	public static final int DEFAULT_MAXIMUM = 1;
	public static final int DEFAULT_NUM_GRID_LINES = 1;
	
	public static final boolean DEFAULT_AUTOSCALE = true;
	
	public static final PlotBuilderType DEFAULT_BUILDER_TYPE = new PlotBuilderType.GCContent();
	public static final PlotDrawerType DEFAULT_DRAWER_TYPE = new PlotDrawerType.Center();
	
	static
	{
		DEFAULT_BUILDER_TYPE.autoScale(DEFAULT_AUTOSCALE);
	}
	
	private final StyleController styleController;
	
	public PlotStyleController(StyleController styleEditorController)
	{
		this.styleController = styleEditorController;
	}
	
	/**
	 * 
	 * @param token The associated plot style.
	 * @return The plot builder object type for the plot.
	 */
	public PlotBuilderType getPlotBuilderType(PlotStyleToken token)
	{
		PlotStyle style = token.getPlotStyle();
		PlotBuilderType type = style.getPlotBuilderType();
		
		if(type == null)
		{
			type = DEFAULT_BUILDER_TYPE;
		}
		
		return type;
	}
	
	/**
	 * 
	 * @param token The associated plot style.
	 * @return The plot drawer object type for the plot.
	 */
	public PlotDrawerType getPlotDrawerType(PlotStyleToken token)
	{
		PlotStyle style = token.getPlotStyle();
		PlotDrawerType type = style.getPlotDrawerType();
		
		if(type == null)
		{
			type = DEFAULT_DRAWER_TYPE;
		}
		
		return type;
	}
	
	/**
	 * 
	 * @param token The associated plot style.
	 * @return The number of grid lines or zero if negative.
	 */
	public int getGridLines(PlotStyleToken token)
	{
		PlotStyle style = token.getPlotStyle();
		int gridLines = style.getGridLines();
		
		if(gridLines < 0)
		{
			gridLines = 0;
		}
		
		return gridLines;
	}
	
	/**
	 * 
	 * @param token The associated plot style.
	 * @return The grid color or the default grid color if NULL.
	 */
	public Paint getGridColor(PlotStyleToken token)
	{
		PlotStyle style = token.getPlotStyle();
		Paint color = style.getGridPaint();
		
		if(color == null)
		{
			color = DEFAULT_GRID_COLOR;
		}
		
		return color;
	}
	
	/**
	 * 
	 * @param token The associated plot style.
	 * @return The plot upper color.
	 */
	public Paint getUpperColor(PlotStyleToken token)
	{
		PlotStyle style = token.getPlotStyle();
		Paint[] colors = style.getPaint();
		Paint color;
		
		if(colors == null || colors.length < 1)
		{
			color = DEFAULT_UPPER_COLOR;
		}
		else
		{
			color = colors[0];
			
			if(color == null)
			{
				color = DEFAULT_UPPER_COLOR;
			}
		}
		
		return color;
	}
	
	/**
	 * 
	 * @param token The associated plot style.
	 * @return The plot lower color.
	 */
	public Paint getLowerColor(PlotStyleToken token)
	{
		PlotStyle style = token.getPlotStyle();
		Paint[] colors = style.getPaint();
		Paint color;
		
		if(colors == null || colors.length < 2)
		{
			color = DEFAULT_LOWER_COLOR;
		}
		else
		{
			color = colors[1];
			
			if(color == null)
			{
				color = DEFAULT_LOWER_COLOR;
			}
		}
		
		return color;
	}	
	
	/**
	 * 
	 * @param builderType The new builder type for the plot.
	 * @param token The associated plot style.
	 */
	public void setPlotBuilderType(PlotBuilderType builderType, PlotStyleToken token)
	{
		PlotStyle style = token.getPlotStyle();
		
		if(builderType == null)
		{
			builderType = DEFAULT_BUILDER_TYPE;
		}
		
		if(!Util.isEqual(style.getPlotBuilderType(), builderType))
		{
			style.setPlotBuilderType(builderType);
			
			this.styleController.notifyFullRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @param drawerType The new drawer type for the plot.
	 * @param token The associated plot style.
	 */
	public void setPlotDrawerType(PlotDrawerType drawerType, PlotStyleToken token)
	{
		PlotStyle style = token.getPlotStyle();
		
		if(drawerType == null)
		{
			drawerType = DEFAULT_DRAWER_TYPE;
		}
		
		if(!Util.isEqual(style.getPlotDrawerType(), drawerType))
		{
			style.setPlotDrawerType(drawerType);
			
			this.styleController.notifyFullRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @param gridLines The new number of grid lines in the plot.
	 * @param token The associated plot style.
	 */
	public void setGridLines(int gridLines, PlotStyleToken token)
	{
		PlotStyle style = token.getPlotStyle();
		
		if(gridLines < 0)
		{
			gridLines = 0;
		}
		
		if(style.getGridLines() != gridLines)
		{
			style.setGridLines(gridLines);
			
			this.styleController.notifyFullRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @param color The new color of the grid.
	 * @param token The associated plot style.
	 */
	public void setGridColor(Paint color, PlotStyleToken token)
	{
		PlotStyle style = token.getPlotStyle();
		
		if(color == null)
		{
			color = DEFAULT_GRID_COLOR;
		}
		
		if(!Util.isEqual(style.getGridPaint(), color))
		{
			style.setGridPaint(color);
			
			this.notifyRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @param color The new plot upper color.
	 * @param token The associated plot style.
	 */
	public void setUpperColor(Paint color, PlotStyleToken token)
	{
		PlotStyle style = token.getPlotStyle();		
		Paint[] colors = style.getPaint();
		
		if(color == null)
		{
			color = DEFAULT_UPPER_COLOR;
		}
		
		if(colors == null || colors.length != 2)
		{
			colors = new Paint[]{null, DEFAULT_LOWER_COLOR};
		}
		
		if(!Util.isEqual(colors[0], color))
		{
			colors[0] = color;
			this.setColors(colors, token);
			
			this.notifyRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @param color The new plot lower color.
	 * @param token The associated plot style.
	 */
	public void setLowerColor(Paint color, PlotStyleToken token)
	{
		PlotStyle style = token.getPlotStyle();		
		Paint[] colors = style.getPaint();
		
		if(color == null)
		{
			color = DEFAULT_LOWER_COLOR;
		}
		
		if(colors == null || colors.length != 2)
		{
			colors = new Paint[]{DEFAULT_UPPER_COLOR, null};
		}
		
		if(!Util.isEqual(colors[1], color))
		{
			colors[1] = color;
			this.setColors(colors, token);
			
			this.notifyRebuildRequired();
		}		
	}
	
	/**
	 * 
	 * @param colors The new colors for the plot.
	 * @param token The associated plot style.
	 */
	public void setColors(Paint[] colors, PlotStyleToken token)
	{
		PlotStyle style = token.getPlotStyle();
		
		if(colors == null || colors.length == 0)
		{
			colors = new Paint[]{DEFAULT_UPPER_COLOR, DEFAULT_LOWER_COLOR};
		}
		
		if(!Arrays.deepEquals(style.getPaint(), colors))
		{
			style.setPaint(colors);
			
			this.notifyRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @param slotToken The slot to remove the plot from.
	 * @param plotToken The plot to remove.
	 */
	public void removePlot(SlotStyleToken slotToken, PlotStyleToken plotToken)
	{
		SlotStyle slotStyle = slotToken.getStyle();
		PlotStyle plotStyle = plotToken.getPlotStyle();
		
		slotStyle.removeSlotItemStyle(plotStyle);
		
		this.styleController.notifyFullRebuildRequired();
	}
	
	/**
	 * Sets the passed plot style's internal components to default.
	 * 
	 * @param plotStyle The plot style to set to default.
	 */
	public static void setPlotStyleDefaults(PlotStyle plotStyle)
	{
		plotStyle.setPlotBuilderType(DEFAULT_BUILDER_TYPE);
		plotStyle.setPlotDrawerType(DEFAULT_DRAWER_TYPE);
		plotStyle.setGridLines(DEFAULT_NUM_GRID_LINES);
		plotStyle.setGridPaint(DEFAULT_GRID_COLOR);
		plotStyle.setPaint(DEFAULT_PAINT);
	}
	
	/**
	 * 
	 * @param plotToken The associated plot.
	 * 
	 * @return The minimum value of the pot.
	 */
	public int getMinimum(PlotStyleToken plotToken)
	{
		PlotBuilderType builder = this.getPlotBuilderType(plotToken);
		
		return builder.getMinimum();
	}
	
	/**
	 * 
	 * @param plotToken The associated plot.
	 * 
	 * @return The maximum value of the pot.
	 */
	public int getMaximum(PlotStyleToken plotToken)
	{
		PlotBuilderType builder = this.getPlotBuilderType(plotToken);
		
		return builder.getMaximum();
	}
	
	/**
	 * 
	 * @param plotToken The associated plot.
	 * 
	 * @return Whether or not auto-scaling of the plot is enabled.
	 */
	public boolean getAutoScale(PlotStyleToken plotToken)
	{
		PlotBuilderType builder = this.getPlotBuilderType(plotToken);
		
		return builder.getAutoScale();
	}
	
	/**
	 * 
	 * @param token
	 * @return A color to represent the entire plot.
	 */
	public static Color getConsensusColor(PlotStyleToken token)
	{
		PlotStyle style = token.getPlotStyle();
		Paint[] paints = style.getPaint();
		Paint paint;
		Color color;
		
		if(paints == null || paints.length < 1)
		{
			paint = DEFAULT_UPPER_COLOR;
		}
		else
		{
			paint = paints[0];
			
			if(paint == null)
			{
				paint = DEFAULT_UPPER_COLOR;
			}
		}
		
		if(paint instanceof Color)
		{
			color = (Color)paint;
		}
		else
		{
			color = DEFAULT_UPPER_COLOR;
		}
		
		return color;
	}
}

