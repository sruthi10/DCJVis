package ca.corefacility.gview.map.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.event.GViewEvent;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.event.ZoomEvent;
import ca.corefacility.gview.map.gui.action.dialog.show.ShowBEVDialogAction;
import ca.corefacility.gview.map.gui.action.dialog.show.ShowScaleDialogAction;
import ca.corefacility.gview.map.gui.action.dialog.show.ShowZoomDialogAction;
import ca.corefacility.gview.map.gui.action.map.FITMAction;
import ca.corefacility.gview.map.gui.action.map.move.MoveEndAction;
import ca.corefacility.gview.map.gui.action.map.move.MoveHalfAction;
import ca.corefacility.gview.map.gui.action.map.move.MoveStartAction;
import ca.corefacility.gview.map.gui.action.map.scale.ScaleInAction;
import ca.corefacility.gview.map.gui.action.map.scale.ScaleOutAction;
import ca.corefacility.gview.map.gui.action.map.zoom.ZoomInAction;
import ca.corefacility.gview.map.gui.action.map.zoom.ZoomOutAction;

/**
 * The default tool bar for the GUI frame.
 * 
 * @author Eric Marinier
 *
 */
public class GViewToolBar extends JToolBar implements ActionListener, GViewEventListener
{
	private static final long serialVersionUID = 1L;
	
	private final String magnifierPath = "images/icons/magnifier.png";
	private final String magnifierPlusPath = "images/icons/magnifier--plus.png";
	private final String magnifierMinusPath = "images/icons/magnifier--minus.png";
	private final String magnifierZoomPath = "images/icons/magnifier-zoom.png";
	private final String magnifierZoomInPath = "images/icons/magnifier-zoom-in.png";
	private final String magnifierZoomOutPath = "images/icons/magnifier-zoom-out.png";
	private final String moveStartPath = "images/icons/control-180.png";
	private final String moveEndPath = "images/icons/control.png";
	private final String moveMiddlePath = "images/icons/control-270.png";
	private final String fitImageToScreenPath = "images/icons/layer-resize.png";
	private final String birdsEyeViewPath = "images/icons/eye.png";
	
	private final JButton magnifier;
	private final JButton magnifierPlus;
	private final JButton magnifierMinus;
	private final JButton magnifierZoom;
	private final JButton magnifierZoomIn;
	private final JButton magnifierZoomOut;
	private final JButton moveStart;
	private final JButton moveEnd;
	private final JButton moveMiddle;
	private final JButton fitImageToScreen;
	private final JButton birdsEyeView;
	
	private final GUIController guiController;
	private final GViewGUIFrame gViewGUIFrame;
	
