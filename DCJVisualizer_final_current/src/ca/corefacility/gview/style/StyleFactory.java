package ca.corefacility.gview.style;

import java.awt.Color;
import java.awt.Font;

import org.biojava.bio.seq.FeatureFilter;
import org.biojava.bio.seq.StrandedFeature;

import ca.corefacility.gview.data.GenomeData;
import ca.corefacility.gview.data.Slot;
import ca.corefacility.gview.layout.feature.FeatureShapeRealizer;
import ca.corefacility.gview.layout.feature.ForwardArrowShapeRealizer;
import ca.corefacility.gview.layout.feature.ReverseArrowShapeRealizer;
import ca.corefacility.gview.layout.sequence.LayoutFactory;
import ca.corefacility.gview.map.GViewMap;
import ca.corefacility.gview.map.GViewMapImp;
import ca.corefacility.gview.map.controllers.LegendItemStyleToken;
import ca.corefacility.gview.map.controllers.link.Link;
import ca.corefacility.gview.map.effects.ShapeEffectRenderer;
import ca.corefacility.gview.map.gui.GViewMapManager.Layout;
import ca.corefacility.gview.map.gui.Style;
import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;
import ca.corefacility.gview.style.datastyle.DataStyle;
import ca.corefacility.gview.style.datastyle.FeatureHolderStyle;
import ca.corefacility.gview.style.datastyle.LabelStyle;
import ca.corefacility.gview.style.datastyle.SlotStyle;
import ca.corefacility.gview.style.items.BackboneStyle;
import ca.corefacility.gview.style.items.LegendAlignment;
import ca.corefacility.gview.style.items.LegendItemStyle;
import ca.corefacility.gview.style.items.LegendStyle;
import ca.corefacility.gview.style.items.RulerStyle;
import ca.corefacility.gview.style.items.TooltipStyle;
import ca.corefacility.gview.textextractor.AnnotationExtractor;
import ca.corefacility.gview.utils.AndFilter;

/**
 * A class which stores/creates some styles that are used many times.
 * 
 * @author Aaron Petkau
 * @author Eric Marinier
 * 
 */
public class StyleFactory
{
	public static MapStyle createDefaultStyle()
	{
		MapStyle style = new MapStyle();

		// set the style
		GlobalStyle gStyle = style.getGlobalStyle();
		gStyle.setDefaultHeight(700);
		gStyle.setDefaultWidth(700);

		gStyle.setBackgroundPaint(Color.WHITE);
		gStyle.setShowBorder(true);
		gStyle.setTitlePaint(Color.BLACK);
		gStyle.setTitleFont(new Font("SansSerif", Font.PLAIN, 20));
		gStyle.setSlotSpacing(3.0);

		TooltipStyle tStyle = gStyle.getTooltipStyle();
		tStyle.setFont(new Font("SansSerif", Font.PLAIN, 12));
		tStyle.setTextPaint(Color.BLACK);
		tStyle.setBackgroundPaint(new Color(134, 134, 255));
		tStyle.setOutlinePaint(new Color(0.0f, 0.0f, 0.0f, 0.5f));

		BackboneStyle bStyle = gStyle.getBackboneStyle();
		bStyle.setPaint(Color.GRAY);
		bStyle.setThickness(3.0);
		bStyle.setShapeEffectRenderer(ShapeEffectRenderer.BASIC_RENDERER);

		RulerStyle rStyle = gStyle.getRulerStyle();
		rStyle.setMajorTickLength(5.0);
		rStyle.setMinorTickLength(2.0);
		rStyle.setTickDensity(0.7f);
		rStyle.setTickThickness(2.0);
		rStyle.setMinorTickPaint(Color.GREEN.darker().darker());
		rStyle.setMajorTickPaint(Color.GREEN.darker().darker());
		rStyle.setFont(new Font("SansSerif", Font.PLAIN, 12));
		rStyle.setTextPaint(Color.BLACK);
		rStyle.setTextBackgroundPaint(new Color(255, 255, 255, 200));

		DataStyle dataStyle = style.getDataStyle();

		SlotStyle positive = dataStyle.createSlotStyle(Slot.FIRST_UPPER);

		positive.setPaint(Color.BLUE);
		positive.setThickness(15);
		positive.setTransparency(1.0f);

		FeatureHolderStyle positiveHolder = positive.createFeatureHolderStyle(new FeatureFilter.And(
				new FeatureFilter.StrandFilter(StrandedFeature.POSITIVE), new FeatureFilter.ByType("CDS")));
		positiveHolder.setToolTipExtractor(new AnnotationExtractor("product"));
		positiveHolder.setFeatureShapeRealizer(FeatureShapeRealizer.CLOCKWISE_ARROW);

		LabelStyle posLabels = positiveHolder.getLabelStyle();
		posLabels.setLabelExtractor(new AnnotationExtractor("gene"));
		posLabels.setShowLabels(true);
		posLabels.setInitialLineLength(50);
		posLabels.setTextPaint(Color.BLUE);
		posLabels.setBackgroundPaint(new Color(255, 255, 255, 200));

		SlotStyle negative = dataStyle.createSlotStyle(Slot.FIRST_LOWER);

		negative.setPaint(Color.RED);
		negative.setThickness(15);
		negative.setTransparency(1.0f);

		FeatureHolderStyle negativeHolder = negative.createFeatureHolderStyle(new FeatureFilter.And(
				new FeatureFilter.StrandFilter(StrandedFeature.NEGATIVE), new FeatureFilter.ByType("CDS")));
		negativeHolder.setToolTipExtractor(new AnnotationExtractor("product"));
		negativeHolder.setFeatureShapeRealizer(FeatureShapeRealizer.COUNTERCLOCKWISE_ARROW);

		LabelStyle negLabels = negativeHolder.getLabelStyle();
		negLabels.setLabelExtractor(new AnnotationExtractor("gene"));
		negLabels.setShowLabels(true);
		negLabels.setInitialLineLength(50);
		negLabels.setTextPaint(Color.BLUE);
		negLabels.setBackgroundPaint(new Color(255, 255, 255, 200));

		return style;
	}

