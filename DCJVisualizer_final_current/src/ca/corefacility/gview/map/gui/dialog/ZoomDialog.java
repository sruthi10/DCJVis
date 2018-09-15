package ca.corefacility.gview.map.gui.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JOptionPane;

import ca.corefacility.gview.layout.sequence.ZoomException;
import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.event.GViewEvent;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.event.ZoomEvent;
import ca.corefacility.gview.map.gui.GUIUtility;

/**
 * The custom zoom dialog box. Extends the abstract SliderDialog class.
 * 
 * @author Eric Marinier
 *
 */
public class ZoomDialog extends SliderDialog implements ActionListener, GViewEventListener
{	
	private static final long serialVersionUID = 1L;
	
	private static final DecimalFormat formatter = new DecimalFormat("###,###.###");
	
	public ZoomDialog(GUIController guiController) 
	{	
		super(guiController, GUIUtility.ZOOM_MIN * GUIUtility.SCALE_FACTOR, 
				GUIUtility.ZOOM_MAX * GUIUtility.SCALE_FACTOR, -1, 
				"Enter a value between " + formatter.format(GUIUtility.ZOOM_MIN * GUIUtility.SCALE_FACTOR) 
				+ "% and " + formatter.format(GUIUtility.ZOOM_MAX * GUIUtility.SCALE_FACTOR) + "%:");
		
		guiController.addEventListener(this);
		this.setTitle("Custom Zoom");
	}
	
	/**
	 * Shows the dialog.
	 */
	public void showZoomDialog()
	{		
		this.update();
		TEXT_FIELD.selectAll();
	}
	
	/**
	 * Listens for the buttons.
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		double zoomFactor;
		
		//OK button pressed.
		if(OK.equals(e.getActionCommand()))
		{	
			//try to parse the string as a double
			try
			{
				zoomFactor = Double.parseDouble(getCleanInput());
			}
			catch(NumberFormatException nfe)
			{
				//set to an invalid value
				zoomFactor = INVALID;				
			}
			
			//check to see if the input was invalid
			if(zoomFactor < MIN || zoomFactor > MAX)
			{
				JOptionPane.showMessageDialog(null, TEXT_FIELD.getText() + " is not within " + MIN + " to " + MAX, "Invalid Number", JOptionPane.WARNING_MESSAGE);
				
				//reset the text field to the slider's value
				TEXT_FIELD.setText(SLIDER.getValue() + "");
			}
			else
			//valid input
			{
				try
                {
                    super.guiController.getCurrentStyleMapManager().setZoomFactor(zoomFactor/GUIUtility.SCALE_FACTOR);
                    setVisible(false);          
                }
				catch (ZoomException e1)
                {
	                JOptionPane.showMessageDialog(null, e1.getMessage(), "Invalid Number", JOptionPane.WARNING_MESSAGE);
                }				
			}
		}
		//Cancel button pressed.
		else if(CANCEL.equals(e.getActionCommand()))
		{
			setVisible(false);
		}
	}
	
	private void update(double zoomFactor)
	{
		//Check to see if the zoomFactor is valid.
		if( zoomFactor >= MIN && zoomFactor <= MAX)
		{
			SLIDER.setValue((int)zoomFactor);
			TEXT_FIELD.setText((int)zoomFactor + "");
		}
		else if (zoomFactor < MIN)
		{
			SLIDER.setValue(MIN);
			TEXT_FIELD.setText(MIN + "");
		}
		else if (zoomFactor > MAX)
		{
			SLIDER.setValue(MAX);
			TEXT_FIELD.setText(MAX + "");			
		}
	}
	
	public void update()
	{
		double zoomFactor = super.guiController.getCurrentStyleMapManager().getZoomFactor() * GUIUtility.SCALE_FACTOR;	//Needs to be scaled by a factor of 100.

		update(zoomFactor);
	}
	
	private void update(ZoomEvent event)
	{
		double zoomFactor = event.getScale();
		
		update(zoomFactor);
	}

	@Override
	public void eventOccured(GViewEvent event)
	{
		if(event instanceof ZoomEvent)
		{
			update((ZoomEvent)event);
		}
	}
}
