package ca.corefacility.gview.main;

import jargs.gnu.CmdLineParser;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import ca.corefacility.gview.layout.sequence.LayoutFactory;
import ca.corefacility.gview.layout.sequence.LocationConverter;
import ca.corefacility.gview.layout.sequence.ZoomException;
import ca.corefacility.gview.layout.sequence.circular.LayoutFactoryCircular;
import ca.corefacility.gview.layout.sequence.linear.LayoutFactoryLinear;
import ca.corefacility.gview.map.GViewMap;
import ca.corefacility.gview.map.GViewMapFactory;
import ca.corefacility.gview.map.gui.GUIManager;
import ca.corefacility.gview.map.gui.GUISettings;
import ca.corefacility.gview.map.gui.GViewGUIFrame;
import ca.corefacility.gview.utils.FileLocationHandler;
import ca.corefacility.gview.utils.GViewInformation;
import ca.corefacility.gview.utils.thread.ThreadService;
import ca.corefacility.gview.writers.ImageWriter;
import ca.corefacility.gview.writers.ImageWriterFactory;

/**
 * The main class for the GView jar file.
 * 
 * @author aaron
 * 
 */
public class GViewMain
{
	private static final String DEFAULT_FORMAT = "png";

	private static void printUsage()
	{
		System.out.println("usage: java -jar gview.jar [OPTION]...");
		System.out.println("-h, --help                  Print the usage.");
		System.out.println("-H, --height <integer>      Specifies the height in pixels for the image.");
		System.out.println("-c, --centerBase <integer>  Specifies the base pair value to center on.");
		System.out.println("-i, --inputFile <file>      The input file to parse.");
		System.out.println("-l, --layout <layout>       The layout of the input, 'circular' or 'linear'.");
		System.out.println("-f, --imageFormat <format>  The format of the output: "
				+ Arrays.toString(ImageWriterFactory.OUTPUT_FORMATS) + " (default:" + DEFAULT_FORMAT + ")");
		System.out.println("-o, --outputFile <file>     The image file to create.");
		System.out.println("-v, --viewer                Loads up the input file in the genome viewer.");
		System.out.println("-s, --style <file>          The file to read the style information from.");
		System.out.println("-b, --birdsEyeView          Displays a birds eye view of the map.");
		System.out.println("                              This is ignored if -v is not used.");
		System.out.println("-g, --gffFile <file>        Specifies a .gff file with additional annotations to include.");
		System.out.println("                              Can be used multiple times for multiple files.");
		System.out.println("-W, --width <integer>       Specifies the width in pixels for the image.");
		System.out.println("-z, --zoomAmount <real>     The factor to zoom in by.");
		System.out.println("-t, --threads <integer>     The number of threads to use.");
		System.out.println("                              The default is the maximum threads the JVM can acquire.");
		System.out
				.println("-q, --quality <quality>     The initial rendering quality of the display, 'low' or 'high'.");
		System.out.println("                              The default quality is high.");
		System.out.println("--version                   Print version information.");
		System.out.println();
		System.out
				.println("Example:  java -jar gview.jar -i example_data/NC_007622.gbk -s example_styles/basicStyle.gss -l linear -W 1200 -H 900 -f png -o image.png");
		System.out
				.println("Example:  java -jar gview.jar -i example_data/NC_007622.gbk -s example_styles/gssExample.gss -l circular -v -b");
	}

	/**
	 * Obtains the region manager factory from the passed layout string.
	 * 
	 * @param layout
	 *            The layout to use (circular or linear).
	 * @return The correct region manager factory, defaults to linear if layout
	 *         is invalid.
	 */
	private static LayoutFactory createLayoutFactory(String layout)
	{
		LayoutFactory layoutFactory;

		if (layout == null)
		{
			layoutFactory = new LayoutFactoryLinear();
		}
		else if (layout.equalsIgnoreCase("circular") || layout.equalsIgnoreCase("c"))
		{
			layoutFactory = new LayoutFactoryCircular();
		}
		else if (layout.equalsIgnoreCase("linear") || layout.equalsIgnoreCase("l"))
		{
			layoutFactory = new LayoutFactoryLinear();
		}
		else
		{
			System.err.println("[warning] " + layout + " invalid layout.  Defaulting to linear.");
			layoutFactory = new LayoutFactoryLinear();
		}

		return layoutFactory;
	}

	private static void createViewer(GUIManager guiManager, GViewMap map, Boolean birdsEyeView, String title,
			String fileName)
	{
		if (map == null)
			return;

		GViewGUIFrame guiFrame = guiManager.buildGUIFrame(title, map, fileName);
		guiFrame.setVisible(true);

		// creates birds eye view if necessary
		if (birdsEyeView != null && birdsEyeView)
		{
			guiFrame.setBirdsEyeView(true);
		}

	}

