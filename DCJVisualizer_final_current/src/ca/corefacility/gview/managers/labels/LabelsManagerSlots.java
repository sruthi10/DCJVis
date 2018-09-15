package ca.corefacility.gview.managers.labels;


import java.awt.geom.Dimension2D;
import java.util.Collections;
import java.util.List;

import ca.corefacility.gview.layout.prototype.SequencePoint;
import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.map.event.BackboneZoomEvent;
import ca.corefacility.gview.map.event.GViewEvent;
import ca.corefacility.gview.map.event.LabelEvent;
import ca.corefacility.gview.map.event.LayoutChangedEvent;
import ca.corefacility.gview.map.event.ResolutionSwitchEvent;
import ca.corefacility.gview.map.items.MapComponent;
import ca.corefacility.gview.utils.ProgressHandler;

/**
 * Handles laying out of the labels on the map.
 */
public class LabelsManagerSlots extends LabelsManager
{
	private LabelSlots labelSlots;
	
	private int MAX_SLOTS = 6;

	//private ResolutionSwitcher resolutionSwitcher;

	// private static final int MAX_LABELS = 5000000;

	boolean cLabels = false;
	
	private LabelBuckets labelBuckets;
	
	public LabelsManagerSlots()
	{
		super();
	}
	
	/**
	 * Determines the largest height value given a list of labels.
	 * @param labels
	 * @return The largest height value in the list of labels.
	 */
	private double findLargestLabelHeight(List<Label> labels)
	{
		double maxHeight = 0;
		
		for (Label label : labels)
		{
			double currHeight = label.getDimension().getHeight();
			
			if (currHeight > maxHeight)
			{
				maxHeight = currHeight;
			}
		}
		
		return maxHeight;
	}
	
	/**
	 * For each label, determines the values where it should be turned on.
	 */
	private void placeLabelsInBuckets(List<Label> labels)
	{
		LabelSlotBounds labelSlotBounds = new LabelSlotBounds(MAX_SLOTS); // used to store which labels have been placed in which slots
		
		double maxScale = slotRegion.getBackbone().getMaxScale();
		
		// sort by priority (should I just have a sorted list passed to here?)
		Collections.sort(labels, new Label.PriorityComparatorDescending());
		
		for (Label label : labels)
		{
			placeInBuckets(label, labelSlotBounds, maxScale);
		}
	}
	
	/**
	 * Determines the zoom level for a given label within a particular slot where it will not intersect any other labels in that slot.
	 * @return The zoom level beyond which the passed label can be displayed in the passed slot.
	 */
	private double calculateLabelZoomLevel(int slot, SequencePoint workingPinnedPoint, Dimension2D workingDimension, List<SequenceLabelBounds> placedLabels, Backbone backbone,
			double maxScale, double prevMinZoomForSlots)
	{
		double maxLabelIntersection = 0;
		
		for (SequenceLabelBounds currLabel : placedLabels)
		{	
			double intersectionScale;
			// linear layout means label is "stretched" by width only
			Dimension2D currLabelDimensions = currLabel.getDimension();
			SequencePoint currLabelPinnedPoint = currLabel.getPinnedPoint();
			
			intersectionScale = backbone.calculateIntersectionScale(workingPinnedPoint, workingDimension, currLabelPinnedPoint, currLabelDimensions);
			
			// if the calculated intersection scale is less than the scale that the curr label is to be displayed
			//  then we can turn this "label" on at any time below this, as "currLabel" is not going to be turned on anyways at this level
			if (intersectionScale < currLabel.getLowerScale())
			{
				intersectionScale = 0.0;
			}
			
			// we only need to worry about case where the calculated intersection scale is bigger than the zoom scale we turn currLabel on
			// 	if the calculated intersection scale (the scale where label and currLabel stop intersecting) is less than the scale currLabel is turned on
			//	 then this label (label) will never overlap currLabel anyways.
			else if (intersectionScale >= currLabel.getLowerScale())
			{
				if (intersectionScale > maxLabelIntersection)
				{
					maxLabelIntersection = intersectionScale;
				}
			}
			//System.out.println("intersection scale " + intersectionScale);
		}
		
		return maxLabelIntersection;
	}
	
