package ca.corefacility.gview.writers;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.zip.GZIPOutputStream;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import ca.corefacility.gview.map.GViewMap;
import ca.corefacility.gview.utils.GViewInformation;
import ca.corefacility.gview.utils.SVGGraphics2DCustom;

public class ImageWriterSVG extends ImageWriter
{
	private final String acceptedFormat;
	
	/**
	 * Creates a new SVG image writer
	 * @param format  The format to use (svg or svgz)
	 */
	public ImageWriterSVG(String format)
	{
		if (!("svg".equals(format) || "svgz".equals(format)))
		{
			throw new IllegalArgumentException("format " + format + " is not accepted by this ImageWriter");
		}
		
		acceptedFormat = format;
	}
	
	private void writeToImage(GViewMap gviewMap, File file, boolean writeLegend)
		throws IOException
	{
        if (file == null)
        {
        	throw new IllegalArgumentException("file is null");
        }
        
        if (gviewMap == null)
        {
        	throw new IllegalArgumentException("gview map is null");
        }
        
        System.out.print("Writing image to " + file.getName() + " ... ");
		
	      // Get a DOMImplementation.
        DOMImplementation domImpl =
            GenericDOMImplementation.getDOMImplementation();

        // Create an instance of org.w3c.dom.Document.
        String svgNS = "http://www.w3.org/2000/svg";
        Document document = domImpl.createDocument(svgNS, "svg", null);
        SVGGeneratorContext context = SVGGeneratorContext.createDefault(document);
        
        context.setComment("Generated by GView version " + (GViewInformation.instance().getVersion() != null ? GViewInformation.instance().getVersion() : "unknown"));
        
        boolean textAsShapes = false;

        // Create an instance of the SVG Generator.
        SVGGraphics2D svgGenerator = new SVGGraphics2DCustom(context, textAsShapes);

        if (writeLegend)
        {
        	gviewMap.drawLegend(svgGenerator);
        }
        else
        {
        	gviewMap.draw(svgGenerator);
        }

        // Finally, stream out SVG to the standard output using
        // UTF-8 encoding.
        boolean useCSS = true; // we want to use CSS style attributes
        Writer out;
		try
		{	        
			if (acceptedFormat.equals("svg"))
			{
				out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			}
			else if (acceptedFormat.equals("svgz"))
			{
				out = new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(file)), "UTF-8");
			}
			else
			{
				System.err.println("[error] - format " + acceptedFormat + " not valid");
				return;
			}
			
		    svgGenerator.stream(out, useCSS);
		    out.close();
		    
	        System.out.println("done");
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SVGGraphics2DIOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeToImage(GViewMap gviewMap, File file)
		throws IOException
	{
		writeToImage(gviewMap, file, false);
	}

	public void writeToImage(GViewMap gviewMap, String filename)
			throws IOException
	{	
        writeToImage(gviewMap, new File(filename));
	}
	
	public void writeLegendToImage(GViewMap gviewMap, File file)
	throws IOException
	{
	    writeToImage(gviewMap, file, true);
	}

	public void writeLegendToImage(GViewMap gviewMap, String filename)
			throws IOException
	{
	    if (filename == null)
	    {
	    	throw new IllegalArgumentException("filename is null");
	    }
	    
	    if (gviewMap == null)
	    {
	    	throw new IllegalArgumentException("gview map is null");
	    }
		
	    writeLegendToImage(gviewMap, new File(filename));
	}

	@Override
	public String[] getAcceptedImageFormats()
	{
		return new String[]{acceptedFormat};
	}
}