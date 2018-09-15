package ca.corefacility.gview.map.controllers;

import java.util.ArrayList;

import ca.corefacility.gview.map.GViewMap;
import ca.corefacility.gview.map.controllers.link.LinkController;
import ca.corefacility.gview.map.controllers.selection.SelectionController;
import ca.corefacility.gview.map.gui.GViewMapManager;
import ca.corefacility.gview.map.gui.Style;
import ca.corefacility.gview.style.GlobalStyle;
import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.style.datastyle.DataStyle;

/**
 * This class is responsible for managing meta-level information regarding all
 * the style controllers.
 * 
 * @author Eric Marinier
 * 
 */
public final class StyleController extends Controller
{
	// Style controllers:
	private final LegendStyleController legendStyleController;
	private final SlotStyleController slotStyleController;
	private final GlobalStyleController globalStyleController;
	private final BackboneStyleController backboneStyleController;
	private final RulerStyleController rulerStyleController;
	private final TooltipStyleController tooltipStyleController;
	private final LabelStyleController labelStyleController;
	private final PlotStyleController plotStyleController;
	private final PropertyMapperController propertyMapperController;
	private final SetStyleController setStyleController;
	private final GenomeDataController genomeDataController;
	private final LinkController linkController;
	private final SelectionController selectionController;

	// Controllers:
	private final ArrayList<Controller> controllers = new ArrayList<Controller>();

	// Related managers:
	private final GViewMapManager gViewMapManager;
	private final FileWriterManager fileWriterManager;

	public StyleController(Style style, GViewMap gViewMap)
	{
		MapStyle mapStyle = gViewMap.getMapStyle();
		GlobalStyle globalStyle = mapStyle.getGlobalStyle();
		DataStyle dataStyle = mapStyle.getDataStyle();

		this.legendStyleController = new LegendStyleController(this, globalStyle);
		this.controllers.add(this.legendStyleController);

		this.slotStyleController = new SlotStyleController(this, dataStyle);
		this.controllers.add(this.slotStyleController);

		this.globalStyleController = new GlobalStyleController(this, globalStyle);
		this.controllers.add(this.globalStyleController);

		this.backboneStyleController = new BackboneStyleController(this, globalStyle.getBackboneStyle());
		this.controllers.add(this.backboneStyleController);

		this.rulerStyleController = new RulerStyleController(this, globalStyle.getRulerStyle());
		this.controllers.add(this.rulerStyleController);

		this.tooltipStyleController = new TooltipStyleController(this, globalStyle.getTooltipStyle());
		this.controllers.add(this.tooltipStyleController);

		this.labelStyleController = new LabelStyleController(this);
		this.controllers.add(this.labelStyleController);

		this.plotStyleController = new PlotStyleController(this);
		this.controllers.add(this.plotStyleController);

		this.propertyMapperController = new PropertyMapperController(this);
		this.controllers.add(this.propertyMapperController);

		this.setStyleController = new SetStyleController(this);
		this.controllers.add(this.setStyleController);

		this.genomeDataController = new GenomeDataController(gViewMap.getGenomeData());

		this.fileWriterManager = new FileWriterManager(mapStyle);
		this.gViewMapManager = new GViewMapManager(style, gViewMap);

		this.linkController = new LinkController();
		this.selectionController = new SelectionController();
	}

	/**
	 * 
	 * @return The legend style controller.
	 */
	public LegendStyleController getLegendStyleController()
	{
		return this.legendStyleController;
	}

	/**
	 * 
	 * @return The slot style controller.
	 */
	public SlotStyleController getSlotStyleController()
	{
		return this.slotStyleController;
	}

	/**
	 * 
	 * @return The global style controller.
	 */
	public GlobalStyleController getGlobalStyleController()
	{
		return this.globalStyleController;
	}

	/**
	 * 
	 * @return The backbone style controller.
	 */
	public BackboneStyleController getBackboneStyleController()
	{
		return this.backboneStyleController;
	}

	/**
	 * 
	 * @return The ruler style controller.
	 */
	public RulerStyleController getRulerStyleController()
	{
		return this.rulerStyleController;
	}

	/**
	 * 
	 * @return The tool tip style controller.
	 */
	public TooltipStyleController getTooltipStyleController()
	{
		return this.tooltipStyleController;
	}

	/**
	 * 
	 * @return The label style controller.
	 */
	public LabelStyleController getLabelStyleController()
	{
		return this.labelStyleController;
	}

	/**
	 * 
	 * @return The plot style controller.
	 */
	public PlotStyleController getPlotStyleController()
	{
		return this.plotStyleController;
	}

	/**
	 * 
	 * @return The property mapper controller.
	 */
	public PropertyMapperController getPropertyMapperController()
	{
		return this.propertyMapperController;
	}

	/**
	 * 
	 * @return The set style controller.
	 */
	public SetStyleController getSetStyleController()
	{
		return this.setStyleController;
	}

	/**
	 * 
	 * @return The GViewMap manager.
	 */
	public GViewMapManager getGViewMapManager()
	{
		return this.gViewMapManager;
	}

	/**
	 * 
	 * @return The file writer controller.
	 */
	public FileWriterManager getFileWriterController()
	{
		return this.fileWriterManager;
	}

	/**
	 * 
	 * @return The genome data controller.
	 */
	public GenomeDataController getGenomeDataController()
	{
		return this.genomeDataController;
	}

	/**
	 * 
	 * @return The link controller.
	 */
	public LinkController getLinkController()
	{
		return this.linkController;
	}

	/**
	 * 
	 * @return The selection controller.
	 */
	public SelectionController getSelectionController()
	{
		return this.selectionController;
	}

	@Override
	// ANY rebuilding.
	public boolean isRebuildRequired()
	{
		boolean rebuild = super.isRebuildRequired();

		for (int i = 0; i < this.controllers.size(); i++)
		{
			rebuild |= this.controllers.get(i).isRebuildRequired();
		}

		return rebuild;
	}

	// Clarity
	/**
	 * 
	 * @return Whether or not a full rebuild is required.
	 */
	public boolean isFullRebuildRequired()
	{
		return super.isRebuildRequired();
	}

	// Clarity
	/**
	 * Notifty the controller that a full rebuild is required.
	 */
	public void notifyFullRebuildRequired()
	{
		super.notifyRebuildRequired();
	}

	@Override
	// Reset ALL rebuilding.
	public void resetRebuildRequired()
	{
		super.resetRebuildRequired();

		for (int i = 0; i < this.controllers.size(); i++)
		{
			this.controllers.get(i).resetRebuildRequired();
		}
	}
}
