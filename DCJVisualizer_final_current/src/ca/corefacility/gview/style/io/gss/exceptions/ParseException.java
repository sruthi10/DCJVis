package ca.corefacility.gview.style.io.gss.exceptions;

public class ParseException extends Exception
{
    private static final long serialVersionUID = 1L;
    
    private String context = null;

	public ParseException()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public ParseException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ParseException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ParseException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	public void setContext(String context)
	{
		this.context = context;
	}
	
	@Override
	public String toString()
	{
	    String message = super.toString();

		if (context != null)
		{
			message += " - " + context;
		}
		
		if (getCause() != null)
		{
		    message += " - " + getCause();
		}
		
		return message;
	}
}
