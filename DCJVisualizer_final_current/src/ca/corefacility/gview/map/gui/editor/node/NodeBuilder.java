package ca.corefacility.gview.map.gui.editor.node;

import java.util.ArrayList;

import ca.corefacility.gview.map.controllers.FeatureHolderStyleToken;
import ca.corefacility.gview.map.controllers.GenomeDataController;
import ca.corefacility.gview.map.controllers.LabelStyleToken;
import ca.corefacility.gview.map.controllers.LegendItemStyleToken;
import ca.corefacility.gview.map.controllers.LegendStyleController;
import ca.corefacility.gview.map.controllers.LegendStyleToken;
import ca.corefacility.gview.map.controllers.PlotStyleToken;
import ca.corefacility.gview.map.controllers.PropertyMappableToken;
import ca.corefacility.gview.map.controllers.PropertyMapperController;
import ca.corefacility.gview.map.controllers.SetStyleController;
import ca.corefacility.gview.map.controllers.SlotItemStyleToken;
import ca.corefacility.gview.map.controllers.SlotStyleController;
import ca.corefacility.gview.map.controllers.SlotStyleToken;
import ca.corefacility.gview.map.controllers.StyleController;
import ca.corefacility.gview.map.controllers.link.LinkController;
import ca.corefacility.gview.map.controllers.link.LinkListener;
import ca.corefacility.gview.map.controllers.link.Linkable;
import ca.corefacility.gview.map.gui.editor.StyleEditorTree;
import ca.corefacility.gview.map.gui.editor.panel.BackbonePanel;
import ca.corefacility.gview.map.gui.editor.panel.GlobalPanel;
import ca.corefacility.gview.map.gui.editor.panel.LabelPanel;
import ca.corefacility.gview.map.gui.editor.panel.LegendItemPanel;
import ca.corefacility.gview.map.gui.editor.panel.LegendPanel;
import ca.corefacility.gview.map.gui.editor.panel.LegendsPanel;
import ca.corefacility.gview.map.gui.editor.panel.PlotPanel;
import ca.corefacility.gview.map.gui.editor.panel.RulerPanel;
import ca.corefacility.gview.map.gui.editor.panel.SetPanel;
import ca.corefacility.gview.map.gui.editor.panel.SlotPanel;
import ca.corefacility.gview.map.gui.editor.panel.SlotsPanel;
import ca.corefacility.gview.map.gui.editor.panel.TooltipPanel;
import ca.corefacility.gview.map.gui.editor.panel.propertyMapper.PropertyMapperPanel;

/**
 * This class is responsible for building nodes for the style editor tree
 * object.
 * 
 * @see StyleEditorTree
 * 
 * @author Eric Marinier
 * 
 */
public class NodeBuilder
{
	private final StyleController styleController;
	private final StyleEditorTree tree;

	public NodeBuilder(StyleController styleController, StyleEditorTree tree)
	{
		this.styleController = styleController;
		this.tree = tree;
	}

	/**
	 * Creates a global node.
	 * 
	 * @return The created global node.
	 */
	public GlobalNode createGlobalNode()
	{
		GlobalPanel globalPanel = new GlobalPanel(this.styleController.getGlobalStyleController());
		GlobalNode globalNode = new GlobalNode(globalPanel);

		this.tree.getRoot().add(globalNode);

		return globalNode;
	}

	/**
	 * Creates the backbone node.
	 * 
	 * @param globalNode
	 *            The global node.
	 * @return The created backbone node.
	 */
	public BackboneNode createBackboneNode(GlobalNode globalNode)
	{
		if (globalNode == null)
			throw new IllegalArgumentException("GlobalNode is null.");

		BackbonePanel backbonePanel = new BackbonePanel(this.styleController.getBackboneStyleController());
		BackboneNode backboneNode = new BackboneNode(backbonePanel);

		globalNode.add(backboneNode);

		return backboneNode;
	}

	/**
	 * Creates the backbone node, when there already exists a backbone panel.
	 * This method exists because the same backbone node exists in two
	 * locations, but functions different.
	 * 
	 * @param globalNode
	 *            The global node.
	 * @param backbonePanel
	 * @return The created backbone node.
	 */
	public BackboneNode createBackboneNode(GlobalNode globalNode, BackbonePanel backbonePanel)
	{
		if (globalNode == null)
			throw new IllegalArgumentException("GlobalNode is null.");

		BackboneNode backboneNode = new BackboneNode(backbonePanel);
		globalNode.add(backboneNode);

		return backboneNode;
	}

