package ca.corefacility.gview.map;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.util.Collection;
import java.util.List;

import javax.swing.event.EventListenerList;

import org.biojava.bio.seq.Feature;

import ca.corefacility.gview.data.GenomeData;
import ca.corefacility.gview.layout.prototype.SequencePoint;
import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.layout.sequence.LayoutFactory;
import ca.corefacility.gview.layout.sequence.SlotRegion;
import ca.corefacility.gview.layout.sequence.ZoomException;
import ca.corefacility.gview.managers.DisplayManager;
import ca.corefacility.gview.map.controllers.StyleController;
import ca.corefacility.gview.map.event.DisplayChangeListener;
import ca.corefacility.gview.map.event.DisplayUpdated;
import ca.corefacility.gview.map.event.GViewEvent;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.event.GViewEventSubjectImp;
import ca.corefacility.gview.map.event.ZoomEvent;
import ca.corefacility.gview.map.event.ZoomNormalEvent;
import ca.corefacility.gview.map.gui.Style;
import ca.corefacility.gview.map.inputHandler.InputEventHandler;
import ca.corefacility.gview.map.inputHandler.SelectHandler;
import ca.corefacility.gview.map.inputHandler.ToolTipHandler;
import ca.corefacility.gview.map.inputHandler.ZoomSubject;
import ca.corefacility.gview.map.items.FeatureItem;
import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.utils.ProgressHandler;
import ca.corefacility.gview.utils.thread.ThreadService;
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEventListener;
import edu.umd.cs.piccolo.event.PPanEventHandler;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolo.util.PPaintContext;

public class GViewMapImp extends PCanvas implements GViewMap
{
	private static final long serialVersionUID = 2656476813849478190L;

	private DisplayManager displayManager;

	private PLayer mainLayer; // probably want to make this a MapLayer later

	// handles events assigned from outside
	private GViewEventSubjectImp eventDispatcher;

	private SelectHandler selectHandler; // handles selection of items

	private DisplayChangeListener displayChangeListener;

	private ZoomSubject zoomSubject;
	private ToolTipHandler toolTipHandler;

	private ElementControl elementControl;

	private GenomeData genomeData;
	private LayoutFactory layoutFactory;

	/**
	 * Creates a new map from the passed data.
	 * 
	 * @param genomeData
	 *            The data defining the genome.
	 * @param mapStyle
	 *            The style defining how the data should be displayed.
	 * @param layoutFactory
	 *            Used to define the default layout of the map.
	 */
	public GViewMapImp(GenomeData genomeData, MapStyle mapStyle, LayoutFactory layoutFactory)
	{
		// canvas = new PCanvas();

		this.mainLayer = getLayer();

		this.eventDispatcher = new GViewEventSubjectImp();

		this.zoomSubject = new ZoomSubject(this); // create here so that it's
													// not null when we create
		this.zoomSubject.addEventListener(this); // listen to zoom events so we
													// can distribute them out
		// layout manager

		// TODO where should we set this?
		this.displayChangeListener = new DisplayChangeListener(getCamera());

		setProperties(mapStyle);
		createEventHandlers(mapStyle); // create here (before display manager)
										// so toolTipHandler and panEventHandler
										// have
		// been created

		this.displayManager = new DisplayManager(getCamera(), genomeData, mapStyle, layoutFactory, this.zoomSubject,
				this.displayChangeListener, this.toolTipHandler);
		// need to set here, since legendHandler is created beforehand (so it
		// can be inserted into inputHandler),
		// but, displayManager is not created during this time

		getCamera().addPropertyChangeListener(PCamera.PROPERTY_VIEW_TRANSFORM, this.displayChangeListener);
		getCamera().addPropertyChangeListener(PCamera.PROPERTY_BOUNDS, this.displayChangeListener);
		getCamera().addPropertyChangeListener(PCamera.PROPERTY_TRANSFORM, this.displayChangeListener);

		buildMap(this.displayManager);

		fitViewToCanvas();

		this.elementControl = new ElementControl(this.displayManager);
		this.elementControl.setLegendDisplayed(false);

		this.genomeData = genomeData;
		this.layoutFactory = layoutFactory;
	}

	public void setLayout(LayoutFactory layoutFactory)
	{
		this.mainLayer.removeAllChildren();

		// TODO Auto-generated method stub
		this.displayManager.changeLayout(layoutFactory, this.zoomSubject);

		buildMap(this.displayManager); // TODO I don't want to rebuild the map
										// for finished program
	}

	public MapStyle getMapStyle()
	{
		return this.displayManager.getMapStyle();
	}

