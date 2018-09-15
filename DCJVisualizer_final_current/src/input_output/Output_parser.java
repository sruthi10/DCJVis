/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package input_output;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author sxc105920
 * With output file path read in the output
 */

public class Output_parser 
{
    // teseting ---> make output_parsing static
     /*  
    public static void main(String[] args) throws IOException {

       //output_parsing("C:\\Users\\sxc105920\\Box Sync\\Final_Version_DCJ_ current\\DCJVisualizer_final_Current\\output_test.txt"); //utd system
       output_parsing("C:\\Users\\chapp\\Box Sync\\Final_Version_DCJ_ current\\DCJVisualizer_final_Current\\output_test_parser_step.txt");//home computer
    } */
    
    public ArrayList<Step> output_parsing(String path) throws IOException // add path argument
    {
        //String path = "C:\\Users\\chapp\\Box Sync\\DCJ-7301\\output.txt";
        FileReader fr  = new FileReader(path);
        BufferedReader reader = new BufferedReader(fr);
        String line,previous_line = "null"; //the steps for the complete rearrangement from output.txt 
        //previous_line  null for the 1st iteration as step 0 don't have prior step
        int count = 0;
        int num_steps = 0;
        boolean step_flag = false; 
        ArrayList<Step> steps = new ArrayList<Step>();
        Step s;
        while ((line = reader.readLine()) != null) 
        {
            if(count == 0)
            {
                System.out.println("FirstLine "+ line);
                String part[] = line.split("=");
                num_steps = Integer.parseInt(part[1].replaceAll("\\W", ""));
                //System.out.println("Steps: " +steps);
                line = reader.readLine(); // skip DCJ sorting scenario of the genomes "Genome A" & "Genome B":
                
            }
            else if(line.startsWith("0.:") || step_flag && !(num_steps < 0))
            {
                step_flag = true;
                s = new Step(line);
                System.out.println(line);
                steps.add(s);      
                s.intialize(previous_line); //previous line is said so we can find what genes changed           
                num_steps--;
                previous_line = line;
            }
            else if (num_steps < 0) 
            {
                break;
            }
        count++;
        }
        fr.close();
        reader.close();
        return steps;
    }
}
