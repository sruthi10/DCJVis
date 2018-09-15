package ca.corefacility.gview.managers.labels;

public enum LabelRank
{
	/**
	 * Ranking assigned to labels that must be placed on the map.
	 */
	ESSENTIAL(0),
	
	/**
	 * Ranking assigned to labels that do not need to be placed on the map.
	 */
	NON_ESSENTIAL(1),
	
	/**
	 * Ranking assigned to labels that should not be displayed on the map.
	 */
	HIDDEN(2);
	
	private int value;
	
	private LabelRank(int value)
	{
		this.value = value;
	}
	
	public int value()
	{
		return value;
	}
	
	public static int max()
	{
		return 3;
	}
}
