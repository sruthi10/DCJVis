package ca.corefacility.gview.map.gui;


import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.action.map.FullScreenAction;
import ca.corefacility.gview.map.gui.dialog.AboutDialog;
import ca.corefacility.gview.map.gui.dialog.BEVDialog;
import ca.corefacility.gview.map.gui.dialog.MoveDialog;
import ca.corefacility.gview.map.gui.dialog.ScaleDialog;
import ca.corefacility.gview.map.gui.dialog.ZoomDialog;
import ca.corefacility.gview.map.gui.menu.FileMenu;
import ca.corefacility.gview.map.gui.menu.HelpMenu;
import ca.corefacility.gview.map.gui.menu.StyleMenu;
import ca.corefacility.gview.map.gui.menu.ViewMenu;

/**
 * The GUI frame for GView.
 * 
 * @author Eric Marinier
 *
 */
public class GViewGUIFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private JPanel windowPanel;	//The main window area of the GUI, where the GViewMap is displayed
	
	//The menu bar for the frame.
	private JMenuBar menuBar;
	
	//The menus on the menu bar.
	private FileMenu fileMenu;
	private ViewMenu viewMenu;
	private HelpMenu helpMenu;
	private StyleMenu styleMenu;
	
	//Dialogs
	private ZoomDialog zoomDialog;
	private MoveDialog moveDialog;
	private ScaleDialog scaleDialog;
	private BEVDialog bevDialog;
	private AboutDialog aboutDialog;

	//The tool bar.
	private GViewToolBar toolBar;
    
    private final GUIController guiController;
    
	public GViewGUIFrame(GUIController guiController)
	{		
		this.guiController = guiController;	
		
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	
	public void build(String title, boolean fullScreenMode)
	{
		if(title == null)
		{
			title = "GView";
		}
		
		buildFrame(title, fullScreenMode);
		buildComponents();
		initializeComponents();
		addComponents();
	}

	/**
	 * Sets the size of the frame and canvas.
	 * 
	 * @param width
	 * @param height
	 */
	public void setFrameSize(int width, int height)
	{
		//Catch very, very small windows.
		if(width < 100)
			width = 100;
		
		if(height < 100)
			height = 100;
		
		this.setSize(width, height);
	}
	
	/**
	 * Creates the menu bar.
	 * 
	 * @return
	 * 	A JMenuBar.
	 */
	private JMenuBar createMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();		
		
		return menuBar;
	}
	
	/**
	 * Initialises the dialog boxes used by the frame.
	 * 
	 */
	private void initializeDialogs()
	{
		this.zoomDialog = new ZoomDialog(this.guiController);
		this.scaleDialog = new ScaleDialog(this.guiController);
		this.moveDialog = new MoveDialog(this.guiController);
		this.aboutDialog = new AboutDialog(this);
	}
	
	/**
	 * Returns the current Bird's Eye View dialog.
	 * @return  The current bird's eye view dialog.
	 */
	public BEVDialog getBEVDialog()
	{
		if(this.bevDialog == null)
			throw new IllegalArgumentException("BEVDialog is null.");
		
		return this.bevDialog;
	}
	
	/**
	 * 
	 * @return The scale dialog (tradition scaling).
	 */
	public ScaleDialog getScaleDialog()
	{
		if(this.scaleDialog == null)
			throw new IllegalArgumentException("ScaleDialog is null.");
		
		return this.scaleDialog;
	}
	
	/**
	 * 
	 * @return The move dialog. Allows users to move to a specific base pair.
	 */
	public MoveDialog getMoveDialog()
	{
		if(this.moveDialog == null)
			throw new IllegalArgumentException("MoveDialog is null.");
		
		return this.moveDialog;
	}
	
	/**
	 * 
	 * @return The zoom dialog (expanding zoom).
	 */
	public ZoomDialog getZoomDialog()
	{
		if(this.zoomDialog == null)
			throw new IllegalArgumentException("ZoomDialog is null.");
		
		return this.zoomDialog;
	}
	
	/**
	 * 
	 * @return The about dialog, which contains information about the program.
	 */
	public AboutDialog getAboutDialog()
	{
		if(this.aboutDialog == null)
			throw new IllegalArgumentException("AboutDialog is null.");
		
		return this.aboutDialog;
	}
	
	private void buildFrame(String title, boolean fullScreenMode)
	{	
		if(title == null)
			title = "GView";
		
		this.setTitle(title);		
		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);	
		
		//Check full screen mode
		if(fullScreenMode)
			new FullScreenAction(this).run();			
	}	

	/**
	 * Builds the internal components.
	 */
	private void buildComponents()
	{
		this.menuBar = createMenuBar();
		
		this.bevDialog = new BEVDialog(this.guiController);
		
		this.fileMenu = new FileMenu(this.guiController);
		this.viewMenu = new ViewMenu(this.guiController);
		this.styleMenu = new StyleMenu(this.guiController);
		this.helpMenu = new HelpMenu(this);	
		
		this.toolBar = new GViewToolBar(this.guiController);
	}
	
	/**
	 * Adds the internal components to the frame.
	 */
	private void addComponents()
	{
		this.windowPanel = new JPanel();
		
		//this.menuBar.add(fileMenu);
		//this.menuBar.add(viewMenu);		
		//this.menuBar.add(styleMenu);			
		//this.menuBar.add(helpMenu);
		
		this.windowPanel.setLayout(new BorderLayout());
		
		this.setJMenuBar(menuBar);
		//this.setContentPane(windowPanel);
		//this.getContentPane().add(toolBar, BorderLayout.NORTH);
               
	}
	
	/**
	 * Initializes the internal components.
	 */
	private void initializeComponents()
	{
		initializeDialogs();
	}
	
	/**
	 * Updates the GViewGUIFrame.
	 */
	public void update()
	{
		this.windowPanel = new JPanel();			
		this.windowPanel.setLayout(new BorderLayout());		
		this.guiController.getCurrentStyle().getStyleController().getGViewMapManager().addMapToPanel(this.windowPanel);
		this.windowPanel.setVisible(true);
		
		this.setContentPane(windowPanel);
		this.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		this.repaint();
		this.validate();
		
		this.bevDialog.update();
		this.toolBar.update();
		this.viewMenu.update();
                
	}
	
	/**
	 * 
	 * @return The window panel.
	 */
	public JPanel getWindowPanel()
	{
		return this.windowPanel;
	}

	/**
	 * 
	 * @param visible Whether or not the bird's eye view will be visible.
	 */
	public void setBirdsEyeView(boolean visible)
	{
		this.bevDialog.setVisible(visible);		
	}
}