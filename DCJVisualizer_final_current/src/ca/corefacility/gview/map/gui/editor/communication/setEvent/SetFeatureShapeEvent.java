package ca.corefacility.gview.map.gui.editor.communication.setEvent;


/**
 * An action for modifying the feature shape of a set.
 * 
 * @author Eric Marinier
 *
 */
public class SetFeatureShapeEvent extends SetEvent
{
	private final String featureShapeRealizer;
	
	/**
	 * 
	 * @param featureShapeRealizer The feature shape realizer.
	 */
	public SetFeatureShapeEvent(String featureShapeRealizer)
	{
		this.featureShapeRealizer = featureShapeRealizer;
	}
	
	@Override
	public String getData()
	{
		return this.featureShapeRealizer;
	}
}
