package ca.corefacility.gview.managers;


import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;

import org.biojava.bio.seq.Feature;

import ca.corefacility.gview.data.GenomeData;
import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.layout.sequence.LayoutFactory;
import ca.corefacility.gview.layout.sequence.LocationConverter;
import ca.corefacility.gview.layout.sequence.SlotRegion;
import ca.corefacility.gview.managers.labels.LabelsManager;
import ca.corefacility.gview.managers.ruler.RulerManager;
import ca.corefacility.gview.map.controllers.StyleController;
import ca.corefacility.gview.map.event.DisplayChangeListener;
import ca.corefacility.gview.map.event.DisplayUpdated;
import ca.corefacility.gview.map.event.GViewEvent;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.inputHandler.ToolTipHandler;
import ca.corefacility.gview.map.inputHandler.ZoomSubject;
import ca.corefacility.gview.map.items.FeatureItem;
import ca.corefacility.gview.map.items.MapComponent;
import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.style.datastyle.FeatureHolderStyle;
import ca.corefacility.gview.style.datastyle.SlotItemStyle;
import ca.corefacility.gview.style.datastyle.SlotStyle;
import ca.corefacility.gview.utils.ProgressHandler;
import edu.umd.cs.piccolo.PCamera;

public class DisplayManager implements GViewEventListener
{
	public FeaturesManager featuresManager;
	private RulerManager rulerManager;
	private LabelsManager labelsManager;
	private PlotsManager plotsManager;
	private LegendManager legendManager;
	
	private SectionManager sectionManager;
	
	private GenomeData genomeData;
	private MapStyle mapStyle;
	private LayoutFactory layoutFactory;
	
	private LayoutManager layoutManager; // should I have this here?  But I need to somehow make featuresManager listener
	//	for BackboneZoomEvents
	
	private LocationConverter locationConverter;
	
	// place tooltip handler here since it depends on tool tip style, which is stored here
	private ToolTipHandler toolTipHandler;
	private ZoomSubject zoomSubject;
	private DisplayChangeListener displayChangeListener;
	
	private final PCamera camera;
	
	/**
	 * Creates a new manager which handles the display/layout of items on the map.
	 * @param genomeData  The genome data to render.
	 * @param mapStyle  The style to render the data as.
	 * @param layoutFactory  A factory defining how to construct the layout.
	 * @param zoomSubject  Used to listen into zoom events on map.
	 */
	public DisplayManager(PCamera camera, GenomeData genomeData, MapStyle mapStyle, LayoutFactory layoutFactory, ZoomSubject zoomSubject, 
			DisplayChangeListener displayChangeListener, ToolTipHandler toolTipHandler)
	{
		this.zoomSubject = zoomSubject;
		this.displayChangeListener = displayChangeListener;
		this.toolTipHandler = toolTipHandler;
		this.camera = camera;
		
		rebuild(mapStyle, layoutFactory, genomeData);
		
		// build features here?  We need to make sure featuresManager got the event of SlotRegionChangedEvent from layoutManager (to get Backbone).
//		featuresManager.buildFeatures();
		
//		styleEventManager = new StyleEventManager();
//		this.mapStyle.addEventListener(styleEventManager);
		
		// this line right here should trigger actual building of items, by passing BackboneChangeEvent to any listeners
		//  need to make sure style/data setup in the respective managers ahead of time
//		layoutManager.createLayout(layoutFactory, mapStyle, zoomSubject);

	}
	
	/**
	 * Changes the genome data to be displayed.  Causes a re-render of everything.
	 * @param genomeData  The new genome data to be displayed.
	 */
	public void setData(GenomeData genomeData)
	{
		if (genomeData == null)
		{	
			throw new NullPointerException("genomeData is null");
		}
		else
		{	
			rebuild(mapStyle, layoutFactory, genomeData);
		}
	}
	
