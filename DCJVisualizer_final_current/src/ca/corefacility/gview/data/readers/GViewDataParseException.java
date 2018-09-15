package ca.corefacility.gview.data.readers;

/**
 * An exception thrown when there is a parsing error for the genome data.
 * @author aaron
 *
 */
public class GViewDataParseException extends Exception
{
    private static final long serialVersionUID = 1L;

    public GViewDataParseException()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public GViewDataParseException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public GViewDataParseException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	public GViewDataParseException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
