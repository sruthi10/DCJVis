package input_output;

/*
Read in path for input files and parse the header section
 * >lcl|AP006852.1_cds_BAE44532.1_1 [gene=CaJ7.0001] [protein=hypothetical protein] [protein_id=BAE44532.1] [location=complement(97..1155)]
 * to get the gene name and size


WriteTo file --> output.txt
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

public class FastaReader {

    String output = "output.txt";
    public static File outfile = new File("output.txt");

    // Map<String, String> 
    public static String ReadFile(String genomeName, File file) throws IOException {
        FASTAElementIterator it;
        //File file = new File("H:\\7000\\sequence.fa");
        //File file = new File(path);
        FASTAFileReader reader = new FASTAFileReaderImpl(file);
        Map<String, String> map = new HashMap<String, String>();
        String input = genomeName + "\n";
        it = reader.getIterator();
        while (it.hasNext()) 
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
            if(header.contains("complement"))
            {
                name = "-" + name;
            }
            input = input + name + " ";
            //System.out.println(name);
            //System.out.println(seq);

            map.put(name, seq);

        }
        input = input + "|";
        input.concat("\n");

        System.out.println("Input :" + input);// input as it is represented in DCJ example sequence 
        reader.close();
        return input;
        //return map;

    }

    public static void WriteToFile(String[] output) throws FileNotFoundException, IOException 
    {

        
        // creates the file
        if(!outfile.exists())
        {
            outfile.createNewFile();
        }
        // creates a FileWriter Object
        FileWriter writer = new FileWriter(outfile);
        // Writes the content to the file

        for (int i = 0; i < output.length; i++) {
            writer.write(output[i] + "\n");
        }
        writer.flush();
        writer.close();

    }
    public static String output_path()
    {
        String path = outfile.getAbsolutePath();
        return path;
    }

    //for testing
    public static void main(final String[] args) throws IOException 
    {
        System.out.println("Path of file");
        /*   Scanner key = new Scanner(System.in);
         String fileLocation = key.nextLine();
         System.out.println(fileLocation); */
        File n = new File("C:\\Users\\sxc105920\\Box Sync\\test1.txt");
        ReadFile(">Genome 1", n);
        String input[] = {"abc", "123", "gcd"};
        WriteToFile(input);
        System.out.println(output_path());        
    }

}
