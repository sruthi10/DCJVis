package ca.corefacility.gview.map.controllers;

import ca.corefacility.gview.style.datastyle.FeatureHolderStyle;
import ca.corefacility.gview.style.datastyle.SlotStyle;
import ca.corefacility.gview.style.datastyle.mapper.PropertyMappable;
import ca.corefacility.gview.style.datastyle.mapper.PropertyStyleMapper;
import ca.corefacility.gview.utils.Util;

/**
 * This class is responsible for controlling access to the property mappers.
 * 
 * @author Eric Marinier
 *
 */
public class PropertyMapperController extends Controller
{
	private final StyleController styleController;
	
	public PropertyMapperController(StyleController styleController)
	{
		this.styleController = styleController;
	}
	
	/**
	 * 
	 * @param token The associated slot style.
	 * @return The associated property mappable.
	 */
	public PropertyMappableToken getPropertyMappable(SlotStyleToken token)
	{
		SlotStyle style = token.getStyle();
		
		return (new PropertyMappableToken(style));
	}
	
	/**
	 * 
	 * @param token The associated feature holder style style.
	 * @return The associated property mappable.
	 */
	public PropertyMappableToken getPropertyMappable(FeatureHolderStyleToken token)
	{
		FeatureHolderStyle style = token.getStyle();
		
		return (new PropertyMappableToken(style));
	}
	
	/**
	 * 
	 * @param token The associated property mappable object.
	 * @return The property style mapper.
	 */
	public PropertyStyleMapper getPropertyStyleMapper(PropertyMappableToken token)
	{
		PropertyMappable mappable = token.getPropertyMappable();
		
		return mappable.getPropertyStyleMapper();
	}
	
	/**
	 * 
	 * @param mapper
	 * @param token The associated property mappable object.
	 */
	public void setPropertyStyleMapper(PropertyStyleMapper mapper, PropertyMappableToken token)
	{
		PropertyMappable mappable = token.getPropertyMappable();
		
		if(!Util.isEqual(mappable.getPropertyStyleMapper(), mapper))
		{
			mappable.setPropertyStyleMapper(mapper);
			
			this.styleController.notifyFullRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @param token The associated slot style to remove the property style mapper from.
	 * @return Whether or not the removal was successful.
	 */
	public boolean removePropertyStyleMapper(SlotStyleToken token)
	{
		SlotStyle style = token.getStyle();
		
		this.styleController.notifyFullRebuildRequired();
		
		return style.removePropertyStyleMapper();
	}
	
	/**
	 * 
	 * @param token The associated feature holder style to remove the property style mapper from.
	 * @return Whether or not the removal was successful.
	 */
	public boolean removePropertyStyleMapper(FeatureHolderStyleToken token)
	{
		FeatureHolderStyle style = token.getStyle();
		
		this.styleController.notifyFullRebuildRequired();
		
		return style.removePropertyStyleMapper();
	} 
}
