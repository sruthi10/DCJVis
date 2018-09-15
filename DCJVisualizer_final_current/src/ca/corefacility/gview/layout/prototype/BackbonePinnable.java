package ca.corefacility.gview.layout.prototype;

import ca.corefacility.gview.map.event.GViewEventListener;

/**
 * Describes something that can be "pinned" to a particular location on the backbone, so that it always stays there no matter the zoom level.
 * @author Aaron Petkau
 *
 */
public interface BackbonePinnable extends GViewEventListener
{
	/**
	 * Pins this item to the passed point on the passed backbone.
	 * 
	 * @param pinnedPoint  The point where we should pin the item.
	 */
	public void pinTo(SequencePoint pinnedPoint);
	
	/**
	 * Translates this pinnable item by the passed base/height values.
	 * @param base  The base to translate by.
	 * @param heightFromBackbone  The height to translate by.
	 */
	public void translate(double base, double heightFromBackbone);
	
	/**
	 * @return  The point we have pinned this item to.
	 */
	public SequencePoint getPinnedPoint();
}