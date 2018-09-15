package ca.corefacility.gview.style.io.gss.featureFilterCoder;


import org.biojava.bio.seq.FeatureFilter;
import org.biojava.bio.symbol.Location;
import org.biojava.bio.symbol.RangeLocation;
import org.w3c.css.sac.LexicalUnit;

public class OverlapsLocationCoder extends FeatureFilterCoder
{
	private String OVERLAPS_LOCATION_FUNCTION = "overlaps";

	@Override
	public boolean canCode(FeatureFilter filter)
	{
		if (filter == null)
			return false;
		return filter.getClass().equals(FeatureFilter.OverlapsLocation.class);
	}

	@Override
	public FeatureFilter decode(LexicalUnit value)
	{
		FeatureFilter filter = null;
		int start,end;
		
		if (OVERLAPS_LOCATION_FUNCTION.equals(value.getFunctionName()))
		{
			LexicalUnit parameters = value.getParameters();
			
			if (parameters != null)
			{
				if (parameters.getLexicalUnitType() == LexicalUnit.SAC_INTEGER)
				{
					start = parameters.getIntegerValue();
					
					parameters = parameters.getNextLexicalUnit();
					
					if (parameters.getLexicalUnitType() == LexicalUnit.SAC_OPERATOR_COMMA)
					{
						parameters = parameters.getNextLexicalUnit();
						
						if (parameters.getLexicalUnitType() == LexicalUnit.SAC_INTEGER)
						{
							end = parameters.getIntegerValue();
							
							filter = new FeatureFilter.OverlapsLocation(new RangeLocation(start, end));
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
		String encoding = OVERLAPS_LOCATION_FUNCTION + "(";
		
		if (canCode(filter))
		{
			FeatureFilter.OverlapsLocation locationFilter = (FeatureFilter.OverlapsLocation)filter;
			
			Location location = locationFilter.getLocation();
			
			if (location == null || !RangeLocation.class.equals(location.getClass()))
			{
				throw new IllegalArgumentException("cannon encode location of type " + location);
			}
			else
			{
				RangeLocation rangeLocation = (RangeLocation)location;
				
				encoding += rangeLocation.getMin() + "," + rangeLocation.getMax();
			}
			
			encoding += ")";
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
		return OVERLAPS_LOCATION_FUNCTION;
	}
}
