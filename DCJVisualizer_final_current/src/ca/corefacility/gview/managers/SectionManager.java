package ca.corefacility.gview.managers;


import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.layout.sequence.BaseRange;
import ca.corefacility.gview.layout.sequence.SequenceRectangle;
import ca.corefacility.gview.layout.sequence.SlotRegion;
import ca.corefacility.gview.map.event.BackboneZoomEvent;
import ca.corefacility.gview.map.event.DisplayUpdated;
import ca.corefacility.gview.map.event.GViewEvent;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.event.GViewEventSubject;
import ca.corefacility.gview.map.event.GViewEventSubjectImp;
import ca.corefacility.gview.map.event.LayoutChangedEvent;
import ca.corefacility.gview.map.event.ResolutionSwitchEvent;
import ca.corefacility.gview.map.event.SectionChangedEvent;

/**
 * Divide map up into sections, and manage/display each section individually.
 * 
 * @author Aaron Petkau
 * 
 */
public class SectionManager implements GViewEventListener, GViewEventSubject
{
	private GViewEventSubjectImp eventSubject;
	private int sequenceLength;

	private int totalSections;
	private double basesPerSection;
	private int initialNumberOfSections;

	private int sectionLevel;

	private Rectangle2D previousView;

	private Backbone backbone;

	public SectionManager(int sequenceLength)
	{
		this(sequenceLength, 4);
	}

	public SectionManager(int sequenceLength, int initialNumberOfSections, Backbone backbone)
	{
		eventSubject = new GViewEventSubjectImp();
		this.sequenceLength = sequenceLength;

		this.initialNumberOfSections = initialNumberOfSections;
		totalSections = initialNumberOfSections;
		sectionLevel = 0;

		previousView = new Rectangle2D.Double(0, 0, 0, 0);

		this.backbone = backbone;

		recalculateSections(sectionLevel);
	}

	public SectionManager(int sequenceLength, int initialNumberOfSections)
	{
		this(sequenceLength, initialNumberOfSections, null);
	}

	public void eventOccured(GViewEvent event)
	{
		if (event instanceof DisplayUpdated)
		{
			DisplayUpdated displayUpdatedEvent = (DisplayUpdated) event;

			this.previousView = displayUpdatedEvent.getViewBounds();

			List<SectionID> overlappingSections = findOverlappingSections(displayUpdatedEvent.getViewBounds());

			if (overlappingSections != null)
			{
				// **//
//				System.out.print("sectionLevel=" + sectionLevel + ", overLappingSections=[");
//				for (SectionID id : overlappingSections)
//				{
//					System.out.print(id.getSectionNumber() + ",");
//				}
//				System.out.println("], totalSections=" + totalSections);
				// **//

				// send currently activated (currnet overlapping sections)
				eventSubject.fireEvent(new SectionChangedEvent(this, overlappingSections, this));
			}
		}
		else if (event instanceof BackboneZoomEvent)
		{
			// BackboneZoomEvent zoomEvent = (BackboneZoomEvent)event;
		}
		else if (event instanceof ResolutionSwitchEvent)
		{
			ResolutionSwitchEvent switchEvent = (ResolutionSwitchEvent) event;

			if (switchEvent.getDirection() == ResolutionManager.Direction.NONE)
			{
				return;
			}
			else if (switchEvent.getDirection() == ResolutionManager.Direction.INCREASE)
			{
				incrementSectionLevel();
			}
			else
			{
				decrementSectionLevel();
			}

			List<SectionID> overlappingSections = findOverlappingSections(previousView);
			eventSubject.fireEvent(new SectionChangedEvent(this, overlappingSections, this));
		}
		else if (event instanceof LayoutChangedEvent)
		{
			SlotRegion slotRegion = ((LayoutChangedEvent) event).getSlotRegion();

			slotRegion.addEventListener(this); // make sure we listen for events from new backbone

			this.backbone = slotRegion.getBackbone();
		}
	}

	private void incrementSectionLevel()
	{
		sectionLevel++;
		recalculateSections(sectionLevel);
	}

