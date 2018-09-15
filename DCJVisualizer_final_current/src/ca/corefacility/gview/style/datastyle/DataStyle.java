package ca.corefacility.gview.style.datastyle;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import ca.corefacility.gview.map.event.GViewEvent;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.event.GViewEventSubject;
import ca.corefacility.gview.map.event.GViewEventSubjectImp;

/**
 * DataStyle controls any style information directly related to the specific genome data to use.  So, it can be used to control which genes to display where,
 * colors to use, slots, etc.
 * @author Aaron Petkau
 *
 */
public class DataStyle implements GViewEventListener, GViewEventSubject
{	
	private FeatureHolderStyleFactory holderFactory;
	
	private SortedMap<Integer, SlotStyle> slotStyles; // sorted so we can iterate through slots in order
	
	// for events
	private GViewEventSubjectImp eventSubject;
	
	/**
	 * Creates a new data style.
	 */
	public DataStyle()
	{
		slotStyles = new TreeMap<Integer, SlotStyle>();
		
		holderFactory = new FeatureHolderStyleFactory();
		
		eventSubject = new GViewEventSubjectImp();
	}

	/**
	 * Obtains a style for a feature holder with the passed name.
	 * 
	 * @param name  The name of the feature holder whose style to obtain.
	 * 
	 * @return The feature holder style (null if no style).
	 */
	/*public ItemHolderStyle getFeatureHolderStyle(String name)
	{
		return holderStyles.get(name);
	}*/
	
	/**
	 * Obtains an iterator for all the feature holder styles stored by this object.
	 * 
	 * @return  Iterator of all the feature holder styles.
	 */
	/*public Iterator<ItemHolderStyle> featureHoldersIterator() // do we need this?
	{
		return holderStyles.values().iterator();
	}*/
	
	/**
	 * Obtains an iterator for all the feature slot styles stored by this object.
	 * 
	 * @return  Iterator of all the feature slot styles.
	 */
	public Iterator<SlotStyle> slots() // do we need this?
	{
		return slotStyles.values().iterator();
	}
	
	/**
	 * @return  The lowest slot number in use.
	 */
	public int getLowerSlot()
	{
		int lowerSlot;
		
		if (slotStyles.isEmpty())
		{
			lowerSlot = 0;
		}
		else
		{
			Integer slot = slotStyles.firstKey();
			
			// this is done since if we only have positive slots, we still want to return 0
			if (slot.intValue() > 0)
			{
				lowerSlot = 0;
			}
			else
			{
				lowerSlot = slot.intValue();
			}
		}

		return lowerSlot;
	}
	
	/**
	 * @return  The greatest slot number in use.
	 */
	public int getUpperSlot()
	{
		int upperSlot;
		
		if (slotStyles.isEmpty())
		{
			upperSlot = 0;
		}
		else
		{
			Integer slot = slotStyles.lastKey();
			
			// this is done so that if we only have negative slots in use, we still want to return 0
			if (slot.intValue() < 0)
			{
				upperSlot = 0;
			}
			else
			{
				upperSlot = slot.intValue();
			}
		}

		return upperSlot;
	}
	
	/**
	 * Determines if this DataStyle contains a SlotStyle object for the passed slot number.
	 * @param slot  The slot number to check.
	 * @return  True if this DataStyle contains a SlotStyle object for the passed slot, false otherwise.
	 */
	public boolean containsSlotStyle(int slot)
	{
		return slotStyles.get(slot) != null;
	}
	
	/**
	 * Obtains a style for a feature slot with the passed name (creating it if necessary).
	 * 
	 * @param slot  The slot number of the feature slot whose style to obtain.
	 * 
	 * @return The feature slot style (null if no style).
	 */
	public SlotStyle getSlotStyle(int slot)
	{		
		return slotStyles.get(slot);
	}
	
