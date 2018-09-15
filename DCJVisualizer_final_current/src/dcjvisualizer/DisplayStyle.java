/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcjvisualizer;

import ca.corefacility.gview.data.Slot;
import ca.corefacility.gview.layout.feature.FeatureShapeRealizer;
import ca.corefacility.gview.style.GlobalStyle;
import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.style.datastyle.DataStyle;
import ca.corefacility.gview.style.datastyle.FeatureHolderStyle;
import ca.corefacility.gview.style.datastyle.SlotStyle;
import ca.corefacility.gview.style.items.BackboneStyle;
import ca.corefacility.gview.style.items.RulerStyle;
import ca.corefacility.gview.style.items.TooltipStyle;
import ca.corefacility.gview.textextractor.LocationExtractor;
import java.awt.Color;
import java.awt.Font;
import org.biojava.bio.seq.FeatureFilter;
import org.biojava.bio.seq.StrandedFeature;

/**
 *
 * Creating the Map for initial step from input file 
 */
public class DisplayStyle {
    
    public static MapStyle mapStyle;
    public static FeatureFilter sourcePosFeature = new FeatureFilter.And(new FeatureFilter.ByType("Source"),
									  new FeatureFilter.StrandFilter(StrandedFeature.POSITIVE));
                
    public static FeatureFilter targetPosFeature = new FeatureFilter.And(new FeatureFilter.ByType("Target"),
									  new FeatureFilter.StrandFilter(StrandedFeature.POSITIVE));
                
    public static FeatureFilter sourceNegFeature = new FeatureFilter.And(new FeatureFilter.ByType("Source"),
									  new FeatureFilter.StrandFilter(StrandedFeature.NEGATIVE));
                
