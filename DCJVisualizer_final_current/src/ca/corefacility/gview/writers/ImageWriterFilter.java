package ca.corefacility.gview.writers;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public abstract class ImageWriterFilter extends FileFilter
{
    /**
     * Determines the file type (by extension) this filter handles.
     * @param extension
     * @return  True if the passed extension is handled, false otherwise.
     */
    public abstract boolean handlesType(String extension);
    
    protected abstract String getDefaultExtension();
    
    @Override
    public boolean accept(File f)
    {
        if (f.isDirectory())
        {
            return true;
        }
        
        String extension = extractExtension(f);
        return handlesType(extension);
    }
    
    public static class JPG extends ImageWriterFilter
    {
        @Override
        public boolean handlesType(String extension)
        {
            return extension != null && 
            (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")); 
        }
    
    	@Override
    	public String getDescription()
    	{
    		return "JPEG Image";
    	}

        @Override
        protected String getDefaultExtension()
        {
            return "jpg";
        }
    }
    
    public static class PNG extends ImageWriterFilter
    {
        @Override
        public boolean handlesType(String extension)
        {
            return extension != null && extension.equalsIgnoreCase("png"); 
        }
    
        @Override
        public String getDescription()
        {
            return "PNG Image";
        }

        @Override
        protected String getDefaultExtension()
        {
            return "png";
        }
    }
    
    public static class SVG extends ImageWriterFilter
    {
        @Override
        public boolean handlesType(String extension)
        {
            return extension != null && extension.equalsIgnoreCase("svg"); 
        }
    
        @Override
        public String getDescription()
        {
            return "SVG Image";
        }

        @Override
        protected String getDefaultExtension()
        {
            return "svg";
        }
    }
    
    public static class SVGZ extends ImageWriterFilter
    {
        @Override
        public boolean handlesType(String extension)
        {
            return extension != null && extension.equalsIgnoreCase("svgz"); 
        }
    
        @Override
        public String getDescription()
        {
            return "SVGZ Image";
        }

        @Override
        protected String getDefaultExtension()
        {
            return "svgz";
        }
    }
	
	public static String extractExtension(File file)
	{
		return extractExtension(file.getName());
	}
	
	public static String extractExtension(String filename)
	{
		String extension = null;

		if (filename != null)
		{
			int periodIndex = filename.lastIndexOf('.');

			// if period is found, and it is not the last character
			if ((periodIndex != -1) && (periodIndex + 1) < filename.length())
			{
				extension = filename.substring(periodIndex + 1);
			}
		}

		return extension;
	}

	/**
	 * Given a File, determines if the extension matches the specific instance of this file filter.
	 *   If not, returns the proper file name (with the correct extension).
	 *   
	 * @param saveFile
	 * @return  If extension is not proper, appends on extension to file name and returns, 
	 *     otherwise returns filename (or null if saveFile is null).
	 */
    public String getProperFileName(File saveFile)
    {
        String properName = null;
        
        if (saveFile != null)
        {
            properName = saveFile.getName();
            
            String extension = extractExtension(saveFile);
            
            if (!handlesType(extension))
            {
                String fileName = saveFile.getName();
                
                if (fileName != null)
                {
                    properName = fileName + "." + getDefaultExtension();
                }
            }
        }
        
        return properName;
    }
}
