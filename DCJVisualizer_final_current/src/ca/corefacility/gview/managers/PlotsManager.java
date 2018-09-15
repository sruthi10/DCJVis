package ca.corefacility.gview.managers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.biojava.bio.symbol.RangeLocation;

import ca.corefacility.gview.data.GenomeData;
import ca.corefacility.gview.layout.Direction;
import ca.corefacility.gview.layout.plot.PlotBuilder;
import ca.corefacility.gview.layout.sequence.SlotPath;
import ca.corefacility.gview.layout.sequence.SlotRegion;
import ca.corefacility.gview.map.event.BackboneZoomEvent;
import ca.corefacility.gview.map.event.GViewEvent;
import ca.corefacility.gview.map.event.GViewEventListener;
import ca.corefacility.gview.map.event.LayoutChangedEvent;
import ca.corefacility.gview.map.items.BasicItem;
import ca.corefacility.gview.map.items.Layer;
import ca.corefacility.gview.map.items.MapComponent;
import ca.corefacility.gview.map.items.MapItem;
import ca.corefacility.gview.map.items.PlotItem;
import ca.corefacility.gview.map.items.PlotLayer;
import ca.corefacility.gview.map.items.ShapeItem;
import ca.corefacility.gview.style.datastyle.DataStyle;
import ca.corefacility.gview.style.datastyle.PlotBuilderType;
import ca.corefacility.gview.style.datastyle.PlotDrawerType;
import ca.corefacility.gview.style.datastyle.PlotStyle;
import ca.corefacility.gview.style.datastyle.SlotItemStyle;
import ca.corefacility.gview.style.datastyle.SlotStyle;
import ca.corefacility.gview.utils.ProgressHandler;

public class PlotsManager implements GViewEventListener
{
	private MapComponent plotsLayer;

	private DataStyle dataStyle;
	private GenomeData genomeData;

	private final HashMap<Integer, ArrayList<PlotItem>[]> plotItemMappings = new HashMap<Integer, ArrayList<PlotItem>[]>();
	private final HashMap<Integer, BasicItem> gridLineMappings = new HashMap<Integer, BasicItem>();

	public PlotsManager(DataStyle dataStyle, GenomeData genomeData)
	{
		this.plotsLayer = new Layer();

		this.dataStyle = dataStyle;
		this.genomeData = genomeData;
	}

	public MapComponent getPlotsLayer()
	{
		return this.plotsLayer;
	}

	private void buildPlots(MapComponent layer, SlotRegion slotRegion)
	{
		this.plotItemMappings.clear();
		this.gridLineMappings.clear();

		Iterator<SlotStyle> slots = this.dataStyle.slots();

		ProgressHandler.start(ProgressHandler.Stage.CREATING_PLOTS);
		while (slots.hasNext())
		{
			SlotStyle currentSlot = slots.next();

			MapComponent slot = createSlot(currentSlot, slotRegion);

			if (slot != null)
			{
				this.plotsLayer.add(slot);
			}
		}
		ProgressHandler.finish(ProgressHandler.Stage.CREATING_PLOTS);
	}

	private MapComponent createSlot(SlotStyle currentSlot, SlotRegion slotRegion)
	{
		MapComponent layer = null;

		Iterator<SlotItemStyle> items = currentSlot.styles();

		while (items.hasNext())
		{
			SlotItemStyle currItemStyle = items.next();

			if (currItemStyle.getClass().equals(PlotStyle.class))
			{
				PlotStyle currPlot = (PlotStyle) currItemStyle;

				if (layer == null)
				{
					layer = new PlotLayer(currPlot);
				}

				try
				{
					if (currPlot.getPlotBuilderType() != null)
					{
						PlotBuilderType plotBuilderType = currPlot.getPlotBuilderType();
						PlotDrawerType plotDrawerType = currPlot.getPlotDrawerType();
						PlotBuilder plotBuilder = plotBuilderType.createPlotBuilder();
						SlotPath slotPath = slotRegion.getSlotPath(currentSlot.getSlot());

						Shape[][] shapeGroups = plotBuilder.createPlot(this.genomeData, slotPath,
								plotDrawerType.createPlotDrawer());

						// extend plotPaint parameters if there are too few
						Paint[] plotPaint = currPlot.getPaint();
						if (plotPaint == null || plotPaint.length == 0)
						{
							System.err.println("[warning] - plotPaint is not set for plot " + currPlot
									+ " setting default paint");
							plotPaint = new Paint[shapeGroups.length];
							for (int i = 0; i < plotPaint.length; i++)
							{
								plotPaint[i] = Color.blue;
							}
							currPlot.setPaint(plotPaint);
						}
						else if (plotPaint.length < shapeGroups.length)
						{
							System.err.println("[warning] - not enough paint parameters set for plot (paint.length="
									+ plotPaint.length + ",shapeGroups.length=" + shapeGroups.length
									+ ") adding extra paint parameters.");

							Paint[] newPlotPaint = new Paint[shapeGroups.length];

							for (int i = 0; i < plotPaint.length; i++)
							{
								newPlotPaint[i] = plotPaint[i];
							}

							for (int i = plotPaint.length; i < newPlotPaint.length; i++)
							{
								newPlotPaint[i] = plotPaint[plotPaint.length - 1];
							}

							plotPaint = newPlotPaint;
							currPlot.setPaint(plotPaint);
						}

						@SuppressWarnings("unchecked")
						// java wont let this work the good way
						ArrayList<PlotItem>[] plotArrays = (ArrayList<PlotItem>[]) new ArrayList[shapeGroups.length];
						for (int i = 0; i < plotArrays.length; i++)
						{
							plotArrays[i] = new ArrayList<PlotItem>();
						}

						this.plotItemMappings.put(currPlot.getID(), plotArrays);

						boolean grid = false;
						for (int i = 0; i < shapeGroups.length; i++)
						{
							Shape[] shapes = shapeGroups[i];
							for (Shape s : shapes)
							{
								PlotItem plot = new PlotItem(currPlot, new RangeLocation(0,
										this.genomeData.getSequenceLength()));
								plotArrays[i].add(plot);

								plot.setPaint(plotPaint[i]);
								plot.setShape(s);

								// if there are grid lines to display
								if (!grid)
								{
									if (currPlot.getGridLines() > 0 && currPlot.getGridPaint() != null)
									{
										BasicItem gridItem = new BasicItem();
										gridItem.setShape(buildGrid(slotRegion, currentSlot.getSlot(),
												this.genomeData.getSequenceLength(), currPlot.getGridLineThickness(),
												currPlot.getGridLines()));
										gridItem.setPaint(currPlot.getGridPaint());

										layer.add(gridItem);
										this.gridLineMappings.put(currPlot.getID(), gridItem);
										grid = true;
									}
								}

								layer.add(plot);
							}
						}
					}
				}
				catch (IllegalArgumentException e)
				{
					System.err.println("[warning] - could not draw plot due to \"" + e + "\"");
				}
			}
		}

		return layer;
	}

