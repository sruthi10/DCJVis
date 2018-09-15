package ca.corefacility.gview.managers.ruler;


import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Dimension2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ca.corefacility.gview.data.Slot;
import ca.corefacility.gview.layout.prototype.PinnableShape;
import ca.corefacility.gview.layout.prototype.SequencePoint;
import ca.corefacility.gview.layout.prototype.SequencePointImp;
import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.layout.sequence.BaseRange;
import ca.corefacility.gview.layout.sequence.LocationConverter;
import ca.corefacility.gview.layout.sequence.SequenceRectangle;
import ca.corefacility.gview.layout.sequence.SlotRegion;
import ca.corefacility.gview.layout.sequence.SlotTranslator;
import ca.corefacility.gview.layout.sequence.circular.BackboneCircular.ClashShiftException;
import ca.corefacility.gview.managers.SectionID;
import ca.corefacility.gview.managers.SectionManager;
import ca.corefacility.gview.map.event.BackboneZoomEvent;
import ca.corefacility.gview.map.event.GViewEvent;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.event.LayoutChangedEvent;
import ca.corefacility.gview.map.event.ResolutionSwitchEvent;
import ca.corefacility.gview.map.event.SectionChangedEvent;
import ca.corefacility.gview.map.items.BackboneTextItem;
import ca.corefacility.gview.map.items.BackboneTextItemImp;
import ca.corefacility.gview.map.items.BasicItem;
import ca.corefacility.gview.map.items.Layer;
import ca.corefacility.gview.map.items.MapComponent;
import ca.corefacility.gview.map.items.MapItem;
import ca.corefacility.gview.map.items.ShapeItem;
import ca.corefacility.gview.style.items.LabelLocation;
import ca.corefacility.gview.style.items.RulerStyle;
import ca.corefacility.gview.utils.Dimension2DDouble;
import edu.umd.cs.piccolo.nodes.PText;

// TODO we probably may even want to adjust slot sizes based upon scale so that slots of things will be smaller
public class RulerManager implements GViewEventListener
{
	// need to handle updating sequenceLength
	@SuppressWarnings("unused")
	private LocationConverter locationConverter;
	
	private SlotRegion slotRegion;
	private RulerStyle rulerStyle;
	
	private MapComponent marksLayer;
	
	private MapComponent markSubLayer;
	
	private TickMarkCalculator marksCalculator;
	
	private Set<SectionID> sectionsCovered;
	
	private double desiredLengthPerMark = 10;
	
	private BaseRange previouslyDisplayedRange;
	
	private SectionManager sectionManager = null;
	
	private boolean displayedState;
	private boolean newZoomEvent;
	
	private BackboneZoomEvent previousZoomEvent;
	
	// should listen into style change events
	// should listen to events from LocationConverter?  About genomeData changed?  Probably not as important
	public RulerManager(RulerStyle rulerStyle, LocationConverter locationConverter)
	{
		assert (rulerStyle != null);
		assert (locationConverter != null);
		
		this.slotRegion = null;
		this.rulerStyle = rulerStyle;
		this.locationConverter = locationConverter;
		
		marksLayer = new Layer();
		
		// major and minor tick mark lengths, as proportion of ruler slot
		float majorTickProportion, minorTickProportion;
		
		// determine which length (major or minor) is longer, and set tick mark proportions accordingly
		if (rulerStyle.getMajorTickLength() > rulerStyle.getMinorTickLength())
		{
			// major tick length was longest, so slot containing tick marks is same thickness as major tick length
			majorTickProportion = 1.0f;
			minorTickProportion = (float)(rulerStyle.getMinorTickLength()/rulerStyle.getMajorTickLength());
		}
		else
		{
			// minor tick length was longest, so slot containing tick marks is same thickness as minor tick length
			minorTickProportion = 1.0f;
			majorTickProportion = (float)(rulerStyle.getMajorTickLength()/rulerStyle.getMinorTickLength());
		}
		
		marksCalculator = new TickMarkCalculator(locationConverter.getSequenceLength(), rulerStyle.getTickDensity(),
				desiredLengthPerMark, majorTickProportion, minorTickProportion);
		
		sectionsCovered = new HashSet<SectionID>();
		
		displayedState = true;
		
		previousZoomEvent = null;
		newZoomEvent = false;
	}
	
	public MapComponent getRulerLayer()
	{
		return marksLayer;
	}
	
