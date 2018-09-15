package ca.corefacility.gview.layout.feature;


import java.awt.Shape;

import org.biojava.bio.symbol.Location;

import ca.corefacility.gview.layout.Direction;
import ca.corefacility.gview.layout.sequence.LocationConverter;
import ca.corefacility.gview.layout.sequence.SequencePath;
import ca.corefacility.gview.layout.sequence.SlotPath;
import ca.corefacility.gview.layout.sequence.SlotTranslator;

/**
 * Deals with defining the actual shape representing a feature.  Used to add different decorations (arrow heads) etc on features.
 * 
 * @author Aaron Petkau
 *
 */
// TODO change around these classes for NonFragmentingStretchableShape
// TODO arrows do not work right now, I may want to change how this is implemented.
public abstract class FeatureShapeRealizer
{	
	/**
	 * Draws features with no arrow head.
	 */
	public static final FeatureShapeRealizer NO_ARROW = new NoArrowShapeRealizer();
	
	/**
	 * Draws features with the first type of arrow head, in the forward direction.
	 */
	public static final FeatureShapeRealizer CLOCKWISE_ARROW = new ForwardArrowShapeRealizer();
	
	/**
	 * Draws features with the first type of arrow head, in the reverse direction.
	 */
	public static final FeatureShapeRealizer COUNTERCLOCKWISE_ARROW = new ReverseArrowShapeRealizer();
	
	/**
	 * Draws features with another type of arrow head (looks more like a standard arrow) in the forward direction.
	 */
	public static final FeatureShapeRealizer CLOCKWISE_ARROW2 = new ForwardArrow2ShapeRealizer();
	
	/**
	 * Draws features with another type of arrow head (looks more like a standard arrow) in the reverse direction.
	 */
	public static final FeatureShapeRealizer COUNTERCLOCKWISE_ARROW2 = new ReverseArrow2ShapeRealizer();
	
	/**
	 * Does not create any shape for a feature, so that it is hidden.
	 */
	public static final FeatureShapeRealizer HIDDEN = new HiddenShapeRealizer();
	
	private static final float POINT_LENGTH = 5;
	
	/**
	 * Creates a shape in the passed slot, at the passed location.
	 * 
	 * @param path  The SlotPath where we should create the shape (this is modified).
	 * @param location  The location to create the shape.
	 * @param thickness  The thickness of the feature in the slot.
	 * @param heightAdjust  The offset from the center of the feature in the slot.
	 * 
	 * @return  The shape of the feature on the map.
	 */
	public Shape createFeaturePrototype(LocationConverter locationConverter, SlotPath path, Location location, double thickness, double heightAdjust)
	{
		Shape shape = null;

		// TODO error checking for thickness
		
		if (path == null)
		{
			throw new IllegalArgumentException("Passed SlotPath is null");
		}
		else if (location == null)
		{
			throw new IllegalArgumentException("Passed Location is null");
		}
		else
		{
			// calculate top/bottom of feature
			// if top or bottom go past the slot thickness, we must shift feature down so it just rests against the edge of the slot
			float top = (float)(SlotTranslator.TOP*thickness);
			float bottom = (float)(SlotTranslator.BOTTOM*thickness);
			float distance = (top - bottom);
			
			// shift up
			if (heightAdjust >= 0)
			{
				// if top out of bounds
				if (top + heightAdjust > SlotTranslator.TOP)
				{
					top =(float)SlotTranslator.TOP;
					bottom = top - distance;
				}
				else
				{
					top += heightAdjust;
					bottom += heightAdjust;
				}
			}
			else // shift down
			{
				// if bottom out of bounds
				if (bottom - (-heightAdjust) < SlotTranslator.BOTTOM)
				{
					bottom = (float)SlotTranslator.BOTTOM;
					top = bottom + distance;
				}
				else
				{
					bottom += heightAdjust;
					top += heightAdjust;
				}
			}
			
			shape = realizeFeatureShape(path, locationConverter.convertLocation(location), top, bottom,locationConverter.getSequenceLength());
		}
		
		return shape;
	}
	
	
	/**
	 * Returns the very start of this location
	 * @param location  A contiguous location to pull the start from (location.isContiguous() must be true)
	 * @return  The start of this location, or -1 if error.
	 */
	protected int extractStart(Location location)
	{
		int start = -1;
		
		if (!location.isContiguous())
		{
			throw new IllegalArgumentException("Location is not contiguous");
		}
		
		start = location.getMin();
		
		return start;
	}
	
