package ca.corefacility.gview.style.datastyle;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.biojava.bio.symbol.RangeLocation;

import ca.corefacility.gview.layout.RangeLocationComparator;
import ca.corefacility.gview.layout.plot.PlotBuilder;
import ca.corefacility.gview.layout.plot.PlotBuilderAnnotation;
import ca.corefacility.gview.layout.plot.PlotBuilderGCContent;
import ca.corefacility.gview.layout.plot.PlotBuilderGCSkew;
import ca.corefacility.gview.layout.plot.PlotBuilderPoints;
import ca.corefacility.gview.layout.plot.PlotBuilderRange;
import ca.corefacility.gview.layout.plot.FeatureFilterMappable;

/**
 * Used to generate a specific type of plot builder
 * @author aaron
 *
 */
public abstract class PlotBuilderType
{
    protected URI uri = null;
    protected boolean autoscale = false;
    protected int min = -1;
    protected int max = 1;
    
    public abstract PlotBuilder createPlotBuilder();
    
    public void setScale(int min, int max)
    {
        this.min = min;
        this.max = max;
    }
    
    /**
     * 
     * @return The minimum value of the plot.
     */
    public int getMinimum()
    {
    	return this.min;
    }
    
    /**
     * 
     * @return The maximum value of the plot.
     */
    public int getMaximum()
    {
    	return this.max;
    }
    
    /**
     * 
     * @return Whether or not autoscaling of the plot is enabled.
     */
    public boolean getAutoScale()
    {
    	return this.autoscale;
    }
    
    /**
     * Turns on/off autoscaling.
     * @param autoscale  True if autoscaling should be performed, false otherwise.
     */
    public void autoScale(boolean autoscale)
    {
        this.autoscale = autoscale;
    }
    
    /**
     * Sets the URI where this plot builder got its information (if any).
     * @param uri  The uri to set.
     */
    public void setURI(URI uri)
    {
        this.uri = uri;
    }

    /**
     * Gets the URI where this plot builder got its data.
     * @return  The URI, or null if unknown.
     */
    public URI getURI()
    {
        return uri;
    }
    
    public static class Annotation extends PlotBuilderType
    {
        private Map<FeatureFilterMappable, Double> annotationsMap;
        
        public Annotation()
        {
            annotationsMap = new HashMap<FeatureFilterMappable, Double>();
        }
        public PlotBuilder createPlotBuilder()
        {
            PlotBuilder plotBuilder = new PlotBuilderAnnotation(annotationsMap);
            if (autoscale)
            {
                plotBuilder.autoScale();
            }
            else
            {
                plotBuilder.scale(min,max);
            }
            
            return plotBuilder;
        }
        
