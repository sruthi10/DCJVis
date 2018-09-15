package ca.corefacility.gview.map.controllers.selection;

import java.util.List;

/**
 * This interface is intended to provide means for interfacing with selectable
 * objects.
 * 
 * @author Eric Marinier
 * 
 */
public interface Selectable
{
	public List<SelectionListener> getSelection();
}
