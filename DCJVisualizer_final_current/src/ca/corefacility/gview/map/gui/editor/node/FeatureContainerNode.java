package ca.corefacility.gview.map.gui.editor.node;

import java.util.Enumeration;

import ca.corefacility.gview.map.gui.editor.panel.StylePanel;

/**
 * This node class is an abstract representation of nodes that are intended to represent styles that 
 * contain features, such as slots and sets.
 * 
 * This class is intended to be used within a style editor tree.
 * 
 * @author Eric Marinier
 *
 */
public abstract class FeatureContainerNode extends StyleEditorNode
{
	private static final long serialVersionUID = 1L;	//requested by java 

	/**
	 * 
	 * @param panel The associate style panel.
	 * @param name The name of the node.
	 */
	public FeatureContainerNode(StylePanel panel, String name)
	{
		super(panel, name);
	}
	
	/**
	 * Whether or not node contains a label style node as a child.
	 * 
	 * @return Whether or not this node contains a label style node as a child.
	 */
	public boolean containsLabelStyleNode()
	{		
		boolean result = false;
		
		@SuppressWarnings("rawtypes")
		Enumeration nodes = this.children();	
		
		while(nodes.hasMoreElements() && result == false)
		{
			if(nodes.nextElement() instanceof LabelNode)
				result = true;
		}
		
		return result;
	}
	
	/**
	 * Determines whether or not the FeatureContainerNode has a property mapper node child.
	 * 
	 * @return Whether or not the FeatureContainerNode has a property mapper node child.
	 */
	public boolean containsPropertyMapper()
	{
		boolean result = false;
		
		@SuppressWarnings("rawtypes")
		Enumeration nodes = this.children();	
		
		while(nodes.hasMoreElements() && result == false)
		{
			if(nodes.nextElement() instanceof PropertyMapperNode)
				result = true;
		}
		
		return result;
	}
	
	/**
	 * 
	 * @return Whether or not this node can have a set node added as a child.
	 */
	public abstract boolean canAddSetNodeAsChild();
}
