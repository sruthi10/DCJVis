package ca.corefacility.gview.utils;

import java.util.ArrayList;
import java.util.HashMap;

import ca.corefacility.gview.main.SplashScreen;
import ca.corefacility.gview.map.gui.ProgressNotifier;

/**
 * Responsible for watching the loading progress of the program and displaying
 * the appropriate information.
 * 
 * @author Eric Marinier
 * 
 */
public class ProgressHandler
{
	public static int MINIMUM_PROGRESS = 0;
	public static int MAXIMUM_PROGRESS = 100;

	public static final String NONE_STRING = "Working..."; // default stage;
															// used as an
															// intermediate
															// stage
	public static final String CREATING_LAYOUT_STRING = "Creating Layout";
	public static final String BUILDING_FEATURES_STRING = "Building Features";
	public static final String POSITIONING_LABELS_STRING = "Positioning Labels";
	public static final String CALCULATING_GC_CONTENT_STRING = "Calculating GC Content";
	public static final String CALCULATING_GC_SKEW_STRING = "Calculating GC Skew";
	public static final String CREATING_PLOTS_STRING = "Creating Plots";
	public static final String FINALIZING_STRING = "Finalizing";

	public static enum Stage
	{
		NONE, CREATING_LAYOUT, BUILDING_FEATURES, POSITIONING_LABELS, CREATING_PLOTS,
	};

	private static final ArrayList<Stage> stages = new ArrayList<Stage>();

	private static final HashMap<Stage, String> stageStrings = new HashMap<Stage, String>();

	static
	{
		stageStrings.put(Stage.NONE, NONE_STRING);
		stageStrings.put(Stage.CREATING_LAYOUT, CREATING_LAYOUT_STRING);
		stageStrings.put(Stage.BUILDING_FEATURES, BUILDING_FEATURES_STRING);
		stageStrings.put(Stage.POSITIONING_LABELS, POSITIONING_LABELS_STRING);
		stageStrings.put(Stage.CREATING_PLOTS, CREATING_PLOTS_STRING);

		stages.add(Stage.CREATING_LAYOUT);
		stages.add(Stage.BUILDING_FEATURES);
		stages.add(Stage.POSITIONING_LABELS);
		stages.add(Stage.CREATING_PLOTS);
	}

	private static final ArrayList<ProgressNotifier> notifiers = new ArrayList<ProgressNotifier>();
	private static SplashScreen splashScreen = null;

	private static Stage currentStage = Stage.NONE;

	/**
	 * Initialize without a splash screen.
	 */
	public static void initialize()
	{
		initialize(null);
	}

	/**
	 * Initialize with a splash screen.
	 */
	public static void initialize(SplashScreen splash)
	{
		splashScreen = splash;
		currentStage = Stage.NONE;

		notifiers.clear();

		if (splashScreen != null)
		{
			notifiers.add(splashScreen);
		}
	}

	/**
	 * Adds the passed notifier to the list of notifiers.
	 * 
	 * @param notifier
	 *            The notifier to add.
	 */
	public static void addNotifier(ProgressNotifier notifier)
	{
		if (!notifiers.contains(notifier))
		{
			notifiers.add(notifier);
		}
	}

	/**
	 * Starts the passed stage.
	 * 
	 * @param stage
	 *            The stage to start.
	 * 
	 */
	public static void start(Stage stage)
	{
		// Not currently in a stage, requesting to start a non-NONE stage, and
		// haven't completed the passed stage:
		if (currentStage == Stage.NONE && stage != Stage.NONE && stage != null)
		{
			currentStage = stage;

			for (ProgressNotifier notifier : notifiers)
			{
				notifier.setMessage(stageStrings.get(stage));
			}

			System.out.print(stageStrings.get(stage) + "...");
		}
	}

	/**
	 * Finishes the passed stage. Checks to see if it is a valid stage.
	 * 
	 * @param stage
	 *            The stage to finish.
	 * 
	 */
	public static void finish(Stage stage)
	{
		int progress;

		// Finishing the current state?
		if (stage == currentStage && stage != Stage.NONE && stage != null)
		{
			currentStage = Stage.NONE;

			progress = (int) ((double) (stages.indexOf(stage) + 1) * ((double) MAXIMUM_PROGRESS / (double) stages
					.size()));

			for (ProgressNotifier notifier : notifiers)
			{
				notifier.setProgress(progress);
			}

			System.out.println(" done");
		}
	}

	/**
	 * Displays the passed message.
	 * 
	 * @param message
	 *            The message to display.
	 */
	public static void setMessage(String message)
	{
		for (ProgressNotifier notifier : notifiers)
		{
			notifier.setMessage(message);
		}

		System.out.print(message);
	}

	/**
	 * 
	 * @return The progress handler's current stage.
	 */
	public static Stage getStage()
	{
		return currentStage;
	}
}
