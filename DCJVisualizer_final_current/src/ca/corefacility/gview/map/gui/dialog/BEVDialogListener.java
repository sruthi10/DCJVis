package ca.corefacility.gview.map.gui.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.corefacility.gview.map.BirdsEyeViewImp;
import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.event.GViewEvent;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.event.ZoomNormalEvent;
import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.action.map.scale.ScaleInAction;
import ca.corefacility.gview.map.gui.action.map.scale.ScaleOutAction;

/**
 * A custom listener class for the BEV Dialog.
 * 
 * @author Eric Marinier
 *
 */
public class BEVDialogListener implements WindowListener, ComponentListener, ChangeListener, GViewEventListener, ActionListener
{
	private final GUIController guiController;
	
	private final BirdsEyeViewImp BEV;	//The actual BEV object.
	private final JSlider slider;
	
	private boolean zoomEventFromGView;	//Needed internally to prevent thrashing of the slider and zoom level.

	public BEVDialogListener(GUIController guiController, BirdsEyeViewImp BEV, JSlider slider)
	{
		if(guiController == null)
			throw new IllegalArgumentException("GUIController is null.");
		
		if(slider == null)
			throw new IllegalArgumentException("JSlider is null.");
		
		if(BEV == null)
			throw new IllegalArgumentException("BirdsEyeView is null.");
		
		this.guiController = guiController;
		this.guiController.addEventListener(this);
		
		this.BEV = BEV;
		this.slider = slider;
	}
	
	@Override
	/**
	 * Updates the BEV Map.
	 */
	public void componentResized(ComponentEvent e) 
	{
		this.BEV.updateFromViewed();			
	}

	@Override
	/**
	 * Updates the BEV Map.
	 */
	public void componentMoved(ComponentEvent e) 
	{
		this.BEV.updateFromViewed();	
	}

	@Override
	/**
	 * Updates the BEV Map.
	 */
	public void componentShown(ComponentEvent e) 
	{
		this.BEV.updateFromViewed();	
	}

	@Override
	/**
	 * Updates the BEV Map.
	 */
	public void componentHidden(ComponentEvent e) 
	{
		this.BEV.updateFromViewed();	
	}

	@Override
	/**
	 * Updates the BEV Map.
	 */
	public void windowOpened(WindowEvent e) 
	{
		this.BEV.updateFromViewed();	
	}

	@Override
	/**
	 * Updates the BEV Map.
	 */
	public void windowClosing(WindowEvent e) 
	{
		this.BEV.updateFromViewed();
	}

	@Override
	/**
	 * Updates the BEV Map.
	 */
	public void windowClosed(WindowEvent e) 
	{
		this.BEV.updateFromViewed();	
	}

	@Override
	/**
	 * Updates the BEV Map.
	 */
	public void windowIconified(WindowEvent e) 
	{
		this.BEV.updateFromViewed();	
	}

	@Override
	/**
	 * Updates the BEV Map.
	 */
	public void windowDeiconified(WindowEvent e) 
	{
		this.BEV.updateFromViewed();	
	}

	@Override
	/**
	 * Updates the BEV Map.
	 */
	public void windowActivated(WindowEvent e) 
	{
		this.BEV.updateFromViewed();	
	}

	@Override
	/**
	 * Updates the BEV Map.
	 */
	public void windowDeactivated(WindowEvent e) 
	{
		this.BEV.updateFromViewed();	
	}

	@Override
	/**
	 * Listens for the slider value being changed.
	 */
	public void stateChanged(ChangeEvent e) 
	{
		//Basically, only update if the zooming event occurred before or after the slider itself sets the zoom level.
		if(!this.zoomEventFromGView)
			this.guiController.getCurrentStyleMapManager().zoomNormal( GUIUtility.convertSliderValueToScale(this.slider.getValue()) );		
	}

	/**
	 * Sets whether or not the next zoom event occurred from the GViewMap itself or from another component.
	 * 
	 * @param b
	 */
	private void setGViewZoomEventOccured(boolean b) 
	{
		this.zoomEventFromGView = b;		
	}

	@Override
	/**
	 * Fires when a GViewEvent occurs. Listens for a ZoomNormal event and updates the slider of the BEV Dialog accordingly.
	 */
	public void eventOccured(GViewEvent event) 
	{
		if(event == null)
			throw new IllegalArgumentException("GViewEvent is null.");
		
		if(event instanceof ZoomNormalEvent)
		{
			//Basically, only update if the zooming event occurred before or after the slider itself sets the zoom level.
			//Prevents thrashing of the zoom level, which is partially caused by the slider having only integer values and 
			//the zoom level accepting double values.
			this.setGViewZoomEventOccured(true);
			this.slider.setValue(GUIUtility.convertScaleToSlider(this.guiController.getCurrentStyleMapManager().getZoomNormalFactor()));
			this.setGViewZoomEventOccured(false);
		}		
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if( GUIUtility.SCALE_IN.equals(e.getActionCommand()) )
		{
			(new ScaleInAction(this.guiController.getCurrentStyleMapManager())).run();
		}
		else if( GUIUtility.SCALE_OUT.equals(e.getActionCommand()) )
		{
			(new ScaleOutAction(this.guiController.getCurrentStyleMapManager())).run();
		}
	}
}
