package ca.corefacility.gview.layout.feature;


import java.awt.Shape;
import java.util.Iterator;

import org.biojava.bio.symbol.Location;

import ca.corefacility.gview.layout.Direction;
import ca.corefacility.gview.layout.prototype.BackboneShape;
import ca.corefacility.gview.layout.prototype.CompositeShape;
import ca.corefacility.gview.layout.sequence.SequencePath;
import ca.corefacility.gview.layout.sequence.SlotPath;

/**
 * An abstract class containing common code for making arrow shapes.
 * @author Aaron Petkau
 *
 */
public abstract class AbstractArrowShapeRealizer extends FeatureShapeRealizer
{
	protected final double ARROW_LENGTH;
	
	/**
	 * Constructs an arrow shape realizer with the arrow head taking up the passed length.
	 * @param arrowLength  The length of the arrow head.
	 */
	public AbstractArrowShapeRealizer(double arrowLength)
	{
		ARROW_LENGTH = arrowLength;
	}
	
	/**
	 * Constructs an arrow shape realizer with the default arrow length.
	 */
	public AbstractArrowShapeRealizer()
	{
		this(5);
	}
	
	protected Shape realizeFeatureShape(SlotPath path, Location trueLocation,
			double top, double bottom, int sequenceLength)
	{
		BackboneShape shape = null;
		
		//NON-CONTIGUOUS
		if(!trueLocation.isContiguous())
		{
			Location arrowLocation = getArrowLocation(trueLocation);
			
			shape = buildNonContiguousShape(false, trueLocation, path, top, bottom, sequenceLength);
			shape = createCompositeArrowHead(trueLocation, arrowLocation, sequenceLength, top, bottom, path, shape);
		}
		else
		{
			//Handles Point and Non-Point
			boolean point = handleSingleBlock(path, trueLocation, sequenceLength, top, bottom);

			//CONTIGUOUS NON-POINT
			if(!point)
			{
				shape = createCompositeArrowHead(trueLocation, trueLocation, sequenceLength, top, bottom, path, path.getShape());
			}
			else
			{
				shape = path.getShape();
			}
		}
		
		return shape;
	}
	
	/**
	 * Creates a non-contiguous shape, with or without the reduced point arrow.
	 * 
	 * @param createArrowHead
	 * @param fullLocation
	 * @param path
	 * @param top
	 * @param bottom
	 * @param sequenceLength
	 * @return The non-contiguous shape.
	 */
	protected BackboneShape buildNonContiguousShape(boolean createArrowHead, Location fullLocation, SlotPath path, double top, double bottom, int sequenceLength)
	{
		Location arrowLocation = getArrowLocation(fullLocation);
		
		float centerBase = findCenterBase(arrowLocation.getMin(), arrowLocation.getMax(), sequenceLength);
		
	    boolean finishedFirstBlock = false;
		Iterator blocks = fullLocation.blockIterator();
		
		while (blocks.hasNext())
		{
			Location block = (Location)blocks.next();
			int start = extractStart(block);
			int stop = extractStop(block);
							
		    if (!finishedFirstBlock)
		    {
		    	if(createArrowHead && arrowLocation.equals(block))
		    	{
		    		createArrowHead(path, arrowLocation.getMin(), centerBase, arrowLocation.getMax(), top, bottom);
		    	}
		    	else
		    	{
		    		createFirstBlock(path, start, stop, top, bottom, sequenceLength);
		    	}
                
                finishedFirstBlock = true;
		    }
		    else
		    {
				// if we are at the last block
				if (blocks.hasNext())
				{
			    	if(createArrowHead && arrowLocation.equals(block))
			    	{
			    		createArrowHead(path, arrowLocation.getMin(), centerBase, arrowLocation.getMax(), top, bottom);
			    	}
			    	else
			    	{
			    		createMiddleBlock(path, start, stop, top, bottom, sequenceLength);
			    	}
				}
				else
				{
			    	if(createArrowHead && arrowLocation.equals(block))
			    	{
			    		createArrowHead(path, arrowLocation.getMin(), centerBase, arrowLocation.getMax(), top, bottom);
			    	}
			    	else
			    	{
			    		createLastBlock(path, start, stop, top, bottom, sequenceLength);
			    	}
				}
		    }
		}
		
		return path.getShape();
	}
	
	/**
	 * 
	 * This methods builds the composite arrow head shape, which allows for switching back and 
	 * forth between a single point arrow or an extended arrow at different zoom levels.
	 * 
	 * @param fullLocation
	 * @param arrowLocation
	 * @param sequenceLength
	 * @param top
	 * @param bottom
	 * @param path
	 * @param shape
	 * @return A BackboneShape object containing the composite arrow.
	 */
	private BackboneShape createCompositeArrowHead(Location fullLocation, Location arrowLocation, int sequenceLength, double top, double bottom, SlotPath path, BackboneShape shape)
	{
		path.clear();
		
		if(!fullLocation.isContiguous())
		{
			buildNonContiguousShape(true, fullLocation, path, top, bottom, sequenceLength);
		}
		else
		{
			float centerBase = findCenterBase(arrowLocation.getMin(), arrowLocation.getMax(), sequenceLength);
			createArrowHead(path, arrowLocation.getMin(), centerBase, arrowLocation.getMax(), top, bottom);
		}
		
		int locationLength = extractMinMaxLength(arrowLocation, sequenceLength);
		
		BackboneShape result = new CompositeShape(path.getShape(), shape, path.getBackbone().findZoomForLength(locationLength,
				ARROW_LENGTH), path.getBackbone().getScale());
		
		return result;
	}
	