	// one problem here, when map initially created camera's bounds are empty
	public void setCenter(Point2D center)
	{
		if (center != null)
		{
			PCamera camera = getCamera();

			setProperBounds();

			PBounds bounds = camera.getViewBounds();

			Point2D delta = new Point2D.Double(bounds.getCenterX() - center.getX(), bounds.getCenterY() - center.getY());

			translate(delta);
			// animateToOffset(center);
		}
	}

	public void translate(Point2D delta)
	{
		if (delta != null)
		{
			getCamera().translateView(delta.getX(), delta.getY());
		}
	}

	public double getZoomFactorForDistance(double deltaBase, double length)
	{
		// Need Backbone to handle this too.
		return this.displayManager.getBackbone().findZoomForLength(deltaBase, length);
	}

	public double getBackboneLength()
	{
		return this.displayManager.getBackbone().getBackboneLength();
	}

	// need Backbone to handle this
	public void setCenter(int base)
	{
		Point2D center = this.displayManager.getBackbone().translate(base, 0);
		setCenter(center);
	}

	public void setMapStyle(final MapStyle mapStyle)
	{
		if (mapStyle == null)
			throw new NullPointerException("MapStyle is Null");

		// The heavy work:
		this.displayManager.setMapStyle(mapStyle);

		// Execution:
		ThreadService.executeOnEDT(new Runnable()
		{
			@Override
			public void run()
			{
				setProperties(mapStyle);
				buildMap(displayManager);

				displayManager.eventOccured(new DisplayUpdated(this, getCamera().getViewBounds(), getCamera()
						.getBounds(), true));
			}
		});
	}

	public void setZoomFactor(double zoomFactor) throws ZoomException
	{
		if (zoomFactor > displayManager.getBackbone().getMaxScale())
		{
			throw new ZoomException("zoom factor(" + zoomFactor + ") > maximum zoom factor ("
					+ displayManager.getBackbone().getMaxScale() + ")");
		}

		if (zoomFactor < displayManager.getBackbone().getMinScale())
		{
			throw new ZoomException("zoom factor(" + zoomFactor + ") < minimum zoom factor ("
					+ displayManager.getBackbone().getMinScale() + ")");
		}

		PCamera camera = getCamera();
		PBounds centerBounds = camera.getViewBounds();

		setProperBounds();

		SequencePoint centerSeqPoint = this.displayManager.getBackbone().translate(centerBounds.getCenter2D());

		this.zoomSubject.zoomToFactor(zoomFactor, centerSeqPoint);

		this.paintImmediately();
	}

	public double getZoomFactor()
	{
		return this.zoomSubject.getStretchZoomFactor();
	}

	public BufferedImage getLegendImage()
	{
		return getLegendImage(BufferedImage.TYPE_INT_ARGB);
	}

	public BufferedImage getLegendImage(int imageType)
	{
		PCamera camera = new PCamera();

		PLayer legendLayer = (PLayer) displayManager.getLegendLayer();
		camera.addChild(legendLayer);
		camera.setPaint(null);
		camera.invalidateFullBounds();

		camera.setViewBounds(camera.getUnionOfLayerFullBounds());
		camera.setViewBounds(camera.getUnionOfLayerFullBounds());

		Rectangle2D bounds = camera.getFullBounds();

		int width = (int) Math.ceil(bounds.getWidth());
		int height = (int) Math.ceil(bounds.getHeight());

		BufferedImage image = new BufferedImage(width, height, imageType);

		// if no alpha channel, set background to gview map background
		if (image.getAlphaRaster() == null)
		{
			camera.toImage(image, displayManager.getMapStyle().getGlobalStyle().getBackgroundPaint());
		}
		else
		{
			camera.toImage(image, null);
		}

		return image;
	}

	public BufferedImage getImage()
	{
		return getImage(BufferedImage.TYPE_INT_ARGB);
	}

	// not entirely sure how to do image writing yet
	public BufferedImage getImage(int imageType)
	{
		// GlobalStyle gStyle = getMapStyle().getGlobalStyle();
		PCamera camera = getCamera();

		setProperBounds();

		Rectangle2D bounds = camera.getBounds();
		// Rectangle2D bounds = camera.getViewBounds();

		int width = (int) Math.ceil(bounds.getWidth());
		int height = (int) Math.ceil(bounds.getHeight());

		BufferedImage image = new BufferedImage(width, height, imageType);

		camera.toImage(image, displayManager.getMapStyle().getGlobalStyle().getBackgroundPaint());

		return image;
		// TODO do I want to write the camera to image, or the full, centered
		// map
	}

