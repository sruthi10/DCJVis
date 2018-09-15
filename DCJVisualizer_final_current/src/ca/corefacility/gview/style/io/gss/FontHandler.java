package ca.corefacility.gview.style.io.gss;


import java.awt.Font;

import org.w3c.css.sac.LexicalUnit;

public class FontHandler
{
	private static final String FONT_FUNCTION = "font";
	
	private static final String PLAIN = "plain";
	private static final String BOLD = "bold";
	private static final String ITALIC = "italic";
	private static final String BOLD_ITALIC = "bold-italic";
	
	private static final int INVALID_STYLE = -1;
	
	public static String encode(Font font)
	{
		if (font == null)
		{
			throw new NullPointerException("font is null");
		}
		
		return FONT_FUNCTION + "(\"" + font.getName() + "\",\"" + getStyleString(font.getStyle()) + "\"," +
				font.getSize() + ")";
	}
	
	public static class MalformedFontStringException extends Exception
	{
        private static final long serialVersionUID = 1L;

        public MalformedFontStringException()
		{
			super();
			// TODO Auto-generated constructor stub
		}

		public MalformedFontStringException(String message, Throwable cause)
		{
			super(message, cause);
			// TODO Auto-generated constructor stub
		}

		public MalformedFontStringException(String message)
		{
			super(message);
			// TODO Auto-generated constructor stub
		}

		public MalformedFontStringException(Throwable cause)
		{
			super(cause);
			// TODO Auto-generated constructor stub
		}
	}
	
	public static Font decode(LexicalUnit font) throws MalformedFontStringException
	{
		if (font == null)
		{
			throw new NullPointerException("font is null");
		}

		Font fontObj = null;
		
		String name;
		int style;
		int size;
		
		String fontString = font.toString();
		
		if (FONT_FUNCTION.equals(font.getFunctionName()))
		{
			LexicalUnit parameters = font.getParameters();
			
			if (parameters != null)
			{
				LexicalUnit currUnit = parameters;
				
				// the font name
				if (currUnit.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE)
				{
					name = currUnit.getStringValue();
					
					currUnit = currUnit.getNextLexicalUnit();
					
					if (currUnit != null)
					{
						// should be a comma here
						if (LexicalUnit.SAC_OPERATOR_COMMA == currUnit.getLexicalUnitType())
						{		
							currUnit = currUnit.getNextLexicalUnit();
							
							if (LexicalUnit.SAC_STRING_VALUE == currUnit.getLexicalUnitType())
							{
								style = getStyleInt(currUnit.getStringValue());
								
								if (style != INVALID_STYLE)
								{
									currUnit = currUnit.getNextLexicalUnit();
		
									if (LexicalUnit.SAC_OPERATOR_COMMA == currUnit.getLexicalUnitType())
									{	
										currUnit = currUnit.getNextLexicalUnit();
										if (LexicalUnit.SAC_INTEGER == currUnit.getLexicalUnitType())
										{
											size = currUnit.getIntegerValue();
											
											if (size > 0)
											{
												fontObj = new Font(name, style, size);
											}
											else
											{
												throw new MalformedFontStringException("\"" + fontString + "\": Negative point size");
											}
										}
										else
										{
											throw new MalformedFontStringException("\"" + fontString + "\": Point size not integer");
										}
									}
									else
									{
										throw new MalformedFontStringException("\"" + fontString + "\": Malformed font string");
									}
								}
								else
								{
									throw new MalformedFontStringException("\"" + fontString + "\": Invalid font style");
								}
							}
							else
							{
								throw new MalformedFontStringException("\"" + fontString + "\": Invalid font style");
							}
						}
						else
						{
							throw new MalformedFontStringException("\"" + fontString + "\": Malformed font string");
						}
					}
					else
					{
						throw new MalformedFontStringException("\"" + fontString + "\": No parameters for font");
					}
				}
				else
				{
					throw new MalformedFontStringException("\"" + fontString + "\": No name for font");
				}
			}
			else
			{
				throw new MalformedFontStringException("\"" + fontString + "\": No parameters for font");
			}
		}
		else
		{
			throw new MalformedFontStringException("\"" + fontString + "\": Incorrect function name");
		}
		
		return fontObj;
	}
	
	private static String getStyleString(int fontStyle)
	{
		if (fontStyle == Font.PLAIN)
		{
			return PLAIN;
		}
		else if (fontStyle == Font.BOLD)
		{
			return BOLD;
		}
		else if (fontStyle == Font.ITALIC)
		{
			return ITALIC;
		}
		else if (fontStyle == (Font.BOLD + Font.ITALIC))
		{
			return BOLD_ITALIC;
		}
		
		return null;
	}
	
	private static int getStyleInt(String style)
	{
		if (PLAIN.equals(style))
		{
			return Font.PLAIN;
		}
		else if (BOLD.equals(style))
		{
			return Font.BOLD;
		}
		else if (ITALIC.equals(style))
		{
			return Font.ITALIC;
		}
		else if (BOLD_ITALIC.equals(style))
		{
			return Font.BOLD + Font.ITALIC;
		}
		
		return INVALID_STYLE;
	}
}