        public void addAnnotationValue(String annotationType, String annotationValue, double dataValue)
        {
            if (annotationType == null)
                throw new IllegalArgumentException("annotationType cannot be null");

            if (annotationValue == null)
                throw new IllegalArgumentException("annotationValue cannot be null");

            // used to filter out features with the passed annotation/annotation value pair
            FeatureFilterMappable filter = new FeatureFilterMappable(annotationType, annotationValue);

            this.annotationsMap.put(filter, dataValue);
        }
        
        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = super.hashCode();
            result = prime
                    * result
                    + ((annotationsMap == null) ? 0 : annotationsMap.hashCode());
            return result;
        }
        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (getClass() != obj.getClass())
                return false;
            Annotation other = (Annotation) obj;
            if (annotationsMap == null)
            {
                if (other.annotationsMap != null)
                    return false;
            } else if (!annotationsMap.equals(other.annotationsMap))
                return false;
            return true;
        }
    }
    
    public static class GCContent extends PlotBuilderType
    {
    	private int windowSize = -1;
    	private int stepSize = -1;
    	
        public GCContent(){}
        
        public GCContent(int windowSize, int stepSize)
        {
        	if (windowSize <= 0)
        	{
        		throw new IllegalArgumentException("Error: windowSize="+windowSize + " cannot be <= 0");
        	}
        	
        	if (stepSize <= 0)
        	{
        		throw new IllegalArgumentException("Error: stepSize="+stepSize + " cannot be <= 0");
        	}
        	
        	this.windowSize = windowSize;
        	this.stepSize = stepSize;
        }
        
        public PlotBuilder createPlotBuilder()
        {
            PlotBuilder plotBuilder;
            
            if (windowSize > 0 && stepSize > 0)
            {
            	plotBuilder = new PlotBuilderGCContent(windowSize,stepSize);
            }
            else
            {
            	plotBuilder = new PlotBuilderGCContent();
            }
            
            if (autoscale)
            {
                plotBuilder.autoScale();
            }
            else
            {
                plotBuilder.scale(min,max);
            }
            
            return plotBuilder;
        }
        
        @Override
        public int hashCode()
        {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (getClass() != obj.getClass())
                return false;
            return true;
        }
    }
    
    public static class GCSkew extends PlotBuilderType
    {
    	private int windowSize = -1;
    	private int stepSize = -1;
    	
        public GCSkew(){}
        
        public GCSkew(int windowSize, int stepSize)
        {
        	if (windowSize <= 0)
        	{
        		throw new IllegalArgumentException("Error: windowSize="+windowSize + " cannot be <= 0");
        	}
        	
        	if (stepSize <= 0)
        	{
        		throw new IllegalArgumentException("Error: stepSize="+stepSize + " cannot be <= 0");
        	}
        	
        	this.windowSize = windowSize;
        	this.stepSize = stepSize;
		}
        
		public PlotBuilder createPlotBuilder()
        {
			PlotBuilder plotBuilder;
			
            if (windowSize > 0 && stepSize > 0)
            {
            	plotBuilder = new PlotBuilderGCSkew(windowSize,stepSize);
            }
            else
            {
            	plotBuilder = new PlotBuilderGCSkew();
            }
            
            if (autoscale)
            {
                plotBuilder.autoScale();
            }
            else
            {
                plotBuilder.scale(min, max);
            }
            
            return plotBuilder;
        }
        
        @Override
        public int hashCode()
        {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (getClass() != obj.getClass())
                return false;
            return true;
        }
    }
    
    public static class Points extends PlotBuilderType
    {
        private SortedMap<Integer, Double> points;
        private boolean autoscale = false;
        
        public Points()
        {
            points = new TreeMap<Integer, Double>();
        }
        
        public PlotBuilder createPlotBuilder()
        {
            PlotBuilder plotBuilder = new PlotBuilderPoints(points,uri);
            if (autoscale)
            {
                plotBuilder.autoScale();
            }
            else
            {
                plotBuilder.scale(min,max);
            }
            
            return plotBuilder;
        }

        public void addPoint(int point, double value)
        {
            if (point < 0)
                throw new IllegalArgumentException("base point cannot be null");

            this.points.put(point, value);
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + (autoscale ? 1231 : 1237);
            result = prime * result
                    + ((points == null) ? 0 : points.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (getClass() != obj.getClass())
                return false;
            Points other = (Points) obj;
            if (autoscale != other.autoscale)
                return false;
            if (points == null)
            {
                if (other.points != null)
                    return false;
            } else if (!points.equals(other.points))
                return false;
            return true;
        }
    }
    
    public static class Range extends PlotBuilderType
    {
        private SortedMap<RangeLocation, Double> ranges;
        
        public Range()
        {
            this.ranges = new TreeMap<RangeLocation, Double>(new RangeLocationComparator());
        }
        
        public PlotBuilder createPlotBuilder()
        {
            PlotBuilder plotBuilder = new PlotBuilderRange(ranges,uri);
            if (autoscale)
            {
                plotBuilder.autoScale();
            }
            else
            {
                plotBuilder.scale(min,max);
            }
            
            return plotBuilder;
        }

        /**
         * Adds a range to this plot.
         * @param startBase
         * @param endBase
         * @param height
         */
        public void addRange(int startBase, int endBase, double height)
        {
            this.ranges.put(new RangeLocation(startBase, endBase), height);
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result
                    + ((ranges == null) ? 0 : ranges.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (!super.equals(obj))
                return false;
            if (getClass() != obj.getClass())
                return false;
            Range other = (Range) obj;
            if (ranges == null)
            {
                if (other.ranges != null)
                    return false;
            } else if (!ranges.equals(other.ranges))
                return false;
            return true;
        }
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (autoscale ? 1231 : 1237);
        result = prime * result + max;
        result = prime * result + min;
        result = prime * result + ((uri == null) ? 0 : uri.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PlotBuilderType other = (PlotBuilderType) obj;
        if (autoscale != other.autoscale)
            return false;
        if (max != other.max)
            return false;
        if (min != other.min)
            return false;
        if (uri == null)
        {
            if (other.uri != null)
                return false;
        } else if (!uri.equals(other.uri))
            return false;
        return true;
    }
}
