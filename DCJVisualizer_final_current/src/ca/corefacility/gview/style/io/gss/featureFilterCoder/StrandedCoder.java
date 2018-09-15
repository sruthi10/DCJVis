package ca.corefacility.gview.style.io.gss.featureFilterCoder;


import org.biojava.bio.seq.FeatureFilter;
import org.biojava.bio.seq.StrandedFeature;
import org.w3c.css.sac.LexicalUnit;

public class StrandedCoder extends FeatureFilterCoder
{
	private static final String STRAND_FUNCTION = "strand";
	
	private static final String POSITIVE = "positive";
	private static final String NEGATIVE = "negative";
	private static final String UNKNOWN = "unknown";
	
	@Override
	public FeatureFilter decode(LexicalUnit value)
	{
		FeatureFilter filter = null;
		
		if (STRAND_FUNCTION.equals(value.getFunctionName()))
		{
			LexicalUnit parameters = value.getParameters();
			
			if (parameters != null)
			{
				if (parameters.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE)
				{
					if (POSITIVE.equals(parameters.getStringValue()))
					{
						filter = new FeatureFilter.StrandFilter(StrandedFeature.POSITIVE);
					}
					else if (NEGATIVE.equals(parameters.getStringValue()))
					{
						filter = new FeatureFilter.StrandFilter(StrandedFeature.NEGATIVE);
					}
					else if (UNKNOWN.equals(parameters.getStringValue()))
					{
						filter = new FeatureFilter.StrandFilter(StrandedFeature.UNKNOWN);
					}
				}
			}
		}
		
		return filter;
	}

	@Override
	public String encode(FeatureFilter filter)
	{
		String encoding = STRAND_FUNCTION + "(\"";
		
		if (canCode(filter))
		{
			FeatureFilter.StrandFilter strandFilter = (FeatureFilter.StrandFilter)filter;
			
			if (strandFilter.getStrand().equals(StrandedFeature.POSITIVE))
			{
				encoding += POSITIVE;
			}
			else if (strandFilter.getStrand().equals(StrandedFeature.NEGATIVE))
			{
				encoding += NEGATIVE;
			}
			else
			{
				encoding += UNKNOWN;
			}
			
			encoding += "\")";
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
		return FeatureFilter.StrandFilter.class.equals(filter.getClass());
	}

	@Override
	public String functionName()
	{
		return STRAND_FUNCTION;
	}
}
