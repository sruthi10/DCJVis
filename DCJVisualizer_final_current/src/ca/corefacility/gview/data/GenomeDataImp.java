package ca.corefacility.gview.data;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.biojava.bio.seq.FeatureFilter;
import org.biojava.bio.seq.FeatureHolder;
import org.biojava.bio.seq.Sequence;
import org.biojavax.bio.seq.RichSequence;

import ca.corefacility.gview.data.readers.cgview.CGViewSetFeatureFilter;
import ca.corefacility.gview.style.datastyle.FeatureHolderStyle;
import ca.corefacility.gview.style.datastyle.SlotItemStyle;

/**
 * An implementation of GenomeData.
 * 
 * @author Aaron Petkau
 * 
 */
public class GenomeDataImp implements GenomeData
{
	private Sequence sequence;
	private boolean XML = false;

	/**
	 * Stores a mapping of Integer id's from FeatureHolderStyles, to feature holders.
	 */
	private Map<Integer, FeatureHolder> featureHolders;

	/**
	 * Creates a new structure to store the sequence data.
	 * 
	 * @param sequence
	 *            The sequence to create this data around.
	 */
	public GenomeDataImp(Sequence sequence)
	{
		if (sequence == null)
		{
			throw new IllegalArgumentException("sequence is null");
		}
		else if (sequence.length() <= 0)
		{
			throw new IllegalArgumentException("sequence length = " + sequence.length() + " is not positive");
		}
		
		this.sequence = sequence;

		featureHolders = new HashMap<Integer, FeatureHolder>();
	}
	
	/**
	 * Creates a new structure to store the sequence data.
	 * 
	 * @param sequence The sequence to create this data around.
	 * @param XML Whether or not this data is acquired from an XML file.
	 */
	public GenomeDataImp(Sequence sequence, boolean XML)
	{
		this(sequence);
		this.XML = XML;
	}

	public int getInitialBase()
	{
		return 0;
	}

	public FeatureHolder getAllFeatures(FeatureHolderStyle holderStyle)
	{
		FeatureHolder features = null;

		if (holderStyle != null)
		{
			if (holderStyle.getFilter() instanceof CGViewSetFeatureFilter)
			{
				features = ((CGViewSetFeatureFilter)holderStyle.getFilter()).getFeatures();
			}
			else
			{
				Integer key = Integer.valueOf(holderStyle.getId());
	
				if (!holderStyle.isValid())
				{
					featureHolders.remove(key);
					features = generateFeatureHolder(holderStyle);
					
					holderStyle.resetValid();
				}
				else if (featureHolders.containsKey(key))
				{
					features = featureHolders.get(key);
				}
				else
				{
					features = generateFeatureHolder(holderStyle);
				}
			}
		}

		return features;
	}

	public FeatureHolder getOnlyFeatures(FeatureHolderStyle holderStyle)
	{
		FeatureHolder remainingFeatures = null;

		if (holderStyle != null)
		{
			remainingFeatures = getAllFeatures(holderStyle);

			Iterator<SlotItemStyle> styles = holderStyle.styles();

			// subtract sub-features from remaining features
			while (styles.hasNext())
			{
				SlotItemStyle currItemStyle = styles.next();
				if (currItemStyle instanceof FeatureHolderStyle)
				{
					FeatureHolderStyle currFeatureStyle = (FeatureHolderStyle) currItemStyle;

					// TODO this may be slow since we are re-filtering a lot of info each time
					remainingFeatures = remainingFeatures.filter(new FeatureFilter.Not(currFeatureStyle.getFilter()));
				}
			}
		}

		return remainingFeatures;
	}

	public boolean baseOnSequence(int base)
	{
		return (getInitialBase() <= base && base <= (getSequenceLength() - getInitialBase()));
	}

	// generates the set corresponding to this FeatureHolderStyle
	// generates any parent sets if they exist
	private FeatureHolder generateFeatureHolder(FeatureHolderStyle holderStyle)
	{
		FeatureHolder parentFeatures = null;
		FeatureHolder features = null;

		if (holderStyle != null)
		{
			Integer key = Integer.valueOf(holderStyle.getId());

			// base case, if no parent, generate from all features on sequence
			if (holderStyle.getParent() == null)
			{
				parentFeatures = sequence;
			}
			else
			{
				parentFeatures = getAllFeatures(holderStyle.getParent());
			}

			// create features and adds them to featureHolders
			features = parentFeatures.filter(holderStyle.getFilter());
			featureHolders.put(key, features);
		}

		return features;
	}

	public int getSequenceLength()
	{
		return sequence.length();
	}

	@Override
	public Sequence getSequence()
	{
		return this.sequence;
	}
	
	

	@Override
	public FeatureHolder getFeatures(FeatureFilter filter)
	{
		if (filter instanceof CGViewSetFeatureFilter)
		{
			return ((CGViewSetFeatureFilter)filter).getFeatures();
		}
		else
		{
			return sequence.filter(filter);
		}
	}

	@Override
	public boolean isCircular()
	{
		boolean circular = false;
		
		if (sequence instanceof RichSequence)
		{
			circular = ((RichSequence)sequence).getCircular();
		}
		
		return circular;
	}
	
	@Override
	public boolean isXML()
	{
		return this.XML;
	}
}
