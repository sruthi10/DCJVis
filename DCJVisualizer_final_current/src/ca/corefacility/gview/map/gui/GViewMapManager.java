package ca.corefacility.gview.map.gui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;

import ca.corefacility.gview.layout.sequence.LayoutFactory;
import ca.corefacility.gview.layout.sequence.ZoomException;
import ca.corefacility.gview.layout.sequence.circular.LayoutFactoryCircular;
import ca.corefacility.gview.layout.sequence.linear.LayoutFactoryLinear;
import ca.corefacility.gview.map.BirdsEyeViewImp;
import ca.corefacility.gview.map.ElementControl;
import ca.corefacility.gview.map.GViewMap;
import ca.corefacility.gview.map.GViewMapImp;
import ca.corefacility.gview.map.controllers.LabelStyleController;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;
import ca.corefacility.gview.map.inputHandler.ImageExportHandler;
import ca.corefacility.gview.utils.thread.ThreadService;
import edu.umd.cs.piccolo.event.PInputEventListener;

public class GViewMapManager
{
	private final Style style;
	private final GViewMap gViewMap;

	public enum Layout
	{
		LINEAR, CIRCULAR, UNKNOWN;
	}

	public GViewMapManager(Style style, GViewMap gViewMap)
	{
		this.style = style;
		this.gViewMap = gViewMap;
	}

	/**
	 * Sets the size of the GViewMap.
	 * 
	 * @param width
	 * @param height
	 */
	public void setMapSize(int width, int height)
	{
		// Catch very, very small windows.
		if (width < 100)
			width = 100;

		if (height < 100)
			height = 100;

		this.gViewMap.setViewSize(width, height);
	}

	/**
	 * Sets the visibility of the map.
	 * 
	 * @param visible
	 */
	public void setMapVisible(boolean visible)
	{
		this.gViewMap.setVisible(visible);
	}

	/**
	 * Attempts to scale the map to fit the screen.
	 */
	public void scaleMapToScreen()
	{
		this.gViewMap.scaleMapToScreen();
	}

	/**
	 * Adds the GViewMap to the passed panel.
	 * 
	 * @param panel
	 */
	public void addMapToPanel(JPanel panel)
	{
		panel.add((Component) this.gViewMap, BorderLayout.CENTER);
	}

	/**
	 * Updates the map.
	 */
	public void updateMap()
	{
		this.gViewMap.updateStyle(style);
	}

	/**
	 * 
	 * @return The zoom normal factor.
	 */
	public double getZoomNormalFactor()
	{
		return this.gViewMap.getZoomNormalFactor();
	}

	/**
	 * 
	 * @return The zoom factor.
	 */
	public double getZoomFactor()
	{
		return this.gViewMap.getZoomFactor();
	}

	/**
	 * Connects the Bird's Eye View object to the GViewMap object.
	 * 
	 * @param BEV
	 */
	public void connectGViewMap(BirdsEyeViewImp BEV)
	{
		BEV.connect(this.gViewMap);
	}

	/**
	 * Adds a GViewEventListener to the GViewMap.
	 * 
	 * @param listener
	 */
	public void addEventListener(GViewEventListener listener)
	{
		this.gViewMap.addEventListener(listener);
	}

	/**
	 * Adds an InputEventListener to the GViewMap, specifically to the map's
	 * camera object.
	 * 
	 * @param listener
	 */
	public void addInputEventListener(PInputEventListener listener)
	{
		if (this.gViewMap instanceof GViewMapImp)
		{
			((GViewMapImp) this.gViewMap).getCamera().addInputEventListener(listener);
		}
		else
		{
			System.err.println("GViewMap class type miss match.");
		}
	}

	/**
	 * Removes the passed event listener from the GViewMap object.
	 * 
	 * @param listener
	 */
	public void removeEventListener(GViewEventListener listener)
	{
		this.gViewMap.removeEventListener(listener);
	}

	/**
	 * Performs a zoom normal operation on the GViewMap.
	 * 
	 * @param scale
	 */
	public void zoomNormal(double scale)
	{
		this.gViewMap.zoomNormal(scale);
	}

	/**
	 * 
	 * @return The max sequence length.
	 */
	public int getMaxSequenceLength()
	{
		return this.gViewMap.getMaxSequenceLength();
	}

	/**
	 * 
	 * @return The center base value.
	 */
	public double getCenterBaseValue()
	{
		return this.gViewMap.getCenterBaseValue();
	}

	/**
	 * Sets the location the camera will center on.
	 * 
	 * @param moveLocation
	 */
	public void setCenter(int moveLocation)
	{
		this.gViewMap.setCenter(moveLocation);
	}

	/**
	 * Sets the zoom factor.
	 * 
	 * @param zoomFactor
	 * @throws ZoomException
	 */
	public void setZoomFactor(double zoomFactor) throws ZoomException
	{
		if (zoomFactor > this.gViewMap.getMaxZoomFactor())
		{
			this.gViewMap.setZoomFactor(this.gViewMap.getMaxZoomFactor());
		}
		else if (zoomFactor < this.gViewMap.getMinZoomFactor())
		{
			this.gViewMap.setZoomFactor(this.gViewMap.getMinZoomFactor());
		}
		else
		{
			this.gViewMap.setZoomFactor(zoomFactor);
		}
	}