	/**
	 * Creates a new slot style with the passed slot.  Note:  you must create slots in order, so you can't create slot 1 and then slot 3 (must create slot 2 first).
	 * If a slot is to be created over another slot, it will 'push' the appropriate slots up or down and take that position.
	 * 
	 * @param slot  The slot that this slot style will refer to.
	 * 					Use one of Slot.FIRST_UPPER, or Slot.FIRST_LOWER, for the first upper/lower slots.
	 * 					Add numbers onto these values to increase/decrease the slot number.
	 * @return  The SlotStyle object for the newly created slot.
	 */
	public SlotStyle createSlotStyle(int slot)
	{
		SlotStyle style = null;
		SlotStyle currentStyle;
		SlotStyle previousStyle;
		
		int upperSlotNumber = getUpperSlot();
		int lowerSlotNumber = getLowerSlot();		
		int upperSlotBounds = upperSlotNumber + 1;
		int lowerSlotBounds = lowerSlotNumber - 1;
		
		// TODO clean this up later, we added 1 so that we could create a new slot after any currently created slots.
		// checks if passed slot is out of range (must create slots within range).
		if (slot > upperSlotBounds || slot < lowerSlotBounds)
		{
			throw new IllegalArgumentException("slot=" + slot + " out of range of [" + lowerSlotBounds + ", " + upperSlotBounds + "]");
		}
		else
		{
			if(slotStyles.get(slot) != null)
			//there is a slot already at that position.. shuffle the appropriate slots to make room and insert
			{
				//priming the previous style with the new style to insert
				style = new SlotStyle(slot, holderFactory);
				previousStyle = style;
				
				//slot to insert is above the backbone
				if(slot > 0)
				{						
					for(int i = slot; i <= upperSlotNumber + 1; i++)
					{
						currentStyle = slotStyles.get(i);
						slotStyles.put(i, previousStyle);
						previousStyle.setSlotNumber(i);
						
						previousStyle = currentStyle;
					}
				}
				//slot to insert is below the backbone
				else if(slot < 0)
				{
					for(int i = slot; i >= lowerSlotNumber - 1; i--)
					{
						currentStyle = slotStyles.get(i);
						slotStyles.put(i, previousStyle);
						previousStyle.setSlotNumber(i);
						
						previousStyle = currentStyle;
					}
				}
				else
				{
					throw new IllegalArgumentException("Cannot insert a new slot in the backbone (slot 0)");
				}
			}
			//no need to move any other slots
			else
			{
				style = new SlotStyle(slot, holderFactory);
				slotStyles.put(slot, style);
			}
		}
		
		return style;
	}
	
	/**
	 * Removes the slot style at slotNumber and pulls the appropriate slots closer to the backbone to fill the gap.
	 * 
	 * @param slotNumber The number of the slot to remove.
	 */
	private void removeSlotStyle(int slotNumber)
	{
		SlotStyle currentSlotStyle;
		
		int upperSlotNumber = getUpperSlot();
		int lowerSlotNumber = getLowerSlot();		
		int upperSlotBounds = upperSlotNumber + 1;
		int lowerSlotBounds = lowerSlotNumber - 1;
		
		if (slotNumber > upperSlotBounds || slotNumber < lowerSlotBounds)
		{
			throw new IllegalArgumentException("slot=" + slotNumber + " out of range of [" + lowerSlotBounds + ", " + upperSlotBounds + "]");
		}
		else
		{
			//slot to be removed is above the backbone
			if(slotNumber > 0)
			{
				for(int i = slotNumber; i < upperSlotNumber; i++)
				{
					//put the next higher slot style at i
					//TODO deep delete..					
					currentSlotStyle = slotStyles.get(i + 1);
					currentSlotStyle.setSlotNumber(currentSlotStyle.getSlot() - 1);
					this.slotStyles.put(i, currentSlotStyle);
				}
				
				//delete the last one
				this.slotStyles.remove(upperSlotNumber);
			}
			//slot to be removed is below the backbone
			else if(slotNumber < 0)
			{
				for(int i = slotNumber; i > lowerSlotNumber; i--)
				{
					//put the next lower slot style at i
					//TODO deep delete..
					currentSlotStyle = this.slotStyles.get(i - 1);
					currentSlotStyle.setSlotNumber(currentSlotStyle.getSlot() + 1);
					this.slotStyles.put(i, currentSlotStyle);
				}
				
				//delete the last one
				this.slotStyles.remove(lowerSlotNumber);
			}
			else
			{
				throw new IllegalArgumentException("Cannot remove the backbone (slot 0)");
			}
		}
	}
	
	/**
	 * Removes the slot style from the data style.
	 * 
	 * @param slotStyle The slot style to remove.
	 */
	public void removeSlotStyle(SlotStyle slotStyle)
	{
		removeSlotStyle(slotStyle.getSlot());
	}
	
	/**
	 * Event methods
	 */
	
