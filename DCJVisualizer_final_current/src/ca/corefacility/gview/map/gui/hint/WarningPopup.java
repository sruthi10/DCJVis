package ca.corefacility.gview.map.gui.hint;

import java.awt.Color;

import javax.swing.BorderFactory;

/**
 * The custom implementation of the tooltip as a window.
 * 
 * This is a warning specific implementation.
 * 
 * @author Eric Marinier
 * 
 */
public class WarningPopup extends HintPopup
{
	private static final long serialVersionUID = 1L;

	public WarningPopup(HintLabel parent)
	{
		super(parent);

		this.getRootPane().setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(255, 211, 26)),
						BorderFactory.createEmptyBorder(4, 4, 4, 4)));

		Color color = new Color(255, 246, 191);

		this.getRootPane().setBackground(color);
		this.getContentPane().setBackground(color);
	}

}
