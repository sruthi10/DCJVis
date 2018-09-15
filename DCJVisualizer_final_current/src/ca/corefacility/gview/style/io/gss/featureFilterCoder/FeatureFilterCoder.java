package ca.corefacility.gview.style.io.gss.featureFilterCoder;

import org.biojava.bio.seq.FeatureFilter;
import org.w3c.css.sac.LexicalUnit;

/**
 * Defines a class used to encode/decode feature filters.
 * @author aaron
 *
 */
public abstract class FeatureFilterCoder
{
	protected FeatureFilterCoderMap coderMap;
	
	/**
	 * Encodes the passed FeatureFilter into a string, using this FeatureFilterCoder.
	 * @param filter  The filter to encode.
	 * @return  A string encoding this FeatureFilter.
	 */
	public abstract String encode(FeatureFilter filter);
	
	/**
	 * Decodes the passed FeatureFilter corresponding to the passed LexicalUnit value.
	 * @param value  The LexicalUnit storing the function (and parameters) for this FeatureFilter.
	 * @return  The decoded FeatureFilter.
	 */
	public abstract FeatureFilter decode(LexicalUnit value);
	
	/**
	 * Sets the value of the FilterCoderMap, which is used to encode/decode any other "sub" feature filter types.
	 * @param coderMap  The FilterCoderMap to use.
	 */
	protected void setFilterCoderMap(FeatureFilterCoderMap coderMap)
	{
		this.coderMap = coderMap;
	}
	
	public abstract boolean canCode(FeatureFilter filter);
	
	public abstract String functionName();

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((coderMap == null) ? 0 : coderMap.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FeatureFilterCoder other = (FeatureFilterCoder) obj;
		if (coderMap == null)
		{
			if (other.coderMap != null)
				return false;
		}
		else if (!coderMap.equals(other.coderMap))
			return false;
		return true;
	}
}
