package ca.corefacility.gview.map.gui.editor.communication.setEvent;


/**
 * An action for modifying the feature effect of a set.
 * 
 * @author Eric Marinier
 *
 */
public class SetFeatureEffectEvent extends SetEvent
{
	private final String shapeEffect;
	
	/**
	 * 
	 * @param shapeEffect The shape effect identifier.
	 */
	public SetFeatureEffectEvent(String shapeEffect)
	{
		this.shapeEffect = shapeEffect;
	}
	
	@Override
	public String getData()
	{
		return this.shapeEffect;
	}
}
