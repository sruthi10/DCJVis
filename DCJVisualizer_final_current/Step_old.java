/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package input_output;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

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

    public Step(String input)
    {
        step_line = input;
    }

    public Step() {

    }

    public void intialize() 
    {
        Chromosome c;
        String[] list_chromosome = setChromosome(step_line);
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
     */
    public String[] setChromosome(String line) 
    {
        int linear_count = occurence(line, '|');
        int circular_count = occurence(line, ')');
        line.trim();

        String[] sorted_chr = new String[linear_count + circular_count];
        line = line.replaceAll("\\d+.\\:\\s*", ""); //replace 0.: in each step
        String[] temp_chr = line.split("\\|");
        System.out.println(temp_chr);
        int j = 0;// 

        for (int i = 0; i < temp_chr.length; i++) {
            if (temp_chr[i].contains(")")) 
            {
                String[] temp1_chr = temp_chr[i].split("\\)");
                for (int k = 0; k < temp1_chr.length; k++) {
                    if (temp1_chr[k].matches("^.*[^a-zA-Z0-9 ].*$")) {
                        sorted_chr[j] = temp1_chr[k].trim() + "$1";///$1 is circular
                        //sorted_chr[j] = sorted_chr[j]
                        j++;
                    }
                }
            } 
            else if (temp_chr[i].matches("^.*[^a-zA-Z0-9 ].*$")) // extra[index] = ""
            {
                sorted_chr[j] = temp_chr[i].trim() + "$2"; //$2 is linear
                //sorted_chr[j] = sorted_chr[j].concat("$2");
                j++;
            }
        }

        return sorted_chr;
    }

    public int occurence(String line, char character) {
        int counter = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == character) {
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
