package ca.corefacility.gview.managers.labels;

import java.util.ArrayList;

import ca.corefacility.gview.managers.ResolutionManager;

public class LabelBuckets
{
	private ArrayList<ArrayList<LabelPosition>> buckets;
	private ResolutionManager resolutionManager;

	private int currentBucketValue = -1;

	public LabelBuckets(ResolutionManager resolutionManager, double maxScale)
	{
		this.resolutionManager = resolutionManager;

		buckets = new ArrayList<ArrayList<LabelPosition>>();

		createBuckets(maxScale);
	}

	private void createBuckets(double maxScale)
	{
		int maxResolutionLevels = resolutionManager.getMaxResolutionLevels(maxScale);

		for (int i = 0; i < maxResolutionLevels; i++)
		{
			buckets.add(i, new ArrayList<LabelPosition>());
		}
	}

	/**
	 * Gets the index of the bucket which contains the passed scale
	 * 
	 * @param zoomValue
	 * @return The index of the bucket given the passed scale.
	 */
	private int getBucketIndex(double zoomValue)
	{
		int bucketIndex = -1;

		if (zoomValue >= 0) // already must be in 1st bucket or above after this
							// check
		{
			bucketIndex = resolutionManager.getResolutionLevel(zoomValue);
		}

		return bucketIndex;
	}

	/**
	 * Gives us the bucket value the passed zoomValue would be added to.
	 * 
	 * @param zoomValue
	 * @return The bucket value (an integer).
	 */
	public int getBucketToAddTo(double zoomValue)
	{
		int bucketValue = -1;

		if (zoomValue == 0)
		{
			bucketValue = 0;
		}
		else if (zoomValue > 0)
		{
			// +1 since getResolutionLevel gives us the resolution of the passed zoomValue that lies within,
			// but the bucket value would be the bucket that completely contains zoom values >= passed zoomValue
			bucketValue = resolutionManager.getResolutionLevel(zoomValue) + 1; 
		}
		else
		{
			throw new IllegalArgumentException("zoomValue can not be negative");
		}

		return bucketValue;
	}

	/**
	 * Adds the passed LabelPosition to the bucket with the passed value.
	 * 
	 * @param labelPosition
	 *            The position of the Label to add to this bucket.
	 * @param bucketValue
	 *            The value of the bucket to add to. Must be between 0 and
	 *            maxBucketValue().
	 */
	public void addToBucket(LabelPosition labelPosition, int bucketValue)
	{
		if (bucketValue < 0 || bucketValue > getMaxBucketValue())
		{
			throw new IllegalArgumentException("bucketValue must be non-negative");
		}

		ArrayList<LabelPosition> bucket;

		bucket = buckets.get(bucketValue);

		bucket.add(labelPosition);
	}

	/**
	 * Updates to the passed scale.
	 * 
	 * @param zoomValue
	 *            The scale to update to, must be positive.
	 */
	public void toNewScale(double zoomValue)
	{
		if (zoomValue <= 0)
		{
			throw new IllegalArgumentException("zoomValue must be positive");
		}

		int bucketIndex = getBucketIndex(zoomValue);

		changeToBucket(bucketIndex);
	}

	private void changeToBucket(int value)
	{
		value = value - 1;

		if (value >= buckets.size() || value < 0)
		{
			throw new IllegalArgumentException("bucket value must be in range [0," + buckets.size() + "]");
		}

		if (value > currentBucketValue) // if we are increasing to higher bucket
										// value
		{
			// for each bucket from currentBucketValue to value
			for (int curr = currentBucketValue + 1; curr <= value; curr++)
			{
				for (LabelPosition currLabelPosition : buckets.get(curr))
				{
					currLabelPosition.changeLabelTo();
				}
			}
		}
		else if (value < currentBucketValue)
		{
			for (int curr = currentBucketValue; curr > value; curr--)
			{
				for (LabelPosition currLabelPosition : buckets.get(curr))
				{
					currLabelPosition.undoChangeLabelTo();
				}
			}
		}
		// if equal, don't do anything

		currentBucketValue = value;
	}

	/**
	 * Gets the maximum allowable bucket value.
	 * 
	 * @return The maximum allowed bucket value.
	 */
	public int getMaxBucketValue()
	{
		return buckets.size() - 1;
	}
}
