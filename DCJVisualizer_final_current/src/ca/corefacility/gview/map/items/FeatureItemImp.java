package ca.corefacility.gview.map.items;


import org.biojava.bio.seq.Feature;
import org.biojava.bio.symbol.Location;

import ca.corefacility.gview.layout.feature.FeatureShapeRealizer;
import ca.corefacility.gview.style.datastyle.FeatureHolderStyle;
import ca.corefacility.gview.textextractor.FeatureTextExtractor;

public class FeatureItemImp extends AbstractBackboneShapeItem implements FeatureItem
{
	private static final long serialVersionUID = -1051809829243461609L;
	// the feature this item refers to
	private Feature feature;
	private FeatureHolderStyle holderStyle;

	private FeatureTextExtractor textExtractor;
	private FeatureTextExtractor hyperlinkExtractor;

	// TODO figure out how to change style in here
	// probably want to add this as an observer of changes to style/data
	// may want to have textExtractor in style?
	// we want to have a reference to the holderStyle so that we can know where to place ourselves
	// in style tree
	// if we ever need to change a style for this feature.
	public FeatureItemImp(FeatureHolderStyle holderStyle, Feature feature)
	{
		// this is sufficient for a null check...
		assert holderStyle != null && feature != null;

		this.feature = feature;
		this.holderStyle = holderStyle; // TODO set ourselves as listener to style changes
		// or just extract style info from holderStyle when we need it?

		setStyle(holderStyle);
	}

	public Feature getFeature()
	{
		return this.feature;
	}

	@Override
	public String getToolTip()
	{
		if (this.textExtractor == null)
		{
			return null;
		}
		else
		{
			return this.textExtractor.extractText(this.feature);
		}
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (this.feature == null ? 0 : this.feature.hashCode());
		result = prime * result + (this.holderStyle == null ? 0 : this.holderStyle.hashCode());
		result = prime * result + (this.textExtractor == null ? 0 : this.textExtractor.hashCode());
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
		FeatureItemImp other = (FeatureItemImp) obj;
		if (this.feature == null)
		{
			if (other.feature != null)
				return false;
		}
		else if (!this.feature.equals(other.feature))
			return false;
		if (this.holderStyle == null)
		{
			if (other.holderStyle != null)
				return false;
		}
		else if (!this.holderStyle.equals(other.holderStyle))
			return false;
		if (this.textExtractor == null)
		{
			if (other.textExtractor != null)
				return false;
		}
		else if (!this.textExtractor.equals(other.textExtractor))
			return false;
		return true;
	}

	public double getThickness()
	{
		return this.holderStyle.getThickness();
	}

	public double getHeightAdjust()
	{
		return this.holderStyle.getHeightAdjust();
	}

	public FeatureShapeRealizer getFeatureShapeRealizer()
	{
		return this.holderStyle.getFeatureShapeRealizer();
	}

	public Location getLocation()
	{
		return this.feature.getLocation();
	}

	public void updateClippedLocation(Location clipLocation)
	{
		if (getLocation().overlaps(clipLocation))
		{
			updateBoundsFromShape(getState());
		}
	}

	// should set style be here? Or in FeatureHolderStyle?
	private void setStyle(FeatureHolderStyle style)
	{
		setPaint(style.getPaint()[0]);
		// setStrokePaint(null);
		setTransparency(style.getTransparency());
		setShapeEffectRenderer(style.getShapeEffectRenderer());
		this.textExtractor = style.getToolTipExtractor();
		this.hyperlinkExtractor = style.getHyperlinkExtractor();
		
		if (style.getPropertyStyleMapper() != null)
		{
			style.getPropertyStyleMapper().performMappingFor(this);
		}
	}

	@Override
	public FeatureHolderStyle getFeatureHolderStyle()
	{
		return this.holderStyle;
	}

	@Override
	public void setFeatureHolderStyle(FeatureHolderStyle fhs)
	{
		this.holderStyle = fhs;
		setStyle(fhs);
		setStyled(); // flags the canvas for updating
		this.invalidateFullBounds();
		this.invalidatePaint();
		this.invalidateLayout();
	}

	@Override
	public String getHyperlink()
	{
		if (this.hyperlinkExtractor != null)
		{
			return this.hyperlinkExtractor.extractText(this.feature);
		}
		else
		{
			return null;
		}
	}
}
