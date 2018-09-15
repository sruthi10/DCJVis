package ca.corefacility.gview.style.datastyle;


import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;

import ca.corefacility.gview.managers.labels.FeatureLabelRanker;
import ca.corefacility.gview.textextractor.FeatureTextExtractor;
import ca.corefacility.gview.textextractor.LocationExtractor;

/**
 * Defines style information dealing with labels of features.
 * @author aaron
 *
 */
public class LabelStyle implements Cloneable
{
	private FeatureTextExtractor labelExtractor = new LocationExtractor(); // used to extract label text from features
	// (TODO allow textExtractor to extract text from other things (plots) as well).

	private FeatureLabelRanker labelRanker = FeatureLabelRanker.DEFAULT;

	private Paint textPaint = Color.BLACK;
	private Paint backgroundPaint = null;
	private Paint labelLinePaint = Color.black;

	// used to allow label lines to be colored automatically
	private boolean autoLabelLinePaint = true;

	private Font font = new Font("SansSerif", Font.PLAIN, 14); // note old default was 10

	private double initialLineLength = 30; // the inital length of a line drawn to the label.
	private float lineThickness = 3;

	private boolean showLabels = false;

	/**
	 * Constructs a new LabelStyle object.
	 */
	public LabelStyle()
	{
		setTextPaint(Color.BLACK); // black by default
	}

	protected LabelStyle(LabelStyle style)
	{
		this.labelExtractor = (FeatureTextExtractor)style.labelExtractor.clone();
		this.labelRanker = (FeatureLabelRanker)style.labelRanker.clone();
		this.textPaint = style.textPaint;
		this.backgroundPaint = style.backgroundPaint;
		this.font = style.font;
		this.initialLineLength = style.initialLineLength;
		this.lineThickness = style.lineThickness;
		this.showLabels = style.showLabels;
		this.labelLinePaint = style.labelLinePaint;
		this.autoLabelLinePaint = style.autoLabelLinePaint;
	}

	/**
	 * @return  The paint for the text of the labels.
	 */
	public Paint getTextPaint()
	{
		return this.textPaint;
	}

	/**
	 * Sets the backgroundPaint of the text of the labels.
	 * @param textPaint
	 */
	public void setTextPaint(Paint textPaint)
	{
		if (textPaint == null)
			throw new NullPointerException("textPaint is null");

		this.textPaint = textPaint;
	}

	/**
	 * @return  The paint of the background of a box surrounding the label.
	 */
	public Paint getBackgroundPaint()
	{
		return this.backgroundPaint;
	}

	/**
	 * Sets the background paint of the label.
	 * @param paint  The paint for the background, null if no paint.
	 */
	public void setBackgroundPaint(Paint paint)
	{
		this.backgroundPaint = paint;
	}

	/**
	 * Gets the paint used to color the label line.
	 * @return  The paint used to color the label line.
	 */
	public Paint getLabelLinePaint()
	{
		return this.labelLinePaint;
	}

	/**
	 * Sets the paint used to color the label lines.
	 * @param labelLinePaint  The paint used to color the label lines.
	 */
	public void setLabelLinePaint(Paint labelLinePaint)
	{
		this.labelLinePaint = labelLinePaint;
	}

	/**
	 * @return  The font of the labels.
	 */
	public Font getFont()
	{
		return this.font;
	}

	/**
	 * @return  The FeatureLabelRanker which is used to divide up labels into categories of importance.
	 */
	public FeatureLabelRanker getLabelRanker()
	{
		return this.labelRanker;
	}

	/**
	 * Sets the FeatureLabelRanker used to divide up labels into categories.
	 * @param labelRanker
	 */
	public void setLabelRanker(FeatureLabelRanker labelRanker)
	{
		if (labelRanker == null)
			throw new NullPointerException("labelRanker is null");

		this.labelRanker = labelRanker;
	}

	/**
	 * Sets the font of the label text.
	 * @param font
	 */
	public void setFont(Font font)
	{
		if (font == null)
			throw new NullPointerException("font is null");

		this.font = font;
	}

	/**
	 * @return  True if labels are to be displayed, false otherwise.
	 */
	public boolean showLabels()
	{
		return this.showLabels;
	}

	/**
	 * Sets whether or not to show labels in this LabelStyle.
	 * @param showLabels
	 */
	public void setShowLabels(boolean showLabels)
	{
		this.showLabels = showLabels;
	}

	/**
	 * @return The FeatureTextExtractor used to extract text for the labels.
	 */
	public FeatureTextExtractor getLabelExtractor()
	{
		return this.labelExtractor;
	}

