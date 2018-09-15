package ca.corefacility.gview.map.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JSlider;
import javax.swing.JToolBar;

import ca.corefacility.gview.map.BirdsEyeViewImp;
import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.GViewMapManager;

/**
 * The bird's eye view dialog box.
 * 
 * @author Eric Marinier, Aaron Petkau
 *
 */
public class BEVDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	private final String magnifierPlusPath = "images/icons/magnifier--plus.png";
	private final String magnifierMinusPath = "images/icons/magnifier--minus.png";	
	
	private final JSlider slider;
	private final JButton plusButton;
	private final JButton minusButton;
	
	private final GUIController guiController;	
	
	private JToolBar toolBar;
	private BEVDialogListener bevDialogListener;
	private BirdsEyeViewImp BEV;	
	
	public BEVDialog(GUIController guiController)
	{
		super(guiController.getGViewGUIFrame());
		
		this.guiController = guiController;
		
		this.setTitle(GUIUtility.BEV_TEXT);
		
		GViewMapManager gViewManager = guiController.getCurrentStyle().getStyleController().getGViewMapManager();
		
		this.BEV = new BirdsEyeViewImp(this.guiController);	
		
		this.slider = new JSlider((int)GUIUtility.SCALE_SLIDER_MIN, (int)GUIUtility.SCALE_SLIDER_MAX);
		this.slider.setValue(GUIUtility.convertScaleToSlider(gViewManager.getZoomNormalFactor()));
		
		this.plusButton = new JButton(new ImageIcon(GUIUtility.loadImage(magnifierPlusPath)));		
		this.minusButton = new JButton(new ImageIcon(GUIUtility.loadImage(magnifierMinusPath)));
		
		this.bevDialogListener = new BEVDialogListener(this.guiController, this.BEV, this.slider);
		
		this.addWindowListener(this.bevDialogListener);				
		this.addComponentListener(this.bevDialogListener);	
		this.setSize(300, 300);
		
		//Create the content of the dialog.
		create(this.getContentPane());
		
		this.setVisible(false);		
	}
	
	/**
	 * Creates the content of the BEVDialog.
	 * 
	 * @param contentPane The pane the dialog will be created on.
	 */
	private void create(Container contentPane)
	{
		this.toolBar = new JToolBar();
		
		this.toolBar.setFloatable(false);
		this.toolBar.setLayout(new BoxLayout(this.toolBar, BoxLayout.X_AXIS));
		
		contentPane.setLayout(new BorderLayout());
		contentPane.add((JComponent)this.BEV, BorderLayout.CENTER);
		
		contentPane.add(this.toolBar, BorderLayout.SOUTH);
		
		this.minusButton.setActionCommand(GUIUtility.SCALE_OUT);
		this.minusButton.setToolTipText(GUIUtility.SCALE_OUT);
		this.minusButton.addActionListener(this.bevDialogListener);
		this.toolBar.add(this.minusButton);	
		
		this.slider.addChangeListener(this.bevDialogListener);
		this.toolBar.add(this.slider);
		
		this.plusButton.setActionCommand(GUIUtility.SCALE_IN);
		this.plusButton.setToolTipText(GUIUtility.SCALE_IN);
		this.plusButton.addActionListener(this.bevDialogListener);
		this.toolBar.add(this.plusButton);
	}

	/**
	 * Updates the Bird's Eye View dialog, ensuring that it is reflective of the current GViewMap.
	 */
	public void update()
	{	
		GViewMapManager gViewManager = guiController.getCurrentStyle().getStyleController().getGViewMapManager();
		
		this.BEV = new BirdsEyeViewImp(this.guiController);
		
		this.bevDialogListener = new BEVDialogListener(this.guiController, BEV, slider);
		this.addWindowListener(this.bevDialogListener);				
		this.addComponentListener(this.bevDialogListener);	
		
		this.slider.setValue(GUIUtility.convertScaleToSlider(gViewManager.getZoomNormalFactor()));
		
		this.getContentPane().removeAll();
		this.getContentPane().add((JComponent)BEV, BorderLayout.CENTER);
		this.getContentPane().add(this.toolBar, BorderLayout.SOUTH);
		
		this.repaint();
		this.validate();
		
		this.BEV.updateFromViewed();
	}
}
