package ca.corefacility.gview.map.controllers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.util.Iterator;

import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;
import ca.corefacility.gview.style.datastyle.FeatureHolderStyle;
import ca.corefacility.gview.style.datastyle.LabelStyle;
import ca.corefacility.gview.style.datastyle.SlotItemStyle;
import ca.corefacility.gview.style.datastyle.SlotStyle;
import ca.corefacility.gview.textextractor.BlankExtractor;
import ca.corefacility.gview.textextractor.FeatureTextExtractor;
import ca.corefacility.gview.utils.Util;

/**
 * This class is responsible for controlling access to the label styles.
 * 
 * @author Eric Marinier
 *
 */
public class LabelStyleController extends Controller
{
	public static final Color DEFAULT_FONT_COLOR = StyleEditorUtility.DEFAULT_FONT_COLOR;
	public static final Color DEFAULT_BACKGROUND_COLOR = new Color(255, 255, 255, 0);
	
	public static final Font DEFAULT_FONT = new Font(Font.DIALOG, Font.PLAIN, 12);
	
	private final StyleController styleController;
	
	/**
	 * 
	 * @param styleEditorController
	 */
	public LabelStyleController(StyleController styleEditorController)
	{
		this.styleController = styleEditorController;
	}
	
	/**
	 * 
	 * @param token
	 * @return The text color or the default color if NULL.
	 */
	public Paint getTextColor(LabelStyleToken token)
	{
		LabelStyle style = token.getStyle();
		Paint color = style.getTextPaint();
		
		if(color == null)
		{
			color = DEFAULT_FONT_COLOR;
		}
		
		return color;
	}
	
	/**
	 * 
	 * @param token
	 * @return The background color or the default color if NULL.
	 */
	public Paint getBackgroundColor(LabelStyleToken token)
	{
		LabelStyle style = token.getStyle();
		Paint color = style.getBackgroundPaint();
		
		if(color == null)
		{
			color = DEFAULT_BACKGROUND_COLOR;
		}
		
		return color;
	}
	
	/**
	 * 
	 * @param token
	 * @return The font or the default font if NULL.
	 */
	public Font getFont(LabelStyleToken token)
	{
		LabelStyle style = token.getStyle();
		Font font = style.getFont();
		
		if(font == null)
		{
			font = DEFAULT_FONT;
		}
		
		return font;
	}
	
	/**
	 * 
	 * @param token
	 * @return The label extractor or a BlankExtractor if NULL.
	 */
	public FeatureTextExtractor getLabelExtractor(LabelStyleToken token)
	{
		LabelStyle style = token.getStyle();
		FeatureTextExtractor extractor = style.getLabelExtractor();
		
		if(extractor == null)
		{
			extractor = new BlankExtractor();
		}
		
		return extractor;
	}
	
	/**
	 * 
	 * @param token
	 * @return Whether or not the labels are visible.
	 */
	public boolean getLabelsVisible(LabelStyleToken token)
	{
		LabelStyle style = token.getStyle();
		
		return style.showLabels();
	}
	
	/**
	 * 
	 * @param token
	 * @param color The color of the text.
	 */
	public void setTextColor(LabelStyleToken token, Paint color)
	{
		LabelStyle style = token.getStyle();
		
		if(color == null)
		{
			color = DEFAULT_FONT_COLOR;
		}
		
		if(!Util.isEqual(style.getTextPaint(), color))
		{
			style.setTextPaint(color);
			
			this.notifyRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @param token
	 * @param color The color of the background.
	 */
	public void setBackgroundColor(LabelStyleToken token, Paint color)
	{
		LabelStyle style = token.getStyle();
		
		if(color == null)
		{
			color = DEFAULT_BACKGROUND_COLOR;
		}
		
		if(!Util.isEqual(style.getBackgroundPaint(), color))
		{
			style.setBackgroundPaint(color);
			
			this.notifyRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @param token
	 * param font The label font.
	 */
	public void setFont(LabelStyleToken token, Font font)
	{
		LabelStyle style = token.getStyle();
		
		if(font == null)
		{
			font = DEFAULT_FONT;
		}
		
		if(!Util.isEqual(style.getFont(), font))
		{
			style.setFont(font);
			
			this.styleController.notifyFullRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @param token
	 * @param extractor The label text extractor.
	 */
	public void setLabelExtractor(LabelStyleToken token, FeatureTextExtractor extractor)
	{
		LabelStyle style = token.getStyle();
		
		if(extractor == null)
		{
			extractor = new BlankExtractor();
		}
		
		if(!Util.isEqual(style.getLabelExtractor(), extractor))
		{
			style.setLabelExtractor(extractor);
			
			this.styleController.notifyFullRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @param token
	 * @param visible Whether or not the labels should be visible.
	 */
	public void setLabelsVisible(LabelStyleToken token, boolean visible)
	{
		LabelStyle style = token.getStyle();
		
		if(style.showLabels() != visible)
		{
			style.setShowLabels(visible);
			
			this.styleController.notifyFullRebuildRequired();
		}
	}
	
	/**
	 * 
	 * @param slotStyle
	 * @return The label style for the associated slot style.
	 */
	public LabelStyleToken getLabelStyleToken(SlotStyleToken slotStyle)
	{
		SlotStyle style = slotStyle.getStyle();
		
		LabelStyleToken token = new LabelStyleToken(style.getLabelStyle());
		
		return token;
	}
	
	/**
	 * 
	 * @param featureHolderStyle
	 * @return The label style for the associated feature holder style.
	 */
	public LabelStyleToken getLabelStyleToken(FeatureHolderStyleToken featureHolderStyle)
	{
		FeatureHolderStyle style = featureHolderStyle.getStyle();		
		LabelStyleToken token = new LabelStyleToken(style.getLabelStyle());
		
		return token;
	}
	
	/**
	 * Determines whether or not to set ALL the label locks.
	 * 
	 * This will be determined on an individual basis, and will set the lock only 
	 * in the case where the set color matches the label text color.
	 * 
	 * @param slotStyles
	 */
	public void initializeLabelLocks(Iterator<SlotStyle> slotStyles)
	{
		SlotStyle slotStyle;
		
		Iterator<SlotItemStyle> slotItemStyles;
		SlotItemStyle slotItemStyle;
		
		//Iterate through all slots:
		while(slotStyles.hasNext())
		{
			slotStyle = slotStyles.next();
			
			slotItemStyles = slotStyle.styles();
			
			//Iterate through all sets:
			while(slotItemStyles.hasNext())
			{
				slotItemStyle = slotItemStyles.next();
				
				if(slotItemStyle instanceof FeatureHolderStyle)
				{
					initializeLabelLock((FeatureHolderStyle)slotItemStyle);
				}
			}
		}
	}
	
	/**
	 * Determines whether or not to set ONE label lock.
	 * 
	 * This will set the lock only in the case where the set color matches the label text color.
	 * 
	 * @param setStyle
	 */
	private void initializeLabelLock(FeatureHolderStyle setStyle)
	{
		FeatureHolderStyleToken setToken = new FeatureHolderStyleToken(setStyle);
		LabelStyleToken labelToken = new LabelStyleToken(setStyle.getLabelStyle());
		
		Paint currentSetColor = this.styleController.getSetStyleController().getColor(setToken);
		Paint currentLabelColor = this.getTextColor(labelToken);
		
		if(currentSetColor.equals(currentLabelColor))
		{
			this.styleController.getSetStyleController().setLockedLabelColors(setToken, true);
		}
		else
		{
			this.styleController.getSetStyleController().setLockedLabelColors(setToken, false);
		}
	}
}
