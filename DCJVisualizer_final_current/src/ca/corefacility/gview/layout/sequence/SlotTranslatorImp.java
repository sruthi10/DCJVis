package ca.corefacility.gview.layout.sequence;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ca.corefacility.gview.data.Slot;
import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.style.datastyle.DataStyle;
import ca.corefacility.gview.style.datastyle.SlotStyle;
import ca.corefacility.gview.style.items.BackboneStyle;
import ca.corefacility.gview.style.items.RulerStyle;

// maybe we should define backbone/rulers in here, so we are defining the constants where we use
// them?
// redo this class, it's become a mess
public class SlotTranslatorImp implements SlotTranslator
{
	// all the thicknesses for slots are kept in mapStyle, someplace
	// private MapStyle mapStyle;

	// keeps track of upper/lower ruler slot numbers
	private int upperRulerSlot;
	private int lowerRulerSlot;

	private List<Double> upperSlots;
	private List<Double> lowerSlots;

	private double slotSpacing = 0.0;
	
	private RulerLabelThicknessCalculator rulerLabelThickness;

	/**
	 * Creates a new implementation for SlotTranslator.
	 * 
	 * @param mapStyle
	 *            The style for this current map, used to extract correct thicknesses of slots.
	 * @param rulerLabelCalculator The object used to calculate the maximum thickness of a ruler label.
	 */
	public SlotTranslatorImp(MapStyle mapStyle, RulerLabelThicknessCalculator rulerLabelCalculator)
	{
		if (mapStyle == null)
		{
			throw new IllegalArgumentException("mapStyle is null");
		}
		
		extractThicknesses(mapStyle.getDataStyle(), 
				mapStyle.getGlobalStyle().getBackboneStyle(), mapStyle.getGlobalStyle().getRulerStyle());
		slotSpacing = mapStyle.getGlobalStyle().getSlotSpacing();

		if (slotSpacing < 0)
			slotSpacing = 0;
		
		this.rulerLabelThickness = rulerLabelCalculator;
	}

	private int getMaxSlot()
	{
		return (upperSlots.size() - 1);
	}

	private int getMinSlot()
	{
		return (-lowerSlots.size());
	}

	private double getThickness(int slot)
	{
		Double thickness = Double.valueOf(-1);
		int realSlot = getRealSlotNumber(slot);

		if (realSlotIsValid(slot))
		{
			int index = realSlotToIndex(realSlot);

			if (realSlot >= 0)
			{
				thickness = upperSlots.get(index);
			}
			else
			{
				thickness = lowerSlots.get(index);
			}
		}

		return thickness.doubleValue();
	}

	private double getHeightUpTo(int slot)
	{
		int realSlot = getRealSlotNumber(slot);

		double height = 0;

		if (slotIsValid(realSlot))
		{
			if (realSlot == Slot.BACKBONE)
			{
				height = 0;
			}
			else if (realSlot > Slot.BACKBONE)
			{
				for (int currentSlot = (Slot.BACKBONE + 1); currentSlot <= realSlot; currentSlot++)
				{
					int prevSlot = currentSlot - 1;
					height += (getThickness(prevSlot) / 2 + slotSpacing + getThickness(currentSlot) / 2);
				}
			}
			else
			{
				for (int currentSlot = (Slot.BACKBONE - 1); realSlot <= currentSlot; currentSlot--)
				{
					int prevSlot = currentSlot + 1;
					height -= (getThickness(prevSlot) / 2 + slotSpacing + getThickness(currentSlot) / 2);
				}
			}
		}

		return height;
	}

	public double getHeightFromBackbone(int slot, double heightInSlot)
	{
		double heightFromBackbone = 0.0;

		if (!slotIsValid(slot))
		{
			throw new IllegalArgumentException("slot=" + slot + " is invalid");
		}
		else if (!heightInSlotInRange(heightInSlot))
		{
			throw new IllegalArgumentException("heightInSlot=" + heightInSlot + " is invalid");
		}
		else
		{
			double slotCenterHeightFromBackbone = getHeightUpTo(slot);

			// height offset from the center of this slot
			// TODO check if this is correct
			double offsetFromCenter = (getThickness(slot) / 2) * heightInSlot;

			heightFromBackbone = slotCenterHeightFromBackbone + offsetFromCenter;
		}

		return heightFromBackbone;
	}