	/**
	 * Creates the ruler node.
	 * 
	 * @param globalNode
	 *            The global node.
	 * @return The created ruler node.
	 */
	public RulerNode createRulerNode(GlobalNode globalNode)
	{
		if (globalNode == null)
			throw new IllegalArgumentException("GlobalNode is null.");

		RulerPanel rulerPanel = new RulerPanel(this.styleController.getRulerStyleController());
		RulerNode rulerNode = new RulerNode(rulerPanel);

		globalNode.add(rulerNode);

		return rulerNode;
	}

	/**
	 * Creates the tool tip node.
	 * 
	 * @param globalNode
	 *            The global node.
	 * @return The created tool tip node.
	 */
	public TooltipNode createTooltipNode(GlobalNode globalNode)
	{
		if (globalNode == null)
			throw new IllegalArgumentException("GlobalNode is null.");

		TooltipPanel tooltipPanel = new TooltipPanel(this.styleController.getTooltipStyleController());
		TooltipNode tooltipNode = new TooltipNode(tooltipPanel);

		globalNode.add(tooltipNode);

		return tooltipNode;
	}

	/**
	 * Creates the legend node.
	 * 
	 * @param globalNode
	 *            The global node.
	 * @return The created legend node.
	 */
	public LegendNode createLegendNode(GlobalNode globalNode)
	{
		if (globalNode == null)
			throw new IllegalArgumentException("GlobalNode is null.");

		LegendNode legendNode = new LegendNode(new LegendsPanel());

		globalNode.add(legendNode);

		createLegendStyleNodes(legendNode);

		return legendNode;
	}

	/**
	 * Creates the legend style nodes.
	 * 
	 * @param legendNode
	 *            The legend node.
	 */
	public void createLegendStyleNodes(LegendNode legendNode)
	{
		if (legendNode == null)
			throw new IllegalArgumentException("LegendNode is null.");

		LegendStyleController legendStyleController = this.styleController.getLegendStyleController();
		ArrayList<LegendStyleToken> legendStyles = legendStyleController.getLegends();

		for (int i = 0; i < legendStyles.size(); i++)
		{
			createLegendStyleNode(legendNode, legendStyles.get(i));
		}
	}

	/**
	 * Creates a legend style node.
	 * 
	 * @param legendNode
	 *            The legend node.
	 * @param legendStyle
	 * @return The created legend style node.
	 */
	public LegendStyleNode createLegendStyleNode(LegendNode legendNode, LegendStyleToken legendStyle)
	{
		if (legendNode == null)
			throw new IllegalArgumentException("LegendNode is null.");

		LegendStyleController legendStyleController = this.styleController.getLegendStyleController();

		LegendPanel legendPanel;
		LegendStyleNode legendStyleNode;

		legendPanel = new LegendPanel(legendStyleController, legendStyle, this.styleController.getSelectionController());
		legendStyleNode = new LegendStyleNode(legendPanel);

		// update name
		legendStyleNode.rename(LegendStyleController.LEGEND_STYLE + " " + legendStyleController.giveLegendStyleID());

		legendNode.add(legendStyleNode);

		createLegendItemStyleNodes(legendStyleNode, legendStyle);

		return legendStyleNode;
	}

	/**
	 * Creates the legend item style nodes.
	 * 
	 * @param legendStyleNode
	 *            The associated legend style node.
	 * @param legendStyle
	 */
	public void createLegendItemStyleNodes(LegendStyleNode legendStyleNode, LegendStyleToken legendStyle)
	{
		if (legendStyleNode == null)
			throw new IllegalArgumentException("LegendStyleNode is null.");

		LegendStyleController legendStyleController = styleController.getLegendStyleController();
		ArrayList<LegendItemStyleToken> legendItemStyles = legendStyleController.getLegendItems(legendStyle);

		for (int i = 0; i < legendItemStyles.size(); i++)
		{
			createLegendItemStyleNode(legendStyleNode, legendStyle, legendItemStyles.get(i));
		}
	}

	/**
	 * Creates a legend item style node.
	 * 
	 * @param legendStyleNode
	 *            The associated legend style node.
	 * @param legendStyle
	 * @param legendItemStyle
	 * @return The created legend item style node.
	 */
	public LegendItemNode createLegendItemStyleNode(LegendStyleNode legendStyleNode, LegendStyleToken legendStyle,
			LegendItemStyleToken legendItemStyle)
	{
		if (legendStyleNode == null)
			throw new IllegalArgumentException("LegendStyleNode is null.");

		LegendItemPanel legendItemStylePanel;
		LegendItemNode legendItemStyleNode;

		LegendStyleController legendStyleController = this.styleController.getLegendStyleController();

		legendItemStylePanel = new LegendItemPanel(legendStyleController, legendStyle, legendItemStyle,
				this.styleController.getLinkController(), this.styleController.getSelectionController());
		legendItemStyleNode = new LegendItemNode(legendItemStylePanel);

		legendStyleNode.add(legendItemStyleNode);

		this.styleController.getLinkController().addLinkListener(legendItemStyleNode);

		return legendItemStyleNode;
	}

