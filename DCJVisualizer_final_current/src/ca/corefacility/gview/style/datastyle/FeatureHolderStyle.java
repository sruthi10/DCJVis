package ca.corefacility.gview.style.datastyle;


import java.awt.Paint;
import java.util.ArrayList;
import java.util.Iterator;

import org.biojava.bio.seq.FeatureFilter;

import ca.corefacility.gview.layout.feature.FeatureShapeRealizer;
import ca.corefacility.gview.map.controllers.link.Link;
import ca.corefacility.gview.map.effects.ShapeEffectRenderer;
import ca.corefacility.gview.style.datastyle.mapper.PropertyStyleMapper;
import ca.corefacility.gview.textextractor.FeatureTextExtractor;
import ca.corefacility.gview.textextractor.GeneTextExtractor;
import ca.corefacility.gview.utils.AndFilter;

/**
 * Stores the style information for a particular set of features. The set of features this applies
 * to is filtered out by the passed filter.
 * 
 * @author Aaron Petkau
 * 
 */
public class FeatureHolderStyle extends SlotItemStyle implements StyleHolder, FeatureContainerStyle
{
	private int id = 0;

	private FeatureHolderStyle parent = null; // this is used so that we can traverse up the tree to
	// generate the correct holder
	private FeatureHolderStyleFactory factory; // used so that when we create a new
	// FeatureHolderStyle, we can pass this off to the
	// factory (to handle ids).

	private ShapeEffectRenderer shapeEffectRenderer = ShapeEffectRenderer.BASIC_RENDERER;

	private LabelStyle labelStyle; // defines the style of labels associated with this holder.
	private boolean lockedLabelColors = false;

	/**
	 * This controls how the shape will be drawn. Different implementations draw different shapes.
	 */
	private FeatureShapeRealizer shapeRealizer = FeatureShapeRealizer.NO_ARROW;
	private FeatureTextExtractor toolTipExtractor = new GeneTextExtractor(); // defines how tooltip
	// text will be
	// extracted
	
	private FeatureTextExtractor hyperlinkExtractor; // defines how to extract a hyperlink

	private FeatureFilter filter;

	private ArrayList<SlotItemStyle> styles;
	
	private PropertyStyleMapper propertyStyleMapper = null;
	
	private boolean valid = true;
	private Link link = null;

	/**
	 * Constructs a FeatureHolderStyle with the passed information.
	 * 
	 * @param factory
	 * @param parent
	 * @param filter
	 * @param id
	 */
	protected FeatureHolderStyle(FeatureHolderStyleFactory factory, FeatureHolderStyle parent, FeatureFilter filter, int id)
	{
		super();

		assert filter != null;
		assert factory != null;

		this.filter = filter;

		this.id = id;
		this.parent = parent;
		this.factory = factory;

		if (parent != null)
		{
			this.labelStyle = parent.getLabelStyle();
		}
		else
		{
			this.labelStyle = new LabelStyle();
		}

		this.styles = new ArrayList<SlotItemStyle>();
	}

	protected FeatureHolderStyle(FeatureHolderStyle holderStyle)
	{
		super(holderStyle);

		this.id = holderStyle.id;
		this.parent = holderStyle.parent;
		this.factory = holderStyle.factory;
		this.filter = holderStyle.filter;
		this.shapeEffectRenderer = holderStyle.shapeEffectRenderer;
		this.shapeRealizer = holderStyle.shapeRealizer;
		this.toolTipExtractor = holderStyle.toolTipExtractor;
		this.hyperlinkExtractor = holderStyle.hyperlinkExtractor;
		this.labelStyle = holderStyle.labelStyle == null ? null : (LabelStyle)holderStyle.labelStyle.clone();
		this.styles = holderStyle.styles == null ? null : (ArrayList<SlotItemStyle>)holderStyle.styles.clone();//(Set<SlotItemStyle>)((HashSet<SlotItemStyle>)holderStyle.styles).clone();
		this.propertyStyleMapper = holderStyle.propertyStyleMapper;
	}

	/**
	 * @return The parent FeatureHolderStyle of this object, or null if no parent.
	 */
	public FeatureHolderStyle getParent()
	{
		return this.parent;
	}

	/**
	 * @return An id which is used by GenomeData to "cache" filtered out features so we don't always
	 *         have to re-run the filter on all features.
	 */
	public int getId()
	{
		return this.id;
	}