	private boolean handleSingleBlock(SlotPath path, Location trueLocation, int sequenceLength, double top, double bottom)
	{
        int start = extractStart(trueLocation);
        int stop = extractStop(trueLocation);
        float centerBase = findCenterBase(start, stop, sequenceLength);
        boolean pointLocation = false;

        if (start != stop)
        {
            createArrowBlock(path, start, centerBase, stop, top, bottom);
        }
        else
        {
            createArrowHead(path, start, centerBase, stop, top, bottom);
            pointLocation = true;
        }
        
        return pointLocation;
	}
	
    /**
     * Method to override to create the first block for non-contiguous regions.
     * @param path  The slot path to draw onto.
     * @param start  The start base for this block.
     * @param stop  The stop base for this block.
     * @param top  The top height for this block, (in range -1.0, to 1.0 defining bottom/top in slot).
     * @param bottom  The bottom height for this block, (in range -1.0, to 1.0 defining bottom/top in slot).
     * @param sequenceLength  The sequence length.
     */
    protected abstract void createFirstBlock(SlotPath path, int start, int stop, double top, double bottom, int sequenceLength);
    
    /**
     * Method to override to create middle block for non-contiguous regions.
     * @param path  The slot path to draw onto.
     * @param start  The start base for this block.
     * @param stop  The stop base for this block.
     * @param top  The top height for this block, (in range -1.0, to 1.0 defining bottom/top in slot).
     * @param bottom  The bottom height for this block, (in range -1.0, to 1.0 defining bottom/top in slot).
     * @param sequenceLength  The sequence length.
     */
    protected abstract void createMiddleBlock(SlotPath path, int start, int stop, double top, double bottom, int sequenceLength);
    
    /**
     * Method to override to create last block for non-contiguous regions.
     * @param path  The slot path to draw onto.
     * @param start  The start base for this block.
     * @param stop  The stop base for this block.
     * @param top  The top height for this block, (in range -1.0, to 1.0 defining bottom/top in slot).
     * @param bottom  The bottom height for this block, (in range -1.0, to 1.0 defining bottom/top in slot).
     * @param sequenceLength  The sequence length.
     */
    protected abstract void createLastBlock(SlotPath path, int start, int stop, double top, double bottom, int sequenceLength);
	
	/**
	 * Method to override to create the arrow block part of the shape.  (Arrow block is the part of the feature which has the arrow head attached).
	 * @param path  The slot path to draw onto.
	 * @param start  The start base for this arrow block.
	 * @param center
	 * @param stop  The stop base for this block.
	 * @param top  The top height for this block, (in range -1.0, to 1.0 defining bottom/top in slot).
	 * @param bottom  The bottom height for this block, (in range -1.0, to 1.0 defining bottom/top in slot).
	 */
	protected abstract void createArrowBlock(SlotPath path, int start, float center, int stop, double top, double bottom);
	
	/**
	 * Method to override to create the arrow head part of the shape.  (Arrow head is the part of the feature that is only the arrow head (displayed at low resolutions)).
	 * @param path  The slot path to draw onto.
	 * @param start  The start base for this arrow head.
	 * @param center
	 * @param stop  The stop base for this head.
	 * @param top  The top height for this head, (in range -1.0, to 1.0 defining bottom/top in slot).
	 * @param bottom  The bottom height for this head, (in range -1.0, to 1.0 defining bottom/top in slot).
	 */
	protected abstract void createArrowHead(SlotPath path, int start, float center, int stop, double top, double bottom);
	
	/**
	 * A convience method used to create a standard (no arrow heads etc) block.
	 * @param path  The slot path to draw onto.
	 * @param start  The start based.
	 * @param stop  The stop base.
	 * @param top  The top height, (in range -1.0, to 1.0 defining bottom/top in slot).
	 * @param bottom  The bottom height, (in range -1.0, to 1.0 defining bottom/top in slot).
	 */
	protected void createStandardBlock(SlotPath path, int start, int stop, double top, double bottom, double heightDecrease)
	{
		path.moveTo(start, top*heightDecrease, SequencePath.Orientation.TOP);
		path.lineTo(stop, Direction.INCREASING, SequencePath.Orientation.TOP);
		path.lineTo(stop, bottom, Direction.NONE, SequencePath.Orientation.NONE);
		path.lineTo(start*heightDecrease, Direction.DECREASING, SequencePath.Orientation.BOTTOM);
		path.closePath();
	}
	
	/**
	 * A method used to determine which location would have the arrow.
	 * 
	 * @param fullLocation The entire location.
	 * @return The arrow location, a single block location within the full location.
	 */
	protected abstract Location getArrowLocation(Location fullLocation);
}
