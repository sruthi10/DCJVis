package ca.corefacility.gview.style.io.gss;

import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;

public abstract class SelectorConverter
{
	public boolean canProcessSelectors(SelectorList selectors)
	{		
		if (selectors == null)
		{
			return false;
		}
		
		for (int i = 0; i < selectors.getLength(); i++)
		{
			Selector selector = selectors.item(i);
			
			if (!canProcessSelector(selector))
			{
				return false;
			}
		}
		
		return true;
	}

	public abstract boolean canProcessSelector(Selector selector);
}