	private void buildMarksInSection(double initialBase, double finalBase, boolean last, LabelLocation labelLocation, MapComponent layer)
	{	
		List<TickMark> tickMarks = marksCalculator.createMarks(initialBase, finalBase);
		
		buildMarks(Slot.UPPER_RULER, layer, tickMarks.iterator(), labelLocation.buildAbove(), last);
		buildMarks(Slot.LOWER_RULER, layer, tickMarks.iterator(), labelLocation.buildBelow(), last);
	}
	
	// builds the tickmarks, and places them on the passed layer
	// label is used to determine if we should draw labels
	private void buildMarks(int slot, MapComponent layer, Iterator<TickMark> tick, boolean createLabels, boolean last)
	{
		if (tick != null)
		{			
			Paint tickPaint;
			
			MapComponent lastLabeledTickMark = null;
			BackboneTextItem lastLabel = null;
			
			while (tick.hasNext())
			{
				TickMark currentMark = tick.next();
				
				if (currentMark.getType() == TickMark.Type.LONG)
				{
					tickPaint = rulerStyle.getMajorTickPaint();
				}
				else
				{
					tickPaint = rulerStyle.getMinorTickPaint();
				}
				
				MapComponent tickMark = new Layer(); // stores mark and label
				
				MapItem mark = createMarkItem(currentMark, slot, tickPaint);
				
				tickMark.add(mark);
				
				if (createLabels && currentMark.getType() == TickMark.Type.LONG)
				{
					BackboneTextItem label = createTextItem(currentMark.getBase(), currentMark.getLabel(), slot);
					
					lastLabeledTickMark = tickMark;
					lastLabel = label;
					
					tickMark.add(label);
				}
				
				layer.add(tickMark);
			}
			
			if (last && lastLabeledTickMark != null && lastLabel != null) // very last tick mark
			{
				Rectangle2D rect = lastLabel.getTextBounds();
				SequenceRectangle spannedRect = slotRegion.getBackbone().getSpannedRectangle(rect);
				
				// remove if base range overlaps 0 base
				if (spannedRect.getStartBase() >= spannedRect.getEndBase())
				{
					lastLabeledTickMark.remove(lastLabel);
				}
			}
		}
	}
	
	private void deleteMarks()
	{
		sectionsCovered.clear();
		marksLayer.clear();
		
		markSubLayer = new Layer();
		
		if (displayedState)
		{
			marksLayer.add(markSubLayer);
		}
	}
	
	private void resetMarks()
	{
		deleteMarks();
		
		if (sectionManager != null)
		{
			updateSections(sectionManager, sectionManager.toSectionRange(previouslyDisplayedRange));
		}
	}
	
	// length is a proportion of the slotLength this mark is in (0 to 1)
	// TODO, currently we just draw the tick shape, and create a pinnable shape from it, possibly re-design this piece of code
	private MapItem createMarkItem(TickMark mark, int slot, Paint tickPaint)
	{
		SlotTranslator slots = slotRegion.getSlotTranslator();
		Backbone backbone = slotRegion.getBackbone();
		
		float thickness = (float)rulerStyle.getTickThickness();
		float lengthProportion = mark.getlengthProportion();
		
		// length relative to distance between TOP and BOTTOM
		// TODO either adjust this or adjust slot thicknesses by scale
		float relativeLength = (float)(SlotTranslator.TOP - SlotTranslator.BOTTOM)*lengthProportion;
		
		// heights in the slot of the top/bottom
		double topSlotHeight;
		double bottomSlotHeight;
		
		// calculates the top/bottom of this tick mark in the slot
		if (slot == Slot.UPPER_RULER)
		{
			topSlotHeight = SlotTranslator.BOTTOM + relativeLength;
			bottomSlotHeight = SlotTranslator.BOTTOM;
		}
		else
		{
			topSlotHeight = SlotTranslator.TOP;
			bottomSlotHeight = SlotTranslator.TOP - relativeLength;
		}
		
		// actual top/bottom of tick mark shape
		double top = slots.getHeightFromBackbone(slot, topSlotHeight);
		double bottom = slots.getHeightFromBackbone(slot, bottomSlotHeight);
		
		BasicItem markItem = new BasicItem(mark.getLabel());
		
		// creates outline
		Path2D path = new Path2D.Double();
		path.moveTo(0, top);
		path.lineTo(0, bottom);
		
		// adds thickness to tick mark
		BasicStroke b = new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
		Shape shape = b.createStrokedShape(path);
		
		// define the center of the shape to pin it to center
		double center = (top+bottom)/2;
		// creates pinnable shape, so that tick marks will move around on zoom
		PinnableShape tickShape = new PinnableShape(backbone, shape,
											new SequencePointImp(mark.getBase(), center));
//		backbone.addEventListener(tickShape);
		
		markItem.setShape(tickShape);
		// not sure where to set colors of markItem for now
		markItem.setPaint(tickPaint);
		markItem.setShapeEffectRenderer(rulerStyle.getShapeEffectRenderer());
//		markItem.setStroke(null);
		
		return markItem;
	}
	
