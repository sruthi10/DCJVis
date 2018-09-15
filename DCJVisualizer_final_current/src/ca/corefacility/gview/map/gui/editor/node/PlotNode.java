package ca.corefacility.gview.map.gui.editor.node;

import java.awt.Paint;

import ca.corefacility.gview.map.controllers.PlotStyleController;
import ca.corefacility.gview.map.controllers.PlotStyleToken;
import ca.corefacility.gview.map.gui.editor.panel.PlotPanel;

/**
 * The node class for the plot styles. 
 * 
 * Intended to be used within a StyleEditorTree.
 * 
 * @author Eric Marinier
 *
 */
public class PlotNode extends StyleEditorNode 
{
	private static final long serialVersionUID = 1L;	//requested by java.
	private static final String PLOT = "Plot";
	
	private final PlotPanel plotPanel;

	/**
	 * 
	 * @param plotPanel The related panel.
	 */
	public PlotNode(PlotPanel plotPanel) 
	{
		super(plotPanel, PLOT);
		
		if(plotPanel == null)
			throw new IllegalArgumentException("PlotPanel is null");

		this.plotPanel = plotPanel;
	}

	@Override
	public PlotPanel getPanel() 
	{
		if(this.plotPanel == null)
			throw new IllegalArgumentException("PlotPanel is null");
		
		return this.plotPanel;
	}
	
	@Override
	/**
	 * All plot nodes should have a slot node as their parent.
	 */
	public SlotNode getParent()
	{
		SlotNode parent = null;
		
		if(super.getParent() instanceof SlotNode)
		{
			parent = (SlotNode)super.getParent();
		}
		else if(super.getParent() != null)
		{
			throw new ClassCastException("PlotNodes must have a SlotNode parent.");
		}
		
		return parent;
	}
	
	public PlotStyleToken getPlotStyle()
	{
		return this.plotPanel.getPlotStyle();
	}

	public Paint getNodeColor()
	{
		PlotStyleController controller = this.plotPanel.getPlotController();
		PlotStyleToken style = this.plotPanel.getPlotStyle();
		
		return controller.getUpperColor(style);
	}
}
