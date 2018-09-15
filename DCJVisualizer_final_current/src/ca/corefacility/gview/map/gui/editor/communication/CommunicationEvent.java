package ca.corefacility.gview.map.gui.editor.communication;

/**
 * This class facilitates the communication between GUI objects.
 * 
 * @author Eric Marinier
 * 
 */
public class CommunicationEvent
{
	private final GUIEvent event;

	public CommunicationEvent(GUIEvent event)
	{
		this.event = event;
	}

	/**
	 * 
	 * @return The specific kind of GUI event.
	 */
	public GUIEvent getGUIEvent()
	{
		return event;
	}
}