	/**
	 * Returns the very stop of this location
	 * @param location  A contiguous location to pull the stop from (location.isContiguous() must be true)
	 * @return  The stop of this location, or -1 if error.
	 */
	protected int extractStop(Location location)
	{
		int stop = -1;
		
		if (!location.isContiguous())
		{
			throw new IllegalArgumentException("Location is not contiguous");
		}
		
		stop = location.getMax();
		
		return stop;
	}
	
	/**
	 * Returns the length of a location, this may overlap the 0 base (circular)
	 * @param location  A location to extract the length from.
	 * @param sequenceLength
	 * @return  The length, or -1 if error.
	 */
	protected int extractMinMaxLength(Location location, int sequenceLength)
	{
		int length = -1;
		
		int start = location.getMin();
		int stop = location.getMax();
		
		if (stop < start) // overlaps 0 base
		{
			int firstBlock = sequenceLength - start;
			int secondBlock = stop;
			
			length = firstBlock + secondBlock;
		}
		else
		{
			length = stop - start;
		}
		
		return length;
	}
	
	protected float findCenterBase(int start, int stop, int sequenceLength)
	{
		float center = -1;
		
		if (stop < start)
		{
			int total = start + stop + sequenceLength;
			center = total/2.0f;
		}
		else
		{
			center = (start + stop)/2.0f;
		}
		
		while (center > sequenceLength)
		{
			center -= sequenceLength;
		}
		
		return center;
	}
	
	/**
	 * This creates a standard block (no decoration) in the slot.  Used so that all implementations can
	 * 	have access to a method to do this.
	 * 
	 * @param path  The SlotPath where we should create the shape (this is modified).
	 * @param start  The start base of this block.
	 * @param stop  The end base of this block.
	 * @param top  The top of this block (in the slot).
	 * @param bottom  The bottom of this block (in the slot).
	 */
	protected void createStandardBlock(SlotPath path, int start, int stop, double top, double bottom)
	{
		path.moveTo(start, top, SequencePath.Orientation.TOP);
		path.lineTo(stop, Direction.INCREASING, SequencePath.Orientation.TOP);
		path.lineTo(stop, bottom, Direction.NONE, SequencePath.Orientation.NONE);
		path.lineTo(start, Direction.DECREASING, SequencePath.Orientation.BOTTOM);
		path.closePath();
	}
	   
    protected void createPointBlock(SlotPath path, int location, double top, double bottom)
    {                
        path.moveTo(location, top, -POINT_LENGTH/2, SequencePath.Orientation.TOP);
        path.realLineTo(location, top, POINT_LENGTH/2, SequencePath.Orientation.TOP);
        path.realLineTo(location, bottom, POINT_LENGTH/2, SequencePath.Orientation.NONE);
        path.realLineTo(location, bottom, -POINT_LENGTH/2, SequencePath.Orientation.BOTTOM);
        path.realLineTo(location, top, -POINT_LENGTH/2, SequencePath.Orientation.NONE);
        path.closePath();
    }
	
	/**
	 * Method to override to do the actual drawing.  Parameters already checked for errors.
	 * 
	 * @param path  The SlotPath where we should create the shape (this is modified).
	 * @param trueLocation  The actual location if the feature on the sequence.
	 * @param top  The top of this block (in the slot).
	 * @param bottom  The bottom of this block (in the slot).
	 */
	protected abstract Shape realizeFeatureShape(SlotPath path, Location trueLocation, double top, double bottom, int sequenceLength);
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		return true;
	}
}
