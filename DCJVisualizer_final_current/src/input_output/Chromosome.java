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
public class Chromosome {

    public LinkedHashMap<String, Boolean> genes = new LinkedHashMap<String, Boolean>(); //Boolean = true --> complement, Boolean = false --> not complement
    //LinkedHashMap used instead of HashMap to keep insertion order
    
    public ArrayList<String> geneNames = new ArrayList<>();
    public String chr_line;
    
    public Chromosome(String input) 
    {
        chr_line = input;
    }
    public Chromosome()
    {
        
    }
    
    public void intialize() 
    {
        String []parts = chr_line.split(" ");
        for(int i = 0 ; i<parts.length;i++)
        {
            if(parts[i].contains("-"))//complement <--
            {
                parts[i] = parts[i].replaceFirst("-", "");
                genes.put(parts[i], true); 
                geneNames.add(parts[i]);
            }
            else 
            {//not complement --> 
                genes.put(parts[i], false);
                geneNames.add(parts[i]);
            }
        }
        // parase string1 to get genename and orientation and store in hashmap
        //genes.put("genename", "value");
    }

/* Debugging
      public static void main(String[] args)
    {
 
        intialize1();
    }
    public  void intialize1() 
    {
         String s =  "-CaJ7.0001 -CaJ7.0003 -CaJ7.0009 -CaJ7.0011 -CaJ7.0013 -CaJ7.0015 CaJ7.0017";
                
        String []parts = s.split(" ");
        for(int i = 0 ; i<parts.length;i++)
        {
            if(parts[i].contains("-"))//complement <--
            {
                parts[i] = parts[i].replaceFirst("-", "");
                genes.put(parts[i], true); 
            }
            else //not complement --> 
                genes.put(parts[i], false);
        }
        //genes.put("test", chr_line);
        // parase string1 to get genename and orientation and store in hashmap
        //genes.put("genename", "value");
        System.out.println(genes.toString());
    }

  */
    /**
     * Get the value of string
     *
     * @return the value of string
     */
    public String getString() {
        return chr_line;
    }

    /**
     * Set the value of string
     *
     * @param string new value of string
     */
    public void setString(String string) 
    {
        chr_line = string;
    }
    
/*
    public class Gene<L, R> {

        private final L left;
        private final R right;

        public Gene(L left, R right) {
            this.left = left;
            this.right = right;
        }

        public L getLeft() {
            return left;
        }

        public R getRight() {
            return right;
        }

        @Override
        public int hashCode() {
            return left.hashCode() ^ right.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Gene)) {
                return false;
            }
            Gene pairo = (Gene) o;
            return this.left.equals(pairo.getLeft())
                    && this.right.equals(pairo.getRight());
        }

    } */
}
