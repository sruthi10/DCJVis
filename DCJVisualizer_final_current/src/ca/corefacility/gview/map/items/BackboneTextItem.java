package ca.corefacility.gview.map.items;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import ca.corefacility.gview.layout.prototype.BackbonePinnable;
import ca.corefacility.gview.layout.sequence.Backbone;


/**
 * Describes a Text item that is to be pinned to the backbone.
 * @author aaron
 *
 */
public interface BackboneTextItem extends TextItem, BackbonePinnable, BackboneItem
{
	/**
	 * @return  The bounds of this textItem in the global coordinate system.
	 */
	public Rectangle2D getTextBounds();
	
	
	public void setBackbone(Backbone backbone);


	Dimension2D getDimension();
}