	public void drawLegend(Graphics2D g)
	{
		PCamera camera = new PCamera();

		PLayer legendLayer = (PLayer) displayManager.getLegendLayer();
		camera.addChild(legendLayer);
		camera.setPaint(null);
		camera.invalidateFullBounds();

		camera.setViewBounds(camera.getUnionOfLayerFullBounds());
		camera.setViewBounds(camera.getUnionOfLayerFullBounds());

		Rectangle2D bounds = camera.getFullBounds();

		PPaintContext paintContext = new PPaintContext(g);
		paintContext.setRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);

		int width = (int) Math.ceil(bounds.getWidth());
		int height = (int) Math.ceil(bounds.getHeight());

		Paper paper = new Paper();
		paper.setSize(width, height);
		paper.setImageableArea(0, 0, width, height);
		PageFormat pageFormat = new PageFormat();
		pageFormat.setPaper(paper);
		camera.print(g, pageFormat, 0);
	}

	public void draw(Graphics2D g)
	{
		PBounds bounds = getCamera().getFullBounds();
		bounds.expandNearestIntegerDimensions();

		int width = (int) bounds.getWidth();
		int height = (int) bounds.getHeight();

		Paper paper = new Paper();
		paper.setSize(width, height);
		paper.setImageableArea(bounds.getX(), bounds.getY(), width, height);
		PageFormat pageFormat = new PageFormat();
		pageFormat.setPaper(paper);
		getCamera().print(g, pageFormat, 0);
	}

	/**
	 * Attempts to fit the current camera view to the size of the canvas.
	 */
	private void fitViewToCanvas()
	{
		int width = getMapStyle().getGlobalStyle().getDefaultWidth();
		int height = getMapStyle().getGlobalStyle().getDefaultHeight();

		setBounds(0, 0, width, height);
		getCamera().setViewOffset(width / 2.0, height / 2.0);
	}

	/**
	 * Used to check if camera bounds are empty and set to proper bounds if it
	 * is
	 */
	private void setProperBounds()
	{
		if (getCamera().getBounds().isEmpty()) // if bounds initially empty
		{
			fitViewToCanvas();
		}
	}

	private void buildMap(DisplayManager displayManager)
	{
		// Updating display:
		ProgressHandler.setMessage("Updating Display\n");

		PCamera camera = getCamera();

		mainLayer.removeAllChildren();
		camera.removeAllChildren();

		// defines order to draw out items
		// need labels before ruler so that transparency backgrounds in tick
		// marks would work
		mainLayer.addChild((PNode) displayManager.getFeaturesLayer());
		mainLayer.addChild((PNode) displayManager.getPlotsLayer());
		mainLayer.addChild((PNode) displayManager.getLabelsLayer());
		mainLayer.addChild((PNode) displayManager.getRulerLayer());
		camera.addChild((PNode) displayManager.getToolTipItem());

		// handle legend layer
		// add as child of camera so it cannot be moved
		if (displayManager.getLegendDisplayed())
		{
			camera.addChild((PNode) displayManager.getLegendLayer());
		}
	}

	private void createEventHandlers(MapStyle mapStyle)
	{
		// TODO how should I created/add event handlers?

		// GlobalStyle globalStyle = null;
		PCamera camera = getCamera();

		// remove all event listeners
		EventListenerList allListeners = camera.getListenerList();
		Object[] listenersArray = allListeners.getListenerList();
		for (int i = 0; i < listenersArray.length; i++)
		{
			if (listenersArray[i] instanceof PInputEventListener)
			{
				camera.removeInputEventListener((PInputEventListener) listenersArray[i]);
			}
		}

		this.selectHandler = new SelectHandler(this.eventDispatcher);
		this.toolTipHandler = new ToolTipHandler(mapStyle.getGlobalStyle().getTooltipStyle());

		camera.addInputEventListener(new PPanEventHandler());
		camera.addInputEventListener(new InputEventHandler(camera, this, this.zoomSubject, this.selectHandler,
				this.toolTipHandler)); // possibly
	}

	/**
	 * Sets some rendering properties of this map.
	 */
	private void setProperties(MapStyle mapStyle) // we need this to listen for
													// StyleEvents, and set
	// background
	{
		// double height = mapStyle.getGlobalStyle().getHeight();
		// double width = mapStyle.getGlobalStyle().getWidth();

		// mainLayer.setChildrenPickable(false);

		// takes care of flashing on map
		setOpaque(true);
		setDoubleBuffered(true);

		// sets background paint
		getCamera().setPaint(mapStyle.getGlobalStyle().getBackgroundPaint());
	}

	public void setData(GenomeData genomeData)
	{
		if (genomeData == null)
			throw new NullPointerException("genomeData is null");
		else
		{
			this.displayManager.setData(genomeData);
			buildMap(this.displayManager);
		}
	}

	public void updateSelected(Collection<Feature> selectedFeatures)
	{
		this.selectHandler.selectOnlyFeatures(this.displayManager, selectedFeatures, false);
	}

	public void addEventListener(GViewEventListener listener)
	{
		this.eventDispatcher.addEventListener(listener);
	}

	public void removeEventListener(GViewEventListener listener)
	{
		this.eventDispatcher.removeEventListener(listener);
	}

	public void removeAllEventListeners()
	{
		this.eventDispatcher.removeAllEventListeners();
	}

	@Override
	public void eventOccured(GViewEvent event)
	{
		if (event instanceof ZoomNormalEvent || event instanceof ZoomEvent) // fire
																			// zoom
																			// &
																			// zoom
																			// normal
																			// events
																			// outwards
		{
			this.eventDispatcher.fireEvent(event);
		}
		else
		{
			this.displayManager.eventOccured(event);
		}
	}

	@Override
	public FeatureItem getFeatureItem(Feature feature)
	{
		List<FeatureItem> items = this.displayManager.findFeatureItems(feature);
		if (items != null && items.size() != 0)
		{
			if (items.size() > 1)
				throw new RuntimeException("More than one feature item for this feature!");

			return items.get(0);
		}

		return null;
	}

	@Override
	public Point2D getBaseCoordinate(int base)
	{
		Backbone backbone = this.displayManager.getBackbone();

		return backbone.translate(base, 0);
	}

	private void viewAllInCamera(PCamera camera)
	{
		// need to run this twice
		// why?
		// This zooms such that the full screen is in view
		// But, zooming causes changes in the current displayed items (tick
		// marks, tick mark labels, etc)
		// So, we run this twice so that the second time, it shouldn't shift by
		// much (and so nothing is cut out of the display).
		camera.setViewBounds(camera.getUnionOfLayerFullBounds());
		camera.setViewBounds(camera.getUnionOfLayerFullBounds());

		this.eventDispatcher.fireEvent(new ZoomNormalEvent(this, camera.getViewScale()));
	}

	@Override
	public void scaleMapToScreen()
	{
		viewAllInCamera(getCamera());
	}

	@Override
	public void scaleScreenToMap()
	{
		PCamera camera = getCamera();
		final int widthPadding = 20;
		final int heightPadding = 20;

		PBounds bounds = mainLayer.computeFullBounds(null);
		bounds.setFrame(bounds.getX() - widthPadding / 2, bounds.getY() - heightPadding / 2, bounds.getWidth()
				+ widthPadding, bounds.getHeight() + heightPadding);

		setViewSize((int) Math.ceil(bounds.getWidth()), (int) Math.ceil(bounds.getHeight()));
		camera.setViewBounds(bounds);
	}

	@Override
	public void viewFullMap()
	{
		PCamera camera = getCamera();

		// padding on left/right/top/bottom of frame
		final float widthPadding = 20;
		final float heightPadding = 20;

		zoomSubject.zoomNormalDelta(1.0, new Point2D.Float(0, 0), camera);

		centerMap();

		SlotRegion slotRegion = this.displayManager.getSlotRegion();
		Rectangle2D slotBounds = slotRegion.getCurrentBounds();

		Rectangle2D fullBounds = mainLayer.computeFullBounds(null);

		fullBounds = this.displayManager.expanedForLegends(fullBounds, false);

		// determine max width of frame
		float frameWidthLeft = (float) Math.abs(slotBounds.getX() - fullBounds.getX());
		float frameWidthRight = (float) Math.abs((fullBounds.getX() + fullBounds.getWidth())
				- (slotBounds.getX() + slotBounds.getWidth()));
		float frameHeightTop = (float) Math.abs((fullBounds.getY() - slotBounds.getY()));
		float frameHeightBottom = (float) Math.abs((slotBounds.getY() + slotBounds.getHeight())
				- (fullBounds.getY() + fullBounds.getHeight()));

		float maxFrameWidth = Math.max(frameWidthLeft, frameWidthRight);
		float maxFrameHeight = Math.max(frameHeightTop, frameHeightBottom);

		float calculatedLeftTopX = (float) (slotBounds.getX() - maxFrameWidth - widthPadding);
		float calculatedLeftTopY = (float) (slotBounds.getY() - maxFrameHeight - heightPadding);

		float calculatedWidth = (float) (slotBounds.getWidth() + 2 * maxFrameWidth + 2 * widthPadding);
		float calculatedHeight = (float) (slotBounds.getHeight() + 2 * maxFrameHeight + 2 * heightPadding);

		Rectangle2D calculatedBounds = new Rectangle2D.Float(calculatedLeftTopX, calculatedLeftTopY, calculatedWidth,
				calculatedHeight);

		setViewSize((int) Math.ceil(calculatedWidth), (int) Math.ceil(calculatedHeight));
		camera.setViewBounds(calculatedBounds);
	}

	@Override
	public void setViewSize(int width, int height)
	{
		if (width <= 0)
			throw new IllegalArgumentException("Width must be positive");
		if (height <= 0)
			throw new IllegalArgumentException("Height must be positive");

		// GlobalStyle gStyle = getMapStyle().getGlobalStyle();
		PCamera camera = getCamera();

		setProperBounds();

		camera.setWidth(width);
		camera.setHeight(height);
	}

	@Override
	public void centerMap()
	{
		Point2D centerPoint = displayManager.getCenterPoint();
		setCenter(centerPoint);
	}

	@Override
	public double getViewHeight()
	{
		return getCamera().getHeight();
	}

	@Override
	public double getViewWidth()
	{
		return getCamera().getWidth();
	}

	@Override
	public double getSpannedBases(int length)
	{
		return this.displayManager.getBackbone().getSpannedBases(length);
	}

	@Override
	public SequencePoint translatePoint(Point2D point)
	{
		return this.displayManager.getBackbone().translate(point);
	}

	@Override
	public int getMaxSequenceLength()
	{
		return displayManager.getLocationConverter().getSequenceLength();
	}

	@Override
	public double getCenterBaseValue()
	{
		PBounds viewBounds = getCamera().getViewBounds();

		SequencePoint point = this.displayManager.getBackbone().translate(viewBounds.getCenter2D());

		return point.getBase();
	}

	public void zoomNormal(double scale)
	{
		PBounds centerBounds = getCamera().getViewBounds();
		Point2D point = centerBounds.getCenter2D();

		zoomSubject.zoomNormal(scale, point, getCamera());
	}

	public double getZoomNormalFactor()
	{
		return getCamera().getViewScale();
	}

	@Override
	public ElementControl getElementControl()
	{
		return this.elementControl;
	}

	public GenomeData getGenomeData()
	{
		return genomeData;
	}

	public LayoutFactory getLayoutFactory()
	{
		return layoutFactory;
	}

	@Override
	public double getMinZoomFactor()
	{
		return this.displayManager.getBackbone().getMinScale();
	}

	@Override
	public double getMaxZoomFactor()
	{
		return this.displayManager.getBackbone().getMaxScale();
	}

	@Override
	public void updateStyle(Style style)
	{
		StyleController styleController = style.getStyleController();

		// Full rebuild
		if (styleController.isFullRebuildRequired())
		{
			// Set it to itself.. rebuilds..
			this.setMapStyle(this.getMapStyle());
		}
		// Any partial rebuild(s)
		else if (styleController.isRebuildRequired())
		{
			// Many partial rebuilds:
			updateStyles(styleController);

			// Global
			if (styleController.getGlobalStyleController().isRebuildRequired())
			{
				updateGlobalStyle();
			}

			// Tooltips:
			if (styleController.getTooltipStyleController().isRebuildRequired())
			{
				updateTooltipStyle();
			}
		}
	}

	/**
	 * Updates (most of) the styles.
	 */
	private void updateStyles(final StyleController styleController)
	{
		// Execution:
		ThreadService.executeOnEDT(new Runnable()
		{
			@Override
			public void run()
			{
				displayManager.updateStyle(styleController);
			}
		});
	}

	/**
	 * Updates the tool tip style.
	 */
	private void updateTooltipStyle()
	{
		// Execution:
		ThreadService.executeOnEDT(new Runnable()
		{
			@Override
			public void run()
			{
				toolTipHandler.setToolTipStyle(getMapStyle().getGlobalStyle().getTooltipStyle());
			}
		});
	}

	/**
	 * Updates the global style.
	 */
	private void updateGlobalStyle()
	{
		// Execution:
		ThreadService.executeOnEDT(new Runnable()
		{
			@Override
			public void run()
			{
				// sets background paint
				getCamera().setPaint(getMapStyle().getGlobalStyle().getBackgroundPaint());
			}
		});
	}

    @Override
    public DisplayManager getDisplayManager() {
        return this.displayManager;
    }
}