	public GViewToolBar(GUIController guiController)
	{
		super();
		
		if(guiController == null)
			throw new IllegalArgumentException("GUIController is null");
		
		this.guiController = guiController;
		this.gViewGUIFrame = guiController.getGViewGUIFrame();
		
		this.guiController.addEventListener(this);
		
		this.setFloatable(false);
		
		this.magnifier = new JButton(new ImageIcon(GUIUtility.loadImage(magnifierPath)));
		this.magnifier.setToolTipText(GUIUtility.SCALE_CUSTOM);
		this.magnifier.setActionCommand(GUIUtility.SCALE_CUSTOM);
		this.magnifier.addActionListener(this);
		this.magnifier.setFocusable(false);
		
		this.magnifierPlus = new JButton(new ImageIcon(GUIUtility.loadImage(magnifierPlusPath)));
		this.magnifierPlus.setToolTipText(GUIUtility.SCALE_IN);
		this.magnifierPlus.setActionCommand(GUIUtility.SCALE_IN);
		this.magnifierPlus.addActionListener(this);
		this.magnifierPlus.setFocusable(false);
		
		this.magnifierMinus = new JButton(new ImageIcon(GUIUtility.loadImage(magnifierMinusPath)));
		this.magnifierMinus.setToolTipText(GUIUtility.SCALE_OUT);
		this.magnifierMinus.setActionCommand(GUIUtility.SCALE_OUT);
		this.magnifierMinus.addActionListener(this);
		this.magnifierMinus.setFocusable(false);
		
		this.magnifierZoom = new JButton(new ImageIcon(GUIUtility.loadImage(magnifierZoomPath)));
		this.magnifierZoom.setToolTipText(GUIUtility.ZOOM_CUSTOM);
		this.magnifierZoom.setActionCommand(GUIUtility.ZOOM_CUSTOM);
		this.magnifierZoom.addActionListener(this);
		this.magnifierZoom.setFocusable(false);
		
		this.magnifierZoomIn = new JButton(new ImageIcon(GUIUtility.loadImage(magnifierZoomInPath)));
		this.magnifierZoomIn.setToolTipText(GUIUtility.ZOOM_IN);
		this.magnifierZoomIn.setActionCommand(GUIUtility.ZOOM_IN);
		this.magnifierZoomIn.addActionListener(this);
		this.magnifierZoomIn.setFocusable(false);
		
		this.magnifierZoomOut = new JButton(new ImageIcon(GUIUtility.loadImage(magnifierZoomOutPath)));
		this.magnifierZoomOut.setToolTipText(GUIUtility.ZOOM_OUT);
		this.magnifierZoomOut.setActionCommand(GUIUtility.ZOOM_OUT);
		this.magnifierZoomOut.addActionListener(this);
		this.magnifierZoomOut.setFocusable(false);
		
		this.moveStart = new JButton(new ImageIcon(GUIUtility.loadImage(moveStartPath)));
		this.moveStart.setToolTipText(GUIUtility.MOVE_TO_START);
		this.moveStart.setActionCommand(GUIUtility.MOVE_TO_START);
		this.moveStart.addActionListener(this);
		this.moveStart.setFocusable(false);
		
		this.moveMiddle = new JButton(new ImageIcon(GUIUtility.loadImage(moveMiddlePath)));
		this.moveMiddle.setToolTipText(GUIUtility.MOVE_TO_MIDDLE);
		this.moveMiddle.setActionCommand(GUIUtility.MOVE_TO_MIDDLE);
		this.moveMiddle.addActionListener(this);
		this.moveMiddle.setFocusable(false);
		
		this.moveEnd = new JButton(new ImageIcon(GUIUtility.loadImage(moveEndPath)));
		this.moveEnd.setToolTipText(GUIUtility.MOVE_TO_END);
		this.moveEnd.setActionCommand(GUIUtility.MOVE_TO_END);
		this.moveEnd.addActionListener(this);
		this.moveEnd.setFocusable(false);
		
		this.fitImageToScreen = new JButton(new ImageIcon(GUIUtility.loadImage(fitImageToScreenPath)));
		this.fitImageToScreen.setToolTipText(GUIUtility.FMTS_TEXT);
		this.fitImageToScreen.setActionCommand(GUIUtility.FMTS_TEXT);
		this.fitImageToScreen.addActionListener(this);
		this.fitImageToScreen.setFocusable(false);
		
		this.birdsEyeView = new JButton(new ImageIcon(GUIUtility.loadImage(birdsEyeViewPath)));
		this.birdsEyeView.setToolTipText(GUIUtility.BEV);
		this.birdsEyeView.setActionCommand(GUIUtility.BEV);
		this.birdsEyeView.addActionListener(this);
		this.birdsEyeView.setFocusable(false);
		
		this.add(magnifierPlus);
		this.add(magnifierMinus);
		this.add(magnifier);
		
		this.addSeparator();
		
		this.add(magnifierZoomIn);
		this.add(magnifierZoomOut);
		this.add(magnifierZoom);
		
		this.addSeparator();
		
		this.add(fitImageToScreen);
		this.add(birdsEyeView);
		
		this.addSeparator();
		
		this.add(moveStart);
		this.add(moveMiddle);
		this.add(moveEnd);
		
		this.addSeparator();

		updateZoom(this.guiController.getCurrentStyleMapManager().getZoomFactor());
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(GUIUtility.SCALE_IN.equals(e.getActionCommand()))
		{
			(new ScaleInAction(this.guiController.getCurrentStyleMapManager())).run();
		}
		else if(GUIUtility.SCALE_OUT.equals(e.getActionCommand()))
		{
			(new ScaleOutAction(this.guiController.getCurrentStyleMapManager())).run();
		}
		else if(GUIUtility.SCALE_CUSTOM.equals(e.getActionCommand()))
		{
			(new ShowScaleDialogAction(gViewGUIFrame.getScaleDialog())).run();
		}
		else if(GUIUtility.ZOOM_IN.equals(e.getActionCommand()))
		{
			(new ZoomInAction(this.guiController.getCurrentStyleMapManager())).run();
		}
		else if(GUIUtility.ZOOM_OUT.equals(e.getActionCommand()))
		{
			(new ZoomOutAction(this.guiController.getCurrentStyleMapManager())).run();
		}
		else if(GUIUtility.ZOOM_CUSTOM.equals(e.getActionCommand()))
		{
			(new ShowZoomDialogAction(gViewGUIFrame.getZoomDialog())).run();
		}
		else if(GUIUtility.MOVE_TO_START.equals(e.getActionCommand()))
		{
			(new MoveStartAction(this.guiController.getCurrentStyleMapManager())).run();
		}
		else if(GUIUtility.MOVE_TO_MIDDLE.equals(e.getActionCommand()))
		{
			(new MoveHalfAction(this.guiController.getCurrentStyleMapManager())).run();
		}
		else if(GUIUtility.MOVE_TO_END.equals(e.getActionCommand()))
		{
			(new MoveEndAction(this.guiController.getCurrentStyleMapManager())).run();
		}
		else if(GUIUtility.FMTS_TEXT.equals(e.getActionCommand()))
		{
			(new FITMAction(this.guiController.getCurrentStyleMapManager())).run();
		}
		else if(GUIUtility.BEV.equals(e.getActionCommand()))
		{
			(new ShowBEVDialogAction(gViewGUIFrame.getBEVDialog())).run();
		}
	}

	@Override
	public void eventOccured(GViewEvent event)
	{		
		//Zooming
		if(event instanceof ZoomEvent)
		{
			double zoomFactor = ((ZoomEvent)event).getScale();
			
			updateZoom(zoomFactor);
		}
	}
	
	/**
	 * Updates the tool bar.
	 */
	public void update()
	{
		double zoomFactor =  this.guiController.getCurrentStyleMapManager().getZoomFactor();
		
		updateZoom(zoomFactor);
	}
	
	/**
	 * Updates the zoom related components on the toolbar.
	 * 
	 * @param zoomFactor
	 */
	private void updateZoom(double zoomFactor)
	{
		if(zoomFactor >= this.guiController.getCurrentStyleMapManager().getMaxZoomFactor())
		{
			this.magnifierZoomIn.setEnabled(false);
			this.magnifierZoomOut.setEnabled(true);
		}
		else if(zoomFactor <= this.guiController.getCurrentStyleMapManager().getMinZoomFactor())
		{
			this.magnifierZoomIn.setEnabled(true);
			this.magnifierZoomOut.setEnabled(false);
		}
		else
		{
			this.magnifierZoomIn.setEnabled(true);
			this.magnifierZoomOut.setEnabled(true);
		}
	}
}
