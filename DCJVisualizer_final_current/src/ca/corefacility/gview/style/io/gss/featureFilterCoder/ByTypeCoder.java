package ca.corefacility.gview.style.io.gss.featureFilterCoder;

import org.biojava.bio.seq.FeatureFilter;
import org.w3c.css.sac.LexicalUnit;

public class ByTypeCoder extends FeatureFilterCoder
{
	private static final String BY_TYPE_FUNCTION = "type";
	
	@Override
	public FeatureFilter decode(LexicalUnit value)
	{
		FeatureFilter filter = null;
		
		if (BY_TYPE_FUNCTION.equals(value.getFunctionName()))
		{
			LexicalUnit parameters = value.getParameters();
			
			if (parameters != null)
			{
				if (parameters.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE)
				{
					String type = parameters.getStringValue();
					
					filter = new FeatureFilter.ByType(type);
				}
			}
		}
		
		return filter;
	}

	@Override
	public String encode(FeatureFilter filter)
	{
		String encoding = BY_TYPE_FUNCTION + "(";
		
		if (canCode(filter))
		{
			FeatureFilter.ByType byTypeFilter = (FeatureFilter.ByType)filter;
			
			String type =  "\"" + byTypeFilter.getType() + "\"";
			
			encoding += type + ")";
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
		return FeatureFilter.ByType.class.equals(filter.getClass());
	}
	
	@Override
	public String functionName()
	{
		return BY_TYPE_FUNCTION;
	}
}
