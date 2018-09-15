package ca.corefacility.gview.style.io.gss;


import org.biojava.bio.seq.FeatureFilter;
import org.w3c.css.sac.LexicalUnit;

import ca.corefacility.gview.style.io.gss.featureFilterCoder.AllCoder;
import ca.corefacility.gview.style.io.gss.featureFilterCoder.AndCoder;
import ca.corefacility.gview.style.io.gss.featureFilterCoder.AnnotationValueContainsCoder;
import ca.corefacility.gview.style.io.gss.featureFilterCoder.AnnotationValueEqualsCoder;
import ca.corefacility.gview.style.io.gss.featureFilterCoder.ByTypeCoder;
import ca.corefacility.gview.style.io.gss.featureFilterCoder.FeatureFilterCoder;
import ca.corefacility.gview.style.io.gss.featureFilterCoder.FeatureFilterCoderMap;
import ca.corefacility.gview.style.io.gss.featureFilterCoder.HasAnnotationCoder;
import ca.corefacility.gview.style.io.gss.featureFilterCoder.NotCoder;
import ca.corefacility.gview.style.io.gss.featureFilterCoder.OrCoder;
import ca.corefacility.gview.style.io.gss.featureFilterCoder.OverlapsLocationCoder;
import ca.corefacility.gview.style.io.gss.featureFilterCoder.SequenceNameCoder;
import ca.corefacility.gview.style.io.gss.featureFilterCoder.StrandedCoder;
import ca.corefacility.gview.utils.AndFilter;
import ca.corefacility.gview.utils.AnnotationValueContains;
import ca.corefacility.gview.utils.SequenceNameFilter;

public class FeatureFilterHandler
{
	private static final String FEATURE_FILTER_FUNCTION = "feature-filter";
	
	private static FeatureFilterCoderMap filterCoderMap;
	
	static
	{
		filterCoderMap = new FeatureFilterCoderMap();
		
		filterCoderMap.addFilterCoder(AndFilter.class, new AndCoder());
		filterCoderMap.addFilterCoder(FeatureFilter.And.class, new AndCoder());
		filterCoderMap.addFilterCoder(FeatureFilter.StrandFilter.class, new StrandedCoder());
		filterCoderMap.addFilterCoder(FeatureFilter.ByType.class, new ByTypeCoder());
		filterCoderMap.addFilterCoder(FeatureFilter.HasAnnotation.class, new HasAnnotationCoder());
		filterCoderMap.addFilterCoder(AnnotationValueContains.class, new AnnotationValueContainsCoder());
		filterCoderMap.addFilterCoder(FeatureFilter.OverlapsLocation.class, new OverlapsLocationCoder());
		filterCoderMap.addFilterCoder(FeatureFilter.Not.class, new NotCoder());
		filterCoderMap.addFilterCoder(FeatureFilter.Or.class, new OrCoder());
		filterCoderMap.addFilterCoder(FeatureFilter.all.getClass(), new AllCoder());
		filterCoderMap.addFilterCoder(FeatureFilter.ByAnnotation.class, new AnnotationValueEqualsCoder());
		filterCoderMap.addFilterCoder(SequenceNameFilter.class, new SequenceNameCoder());
	}
	
	/**
	 * Registers the passed class to be encoded/decoded by the passed coder.
	 * @param filterClass
	 * @param coder
	 */
	public static void registerFilterCoder(Class<? extends FeatureFilter> filterClass, FeatureFilterCoder coder)
	{
		if (coder == null)
		{
			throw new IllegalArgumentException("coder is null");
		}
		
		filterCoderMap.addFilterCoder(filterClass, coder);
	}
	
	public static String encode(FeatureFilter filter)
	{
		return FEATURE_FILTER_FUNCTION + "(" + filterCoderMap.encode(filter) + ")";
	}
	
	public static FeatureFilter decode(LexicalUnit featureFilter) throws NullPointerException, IllegalArgumentException
	{
		if (featureFilter == null)
		{
			throw new NullPointerException("featureFilter is null");
		}

		FeatureFilter featureFilterObj = null;
		
		if (featureFilter.getLexicalUnitType() == LexicalUnit.SAC_FUNCTION)
		{
			LexicalUnit parameters = featureFilter.getParameters();
			featureFilterObj = filterCoderMap.decode(parameters);
		}
		else if (featureFilter.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE)
		{
			featureFilterObj = filterCoderMap.decode(featureFilter);
		}
		else
		{
			throw new IllegalArgumentException("featureFilter not of type SAC_FUNCTION");
		}
		
		return featureFilterObj;
	}
}
