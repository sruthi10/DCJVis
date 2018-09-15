package ca.corefacility.gview.map.inputHandler;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import ca.corefacility.gview.layout.sequence.ZoomException;
import ca.corefacility.gview.map.GViewMap;
import ca.corefacility.gview.map.gui.GUISettings;
import ca.corefacility.gview.map.gui.GUISettings.RenderingQuality;
import ca.corefacility.gview.map.items.FeatureItem;
import ca.corefacility.gview.map.items.MapItem;
import ca.corefacility.gview.utils.thread.ThreadService;
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;

/**
 * Handles keyboard and mouse events on the GView map.
 * 
 * @author Aaron Petkau, Eric Marinier
 * 
 */
public class InputEventHandler extends PBasicInputEventHandler
{
	private Timer timer = new Timer(true);

	private ZoomTask zoomTask = null;
	private MoveTask moveTask = null;

	private PCamera camera;

	private ZoomSubject zoomSubject;
	private SelectHandler selectHandler;
	private MouseOverHandler mouseOverHandler;

	private GViewMap gViewMap;

	private Point2D lastPosition = new Point2D.Double(0, 0);

	private static final char RE_START = KeyEvent.VK_COMMA;
	private static final char RE_END = KeyEvent.VK_PERIOD;
	private static final char ZOOM_OUT = KeyEvent.VK_MINUS;
	private static final char ZOOM_IN = KeyEvent.VK_EQUALS;
	private static final char EXPORT = KeyEvent.VK_X;
	private static final char VIEW_ALL = KeyEvent.VK_V;
	private static final char RESET_VIEW = KeyEvent.VK_R;
	private static final char RESET_ZOOM = KeyEvent.VK_0;
	private static final char TOGGLE_QUALITY = KeyEvent.VK_Q;

	// The directional arrow keys.
	private enum ARROW
	{
		UP, DOWN, LEFT, RIGHT;
	}

	// Whether or not the shift key is pressed.
	private boolean shift = false;

	// The actual state of the arrow key.
	private final HashMap<ARROW, Boolean> arrowState = new HashMap<ARROW, Boolean>();

	// The intermediate "pressed" actions. This is used to determine the state.
	private final HashMap<ARROW, Boolean> arrowPressed = new HashMap<ARROW, Boolean>();

	// The delay of the moving operation.
	private static final int MOVE_DELAY = 68; // in milliseconds

	/**
	 * Handles all keyboard/mouse input events.
	 * 
	 * @param camera
	 *            The camera used to view the GView map.
	 * @param gviewMap
	 *            The GView map itself.
	 * @param zoomSubject
	 *            The object used to handle zooming.
	 * @param selectHandler
	 *            The object used to handle selection of items.
	 * @param toolTipHandler
	 *            The object used to control the display of the tooltip.
	 */
	public InputEventHandler(PCamera camera, GViewMap gviewMap, ZoomSubject zoomSubject, SelectHandler selectHandler,
			ToolTipHandler toolTipHandler)
	{
		this.camera = camera;

		this.gViewMap = gviewMap;

		this.zoomSubject = zoomSubject;
		this.selectHandler = selectHandler;

		this.mouseOverHandler = new MouseOverHandler(toolTipHandler);

		// Initialize arrow key states:
		setArrowState(ARROW.UP, false);
		setArrowState(ARROW.DOWN, false);
		setArrowState(ARROW.LEFT, false);
		setArrowState(ARROW.RIGHT, false);

		// Initialize arrow key "pressed" actions.
		setArrowPressed(ARROW.UP, false);
		setArrowPressed(ARROW.DOWN, false);
		setArrowPressed(ARROW.LEFT, false);
		setArrowPressed(ARROW.RIGHT, false);
	}

	/**
	 * 
	 * @param arrow
	 *            The arrow key.
	 * @return Whether or not the arrow key is pressed.
	 */
	public boolean getArrowState(ARROW arrow)
	{
		return this.arrowState.get(arrow);
	}

	/**
	 * Sets the arrow key state.
	 * 
	 * @param arrow
	 *            The arrow key.
	 * @param state
	 *            Whether or not it is pressed.
	 */
	private void setArrowState(ARROW arrow, boolean state)
	{
		this.arrowState.put(arrow, state);
	}

	/**
	 * Sets the arrow's "pressed" action.
	 * 
	 * @param arrow
	 *            The arrow key.
	 * @param pressed
	 *            Whether or not the "pressed" action should be registered.
	 */
	private void setArrowPressed(ARROW arrow, boolean pressed)
	{
		this.arrowPressed.put(arrow, pressed);
	}

	/**
	 * 
	 * @param arrow
	 *            The arrow key.
	 * @return Whether or not the "pressed" action should be registered.
	 */
	private boolean getArrowPressed(ARROW arrow)
	{
		return this.arrowPressed.get(arrow);
	}

