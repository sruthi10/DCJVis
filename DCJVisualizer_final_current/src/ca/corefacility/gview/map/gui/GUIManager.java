package ca.corefacility.gview.map.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import ca.corefacility.gview.data.readers.GViewDataParseException;
import ca.corefacility.gview.layout.sequence.LayoutFactory;
import ca.corefacility.gview.main.SplashScreen;
import ca.corefacility.gview.map.GViewMap;
import ca.corefacility.gview.map.GViewMapFactory;
import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.editor.StyleEditorFrame;
import ca.corefacility.gview.map.gui.editor.StyleNameManager;
import ca.corefacility.gview.map.gui.open.OpenDialog;
import ca.corefacility.gview.utils.FileLocationHandler;
import ca.corefacility.gview.utils.thread.ThreadService;

public class GUIManager
{
	private static GUIManager instance = null;

	private SplashScreen splash;
	private static final String SPLASH_PATH = "/images/splash.png";

	private final ArrayList<GUIController> guiControllers = new ArrayList<GUIController>();

	private File currentDirectory;

	private GUIManager()
	{
		setLookAndFeel();
	}

	public static synchronized GUIManager getInstance()
	{
		if (instance == null)
		{
			instance = new GUIManager();
		}

		return instance;
	}

	/**
	 * Attempt to set native look-and-feel if possible.
	 */
	private static void setLookAndFeel()
	{
		String currentLookAndFeel = UIManager.getSystemLookAndFeelClassName();

		// GTK (Linux):
		if (currentLookAndFeel.contains("GTKLookAndFeel"))
		{
			try
			{
				for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
				{
					if ("Nimbus".equals(info.getName()))
					{
						UIManager.setLookAndFeel(info.getClassName());

						break;
					}
				}
			}
			catch (Exception e)
			{
				System.err.println("Could not set Nimbus Look and Feel" + e);
			}

		}
		// Windows:
		else if (currentLookAndFeel.contains("Windows"))
		{
			setWindowsLookAndFeel();
		}
		// Others:
		else
		{
			setSystemDefaultLookAndFeel();
		}
	}

	/**
	 * Attempts to set the windows look and feel, with a few adjustments.
	 */
	private static void setWindowsLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			UIDefaults defaults = UIManager.getDefaults();

			// ComboBox changes:
			defaults.put("ComboBox.border", new EmptyBorder(0, 2, 0, 2));

