package ca.corefacility.gview.managers;

public class SectionID
{
	private int sectionLevel;
	private int sectionNumber;
	
	private boolean last = false;
	
	public SectionID(int sectionLevel, int sectionNumber, boolean last)
	{
		this.sectionLevel = sectionLevel;
		this.sectionNumber = sectionNumber;
		
		this.last = last;
	}

	public int getSectionLevel()
	{
		return sectionLevel;
	}

	public int getSectionNumber()
	{
		return sectionNumber;
	}
	
	public String toString()
	{
		return "(" + sectionLevel + "," + sectionNumber + ")";
	}

	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + sectionLevel;
		result = prime * result + sectionNumber;
		return result;
	}

	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SectionID other = (SectionID) obj;
		if (sectionLevel != other.sectionLevel)
			return false;
		if (sectionNumber != other.sectionNumber)
			return false;
		return true;
	}

	public boolean isLast()
	{
		return last;
	}

}