	/**
	 * Moves the camera so that it centers over the start of the sequence.
	 */
	public void moveStart()
	{
		this.gViewMap.setCenter(0);
	}

	/**
	 * Moves the camera so that it centers over the middle of the sequence.
	 */
	public void moveMiddle()
	{
		this.gViewMap.setCenter((int) Math.round(this.gViewMap.getMaxSequenceLength() / 2.0));
	}

	/**
	 * Moves the camera so that it centers over the end of the sequence.
	 */
	public void moveEnd()
	{
		this.gViewMap.setCenter(this.gViewMap.getMaxSequenceLength());
	}

	/**
	 * Moves the camera so that it centers over the base located at the first
	 * quarter position of the sequence.
	 */
	public void moveFirstQuarter()
	{
		this.gViewMap.setCenter((int) Math.round(this.gViewMap.getMaxSequenceLength() / 4.0));
		;
	}

	/**
	 * Moves the camera so that it centers over the base located at the third
	 * quarter position of the sequence.
	 */
	public void moveThirdQuarter()
	{
		this.gViewMap.setCenter((int) Math.round(3.0 * this.gViewMap.getMaxSequenceLength() / 4.0));
	}

	/**
	 * Attempts to export the image to file.
	 * 
	 * May require further actions provided by a user through a GUI interface.
	 */
	public void exportImage()
	{
		ImageExportHandler.getInstance().performExport(this.gViewMap);
		;
	}

	/**
	 * 
	 * @return The element control object, which exposes access to the control
	 *         of certain display elements, such as the legend.
	 */
	public ElementControl getElementControl()
	{
		return this.gViewMap.getElementControl();
	}

	/**
	 * 
	 * @return The minimum zoom factor.
	 */
	public double getMinZoomFactor()
	{
		return this.gViewMap.getMinZoomFactor();
	}

	/**
	 * 
	 * return The maximum zoom factor.
	 */
	public double getMaxZoomFactor()
	{
		return this.gViewMap.getMaxZoomFactor();
	}

	/**
	 * 
	 * @return The layout of the GViewMap.
	 */
	public Layout getLayout()
	{
		Layout layout = Layout.UNKNOWN;
		LayoutFactory factory = this.gViewMap.getLayoutFactory();

		if (factory instanceof LayoutFactoryLinear)
		{
			layout = Layout.LINEAR;
		}
		else if (factory instanceof LayoutFactoryCircular)
		{
			layout = Layout.CIRCULAR;
		}

		return layout;
	}

	/**
	 * This is used to change the Style layout.
	 * 
	 * @param style
	 *            The style which will have its layout changed.
	 * @param layout
	 *            The new layout to use.
	 * 
	 * @return A new, shallow copy of the current Style with the specified
	 *         layout. Specifically, the internal GenomeData and MapStyle
	 *         objects will be shallow.
	 */
	public Style changeGViewMapLayout(Style style, Layout layout)
	{
		LayoutFactory layoutFactory = StyleEditorUtility.DEFAULT_LAYOUT;

		switch (layout)
		{
			case LINEAR:
				layoutFactory = new LayoutFactoryLinear();
				break;
			case CIRCULAR:
				layoutFactory = new LayoutFactoryCircular();
				break;
			default:
				layoutFactory = StyleEditorUtility.DEFAULT_LAYOUT;
				break;
		}

		GViewMapImp gViewMap = new GViewMapImp(this.gViewMap.getGenomeData(), this.gViewMap.getMapStyle(),
				layoutFactory);
		Style result = new Style(gViewMap, style.getName());

		return result;
	}

	/**
	 * Refreshes the GViewMap.
	 */
	public void refresh()
	{
		if (this.gViewMap instanceof GViewMapImp)
		{
			ThreadService.executeOnEDT(new Runnable()
			{
				@Override
				public void run()
				{
					// We don't want to run both, because we'd do double the
					// work.
					// Changing render quality:
					if (GUISettings.getDisplayQuality() != ((GViewMapImp) gViewMap).getNormalRenderQuality())
					{
						// Will repaint on its own.
						((GViewMapImp) gViewMap).setDefaultRenderQuality(GUISettings.getDisplayQuality());
					}
					// No changing render quality:
					else
					{
						((GViewMapImp) gViewMap).invalidate();
						((GViewMapImp) gViewMap).repaint();
					}
				}
			});
		}
	}

	/**
	 * Initalizes the set-label locks.
	 * 
	 * @param labelStyleController
	 */
	public void initializeLocks(LabelStyleController labelStyleController)
	{
		labelStyleController.initializeLabelLocks(this.gViewMap.getMapStyle().getDataStyle().slots());
	}
}
