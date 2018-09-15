package ca.corefacility.gview.style.io.gss;

import org.w3c.css.sac.LexicalUnit;

import ca.corefacility.gview.style.io.gss.exceptions.ParseException;
import ca.corefacility.gview.style.items.LabelLocation;

public class LabelLocationHandler
{
    private static final String NONE = "none";
    private static final String ABOVE = "above";
    private static final String BELOW = "below";
    private static final String BOTH = "all";
    
    public static String encode(LabelLocation location)
    {
        String encoded = "";
        
        if (LabelLocation.BOTH.equals(location))
        {
            encoded += "\"" + BOTH + "\"";
        }
        else if (LabelLocation.ABOVE_BACKBONE.equals(location))
        {
            encoded += "\"" + ABOVE + "\"";
        }
        else if (LabelLocation.BELOW_BACKBONE.equals(location))
        {
            encoded += "\"" + BELOW + "\"";
        }
        else
        {
            encoded += "\"" + NONE + "\"";
        }
        
        return encoded;
    }
    
    public static LabelLocation decode(LexicalUnit currUnit) throws ParseException
    {
        if (currUnit == null)
        {
            throw new NullPointerException("currUnit is null");
        }

        LabelLocation location = LabelLocation.NONE;
        
        if (currUnit.getLexicalUnitType() == LexicalUnit.SAC_FUNCTION ||
                currUnit.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE)
        {
            String name = currUnit.getStringValue();
            if (NONE.equals(name))
            {
                location = LabelLocation.NONE;
            }
            else if (ABOVE.equals(name))
            {
                location = LabelLocation.ABOVE_BACKBONE;
            }
            else if (BELOW.equals(name))
            {
                location = LabelLocation.BELOW_BACKBONE;
            }
            else if (BOTH.equals(name))
            {
                location = LabelLocation.BOTH;
            }
            else
            {
                throw new ParseException("Invalid label location \"" + name + "\"");
            }
        }
        else
        {
            throw new ParseException("label location invalid, not of type string");
        }
        
        return location;
    }
}
