package ca.corefacility.gview.style.io.gss.featureFilterCoder;

import java.util.ArrayList;
import java.util.List;

import org.biojava.bio.seq.FeatureFilter;
import org.w3c.css.sac.LexicalUnit;

import ca.corefacility.gview.utils.AndFilter;

public class AndCoder extends FeatureFilterCoder
{
	private static final String AND_FUNCTION = "and";
	
	@Override
	public FeatureFilter decode(LexicalUnit value)
	{
		List<LexicalUnit> andUnits;
		AndFilter filter = new AndFilter();
		
		if (AND_FUNCTION.equals(value.getFunctionName()))
		{
			LexicalUnit parameters = value.getParameters();
			
			if (parameters != null)
			{
				andUnits = new ArrayList<LexicalUnit>();
				
				while (parameters != null && (parameters.getLexicalUnitType() == LexicalUnit.SAC_FUNCTION ||
						parameters.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE))
				{
					andUnits.add(parameters); // add current and unit
					
					parameters = parameters.getNextLexicalUnit();
					
					if (parameters != null)
					{
						if (parameters.getLexicalUnitType() == LexicalUnit.SAC_OPERATOR_COMMA) // if comma, skip
						{
							parameters = parameters.getNextLexicalUnit();
						}
						else // TODO, throw exception
						{
							System.err.println("Missing \",\" in " + value);
							return null;
						}
					}
				}
				
				//Add the andUnits to the filter:
				for(LexicalUnit currUnit : andUnits)
				{
					FeatureFilter currFilter = coderMap.decode(currUnit);
					
					filter.add(currFilter);
				}
			}
		}
		
		return filter;
	}

	@Override
	public String encode(FeatureFilter filter)
	{
		String encoding = AND_FUNCTION + "(";
		
		if (canCode(filter))
		{
			AndFilter andFilter;
			
			//Convert the legacy and filter to AndFilter:
			if(filter instanceof FeatureFilter.And)
			{
				FeatureFilter.And legacyAndFilter = (FeatureFilter.And)filter;
				
				andFilter = new AndFilter(legacyAndFilter.getChild1(), legacyAndFilter.getChild2());
			}
			//Cast:
			else if(filter instanceof AndFilter)
			{
				andFilter = (AndFilter)filter;
			}
			else
			{
				throw new ClassCastException("Unrecognized feature filter.");
			}
			
			List<FeatureFilter> filters = andFilter.getFilters();
			
			for(int i = 0; i < filters.size(); i++)
			{
				encoding += coderMap.encode(filters.get(i));
				
				//Comma?
				if(i < filters.size() - 1)
				{
					encoding += ",";
				}
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
	public boolean canCode(FeatureFilter filter)
	{
		if (filter == null)
			return false;
		return AndFilter.class.equals(filter.getClass()) || FeatureFilter.And.class.equals(filter.getClass());
	}

	@Override
	public String functionName()
	{
		return AND_FUNCTION;
	}
}
