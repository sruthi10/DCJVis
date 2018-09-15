package ca.corefacility.gview.map.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;

import javax.swing.BoxLayout;

import ca.corefacility.gview.map.gui.GUIUtility;

/**
 * Creates the about dialog.
 * 
 * @author Eric Marinier
 *
 */
public class AboutDialog extends JDialog implements HyperlinkListener, ActionListener
{
	private static final long serialVersionUID = 1L;	//requested by java
	
	private final JPanel contentPanel = new JPanel();
	private final JTextPane textPane = new JTextPane();

	/**
	 * Create the dialog.
	 */
	public AboutDialog(JFrame frame) 
	{
		super(frame, "About GView");
		
		if(frame == null)
			throw new IllegalArgumentException("JFrame is null.");
		
		this.setBounds(100, 100, 450, 450);
		this.getContentPane().setLayout(new BorderLayout());
		
		createContentPanel();
		createTextPane();
		createButtonPane();
		
		this.pack();
	}
	
	/**
	 * Creates the button pane.
	 */
	private void createButtonPane()
	{
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));

		JButton okButton = new JButton("OK");
		okButton.addActionListener(this);
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		
		getRootPane().setDefaultButton(okButton);	
		
		this.getContentPane().add(buttonPane, BorderLayout.SOUTH);
	}

	/**
	 * Creates the text pane.
	 */
	private void createTextPane()
	{
		this.textPane.addHyperlinkListener(this);		
		this.textPane.setEditable(false);
		this.textPane.setEditorKit(new HTMLEditorKit());
		this.textPane.setText(GUIUtility.ABOUT_DIALOG_TEXT);
		
		this.contentPanel.add(this.textPane);		
	}

	/**
	 * Creates the content panel.
	 */
	private void createContentPanel()
	{
		this.contentPanel.setLayout(new BorderLayout());
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));		
		this.contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		
		this.getContentPane().add(this.contentPanel, BorderLayout.CENTER);
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) 
	{
		if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
        {
			if (java.awt.Desktop.isDesktopSupported())
			{
	        	Desktop desktop = Desktop.getDesktop();
	        	if (desktop.isSupported(Desktop.Action.BROWSE))
	        	{       		
	        		try
					{
						URI usageURI = e.getURL().toURI();
						desktop.browse(usageURI);
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
		else if (e.getEventType() == HyperlinkEvent.EventType.ENTERED)
		{
			this.textPane.setToolTipText(e.getURL().toString());
		}
		else if (e.getEventType() == HyperlinkEvent.EventType.EXITED)
		{
			this.textPane.setToolTipText(null);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equals("OK"))
		{
			this.setVisible(false);
		}
	}
}