	/**
	 * Creates the slots node. This is a single node.
	 * 
	 * @return The created slots node.
	 */
	public SlotsNode createSlotNodes()
	{
		SlotNode currentSlotNode;
		SlotsNode slotsNode = new SlotsNode(new SlotsPanel());

		SlotStyleController slotStyleController = this.styleController.getSlotStyleController();
		ArrayList<SlotStyleToken> slotStyles = slotStyleController.getSlots();

		BackbonePanel backbonePanel = new BackbonePanel(this.styleController.getBackboneStyleController());

		this.tree.getRoot().add(slotsNode);
		slotsNode.add(new BackboneSlotNode(backbonePanel));

		for (int i = 0; i < slotStyles.size(); i++)
		{
			currentSlotNode = createSlotNode(slotStyles.get(i));
			slotsNode.add(currentSlotNode);

			createRecursiveSetNodes(currentSlotNode, slotStyleController.getSlotItems(slotStyles.get(i)));
		}

		return slotsNode;
	}

	/**
	 * Creates a single slot node under the slots node.
	 * 
	 * @param slotStyle
	 * @return The created slot node.
	 */
	public SlotNode createSlotNode(SlotStyleToken slotStyle)
	{
		if (slotStyle == null)
			throw new IllegalArgumentException("SlotStyleToken is null.");

		SlotStyleController slotStyleController = this.styleController.getSlotStyleController();

		SlotPanel slotPanel = new SlotPanel(this.styleController.getSlotStyleController(), slotStyle,
				this.styleController.getSelectionController());
		SlotNode slotNode = new SlotNode(slotPanel);

		if (slotStyleController.hasPropertyMapper(slotStyle))
		{
			createPropertyMapperNode(slotNode, slotStyle);
		}

		slotNode.update();

		return slotNode;
	}

	/**
	 * Creates a property mapper node.
	 * 
	 * @param node
	 *            The associated parent node.
	 * @param token
	 * @return The created property mapper node.
	 */
	public PropertyMapperNode createPropertyMapperNode(FeatureContainerNode node, FeatureHolderStyleToken token)
	{
		PropertyMapperController propertyMapperController = this.styleController.getPropertyMapperController();
		PropertyMappableToken propertyMappable = propertyMapperController.getPropertyMappable(token);

		return createPropertyMapperNode(node, propertyMappable);
	}

	/**
	 * Creates a property mapper node.
	 * 
	 * @param node
	 *            The associated parent node.
	 * @param token
	 * @return The created property mapper node.
	 */
	public PropertyMapperNode createPropertyMapperNode(FeatureContainerNode node, SlotStyleToken token)
	{
		PropertyMapperController propertyMapperController = this.styleController.getPropertyMapperController();
		PropertyMappableToken propertyMappable = propertyMapperController.getPropertyMappable(token);

		return createPropertyMapperNode(node, propertyMappable);
	}

	/**
	 * Creates a property mapper node.
	 * 
	 * @param node
	 *            The associated parent node.
	 * @param propertyMappable
	 * @return The created property mapper node.
	 */
	public PropertyMapperNode createPropertyMapperNode(FeatureContainerNode node, PropertyMappableToken propertyMappable)
	{
		if (node == null)
		{
			throw new IllegalArgumentException("FeatureContainerNode is null.");
		}

		PropertyMapperPanel propertyMapperPanel;
		PropertyMapperNode propertyMapperNode;
		PropertyMapperController propertyMapperController = this.styleController.getPropertyMapperController();

		if (!node.containsPropertyMapper())
		{
			propertyMapperPanel = new PropertyMapperPanel(propertyMapperController, propertyMappable,
					this.styleController.getSelectionController());
			propertyMapperNode = new PropertyMapperNode(propertyMapperPanel);

			node.add(propertyMapperNode);
		}
		else
		{
			throw new IllegalArgumentException("The provided node should not already contain a property mapper.");
		}

		return propertyMapperNode;
	}

