package ca.corefacility.gview.map.controllers;

import java.awt.Color;
import java.awt.Paint;
import java.util.List;

import org.biojava.bio.seq.FeatureFilter;
import org.biojava.bio.seq.StrandedFeature;
import org.biojava.bio.seq.StrandedFeature.Strand;

import ca.corefacility.gview.data.readers.cgview.CGViewSetFeatureFilter;
import ca.corefacility.gview.layout.feature.FeatureShapeRealizer;
import ca.corefacility.gview.map.controllers.link.Link;
import ca.corefacility.gview.map.effects.ShapeEffectRenderer;
import ca.corefacility.gview.style.datastyle.FeatureHolderStyle;
import ca.corefacility.gview.style.datastyle.SlotStyle;
import ca.corefacility.gview.textextractor.FeatureTextExtractor;
import ca.corefacility.gview.utils.AndFilter;
import ca.corefacility.gview.utils.AnnotationValueContains;
import ca.corefacility.gview.utils.SequenceNameFilter;
import ca.corefacility.gview.utils.Util;

/**
 * This class is responsible for controlling access to the feature holder
 * styles.
 * 
 * @author Eric Marinier
 * 
 */
public class SetStyleController extends Controller
{
	public static final Color DEFAULT_SET_COLOR = new Color(200, 200, 200);

	public static final FeatureTextExtractor DEFAULT_EXTRACTOR = FeatureTextExtractor.BLANK;
	public static final FeatureShapeRealizer DEFAULT_REALIZER = FeatureShapeRealizer.NO_ARROW;
	public static final ShapeEffectRenderer DEFAULT_RENDERER = ShapeEffectRenderer.STANDARD_RENDERER;

	private static final String ALL = "All";
	private static final String AND = "And";
	private static final String HAS_ANNOTATION = "Has Annotation";
	private static final String NOT = "Not";
	private static final String OR = "Or";
	private static final String SEQUENCE_NAME = "Sequence Name";
	private static final String CGVIEW_FILTER = "CGView Set";
	private static final String UNKNOWN_FEATURE = "Unknown";

	private static final String POSITIVE = "Positive";
	private static final String NEGATIVE = "Negative";
	private static final String UNKNOWN = "Unknown";

	private final StyleController styleController;

	public SetStyleController(StyleController styleController)
	{
		this.styleController = styleController;
	}

	/**
	 * 
	 * @param token
	 *            The associated feature holder style.
	 * @return The set's thickness within [0, 1].
	 */
	public double getThickness(FeatureHolderStyleToken token)
	{
		FeatureHolderStyle style = token.getStyle();

		double thickness = style.getThickness();

		if (thickness < 0)
		{
			thickness = 0;
		}
		else if (thickness > 1)
		{
			thickness = 1;
		}

		return thickness;
	}

	/**
	 * 
	 * @param token
	 *            The associated feature holder style.
	 * @return The text extractor or the default extractor if NULL.
	 */
	public FeatureTextExtractor getTextExtractor(FeatureHolderStyleToken token)
	{
		FeatureHolderStyle style = token.getStyle();

		FeatureTextExtractor extractor = style.getToolTipExtractor();

		if (extractor == null)
		{
			extractor = DEFAULT_EXTRACTOR;
		}

		return extractor;
	}

	/**
	 * 
	 * @param token
	 *            The associated feature holder style.
	 * @return The feature shape realizer or the default realizer if NULL.
	 */
	public FeatureShapeRealizer getFeatureShapeRealizer(FeatureHolderStyleToken token)
	{
		FeatureHolderStyle style = token.getStyle();

		FeatureShapeRealizer realizer = style.getFeatureShapeRealizer();

		if (realizer == null)
		{
			realizer = DEFAULT_REALIZER;
		}

		return realizer;
	}

	/**
	 * 
	 * @param token
	 *            The associated feature holder style.
	 * @return The shape effect renderer or the default renderer if NULL.
	 */
	public ShapeEffectRenderer getShapeEffectRenderer(FeatureHolderStyleToken token)
	{
		FeatureHolderStyle style = token.getStyle();

		ShapeEffectRenderer renderer = style.getShapeEffectRenderer();

		if (renderer == null)
		{
			renderer = DEFAULT_RENDERER;
		}

		return renderer;
	}

