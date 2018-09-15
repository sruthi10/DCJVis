package ca.corefacility.gview.map.gui.editor.communication.setEvent;

import ca.corefacility.gview.textextractor.FeatureTextExtractor;

/**
 * An action for modifying the set tooltips.
 * 
 * @author Eric Marinier
 *
 */
public class SetTooltipEvent extends SetEvent
{
	private final FeatureTextExtractor extractor;
	
	/**
	 * 
	 * @param extractor The feature text extractor.
	 */
	public SetTooltipEvent(FeatureTextExtractor extractor)
	{
		this.extractor = extractor;
	}
	
	@Override
	public FeatureTextExtractor getData()
	{
		return this.extractor;
	}
}
