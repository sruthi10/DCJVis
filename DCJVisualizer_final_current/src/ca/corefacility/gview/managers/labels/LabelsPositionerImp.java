package ca.corefacility.gview.managers.labels;


import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.biojava.bio.seq.Feature;

import ca.corefacility.gview.layout.prototype.SequencePoint;
import ca.corefacility.gview.layout.prototype.SequencePointImp;
import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.layout.sequence.SlotRegion;
import ca.corefacility.gview.layout.sequence.circular.BackboneCircular.ClashShiftException;
import ca.corefacility.gview.map.items.BackboneLabelItem;
import ca.corefacility.gview.style.datastyle.LabelStyle;

@SuppressWarnings("unused")
public class LabelsPositionerImp extends LabelsPositioner
{
	private LabelSlots labelSlots;

	private static final int INITIAL_MAX_LABELS = 30;

	public LabelsPositionerImp(SlotRegion slotRegion, LabelSlots labelSlots)
	{
		super(slotRegion);
		
		this.labelSlots = labelSlots;
	}

	// returns labels that were placed on screen
	private List<Label> placeLabelsIn(List<Label> labelsToPlace, double scale)
	{
		int numberPlacedLabels = 0;
		List<Label> removedLabels = null;

		List<Label> labelsPlaced = new ArrayList<Label>();
		
		List<Label> labelsPlacedAbove = new LinkedList<Label>();
		List<Label> labelsPlacedBelow = new LinkedList<Label>();

		Iterator<Label> placingIter = labelsToPlace.iterator();
		// place labels
		while (placingIter.hasNext() && numberPlacedLabels < (INITIAL_MAX_LABELS * scale))
		{
			Label label = placingIter.next();

			// make sure labels placed are not outside of slot for labels

			if (label.isAboveBackbone())
			{
				positionLabel(label, 1);
				labelsPlacedAbove.add(label);
			}
			else
			{
				positionLabel(label, -1);
				labelsPlacedBelow.add(label);
			}
			labelsPlaced.add(label);

			numberPlacedLabels++;
		}

		// make sure labels don't intersect the map (above backbone)
		for (Label label : labelsPlacedAbove)
		{
			BackboneLabelItem labelItem = label.getLabelTextItem();
			Rectangle2D bounds = labelItem.getFullBounds();

			try
			{
				double heightLimitFromBackbone = slotRegion.getSlotTranslator().getTopMostHeight();
				
				// if no tick mark labels, then insert a "buffer" area so labels aren't right up against tick marks 
				heightLimitFromBackbone += (slotRegion.getSlotTranslator().getTopTickThickness() > 0) ?
						slotRegion.getSlotTranslator().getTopTickThickness() : 20.0f;
						
				double heightAdjust = 0;
				heightAdjust = slotRegion.getBackbone().calculateClashShift(labelItem.getPinnedPoint(),
						heightLimitFromBackbone, bounds.getWidth(), bounds.getHeight(), Backbone.ShiftDirection.ABOVE);

				label.translate(0, heightAdjust, slotRegion.getSequencePath());
			}
			catch (ClashShiftException e)
			{
				System.err.println(e);
			}
		}
		
		// make sure labels don't intersect the map (below backbone)
		for (Label label : labelsPlacedBelow)
		{
			BackboneLabelItem labelItem = label.getLabelTextItem();
			Rectangle2D bounds = labelItem.getFullBounds();

			try
			{
				double heightLimitFromBackbone = slotRegion.getSlotTranslator().getBottomMostHeight() ;
				
				// if no tick mark labels, then insert a "buffer" area so labels aren't right up against tick marks 
				heightLimitFromBackbone -= (slotRegion.getSlotTranslator().getBottomTickThickness() > 0)
					? slotRegion.getSlotTranslator().getBottomTickThickness() : 20.0f;
					
				double heightAdjust = 0;
				heightAdjust = slotRegion.getBackbone().calculateClashShift(labelItem.getPinnedPoint(),
						heightLimitFromBackbone, bounds.getWidth(), bounds.getHeight(), Backbone.ShiftDirection.BELOW);

				label.translate(0, heightAdjust, slotRegion.getSequencePath());
			}
			catch (ClashShiftException e)
			{
				//System.err.println(e);
			}
		}

		// attempt to fan out labels (above backbone)
		removedLabels = fanLabels(labelsPlacedAbove);

		if (removedLabels != null)
		{
			labelsPlaced.removeAll(removedLabels);
		}
		
		// attempt to fan out labels (below backbone)
		removedLabels = fanLabels(labelsPlacedBelow);

		if (removedLabels != null)
		{
			labelsPlaced.removeAll(removedLabels);
		}

		return labelsPlaced;
	}

