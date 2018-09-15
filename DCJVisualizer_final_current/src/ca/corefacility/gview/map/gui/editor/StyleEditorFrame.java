package ca.corefacility.gview.map.gui.editor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import ca.corefacility.gview.map.controllers.GUIController;
import ca.corefacility.gview.map.gui.Style;
import ca.corefacility.gview.map.gui.editor.menu.StyleEditorMenuBar;
import ca.corefacility.gview.map.gui.editor.node.StyleEditorNode;
import ca.corefacility.gview.map.gui.editor.panel.proxy.ProxiablePanel;
import ca.corefacility.gview.utils.thread.ThreadService;

/**
 * The main frame of GView's Style Editor.
 * 
 * @author Eric Marinier
 * 
 */
public class StyleEditorFrame extends JFrame implements TreeSelectionListener
{
	private static final long serialVersionUID = 1L; // requested by java

	private static final int DEFAULT_DIVIDER_LOCATION = 250;

	private JPanel contentPane;
	private JScrollPane contextPane;
	private JSplitPane splitPane;
	private JScrollPane treePane;
	private StyleEditorTreeComboBox styleComboBox;

	private StyleEditorMenuBar menuBar;
	private StyleEditorToolBar styleEditorToolBar;

	private final GUIController guiController;

	private final HashMap<Style, StyleEditorTree> styleTrees = new HashMap<Style, StyleEditorTree>();

	private boolean built = false;

	public StyleEditorFrame(GUIController guiController)
	{
		super("Style Editor");

		this.guiController = guiController;

		this.setDefaultCloseOperation(HIDE_ON_CLOSE);

		this.setVisible(false);
	}

	/**
	 * Builds the style editor frame.
	 */
	public void build()
	{
		// TODO: Is the order okay?

		// Frame
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.contentPane = new JPanel();
		this.contentPane.setLayout(new BorderLayout(0, 0));
		this.setContentPane(contentPane);

		// Split pane
		this.splitPane = new JSplitPane();
		this.splitPane.setBorder(new EmptyBorder(0, 5, 5, 5));
		this.contentPane.add(splitPane, BorderLayout.CENTER);

		// tree pane (left side)
		JPanel panel = new JPanel();
		this.splitPane.setLeftComponent(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		this.treePane = new JScrollPane();
		panel.add(treePane);

		// Context pane (right side)
		JPanel panel_1 = new JPanel();
		this.splitPane.setRightComponent(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));

		this.contextPane = new JScrollPane();
		panel_1.add(contextPane);

		JPanel buttonPanel = createButtonPanel();
		this.add(buttonPanel, BorderLayout.SOUTH);

		this.splitPane.setDividerLocation(DEFAULT_DIVIDER_LOCATION);

		// Menu bar
		this.menuBar = new StyleEditorMenuBar(this.guiController);
		this.setJMenuBar(menuBar);

		// Tool bar
		this.styleEditorToolBar = new StyleEditorToolBar(this.guiController);
		this.add(styleEditorToolBar, BorderLayout.NORTH);

		// Combo box (from tool bar)
		this.styleComboBox = this.styleEditorToolBar.getComboBox();

		// Create style tree(s):
		synchronizeStyles();

		// Tree object
		this.treePane.setViewportView(getCurrentStyleTree());
		getCurrentStyleTree().updateTree();

		this.contextPane.setViewportView(getCurrentStyleTree().getRoot().getPanel());
		this.contextPane.getVerticalScrollBar().setUnitIncrement(10);
		this.synchronizeStyles();

		this.pack();

		this.built = true;
	}

	/**
	 * 
	 * @return The current style tree.
	 */
	public StyleEditorTree getCurrentStyleTree()
	{
		synchronizeStyles();

		Style currentStyle = this.guiController.getCurrentStyle();
		StyleEditorTree currentTree = this.styleTrees.get(currentStyle);

		return currentTree;
	}

