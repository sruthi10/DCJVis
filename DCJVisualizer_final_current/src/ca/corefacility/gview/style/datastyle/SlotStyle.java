package ca.corefacility.gview.style.datastyle;


import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Iterator;

import org.biojava.bio.seq.FeatureFilter;

import ca.corefacility.gview.data.Slot;
import ca.corefacility.gview.layout.feature.FeatureShapeRealizer;
import ca.corefacility.gview.map.effects.ShapeEffectRenderer;
import ca.corefacility.gview.style.datastyle.mapper.PropertyStyleMapper;
import ca.corefacility.gview.textextractor.FeatureTextExtractor;

/**
 * Defines a class for the style information for a particular slot.  A slot is a ring/tract around the backbone where features and such can be placed into.
 * 
 * @author Aaron Petkau
 */
public class SlotStyle implements StyleHolder, Cloneable, FeatureContainerStyle
{
	private int slot;
	private FeatureHolderStyleFactory factory; // used to create featureholderstyles

	private FeatureShapeRealizer shapeRealizer = FeatureShapeRealizer.NO_ARROW;

	private double thickness = 20;
	private Paint paint = Color.BLUE;
	private float transparency = 1.0f; // should I have this here, since transparency can be set in the paint.

	private ShapeEffectRenderer shapeEffectRenderer = ShapeEffectRenderer.STANDARD_RENDERER;

	private FeatureTextExtractor toolTipExtractor = FeatureTextExtractor.BLANK;
	private FeatureTextExtractor hyperlinkExtractor;

	//private boolean showShading = true; // TODO probably want to have something about general effects

	private ArrayList<SlotItemStyle> styles;

	private LabelStyle labelStyle = new LabelStyle(); // used to define how to display labels within this slot
	
	private PropertyStyleMapper propertyStyleMapper = null;

	/**
	 * Creates a new SlotStyle with the passed parameters.
	 * @param slot  The slot number this SlotStyle applies to.
	 * @param factory  A FeatureHolderStyleFactory used to assign unique id's to FeatureHolderstyles created in this slot.
	 */
	protected SlotStyle(int slot, FeatureHolderStyleFactory factory) // do I want this public?
	{
		if(!Slot.validSlot(slot))
			throw new IllegalArgumentException("slot=" + slot + ", is invalid");
		else if (factory == null)
			throw new NullPointerException("factory is null");

		this.slot = slot;

		this.styles = new ArrayList<SlotItemStyle>();
		this.factory = factory;

		this.labelStyle.setShowLabels(false); // defaults to not showing labels
	}

	/**
	 * @return  The slot number this style applies to.
	 */
	public int getSlot()
	{
		return this.slot;
	}

	/**
	 * Gets the thickness of this slot.
	 * @return  The thickness of this slot.
	 */
	public double getThickness()
	{
		return this.thickness;
	}

	/**
	 * Sets the thickness of this slot.
	 * @param thickness  The thickness, must be non-negative.
	 */
	public void setThickness(double thickness)
	{
		if (thickness < 0)
			throw new IllegalArgumentException("thickness is negative");

		this.thickness = thickness;
	}

	/**
	 * @return  The tool tip extractor to be used by all features in this slot (can be overriden in FeatureHolderStyle).
	 */
	public FeatureTextExtractor getToolTipExtractor()
	{
		return this.toolTipExtractor;
	}

	/**
	 * Sets the tool tip extractor to be used by features in this slot.  Note:  setting this value after you have already created a FeatureHolderStyle
	 * 	under this SlotStyle will NOT change the value in the FeatureHolderStyle.
	 * @param toolTipExtractor
	 */
	public void setToolTipExtractor(FeatureTextExtractor toolTipExtractor)
	{
		if (toolTipExtractor == null)
			throw new NullPointerException("toolTipExtractor is null");

		this.toolTipExtractor = toolTipExtractor;
	}

	/**
	 * @return  The default paint of any items in this slot.
	 */
	public Paint getPaint()
	{
		return this.paint;
	}

	/**
	 * Sets the default paint for any items in this slot.
	 * @param paint  The paint, null for no paint.
	 */
	public void setPaint(Paint paint)
	{
		this.paint = paint;
	}

	/**
	 * @return  The FeatureShapeRealizer to render the shapes of features in this slot.
	 */
	public FeatureShapeRealizer getFeatureShapeRealizer()
	{
		return this.shapeRealizer;
	}

	/**
	 * Sets the FeatureShapeRealizer to render shapes of features in this slot.
	 * @param shapeRealizer
	 */
	public void setFeatureShapeRealizer(FeatureShapeRealizer shapeRealizer)
	{
		if (shapeRealizer == null)
			throw new NullPointerException("shapeRealizer is null");

		this.shapeRealizer = shapeRealizer;
	}

