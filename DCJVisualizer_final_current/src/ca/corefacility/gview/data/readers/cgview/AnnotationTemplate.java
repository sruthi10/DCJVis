package ca.corefacility.gview.data.readers.cgview;

/**
 * A class used to store properties belonging to the annotation of a Featre.
 * @author aaron
 *
 */
class AnnotationTemplate
{
	public String hyperlink = null;
	public String label = null;
	public String mouseover = null;

	public AnnotationTemplate()
	{
	}

	public AnnotationTemplate(AnnotationTemplate template)
	{
		this.hyperlink = template.hyperlink;
		this.label = template.label;
		this.mouseover = template.mouseover;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((hyperlink == null) ? 0 : hyperlink.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result
				+ ((mouseover == null) ? 0 : mouseover.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		AnnotationTemplate other = (AnnotationTemplate) obj;
		if (hyperlink == null)
		{
			if (other.hyperlink != null)
			{
				return false;
			}
		} else if (!hyperlink.equals(other.hyperlink))
		{
			return false;
		}
		if (label == null)
		{
			if (other.label != null)
			{
				return false;
			}
		} else if (!label.equals(other.label))
		{
			return false;
		}
		if (mouseover == null)
		{
			if (other.mouseover != null)
			{
				return false;
			}
		} else if (!mouseover.equals(other.mouseover))
		{
			return false;
		}
		return true;
	}
}
