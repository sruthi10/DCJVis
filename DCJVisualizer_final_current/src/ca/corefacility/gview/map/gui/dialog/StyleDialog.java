package ca.corefacility.gview.map.gui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.action.style.ApplyStyleAction;
import ca.corefacility.gview.map.gui.editor.panel.StylePanel;

/**
 * A dialog box for displaying style panels.
 * 
 * @author Eric Marinier
 *
 */
public class StyleDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private static final String OK_COMMAND = "Ok";
	private static final String CANCEL_COMMAND = "Cancel";
	private static final String APPLY_COMMAND = "Apply";
	
	private final GUIController guiController;
	private final StylePanel contentPanel;

	public StyleDialog(GUIController guiController, StylePanel contentPanel) 
	{	
		if(guiController == null)
			throw new IllegalArgumentException("GViewGUIFrame is null.");
		
		if(contentPanel == null)
			throw new IllegalArgumentException("StylePanel is null.");
		
		this.guiController = guiController;		
		this.contentPanel = contentPanel;
		
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		{
			JScrollPane scrollPane = new JScrollPane();
			panel.add(scrollPane);
			{
				scrollPane.setViewportView(contentPanel);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton applyButton = new JButton("Apply");
				applyButton.setActionCommand(APPLY_COMMAND);
				buttonPane.add(applyButton);
				applyButton.addActionListener(this);
			}
			{
				JButton okButton = new JButton("Ok");
				okButton.setActionCommand(OK_COMMAND);
				buttonPane.add(okButton);
				okButton.addActionListener(this);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand(CANCEL_COMMAND);
				buttonPane.add(cancelButton);
				getRootPane().setDefaultButton(cancelButton);
				cancelButton.addActionListener(this);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equals(OK_COMMAND))
		{
			(new ApplyStyleAction(this.guiController)).run();

			this.setVisible(false);
		}
		else if(e.getActionCommand().equals(CANCEL_COMMAND))
		{
			this.setVisible(false);
		}
		else if (e.getActionCommand().equals(APPLY_COMMAND))
		{
			(new ApplyStyleAction(this.guiController)).run();
		}
	}
	
	/**
	 * Updates the content panel.
	 */
	public void updateContentPanel()
	{
		this.contentPanel.update();
	}
}