	/**
	 * @return  The transparency to use to paint features in this slot.
	 */
	public float getTransparency()
	{
		return this.transparency;
	}

	/**
	 * Sets the transparency to use to paint features in this slot.
	 * @param transparency  The transparency, in range [0,1]
	 */
	public void setTransparency(float transparency)
	{
		if (0.0f <= transparency && transparency <= 1.0f)
		{
			this.transparency = transparency;
		}
		else
			throw new IllegalArgumentException("transparency=" + transparency + " out of range of [0.0f,1.0f]");
	}

	public LabelStyle getLabelStyle()
	{
		return this.labelStyle;
	}

	public PropertyStyleMapper getPropertyStyleMapper()
	{
		return propertyStyleMapper;
	}

	public void setPropertyStyleMapper(PropertyStyleMapper propertyStyleMapper)
	{
		this.propertyStyleMapper = propertyStyleMapper;
	}
	
	/**
	 * Removes the property mapper from the slot style.
	 */
	public boolean removePropertyStyleMapper()
	{
		this.propertyStyleMapper = null;
		
		return true;
	}

	/**
	 * @return  The ShapeEffectRenderer used to color/draw the feature shapes.
	 */
	public ShapeEffectRenderer getShapeEffectRenderer()
	{
		return this.shapeEffectRenderer;
	}

	/**
	 * Sets the FeatureShapeRenderer used to color/draw the feature shapes.
	 * @param shapeEffectRenderer
	 */
	public void setShapeEffectRenderer(ShapeEffectRenderer shapeEffectRenderer)
	{
		if (shapeEffectRenderer != null)
		{
			this.shapeEffectRenderer = shapeEffectRenderer;
		}
		else
			throw new NullPointerException("shapeEffectRenderer is null");
	}

	/**
	 * Creates a style in this slot with the passed filter.
	 * 
	 * @param filter  The filter to use for the style.
	 * 
	 * @return  The newly created FeatureHolderStyle.
	 */
	public FeatureHolderStyle createFeatureHolderStyle(FeatureFilter filter)
	{
		FeatureHolderStyle style = null;

		if (filter == null)
			throw new NullPointerException("filter is null");

		style = this.factory.createFeatureHolderStyle(filter, null);
		style.setPaint(this.paint);
		style.setTransparency(this.transparency);
		style.setShapeEffectRenderer(this.shapeEffectRenderer);
		style.setFeatureShapeRealizer(this.shapeRealizer);
		style.setToolTipExtractor(this.toolTipExtractor);
		style.setLabelStyle((LabelStyle)this.labelStyle.clone());
		style.setHyperlinkExtractor(this.hyperlinkExtractor);
		style.setPropertyStyleMapper(this.propertyStyleMapper);

		this.styles.add(style);

		return style;
	}

	// TODO refactor this
	public void addStyleItem(SlotItemStyle slotItem)
	{
		this.styles.add(slotItem);
	}

	// obtains featureholderstyle matching the passed feature filter
	// TODO should this be changed to return a list of matching styles?
	public FeatureHolderStyle getFeatureHolderStyle(FeatureFilter filter)
	{
		FeatureHolderStyle featureHolderStyle = null;

		if (filter != null)
		{
			// we now extract the FeatureHolderStyle
			Iterator<SlotItemStyle> stylesIter = styles();
			while (stylesIter.hasNext() && featureHolderStyle == null)
			{
				SlotItemStyle itemHolderStyle = stylesIter.next();

				if (itemHolderStyle instanceof FeatureHolderStyle)
				{
					FeatureHolderStyle fhs = (FeatureHolderStyle)itemHolderStyle;

					if (fhs.hasFeatureFilter(filter))
					{
						featureHolderStyle = fhs;
					}
				}
			}
		}

		return featureHolderStyle;
	}

	/*
	public void addStyle(ItemHolderStyle style)
	{
		if (style != null)
		{
			styles.add(style);
		}
	}*/

	@Override
	public Object clone()
	{
		SlotStyle clonedStyle = new SlotStyle(this.slot, (FeatureHolderStyleFactory)this.factory.clone());

		clonedStyle.slot = this.slot;
		clonedStyle.paint = this.paint;
		clonedStyle.shapeEffectRenderer = this.shapeEffectRenderer;
		clonedStyle.shapeRealizer = this.shapeRealizer;
		clonedStyle.thickness = this.thickness;
		clonedStyle.toolTipExtractor = this.toolTipExtractor;
		clonedStyle.transparency = this.transparency;
		clonedStyle.styles = (ArrayList<SlotItemStyle>)styles.clone(); //(Set<SlotItemStyle>)((HashSet<SlotItemStyle>)this.styles).clone();
		clonedStyle.labelStyle = this.labelStyle; // TODO should we clone some of these internal variables instead?
		clonedStyle.hyperlinkExtractor = this.hyperlinkExtractor;
		clonedStyle.propertyStyleMapper = this.propertyStyleMapper;

		return clonedStyle;
	}


