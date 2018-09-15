package ca.corefacility.gview.map;


import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Collection;

import org.biojava.bio.seq.Feature;

import ca.corefacility.gview.data.GenomeData;
import ca.corefacility.gview.layout.prototype.SequencePoint;
import ca.corefacility.gview.layout.sequence.LayoutFactory;
import ca.corefacility.gview.layout.sequence.ZoomException;
import ca.corefacility.gview.managers.DisplayManager;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.event.GViewEventSubject;
import ca.corefacility.gview.map.gui.Style;
import ca.corefacility.gview.map.items.FeatureItem;
import ca.corefacility.gview.style.MapStyle;

/**
 * gview.main viewing area, handles display of items
 */
public interface GViewMap extends GViewEventSubject, GViewEventListener
{
	// public void setPositioner(SlotRegion positioner);
	public void setMapStyle(MapStyle style);

	public MapStyle getMapStyle();
        
        public DisplayManager getDisplayManager();

	public double getZoomFactorForDistance( double deltaBase, double length );

	public double getBackboneLength();

	public SequencePoint translatePoint(Point2D point);


	/**
	 * Used to display/hide this map.
	 * 
	 * @param visible
	 *            True if it is visible, false if hidden.
	 */
	public void setVisible(boolean visible);

	/**
	 * Sets the center point of this map.
	 * 
	 * @param center
	 *            The point that this map will be centered on.
	 */
	public void setCenter(Point2D center);

	// TODO should I allow translate here? Only used so we can properly translate for zooming now.
	public void translate(Point2D delta); // translates map view by the passed amount

	/**
	 * Given a base on the current map, determines the current coordinates.
	 * @param base  A base on the current map.
	 * @return  The coordinates on the map.
	 */
	public Point2D getBaseCoordinate(int base);

	/**
	 * Sets the centre base of this map.
	 * 
	 * @param base
	 *            The base that this map will be centred on.
	 */
	public void setCenter(int base);

	/**
	 * Sets the zoom factor for this map.
	 * 
	 * @param zoomFactor
	 *            A number from getMinZoomFactor() to getMaxZoomFactor().
	 * @throws ZoomException 
	 */
	public void setZoomFactor(double zoomFactor) throws ZoomException;
	
	/**
	 * @return  The minimum zoom factor.
	 */
    public double getMinZoomFactor();
    
    /**
     * @return  The maximum zoom factor.
     */
    public double getMaxZoomFactor();
	
	/**
	 * @return  The current zoom factor for this map.
	 */
	public double getZoomFactor();

	/**
	 * Sets the current view size to the passed width/height.
	 * @param width  The width (in pixels) to set the view size to.
	 * @param height  The height (in pixels) to set the view size to.
	 */
	public void setViewSize(int width, int height);

	/**
	 * Gets the width of the current view
	 * @return The width (in pixels) of the current view size
	 */
	public double getViewWidth();

	/**
	 * Gets the height of the current view
	 * @return The height ( in pixels ) of the current view size
	 */
	public double getViewHeight();


	public double getSpannedBases( int length );

	/**
	 * Centres the GView map to the very middle of the sequence.
	 */
	public void centerMap();

	/**
	 * Gets the current view as a BufferedImage (of type INT_ARGB).
	 * @return A representation of the current view of this map as a BufferedImage.
	 */
	public BufferedImage getImage();
	
	/**
	 * Gets the current view as a BufferedImage of the specific type
	 * @param imageType  The image type to use.
	 * @return The image as a BufferedImage of the passed type.
	 */
	public BufferedImage getImage(int imageType);
	
	/**
	 * Gets the current Legend of this map as a BufferedImage (of type INT_ARGB)
	 * @return  A representation of the Legend of this map as a BufferedImage
	 */
	public BufferedImage getLegendImage();
	
	/**
	 * Gets the current Legend of this map as a BufferedImage of the specific type.
	 * @param imageType  The image type to use
	 * @return  The legend image as a BufferedImage of the passed type.
	 */
	public BufferedImage getLegendImage(int imageType);

	/**
	 * Draws out this map onto the passed graphics context.
	 * 
	 * @param g
	 *            The graphics context to draw onto.
	 */
	public void draw(Graphics2D g);
	

	/**
	 * Draws out the legend onto the passed graphics context.
	 * @param g  The graphics context to draw onto.
	 */
	public void drawLegend(Graphics2D g);

	/**
	 * Sets the layout of this map using the passed factory.
	 * 
	 * @param layoutFactory
	 *            The factory to use create an object to layout the map.
	 */
	public void setLayout(LayoutFactory layoutFactory);

	/**
	 * Updates the passed collection of features to be selected.
	 * 
	 * @param selectedFeatures
	 *            The features that are selected.
	 */
	public void updateSelected(Collection<Feature> selectedFeatures);

	/**
	 * Searches for and returns the FeatureItem on the map representing the passed feature.
	 * (should probably return a list here of all FeatureItem's mapped to the passed feature).
	 * 
	 * @param feature  The feature to search for.
	 * 
	 * @return FeatureItem  The FeatureItem mapped to the passed feature.
	 */
	public FeatureItem getFeatureItem(Feature feature);

	/**
	 * Sets the data to be rendered by this map (causes a re-render). Note: not working properly,
	 * probably just easier to do GViewMapFactory.createMap if needed.
	 * 
	 * @param genomeData
	 *            The data to be rendered.
	 */
	public void setData(GenomeData genomeData);

	/**
	 * Sets the view to view all information on the screen.
	 */
	public void scaleMapToScreen();
	
	/**
	 * Changes the screen size to fit the full image.
	 */
	public void scaleScreenToMap();
	
	/**
	 * Attempts to center the full map within a frame.
	 *   Used for publishing/generating full quality images.
	 */
	public void viewFullMap();
	
	/**
	 * @return  The maximum sequence length (in base values) of this map.
	 */
	public int getMaxSequenceLength();
	
	/**
	 * @return  The closest base value this view is centred on.
	 */
	public double getCenterBaseValue();


	/**
	 * Performs a "normal" zoom on the map, ie enlarges everything by a constant factor.
	 * @param scale  The scale to zoom in by.
	 */
	public void zoomNormal(double scale);
	
	/**
	 * @return  The factor we are scaling the map by.
	 */
	public double getZoomNormalFactor();
	
	/**
	 * Provides access to an ElementControl object to control display of elements on the map.
	 * @return  The ElementControl object.
	 */
	public ElementControl getElementControl();
	
	/**
	 * Provides access to the GenomeData this map is viewing.
	 * @return  The GenomeData this map is viewing.
	 */
	public GenomeData getGenomeData();
	
	/**
	 * Provides access to the layout for this map.
	 * @return  The LayoutFactory for this map.
	 */
	public LayoutFactory getLayoutFactory();
	
	public void updateStyle(Style style);
}
