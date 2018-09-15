package ca.corefacility.gview.map.gui.editor.node;

import ca.corefacility.gview.map.gui.editor.panel.propertyMapper.PropertyMapperPanel;

/**
 * The node class for the property mapper panel.
 * 
 * Intended to be used within a StyleEditorTree.
 * 
 * @author Eric Marinier
 */
public class PropertyMapperNode extends StyleEditorNode 
{
	private static final long serialVersionUID = 1L;	//requested by java
	private static final String PROPERTY_MAPPER = "Property Mapper";
	
	private final PropertyMapperPanel propertyMapperPanel;	//the related property mapper panel
	
	/**
	 * 
	 * @param propertyMapperPanel The related property mapper panel.
	 */
	public PropertyMapperNode(PropertyMapperPanel propertyMapperPanel) 
	{
		super(propertyMapperPanel, PROPERTY_MAPPER);
		
		if(propertyMapperPanel == null)
		{
			throw new IllegalArgumentException("PropertyMapperPanel is null.");
		}

		this.propertyMapperPanel = propertyMapperPanel;
	}
	
	@Override
	/**
	 * Returns the property mapper panel.
	 */
	public PropertyMapperPanel getPanel() 
	{
		if(this.propertyMapperPanel == null)
		{
			throw new IllegalArgumentException("PropertyMapperPanel is null.");
		}
		
		return this.propertyMapperPanel;
	}
	
	@Override
	public FeatureContainerNode getParent()
	{
		FeatureContainerNode parent = null;
		
		if(super.getParent() instanceof FeatureContainerNode)
		{
			parent = (FeatureContainerNode)super.getParent();
		}
		else if(super.getParent() != null)
		{
			throw new ClassCastException("PropertyMapperNode has non-FeatureContainerNode parent.");
		}
		
		return parent;
	}
}
