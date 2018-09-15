package ca.corefacility.gview.map.controllers;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ca.corefacility.gview.map.event.GViewEvent;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.gui.GUIManager;
import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.GViewGUIFrame;
import ca.corefacility.gview.map.gui.GViewMapManager;
import ca.corefacility.gview.map.gui.GViewMapManager.Layout;
import ca.corefacility.gview.map.gui.ProgressNotification;
import ca.corefacility.gview.map.gui.Style;
import ca.corefacility.gview.map.gui.editor.StyleEditorFrame;
import ca.corefacility.gview.map.inputHandler.PopupInputEventHandler;
import ca.corefacility.gview.style.StyleFactory;
import ca.corefacility.gview.utils.thread.ThreadService;

/**
 * The GUI controller is responsible for maintaining a single instance of the
 * GUI in it's entirety.
 * 
 * @author Eric Marinier
 * 
 */
public class GUIController implements WindowListener, GViewEventListener
{
	// Default window size.
	private final int DEFAULT_WIDTH = 800;
	private final int DEFAULT_HEIGHT = 600;

	private final int DEFAULT_STYLE_EDITOR_WIDTH = 800;
	private final int DEFAULT_STYLE_EDITOR_HEIGHT = 400;

	private final ArrayList<Style> styles = new ArrayList<Style>();;

	private GViewGUIFrame gViewGUIFrame = null;
	private StyleEditorFrame styleEditorFrame = null;

	private Style currentStyle;

	private final ArrayList<GViewEventListener> listeners = new ArrayList<GViewEventListener>();

	/**
	 * Sets the GViewGUIFrame associated with this GUIController.
	 * 
	 * @param gViewGUIFrame
	 */
	public void setGViewGUIFrame(GViewGUIFrame gViewGUIFrame)
	{
		if (gViewGUIFrame == null)
		{
			throw new IllegalArgumentException("The GViewGUIFrame cannot be null.");
		}

		// Only set the frame if reference hasn't been set.
		if (this.gViewGUIFrame == null)
		{
			this.gViewGUIFrame = gViewGUIFrame;
			this.gViewGUIFrame.addWindowListener(this);
		}
		else
		{
			throw new IllegalArgumentException("GViewGUIFrame should only be set once.");
		}
	}

	/**
	 * Sets the StyleEditorFrame associated with this GUIController.
	 * 
	 * @param styleEditorFrame
	 */
	public void setStyleEditor(StyleEditorFrame styleEditorFrame)
	{
		if (styleEditorFrame == null)
		{
			throw new IllegalArgumentException("The StyleEditorFrame cannot be null.");
		}

		// Only set the frame if reference hasn't been set.
		if (this.styleEditorFrame == null)
		{
			this.styleEditorFrame = styleEditorFrame;
		}
		else
		{
			throw new IllegalArgumentException("StyleEditorFrame should only be set once.");
		}
	}

	/**
	 * 
	 * @return All of the styles associated with this controller.
	 */
	public ArrayList<Style> getStyles()
	{
		return this.styles;
	}

	/**
	 * 
	 * @return The GViewGUIFrame associated with this controller.
	 */
	public GViewGUIFrame getGViewGUIFrame()
	{
		return this.gViewGUIFrame;
	}

	/**
	 * 
	 * @return The StyleEditorFrame associated with this controller.
	 */
	public StyleEditorFrame getStyleEditor()
	{
		return this.styleEditorFrame;
	}

	/**
	 * Attempts to set the size of the GUI.
	 * 
	 * @param width
	 * @param height
	 */
	public void setGUISize(int width, int height)
	{
		// Catch very, very small windows.
		if (width < 100)
			width = 100;

		if (height < 100)
			height = 100;

		this.gViewGUIFrame.setFrameSize(width, height);

		if (this.currentStyle != null)
		{
			StyleController controller = this.currentStyle.getStyleController();
			controller.getGViewMapManager().setMapSize(width, height);
		}
	}

	/**
	 * Attempts to set the visibility of the GUI frame.
	 * 
	 * @param visible
	 */
	public void setGUIVisible(boolean visible)
	{
		if (visible)
		{
			this.gViewGUIFrame.setVisible(true);
			setCurrentMapVisible(true);
		}
		else
		{
			this.gViewGUIFrame.setVisible(false);
			setCurrentMapVisible(false);
		}
	}

