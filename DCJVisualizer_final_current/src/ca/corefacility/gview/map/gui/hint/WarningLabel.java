package ca.corefacility.gview.map.gui.hint;

import java.awt.Point;

import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;

/**
 * A label designed to show warning information on mouse-over.
 * 
 * @author Eric Marinier
 * 
 */
public class WarningLabel extends HintLabel
{
	private static final long serialVersionUID = 1L;

	public WarningLabel(String hint)
	{
		super(hint, StyleEditorUtility.EXCLAMATION);
	}

	@Override
	protected Point getPopupLocation()
	{
		int popupHeight = 0;

		if (this.getPopup() != null)
		{
			popupHeight = this.getPopup().getHeight();
		}

		return new Point(this.getLocationOnScreen().x + this.getWidth() + 1, this.getLocationOnScreen().y
				+ (this.getHeight() / 2) - (popupHeight / 2));
	}

	@Override
	protected HintPopup createPopup()
	{
		return new WarningPopup(this);
	}
}
