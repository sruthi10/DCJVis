package ca.corefacility.gview.managers;


import java.awt.Shape;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import org.biojava.bio.seq.Feature;
import org.biojava.bio.symbol.Location;

import ca.corefacility.gview.data.GenomeData;
import ca.corefacility.gview.layout.feature.FeatureShapeRealizer;
import ca.corefacility.gview.layout.feature.FullBackboneShapeRealizer;
import ca.corefacility.gview.layout.sequence.LocationConverter;
import ca.corefacility.gview.layout.sequence.SlotPath;
import ca.corefacility.gview.layout.sequence.SlotRegion;
import ca.corefacility.gview.managers.labels.LabelsManager;
import ca.corefacility.gview.map.event.BackboneZoomEvent;
import ca.corefacility.gview.map.event.GViewEvent;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.event.LayoutChangedEvent;
import ca.corefacility.gview.map.items.BasicBackboneItem;
import ca.corefacility.gview.map.items.FeatureItem;
import ca.corefacility.gview.map.items.FeatureItemImp;
import ca.corefacility.gview.map.items.FeaturesLayer;
import ca.corefacility.gview.map.items.Layer;
import ca.corefacility.gview.map.items.MapComponent;
import ca.corefacility.gview.map.items.MapItem;
import ca.corefacility.gview.map.items.SlotLayer;
import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.style.datastyle.DataStyle;
import ca.corefacility.gview.style.datastyle.FeatureHolderStyle;
import ca.corefacility.gview.style.datastyle.SlotItemStyle;
import ca.corefacility.gview.style.datastyle.SlotStyle;
import ca.corefacility.gview.style.items.BackboneStyle;
import ca.corefacility.gview.utils.ProgressHandler;
import ca.corefacility.gview.utils.thread.ThreadRange;
import ca.corefacility.gview.utils.thread.ThreadService;
import ca.corefacility.gview.utils.thread.ZoomEventThread;

/**
 * Handles layout/building up of features within the slots.
 * 
 * @author Aaron Petkau
 * 
 */
public class FeaturesManager implements GViewEventListener
{
	private FeaturesLayer featuresLayer;
	private LabelsManager labelsManager;

	private GenomeData genomeData; // we need access to this in order to build/re-build features
	private MapStyle mapStyle; // again, need acces to this in order to build/re-build features
	// (BackboneChangeEvents).

	private LocationConverter converter;
	
	public final HashMap<Integer, ArrayList<FeatureItem>> featureItems = new HashMap<Integer, ArrayList<FeatureItem>>();
	private BasicBackboneItem backboneItem;

	public FeaturesManager(GenomeData genomeData, MapStyle mapStyle, LabelsManager labelsManager) // need
	// labelsManager
	// to
	// construct
	// labels
	// from
	// features
	{
		featuresLayer = new FeaturesLayer();
		this.genomeData = genomeData;
		converter = new LocationConverter(genomeData);
		this.mapStyle = mapStyle;

		this.labelsManager = labelsManager;

		// buildFeatures(featuresLayer, mapStyle);
		// can't build features here, not ready yet
	}

	/**
	 * Provides access to layer storing all features, builds features if necessary.
	 * 
	 * @return The layer storing all the features.
	 */
	public MapComponent getFeaturesLayer()
	{
		return featuresLayer;
	}

	public void eventOccured(GViewEvent event)
	{
		if (event instanceof BackboneZoomEvent)
		{
			// zoom all features in layer, don't add each feature as a listener to Backbone
			MapItem[] items = this.featuresLayer.getMapItems();
			
			final int count = items.length;
			
			ArrayList<ThreadRange> ranges = ThreadService.getRanges(count);
			ArrayList<Callable<Object>> instances = new ArrayList<Callable<Object>>();
			
			//CREATE THREADS:
			for(ThreadRange range: ranges)
			{
				instances.add(new ZoomEventThread(range.getStart(), range.getEnd(), items, (BackboneZoomEvent)event));
			}
			
			//WAIT
			ThreadService.invokeAll(instances);
		}
		else if (event instanceof LayoutChangedEvent)
		{
			SlotRegion slotRegion = ((LayoutChangedEvent) event).getSlotRegion();

			// reconstruct features
			featuresLayer.clear();
			buildFeatures(featuresLayer, mapStyle, slotRegion, genomeData);
			slotRegion.addEventListener(this); // make sure we listen for events from new backbone
		}
		// make sure to listen to style change events
		// else if (event instanceof StyleChangedEvent)
		// {
		//			
		// }
	}

