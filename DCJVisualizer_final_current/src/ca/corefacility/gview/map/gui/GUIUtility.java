package ca.corefacility.gview.map.gui;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import ca.corefacility.gview.map.gui.dialog.BEVDialog;
import ca.corefacility.gview.utils.GViewInformation;
import ca.corefacility.gview.utils.thread.ThreadService;

/**
 * Provides utility for the GUI. Contains many constants needed by various GUI
 * components.
 * 
 * @author Eric Marinier
 * 
 */
public class GUIUtility
{
	public final static String OPEN_SHORTCUT = "ctrl O";
	public final static String FMTS_SHORTCUT = "ctrl V";
	public final static String LEGEND_SHORTCUT = "ctrl L";
	public final static String FULL_SCREEN_SHORTCUT = "F11";
	public final static String HELP_SHORTCUT = "F1";
	public final static String BEV_SHORTCUT = "ctrl B";
	public final static String MOVE_CUSTOM_SHORTCUT = "ctrl M";
	public final static String SCALE_CUSTOM_SHORTCUT = "ctrl C";
	public final static String ZOOM_CUSTOM_SHORTCUT = "ctrl Z";
	public final static String STYLE_EDITOR_SHORTCUT = "ctrl E";

	public final static String CUSTOM_TEXT = "Custom ...";
	public final static String FILE_TEXT = "File";
	public final static String EXIT_TEXT = "Exit";
	public final static String SCALE_TEXT = "Scale";
	public final static String BEV_TEXT = "Bird's Eye View";
	public final static String EXPORT_TEXT = "Export View As ...";
	public final static String FMTS_TEXT = "Fit Map to Screen";
	public static final String MOVE_TEXT = "Move";
	public static final String OPEN_TEXT = "Open";
	public static final String ZOOM_TEXT = "Zoom";
	public final static String HELP_TEXT = "Help";
	public static final String SHOW_ALL_TEXT = "Enable All";
	public static final String SHOW_LABELS_TEXT = "Enable Labels";
	public static final String SHOW_RULER_TEXT = "Enable Ruler";
	public static final String SHOW_LEGEND_TEXT = "Enable Legend";
	public static final String VIEWER_USAGE_TEXT = "Viewer Usage";
	public static final String WEBSITE_TEXT = "Website";
	public static final String VIEW_TEXT = "View";
	public static final String ABOUT_TEXT = "About";
	public static final String STYLE_TEXT = "Style";
	public static final String STYLE_EDITOR_TEXT = "Style Editor";
	public static final String QUALITY_TEXT = "Quality";
	public static final String HIGH_QUALITY_TEXT = "High";
	public static final String LOW_QUALITY_TEXT = "Low";

	public final static String ABOUT_DIALOG_TEXT = "<h1>GView</h1>" + "<p><b>Version:</b> "
			+ GViewInformation.instance().getVersion()
			+ "<br/>"
			+ "<b>Build Revision:</b> "
			+ GViewInformation.instance().getBuildRevision()
			+ "<br/>"
			+ "<b>Built On:</b> "
			+ GViewInformation.instance().getBuildDate()
			+ "<br/>"
			+ "<b>Website:</b>  <a href=\"http://www.gview.ca\">http://www.gview.ca</a></p>"
			+ "<p>"
			+ "<b>Dependencies:</b>"
			+ "<ul style='list-style-type: none;'>"
			+ "<li><a href='http://xmlgraphics.apache.org/batik/'>Batik SVG Toolkit</a></li>"
			+ "<li><a href='http://biojava.org/'>Biojava</a></li>"
			+ "<li><a href='http://javagraphics.blogspot.com/2007/04/jcolorchooser-making-alternative.html'>"
			+ "Colorpicker</a></li>"
			+ "<li><a href='http://cssparser.sourceforge.net/'>CSS Parser</a></li>"
			+ "<li><a href='http://jargs.sourceforge.net/'>JArgs</a></li>"
			+ "<li><a href='http://www.jgoodies.com/freeware/forms/'>JGoodies Forms</a></li>"
			+ "<li><a href='http://opencsv.sourceforge.net/'>Opencsv</a></li>"
			+ "<li><a href='http://www.piccolo2d.org/'>Piccolo2D</a></li>"
			+ "<li><a href='http://www.w3.org/Style/CSS/SAC/'>SAC</a></li>"
			+ "</ul>"
			+ "</p>"
			+ "<p>Some icons are copyright © <a href=\"http://p.yusukekamiyamane.com/\">Yusuke Kamiyamane</a>."
			+ "  All rights reserved.<br/>"
			+ " Licensed under a <a href=\"http://creativecommons.org/licenses/by/3.0/\">Creative Commons Attribution 3.0 license</a>.</p>";

