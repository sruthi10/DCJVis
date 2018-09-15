package ca.corefacility.gview.style.io.gss.coders;


import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.Stack;

import org.biojava.bio.seq.FeatureFilter;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Selector;

import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.style.datastyle.DataStyle;
import ca.corefacility.gview.style.datastyle.FeatureHolderStyle;
import ca.corefacility.gview.style.datastyle.LabelStyle;
import ca.corefacility.gview.style.datastyle.PlotStyle;
import ca.corefacility.gview.style.datastyle.SlotItemStyle;
import ca.corefacility.gview.style.datastyle.SlotStyle;
import ca.corefacility.gview.style.datastyle.StyleHolder;
import ca.corefacility.gview.style.io.FeatureSetMap;
import ca.corefacility.gview.style.io.gss.LinkMapper;
import ca.corefacility.gview.style.io.gss.exceptions.MalformedSelectorException;
import ca.corefacility.gview.style.io.gss.exceptions.NoStyleExistsException;
import ca.corefacility.gview.style.io.gss.exceptions.NoSuchFilterException;
import ca.corefacility.gview.style.io.gss.exceptions.ParseException;

public class DescendentCoder extends AbstractDescendentCoder
{
	private final FeatureSetCoder featureSetCoder;
	private final LabelCoder labelCoder = new LabelCoder();
	private final PlotCoder plotCoder = new PlotCoder();
	
	public DescendentCoder(LinkMapper linkMapper)
	{
		this.featureSetCoder = new FeatureSetCoder(linkMapper);
	}

	public void decodeProperty(DescendantSelector selector, MapStyle mapStyle, FeatureSetMap filtersMap, String name, LexicalUnit value, URI sourceURI) throws ParseException, IOException
	{
		String selectorType = getSelectorType(selector);

		if (selectorType.equals(this.labelCoder.getSelectorName()))
		{
			// get particular labelStyle to decode into
			LabelStyle labelStyle = extractLabelStyle(selector, mapStyle.getDataStyle(), filtersMap);
			this.labelCoder.decodeProperty(labelStyle, name, value, sourceURI);
		}
		else if (selectorType.equals(this.featureSetCoder.getSelectorName()))
		{
			FeatureHolderStyle holderStyle = extractFeatureHolderStyle((DescendantSelector)selector, mapStyle.getDataStyle(), filtersMap);

			this.featureSetCoder.decodeProperty(holderStyle, name, value, sourceURI);
		}
		else if (selectorType.equals(this.plotCoder.getSelectorName()))
		{
			PlotStyle plotStyle = extractPlotStyle(selector, mapStyle.getDataStyle());

			this.plotCoder.decodeProperty(plotStyle, name, value, sourceURI);
		}
	}

	private PlotStyle extractPlotStyle(DescendantSelector selector,
			DataStyle dataStyle) throws NoStyleExistsException, MalformedSelectorException
			{
		PlotStyle plotStyle = null;

		Stack<Selector> ancestors = getAncestorSelectors(selector);

		if (ancestors != null && !ancestors.isEmpty())
		{
			Selector currSelector = ancestors.pop();

			if (currSelector instanceof ConditionalSelector)
			{
				ConditionalSelector currSelectorCond = (ConditionalSelector)currSelector;

				SlotStyle slotStyle = SlotCoder.extractSlotStyle(currSelectorCond, dataStyle); // first selector defines the slot style we are located in
				// should be at slot selector
				if (slotStyle == null)
					// no slot style created yet, so throw exception
					throw new NoStyleExistsException("slot belonging to selector " + selector + " does not exist");

				currSelector = ancestors.pop();

				if (currSelector instanceof ElementSelector)
				{
					ElementSelector currElemSelector = (ElementSelector)currSelector;

					if (this.plotCoder.getSelectorName().equals(currElemSelector.getLocalName()))
					{
						// search for correct plot style in slot style
						Iterator<SlotItemStyle> iterator = slotStyle.styles();
						SlotItemStyle currSlotItemStyle;
						
						while(iterator.hasNext() && plotStyle == null)
						{
							currSlotItemStyle = iterator.next();
							
							if(currSlotItemStyle instanceof PlotStyle)
							{
								plotStyle = (PlotStyle)currSlotItemStyle;
							}
						}
						
						if(plotStyle == null)
						{
							plotStyle = new PlotStyle();
							slotStyle.addStyleItem(plotStyle);
						}

					}
					else
						throw new MalformedSelectorException("2nd selector in \"" + selector + "\" is not a PlotSelector");
				}
				else
					throw new MalformedSelectorException("2nd selector in \"" + selector + "\" is not an ElementSelector");
			}
			else
				throw new MalformedSelectorException("top selector in \"" + selector + "\" is not a ConditionalSelector");
		}
		else
			throw new MalformedSelectorException("selectors in \"" + selector + "\" are incorrect");

		return plotStyle;
			}

