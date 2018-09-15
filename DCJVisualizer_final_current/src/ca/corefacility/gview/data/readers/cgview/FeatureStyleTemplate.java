package ca.corefacility.gview.data.readers.cgview;

import java.awt.Color;
import java.awt.Font;

import ca.corefacility.gview.layout.feature.FeatureShapeRealizer;

/**
 * Defines specific style information for each Feature.
 * @author aaron
 *
 */
class FeatureStyleTemplate
{
	public Color color = null;
	public FeatureShapeRealizer decoration = null;
	public boolean giveFeaturePositions;
	public Color globalLabelColor = null;
	public Font labelFont = null;
	public float labelLineLength = 0.0f;
	public float labelLineThickness = 0.0f;
	public float opacity = 0.0f;
	public float proportionOfThickness = 0.0f;
	public float radiusAdjustment = 0.0f;
	public boolean showLabel = false;
	public boolean showShading = false;
	public boolean useColoredLabelBackgrounds = false;

	public FeatureStyleTemplate()
	{
		color = Color.blue;
		decoration = FeatureShapeRealizer.NO_ARROW;
		giveFeaturePositions = false;
		globalLabelColor = null;
		labelFont = new Font("SansSerif", Font.PLAIN, 10);
		labelLineLength = 0.0f;
		opacity = 1.0f;
		proportionOfThickness = 1.0f;
		radiusAdjustment = 0.0f;
		showLabel = true;
		showShading = true;
		useColoredLabelBackgrounds = false;
	}

	public FeatureStyleTemplate(FeatureStyleTemplate styleTemplate)
	{
		this.color = styleTemplate.color;
		this.decoration = styleTemplate.decoration;
		this.giveFeaturePositions = styleTemplate.giveFeaturePositions;
		this.globalLabelColor = styleTemplate.globalLabelColor;
		this.labelFont = styleTemplate.labelFont;
		this.labelLineLength = styleTemplate.labelLineLength;
		this.opacity = styleTemplate.opacity;
		this.proportionOfThickness = styleTemplate.proportionOfThickness;
		this.radiusAdjustment = styleTemplate.radiusAdjustment;
		this.showLabel = styleTemplate.showLabel;
		this.showShading = styleTemplate.showShading;
		this.labelLineThickness = styleTemplate.labelLineThickness;
		this.useColoredLabelBackgrounds = styleTemplate.useColoredLabelBackgrounds;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result
				+ ((decoration == null) ? 0 : decoration.hashCode());
		result = prime * result + (giveFeaturePositions ? 1231 : 1237);
		result = prime
				* result
				+ ((globalLabelColor == null) ? 0 : globalLabelColor.hashCode());
		result = prime * result
				+ ((labelFont == null) ? 0 : labelFont.hashCode());
		result = prime * result + Float.floatToIntBits(labelLineLength);
		result = prime * result + Float.floatToIntBits(labelLineThickness);
		result = prime * result + Float.floatToIntBits(opacity);
		result = prime * result + Float.floatToIntBits(proportionOfThickness);
		result = prime * result + Float.floatToIntBits(radiusAdjustment);
		result = prime * result + (showLabel ? 1231 : 1237);
		result = prime * result + (showShading ? 1231 : 1237);
		result = prime * result + (useColoredLabelBackgrounds ? 1231 : 1237);
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
		FeatureStyleTemplate other = (FeatureStyleTemplate) obj;
		if (color == null)
		{
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (decoration == null)
		{
			if (other.decoration != null)
				return false;
		} else if (!decoration.equals(other.decoration))
			return false;
		if (giveFeaturePositions != other.giveFeaturePositions)
			return false;
		if (globalLabelColor == null)
		{
			if (other.globalLabelColor != null)
				return false;
		} else if (!globalLabelColor.equals(other.globalLabelColor))
			return false;
		if (labelFont == null)
		{
			if (other.labelFont != null)
				return false;
		} else if (!labelFont.equals(other.labelFont))
			return false;
		if (Float.floatToIntBits(labelLineLength) != Float
				.floatToIntBits(other.labelLineLength))
			return false;
		if (Float.floatToIntBits(labelLineThickness) != Float
				.floatToIntBits(other.labelLineThickness))
			return false;
		if (Float.floatToIntBits(opacity) != Float
				.floatToIntBits(other.opacity))
			return false;
		if (Float.floatToIntBits(proportionOfThickness) != Float
				.floatToIntBits(other.proportionOfThickness))
			return false;
		if (Float.floatToIntBits(radiusAdjustment) != Float
				.floatToIntBits(other.radiusAdjustment))
			return false;
		if (showLabel != other.showLabel)
			return false;
		if (showShading != other.showShading)
			return false;
		if (useColoredLabelBackgrounds != other.useColoredLabelBackgrounds)
			return false;
		return true;
	}
}
