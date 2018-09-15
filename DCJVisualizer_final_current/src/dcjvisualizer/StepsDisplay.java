/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcjvisualizer;

import ca.corefacility.gview.data.BlankSymbolList;
import ca.corefacility.gview.data.GenomeData;
import ca.corefacility.gview.data.GenomeDataFactory;
import ca.corefacility.gview.layout.sequence.LayoutFactory;
import ca.corefacility.gview.layout.sequence.circular.LayoutFactoryCircular;
import ca.corefacility.gview.managers.DisplayManager;
import ca.corefacility.gview.managers.FeaturesManager;
import ca.corefacility.gview.map.GViewMap;
import ca.corefacility.gview.map.GViewMapFactory;
import ca.corefacility.gview.map.gui.GUIManager;
import ca.corefacility.gview.map.items.FeatureItem;
import ca.corefacility.gview.style.MapStyle;
import ca.corefacility.gview.style.datastyle.FeatureHolderStyle;
import static dcjvisualizer.GeneData.sourceGene;
import input_output.Chromosome;
import input_output.Step;
import input_output.StepComparer;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.biojava.bio.BioException;
import org.biojava.bio.seq.Feature;
import org.biojava.bio.seq.Sequence;
import org.biojava.bio.seq.StrandedFeature;
import org.biojava.bio.seq.impl.SimpleSequenceFactory;
import org.biojava.bio.symbol.RangeLocation;
import org.biojava.bio.symbol.SymbolList;
import org.biojava.utils.ChangeVetoException;

public class StepsDisplay { // removed extends PFrame --> cause of extra windows
    
