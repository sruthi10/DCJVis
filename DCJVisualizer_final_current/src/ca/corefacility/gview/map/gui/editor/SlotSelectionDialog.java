package ca.corefacility.gview.map.gui.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

/**
 * This dialog is used when the user needs to select a slot number.
 * 
 * @author Eric Marinier
 *
 */
public class SlotSelectionDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();	
	private final JComboBox comboBox = new JComboBox();
	
	private Integer slotNumber = null;

	private static final String CANCEL = "Cancel";
	private static final String OK = "Ok";

	/**
	 * Create the dialog.
	 */
	public SlotSelectionDialog(int low, int high)
	{
		if(low > high)
		{
			low = high;
		}
		else if(high < low)
		{
			high = low;
		}
		
		this.setTitle("Slot:");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 200, 170);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(null);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.NORTH);
			{
				JLabel lblSelectASlot = new JLabel("Select a slot number:");
				panel.add(lblSelectASlot);
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.CENTER);
			{
				panel.add(comboBox);
				comboBox.setPreferredSize(new Dimension(120, 24));
				{
					JSeparator separator = new JSeparator();
					contentPanel.add(separator, BorderLayout.SOUTH);
				}
				
				//So after they're added the order makes sense:
				for(int i = high; i >= low; i--)
				{
					if(i != 0)
					{
						comboBox.addItem(i);
					}
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton(OK);
				okButton.setActionCommand(OK);
				okButton.addActionListener(this);
				
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton(CANCEL);
				cancelButton.setActionCommand(CANCEL);
				cancelButton.addActionListener(this);
				
				buttonPane.add(cancelButton);
			}
		}
		
		this.pack();
	}
	
	/**
	 * 
	 * @return The selected slot number or NULL if invalid.
	 */
	private Integer getSelection()
	{
		Object tempNumber = this.comboBox.getSelectedItem();
		Integer slotNumber = null;
		
		if (tempNumber != null && tempNumber instanceof Integer)
		{
				slotNumber = (Integer)tempNumber;
		}
		
		return slotNumber;
	}
	
	/**
	 * 
	 * @return The selected slot number or NULL if cancelled or invalid.
	 */
	public Integer getSlotNumber()
	{
		return this.slotNumber;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals(CANCEL))
		{
			this.slotNumber = null;
		}
		else if(e.getActionCommand().equals(OK))
		{
			this.slotNumber = getSelection();
		}
		
		this.setVisible(false);
	}
}
