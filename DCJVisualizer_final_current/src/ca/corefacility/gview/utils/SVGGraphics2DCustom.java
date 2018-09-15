package ca.corefacility.gview.utils;

import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.Shape;

import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;

public class SVGGraphics2DCustom extends SVGGraphics2D
{

	public SVGGraphics2DCustom(SVGGeneratorContext generatorCtx,
			boolean textAsShapes)
	{
		super(generatorCtx, textAsShapes);
	}
	
	@Override
    public void fill(Shape s) {
		Paint p = getPaint();
		if (p instanceof Gradient)
		{
			GradientPaint gp = ((Gradient)p).getGradientPaint(s);
			setPaint(gp);
		}
		
        super.fill(s);
        
		if (p instanceof Gradient)
		{
			setPaint(p);
		}
    }
	
    public void draw(Shape s) {
		Paint p = getPaint();
		if (p instanceof Gradient)
		{
			GradientPaint gp = ((Gradient)p).getGradientPaint(s);
			setPaint(gp);
		}
    	
		super.draw(s);
        
		if (p instanceof Gradient)
		{
			setPaint(p);
		}
    }
}
