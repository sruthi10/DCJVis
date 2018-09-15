/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package input_output;

import de.unibi.cebitec.gi.unimog.exceptions.InputOutputException;
import de.unibi.cebitec.gi.unimog.framework.*;
import static input_output.FastaReader.*;
import static input_output.Output_parser.*;
import java.awt.Frame;
import java.io.File;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author sxc105920
 * this will be called with input file paths then get input string for genome1 and genome2 to pass for DCJ calculation
 * eg;
 * >Genome A
 * 1 2 3
 * 4 5
 * 
 * >Genome B
 * -3 4 
 * 1 2 5
 */
public class Main_Input_Output {

  public static void main(String[] args) throws IOException, InputOutputException 
    {
        //testing purpose . make start_method static to test
      /*  File sourceFile = this.getSequenceFile(openSequenceTextFieldSource);
        File targetFile = this.getSequenceFile(openSequenceTextFieldTarget);
       String output_file_path = start_method();
       output_parsing(output_file_path); */
        //with source code
        /*File sourceFile = new File("C:\\Users\\sxc105920\\Box Sync\\BioInformatics Research\\Versions_ DCJ _Visualizer\\Input-data-(papers-folder)\\C.albicans_Chr_7.fa");
        File targetFile = new File("C:\\Users\\sxc105920\\Box Sync\\BioInformatics Research\\Versions_ DCJ _Visualizer\\Input-data-(papers-folder)\\S.cerevisiae_Chr_3.fa");
                    
       String output_file_path = start_method(sourceFile,targetFile);
       */
    } 
    /*
        Passes the source and target genome file path to read and parse into 1 fasta_input
         mainclass.execute() to pass the input the scenario to return the printable results
         write the results to file 
     Return the Output File path
    */
    public String start_method(File source, File target) throws IOException, InputOutputException
    {
        MainClass mainclass = new MainClass();
        //MainFrame frame = new MainFrame(mainclass ,"test"); //2nd constructor Mainframe

        String fasta_input = "";
        File genome1_path, genome2_path;
        genome1_path = source;
        genome2_path = target;
        //#final String input = MainFrame.this.inputTextArea.getText().concat("\n"); //the input as 1 line
        //ReadFile called twice for genome1 and genome2
    
        fasta_input = FastaReader.ReadFile(">Genome 1", genome1_path);
        fasta_input.concat("\n");
        fasta_input = fasta_input + FastaReader.ReadFile(">Genome 2", genome2_path);
               
        //frame.getWorkingRunnable(fasta_input); // calling and passing the files within the code
        mainclass.execute(Scenario.DCJ, null, fasta_input);  // calling the execute to all DCJ steps to finish the genome rearrangement
        
        String[] printableResult = OutputPrinter.printResults(mainclass.getOutputData(),mainclass.getGlobalData().getGenomeIDs(),Scenario.DCJ);

        //#Output to file
        FastaReader.WriteToFile(printableResult);  //produce output.txt with DCJ steps                   
        //System.out.println(output_path());
       //JOptionPane.showMessageDialog(frame,"Output Location " + output_path());
        return output_path();
    }

}
