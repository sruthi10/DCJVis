package ca.corefacility.gview.map.inputHandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ca.corefacility.gview.map.GViewMap;

/**
 * A class used to export the current view of the map to an image.
 * @author aaron
 *
 */
public class ExportImage
{
	private JFileChooser saveDialog;
	
	private JTextField filenameField;
	private JButton browseButton;
	private String browseActionCommand = "browse";
	
	private JPanel filenamePanel;
	
	private String activeDirectory = "Choose file...";
	private File currentSelectedFile = null;
	
	public ExportImage()
	{
		buildComponents();
	}
	
	public void exportView(GViewMap gviewMap)
	{
		createExport();
	}
	
	private void buildComponents()
	{
		filenamePanel = new JPanel();
		
		saveDialog = new JFileChooser();
		
		browseButton = createBrowseButton();
		activeDirectory = System.getProperty("user.dir");
		filenameField = new JTextField(20);
		filenameField.setText(activeDirectory);
		
		filenamePanel.add(filenameField);
		filenamePanel.add(browseButton);		
	}
	
	private JFrame createExport()
	{
		JFrame mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainFrame.getContentPane().add(filenamePanel);
		
		mainFrame.pack();
		mainFrame.setVisible(true);
		
		return mainFrame;
	}
	
	private JButton createBrowseButton()
	{
		JButton button = new JButton("Browse...");
		
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (browseActionCommand.equals(e.getActionCommand()))
				{
					int chooserState = saveDialog.showDialog(null, "Export to");
					
					if (chooserState == JFileChooser.APPROVE_OPTION)
					{
						currentSelectedFile = saveDialog.getSelectedFile();
						filenameField.setText(currentSelectedFile.getAbsolutePath());
					}
				}
			}
		});
		button.setActionCommand(browseActionCommand);
		
		return button;
	}
	
	public static void main(String[] args)
	{
		ExportImage export = new ExportImage();
		export.exportView(null);
	}
}
