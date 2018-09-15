package ca.corefacility.gview.map.controllers.link;

/**
 * This interface allows objects to be linked.
 * 
 * @author Eric Marinier
 *
 */
public interface Linkable
{
	/**
	 * 
	 * @return The associated link.
	 */
	public Link getLink();
	
	/**
	 * Sets the associated link.
	 * 
	 * @param link
	 */
	public void setLink(Link link);
}