   // public  ArrayList<GeneDisplayInfo> sourceGene;
   // public  ArrayList<GeneDisplayInfo> targetGene;
    public Map<String,GeneDisplayInfo> sourceGeneInfo;
    public Map<String,GeneDisplayInfo> targetGeneInfo;
    private  GenomeData curdata = null;
    private  Map<String,Color> colorMap = new HashMap<String, Color>();
    private Color[] cols;
    private  Sequence blankSequence;
    private  int totalLength;
    private int totalFeature;
    //chrinfo has a boolean value for each chromosome to specify circularity
    private LinkedHashMap<Chromosome, Boolean> chrInfo;
    private ArrayList<Chromosome> chromosomes;
    private int chromosomeCount =0;
    private ArrayList<ArrayList<GeneDisplayInfo>> outputGenes;
    private ArrayList<GeneDisplayInfo> complegeneset;
    private String operationType = "";
    //public GViewGUIFrame listFrames;
    private int stepNo;
    public StepsDisplay(Step step,int stepNo)
    {
        this.sourceGeneInfo = GeneData.sourceGeneInfo;
        this.targetGeneInfo = GeneData.targetGeneInfo;
        this.totalFeature = targetGeneInfo.size();
        this.totalLength = GeneData.totalLength;
        this.colorMap = GeneData.colorMap;
        cols = GeneData.cols;
        chromosomes = step.chromosomes;
        this.chrInfo = step.chr;
        chromosomeCount = chrInfo.size();
        ReadOutputGene();
        this.stepNo = stepNo;
        
        
    }
    // circularity 
    private void ReadOutputGene()
    {
        complegeneset = new ArrayList<>();
        outputGenes = new ArrayList<>();
        Chromosome c;
        for(int i =0; i< chromosomeCount;i++)
        {
            //circular variable defines the circularity initially all genes are linear
            boolean circular = false;
            //reads the chromosome output for each step
            c = chromosomes.get(i);
            int geneCount = c.genes.size();
                        
            ArrayList<GeneDisplayInfo> tempList= new ArrayList<>(geneCount);
            outputGenes.add(tempList);
            LinkedHashMap<String, Boolean> genes = c.genes;
            ArrayList<String> geneNames = c.geneNames;
            int index = GeneData.sourceGene.indexOf(sourceGeneInfo.get(geneNames.get(0)));
            int loc = GeneData.location1[index];
          
            for(int j = 0;j<geneCount;j++)
            {
                String name = geneNames.get(j);
                GeneDisplayInfo tempInfo = sourceGeneInfo.get(name);
                tempInfo.Complement = genes.get(name);
                //to do something something
                
                //get the previous locations of the current gene
               int tempLoc1 = tempInfo.Location1 ;
                int tempLoc2 = tempInfo.Location2;
                int initialLength = tempLoc2 -tempLoc1;
               
                tempInfo.Location1 = loc;
                loc = loc + initialLength;
                tempInfo.Location2 =loc;
                loc += 1;
                tempList.add(sourceGeneInfo.get(name));
                complegeneset.add(sourceGeneInfo.get(name));
                //GeneDisplayInfo = new GeneDisplayInfo(name, sourceGeneInfo.get(name), SOMEBITS, SOMEBITS, circular)
            }
                
            if(chrInfo.get(c))
            {
                circular = true;                
                tempList.add(new GeneDisplayInfo("Circular",Integer.toString(tempList.size()+1) , tempList.get(tempList.size()-1).Location2, GeneData.totalLength-1, false,-1));
                tempList.add(new GeneDisplayInfo("Circular",Integer.toString(tempList.size()+1) , 0,tempList.get(0).Location1, false,-1));
                
            }
            //read the genes changed through the stepcomparer 
        }
    }
    private void CreateGenomeData()
    {
        
          
            //SymbolList blankList = new BlankSymbolList(totalLength * 2); //different from 8_16
            SymbolList blankList = new BlankSymbolList(totalLength);
            SimpleSequenceFactory seqFactory = new SimpleSequenceFactory();
            blankSequence = seqFactory.createSequence(blankList, null, null, null);
            
            try {
               
                for(int i =0;i<chromosomeCount;i++)
                {
                    ArrayList<GeneDisplayInfo> currentChr = outputGenes.get(i);
                    int geneCount = currentChr.size();

                    for(int j =0; j < geneCount ; j++)
                    {
                
                        GeneDisplayInfo gene = currentChr.get(j);
                        StrandedFeature.Template stranded = new StrandedFeature.Template();

                        if(gene.Complement)
                            stranded.strand = StrandedFeature.NEGATIVE;
                        else
                            stranded.strand = StrandedFeature.POSITIVE;
                        stranded.location = new RangeLocation(gene.Location1,gene.Location2);
                        //String s = "Source
                       
                        stranded.type = "Source" + Integer.toString(i);
                         if(gene.name =="Circular")
                            stranded.type = "CircularF" + Integer.toString(i);
                        stranded.source = gene.name;
                        Feature f = blankSequence.createFeature(stranded);
                        
                    }
                    
                }       
                
                
                 int totalfeature =sourceGene.size();  
                for(int i =0; i < totalfeature ; i++)
                {

                    GeneDisplayInfo gene = GeneData.targetGene.get(i);
                    StrandedFeature.Template stranded = new StrandedFeature.Template();
                    
                    if(gene.Complement)
                        stranded.strand = StrandedFeature.NEGATIVE;
                    else
                        stranded.strand = StrandedFeature.POSITIVE;
                    stranded.location = new RangeLocation(gene.Location1,gene.Location2);
                    stranded.type = "Target";
                    stranded.source = gene.name;
                    blankSequence.createFeature(stranded);
                    
                }
            
           
            }
            catch (BioException ex) {
                    Logger.getLogger(GeneData.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ChangeVetoException ex) {
                    Logger.getLogger(GeneData.class.getName()).log(Level.SEVERE, null, ex);
                }
            
		curdata = GenomeDataFactory.createGenomeData(blankSequence);	
                  
            
        }
    
    
    
    public void DisplayMap(ArrayList<String> changedGenes, String operationType)
        {
            this.operationType = operationType;
            OutputDisplayStyle style = new OutputDisplayStyle();
            MapStyle mapStyle = style.buildStyle(chromosomeCount);
            //MapStyle mapStyle = DisplayStyle.mapStyle;
            CreateGenomeData();
            LayoutFactory layoutFactory;
            layoutFactory = new LayoutFactoryCircular();
		
            // builds the map from the data/style/layout information
            GViewMap gViewMap = GViewMapFactory.createMap(curdata, mapStyle, layoutFactory);
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
              // items.get(0).setSelect();
               //displaying the inner target gene
              
               if(fs.hasFeatureFilter(DisplayStyle.targetPosFeature) || fs.hasFeatureFilter(DisplayStyle.targetNegFeature))
               {
                   
                   fs.setPaint(cols[targetIndex]);
                   items.get(0).setFeatureHolderStyle(fs);
                   targetIndex++;
               }
               
               //displaying the source genome
               boolean filterSource = false;
               for(int cc =0;cc < chromosomeCount*3;cc+=3)
               {
                   filterSource = filterSource || fs.hasFeatureFilter(style.outputFilters.get(cc)) || fs.hasFeatureFilter(style.outputFilters.get(cc+1));
               }
               if(filterSource)
              // if(fs.hasFeatureFilter(DisplayStyle.sourcePosFeature) || fs.hasFeatureFilter(DisplayStyle.sourceNegFeature))
               {
                   //items.get(0).getFeature().getSource()
                   //check this code
                    fs.setPaint(colorMap.get(items.get(0).getFeature().getSource()));
                    items.get(0).setFeatureHolderStyle(fs);
                    if(changedGenes.contains(items.get(0).getFeature().getSource()))
                    {
                        items.get(0).setSelect();
                      
                    }
                    sourceIndex++;
               }
//                filterSource = false;
//               for(int cc =2;cc < chromosomeCount*3;cc+=3)
//               {
//                   filterSource = filterSource || fs.hasFeatureFilter(style.outputFilters.get(cc));
//               }
//               
//                if(filterSource)
//              // if(fs.hasFeatureFilter(DisplayStyle.sourcePosFeature) || fs.hasFeatureFilter(DisplayStyle.sourceNegFeature))
//               {
//                   //items.get(0).getFeature().getSource()
//                   //check this code
//                    fs.setPaint(Color.PINK);
//                    items.get(0).setFeatureHolderStyle(fs);
//                    sourceIndex++;
//               }
               
              }
            String s;
            if(stepNo == 0)
            {
                s = "Step :" + stepNo;
            }
            else
                s = "Step :" + stepNo + "(" + this.operationType + ")";
            GUIManager.getInstance().buildGUIFrame(s, gViewMap); // display window with title as step No. and operationType
             
        }
    }

    

