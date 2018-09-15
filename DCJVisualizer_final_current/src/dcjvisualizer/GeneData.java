/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcjvisualizer;

import ca.corefacility.gview.data.GenomeData;
import edu.umd.cs.piccolox.PFrame;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import jfasta.FastaReader;
import jfasta.extractor.PropertyExtractor;
import jfasta.model.DBFastaSequence;
import org.biojava.bio.symbol.SymbolList;
import ca.corefacility.gview.data.BlankSymbolList;
import ca.corefacility.gview.data.GenomeDataFactory;
import ca.corefacility.gview.layout.sequence.LayoutFactory;
import ca.corefacility.gview.layout.sequence.circular.LayoutFactoryCircular;
import ca.corefacility.gview.managers.DisplayManager;
import ca.corefacility.gview.managers.FeaturesManager;
import ca.corefacility.gview.map.GViewMap;
import ca.corefacility.gview.map.GViewMapFactory;
import ca.corefacility.gview.map.gui.GUIManager;
import ca.corefacility.gview.map.gui.GViewGUIFrame;
import ca.corefacility.gview.map.items.FeatureItem;
import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.style.datastyle.FeatureHolderStyle;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.biojava.bio.BioException;
import org.biojava.bio.seq.Feature;
import org.biojava.bio.seq.Sequence;
import org.biojava.bio.seq.StrandedFeature;
import org.biojava.bio.seq.impl.SimpleSequenceFactory;
import org.biojava.bio.symbol.RangeLocation;
import org.biojava.utils.ChangeVetoException;

/**
 * To read the fasta input files and parse them into sourceGene or targetGene arraylist
 * - DisplayMap: to get initial graph instance 
 * - CreatesourceMap - 
 */
public class GeneData  extends PFrame{
        public static ArrayList<GeneDisplayInfo> sourceGene;
        public static ArrayList<GeneDisplayInfo> targetGene;
        public static int[] location1;
        public static int[] location2;
        
        public static Map<String,GeneDisplayInfo> sourceGeneInfo;
        public static Map<String,GeneDisplayInfo> targetGeneInfo;
        
        private static FastaReader[] readers = new FastaReader[2];
        private static GenomeData data = null;
        public static Color[] cols;
        public static Map<String,Color> colorMap = new HashMap<String, Color>();
        private static Sequence blankSequence;
        public static int totalLength = 0;
        public static GViewGUIFrame initialFrame;
        
        
        //called after Build Map button
        public static void setSourceData(File aFile)
        {
            sourceGene = ReadFastaFile(aFile,0); // 0 readerid --> sourceGeneInfo
          
        }
        
        public static void setTargetData(File aFile)
        {
             targetGene = ReadFastaFile(aFile,1);//1 readerid --> targetGeneInfo
        }
        
        public static ArrayList<GeneDisplayInfo> ReadFastaFile(File file,int readerid)
        {
            ArrayList<GeneDisplayInfo> temp = new ArrayList<>();
            Map<String,GeneDisplayInfo> tempMap = new HashMap<>();
            
            FastaReader reader = new FastaReader(file,"r");
            reader.readFastaFile();
            PropertyExtractor px = new PropertyExtractor();
            int length = 0;
            ListIterator<String> ids = reader.getDBFastaSequenceIds().listIterator(); //ids? which part are we reaing from 
            int index =0;
            while(ids.hasNext())
            {
                String id = ids.next();
              //  System.out.println("GeneData while: " +id);
                DBFastaSequence element = reader.get(id);
                px.SetFastaSequence(element);
                int[] loc =  px.getLocation();
              //  totalLength += (loc[1] - loc[0]);  // different from 8_16 no issues
                String geneName = px.getName();
                
                GeneDisplayInfo gene = new GeneDisplayInfo(geneName, id, loc[0], loc[1],px.isComplement(),index);
                index++;
                temp.add(gene);
                tempMap.put(geneName, gene);
            }
            length = Math.abs(temp.get(0).Location1 - temp.get(temp.size()-1).Location2); // add on from 8_15 no issues

            readers[readerid] = reader; //Explain?
            if(readerid ==0)
                sourceGeneInfo = tempMap;
            else
                targetGeneInfo = tempMap;
            if(totalLength < length)
                totalLength = length;
            totalLength+=50;
            return temp;
        }
        
