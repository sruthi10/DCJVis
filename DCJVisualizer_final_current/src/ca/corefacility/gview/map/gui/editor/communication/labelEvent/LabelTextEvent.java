package ca.corefacility.gview.map.gui.editor.communication.labelEvent;

import ca.corefacility.gview.map.gui.editor.communication.setEvent.SetEvent;
import ca.corefacility.gview.textextractor.FeatureTextExtractor;

/**
 * An action for modifying the label text.
 * 
 * @author Eric Marinier
 * 
 */
public class LabelTextEvent extends SetEvent
{
	private final FeatureTextExtractor extractor;

	/**
	 * 
	 * @param extractor
	 *            The feature text extractor.
	 */
	public LabelTextEvent(FeatureTextExtractor extractor)
	{
		this.extractor = extractor;
	}

	@Override
	public FeatureTextExtractor getData()
	{
		return this.extractor;
	}
}