	/**
	 * 
	 * @param token
	 *            The associated feature holder style.
	 * @return The color of the set or the default color if NULL.
	 */
	public Paint getColor(FeatureHolderStyleToken token)
	{
		FeatureHolderStyle style = token.getStyle();

		Paint color;

		if (style.getPaint().length == 0 || style.getPaint()[0] == null)
		{
			color = DEFAULT_SET_COLOR;
		}
		else
		{
			color = style.getPaint()[0];
		}

		return color;
	}

	/**
	 * 
	 * @param token
	 *            The associated feature holder style.
	 * @param thickness
	 *            The new thickness of the set between [0, 1].
	 */
	public void setThickness(FeatureHolderStyleToken token, double thickness)
	{
		FeatureHolderStyle style = token.getStyle();

		if (thickness < 0)
		{
			thickness = 0;
		}
		else if (thickness > 1)
		{
			thickness = 1;
		}

		if (style.getThickness() != thickness)
		{
			style.setThickness(thickness);

			this.styleController.notifyFullRebuildRequired();
		}
	}

	/**
	 * 
	 * @param token
	 *            The associated feature holder style.
	 * @param extractor
	 *            The new text extractor.
	 */
	public void setTextExtractor(FeatureHolderStyleToken token, FeatureTextExtractor extractor)
	{
		FeatureHolderStyle style = token.getStyle();

		if (extractor == null)
		{
			extractor = DEFAULT_EXTRACTOR;
		}

		if (!Util.isEqual(style.getToolTipExtractor(), extractor))
		{
			style.setToolTipExtractor(extractor);

			this.notifyRebuildRequired();
		}
	}

	/**
	 * 
	 * @param token
	 *            The associated feature holder style.
	 * @param realizer
	 *            The new shape realizer.
	 */
	public void setFeatureShapeRealizer(FeatureHolderStyleToken token, FeatureShapeRealizer realizer)
	{
		FeatureHolderStyle style = token.getStyle();

		if (realizer == null)
		{
			realizer = DEFAULT_REALIZER;
		}

		if (!Util.isEqual(style.getFeatureShapeRealizer(), realizer))
		{
			style.setFeatureShapeRealizer(realizer);

			this.styleController.notifyFullRebuildRequired();
		}
	}

	/**
	 * 
	 * @param token
	 *            The associated feature holder style.
	 * @param renderer
	 *            The new effect renderer.
	 */
	public void setShapeEffectRenderer(FeatureHolderStyleToken token, ShapeEffectRenderer renderer)
	{
		FeatureHolderStyle style = token.getStyle();

		if (renderer == null)
		{
			renderer = DEFAULT_RENDERER;
		}

		if (!Util.isEqual(style.getShapeEffectRenderer(), renderer))
		{
			style.setShapeEffectRenderer(renderer);

			this.notifyRebuildRequired();
		}
	}

	/**
	 * 
	 * @param token
	 *            The associated feature holder style.
	 * @param color
	 *            The new set color.
	 */
	public void setColor(FeatureHolderStyleToken token, Paint color)
	{
		FeatureHolderStyle style = token.getStyle();

		if (color == null)
		{
			color = DEFAULT_SET_COLOR;
		}

		if (!Util.isEqual(style.getPaint()[0], color))
		{
			style.setPaint(color);

			this.notifyRebuildRequired();

			// Are the label colors locked?
			if (style.getLockedLabelColors())
			{
				// Notify the labels that they need to be rebuilt:
				this.styleController.getLabelStyleController().notifyRebuildRequired();
			}
		}
	}

	/**
	 * 
	 * @param token
	 *            The associated feature holder style.
	 * @return Whether or not the set contains a property mapper.
	 */
	public boolean hasPropertyMapper(FeatureHolderStyleToken token)
	{
		FeatureHolderStyle style = token.getStyle();
		boolean result;

		if (style.getPropertyStyleMapper() == null)
		{
			result = false;
		}
		else
		{
			result = true;
		}

		return result;
	}

	/**
	 * 
	 * @param token
	 *            The associate slot style.
	 * @param filter
	 *            The filter to be used in the new set.
	 * @return The newly created set.
	 */
	public FeatureHolderStyleToken createSet(SlotStyleToken token, FeatureFilter filter)
	{
		SlotStyle slotStyle = token.getStyle();

		if (filter == null)
		{
			filter = FeatureFilter.none;
		}

		FeatureHolderStyle createdStyle = slotStyle.createFeatureHolderStyle(filter);
		FeatureHolderStyleToken featureHolderStyleToken = new FeatureHolderStyleToken(createdStyle);

		this.applyDefaults(createdStyle);
		initializeSet(createdStyle);

		this.styleController.notifyFullRebuildRequired();

		return featureHolderStyleToken;
	}

