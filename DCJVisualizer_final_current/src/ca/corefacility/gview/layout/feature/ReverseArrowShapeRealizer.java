package ca.corefacility.gview.layout.feature;

import java.util.Iterator;

import org.biojava.bio.symbol.Location;

import ca.corefacility.gview.layout.Direction;
import ca.corefacility.gview.layout.sequence.SequencePath;
import ca.corefacility.gview.layout.sequence.SlotPath;

/**
 * FeatureShapeRealizer used to draw an arrow which looks like
 * 
 * <===
 * 
 * @author Aaron Petkau
 *
 */
public class ReverseArrowShapeRealizer extends AbstractArrowShapeRealizer
{
	/**
	 * Constructs a ReverseArrowShapeRealizer to draw arrow shapes with the passed arrow length.
	 * @param arrowLength  The length of the arrow part to use.
	 */
	public ReverseArrowShapeRealizer(double arrowLength)
	{
		super(arrowLength);
	}
	
	/**
	 * Constructs a ReverseArrowShapeRealizer to draw arrow shapes with the default arrow lenth.
	 */
	public ReverseArrowShapeRealizer()
	{
		super();
	}
	
	protected void createArrowBlock(SlotPath path, int start, float center, int stop, double top, double bottom)
	{
		double centerHeight = ((double)top + bottom)/2;
		
		path.moveTo(start, centerHeight, SequencePath.Orientation.TOP);
		path.realLineTo(start, top, ARROW_LENGTH, SequencePath.Orientation.TOP); // (lineTo(pinned point, lengthFromPin, heightInSlot))
		path.lineTo(stop, Direction.INCREASING, SequencePath.Orientation.TOP);
		path.lineTo(stop, bottom, Direction.NONE, SequencePath.Orientation.NONE);
		path.lineTo(start, bottom, ARROW_LENGTH, Direction.DECREASING, SequencePath.Orientation.BOTTOM);
		path.realLineTo(start, centerHeight, 0, SequencePath.Orientation.BOTTOM);
		path.closePath();
	}
	
	protected void createArrowHead(SlotPath path, int start, float center, int stop, double top, double bottom)
	{
		double centerBase = center;
		
		double centerHeight = ((double)top + bottom)/2;
		
		path.moveTo(centerBase, centerHeight, -ARROW_LENGTH/2, SequencePath.Orientation.TOP);
		path.realLineTo(centerBase, top, ARROW_LENGTH/2, SequencePath.Orientation.TOP);
		path.realLineTo(centerBase, bottom, ARROW_LENGTH/2, SequencePath.Orientation.NONE);
		path.realLineTo(centerBase, centerHeight, -ARROW_LENGTH/2, SequencePath.Orientation.BOTTOM);
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
