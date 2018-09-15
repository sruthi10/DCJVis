package ca.corefacility.gview.map.gui.editor.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import ca.corefacility.gview.map.gui.editor.communication.GUIEvent;
import ca.corefacility.gview.map.gui.editor.communication.GUIEventBroadcaster;

/**
 * The abstract class for style panels.
 * 
 * @author Eric Marinier
 * 
 */
public abstract class StylePanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private boolean updating; // updating variable is used to stop the action
								// listener from firing on an update.

	private GUIEvent previousEvent = null;

	private final ArrayList<GUIEventBroadcaster> eventBroadcasters = new ArrayList<GUIEventBroadcaster>();

	/**
	 * Create the panel.
	 */
	public StylePanel()
	{
		super();

		this.updating = true;

		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.EAST);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		this.updating = false;
	}

	/**
	 * Adds a GUIEvent broadcaster to this panel.
	 * 
	 * This event broadcaster will be given the opportunity to broadcast the
	 * event to it's listeners when the panel makes a request to broadcast a
	 * given event.
	 * 
	 * @param broadcaster
	 */
	public void addGUIEventBroadcaster(GUIEventBroadcaster broadcaster)
	{
		this.eventBroadcasters.add(broadcaster);
	}

	/**
	 * Updates the panel.
	 */
	public void updatePanel()
	{
		// stop the action listeners from firing on an update.
		setUpdating(true);
		update();
		setUpdating(false);
	}

	/**
	 * Updates the panel.
	 */
	public abstract void update();

	/**
	 * Saves the panel.
	 */
	public void save()
	{
		apply();
	}

	/**
	 * Applies the panel's properties to the appropriate style.
	 */
	public void apply()
	{
		doApply();
	}

	/**
	 * Applies changes.
	 */
	protected abstract void doApply();

	/**
	 * 
	 * @return Whether or not the panel is updating.
	 */
	public boolean isUpdating()
	{
		return this.updating;
	}

	/**
	 * Sets the updating parameter.
	 * 
	 * @param updating
	 *            Whether or not the panel is updating.
	 */
	public void setUpdating(boolean updating)
	{
		this.updating = updating;
	}

	/**
	 * Allows broadcasters to broadcast the event to their listeners. If no
	 * broadcaster broadcasts the event, it will be sent directly to the calling
	 * panel to handle the event.
	 * 
	 * @param event
	 */
	protected void broadcastEvent(GUIEvent event)
	{
		boolean broadcasted = false;

		// Broadcasters:
		for (GUIEventBroadcaster broadcaster : this.eventBroadcasters)
		{
			broadcasted |= broadcaster.broadcastEvent(event);
		}

		// Catch:
		if (!broadcasted)
		{
			handleGUIEvent(event);
		}
	}

	/**
	 * Appropriately handles the event.
	 * 
	 * @param event
	 */
	public final void handleGUIEvent(GUIEvent event)
	{
		if (this.isUpdating())
		{
			return;
		}

		if (this.previousEvent != null && this.previousEvent.equals(event))
		{
			return;
		}

		this.setUpdating(true);
		this.previousEvent = event;

		// Execute FIRST:
		executeGUIEvent(event);

		// Re-broadcast:
		this.broadcastEvent(event);

		this.setUpdating(false);
	}

	/**
	 * Executes the event.
	 * 
	 * @param event
	 */
	public void executeGUIEvent(GUIEvent event)
	{
	}
}
