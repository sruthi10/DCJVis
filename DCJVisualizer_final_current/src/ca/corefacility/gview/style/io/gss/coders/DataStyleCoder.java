package ca.corefacility.gview.style.io.gss.coders;


import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Selector;

import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.style.io.FeatureSetMap;

public abstract class DataStyleCoder
{
	/**
	 * Encodes the appropriate information from the style object and writes it to the passed PrintWriter.
	 * @param style
	 * @param writer
	 */
	public void encodeSelector(MapStyle style, FeatureSetMap setMap, GSSWriter writer)
	{
		if (style == null)
		{
			throw new NullPointerException("style is null");
		}
		
		if (writer == null)
		{
			throw new NullPointerException("writer is null");
		}
		
		encodeProperties(style, setMap, writer);
	}

	public void decodeProperty(Selector selector, MapStyle mapStyle, String name, LexicalUnit value, boolean important)
	{
		if (mapStyle == null)
		{
			throw new NullPointerException("mapStyle is null");
		}
		
		if (name == null)
		{
			throw new NullPointerException("name is null");
		}
		
		if (value == null)
		{
			throw new NullPointerException("value is null");
		}
		
		if (selector == null)
		{
			throw new NullPointerException("selector is null");
		}
		
		performDecodeProperty(selector, mapStyle, name, value, important);
	}
	
	protected abstract void performDecodeProperty(Selector selector, MapStyle mapStyle,
			String name, LexicalUnit value, boolean important);

	/**
	 * Performs the actual encoding of the properties of the selector.
	 * @param style
	 * @param writer
	 */
	protected abstract void encodeProperties(MapStyle style, FeatureSetMap setMap, GSSWriter writer);
	
	/**
	 * @return  The name of the ElementSelector that this instance of GSSCoder can encode/decode.
	 */
	public abstract String getSelectorName();
}