	// TODO properly handle changing thicknesses
	private void extractThicknesses(DataStyle dataStyle, BackboneStyle backboneStyle, RulerStyle rulerStyle)
	{
		Iterator<SlotStyle> slotStyles = dataStyle.slots();
		SlotStyle currentSlotStyle;

		int maxSlotIndex;
		int minSlotIndex;

		// make room for ruler slots
		upperRulerSlot = dataStyle.getUpperSlot() + 1;
		lowerRulerSlot = dataStyle.getLowerSlot() - 1;

		maxSlotIndex = realSlotToIndex(upperRulerSlot);
		minSlotIndex = realSlotToIndex(lowerRulerSlot);

		upperSlots = new ArrayList<Double>(maxSlotIndex + 1);
		lowerSlots = new ArrayList<Double>(minSlotIndex + 1);

		// TODO change how this is done
		initalizeList(upperSlots, maxSlotIndex + 1);
		initalizeList(lowerSlots, minSlotIndex + 1);

		// set DataStyle slots
		while (slotStyles.hasNext())
		{
			currentSlotStyle = slotStyles.next();

			setThickness(currentSlotStyle.getSlot(), currentSlotStyle.getThickness());
		}

		// set backbone thickness
		setThickness(Slot.BACKBONE, backboneStyle.getThickness());

		// set ruler slot thicknesses
		
		// pull out the largest length (out of major or minor tick lengths)
		double largestLength =
			rulerStyle.getMajorTickLength() <  rulerStyle.getMinorTickLength()
			? rulerStyle.getMinorTickLength() : rulerStyle.getMajorTickLength();
				
		setThickness(Slot.UPPER_RULER, largestLength);
		setThickness(Slot.LOWER_RULER, largestLength);
	}

	// adds initial values to the passed list so it has a size of size
	// used so that we can set slot thicknesses in this list in random order
	// not sure if this is good
	private void initalizeList(List<Double> list, int size)
	{
		for (int i = 0; i < size; i++)
		{
			list.add(Double.valueOf(0.0));
		}
	}

	// gets the actual slot number (accounting for special slots)
	private int getRealSlotNumber(int slot)
	{
		int realSlot = 0;

		// account for special slots
		if (slot == Slot.BACKBONE)
		{
			realSlot = 0;
		}
		else if (slot == Slot.UPPER_RULER)
		{
			realSlot = getMaxSlot();
		}
		else if (slot == Slot.LOWER_RULER)
		{
			realSlot = getMinSlot();
		}
		// TODO account for slots out of range of SlotRegion.getMaxSlot() or whatever
		else
		{
			realSlot = slot;
		}

		return realSlot;
	}

	// sets the thickness of the passed slot
	private void setThickness(int slot, double thickness)
	{
		List<Double> slots;

		int realSlot = getRealSlotNumber(slot);

		if (realSlotIsValid(realSlot))
		{
			if (realSlot >= 0)
			{
				slots = upperSlots;
			}
			else
			{
				slots = lowerSlots;
			}

			slots.set(realSlotToIndex(realSlot), Double.valueOf(thickness));
		}
	}

	// determines if the real (actual slot number) is valid
	private boolean realSlotIsValid(int realSlot)
	{
		boolean valid = false;

		if ((getMinSlot() <= realSlot) || (realSlot <= getMaxSlot()))
		{
			valid = true;
		}

		return valid;
	}

	// converts the realSlot number to an index in the array
	private int realSlotToIndex(int realSlot)
	{
		if (realSlot >= 0)
		{
			return realSlot;
		}
		else
		{
			return (Math.abs(realSlot) - 1);
		}
	}

	/**
	 * Verifies that the heightInSlot is in the correct range.
	 * 
	 * @param heightInSlot
	 *            The value to check.
	 * 
	 * @return true if in range, false if out of range.
	 */
	private boolean heightInSlotInRange(double heightInSlot)
	{
		if ((heightInSlot < BOTTOM) || (heightInSlot > TOP))
		{
			return false;
		}
		return true;
	}

	// do we need this?
	private boolean slotIsValid(int slot)
	{
		boolean valid = false;

		if (realSlotIsValid(getRealSlotNumber(slot)))
		{
			valid = true;
		}

		return valid;
	}

	public double getBottomMostHeight()
	{
		double height = 0;

		if (slotIsValid(Slot.BACKBONE))
		{
			int minSlot = getMinSlot();

			height = -getThickness(Slot.BACKBONE) / 2;

			for (int currentSlot = -1; minSlot <= currentSlot; currentSlot--)
			{
				// Double currThickness = lowerSlots.get(realSlotToIndex(currentSlot));
				height -= (getThickness(currentSlot) + slotSpacing);// currThickness.doubleValue();
			}
		}

		return height;
	}

	public double getTopMostHeight()
	{
		double height = 0;

		if (slotIsValid(Slot.BACKBONE))
		{
			int maxSlot = getMaxSlot();

			height = getThickness(Slot.BACKBONE) / 2;

			for (int currentSlot = 1; currentSlot <= maxSlot; currentSlot++)
			{
				// Double currThickness = upperSlots.get(realSlotToIndex(currentSlot));
				height += getThickness(currentSlot) + slotSpacing;// currThickness.doubleValue();
			}
		}

		return height;
	}

	@Override
	public double getTopTickThickness()
	{
		return rulerLabelThickness.getMaxTopThickness();
	}

	@Override
	public double getBottomTickThickness()
	{
		return rulerLabelThickness.getMaxBottomThickness();
	}

	// remove this later
	/*
	 * public void setScale(double scale) { for (int i = 0; i < upperSlots.size(); i++) {
	 * upperSlots.set(i, upperSlots.get(i)/scale); } }
	 */
}
