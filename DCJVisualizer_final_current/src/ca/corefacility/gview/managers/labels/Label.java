package ca.corefacility.gview.managers.labels;


import java.awt.BasicStroke;
import java.awt.Stroke;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.util.Comparator;

import org.biojava.bio.seq.Feature;
import org.biojava.bio.symbol.Location;

import ca.corefacility.gview.layout.prototype.BackboneShape;
import ca.corefacility.gview.layout.prototype.SequencePoint;
import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.layout.sequence.SequencePath;
import ca.corefacility.gview.map.effects.LabelLineRenderer;
import ca.corefacility.gview.map.event.BackboneZoomEvent;
import ca.corefacility.gview.map.event.GViewEvent;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.items.BackboneLabelItem;
import ca.corefacility.gview.map.items.FeatureItem;
import ca.corefacility.gview.map.items.LabelLineItem;
import ca.corefacility.gview.map.items.Layer;
import ca.corefacility.gview.map.items.MapComponent;
import ca.corefacility.gview.map.items.ShapeItem;
import ca.corefacility.gview.style.datastyle.LabelStyle;

public class Label implements GViewEventListener
{
	protected BackboneLabelItem labelTextItem;
	private FeatureItem labeledItem;
	private ShapeItem line;
	private BackboneShape lineShape;

	private MapComponent labelTextLayer;
	private MapComponent labelLineLayer;

	private SequencePoint lineStart;

	private LabelStyle labelStyle;
	
	private boolean aboveBackbone;
	
	// should these values be stored someplace else?
	private double zoomToDisplay; // zoom level to turn this label "on"
	private float priority;
	
	private boolean isDisplayed = true;

	public Label(LabelStyle labelStyle, FeatureItem labeledItem, boolean aboveBackbone)
	{
		this.labelTextLayer = new Layer();
		this.labelLineLayer = new Layer();

		this.labeledItem = labeledItem;
		this.labelStyle = labelStyle;
		this.labelTextItem = buildLabel(labelStyle, labeledItem);

		line = new LabelLineItem(labelTextItem);
		line.setShapeEffectRenderer(new LabelLineRenderer());
		
		// make sure auto paint works for label lines
		if (labelStyle.isAutoLabelLinePaint())
		{
			line.setPaint(labelStyle.getTextPaint());
		}
		else
		{
			line.setPaint(labelStyle.getLabelLinePaint());
		}

		turnOnLabel();
		//labelItem.add(labelTextItem);
		//labelItem.add(line);
		
		zoomToDisplay = 0.0;
		
		// should I have these values in here?
		FeatureLabelRanker ranker = labelStyle.getLabelRanker();
		LabelRank rank = ranker.rankFeature(labeledItem.getFeature());
		priority = calculatePriority(rank);
		
		this.aboveBackbone = aboveBackbone;
	}

	private BackboneLabelItem buildLabel(LabelStyle style, FeatureItem labeledItem)
	{
		BackboneLabelItem labelItem = new BackboneLabelItem(null, labeledItem);
		String labelText = style.getLabelExtractor().extractText(labeledItem.getFeature());

		labelItem.setFont(style.getFont());
		labelItem.setTextPaint(style.getTextPaint());
		labelItem.setPaint(style.getBackgroundPaint());
		
		if (labelText != null && !labelText.equals(""))
		{
		    labelItem.setText(labelText);
		}

		return labelItem;
	}
	
	private float calculatePriority(LabelRank rank)
	{
		float priority = 0.0f;
		float essentialBase = 1;
		
		Rectangle2D bounds = this.getBounds();
		float area = (float)(bounds.getHeight()*bounds.getWidth());
		
		if (rank.equals(LabelRank.HIDDEN))
		{
			priority = -1;
		}
		else if (rank.equals(LabelRank.NON_ESSENTIAL)) // value from 0 to 1, with 1 being highest
		{
			priority = essentialBase/(essentialBase + area);
		}
		else // higher priority
		{
			priority = essentialBase + essentialBase/(essentialBase + area); // value from 1 to 2, 2 highest
		}
		
		return priority;
	}
	
