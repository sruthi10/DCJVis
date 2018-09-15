package ca.corefacility.gview.style;


import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ca.corefacility.gview.map.event.GViewEvent;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.event.GViewEventSubject;
import ca.corefacility.gview.map.event.GViewEventSubjectImp;
import ca.corefacility.gview.style.items.BackboneStyle;
import ca.corefacility.gview.style.items.LegendItemStyle;
import ca.corefacility.gview.style.items.LegendStyle;
import ca.corefacility.gview.style.items.RulerStyle;
import ca.corefacility.gview.style.items.TooltipStyle;

/**
 * Stores all the information would effect the appearance of the map independent of the data.  For example, controls color of background etc.
 * 
 * Note:  There are a number of variables here which aren't used currently.  The were included since these were the style variables that could be controled
 * 		in cgview.
 * @author Aaron Petkau
 *
 */
public class GlobalStyle implements GViewEventListener, GViewEventSubject
{
	// style data, move this around later
	private int defaultHeight = 700;
	private int defaultWidth = 700;
	
	// different styles we have access to
	private BackboneStyle backboneStyle;
	private RulerStyle rulerStyle;
	private TooltipStyle tooltipStyle;
	private List<LegendStyle> legendStyles;
	
	// TODO place these into there own classes
	//private double arrowheadLength; // = medium
	
	private Paint backgroundPaint = Color.white;
	//private Paint borderPaint = Color.black;
	
	private double slotSpacing = 4.0; // = medium
	//private double featureThickness; // = medium,  Max thickness
	
	//private double minimumFeatureLength = 0.5; // = xxx-small
	
	// this doesn't make sense for linear, possible change it
//	private int origin = 12; // clock position of sequence beginning
	
	private boolean showBorder = true;
	
	// private boolean showShading;  Add this to effects instead maybe
	
	private boolean showWarning = false;
	private Font warningFont = new Font("SansSerif", Font.PLAIN, 8);
	private Paint warningPaint = Color.BLACK;
	
	// TODO should I have name set in GenomeData instead?
	private String title = null;//"Untitled";
	private Paint titlePaint = null;
	private Font titleFont = new Font("SansSerif", Font.PLAIN, 12);
	
	// private boolean useColoredLabelBackgrounds = false; Do we need this?
	
	// private int userInnerLabels = auto;  Do I need this?
	
	
	// events
	private GViewEventSubjectImp eventSubject;
	
	/**
	 * Creates a new object used to define global styles.
	 */
	public GlobalStyle()
	{	
		// TODO see if I should create a new style here?  Or if I should
		//	have the user create new styles themselves
		backboneStyle = new BackboneStyle();
		rulerStyle = new RulerStyle();
//		labelStyle = new LabelStyle();
		tooltipStyle = new TooltipStyle();
		
		legendStyles = new LinkedList<LegendStyle>();
		
		eventSubject = new GViewEventSubjectImp();
		backboneStyle.addEventListener(this);
		rulerStyle.addEventListener(this);
		tooltipStyle.addEventListener(this);
	}
	
	public GlobalStyle(GlobalStyle globalStyle)
	{
		if (globalStyle == null)
		{
			throw new NullPointerException("globalStyle is null");
		}
		
		this.defaultHeight = globalStyle.defaultHeight;
		this.defaultWidth = globalStyle.defaultWidth;
		this.backboneStyle = new BackboneStyle(globalStyle.backboneStyle);
		this.rulerStyle = new RulerStyle(globalStyle.rulerStyle);
		this.tooltipStyle = new TooltipStyle(globalStyle.tooltipStyle);
		this.backgroundPaint = globalStyle.backgroundPaint;
		this.slotSpacing = globalStyle.slotSpacing;
		this.showBorder = globalStyle.showBorder;
		this.showWarning = globalStyle.showWarning;
		this.warningFont = globalStyle.warningFont;
		this.warningPaint = globalStyle.warningPaint;
		this.title = globalStyle.title;
		this.titlePaint = globalStyle.titlePaint;
		this.titleFont = globalStyle.titleFont;
		this.eventSubject = new GViewEventSubjectImp(globalStyle.eventSubject);
		
		if (globalStyle.legendStyles == null)
		{
			this.legendStyles = null;
		}
		else
		{
			this.legendStyles = (List<LegendStyle>)((LinkedList<LegendStyle>)globalStyle.legendStyles).clone();
		}
	}

