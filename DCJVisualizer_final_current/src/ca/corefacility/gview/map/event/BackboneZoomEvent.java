package ca.corefacility.gview.map.event;

import ca.corefacility.gview.layout.sequence.Backbone;

/**
 * Defines a change in the scale of the backbone.
 * 
 * @author Aaron Petkau
 *
 */
public class BackboneZoomEvent extends GViewEvent
{	
	private static final long serialVersionUID = 5924363295733549116L;

	public BackboneZoomEvent(Backbone source)
	{
		super(source);
	}
	
	// TODO should I have my own method here, or should the user just use getSource() and cast it?
	public Backbone getBackbone()
	{
		return (Backbone)getSource();
	}
}
