package ca.corefacility.gview.layout.feature;

import java.util.Iterator;

import org.biojava.bio.symbol.Location;

import ca.corefacility.gview.layout.Direction;
import ca.corefacility.gview.layout.sequence.SequencePath;
import ca.corefacility.gview.layout.sequence.SlotPath;

/**
 * FeatureShapeRealizer used to draw an arrow which looks like
 * 
 * ---->
 * 
 * @author Aaron Petkau
 *
 */
public class ForwardArrow2ShapeRealizer extends AbstractArrowShapeRealizer
{
	private final double HEIGHT_DECREASE;
	
	/**
	 * Constructs a ForwardArrow2ShapeRealizer to draw alternate arrow shapes with the passed arrow length and height decrease.
	 * @param arrowLength  The length of the arrow part to use.
	 * @param heightDecrease  A proportion of thickness of the straight part and arrow head part of the arrow (1.0 is no decrease).
	 * 							So controls difference between
	 * 							this (--->) and
	 * 							this(==>)
	 */
	public ForwardArrow2ShapeRealizer(double arrowLength, double heightDecrease)
	{
		super(arrowLength);
		HEIGHT_DECREASE = heightDecrease;
	}
	
	/**
	 * Constructs a ForwardArrow2ShapeRealizer to draw arrow shapes with the default settings
	 */
	public ForwardArrow2ShapeRealizer()
	{
		this(10, 0.7);
	}

	protected void createArrowBlock(SlotPath path, int start, float center,
			int stop, double top, double bottom)
	{	
		double centerHeight = ((double)top + bottom)/2;

		path.moveTo(start, top*HEIGHT_DECREASE, SequencePath.Orientation.TOP);
		
		// (lineTo(pinned point, lengthFromPin, heightInSlot))
		path.lineTo(stop, top*HEIGHT_DECREASE, -ARROW_LENGTH, Direction.INCREASING, SequencePath.Orientation.TOP);
		path.realLineTo(stop, top, -ARROW_LENGTH, SequencePath.Orientation.TOP);
		path.realLineTo(stop, centerHeight, 0, SequencePath.Orientation.TOP);
		path.realLineTo(stop, bottom, -ARROW_LENGTH, SequencePath.Orientation.BOTTOM);
		path.realLineTo(stop, bottom*HEIGHT_DECREASE, -ARROW_LENGTH, SequencePath.Orientation.BOTTOM);
		path.lineTo(start, Direction.DECREASING, SequencePath.Orientation.BOTTOM);
		path.closePath();
	}
	
	protected void createArrowHead(SlotPath path, int start, float center, int stop, double top, double bottom)
	{
		double centerBase = ((double)start + stop)/2;
		
		double centerHeight = ((double)top + bottom)/2;
		
		path.moveTo(centerBase, top, -ARROW_LENGTH/2, SequencePath.Orientation.TOP);
		path.realLineTo(centerBase, centerHeight, ARROW_LENGTH/2, SequencePath.Orientation.TOP);
		path.realLineTo(centerBase, bottom, -ARROW_LENGTH/2, SequencePath.Orientation.BOTTOM);
		path.realLineTo(centerBase, top, -ARROW_LENGTH/2, SequencePath.Orientation.NONE);
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
