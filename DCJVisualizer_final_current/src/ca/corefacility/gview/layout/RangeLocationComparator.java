package ca.corefacility.gview.layout;

import org.biojava.bio.symbol.RangeLocation;
import java.util.Comparator;

public class RangeLocationComparator implements Comparator<RangeLocation>
{
	@Override
	public int compare(RangeLocation r1, RangeLocation r2)
	{
		if (r1 == null || r2 == null)
		{
			throw new NullPointerException("range locations cannot be null");
		}
		else
		{
			return r1.getMin()-r2.getMin();
		}
	}
}