	/**
	 * Attempts to set the visibility of the map.
	 * 
	 * @param visible
	 */
	private void setCurrentMapVisible(boolean visible)
	{
		if (this.currentStyle != null)
		{
			StyleController controller = this.currentStyle.getStyleController();
			controller.getGViewMapManager().setMapVisible(visible);
		}
	}

	/**
	 * Attempts to set the visibility of the style editor.
	 * 
	 * @param visible
	 */
	public void setStyleEditorVisibile(boolean visible)
	{
		this.styleEditorFrame.setVisible(visible);
	}

	/**
	 * Initializes the GUI.
	 */
	public void initializeGUI()
	{
		GViewMapManager gViewMapManager = getCurrentStyleMapManager();

		// Check that all components have been added:
		if (this.gViewGUIFrame == null || this.styleEditorFrame == null)
		{
			throw new NullPointerException("Not all GUI components have been added.");
		}

		ThreadService.executeOnEDT(new Runnable()
		{
			@Override
			public void run()
			{
				GViewMapManager gViewMapManager = getCurrentStyleMapManager();

				// Setting the size:
				setGUISize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
				styleEditorFrame.setSize(DEFAULT_STYLE_EDITOR_WIDTH, DEFAULT_STYLE_EDITOR_HEIGHT);
				resetGUIPosition();

				getCurrentStyle().getStyleController().getGViewMapManager()
						.addMapToPanel((JPanel) gViewGUIFrame.getContentPane());
				gViewGUIFrame.validate();

				// Ensure the display is rendering correctly:
				getCurrentStyle().getStyleController().getGViewMapManager().refresh();

				// Scale map to screen:
				gViewMapManager.scaleMapToScreen();

				// Set legends to visible:
				getCurrentStyleMapManager().getElementControl().setLegendDisplayed(false);
			}
		});

		// Add event listener(s):
		gViewMapManager.addEventListener(this);
	}

	/**
	 * Sets the GUI size and position.
	 */
	private void resetGUIPosition()
	{
		// get screen size for displaying splash screen
		// assumes possibility of more than one display
		int screenWidth, screenHeight;

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gd = ge.getScreenDevices();
		if (gd == null || gd.length == 0)
		{
			System.err.println("[warning] - could not get screen resolution");
			screenWidth = 800;
			screenHeight = 600;
		}
		else
		{
			DisplayMode d = gd[0].getDisplayMode();

			if (d != null)
			{
				screenWidth = d.getWidth();
				screenHeight = d.getHeight();
			}
			else
			{
				System.err.println("[warning] - could not get screen resolution");
				screenWidth = 800;
				screenHeight = 600;
			}
		}

		this.gViewGUIFrame.setLocation((screenWidth - DEFAULT_WIDTH) / 2, (screenHeight - DEFAULT_HEIGHT) / 2);
		this.styleEditorFrame.setLocation((screenWidth - DEFAULT_WIDTH) / 2, (screenHeight - DEFAULT_HEIGHT) / 2);
	}

	/**
	 * Attempts to reset the position of the style editor so that it appears
	 * relative to the GUI frame.
	 */
	public void resetStyleEditorPositionRelativeToFrame()
	{
		this.styleEditorFrame.setExtendedState(JFrame.NORMAL);

		this.styleEditorFrame.setSize(DEFAULT_STYLE_EDITOR_WIDTH, DEFAULT_STYLE_EDITOR_HEIGHT);
		this.styleEditorFrame.setLocation((this.gViewGUIFrame.getLocation()));
	}

	/**
	 * Disposes of the controller's GUI components.
	 */
	public void disposeGUI()
	{
		this.styleEditorFrame.dispose();
		this.gViewGUIFrame.dispose();

		this.listeners.clear();
		this.styles.clear();
	}