    public static FeatureFilter targetNegFeature = new FeatureFilter.And(new FeatureFilter.ByType("Target"),
									  new FeatureFilter.StrandFilter(StrandedFeature.NEGATIVE));
    public static void buildStyle()
    {
            mapStyle = new MapStyle();
		
                
            // extract associated GlobalStyle from newly created MapStyle object
            GlobalStyle global = mapStyle.getGlobalStyle();

            // set initial height/width of the map
            global.setDefaultHeight(700);
            global.setDefaultWidth(700);

            global.setBackgroundPaint(Color.WHITE);

            // set tool tip style.  This is used to display extra information about various items on the map.
            TooltipStyle tooltip = global.getTooltipStyle();
            tooltip.setFont(new Font("SansSerif", Font.PLAIN, 15));
            tooltip.setBackgroundPaint(new Color(134, 134, 255)); // set background paint to (r,g,b)=(134,134,255)
            tooltip.setOutlinePaint(new Color(0.0f, 0.0f, 0.0f, 0.5f)); // set outline paint of tool tip item to (r,g,b) = (0,0,0) and alpha=0.5
            tooltip.setTextPaint(Color.BLACK);  // set the paint of the text for the tool tip item

            // set style information dealing with the backbone.  This is the item displayed in the very centre of the slots.
            BackboneStyle backbone = global.getBackboneStyle();
            backbone.setPaint(Color.GRAY.darker());
            backbone.setThickness(5.0);

            // set information dealing with the ruler style
            RulerStyle ruler = global.getRulerStyle();
            ruler.setMajorTickLength(10.0);
            ruler.setTickThickness(2.0);
            ruler.setMinorTickPaint(Color.GREEN.darker().darker());
            ruler.setMajorTickPaint(Color.GREEN.darker().darker());
            ruler.setFont(new Font("SansSerif", Font.BOLD, 12));  // font/font paint set information dealing with the text label of the ruler
            ruler.setTextPaint(Color.BLACK);

            /**Slots**/

            // assumes mapStyle created as above

            // extract data style from map style
            DataStyle dataStyle = mapStyle.getDataStyle();

            // creates the first two slots

            //slot to display the source gene
            SlotStyle firstUpperSlot = dataStyle.createSlotStyle(Slot.FIRST_UPPER); // first slot above backbone
            

            SlotStyle firstLowerSlot = dataStyle.createSlotStyle(Slot.FIRST_LOWER); // first slot below backbone
            

            //Todo : dynamic creation of slots

            //slot to display the target gene
            // sets the default color of any features in these slots
            firstUpperSlot.setPaint(Color.BLACK);
            firstLowerSlot.setPaint(Color.BLACK);


             // sets the thickness of the respective slots
            firstUpperSlot.setThickness(30);
            firstLowerSlot.setThickness(30);

            /**FeatureHolderStyle**/

            // assumes SlotStyles were created as above

            // creates a feature holder style in the first upper slot containing all the positive stranded features
            FeatureHolderStyle positiveFeatures = firstUpperSlot.createFeatureHolderStyle(sourcePosFeature);
            positiveFeatures.setThickness(0.7); // sets the thickness of these features as a proportion of the thickness of the slot
            positiveFeatures.setTransparency(0.9f); // sets transparency of all features drawn within this slot
            positiveFeatures.setToolTipExtractor(new LocationExtractor()); 	// sets how to extract text to be displayed for tool tips on these features
            positiveFeatures.setPaint(Color.BLUE); // sets default color of the positive features
            positiveFeatures.setFeatureShapeRealizer(FeatureShapeRealizer.CLOCKWISE_ARROW);

//	and sets all features in this holder to be drawn with no arrow

            // creates a holder containing all negative features in the first lower slot
            FeatureHolderStyle negativeFeatures = firstUpperSlot.createFeatureHolderStyle(sourceNegFeature);
            negativeFeatures.setThickness(0.7);
            negativeFeatures.setToolTipExtractor(new LocationExtractor());
            negativeFeatures.setPaint(Color.RED);
            negativeFeatures.setFeatureShapeRealizer(FeatureShapeRealizer.COUNTERCLOCKWISE_ARROW);

            FeatureHolderStyle circularFeature = firstUpperSlot.createFeatureHolderStyle(new FeatureFilter.ByType("CircularF"));
            circularFeature.setThickness(0.7);
            circularFeature.setToolTipExtractor(new LocationExtractor());
            circularFeature.setPaint(Color.GRAY);
            circularFeature.setFeatureShapeRealizer(FeatureShapeRealizer.NO_ARROW);
            
            
            FeatureHolderStyle positiveFeatures1 = firstLowerSlot.createFeatureHolderStyle(targetPosFeature);
            positiveFeatures1.setThickness(0.7); // sets the thickness of these features as a proportion of the thickness of the slot
            positiveFeatures1.setTransparency(0.9f); // sets transparency of all features drawn within this slot
            positiveFeatures1.setToolTipExtractor(new LocationExtractor()); 	// sets how to extract text to be displayed for tool tips on these features
            positiveFeatures1.setPaint(Color.BLUE); // sets default color of the positive features
            positiveFeatures1.setFeatureShapeRealizer(FeatureShapeRealizer.CLOCKWISE_ARROW);
                                                               //	and sets all features in this holder to be drawn with no arrow

            // creates a holder containing all negative features in the first lower slot
            FeatureHolderStyle negativeFeatures1 = firstLowerSlot.createFeatureHolderStyle(targetNegFeature);
            negativeFeatures1.setThickness(0.7);
            negativeFeatures1.setToolTipExtractor(new LocationExtractor());
            negativeFeatures1.setPaint(Color.RED);
            negativeFeatures1.setFeatureShapeRealizer(FeatureShapeRealizer.COUNTERCLOCKWISE_ARROW);
            
            FeatureHolderStyle circularFeature1 = firstLowerSlot.createFeatureHolderStyle(new FeatureFilter.ByType("TargetCircular"));
            circularFeature1.setThickness(0.7);
            circularFeature1.setToolTipExtractor(new LocationExtractor());
            circularFeature1.setPaint(Color.GRAY);
            circularFeature1.setFeatureShapeRealizer(FeatureShapeRealizer.NO_ARROW);
                
                
		
    }
    
}
