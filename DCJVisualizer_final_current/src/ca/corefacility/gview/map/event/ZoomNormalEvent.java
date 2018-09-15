package ca.corefacility.gview.map.event;

public class ZoomNormalEvent extends GViewEvent
{
    private static final long serialVersionUID = 1L;
    
    private double scale;
	
	public ZoomNormalEvent(Object source, double scale)
	{
		super(source);
		
		this.scale = scale;
	}
	
	public double getScale()
	{
		return scale;
	}
}
