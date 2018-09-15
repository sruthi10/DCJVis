package ca.corefacility.gview.style.datastyle.mapper;

import org.biojava.bio.Annotation;
import org.biojava.bio.seq.Feature;

public class PropertyMapperScore implements ContinuousPropertyMapper
{
	private float minScore;
	private float maxScore;
	
	public PropertyMapperScore(float minScore, float maxScore)
	{
		if (minScore > maxScore)
		{
			throw new IllegalArgumentException("minScore > maxScore");
		}
		
		this.minScore = minScore;
		this.maxScore = maxScore;
	}
	
	@Override
	public float propertyValue(Feature f)
	{
		float value = INVALID;
		
		if (f == null)
		{
			throw new IllegalArgumentException("feature f is null");
		}
		
		Annotation a = f.getAnnotation();
		
		if (a != null)
		{
			if (a.containsProperty("Score"))
			{
				Object scoreObj = a.getProperty("Score");
				
				if (scoreObj instanceof String)
				{
					String scoreStr = (String)scoreObj;
					
					try
					{
						float score = Float.valueOf(scoreStr);
						
						if (score < minScore)
						{
							value = 0.0f;
						}
						else if (score > maxScore)
						{
							value = 1.0f;
						}
						else
						{
							value = (score - minScore)/(maxScore - minScore);
						}
					}
					catch (NumberFormatException e)
					{
					}
				}
			}
		}
		
		return value;
	}
	
	@Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(maxScore);
        result = prime * result + Float.floatToIntBits(minScore);
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PropertyMapperScore other = (PropertyMapperScore) obj;
        if (Float.floatToIntBits(maxScore) != Float
                .floatToIntBits(other.maxScore))
            return false;
        if (Float.floatToIntBits(minScore) != Float
                .floatToIntBits(other.minScore))
            return false;
        return true;
    }

    public float getMaxScore()
	{
		return this.maxScore;
	}
	
	public float getMinScore()
	{
		return this.minScore;
	}
}
