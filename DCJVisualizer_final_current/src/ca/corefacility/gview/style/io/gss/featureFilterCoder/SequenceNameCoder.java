package ca.corefacility.gview.style.io.gss.featureFilterCoder;

import org.biojava.bio.seq.FeatureFilter;
import org.w3c.css.sac.LexicalUnit;

import ca.corefacility.gview.utils.SequenceNameFilter;

public class SequenceNameCoder extends FeatureFilterCoder
{
	private static final String SEQUENCE_NAME_FUNCTION = "sequenceName";

	@Override
	public String encode(FeatureFilter filter)
	{
		String encoding = SEQUENCE_NAME_FUNCTION + "(";
		
		if (canCode(filter))
		{
			SequenceNameFilter nameFilter = (SequenceNameFilter)filter;
			
			encoding += "\"" + nameFilter.getSequenceName() + "\"";
			encoding += ")";
		}
		else
		{
			throw new IllegalArgumentException("cannot encode filter of type " + ((filter != null)?filter.getClass():"null"));
		}
		
		return encoding;
	}

	@Override
	public FeatureFilter decode(LexicalUnit value)
	{
		FeatureFilter filter = null;
		
		if (SEQUENCE_NAME_FUNCTION.equals(value.getFunctionName()))
		{
			LexicalUnit parameters = value.getParameters();
			
			if (parameters.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE)
			{
				if (parameters.getNextLexicalUnit() == null)
				{
					filter = new SequenceNameFilter(parameters.getStringValue());
				}
			}
		}
		
		return filter;
	}

	@Override
	public boolean canCode(FeatureFilter filter)
	{
		if (filter == null)
			return false;
		return SequenceNameFilter.class.equals(filter.getClass());
	}

	@Override
	public String functionName()
	{
		return SEQUENCE_NAME_FUNCTION;
	}
}