	private void updateSections(SectionManager sectionManager, List<SectionID> currentlyViewedSections)
	{
		// keep track of these so we can set a BaseRange to be the previous display
		double lowestStartBase = Double.MAX_VALUE;
		double highestEndBase = -Double.MAX_VALUE;
		
		for (SectionID section : currentlyViewedSections)
		{
			BaseRange sectionRange = sectionManager.toBaseRange(section);
			
			lowestStartBase = Math.min(sectionRange.getStartBase(), lowestStartBase);
			highestEndBase = Math.max(sectionRange.getEndBase(), highestEndBase);
			
			if (!sectionsCovered.contains(section))
			{
				buildMarksInSection(sectionRange.getStartBase(), sectionRange.getEndBase(), section.isLast(),
						rulerStyle.getRulerLabelsLocation(), markSubLayer);
				
				// mark section as already covered
				sectionsCovered.add(section);
			}
		}
		
		// if these values changed at all
		if (lowestStartBase != Double.MAX_VALUE && highestEndBase != -Double.MAX_VALUE)
		{
			previouslyDisplayedRange = new BaseRange(lowestStartBase, highestEndBase);
		}
	}
	
	private BackboneTextItem createTextItem(int base, String label, int slot)
	{
		BackboneTextItem textItem;
		SlotTranslator trans = slotRegion.getSlotTranslator();
		
		// set inital height
		double heightFromBackbone = 0;
		if (slot == Slot.UPPER_RULER)
		{
			heightFromBackbone = trans.getTopMostHeight();
		}
		else if (slot == Slot.LOWER_RULER)
		{
			heightFromBackbone = trans.getBottomMostHeight();
		}
		
		SequencePoint pinnedPoint = new SequencePointImp(base, heightFromBackbone);
		
		textItem = new BackboneTextItemImp(slotRegion.getBackbone(), pinnedPoint);
		textItem.setTextPaint(rulerStyle.getTextPaint());
		textItem.setPaint(rulerStyle.getTextBackgroundPaint());
		textItem.setFont(rulerStyle.getFont());
		textItem.setText(label);
		
		// try to adjust height from backbone properly, maybe seperate this into another method?
		Rectangle2D bounds = textItem.getTextBounds();
		double heightAdjust = 0;
		try
		{
			if (slot == Slot.UPPER_RULER)
			{
				heightAdjust = slotRegion.getBackbone().calculateClashShift(pinnedPoint, trans.getTopMostHeight(), bounds.getWidth(), bounds.getHeight(), Backbone.ShiftDirection.ABOVE)
				+ 2;
			}
			else if (slot == Slot.LOWER_RULER)
			{
				heightAdjust = slotRegion.getBackbone().calculateClashShift(pinnedPoint, trans.getBottomMostHeight(), bounds.getWidth(), bounds.getHeight(), Backbone.ShiftDirection.BELOW)
				-2;
			}
		}
		catch (ClashShiftException e)
		{
//			System.err.println(e);
		}
		
//		System.out.println("label=" + label + ",slot=" + slot + ",initialHeight=" + pinnedPoint.getHeightFromBackbone() + ",heightAdjust=" + heightAdjust);
		pinnedPoint.setHeightFromBackbone(pinnedPoint.getHeightFromBackbone() + heightAdjust);
		textItem.pinTo(pinnedPoint);
		
		return textItem;
	}
	
