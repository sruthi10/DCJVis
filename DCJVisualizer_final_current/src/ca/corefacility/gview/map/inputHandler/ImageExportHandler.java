package ca.corefacility.gview.map.inputHandler;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import ca.corefacility.gview.map.GViewMap;
import ca.corefacility.gview.writers.ImageWriter;
import ca.corefacility.gview.writers.ImageWriterFactory;
import ca.corefacility.gview.writers.ImageWriterFilter;

public class ImageExportHandler
{
	private static ImageExportHandler instance = null;
	
	private JFileChooser exportDialog;

	private ImageExportHandler()
	{
		this.exportDialog = makeExporteDialog();
	}
	

	/**
	 * Builds the dialog used to export to an image file.
	 * @return  A JFileChooser used to export to an image file.
	 */
	private JFileChooser makeExporteDialog()
	{
		JFileChooser saveDialog = new JFileChooser();
		saveDialog.setDialogType(JFileChooser.SAVE_DIALOG);

		saveDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
		saveDialog.addChoosableFileFilter(new ImageWriterFilter.JPG());
		saveDialog.addChoosableFileFilter(new ImageWriterFilter.PNG());
	    saveDialog.addChoosableFileFilter(new ImageWriterFilter.SVG());
	    saveDialog.addChoosableFileFilter(new ImageWriterFilter.SVGZ());
	    saveDialog.setFileFilter(saveDialog.getAcceptAllFileFilter());

		return saveDialog;
	}
	
	/**
	 * Determines if we need to add an extension to the original save file to save as the proper file type.
	 * @param originalSaveFile
	 * @param selectedFilter
	 * @return  A File object representing the proper file to save to.
	 */
	private File determineProperSaveFile(File originalSaveFile, javax.swing.filechooser.FileFilter selectedFilter)
	{
	    File saveFile = originalSaveFile;
	    
        if (selectedFilter != null && (selectedFilter instanceof ImageWriterFilter))
        {
            ImageWriterFilter iwFilter = (ImageWriterFilter)selectedFilter;
            String properFileName = iwFilter.getProperFileName(originalSaveFile);
            
            if (originalSaveFile.getName() != null && !originalSaveFile.getName().equals(properFileName))
            {
                File absSaveFile = originalSaveFile.getAbsoluteFile();
                File absSaveFileParent = absSaveFile.getParentFile();
                
                String absSaveFileParentPath = absSaveFileParent.getAbsolutePath();
                if (absSaveFileParentPath == null)
                {
                    absSaveFileParentPath = "";
                }
                
                saveFile = new File(absSaveFileParentPath + File.separatorChar + properFileName);
            }
        }
        else
        {
            // if unknown file type, default to png
            if (!ImageWriterFactory.acceptsFile(originalSaveFile))
            {
                String absSaveFilePath = originalSaveFile.getAbsolutePath();
                saveFile = new File(absSaveFilePath + ".png");
            }
        }
        
        return saveFile;
	}

	public void performExport(GViewMap gviewMap)
	{
		if (gviewMap == null)
		{
			throw new IllegalArgumentException("gviewMap is null");
		}
		
		int chooserState;
		boolean continueToShow = true;

		while (continueToShow)
		{
			continueToShow = false;

			chooserState = this.exportDialog.showDialog(null, "Export");

			if (chooserState == JFileChooser.APPROVE_OPTION)
			{
				File saveFile = this.exportDialog.getSelectedFile();
				saveFile = determineProperSaveFile(saveFile, this.exportDialog.getFileFilter());
				
				// if file to save to already exists
				if (saveFile.exists())
				{
					int choice = JOptionPane.showConfirmDialog(null,
							"File " + saveFile.getName() + " already exists.  Are you sure you wish to overwrite it?",
							"File already exists", JOptionPane.YES_NO_OPTION);

					if (choice == JOptionPane.YES_OPTION)
					{
						continueToShow = false;
						doSave(saveFile, gviewMap);
					}
					else
					{
						continueToShow = true;
					}
				}
				else
				{
					doSave(saveFile, gviewMap);
				}
			}
		}
	}
	
	/**
	 * Performs the actual saving to an image file.
	 * @param saveFile  The File to save the current view to.
	 */
	private void doSave(File saveFile, GViewMap gviewMap)
	{
		ImageWriter writer;
		try
		{
			if (ImageWriterFactory.acceptsFile(saveFile))
			{
				writer = ImageWriterFactory.createImageWriter(saveFile);
				writer.writeToImage(gviewMap, saveFile);
			}
			else
			{
				System.out.println("Unsupported file type " + ImageWriterFactory.extractExtension(saveFile.getName()));
				System.out.print("Extension should be one of ");

				String[] acceptedExtensions = ImageWriterFactory.getAcceptedExtensions();

				for (int i = 0; i < acceptedExtensions.length; i++)
				{
					System.out.print(acceptedExtensions[i] + ",");
				}
				System.out.println("\nDefaulting to png format");

				writer = ImageWriterFactory.createImageWriter("png");
				writer.writeToImage(gviewMap, saveFile.getAbsolutePath() + ".png");
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static ImageExportHandler getInstance()
	{
		if (instance == null)
		{
			instance = new ImageExportHandler();
		}
		
		return instance;
	}
}