	public void eventOccured(GViewEvent event)
	{
		// forward event to any listeners
		eventSubject.fireEvent(event);
	}

	public void addEventListener(GViewEventListener listener)
	{
		eventSubject.addEventListener(listener);
	}

	public void removeAllEventListeners()
	{
		eventSubject.removeAllEventListeners();
	}

	public void removeEventListener(GViewEventListener listener)
	{
		eventSubject.removeEventListener(listener);
	}

	/**
	 * Moves the source slot to the destination slot. 
	 * 
	 * The source slot will reside where the destination slot is and the destination slot,
	 * and all slots farther away from the backbone on the same side of the same side of the backbone, 
	 * will be push away from the backbone as appropriate. The source slot will be removed from its location 
	 * and the slots on the same side as the source slot will fall closer to the backbone as appropriate.
	 * 
	 * @param source The slot number of the source slot.
	 * @param destination The slot number of the destination slot.
	 */
	public void moveSlot(int source, int destination)
	{
		SlotStyle sourceSlotStyle;
		SlotStyle destinationSlotStyle;
		
		int upperSlotNumber = getUpperSlot();
		int lowerSlotNumber = getLowerSlot();		
		int upperSlotBounds = upperSlotNumber + 1;
		int lowerSlotBounds = lowerSlotNumber - 1;
		
		//note:	There is a difference between using upper & lower slot numbers and getUppperSlot() and getLowerSlot()
		//		after we remove the source slot and collapse the slots towards the backbone, getUpperSlot() and upperSlotNumber
		//		will not always be the same. sourceSlotStyle.getSlot() and destinationSlotStyle.getSlot() can be used to pull
		//		the intended slot destination location as well.
		
		SlotStyle currentStyle;
		SlotStyle previousStyle;
		
		//checking
		if (source > upperSlotBounds || source < lowerSlotBounds)
		{
			throw new IllegalArgumentException("Source slot = " + source + " out of range of [" + lowerSlotBounds + ", " + upperSlotBounds + "]");
		}
		else if (destination > upperSlotBounds || destination < lowerSlotBounds)
		{
			throw new IllegalArgumentException("Destination slot = " + destination + " out of range of [" + lowerSlotBounds + ", " + upperSlotBounds + "]");
		}		
		else if(source == 0 || destination == 0)
		{
			throw new IllegalArgumentException("The source and destination slots cannot be zero.");
		}
		
		sourceSlotStyle = this.slotStyles.get(source);
		destinationSlotStyle = this.slotStyles.get(destination);
		
		//removing the source
		this.slotStyles.remove(source);

		//collapsing the slots closer to the backbone, filling up the gap
		if(source > 0 && source < getUpperSlot())
		{
			for(int i = source; i < getUpperSlot(); i++)
			{
				this.slotStyles.put(i, this.slotStyles.get(i + 1));
				this.slotStyles.get(i).setSlotNumber(i);
			}
			
			this.slotStyles.remove(upperSlotNumber);	//part of the collapse; needed given the nature of how the collapse works
		}
		else if(source < 0 && source > getLowerSlot())
		{
			for(int i = source; i > getLowerSlot(); i--)
			{
				this.slotStyles.put(i, this.slotStyles.get(i - 1));
				this.slotStyles.get(i).setSlotNumber(i);
			}
			
			this.slotStyles.remove(lowerSlotNumber); //part of the collapse; needed given the nature of how the collapse works
		}
		else
		{
			//the situation where we removed one of the 'outer' slots
			//so no need to do any collapsing
		}
		
		//inserting
		if(destinationSlotStyle == null)
			//then we're putting it at either end. there shouldn't be a null in the middle!
		{
			if(destination > 0)
			{
				sourceSlotStyle.setSlotNumber(getUpperSlot() + 1);	//do before.. getUpperSlot() will change otherwise
				this.slotStyles.put(getUpperSlot() + 1, sourceSlotStyle);
			}
			else if(destination < 0)
			{
				sourceSlotStyle.setSlotNumber(getLowerSlot() - 1);	//do before.. getLowerSlot() will change otherwise
				this.slotStyles.put(getLowerSlot() - 1, sourceSlotStyle);
			}
		}
		else if(destinationSlotStyle.getSlot() > 0)
		{
			upperSlotNumber = getUpperSlot();
			
			previousStyle = sourceSlotStyle;
			currentStyle = destinationSlotStyle;
			
			for(int i = destinationSlotStyle.getSlot(); i <= upperSlotNumber + 1; i++)
			{
				this.slotStyles.put(i, previousStyle);
				previousStyle.setSlotNumber(i);
				
				previousStyle = currentStyle;
				currentStyle = this.slotStyles.get(i + 1);
			}
		}
		else if(destinationSlotStyle.getSlot() < 0)
		{
			lowerSlotNumber = getLowerSlot();
			
			previousStyle = sourceSlotStyle;
			currentStyle = destinationSlotStyle;
			
			for(int i = destinationSlotStyle.getSlot(); i >= lowerSlotNumber - 1; i--)
			{
				this.slotStyles.put(i, previousStyle);
				previousStyle.setSlotNumber(i);
				
				previousStyle = currentStyle;
				currentStyle = this.slotStyles.get(i - 1);
			}
		}
		else
		{
			throw new IllegalArgumentException("The destination of the slot move cannot be slot zero.");
		}
	}

