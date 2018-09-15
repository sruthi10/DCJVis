package ca.corefacility.gview.map.inputHandler;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import ca.corefacility.gview.map.controllers.FeatureHolderStyleToken;
import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.controllers.LegendItemStyleToken;
import ca.corefacility.gview.map.gui.editor.node.StyleEditorNode;
import ca.corefacility.gview.map.items.FeatureItemImp;
import ca.corefacility.gview.map.items.LegendEntryItem;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;

/**
 * This class is responsible for handling the PInput events that would result in
 * a popup and handles those events appropriately.
 * 
 * @author Eric Marinier
 * 
 */
public class PopupInputEventHandler extends PBasicInputEventHandler implements ActionListener
{
	private static final String EDIT_SET_TEXT = "Edit Set";
	private static final String EDIT_SET = "Edit Set";

	private static final String EDIT_LEGEND_TEXT = "Edit Legend Text";
	private static final String EDIT_LEGEND = "Edit Legend";

	private final GUIController controller;

	private final JPopupMenu menu;

	private final JMenuItem editSet;
	private final JMenuItem editLegendText;

	private Object clickedItem = null;

	public PopupInputEventHandler(GUIController controller)
	{
		this.controller = controller;

		this.menu = new JPopupMenu();

		this.editSet = new JMenuItem(EDIT_SET_TEXT);
		this.editSet.setActionCommand(EDIT_SET);
		this.editSet.addActionListener(this);

		this.editLegendText = new JMenuItem(EDIT_LEGEND_TEXT);
		this.editLegendText.setActionCommand(EDIT_LEGEND);
		this.editLegendText.addActionListener(this);
	}

	@Override
	public void mousePressed(final PInputEvent event)
	{
		maybeShowPopup(event);
	}

	@Override
	public void mouseReleased(final PInputEvent event)
	{
		maybeShowPopup(event);
	}

	/**
	 * Determines whether or not the show the popup and displays the popup if
	 * appropriate.
	 * 
	 * @param event
	 *            The associated input event.
	 */
	private void maybeShowPopup(final PInputEvent event)
	{
		if (null != event && event.isMouseEvent() && event.isPopupTrigger())
		{
			MouseEvent mouseEvent = (MouseEvent) event.getSourceSwingEvent();
			PNode clickedNode = event.getInputManager().getMouseOver().getPickedNode();

			setMenuItems(clickedNode);
			showMenu(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
		}
	}

	/**
	 * Displays the menu.
	 * 
	 * @param component
	 *            The component to display the popup over.
	 * @param x
	 *            The x value relative to the component.
	 * @param y
	 *            The y value relative to the component.
	 */
	private void showMenu(Component component, int x, int y)
	{
		if (this.menu.getSubElements().length > 0)
		{
			this.menu.show(component, x, y);
		}
	}

	/**
	 * Automatically sets the appropriate menu items for the popup.
	 * 
	 * @param item
	 *            The subject of the right click.
	 */
	private void setMenuItems(PNode item)
	{
		this.menu.removeAll();
		this.clickedItem = item;

		if (item instanceof FeatureItemImp)
		{
			this.menu.add(this.editSet);
		}
		else if (item instanceof LegendEntryItem)
		{
			this.menu.add(this.editLegendText);
		}
		else
		{
			// Nothing to add?

			// Parent?
			PNode parent = ((PNode) item).getParent();

			if (parent != null)
			{
				setMenuItems(parent);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// Edit Set:
		if (this.editSet.equals(e.getSource()) && this.clickedItem instanceof FeatureItemImp)
		{
			editSetAction();
		}
		// Edit Legend:
		else if (this.editLegendText.equals(e.getSource()) && this.clickedItem instanceof LegendEntryItem)
		{
			editLegendAction();
		}
	}

	/**
	 * The action for editing the set.
	 */
	private void editSetAction()
	{
		FeatureHolderStyleToken token = new FeatureHolderStyleToken(
				((FeatureItemImp) this.clickedItem).getFeatureHolderStyle());

		StyleEditorNode node = this.controller.getStyleEditor().getCurrentStyleTree().findSetNode(token);

		this.controller.getStyleEditor().displayNode(node);
	}

	/**
	 * The action for editing the legend.
	 */
	private void editLegendAction()
	{
		LegendItemStyleToken token = new LegendItemStyleToken(((LegendEntryItem) this.clickedItem).getLegendItemStyle());

		StyleEditorNode node = this.controller.getStyleEditor().getCurrentStyleTree().findLegendNode(token);

		this.controller.getStyleEditor().displayNode(node);
	}
}
