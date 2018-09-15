package ca.corefacility.gview.map.items;

public class BasicItem extends AbstractShapeItem
{
	private static final long serialVersionUID = 4375411252058958523L;
	private String tooltip; // stores text used for tooltip
	
	public BasicItem(String tooltip)
	{
		this.tooltip = tooltip;
	}
	
	public BasicItem()
	{
		this(null);
	}

	@Override
	public String getToolTip()
	{
		return tooltip;
	}
}
