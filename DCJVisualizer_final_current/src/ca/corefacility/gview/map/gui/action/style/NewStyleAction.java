package ca.corefacility.gview.map.gui.action.style;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import ca.corefacility.gview.data.GenomeDataImp;
import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.Style;
import ca.corefacility.gview.utils.thread.ThreadService;

/**
 * New style action.
 * 
 * @author Eric Marinier
 *
 */
public abstract class NewStyleAction extends StyleEditorAction
{
	private final GUIController guiController;
	
	public NewStyleAction(GUIController guiController)
	{		
		if(guiController == null)
			throw new IllegalArgumentException("GUIController is null.");
		
		this.guiController = guiController;
	}
	
	@Override
	public void undo() throws CannotUndoException
	{
		throw new CannotUndoException();
	}

	@Override
	public void redo() throws CannotRedoException 
	{
		throw new CannotRedoException();
	}
	
	@Override
	public boolean canUndo() 
	{
		return false;
	}
	
	@Override
	public boolean canRedo() 
	{
		return false;
	}

	@Override
	public void run()
	{
		if(this.guiController.getCurrentStyle().isXML())
		{
			GUIUtility.displayXMLNotification(this.guiController.getStyleEditor());
			return;
		}
		
		GenomeDataImp genomeData = new GenomeDataImp(this.guiController.getCurrentStyle().getGenomeData().getSequence());
		final Style style = createStyle(genomeData);
		
		if(style == null)
			throw new NullPointerException("Style is null.");
		
		ThreadService.executeOnEDT(new Runnable()
		{
			@Override
			public void run()
			{
				//Add the style
				guiController.addStyle(style);
			}			
		});		
	}
	
	/**
	 * Creates the actual style.
	 * 
	 * @param genomeData The associated genome data.
	 * 
	 * @return The actual style to return when the action is run.
	 */
	public abstract Style createStyle(GenomeDataImp genomeData);
}
