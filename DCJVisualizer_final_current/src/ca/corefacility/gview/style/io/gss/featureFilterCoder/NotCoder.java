package ca.corefacility.gview.style.io.gss.featureFilterCoder;

import org.biojava.bio.seq.FeatureFilter;
import org.w3c.css.sac.LexicalUnit;

public class NotCoder extends FeatureFilterCoder
{
	private static final String NOT_FUNCTION = "not";
	
	@Override
	public FeatureFilter decode(LexicalUnit value)
	{
		FeatureFilter filter = null;
		
		if (NOT_FUNCTION.equals(value.getFunctionName()))
		{
			LexicalUnit parameters = value.getParameters();
			
			if (parameters != null)
			{
				if (parameters.getLexicalUnitType() == LexicalUnit.SAC_FUNCTION ||
						parameters.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE)
				{
					LexicalUnit filterUnit1 = parameters;
					
					FeatureFilter filter1 = coderMap.decode(filterUnit1);
						
					filter = new FeatureFilter.Not(filter1);
				}
			}
		}
		
		return filter;
	}

	@Override
	public String encode(FeatureFilter filter)
	{
		String encoding = NOT_FUNCTION + "(";
		
		if (canCode(filter))
		{
			FeatureFilter.Not notFilter = (FeatureFilter.Not)filter;
			
			FeatureFilter filter1 = notFilter.getChild();
			
			encoding += coderMap.encode(filter1);
			
			encoding += ")";
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
		return FeatureFilter.Not.class.equals(filter.getClass());
	}

	@Override
	public String functionName()
	{
		return NOT_FUNCTION;
	}
}
