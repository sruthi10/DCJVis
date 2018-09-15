package ca.corefacility.gview.map.gui.editor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.GUIUtility;
import ca.corefacility.gview.map.gui.Style;
import ca.corefacility.gview.map.gui.action.style.ExportStyleAction;
import ca.corefacility.gview.map.gui.action.style.ImportStyleAction;
import ca.corefacility.gview.map.gui.action.style.NewBlankStyleAction;
import ca.corefacility.gview.map.gui.editor.node.StyleEditorNode;
import ca.corefacility.gview.map.gui.editor.panel.StylePanel;

/**
 * The tool bar for the Style Editor.
 * 
 * @author Eric Marinier
 *
 */
public class StyleEditorToolBar extends JToolBar implements ActionListener
{
	private static final long serialVersionUID = 1L;	//requested by java		
	private static final String CURRENT_SYTLE = "Current Style: ";	
	
	private final String IMPORT_STYLE = "Open Style";
	private final String EXPORT_STYLE = "Save Style";
	private final String NEW_STYLE = "New Style";
	private final String REMOVE_STYLE = "Remove Style";
	
	private final String importStylePath = "images/icons/folder-horizontal-open.png";
	private final String exportStylePath = "images/icons/disk-black.png";
	private final String newStylePath = "images/icons/blue-document--plus.png";
	private final String removeStylePath = "images/icons/blue-document--minus.png";
	
	private final JButton importStyle;
	private final JButton exportStyle;
	private final JButton newStyle;
	private final JButton removeStyle;
	
	private final StyleEditorTreeComboBox comboBox;
	
	private final GUIController guiController;
	private final StyleEditorFrame styleEditorFrame;	
	

	public StyleEditorToolBar(GUIController guiController)
	{
		super();
		
		if(guiController == null)
			throw new IllegalArgumentException("GUIController is null.");
		
		this.guiController = guiController;
		this.styleEditorFrame = guiController.getStyleEditor();
		
		this.setFloatable(false);
		
		//Import style
		this.importStyle = new JButton(new ImageIcon(GUIUtility.loadImage(importStylePath)));
		this.importStyle.setToolTipText(IMPORT_STYLE);
		this.importStyle.setActionCommand(IMPORT_STYLE);
		this.importStyle.addActionListener(this);
		
		//Export style
		this.exportStyle = new JButton(new ImageIcon(GUIUtility.loadImage(exportStylePath)));
		this.exportStyle.setToolTipText(EXPORT_STYLE);
		this.exportStyle.setActionCommand(EXPORT_STYLE);
		this.exportStyle.addActionListener(this);
		
		//New style
		this.newStyle = new JButton(new ImageIcon(GUIUtility.loadImage(newStylePath)));
		this.newStyle.setToolTipText(NEW_STYLE);
		this.newStyle.setActionCommand(NEW_STYLE);
		this.newStyle.addActionListener(this);
		
		//Remove style
		this.removeStyle = new JButton(new ImageIcon(GUIUtility.loadImage(removeStylePath)));
		this.removeStyle.setToolTipText(REMOVE_STYLE);
		this.removeStyle.setActionCommand(REMOVE_STYLE);
		this.removeStyle.addActionListener(this);
		
		//Combo box
		this.comboBox = new StyleEditorTreeComboBox(styleEditorFrame);
		this.comboBox.setPreferredSize(new Dimension(200, 22));
		this.comboBox.setMaximumSize(new Dimension(200, 22));
		this.comboBox.setMinimumSize(new Dimension(200, 22));

		//Label 
		JLabel label = new JLabel(CURRENT_SYTLE);
		label.setBorder(new EmptyBorder(0, 5, 0, 0));
		
		//Add items
		this.add(label);
		this.add(this.comboBox);
		
		this.addSeparator();
		
		this.add(this.newStyle);
		this.add(this.removeStyle);
		
		this.addSeparator();
		
		this.add(this.importStyle);
		this.add(this.exportStyle);
		
		this.addSeparator();
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{		
		Style style = this.styleEditorFrame.getGUIController().getCurrentStyle();		
		
		if(e.getActionCommand().equals(IMPORT_STYLE))
		{
			importStyle();
		}
		else if(e.getActionCommand().equals(EXPORT_STYLE))
		{
			exportStyle();
		}
		else if(e.getActionCommand().equals(NEW_STYLE))
		{
			newStyle();
		}
		else if(e.getActionCommand().equals(REMOVE_STYLE))
		{
			if(style != null)
			{
				removeStyle(style);
			}
		}		
	}
	
	/**
	 * Imports a new style.
	 */
	private void importStyle()
	{
		(new ImportStyleAction(this.styleEditorFrame.getGUIController())).run();		
	}

	/**
	 * Exports the current style (saves it).
	 */
	private void exportStyle()
	{	
		JPanel tempPanel;
		
		if(this.comboBox.getSelectedItem() instanceof StyleEditorTree)
		{
			Enumeration<StyleEditorNode> nodes = ((StyleEditorTree)this.comboBox.getSelectedItem()).breadthFirstEnumeration();
			
			while(nodes.hasMoreElements())
			{
				tempPanel = nodes.nextElement().getPanel();
				
				if(tempPanel instanceof StylePanel)
					((StylePanel)tempPanel).save();
			}			
		}
		else
		{
			throw new ClassCastException("Non-StyleEditorTree within StyleEditor's tool bar's combo box.");
		}
		
		(new ExportStyleAction(this.guiController)).run();
	}	
	
	/**
	 * Creates a new style.
	 */
	private void newStyle()
	{
		GUIController guiController = this.styleEditorFrame.getGUIController();
		Runnable newStyleAction = (new NewBlankStyleAction(this.styleEditorFrame.getGUIController()));
		
		guiController.displayProgressWhileRebuilding(newStyleAction);
	}
	
	/**
	 * Removes the style.
	 * 
	 * @param style The style to remove.
	 */
	private void removeStyle(Style style)
	{
		this.styleEditorFrame.getGUIController().removeStyle(style);
	}

	public StyleEditorTreeComboBox getComboBox()
	{
		if(this.comboBox == null)
			throw new NullPointerException("ComboBox is null.");
		
		return this.comboBox;
	}
}