	/**
	 * 
	 * @param token
	 *            The associated feature holder style.
	 * @param filter
	 *            The filter to be used in the new set.
	 * @return The newly created set.
	 */
	public FeatureHolderStyleToken createSet(FeatureHolderStyleToken token, FeatureFilter filter)
	{
		FeatureHolderStyle featureHolderStyle = token.getStyle();

		FeatureHolderStyle createdStyle = featureHolderStyle.createFeatureHolderStyle(filter);
		FeatureHolderStyleToken featureHolderStyleToken = new FeatureHolderStyleToken(createdStyle);

		initializeSet(createdStyle);

		this.styleController.notifyFullRebuildRequired();

		return featureHolderStyleToken;
	}

	/**
	 * Initializes the set.
	 * 
	 * @param featureHolderStyle
	 */
	private void initializeSet(FeatureHolderStyle featureHolderStyle)
	{
		featureHolderStyle.setLockedLabelColors(true);
	}

	/**
	 * Applies the defaults to the provided style.
	 * 
	 * @param style
	 */
	private void applyDefaults(FeatureHolderStyle style)
	{
		style.setToolTipExtractor(DEFAULT_EXTRACTOR);
		style.setFeatureShapeRealizer(DEFAULT_REALIZER);
		style.setShapeEffectRenderer(DEFAULT_RENDERER);
		style.setPaint(DEFAULT_SET_COLOR);
	}

	/**
	 * Removes the set from its parent.
	 * 
	 * @param setToken
	 *            The associated set style to remove FROM. The parent.
	 * @param removeToken
	 *            The set style to REMOVE.
	 */
	public void removeSet(FeatureHolderStyleToken setToken, FeatureHolderStyleToken removeToken)
	{
		FeatureHolderStyle setStyle = setToken.getStyle();
		FeatureHolderStyle removedStyle = removeToken.getStyle();

		this.styleController.notifyFullRebuildRequired();

		setStyle.removeSlotItemStyle(removedStyle);
	}

	/**
	 * Generates a default name for the set.
	 * 
	 * @param token
	 *            The set to generate a name for.
	 * @return The generated name.
	 */
	public String generateDefaultName(FeatureHolderStyleToken token)
	{
		FeatureHolderStyle style = token.getStyle();
		FeatureFilter featureFilter = style.getFilter();

		return generateDefaultName(featureFilter);
	}

