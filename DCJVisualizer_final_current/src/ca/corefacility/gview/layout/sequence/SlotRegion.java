package ca.corefacility.gview.layout.sequence;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import ca.corefacility.gview.managers.ResolutionManager;
import ca.corefacility.gview.map.event.GViewEventSubject;


/**
 * Provides access to objects that can layout shapes on certain regions.
 * @author Aaron Petkau
 *
 */
public interface SlotRegion extends GViewEventSubject
{
	public SlotPath getSlotPath(int slot);
	
	/**
	 * @return  The translator used to translate (slot, height) pair values into heights above backbone.
	 */
	public SlotTranslator getSlotTranslator(); // When ready, remove this method from here (possibly), since SlotTranslator has nothing to do with Layout
	public Backbone getBackbone(); // TODO should I allow this.  In need to have access to backbone in many different places
	
	public SequencePath getSequencePath();

	public ResolutionManager getResolutionManager();

	/**
	 * @return  The center point of the slot region.
	 */
	public Point2D getCenter();
	
	/**
	 * Gives the bounding box around this slot region, depending on the zoom level.
	 * @return  The bounding box around this slot region.
	 */
	public Rectangle2D getCurrentBounds();
}