	private void decrementSectionLevel()
	{
		if (sectionLevel > 1)
		{
			sectionLevel--;
			recalculateSections(sectionLevel);
		}
	}

	/**
	 * Converts the passed number to a number within the range [min,max] (if it is outside the
	 * bounds, it becomes equal to the bounds).
	 * 
	 * @param number
	 *            The number to convert.
	 * @param min
	 *            The minimum value the passed number should be.
	 * @param max
	 *            The maximum value the passed number should be.
	 * @return A corresponding number, within the range [min, max].
	 */
	private int numberToRange(int number, int min, int max)
	{
		number = Math.max(min, number);
		number = Math.min(max, number);

		return number;
	}

	private List<SectionID> findOverlappingSections(Rectangle2D display)
	{
		List<SectionID> sectionList = null;

		// TODO redo this method so it's more accurate
		SequenceRectangle sequenceRectangle = backbone.getSpannedRectangle(display);

		// TODO take into account HeightRange (important for circular)
		if (sequenceRectangle != null)
		{
			BaseRange baseRange = sequenceRectangle.getBaseRange();

			sectionList = toSectionRange(baseRange);
		}

		return sectionList;
	}

	private void recalculateSections(int sectionLevel)
	{
		final double maxSections = (double) sequenceLength / 5;

		// TODO change the value 2 here, link up with value from ResolutionSwitcher
		totalSections = (int) Math.pow(2, sectionLevel) * initialNumberOfSections;
		totalSections = (int) Math.min(totalSections, maxSections);

		basesPerSection = sequenceLength / (double) totalSections;
	}

	public void addEventListener(GViewEventListener listener)
	{
		eventSubject.addEventListener(listener);
	}

	public void removeAllEventListeners()
	{
		eventSubject.removeAllEventListeners();
	}

	public void removeEventListener(GViewEventListener listener)
	{
		eventSubject.removeEventListener(listener);
	}

	/**
	 * Converts the passed base range to a section range.
	 * 
	 * @param baseRange
	 *            The base range to convert.
	 * @return A list of section ids which intersect the passed base range, or null if base range is
	 *         null.
	 */
	public List<SectionID> toSectionRange(BaseRange baseRange)
	{
		if (baseRange == null)
			return null;

		List<SectionID> sectionList = new LinkedList<SectionID>();

		// if one side overlaps [0,sequenceLength], then we should calculate the sections displayed.
		if (!((baseRange.getStartBase() < 0 && baseRange.getEndBase() < 0) || (baseRange.getStartBase() > sequenceLength && baseRange.getEndBase() > sequenceLength)))
		{
			int beginSectionNumber = (int) Math.floor(baseRange.getStartBase() / basesPerSection);
			beginSectionNumber = numberToRange(beginSectionNumber, 0, totalSections - 1);
			int endSectionNumber = (int) Math.floor(baseRange.getEndBase() / basesPerSection);
			endSectionNumber = numberToRange(endSectionNumber, 0, totalSections - 1);

			// section spans 0-base gap
			if (beginSectionNumber > endSectionNumber)
			{
				// handle part up to 0-base
				for (int section = beginSectionNumber; section <= totalSections - 1; section++)
				{
					sectionList.add(new SectionID(sectionLevel, section, (section == totalSections - 1)));
				}

				// handle part past 0-base
				for (int section = 0; section <= endSectionNumber; section++)
				{
					sectionList.add(new SectionID(sectionLevel, section, (section == totalSections - 1)));
				}
			}

			for (int section = beginSectionNumber; section <= endSectionNumber; section++)
			{
				sectionList.add(new SectionID(sectionLevel, section, (section == totalSections - 1)));
			}
		}

		return sectionList;
	}

	public BaseRange toBaseRange(SectionID section)
	{
		if (section == null)
			return null;

		double startBase = (section.getSectionNumber() * basesPerSection);
		double endBase = ((section.getSectionNumber() + 1) * basesPerSection); // start of next
		// section

		return new BaseRange(startBase, endBase);
	}
	
	public int getSectionLevel()
	{
		return sectionLevel;
	}

	public int getTotalSections()
	{
		return totalSections;
	}
}