	public final static String SCALE_IN = "Scale In";
	public final static String SCALE_OUT = "Scale Out";
	public final static String SCALE_CUSTOM = "Scale Custom";
	public final static String SCALE_10 = "10%";
	public final static String SCALE_25 = "25%";
	public final static String SCALE_50 = "50%";
	public final static String SCALE_100 = "100%";
	public final static String SCALE_200 = "200%";
	public final static String SCALE_400 = "400%";
	public final static String SCALE_800 = "800%";
	public final static String SCALE_1600 = "1600%";

	public final static String[] specificScaleItems = { SCALE_10, SCALE_25, SCALE_50, SCALE_100, SCALE_200, SCALE_400,
			SCALE_800, SCALE_1600 };
	public final static int[] specificScaleLevels = { 10, 25, 50, 100, 200, 400, 800, 1600 };

	public static final String ZOOM_OUT = "Zoom Out";
	public static final String ZOOM_IN = "Zoom In";
	public static final String ZOOM_100 = "100%";
	public static final String ZOOM_200 = "200%";
	public static final String ZOOM_400 = "400%";
	public static final String ZOOM_800 = "800%";
	public static final String ZOOM_1600 = "1600%";
	public static final String ZOOM_3200 = "3200%";
	public static final String ZOOM_CUSTOM = "Zoom Custom";

	public static final String[] specificZoomItems = { ZOOM_100, ZOOM_200, ZOOM_400, ZOOM_800, ZOOM_1600, ZOOM_3200 };
	public static final int[] specificZoomLevels = { 100, 200, 400, 800, 1600, 3200 };

	public static final String MOVE_BASE_CUSTOM = "Custom ...";
	public static final String START = "0%";
	public static final String QUARTER_1 = "25%";
	public static final String HALF = "50%";
	public static final String QUARTER_3 = "75%";
	public static final String END = "100%";

	public static final String MOVE_TO_START = "Move to Start";
	public static final String MOVE_TO_MIDDLE = "Move to Middle";
	public static final String MOVE_TO_END = "Move to End";

	public static final String[] constantBaseMoves = { START, QUARTER_1, HALF, QUARTER_3, END };

	public static final String OPEN = "Open";
	public final static String EXIT = "Exit";
	public final static String EXPORT = "Export";
	public final static String FULL_SCREEN = "Full Screen";
	public static final String FMTS = "Fit Image to Map";
	public static final String BEV = "Bird's Eye View";
	public static final String VIEWER_USAGE = "Viewer Usage";
	public static final String WEBSITE = "Website";
	public static final String ABOUT = "About";
	public static final String UNDO = "Undo";
	public static final String REDO = "Redo";

	public static final String USAGE_WEBSITE = "https://www.gview.ca/wiki/GViewDocumentation/GViewInteraction";
	public static final String URL = "https://www.gview.ca/";

	// The direct scale values that the gViewMap will expect.
	public final static double SCALE_MIN = 0.01;
	public final static double SCALE_MAX = 25;

	// To show the scale & zoom as a percent.
	public final static int SCALE_FACTOR = 100;

	public static final int ZOOM_MIN = 1;
	public static final int ZOOM_MAX = 100;