	/**
	 * Combines together a style holder, and a FeatureFilter
	 * Used so we can search for/return a pair of these values in a method.
	 * @author aaron
	 *
	 */
	private class StyleHolderFilter
	{
		private StyleHolder styleHolder;
		private FeatureFilter featureFilter;

		public StyleHolderFilter(StyleHolder styleHolder,
				FeatureFilter featureFilter)
		{
			super();
			this.styleHolder = styleHolder;
			this.featureFilter = featureFilter;
		}

		public StyleHolder getStyleHolder()
		{
			return this.styleHolder;
		}
		public FeatureFilter getFeatureFilter()
		{
			return this.featureFilter;
		}
	}

	// extracts FeatureHolderStyle from the passed contional selector
	private FeatureHolderStyle getFeatureHolderStyleFrom(ConditionalSelector selector, StyleHolder styleHolder, FeatureSetMap filtersMap)
	{
		if (selector == null)
			throw new NullPointerException("selector is null");

		if (styleHolder == null)
			throw new NullPointerException("styleHolder is null");

		FeatureHolderStyle holderStyle = null;

		FeatureFilter filter = FeatureSetCoder.extractFeatureFilter(selector, filtersMap);

		if (filter != null)
		{
			holderStyle = styleHolder.getFeatureHolderStyle(filter);
		}

		return holderStyle;
	}

	/**
	 * Extracts the FeatureHolderStyle to decode into, given the passed Descendent Selector
	 * @param baseSelector
	 * @param mapStyle
	 * @return  The FeatureHolderStyle to decode into.
	 * @throws NoStyleExistsException
	 * @throws NoSuchFilterException
	 */
	private FeatureHolderStyle extractFeatureHolderStyle(DescendantSelector baseSelector, DataStyle dataStyle, FeatureSetMap filtersMap) throws ParseException
	{
		StyleHolderFilter currStyleFilter = getParentHolderStyleFilter(baseSelector, dataStyle, filtersMap);

		StyleHolder parentHolder = currStyleFilter.getStyleHolder();

		FeatureHolderStyle holderStyle = parentHolder.getFeatureHolderStyle(currStyleFilter.getFeatureFilter());

		if (holderStyle == null)
			throw new NoStyleExistsException("no FeatreHolderStyle exists defined by " + baseSelector);
		else
			return holderStyle;
	}

	private LabelStyle extractLabelStyle(DescendantSelector baseSelector, DataStyle dataStyle, FeatureSetMap filtersMap) throws ParseException
	{
		LabelStyle labelStyle = null;

		if (baseSelector == null)
			throw new NullPointerException("baseSelector is null");

		if (dataStyle == null)
			throw new NullPointerException("dataStyle is null");

		// extract the very first ancestor in this chain of selectors
		// this should be the selector for the particular slot this FeatureSet belongs to
		StyleHolder currHolderStyle = null;
		Stack<Selector> ancestors = getAncestorSelectors(baseSelector);

		if (!ancestors.isEmpty())
		{
			Selector currSelector = ancestors.pop();

			ConditionalSelector currSelectorCond = null;
			ConditionalSelector prevSelectorCond = null;

			if (currSelector instanceof ConditionalSelector)
			{
				currSelectorCond = (ConditionalSelector)currSelector;

				currHolderStyle = SlotCoder.extractSlotStyle(currSelectorCond, dataStyle); // first selector defines the slot style we are located in
				// should be at slot selector
				if (currHolderStyle == null)
					// no slot style created yet, so throw exception
					throw new NoStyleExistsException("slot belonging to selector " + baseSelector + " does not exist");
				else
				{
					while (!ancestors.isEmpty())
					{
						prevSelectorCond = currSelectorCond;
						currSelector = ancestors.pop();

						if (currSelector instanceof ConditionalSelector)
						{
							currSelectorCond = (ConditionalSelector)currSelector;

							if (currHolderStyle == null)
								throw new NoStyleExistsException("featureHolderStyle under " + prevSelectorCond + " with filter " +
										FeatureSetCoder.extractFeatureFilter(prevSelectorCond, filtersMap) + " does not exist");

							currHolderStyle = getFeatureHolderStyleFrom(currSelectorCond, currHolderStyle, filtersMap);
						}
						else // hit a non-conditional selector (label selector)
						{

						}
					}

					// currHolderStyle now contains the holder style we wish to look at

					if (currHolderStyle == null)
						throw new NoStyleExistsException("currHolderStyle is null");
					else if (currHolderStyle instanceof SlotStyle) // TODO should I perform casting here, or have "getLabelStyle" in StyleHolder
					{
						labelStyle = ((SlotStyle)currHolderStyle).getLabelStyle();
					}
					else if (currHolderStyle instanceof FeatureHolderStyle)
					{
						labelStyle = ((FeatureHolderStyle)currHolderStyle).getLabelStyle();
					}
				}
			}
		}

		return labelStyle;
	}

