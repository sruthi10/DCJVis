package ca.corefacility.gview.style.io.gss.featureFilterCoder;


import org.biojava.bio.seq.FeatureFilter;
import org.w3c.css.sac.LexicalUnit;

import ca.corefacility.gview.utils.AnnotationValueContains;

public class AnnotationValueContainsCoder extends FeatureFilterCoder
{
	private String ANNOTATION_FUNCTION = "annotationValueContains";

	@Override
	public boolean canCode(FeatureFilter filter)
	{
		if (filter == null)
			return false;
		return filter.getClass().equals(AnnotationValueContains.class);
	}

	@Override
	public FeatureFilter decode(LexicalUnit value)
	{
		FeatureFilter filter = null;
		String key, valueOfType;
		
		if (ANNOTATION_FUNCTION.equals(value.getFunctionName()))
		{
			LexicalUnit parameters = value.getParameters();
			
			if (parameters != null)
			{
				if (parameters.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE)
				{
					key = parameters.getStringValue();
					
					parameters = parameters.getNextLexicalUnit();
					
					if (parameters.getLexicalUnitType() == LexicalUnit.SAC_OPERATOR_COMMA)
					{
						parameters = parameters.getNextLexicalUnit();
						
						if (parameters.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE)
						{
							valueOfType = parameters.getStringValue();
							
							filter = new AnnotationValueContains(key, valueOfType);
						}
					}
				}
			}
		}
		
		return filter;
	}

	@Override
	public String encode(FeatureFilter filter)
	{
		String encoding = ANNOTATION_FUNCTION + "(";
		
		if (canCode(filter))
		{
			AnnotationValueContains annContains = (AnnotationValueContains)filter;
			
			String type =  "\"" + annContains.getKey() + "\",\"" + annContains.getValue() + "\"";
			
			encoding += type + ")";
		}
		else
		{
			throw new IllegalArgumentException("cannot encode filter of type " + filter.getClass());
		}
		
		return encoding;
	}

	@Override
	public String functionName()
	{
		return ANNOTATION_FUNCTION;
	}
}