	/**
	 * Sets the FeatureTextExtractor to extract text for the labels.
	 * @param labelExtractor
	 */
	public void setLabelExtractor(FeatureTextExtractor labelExtractor)
	{
		this.labelExtractor = labelExtractor;
	}

	/**
	 * @return  The initial line length of the labels.
	 */
	public double getInitialLineLength()
	{
		return this.initialLineLength;
	}

	/**
	 * Sets the initial line length of the labels.  Can't be negative.
	 * @param initalLineLength
	 */
	public void setInitialLineLength(double initalLineLength)
	{
		if (this.initialLineLength < 0)
			throw new IllegalArgumentException("initialLineLength is negative");

		this.initialLineLength = initalLineLength;
	}

	/**
	 * @return  The thickness of the line leading up to the label.
	 */
	public float getLineThickness()
	{
		return this.lineThickness;
	}

	/**
	 * Sets the thickness of the line leading up to the label.
	 * @param lineThickness  The thickness of the line.  Must be 0 or positive.
	 */
	public void setLineThickness(float lineThickness)
	{
		if (lineThickness < 0.0f)
			throw new IllegalArgumentException("lineThickness is negative");

		this.lineThickness = lineThickness;
	}

	/**
	 * Checks if the label style is such that label lines should automatically be colored (using the same paint as the label text).
	 * @return  True if the label lines should automatically be colored, false otherwise.
	 */
	public boolean isAutoLabelLinePaint()
	{
		return this.autoLabelLinePaint;
	}

	/**
	 * Instructs this style to always keep the label line paint the same as the label text paint.
	 * @param autoLabelLinePaint  True if we should auto-paint the label lines, false otherwise.
	 */
	public void setAutoLabelLinePaint(boolean autoLabelLinePaint)
	{
		this.autoLabelLinePaint = autoLabelLinePaint;
	}

	@Override
	public Object clone()
	{
		return new LabelStyle(this);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.autoLabelLinePaint ? 1231 : 1237);
		result = prime * result
		+ (this.backgroundPaint == null ? 0 : this.backgroundPaint.hashCode());
		result = prime * result + (this.font == null ? 0 : this.font.hashCode());
		long temp;
		temp = Double.doubleToLongBits(this.initialLineLength);
		result = prime * result + (int) (temp ^ temp >>> 32);
		result = prime * result
		+ (this.labelExtractor == null ? 0 : this.labelExtractor.hashCode());
		result = prime * result
		+ (this.labelLinePaint == null ? 0 : this.labelLinePaint.hashCode());
		result = prime * result
		+ (this.labelRanker == null ? 0 : this.labelRanker.hashCode());
		result = prime * result + Float.floatToIntBits(this.lineThickness);
		result = prime * result + (this.showLabels ? 1231 : 1237);
		result = prime * result
		+ (this.textPaint == null ? 0 : this.textPaint.hashCode());
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
		LabelStyle other = (LabelStyle) obj;
		if (this.autoLabelLinePaint != other.autoLabelLinePaint)
			return false;
		if (this.backgroundPaint == null)
		{
			if (other.backgroundPaint != null)
				return false;
		}
		else if (!this.backgroundPaint.equals(other.backgroundPaint))
			return false;
		if (this.font == null)
		{
			if (other.font != null)
				return false;
		}
		else if (!this.font.equals(other.font))
			return false;
		if (Double.doubleToLongBits(this.initialLineLength) != Double
				.doubleToLongBits(other.initialLineLength))
			return false;
		if (this.labelExtractor == null)
		{
			if (other.labelExtractor != null)
				return false;
		}
		else if (!this.labelExtractor.equals(other.labelExtractor))
			return false;
		if (this.labelLinePaint == null)
		{
			if (other.labelLinePaint != null)
				return false;
		}
		else if (!this.labelLinePaint.equals(other.labelLinePaint))
			return false;
		if (this.labelRanker == null)
		{
			if (other.labelRanker != null)
				return false;
		}
		else if (!this.labelRanker.equals(other.labelRanker))
			return false;
		if (Float.floatToIntBits(this.lineThickness) != Float
				.floatToIntBits(other.lineThickness))
			return false;
		if (this.showLabels != other.showLabels)
			return false;
		if (this.textPaint == null)
		{
			if (other.textPaint != null)
				return false;
		}
		else if (!this.textPaint.equals(other.textPaint))
			return false;
		return true;
	}
}
