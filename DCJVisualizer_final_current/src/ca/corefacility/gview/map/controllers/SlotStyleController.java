package ca.corefacility.gview.map.controllers;

import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Iterator;

import ca.corefacility.gview.layout.feature.FeatureShapeRealizer;
import ca.corefacility.gview.map.effects.ShapeEffectRenderer;
import ca.corefacility.gview.style.datastyle.DataStyle;
import ca.corefacility.gview.style.datastyle.FeatureHolderStyle;
import ca.corefacility.gview.style.datastyle.PlotStyle;
import ca.corefacility.gview.style.datastyle.SlotItemStyle;
import ca.corefacility.gview.style.datastyle.SlotStyle;
import ca.corefacility.gview.textextractor.FeatureTextExtractor;
import ca.corefacility.gview.utils.Util;

/**
 * This class is responsible for controlling access to the slot styles.
 * 
 * @author Eric Marinier
 *
 */
public class SlotStyleController extends Controller
{
    public static final Color DEFAULT_SLOT_COLOR = new Color(200, 200, 200);    
    public static final double DEFAULT_THICKNESS = 12.0;
    
	public static final FeatureTextExtractor DEFAULT_EXTRACTOR = FeatureTextExtractor.BLANK;
	public static final FeatureShapeRealizer DEFAULT_REALIZER = FeatureShapeRealizer.NO_ARROW;
	public static final ShapeEffectRenderer DEFAULT_RENDERER = ShapeEffectRenderer.STANDARD_RENDERER;
    
	public static final int BACKBONE_SLOT_NUMBER = 0;
	
	private final StyleController styleController;
	
	private final DataStyle dataStyle; 
	
	public SlotStyleController(StyleController styleController, DataStyle dataStyle)
	{
		this.styleController = styleController;
		this.dataStyle = dataStyle;
	}
	
	/**
	 * 
	 * @return The number of slots.
	 */
	public int getNumSlots()
	{
		Iterator<SlotStyle> slots = this.dataStyle.slots();
		
		int count = 0;
		
		while (slots.hasNext())
		{
			count++;
			slots.next();
		}
		
		return count;
	}
	
	/**
	 * 
	 * @return The slots.
	 */
	public ArrayList<SlotStyleToken> getSlots()
	{
		Iterator<SlotStyle> slotStyles = this.dataStyle.slots();
		ArrayList<SlotStyleToken> tokens = new ArrayList<SlotStyleToken>();
		
		while(slotStyles.hasNext())
		{
			tokens.add(new SlotStyleToken(slotStyles.next()));
		}
		
		return tokens;
	}
	
	/**
	 * 
	 * @param parent The associated slot style.
	 * @return The slot items contained in the slot style.
	 */
	public ArrayList<SlotItemStyleToken> getSlotItems(SlotStyleToken parent)
	{
		SlotStyle style = parent.getStyle();
		Iterator<SlotItemStyle> set = style.styles();
		
		ArrayList<SlotItemStyleToken> tokens = new ArrayList<SlotItemStyleToken>();
		
		SlotItemStyle current;
		
		while(set.hasNext())
		{
			current = set.next();
			
			if(current instanceof PlotStyle)
			{
				tokens.add(new PlotStyleToken((PlotStyle)current));
			}
			else if(current instanceof FeatureHolderStyle)
			{
				tokens.add(new FeatureHolderStyleToken((FeatureHolderStyle)current));
			}
		}
		
		return tokens;
	}
	
	/**
	 * 
	 * @param parent The associated slot item style.
	 * @return The slot items contained in the slot style.
	 */
	public ArrayList<SlotItemStyleToken> getSlotItems(SlotItemStyleToken parent)
	{
		SlotItemStyle style = parent.getStyle();
		SlotItemStyle current;
		
		Iterator<SlotItemStyle> set;
		
		ArrayList<SlotItemStyleToken> tokens = new ArrayList<SlotItemStyleToken>();
		
		//Only concerned with feature holder styles.
		if(style instanceof FeatureHolderStyle)
		{
			set = ((FeatureHolderStyle)style).styles();
					
			while(set.hasNext())
			{
				current = set.next();
				
				if(current instanceof PlotStyle)
				{
					tokens.add(new PlotStyleToken((PlotStyle)current));
				}
				else if(current instanceof FeatureHolderStyle)
				{
					tokens.add(new FeatureHolderStyleToken((FeatureHolderStyle)current));
				}
			}
		}
		
		return tokens;
	}
	
	/**
	 * 
	 * @return The uppermost slot number.
	 */
	public int getUpperSlotNumber()
	{
		return this.dataStyle.getUpperSlot();
	}
	
	/**
	 * 
	 * @return The lowermost slot number.
	 */
	public int getLowerSlotNumber()
	{
		return this.dataStyle.getLowerSlot();
	}
	
