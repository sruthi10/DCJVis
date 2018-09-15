package ca.corefacility.gview.layout.sequence.linear;

import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.layout.sequence.LayoutFactory;
import ca.corefacility.gview.layout.sequence.LocationConverter;
import ca.corefacility.gview.layout.sequence.RulerLabelThicknessCalculator;
import ca.corefacility.gview.layout.sequence.SlotRegion;
import ca.corefacility.gview.layout.sequence.SlotTranslator;
import ca.corefacility.gview.layout.sequence.SlotTranslatorImp;
import ca.corefacility.gview.managers.labels.LabelsManager;
import ca.corefacility.gview.managers.labels.LabelsManagerSlots;
import ca.corefacility.gview.style.MapStyle;

public class LayoutFactoryLinear extends LayoutFactory
{

	@Override
	public SlotRegion createSlotRegion(MapStyle mapStyle, LocationConverter locationConverter, double initialBackboneLength)
	{
		if (initialBackboneLength <= 0)
		{
			throw new IllegalArgumentException("initialBackboneLength must be positive");
		}
		
		RulerLabelThicknessCalculator rulerLabelCalculator =
			new RulerLabelThicknessLinear(mapStyle.getGlobalStyle().getRulerStyle(), locationConverter);
		SlotTranslator slots = new SlotTranslatorImp(mapStyle,rulerLabelCalculator);
		
		Backbone backbone = new BackboneLinear(locationConverter, initialBackboneLength);
		
		return new SlotRegionLinear(backbone, slots);
	}

	@Override
	public LabelsManager createLabelsManager()
	{
		return new LabelsManagerSlots();
	}
}
