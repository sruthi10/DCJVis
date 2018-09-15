package ca.corefacility.gview.managers.labels;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Stores a collection of labels with different rankings.
 * @author Aaron Petkau
 *
 */
public class LabelsStore
{
	private List<Label> essentialLabels;
	private List<Label> nonEssentialLabels;
	private List<Label> hiddenLabels; // is there any reason to store these?
	
	public LabelsStore()
	{
		essentialLabels = new ArrayList<Label>();
		nonEssentialLabels = new ArrayList<Label>();
		hiddenLabels = new ArrayList<Label>();
	}
	
	public void addLabel(Label label, LabelRank labelRank)
	{
		if (labelRank == LabelRank.ESSENTIAL)
		{
			essentialLabels.add(label);
		}
		else if (labelRank == LabelRank.NON_ESSENTIAL)
		{
			nonEssentialLabels.add(label);
		}
		else
		{
			hiddenLabels.add(label);
		}
	}
	
	// TODO change how this is done (maybe add in order) later
	public void sort()
	{
//		Collections.sort(essentialLabels);
//		Collections.sort(nonEssentialLabels);
//		Collections.sort(hiddenLabels);
	}
	
	public Iterator<Label> getIteratorFor(LabelRank labelRank)
	{
		if (labelRank == LabelRank.ESSENTIAL)
		{
			return essentialLabels.iterator();
		}
		else if (labelRank == LabelRank.NON_ESSENTIAL)
		{
			return nonEssentialLabels.iterator();
		}
		else if (labelRank == LabelRank.HIDDEN)
		{
			return hiddenLabels.iterator();
		}
		
		return (new ArrayList<Label>()).iterator(); // blank iterator
	}
	
	public Iterator<Label> iterator()
	{
		return essentialLabels.iterator();
		
		// need to make this iterator over all labels
//		return new Iterator<Label>()
//		{
//			private Iterator<Label> currIterator;
//			
//
//			public boolean hasNext()
//			{
//				// TODO Auto-generated method stub
//				return false;
//			}
//
//			public Label next()
//			{
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			public void remove()
//			{
//				// TODO Auto-generated method stub
//				
//			}
//			
//		};
	}
}