	/**
	 * Alternative style!
	 */
	public static MapStyle createDefaultStyle2()
	{
		MapStyle mapStyle = new MapStyle();

		SlotStyle positiveSlot = mapStyle.getDataStyle().createSlotStyle(1);
		SlotStyle negativeSlot = mapStyle.getDataStyle().createSlotStyle(-1);

		positiveSlot.setThickness(12);
		negativeSlot.setThickness(12);

		FeatureFilter positiveFilter = new AndFilter(new FeatureFilter.ByType("CDS"), new FeatureFilter.StrandFilter(
				StrandedFeature.POSITIVE));
		FeatureFilter negativeFilter = new AndFilter(new FeatureFilter.ByType("CDS"), new FeatureFilter.StrandFilter(
				StrandedFeature.NEGATIVE));

		FeatureHolderStyle positiveStyle = positiveSlot.createFeatureHolderStyle(positiveFilter);
		FeatureHolderStyle negativeStyle = negativeSlot.createFeatureHolderStyle(negativeFilter);

		positiveStyle.setToolTipExtractor(new AnnotationExtractor("product"));
		negativeStyle.setToolTipExtractor(new AnnotationExtractor("product"));

		positiveStyle.setPaint(new Color(87, 21, 21));
		negativeStyle.setPaint(new Color(55, 64, 87));

		positiveStyle.setFeatureShapeRealizer(new ForwardArrowShapeRealizer());
		negativeStyle.setFeatureShapeRealizer(new ReverseArrowShapeRealizer());

		mapStyle.getGlobalStyle().getBackboneStyle().setThickness(2);
		mapStyle.getGlobalStyle().getBackboneStyle().setPaint(new Color(0, 0, 0));

		mapStyle.getGlobalStyle().getRulerStyle().setFont(new Font("Serif", Font.PLAIN, 10));

		mapStyle.getGlobalStyle().getTooltipStyle().setBackgroundPaint(new Color(228, 231, 235));
		mapStyle.getGlobalStyle().getTooltipStyle().setOutlinePaint(new Color(0, 0, 0));
		mapStyle.getGlobalStyle().getTooltipStyle().setTextPaint(new Color(0, 0, 0));
		mapStyle.getGlobalStyle().getTooltipStyle().setFont(new Font("Serif", Font.PLAIN, 14));

		LabelStyle positiveLabels = positiveStyle.getLabelStyle();
		positiveLabels.setLabelExtractor(new AnnotationExtractor("gene"));
		positiveLabels.setShowLabels(true);
		positiveLabels.setInitialLineLength(50);
		positiveLabels.setTextPaint(new Color(87, 21, 21));
		positiveLabels.setBackgroundPaint(new Color(255, 255, 255, 0));
		positiveLabels.setFont(new Font("Serif", Font.PLAIN, 12));

		LabelStyle negativeLabels = negativeStyle.getLabelStyle();
		negativeLabels.setLabelExtractor(new AnnotationExtractor("gene"));
		negativeLabels.setShowLabels(true);
		negativeLabels.setInitialLineLength(50);
		negativeLabels.setTextPaint(new Color(55, 64, 87));
		negativeLabels.setBackgroundPaint(new Color(255, 255, 255, 0));
		negativeLabels.setFont(new Font("Serif", Font.PLAIN, 12));

		LegendStyle legendStyle = new LegendStyle();
		LegendItemStyle positiveLegend = new LegendItemStyle();
		LegendItemStyle negativeLegend = new LegendItemStyle();

		positiveLegend.setShowSwatch(true);
		positiveLegend.setText("Positve Stranded CDS");
		positiveLegend.setSwatchPaint(new Color(87, 21, 21));

		negativeLegend.setShowSwatch(true);
		negativeLegend.setText("Negative Stranded CDS");
		negativeLegend.setSwatchPaint(new Color(55, 64, 87));

		legendStyle.addLegendItem(positiveLegend);
		legendStyle.addLegendItem(negativeLegend);

		legendStyle.setAlignment(LegendAlignment.UPPER_LEFT);

		mapStyle.getGlobalStyle().addLegendStyle(legendStyle);

		Link positiveLink = new Link(new LegendItemStyleToken(positiveLegend));
		Link negativeLink = new Link(new LegendItemStyleToken(negativeLegend));

		positiveLegend.setLink(positiveLink);
		negativeLegend.setLink(negativeLink);

		positiveStyle.setLink(positiveLink);
		negativeStyle.setLink(negativeLink);

		return mapStyle;
	}

