package ca.corefacility.gview.layout.plot;

import org.biojava.bio.seq.FeatureFilter;

/**
 * Wrapper around FeatureFilter.ByAnnotation which can be stored in a hash map.
 * @author aaron
 *
 */
public class FeatureFilterMappable
{
    private FeatureFilter.ByAnnotation filter;
    
    public FeatureFilterMappable(String annotationType, String annotationValue)
    {
        filter = new FeatureFilter.ByAnnotation(annotationType, annotationValue);
    }
    
    public FeatureFilter getFilter()
    {
        return filter;
    }

    @Override
    public int hashCode()
    {
        String t = (String)filter.getKey();
        String v = (String)filter.getValue();
        
        final int prime = 31;
        int result = 1;
//        result = prime * result + getOuterType().hashCode();
        result = prime * result + ((t == null) ? 0 : t.hashCode());
        result = prime * result + ((v == null) ? 0 : v.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        String t = (String)filter.getKey();
        String v = (String)filter.getValue();
        
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FeatureFilterMappable other = (FeatureFilterMappable) obj;
//        if (!getOuterType().equals(other.getOuterType()))
//            return false;
        if (t == null)
        {
            if (other.filter.getKey() != null)
                return false;
        } else if (!t.equals(other.filter.getKey()))
            return false;
        if (v == null)
        {
            if (other.filter.getValue() != null)
                return false;
        } else if (!v.equals(other.filter.getValue()))
            return false;
        return true;
    }
}
