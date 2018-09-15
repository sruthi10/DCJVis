package ca.corefacility.gview.map.event;

import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.managers.ResolutionManager;

public class ResolutionSwitchEvent extends GViewEvent
{
	private static final long serialVersionUID = 6203476666522120326L;
	private ResolutionManager.Direction direction;
	private int resolutionLevel;
	private double baseResolutionScale;
	
	public ResolutionSwitchEvent(Backbone source, ResolutionManager.Direction direction, int resolutionLevel, double baseResolutionScale)
	{
		super(source);
		
		this.direction = direction;
		this.resolutionLevel = resolutionLevel;
		this.baseResolutionScale = baseResolutionScale;
	}

	public Backbone getBackbone()
	{
		return (Backbone)getSource();
	}
	
	public ResolutionManager.Direction getDirection()
	{
		return direction;
	}
	
	public int getResolutionLevel()
	{
		return resolutionLevel;
	}
	
	public double getBaseResolutionScale()
	{
		return baseResolutionScale;
	}
}
