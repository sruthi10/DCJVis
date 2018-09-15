package ca.corefacility.gview.layout.feature;

import java.util.Iterator;

import org.biojava.bio.symbol.Location;

import ca.corefacility.gview.layout.Direction;
import ca.corefacility.gview.layout.sequence.SequencePath;
import ca.corefacility.gview.layout.sequence.SlotPath;

/**
 * FeatureShapeRealizer used to draw an arrow which looks like
 * 
 * <---
 * 
 * @author Aaron Petkau
 *
 */
public class ReverseArrow2ShapeRealizer extends AbstractArrowShapeRealizer
{
	private final double HEIGHT_DECREASE;

	/**
	 * Constructs a ForwardArrow2ShapeRealizer to draw alternate arrow shapes with the passed arrow length and height decrease.
	 * @param arrowLength  The length of the arrow part to use.
	 * @param heightDecrease  A proportion of thickness of the straight part and arrow head part of the arrow (1.0 is no decrease).
	 * 							So controls difference between
	 * 							this (<---) and
	 * 							this(<==)
	 */
	public ReverseArrow2ShapeRealizer(double arrowLength, double heightDecrease)
	{
		super(arrowLength);
		HEIGHT_DECREASE = heightDecrease;
	}
	
	/**
	 * Constructs a ForwardArrow2ShapeRealizer to draw arrow shapes with the default settings
	 */
	public ReverseArrow2ShapeRealizer()
	{
		this(10, 0.7);
	}
	
	protected void createArrowBlock(SlotPath path, int start, float center,
			int stop, double top, double bottom)
	{	
		double centerHeight = (top + bottom)/2.0;
		
		path.moveTo(start, centerHeight, SequencePath.Orientation.TOP);
		path.realLineTo(start, top, ARROW_LENGTH, SequencePath.Orientation.TOP);
		path.realLineTo(start, top*HEIGHT_DECREASE, ARROW_LENGTH, SequencePath.Orientation.TOP);
		path.lineTo(stop, Direction.INCREASING, SequencePath.Orientation.TOP);
		path.lineTo(stop, bottom*HEIGHT_DECREASE, Direction.NONE, SequencePath.Orientation.NONE);
		path.lineTo(start, bottom*HEIGHT_DECREASE, ARROW_LENGTH, Direction.DECREASING, SequencePath.Orientation.BOTTOM);
		path.realLineTo(start, bottom, ARROW_LENGTH, SequencePath.Orientation.BOTTOM);
		path.realLineTo(start, centerHeight, 0, SequencePath.Orientation.BOTTOM);
		path.closePath();
	}
	
	protected void createArrowHead(SlotPath path, int start, float center, int stop, double top, double bottom)
	{		
		double centerHeight = ((double)top + bottom)/2;
		
		path.moveTo(center, centerHeight, -ARROW_LENGTH/2, SequencePath.Orientation.TOP);
		path.realLineTo(center, top, ARROW_LENGTH/2, SequencePath.Orientation.TOP);
		path.realLineTo(center, bottom, ARROW_LENGTH/2, SequencePath.Orientation.NONE);
		path.realLineTo(center, centerHeight, -ARROW_LENGTH/2, SequencePath.Orientation.BOTTOM);
		path.closePath();
	}
	
    @Override
    protected void createFirstBlock(SlotPath path, int start,
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
    protected Location getArrowLocation(Location fullLocation)
    {
    	Location arrowLocation = null;
    	
		Iterator blocks = fullLocation.blockIterator();
		
    	if(blocks.hasNext())
    	{
    		arrowLocation = (Location)blocks.next();
    	}
    	
    	return arrowLocation;
    }
}