	public void setDisplayed(boolean displayed)
	{
		if (displayed != displayedState)
		{
			displayedState = displayed;

			if (displayed)
			{
				marksLayer.add(markSubLayer);
				
				if (newZoomEvent)
				{
					eventOccured(previousZoomEvent);
					newZoomEvent = false;
				}
			}
			else
			{
				marksLayer.remove(markSubLayer);
			}			
		}
		// else, do nothing
	}

	
	// use this to re-draw tick marks at differnet scales
	public void eventOccured(GViewEvent event)
	{
		if (event instanceof BackboneZoomEvent)
		{
			if (displayedState)
			{
				BackboneZoomEvent zoomEvent = (BackboneZoomEvent)event;
				Backbone backbone = zoomEvent.getBackbone();
				if (backbone == null) {
					return;
				}
	
				Iterator<MapItem> marks = marksLayer.itemsIterator();
				
				while (marks.hasNext())
				{
					MapItem currentItem = marks.next();
					
					if (currentItem instanceof ShapeItem)
					{
						ShapeItem currentShapeItem = (ShapeItem)currentItem;
						
						currentShapeItem.eventOccured(event);
					}
					else if (currentItem instanceof BackboneTextItem)
					{
						BackboneTextItem btItem = (BackboneTextItem)currentItem;
						
						btItem.eventOccured(event);
					}
				}
			}
			else
			{
				newZoomEvent = true;
			}
			
			previousZoomEvent = (BackboneZoomEvent)event;
		}
		else if (event instanceof LayoutChangedEvent)
		{
			if (slotRegion != null)
			{
				slotRegion.getBackbone().removeEventListener(this); // remove from listening to old backbone
			}
			
			this.slotRegion = ((LayoutChangedEvent)event).getSlotRegion();
			
			slotRegion.addEventListener(this); // listen to new backbone
			
			// rebuild items
			resetMarks();
			
			marksCalculator.changeBackboneLength(slotRegion.getBackbone().getBackboneLength());
		}
		else if (event instanceof ResolutionSwitchEvent)
		{
			ResolutionSwitchEvent resolutionEvent = (ResolutionSwitchEvent)event;
			
			double baseScale = resolutionEvent.getBaseResolutionScale();
			marksCalculator.changeBackboneLength(resolutionEvent.getBackbone().getBackboneLengthAt(baseScale));
			
			resetMarks();
		}
		else if (event instanceof SectionChangedEvent)
		{
			boolean deleteMarks = false;
			
			SectionChangedEvent sectionEvent = (SectionChangedEvent)event;
			
			this.sectionManager = sectionEvent.getSectionManager();

			// check if the sections displayed have any from previous resolution
			for (SectionID id : sectionsCovered)
			{
				// if we find sections from a different level
				if (id.getSectionLevel() != sectionManager.getSectionLevel())
				{
					deleteMarks = true;
					break;
				}
			}
			
			if (deleteMarks)
			{
				deleteMarks();
			}
			
			updateSections(sectionEvent.getSectionManager(), sectionEvent.getCurrentDisplayedSections());
		}
	}

	public boolean getDisplayed()
	{
		return displayedState;
	}
	
	/**
	 * Determines the size of a constructed label with the given ruler style and text.
	 * @param rulerStyle  The RulerStyle to use.
	 * @param labelText  The text of the label.
	 * @return  The size of the constructed label as a Dimension2D object.
	 */
	public static Dimension2D determineSizeOf(RulerStyle rulerStyle, String labelText)
	{
		PText textItem = new PText(labelText);
		
		textItem.setTextPaint(rulerStyle.getTextPaint());
		textItem.setPaint(rulerStyle.getTextBackgroundPaint());
		textItem.setFont(rulerStyle.getFont());
		
		Rectangle2D bounds = textItem.getBounds();
		
		return new Dimension2DDouble(bounds.getHeight(),bounds.getWidth());
	}
	
	public void updateRuler()
	{
		// major and minor tick mark lengths, as proportion of ruler slot
		float majorTickProportion, minorTickProportion;
		
		// determine which length (major or minor) is longer, and set tick mark proportions accordingly
		if (rulerStyle.getMajorTickLength() > rulerStyle.getMinorTickLength())
		{
			// major tick length was longest, so slot containing tick marks is same thickness as major tick length
			majorTickProportion = 1.0f;
			minorTickProportion = (float)(rulerStyle.getMinorTickLength()/rulerStyle.getMajorTickLength());
		}
		else
		{
			// minor tick length was longest, so slot containing tick marks is same thickness as minor tick length
			minorTickProportion = 1.0f;
			majorTickProportion = (float)(rulerStyle.getMajorTickLength()/rulerStyle.getMinorTickLength());
		}
		
		//marksCalculator = new TickMarkCalculator(locationConverter.getSequenceLength(), rulerStyle.getTickDensity(),
		//		desiredLengthPerMark, majorTickProportion, minorTickProportion);	
		
		marksCalculator.setTickDensity(rulerStyle.getTickDensity());
		marksCalculator.setMajorLengthProportion(majorTickProportion);
		marksCalculator.setMinorLengthProportion(minorTickProportion);
		
		resetMarks();
	}
}