	/**
	 * Builds a grid for the plot
	 * 
	 * @param slotRegion
	 * @param slot
	 *            The slot the grid should go in.
	 * @param backboneParallelLines
	 *            The number of lines parallel to the backbone
	 * 
	 * @return  A Shape defining the grid for a plot.
	 */
	private Shape buildGrid(SlotRegion slotRegion, int slot, int sequenceLength, float thickness,
			int backboneParallelLines)
	{
		SlotPath slotPath = slotRegion.getSlotPath(slot);

		if (backboneParallelLines == 1) // if one line, draw only center line
		{
			slotPath.moveTo(0, 0);
			slotPath.lineTo(sequenceLength, Direction.INCREASING);
		}
		else
		// draw top, bottom, divide rest of lines among internal
		{
			float regionThickness = 2.0f;

			// draw top
			slotPath.moveTo(0, 1.0f);
			slotPath.lineTo(sequenceLength, Direction.INCREASING);

			// draw bottom
			slotPath.moveTo(0, -1.0f);
			slotPath.lineTo(sequenceLength, Direction.INCREASING);

			if (backboneParallelLines > 2)
			{
				// divide into a number of regions equal to number of lines -1
				float gridLineSpacing = regionThickness / (backboneParallelLines - 1);

				// draw other lines
				for (float currLineHeight = -1.0f + gridLineSpacing; currLineHeight < 1.0f; currLineHeight += gridLineSpacing)
				{
					slotPath.moveTo(0, currLineHeight);
					slotPath.lineTo(sequenceLength, Direction.INCREASING);
				}
			}
		}

		return slotPath.createStrokedShape(new BasicStroke(thickness));
	}

	@Override
	public void eventOccured(GViewEvent event)
	{
		if (event instanceof BackboneZoomEvent)
		{
			// zoom all features in layer, don't add each feature as a listener
			// to Backbone
			Iterator<MapItem> items = this.plotsLayer.itemsIterator();

			while (items.hasNext())
			{
				MapItem currItem = items.next();

				if (currItem instanceof ShapeItem)
				{
					ShapeItem shapeItem = (ShapeItem) currItem;

					shapeItem.eventOccured(event);
				}
			}
		}
		else if (event instanceof LayoutChangedEvent)
		{
			SlotRegion slotRegion = ((LayoutChangedEvent) event).getSlotRegion();

			// reconstruct features
			this.plotsLayer.clear();
			buildPlots(this.plotsLayer, slotRegion);
			slotRegion.addEventListener(this); // make sure we listen for events
												// from new backbone
		}
	}

	/**
	 * Updates the plots.
	 */
	public void update()
	{
		for (Iterator<SlotStyle> i = this.dataStyle.slots(); i.hasNext();)
		{
			SlotStyle slotStyle = i.next();

			for (Iterator<SlotItemStyle> j = slotStyle.styles(); j.hasNext();)
			{
				SlotItemStyle slotItemStyle = j.next();

				if (slotItemStyle instanceof PlotStyle)
				{
					updatePlotFromStyle((PlotStyle) slotItemStyle);
				}
			}
		}
	}

	/**
	 * Updates the plot, based on the information from the plot style.
	 * 
	 * @param plotStyle
	 */
	private void updatePlotFromStyle(PlotStyle plotStyle)
	{
		// Upper and lower plot colors:
		ArrayList<PlotItem>[] plotArray = this.plotItemMappings.get(plotStyle.getID());

		for (int i = 0; i < plotArray.length; i++)
		{
			ArrayList<PlotItem> plotList = plotArray[i];

			for (PlotItem item : plotList)
			{
				item.setPaint(plotStyle.getPaint()[i]);
			}
		}

		// Grid colors:
		Set<Integer> keys = this.gridLineMappings.keySet();

		for (Integer key : keys)
		{
			this.gridLineMappings.get(key).setPaint(plotStyle.getGridPaint());
		}
	}
}