	/**
	 * Searches for the FeatureItem associated with the passed feature.
	 * 
	 * @param feature
	 *            The feature to search for it's associated FeatureItem.
	 * @return The FeatureItem if found, or null if not found.
	 */
	public List<FeatureItem> findFeatureItems(Feature feature)
	{
		List<FeatureItem> itemsList = null;

		if (feature != null)
		{
			// TODO should I do a different search method later? This is just basic linear search,
			// must go through all FeatureItems each time.
			Iterator<MapItem> items = featuresLayer.itemsIterator();

			while (items.hasNext())
			{
				MapItem mapItem = items.next();

				if (mapItem instanceof FeatureItem)
				{
					FeatureItem currItem = (FeatureItem) mapItem;

					if (feature.equals(currItem.getFeature()))
					{
						if (itemsList == null)
						{
							itemsList = new ArrayList<FeatureItem>();
						}

						itemsList.add(currItem);
					}
				}
			}
		}

		return itemsList;
	}

	/**
	 * Builds all of the features onto the passed layer.
	 * 
	 * @param layer
	 *            The layer to build the features onto.
	 */
	private void buildFeatures(MapComponent layer, MapStyle mapStyle, SlotRegion slotRegion, GenomeData genomeData)
	{
		// need to set these
		DataStyle dataStyle = mapStyle.getDataStyle();

		MapComponent currentLayer;

		ProgressHandler.start(ProgressHandler.Stage.BUILDING_FEATURES);

		currentLayer = new Layer();
		layer.add(currentLayer);

		// should I create backbone here? or in some other object?
		currentLayer.add(createBackboneItem(mapStyle.getGlobalStyle().getBackboneStyle(), slotRegion));

		// creates item slots
		Iterator<SlotStyle> slots = dataStyle.slots();
		while (slots.hasNext())
		{
			SlotStyle currentSlot = slots.next();

			MapComponent slot = createSlot(currentSlot, slotRegion, genomeData);

			if (slot != null)
			{
				currentLayer.add(slot);
			}
		}
		
		ProgressHandler.finish(ProgressHandler.Stage.BUILDING_FEATURES);

		// instead of layout, we need to do all code in here to layout
		// layer.layout();
	}

	private MapItem createBackboneItem(BackboneStyle backboneStyle, SlotRegion slotRegion)
	{
		double thickness = 1.0;
		double heightAdjust = 0.0;
		
		BasicBackboneItem backboneItem = new BasicBackboneItem(Location.full); // TODO change this
		this.backboneItem = backboneItem;
		// possibly?

		backboneItem.setPaint(backboneStyle.getPaint());
		backboneItem.setShapeEffectRenderer(backboneStyle.getShapeEffectRenderer());

		Location location = backboneItem.getLocation();

		backboneItem.setShape(calculateLayout(0, location, thickness, heightAdjust, FullBackboneShapeRealizer.instance, slotRegion));
		// backboneItem.setStroke(null);

		return backboneItem;
	}

	// used to create an individual slot
	private MapComponent createSlot(SlotStyle slotStyle, SlotRegion slotRegion, GenomeData genomeData)
	{
		MapComponent layer = new SlotLayer(slotStyle);
		ArrayList<FeatureItem> featuresInLayer = new ArrayList<FeatureItem>(); // create features in layer, sort them, then add to layer

		Iterator<SlotItemStyle> items = slotStyle.styles();

		while (items.hasNext())
		{
			SlotItemStyle currItemStyle = items.next();
			
			if (currItemStyle.getClass().equals(FeatureHolderStyle.class))
			{
				FeatureHolderStyle currHolder = (FeatureHolderStyle) currItemStyle;
	
				createFeaturesInHolder(featuresInLayer, slotStyle.getSlot(), currHolder, slotRegion, genomeData);
			}
		}
				
		if (slotStyle.getSlot() > 0)
		{
			Collections.sort(featuresInLayer, new FeatureItemLocationComparatorDescending());
		}
		else
		{
			Collections.sort(featuresInLayer, new FeatureItemLocationComparatorAscending());			
		}
		
		// add sorted features to layer to be displayed
		for (FeatureItem item : featuresInLayer)
		{
			layer.add(item);	
		}

		return layer;
	}

	private void createFeaturesInHolder(ArrayList<FeatureItem> featuresInLayer, int slot, FeatureHolderStyle style, SlotRegion slotRegion, GenomeData genomeData)
	{
		// sufficient for a null check
		if (style == null)
		{
			throw new IllegalArgumentException("style is null");
		}

		Iterator<SlotItemStyle> featureStyles = style.styles();

		while (featureStyles.hasNext())
		{
			FeatureHolderStyle currStyle = (FeatureHolderStyle) featureStyles.next();

			createFeaturesInHolder(featuresInLayer, slot, currStyle, slotRegion, genomeData);
		}

		createFeaturesOnlyInHolder(slot, featuresInLayer, style, slotRegion, genomeData);
	}