	/**
	 * Search for and returns the parent HolderStyle, and the FeatureFilter under this parent.
	 *   For example, if we have a selector that looks like "slot#1 FeatureSet#set0 FeatureSet#set1", then
	 *   this method will return the FeatureHolderStyle for "FeatureSet#set0", and the FeatureFilter for "set1".
	 *   This is used so that we can check for the existence of a FeatureHolderStyle (with filter "set1" for example)
	 *    under the parent FeatureHolderStyle (for example "FeatureSet#set0"), and create it if necessary.
	 * @param selector
	 * @param dataStyle
	 * @return
	 * // TODO may went to change to extract up to null FeatureHolderStyle
	 */
	private StyleHolderFilter getParentHolderStyleFilter(DescendantSelector selector, DataStyle dataStyle, FeatureSetMap filtersMap) throws ParseException
	{
		if (selector == null)
			throw new NullPointerException("baseSelector is null");

		if (dataStyle == null)
			throw new NullPointerException("dataStyle is null");

		StyleHolderFilter holderFilter = null;

		// extract the very first ancestor in this chain of selectors
		// this should be the selector for the particular slot this FeatureSet belongs to
		StyleHolder currHolderStyle = null;
		StyleHolder prevHolderStyle = null;
		Stack<Selector> ancestors = getAncestorSelectors(selector);

		if (!ancestors.isEmpty())
		{
			Selector currSelector = ancestors.pop();

			ConditionalSelector currSelectorCond = null;
			ConditionalSelector prevSelectorCond = null;

			if (currSelector instanceof ConditionalSelector)
			{
				currSelectorCond = (ConditionalSelector)currSelector;

				prevHolderStyle = null;
				currHolderStyle = SlotCoder.extractSlotStyle(currSelectorCond, dataStyle); // first selector defines the slot style we are located in
				// should be at slot selector
				if (currHolderStyle == null)
					// no slot style created yet, so throw exception
					throw new NoStyleExistsException("slot belonging to selector " + selector + " does not exist");
				else
				{
					while (!ancestors.isEmpty())
					{
						prevSelectorCond = currSelectorCond;
						currSelector = ancestors.pop();

						if (currSelector instanceof ConditionalSelector)
						{
							currSelectorCond = (ConditionalSelector)currSelector;

							if (currHolderStyle == null)
								throw new NoStyleExistsException("featureHolderStyle under " + prevSelectorCond + " with filter " +
										FeatureSetCoder.extractFeatureFilter(prevSelectorCond, filtersMap) + " does not exist");

							prevHolderStyle = currHolderStyle;
							currHolderStyle = getFeatureHolderStyleFrom(currSelectorCond, currHolderStyle, filtersMap);
						}
						else // hit a non-conditional selector (label selector)
						{

						}
					}

					// prevSelector is now the parent of the very bottom selector
					// prevHolderStyle is the parent holder style of the very bottom selector

					// extract FeatureFilter from currSelector
					FeatureFilter currFilter = FeatureSetCoder.extractFeatureFilter(currSelectorCond, filtersMap);

					if (currFilter == null)
						throw new NoSuchFilterException("feature set corresponding to \"" + currSelectorCond + "\" not found");

					holderFilter = new StyleHolderFilter(prevHolderStyle, currFilter);
				}
			}
		}

		return holderFilter;
	}

	public void startSelector(DescendantSelector selector, MapStyle mapStyle,
			FeatureSetMap filtersMap) throws ParseException
	{
		String type = getSelectorType(selector);

		if (type.equals(this.featureSetCoder.getSelectorName()))
		{
			StyleHolderFilter holderFilter = getParentHolderStyleFilter(selector, mapStyle.getDataStyle(), filtersMap);

			if (holderFilter != null)
			{
				this.featureSetCoder.startSelector(holderFilter.getStyleHolder(), holderFilter.getFeatureFilter());
			}
		}
		else if (type.equals(this.labelCoder.getSelectorName()))
		{
			this.labelCoder.startSelector(selector, mapStyle.getDataStyle());
		}
	}
}
