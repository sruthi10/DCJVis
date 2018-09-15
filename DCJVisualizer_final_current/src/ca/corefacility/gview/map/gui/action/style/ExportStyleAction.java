package ca.corefacility.gview.map.gui.action.style;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.open.StyleDataFilter;

public class ExportStyleAction extends StyleEditorAction
{
	private static final String FILE_EXTENSION = ".gss";	
	private final GUIController guiController;
	
	public ExportStyleAction(GUIController guiController)
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
		
		JFileChooser styleChooser = new JFileChooser(this.guiController.getCurrentDirectory());	
		styleChooser.setSelectedFile(new File(this.guiController.getCurrentStyle().getName()));	//TODO: does this act as intended?
		styleChooser.setFileFilter(new StyleDataFilter());
		
		File styleFile;		
		
		//Approved
		if(styleChooser.showSaveDialog(this.guiController.getStyleEditor()) == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				//Set current directory
				this.guiController.setCurrentDirectory(styleChooser.getCurrentDirectory());
				
				//Grab the file
				styleFile = styleChooser.getSelectedFile();
				
				//Auto append file extension
				if(!styleFile.getName().endsWith(FILE_EXTENSION))
				{
					styleFile = new File(styleFile.getAbsolutePath() + FILE_EXTENSION);
				}
				
				if(styleFile.exists())
				{
					//Overwrite?
					if(JOptionPane.showConfirmDialog(this.guiController.getStyleEditor(), "Are you sure you want to overwrite this file?") == JOptionPane.YES_OPTION)
					{
						this.guiController.writeCurrentStyleToFile(styleFile);						
					}
				}
				else
				//does not exist
				{
					this.guiController.writeCurrentStyleToFile(styleFile);	
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
