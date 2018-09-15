package ca.corefacility.gview.layout.feature;

import java.util.Iterator;

import org.biojava.bio.symbol.Location;

import ca.corefacility.gview.layout.Direction;
import ca.corefacility.gview.layout.sequence.SequencePath;
import ca.corefacility.gview.layout.sequence.SlotPath;

/**
 * FeatureShapeRealizer used to draw an arrow which looks like
 * 
 * ===>
 * 
 * @author Aaron Petkau
 *
 */
public class ForwardArrowShapeRealizer extends AbstractArrowShapeRealizer
{	
	/**
	 * Constructs a ForwardArrowShapeRealizer to draw arrow shapes with the passed arrow length.
	 * @param arrowLength  The length of the arrow part to use.
	 */
	public ForwardArrowShapeRealizer(double arrowLength)
	{
		super(arrowLength);
	}
	
	/**
	 * Constructs a ForwardArrowShapeRealizer to draw arrow shapes with the default arrow lenth.
	 */
	public ForwardArrowShapeRealizer()
	{
		super();
	}
	
	protected void createArrowBlock(SlotPath path, int start, float center, int stop, double top, double bottom)
	{	
		double centerHeight = (top + bottom)/2.0;
		
		// draws out the arrow block on the sequence
		path.moveTo(start, top, SequencePath.Orientation.TOP);
		
		// (lineTo(pinned point, lengthFromPin, heightInSlot))
		path.lineTo(stop, top, -ARROW_LENGTH, Direction.INCREASING, SequencePath.Orientation.TOP);
		path.realLineTo(stop, centerHeight, 0, SequencePath.Orientation.TOP);
		path.realLineTo(stop, bottom, -ARROW_LENGTH, SequencePath.Orientation.BOTTOM);
		path.lineTo(start, Direction.DECREASING, SequencePath.Orientation.BOTTOM);
		path.closePath();
	}
	
	protected void createArrowHead(SlotPath path, int start, float center, int stop, double top, double bottom)
	{		
		double centerHeight = ((double)top + bottom)/2;
		
		path.moveTo(center, top, -ARROW_LENGTH/2, SequencePath.Orientation.TOP);
		path.realLineTo(center, centerHeight, ARROW_LENGTH/2, SequencePath.Orientation.TOP);
		path.realLineTo(center, bottom, -ARROW_LENGTH/2, SequencePath.Orientation.BOTTOM);
		path.realLineTo(center, top, -ARROW_LENGTH/2, SequencePath.Orientation.NONE);
		path.closePath();		
	}

    @Override
    protected void createFirstBlock(SlotPath path, int start,
            int stop, double top, double bottom, int sequenceLength)
    {
        if (start == stop)
        {
            createPointBlock(path, start, top, bottom);
        }
        else
        {
            createStandardBlock(path,start,stop,top,bottom);
        }
    }

    @Override
    protected void createMiddleBlock(SlotPath path, int start,
            int stop, double top, double bottom, int sequenceLength)
    {
        if (start == stop)
        {
            createPointBlock(path, start, top, bottom);
        }
        else
        {
            createStandardBlock(path,start,stop,top,bottom);
        }
    }

    @Override
    protected void createLastBlock(SlotPath path, int start,
            int stop, double top, double bottom, int sequenceLength)
    {
        float center = findCenterBase(start,stop,sequenceLength);

        if (start == stop)
        {
            createArrowHead(path,start,center,stop,top,bottom);
        }
        else
        {
            createArrowBlock(path,start,center,stop,top,bottom);
        }
    }
    
    @Override
    protected Location getArrowLocation(Location fullLocation)
    {
    	Location arrowLocation = null;
    	
		Iterator blocks = fullLocation.blockIterator();
		
		while (blocks.hasNext())
		{
			Location block = (Location)blocks.next();
							
			// if we are at the last block
			if (!blocks.hasNext())
			{
				arrowLocation = block;
			}
		}
    	
    	return arrowLocation;
    }
}
