package ca.corefacility.gview.map.gui.action.style;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import ca.corefacility.gview.map.controllers.GUIController;

public class ApplyStyleAction extends StyleAction
{
	private final GUIController guiController;
	
	public ApplyStyleAction(GUIController guiController)
	{
		if(guiController == null)
			throw new IllegalArgumentException("GUIController is null");	
		
		this.guiController = guiController;
	}
	
	@Override
	public void undo() throws CannotUndoException
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void redo() throws CannotRedoException
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void run()
	{
		this.guiController.rebuildStyle();		
	}
}
