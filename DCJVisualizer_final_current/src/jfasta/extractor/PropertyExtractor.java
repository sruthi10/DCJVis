
package jfasta.extractor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jfasta.model.DBFastaSequence;

/**
 *
 * @author rxm156430
 */
public class PropertyExtractor {
    
    DBFastaSequence element;
    Pattern namePattern;
    Pattern locPattern;
    Pattern locValuePattern1;
    Pattern locValuePattern2;
    
   public PropertyExtractor()
    {
        
        String nameRegx = "\\W\\bgene\\b\\W";
        String locRegx = "\\W\\blocation\\b.*";
        String locValRegx1 = "(\\(|\\=)(\\d+)";
        String locValRegx2 = "(\\d+)(\\)|\\])";
        locPattern = Pattern.compile(locRegx,Pattern.CASE_INSENSITIVE );
        namePattern = Pattern.compile(nameRegx);
        locValuePattern1 = Pattern.compile(locValRegx1);
        locValuePattern2 = Pattern.compile(locValRegx2);
    }
    
    public void SetFastaSequence(DBFastaSequence element)
    {
        
        this.element = element; 
    }
    public String getName()
    {
        String header = element.getHeader();
        String[] parts = header.split(" ");
        String gene = parts[1];
        String[] genepart = gene.split("=");
        String genename = genepart[1];
        String[] finalpart = genename.split("]");
        String name = finalpart[0];
        System.out.println(name);
        return name;
    }
    public boolean isComplement()
    {
        return element.getHeader().contains("complement");
    }
    public int[] getLocation()
    {
        String header = element.getHeader();
        int[] location = new int[2];
        Matcher m = locPattern.matcher(header);
        if(m.find())
        {
            System.out.println(header.substring(m.start(),m.end()));
            String locationAttr = header.substring(m.start(),m.end());
            m = locValuePattern1.matcher(locationAttr);
            if(m.find())
            {
               System.out.println(m.group(2));
               location[0] = Integer.parseInt(m.group(2));
               m= locValuePattern2.matcher(locationAttr);
               if(m.find())
               {
                   System.out.println(m.group(1));
                    location[1] = Integer.parseInt(m.group(1));
                    
               }
            }
        }
        return location;
    }
    
}
