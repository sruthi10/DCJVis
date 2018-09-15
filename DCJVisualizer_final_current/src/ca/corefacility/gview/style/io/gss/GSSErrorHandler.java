package ca.corefacility.gview.style.io.gss;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;

import ca.corefacility.gview.style.io.gss.exceptions.ParseException;

public class GSSErrorHandler implements ErrorHandler
{
	private String getErrorHeader(CSSParseException e)
	{
		String locationString = "line:" + e.getLineNumber() + " column:" + e.getColumnNumber() + ": ";
		
		return locationString;
	}

	@Override
	public void warning(CSSParseException e) throws CSSException
	{
		warning(getErrorHeader(e) + e);
	}

	@Override
	public void error(CSSParseException e) throws CSSException
	{
		error(getErrorHeader(e) + e);
	}

	@Override
	public void fatalError(CSSParseException e) throws CSSException
	{
		fatalError(getErrorHeader(e) + e);
	}
	
	public void warning(ParseException e) throws ParseException
	{
		warning(e.toString());
	}
	
	public void error(ParseException e) throws ParseException
	{
		error(e.toString());
	}
	
	public void fatalError(ParseException e) throws ParseException
	{
		fatalError(e.toString());
	}

	private void warning(String message)
	{
		System.err.println("[warning] - " + message);
	}
	
	private void error(String message)
	{
		System.err.println("[error] - " + message);
	}
	
	private void fatalError(String message)
	{
		System.err.println("[fatal error] - " + message);
	}
}
