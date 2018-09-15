package ca.corefacility.gview.map.gui.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JOptionPane;

import ca.corefacility.gview.map.controllers.GUIController;

/**
 * The custom move dialog box. Extends the abstract SliderDialog class.
 * 
 * @author Eric Marinier
 *
 */
public class MoveDialog extends SliderDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;	//requested by java
	
	private static final DecimalFormat formatter = new DecimalFormat("###,###.###");

	public MoveDialog(GUIController guiController) 
	{	
		super(guiController, 0, 
				guiController.getCurrentStyleMapManager().getMaxSequenceLength(), -1, 
				"Enter a location between " + formatter.format(0) + " bp and " 
				+ formatter.format(guiController.getCurrentStyleMapManager().getMaxSequenceLength()) + " bp:");
		
		this.setTitle("Custom Move");
	}
	
	/**
	 * Shows the dialog.
	 */
	public void showMoveDialog()
	{
		double moveLocation = super.guiController.getCurrentStyleMapManager().getCenterBaseValue();
		
		//Check to see if the move location is valid.
		if( moveLocation >= MIN && moveLocation <= super.guiController.getCurrentStyleMapManager().getMaxSequenceLength())
		{
			SLIDER.setValue((int)moveLocation);
			TEXT_FIELD.setText((int)moveLocation + "");
		}
		else if (moveLocation < MIN)
		{
			SLIDER.setValue(MIN);
			TEXT_FIELD.setText(MIN + "");
		}
		else if (moveLocation > super.guiController.getCurrentStyleMapManager().getMaxSequenceLength())
		{
			SLIDER.setValue(super.guiController.getCurrentStyleMapManager().getMaxSequenceLength());
			TEXT_FIELD.setText(super.guiController.getCurrentStyleMapManager().getMaxSequenceLength() + "");			
		}
		
		TEXT_FIELD.selectAll();
	}
	
	@Override
	/**
	 * Listens for the buttons.
	 */
	public void actionPerformed(ActionEvent e) 
	{
		double moveLocation;
		
		//OK button pressed.
		if(OK.equals(e.getActionCommand()))
		{	
			try
			{
				//Try to parse the string to a double.
				moveLocation = Double.parseDouble(getCleanInput());
			}
			catch(NumberFormatException nfe)
			{
				//Set to invalid input.
				moveLocation = INVALID;				
			}
			
			//Check for invalid input.
			if(moveLocation < MIN || moveLocation > super.guiController.getCurrentStyleMapManager().getMaxSequenceLength())
			{
				JOptionPane.showMessageDialog(null, TEXT_FIELD.getText() + " is not within " + MIN + " to " + super.guiController.getCurrentStyleMapManager().getMaxSequenceLength(), "Invalid Number", JOptionPane.WARNING_MESSAGE);
				TEXT_FIELD.setText(SLIDER.getValue() + "");
			}
			//Valid input.
			else
			{
				super.guiController.getCurrentStyleMapManager().setCenter((int)moveLocation);			
				setVisible(false);			
			}
		}
		//Cancel button pressed.
		else if(CANCEL.equals(e.getActionCommand()))
		{
			setVisible(false);
		}
	}
}