	private static void handleCenterBase(GViewMap gViewMap, LocationConverter locationConverter, Integer centerBase)
	{
		if (gViewMap == null || gViewMap.getGenomeData() == null)
			return;

		if (centerBase != null)
		{
			if (gViewMap.getGenomeData().baseOnSequence(centerBase.intValue()))
			{
				gViewMap.setCenter(centerBase.intValue());
			}
			else
			{
				System.err.println("[warning] base=" + centerBase + " outside of range of 0 to "
						+ gViewMap.getGenomeData().getSequenceLength());
				System.err.println("Using default center base = 0");
				gViewMap.setCenter(0);
			}
		}
	}

	private static void handleWriteToFile(GViewMap gviewMap, String format, String outputFile)
	{
		if (gviewMap == null)
			throw new IllegalArgumentException("gviewMap cannot be null");

		if (format == null && outputFile == null)
		{
			System.err
					.println("[error] no output file format or output file location given. If you don't want to create an output file, use -v to create a viewer window.");
			return;
		}

		// either format or outputFile is non-null
		if (format != null)
		{
			if (outputFile != null)
			{
				ImageWriter writer = ImageWriterFactory.createImageWriter(format);
				if (writer == null)
				{
					System.err.println("[error] output file format '" + format + "' not recognized.");
				}
				else
				{
					try
					{
						writer.writeToImage(gviewMap, outputFile);
					}
					catch (IOException e)
					{
						System.err.println(e);
					}
				}
			}
			else
			{
				System.err.println("[error] no output filename specified.");
			}
		}
		else
		{
			System.err.println("[error] no output file format given.");
		}
	}

	private static void handleZoom(GViewMap gviewMap, double zoomFactor)
	{
		if (gviewMap == null)
			return;

		if (zoomFactor >= gviewMap.getMinZoomFactor() && zoomFactor <= gviewMap.getMaxZoomFactor())
		{
			try
			{
				gviewMap.setZoomFactor(zoomFactor);
			}
			catch (ZoomException e)
			{
				System.err.println("[warning]  " + e.getMessage());
			}
		}
		else
		{
			System.err.println("[warning]  zoom factor=" + zoomFactor + " is not within ("
					+ gviewMap.getMinZoomFactor() + "," + gviewMap.getMaxZoomFactor() + "]");
		}
	}

	/**
	 * This attempts to verify the Java version.
	 */
	private static void verifyJavaVersion()
	{
		String property = System.getProperty("java.version");

		try
		{
			double version = Double.parseDouble(property.substring(0, 3));

			if (version < 1.5)
			{
				System.err.println("[warning] - Java " + property + " may be incompatible with GView.");
			}
		}
		catch (NumberFormatException nfe)
		{
			System.err.println("[warning] - Java " + property + " is unrecognized and may be incompatible with GView.");
		}
	}