	/**
	 * Attempts to create a slot at the specified slot location.
	 * 
	 * @param slotNumber A valid slot location. 
	 * 		Will 'push' other slots outwards from the backbone if necessary.
	 * 		A valid location can be checked by calling: validNewSlotNumber(slotNumber).
	 * 
	 * @return The create slot style as a token or NULL if the location was invalid.
	 */
	public SlotStyleToken createSlot(int slotNumber)
	{
		SlotStyle slotStyle;
		SlotStyleToken token = null;
		
		boolean valid = validNewSlotNumber(slotNumber);
		
		if(valid)
		{
			slotStyle = dataStyle.createSlotStyle(slotNumber);
			token = new SlotStyleToken(slotStyle);
			
			this.applyDefaults(slotStyle);
			
			this.styleController.notifyFullRebuildRequired();
		}		

		return token;
	}
	
	/**
	 * Applies the default styling attributes to the slot style.
	 * 
	 * @param style The style to apply the defaults to.
	 */
	private void applyDefaults(SlotStyle style)
	{
		style.setPaint(DEFAULT_SLOT_COLOR);
		style.setThickness(DEFAULT_THICKNESS);
	}
	
	/**
	 * Verifies if the slot number is valid.
	 * 
	 * @param slotNumber The slot number to verify.
	 * 
	 * @return Whether or not the slot number is valid.
	 */
	public boolean validNewSlotNumber(int slotNumber)
	{		
		boolean result = true;
		//inclusive
		int lowerBounds = getLowerSlotNumber() - 1;
		int upperBounds = getUpperSlotNumber() + 1;
		
		if(slotNumber < lowerBounds || slotNumber > upperBounds || slotNumber == 0)
		{
			result = false;
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param token The associated slot style.
	 * @return The thickness of the slot of the default slot thickness if negative.
	 */
	public double getThickness(SlotStyleToken token)
	{
		SlotStyle style = token.getStyle();
		
		double thickness = style.getThickness();
		
		if(thickness < 0)
		{
			thickness = 0;
		}
		
		return thickness;
	}
	
	/**
	 * 
	 * @param token The associated slot style.
	 * @return The feature text extractor or the default extractor if NULL.
	 */
	public FeatureTextExtractor getTextExtractor(SlotStyleToken token)
	{
		SlotStyle style = token.getStyle();
		
		FeatureTextExtractor extractor = style.getToolTipExtractor();
		
		if(extractor == null)
		{
			extractor = DEFAULT_EXTRACTOR;
		}
		
		return extractor;
	}
	
	/**
	 * 
	 * @param token The associated slot style.
	 * @return The feature shape realizer or the default realizer if NULL.
	 */
	public FeatureShapeRealizer getFeatureShapeRealizer(SlotStyleToken token)
	{
		SlotStyle style = token.getStyle();
		
		FeatureShapeRealizer realizer = style.getFeatureShapeRealizer();
		
		if(realizer == null)
		{
			realizer = DEFAULT_REALIZER;
		}
		
		return realizer;
	}
	
	/**
	 * 
	 * @param token The associated slot style.
	 * @return The shape effect renderer or the default realizer if NULL.
	 */
	public ShapeEffectRenderer getShapeEffectRenderer(SlotStyleToken token)
	{
		SlotStyle style = token.getStyle();
		
		ShapeEffectRenderer renderer = style.getShapeEffectRenderer();
		
		if(renderer == null)
		{
			renderer = DEFAULT_RENDERER;
		}
		
		return renderer;
	}
	
	/**
	 * 
	 * @param token The associated slot style.
	 * @return The color of the slot or the default color if NULL. 
	 * 		Specifically, when new sets are added to the slot, this is the color that will be applied to them. 
	 */
	public Paint getColor(SlotStyleToken token)
	{
		SlotStyle style = token.getStyle();
		
		Paint color = style.getPaint();
		
		if(color == null)
		{
			color = DEFAULT_SLOT_COLOR;
		}
		
		return color;
	}
	
	/**
	 * 
	 * @param token The associated slot style.
	 * @param thickness The new thickness of the slot.
	 */
	public void setThickness(SlotStyleToken token, double thickness)
	{
		SlotStyle style = token.getStyle();
		
		if(thickness < 0)
		{
			thickness = 0;
		}
		
		if(style.getThickness() != thickness)
		{
			style.setThickness(thickness);
			
			this.styleController.notifyFullRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @param token The associated slot style.
	 * @param extractor The new extractor for tool tips.
	 */
	public void setTextExtractor(SlotStyleToken token, FeatureTextExtractor extractor)
	{
		SlotStyle style = token.getStyle();
		
		if(extractor == null)
		{
			extractor = DEFAULT_EXTRACTOR;
		}
		
		if(!Util.isEqual(style.getToolTipExtractor(), extractor))
		{
			style.setToolTipExtractor(extractor);
			
			this.styleController.notifyFullRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @param token The associated slot style.
	 * @param realizer The new feature shape realizer to use.
	 */
	public void setFeatureShapeRealizer(SlotStyleToken token, FeatureShapeRealizer realizer)
	{
		SlotStyle style = token.getStyle();
		
		if(realizer == null)
		{
			realizer = DEFAULT_REALIZER;
		}
		
		if(!Util.isEqual(style.getFeatureShapeRealizer(), realizer))
		{
			style.setFeatureShapeRealizer(realizer);
			
			this.styleController.notifyFullRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @param token The associated slot style.
	 * @param renderer The new shape effect renderer to use.
	 */
	public void setShapeEffectRenderer(SlotStyleToken token, ShapeEffectRenderer renderer)
	{
		SlotStyle style = token.getStyle();
		
		if(renderer == null)
		{
			renderer = DEFAULT_RENDERER;
		}
		
		if(!Util.isEqual(style.getShapeEffectRenderer(), renderer))
		{
			style.setShapeEffectRenderer(renderer);
		
			this.styleController.notifyFullRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @param token The associated slot style.
	 * @param color The new color.
	 */
	public void setColor(SlotStyleToken token, Paint color)
	{
		SlotStyle style = token.getStyle();
		
		if(color == null)
		{
			color = DEFAULT_SLOT_COLOR;
		}
		
		if(!Util.isEqual(style.getPaint(), color))
		{
			style.setPaint(color);
			
			this.styleController.notifyFullRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @param token The associated slot style.
	 * @return Whether or not the slot has a property mapper.
	 */
	public boolean hasPropertyMapper(SlotStyleToken token)
	{
		SlotStyle style = token.getStyle();
		boolean result;
		
		if(style.getPropertyStyleMapper() == null)
		{
			result = false;
		}
		else
		{
			result = true;
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param token The associated slot style.
	 * @return The slot's number.
	 */
	public int getSlotNumber(SlotStyleToken token)
	{
		SlotStyle style = token.getStyle();
		
		return style.getSlot();
	}
	
	/**
	 * 
	 * @param slotToken The associated slot style.
	 * @return The newly created plot.
	 */
	public PlotStyleToken createPlot(SlotStyleToken slotToken)
	{
		PlotStyle plotStyle = new PlotStyle();
		PlotStyleToken plotToken = new PlotStyleToken(plotStyle);
		
		SlotStyle slotStyle = slotToken.getStyle();
		slotStyle.addStyleItem(plotStyle);
		
		PlotStyleController.setPlotStyleDefaults(plotStyle);
		
		this.styleController.notifyFullRebuildRequired();
		
		return plotToken;
	}
	
	/**
	 * 
	 * @param token The slot style to remove.
	 */
	public void removeSlot(SlotStyleToken token)
	{
		SlotStyle style = token.getStyle();
		
		this.dataStyle.removeSlotStyle(style);
		
		this.styleController.notifyFullRebuildRequired();
	}
	
	/**
	 * 
	 * @param slotToken The parent slot style.
	 * @param setToken The set to remove.
	 */
	public void removeSet(SlotStyleToken slotToken, FeatureHolderStyleToken setToken)
	{
		SlotStyle slotStyle = slotToken.getStyle();
		FeatureHolderStyle setStyle = setToken.getStyle();
		
		slotStyle.removeSlotItemStyle(setStyle);
		
		this.styleController.notifyFullRebuildRequired();
	}
	
	/**
	 * 
	 * @param source
	 * @param destination
	 * @return Whether or not the move was successful.
	 */
	public boolean moveSlot(int source, int destination)
	{
		boolean result = false;
		
		if(source != BACKBONE_SLOT_NUMBER && validNewSlotNumber(source) && validNewSlotNumber(destination) && source != destination)
		{
			this.dataStyle.moveSlot(source, destination);			
			result = true;
			
			this.styleController.notifyFullRebuildRequired();
		}
		
		return result;
	}
	
	/**
	 * See DataStyle.moveBackbone(backbone) for implementation details.
	 * 
	 * @param destination The destination location for the backbone.
	 * @return Whether or not the move was successful.
	 */
	public boolean moveBackbone(int destination)
	{
		boolean result = false;
		
		if(validNewSlotNumber(destination))
		{
			this.dataStyle.moveBackbone(destination);			
			result = true;
			
			this.styleController.notifyFullRebuildRequired();
		}
		
		return result;
	}

	/**
	 * 
	 * @param token
	 * @return A color to represent the entire slot.
	 */
	public static Color getConsensusColor(SlotStyleToken token)
	{
		SlotStyle style = token.getStyle();
		
		Iterator<SlotItemStyle> sets = style.styles();
		SlotItemStyle firstSet;
		
		Paint[] paints;
		Paint paint;
		Color color;
		
		//Get the paint of the set:
		if(sets.hasNext())
		{
			firstSet = sets.next();
			paints = firstSet.getPaint();
			
			if(paints.length > 0)
			{
				paint = paints[0];
			}
			else
			{
				paint = Color.WHITE;
			}
		}	
		else
		{
			paint = DEFAULT_SLOT_COLOR;
		}
		
		//Convert to a color:
		if(paint instanceof Color)
		{
			color = (Color)paint;
		}
		else
		{
			color = DEFAULT_SLOT_COLOR;
		}
		
		return color;
	}
}
