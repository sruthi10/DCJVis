package ca.corefacility.gview.map.gui.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JOptionPane;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.event.GViewEvent;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.event.ZoomNormalEvent;
import ca.corefacility.gview.map.gui.GUIUtility;

/**
 * The custom scale dialog box. Extends the abstract SliderDialog class.
 * 
 * @author Eric Marinier
 *
 */
public class ScaleDialog extends SliderDialog implements ActionListener, GViewEventListener
{
	private static final long serialVersionUID = 1L;	//requested by java
	
	private static final DecimalFormat formatter = new DecimalFormat("###,###.###");

	public ScaleDialog(GUIController guiController)
	{
		super(guiController, (int)Math.round( (GUIUtility.SCALE_MIN * GUIUtility.SCALE_FACTOR)), 
				(int)Math.round( (GUIUtility.SCALE_MAX * GUIUtility.SCALE_FACTOR)), 
				-1, "Enter a value between " + formatter.format((int)Math.round((GUIUtility.SCALE_MIN * GUIUtility.SCALE_FACTOR)))
				+ "% and " + formatter.format((int)Math.round((GUIUtility.SCALE_MAX * GUIUtility.SCALE_FACTOR))) + "%:");
		
		if(guiController == null)
			throw new IllegalArgumentException("GViewGUIFrame is null.");
		
		guiController.addEventListener(this);
		this.setTitle("Custom Scale");
	}
	
	/**
	 * Shows the dialog.
	 */
	public void showScaleDialog()
	{		
		double scaleFactor = super.guiController.getCurrentStyleMapManager().getZoomNormalFactor() * GUIUtility.SCALE_FACTOR;
		
		//Check to see if the scaleFactor is valid.
		if( scaleFactor >= MIN && scaleFactor <= MAX)
		{
			SLIDER.setValue((int)Math.round(scaleFactor));
			TEXT_FIELD.setText((int)Math.round(scaleFactor) + "");
		}
		else if (scaleFactor < MIN)
		{
			SLIDER.setValue(MIN);
			TEXT_FIELD.setText(MIN + "");
		}
		else if (scaleFactor > MAX)
		{
			SLIDER.setValue(MAX);
			TEXT_FIELD.setText(MAX + "");			
		}
		
		TEXT_FIELD.selectAll();
	}
	
	/**
	 * Listens for the buttons.
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		double scaleFactor;
		
		//OK button pressed.
		if(OK.equals(e.getActionCommand()))
		{	
			//try to parse the string as a double
			try
			{
				scaleFactor = Double.parseDouble(getCleanInput());
			}
			catch(NumberFormatException nfe)
			{
				//set to an invalid value
				scaleFactor = INVALID;				
			}
			
			//check to see if the input was invalid
			if(scaleFactor < MIN || scaleFactor > MAX)
			{
				JOptionPane.showMessageDialog(null, TEXT_FIELD.getText() + " is not within " + MIN + " to " + MAX, "Invalid Number", JOptionPane.WARNING_MESSAGE);
				
				//reset the text field to the slider's value
				TEXT_FIELD.setText(SLIDER.getValue() + "");
			}
			else
			//valid input
			{
				super.guiController.getCurrentStyleMapManager().zoomNormal(scaleFactor / GUIUtility.SCALE_FACTOR);				
				setVisible(false);			
			}
		}
		//Cancel button pressed.
		else if(CANCEL.equals(e.getActionCommand()))
		{
			setVisible(false);
		}
	}

	@Override
	/**
	 * Listens for a ZoomNormalEvent and updates its slider to the current scale level.
	 */
	public void eventOccured(GViewEvent event) 
	{
		if(event instanceof ZoomNormalEvent)
		{
			SLIDER.setValue((int)Math.round( (super.guiController.getCurrentStyleMapManager().getZoomNormalFactor() * GUIUtility.SCALE_FACTOR) ) );
		}
		
	}
}