	/**
	 * Generates a default name based on the feature filter.
	 * 
	 * @param featureFilter
	 *            The feature filter to derive a name from.
	 * @return The generated name.
	 */
	private String generateDefaultName(FeatureFilter featureFilter)
	{
		if (featureFilter == null)
			throw new IllegalArgumentException("FeatureFilter is null.");

		String result = (UNKNOWN_FEATURE);

		// ALL
		if (FeatureFilter.all.equals(featureFilter))
		{
			result = (ALL);
		}
		// AND (Legacy)
		else if (featureFilter instanceof FeatureFilter.And)
		{
			result = ("(" + generateDefaultName(((FeatureFilter.And) featureFilter).getChild1()) + " " + AND + " "
					+ generateDefaultName(((FeatureFilter.And) featureFilter).getChild2()) + ")");
		}
		// AND
		else if (featureFilter instanceof AndFilter)
		{
			List<FeatureFilter> filters = ((AndFilter) featureFilter).getFilters();

			result = "(";

			for (int i = 0; i < filters.size(); i++)
			{
				result += generateDefaultName(filters.get(i));

				// AND?
				if (i < filters.size() - 1)
				{
					result += " " + AND + " ";
				}
			}

			result += ")";
		}
		// ANNOTATION VALUE CONTAINS
		else if (featureFilter instanceof AnnotationValueContains)
		{
			result = ("\"" + ((AnnotationValueContains) featureFilter).getKey() + "\" Contains \""
					+ ((AnnotationValueContains) featureFilter).getValue() + "\"");
		}
		// ANNOTATION VALUE EQUALS
		else if (featureFilter instanceof FeatureFilter.ByAnnotation)
		{
			result = ("\"" + ((FeatureFilter.ByAnnotation) featureFilter).getKey() + "\" Equals \""
					+ ((FeatureFilter.ByAnnotation) featureFilter).getValue() + "\"");
		}
		// BY TYPE
		else if (featureFilter instanceof FeatureFilter.ByType)
		{
			result = ("Type: \"" + ((FeatureFilter.ByType) featureFilter).getType() + "\"");
		}
		// HAS ANNOTATION
		else if (featureFilter instanceof FeatureFilter.HasAnnotation)
		{
			result = (HAS_ANNOTATION + ": \"" + ((FeatureFilter.HasAnnotation) featureFilter).getKey()) + "\"";
		}
		// NOT
		else if (featureFilter instanceof FeatureFilter.Not)
		{
			result = ("(" + NOT + " " + generateDefaultName(((FeatureFilter.Not) featureFilter).getChild()) + ")");
		}
		// OR
		else if (featureFilter instanceof FeatureFilter.Or)
		{
			result = "("
					+ (generateDefaultName(((FeatureFilter.Or) featureFilter).getChild1()) + " " + OR + " "
							+ generateDefaultName(((FeatureFilter.Or) featureFilter).getChild2()) + ")");
		}
		// OVERLAPS LOCATION
		else if (featureFilter instanceof FeatureFilter.OverlapsLocation)
		{
			result = ("[" + ((FeatureFilter.OverlapsLocation) featureFilter).getLocation().getMin() + ", "
					+ ((FeatureFilter.OverlapsLocation) featureFilter).getLocation().getMax() + "]");
		}
		// SEQUENCE NAME
		else if (featureFilter instanceof SequenceNameFilter)
		{
			result = (SEQUENCE_NAME + ": \"" + ((SequenceNameFilter) featureFilter).getSequenceName() + "\"");
		}
		// STRANDED
		else if (featureFilter instanceof FeatureFilter.StrandFilter)
		{
			Strand strand = ((FeatureFilter.StrandFilter) featureFilter).getStrand();
			int strandValue = strand.getValue();

			if (strandValue == StrandedFeature.POSITIVE.getValue())
			{
				result = (POSITIVE);
			}
			else if (strandValue == StrandedFeature.NEGATIVE.getValue())
			{
				result = (NEGATIVE);
			}
			else if (strandValue == StrandedFeature.UNKNOWN.getValue())
			{
				result = (UNKNOWN);
			}
		}
		// CGViewSetFeatureFilter
		else if (featureFilter instanceof CGViewSetFeatureFilter)
		{
			result = (CGVIEW_FILTER);
		}
		else
		{
			result = (UNKNOWN_FEATURE);
		}

		return result;
	}

	/**
	 * Generates a short name for the set.
	 * 
	 * @param token
	 *            The set to generate a name for.
	 * @return The generated short name.
	 */
	public String generateShortName(FeatureHolderStyleToken token)
	{
		FeatureHolderStyle style = token.getStyle();
		FeatureFilter featureFilter = style.getFilter();

		return generateShortName(featureFilter);
	}

