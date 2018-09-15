package ca.corefacility.gview.writers;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ca.corefacility.gview.map.GViewMap;

public class ImageWriterPNG extends ImageWriter
{
	private static final String[] acceptedFormats = {"png"};
	
	public ImageWriterPNG()
	{
	}
	
	public void writeToImage(GViewMap gviewMap, String filename)
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
        
        writeToImage(gviewMap, new File(filename));
	}

	public void writeToImage(GViewMap gviewMap, File file) throws IOException
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
        
        BufferedImage buffImage = gviewMap.getImage();

        ImageIO.write(buffImage, "png", file);
        System.out.println("done");
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

	public void writeLegendToImage(GViewMap gviewMap, File file) throws IOException
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
	    
	    BufferedImage buffImage = gviewMap.getLegendImage();
	    
	    if (buffImage == null)
	    {
	    	System.err.println("[error] - legend image returned was null");
	    }
	    else
	    {
	    	ImageIO.write(buffImage, "png", file);
	    }
	    System.out.println("done");
	}
	
	public static boolean acceptsFileFormat(String fileFormat)
	{
		return "png".equalsIgnoreCase(fileFormat);
	}

	@Override
	public String[] getAcceptedImageFormats()
	{
		return acceptedFormats;
	}
}
