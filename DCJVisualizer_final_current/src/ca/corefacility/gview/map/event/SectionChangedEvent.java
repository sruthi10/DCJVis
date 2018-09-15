package ca.corefacility.gview.map.event;


import java.util.List;

import ca.corefacility.gview.managers.SectionID;
import ca.corefacility.gview.managers.SectionManager;

/**
 * An event that describes when the view has changed which sections are being displayed.
 * @author aaron
 *
 */
public class SectionChangedEvent extends GViewEvent
{
	private static final long serialVersionUID = 3649276543083643241L;
	private List<SectionID> currentDisplayedSections;
	private SectionManager sectionManager;

	/**
	 * Creates a new SectionChangedEvent.
	 * @param source  The object on which this event initally occured.
	 * @param currentDisplayedSections  A list of the currently displayed sections on the map.
	 * @param sectionManager  The SectionManager for the sections.
	 */
	public SectionChangedEvent(Object source, List<SectionID> currentDisplayedSections, SectionManager sectionManager)
	{
		super(source);

		this.currentDisplayedSections = currentDisplayedSections;
		this.sectionManager = sectionManager;
	}

	/**
	 * Gets the list of currently displayed sections.
	 * @return  A list of currently displayed sections.
	 */
	public List<SectionID> getCurrentDisplayedSections()
	{
		return currentDisplayedSections;
	}

	/**
	 * Gets the current section manager.
	 * @return  The current SectionManager.
	 */
	public SectionManager getSectionManager()
	{
		return sectionManager;
	}
}