	/**
	 * Generates a short name based on the feature filter.
	 * 
	 * @param featureFilter
	 *            The feature filter to derive a name from.
	 * @return The generated short name.
	 */
	private String generateShortName(FeatureFilter featureFilter)
	{
		if (featureFilter == null)
			throw new IllegalArgumentException("FeatureFilter is null.");

		String result = (UNKNOWN_FEATURE);

		// ALL
		if (FeatureFilter.all.equals(featureFilter))
		{
			result = (ALL);
		}
		// AND (Legacy)
		else if (featureFilter instanceof FeatureFilter.And)
		{
			result = ("(" + generateShortName(((FeatureFilter.And) featureFilter).getChild1()) + " " + AND + " "
					+ generateShortName(((FeatureFilter.And) featureFilter).getChild2()) + ")");
		}
		// AND
		else if (featureFilter instanceof AndFilter)
		{
			List<FeatureFilter> filters = ((AndFilter) featureFilter).getFilters();

			result = "(";

			for (int i = 0; i < filters.size(); i++)
			{
				result += generateShortName(filters.get(i));

				// AND?
				if (i < filters.size() - 1)
				{
					result += " " + AND + " ";
				}
			}

			result += ")";
		}
		// ANNOTATION VALUE CONTAINS
		else if (featureFilter instanceof AnnotationValueContains)
		{
			result = ("\"" + ((AnnotationValueContains) featureFilter).getValue() + "\" ∈ \""
					+ ((AnnotationValueContains) featureFilter).getKey() + "\"");
		}
		// ANNOTATION VALUE EQUALS
		else if (featureFilter instanceof FeatureFilter.ByAnnotation)
		{
			result = ("\"" + ((FeatureFilter.ByAnnotation) featureFilter).getValue() + "\" = \""
					+ ((FeatureFilter.ByAnnotation) featureFilter).getKey() + "\"");
		}
		// BY TYPE
		else if (featureFilter instanceof FeatureFilter.ByType)
		{
			result = ("\"" + ((FeatureFilter.ByType) featureFilter).getType() + "\"");
		}
		// HAS ANNOTATION
		else if (featureFilter instanceof FeatureFilter.HasAnnotation)
		{
			result = ("\"" + ((FeatureFilter.HasAnnotation) featureFilter).getKey()) + "\"";
		}
		// NOT
		else if (featureFilter instanceof FeatureFilter.Not)
		{
			result = ("(~" + generateShortName(((FeatureFilter.Not) featureFilter).getChild()) + ")");
		}
		// OR
		else if (featureFilter instanceof FeatureFilter.Or)
		{
			result = "("
					+ (generateShortName(((FeatureFilter.Or) featureFilter).getChild1()) + " " + OR + " "
							+ generateShortName(((FeatureFilter.Or) featureFilter).getChild2()) + ")");
		}
		// OVERLAPS LOCATION
		else if (featureFilter instanceof FeatureFilter.OverlapsLocation)
		{
			result = ("[" + ((FeatureFilter.OverlapsLocation) featureFilter).getLocation().getMin() + ", "
					+ ((FeatureFilter.OverlapsLocation) featureFilter).getLocation().getMax() + "]");
		}
		// SEQUENCE NAME
		else if (featureFilter instanceof SequenceNameFilter)
		{
			result = ("\"" + ((SequenceNameFilter) featureFilter).getSequenceName() + "\"");
		}
		// STRANDED
		else if (featureFilter instanceof FeatureFilter.StrandFilter)
		{
			Strand strand = ((FeatureFilter.StrandFilter) featureFilter).getStrand();
			int strandValue = strand.getValue();

			if (strandValue == StrandedFeature.POSITIVE.getValue())
			{
				result = (POSITIVE);
			}
			else if (strandValue == StrandedFeature.NEGATIVE.getValue())
			{
				result = (NEGATIVE);
			}
			else if (strandValue == StrandedFeature.UNKNOWN.getValue())
			{
				result = (UNKNOWN);
			}
		}
		// CGViewSetFeatureFilter
		else if (featureFilter instanceof CGViewSetFeatureFilter)
		{
			result = (CGVIEW_FILTER);
		}
		else
		{
			result = (UNKNOWN_FEATURE);
		}

		return result;
	}

	/**
	 * Performs a AND operation of the feature filter of the first set with the
	 * provided feature filter.
	 * 
	 * That is, (NEW FILTER) = new FeatureFilter.And((OLD SET FILTER), (PROVIDED
	 * FILTER))
	 * 
	 * @param token
	 *            The original set.
	 * @param featureFilter
	 *            The feature filter to refine.
	 */
	public void refine(FeatureHolderStyleToken token, FeatureFilter featureFilter)
	{
		FeatureHolderStyle style = token.getStyle();

		style.appendFeatureHolderStyle(featureFilter);

		this.styleController.notifyFullRebuildRequired();
	}

	/**
	 * 
	 * @param token
	 *            The associated set.
	 * 
	 * @return Whether or not to the label colors are forced to be the same as
	 *         the feature holder style.
	 */
	public boolean getLockedLabelColors(FeatureHolderStyleToken token)
	{
		FeatureHolderStyle style = token.getStyle();

		return style.getLockedLabelColors();
	}

	/**
	 * Whether or not to force the label colors to be the same as the feature
	 * holder style.
	 * 
	 * @param lock
	 *            Whether or not the lock the colors.
	 */
	public void setLockedLabelColors(FeatureHolderStyleToken token, boolean lock)
	{
		FeatureHolderStyle style = token.getStyle();

		if (!Util.isEqual(lock, style.getLockedLabelColors()))
		{
			style.setLockedLabelColors(lock);

			// Notify the label style controller that it needs to rebuild:
			this.styleController.getLabelStyleController().notifyRebuildRequired();
		}
	}

	/**
	 * 
	 * @param token
	 *            The set to inquire.
	 * 
	 * @return The link.
	 */
	public Link getLink(FeatureHolderStyleToken token)
	{
		FeatureHolderStyle FHS = token.getStyle();

		return FHS.getLink();
	}

	/**
	 * 
	 * @param token
	 *            The set to modify.
	 * @param link
	 *            The link to apply.
	 */
	public void setLink(FeatureHolderStyleToken token, Link link)
	{
		FeatureHolderStyle FHS = token.getStyle();

		FHS.setLink(link);
	}
}
