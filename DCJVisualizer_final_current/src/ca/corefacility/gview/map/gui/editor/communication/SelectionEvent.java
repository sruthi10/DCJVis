package ca.corefacility.gview.map.gui.editor.communication;


/**
 * This class is responsible for facilitating notifications of selected objects.
 * 
 * This facilitation is useful for allowing GUI components to remain
 * synchronized and consistent.
 * 
 * @author Eric Marinier
 * 
 */
public class SelectionEvent extends CommunicationEvent
{
	public SelectionEvent(GUIEvent event)
	{
		super(event);
	}
}
