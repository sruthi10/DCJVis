package ca.corefacility.gview.writers;


import javax.imageio.ImageIO;

import ca.corefacility.gview.map.GViewMap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageWriterJPG extends ImageWriter
{
	private static final String[] acceptedFormats = {"jpg", "jpeg"};
	
	public ImageWriterJPG()
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
        
        BufferedImage buffImage = gviewMap.getImage(BufferedImage.TYPE_INT_RGB);

        ImageIO.write(buffImage, "jpg", file);
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
	    
	    BufferedImage buffImage = gviewMap.getLegendImage(BufferedImage.TYPE_INT_RGB);
	
	    if (buffImage == null)
	    {
	    	System.err.println("[error] - legend image returned was null");
	    }
	    else
	    {
	    	ImageIO.write(buffImage, "jpg", file);
	    }
	    System.out.println("done");
	}

	@Override
	public String[] getAcceptedImageFormats()
	{
		return acceptedFormats;
	}
}