	// attempt to re-position labels/add new labels if necessary
	@Override
	public List<Label> positionLabels(List<Label> labels, double scale)
	{
		List<Label> activatedLabels;

		// go through undisplayed labels, placing them in initial positions
		List<Label> undisplayed = labels;
		Collections.shuffle(undisplayed);

		activatedLabels = placeLabelsIn(undisplayed, scale);
		
		return activatedLabels;
	}

	private void addNewLabels()
	{
		// iterate through remaining labels, ordered by rank
		// attempt to fit in a certain number of new labels, up to some maxium value
		int maxLabels = 100;
		int labelsAdded = 0;

		// Iterator remainingLabels;

	}

	@Override
	public void setSlotRegion(SlotRegion slotRegion, LabelSlots labelSlots)
	{
		this.slotRegion = slotRegion;
		this.labelSlots = labelSlots;
	}

	// initial build of label within label slot
	private void positionLabel(Label label, int slot)
	{
		// final double linePadding = 5;
		final double labelPadding = 5;

		// used to calculate position of label
		double lineStart;
		// double lineCenter;
		double labelStart;
		double midBase;

		if (label == null)
		{
			return;
		}
		else if (label.getLabelTextLayer() == null || label.getLabelLineLayer() == null)
		{
			return;
		}
		else if (label.getLabelTextItem().getLabeledFeature() == null)
		{
			return;
		}
		else if (label.getLabelTextItem().getLabeledFeature().getFeature() == null)
		{
			return;
		}

		Feature feature = label.getLabeledFeature();
		LabelStyle style = label.getLabelStyle();

		// TODO this is a crude way to create labels, probably want to only add one thing (feature)
		// as a listener, and have label a subitem
		midBase = (feature.getLocation().getMin() + feature.getLocation().getMax()) / 2.0;

		if (slot > 0)
		{
			lineStart = labelSlots.getClosestTopHeight();
			labelStart = labelSlots.getSlotHeight(slot);
		}
		else
		{
			lineStart = labelSlots.getClosestBottomHeight();
			labelStart = labelSlots.getSlotHeight(slot);
		}

		SequencePoint labelPoint = new SequencePointImp(midBase, labelStart);
		SequencePoint lineStartPoint = new SequencePointImp(midBase, lineStart);

		label.setLabelPositions(lineStartPoint, labelPoint, slotRegion.getSequencePath());
	}

	@Override
	public void placeInSlot(int labelSlot, Label label)
	{
		// TODO change this around/refactor later
		try
		{
			double heightLimit = labelSlots.getSlotHeight(labelSlot);
			
			SequencePoint labelPoint = label.getPinnedPoint();
			Dimension2D dimension = label.getDimension();
			
			if (labelPoint == null)
			{
				positionLabel(label, labelSlot);
				labelPoint = label.getPinnedPoint();
				dimension = label.getDimension();
//				System.out.println("inital postition : pinnedPoint=" + labelPoint);
			}
			
			Backbone.ShiftDirection direction = (labelSlot > 0) ? Backbone.ShiftDirection.ABOVE : Backbone.ShiftDirection.BELOW;

			double height = slotRegion.getBackbone().calculateClashShift(labelPoint, heightLimit, dimension.getWidth(), dimension.getHeight(),
					direction);

			label.translate(0, height, slotRegion.getSequencePath());
		}
		catch (ClashShiftException e)
		{
//			System.err.println(e);
		}
	}

