/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package input_output;

import com.google.common.collect.ArrayListMultimap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
//import static com.sun.org.apache.regexp.internal.RETest.test;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map.Entry;
import org.apache.commons.lang.ArrayUtils;
/**
 *
 * @author sxc105920
 */
public class Step 
{

    //$1 --> circular chromosome
    //$2 --> linear chromosome   
    //public  ArrayList<Chromosome> chr = new ArrayList<Chromosome>();
    public LinkedHashMap<Chromosome, Boolean> chr = new LinkedHashMap<Chromosome, Boolean>(); //Boolean -->true  if Circular, Boolean --> false  linear chromosome
    public ArrayList<Chromosome> chromosomes = new ArrayList<>();
    public String step_line, prev_step_line;
    //similar to Multimap 
    //Map<Character, List<Integer>> occurence_index = new HashMap<Character, List<Integer>>(); //it will keep the occurence of )or | and index at which it occured
   // Multimap<Character, Integer> myMultimap = ArrayListMultimap.create();
    
    ArrayList<String> occurence_index = new ArrayList<>();  // used to told occurence+character (string) in given step
    ArrayList<String> changedGenes = new ArrayList<>(); // for the highlight or focusing
    boolean linear , circular = false;
     
    public Step(String input)
    {
        step_line = input;
    }

    public Step() 
    {
    }    

    public void intialize(String prev_line) 
    {
        //pre-process to remove the step#
        prev_line = prev_line.replaceAll("\\d+.\\:\\s*", ""); //replace 0(step#).: in each step
        prev_line = prev_line.trim();  
        prev_step_line = prev_line;
        
        step_line = step_line.replaceAll("\\d+.\\:\\s*", ""); //replace 0(step#).: in each step
        step_line = step_line.trim();

        if(!prev_step_line.equalsIgnoreCase("null")) // to ignore step0, as it doesn't have prev_step
        {
           changedGenes();// to store the position changes of genes from prev_step to current_step --> to focus in GUI
        } 
        Chromosome c;
        String[] list_chromosome = setChromosome(step_line); // to go thru step input and add flag $1 for ) $2 for |
                
        for (int i = 0; i < list_chromosome.length; i++) 
        {
            if (list_chromosome[i].contains("$1")) {
                list_chromosome[i] = list_chromosome[i].replaceAll("\\$1", "");
                c = new Chromosome(list_chromosome[i]);
                chr.put(c, true);
                chromosomes.add(c);
                c.intialize();
            } 
            else //linear chromosome with $2
            {
                list_chromosome[i] = list_chromosome[i].replaceAll("\\$2", "");
                //chr.put(new Chromosome(list_chromosome[i]), false);
                c = new Chromosome(list_chromosome[i]);
                chr.put(c, false);
                chromosomes.add(c);
                c.intialize();
            }
        }
    }

