package de.unibi.cebitec.gi.unimog.utils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import net.sf.jfasta.FASTAElement;
import net.sf.jfasta.FASTAFile;
import net.sf.jfasta.FASTAFileReader;
import net.sf.jfasta.HeaderDialect;
import net.sf.jfasta.Utils;
import net.sf.jfasta.impl.ExceptionRuntimeFASTA;
import net.sf.jfasta.impl.FASTAElementImpl;
import net.sf.jfasta.impl.FASTAElementIterator;
import net.sf.jfasta.impl.FASTAFileImpl;
import net.sf.jfasta.impl.FASTAFileReaderImpl;
import net.sf.jfasta.impl.FASTAFileWriter;
import net.sf.jfasta.impl.HeaderDialectUniprot;
/**
 *
 * @author Rashika
 */
public class FastaReader {
    
   
   // Map<String, String> 
    public static String ReadFile(String genomeName,String path) throws IOException
    {
        FASTAElementIterator it;
        //File file = new File("H:\\7000\\sequence.fa");
        File file = new File(path); 
        FASTAFileReader reader = new FASTAFileReaderImpl(file);
        Map<String, String> map = new HashMap<String, String>();
        String input = genomeName + "\n";
        it = reader.getIterator();
        while(it.hasNext())
        {
            FASTAElement el = it.next();
            String header = el.getHeader();
            //>lcl|AP006852.1_cds_BAE44532.1_1 [gene=CaJ7.0001] [protein=hypothetical protein] [protein_id=BAE44532.1] [location=complement(97..1155)]
            String[] parts = header.split(" ");
            //parts[5] -->[location=complement(97..1155)]
            //Extract gene
            String gene = parts[1];    
            String[] genepart = gene.split("=");
            String genename = genepart[1];
            String[] finalpart = genename.split("]");
            String name = finalpart[0]; //name of the gene
            String seq = el.getSequence();
            
            //Gene orientation 
            String location = parts[5];
            String[] locationpart = location.split("=");
            String  orientation = locationpart[1];
            finalpart = orientation.split("\\(");
            String orientationdes = finalpart[0];
            if(orientationdes.equalsIgnoreCase("complement")) //reverse orientation   3'<---5'  negative sign
            {
                name = "-" + name;
            }
            input = input + name + " ";
            System.out.println(name); 
            System.out.println(orientationdes);
            System.out.println(seq);
            
            map.put(name, seq);
            
        }
        input = input + "|";
        input.concat("\n");
        
        System.out.println("Input :" + input);// input as it is represented in DCJ example sequence 
        return input;
        //return map;
        
    }
    public static void WriteToFile(String[] output) throws FileNotFoundException, IOException
    {
    	 File file = new File("output.txt");
      // creates the file
      file.createNewFile();
      // creates a FileWriter Object
      FileWriter writer = new FileWriter(file); 
      // Writes the content to the file
      
      for(int i = 0; i < output.length; i++)
      {
          writer.write(output[i] + "\n");
      } 
      writer.flush();
      writer.close();

      //Creates a FileReader Object
      FileReader fr = new FileReader(file); 
      char [] a = new char[50];
      fr.read(a); // reads the content to the array
      for(char c : a)
      System.out.print(c); //prints the characters one by one
      fr.close();
    }
    public static void main(final String[] args) throws IOException {
         System.out.println("Path of file");
      /*   Scanner key = new Scanner(System.in);
         String fileLocation = key.nextLine();
         System.out.println(fileLocation); */
         ReadFile(">Genome 1","C:\\Users\\chapp\\OneDrive\\BioInformatics Research\\UniMoG-Sources\\examples\\test1.txt");
         String input[] = {"abc","123","gcd"};
         WriteToFile(input);
    }
    
    
}