	public static void main(String[] args)
	{
		verifyJavaVersion();

		try
		{
 			GUIManager guiManager = GUIManager.getInstance();

			CmdLineParser parser = new CmdLineParser();

			// set up command line arguments
			CmdLineParser.Option birdsEyeView = parser.addBooleanOption('b', "birdsEyeView");
			CmdLineParser.Option centerBase = parser.addIntegerOption('c', "centerBase");
			CmdLineParser.Option format = parser.addStringOption('f', "imageFormat");
			CmdLineParser.Option input = parser.addStringOption('i', "inputFile");
			CmdLineParser.Option layout = parser.addStringOption('l', "layout");
			CmdLineParser.Option output = parser.addStringOption('o', "outputFile");
			CmdLineParser.Option style = parser.addStringOption('s', "style");
			CmdLineParser.Option gff = parser.addStringOption('g', "gffFile");
			CmdLineParser.Option viewer = parser.addBooleanOption('v', "viewer");
			CmdLineParser.Option zoom = parser.addDoubleOption('z', "zoomAmount");
			CmdLineParser.Option help = parser.addBooleanOption('h', "help");
			CmdLineParser.Option height = parser.addIntegerOption('H', "height");
			CmdLineParser.Option width = parser.addIntegerOption('W', "width");
			CmdLineParser.Option versionOpt = parser.addBooleanOption("version");

			CmdLineParser.Option legendDisplayOpt = parser.addBooleanOption("legend");
			CmdLineParser.Option threadsOpt = parser.addIntegerOption('t', "threads");
			CmdLineParser.Option qualityOpt = parser.addStringOption('q', "quality");

			// attempts to parse the command line
			try
			{
				parser.parse(args);
			}
			catch (CmdLineParser.OptionException e)
			{
				System.err.println(e.getMessage());
				printUsage();
				System.exit(1);
			}

			Boolean birdsEyeViewValue = (Boolean) parser.getOptionValue(birdsEyeView, Boolean.FALSE);
			Integer centerBaseValue = (Integer) parser.getOptionValue(centerBase);
			String formatValue = (String) parser.getOptionValue(format);
			String inputValue = (String) parser.getOptionValue(input);
			String layoutValue = (String) parser.getOptionValue(layout);
			String outputValue = (String) parser.getOptionValue(output);
			String styleValue = (String) parser.getOptionValue(style);
			Vector gffValue = parser.getOptionValues(gff);
			Boolean viewerValue = (Boolean) parser.getOptionValue(viewer, Boolean.FALSE);
			Double zoomValue = (Double) parser.getOptionValue(zoom);
			Boolean helpValue = (Boolean) parser.getOptionValue(help, Boolean.FALSE);
			Integer heightValue = (Integer) parser.getOptionValue(height);
			Integer widthValue = (Integer) parser.getOptionValue(width);
			Boolean versionValue = (Boolean) parser.getOptionValue(versionOpt, Boolean.FALSE);

			Boolean legendDisplayValue = (Boolean) parser.getOptionValue(legendDisplayOpt, Boolean.FALSE);
			Integer threadsValue = (Integer) parser.getOptionValue(threadsOpt);
			String qualityValue = (String) parser.getOptionValue(qualityOpt);

			setupEnvironmentVars();

			// Number of threads to use (should set very early!):
			if (threadsValue != null)
			{
				ThreadService.setNumThreads(threadsValue);
			}

			// Rendering quality:
			if (qualityValue != null && (qualityValue.equalsIgnoreCase("low") || qualityValue.equalsIgnoreCase("l")))
			{
				GUISettings.setRenderingQuality(GUISettings.RenderingQuality.LOW);
			}

			// No arguments; show open dialog:
			if (args.length == 0)
			{
				guiManager.showOpenDialog();
			}
			// Help:
			else if (helpValue != null && helpValue)
			{
				printUsage();
				System.exit(0);
			}
			// Version:
			else if (versionValue != null && versionValue)
			{
				GViewInformation information = GViewInformation.instance();

				System.out.println("GView version: "
						+ (information.getVersion() != null ? information.getVersion() : "unknown"));
				System.out.println("Revision: "
						+ (information.getBuildRevision() != null ? information.getBuildRevision() : "unknown"));
				System.out.println("Built on: "
						+ (information.getBuildDate() != null ? information.getBuildDate() : "unknown"));
				System.exit(0);
			}
			// No input value (bad):
			else if (inputValue == null)
			{
				printUsage();
				System.err.println("\n[error] Please specify an input file name.");
				System.exit(1);
			}
			// No viewer, but no output value specified (bad):
			else if (!viewerValue && outputValue == null)
			{
				printUsage();
				System.err
						.println("\n[error] Please either use the interactive viewer (-v) or specify an output file (-o)");
				System.exit(1);
			}
			// Continue building GViewMap:
			else
			{
				// Show splash screen if displaying GUI:
				if (viewerValue)
				{
					guiManager = GUIManager.getInstance();
					guiManager.getSplashScreen().showSplash(true);
				}

				// Input:
				URI sequenceURI = FileLocationHandler.getURI(inputValue);
				URI styleURI = null;
				ArrayList<URI> gffURI = new ArrayList<URI>();
				LayoutFactory layoutFactory = createLayoutFactory(layoutValue);

				// Handle style input:
				if (styleValue != null)
				{
					styleURI = FileLocationHandler.getURI(styleValue);
				}

				// Handle GFF(s) if a gffValue was specified:
				if (gffValue != null)
				{
					for (Object value : gffValue)
					{
						if (value instanceof String)
						{
							gffURI.add(FileLocationHandler.getURI((String) value));
						}
					}
				}

				// Build the GViewMap:
				GViewMap gViewMap = GViewMapFactory.createMap(sequenceURI, styleURI, gffURI, layoutFactory);

				// GUI:
				if (viewerValue)
				{
					// Create the GViewGUIFrame:
					// We should do this first because it will set MANY default
					// attributes
					// which we potentially need to override with command-line
					// arguments:
					createViewer(guiManager, gViewMap, birdsEyeViewValue, "GView", styleValue);

					gViewMap.getElementControl().setLegendDisplayed(legendDisplayValue);

					// handles zoom
					if (zoomValue != null)
					{
						handleZoom(gViewMap, zoomValue.doubleValue());
					}

					// handles center base
					if (centerBaseValue != null)
					{
						handleCenterBase(gViewMap, new LocationConverter(gViewMap.getGenomeData()), centerBaseValue);
					}
					else
					{
						gViewMap.centerMap(); // centre at middle of sequence
												// backbone
					}

					if (guiManager != null)
					{
						guiManager.getSplashScreen().showSplash(false);
					}
				}
				// No GUI:
				else
				{
					if (formatValue == null)
					{
						System.err.println("[warning] No output format set, using default of " + DEFAULT_FORMAT);
						formatValue = DEFAULT_FORMAT;
					}

					// always display legends for non-viewer maps
					gViewMap.getElementControl().setLegendDisplayed(true);

					// handles zoom
					if (zoomValue != null)
					{
						handleZoom(gViewMap, zoomValue.doubleValue());
					}

					if (centerBaseValue != null)
					{
						handleCenterBase(gViewMap, new LocationConverter(gViewMap.getGenomeData()), centerBaseValue);

						// resize map to match width/height
						if (widthValue != null && heightValue != null)
						{
							gViewMap.setViewSize(widthValue, heightValue);
						}
						else
						{
							final int defaultWidth = 800;
							final int defaultHeight = 600;
							gViewMap.setViewSize(defaultWidth, defaultHeight);
						}

						handleCenterBase(gViewMap, new LocationConverter(gViewMap.getGenomeData()), centerBaseValue);
					}
					else
					{
						final int minWidth = 400;
						final int minHeight = 400;

						final int widthSpacing = 40;
						final int heightSpacing = 40;

						gViewMap.centerMap();

						// resize map to match width/height
						if (widthValue != null && heightValue != null)
						{
							if (widthValue >= minWidth && heightValue >= minHeight)
							{
								gViewMap.setViewSize(widthValue - widthSpacing, heightValue - heightSpacing);
								gViewMap.scaleMapToScreen();
								gViewMap.setViewSize(widthValue, heightValue);
							}
							else
							{
								gViewMap.setViewSize(widthValue, heightValue);
								gViewMap.scaleMapToScreen();
							}
						}
						else
						{
							gViewMap.viewFullMap();
						}

						gViewMap.centerMap();
					}

					handleWriteToFile(gViewMap, formatValue, outputValue);
					System.exit(0);
				}
			}
		}
		catch (Exception e)
		{
			GUIManager guiManager = GUIManager.getInstance();
			guiManager.getSplashScreen().showSplash(false);
			e.printStackTrace();
		}
	}

