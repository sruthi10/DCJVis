package ca.corefacility.gview.textextractor;

import java.util.NoSuchElementException;

import org.biojava.bio.seq.Feature;

/**
 * Extracts an arbitrary annotation from a feature.  Assumes passed annotation name refers to an annotation accepting the toString() method.
 * @author Aaron Petkau
 *
 */
public class AnnotationExtractor implements FeatureTextExtractor
{
	private String annotation;

	public AnnotationExtractor(String annotation)
	{
		if (annotation == null)
			throw new IllegalArgumentException("annotation is null");

		this.annotation = annotation;
	}

	public String extractText(Feature feature)
	{
		String text = null;

		try
		{
			text = feature.getAnnotation().getProperty(this.annotation).toString();
		}
		catch (NoSuchElementException e)
		{
			text = null;
		}

		return text;
	}

	public String getAnnotation()
	{
		return this.annotation;
	}

	@Override
	public Object clone()
	{
		return new AnnotationExtractor(this.annotation);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
		+ (this.annotation == null ? 0 : this.annotation.hashCode());
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
		AnnotationExtractor other = (AnnotationExtractor) obj;
		if (this.annotation == null)
		{
			if (other.annotation != null)
				return false;
		}
		else if (!this.annotation.equals(other.annotation))
			return false;
		return true;
	}
}
