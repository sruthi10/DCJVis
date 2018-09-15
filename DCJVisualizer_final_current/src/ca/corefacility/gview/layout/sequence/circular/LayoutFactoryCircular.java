package ca.corefacility.gview.layout.sequence.circular;

import ca.corefacility.gview.layout.sequence.Backbone;
import ca.corefacility.gview.layout.sequence.LayoutFactory;
import ca.corefacility.gview.layout.sequence.LocationConverter;
import ca.corefacility.gview.layout.sequence.RulerLabelThicknessCalculator;
import ca.corefacility.gview.layout.sequence.ScaleCalculator;
import ca.corefacility.gview.layout.sequence.SlotRegion;
import ca.corefacility.gview.layout.sequence.SlotTranslator;
import ca.corefacility.gview.layout.sequence.SlotTranslatorImp;
import ca.corefacility.gview.layout.sequence.linear.RulerLabelThicknessLinear;
import ca.corefacility.gview.managers.labels.LabelsManager;
import ca.corefacility.gview.managers.labels.LabelsManagerSingle;
import ca.corefacility.gview.style.MapStyle;

public class LayoutFactoryCircular extends LayoutFactory
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
		
		SlotTranslator slotTranslator = new SlotTranslatorImp(mapStyle, rulerLabelCalculator);
		
		double initialRadius = initialBackboneLength/(2*Math.PI);
		
		Backbone backbone = new BackboneCircular(locationConverter, initialRadius, slotTranslator.getBottomMostHeight());

		return new SlotRegionCircular(backbone, slotTranslator);
	}

	@Override
	public LabelsManager createLabelsManager()
	{
		ScaleCalculator scaleCalculator = new ScaleCalculatorCircular();
		return new LabelsManagerSingle(scaleCalculator);
	}
}