	private void rebuild(MapStyle mapStyle, LayoutFactory layoutFactory, GenomeData genomeData)
	{
		this.genomeData = genomeData;
		this.mapStyle = mapStyle;
		this.layoutFactory = layoutFactory;
		
		locationConverter = new LocationConverter(genomeData);
		
		ProgressHandler.start(ProgressHandler.Stage.CREATING_LAYOUT);
		layoutManager = new LayoutManager(locationConverter, layoutFactory, mapStyle, zoomSubject); // need to create first, since building of actual items requires this to be created
		toolTipHandler.setToolTipStyle(mapStyle.getGlobalStyle().getTooltipStyle());
		ProgressHandler.finish(ProgressHandler.Stage.CREATING_LAYOUT);
		
		createManagers(genomeData, mapStyle, layoutManager, displayChangeListener);
	}
	
	private void createManagers(GenomeData data, MapStyle mapStyle, LayoutManager layoutManager, DisplayChangeListener displayChangeListener)
	{
		labelsManager = layoutFactory.createLabelsManager();
		
		featuresManager = new FeaturesManager(data, mapStyle, labelsManager); // need to pass labelsManager here
		rulerManager = new RulerManager(mapStyle.getGlobalStyle().getRulerStyle(), locationConverter);
		sectionManager = new SectionManager(locationConverter.getSequenceLength());
		
		plotsManager = new PlotsManager(mapStyle.getDataStyle(), genomeData);
		
		if(legendManager == null)
		{
			legendManager = new LegendManager(camera, mapStyle);
		}
		else
		{
			legendManager.rebuild(mapStyle);
		}
		
		layoutManager.addEventListener(featuresManager);
		layoutManager.addEventListener(rulerManager);
		layoutManager.addEventListener(labelsManager);
		layoutManager.addEventListener(plotsManager);
		layoutManager.addEventListener(legendManager);
		
		sectionManager.addEventListener(rulerManager);
		
		// remove later, when event listeners are all tied together
		layoutManager.addEventListener(sectionManager);
		//sectionManager.eventOccured(new ResolutionSwitchEvent(layoutManager.getBackbone(), ResolutionSwitchEvent.Direction.INCREASE));
		
		// need access to PanEventHandler hear to make sectionManager listen for events from it
		
		displayChangeListener.removeAllEventListeners();
		displayChangeListener.addEventListener(sectionManager);
		displayChangeListener.addEventListener(legendManager);
		sectionManager.eventOccured(new DisplayUpdated(this, null, null, true));
	}
	
	public MapComponent getFeaturesLayer()
	{
		return featuresManager.getFeaturesLayer();
	}
	
	/**
	 * Searches for the FeatureItems associated with the passed feature.
	 * @param feature  The feature to search for it's associated FeatureItem.
	 * @return  The FeatureItems in a List if found, or null if not found.
	 */
	public List<FeatureItem> findFeatureItems(Feature feature)
	{
		return featuresManager.findFeatureItems(feature);
	}
	
	public MapComponent getRulerLayer()
	{
		return rulerManager.getRulerLayer();
	}
	
	public MapComponent getLabelsLayer()
	{
		return labelsManager.getLabelsLayer();
	}
	
	public MapComponent getToolTipItem()
	{
		return toolTipHandler.getToolTipItem();
	}
	
	public MapComponent getPlotsLayer()
	{
		return plotsManager.getPlotsLayer();
	}
	
	public MapComponent getLegendLayer()
	{
		return legendManager.getLegendLayer();
	}
	
	public void changeLayout(LayoutFactory layoutFactory, ZoomSubject zoomSubject)
	{
		this.layoutFactory = layoutFactory;
		layoutManager.createLayout(layoutFactory, mapStyle, zoomSubject);
	}
	
	public MapStyle getMapStyle()
	{
		return mapStyle;
	}
	
	/**
	 * Sets the MapStyle for this map and rebuilds.
	 * 
	 * @param mapStyle  The map style to set.
	 */
	public void setMapStyle(MapStyle mapStyle)
	{
		if (mapStyle == null)
		{
			throw new NullPointerException("mapStyle is null");
		}
		else
		{
			rebuild(mapStyle, layoutFactory, genomeData);
		}
	}
	
	/**
	 * @return  The backbone object.
	 */
	public Backbone getBackbone()
	{
		return layoutManager.getBackbone();
	}
	
