package ca.corefacility.gview.style.io.gss.coders;

import java.awt.Paint;
import java.net.URI;

import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Selector;

import ca.corefacility.gview.style.GlobalStyle;
import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.style.io.FeatureSetMap;
import ca.corefacility.gview.style.io.gss.PaintHandler;
import ca.corefacility.gview.style.io.gss.exceptions.ParseException;
import ca.corefacility.gview.style.io.gss.exceptions.UnknownPropertyException;

public class BackgroundCoder extends GSSCoder
{
	private static final String COLOR_NAME = "color";

	@Override
	protected void encodeProperties(MapStyle style, FeatureSetMap setMap, GSSWriter writer)
	{
		GlobalStyle gStyle = style.getGlobalStyle();
		
		Paint paint = null;
		
		writer.startSelector(getSelectorName());
		
		paint = gStyle.getBackgroundPaint();
		writer.writePropertyPaint(COLOR_NAME, paint);
		
		writer.endSelector();
	}

	@Override
	protected void performDecodeProperty(Selector selector, MapStyle mapStyle,
			FeatureSetMap filtersMap, String name, LexicalUnit value, URI sourceURI) throws ParseException
	{
		GlobalStyle gStyle = mapStyle.getGlobalStyle();
		
		if (name.equals(COLOR_NAME))
		{
			Paint paint;
			paint = PaintHandler.decode(value);
			gStyle.setBackgroundPaint(paint);
		}
		else
		{
			throw new UnknownPropertyException("\"" + name + "\" is not valid for selector \"" + selector +"\"");
		}
	}
	
	

	@Override
	public void startSelector(Selector selector, FeatureSetMap filtersMap, MapStyle mapStyle)
	{
		// TODO Auto-generated method stub
		
	}

	public String getSelectorName()
	{
		return selectorName();
	}
	
	public static String selectorName()
	{
		return "background";
	}
}
