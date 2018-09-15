package ca.corefacility.gview.managers;

import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.map.event.ResolutionSwitchEvent;

/**
 * Handles the calculation of when to switch resolutions based upon the current
 * scale. A resolution switch is a point at which more detail should be drawn on
 * the map, such as increasing the tick marks on the ruler etc.
 * 
 * @author Aaron Petkau
 * 
 */
public class ResolutionManager
{
	private final double startingScale; // the starting scale, which is just
										// above the min scale
	private final double switchScale;
	private final double logSwitchScale;

	private double minScale; // stores the minimum zoom level

	private int currResolutionLevel;

	public static enum Direction
	{
		/**
		 * Represents a direction where we have increased to a finer resolution.
		 */
		INCREASE,

		/**
		 * Represents a direction where we have decreased to a lower resolution.
		 */
		DECREASE,

		NONE;
	}

	private Direction direction;

	/**
	 * Creates a new Resolution Switcher object with the default value of 2 for
	 * scale switch.
	 * 
	 * @param initialScale
	 *            The initial scale of the map.
	 */
	public ResolutionManager(double initialScale, double minZoom)
	{
		this(initialScale, 2, minZoom);
	}

	/**
	 * Creates a new Resolution switcher object which is used to control when to
	 * switch resolutions.
	 * 
	 * @param initialScale
	 *            The initial scale of the map.
	 * @param switchScale
	 *            The scale switch value to use.
	 */
	public ResolutionManager(double initialScale, double switchScale, double minScale)
	{
		if (minScale > initialScale)
		{
			throw new IllegalArgumentException("minScale is greater than initialScale");
		}
		else if (minScale <= 0)
		{
			throw new IllegalArgumentException("minScale must be positive");
		}
		else if (switchScale <= 1.0)
		{
			throw new IllegalArgumentException("switchScale must be greater than 1");
		}
		else if (initialScale <= 0)
		{
			throw new IllegalArgumentException("initialScale must be positive");
		}

		int l = (int) Math.floor((Math.log(minScale / initialScale) / Math.log(switchScale)));
		this.startingScale = Math.pow(switchScale, l) * initialScale;

		this.switchScale = switchScale;

		direction = Direction.NONE;

		this.minScale = minScale;
		logSwitchScale = Math.log(switchScale);

		currResolutionLevel = getResolutionLevel(initialScale);
	}

	/**
	 * The Scale switch value defines the value which is multiplied by the
	 * previous zoom level we switched at to get the next zoom level to switch.
	 * So, if we previously switched resolutions at zoom=10, and if scale switch
	 * = 2, then the next zoom level we will switch resolutions is at zoom = 20.
	 * 
	 * @return Returns the scale switch value.
	 */
	public double getSwitchScale()
	{
		return switchScale;
	}

	/**
	 * Determines the particular resolution level that the passed scale would
	 * lie within.
	 * 
	 * @param scale
	 * @return The resolution scale (an int >= 0).
	 */
	public int getResolutionLevel(double scale)
	{
		if (scale <= 0)
		{
			throw new IllegalArgumentException("scale must be non-negative");
		}
		else if (scale < minScale)
		{
			return 0;
		}
		else
		{
			return (int) Math.floor(Math.log(scale / startingScale) / logSwitchScale + 1);
		}
	}

	public int getCurrentResolutionLevel()
	{
		return currResolutionLevel;
	}

	private double getScaleFor(int level)
	{
		double scale = Math.pow(switchScale, level - 1) * startingScale;

		if (scale < minScale)
		{
			scale = minScale;
		}

		return scale;
	}

	public double getBaseResolutionScale()
	{
		return getScaleFor(currResolutionLevel);
	}

	/**
	 * Performs the resolution switch, given the current scale.
	 * 
	 * @param scale
	 *            The current scale of the map.
	 */
	public void performSwitch(double scale)
	{
		int level = getResolutionLevel(scale);

		if (level >= 0)
		{
			if (level < currResolutionLevel)
			{
				direction = Direction.DECREASE;
			}
			else if (level > currResolutionLevel)
			{
				direction = Direction.INCREASE;
			}
			else
			{
				direction = Direction.NONE;
			}

			currResolutionLevel = level;
		}
	}

	/**
	 * Determines if the passed scale would cause a switch to a new resolution.
	 * 
	 * @param scale
	 * @return True if it would cause a switch to a new resolution, false
	 *         otherwise.
	 */
	public boolean isNewResolutionLevel(double scale)
	{
		int level = getResolutionLevel(scale);

		return level != currResolutionLevel;
	}

	/**
	 * Gets the maximum number of resolution levels.
	 * 
	 * @param maxScale
	 *            The maximum scale for the map.
	 * @return The maximum number of resolution levels for the passed scale.
	 */
	public int getMaxResolutionLevels(double maxScale)
	{
		return getResolutionLevel(maxScale);
	}

	/**
	 * Determines if the passed zoom value is at the minimum resolution level.
	 * 
	 * @param zoom
	 *            The zoom value to check.
	 * @return True if the passed zoom value is at the minimum resolution level,
	 *         false otherwise.
	 */
	public boolean isMinResolutionLevel(double zoom)
	{
		return (getResolutionLevel(zoom) == 0);
	}

	public ResolutionSwitchEvent createResolutionSwitchEvent(Backbone backbone)
	{
		return new ResolutionSwitchEvent(backbone, this.direction, this.currResolutionLevel,
				this.getBaseResolutionScale());
	}
}