	private void placeInBuckets(Label label,
			LabelSlotBounds labelSlotBounds, double initialMaxScale)
	{
		double maxScale = initialMaxScale;
		
		Backbone backbone = slotRegion.getBackbone();
		
		// stores the minimum 
		double minZoomForSlots = initialMaxScale;
		double labelZoom;
		
		// keep track of minimum bucket we added this label to
		int minBucketValue = labelBuckets.getMaxBucketValue()+1;
		
		// TODO support inner slots
		// iterate through slots and determine at which zoom level should "label" be positioned in this slot (if any)
		if (label.isAboveBackbone())
		{
			for (int slot = 1; slot < labelSlots.getTopSlot() && minZoomForSlots > 0 && minBucketValue > 0; slot++)
			{
				int currBucketValue;
				
				labelsPositioner.placeInSlot(slot, label);
				SequencePoint workingPinnedPoint = label.getPinnedPoint();
				Dimension2D workingDimension = label.getDimension();
				
				labelZoom = this.calculateLabelZoomLevel(slot, workingPinnedPoint, workingDimension, labelSlotBounds.getBoundsList(slot), backbone, maxScale, minZoomForSlots);
				
				if (labelZoom < minZoomForSlots)
				{
					minZoomForSlots = labelZoom;
					
					LabelPosition currLabelPosition = new LabelPosition((SequencePoint)label.getPinnedPoint().clone(), label);
					currLabelPosition.setSlotRegion(slotRegion);
					
					currBucketValue = labelBuckets.getBucketToAddTo(labelZoom);
				
					if (currBucketValue < minBucketValue)
					{
						minBucketValue = currBucketValue;
						
						labelBuckets.addToBucket(currLabelPosition, currBucketValue);
						SequenceLabelBounds labelBounds = new SequenceLabelBounds((SequencePoint)workingPinnedPoint.clone(), (Dimension2D)workingDimension.clone(), 
								minZoomForSlots, maxScale);
						labelSlotBounds.addLabelBoundsTo(labelBounds, slot);
					}
				}
			}
		}
		else // bottom slots
		{
			for (int slot = -1; slot > labelSlots.getBottomSlot() && minZoomForSlots > 0; slot--)
			{
				int currBucketValue;
				
				labelsPositioner.placeInSlot(slot, label);
				SequencePoint workingPinnedPoint = label.getPinnedPoint();
				Dimension2D workingDimension = label.getDimension();
				
				labelZoom = this.calculateLabelZoomLevel(slot, workingPinnedPoint, workingDimension, labelSlotBounds.getBoundsList(slot), backbone, maxScale, minZoomForSlots);
				
				if (labelZoom < minZoomForSlots)
				{
					minZoomForSlots = labelZoom;
					
					LabelPosition currLabelPosition = new LabelPosition((SequencePoint)label.getPinnedPoint().clone(), label);
					currLabelPosition.setSlotRegion(slotRegion);
					
					currBucketValue = labelBuckets.getBucketToAddTo(labelZoom);
				
					if (currBucketValue < minBucketValue)
					{
						minBucketValue = currBucketValue;
						
						labelBuckets.addToBucket(currLabelPosition, currBucketValue);
						SequenceLabelBounds labelBounds = new SequenceLabelBounds((SequencePoint)workingPinnedPoint.clone(), (Dimension2D)workingDimension.clone(), 
								minZoomForSlots, maxScale);
						labelSlotBounds.addLabelBoundsTo(labelBounds, slot);
					}
				}
			}
		}
	}
	
	private void initialPositionLabels()
	{	
		// calculate where labels should be turned on/off
		placeLabelsInBuckets(allLabels);
		
		// add all labels to displayed labels/add to labels layer
		for (Label label : allLabels)
		{
			labelTextLayer.add(label.getLabelTextLayer());
			labelLinesLayer.add(label.getLabelLineLayer());
			label.turnOffLabel(); // turn off label initially
		}		
		// turn on/off labels depending on scale
		calculateDisplayedLabels(prevScale);
	}

	/* (non-Javadoc)
	 * @see gview.managers.labels.LabelsManagerInt#getLabelsLayer()
	 */
	public MapComponent getLabelsLayer()
	{
		return labelsLayer;
	}
	
	private void calculateDisplayedLabels(double scale)
	{
		labelBuckets.toNewScale(scale);
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
	
	//			calculateDisplayedLabels(backbone.getScale());
				
				for (Label label : allLabels)
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
			
			double slotHeight = this.findLargestLabelHeight(allLabels);
			
			labelSlots = new LabelSlots(slotRegion.getSlotTranslator(), MAX_SLOTS, 15, slotHeight, 5);
			
			labelBuckets = new LabelBuckets(slotRegion.getResolutionManager(), slotRegion.getBackbone().getMaxScale());

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
