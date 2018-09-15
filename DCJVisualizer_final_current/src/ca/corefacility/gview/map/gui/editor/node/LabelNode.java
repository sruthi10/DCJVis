package ca.corefacility.gview.map.gui.editor.node;

import ca.corefacility.gview.map.gui.editor.panel.LabelPanel;

/**
 * The node class for the label styles. Intended to be used within a StyleTree.
 * 
 * @author Eric Marinier
 * 
 */
public class LabelNode extends StyleEditorNode
{
	private static final long serialVersionUID = 1L; // requested by java
	private static final String LABEL = "Labels";

	private final LabelPanel labelPanel;

	/**
	 * 
	 * @param labelPanel
	 *            The related panel.
	 */
	public LabelNode(LabelPanel labelPanel)
	{
		super(labelPanel, LABEL);

		if (labelPanel == null)
			throw new IllegalArgumentException("LabelPanel is null");

		this.labelPanel = labelPanel;
	}

	@Override
	/**
	 * Returns the label panel.
	 */
	public LabelPanel getPanel()
	{
		if (this.labelPanel == null)
			throw new IllegalArgumentException("LabelPanel is null");

		return this.labelPanel;
	}
}
