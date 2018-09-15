package ca.corefacility.gview.data.readers.cgview;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.biojava.bio.Annotation;
import org.biojava.bio.BioException;
import org.biojava.bio.seq.Feature;
import org.biojava.bio.seq.Sequence;
import org.biojava.bio.seq.StrandedFeature;

import ca.corefacility.gview.layout.feature.FeatureShapeRealizer;
import ca.corefacility.gview.map.effects.ShapeEffectRenderer;
import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.style.datastyle.FeatureHolderStyle;
import ca.corefacility.gview.style.datastyle.LabelStyle;
import ca.corefacility.gview.style.datastyle.SlotStyle;
import ca.corefacility.gview.textextractor.AnnotationExtractor;

/**
 * Manages style information for features.  Will reduce similar style information for features
 *  to a single FeatureHolderStyle
 * @author aaron
 *
 */
class FeatureStyleManager
{
	private Sequence currentSequence;
	private MapStyle mapStyle;
	private SlotStyle currentSlotStyle;
	
	private static final String LABEL_PROPERTY = "CGView_Label";
	private static final String MOUSEOVER_PROPERTY = "CGView_Mouseover";
	private Map<FeatureStyleTemplate, FeatureHolderStyle> stylesMap;
	private Map<FeatureStyleTemplate, CGViewSetFeatureFilter> styleFilters;
	private static final String HYPERLINK_PROPERY = "CGView_Hyperlink";

	public FeatureStyleManager(Sequence currentSequence, MapStyle mapStyle)
	{
		this.currentSequence = currentSequence;
		this.mapStyle = mapStyle;
	}

	private double convertToHeightAdjust(double radiusAdjust, double proportionOfThickness)
	{
		double heightAdjust = 0;
	
		// if proportionOfThickness is not equal to the default, then heightAdjust should apply
		if (proportionOfThickness < 1.0)
		{
			heightAdjust = radiusAdjust * 2 - 1; // convert from value in [0,1] to value in [-1,1]
			// at 0, center of feature should be at bottom of slot, at 1 center should be at top
		}
		else // else, feature should be centered (heightAdjust = 0)
		{
			heightAdjust = 0;
		}
	
		return heightAdjust;
	}
	
	private ShapeEffectRenderer getShapeEffectRenderer(boolean showShading)
	{
		ShapeEffectRenderer theRenderer = null;
	
		if (showShading)
		{
			theRenderer = CGViewConstants.getDefaultShadedEffect();
		}
		else
		{
			theRenderer = CGViewConstants.getDefaultEffect();
		}
	
		return theRenderer;
	}

	public int getCgviewLength()
	{
		return currentSequence.length();
	}

	private void setAnnotationText(Annotation featureAnnotation, AnnotationTemplate annotationTemplate, boolean showLabels)
	{
		// set label if it exists
		if (annotationTemplate.label != null && showLabels)
		{
			featureAnnotation.setProperty( LABEL_PROPERTY , annotationTemplate.label);
		}

		if (annotationTemplate.mouseover != null)
		{
			featureAnnotation.setProperty(MOUSEOVER_PROPERTY, annotationTemplate.mouseover);
		}

		if (annotationTemplate.hyperlink != null)
		{
			featureAnnotation.setProperty(HYPERLINK_PROPERY, annotationTemplate.hyperlink);
		}
	}

	private void setupLabelStyle(LabelStyle labelStyle, FeatureStyleTemplate styleTemplate, Boolean globalShowLabels)
	{
		labelStyle.setFont(styleTemplate.labelFont);
		
		if (styleTemplate.giveFeaturePositions)
		{
			labelStyle.setLabelExtractor(new ca.corefacility.gview.textextractor.StringBuilder("%s %s",
					new AnnotationExtractor(LABEL_PROPERTY), new FeaturePositionsTextExtractor()));
		}
		else
		{
			labelStyle.setLabelExtractor(new AnnotationExtractor(LABEL_PROPERTY));
		}
		
		labelStyle.setLineThickness(styleTemplate.labelLineThickness);

		Color textColor;
		Color featureColorOpacity = CGViewConstants.colorToOpacity(styleTemplate.color, styleTemplate.opacity);

		// globalLabelColor overrides color of label
		if (styleTemplate.globalLabelColor != null)
		{
			textColor = styleTemplate.globalLabelColor;
		}
		else // else, set label text color the same as the feature color
		{
			textColor = featureColorOpacity;
		}

		// if useColoredLabelBackgrounds, adjust color of labels
		if (styleTemplate.useColoredLabelBackgrounds)
		{
			labelStyle.setTextPaint(Color.white);
			labelStyle.setBackgroundPaint(textColor);
			labelStyle.setAutoLabelLinePaint(false);
			labelStyle.setLabelLinePaint(textColor);
		}
		else
		{
			labelStyle.setTextPaint(textColor);
		}

		// if globalShowLabels is not set
		if (globalShowLabels == null)
		{
			labelStyle.setShowLabels(styleTemplate.showLabel);
		}
		else
		{
			labelStyle.setShowLabels(globalShowLabels);
		}
	}
	
