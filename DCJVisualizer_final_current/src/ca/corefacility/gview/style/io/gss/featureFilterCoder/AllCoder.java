package ca.corefacility.gview.style.io.gss.featureFilterCoder;

import org.biojava.bio.seq.FeatureFilter;
import org.w3c.css.sac.LexicalUnit;

public class AllCoder extends FeatureFilterCoder
{
	private static final String ALL_FUNCTION = "all";
	
	@Override
	public FeatureFilter decode(LexicalUnit value)
	{
		FeatureFilter filter = null;
		
		if (value.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE)
		{
			if (ALL_FUNCTION.equals(value.getStringValue()))
			{
				filter = FeatureFilter.all;
			}	
		}
		
		return filter;
	}

	@Override
	public String encode(FeatureFilter filter)
	{
		String encoding;
		
		if (canCode(filter))
		{				
			encoding = "\"" + ALL_FUNCTION + "\"";
		}
		else
		{
			throw new IllegalArgumentException("cannot encode filter of type " + filter.getClass());
		}
		
		return encoding;
	}
	
	@Override
	public boolean canCode(FeatureFilter filter)
	{
		if (filter == null)
			return false;
		return FeatureFilter.all.equals(filter);
	}

	@Override
	public String functionName()
	{
		return ALL_FUNCTION;
	}
}