	/**
	 * @return The text extractor used for the tool tips on any features in this style.
	 */
	public FeatureTextExtractor getToolTipExtractor()
	{
		return this.toolTipExtractor;
	}

	/**
	 * Determines if this holder style is using the passed FeatureFilter.
	 * @param filter  The FeatureFilter to check against.
	 * @return  True if this holder style is using the passed FeatureFilter, false otherwise.
	 */
	public boolean hasFeatureFilter(FeatureFilter filter)
	{
		return this.filter.equals(filter);
	}

	/**
	 * Sets the text extractor for the tool tip text.
	 * 
	 * @param toolTipExtractor
	 */
	public void setToolTipExtractor(FeatureTextExtractor toolTipExtractor)
	{
		if (toolTipExtractor == null)
			throw new NullPointerException("toolTipExtractor is null");

		this.toolTipExtractor = toolTipExtractor;
	}

	/**
	 * @return The FeatureFilter used to filter out features this style applies to.
	 */
	public FeatureFilter getFilter()
	{
		return this.filter;
	}

	/**
	 * @return The FeatureShapeRealizer used to construct the shapes for features in this style.
	 */
	public FeatureShapeRealizer getFeatureShapeRealizer()
	{
		return this.shapeRealizer;
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
	 * Removes the property mapper from the feature holder style.
	 */
	public boolean removePropertyStyleMapper()
	{
		this.propertyStyleMapper = null;
		
		return true;
	}

	/**
	 * Sets the FeatureShapeRealizer used to construct shapes for features.
	 * 
	 * @param shapeRealizer
	 */
	public void setFeatureShapeRealizer(FeatureShapeRealizer shapeRealizer)
	{
		if (shapeRealizer == null)
			throw new NullPointerException("shapeRealizer is null");

		this.shapeRealizer = shapeRealizer;
	}

	/**
	 * @return The ShapeEffectRenderer used to define how to draw/color the shape of this feature.
	 */
	public ShapeEffectRenderer getShapeEffectRenderer()
	{
		return this.shapeEffectRenderer;
	}

	/**
	 * Sets the ShapeEffectRenderer used to draw/color this feature.
	 * 
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

	public FeatureHolderStyle createFeatureHolderStyle(FeatureFilter filter)
	{
		FeatureHolderStyle style = null;

		assert filter != null;

		style = this.factory.createFeatureHolderStyle(filter, this);

		// sets newly created default styles
		style.setFeatureShapeRealizer(this.shapeRealizer);
		style.setPaint(this.paint);
		style.setThickness(this.thickness);
		style.setToolTipExtractor(this.toolTipExtractor);
		style.setTransparency(this.transparency);
		style.setShapeEffectRenderer(this.shapeEffectRenderer);
		style.setLabelStyle((LabelStyle)this.labelStyle.clone());
		style.setHyperlinkExtractor(this.hyperlinkExtractor);
		style.propertyStyleMapper = null;

		this.styles.add(style);

		return style;
	}
	
	public void appendFeatureHolderStyle(FeatureFilter filter)
	{
		//Append to the current filter:
		if(this.filter instanceof AndFilter)
		{
			((AndFilter)this.filter).add(filter);
		}
		else if(this.filter != null)
		{
			this.filter = new AndFilter(this.filter, filter);
		}
		else
		{
			this.filter = filter;
		}
		
		this.valid = false;
	}

	/**
	 * Searches for a sub-FeatureHolderStyle with the passed FeatureFilter
	 * 
	 * @param filter
	 *            The filter to search for the substyle.
	 * 
	 * @return The FeatureHolderStyle using the passed filter, or null if not found.
	 */
	public FeatureHolderStyle getFeatureHolderStyle(FeatureFilter filter)
	{
		FeatureHolderStyle style = null;

		if (filter == null)
			throw new NullPointerException("filter is null");

		Iterator<SlotItemStyle> styles = styles();
		while (styles.hasNext() && style == null)
		{
			SlotItemStyle currStyle = styles.next();

			if (currStyle instanceof FeatureHolderStyle)
			{
				FeatureHolderStyle currFeatureHolderStyle = (FeatureHolderStyle)currStyle;

				if (currFeatureHolderStyle.hasFeatureFilter(filter))
				{
					style = currFeatureHolderStyle;
				}
			}
		}

		return style;
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
	public Object clone()
	{
		return new FeatureHolderStyle(this);
	}

	public LabelStyle getLabelStyle()
	{
		return this.labelStyle;
	}

	public void setLabelStyle(LabelStyle labelStyle)
	{
		this.labelStyle = labelStyle;
	}
	
	public void setLink(Link link)
	{
		this.link = link;
	}
	
	public Link getLink()
	{
		return this.link;
	}

	@Override
	public Iterator<SlotItemStyle> styles()
	{
		return this.styles.iterator();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((filter == null) ? 0 : filter.hashCode());
		result = prime
				* result
				+ ((hyperlinkExtractor == null) ? 0 : hyperlinkExtractor
						.hashCode());
		result = prime * result + id;
		result = prime * result
				+ ((labelStyle == null) ? 0 : labelStyle.hashCode());
		result = prime * result
				+ ((propertyStyleMapper == null) ? 0 : propertyStyleMapper.hashCode());
		result = prime
				* result
				+ ((shapeEffectRenderer == null) ? 0 : shapeEffectRenderer
						.hashCode());
		result = prime * result
				+ ((shapeRealizer == null) ? 0 : shapeRealizer.hashCode());
		result = prime * result + ((styles == null) ? 0 : styles.hashCode());
		result = prime
				* result
				+ ((toolTipExtractor == null) ? 0 : toolTipExtractor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FeatureHolderStyle other = (FeatureHolderStyle) obj;
		if (filter == null)
		{
			if (other.filter != null)
				return false;
		} else if (!filter.equals(other.filter))
			return false;
		if (hyperlinkExtractor == null)
		{
			if (other.hyperlinkExtractor != null)
				return false;
		} else if (!hyperlinkExtractor.equals(other.hyperlinkExtractor))
			return false;
		if (id != other.id)
			return false;
		if (labelStyle == null)
		{
			if (other.labelStyle != null)
				return false;
		} else if (!labelStyle.equals(other.labelStyle))
			return false;
		if (propertyStyleMapper == null)
		{
			if (other.propertyStyleMapper != null)
				return false;
		} else if (!propertyStyleMapper.equals(other.propertyStyleMapper))
			return false;
		if (shapeEffectRenderer == null)
		{
			if (other.shapeEffectRenderer != null)
				return false;
		} else if (!shapeEffectRenderer.equals(other.shapeEffectRenderer))
			return false;
		if (shapeRealizer == null)
		{
			if (other.shapeRealizer != null)
				return false;
		} else if (!shapeRealizer.equals(other.shapeRealizer))
			return false;
		if (styles == null)
		{
			if (other.styles != null)
				return false;
		} else if (!styles.equals(other.styles))
			return false;
		if (toolTipExtractor == null)
		{
			if (other.toolTipExtractor != null)
				return false;
		} else if (!toolTipExtractor.equals(other.toolTipExtractor))
			return false;
		return true;
	}
	
	/**
	 * Removes the passed slot item style from the feature holder style.
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
	
	/**
	 * 
	 * @return Whether or not the holder style is valid.
	 * 
	 * (Used by the GenomeData class)
	 */
	public boolean isValid()
	{
		return this.valid;
	}
	
	/**
	 * Resets the valid flag for the feature filter.
	 * 
	 * (Used by the GenomeData class)
	 */
	public void resetValid()
	{
		this.valid = true;
	}
	
	/**
	 * Whether or not to force the label colors to be the same as the feature holder style.
	 * 
	 * @param lock
	 */
	public void setLockedLabelColors(boolean lock)
	{
		this.lockedLabelColors = lock;
		
		if(lock)
		{
			this.labelStyle.setTextPaint(this.getPaint()[0]);
		}
	}
	
	/**
	 * 
	 * @return Whether or not to the label colors are forced to be the same as the feature holder style.
	 */
	public boolean getLockedLabelColors()
	{
		return this.lockedLabelColors;
	}
	
	@Override
	public void setPaint(Paint paint)
	{
		super.setPaint(paint);
		
		if(this.lockedLabelColors)
		{
			this.labelStyle.setTextPaint(this.getPaint()[0]);
		}
	}

	@Override
	public void setPaint( Paint[] paintArray )
	{
		super.setPaint(paintArray);
		
		if(this.lockedLabelColors)
		{
			this.labelStyle.setTextPaint(this.getPaint()[0]);
		}
	}
}
