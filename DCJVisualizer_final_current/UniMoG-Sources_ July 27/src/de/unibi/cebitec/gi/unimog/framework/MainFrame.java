package de.unibi.cebitec.gi.unimog.framework;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTMLEditorKit;

import de.unibi.cebitec.gi.unimog.exceptions.InputOutputException;
import de.unibi.cebitec.gi.unimog.utils.Constants;
import de.unibi.cebitec.gi.unimog.utils.Toolz;
import de.unibi.cebitec.gi.unimog.utils.FastaReader;
import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;

/***************************************************************************
 *   Copyright (C) 2010 by Rolf Hilker                                     *
 *   rhilker   a t  cebitec.uni-bielefeld.de                               *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 ***************************************************************************/

/**
 * @author -Rolf Hilker-
 *
 * Main frame of the program. Assembles all visual program content. 
 */
public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final int INIT_WIDTH = 800;
    private static final int INIT_HEIGHT = 600;
    private static final int CONTROL_HEIGHT = 45;
    private static final int OFFSET = 18;
    private static final int I_GRAPHIC_OUTPUT = 2;
    public static int colorMode = Constants.OUTPUT_1;
    private final MainClass mainClass;
    private Scenario scenario = Scenario.DCJ;
    private double zoomFactor = Constants.ZOOM_FACTOR1;
    
    private JPanel northPanel;
    private JPanel northLeftPanel;
    private JPanel northRightPanel;
    private GraphicPanel graphicPanel;
    private JPanel southPanel;
    
    private JSplitPane workingSplitPane;
    private JTextArea inputTextArea;
    private JTextPane stdOutputArea;
    private JTextArea adjacenciesArea;
    private JTabbedPane outputPane;
    private JScrollPane graphicScrollPane;
    private HelpPage helpPane;
    
    // private JFrame waitingPopup;
    private DisposableRunnable waitingRun;
    private Runnable workingRun;
    private Thread waitingThread;
    private Thread workingThread;
    private Font fontGeneral;
    private ButtonGroup scenarioButtons;
    private JButton loadButton;
    private JButton clearButton;
    private JButton exampleButton;
    private JButton helpButton;
    private JButton runButton;
    private JButton saveTextButton;
    private JButton saveImageButton;
    private JButton exitButton;
    private JRadioButton dcjButton;
   // private JRadioButton hpButton;
    //private JRadioButton invButton;
    //private JRadioButton transButton;
    //private JRadioButton resDCJButton;
    //private JRadioButton allButton;
    private JCheckBox stepsCheckBox;
    private JCheckBox colorCheckBox;
    //private JComboBox colorComboBox;
    private JSlider sizeSlider;
    private File[] files;
    private JDialog dialog;

    /**
     * Main Constructor.
     * @param mainClass The main class instance.
     */
    public MainFrame(final MainClass mainClass) {
//          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        this.fontGeneral = new Font("Dialog", 1, 10);
        this.mainClass = mainClass;
        this.setTitle("Unified Model of Genomic Distance Computation via Double Cut & Join");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.initGUI();
        this.addActions();
    }

    /**
     * Initializes the gui with all its components.
     */
    private void initGUI() {

        this.setSize(MainFrame.INIT_WIDTH, MainFrame.INIT_HEIGHT);
        final Container contentPane = this.getContentPane();

        final FlowLayout thisLayout = new FlowLayout();
        contentPane.setLayout(thisLayout);

        // creating a panel for controls in the north
        this.northPanel = new JPanel();
        final GridLayout northPanelLayout = new GridLayout(1, 2);
        this.northPanel.setPreferredSize(new Dimension(this.getWidth() - MainFrame.OFFSET, MainFrame.CONTROL_HEIGHT));
        this.northPanel.setLayout(northPanelLayout);
        this.northPanel.setBorder(BorderFactory.createEtchedBorder());
        contentPane.add(this.northPanel);

        this.createInnerNorthPanels(this.northPanel);
        // creating a split pane for the main working areas in the center
        this.createSplitPane(contentPane);

        // creating a panel for controls in the south
        this.southPanel = new JPanel();
        final GridBagLayout southPanelLayout = new GridBagLayout();
        this.southPanel.setLayout(southPanelLayout);
        this.southPanel.setPreferredSize(new Dimension(this.getWidth() - MainFrame.OFFSET, MainFrame.CONTROL_HEIGHT));
        this.southPanel.setSize(this.getWidth() - MainFrame.OFFSET, MainFrame.CONTROL_HEIGHT);
        contentPane.add(this.southPanel);

        this.createSouthPanelComps(this.southPanel);

        this.setToolTips();
        this.setVisible(true);

        //enlarge size, if scenario panel cannot be shown completely
        int currWidth = this.northRightPanel.getSize().width;
        int prefWidth = this.northRightPanel.getPreferredSize().width;
        if (currWidth < prefWidth) {
            Dimension newSize = new Dimension(this.getWidth() + 2 * (prefWidth - currWidth) + 50, this.getHeight());
            this.setPreferredSize(newSize);
            this.setSize(newSize);
        }
    }

    /**
     * Creates the north panel containing the basic controls to create a scenario.
     * @param northPanel the panel to display the components on
     */
    private void createInnerNorthPanels(final JPanel northPanel) {

        // create left panel containing control buttons
        this.northLeftPanel = new JPanel();
        final GridBagLayout northLeftLayout = new GridBagLayout();
        this.northLeftPanel.setLayout(northLeftLayout);
        this.northLeftPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        northPanel.add(this.northLeftPanel);

        this.loadButton = (JButton) this.setJCompProps(new JButton("Load File(s)"), this.northLeftPanel);
        this.clearButton = (JButton) this.setJCompProps(new JButton("Clear"), this.northLeftPanel);
        this.exampleButton = (JButton) this.setJCompProps(new JButton("Example"), this.northLeftPanel);
        this.helpButton = (JButton) this.setJCompProps(new JButton("Help"), this.northLeftPanel);

      // create right panel containing scenario selection buttons
        this.northRightPanel = new JPanel();
        final GridBagLayout northRightLayout = new GridBagLayout();
        this.northRightPanel.setLayout(northRightLayout);
        this.northRightPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
       // northPanel.add(this.northRightPanel);

       // this.setJCompProps(new JLabel("Scenario: "), this.northRightPanel); //no need to store in a variable

        this.dcjButton = new JRadioButton("DCJ"); // modified
        this.dcjButton.setSelected(true);
        //this.resDCJButton = (JRadioButton) this.setJCompProps(new JRadioButton("rDCJ"), this.northRightPanel);
        //this.hpButton = (JRadioButton) this.setJCompProps(new JRadioButton("HP"), this.northRightPanel);
        //this.invButton = (JRadioButton) this.setJCompProps(new JRadioButton("Inversion"), this.northRightPanel);
        //this.transButton = (JRadioButton) this.setJCompProps(new JRadioButton("Translocation"), this.northRightPanel);
       // this.allButton = (JRadioButton) this.setJCompProps(new JRadioButton("All"), this.northRightPanel);

        // put scenario buttons in a button group
        //this.scenarioButtons = new ButtonGroup();
        //this.scenarioButtons.add(this.dcjButton);
        //this.scenarioButtons.add(this.hpButton);
        //this.scenarioButtons.add(this.invButton);
        //this.scenarioButtons.add(this.transButton);
        //this.scenarioButtons.add(this.resDCJButton);
        //this.scenarioButtons.add(this.allButton);

    }

    /**
     * Creates the split pane containing the two working areas.
     * @param contentPane The content pane of the main frame
     */
    private void createSplitPane(final Container contentPane) {

        // creating the input area
        this.inputTextArea = new JTextArea();
        final JScrollPane inputScrollPane = new JScrollPane(this.inputTextArea);

        // creating the output areas & embrace them by a tabbed pane
        this.stdOutputArea = new JTextPane();
        this.stdOutputArea.setEditorKit(new HTMLEditorKit());
        final JScrollPane outputScrollPane = new JScrollPane(this.stdOutputArea);
        this.stdOutputArea.setFont(Toolz.getFontOutput());
        this.stdOutputArea.setEditable(false);
        this.adjacenciesArea = new JTextArea();
        final JScrollPane adjacencyScrollPane = new JScrollPane(this.adjacenciesArea);
        this.adjacenciesArea.setEditable(false);
        this.graphicPanel = new GraphicPanel();
        this.graphicPanel.setLayout(new BorderLayout());
        this.graphicScrollPane = new JScrollPane(this.graphicPanel);
        this.graphicScrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
        final int scrollInc = 15;
        this.graphicScrollPane.getHorizontalScrollBar().setUnitIncrement(scrollInc);
        this.graphicScrollPane.getVerticalScrollBar().setUnitIncrement(scrollInc);

        this.outputPane = new JTabbedPane(JTabbedPane.TOP);
        this.outputPane.setFont(this.fontGeneral);
        this.outputPane.addTab("Genomes", outputScrollPane);
        this.outputPane.addTab("Adjacencies", adjacencyScrollPane);
        this.outputPane.addTab("Graphics", graphicScrollPane);

        // put both components in a split pane
        this.workingSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputScrollPane, this.outputPane);
        this.workingSplitPane.setOneTouchExpandable(true);
        this.workingSplitPane.setDividerLocation(300);
        this.workingSplitPane.setPreferredSize(new Dimension(MainFrame.INIT_WIDTH - MainFrame.OFFSET, 460));
        contentPane.add(this.workingSplitPane, FlowLayout.CENTER);
    }

    /**
     * Creates the south panel containing containing controls & output options.
     * @param southPanel the panel to display the components on
     */
    private void createSouthPanelComps(final JPanel southPanel) {

        this.runButton = (JButton) this.setJCompProps(new JButton("Run"), southPanel);
        this.stepsCheckBox = (JCheckBox) this.setJCompProps(new JCheckBox("Show Steps", true), southPanel);
        //southPanel.add(new JLabel("       Chromosome colors: "));
        //this.colorComboBox = new JComboBox(new String[] {"Uncolored", "Chromosomes"});
        //southPanel.add(this.colorComboBox);
        this.colorCheckBox = (JCheckBox) this.setJCompProps(new JCheckBox("Colored chromosomes"), southPanel);
        final int minSize = 0;
        final int maxSize = 2;
        this.sizeSlider = (JSlider) this.setJCompProps(new JSlider(JSlider.HORIZONTAL, minSize, maxSize, minSize), southPanel);
        this.sizeSlider.setPreferredSize(new Dimension(60, MainFrame.CONTROL_HEIGHT));
        this.sizeSlider.setSnapToTicks(true);
        this.sizeSlider.setMinorTickSpacing(1);
        this.sizeSlider.setPaintTicks(true);
        this.saveTextButton = (JButton) this.setJCompProps(new JButton("Save Text"), southPanel);
        this.saveImageButton = (JButton) this.setJCompProps(new JButton("Save Graphics"), southPanel);
        this.exitButton = (JButton) this.setJCompProps(new JButton("Exit"), southPanel);
    }

    /**
     * Finishes a JComponent by setting its general properties. Currently this only
     * includes to set the font to the general font defined in MainFrame.java.
     * @param comp the component
     * @return the finished JComponent
     */
    private JComponent setJCompProps(JComponent comp, JComponent parent) {
        comp.setFont(this.fontGeneral);
        parent.add(comp);
        return comp;
    }

    /**
     * Adds the tooltips to all components.
     * Explains each elements on frame include tabs to show adjacencies, graph and list of genome
     * Has to be called after creation of the components.
     */
    private void setToolTips() {
        this.loadButton.setToolTipText("Load file(s) with genome content");
        this.clearButton.setToolTipText("Clear input and output areas");
        this.exampleButton.setToolTipText("Show an input example");
        this.helpButton.setToolTipText("Show detailed help window");
        final String scenarioString = "Select scenario for genome comparisons";
        this.northRightPanel.setToolTipText(scenarioString);
        this.dcjButton.setToolTipText(scenarioString);
        //this.hpButton.setToolTipText(scenarioString);
        //this.invButton.setToolTipText(scenarioString);
        //this.transButton.setToolTipText(scenarioString);
        //this.resDCJButton.setToolTipText(scenarioString);
        //this.allButton.setToolTipText("All 4 scenarios are calculated in one run");
        this.inputTextArea.setToolTipText("Input area, enter at least two genomes");
        this.stdOutputArea.setToolTipText("Standard output of distances and genomes during comparison");
        this.adjacenciesArea.setToolTipText("Displays sets of adjacencies of genomes during comparison");
        this.graphicPanel.setToolTipText("Displays genomes during comparison graphically");
        this.runButton.setToolTipText("Runs genome comparison with selected options and given input");
        this.stepsCheckBox.setToolTipText("Select if only distances or distances and optimal sorting sequence are calculated");
        this.colorCheckBox.setToolTipText("Select color mode of the output");
        this.saveTextButton.setToolTipText("Save output from 'Genomes' and 'Adjacencies' in text file");
        this.saveImageButton.setToolTipText("Save graphical output as image file");
        this.exitButton.setToolTipText("Closes the program");
        this.sizeSlider.setToolTipText("Select size of the graphical output");

    }

    /**
     * Adds the actions to all buttons and controls.
     */
    private void addActions() 
    {
        this.loadButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                MainFrame.this.openFileChooser(Constants.OPEN);
            }
        });

        this.clearButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                MainFrame.this.inputTextArea.setText("");
                MainFrame.this.stdOutputArea.setText("");
                MainFrame.this.adjacenciesArea.setText("");
                MainFrame.this.graphicPanel.setData(null, MainFrame.this.scenario, Constants.ZOOM_FACTOR1);
                MainFrame.this.graphicPanel.getGraphics().clearRect(0, 0, MainFrame.this.graphicPanel.getWidth(),
                        MainFrame.this.graphicPanel.getHeight());
                MainFrame.this.outputPane.getSelectedComponent().repaint();
            }
        });

        this.exampleButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                MainFrame.this.inputTextArea.setText(Constants.GENOME_EXAMPLE); // GENOME_EXAMPLE part of Constants.java in utils
            }
        });

        this.helpButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (MainFrame.this.helpPane == null) {
                    MainFrame.this.helpPane = new HelpPage();
                }
                MainFrame.this.helpPane.setVisible(true);
            }
        });

        this.dcjButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                MainFrame.this.scenario = Scenario.DCJ;
            }
        });

        /*this.resDCJButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                MainFrame.this.scenario = Scenario.DCJ_RESTRICTED;
            }
        });*/
       /* this.hpButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                MainFrame.this.scenario = Scenario.HP;
            }
        }); */

       /*
        this.invButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                MainFrame.this.scenario = Scenario.INVERSION;
            }
        }); */
      /*
        this.transButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                MainFrame.this.scenario = Scenario.TRANSLOCATION;
            }
        });
       */
        /*this.allButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                MainFrame.this.scenario = Scenario.ALL;
            }
        }); */

        //# key step to see how the input is processed and stored and accessed
        this.runButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {

                MainFrame.this.waitingRun = new DisposableRunnable();
                waitingThread = new Thread(MainFrame.this.waitingRun);
                waitingThread.start();
                MainFrame.this.getWorkingRunnable();
                workingThread = new Thread(MainFrame.this.workingRun);
                workingThread.start();
                waitingThread.interrupt();
            }
        });

        this.stepsCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                if (MainFrame.this.stepsCheckBox.isSelected()) {
                    MainFrame.this.mainClass.setShowSteps(true);
                } else {
                    MainFrame.this.mainClass.setShowSteps(false);
                }
            }
        });

        this.colorCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                if (MainFrame.this.colorCheckBox.isSelected()) {
                    MainFrame.colorMode = Constants.OUTPUT_2;
                } else {
                    MainFrame.colorMode = Constants.OUTPUT_1;
                }
                MainFrame.this.graphicPanel.setColorMode();
                MainFrame.this.outputPane.getSelectedComponent().repaint();
            }
        });

        this.sizeSlider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                switch (MainFrame.this.sizeSlider.getValue()) {
                    case 0: MainFrame.this.zoomFactor = Constants.ZOOM_FACTOR1; break;
                    case 1: MainFrame.this.zoomFactor = Constants.ZOOM_FACTOR2; break;
                    case 2: MainFrame.this.zoomFactor = Constants.ZOOM_FACTOR3; break;
                    default: MainFrame.this.zoomFactor = Constants.ZOOM_FACTOR1;
                }
                MainFrame.this.graphicPanel.setZoomFactor(MainFrame.this.zoomFactor);
                if (MainFrame.this.outputPane.getSelectedIndex() != MainFrame.I_GRAPHIC_OUTPUT) {
                    MainFrame.this.outputPane.getSelectedComponent().repaint();
                }
            }
        });

        this.saveTextButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                MainFrame.this.openFileChooser(Constants.SAVE_TXT);
            }
        });

        this.saveImageButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                MainFrame.this.openFileChooser(Constants.SAVE_IMG);
            }
        });

        this.exitButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                MainFrame.this.dispose();
                System.exit(0);
            }
        });

        this.inputTextArea.addMouseListener(new MenuEvent(true));
        this.stdOutputArea.addMouseListener(new MenuEvent(false));
        this.adjacenciesArea.addMouseListener(new MenuEvent(false));

    }

    @Override
    public void validate() {
        MainFrame.this.adaptComponentSize();
        super.validate();
    }

    /**
     * Adapts the size of the components according to the window size.
     */
    private void adaptComponentSize() {
        final int offset = 55;
        this.northPanel.setPreferredSize(new Dimension(this.getWidth() - MainFrame.OFFSET, MainFrame.CONTROL_HEIGHT));
        this.workingSplitPane.setPreferredSize(new Dimension(this.getWidth() - MainFrame.OFFSET, this.getHeight()
                - (2 * MainFrame.CONTROL_HEIGHT) - offset));
        this.southPanel.setPreferredSize(new Dimension(this.getWidth() - MainFrame.OFFSET, MainFrame.CONTROL_HEIGHT));
        this.getContentPane().repaint();
    }

    /**
     * Opens a file chooser for input file selection.
     */
    private void openFileChooser(final int eventType) {
        final String error = "Cannot find look and feel for file chooser.";
        final LookAndFeel stdLookAndFeel = UIManager.getLookAndFeel();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException exception) {
            System.err.println(error);
        } catch (InstantiationException exception) {
            System.err.println(error);
        } catch (IllegalAccessException exception) {
            System.err.println(error);
        } catch (UnsupportedLookAndFeelException exception) {
            System.err.println(error);
        }
        final JFileChooser jfc = new JFileChooser();
        try {
            String directory = !(this.files == null) && !this.files[0].getAbsolutePath().equals("") ? this.files[0].getAbsolutePath() : new File(".").getCanonicalPath();
            jfc.setCurrentDirectory(new File(directory));
        } catch (final IOException exception) {
            jfc.setCurrentDirectory(null);
        }
        int option;
        if (eventType == Constants.OPEN) {
            jfc.setMultiSelectionEnabled(true);
            option = jfc.showOpenDialog(null);
        } else {
            if (eventType == Constants.SAVE_TXT) {
                jfc.setFileFilter(new FileNameExtensionFilter("txt", "txt"));
            } else {
                jfc.setFileFilter(new FileNameExtensionFilter("jpg", "jpg", "jpeg"));
            }
            option = jfc.showSaveDialog(null);
        }

        try {
            UIManager.setLookAndFeel(stdLookAndFeel);
        } catch (final UnsupportedLookAndFeelException e) {
            // Do nothing and keep old look and feel
        }
        if (option == JFileChooser.CANCEL_OPTION) {
            return;
        }
        if (eventType == Constants.OPEN) {
            this.files = jfc.getSelectedFiles();
            this.displayText(this.files);
        } else { // a filechooser for storing a file
            File file = jfc.getSelectedFile();
            String fileLocation = file.getAbsolutePath();

            if (eventType == Constants.SAVE_TXT) {
                file = new File(this.addFileExtension(fileLocation, Constants.SAVE_TXT));
            } else { // if (eventType == Constants.SAVE_IMG){
                file = new File(this.addFileExtension(fileLocation, Constants.SAVE_IMG));
            }

            if (file.exists()) {
                final int overwriteFile = JOptionPane.showConfirmDialog(jfc, "File already exists. Do you want to overwrite the existing file?",
                        "Overwrite File Dialog", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (overwriteFile == JOptionPane.YES_OPTION) {
                    if (eventType == Constants.SAVE_TXT) {
                        this.saveToTxtFile(file.getAbsolutePath());
                    } else {
                        this.saveToImgFile(file.getAbsolutePath());
                    }
                } else {
                    this.openFileChooser(eventType);
                }

            } else {
                if (eventType == Constants.SAVE_TXT) {
                    this.saveToTxtFile(file.getAbsolutePath());
                } else {
                    this.saveToImgFile(file.getAbsolutePath());
                }
            }
        }
    }

    /**
     * Adds the file extension "txt" to the output file name.
     * @param absolutePath the file path including the name
     * @param extensionType the type of extension to add (txt or jpg)
     * @return absolutePath including correct file extension
     */
    private String addFileExtension(String absolutePath, final int extensionType) {
        if (extensionType == Constants.SAVE_TXT) {
            if (!absolutePath.endsWith(".txt") && !absolutePath.endsWith(".TXT")) {
                absolutePath = absolutePath.concat(".txt");
            }
        } else {
            if (!absolutePath.endsWith(".jpg") && !absolutePath.endsWith(".jpeg")
                    && !absolutePath.endsWith(".JPG") && !absolutePath.endsWith(".JPEG")) {
                absolutePath = absolutePath.concat(".jpg");
            }
        }

        return absolutePath;
    }

    /**
     * Saves all text in the text areas to a text file.
     * @param fileLocation the location & name of the file to create
     */
    private void saveToTxtFile(String fileLocation) {
        try {
            String output = "Data generated by UniMoG (http://bibiserv.techfak.uni-bielefeld.de/dcj/webstart.html)\n\nInput:\n\n".concat(this.inputTextArea.getText()).concat("\nOutput:\n\n").
                    concat(this.stdOutputArea.getDocument().getText(0, this.stdOutputArea.getDocument().getLength())).concat("Adjacencies of each genome comparison after each step:\n\n").concat(this.adjacenciesArea.getText());
            final FileWriter fileStream = new FileWriter(fileLocation);
            final BufferedWriter outputWriter = new BufferedWriter(fileStream);
            int index = output.indexOf(Constants.LINE_BREAK);
            while (index != -1 && index < output.length()) {
                // outputWriter.write(Constants.LINE_BREAK_OUTPUT);
                outputWriter.write(output.substring(0, index));
                outputWriter.write(Constants.LINE_BREAK_OUTPUT);
                output = output.substring(index + 1);
                index = output.indexOf(Constants.LINE_BREAK);
            }
            outputWriter.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Write file error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Saves the graphics to a jpg file.
     * @param fileLocation the location & name of the file to create
     */
    private void saveToImgFile(final String fileLocation) {
        String fileLocus = this.addFileExtension(fileLocation, Constants.SAVE_IMG);
        try {
            BufferedImage bufferedImage = new BufferedImage(MainFrame.this.graphicPanel.getWidth(),
                    MainFrame.this.graphicPanel.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            final Graphics graphic = bufferedImage.createGraphics();
            MainFrame.this.graphicPanel.paintAll(graphic);

            try {
                ImageIO.write(bufferedImage, "jpg", new File(fileLocus));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Picture could not be saved, try again.", "Save Picture Error",
                        JOptionPane.ERROR_MESSAGE);

            }
        } catch (OutOfMemoryError e) {
            try {
                Robot robot = new Robot();
                Rectangle rect = this.getBounds();
                Rectangle rect2 = this.outputPane.getBounds();
                BufferedImage bufferedImage = robot.createScreenCapture(new Rectangle(rect.x + rect2.x + 13, rect.y
                        + rect2.y + 113, rect2.width - 24, rect2.height - 48));
                try {
                    ImageIO.write(bufferedImage, "jpg", new File(fileLocus));
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(this, "Picture could not be saved, try again.", "Save Picture Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                JOptionPane.showMessageDialog(this,
                        "Picture is too big for your memory, only saving screenshot of visible area.",
                        "Picture Size Problem", JOptionPane.ERROR_MESSAGE);
            } catch (AWTException e2) {
                JOptionPane.showMessageDialog(this, "Error with retrieving the picture from the GUI.",
                        "Save Picture Error", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    /**
     * Reads & displays the text from a file.
     * @param files The location of a file
     */
    private void displayText(final File[] files) {
        if (files != null) {
            this.inputTextArea.setText("");
            for (File file : files) {
                if (file != null) {
                    if (!file.exists() && !file.canRead()) {
                        JOptionPane.showMessageDialog(this, "Cannot read file from ".concat(file.getAbsolutePath()),
                                "Input Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        try {
                            final BufferedReader in = new BufferedReader(new FileReader(file));
                            String readLine = "";
                            while ((readLine = in.readLine()) != null) {
                                this.inputTextArea.append(readLine.concat("\n"));
                            }
                            in.close();
                        } catch (final FileNotFoundException e) {
                            JOptionPane.showMessageDialog(this, "Cannot find file from ".concat(file.getAbsolutePath()),
                                    "Input Error", JOptionPane.ERROR_MESSAGE);
                        } catch (final IOException e) {
                            JOptionPane.showMessageDialog(this, "Cannot read file from ".concat(file.getAbsolutePath()),
                                    "Input Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        }
    }

    /**
     * Creates the working Runnable, which is needed when the run button
     * was pressed an all calculations have to be carried out. At the end
     * it hides again the waiting popup.
     */
    private void getWorkingRunnable() {
        this.workingRun = new Runnable() {

            public void run() {
                MainFrame.this.runButton.setEnabled(false);
                MainFrame.this.northRightPanel.setEnabled(false);
                MainFrame.this.saveTextButton.setEnabled(false);
                MainFrame.this.saveImageButton.setEnabled(false);
                MainFrame.this.graphicPanel.removeAll();
                
                String fasta_input = "";
                //#final String input = MainFrame.this.inputTextArea.getText().concat("\n"); //the input as 1 line
                //ReadFile called twice for genome1 and genome2
                try 
                {                    
                    fasta_input = FastaReader.ReadFile(">Genome A","C:\\Users\\chapp\\Box Sync\\BioInformatics Research\\Versions_ DCJ _Visualizer\\Input-data-(papers-folder)\\S.cerevisiae_Chr_3.txt");
                    fasta_input.concat("\n");
                    fasta_input = fasta_input + FastaReader.ReadFile(">Genome B","C:\\Users\\chapp\\Box Sync\\BioInformatics Research\\Versions_ DCJ _Visualizer\\Input-data-(papers-folder)\\C.albicans_Chr_7.txt");
                } catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                final String input  = fasta_input;
                
                System.out.println("MainFrame.getWorkingRunnable Input from FastaReader: " + input);
                try {
                    MainFrame.this.mainClass.execute(MainFrame.this.scenario, null, input); // To MainClass.java
                    String[] printableResult = OutputPrinter.printResults(MainFrame.this.mainClass.getOutputData(),
                            MainFrame.this.mainClass.getGlobalData().getGenomeIDs(), MainFrame.this.scenario);

                    FastaReader.WriteToFile(printableResult);
                    MainFrame.this.stdOutputArea.setText("");
                    StyledDocument doc = MainFrame.this.stdOutputArea.getStyledDocument();
                    StyleConstants.setUnderline(doc.addStyle("underlined",
                            StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE)), true);
                    try {
                        for (String msg : mainClass.getNotifications()) {
                            doc.insertString(doc.getLength(), msg.concat(Constants.LINE_BREAK), doc.getStyle(StyleContext.DEFAULT_STYLE));
                        }
                        doc.insertString(doc.getLength(), Constants.LINE_BREAK, doc.getStyle(StyleContext.DEFAULT_STYLE));
                        mainClass.clearNotifications();
                        doc.insertString(doc.getLength(), printableResult[0], doc.getStyle("underlined"));
                        doc.insertString(doc.getLength(), Constants.LINE_BREAK.concat(Constants.LINE_BREAK).concat(
                                printableResult[3]).concat(Constants.LINE_BREAK).concat(Constants.LINE_BREAK).concat(
                                printableResult[1]), doc.getStyle(StyleContext.DEFAULT_STYLE));
                    } catch (BadLocationException e1) {
                        MainFrame.this.stdOutputArea.setText("Couldn't insert initial text into text pane.");
                    }
                    MainFrame.this.stdOutputArea.setPreferredSize(new Dimension(Short.MAX_VALUE, MainFrame.this.stdOutputArea.getHeight()));
                    MainFrame.this.stdOutputArea.setSize(new Dimension(Short.MAX_VALUE, MainFrame.this.stdOutputArea.getHeight()));
                    MainFrame.this.adjacenciesArea.setText(printableResult[2]);

                    MainFrame.this.graphicPanel.setData(MainFrame.this.mainClass.getOutputData(),
                            MainFrame.this.scenario, MainFrame.this.zoomFactor);
                    MainFrame.this.graphicPanel.setSize(MainFrame.this.graphicPanel.getPreferredSize());
                    MainFrame.this.outputPane.setSelectedIndex(2);
                    if (!MainFrame.this.mainClass.isShowSteps()) {
                        MainFrame.this.adjacenciesArea.setText("Showing the sorting steps is deactivated, "
                                + "switch to the genome tab in order to see distances.");
                        MainFrame.this.outputPane.setSelectedIndex(0);
                    }
                } catch (InputOutputException inputException) {
                    JOptionPane.showMessageDialog(MainFrame.this, inputException.getMessage(),
                            "No valid input Error", JOptionPane.ERROR_MESSAGE);
                    MainFrame.this.outputPane.setSelectedIndex(0);
                    MainFrame.this.inputTextArea.setText("");
                    MainFrame.this.stdOutputArea.setText("");
                    MainFrame.this.adjacenciesArea.setText("");

                
                } catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                MainFrame.this.setCursor(null);
                MainFrame.this.runButton.setEnabled(true);
                //MainFrame.this.northRightPanel.setEnabled(true);
                MainFrame.this.saveTextButton.setEnabled(true);
                MainFrame.this.saveImageButton.setEnabled(true);
                MainFrame.this.outputPane.setSelectedIndex(0);
                if (MainFrame.this.graphicPanel.getComponentCount() > 0) {
                    MainFrame.this.outputPane.setSelectedIndex(2);
                }

                MainFrame.this.dialog.dispose();
            }
        };
    }

    /**
     * Creates the runnable for the thread handling the waiting popup.
     * This popup should be displayed as long as the calculations are carried out.
     */
    private class DisposableRunnable implements Runnable {

        public void run() {

            MainFrame.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            final JPanel waitingPanel = new JPanel();
            waitingPanel.setLayout(new BorderLayout());

            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            waitingPanel.add(progressBar, BorderLayout.CENTER);

            JOptionPane pan = new JOptionPane(waitingPanel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
            dialog = pan.createDialog(MainFrame.this, "Calculating...");
            List<JButton> buttons = new ArrayList<JButton>();
            this.getButtons(pan, buttons);
            for (JButton button : buttons) {
                button.setVisible(false);
            }

            dialog.setSize(new Dimension(250, 60));
            dialog.setVisible(true);
        }

        public void dispose() {
            dialog.dispose();
        }

        private void getButtons(JComponent comp, List<JButton> buttons) {
            if (comp == null) {
                return;
            }

            for (Component c : comp.getComponents()) {
                if (c instanceof JButton) {
                    buttons.add((JButton) c);

                } else if (c instanceof JComponent) {
                    this.getButtons((JComponent) c, buttons);
                }
            }
        }
    }
}