	/**
	 * Moves the backbone to the destination slot.
	 * 
	 * If a slot currently resides in the destination slot, then the backbone will push that slot 'away' from where
	 * the backbone originally was and push all other slots farther away 
	 * in the same manner. The other side of the backbone will collapse appropriately inward towards the new backbone
	 * location.
	 * 
	 * Example:
	 * 
	 * Numbers denote the slot number and letters denote their respective slot styles.
	 * 
	 * Slots: (-2[a], -1[b], 0[backbone], 1[c], 2[d], 3[e])
	 * Destination: 2
	 * Result: (-3[a], -2[b], -1[c], 0[backbone], 1[d], 2[e])
	 * 
	 * Note: A destination of -1, 0 or 1 will not do anything, given the implementation.
	 * 
	 * @param destination The new slot number location for the backbone.
	 */
	public void moveBackbone(int destination)
	{
		Iterator<SlotStyle> slotsIterator = this.slots();
		ArrayList<SlotStyle> slots = new ArrayList<SlotStyle>();
		SlotStyle currentStyle;
		
		while(slotsIterator.hasNext())
		{
			slots.add(slotsIterator.next());
		}
		
		this.slotStyles.clear();
		
		if(destination > 1)
		{
			for(int i = 0; i < slots.size(); i++)
			{
				currentStyle = slots.get(i);
				
				if(currentStyle.getSlot() >= destination && currentStyle.getSlot() != 0)
				{
					currentStyle.setSlotNumber(currentStyle.getSlot() - destination + 1);
					this.slotStyles.put(currentStyle.getSlot(), currentStyle);
				}
				else if (currentStyle.getSlot() < destination && currentStyle.getSlot() > 0)
				{
					currentStyle.setSlotNumber(currentStyle.getSlot() - destination);
					this.slotStyles.put(currentStyle.getSlot(), currentStyle);
				}
				else if (currentStyle.getSlot() < destination && currentStyle.getSlot() < 0)
				{
					currentStyle.setSlotNumber(currentStyle.getSlot() - destination + 1);
					this.slotStyles.put(currentStyle.getSlot(), currentStyle);
				}
				else
				{
					throw new IllegalArgumentException("Cannot have a slot with slot number 0.");
				}
			}
		}
		else if(destination < -1)
		{
			for(int i = 0; i < slots.size(); i++)
			{
				currentStyle = slots.get(i);
				
				if(currentStyle.getSlot() <= destination && currentStyle.getSlot() != 0)
				{
					currentStyle.setSlotNumber(currentStyle.getSlot() - destination - 1);
					this.slotStyles.put(currentStyle.getSlot(), currentStyle);
				}
				else if (currentStyle.getSlot() > destination && currentStyle.getSlot() < 0)
				{
					currentStyle.setSlotNumber(currentStyle.getSlot() - destination);
					this.slotStyles.put(currentStyle.getSlot(), currentStyle);
				}
				else if (currentStyle.getSlot() > destination && currentStyle.getSlot() > 0)
				{
					currentStyle.setSlotNumber(currentStyle.getSlot() - destination - 1);
					this.slotStyles.put(currentStyle.getSlot(), currentStyle);
				}
				else
				{
					throw new IllegalArgumentException("Cannot have a slot with slot number 0.");
				}
			}
		}
		else
		{
			//do nothing.. already done
		}
	}
}