	public SlotStyle createNewSlotStyle(SlotNumberManager slotNumberManager, StrandedFeature.Strand strand, FeatureShapeRealizer shapeRealizer,
			FeatureStyleTemplate styleTemplate, double thickness, Boolean globalLabel)
	{
		LabelStyle labelStyle;
		
		Color featureColorOpacity = CGViewConstants.colorToOpacity(styleTemplate.color, styleTemplate.opacity);
	
		currentSlotStyle = slotNumberManager.createNewSlot(strand, mapStyle.getDataStyle());
	
		labelStyle = currentSlotStyle.getLabelStyle();
	
		currentSlotStyle.setFeatureShapeRealizer(shapeRealizer);
		currentSlotStyle.setThickness(thickness);
		currentSlotStyle.setPaint(featureColorOpacity);
	
		labelStyle.setFont(styleTemplate.labelFont);
		labelStyle.setAutoLabelLinePaint(true);
	
		if (globalLabel != null)
		{
			labelStyle.setShowLabels(globalLabel);
		}
	
		currentSlotStyle.setTransparency(styleTemplate.opacity);
	
		labelStyle.setShowLabels(styleTemplate.showLabel);
	
		currentSlotStyle.setShapeEffectRenderer(getShapeEffectRenderer(styleTemplate.showShading));
	
		if (styleTemplate.useColoredLabelBackgrounds && styleTemplate.globalLabelColor != null)
		{
			labelStyle.setBackgroundPaint(styleTemplate.globalLabelColor);
		}
	
		// create new styles maps
		stylesMap = new HashMap<FeatureStyleTemplate, FeatureHolderStyle>();
		styleFilters = new HashMap<FeatureStyleTemplate, CGViewSetFeatureFilter>();
	
		return currentSlotStyle;
	}

	/**
	 * Tells this wrapper class to create the feature within the sequence.
	 */
	public void buildFeature(Feature.Template featureTemplate, FeatureStyleTemplate styleTemplate,
			AnnotationTemplate annotationTemplate, Boolean globalShowLabels)
	{
		if (currentSequence == null)
		{
			return;
		}

		try
		{
			Feature feature = currentSequence.createFeature(featureTemplate);
			// Remove the location from the featureTemplate and try to match a filter.

			FeatureHolderStyle holderStyle = stylesMap.get(styleTemplate);

			if (holderStyle != null)
			{
				CGViewSetFeatureFilter styleFilter = styleFilters.get(styleTemplate);
				styleFilter.addFeature(feature);

				Annotation featureAnnotation = feature.getAnnotation();
				if( featureAnnotation != null )
				{
					LabelStyle labelStyle = holderStyle.getLabelStyle();
					setAnnotationText(featureAnnotation, annotationTemplate, labelStyle.showLabels());
				}
			}
			else // build new FeatureHolderStyle
			{
				CGViewSetFeatureFilter featureFilter = new CGViewSetFeatureFilter();
				featureFilter.addFeature(feature);
				
				Color featureColorOpacity = CGViewConstants.colorToOpacity(styleTemplate.color, styleTemplate.opacity);

				// Create new FeatureHolderStyle and new Annotation.
				// Associate the Annotation with the new feature and make the FeatureHolderStyle match on the Annotation.
				// creates a style for each individual feature, and sets correct properties
				Annotation annotation = feature.getAnnotation();
				FeatureHolderStyle featureStyle = currentSlotStyle.createFeatureHolderStyle(
						featureFilter);
				featureStyle.setPaint(featureColorOpacity);
				featureStyle.setThickness(styleTemplate.proportionOfThickness);
				
				featureStyle.setHeightAdjust(
						convertToHeightAdjust(styleTemplate.radiusAdjustment, styleTemplate.proportionOfThickness));

				featureStyle.setFeatureShapeRealizer(styleTemplate.decoration);

				featureStyle.setShapeEffectRenderer(getShapeEffectRenderer(styleTemplate.showShading));

				featureStyle.setToolTipExtractor(new AnnotationExtractor(MOUSEOVER_PROPERTY));

				LabelStyle labelStyle = featureStyle.getLabelStyle();
				setupLabelStyle(labelStyle, styleTemplate, globalShowLabels);
				
				featureStyle.setHyperlinkExtractor(new AnnotationExtractor(HYPERLINK_PROPERY));

				setAnnotationText(annotation, annotationTemplate, labelStyle.showLabels());

				stylesMap.put(styleTemplate, featureStyle);
				styleFilters.put(styleTemplate, featureFilter);
			}
		}
		catch (BioException be)
		{
			be.printStackTrace();
		}
	}

	public void endSlot()
	{
		currentSlotStyle = null;
	}
}