	@Override
	public void keyPressed(PInputEvent event)
	{
		char keyCode = (char) event.getKeyCode(); // returns letters as capitals

		// Move to.. start:
		if (RE_START == keyCode && event.isControlDown())
		{
			this.gViewMap.setCenter(0);
		}
		// Move to.. end:
		if (RE_END == keyCode && event.isControlDown())
		{
			this.gViewMap.setCenter(this.gViewMap.getMaxSequenceLength());
		}
		// Zoom in:
		else if ((ZOOM_IN == keyCode || keyCode == KeyEvent.VK_PLUS) && event.isControlDown())
		{
			this.lastPosition = this.zoomSubject.zoomStretch(1.2, this.lastPosition);
		}
		// Zoom out:
		else if (ZOOM_OUT == keyCode && event.isControlDown())
		{
			this.lastPosition = this.zoomSubject.zoomStretch(0.8, this.lastPosition);
		}
		// Reset zoom:
		else if (RESET_ZOOM == keyCode && event.isControlDown())
		{
			try
			{
				this.gViewMap.setZoomFactor(1.0);
			}
			catch (ZoomException e)
			{
				e.printStackTrace();
			}
		}
		// View all:
		else if (VIEW_ALL == keyCode && event.isControlDown())
		{
			this.gViewMap.scaleMapToScreen();
		}
		// Export:
		else if (EXPORT == keyCode && event.isControlDown())
		{
			ImageExportHandler.getInstance().performExport(this.gViewMap);
		}
		// Reset view:
		else if (keyCode == RESET_VIEW && event.isControlDown())
		{
			try
			{
				this.gViewMap.setZoomFactor(1.0);
			}
			catch (ZoomException e)
			{
				e.printStackTrace();
			}

			this.gViewMap.scaleMapToScreen();
		}
		// Quality:
		else if (TOGGLE_QUALITY == keyCode && event.isControlDown())
		{
			if (GUISettings.getRenderingQuality() == RenderingQuality.HIGH)
			{
				GUISettings.setRenderingQuality(RenderingQuality.LOW);
			}
			else
			{
				GUISettings.setRenderingQuality(RenderingQuality.HIGH);
			}
		}
		// Up arrow:
		else if (keyCode == KeyEvent.VK_UP)
		{
			setArrowState(ARROW.UP, true);
			setArrowPressed(ARROW.UP, true);
		}
		// Down arrow:
		else if (keyCode == KeyEvent.VK_DOWN)
		{
			setArrowState(ARROW.DOWN, true);
			setArrowPressed(ARROW.DOWN, true);
		}
		// Left arrow:
		else if (keyCode == KeyEvent.VK_LEFT)
		{
			setArrowState(ARROW.LEFT, true);
			setArrowPressed(ARROW.LEFT, true);
		}
		// Right arrow:
		else if (keyCode == KeyEvent.VK_RIGHT)
		{
			setArrowState(ARROW.RIGHT, true);
			setArrowPressed(ARROW.RIGHT, true);
		}
		// Shift key:
		else if (keyCode == KeyEvent.VK_SHIFT)
		{
			this.shift = true;
		}

		// Whether or not to start scheduling arrow-based movement events.
		if (isArrowKeyDown() && this.moveTask == null)
		{
			this.moveTask = new MoveTask();
			this.timer.scheduleAtFixedRate(this.moveTask, 1, MOVE_DELAY);
		}
	}