	public boolean isAboveBackbone()
	{
		return aboveBackbone;
	}

	public float getPriority()
	{
		return priority;
	}

	public MapComponent getLabelTextLayer()
	{
		return labelTextLayer;
	}
	
	public MapComponent getLabelLineLayer()
	{
		return labelLineLayer;
	}
	
	public void turnOffLabel()
	{
		labelTextLayer.clear();
		labelLineLayer.clear();
		
		isDisplayed = false;
	}
	
	public void turnOnLabel()
	{
		labelTextLayer.add(labelTextItem);
		labelLineLayer.add(line);
		
		isDisplayed = true;
	}

	public BackboneLabelItem getLabelTextItem()
	{
		return labelTextItem;
	}

	public Rectangle2D getBounds()
	{
		return labelTextItem.getGlobalBounds();
	}

	public ShapeItem getLine()
	{
		return line;
	}

	public LabelStyle getLabelStyle()
	{
		return labelStyle;
	}

	public Feature getLabeledFeature()
	{
		return getLabelTextItem().getLabeledFeature().getFeature();
	}

	public SequencePoint getPinnedPoint()
	{
		return labelTextItem.getPinnedPoint();
	}
	
	/**
	 * Updates this label to be pinned to the passed point.
	 * @param labelPinnedPoint  The point this label should be pinned to.
	 * @param sequencePath 
	 */
	public void updatePosition(SequencePoint labelPinnedPoint, SequencePath sequencePath)
	{
		labelTextItem.pinTo(labelPinnedPoint);

		lineShape = buildLine(lineStart, labelPinnedPoint, sequencePath);
		line.setShape(lineShape);
	}

	public double getZoomToDisplay()
	{
		return zoomToDisplay;
	}

	public void setZoomToDisplay(double zoomToDisplay)
	{
		this.zoomToDisplay = zoomToDisplay;
	}
	
	/**
	 * Determines whether or not we should display this label, given the passed zoom level.
	 * @param zoomScale
	 * @return  True if we should display this label, false otherwise.
	 */
	public boolean isDisplayedAt(double zoomScale)
	{
		return zoomScale >= zoomToDisplay;
	}

	public void setLabelPositions(SequencePoint lineStart, SequencePoint labelStart, SequencePath sequencePath)
	{
		this.lineStart = lineStart;

		labelTextItem.pinTo(labelStart);

		lineShape = buildLine(lineStart, labelStart, sequencePath);
		line.setShape(lineShape);
	}

	public void translate(double base, double heightFromBackbone, SequencePath sequencePath)
	{
		labelTextItem.translate(base, heightFromBackbone);

		lineShape = buildLine(lineStart, labelTextItem.getPinnedPoint(), sequencePath);
		line.setShape(lineShape);
	}

	private BackboneShape buildLine(SequencePoint lineStart, SequencePoint lineEnd, SequencePath sequencePath)
	{
		Stroke s = new BasicStroke(1.0f);
		
		sequencePath.clear();
		sequencePath.moveTo(lineStart.getBase(), lineStart.getHeightFromBackbone());
		sequencePath.realLineTo(lineEnd.getBase(), lineEnd.getHeightFromBackbone(), 0);

		return sequencePath.createStrokedShape(s);
	}

	public void eventOccured(GViewEvent event)
	{
		if (event instanceof BackboneZoomEvent)
		{
			// only update position, etc if displayed
			if (isDisplayed())
			{
				labelTextItem.eventOccured(event);
				line.eventOccured(event);
			}
		}
	}
	
	public void updateBackbone(Backbone backbone)
	{
		// instruct each label to update it's own position
		labelTextItem.setBackbone(backbone);
	}