        //to get initial graph instance 
        public static void DisplayMap()
        {
            DisplayStyle.buildStyle();
            createSourceMap();
               MapStyle style = DisplayStyle.mapStyle;
		LayoutFactory layoutFactory;
                layoutFactory = new LayoutFactoryCircular();
		
		// builds the map from the data/style/layout information
		GViewMap gViewMap = GViewMapFactory.createMap(data, style, layoutFactory);
                DisplayManager d = gViewMap.getDisplayManager();
                FeaturesManager fm = d.featuresManager;
                Iterator<Feature> features= blankSequence.features();
                int targetIndex =0;
                int sourceIndex = 0;
                
                
                while(features.hasNext())
                {
                    Feature fe = features.next();
                    List<FeatureItem> items = fm.findFeatureItems(fe);
                    FeatureHolderStyle fs = items.get(0).getFeatureHolderStyle();
                   
                    
                   if(fs.hasFeatureFilter(DisplayStyle.targetPosFeature) || fs.hasFeatureFilter(DisplayStyle.targetNegFeature))
                   {
                       fs.setPaint(cols[targetIndex]);
                        items.get(0).setFeatureHolderStyle(fs);
                        targetIndex++;
                   }
                   if(fs.hasFeatureFilter(DisplayStyle.sourcePosFeature) || fs.hasFeatureFilter(DisplayStyle.sourceNegFeature))
                   {
                       //fs.setToolTipExtractor(sourceGene.get(sourceIndex).name) + new LocationExtractor());
                       //fs.setPaint(colorMap.get(sourceGene.get(sourceIndex).name));
                       fs.setPaint(colorMap.get(items.get(0).getFeature().getSource())); //
                        items.get(0).setFeatureHolderStyle(fs);
                        sourceIndex++;
                   }
                  }
                initialFrame = GUIManager.getInstance().buildGUIFrame("Initial", gViewMap);
        }
        
        private static void createSourceMap() 
        {
            int totalfeature =sourceGene.size();  
            colorMap = new HashMap<String, Color>();
            int colorIndex =0;
            cols = new Color[totalfeature];
            // we can change the way 3 parameters passed to get more variation of color 
            //hue, saturation, brightness
            for (int i = 0; i < totalfeature; i++)
                //Need to take care if we get color already been assigned 
                cols[i] = Color.getHSBColor((float) i / totalfeature, 1, 1);
           
           // SymbolList blankList = new BlankSymbolList(totalLength * 2); //different from 8_16 --> no issues
             SymbolList blankList = new BlankSymbolList(totalLength);
            SimpleSequenceFactory seqFactory = new SimpleSequenceFactory();
            blankSequence = seqFactory.createSequence(blankList, null, null, null);
            location1 = new int[totalfeature];
            location2 = new int[totalfeature];
            try {
                //source
            for(int i =0; i < totalfeature ; i++)
            {
                
                    GeneDisplayInfo gene = sourceGene.get(i);
                    StrandedFeature.Template stranded = new StrandedFeature.Template();
                    
                    if(gene.Complement)
                        stranded.strand = StrandedFeature.NEGATIVE;
                    else
                        stranded.strand = StrandedFeature.POSITIVE;
                    stranded.location = new RangeLocation(gene.Location1,gene.Location2);
                    //comment this line out and save locations with gene names and refer them
                    location1[i] = gene.Location1; //different from 8_16 moved these 2 lines to below for loop of target
                    location2[i] = gene.Location2;
                    stranded.type = "Source";
                    stranded.source = gene.name;
                    blankSequence.createFeature(stranded);
                    
                }
            
            //target            
            for(int i =0; i < totalfeature ; i++)
            {
                
                    GeneDisplayInfo gene = targetGene.get(i);
                    StrandedFeature.Template stranded = new StrandedFeature.Template();
                    
                    if(gene.Complement)
                        stranded.strand = StrandedFeature.NEGATIVE;
                    else
                        stranded.strand = StrandedFeature.POSITIVE;
                    stranded.location = new RangeLocation(gene.Location1,gene.Location2);
                    
                    stranded.type = "Target";
                    blankSequence.createFeature(stranded);
                    colorMap.put(gene.name,cols[colorIndex]); // assigning color to all the genes as in order of target genome --> colorMap can be used to fs.setPaint() StepsDisplay.java
                    stranded.source = gene.name;
                    colorIndex++;
                } 
            }
            catch (BioException ex) {
                    Logger.getLogger(GeneData.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ChangeVetoException ex) {
                    Logger.getLogger(GeneData.class.getName()).log(Level.SEVERE, null, ex);
                }
            
		data = GenomeDataFactory.createGenomeData(blankSequence);	
                    
            
        }
        
}
