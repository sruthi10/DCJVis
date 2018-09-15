/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package input_output;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author chapp
 */
public class StepComparer {
    
    Step prevStep;
    Step currStep;
    public String operation;
    private ArrayList<String> invertedGenes ;
    public StepComparer(Step prev,Step cur)
    {
        this.prevStep = prev;
        this.currStep = cur;
        
    }
    public ArrayList<String> CompareSteps()
    {
        ArrayList<Chromosome> prevChromosomes = prevStep.chromosomes;
        ArrayList<Chromosome> currChromosomes = currStep.chromosomes;
        if(prevChromosomes.size() > currChromosomes.size())
        {
            operation = "FUSION"; //fusion
            //fusion method
            ArrayList<String> fusedGenes = new ArrayList<>();
           
               // Chromosome curChr = currChromosomes.get(i);
                for(int j =0; j< prevChromosomes.size();j++)
                {
                    boolean copyExist = false;
                    Chromosome prevChr = prevChromosomes.get(j);
                   for(int i =0; i < currChromosomes.size();i++)
                   {
                    if(prevChr.geneNames.size() == currChromosomes.get(i).geneNames.size())
                    {
                       // return cur
                        copyExist = true;
                        break;
                    }
                   }
                   if(!copyExist)
                   {
                       fusedGenes.addAll(prevChr.geneNames);
                   }
                    
                }
            
            return  fusedGenes;
        }
        else if(prevChromosomes.size() < currChromosomes.size())
        {
            operation = "FISSION"; //fission
            //call fission method
           Chromosome changedChr = currChromosomes.get(currChromosomes.size()-1);
           return changedChr.geneNames;
           //return new ArrayList<String>();
        }
        else
        {
            operation = "INVERSION" ; //inversion
            invertedGenes = new ArrayList<String>();
            InversionCalc(prevChromosomes,currChromosomes);
            return invertedGenes;
        }
    }
    // checking boolean of each genem from Chromosome class which tells the orientaiton ---> or <--- with boolean variable
    /*
    we check in for loop if the genes in previous chromosome (previous step) has same or different boolean orientaiton compared to current chromosome(current step)
    */
    private void InversionCalc(ArrayList<Chromosome> prevChromosomes, ArrayList<Chromosome> currChromosomes)
    {
        int chromosomeCount =prevChromosomes.size();
        for (int i =0 ; i < chromosomeCount;i++)
        {
            Chromosome prevC = prevChromosomes.get(i);
            Chromosome currC = currChromosomes.get(i);
            int geneCount = prevC.geneNames.size();
            LinkedHashMap<String, Boolean> prevGenes = prevC.genes;
            LinkedHashMap<String, Boolean> currGenes = currC.genes;
            
            ArrayList<String> pGenesNames = prevC.geneNames;
            ArrayList<String> cGenesNames = currC.geneNames;
            boolean pGName, cGName;
            for(int j = 0 ; j<geneCount;j++)
            {
                pGName = prevGenes.get(pGenesNames.get(j));
                cGName = currGenes.get(pGenesNames.get(j)); // changed from cGenesNames.get(j)  to pGenesNames.get(j) because the position of the gene can check, so use pGenesNames.get(j) to find geneName(key) then boolean value
               if(pGName != cGName)
               {
                    System.out.println();
                                                                                        //pGenesNames.get(j) and cGenesNames.get(j) may point to different genes as index pos of gene might change
                    //System.out.println("Prev Gene Name"+pGenesNames.get(j)+ "Bool :"+ pGName +"Prev Gene Name"+ cGenesNames.get(j)+" Bool :" + cGName);
                    // problem for not working for step 4 is that the position of two genes changed 
                   invertedGenes.add(pGenesNames.get(j));
               }
                
            }
            
        }
    }
    
}