	/**
	 * Creates a new, default style.
	 * 
	 */
	public static Style createNewDefaultStyle(GenomeData genomeData)
	{
		return createNewStyle(genomeData, StyleFactory.createDefaultStyle2());
	}

	/**
	 * Creates a new, blank style.
	 * 
	 * @param genomeData
	 * @return A newly created blank style.
	 */
	public static Style createNewBlankStyle(GenomeData genomeData)
	{
		MapStyle mapStyle = new MapStyle();

		mapStyle.getGlobalStyle().getBackboneStyle().setThickness(2);
		mapStyle.getGlobalStyle().getBackboneStyle().setPaint(new Color(0, 0, 0));

		mapStyle.getGlobalStyle().getRulerStyle().setFont(new Font("Serif", Font.PLAIN, 10));

		return createNewStyle(genomeData, mapStyle);
	}

	/**
	 * Creates a new style.
	 * 
	 * @param genomeData
	 * @param mapStyle
	 * @return  A new Style object.
	 */
	private static Style createNewStyle(GenomeData genomeData, MapStyle mapStyle)
	{
		Style style;
		GViewMap gViewMap;

		LayoutFactory layoutFactory = StyleEditorUtility.DEFAULT_LAYOUT;

		gViewMap = new GViewMapImp(genomeData, mapStyle, layoutFactory);
		style = new Style(gViewMap);

		return style;
	}

	/**
	 * 
	 * @param style
	 * @param layout
	 * 
	 * @return A new, shallow copy of the current Style with the specified
	 *         layout.
	 */
	public static Style changeStyleLayout(Style style, Layout layout)
	{
		Style result = style.getStyleController().getGViewMapManager().changeGViewMapLayout(style, layout);

		return result;
	}
}
