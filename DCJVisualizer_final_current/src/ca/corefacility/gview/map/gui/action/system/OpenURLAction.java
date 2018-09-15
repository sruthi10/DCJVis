package ca.corefacility.gview.map.gui.action.system;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * Open URL action.
 * 
 * Opens the URL.
 * 
 * @author Eric Marinier
 *
 */
public class OpenURLAction extends SystemAction 
{
	private final String website;
	
	public OpenURLAction(String website)
	{
		if(website == null)
			throw new IllegalArgumentException("Website is null.");
		
		this.website = website;
	}
	
	@Override
	public void run() 
	{
        if(java.awt.Desktop.isDesktopSupported())
        {
        	Desktop desktop = Desktop.getDesktop();
        	if (desktop.isSupported(Desktop.Action.BROWSE))
        	{
        		try
				{
        			if(website != null)
        			{
        				URI uri = new URI(this.website);
    					desktop.browse(uri);
        			}        			
				}
        		catch (URISyntaxException e1)
				{
					e1.printStackTrace();
				}
        		catch (IOException e1)
				{
					e1.printStackTrace();
				}
        	}
        }
	}
}
