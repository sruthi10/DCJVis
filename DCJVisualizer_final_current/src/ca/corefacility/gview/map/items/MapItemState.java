package ca.corefacility.gview.map.items;

/**
 * Defines the states a map item can be in.
 * 
 * @author Aaron Petkau
 * 
 */
public enum MapItemState
{
	/**
	 * Normal state.
	 */
	NORMAL,

	/**
	 * Indicates that the mouse is hovering over the item with this state.
	 */
	MOUSE_OVER,

	/**
	 * Indicates that this item was selected.
	 */
	SELECTED,

	/**
	 * Indicates that this item was selected, and the mouse is hovering over this item.
	 */
	MOUSE_OVER_SELECTED,

	/**
	 * Indicates that this item's style just changed
	 */
	STYLED,

	/**
	 * Indicates that this item's style just changed and the mouse is also over it
	 */
	MOUSE_OVER_STYLED;

	/**
	 * @return True if this state is a selected state, false otherwise.
	 */
	public boolean isSelected()
	{
		return (this.equals(MapItemState.SELECTED) || this.equals(MapItemState.MOUSE_OVER_SELECTED));
	}

	/**
	 * @return True if this state is a mouse over state, false otherwise.
	 */
	public boolean isMouseOver()
	{
		return (this.equals(MapItemState.MOUSE_OVER) || this.equals(MapItemState.MOUSE_OVER_SELECTED));
	}

	public boolean isStyled()
	{
		return (this.equals(MapItemState.STYLED) || this.equals(MapItemState.MOUSE_OVER_STYLED));
	}
}