	@Override
	public void keyReleased(final PInputEvent event)
	{
		int keyCode = event.getKeyCode();

		if (keyCode == KeyEvent.VK_SHIFT)
		{
			this.shift = false;
		}

		if (isArrowKey(keyCode))
		{
			// UP ARROW
			if (keyCode == KeyEvent.VK_UP)
			{
				setArrowPressed(ARROW.UP, false);
			}
			// DOWN ARROW
			else if (keyCode == KeyEvent.VK_DOWN)
			{
				setArrowPressed(ARROW.DOWN, false);
			}
			// LEFT ARROW
			else if (keyCode == KeyEvent.VK_LEFT)
			{
				setArrowPressed(ARROW.LEFT, false);
			}
			// RIGHT ARROW
			else if (keyCode == KeyEvent.VK_RIGHT)
			{
				setArrowPressed(ARROW.RIGHT, false);
			}

			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run()
				{
					maybeReleaseArrowKey(event);
				}
			});
		}
	}

	/**
	 * Determines whether or not to release the arrow key.
	 * 
	 * This implementation exists because of a bug with key presses on Unix.
	 * Bug: http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4153069
	 * 
	 * @param event
	 */
	private void maybeReleaseArrowKey(PInputEvent event)
	{
		int keyCode = event.getKeyCode();

		// Is it an arrow key?
		if (isArrowKey(keyCode))
		{
			// UP ARROW
			if (keyCode == KeyEvent.VK_UP)
			{
				if (!getArrowPressed(ARROW.UP))
				{
					setArrowState(ARROW.UP, false);
				}
			}
			// DOWN ARROW
			else if (keyCode == KeyEvent.VK_DOWN)
			{
				if (!getArrowPressed(ARROW.DOWN))
				{
					setArrowState(ARROW.DOWN, false);
				}
			}
			// LEFT ARROW
			else if (keyCode == KeyEvent.VK_LEFT)
			{
				if (!getArrowPressed(ARROW.LEFT))
				{
					setArrowState(ARROW.LEFT, false);
				}
			}
			// RIGHT ARROW
			else if (keyCode == KeyEvent.VK_RIGHT)
			{
				if (!getArrowPressed(ARROW.RIGHT))
				{
					setArrowState(ARROW.RIGHT, false);
				}
			}
		}
	}

	/**
	 * 
	 * @param keyCode
	 *            The KeyEvent key code.
	 * @return Whether or not the key code is an arrow key.
	 */
	private boolean isArrowKey(int keyCode)
	{
		boolean result = false;

		if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_LEFT
				|| keyCode == KeyEvent.VK_RIGHT)
		{
			result = true;
		}

		return result;
	}

	/**
	 * 
	 * @return Whether or not there is an arrow key pressed down.
	 */
	private boolean isArrowKeyDown()
	{
		boolean result = false;

		if (getArrowState(ARROW.UP) | getArrowState(ARROW.DOWN) | getArrowState(ARROW.LEFT)
				| getArrowState(ARROW.RIGHT))
		{
			result = true;
		}

		return result;
	}

	// TODO properly handle these
	@Override
	public void keyboardFocusGained(PInputEvent event)
	{

	}

	@Override
	public void keyboardFocusLost(PInputEvent event)
	{

	}

	@Override
	public void mouseClicked(PInputEvent event)
	{
		if (null != event)
		{
			PNode clickedNode = event.getInputManager().getMouseOver().getPickedNode();

			if (clickedNode instanceof MapItem)
			{
				MapItem clickedItem = (MapItem) clickedNode;

				// only select the item clicked on, unless control is down
				if (event.isControlDown() && !event.isAltDown())
				{
					this.selectHandler.multiItemSelect(clickedItem);
				}
				else if ((event.isControlDown() && event.isAltDown()) || event.isMiddleMouseButton())
				{
					if (clickedItem instanceof FeatureItem)
					{
						FeatureItem clickedFeatureItem = (FeatureItem) clickedItem;
						String hyperlink = clickedFeatureItem.getHyperlink();
						clickedFeatureItem.setPaint(Color.ORANGE);

						if (hyperlink == null)
						{
							System.out.println("No hyperlink for feature");
						}
						else if (hyperlink != null && java.awt.Desktop.isDesktopSupported())
						{
							System.out.println("Opening " + hyperlink);
							Desktop desktop = Desktop.getDesktop();
							if (desktop.isSupported(Desktop.Action.BROWSE))
							{
								try
								{
									URI usageURI = new URI(hyperlink);
									desktop.browse(usageURI);
								}
								catch (URISyntaxException e1)
								{
									System.err.println("invalid hyperlink " + hyperlink);
								}
								catch (IOException e1)
								{
									e1.printStackTrace();
								}
							}
						}

					}
				}
				else if (event.isPopupTrigger())
				{
					// Do nothing..
				}
				else
				{
					this.selectHandler.singleItemSelect(clickedItem);
				}
			}
			else
			{
				this.selectHandler.unselectAll();
			}
		}
	}

	@Override
	public void mousePressed(PInputEvent event)
	{

	}

	@Override
	public void mouseWheelRotated(PInputEvent event)
	{
		// look at mouse wheel rotation more closely
		final double ROTATE_SCALE_DELTA = 0.1;
		final double INITAL_SCALE_FACTOR = 1.0;

		double scaleDelta = INITAL_SCALE_FACTOR - event.getWheelRotation() * ROTATE_SCALE_DELTA;

		Point2D zoomPoint = event.getPosition();

		if (!event.isControlDown())
		{
			if (GUISettings.getRenderingQuality() == GUISettings.RenderingQuality.LOW)
			{
				scheduleNewZoomTask(scaleDelta, zoomPoint);
			}
			else
			{
				zoomSubject.zoomStretch(scaleDelta, zoomPoint);
			}
		}
		else
		{
			// TODO I probably don't want to pass camera into here
			this.zoomSubject.zoomNormalDelta(scaleDelta, zoomPoint, this.camera);
		}
	}

	/**
	 * 
	 * @return The scale delta value of the zoom timer task or 1.0 if NULL.
	 */
	private synchronized Double getTaskScaleDelta()
	{
		if (this.zoomTask == null)
		{
			return 1.0;
		}
		else
		{
			return this.zoomTask.getScaleDelta();
		}
	}

	/**
	 * Resets the zoom task.
	 */
	private synchronized void resetZoomTask()
	{
		this.zoomTask = null;
	}

	/**
	 * This method schedules a new zoom timer task.
	 * 
	 * @param scaleDelta
	 *            The scale delta value of the timer task.
	 * @param zoomPoint
	 */
	private synchronized void scheduleNewZoomTask(double scaleDelta, Point2D zoomPoint)
	{
		this.zoomTask = (new ZoomTask(scaleDelta * getTaskScaleDelta(), zoomPoint));
		this.timer.schedule(this.zoomTask, 250);
	}

	/**
	 * 
	 * @param task
	 * @return Whether or not the passed task is the "active" task.
	 */
	private synchronized boolean isZoomTask(ZoomTask task)
	{
		return (this.zoomTask == task);
	}

	@Override
	public void mouseMoved(PInputEvent event)
	{
		handleNode(event);

		event.getInputManager().setKeyboardFocus(this); // sets this class as
														// the class to handle
														// keyboard events

		this.lastPosition = event.getPosition();
	}

	@Override
	public void mouseDragged(PInputEvent event)
	{
		handleNode(event);

		this.lastPosition = event.getPosition();
	}

	/**
	 * Handles the node the current event is over.
	 * 
	 * @param event
	 *            The current event to handle.
	 */
	private void handleNode(PInputEvent event)
	{
		PNode n = event.getInputManager().getMouseOver().getPickedNode();

		// not sure if this is the best way
		if (n instanceof MapItem)
		{
			MapItem item = (MapItem) n;

			Point2D itemCursorPosition = event.getCanvasPosition();
			event.getPath().canvasToLocal(itemCursorPosition, this.camera);

			this.mouseOverHandler.mouseOver(item, itemCursorPosition);
		}
		else
		{
			this.mouseOverHandler.mouseOver(null, null);
		}
	}

	// ---- Private Classes ---- //

	/**
	 * This class is an extension of a TimerTask.
	 * 
	 * It is intended to be used to delay the zooming event by allowing for a
	 * small window of time for more user input to be provided.
	 * 
	 * @author Eric Marinier
	 * 
	 */
	private class ZoomTask extends TimerTask
	{
		private final double scaleDelta;
		private final Point2D zoomPoint;

		public ZoomTask(double scaleDelta, Point2D zoomPoint)
		{
			this.scaleDelta = scaleDelta;
			this.zoomPoint = zoomPoint;
		}

		@Override
		public void run()
		{
			execute();
		}

		/**
		 * Executes the ZoomTask.
		 * 
		 * This needs to be synchronized.
		 */
		public synchronized void execute()
		{
			if (isZoomTask(this))
			{
				ThreadService.executeOnEDT(new Runnable()
				{
					@Override
					public void run()
					{
						zoomSubject.zoomStretch(scaleDelta, zoomPoint);
					}
				});

				resetZoomTask();
			}
		}

		/**
		 * 
		 * @return The scale delta value.
		 */
		public Double getScaleDelta()
		{
			return this.scaleDelta;
		}
	}

	/**
	 * This class is responsible for executing a move task, if applicable.
	 * 
	 * @author Eric Marinier
	 * 
	 */
	private class MoveTask extends TimerTask
	{
		private static final double INCREMENT = 10;

		@Override
		public void run()
		{
			ThreadService.executeOnEDT(new Runnable()
			{
				@Override
				public void run()
				{
					doRun();

					// Stop scheduling the task?
					if (!isArrowKeyDown())
					{
						moveTask.cancel();
						moveTask = null;
					}
				}
			});
		}

		/**
		 * Actually do the .run() of the class, but executed on the EDT.
		 */
		private void doRun()
		{
			if (isArrowKeyDown())
			{
				double distance = INCREMENT / camera.getViewScale();

				int x = 0;
				int y = 0;

				if (shift)
				{
					distance *= 2;
				}

				if (getArrowState(ARROW.UP))
				{
					y += -distance;
				}

				if (getArrowState(ARROW.DOWN))
				{
					y += distance;
				}

				if (getArrowState(ARROW.LEFT))
				{
					x += -distance;
				}

				if (getArrowState(ARROW.RIGHT))
				{
					x += distance;
				}

				camera.animateViewToPanToBounds(camera.getViewBounds().moveBy(x, y), MOVE_DELAY / 2);
			}
		}
	}
}
