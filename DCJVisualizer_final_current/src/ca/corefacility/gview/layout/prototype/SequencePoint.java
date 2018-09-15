package ca.corefacility.gview.layout.prototype;

/**
 * A point in terms of sequence coordinates (base, heightFromBackbone).  Base is the base on the dna sequence (0 is zero base, 100 is 100th base/letter on gene, etc).
 * 	heightFromBackbone is the height for the central part of the sequence.  For linear, it coressponds to y-coords, for circular it's the radius.
 * @author Aaron Petkau
 *
 */
public interface SequencePoint
{
	public double getBase();
	public double getHeightFromBackbone();
	
	/**
	 * Translates this point by the passed values.
	 * @param base
	 * @param heightFromBackbone
	 */
	public void translate(double base, double heightFromBackbone);
	
	public void setBase(double base);
	
	public void setHeightFromBackbone(double heightFromBackbone);
	
	public Object clone();
}