	/**
	 * Gets the default height of the map (in pixels).
	 * @return  The default height of the map (in pixels).
	 */
	public int getDefaultHeight()
	{
		return defaultHeight;
	}

	/**
	 * Gets the default width of the map (in pixels).
	 * @return  The default width of the map (in pixels).
	 */
	public int getDefaultWidth()
	{
		return defaultWidth;
	}

	/**
	 * Sets default height of the map in pixels.
	 * @param height
	 */
	public void setDefaultHeight(int height)
	{
		this.defaultHeight = height;
	}

	/**
	 * Sets default width of the map (pixels).
	 * @param width
	 */
	public void setDefaultWidth(int width)
	{
		this.defaultWidth = width;
	}
	
	/**
	 * @return  The BackboneStyle used for this map.
	 */
	public BackboneStyle getBackboneStyle()
	{
		return this.backboneStyle;
	}
	
	/**
	 * @return  The style information used for the ruler.
	 */
	public RulerStyle getRulerStyle()
	{
		return rulerStyle;
	}

	/**
	 * @return  The paint used to paint the background.
	 */
	public Paint getBackgroundPaint()
	{
		return backgroundPaint;
	}

	/**
	 * Sets the background paint.
	 * @param backgroundPaint
	 */
	public void setBackgroundPaint(Paint backgroundPaint)
	{
		this.backgroundPaint = backgroundPaint;
	}

//	/**
//	 * @return
//	 */
//	public Paint getBorderPaint()
//	{
//		return borderPaint;
//	}
//
//	public void setBorderPaint(Paint borderPaint)
//	{
//		this.borderPaint = borderPaint;
//	}

//	public int getOrigin()
//	{
//		return origin;
//	}
//
//	public void setOrigin(int origin)
//	{
//		this.origin = origin;
//	}
//
	
	/**
	 * @return True if we should show the border, false otherwise (border is a border around picture).  Has no effect for now.
	 */
	public boolean showBorder()
	{
		return showBorder;
	}

	/**
	 * Sets whether we should show the border or not (has no effect right now).
	 * @param showBorder
	 */
	public void setShowBorder(boolean showBorder)
	{
		this.showBorder = showBorder;
	}

	/**
	 * @return True if we should show a warning message (message at bottom of map), false otherwise.  Has no effect for now.
	 */
	public boolean showWarning()
	{
		return showWarning;
	}

	/**
	 * Sets whether we should show warning messages or not. (no effect for now).
	 * @param showWarning
	 */
	public void setShowWarning(boolean showWarning)
	{
		this.showWarning = showWarning;
	}

	/**
	 * @return  The font of the warning message.
	 */
	public Font getWarningFont()
	{
		return warningFont;
	}

	/**
	 * Sets the font of the warning message.
	 * @param warningFont
	 */
	public void setWarningFont(Font warningFont)
	{
		this.warningFont = warningFont;
	}

	/**
	 * @return  The paint of the warning message.
	 */
	public Paint getWarningPaint()
	{
		return warningPaint;
	}

	/**
	 * Sets the paint of the warning message.
	 * @param warningPaint
	 */
	public void setWarningPaint(Paint warningPaint)
	{
		this.warningPaint = warningPaint;
	}

	/**
	 * @return  The paint of the title (title is not implemented yet).
	 */
	public Paint getTitlePaint()
	{
		return titlePaint;
	}

	/**
	 * Sets the paint of the title.
	 * @param titlePaint
	 */
	public void setTitlePaint(Paint titlePaint)
	{
		this.titlePaint = titlePaint;
	}

	/**
	 * @return  The font of the title.
	 */
	public Font getTitleFont()
	{
		return titleFont;
	}

	/**
	 * Sets the font of the title (title not implemented yet).
	 * @param titleFont
	 */
	public void setTitleFont(Font titleFont)
	{
		this.titleFont = titleFont;
	}

	/**
	 * @return  The title of the map.
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * Sets the title of the map (title not implemented yet).
	 * @param title
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * Used to access the tool tip style for the viewer.
	 * @return  The tool tip style.
	 */
	public TooltipStyle getTooltipStyle()
	{
		return tooltipStyle;
	}

	/**
	 * Used to set the tool tip style for the viewer.
	 * @param tooltipStyle  The tool tip style.
	 */
	public void setTooltipStyle(TooltipStyle tooltipStyle)
	{
		this.tooltipStyle = tooltipStyle;
	}

