package ca.corefacility.gview.managers.labels;

import ca.corefacility.gview.layout.sequence.SlotTranslator;

public class LabelSlots
{
	private final int maxInSlotsArray;

	private double[] slotsAbove; // slots starting height
	private double[] slotsBelow;
	private int[] maxInSlotAbove;
	private int[] maxInSlotBelow;
	
//	private double slotThickness;
	
	private double topClosestHeight;
	private double bottomClosestHeight;

	/**
	 * Creates a new LabelSlots object.
	 * @param slotTranslator  The SlotTranslator to use to determine thickness of Feature/etc slots
	 * @param maxSlots  The maximum number of slots for labels.
	 * @param initialPadding  The inititalPadding above feature slots for label slots.
	 * @param slotThickness The height of each slot.
	 * @param padding  How much extra space between slots.
	 */
	public LabelSlots(SlotTranslator slotTranslator, int maxSlots, double initialPadding, double slotThickness, double padding)
	{
		this.maxInSlotsArray = maxSlots+1;
//		this.slotThickness = slotThickness;
		
		slotsAbove = new double[maxInSlotsArray];
		slotsBelow = new double[maxInSlotsArray];
		maxInSlotAbove = new int[maxInSlotsArray];
		maxInSlotBelow = new int[maxInSlotsArray];
		
		topClosestHeight = slotTranslator.getTopMostHeight();
		bottomClosestHeight = slotTranslator.getBottomMostHeight();

		slotsAbove[(1) - 1] = slotTranslator.getTopMostHeight()
			+ initialPadding;
		
		slotsBelow[(-1) + 1] = slotTranslator.getBottomMostHeight()
			- initialPadding;
		
		maxInSlotAbove[(1) - 1] = 100;
		maxInSlotBelow[(-1) + 1] = maxInSlotAbove[(1) - 1];

		for (int i = 1; i < maxInSlotsArray; i++)
		{
			slotsAbove[i] = slotsAbove[i - 1] + slotThickness + padding;
			slotsBelow[i] = slotsBelow[i - 1] - slotThickness - padding;
			
			maxInSlotAbove[i] = maxInSlotAbove[i - 1] + 10;
			maxInSlotBelow[i] = maxInSlotBelow[i - 1] + 10;
		}
	}

	public double getSlotHeight(int slot)
	{
		if (slot <= -maxInSlotsArray || slot >= maxInSlotsArray || slot == 0)
		{
			throw new IllegalArgumentException("slot out of range of [" + -(maxInSlotsArray-1) + "," + (maxInSlotsArray - 1) + "] or slot is 0");
		}

		return (slot > 0) ? slotsAbove[slot - 1] : slotsBelow[-slot-1];
	}
	
	/**
	 * @return  The closest height on the top.
	 */
	public double getClosestTopHeight()
	{
		return topClosestHeight;
	}
	
	public double getClosestBottomHeight()
	{
		return bottomClosestHeight;
	}
	
	public int getMaxSlots()
	{
		return maxInSlotsArray;
	}
	
	public int getTopSlot()
	{
		return slotsAbove.length;
	}
	
	public int getBottomSlot()
	{
		return -slotsBelow.length;
	}

	public int getMaxLabelsInSlot(int slot)
	{
		if (slot <= -maxInSlotsArray || slot >= maxInSlotsArray || slot == 0)
		{
			throw new IllegalArgumentException("slot out of range of [" + -(maxInSlotsArray-1) + "," + (maxInSlotsArray - 1) + "] or slot is 0");
		}

		return (slot > 0) ? maxInSlotAbove[slot - 1] : maxInSlotBelow[slot + 1];
	}
}