    /**
     * Get the value of string
     *
     * @return array of string each index with 1 chromosome from each step of
     * DCJ line
     * Will return the list of chromosome from the give step. with $1 appending to circular chromosome, $2 appending linear chromosome
     */
    public String[] setChromosome(String line) 
    {
//        line = line.replaceAll("\\d+.\\:\\s*", ""); //replace 0(step#).: in each step
//        line = line.trim();  

        int circular_count = occurenceIndex(line, ')'); //get index at which circular chromosome 
        int linear_count = occurenceIndex(line, '|'); //get index at which this linear chromosome is
        //get index at which circular chromosome 

        
        // sort the occurence_index, so that indexes will be stored from low to high index.
        Collections.sort(occurence_index); // sorts the input and results with index of ) or | in the order they occured in given step
        String[] sorted_chr = new String[linear_count + circular_count];

        int prev_index = 0; //to hold the previous location where ) or | occurred in given string
        int index = 0;
        String temp, value = null;
        int j =0;
        
        if(!(linear && circular))//if(linear && circular)
        {
            String[] temp_chr;
            if(linear)
            {
                temp_chr = line.split("\\|");
                for (int i = 0; i < temp_chr.length; i++) 
                {
                    if (temp_chr[i].matches("^.*[^a-zA-Z0-9 ].*$")) // extra[index] = ""
                    {
                        sorted_chr[j] = temp_chr[i].trim() + "$2"; //$2 is linear
                        //sorted_chr[j] = sorted_chr[j].concat("$2");
                        j++;
                    }
                }                
            }
            else //circular
            {
                temp_chr = line.split("\\)");
                for (int i = 0; i < temp_chr.length; i++) 
                {
                    if (temp_chr[i].matches("^.*[^a-zA-Z0-9 ].*$")) // extra[index] = ""
                    {
                        sorted_chr[j] = temp_chr[i].trim() + "$1"; //$1 is circular
                        //sorted_chr[j] = sorted_chr[j].concat("$1");
                        j++;
                    }
                }
            }           
        }             
        else if(linear && circular) //myMultimap.keySet().size() == 2
        {
           // occurence_index.forEach( (i) -> System.out.println(i)); // access each entry and printing it out
      
            for(int i=0; i<occurence_index.size();i++) 
            {
                value = occurence_index.get(i);
                                
                char selector = value.charAt(value.length()-1);
                index = Integer.parseInt(value.substring(0, value.length()-1)); // remove the selector at the end of each entry (eg 15|) just store the index
                if(selector == ')') 
                {                  
                    if(prev_index == 0)
                    {
                        temp = line.substring(prev_index, index-1); //index-1 because we want to eliminate the ) or |
                    }
                    else
                    {
                       temp = line.substring(prev_index+1, index-1); // becuase we will don't want to store ) or | character
                      /* if (temp.matches("^.*[^a-zA-Z0-9 ].*$"))//https://regex101.com/
                        {
                            sorted_chr[j] = temp.trim() + "$1";///$1 is circular
                            //sorted_chr[j] = sorted_chr[j]
                            j++;
                        } */
                    }
                    sorted_chr[j] = temp.trim() + "$1";///$1 is circular
                            //sorted_chr[j] = sorted_chr[j]
                    j++;                    
                }
                else if (selector == '|')
                {
                    if(prev_index == 0) //if this is the first chromosome
                    {
                        temp = line.substring(prev_index, index-1); //index-1 because we want to eliminate the ) or |   
                    }
                    else
                    {
                       temp = line.substring(prev_index+1, index-1); // becuase we will don't want to store ) or | character
                        /*if (temp.matches("^.*[^a-zA-Z0-9 ].*$")) // extra[index] = ""
                        {
                            sorted_chr[j] = temp.trim() + "$2"; //$2 is linear
                            //sorted_chr[j] = sorted_chr[j].concat("$2");
                            j++;                
                        }  */   
                    }
                    sorted_chr[j] = temp.trim() + "$2"; //$2 is linear
                    //sorted_chr[j] = sorted_chr[j].concat("$2");
                    j++; 
                }
                prev_index = index;

            } 
        }//else if whne keys =2        
        return sorted_chr;
    }
    /*
    checking if | or ) character occurs int the given string line, which will reutrn count of linear chromosome when | was passed as chracter
    if ) was passed character then it will return the count of circular chromosomes
    */
    public int occurenceIndex(String line, char character)
    {
        int counter = 0;
        if (character == '|')
            linear = true;
        else if(character == ')')
            circular = true;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == character) {
               // myMultimap.put(character, i);
                occurence_index.add(i+ "" +character);
                counter++;
            }
        }
        return counter;
    }
    /*
    method compares the current step_line and previous_step_line and store the changed genes postion into arraylist
    so this can be highlighted to show which genes changed between steps
    */ 
    public void changedGenes() 
    {     
        //remove all ) and |
        String temp_prev = prev_step_line.replaceAll("[\\|\\)]", "");       
        String temp_current = step_line.replaceAll("[\\|\\)]", "");
                
        String[] prev = temp_prev.split(" ");
        for(int i = 0; i<prev.length;i++)
        {
            if(prev[i].equalsIgnoreCase(""))
            //let's remove or delete an element from Array using Apache Commons ArrayUtils
            prev = (String[]) ArrayUtils.remove(prev, i); //removing element at index 2
        }        
        String[] current = temp_current.split(" ");
        for(int i = 0; i<current.length;i++)
        {
            if(current[i].equalsIgnoreCase(""))
            //let's remove or delete an element from Array using Apache Commons ArrayUtils
            current = (String[]) ArrayUtils.remove(current, i); //removing element at index 2
        }        
        // both current and prev should be same length = # genes
        for(int i = 0; i<prev.length ; i++)
        {
            if(prev[i].equalsIgnoreCase(current[i]))
            {
                //nothing same position of gene in previous and current step
            }
            else
            {
                changedGenes.add(current[i]);
            }                
        }   
    }// end of changedGenes
 }//end of class
  
 //debugging to test intialize assinging ciruclar and linear properly   
  /*  public static void main(String[] args) 
    {
        //Step.intialize1();
       ArrayList<String> changedGenes = new ArrayList<>();
        changedGenes("0.:  DCC1 NFS1 | LEU2 -BUD3 | -GBP2 -SGF29 ) YCL002 -PGS1 | ","1.:  PGS1 -YCL002 ) SGF29 GBP2 BUD3 | LEU2 -NFS1 -DCC1 ) ", changedGenes);
 
    }
    public static void changedGenes(String prev_line, String step_line, ArrayList<String> changedGenes) 
    {
        prev_line = prev_line.replaceAll("\\d+.\\:\\s*", ""); //replace 0(step#).: in each step
        prev_line = prev_line.trim();  
        String prev_step_line = prev_line;
        
        step_line = step_line.replaceAll("\\d+.\\:\\s*", ""); //replace 0(step#).: in each step
        step_line = step_line.trim();
        
        //remove all ) and |
        String temp_prev = prev_step_line.replaceAll("[\\|\\)]", "");       
        String temp_current = step_line.replaceAll("[\\|\\)]", "");
        
        
        String[] prev = temp_prev.split(" ");
        for(int i = 0; i<prev.length;i++)
        {
            if(prev[i].equalsIgnoreCase(""))
            //let's remove or delete an element from Array using Apache Commons ArrayUtils
            prev = (String[]) ArrayUtils.remove(prev, i); //removing element at index 2
        }
        
        String[] current = temp_current.split(" ");
        for(int i = 0; i<current.length;i++)
        {
            if(current[i].equalsIgnoreCase(""))
            //let's remove or delete an element from Array using Apache Commons ArrayUtils
            current = (String[]) ArrayUtils.remove(current, i); //removing element at index 2
        }
        // both current and prev should be same length = # genes
        for(int i = 0; i<prev.length ; i++)
        {
            if(prev[i].equalsIgnoreCase(current[i]))
            {
                //nothing same position of gene
            }
            else
            {
                changedGenes.add(current[i]);
            }                
        }   
    }*/

/*
    private static void intialize1() 
    {
        
        String[] list_chromosome = Step.setChromosome("1.:  -CaJ7.0001 -CaJ7.0003 -CaJ7.0009 -CaJ7.0011 -CaJ7.0013 -CaJ7.0015 CaJ7.0017 | -CaJ7.0005 -CaJ7.0007 CaJ7.0004 ) ");
        for(int i=0;i<list_chromosome.length;i++)
        {
            if(list_chromosome[i].contains("$1"))
            {
                list_chromosome[i] = list_chromosome[i].replaceAll("\\$1", ""); 
                //chr.put(new Chromosome(list_chromosome[i]),true);
                //c.intialize();
            }
            else //linear chromosome with $2
            {
                list_chromosome[i] = list_chromosome[i].replaceAll("\\$2", ""); 
                //chr.put(new Chromosome(list_chromosome[i]),false); 
               // c.intialize();
            }
            
           
        }
    }*/