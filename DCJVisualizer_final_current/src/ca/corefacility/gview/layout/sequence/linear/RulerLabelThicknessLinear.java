package ca.corefacility.gview.layout.sequence.linear;

import java.awt.geom.Dimension2D;

import ca.corefacility.gview.layout.sequence.LocationConverter;
import ca.corefacility.gview.layout.sequence.RulerLabelThicknessCalculator;
import ca.corefacility.gview.managers.ruler.RulerManager;
import ca.corefacility.gview.managers.ruler.TickMarkCalculator;
import ca.corefacility.gview.style.items.RulerStyle;

public class RulerLabelThicknessLinear extends RulerLabelThicknessCalculator
{
	public RulerLabelThicknessLinear(RulerStyle rulerStyle, LocationConverter locationConverter)
	{
		String longestLabel = TickMarkCalculator.getLongestTickLabel(locationConverter.getSequenceLength());
		Dimension2D largestLabel = RulerManager.determineSizeOf(rulerStyle, longestLabel);
		
		if (rulerStyle.getRulerLabelsLocation().buildAbove())
		{
			maxTopThickness = (float)largestLabel.getHeight();
		}
		
		if (rulerStyle.getRulerLabelsLocation().buildBelow())
		{
			maxBottomThickness = (float)largestLabel.getHeight();
		}
	}
}
