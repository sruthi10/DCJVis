package ca.corefacility.gview.layout.plot;

public abstract class PlotBuilderSymbols extends PlotBuilderRange
{	
	protected int windowSize = -1;
	protected int stepSize = -1;
	
	public PlotBuilderSymbols()
	{
		this.minValue = -1;
		this.maxValue = 1;
	}
	
	public PlotBuilderSymbols(int windowSize, int stepSize)
	{
		this.windowSize = windowSize;
		this.stepSize = stepSize;
	}
	
	protected PlotBuilderSymbols(PlotBuilderSymbols other)
	{
	    super(other);
	}
}
