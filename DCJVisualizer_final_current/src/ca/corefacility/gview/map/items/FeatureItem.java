package ca.corefacility.gview.map.items;


import java.awt.Paint;

import org.biojava.bio.seq.Feature;

import ca.corefacility.gview.layout.feature.FeatureShapeRealizer;
import ca.corefacility.gview.style.datastyle.FeatureHolderStyle;

public interface FeatureItem extends ShapeItem
{
	public Feature getFeature();

	/**
	 * Obtains the FeatureShapeRealizer which is used to draw out the feature's shape. (not sure if
	 * this should be available here?)
	 * 
	 * @return The FeatureShapeRealizer used to draw out the feature's shape.
	 */
	public FeatureShapeRealizer getFeatureShapeRealizer();

	/**
	 * @return The thickness of this feature item as a proportion of the slot it is in.
	 */
	public double getThickness();

	// TODO do I wish to have this hear? Or maybe just make available the corresponding
	// FeatureHolderStyle?

	/**
	 * @return The adjust of the height of this feature item in the slot.
	 */
	public double getHeightAdjust();

	public FeatureHolderStyle getFeatureHolderStyle();

	public void setFeatureHolderStyle(FeatureHolderStyle fhs);

	public String getHyperlink();

	public void setTransparency(float transparency);

	public Paint getPaint();
}
