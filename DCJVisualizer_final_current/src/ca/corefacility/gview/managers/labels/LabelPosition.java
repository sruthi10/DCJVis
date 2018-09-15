package ca.corefacility.gview.managers.labels;

import ca.corefacility.gview.layout.prototype.SequencePoint;
import ca.corefacility.gview.layout.sequence.SlotRegion;

/**
 * Used to reposition a label depending on zoom levels/keep track of these zoom levels.
 * @author aaron
 *
 */
public class LabelPosition
{
	private Label label;
	
	private SlotRegion slotRegion; // probably wrong to store this here, but need it so Label can re-generate the line on being updated.
	
	private SequencePoint labelPosition;
	
	private SequencePoint prevPosition = null;
	private boolean prevLabelOff = false; // if the previous position of the label was "off"
	
	private boolean prevPositionSet = false; // if prev position is set or not (so we can go back)
	
	public LabelPosition(SequencePoint labelPosition, Label label)
	{
		this.labelPosition = labelPosition;
		this.label = label;
	}

	public Label getLabel()
	{
		return label;
	}
	
	public void setSlotRegion(SlotRegion slotRegion)
	{
		this.slotRegion = slotRegion;
	}

	public void changeLabelTo()
	{
		/**/
		if (label.getLabelTextItem().isSelected())
		{
			System.out.println();
		}
		/**/
		
		prevPosition = label.getPinnedPoint();
		prevLabelOff = !label.isDisplayed();
		prevPositionSet = true;
		
		label.updatePosition(labelPosition, slotRegion.getSequencePath());
		
		if (prevLabelOff)
		{
			label.turnOnLabel();
		}
	}

	public void undoChangeLabelTo()
	{
		if (prevPositionSet)
		{
			/**/
			if (label.getLabelTextItem().isSelected())
			{
				System.out.println();
			}
			/**/
			
			label.updatePosition(prevPosition, slotRegion.getSequencePath());
			
			if (prevLabelOff)
			{
				label.turnOffLabel();
			}
			
			prevPosition = null;
			
			prevLabelOff = false;
			prevPositionSet = false;
		}
	}
}
