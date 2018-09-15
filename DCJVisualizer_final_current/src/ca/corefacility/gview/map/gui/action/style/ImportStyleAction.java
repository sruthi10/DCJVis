package ca.corefacility.gview.map.gui.action.style;

import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import ca.corefacility.gview.data.GenomeDataImp;
import ca.corefacility.gview.map.GViewMap;
import ca.corefacility.gview.map.GViewMapImp;
import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.Style;
import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;
import ca.corefacility.gview.map.gui.open.StyleDataFilter;
import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.style.io.StyleIOGSS;

public class ImportStyleAction extends StyleEditorAction
{
	private final GUIController guiController;
	
	public ImportStyleAction(GUIController guiController)
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
		
		StyleIOGSS styleIO = new StyleIOGSS();
		
		JFileChooser styleChooser = new JFileChooser(this.guiController.getCurrentDirectory());		
		final File styleFile;
		
		final GenomeDataImp genomeData = new GenomeDataImp(this.guiController.getCurrentStyle().getGenomeData().getSequence());
		final MapStyle mapStyle;
		
		styleChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		styleChooser.setFileFilter(new StyleDataFilter());
		
		try
		{			
			if(styleChooser.showOpenDialog(this.guiController.getStyleEditor()) == JFileChooser.APPROVE_OPTION 
				&&  styleChooser.getSelectedFile() != null)
			{
				styleFile = styleChooser.getSelectedFile();
				
				this.guiController.setCurrentDirectory(styleChooser.getCurrentDirectory());				
				mapStyle = styleIO.readMapStyle(styleFile);
				
				if(mapStyle != null)
				{
					this.guiController.displayProgressWhileRebuilding(new Runnable()
					{
						@Override
						public void run()
						{
							final Style style;	
							final GViewMap gViewMap;
							
							gViewMap = new GViewMapImp(genomeData, mapStyle, StyleEditorUtility.DEFAULT_LAYOUT);
							style = new Style(gViewMap, styleFile.getName());
							
							guiController.addStyle(style);
						}						
					});	
				}
			}
		}
		catch (HeadlessException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
