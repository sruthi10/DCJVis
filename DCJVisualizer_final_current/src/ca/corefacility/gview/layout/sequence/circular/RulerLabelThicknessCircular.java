package ca.corefacility.gview.layout.sequence.circular;

import java.awt.geom.Dimension2D;

import ca.corefacility.gview.layout.sequence.LocationConverter;
import ca.corefacility.gview.layout.sequence.RulerLabelThicknessCalculator;
import ca.corefacility.gview.managers.ruler.RulerManager;
import ca.corefacility.gview.managers.ruler.TickMarkCalculator;
import ca.corefacility.gview.style.items.RulerStyle;

public class RulerLabelThicknessCircular extends RulerLabelThicknessCalculator
{
	
	public RulerLabelThicknessCircular(RulerStyle rulerStyle, LocationConverter locationConverter)
	{
		String longestLabel = TickMarkCalculator.getLongestTickLabel(locationConverter.getSequenceLength());
		Dimension2D largestLabel = RulerManager.determineSizeOf(rulerStyle, longestLabel);
		
		if (rulerStyle.getRulerLabelsLocation().buildAbove())
		{
			maxTopThickness = (float)largestLabel.getWidth();
		}
		
		if (rulerStyle.getRulerLabelsLocation().buildBelow())
		{
			maxBottomThickness = (float)largestLabel.getWidth();
		}
	}
}
