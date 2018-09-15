package ca.corefacility.gview.style.datastyle;

import ca.corefacility.gview.layout.plot.PlotDrawer;
import ca.corefacility.gview.layout.plot.PlotDrawerBar;
import ca.corefacility.gview.layout.plot.PlotDrawerCenter;
import ca.corefacility.gview.layout.plot.PlotDrawerLine;
import ca.corefacility.gview.layout.plot.PlotDrawerSolid;

public abstract class PlotDrawerType
{
    public abstract PlotDrawer createPlotDrawer();
    
    public static class Bar extends PlotDrawerType
    {
        public Bar(){}
        public PlotDrawer createPlotDrawer(){return new PlotDrawerBar();}
        
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
    
    public static class Line extends PlotDrawerType
    {
        public Line(){}
        public PlotDrawer createPlotDrawer(){return new PlotDrawerLine();}
        
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
    
    public static class Solid extends PlotDrawerType
    {
        public Solid(){}
        public PlotDrawer createPlotDrawer(){return new PlotDrawerSolid();}
        
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
    
    public static class Center extends PlotDrawerType
    {
        public Center(){}        
        public PlotDrawer createPlotDrawer(){return new PlotDrawerCenter();}
        
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
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return true;
    }
}
