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
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

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
    public String step_line;
    Map<Character, List<Integer>> occurence_index = new HashMap<Character, List<Integer>>(); //it will keep the occurence of )or | and index at which it occured
    Multimap<Character, Integer> myMultimap = ArrayListMultimap.create();
    ArrayList<String> temp_chr_input = new ArrayList<>(); 
    String [] changedGenes; // for the highlight or focusing
    int no_genes = 0;
    ArrayList<String> changed = new ArrayList<>();
    boolean linear , circular = false;
    
    public Step(String input)
    {
        step_line = input;
    }

    public Step() {

    }
    

    public void intialize() 
    {
        Chromosome c;
        String[] list_chromosome = setChromosome(step_line); // to go thru step input and add flag $1 for ) $2 for |
        
        for (int i = 0; i < list_chromosome.length; i++) {
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
       
        int linear_count = occurenceIndex(line, '|'); //get index at which this linear chromosome is
        //get index at which circular chromosome 
        int circular_count = occurenceIndex(line, ')'); //get index at which circular chromosome 
        Map map = myMultimap.asMap(); // get compare the key as either ) or | then count the index
        
        String[] sorted_chr = new String[linear_count + circular_count];
        line = line.replaceAll("\\d+.\\:\\s*", ""); //replace 0(step#).: in each step
        line = line.trim();
        //System.out.println("Keys: " + myMultimap.keys().toString());
        //System.out.println("Keys set: "+ myMultimap.keySet());
        // Iterating over entire Mutlimap
        for (Integer value : myMultimap.values()) 
        {
            System.out.println(value);
        }
        int j = 0;
        ArrayList<Integer> c_value_index = new ArrayList<Integer>(map.values()); // to get the index of each value for a key at 0 (get(0)) index in this case
        //ArrayList<Integer> test = new ArrayList<Integer>(c_value_index.get(0));
        //  ArrayList<Integer> test = new ArrayList<Integer>(s);
        Iterator entries = map.entrySet().iterator();
        
        int prev_index = 0; //for the first round
        int index = 0;
        String temp = null;
        
        while (entries.hasNext()) 
        {
            Entry thisEntry = (Entry) entries.next();
            char selector = (char)thisEntry.getKey();
            String index1 = thisEntry.getValue().toString(); // index1  = [##]
            String temp_index1 = index1.substring(1, index1.length()-1); //to remove []around ##
            c_value_index.
            index = Integer.parseInt(temp_index1);
            if(selector == ')') 
            {                  
                if(prev_index == 0)
                {
                    temp = line.substring(prev_index, index-1); //index-1 because we want to eliminate the ) or |
                }
                else
                {
                   temp = line.substring(prev_index, index-1);
                }
                if (temp.matches("^.*[^a-zA-Z0-9 ].*$"))//https://regex101.com/
                {
                        sorted_chr[j] = temp.trim() + "$1";///$1 is circular
                        //sorted_chr[j] = sorted_chr[j]
                        j++;
                }
            }
            else if (selector == '|')
            {
                String[] temp1_chr = line.split("\\)");
                if(prev_index == 0) //if this is the first chromosome
                {
                    temp = line.substring(prev_index, index-1); //index-1 because we want to eliminate the ) or |
                }
                else
                {
                   temp = line.substring(prev_index, index-1);
                }
                
                
                if (temp.matches("^.*[^a-zA-Z0-9 ].*$")) // extra[index] = ""
                {
                    sorted_chr[j] = temp.trim() + "$2"; //$2 is linear
                    //sorted_chr[j] = sorted_chr[j].concat("$2");
                    j++;                
                }                
            }
           
            prev_index = index;
            
        } 
           /* else if (temp_chr[i].matches("^.*[^a-zA-Z0-9 ].*$")) // extra[index] = ""
            {
                sorted_chr[j] = temp_chr[i].trim() + "$2"; //$2 is linear
                //sorted_chr[j] = sorted_chr[j].concat("$2");
                j++;
            }*/
            // ...
            
        
        map.forEach( (k,v) -> System.out.println("Key: " + k + ": Value: " + v));
        System.out.println(myMultimap.entries());
        //System.out.println("setChromosome: " +myMultimap.keySet().iterator().next());      
     
        //myMultimap.keySet().iterator().next() returns the first key stored either ) or |
        line = line.trim();

       if(myMultimap.keySet().size() > 1) // both linear and circular chromosomes are preesent
           // also can use global boolean variable linear and circular if both true then both types otherwise only 1 type
        {
           //work
           myMultimap.keySet().iterator().next();
      
         
        }
       else if (myMultimap.keySet().size() == 1)
       {
           //one type of chrosome
           //get the key stored and then go thru value by value
           
       }
      
        //below to be modified to parse multiple circular and linear chromosomes from 1 string
        
       /*int linear_count = occurence(line, '|');
        int circular_count = occurence(line, ')');
        line = line.trim();

        String[] sorted_chr = new String[linear_count + circular_count];
        line = line.replaceAll("\\d+.\\:\\s*", ""); //replace 0.: in each step
        String[] temp_chr = line.split("(\\))|(\\|)");
        System.out.println(line);
        int j = 0;// 
        int prevIndex = 0;
         String circ = "";
        for(int index =0 ;index < temp_chr.length;index++)
        {
            prevIndex += temp_chr[index].length();
            char c = line.charAt(prevIndex);
            if(c == ')')
            {
                 circ = "$1";
            }
            else if (c == '|')
            {
                 circ = "$2";
            }
             sorted_chr[j] =temp_chr[index] + circ;
             j++;
             prevIndex++;
        }
        
//        while (prevIndex < line.length()-2)
//        {            
//            
//            if (line.contains(")") || line.contains("|")) 
//            {
//                int indexC = 0;
//                String circ = "";
//                if(line.contains(")"))
//                {
//                    indexC = line.indexOf(")")-1;
//                   circ = "$1";
//                }
//                
//                else if (line.contains("|"))
//                {
//                    indexC = line.indexOf("|")-1;
//                    circ = "$2";
//                }
//                
//                sorted_chr[j] = line.substring(prevIndex, indexC) + circ;
//                j++;
//                
//                prevIndex = indexC;
//               
//            } 
//            
//        }

        return sorted_chr;
    */
       return sorted_chr;
    }
    
    /*
    checking if | or ) character occurs int the given string line, which will reutrn count of linear chromosome when | was passed as chracter
    if ) was passed character then it will return the count of circular chromosomes
    */
    public int occurence(String line, char character) {
        int counter = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == character) {
                //occurence_index.put(character, i);
                counter++;
            }
        }
        return counter;
    }
    /*
    To return the array where exactly the occurence of the character happend
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
               // occurence_index.put(character, i);
                myMultimap.put(character, i);
                temp_chr_input.add(i+ "" +character);
                counter++;
            }
        }
        return counter;
    }
}
  
 /*debugging to test intialize assinging ciruclar and linear properly   
    public static void main(String[] args) 
    {
        Step.intialize1();
    }

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
