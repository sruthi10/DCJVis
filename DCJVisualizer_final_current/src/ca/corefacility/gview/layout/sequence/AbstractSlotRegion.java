package ca.corefacility.gview.layout.sequence;

import ca.corefacility.gview.managers.ResolutionManager;
import ca.corefacility.gview.managers.ZoomEventDistributor;
import ca.corefacility.gview.map.event.GViewEventListener;

public abstract class AbstractSlotRegion implements SlotRegion
{
	// defines the maximum number of slots (excluding backbone)
	// used so that we can reserve some integers to represent special slots (ruler slots, etc.)
	private int MAX_NUMBER_SLOTS = 2000000;

	// we intercept zoom events from backbone here, and distribute them to any listeners
	private ZoomEventDistributor zoomEventDistributor;

	protected SlotTranslator slots;
	protected Backbone backbone;

	protected SequencePath sequencePath;

	private ResolutionManager resolutionManager;

	public AbstractSlotRegion(Backbone backbone, SlotTranslator slots)
	{
		if (backbone == null)
			throw new IllegalArgumentException("Backbone is null");
		else if (slots == null)
			throw new IllegalArgumentException("SlotTranslator is null");

		this.slots = slots;
		this.backbone = backbone;
		try
		{
			this.resolutionManager = new ResolutionManager(1.0, backbone.getMinScale());
		}
		catch ( IllegalArgumentException e )
		{
			this.resolutionManager = new ResolutionManager(2.0, backbone.getMinScale() );
		}

		this.zoomEventDistributor = new ZoomEventDistributor(this.resolutionManager);

		backbone.addEventListener(this.zoomEventDistributor);
	}

	public SlotPath getSlotPath(int slot)
	{
		this.sequencePath.clear();
		return new SlotPathImp(this.sequencePath, this.backbone, this.slots, slot);
	}

	public SlotTranslator getSlotTranslator()
	{
		return this.slots;
	}

	public int getMaxSlot()
	{
		return this.MAX_NUMBER_SLOTS/2;
	}

	public int getMinSlot()
	{
		return -1 * this.MAX_NUMBER_SLOTS/2;
	}

	public Backbone getBackbone()
	{
		return this.backbone;
	}

	@Override
	public ResolutionManager getResolutionManager()
	{
		return this.resolutionManager;
	}

	public void addEventListener(GViewEventListener listener)
	{
		this.zoomEventDistributor.addEventListener(listener);
	}

	public void removeAllEventListeners()
	{
		this.zoomEventDistributor.removeAllEventListeners();
	}

	public void removeEventListener(GViewEventListener listener)
	{
		this.zoomEventDistributor.removeEventListener(listener);
	}
}