	public static class BaseComparator implements Comparator
	{
		public int compare(Object o1, Object o2)
		{
			if (o1 instanceof Label && o2 instanceof Label)
			{
				Label l1 = (Label) o1;
				Label l2 = (Label) o2;

				// compares based upon centre of locations
				// TODO should I change how to compare here (not by centre).
				Location location1 = l1.labelTextItem.getLabeledFeature().getFeature().getLocation();
				Location location2 = l2.labelTextItem.getLabeledFeature().getFeature().getLocation();
				double center1 = (location1.getMin() + location1.getMax()) / 2.0;
				double center2 = (location2.getMin() + location2.getMax()) / 2.0;

				if (center1 < center2)
				{
					return -1;
				}
				else if (Double.valueOf(center1).equals(Double.valueOf(center2)))
				{
					return 0;
				}
				else
				{
					return 1;
				}
			}
			else
			{
				throw new IllegalArgumentException("arguments not of type Label");
			}
		}
	}

	public static class HeightComparator implements Comparator
	{
		public int compare(Object o1, Object o2)
		{
			if (o1 instanceof Label && o2 instanceof Label)
			{
				Label l1 = (Label) o1;
				Label l2 = (Label) o2;

				// compares based upon centre of locations
				// TODO should I change how to compare here (not by centre).
				double height1 = l1.labelTextItem.getPinnedPoint().getHeightFromBackbone();
				double height2 = l2.labelTextItem.getPinnedPoint().getHeightFromBackbone();

				if (height1 < height2)
				{
					return -1;
				}
				else if (Double.valueOf(height1).equals(Double.valueOf(height2)))
				{
					return 0;
				}
				else
				{
					return 1;
				}
			}
			else
			{
				throw new IllegalArgumentException("arguments not of type Label");
			}
		}
	}
	
	public static class ZoomScaleComparatorAscending implements Comparator<Label>
	{
		@Override
		public int compare(Label o1, Label o2)
		{
			if (o1.getZoomToDisplay() < o2.getZoomToDisplay())
			{
				return -1;
			}
			else if (Double.valueOf(o1.getZoomToDisplay()).equals(Double.valueOf(o2.getZoomToDisplay())))
			{
				return 0;
			}
			else
			{
				return 1;
			}
		}
	}
	
	public static class ZoomScaleComparatorDescending implements Comparator<Label>
	{
		@Override
		public int compare(Label o1, Label o2)
		{
			if (o2.getZoomToDisplay() < o1.getZoomToDisplay())
			{
				return -1;
			}
			else if (Double.valueOf(o2.getZoomToDisplay()).equals(Double.valueOf(o1.getZoomToDisplay())))
			{
				return 0;
			}
			else
			{
				return 1;
			}
		}
	}
	
	public static class PriorityComparatorDescending implements Comparator<Label>
	{
		@Override
		public int compare(Label o1, Label o2)
		{
			if (o2.getPriority() < o1.getPriority())
			{
				return -1;
			}
			else if (Double.valueOf(o2.getPriority()).equals(Double.valueOf(o1.getPriority())))
			{
				return 0;
			}
			else
			{
				return 1;
			}
		}
	}

	public boolean isDisplayed()
	{
		return isDisplayed;
	}

	public Dimension2D getDimension()
	{		
		return labelTextItem.getDimension();
	}
	
	public void update()
	{
		String labelText = this.labelStyle.getLabelExtractor().extractText(this.labeledItem.getFeature());

		this.labelTextItem.setFont(this.labelStyle.getFont());
		this.labelTextItem.setTextPaint(this.labelStyle.getTextPaint());
		this.labelTextItem.setPaint(this.labelStyle.getBackgroundPaint());
		this.line.setPaint(this.labelStyle.getTextPaint());
		
		if (labelText != null && !labelText.equals(""))
		{
			this.labelTextItem.setText(labelText);
		}
		
		if(this.labelStyle.showLabels())
		{
			this.labelTextItem.setVisible(true);
			this.line.setVisible(true);
		}
		else
		{
			this.labelTextItem.setVisible(false);
			this.line.setVisible(false);
		}
		
		this.labelTextItem.invalidateFullBounds();
		this.labelTextItem.invalidateLayout();
		this.labelTextItem.invalidatePaint();
	}
}