	public SlotRegion getSlotRegion()
	{
		return layoutManager.getSlotRegion();
	}

	// TODO this was only placed so we can send events by keyboard to re-calculate zoom
	// there must be a better way
	public void eventOccured(GViewEvent event)
	{
		labelsManager.eventOccured(event);
		legendManager.eventOccured(event);
	}

	public void setLegendDisplayed(boolean display)
	{
		legendManager.setLegendDisplayed(display);
	}

	public Point2D getCenterPoint()
	{
		return layoutManager.getCenterPoint();
	}

	public LocationConverter getLocationConverter()
	{
		return locationConverter;
	}

	public void setRulerDisplayed(boolean displayed)
	{
		rulerManager.setDisplayed(displayed);
	}

	public void setLabelsDisplayed(boolean displayed)
	{
		labelsManager.setLabelsDisplayed(displayed);
	}

	public boolean getLegendDisplayed()
	{
		return legendManager.getDisplayed();
	}

	public boolean getLabelsDisplayed()
	{
		return labelsManager.getDisplayed();
	}

	public boolean getRulerDisplayed()
	{
		return rulerManager.getDisplayed();
	}

	public Rectangle2D expanedForLegends(Rectangle2D fullBounds, boolean b)
	{
		return this.legendManager.expanedForLegends(fullBounds, b);
	}

	//Updates the display of the style; does not do a full rebuild.
	public void updateStyle(StyleController styleController)
	{
		if(styleController.isRebuildRequired())
		{
			//Features / Sets
			if(styleController.getSetStyleController().isRebuildRequired())
			{
				this.updateFeatureHolderStyles();
			}
			
			//Backbone
			if(styleController.getBackboneStyleController().isRebuildRequired())
			{
				this.featuresManager.updateBackboneItem(this.mapStyle.getGlobalStyle().getBackboneStyle());
			}
			
			//Ruler
			if(styleController.getRulerStyleController().isRebuildRequired())
			{
				this.rulerManager.updateRuler();
			}
			
			//Legends
			if(styleController.getLegendStyleController().isRebuildRequired())
			{
				this.legendManager.update(mapStyle);
			}
			
			//Labels
			if(styleController.getLabelStyleController().isRebuildRequired())
			{
				this.labelsManager.update();
			}
			
			//Plots
			if(styleController.getPlotStyleController().isRebuildRequired())
			{
				this.plotsManager.update();
			}
		}
	}
	
	//ALL FHS
	private void updateFeatureHolderStyles()
	{
		SlotStyle currentSlotStyle;
		SlotItemStyle currentSlotItemStyle;		
		
		for(Iterator<SlotStyle> slots = mapStyle.getDataStyle().slots(); slots.hasNext();)
		{
			currentSlotStyle = slots.next();
			
			for(Iterator<SlotItemStyle> slotItemStyles = currentSlotStyle.styles(); slotItemStyles.hasNext();)
			{
				currentSlotItemStyle = slotItemStyles.next();
				
				if(currentSlotItemStyle instanceof FeatureHolderStyle)
				{
					updateFeatureHolderStyle((FeatureHolderStyle)currentSlotItemStyle);
				}
			}
		}
	}
	
	//Individual FHS and ALL its child FHS
	private void updateFeatureHolderStyle(FeatureHolderStyle style)
	{
		SlotItemStyle currentSlotItemStyle;
		FeatureHolderStyle currentFeatureHolderStyle;
		
		this.featuresManager.updateFeatureItems(style);
		
		//Sub-styles:
		for(Iterator<SlotItemStyle> slotItemStyles = style.styles(); slotItemStyles.hasNext();)
		{
			currentSlotItemStyle = slotItemStyles.next();
			
			if(currentSlotItemStyle instanceof FeatureHolderStyle)
			{
				currentFeatureHolderStyle = (FeatureHolderStyle)currentSlotItemStyle;				
				this.featuresManager.updateFeatureItems(currentFeatureHolderStyle);
				
				updateFeatureHolderStyle(currentFeatureHolderStyle);
			}
		}		
	}
}