	public Iterator<SlotItemStyle> styles()
	{
		return this.styles.iterator();
	}

	@Override
	public boolean containsFeatureHolderStyle(FeatureFilter filter)
	{
		Iterator<SlotItemStyle> styles = styles();

		while(styles.hasNext())
		{
			SlotItemStyle style = styles.next();

			if (style instanceof FeatureHolderStyle)
			{
				FeatureHolderStyle holderStyle = (FeatureHolderStyle)style;

				if (holderStyle.hasFeatureFilter(filter))
					return true;

			}
		}

		return false;
	}

	public FeatureTextExtractor getHyperlinkExtractor()
	{
		return hyperlinkExtractor;
	}

	public void setHyperlinkExtractor(FeatureTextExtractor hyperlinkExtractor)
	{
		this.hyperlinkExtractor = hyperlinkExtractor;
	}


	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((hyperlinkExtractor == null) ? 0 : hyperlinkExtractor
						.hashCode());
		result = prime * result
				+ ((labelStyle == null) ? 0 : labelStyle.hashCode());
		result = prime * result + ((paint == null) ? 0 : paint.hashCode());
		result = prime * result
				+ ((propertyStyleMapper == null) ? 0 : propertyStyleMapper.hashCode());
		result = prime
				* result
				+ ((shapeEffectRenderer == null) ? 0 : shapeEffectRenderer
						.hashCode());
		result = prime * result
				+ ((shapeRealizer == null) ? 0 : shapeRealizer.hashCode());
		result = prime * result + slot;
		result = prime * result + ((styles == null) ? 0 : styles.hashCode());
		long temp;
		temp = Double.doubleToLongBits(thickness);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime
				* result
				+ ((toolTipExtractor == null) ? 0 : toolTipExtractor.hashCode());
		result = prime * result + Float.floatToIntBits(transparency);
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
		SlotStyle other = (SlotStyle) obj;
		if (hyperlinkExtractor == null)
		{
			if (other.hyperlinkExtractor != null)
				return false;
		}
		else if (!hyperlinkExtractor.equals(other.hyperlinkExtractor))
			return false;
		if (labelStyle == null)
		{
			if (other.labelStyle != null)
				return false;
		}
		else if (!labelStyle.equals(other.labelStyle))
			return false;
		if (paint == null)
		{
			if (other.paint != null)
				return false;
		}
		else if (!paint.equals(other.paint))
			return false;
		if (propertyStyleMapper == null)
		{
			if (other.propertyStyleMapper != null)
				return false;
		}
		else if (!propertyStyleMapper.equals(other.propertyStyleMapper))
			return false;
		if (shapeEffectRenderer == null)
		{
			if (other.shapeEffectRenderer != null)
				return false;
		}
		else if (!shapeEffectRenderer.equals(other.shapeEffectRenderer))
			return false;
		if (shapeRealizer == null)
		{
			if (other.shapeRealizer != null)
				return false;
		}
		else if (!shapeRealizer.equals(other.shapeRealizer))
			return false;
		if (slot != other.slot)
			return false;
		if (styles == null)
		{
			if (other.styles != null)
				return false;
		}
		else if (!styles.equals(other.styles))
			return false;
		if (Double.doubleToLongBits(thickness) != Double
				.doubleToLongBits(other.thickness))
			return false;
		if (toolTipExtractor == null)
		{
			if (other.toolTipExtractor != null)
				return false;
		}
		else if (!toolTipExtractor.equals(other.toolTipExtractor))
			return false;
		if (Float.floatToIntBits(transparency) != Float
				.floatToIntBits(other.transparency))
			return false;
		return true;
	}

	/**
	 * Sets the slot number of this slot style.
	 * 
	 * @param slotNumber The slot number to set the slot style to.
	 */
	protected void setSlotNumber(int slotNumber)
	{
		this.slot = slotNumber;
	}
	
	/**
	 * Removes the passed slot item style from the slot style.
	 * 
	 * @param slotItemStyle The slot item style to remove.
	 * @return The result of the removal.
	 */
	public boolean removeSlotItemStyle(SlotItemStyle slotItemStyle)
	{
		boolean removed = false;
		
		for(int i = 0; i < styles.size() && !removed; i++)
		{
			if(styles.get(i).equals(slotItemStyle))
			{
				styles.remove(i);
				removed = true;
			}
		}
		
		return removed;
	}
}