	// Allows for exponential scale slider functionality.
	public final static double SCALE_SLIDER_CONSTANT = 1.1;
	public final static double SCALE_SLIDER_MIN = Math.log(SCALE_MIN) / Math.log(SCALE_SLIDER_CONSTANT);
	public final static double SCALE_SLIDER_MAX = Math.log(SCALE_MAX) / Math.log(SCALE_SLIDER_CONSTANT);

	public static final String GLOBAL_STYLE_TEXT = "Global Style";
	public static final String GLOBAL_STYLE = "Global Style";
	public static final String BACKBONE_STYLE_TEXT = "Backbone Style";
	public static final String BACKBONE_STYLE = "Backbone Style";
	public static final String RULER_STYLE_TEXT = "Ruler Style";
	public static final String RULER_STYLE = "Ruler Style";
	public static final String TOOLTIP_STYLE_TEXT = "Tooltip Style";
	public static final String TOOLTIP_STYLE = "Tooltip Style";

	/**
	 * Converts a scale value to a slider value.
	 * 
	 * @param scale
	 *            The current scale value to be converted into corresponding
	 *            slider value. The "scale" in the "scale = b^x" equation;
	 *            returns the "x".
	 * 
	 * @return The corresponding slider value.
	 */
	public static int convertScaleToSlider(double scale)
	{
		int result;

		if (scale > 0)
		{
			result = (int) Math.round((Math.log(scale) / Math.log(SCALE_SLIDER_CONSTANT)));

			if (result > SCALE_SLIDER_MAX)
				result = (int) Math.round(SCALE_SLIDER_MAX);

			else if (result < SCALE_SLIDER_MIN)
				result = (int) Math.round(SCALE_SLIDER_MIN);
		}
		else
		{
			result = (int) Math.round(SCALE_SLIDER_MIN);
		}

		return result;
	}

	/**
	 * Converts the value on the scale slider to an appropriate integer to scale
	 * the gViewMap.
	 * 
	 * @param sliderValue
	 *            The slider value. The "x" in the "scale = b^x" equation;
	 *            returns the "scale".
	 * 
	 * @return The corresponding scale value.
	 */
	public static double convertSliderValueToScale(int sliderValue)
	{
		double result;

		if ((sliderValue) > SCALE_SLIDER_MAX)
			sliderValue = (int) Math.round(SCALE_SLIDER_MAX);

		else if ((sliderValue) < SCALE_SLIDER_MIN)
			sliderValue = (int) Math.round(SCALE_SLIDER_MIN);

		result = Math.pow(SCALE_SLIDER_CONSTANT, (double) sliderValue);

		return result;
	}

	/**
	 * Loads a buffered image from the location specified.
	 * 
	 * @param path
	 *            The location of the image. Expected to NOT start with a slash.
	 * @return The loaded buffered image.
	 */
	public static BufferedImage loadImage(String path)
	{
		if (path == null)
			throw new IllegalArgumentException("File path is null.");

		BufferedImage image = null;

		InputStream imageStream = BEVDialog.class.getResourceAsStream("/" + path);

		if (imageStream != null)
		{
			try
			{
				image = ImageIO.read(imageStream);
			}
			catch (IOException e)
			{
				System.err.println("[error] - io error loading image " + "/" + path + ": " + e);
			}
		}
		else
		{
			System.err.println("[warning] - image " + path + " not found in jar");

			// read image from filesystem
			// for testing without jaring
			try
			{
				image = ImageIO.read(new File(path));
			}
			catch (IOException e)
			{
				System.err.println("[error] - io error loading image " + path + ": " + e);
			}
		}

		return image;
	}

	/**
	 * Displays a pop-up notification about the limitations of using an XML
	 * file.
	 * 
	 * @param parent
	 *            The GUI component to display the pop-up over.
	 */
	public static void displayXMLNotification(final Component parent)
	{
		ThreadService.executeOnEDT(new Runnable()
		{
			@Override
			public void run()
			{
				JOptionPane.showMessageDialog(parent, "This feature is unavailable for XML files.");
			}
		});
	}
}
