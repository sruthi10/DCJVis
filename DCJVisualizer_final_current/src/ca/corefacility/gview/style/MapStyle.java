package ca.corefacility.gview.style;

import ca.corefacility.gview.map.event.GViewEvent;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.event.GViewEventSubject;
import ca.corefacility.gview.map.event.GViewEventSubjectImp;
import ca.corefacility.gview.map.event.style.StyleChangeEvent;
import ca.corefacility.gview.style.datastyle.DataStyle;

/**
 * The main style class.  This provides access to any information that is used to control the appearence of a map.
 * @author Aaron Petkau
 *
 */
public class MapStyle implements GViewEventListener, GViewEventSubject
{

	private GlobalStyle globalStyle;
	private DataStyle dataStyle;
	
	
	// for events
	private GViewEventSubjectImp eventSubject;
	
	/**
	 * Creates a new object storing all the style information for a map.
	 */
	public MapStyle()
	{
		eventSubject = new GViewEventSubjectImp();
		
		globalStyle = new GlobalStyle();
		dataStyle = new DataStyle();
		
		// listen into any style change events from sub-styles
		globalStyle.addEventListener(this);
		dataStyle.addEventListener(this);
	}
	
	/**
	 * @return  A DataStyle object used to control the appearence of anything related to the genome data.
	 */
	public DataStyle getDataStyle()
	{
		return dataStyle;
	}

	/**
	 * @return  A GlobalStyle object used to control the appearence of anything directly tied to the data (fonts to use, colors, etc).
	 */
	public GlobalStyle getGlobalStyle()
	{
		// TODO Auto-generated method stub
		return globalStyle;
	}

	/**
	 * Sets the data style.
	 * @param dataStyle
	 */
	public void setDataStyle(DataStyle dataStyle)
	{
		// stop listening to events from old style
		this.dataStyle.removeEventListener(this);
		
		// start listening to events from new style
		dataStyle.addEventListener(this);
		
		this.dataStyle = dataStyle;
		
		eventSubject.fireEvent(new StyleChangeEvent(dataStyle));
	}

	/**
	 * Sets the global style.
	 * @param globalStyle
	 */
	public void setGlobalStyle(GlobalStyle globalStyle)
	{
		// stop listening to events from old style
		this.globalStyle.removeEventListener(this);
		
		// start listening to events from new style
		globalStyle.addEventListener(this);
		
		this.globalStyle = globalStyle;
	}

	/**
	 * Event methods
	 */
	
	public void eventOccured(GViewEvent event)
	{
		// forward event to any listeners
		eventSubject.fireEvent(event);
	}

	public void addEventListener(GViewEventListener listener)
	{
		eventSubject.addEventListener(listener);
	}

	public void removeAllEventListeners()
	{
		eventSubject.removeAllEventListeners();
	}

	public void removeEventListener(GViewEventListener listener)
	{
		eventSubject.removeEventListener(listener);
	}
}
