package ca.corefacility.gview.map.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import ca.corefacility.gview.utils.ProgressHandler;

/**
 * This class is intended to be used to show the current status of rebuilding operations on the GViewMap object.
 */
public class ProgressNotification extends JDialog implements ProgressNotifier, WindowListener
{
	private static final long serialVersionUID = 1L;
	
	private final JProgressBar progressBar = new JProgressBar(ProgressHandler.MINIMUM_PROGRESS, ProgressHandler.MAXIMUM_PROGRESS);
	
	/**
	 * 
	 * @param parent The component to display the dialog over.
	 */
	public ProgressNotification(JComponent parent)
	{				
		this.setTitle("Working..");
		getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		this.progressBar.setMinimumSize(new Dimension(200, 20));
		this.progressBar.setMaximumSize(new Dimension(200, 20));
		this.progressBar.setPreferredSize(new Dimension(200, 20));
		this.progressBar.setStringPainted(true);
		this.progressBar.setIndeterminate(false);
		
		getContentPane().add(this.progressBar);
		this.pack();

		this.setLocationRelativeTo(parent);
		this.setModal(true);
		this.toFront(); // raise above other java windows
		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setMessage("Working..");
		
		this.addWindowListener(this);
		
		ProgressHandler.addNotifier(this);
	}
	
	@Override
	public void setMessage(String message)
	{
		this.progressBar.setString(message);
	}

	@Override
	public void setProgress(int progress)
	{
		this.progressBar.setValue(progress);
	}

	@Override
	public void windowOpened(WindowEvent e)
	{
		
	}

	@Override
	public void windowClosing(WindowEvent e)
	{
		int response = JOptionPane.showConfirmDialog(this, "Are you sure you wish to quit GView?");
		
		if(response == JOptionPane.OK_OPTION)
		{
			System.exit(0);
		}
	}

	@Override
	public void windowClosed(WindowEvent e)
	{
		
	}

	@Override
	public void windowIconified(WindowEvent e)
	{
		
	}

	@Override
	public void windowDeiconified(WindowEvent e)
	{
		
	}

	@Override
	public void windowActivated(WindowEvent e)
	{
		
	}

	@Override
	public void windowDeactivated(WindowEvent e)
	{
		
	}
}