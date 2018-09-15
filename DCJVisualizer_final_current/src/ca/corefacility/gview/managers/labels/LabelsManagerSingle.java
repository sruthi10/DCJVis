package ca.corefacility.gview.managers.labels;


import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.layout.sequence.ScaleCalculator;
import ca.corefacility.gview.map.event.BackboneZoomEvent;
import ca.corefacility.gview.map.event.GViewEvent;
import ca.corefacility.gview.map.event.LabelEvent;
import ca.corefacility.gview.map.event.LayoutChangedEvent;
import ca.corefacility.gview.map.event.ResolutionSwitchEvent;
import ca.corefacility.gview.map.event.ZoomEvent;
import ca.corefacility.gview.map.items.MapComponent;
import ca.corefacility.gview.utils.ProgressHandler;

/**
 * Handles laying out of the labels on the map.
 */
public class LabelsManagerSingle extends LabelsManager
{
//	private LabelsStore rankedLabels; // stores labels, ranked by priority

	// private static final int MAX_LABELS = 5000000;
	
	private List<Label> activatedLabels;

	private PriorityQueue<Label> displayedLabels;
	private PriorityQueue<Label> undisplayedLabels;

	boolean cLabels = false;
	
	private double prevScale;
	
	private ScaleCalculator scaleCalculator;
	
	public LabelsManagerSingle(ScaleCalculator scaleCalculator)
	{
		super();

		displayedLabels = new PriorityQueue<Label>(10, new Label.ZoomScaleComparatorDescending());
		undisplayedLabels = new PriorityQueue<Label>(10, new Label.ZoomScaleComparatorAscending());
		
//		rankedLabels = new LabelsStore();
		labelsPositioner = new LabelsPositionerImp(slotRegion, labelSlots);

		activatedLabels = new LinkedList<Label>();
		
		previousZoomEvent = null;
		
		this.scaleCalculator = scaleCalculator;
	}
	
	/**
	 * For each label, determines the value where it should be turned on.
	 * @param labels
	 */
	private void calculateLabelsScaleFactor(List<Label> labels)
	{
		List<Label> processedLabels = new LinkedList<Label>();
		double maxScale = slotRegion.getBackbone().getMaxScale();
		
		// sort by priority (should I just have a sorted list passed to here?)
		Collections.sort(labels, new Label.PriorityComparatorDescending());
		
		for (Label label : labels)
		{
			double scaleIntersect = intersectsAtScaleLabels(label, processedLabels);
			double scaleIntersectSlots = intersectsAtScaleLayout(label);
			
			scaleIntersect = Math.max(scaleIntersect, scaleIntersectSlots);
			
			if(scaleIntersect < maxScale)
			{
				label.setZoomToDisplay(scaleIntersect);
			}
			else
			{
				label.setZoomToDisplay(maxScale);
			}
			
			processedLabels.add(label);
		}
	}
	
	/**
	 * Determines at what scale level the given label will intersect (dependent on the layout).
	 * @param label
	 * @return  The scale the given label will intersect.
	 */
	private double intersectsAtScaleLayout(Label label)
	{
		return scaleCalculator.intersectsAtScale(label,slotRegion);
	}

	// determines at which scale the passed label intersects some label in the passed list
	// this is specific to the layout, so should have linear/circular implementations
	// assumption for all this, labels "pinned to" center point
	private double intersectsAtScaleLabels(Label label, List<Label> placedLabels)
	{
		Dimension2D labelDimension = label.getDimension();
		double maxIntersection = 0;
		
		Backbone backbone = slotRegion.getBackbone();
		
		for (Label currLabel : placedLabels)
		{			
			double intersectionScale;
			// linear layout means label is "stretched" by width only
			Dimension2D currLabelDimension = currLabel.getDimension();
			
			intersectionScale = backbone.calculateIntersectionScale(label.getPinnedPoint(), labelDimension, currLabel.getPinnedPoint(), currLabelDimension);
			
			// if the calculated intersection scale is less than the scale that the curr label is to be displayed
			//  then we can turn this "label" on at any time below this, as "currLabel" is not going to be turned on anyways at this level
			if (intersectionScale < currLabel.getZoomToDisplay())
			{
				intersectionScale = 0.0;
			}
			
			// we only need to worry about case where the calculated intersection scale is bigger than the zoom scale we turn currLabel on
			// 	if the calculated intersection scale (the scale where label and currLabel stop intersecting) is less than the scale currLabel is turned on
			//	(currLabel.getZoomToDisplay()) then this label (label) will never overlap currLabel anyways.
			if (intersectionScale >= currLabel.getZoomToDisplay())
			{
				if (intersectionScale > maxIntersection)
				{
					maxIntersection = intersectionScale;
				}
			}
			
			//System.out.println("intersection scale " + intersectionScale);
		}
		
		return maxIntersection;
	}
	