	/**
	 * Synchronizes all of the styles associated with the style editor frame.
	 */
	public void synchronizeStyles()
	{
		ArrayList<Style> styles = this.guiController.getStyles();
		ArrayList<Style> toRemove = new ArrayList<Style>();

		Style style;
		StyleEditorTree tree;

		// Check to see if all the styles are in the map:
		for (int i = 0; i < styles.size(); i++)
		{
			style = styles.get(i);

			// Does the element exist in the collection? (it should)
			// No:
			if (this.styleTrees.get(style) == null)
			{
				// Create it and add to the collection:
				tree = new StyleEditorTree(this, style);
				this.styleTrees.put(style, tree);
			}
		}

		// Check to see if there are no left-over styles in the collection (the
		// reverse question):
		for (Iterator<Style> keys = this.styleTrees.keySet().iterator(); keys.hasNext();)
		{
			style = keys.next();

			// Does the element exist in the list of styles?
			// No:
			if (!styles.contains(style))
			{
				// Remove the left over:
				toRemove.add(style);
			}
		}

		// Actually remove the styles:
		// (This is because you can't modify while you're iterating over it)
		for (int i = 0; i < toRemove.size(); i++)
		{
			this.styleTrees.get(toRemove.get(i)).delete();
			this.styleTrees.remove(toRemove.get(i));
		}

		Style currentStyle = this.guiController.getCurrentStyle();
		StyleEditorTree currentTree = this.styleTrees.get(currentStyle);

		// GUI components might not be built when this is called.
		if (this.built)
		{
			this.styleComboBox.update();
			this.styleComboBox.updateSelection(currentTree);
		}
	}