	/**
	 * Sets the initial style.
	 * 
	 * This is important because of how the updating of components can work. It
	 * is helpful to have a style initialized before various other GUI
	 * components are initialized.
	 * 
	 * @param style
	 *            The initial style to use.
	 */
	public void setInitialStyle(Style style)
	{
		if (style == null)
		{
			throw new IllegalArgumentException("Style cannot be null.");
		}

		if (this.currentStyle != null)
		{
			throw new IllegalArgumentException("This method should only be called when the current style is null!");
		}

		this.currentStyle = style;

		initializeStyle(style);

		this.styles.add(style);
	}

	/**
	 * Initializes the style.
	 * 
	 * This is intended to be used by both setInitialStyle() and addStyle().
	 * 
	 * @param style
	 *            The style to initialize.
	 */
	private void initializeStyle(Style style)
	{
		addRightClickEventHandler(style);

		GViewMapManager gViewMapManager = style.getStyleController().getGViewMapManager();
		LabelStyleController labelStyleController = style.getStyleController().getLabelStyleController();

		gViewMapManager.initializeLocks(labelStyleController);
	}

	/**
	 * Adds a new style and automatically makes it the active style.
	 * 
	 * @param style
	 */
	public void addStyle(Style style)
	{
		// Should be first:
		this.styles.add(style);

		initializeStyle(style);

		// Should be last:
		this.setStyle(style);
	}

	/**
	 * Adds the right click event handler.
	 * 
	 * @param style
	 */
	private void addRightClickEventHandler(Style style)
	{
		// Add right-click event handler:
		style.getStyleController().getGViewMapManager().addInputEventListener(new PopupInputEventHandler(this));
	}

	/**
	 * Sets the active style.
	 * 
	 * @param style
	 */
	public void setStyle(final Style style)
	{
		if (style == null)
		{
			throw new IllegalArgumentException("Style cannot be null.");
		}
		// It --MUST NOT-- be a new style. (Use .addStyle()).
		else if (!this.styles.contains(style))
		{
			throw new IllegalArgumentException("Cannot set style to a new style.");
		}

		this.getCurrentStyleMapManager().removeEventListener(this);
		this.currentStyle = style;
		this.getCurrentStyleMapManager().addEventListener(this);

		ThreadService.executeOnEDT(new Runnable()
		{
			@Override
			public void run()
			{
				gViewGUIFrame.update();
				styleEditorFrame.update();
				style.getStyleController().getGViewMapManager().refresh();
			}
		});
	}

	/**
	 * This method will rebuild the current style.
	 */
	public void rebuildStyle()
	{
		GViewMapManager gViewMapManager = this.currentStyle.getStyleController().getGViewMapManager();

		// Is there any rebuild required?
		if (this.currentStyle.getStyleController().isRebuildRequired())
		{
			this.getCurrentStyleMapManager().removeEventListener(this);
			gViewMapManager.updateMap();
			this.getCurrentStyleMapManager().addEventListener(this);

			// Execution:
			ThreadService.executeOnEDT(new Runnable()
			{
				@Override
				public void run()
				{
					getCurrentStyleMapManager().refresh();

					gViewGUIFrame.getBEVDialog().update();
				}
			});

			// Reset the (multiple) build required flags:
			this.currentStyle.getStyleController().resetRebuildRequired();
		}
	}

	/**
	 * 
	 * @return The current style.
	 */
	public Style getCurrentStyle()
	{
		return this.currentStyle;
	}

	/**
	 * Returns the current directory. Intended to be used with a JFileChooser.
	 * 
	 * @return The current directory.
	 */
	public File getCurrentDirectory()
	{
		return GUIManager.getInstance().getCurrentDirectory();
	}

	/**
	 * Sets the current directory. Intended to be used with a JFileChooser.
	 * 
	 * @param current
	 *            The new current directory.
	 */
	public void setCurrentDirectory(File current)
	{
		GUIManager.getInstance().setCurrentDirectory(current);
	}

	/**
	 * Attempts to remove the passed style.
	 * 
	 * @param style
	 *            The style to remove.
	 */
	public void removeStyle(Style style)
	{
		if (this.getCurrentStyle().isXML())
		{
			GUIUtility.displayXMLNotification(this.getStyleEditor());
			return;
		}

		if (this.styles.size() > 1)
		{
			this.styles.remove(style);

			// Set the current style to something.
			if (this.currentStyle == style)
			{
				this.currentStyle = this.styles.get(0);
			}

			this.gViewGUIFrame.update();
			this.styleEditorFrame.update();
		}
	}