			// ToolBar changes:
			Border line = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray);
			defaults.put("ToolBar.border", line);
		}
		catch (Exception e)
		{
			System.err.println("Could not set Windows Look and Feel: " + e);
			try
			{
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			}
			catch (Exception e1)
			{
				System.err.println("Could not set cross platform Look and Feel: " + e1);
				setJavaDefaultLookAndFeel();
			}
		}
	}

	/**
	 * Attempts to set the system look and feel.
	 */
	private static void setSystemDefaultLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			System.err.println("Could not set native Look and Feel: " + e);
			try
			{
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			}
			catch (Exception e1)
			{
				System.err.println("Could not set cross platform Look and Feel: " + e1);
				setJavaDefaultLookAndFeel();
			}
		}
	}

	/**
	 * Attempts to set the Java default look and feel.
	 */
	private static void setJavaDefaultLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
		}
		catch (Exception e2)
		{
			System.err.println("Could not set Motif Look and Feel: " + e2);
		}
	}

	/**
	 * 
	 * @return The image for the splash screen.
	 */
	private BufferedImage getImageForSplash()
	{
		BufferedImage image = null;

		InputStream imageStream = GUIManager.class.getResourceAsStream(SPLASH_PATH);

		if (imageStream != null)
		{
			try
			{
				image = ImageIO.read(imageStream);
			}
			catch (IOException e)
			{
				System.err.println("[error] - io error loading splash image " + SPLASH_PATH + ": " + e);
			}
		}
		else
		{
			System.err.println("[warning] - image " + SPLASH_PATH + " not found in jar");

			// read image from filesystem
			// for testing without jaring
			try
			{
				image = ImageIO.read(new File("images/splash.png"));
			}
			catch (IOException e)
			{
				System.err.println("[error] - io error loading splash image " + SPLASH_PATH + ": " + e);
			}
		}

		return image;
	}

	/**
	 * This is the only method of creating the GUI that makes any guarantees of
	 * having all functionality.
	 * 
	 * @param title
	 *            The title of the GViewGUIFrame.
	 * @param fullScreen
	 *            Whether or not the GViewGUIFrame should launch in full screen
	 *            mode.
	 * @param gViewMap
	 *            The initial GViewMap object.
	 * @param uri
	 *            The URI of the provided STYLE file.
	 * 
	 * @return The newly created GViewGUIFrame.
	 */
	private GViewGUIFrame buildGUI(final String title, final boolean fullScreen, GViewMap gViewMap, URI uri)
	{
		// ORDER IS IMPORTANT!
		final GUIController guiController = new GUIController();

		final Style style = new Style(gViewMap, StyleNameManager.getStyleNameFromFile(uri), uri);
		guiController.setInitialStyle(style);

		// Should not be realized by this point; should be safe outside EDT.
		final GViewGUIFrame gViewGUIFrame = new GViewGUIFrame(guiController);

		ThreadService.executeOnEDT(new Runnable()
		{
			@Override
			public void run()
			{
				guiController.setGViewGUIFrame(gViewGUIFrame);
				gViewGUIFrame.build(title, fullScreen);

				final StyleEditorFrame styleEditorFrame = new StyleEditorFrame(guiController);
				guiController.setStyleEditor(styleEditorFrame);

				// Add controller to maintained list of controllers:
				guiControllers.add(guiController);

				// Initialization:
				guiController.initializeGUI();
			}
		});

		// Display
		guiController.setGUIVisible(true);

		// Request focus:
		if (gViewMap instanceof Component)
		{
			((Component) gViewMap).requestFocus();
		}

		return gViewGUIFrame;
	}

	/**
	 * 
	 * @param title
	 *            The title of the GViewGUIFrame.
	 * @param gViewMap
	 *            The initial GViewMap object.
	 * 
	 * @return The newly created GViewGUIFrame.
	 */
	public GViewGUIFrame buildGUIFrame(String title, GViewMap gViewMap)
	{
		return buildGUI(title, false, gViewMap, null);
	}

	/**
	 * 
	 * @param title
	 *            The title of the GViewGUIFrame.
	 * @param gViewMap
	 *            The initial GViewMap object.
	 * @param fileName
	 *            The name of the STYLE file.
	 * 
	 * @return The newly created GViewGUIFrame.
	 */
	public GViewGUIFrame buildGUIFrame(String title, GViewMap gViewMap, String fileName)
	{
		URI uri = null;

		if (fileName != null)
		{
			try
			{
				uri = FileLocationHandler.getURI(fileName);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		return buildGUI(title, false, gViewMap, uri);
	}

	/**
	 * 
	 * @param title
	 *            The title of the GViewGUIFrame.
	 * @param gViewMap
	 *            The initial GViewMap object.
	 * @param uri
	 *            The URI of the provided STYLE file.
	 * 
	 * @return The newly created GViewGUIFrame.
	 */
	public GViewGUIFrame buildGUIFrame(String title, GViewMap gViewMap, URI uri)
	{
		return buildGUI(title, false, gViewMap, uri);
	}

	/**
	 * 
	 * @return The splash screen.
	 */
	public SplashScreen getSplashScreen()
	{
		if (splash == null)
		{
			BufferedImage image = getImageForSplash();
			splash = new SplashScreen(image);
		}

		return splash;
	}

	/**
	 * Shows the OpenDialog.
	 */
	public void showOpenDialog()
	{
		OpenDialog.showOpenDialog();
	}

	/**
	 * 
	 * @return The number of GUI instances currently active.
	 */
	public synchronized int getNumGUIInstances()
	{
		return this.guiControllers.size();
	}

	/**
	 * Disposes of the GUIController and it's related GUIComponents. Handles the
	 * case when there are no more GUIControllers active.
	 * 
	 * @param controller
	 *            The GUIController to dispose.
	 */
	public void disposeGUI(GUIController controller)
	{
		if (controller == null)
		{
			throw new IllegalArgumentException("The controller cannot be null.");
		}

		this.guiControllers.remove(controller);
		controller.disposeGUI();

		if (this.getNumGUIInstances() <= 0)
		{
			System.exit(0);
		}
	}

	/**
	 * 
	 * @return The current "working" directory.
	 */
	public File getCurrentDirectory()
	{
		return this.currentDirectory;
	}

	/**
	 * Sets the current "working" directory.
	 * 
	 * @param file
	 */
	public void setCurrentDirectory(File file)
	{
		this.currentDirectory = file;
	}

	/**
	 * Refreshes the display of all the active GViewMap's in all the
	 * GUIControllers.
	 */
	public void refreshDisplay()
	{
		for (GUIController controller : guiControllers)
		{
			controller.getCurrentStyle().getStyleController().getGViewMapManager().refresh();
		}
	}

	/**
	 * Updates the active styles and their associated GUI elements.
	 */
	public void updateStyles()
	{
		for (GUIController controller : guiControllers)
		{
			controller.getStyleEditor().updateStyles();
		}
	}

	/**
	 * 
	 * @param sequence
	 *            The sequence.
	 * @param style
	 *            The style.
	 * @param gff
	 *            A single GFF file.
	 * @param layout
	 *            The layout to use.
	 * @return The newly created GViewGUIFrame.
	 * @throws IOException
	 * @throws GViewDataParseException
	 */
	public GViewGUIFrame buildGViewGUIFrameFromInput(URI sequence, URI style, URI gff, LayoutFactory layout)
			throws IOException, GViewDataParseException
	{
		ArrayList<URI> gffs = new ArrayList<URI>();
		gffs.add(gff);

		return this.buildGViewGUIFrameFromInput(sequence, style, gffs, layout);
	}

	/**
	 * 
	 * @param sequence
	 *            The sequence.
	 * @param style
	 *            The style.
	 * @param gffs
	 *            A list of GFF files.
	 * @param layout
	 *            The layout to use.
	 * @return The newly created GViewGUIFrame.
	 * @throws IOException
	 * @throws GViewDataParseException
	 */
	public GViewGUIFrame buildGViewGUIFrameFromInput(URI sequence, URI style, ArrayList<URI> gffs, LayoutFactory layout)
			throws IOException, GViewDataParseException
	{
		GViewMap gViewMap = GViewMapFactory.createMap(sequence, style, gffs, layout);
		GViewGUIFrame guiFrame;

		// Frame:
		if (style != null)
		{
			// We can pass the style file URI:
			guiFrame = GUIManager.getInstance().buildGUIFrame("GView", gViewMap, style);
		}
		else
		{
			// We can't pass the style file URI:
			guiFrame = GUIManager.getInstance().buildGUIFrame("GView", gViewMap);
		}

		return guiFrame;
	}
}
