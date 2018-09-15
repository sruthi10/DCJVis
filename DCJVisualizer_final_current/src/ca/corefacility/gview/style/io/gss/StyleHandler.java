package ca.corefacility.gview.style.io.gss;

import java.io.IOException;
import java.net.URI;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;

import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.style.io.gss.exceptions.ParseException;
import ca.corefacility.gview.style.io.gss.exceptions.UnknownPropertyException;

public class StyleHandler implements org.w3c.css.sac.DocumentHandler, com.steadystate.css.sac.DocumentHandlerExt
{
	private MapStyle currentStyle;

	private StyleConverter styleConverter;
	private GSSErrorHandler gssErrorHandler;
	private FilterConverter filterConverter;

	private SelectorList currentSelectors = null;

	private URI sourceURI;

	public StyleHandler(StyleConverter styleConverter, GSSErrorHandler gssErrorHandler, MapStyle mapStyle)
	{
		this(styleConverter, gssErrorHandler, mapStyle, null);
	}

	public StyleHandler(StyleConverter styleConverter, GSSErrorHandler gssErrorHandler, MapStyle mapStyle,
			URI uri)
	{
		this.styleConverter = styleConverter;
		this.currentStyle = mapStyle;
		this.sourceURI = uri;
		this.gssErrorHandler = gssErrorHandler;

		filterConverter = new FilterConverter();
	}

	@Override
	public void comment(String text) throws CSSException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void endDocument(InputSource source) throws CSSException
	{
	}

	@Override
	public void endFontFace() throws CSSException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void endMedia(SACMediaList media) throws CSSException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void endPage(String name, String pseudoPage) throws CSSException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void endSelector(SelectorList selectors) throws CSSException
	{
		this.currentSelectors = null;

		// if this is the end of the "filter" selector
		if (filterConverter.canProcessSelectors(selectors))
		{
			styleConverter.setFilterMap(filterConverter.getFeatureSetMap());
		}
	}

	@Override
	public void ignorableAtRule(String atRule) throws CSSException
	{
		try
		{
			gssErrorHandler.warning(new ParseException(atRule));
		}
		catch (ParseException e)
		{
			throw new CSSException(e);
		}
	}

	@Override
	public void importStyle(String uri, SACMediaList media,
			String defaultNamespaceURI) throws CSSException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void namespaceDeclaration(String prefix, String uri)
			throws CSSException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void property(String name, LexicalUnit value, boolean important)
			throws CSSException
	{
		for (int i = 0; i < currentSelectors.getLength(); i++)
		{
			Selector selector = currentSelectors.item(i);

			if (styleConverter.canProcessSelector(selector))
			{
				try
				{
					styleConverter.decode(currentStyle, selector, name, value, sourceURI);
				}
				catch (UnknownPropertyException e)
				{
					e.setContext(" at \"" + name + " : " + value + "\"");
					try
					{
						gssErrorHandler.warning(e);
					}
					catch (ParseException e1)
					{
						throw new CSSException(e1);
					}
				}
				catch (ParseException e)
				{
					e.setContext(" at \"" + name + " : " + value + "\"");
					try
					{
						gssErrorHandler.error(e);
					}
					catch (ParseException e1)
					{
						throw new CSSException(e1);
					}
				}
				catch (IOException e)
				{
					throw new CSSException(e);
				}
			}
			else if (filterConverter.canProcessSelector(selector))
			{
				filterConverter.decode(currentStyle, selector, name, value, important);
			}
		}
	}

	@Override
	public void startDocument(InputSource source) throws CSSException
	{
		//System.out.println(source.getURI());
	}

	@Override
	public void startFontFace() throws CSSException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void startMedia(SACMediaList media) throws CSSException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void startPage(String name, String pseudoPage) throws CSSException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void startSelector(SelectorList selectors) throws CSSException
	{
		this.currentSelectors = selectors;

		if (styleConverter.canProcessSelectors(selectors))
		{
			try
			{
				styleConverter.startSelector(selectors, currentStyle);
			}
			catch (ParseException e)
			{
				e.setContext("at \"" + selectors + "\" ");
				try
				{
					gssErrorHandler.error(e);
				}
				catch (ParseException e1)
				{
					throw new CSSException(e);
				}
			}
		}

		if (filterConverter.canProcessSelectors(selectors))
		{
			filterConverter.startSelector(selectors, currentStyle);
		}
	}

	@Override
	public void charset(String arg0) throws CSSException
	{
		// TODO Auto-generated method stub

	}
}
