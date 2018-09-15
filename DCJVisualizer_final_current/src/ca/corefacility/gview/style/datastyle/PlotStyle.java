package ca.corefacility.gview.style.datastyle;

import java.awt.Paint;

import ca.corefacility.gview.map.effects.ShapeEffectRenderer;
import ca.corefacility.gview.map.effects.StandardEffect;

public class PlotStyle extends SlotItemStyle
{
	private PlotBuilderType plotBuilderType;

	private PlotDrawerType plotDrawerType = new PlotDrawerType.Line();

	private int gridLines = 0;
	private Paint gridPaint = null;
	private float gridLineThickness = 0.3f;
	
	private static int ID = 0;	//Used for giving unique IDs.	
	private final int myID;	//Unique identifier for plots.

	private ShapeEffectRenderer shapeEffectRenderer = new StandardEffect();

	public PlotStyle()
	{
		this.myID = this.getNextID();
	}

	protected PlotStyle(PlotStyle style)
	{
		super(style);

		this.plotBuilderType = style.plotBuilderType;
		this.plotDrawerType = style.plotDrawerType;
		this.shapeEffectRenderer = style.shapeEffectRenderer;

		this.gridLines = style.gridLines;
		this.gridPaint = style.gridPaint;
		this.gridLineThickness = style.gridLineThickness;
		
		this.myID = getNextID();
	}

	public PlotBuilderType getPlotBuilderType()
	{
		return this.plotBuilderType;
	}

	public void setPlotBuilderType(PlotBuilderType plotBuilderType)
	{
		this.plotBuilderType = plotBuilderType;
	}

	public void setPlotDrawerType(PlotDrawerType plotDrawerType)
	{
		this.plotDrawerType = plotDrawerType;
	}

	public PlotDrawerType getPlotDrawerType()
	{
		return this.plotDrawerType;
	}

	public void setShapeEffectRenderer(ShapeEffectRenderer shapeEffectRenderer)
	{
		if (shapeEffectRenderer != null)
		{
			this.shapeEffectRenderer = shapeEffectRenderer;
		}
	}

	public ShapeEffectRenderer getShapeEffectRenderer()
	{
		return this.shapeEffectRenderer;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Float.floatToIntBits(this.gridLineThickness);
		result = prime * result + this.gridLines;
		result = prime * result
		+ (this.gridPaint == null ? 0 : this.gridPaint.hashCode());
		result = prime * result
		+ (this.plotBuilderType == null ? 0 : this.plotBuilderType.hashCode());
		result = prime * result
		+ (this.plotDrawerType == null ? 0 : this.plotDrawerType.hashCode());
		result = prime
		* result
		+ (this.shapeEffectRenderer == null ? 0 : this.shapeEffectRenderer
				.hashCode());
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
		PlotStyle other = (PlotStyle) obj;
		if (Float.floatToIntBits(this.gridLineThickness) != Float
				.floatToIntBits(other.gridLineThickness))
			return false;
		if (this.gridLines != other.gridLines)
			return false;
		if (this.gridPaint == null)
		{
			if (other.gridPaint != null)
				return false;
		}
		else if (!this.gridPaint.equals(other.gridPaint))
			return false;
		if (this.plotBuilderType == null)
		{
			if (other.plotBuilderType != null)
				return false;
		}
		else if (!this.plotBuilderType.equals(other.plotBuilderType))
			return false;
		if (this.plotDrawerType == null)
		{
			if (other.plotDrawerType != null)
				return false;
		}
		else if (!this.plotDrawerType.equals(other.plotDrawerType))
			return false;
		if (this.shapeEffectRenderer == null)
		{
			if (other.shapeEffectRenderer != null)
				return false;
		}
		else if (!this.shapeEffectRenderer.equals(other.shapeEffectRenderer))
			return false;
		return true;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return new PlotStyle(this);
	}

	public void setGridLines(int gridLines)
	{
		this.gridLines = gridLines;
	}

	/**
	 * @return  The number of grid lines to display.
	 */
	public int getGridLines()
	{
		return this.gridLines;
	}

	public Paint getGridPaint()
	{
		return this.gridPaint;
	}

	public void setGridPaint(Paint paint)
	{
		this.gridPaint = paint;
	}

	public float getGridLineThickness()
	{
		return this.gridLineThickness;
	}

	public void setGridLineThickness(float thickness)
	{
		this.gridLineThickness = thickness;
	}
	
	private synchronized int getNextID()
	{
		ID++;
		
		return ID;
	}
	
	public int getID()
	{
		return this.myID;
	}
}
