package ca.corefacility.gview.map.gui.editor;

import ca.corefacility.gview.map.gui.editor.panel.propertyMapper.ContinuousMapperListItem;
import ca.corefacility.gview.style.datastyle.mapper.PropertyMapperScore;

/**
 * This class represents a score to opacity property mapper as a list item.
 * 
 * @author Eric Marinier
 */
public class ScoreToOpacityMapperListItem extends ContinuousMapperListItem
{
	private static final String SCORE = "Score";
	private static final String OPACITY = "Opacity";
	
	private float minScore;	//the minimum score in the property mapper
	private float maxScore;	//the maximum score in the property mapper
	
	/**
	 * Pulls the necessary information from the passed property mapper.
	 * 
	 * @param propertyMapper The property mapper to pull the min and max score from.
	 */
	public ScoreToOpacityMapperListItem(PropertyMapperScore propertyMapper)
	{
		this(propertyMapper.getMinScore(), propertyMapper.getMaxScore());
	}	

	/**
	 * 
	 * @param min The minimum score in the property mapper.
	 * @param max The maximum score in the property mapper.
	 */
	public ScoreToOpacityMapperListItem(float min, float max)
	{
		super();
		
		this.minScore = min;
		this.maxScore = max;
		
		updateName();
	}
	
	/**
	 * 
	 * @return The minimum score.
	 */
	public float getMin()
	{
		return this.minScore;
	}
	
	/**
	 * 
	 * @return The maximum score.
	 */
	public float getMax()
	{
		return this.maxScore;
	}
	
	/**
	 * Updates the name of the list item.
	 */
	private void updateName()
	{
		super.setName(SCORE + " [" + this.minScore + ", " + this.maxScore + "]" + " -> " + OPACITY);
	}
	
	/**
	 * Sets the max score.
	 * 
	 * @param max
	 */
	public void setMax(float max)
	{
		this.maxScore = max;
		
		updateName();
	}
	
	/**
	 * Sets the min score.
	 * @param min
	 */
	public void setMin(float min)
	{
		this.minScore = min;
		
		updateName();
	}
}
