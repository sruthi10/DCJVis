package ca.corefacility.gview.style.datastyle;

import java.util.Iterator;

import org.biojava.bio.seq.FeatureFilter;

/**
 * Describes a type which can hold sets of item styles (sets of ItemHolderStyles).
 *   TODO not sure exactly what would make sense for a name here?
 * @author Aaron Petkau
 *
 */
public interface StyleHolder
{
	/**
	 * @return  An iterator over all the styles held in this holder.
	 */
	public Iterator<SlotItemStyle> styles(); // should I do this, or should I look more at templates?
	
	/**
	 * Creates a FeatureHolderStyle within this style holder.
	 * @param filter  The filter to use to create the FeatureHolderStyle.
	 * @return  The newly created FeatureHolderStyle.
	 */
	public FeatureHolderStyle createFeatureHolderStyle(FeatureFilter filter);
	
	/**
	 * Gets a FeatureHolderStyle within this style holder.
	 * @param filter  The filter to use to search for the FeatureHolderStyle.
	 * @return  The newly searched for FeatureHolderStyle.
	 */
	public FeatureHolderStyle getFeatureHolderStyle(FeatureFilter filter);
	
//	public void addStyle(ItemHolderStyle style);
	//public void removeStyle(ItemHolderStyle style);
	
	/**
	 * Determines if this style holder contains a feature holder style for the passed filter.
	 * 
	 * @param filter  The FeatureFilter corresponding to some possible feature holder style.
	 * 
	 * @return True if it contains a corresponding feature holder style, false otherwise.
	 */
	public boolean containsFeatureHolderStyle(FeatureFilter filter);
}
