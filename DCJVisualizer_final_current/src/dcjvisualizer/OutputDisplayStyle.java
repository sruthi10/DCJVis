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
import java.util.ArrayList;
import org.biojava.bio.seq.FeatureFilter;
import org.biojava.bio.seq.StrandedFeature;

/**
 *
 * @author Rashika
 */
public class OutputDisplayStyle {
    
     public  MapStyle mapStyle;
           
    public  FeatureFilter targetPosFeature = new FeatureFilter.And(new FeatureFilter.ByType("Target"),
									  new FeatureFilter.StrandFilter(StrandedFeature.POSITIVE));
                
           
    public  FeatureFilter targetNegFeature = new FeatureFilter.And(new FeatureFilter.ByType("Target"),
									  new FeatureFilter.StrandFilter(StrandedFeature.NEGATIVE));
    
    public  ArrayList<FeatureFilter> outputFilters = new ArrayList<>();
    
    
    public  MapStyle buildStyle(int chromosomeCount)
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
            tooltip.setFont(new Font("SansSerif", Font.PLAIN, 12));
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
            CreateTargetSLot();
            // assumes mapStyle created as above

            // extract data style from map style
            DataStyle dataStyle = mapStyle.getDataStyle();

            // creates the first two slots

            //slot to display the source gene
            ArrayList<SlotStyle> slotsource = new ArrayList<>();
            ArrayList<FeatureHolderStyle> holders = new ArrayList<>();
            for(int i =0 ;i< chromosomeCount;i++)
            {
                SlotStyle upperSlot = dataStyle.createSlotStyle(Slot.FIRST_UPPER + i); // first slot above backbone
                upperSlot.setPaint(Color.BLACK);
                upperSlot.setThickness(30);
                FeatureFilter slotPosFilter = new FeatureFilter.And(new FeatureFilter.ByType("Source" + i),
									  new FeatureFilter.StrandFilter(StrandedFeature.POSITIVE));
                FeatureFilter slotNegFilter= new FeatureFilter.And(new FeatureFilter.ByType("Source"+ i),
									  new FeatureFilter.StrandFilter(StrandedFeature.NEGATIVE));
                outputFilters.add(slotPosFilter);
                outputFilters.add(slotNegFilter);
                 // creates a feature holder style in the first upper slot containing all the positive stranded features
            FeatureHolderStyle positiveFeatures = upperSlot.createFeatureHolderStyle(slotPosFilter);
            positiveFeatures.setThickness(0.7); // sets the thickness of these features as a proportion of the thickness of the slot
            positiveFeatures.setTransparency(0.9f); // sets transparency of all features drawn within this slot
            positiveFeatures.setToolTipExtractor(new LocationExtractor()); 	// sets how to extract text to be displayed for tool tips on these features
            positiveFeatures.setPaint(Color.BLUE); // sets default color of the positive features
            positiveFeatures.setFeatureShapeRealizer(FeatureShapeRealizer.CLOCKWISE_ARROW);

                                                                                                                                            //	and sets all features in this holder to be drawn with no arrow

            // creates a holder containing all negative features in the first lower slot
            FeatureHolderStyle negativeFeatures = upperSlot.createFeatureHolderStyle(slotNegFilter);
            negativeFeatures.setThickness(0.7);
            negativeFeatures.setToolTipExtractor(new LocationExtractor());
            negativeFeatures.setPaint(Color.RED);
            negativeFeatures.setFeatureShapeRealizer(FeatureShapeRealizer.COUNTERCLOCKWISE_ARROW);

             FeatureFilter circularFilter= new FeatureFilter.And(new FeatureFilter.ByType("CircularF"),
									  new FeatureFilter.StrandFilter(StrandedFeature.NEGATIVE));
            FeatureHolderStyle circularFeature = upperSlot.createFeatureHolderStyle(new FeatureFilter.ByType("CircularF" + i));
            circularFeature.setThickness(0.7);
            circularFeature.setToolTipExtractor(new LocationExtractor());
            circularFeature.setPaint(Color.GRAY);
            circularFeature.setFeatureShapeRealizer(FeatureShapeRealizer.NO_ARROW);
            slotsource.add(upperSlot);
            outputFilters.add(circularFilter);
           // holders.add(positiveFeatures);
            //holders.add(circularFeature);
           // holders.add(negativeFeatures);
            }
            return mapStyle;
    }
    
    private  void CreateTargetSLot()
    {
        DataStyle dataStyle = mapStyle.getDataStyle();
         //slot to display the target gene
            SlotStyle firstLowerSlot = dataStyle.createSlotStyle(Slot.FIRST_LOWER); // first slot below backbone
             firstLowerSlot.setPaint(Color.BLACK);
              firstLowerSlot.setThickness(30);
               FeatureHolderStyle positiveFeatures1 = firstLowerSlot.createFeatureHolderStyle(targetPosFeature);
            positiveFeatures1.setThickness(0.7); // sets the thickness of these features as a proportion of the thickness of the slot
            positiveFeatures1.setTransparency(0.9f); // sets transparency of all features drawn within this slot
            positiveFeatures1.setToolTipExtractor(new LocationExtractor()); // sets how to extract text to be displayed for tool tips on these features
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
    

