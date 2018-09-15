package ca.corefacility.gview.style.io.gss.featureFilterCoder;

import org.biojava.bio.seq.FeatureFilter;
import org.w3c.css.sac.LexicalUnit;

public class HasAnnotationCoder extends FeatureFilterCoder
{
	private static final String HAS_ANNOTATION_NAME = "hasAnnotation";
	
	@Override
	public boolean canCode(FeatureFilter filter)
	{
		if (filter == null)
			return false;
		return filter.getClass().equals(FeatureFilter.HasAnnotation.class);
	}

	@Override
	public String encode(FeatureFilter filter)
	{
		String encoding = HAS_ANNOTATION_NAME + "(";
		
		if (canCode(filter))
		{
			FeatureFilter.HasAnnotation hasAnnotation = (FeatureFilter.HasAnnotation)filter;
			
			encoding +=  "\"" + hasAnnotation.getKey() + "\")";
		}
		else
		{
			throw new IllegalArgumentException("cannot encode filter of type " + filter.getClass());
		}
		
		return encoding;
	}

	@Override
	public FeatureFilter decode(LexicalUnit value)
	{
		FeatureFilter filter = null;
		String key;
		
		if (HAS_ANNOTATION_NAME.equals(value.getFunctionName()))
		{
			LexicalUnit parameters = value.getParameters();
			
			if (parameters != null)
			{
				if (parameters.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE)
				{
					key = parameters.getStringValue();
					
					filter = new FeatureFilter.HasAnnotation(key);
				}
			}
		}
		
		return filter;
	}

	@Override
	public String functionName()
	{
		return HAS_ANNOTATION_NAME;
	}

}
