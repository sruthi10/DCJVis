package ca.corefacility.gview.managers.labels;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.biojava.bio.seq.Feature;

import ca.corefacility.gview.layout.sequence.SlotRegion;
import ca.corefacility.gview.map.event.BackboneZoomEvent;
import ca.corefacility.gview.map.event.GViewEvent;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.event.ResolutionSwitchEvent;
import ca.corefacility.gview.map.items.FeatureItem;
import ca.corefacility.gview.map.items.Layer;
import ca.corefacility.gview.map.items.MapComponent;
import ca.corefacility.gview.style.datastyle.LabelStyle;


public abstract class LabelsManager implements GViewEventListener
{
	protected SlotRegion slotRegion;
	
	protected List<Label> allLabels;
	
	protected LabelsPositioner labelsPositioner;

	protected MapComponent labelsLayer;
	protected MapComponent labelLinesLayer;
	protected MapComponent labelTextLayer;
	
	protected double prevScale;
	
	protected LabelSlots labelSlots;
	
	protected boolean displayedState;
	
	protected BackboneZoomEvent previousZoomEvent;
	protected ResolutionSwitchEvent previousResolutionEvent;
	protected boolean zoomEventFirst;
	
	public LabelsManager()
	{
		this.slotRegion = null;

		labelsLayer = new Layer();
		labelLinesLayer = new Layer();
		labelTextLayer = new Layer();
		
		labelsLayer.add(labelLinesLayer);
		labelsLayer.add(labelTextLayer);
		
		labelsPositioner = new LabelsPositionerImp(slotRegion, labelSlots);
		
		allLabels = new LinkedList<Label>();
		
		prevScale = 1.0;
		
		displayedState = true;
		
		previousZoomEvent = null;
		previousResolutionEvent = null;
		zoomEventFirst = false;
	}

	/**
	 * Adds a label item for the passed FeatureItem, using the passed label style.
	 * 
	 * @param style
	 *            The style appropriate for this label.
	 * @param featureItem
	 *            The item corresponding to the feature to label.
	 */
	// currently, we create text items for each label, and show/hide them at diff zoom levels
	// would it be better to re-create them from styles at diff zoom levels

	// this simply adds labels into a list, doesn't determine position yet
	public void buildLabel(LabelStyle style, FeatureItem featureItem, boolean aboveBackbone)
	{
		if (style == null || featureItem == null)
		{
			return;
		}

		if (style.showLabels())
		{
			Feature feature = featureItem.getFeature();

			if (feature == null)
			{
				return;
			}

			// if there is text in this label
			String labelText = style.getLabelExtractor().extractText(feature);
			if (labelText != null && !labelText.equals(""))
			{
				Label label = new Label(style, featureItem, aboveBackbone);
				
				allLabels.add(label);
			}
		}
	}

	/**
	 * Creates the layer and stores all the labels added with addLabel into it.
	 * 
	 * @return A MapComponent containing all labels.
	 */
	public abstract MapComponent getLabelsLayer();

	public void setLabelsDisplayed(boolean displayed)
	{
		if (displayed != displayedState)
		{
			displayedState = displayed;
			
			if (displayed)
			{
				if (previousZoomEvent != null && previousResolutionEvent != null)
				{
					if (zoomEventFirst)
					{
						eventOccured(previousZoomEvent);
						eventOccured(previousResolutionEvent);
					}
					else
					{
						eventOccured(previousResolutionEvent);
						eventOccured(previousZoomEvent);
					}
				}
				else if (previousZoomEvent != null)
				{
					eventOccured(previousZoomEvent);
				}
				else if (previousResolutionEvent != null)
				{
					eventOccured(previousResolutionEvent);
				}
				
				labelsLayer.add(labelLinesLayer);
				labelsLayer.add(labelTextLayer);
			}
			else
			{
				labelsLayer.remove(labelLinesLayer);
				labelsLayer.remove(labelTextLayer);
			}
			
		}
	}

	public boolean getDisplayed()
	{
		return displayedState;
	}
	
	@Override
	public void eventOccured(GViewEvent event)
	{
		if (!displayedState)
		{
			if (event instanceof BackboneZoomEvent)
			{
				previousZoomEvent = (BackboneZoomEvent)event;
				
				if (previousResolutionEvent == null)
				{
					zoomEventFirst = true;
				}
				else
				{
					zoomEventFirst = false;
				}
			}
			else if (event instanceof ResolutionSwitchEvent)
			{
				previousResolutionEvent = (ResolutionSwitchEvent)event;
				
				if (previousZoomEvent == null)
				{
					zoomEventFirst = false;
				}
				else
				{
					zoomEventFirst = true;
				}
			}
		}
	}
	
	public void update()
	{
		Iterator<Label> labels = this.allLabels.iterator();
		Label current;
		
		while(labels.hasNext())
		{
			current = labels.next();
			
			current.update();
		}
	}
}