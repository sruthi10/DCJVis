package ca.corefacility.gview.style.io.gss.featureFilterCoder;

import java.util.LinkedList;
import java.util.Queue;

import org.biojava.bio.seq.FeatureFilter;
import org.w3c.css.sac.LexicalUnit;

public class OrCoder extends FeatureFilterCoder
{
	private static final String OR_FUNCTION = "or";

	@Override
	public FeatureFilter decode(LexicalUnit value)
	{
		FeatureFilter filter = null;
		Queue<LexicalUnit> orUnits;

		if (OR_FUNCTION.equals(value.getFunctionName()))
		{
			LexicalUnit parameters = value.getParameters();

			if (parameters != null)
			{
				orUnits = new LinkedList<LexicalUnit>();

				while (parameters != null
						&& (parameters.getLexicalUnitType() == LexicalUnit.SAC_FUNCTION || parameters
								.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE))
				{
					orUnits.add(parameters); // add current and unit

					parameters = parameters.getNextLexicalUnit();

					if (parameters != null)
					{
						if (parameters.getLexicalUnitType() == LexicalUnit.SAC_OPERATOR_COMMA) // if
																								// comma,
																								// skip
						{
							parameters = parameters.getNextLexicalUnit();
						}
						else
						// TODO, throw exception
						{
							System.err.println("Missing \",\" in " + value);
							return null;
						}
					}
				}

				// pull off last two feature filters
				if (!orUnits.isEmpty())
				{
					LexicalUnit currUnit = orUnits.remove();
					FeatureFilter currFilter1 = coderMap.decode(currUnit);

					// only single parameter for or(), so treat as
					// or(child1,child1)
					if (orUnits.isEmpty())
					{
						filter = new FeatureFilter.Or(currFilter1, currFilter1);
					}
					else
					{
						currUnit = orUnits.remove();
						FeatureFilter currFilter2 = coderMap.decode(currUnit);

						filter = new FeatureFilter.Or(currFilter1, currFilter2);

						// extract out rest of parameters
						while (!orUnits.isEmpty())
						{
							currUnit = orUnits.remove();
							currFilter1 = coderMap.decode(currUnit);

							filter = new FeatureFilter.Or(currFilter1, filter);
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
		String encoding = OR_FUNCTION + "(";

		if (canCode(filter))
		{
			FeatureFilter.Or orFilter = (FeatureFilter.Or) filter;

			FeatureFilter filter1 = orFilter.getChild1();
			FeatureFilter filter2 = orFilter.getChild2();

			encoding += coderMap.encode(filter1);
			encoding += ",";
			encoding += coderMap.encode(filter2);

			encoding += ")";
		}
		else
		{
			throw new IllegalArgumentException("cannot encode filter of type "
					+ filter.getClass());
		}

		return encoding;
	}

	@Override
	public boolean canCode(FeatureFilter filter)
	{
		if (filter == null)
			return false;
		return FeatureFilter.Or.class.equals(filter.getClass());
	}

	@Override
	public String functionName()
	{
		return OR_FUNCTION;
	}
}
