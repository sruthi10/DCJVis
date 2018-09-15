package ca.corefacility.gview.map.gui.editor.communication;

/**
 * This class represents a GUI event broadcaster.
 * 
 * A GUI event broadcaster is responsible for deciding whether or not to
 * broadcast any given event to its event listeners.
 * 
 * @author Eric Marinier
 * 
 */
public interface GUIEventBroadcaster
{
	public boolean broadcastEvent(GUIEvent event);
}