	/**
	 * Sets up environment variables In particular, sets up proxy server
	 * information.
	 */
	private static void setupEnvironmentVars()
	{
		// setup proxy servers
		final String httpProxys = System.getenv("http_proxy");
		final String httpsProxys = System.getenv("https_proxy");

		if (httpProxys != null || httpsProxys != null)
		{
			ProxySelector ps = new ProxySelector()
			{
				private URI httpProxy;
				private URI httpsProxy;

				{
					if (httpProxys != null)
					{
						try
						{
							httpProxy = new URI(httpProxys);
						}
						catch (URISyntaxException e)
						{
							System.err.println("Invalid value for http_proxy " + httpProxy);
						}
					}

					if (httpsProxys != null)
					{
						try
						{
							httpsProxy = new URI(httpsProxys);
						}
						catch (URISyntaxException e)
						{
							System.err.println("Invalid value for https_proxy " + httpsProxy);
						}
					}
				}

				@Override
				public List<Proxy> select(URI uri)
				{
					List<Proxy> pList = new LinkedList<Proxy>();
					if (uri == null)
					{
						pList.add(Proxy.NO_PROXY);
					}
					else if ("http".equals(uri.getScheme()))
					{
						if (httpProxy != null)
						{
							int port = httpProxy.getPort();
							if (port < 0)
							{
								port = 80;
							}

							pList.add(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(httpProxy.getHost(), port)));
						}
						else
						{
							pList.add(Proxy.NO_PROXY);
						}
					}
					else if ("https".equals(uri.getScheme()))
					{
						if (httpsProxy != null)
						{
							int port = httpsProxy.getPort();
							if (port < 0)
							{
								port = 443;
							}

							pList.add(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(httpsProxy.getHost(), port)));
						}
						else
						{
							pList.add(Proxy.NO_PROXY);
						}
					}
					else
					{
						pList.add(Proxy.NO_PROXY);
					}

					return pList;
				}

				@Override
				public void connectFailed(URI uri, SocketAddress sa, IOException ioe)
				{
					System.err.println("[failed] - " + ioe + " Connection failed for " + uri + " " + sa);
				}

			};

			ProxySelector.setDefault(ps);
		}
	}
}
