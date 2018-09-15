package ca.corefacility.gview.layout.prototype;


import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.IllegalPathStateException;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ca.corefacility.gview.layout.prototype.segments.MoveSegment;
import ca.corefacility.gview.layout.prototype.segments.StretchableSegment;
import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.layout.sequence.SequencePath;

/**
 * A shape which can stretch itself along the backbone on receiving an event. Does not fragment
 * shape if it is cut off at some point. This class isn't important for any current code (could be
 * done directly in Stretchable Shape) but if there is to be other layouts (such as wrapped linear
 * layouts) Then we would need to divide StretchableShape into nonfragmenting/fragmenting since a
 * wrapped linear layout could cut off feature shapes and wrap them to a different location.
 * 
 * @author Aaron Petkau
 * 
 */
public class NonFragmentingStretchableShape extends StretchableShape
{   
	/**
	 * Stores a list of segments which can be used to generate a scaled shape.
	 */
	List<StretchableSegment> stretchableSegments = new LinkedList<StretchableSegment>();
	
	private Path2D currentPath;
	private Shape currentShape;
	
	private RegionHandler topRegion;
	private RegionHandler bottomRegion;
	
    private float maxHeight;
    private float minHeight;

	public NonFragmentingStretchableShape(Backbone backbone) // we need this so that we can create
																// the shape initally
	{
		super(backbone);
		
		currentPath = new Path2D.Double(Path2D.WIND_NON_ZERO);
		topRegion = new RegionHandler(SequencePath.Orientation.TOP);
		bottomRegion = new RegionHandler(SequencePath.Orientation.BOTTOM);
		currentShape = currentPath;
		
        maxHeight = -Float.MAX_VALUE;
        minHeight = Float.MAX_VALUE;
	}
	
    /**
     * @return  The maximum thickness of the shape defined by this slot path, given in terms of difference in extreme heightFromBackbone values.
     */
    private float getMaxThickness()
    {
        if (maxHeight == -Float.MAX_VALUE || minHeight == Float.MAX_VALUE)
        {
            return 0.0f;
        }
        else
        {
            return (maxHeight-minHeight);
        }
    }
    
    @Override
    public void updateHeight(float height)
    {
        if (height > this.maxHeight)
        {
            this.maxHeight = height;
        }
        else if (height < this.minHeight)
        {
            this.minHeight = height;
        }
    }

	/**
	 * Appends the passed segment onto this shape prototype.
	 * 
	 * @param segment
	 *            The segment to append.
	 */
	@Override
	public void appendSegment(StretchableSegment segment)
	{
		if (segment != null)
		{
			// this makes sure that a moveto is always the first segment added
			if (!(segment instanceof MoveSegment) && ((stretchableSegments.isEmpty()) || !(stretchableSegments.get(0) instanceof MoveSegment)))
			{
				throw new IllegalPathStateException("Missing moveto in path");
				// TODO should I use this exception, or create my own?
			}
			else
			{
				stretchableSegments.add(segment);
				shapeChanged = true;
			}
		}
		else
		{
			throw new IllegalArgumentException("segment is null");
		}
	}

	@Override
	protected Shape getCurrentShape()
	{
		return currentShape;
	}

	@Override
	protected void modifyShape(Backbone backbone)
	{
		if (!shapeChanged)
		{
			return;
		}
		
		topRegion.reset();
		bottomRegion.reset();
		
		topRegion.setThickness(getMaxThickness());
	    bottomRegion.setThickness(getMaxThickness());

		// iterator through all segments, creating the new, scaled shape.
		if (backbone != null)
		{
			Iterator<StretchableSegment> segments = stretchableSegments.iterator();
			currentPath.reset();

			while (segments.hasNext())
			{
				StretchableSegment currSegment = segments.next();

				if (currSegment != null)
				{
				    if (currSegment instanceof MoveSegment)
				    {
				        topRegion.newMoveSegment();
				        bottomRegion.newMoveSegment();
				    }
				    
                    topRegion.handleRegion(backbone, currentPath.getCurrentPoint(), currSegment);
                    bottomRegion.handleRegion(backbone, currentPath.getCurrentPoint(), currSegment);

                    currSegment.appendWithScale(currentPath, backbone);
				}
			}

			shapeChanged = false;
		}
	}

    @Override
    public Shape getRegion(SequencePath.Orientation orientation)
    {
        if (SequencePath.Orientation.TOP.equals(orientation))
        {
            return topRegion.getRegion();
        }
        else if (SequencePath.Orientation.BOTTOM.equals(orientation))
        {
            return bottomRegion.getRegion();
        }
        else
        {
            return null;
        }
    }
    
    private class RegionHandler
    {
        private final static float proportion = 0.1f;
        
        private SequencePath.Orientation orientation;
        private Path2D regionPath;
        private Shape regionShape;
        
        private Stroke stroke;
        
        private boolean newMoveSegment;
                
        public RegionHandler(SequencePath.Orientation orientation)
        {
            this.orientation = orientation;
            regionPath = new Path2D.Double(Path2D.WIND_NON_ZERO);
            newMoveSegment = true;
            stroke = null;
        }
        
        public void setThickness(float maxThickness)
        {
            stroke = new BasicStroke(maxThickness*2*proportion,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_MITER);
        }

        public void reset()
        {
            regionPath.reset();
            regionShape = null;
            newMoveSegment = true;
        }
        
        public void newMoveSegment()
        {
            newMoveSegment = true;
        }

        public void handleRegion(Backbone backbone, Point2D mainPathPoint, StretchableSegment segment)
        {
            if (orientation != null)
            {
                if (orientation.equals(segment.getOrientation()))
                {
                    if (!(segment instanceof MoveSegment))
                    {
                        if (newMoveSegment)
                        {
                            regionPath.moveTo(mainPathPoint.getX(), mainPathPoint.getY());
                            newMoveSegment = false;
                        }
                        else if (regionPath.getCurrentPoint() == null)
                        {
                            regionPath.moveTo(mainPathPoint.getX(), mainPathPoint.getY());
                        }
                    }
                    else
                    {
                        newMoveSegment = false;
                    }
        
                    segment.appendWithScale(regionPath, backbone);
                }
            }
        }
        
        public Shape getRegion()
        {
            if (regionShape == null)
            {
                regionShape = stroke.createStrokedShape(regionPath);
            }
            
            return regionShape;
        }
    }
}