	// checks to see if passed label intersects the passed set of labels, returns the intersected
	// label if so
	private Label getIntersectedLabel(Label label, Iterator<Label> labelsIterator)
	{
		while (labelsIterator.hasNext())
		{
			Object o = labelsIterator.next();
			if (o instanceof Label)
			{
				Label currLabel = (Label) o;
				if (currLabel == label)
				{
					continue;
				}

				BackboneLabelItem currLabelItem = currLabel.getLabelTextItem();

				if (currLabelItem.getGlobalBounds().intersects(label.getLabelTextItem().getGlobalBounds()))
				{
					return currLabel;
				}
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	private List<Label> fanLabels(List<Label> labels)
	{
		List<Label> removedLabels = null;

		// spread out by bases
		Collections.sort(labels, new Label.BaseComparator());
		fanLabelsBaseInDirection(labels, 10, 0);

		// spread out by height
		Collections.sort(labels, new Label.HeightComparator());
		Collections.reverse(labels);
		removedLabels = fanLabelsHeightInDirection(labels, 0, 2);

		return removedLabels;
	}

	// base/heightShift, positive is outwards, negative is inwards
	private List<Label> fanLabelsHeightInDirection(List<Label> labels, double baseShift, double heightShift)
	{
		List<Label> removedLabels = null;

		final int maxIterations = 20;

		boolean clashes = true;

		for (int numberIterations = 0; numberIterations < maxIterations && clashes; numberIterations++)
		{
			clashes = false;
			Iterator<Label> labelsIter = labels.iterator();

			while (labelsIter.hasNext())
			{
				Label currLabel = labelsIter.next();

				// check for intersections
				Label intersectLabel = getIntersectedLabel(currLabel, labels.iterator());

				if (intersectLabel != null)
				{
					clashes = true;

					// fan out currLabel
					if (currLabel.getPinnedPoint().getHeightFromBackbone() < 0)
					{
						shiftLabel(currLabel, baseShift, -heightShift);
					}
					else
					{
						shiftLabel(currLabel, baseShift, heightShift);
					}
				}
			}
		}

		if (clashes)
		{
			removedLabels = new LinkedList<Label>();
		}

		while (clashes) // if there still were clashing labels, find and remove them
		{
			clashes = false;
			Iterator<Label> labelsIter = labels.iterator();

			while (labelsIter.hasNext())
			{
				Label currLabel = labelsIter.next();

				if (removedLabels.contains(currLabel))
				{
					continue;
				}

				// check for intersections
				for (Label checkLabel : labels)
				{
					if (currLabel == checkLabel)
					{
						continue;
					}
					else if (removedLabels.contains(checkLabel))
					{
						continue;
					}

					BackboneLabelItem currLabelItem = currLabel.getLabelTextItem();

					if (currLabelItem.getGlobalBounds().intersects(checkLabel.getLabelTextItem().getGlobalBounds()))
					{
						clashes = true;

						// remove one of clashed labels
						removedLabels.add(checkLabel);
					}
				}
			}
		}

		return removedLabels;
	}

	// returns list of labels which were removed
	// list of labels it accepts is assumed to be sorted in increasing order by base pair the label
	// is pinned to
	private List<Label> fanLabelsBaseInDirection(List<Label> labels, double baseShift, double heightShift)
	{
		List<Label> removedLabels = null;

		final int maxIterations = 20;

		boolean clashes = true;

		for (int numberIterations = 0; numberIterations < maxIterations && clashes; numberIterations++)
		{
			clashes = false;
			Iterator<Label> labelsIter = labels.iterator();

			Label currLabel = null;
			Label prevLabel = null;
			Label firstLabel = null;

			if (labelsIter.hasNext())
			{
				currLabel = labelsIter.next();
				firstLabel = currLabel;
			}

			while (labelsIter.hasNext())
			{
				prevLabel = currLabel;
				currLabel = labelsIter.next();

				if (prevLabel.getBounds().intersects(currLabel.getBounds()))
				{
					clashes = true;

					// fan out prevLabel
					shiftLabel(prevLabel, -baseShift, 0);

					// fan out currLabel
					if (currLabel.getPinnedPoint().getHeightFromBackbone() < 0)
					{
						shiftLabel(currLabel, baseShift, -heightShift);
					}
					else
					{
						shiftLabel(currLabel, baseShift, heightShift);
					}
				}
			}

			// if more than one label in list
			// check first label with last label
			if (prevLabel != null)
			{
				prevLabel = currLabel;
				currLabel = firstLabel;
				if (prevLabel.getBounds().intersects(currLabel.getBounds()))
				{
					clashes = true;

					// fan out prevLabel
					shiftLabel(prevLabel, -baseShift, 0);

					// fan out currLabel
					shiftLabel(currLabel, baseShift, heightShift);
				}
			}
		}

		if (clashes) // if there still were clashing labels, find and remove them
		{
			removedLabels = new ArrayList<Label>();
			Iterator<Label> labelsIter = labels.iterator();

			Label currLabel = null;
			Label prevLabel = null;
			Label firstLabel = null;

			if (labelsIter.hasNext())
			{
				currLabel = labelsIter.next();
				firstLabel = currLabel;
			}

			while (labelsIter.hasNext())
			{
				prevLabel = currLabel;
				currLabel = labelsIter.next();

				if (prevLabel.getBounds().intersects(currLabel.getBounds()))
				{
					removedLabels.add(prevLabel);
				}
			}

			if (prevLabel != null)
			{
				prevLabel = currLabel;
				currLabel = firstLabel;
				if (prevLabel.getBounds().intersects(currLabel.getBounds()))
				{
					removedLabels.add(prevLabel);
				}
			}
		}

		return removedLabels;
	}

	private void shiftLabel(Label label, double baseShift, double heightShift)
	{
		// fan out prevLabel
		// TODO this is very poor method, should check into this later
		// double actualBaseShift = slotRegion.getBackbone().getSpannedBases(baseShift);

		label.translate(baseShift, heightShift, slotRegion.getSequencePath());
	}
}