	/**
	 * Attempts to write the current style to a file. This will update the name
	 * of the style to the name of the file.
	 * 
	 * @param file
	 *            The target file.
	 * @throws IOException
	 */
	public void writeCurrentStyleToFile(File file) throws IOException
	{
		this.writeStyleToFile(file, this.currentStyle);
	}

	/**
	 * Attempts to write the passed style to a file. This will update the name
	 * of the style to the name of the file.
	 * 
	 * @param file
	 *            The target file.
	 * @param style
	 *            The style to write to file.
	 * @throws IOException
	 */
	public void writeStyleToFile(File file, Style style) throws IOException
	{
		if (file == null)
		{
			throw new IllegalArgumentException("The target file cannot be null.");
		}

		if (style == null)
		{
			throw new IllegalArgumentException("The style cannot be null.");
		}

		style.getStyleController().getFileWriterController().writeStyleToFile(file);
		style.setName(file.getName());

		this.styleEditorFrame.synchronizeStyles();
	}

	/**
	 * 
	 * @return The map manager of the current style.
	 */
	public GViewMapManager getCurrentStyleMapManager()
	{
		return this.currentStyle.getStyleController().getGViewMapManager();
	}

	@Override
	public void windowOpened(WindowEvent e)
	{
	}

	@Override
	public void windowClosing(WindowEvent e)
	{
		GUIManager.getInstance().disposeGUI(this);
	}

	@Override
	public void windowClosed(WindowEvent e)
	{
	}

	@Override
	public void windowIconified(WindowEvent e)
	{
	}

	@Override
	public void windowDeiconified(WindowEvent e)
	{
	}

	@Override
	public void windowActivated(WindowEvent e)
	{
	}

	@Override
	public void windowDeactivated(WindowEvent e)
	{
	}

	@Override
	public void eventOccured(GViewEvent event)
	{
		for (int i = 0; i < this.listeners.size(); i++)
		{
			this.listeners.get(i).eventOccured(event);
		}
	}

	/**
	 * Adds a GViewEventListener to the GUI controller. This will ensure that
	 * components continue to listen to the active events, even after the
	 * GViewMap has been switched or rebuilt entirely.
	 * 
	 * @param listener
	 */
	public void addEventListener(GViewEventListener listener)
	{
		this.listeners.add(listener);
	}

	/**
	 * Removes the listener from the list of event listeners.
	 * 
	 * @param listener
	 */
	public void removeEventListener(GViewEventListener listener)
	{
		this.listeners.remove(listener);
	}

	/**
	 * Sets the layout of the current style.
	 * 
	 * @param layout
	 */
	public void setCurrentLayout(Layout layout)
	{
		Style current = this.getCurrentStyle();

		// Only change the layout if it's different:
		if (this.getCurrentStyleMapManager().getLayout() != layout && layout != Layout.UNKNOWN && layout != null)
		{
			Style result = StyleFactory.changeStyleLayout(current, layout);

			// Order is important (can't remove the last and only style)
			this.addStyle(result);
			this.removeStyle(current);
		}
	}

	/**
	 * This method displays the progress notification dialog while working on
	 * rebuilding the GViewMap.
	 * 
	 * This will create and execute a NEW thread for the passed work, which will
	 * then execute OFF the Swing Event Dispatch Thread. Ensure that all work
	 * done to Swing components (note: GViewMap is a PCanvas, which is a
	 * JComponent, which is Swing) is done by invoking
	 * ThreadService.executeOnEDT(Runnable) method that has been implemented.
	 * 
	 * @param rebuild
	 *            The gViewMap rebuilding Runnable object.
	 */
	public synchronized void displayProgressWhileRebuilding(final Runnable rebuild)
	{
		final ProgressNotification dialog = new ProgressNotification(this.gViewGUIFrame.getRootPane());

		Thread thread = (new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				rebuild.run();

				dialog.dispose();
			}

		}));

		thread.start();

		dialog.setVisible(true);

		while (thread.isAlive())
			;
	}
}