	// TODO: CLEAN THIS UP!
	private JPanel createButtonPanel()
	{
		final String OK_COMMAND = "Ok";
		final String CANCEL_COMMAND = "Cancel";
		final String APPLY_COMMAND = "Apply";

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		ActionListener buttonListener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (e.getActionCommand().equals(OK_COMMAND))
				{
					StyleEditorFrame.this.setVisible(false);

					guiController.displayProgressWhileRebuilding(new Runnable()
					{
						@Override
						public void run()
						{
							StyleEditorFrame.this.applyCurrentStyle();
						}
					});

					StyleEditorFrame.this.getCurrentStyleTree().updateTree();
				}
				else if (e.getActionCommand().equals(CANCEL_COMMAND))
				{
					StyleEditorFrame.this.setVisible(false);
					StyleEditorFrame.this.getCurrentStyleTree().updateTree();
				}
				else if (e.getActionCommand().equals(APPLY_COMMAND))
				{
					guiController.displayProgressWhileRebuilding(new Runnable()
					{
						@Override
						public void run()
						{
							StyleEditorFrame.this.applyCurrentStyle();
						}
					});

					StyleEditorFrame.this.getCurrentStyleTree().updateTree();
				}
			}
		};

		{
			JButton applyButton = new JButton("Apply");
			applyButton.setActionCommand(APPLY_COMMAND);
			buttonPanel.add(applyButton);
			applyButton.addActionListener(buttonListener);
		}
		{
			JButton okButton = new JButton("Ok");
			okButton.setActionCommand(OK_COMMAND);
			buttonPanel.add(okButton);
			okButton.addActionListener(buttonListener);
		}
		{
			JButton cancelButton = new JButton("Cancel");
			cancelButton.setActionCommand(CANCEL_COMMAND);
			buttonPanel.add(cancelButton);
			cancelButton.addActionListener(buttonListener);
		}

		return buttonPanel;
	}

	@Override
	public void valueChanged(TreeSelectionEvent e)
	{
		StyleEditorTree tree = getCurrentStyleTree();
		DefaultMutableTreeNode temp = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

		// Safety:
		if (temp == null || !(temp instanceof StyleEditorNode))
		{
			this.contextPane.setViewportView(null);
			return;
		}

		StyleEditorNode currentNode = (StyleEditorNode) temp;
		JPanel currentPanel = null;

		if (currentNode != null)
		{
			currentPanel = currentNode.getPanel();
		}

		// Multiple Selection:
		if (tree.getSelectionCount() > 1)
		{
			// Consistent:
			if (tree.getStyle().getStyleController().getSelectionController().isSelectionConsistent())
			{
				// Proxiable:
				if (currentPanel instanceof ProxiablePanel)
				{
					this.contextPane.setViewportView(((ProxiablePanel) currentPanel).createProxy());
				}
				else
				{
					this.contextPane.setViewportView(currentPanel);
				}

			}
			// Inconsistent:
			else
			{
				this.contextPane.setViewportView(null);
			}
		}
		// Single Selection:
		else
		{
			this.contextPane.setViewportView(currentPanel);
		}

		tree.repaint();
		tree.updateTreeRendering();
	}

	/**
	 * Sets the selection to be only the passed node, displays the associated
	 * panel in the contextual area and scrolls the JTree so that the selected
	 * node is visible.
	 * 
	 * @param node
	 *            The node to select.
	 */
	public void displayNode(StyleEditorNode node)
	{
		if (node != null)
		{
			if (!this.isVisible())
			{
				this.setVisible(true); // Will build if required!
			}

			StyleEditorTree tree = this.getCurrentStyleTree();
			tree.scrollPathToVisible(tree.getPath(node));
			tree.setSelectionPath(tree.getPath(node));

			this.toFront();

			if ((this.getExtendedState() & Frame.ICONIFIED) != 0)
			{
				this.setExtendedState(Frame.NORMAL);
			}
		}
	}

	/**
	 * Sets the current tree to be used in the style editor.
	 * 
	 * @param styleTree
	 *            The tree to be used in the style editor.
	 */
	public void setCurrentStyle(StyleEditorTree styleTree)
	{
		if (styleTree == null)
			throw new IllegalArgumentException("StyleEditorTree is null.");

		this.guiController.setStyle(styleTree.getStyle());
	}

	/**
	 * Updates the frame.
	 */
	public void update()
	{
		this.synchronizeStyles();

		if (this.built)
		{
			// Select the root (so that it refreshes context window and
			// whatnot):
			getCurrentStyleTree().selectRoot();
			getCurrentStyleTree().updateTree();
			this.treePane.setViewportView(getCurrentStyleTree());
			this.contextPane.setViewportView(getCurrentStyleTree().getRoot().getPanel());

			this.styleComboBox.updateSelection(getCurrentStyleTree());
		}
	}

	/**
	 * Sets the name of the current style. Handles updating the appropriate GUI
	 * components.
	 * 
	 * @param name
	 *            The new name of the current style.
	 */
	public void setCurrentStyleName(String name)
	{
		if (name != null)
		{
			this.guiController.getCurrentStyle().setName(name);
			this.styleComboBox.repaint();
		}
	}

	/**
	 * Applies the changes to the current style.
	 */
	public void applyCurrentStyle()
	{
		this.getCurrentStyleTree().saveCurrentStyle();
		this.guiController.rebuildStyle();

		ThreadService.executeOnEDT(new Runnable()
		{
			@Override
			public void run()
			{
				getCurrentStyleTree().repaint();
			}
		});
	}

	/**
	 * 
	 * @return The GUI controller.
	 */
	public GUIController getGUIController()
	{
		return this.guiController;
	}

	/**
	 * 
	 * @return The style editor trees associated with this style editor frame.
	 */
	public Iterator<StyleEditorTree> getStyleTrees()
	{
		return this.styleTrees.values().iterator();
	}

	@Override
	public void setVisible(boolean visible)
	{
		if (visible && !built)
		{
			this.build();
			this.update();
		}

		if (built)
		{
			this.guiController.resetStyleEditorPositionRelativeToFrame();
		}

		super.setVisible(visible);
	}

	/**
	 * Updates only and all the styles.
	 */
	public void updateStyles()
	{
		Iterator<StyleEditorTree> trees = this.getStyleTrees();
		StyleEditorTree current;

		while (trees.hasNext())
		{
			current = trees.next();
			current.updateTreeRendering();
		}
	}
}