	/**
	 * Recursively creates all set nodes for the passed list of sets and their
	 * children. They are added to the passed parent node.
	 * 
	 * @param topNode
	 *            The associated parent.
	 * @param set
	 */
	public void createRecursiveSetNodes(StyleEditorNode topNode, ArrayList<SlotItemStyleToken> set)
	{
		if (topNode == null)
			throw new IllegalArgumentException("StyleEditorNode topNode is null.");

		SlotStyleController slotStyleController = this.styleController.getSlotStyleController();

		PlotNode plotNode;
		SetNode setNode;

		SlotItemStyleToken current;
		PlotStyleToken plotStyle;
		FeatureHolderStyleToken featureHolderStyle;

		for (int i = 0; i < set.size(); i++)
		{
			current = set.get(i);

			// Plot Style
			if (current instanceof PlotStyleToken)
			{
				plotStyle = (PlotStyleToken) current;
				plotNode = createPlotNode(plotStyle);

				topNode.add(plotNode);
			}
			// Feature Holder Style
			else if (current instanceof FeatureHolderStyleToken)
			{
				featureHolderStyle = (FeatureHolderStyleToken) current;
				setNode = createSetNode(featureHolderStyle);

				topNode.add(setNode);

				// sub nodes
				createRecursiveSetNodes(setNode, slotStyleController.getSlotItems(featureHolderStyle));
			}
		}
	}

	/**
	 * Creates a plot node.
	 * 
	 * @param plotStyle
	 * @return The created plot node.
	 */
	public PlotNode createPlotNode(PlotStyleToken plotStyle)
	{
		if (plotStyle == null)
			throw new IllegalArgumentException("PlotStyleToken is null.");

		PlotPanel plotPanel = new PlotPanel(this.styleController.getPlotStyleController(), plotStyle,
				this.styleController.getSelectionController());
		PlotNode plotNode = new PlotNode(plotPanel);

		return plotNode;
	}

	/**
	 * Creates a set node.
	 * 
	 * @param featureHolderStyle
	 * @return The created set node.
	 */
	public SetNode createSetNode(FeatureHolderStyleToken featureHolderStyle)
	{
		if (featureHolderStyle == null)
			throw new IllegalArgumentException("FeatureHolderStyleToken is null.");

		SetStyleController setStyleController = this.styleController.getSetStyleController();
		GenomeDataController genomeDataController = this.styleController.getGenomeDataController();

		SetPanel setPanel = new SetPanel(setStyleController, featureHolderStyle, genomeDataController,
				this.styleController.getLinkController(), this.styleController.getSelectionController());
		SetNode setNode = new SetNode(setPanel);
		setNode.update();

		LabelStyleToken labelStyle = this.styleController.getLabelStyleController().getLabelStyleToken(
				featureHolderStyle);
		createLabelStyleNode(setNode, labelStyle, featureHolderStyle);

		if (setStyleController.hasPropertyMapper(featureHolderStyle))
		{
			createPropertyMapperNode(setNode, featureHolderStyle);
		}

		this.styleController.getLinkController().addLinkListener(setNode);

		return setNode;
	}

	/**
	 * Creates a label style node.
	 * 
	 * @param parentNode
	 *            The associated parent node.
	 * @param labelStyle
	 */
	public void createLabelStyleNode(FeatureContainerNode parentNode, LabelStyleToken labelStyle,
			FeatureHolderStyleToken featureHolderStyle)
	{
		if (parentNode == null)
			throw new IllegalArgumentException("FeatureContainerNode is null.");

		if (!parentNode.containsLabelStyleNode())
		// if it doesn't contain a label style node already
		{
			LabelPanel labelPanel = new LabelPanel(labelStyle, featureHolderStyle,
					this.styleController.getLabelStyleController(), this.styleController.getSetStyleController(),
					this.styleController.getGenomeDataController(), this.styleController.getSelectionController());
			LabelNode labelNode = new LabelNode(labelPanel);

			parentNode.add(labelNode);
			labelNode.update();
		}
	}

	/**
	 * Handles the deletion of the node from the passed tree model.
	 * 
	 * This is the preferred way of deleting a StyleEditorNode.
	 * 
	 * @param node
	 *            The node to delete.
	 */
	public void removeNode(StyleEditorNode node)
	{
		Object child;

		// Recursively remove all the node's children:
		while (node.getChildCount() > 0)
		{
			child = node.getFirstChild();

			if (child instanceof StyleEditorNode)
			{
				removeNode((StyleEditorNode) child);
			}
		}

		// Non-recursive handling of individual nodes:
		StyleEditorNode parent = node.getParent();
		this.tree.getTreeModel().removeNodeFromParent(node);

		if (node instanceof LinkListener)
		{
			this.styleController.getLinkController().removeLinkListener((LinkListener) node);
		}

		if (node instanceof Linkable && ((Linkable) node).getLink() != null)
		{
			LinkController.removeLink(node, this.tree);
		}

		if (parent != null)
		{
			parent.update();
		}
	}
}
