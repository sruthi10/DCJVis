/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcjvisualizer;
import ca.corefacility.gview.map.gui.GUIManager;
import ca.corefacility.gview.map.gui.open.SequenceDataFilter;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import de.unibi.cebitec.gi.unimog.exceptions.InputOutputException;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import input_output.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FileUtils;


/**
 *
 *
 */
public class FileOpenDialog extends JPanel implements ActionListener, WindowListener{
    
    
    private static final String OPEN_SOURCE = "Open Source";
    private static final String OPEN_TARGET = "Open Target";
    private static final String BUILD_MAP = "Build Map";
	
    private static JFrame dialog; //initial frame to get the location paths for source and target genome fasta files
    private static FileOpenDialog instance;

    private JFileChooser openSequenceChooserSource;
    private JTextField openSequenceTextFieldSource;
    
    private JFileChooser openSequenceChooserTarget;
    private JTextField openSequenceTextFieldTarget;

    private JComboBox layoutBox;
    

        
        
    private FileOpenDialog()
    {
        GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layoutPanel(this, layout);
		
		dialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		dialog.addWindowListener(this);
    }
    private void layoutPanel(JPanel panel, GroupLayout layout)
	{			
		JPanel descriptionPanel = makeDescriptionPanel();
		JPanel openPanel = makeOpenPanel();
		
		JButton buildMap = new JButton(BUILD_MAP);
		buildMap.addActionListener(this);
		
		GroupLayout.SequentialGroup horizontalGroup = layout.createSequentialGroup();
		horizontalGroup.addGroup(layout.createParallelGroup()
				.addComponent(descriptionPanel)
				.addComponent(openPanel)				
				.addComponent(buildMap, GroupLayout.Alignment.TRAILING));
		layout.setHorizontalGroup(horizontalGroup);
		
		GroupLayout.SequentialGroup verticalGroup = layout.createSequentialGroup();
		verticalGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(descriptionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
		verticalGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(openPanel));		
		verticalGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(buildMap));
		layout.setVerticalGroup(verticalGroup);		
	}
  private JPanel makeDescriptionPanel()
	{
		JPanel descriptionPanel = new JPanel(new BorderLayout());
		descriptionPanel.setBorder(BorderFactory.createTitledBorder("Description"));
		
		JLabel description = new JLabel("<html><span style='font-size:11px'>Select the two input files (FASTA Format):</span></html>");
                
                /////////////////////////////////////////////////////////////////////////////////////////////////
                //for jar
                description = new JLabel("<html><span style='font-size:11px'>Demo with S.cerevisiae chromosome3 genes and C.albicans chromosome7 genes: please click \"Build Map\" </span></html>");
                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		JPanel labels = new JPanel();
		labels.add(description);
		
		descriptionPanel.add(labels, BorderLayout.LINE_START);
		
		return descriptionPanel;
	}
    private JPanel makeOpenPanel()
	{
		JPanel openPanels = new JPanel();
		openPanels.setBorder(BorderFactory.createTitledBorder("Files"));
		
		JLabel sequenceLabel = new JLabel("<html><span style='font-size:11px'>Sequence Data(Source) : </span></html>");
                JLabel sequenceLabel1 = new JLabel("<html><span style='font-size:11px'>Sequence Data(Target) : </span></html>");
                
		
		openSequenceChooserSource = new JFileChooser(GUIManager.getInstance().getCurrentDirectory());
		openSequenceChooserSource.setFileSelectionMode(JFileChooser.FILES_ONLY);
		openSequenceChooserSource.setFileFilter(new SequenceDataFilter());

		openSequenceTextFieldSource = new JTextField(20);
		openSequenceTextFieldSource.setText(openSequenceChooserSource.getCurrentDirectory().getAbsolutePath()); //default directory
                                
               	
		JButton openSequenceButton = new JButton("Browse ...");
		openSequenceButton.setActionCommand(OPEN_SOURCE);
     		openSequenceButton.addActionListener(this);
		                               
                openSequenceChooserTarget = new JFileChooser(GUIManager.getInstance().getCurrentDirectory());
		openSequenceChooserTarget.setFileSelectionMode(JFileChooser.FILES_ONLY);
		openSequenceChooserTarget.setFileFilter(new SequenceDataFilter());

		openSequenceTextFieldTarget = new JTextField(20);
		openSequenceTextFieldTarget.setText(openSequenceChooserTarget.getCurrentDirectory().getAbsolutePath());
		
		JButton openSequenceButton1 = new JButton("Browse ...");
		openSequenceButton1.setActionCommand(OPEN_TARGET);
		openSequenceButton1.addActionListener(this);
                
                ///////////////////////////////////////////////////////////////
                 //Change for demo so that works for our example
                openSequenceTextFieldSource.setText("S.cerevisiae_Chr_3.fa"); //default directory
                openSequenceTextFieldTarget.setText("C.albicans_Chr_7.fa"); 
                openSequenceTextFieldSource.setEditable(true); // CHANGE when running on desktop, when jar is being created
                openSequenceTextFieldTarget.setEditable(true);// CHANGE when running on desktop, when jar is being created
                openSequenceButton.setEnabled(true);// CHANGE when running on desktop, when jar is being created
                openSequenceButton1.setEnabled(true);                                // CHANGE when running on desktop, when jar is being created
                
                
                //////////////////////////////////////////////////////////////////                
		openPanels.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		openPanels.add(sequenceLabel, "2, 2, left, default");
		openPanels.add(sequenceLabel1, "2, 4, left, default");
                
		openPanels.add(openSequenceTextFieldSource, "4, 2, fill, default");
                openPanels.add(openSequenceTextFieldTarget, "4, 4, fill, default");
                
		openSequenceTextFieldSource.setColumns(50); // to increase the width of text field and overall dialog box
		openSequenceTextFieldTarget.setColumns(50);
                
		openPanels.add(openSequenceButton, "6, 2");
                openPanels.add(openSequenceButton1, "6, 4");
		
		return openPanels;
	}
    
    private static void createInstances()
	{
		dialog = new JFrame("DCJ Visualizer: Open Files (Source and Target Genome in FASTA file");
                dialog.setSize(180, 30);
                instance = new FileOpenDialog();
		
		dialog.getContentPane().add(instance);
		
		dialog.pack();
	}
	
	public static void showOpenDialog()
	{
		if (dialog == null)
		{
			createInstances();
		}
		
		dialog.setVisible(true);
	}
	
	public static void hideOpenDialog()
	{
		if (dialog != null)
		{
			dialog.setVisible(false);
		}
	}
	
	public static FileOpenDialog getInstance()
	{
		if (dialog == null)
		{
			createInstances();
		}
		
		return instance;
	}
    
    @Override
    public void actionPerformed(ActionEvent e) {
       if (OPEN_SOURCE.equals(e.getActionCommand()))
		{
			performOpenAction(openSequenceChooserSource, openSequenceTextFieldSource);
                        
		}
       else if (OPEN_TARGET.equals(e.getActionCommand()))
		{
			 performOpenAction(openSequenceChooserTarget, openSequenceTextFieldTarget);
		}
       else if (BUILD_MAP.equals(e.getActionCommand()))
		{
           try {
               doBuild();
           } catch (IOException ex) {
               Logger.getLogger(FileOpenDialog.class.getName()).log(Level.SEVERE, null, ex);
           } catch (InputOutputException ex) {
               Logger.getLogger(FileOpenDialog.class.getName()).log(Level.SEVERE, null, ex);
           }
		}
    }

    private void performOpenAction(JFileChooser chooser, JTextField textField)
	{
		int state;
		boolean finished = false;
		
		chooser.setCurrentDirectory(GUIManager.getInstance().getCurrentDirectory());
		File chosenFile = null;
		do
		{
			state = chooser.showOpenDialog(this);
			if (state == JFileChooser.APPROVE_OPTION)
			{
				chosenFile = chooser.getSelectedFile();
				
				if (chosenFile != null && !chosenFile.exists())
				{
					JOptionPane.showMessageDialog(dialog, "File \"" + chosenFile.getAbsolutePath() + "\" does not exist.",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					finished = true;
					GUIManager.getInstance().setCurrentDirectory(chooser.getCurrentDirectory());
					
					if (chosenFile != null)
					{
						textField.setText(chosenFile.getAbsolutePath());
					}
				}
			}
			else
			{
				finished = true;
			}
		} while (!finished);
               
	}
    //processing the input files and function calls and then process step by step for display
    private void doBuild() throws IOException, InputOutputException
	{
            /* validating the input files */
            
            File sourceFile = this.getSequenceFile(openSequenceTextFieldSource);
            System.out.println("Source File" + sourceFile);
            File targetFile = this.getSequenceFile(openSequenceTextFieldTarget);
            System.out.println("Target File" + targetFile);
            if (sourceFile == null || !sourceFile.isFile() || targetFile == null || !targetFile.isFile())
            {
                JOptionPane.showMessageDialog(dialog, "Input file is not valid.","Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            GeneData.setTargetData(targetFile);//Map<String,GeneDisplayInfo> targetGeneInfo
            GeneData.setSourceData(sourceFile); //Map<String,GeneDisplayInfo> sourceGeneInfo
            
            
            hideOpenDialog();
            GeneData.DisplayMap(); // Creating initial graph from input fasta file
            Main_Input_Output m = new Main_Input_Output(); // 285 and 286 CALLING Input_Output Folder Main_Input_Output.java --> to get the parsing done of FASTA format file 
            String output_file_path = m.start_method(sourceFile, targetFile);  // parsing the input --> UniMoG DCJ MainClass.execute ( return all the dcj steps)--> store in output.txts
            //output_file_path --> use output_modified for scenario1 for paper
           ///////////////////////////////////////////////////////////////////////////////////////////////////// 
           ///for jar file can't have static path (for normal change the below line)
           //output_file_path = "C:\\Users\\chapp\\Box Sync\\Final_Version_DCJ_ current\\DCJVisualizer_final_Current\\output_modified.txt"; // for any other input CHANGE to output.txt
            File f = new File("output.txt");
            output_file_path = f.getPath();
            ////////////////////////////////////////////////////////////////////////
            Output_parser output = new Output_parser();
            ArrayList<Step> dcj_steps = output.output_parsing(output_file_path); // storing each step into array
            //GeneData.initialFrame.update();
            /*
             for each step of DCJ new map is displayed
            *////////////////////////////////////////////////
            sourceFile.deleteOnExit();
            targetFile.deleteOnExit();
            f.deleteOnExit();//remove this (just for demo on webiste)
                if(f.delete())
                {
                    System.out.println("File deleted successfully");
                }
                else
                {
                    System.out.println("Failed to delete the file");
                }
            StepsDisplay stepD = new StepsDisplay(dcj_steps.get(0),0);
            String operationType = "";
            stepD.DisplayMap(new ArrayList<>(), operationType);
            for(int i = 1;i<dcj_steps.size();i++)
            {
               StepComparer comparer = new StepComparer(dcj_steps.get(i-1),dcj_steps.get(i));
               ArrayList<String> changedGenes = comparer.CompareSteps();
               operationType = comparer.operation;
               stepD = new StepsDisplay(dcj_steps.get(i),i);
              // stepD.HighlightChangedData(comparer.CompareSteps()); 
                stepD.DisplayMap(changedGenes,operationType);
                //stepD.listFrames.update();
                //stepD.listFrames.setVisible(true);
            }
            
	}
    
    
    private File getSequenceFile(JTextField text) throws IOException
	{
           /* JarFile jarFile = new JarFile("DCJVisualizer_final_Current.jar"); 
            String path = "example/" + text.getText();
            JarEntry entry = jarFile.getJarEntry(path);
            InputStream is = FileOpenDialog.class.getResourceAsStream(text.getText());
           // InputStreamReader input = new InputStreamReader(jarFile.getInputStream(entry) );
            //System.out.println("Path"+ file);
            File file = new File(text.getText());
            FileUtils.copyInputStreamToFile(is, file); // CHECK Uncomment for JAr file (4/3/18

//File f = new File(text.getText());
            
            return file; */
            File f = new File(text.getText());
		
		return f;
	}
    @Override
    public void windowOpened(WindowEvent e) {
       
    }

    @Override
    public void windowClosing(WindowEvent e) {
        close();
    }

    @Override
    public void windowClosed(WindowEvent e) {
        
    }

    @Override
    public void windowIconified(WindowEvent e) {
       
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        
    }

    @Override
    public void windowActivated(WindowEvent e) {
        
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        
    }
    private void close()
	{
		if (GUIManager.getInstance().getNumGUIInstances() <= 0)
		{
			System.exit(0);
		}
		else
		{
			hideOpenDialog();
		}
	}
    
}
