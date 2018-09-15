package ca.corefacility.gview.utils;

import org.biojava.bio.Annotatable;
import org.biojava.bio.Annotation;
import org.biojava.bio.seq.Feature;
import org.biojava.bio.seq.FeatureFilter;

/**
 * Used to filter features who's annotation's value contains a substring of the passed string
 * TODO find another package for this
 * @author aaron
 *
 */
public class AnnotationValueContains implements FeatureFilter
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7491645131162377963L;
	
	private String key;
	private String value;
	
	public AnnotationValueContains(String key, String value)
	{
		super();
		this.key = key;
		this.value = value;
		
		if (value != null)
		{
			this.value = this.value.toLowerCase();
		}
	}

	public String getKey()
	{
		return key;
	}

	public String getValue()
	{
		return value;
	}

	@Override
	public boolean accept(Feature feature)
	{
		boolean accepted = false;
		
		if (feature != null && feature instanceof Annotatable)
		{
			Annotatable anFeature = (Annotatable)feature;
			
			Annotation ann = anFeature.getAnnotation();
			
			if (ann.containsProperty(key))
			{
				Object property = ann.getProperty(key);
				
				if (property instanceof String)
				{
					if (((String)property).toLowerCase().contains(value))
					{
						accepted = true;
					}
				}
			}
		}
		
		return accepted;
	}
}
