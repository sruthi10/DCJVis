package ca.corefacility.gview.style.datastyle.mapper;

import ca.corefacility.gview.map.items.FeatureItem;

/**
 * Classes which implement PropertyStyle will be able to perform a mapping between a Feature property
 * 	and a FeatureItem style aspect.
 * @author aaron
 *
 */
public interface PropertyStyleMapper
{
	public abstract void performMappingFor(FeatureItem item);
}