	private void initialPositionLabels()
	{
		Backbone backbone = slotRegion.getBackbone();
		
		displayedLabels.clear();
		undisplayedLabels.clear();
		
		double prevScale = backbone.getScale();
		double zoomFactor = backbone.getMaxScale();
		
		//
		//zoomFactor = 100;
		//
		
		// zoom to largest display
		backbone.eventOccured(new ZoomEvent(zoomFactor, new Point2D.Double(), this));
		
		// do initial position of labels
		activatedLabels = labelsPositioner.positionLabels(allLabels, backbone.getScale());
		
		// calculate where labels should be turned on/off
		calculateLabelsScaleFactor(activatedLabels);
		
		// add all labels to displayed labels/add to labels layer
		for (Label label : activatedLabels)
		{
			labelTextLayer.add(label.getLabelTextLayer());
			labelLinesLayer.add(label.getLabelLineLayer());
		}
		// all labels are now displayed, so add all to displayedLabels
		displayedLabels.addAll(activatedLabels);
		
		// zoom back to current scale
		backbone.eventOccured(new ZoomEvent(prevScale, new Point2D.Double(), this));
		
		// turn on/off labels depending on scale
		calculateDisplayedLabels(prevScale);
	}

	/**
	 * Creates the layer and stores all the labels added with addLabel into it.
	 * 
	 * @return A MapComponent containing all labels.
	 */
	public MapComponent getLabelsLayer()
	{
		return labelsLayer;
	}

	private void calculateDisplayedLabels(double scale)
	{
		if (scale >= prevScale)
		{
			int addedLabels = 0;
			
			// search for any new undisplayed labels to turn on
			Label label = undisplayedLabels.peek();
			
			while (label != null && label.isDisplayedAt(scale))
			{	
				label = undisplayedLabels.poll();
				label.turnOnLabel();
				
				// add label to displayed labels
				displayedLabels.add(label);
				
				label = undisplayedLabels.peek();
				addedLabels++;
			}
		}
		else
		{
			int removedLabels = 0;
			
			// search for any new displayed labels to turn off
			Label label = displayedLabels.peek();
			
			while (label != null && !label.isDisplayedAt(scale))
			{
				label = displayedLabels.poll();
				label.turnOffLabel();
				
				// add label to undisplayed labels
				undisplayedLabels.add(label);
				
				label = displayedLabels.peek();
				
				removedLabels++;
			}
		}
		
		prevScale = scale;
	}

	// use this to re-draw tick marks at different scales
	public void eventOccured(GViewEvent event)
	{
		super.eventOccured(event);
		
		if (event instanceof BackboneZoomEvent)
		{
			if (displayedState)
			{
				BackboneZoomEvent zoomEvent = (BackboneZoomEvent) event;
				Backbone backbone = zoomEvent.getBackbone();
				if (backbone == null)
				{
					return;
				}
	
				calculateDisplayedLabels(backbone.getScale());
				
				for (Label label : displayedLabels)
				{
					label.eventOccured(event);
				}
			}
		}
		else if (event instanceof LayoutChangedEvent)
		{
			if (slotRegion != null)
			{
				slotRegion.getBackbone().removeEventListener(this); // remove from listening to old
				// backbone
			}

			this.slotRegion = ((LayoutChangedEvent) event).getSlotRegion();

			slotRegion.addEventListener(this); // listen to new backbone
			
			labelSlots = new LabelSlots(slotRegion.getSlotTranslator(), 1, 15, 30, 5);

			labelsPositioner.setSlotRegion(slotRegion, labelSlots);
			
			for (Label label : allLabels)
			{
				label.updateBackbone(slotRegion.getBackbone());
			}
			
			// position labels
			ProgressHandler.start(ProgressHandler.Stage.POSITIONING_LABELS);
			initialPositionLabels();
			ProgressHandler.finish(ProgressHandler.Stage.POSITIONING_LABELS);
		}
		else if (event instanceof ResolutionSwitchEvent)
		{
			if (displayedState)
			{
				calculateDisplayedLabels(((ResolutionSwitchEvent) event).getBackbone().getScale());
			}
		}
		else if (event instanceof LabelEvent)
		{
			// position labels
			initialPositionLabels();
		}
	}
}
