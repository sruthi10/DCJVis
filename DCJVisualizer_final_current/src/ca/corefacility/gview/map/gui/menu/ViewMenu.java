package ca.corefacility.gview.map.gui.menu;

import javax.swing.JMenu;
import javax.swing.JSeparator;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.GUIUtility;

/**
 * Responsible for creating and managing the View menu on GView's menu bar.
 * 
 * @author Eric Marinier
 *
 */
public class ViewMenu extends JMenu implements MenuListener
{
	private static final long serialVersionUID = 1L;	//requested by java
	
	//Items in the View menu.
	private final ShowLegendMenuItem legendItem;
	private final ShowLabelsMenuItem labelsItem;
	private final ShowRulerMenuItem rulerItem;
	private final ShowAllMenuItem allItem;
	
	private final QualityMenuItem qualityItem;
	private final BEVMenuItem BEVItem;
	private final ZoomMenu zoomMenu;
	private final ScaleMenu scaleMenu;
	private final MoveMenu moveMenu;
	private final FitMapToScreenMenuItem fitMapToScreenItem;
	private final FullScreenMenuItem fullScreenItem;
	private final LayoutMenu layoutMenu;
	
	public ViewMenu(GUIController guiController)
	{
		super(GUIUtility.VIEW_TEXT);
		
		if(guiController == null)
			throw new IllegalArgumentException("GUIController is null.");
	
		this.addMenuListener(this);
		
		//Show Legend Menu Item
		this.legendItem = new ShowLegendMenuItem(guiController);
		this.add(this.legendItem);
		
		//Show Labels Menu Item
		this.labelsItem = new ShowLabelsMenuItem(guiController);
		this.add(this.labelsItem);
		
		//Show Ruler Menu Item
		this.rulerItem = new ShowRulerMenuItem(guiController);
		this.add(this.rulerItem);
		
		//Show All Menu Item
		this.allItem = new ShowAllMenuItem(guiController);
		this.add(this.allItem);
		
		//Separator.
		this.add(new JSeparator());
		
		//Bird's Eye View Menu Item.
		this.BEVItem = new BEVMenuItem(guiController.getGViewGUIFrame());
		this.add(this.BEVItem);
		
		//Separator.
		this.add(new JSeparator());
		
		//Zoom Sub Menu
		this.zoomMenu = new ZoomMenu(guiController);
		this.add(this.zoomMenu);
		
		//Scale Sub Menu
		this.scaleMenu = new ScaleMenu(guiController);
		this.add(this.scaleMenu);
		
		//Move Sub Menu
		this.moveMenu = new MoveMenu(guiController);
		this.add(this.moveMenu);
		
		//Layout
		this.layoutMenu = new LayoutMenu(guiController);
		this.add(this.layoutMenu);
		
		//Separator.
		this.add(new JSeparator());
		
		//Fit Map to Screen Menu Item
		this.fitMapToScreenItem = new FitMapToScreenMenuItem(guiController);
		this.add(this.fitMapToScreenItem);
		
		//Full Screen Menu Item
		this.fullScreenItem = new FullScreenMenuItem(guiController.getGViewGUIFrame());
		this.add(this.fullScreenItem);
		
		//Separator.
		this.add(new JSeparator());
		
		//Quality Menu Item
		this.qualityItem = new QualityMenuItem();
		this.add(this.qualityItem);
	}

	@Override
	/**
	 * Listens for the menu being selected and causes sub menus' and sub items' states to be updated.
	 */
	public void menuSelected(MenuEvent e) 
	{
		this.update();
	}

	@Override
	/**
	 * no effect
	 */
	public void menuDeselected(MenuEvent e) 
	{
	}

	@Override
	/**
	 * no effect
	 */
	public void menuCanceled(MenuEvent e) 
	{
	}
	
	/**
	 * Updates the menus.
	 */
	public void update()
	{
		//This allows for the correct values to be checked off in their respective sub menus.
		this.zoomMenu.updateMenu();
		this.scaleMenu.updateMenu();
		this.fullScreenItem.update();
		this.qualityItem.update();
		this.rulerItem.update();
		this.labelsItem.update();
		this.legendItem.update();
		this.allItem.update();
		this.BEVItem.update();
		this.layoutMenu.update();
	}
}