	/**
	 * Sets the backbone style for the map.
	 * @param backboneStyle
	 */
	public void setBackboneStyle(BackboneStyle backboneStyle)
	{
		this.backboneStyle = backboneStyle;
	}

	/**
	 * @return  The spacing used to separate slots.
	 */
	public double getSlotSpacing()
	{
		return slotSpacing;
	}

	/**
	 * Sets the spacing used to separate slots.
	 * @param slotSpacing  The spacing to use.  Should be >= 0.
	 */
	public void setSlotSpacing(double slotSpacing)
	{
		if (slotSpacing < 0)
		{
			throw new IllegalArgumentException("slot spacing is negative negative");
		}
		
		this.slotSpacing = slotSpacing;
	}

	public Iterator<LegendStyle> legendStyles()
	{
		return legendStyles.iterator();
	}

	public void addLegendStyle(LegendStyle legendStyle)
	{
		this.legendStyles.add(legendStyle);
	}
	
	public boolean removeLegendStyle(LegendStyle legendStyle)
	{
		boolean result = false;
		
		if(this.legendStyles == null)
		{
			throw new NullPointerException("LegendStyles is null.");
		}
		else if(legendStyle != null)
		{
			result = this.legendStyles.remove(legendStyle);
		}
		
		return result;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((backboneStyle == null) ? 0 : backboneStyle.hashCode());
		result = prime * result
				+ ((backgroundPaint == null) ? 0 : backgroundPaint.hashCode());
		result = prime * result + defaultHeight;
		result = prime * result + defaultWidth;
		result = prime * result
				+ ((eventSubject == null) ? 0 : eventSubject.hashCode());
		result = prime * result
				+ ((legendStyles == null) ? 0 : legendStyles.hashCode());
		result = prime * result
				+ ((rulerStyle == null) ? 0 : rulerStyle.hashCode());
		result = prime * result + (showBorder ? 1231 : 1237);
		result = prime * result + (showWarning ? 1231 : 1237);
		long temp;
		temp = Double.doubleToLongBits(slotSpacing);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result
				+ ((titleFont == null) ? 0 : titleFont.hashCode());
		result = prime * result
				+ ((titlePaint == null) ? 0 : titlePaint.hashCode());
		result = prime * result
				+ ((tooltipStyle == null) ? 0 : tooltipStyle.hashCode());
		result = prime * result
				+ ((warningFont == null) ? 0 : warningFont.hashCode());
		result = prime * result
				+ ((warningPaint == null) ? 0 : warningPaint.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GlobalStyle other = (GlobalStyle) obj;
		if (backboneStyle == null)
		{
			if (other.backboneStyle != null)
				return false;
		} else if (!backboneStyle.equals(other.backboneStyle))
			return false;
		if (backgroundPaint == null)
		{
			if (other.backgroundPaint != null)
				return false;
		} else if (!backgroundPaint.equals(other.backgroundPaint))
			return false;
		if (defaultHeight != other.defaultHeight)
			return false;
		if (defaultWidth != other.defaultWidth)
			return false;
		if (eventSubject == null)
		{
			if (other.eventSubject != null)
				return false;
		} else if (!eventSubject.equals(other.eventSubject))
			return false;
		if (legendStyles == null)
		{
			if (other.legendStyles != null)
				return false;
		} else if (!legendStyles.equals(other.legendStyles))
			return false;
		if (rulerStyle == null)
		{
			if (other.rulerStyle != null)
				return false;
		} else if (!rulerStyle.equals(other.rulerStyle))
			return false;
		if (showBorder != other.showBorder)
			return false;
		if (showWarning != other.showWarning)
			return false;
		if (Double.doubleToLongBits(slotSpacing) != Double
				.doubleToLongBits(other.slotSpacing))
			return false;
		if (title == null)
		{
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (titleFont == null)
		{
			if (other.titleFont != null)
				return false;
		} else if (!titleFont.equals(other.titleFont))
			return false;
		if (titlePaint == null)
		{
			if (other.titlePaint != null)
				return false;
		} else if (!titlePaint.equals(other.titlePaint))
			return false;
		if (tooltipStyle == null)
		{
			if (other.tooltipStyle != null)
				return false;
		} else if (!tooltipStyle.equals(other.tooltipStyle))
			return false;
		if (warningFont == null)
		{
			if (other.warningFont != null)
				return false;
		} else if (!warningFont.equals(other.warningFont))
			return false;
		if (warningPaint == null)
		{
			if (other.warningPaint != null)
				return false;
		} else if (!warningPaint.equals(other.warningPaint))
			return false;
		return true;
	}

	/**
	 * Event methods:  These were to be used so that we can send messages if any information from the style changes.
	 * 	This is not implemented yet.
	 */
	
	public void eventOccured(GViewEvent event)
	{
		// forward event to any listeners
		eventSubject.fireEvent(event);
	}

	public void addEventListener(GViewEventListener listener)
	{
		eventSubject.addEventListener(listener);
	}

	public void removeAllEventListeners()
	{
		eventSubject.removeAllEventListeners();
	}

	public void removeEventListener(GViewEventListener listener)
	{
		eventSubject.removeEventListener(listener);
	}

	/**
	 * Moves the source legend item style, under its source legend style, to the position of the 
	 * destination legend item style, under its respective destination legend style, and pushes the 
	 * destination legend item style and all appropriate legend item styles 'down'. The source 
	 * legend item style is removed from its source legend style and the nodes and 'pulled' together 
	 * as appropriate.
	 * 
	 * For example:
	 * 
	 * Initially:
	 * 
	 * Legend Style A:
	 * 		Legend Item Style 1
	 * 		Legend Item Style 2
	 * 		Legend Item Style 3
	 * 
	 * Legend Style B:
	 * 		Legend Item Style 4
	 * 		Legend Item Style 5
	 * 		Legend Item Style 6
	 * 
	 * 
	 * sourceLegendStyleIndex = A
	 * sourceLegendStyleIndex = 1
	 * destinationLegendStyleIndex = B
	 * destinationLegendItemStyleIndex = 5
	 * 
	 * Result:
	 * 
	 * Legend Style A:
	 * 		Legend Item Style 2
	 * 		Legend Item Style 3
	 * 
	 * Legend Style B:
	 * 		Legend Item Style 4
	 * 		Legend Item Style 1
	 * 		Legend Item Style 5
	 * 		Legend Item Style 6
	 * 
	 * @param sourceLegendStyleIndex
	 * @param sourceLegendItemStyleIndex
	 * @param destinationLegendStyleIndex
	 * @param destinationLegendItemStyleIndex
	 */
	public void moveLegendItemStyle(int sourceLegendStyleIndex,
			int sourceLegendItemStyleIndex, int destinationLegendStyleIndex,
			int destinationLegendItemStyleIndex)
	{
		LegendStyle sourceLegendStyle;
		LegendStyle destinationLegendStyle;
		
		LegendItemStyle sourceLegendItemStyle;
		
		if(sourceLegendStyleIndex < this.legendStyles.size() && destinationLegendStyleIndex < this.legendStyles.size()
				&& sourceLegendStyleIndex >= 0 && destinationLegendStyleIndex >= 0)	
		{
			sourceLegendStyle = this.legendStyles.get(sourceLegendStyleIndex);
			destinationLegendStyle = this.legendStyles.get(destinationLegendStyleIndex);
			
			if(sourceLegendStyle != null && destinationLegendStyle != null)
			{
				sourceLegendItemStyle = sourceLegendStyle.getLegendItemStyle(sourceLegendItemStyleIndex);
				
				if(sourceLegendItemStyle != null)
				{
					sourceLegendStyle.removeLegendItem(sourceLegendItemStyle);
					
					if(sourceLegendStyleIndex == destinationLegendStyleIndex && sourceLegendItemStyleIndex <= destinationLegendItemStyleIndex)
					{
						//adjust the destination index because of the remove (one less item now)
						//but ONLY if the source is less than or equal to the destination
						destinationLegendStyle.insertLegendItem(sourceLegendItemStyle, destinationLegendItemStyleIndex - 1);
					}
					else
					{
						//normal
						destinationLegendStyle.insertLegendItem(sourceLegendItemStyle, destinationLegendItemStyleIndex);
					}
				}
				else
				{
					throw new IllegalArgumentException("The source legend item style cannot be null.");
				}
			}
			else
			{
				throw new IllegalArgumentException("Source and destination legend styles may not be null.");
			}
		}
		else
		{
			throw new IllegalArgumentException("Source and destination legend style indices out of bounds.");
		}
	}
}
