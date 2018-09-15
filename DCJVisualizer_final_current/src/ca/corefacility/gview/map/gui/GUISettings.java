package ca.corefacility.gview.map.gui;

import edu.umd.cs.piccolo.util.PPaintContext;

public class GUISettings
{
	// Graphical:
	private static RenderingQuality renderingQuality = RenderingQuality.HIGH;
	private static int displayQuality = PPaintContext.HIGH_QUALITY_RENDERING;

	// Other:
	private static SlotNodeDetail slotNodeDetail = SlotNodeDetail.LINK;

	public enum RenderingQuality
	{
		LOW, HIGH;
	}

	public enum SlotNodeDetail
	{
		FULL, MINIMAL, LINK, NONE;
	}

	/**
	 * The quality of the PPaintContext.
	 * 
	 * @return The PPaintContext quality: either
	 *         PPaintContext.LOW_QUALITY_RENDERING or
	 *         PPaintContext.HIGH_QUALITY_RENDERING.
	 */
	public static int getDisplayQuality()
	{
		return GUISettings.displayQuality;
	}

	/**
	 * 
	 * @param quality
	 *            A PPaintContext quality: either
	 *            PPaintContext.LOW_QUALITY_RENDERING or
	 *            PPaintContext.HIGH_QUALITY_RENDERING.
	 */
	private static void setDisplayQuality(int quality)
	{
		if (quality != PPaintContext.LOW_QUALITY_RENDERING && quality != PPaintContext.HIGH_QUALITY_RENDERING)
		{
			throw new IllegalArgumentException("Invalid PPaintContext");
		}

		GUISettings.displayQuality = quality;
	}

	/**
	 * Sets the rendering quality.
	 * 
	 * Thread safe.
	 * 
	 * @param quality
	 *            The rendering quality.
	 */
	public static synchronized void setRenderingQuality(RenderingQuality quality)
	{
		if (quality == null)
		{
			throw new NullPointerException("Rending quality cannot be null.");
		}

		// A different rendering quality:
		if (quality != GUISettings.renderingQuality)
		{
			GUISettings.renderingQuality = quality;

			// High:
			if (quality == RenderingQuality.HIGH)
			{
				GUISettings.setDisplayQuality(PPaintContext.HIGH_QUALITY_RENDERING);
			}
			// Low:
			else if (quality == RenderingQuality.LOW)
			{
				GUISettings.setDisplayQuality(PPaintContext.LOW_QUALITY_RENDERING);
			}
		}

		GUIManager.getInstance().refreshDisplay();
	}

	/**
	 * 
	 * @return The rendering quality.
	 */
	public static RenderingQuality getRenderingQuality()
	{
		return GUISettings.renderingQuality;
	}

	/**
	 * Sets the amount of slot node detail.
	 * 
	 * @param detail
	 */
	public static void setSlotNodeDetail(SlotNodeDetail detail)
	{
		if (detail == null)
		{
			throw new NullPointerException("Node detail cannot be null.");
		}

		GUISettings.slotNodeDetail = detail;

		GUIManager.getInstance().updateStyles();
	}

	/**
	 * 
	 * @return The amount of slot node detail.
	 */
	public static SlotNodeDetail getSlotNodeDetail()
	{
		return GUISettings.slotNodeDetail;
	}
}
