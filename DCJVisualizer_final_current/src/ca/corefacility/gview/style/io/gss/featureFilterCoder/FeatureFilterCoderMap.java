package ca.corefacility.gview.style.io.gss.featureFilterCoder;


import java.util.HashMap;
import java.util.Map;

import org.biojava.bio.seq.FeatureFilter;
import org.w3c.css.sac.LexicalUnit;

/**
 * An implementation of FeatureFilterCoder used to detect/encode any general FeatureFilter.
 * @author aaron
 *
 */
public class FeatureFilterCoderMap extends FeatureFilterCoder
{
	private Map<String, FeatureFilterCoder> stringCoderMap;
	private Map<Class<? extends FeatureFilter>, FeatureFilterCoder> filterCoderMap;
	
	public FeatureFilterCoderMap()
	{
		stringCoderMap = new HashMap<String, FeatureFilterCoder>();
		filterCoderMap = new HashMap<Class<? extends FeatureFilter>, FeatureFilterCoder>();
	}
	
	/**
	 * Adds a new FilterFeatureCoder to this list of coders.
	 * @param filterClass  The FeatureFilter.
	 * @param coder  The FeatureFilterCoder.
	 */
	public void addFilterCoder(Class<? extends FeatureFilter> filterClass, FeatureFilterCoder coder)
	{
		if (filterClass == null)
		{
			throw new NullPointerException("filterClass is null");
		}
		
		if (coder == null)
		{
			throw new NullPointerException("coder is null");
		}
		
		coder.setFilterCoderMap(this);
		stringCoderMap.put(coder.functionName(), coder);
		filterCoderMap.put(filterClass, coder);
	}
	
	@Override
	public FeatureFilter decode(LexicalUnit value)
	{	
		if (value == null)
		{
			throw new NullPointerException("value is null");
		}
		
		FeatureFilter filter = null;
		FeatureFilterCoder coder = null;
		
		if (value.getLexicalUnitType() == LexicalUnit.SAC_FUNCTION)
		{
			coder = stringCoderMap.get(value.getFunctionName());
			
			if (coder == null)
			{
				throw new IllegalArgumentException("no FeatureFilterCoder for function " + value.getFunctionName());
			}
			
			filter = coder.decode(value);
		}
		else if (value.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE)
		{
			coder = stringCoderMap.get(value.getStringValue());
			
			if (coder == null)
			{
				throw new IllegalArgumentException("no FeatureFilterCoder for function " + value.getStringValue());
			}
			
			filter = coder.decode(value);
		}
		else
		{
			throw new IllegalArgumentException("passed LexicalUnit not of type SAC_FUNCTION or SAC_STRING_VALUE");	
		}
		
		return filter;
	}

	@Override
	public String encode(FeatureFilter filter)
	{
		if (filter == null)
		{
			throw new NullPointerException("filter is null");
		}
		
		FeatureFilterCoder coder = filterCoderMap.get(filter.getClass());
		
		if (coder == null)
		{
			throw new IllegalArgumentException("filter=" + filter.getClass() + " cannot be encoded");
		}
		
		return coder.encode(filter);
	}

	@Override
	public boolean canCode(FeatureFilter filter)
	{
		if (filter == null)
			return false;
		return filterCoderMap.containsKey(filter.getClass());
	}

	@Override
	public String functionName()
	{
		return "coder-map";
	}
}
