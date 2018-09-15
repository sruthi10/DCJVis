package ca.corefacility.gview.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.GViewMapManager;
import ca.corefacility.gview.style.MapStyle;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PDragSequenceEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.util.PAffineTransform;
import edu.umd.cs.piccolo.util.PDimension;
import edu.umd.cs.piccolo.util.PPaintContext;
import edu.umd.cs.piccolox.nodes.P3DRect;

public class BirdsEyeViewImp extends PCanvas implements BirdsEyeView, PropertyChangeListener
{
	private static final long serialVersionUID = 1610103487483090629L;

	/**
	 * This is the node that shows the viewed area.
	 */
	private PNode areaVisiblePNode;

	/**
	 * This is the canvas that is being viewed
	 */
	private PCanvas viewedCanvas;

	/**
	 * The change listener to know when to update the birds eye view.
	 */
	private PropertyChangeListener changeListener;	
	private GUIController guiController;

	/**
	 * Creates a new instance of a BirdsEyeView
	 */
	public BirdsEyeViewImp()
	{
		// create the PropertyChangeListener for listening to the viewed
		// canvas
		changeListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt)
			{
				updateFromViewed();
			}
		};

		// create the coverage node
		areaVisiblePNode = new P3DRect();
		areaVisiblePNode.setPaint(new Color(128, 128, 255));
		areaVisiblePNode.setTransparency(.7f);
		areaVisiblePNode.setBounds(0, 0, 100, 100);
		getCamera().addChild(areaVisiblePNode);

		// add the drag event handler
		getCamera().addInputEventListener(new PDragSequenceEventHandler() {
			@Override
			protected void startDrag(PInputEvent e)
			{
				if (e.getPickedNode() == areaVisiblePNode)
					super.startDrag(e);
			}

			@Override
			protected void drag(PInputEvent e)
			{
				PDimension dim = e.getDelta();
				viewedCanvas.getCamera().translateView(0 - dim.getWidth(), 0 - dim.getHeight());
			}
		});
	
		getCamera().addInputEventListener(new PBasicInputEventHandler() {
			@Override
			public void mousePressed(PInputEvent event)
			{
				PAffineTransform transformViewed = viewedCanvas.getCamera().getViewTransformReference();
				Point2D point =  event.getPosition();				
				Dimension2D d = new Dimension((int)point.getX(), (int)point.getY());
				
				int clickCount = event.getClickCount();					
				double x; 
				double y;
				
				
				//transform to the viewedCanvas's coordinate scaling
				d = transformViewed.transform(d, null);
				
				//extract information, add offset so that the camera is centered on the click, not the top left corner
				x = d.getWidth() - viewedCanvas.getCamera().getWidth() / 2;
				y = d.getHeight() - viewedCanvas.getCamera().getHeight() / 2;
				
				if(clickCount == 2)
					viewedCanvas.getCamera().setViewOffset(0 - x, 0 - y);
			}
		});

		// remove Pan and Zoom
		removeInputEventListener(getPanEventHandler());
		removeInputEventListener(getZoomEventHandler());

		setDefaultRenderQuality(PPaintContext.LOW_QUALITY_RENDERING);

	}
	
	/**
	 * Creates a new instance of a BirdsEyeView
	 */
	public BirdsEyeViewImp(GUIController guiController)
	{
		this();
		
		this.guiController = guiController;
		GViewMapManager gViewMapManager = this.guiController.getCurrentStyle().getStyleController().getGViewMapManager();
		
		gViewMapManager.connectGViewMap(this);
	}

	public void connect(GViewMap gViewMap)
	{
		// a sufficient instance check
		assert (gViewMap instanceof PCanvas);

		this.viewedCanvas = (PCanvas) gViewMap;

		MapStyle style = gViewMap.getMapStyle();

		if (style != null)
		{
			// set birds eye view to same background as map
			this.getCamera().setPaint(style.getGlobalStyle().getBackgroundPaint());
		}

		viewedCanvas.getCamera().addPropertyChangeListener(changeListener);

		getCamera().addLayer(viewedCanvas.getLayer()); // adds gview.main layer of map to this
		// BirdsEyeView
	}

	/**
	 * This method will get called when the viewed canvas changes
	 */
	public void propertyChange(PropertyChangeEvent event)
	{
		updateFromViewed();
	}

	/**
	 * This method gets the state of the viewed canvas and updates the BirdsEyeViewer This can be
	 * called from outside code
	 */
	public void updateFromViewed()
	{
		double viewedX;
		double viewedY;
		double viewedHeight;
		double viewedWidth;
		
		Rectangle2D drag_bounds = getCamera().getUnionOfLayerFullBounds();
		getCamera().animateViewToCenterBounds(drag_bounds, true, 0);

		double ul_camera_x = viewedCanvas.getCamera().getViewBounds().getX();
		double ul_camera_y = viewedCanvas.getCamera().getViewBounds().getY();
		double lr_camera_x = ul_camera_x + viewedCanvas.getCamera().getViewBounds().getWidth();
		double lr_camera_y = ul_camera_y + viewedCanvas.getCamera().getViewBounds().getHeight();		

		double ul_layer_x = drag_bounds.getX();
		double ul_layer_y = drag_bounds.getY();
		double lr_layer_x = drag_bounds.getX() + drag_bounds.getWidth();
		double lr_layer_y = drag_bounds.getY() + drag_bounds.getHeight();

		// find the upper left corner

		// set to the lesser value
		if (ul_camera_x < ul_layer_x)
			viewedX = ul_layer_x;
		else
			viewedX = ul_camera_x;

		// same for y
		if (ul_camera_y < ul_layer_y)
			viewedY = ul_layer_y;
		else
			viewedY = ul_camera_y;

		// find the lower right corner

		// set to the greater value
		if (lr_camera_x < lr_layer_x)
			viewedWidth = lr_camera_x - viewedX;
		else
			viewedWidth = lr_layer_x - viewedX;

		// same for height
		if (lr_camera_y < lr_layer_y)
			viewedHeight = lr_camera_y - viewedY;
		else
			viewedHeight = lr_layer_y - viewedY;

		Rectangle2D bounds = new Rectangle2D.Double(viewedX, viewedY, viewedWidth, viewedHeight);
		bounds = getCamera().viewToLocal(bounds);
		areaVisiblePNode.setBounds(bounds);	

		// keep the birds eye view centered
		getCamera().animateViewToCenterBounds(drag_bounds, true, 0);
	}
}