	// creates only those features styled by the passed style
	private void createFeaturesOnlyInHolder(int slot, ArrayList<FeatureItem> featuresInLayer, FeatureHolderStyle style, SlotRegion slotRegion, GenomeData genomeData)
	{
		// LabelsManager labelsManager = null;

		// sufficient for a null check
		if ((style == null) || (featuresInLayer == null))
		{
			throw new IllegalArgumentException("null arguments");
		}
		
		ArrayList<FeatureItem> featureItems = this.getAssociatedFeatureItems(style);

		Iterator<Feature> features = genomeData.getOnlyFeatures(style).features();
		while (features.hasNext())
		{
			Feature currentFeature = (Feature) features.next();

			FeatureItem item = new FeatureItemImp(style, currentFeature);
			featureItems.add(item);

			layout(slot, item, slotRegion);

			// TODO even if we aren't supposed to create a label, we are adding null here
			labelsManager.buildLabel(style.getLabelStyle(), item, slot > 0);

			featuresInLayer.add(item);
		}
	}
	
	//Gets the FeatureItem list associated with the style.
	private ArrayList<FeatureItem> getAssociatedFeatureItems(FeatureHolderStyle style)
	{
		ArrayList<FeatureItem> featureItems = this.featureItems.get(style.getId());
		
		if(featureItems == null)
		{
			featureItems = new ArrayList<FeatureItem>();
			this.featureItems.put(style.getId(), featureItems);
		}
		
		return featureItems;
	}
	
	//Updates all feature items associated with the style.
	public void updateFeatureItems(FeatureHolderStyle style)
	{		
		ArrayList<FeatureItem> featureItems = getAssociatedFeatureItems(style);
		
		for(int i = 0; i < featureItems.size(); i++)
		{
			featureItems.get(i).setFeatureHolderStyle(style);
		}
	}
	
        public void updateFeatureStyle(Feature feature,FeatureHolderStyle style)
        {
        
        }
	//Updates the backbone item.
	public void updateBackboneItem(BackboneStyle style)
	{
		this.backboneItem.setPaint(style.getPaint());
	}

	// methods below handle layout of item

	private void layout(int slot, FeatureItem featureItem, SlotRegion slotRegion)
	{

		// should I do this? Or should I keep track of styles within this object, and not
		// obtain/store styles in FeatureItem?
		double thickness = featureItem.getThickness();
		double heightAdjust = featureItem.getHeightAdjust();

		Feature feature = featureItem.getFeature(); // this is bad, I know, but I will change it
		// later
		Location location = feature.getLocation();
		FeatureShapeRealizer shapeRealizer = featureItem.getFeatureShapeRealizer();

		featureItem.setShape(calculateLayout(slot, location, thickness, heightAdjust, shapeRealizer, slotRegion));
	}

	// shape prototype for item
	private Shape calculateLayout(int slot, Location location, double thickness, double heightAdjust, FeatureShapeRealizer shapeRealizer,
			SlotRegion slotRegion)
	{
		Shape shapeLayout = null;

		SlotPath shape = slotRegion.getSlotPath(slot);

		shapeLayout = shapeRealizer.createFeaturePrototype(converter, shape, location, thickness, heightAdjust); // TODO
		// change,
		// only
		// testing

		return shapeLayout;
	}
	
	private class FeatureItemLocationComparatorDescending implements Comparator<FeatureItem>
	{
        // sort by order of minimum location
		@Override
		public int compare(FeatureItem o1, FeatureItem o2)
		{
			Feature f1 = o1.getFeature();
			Feature f2 = o2.getFeature();
			
			if (f1 == null || f2 == null)
			{
				throw new NullPointerException("Feature in FeatureItem is null");
			}
			
			Location l1 = f1.getLocation();
			Location l2 = f2.getLocation();
			
			return l2.getMin() - l1.getMin();
		}
	}
	
	private class FeatureItemLocationComparatorAscending implements Comparator<FeatureItem>
	{
        // sort by order of minimum location
		@Override
		public int compare(FeatureItem o1, FeatureItem o2)
		{
			Feature f1 = o1.getFeature();
			Feature f2 = o2.getFeature();
			
			if (f1 == null || f2 == null)
			{
				throw new NullPointerException("Feature in FeatureItem is null");
			}
			
			Location l1 = f1.getLocation();
			Location l2 = f2.getLocation();
			
			return l1.getMin() - l2.getMin();
		}
	}
}
