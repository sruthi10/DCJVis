package de.unibi.cebitec.gi.unimog.framework;

import java.io.IOException;
import java.util.ArrayList;
import de.luschny.math.arithmetic.Xint;
import de.luschny.math.factorial.FactorialParallelPrimeSwing;
import de.unibi.cebitec.gi.unimog.algorithms.DistanceDCJ;
import de.unibi.cebitec.gi.unimog.algorithms.DistanceHP;
import de.unibi.cebitec.gi.unimog.algorithms.DistanceInv;
import de.unibi.cebitec.gi.unimog.algorithms.DistanceResDCJ;
import de.unibi.cebitec.gi.unimog.algorithms.DistanceTrans;
import de.unibi.cebitec.gi.unimog.algorithms.GenomeParser;
import de.unibi.cebitec.gi.unimog.algorithms.IntermediateGenomesGenerator;
import de.unibi.cebitec.gi.unimog.algorithms.SortingDCJ;
import de.unibi.cebitec.gi.unimog.algorithms.SortingHP;
import de.unibi.cebitec.gi.unimog.algorithms.SortingInv;
import de.unibi.cebitec.gi.unimog.algorithms.SortingRestrictedDCJ;
import de.unibi.cebitec.gi.unimog.algorithms.SortingTrans;
import de.unibi.cebitec.gi.unimog.algorithms.Utilities;
import de.unibi.cebitec.gi.unimog.datastructure.AdditionalDataHPDistance;
import de.unibi.cebitec.gi.unimog.datastructure.AdjacencyGraph;
import de.unibi.cebitec.gi.unimog.datastructure.Data;
import de.unibi.cebitec.gi.unimog.datastructure.DataFramework;
import de.unibi.cebitec.gi.unimog.datastructure.DataOutput;
import de.unibi.cebitec.gi.unimog.datastructure.Genome;
import de.unibi.cebitec.gi.unimog.datastructure.IAdditionalData;
import de.unibi.cebitec.gi.unimog.datastructure.OperationList;
import de.unibi.cebitec.gi.unimog.datastructure.Pair;
import de.unibi.cebitec.gi.unimog.exceptions.InputOutputException;
import de.unibi.cebitec.gi.unimog.utils.Constants;
import de.unibi.cebitec.gi.unimog.utils.Toolz;
import java.math.BigInteger;
import java.util.List;
import javax.swing.JOptionPane;

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
 * Main class for starting the program either in gui or console modus.
 * 
 * 
 * 1 "D:\Programmieren & Studieren\NetbeansProjects\DCJ-UniMoG\tag\2011_may\examples\GenomeExample2.txt" "D:\Programmieren & Studieren\NetbeansProjects\DCJ-UniMoG\tag\2011_may\examples\GenomeExample3.txt" "D:\Programmieren & Studieren\NetbeansProjects\DCJ-UniMoG\tag\2011_may\examples\GenomeExample5.txt"
 * 1 = Standard DCJ distance model
 * 2 = HP Distance model via DCJ
 * 3 = Inversion Distance model via DCJ
 * 4 = Translocation Distance model via DCJ
 * 5 = all distance methods which are included in the framework
 * 
 * 4
"D:\Programmieren & Studieren\NetbeansProjects\DCJ-UniMoG\tag\2011_may\examples\GenomeExample3.txt"
 * 
 */
public class MainClass {

    public static final String enterGenomesFst = "You have to enter at least 2 genomes using the correct format before you can compare them!\n";
    public static final String scenarioError = "The selected scenario is not valid, try again!";
	private static final int NB_SCENARIOS = 5;
    private Data data;
    private Scenario scenario;
    private String filepath;
    private IAdditionalData additionalData;
    private MainFrame mainFrame;
    private boolean guiMode = false;
    private DataFramework globalData;
    private DataOutput[] outputData;
    private boolean showSteps = true;
    private ArrayList<Integer> genomeIndices = new ArrayList<Integer>();
    private List<String> notifications = new ArrayList<String>();
    private boolean useOtherScenario = false;

    /**
     * Standard constructor for console modus.
     * @param scenario Determining the scenario to use
     * @param pathnames Path for the file containing the genomes
     */
    public MainClass(final Scenario scenario, final String[] pathnames) {

        //this.distances[0] = Constants.ERROR_NUM; //checks if at least one distance could be calculated or if error occurred
        try 
        {
            this.scenario = scenario;
            this.execute(scenario, pathnames, null);
        } catch (final InputOutputException exception) 
        {
            System.out.println(exception.getMessage());
        }
        final String[] printableResults = OutputPrinter.printResults(this.outputData, this.globalData.getGenomeIDs(),
                this.scenario);
        System.out.println(printableResults[0].concat(Constants.LINE_BREAK_OUTPUT));
        System.out.println(printableResults[3].concat(Constants.LINE_BREAK_OUTPUT));
        System.out.println(Constants.LINE_BREAK_OUTPUT.concat("Steps of each genome comparison:").concat(
                Constants.LINE_BREAK_OUTPUT));
        System.out.println(printableResults[1]);
        System.out.println(Constants.LINE_BREAK_OUTPUT.concat("Adjacencies of each genome comparison after each step:").concat(Constants.LINE_BREAK_OUTPUT));
        System.out.println(printableResults[2]);
        System.out.println("");
        // print error messages at the end, to focus them
        for (String msg : this.notifications) {
            System.out.println(msg);
        }
        this.notifications.clear();

    }

    /**
     * Standard constructor for gui modus.
     */
    public MainClass() {
        this.data = new Data();
    }

    /**
     * Main method initializes the program.
     * 
     * @param args 2 Options: Pass no arguments to start in gui mode Pass the number of the scenario you want to use +
     *           the path of the file containing the genomes.
     */
    public static void main(String[] args) {

        MainClass mc;
        //Passing the console parameters
        if (args.length > 1) {
            Scenario scenario = Scenario.NONE;
            try {
                scenario = Scenario.getScenario(Integer.valueOf(args[0]));
            } catch (NumberFormatException e) {
                System.err.println("First argument is not an integer!");
                System.exit(1);
            }
            String[] pathnames = new String[args.length - 1];
            System.arraycopy(args, 1, pathnames, 0, args.length - 1);
            mc = new MainClass(scenario, pathnames);

        } else if (args.length == 1 && (args[0].equals("-h") || args[0].equals("-help") || args[0].equals("--help"))) {
            System.out.println(Constants.CONSOLE_HELP);
        } 
        // GUI with no intial parameters
        else {
            System.out.println("No (correct) console parameters given. Starting Gui...");
            mc = new MainClass();
            mc.guiMode = true;
            final MainFrame mf = new MainFrame(mc); //# displaying the frame
            mc.mainFrame = mf; 
            mf.setVisible(true);
            // mf.setBlockOnOpen(true);
            // mf.open();
        }
    }

    /**
     * Returns the main frame.
     * @return The main frame
     */
    public MainFrame getMainFrame() {
        return this.mainFrame;
    }

    /**
     * Method for performing all calculations to receive the correct distances
     * according to the chosen scenario and the given file.
     * @param scenario The scenario value
     * @param pathnames The pathname of the genomes file, <code>null</code> for gui mode
     * @param input For gui mode the input is handed over, <code>null</code> for console mode
     * @throws InputOutputException exception occurs if no valid input can be detected
     */
    //being called from 
    public void execute(final Scenario scenario, final String[] pathnames, String input) throws InputOutputException {

        this.notifications.add("Note that the given results only depict one of many possible rearrangement scenarios!".concat(Constants.LINE_BREAK_OUTPUT));

        boolean validInput = false;
        final GenomeParser parser = new GenomeParser();//#setting up and to read the input
        if (pathnames != null) {
            try {
                this.globalData = parser.parseInput(pathnames);
                validInput = true;
            } catch (final IOException e) {
                this.handleIOException(e.getMessage());
            }
        } else if (input != null) {
            this.globalData = parser.readGenomes(input);//# input string 
            validInput = true;
        }

        if (validInput) {
            this.data = new Data();
            final int nbComparisons = Toolz.sum(this.globalData.getGenomes().size());
            this.outputData = new DataOutput[nbComparisons];
            // call distance methods directly

            final ArrayList<Genome> genomes = this.globalData.getGenomes();
            final int size = genomes.size();
            if (size < 2) {
                if (!this.guiMode) {
                    System.err.println(MainClass.enterGenomesFst);
                    System.exit(1);
                } else {
                    throw (new InputOutputException(MainClass.enterGenomesFst));
                }
            } else { // Distance and sorting calculations for each comparison
                int counter = 0;
                for (int i = 0; i < size; ++i) {
                    for (int j = i + 1; j < size; ++j) {
//                        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Comparing genomes {0}, {1}", 
//                                new Object[]{globalData.getGenomeIDs().get(i+1), globalData.getGenomeIDs().get(j+1)});
                        //preprocess a pair of genomes and put it into data
                        final Pair<Genome, Genome> genomePair = this.globalData.preprocessGenomePair(i, j);

                        this.addIgnoredGenesToNotifications(i, j, genomePair.getFirst().getNumberOfChromosomes()); //add ignored gene names to output

                        this.data.setGenomeA(genomePair.getFirst());
                        this.data.setGenomeB(genomePair.getSecond());
                        final AdjacencyGraph adjGraph = new AdjacencyGraph(this.data.getGenomeA(), this.data.getGenomeB());
                        this.data.setAdjGraph(adjGraph);
                        this.outputData[counter] = this.runDistCalculation(scenario, i + 1, j + 1);
                        if (this.showSteps && this.data.getGenomeA().getNumberOfGenes() > 0) {
                            final IntermediateGenomesGenerator[] intermedList = this.sort(scenario, i, j);
                            BigInteger lowerBoundSeq = this.calcLowerBound();
                            this.outputData[counter].setIntermedGenomes(intermedList);
                            this.outputData[counter].setLowerBound(lowerBoundSeq);
                        }
                        ++counter;
                        System.gc();
                    }
                }
            }
        } else {
            throw new InputOutputException(MainClass.enterGenomesFst);
        }
    }

    /**
     * Adds a string containing all ignored gene names of the current genome comparison
     * seperated by ", " to the list of notifications.
     * @param i index of fst genome of current comparison
     * @param j index of scnd genome of current comparison
     */
    private void addIgnoredGenesToNotifications(int i, int j, int numChromsGenome1) {
        String ignoredGenes = "";
        for (String ignoredGene : this.globalData.getSuspendedGenes()) {
            ignoredGenes = ignoredGenes.concat(ignoredGene).concat(", ");
        }
        if (!ignoredGenes.isEmpty()) {
            if (numChromsGenome1 > 0) {
                ignoredGenes = ignoredGenes.substring(0, ignoredGenes.length() - 2);
                this.notifications.add("- Genomes \"" + this.globalData.getGenomeIDs().get(i + 1) + "\" & \""
                        + this.globalData.getGenomeIDs().get(j + 1) + "\": The following singleton genes were ignored: " + ignoredGenes + ".");
            } else {
                this.notifications.add("- Genomes \"" + this.globalData.getGenomeIDs().get(i + 1) + "\" & \""
                        + this.globalData.getGenomeIDs().get(j + 1) + "\": Comparison not possible, since no gene is contained in both genomes.");
            }
        }
    }

    /**
     * Runs the calculations.
     * @param scenario The scenario
     * @param i index first genome
     * @param j index second genome
     */
    private DataOutput runDistCalculation(final Scenario scenario, final int i, final int j) {

        int[] distances = new int[1];
        distances[0] = Constants.ERROR_NUM;
        if (this.data.getGenomeA().getNumberOfGenes() > 0) {
            switch (scenario) {

                case DCJ: // ordinary DCJ Distance
                    distances[0] = this.calcDCJDistance();
                    break;
                    
                case DCJ_RESTRICTED:  //Restricted Distance via DCJ
                    distances[0] = this.calcRDCJDistance();
                    break;

                case HP: // HP Distance via DCJ
                    distances[0] = this.calcHPDistance();
                    if (distances[0] == Constants.ERROR_NUM) { //TODO: what is this for?
                    }
                    break;

                case INVERSION: // Inversion Distance via DCJ
                    distances[0] = this.calcInvDistance();
                    break;

                case TRANSLOCATION: // Translocation Distance via DCJ
                    distances[0] = this.calcTransDistance();
                    break;
                    
                case ALL: // Calculate all distances included in the framework
                    distances = new int[MainClass.NB_SCENARIOS];
                    distances[0] = this.calcDCJDistance();
                    distances[1] = this.calcRDCJDistance();
                    distances[2] = this.calcHPDistance();
                    distances[3] = this.calcInvDistance();
                    distances[4] = this.calcTransDistance();
                    break;
                default:
                    break;
            }
        } else {
            if (scenario != Scenario.NONE) {
                distances = new int[MainClass.NB_SCENARIOS];
                distances[0] = Constants.ERROR_NUM;
                distances[1] = Constants.ERROR_NUM;
                distances[2] = Constants.ERROR_NUM;
                distances[3] = Constants.ERROR_NUM;
                distances[4] = Constants.ERROR_NUM;
            }
        }
        return new DataOutput(i, j, distances);
    }

    /**
     * Runs the sorting algorithm corresponding to the given scenario.
     * @param scenario The scenario to use here
     * @param i index of first genome in parser
     * @param j index of second genome in parser
     * @return Intermediate Genomes Generator which contains all data for output
     * @throws InputOutputException
     */
    public IntermediateGenomesGenerator[] sort(final Scenario scenario, final int i, final int j) throws InputOutputException {

        IntermediateGenomesGenerator[] intermediateGenomes = new IntermediateGenomesGenerator[MainClass.NB_SCENARIOS];
        Genome genomeA = this.data.getGenomeA();
        Genome genomeB = this.data.getGenomeB();

        switch (scenario) {

            case DCJ: // ordinary DCJ sorting
                intermediateGenomes[0] = this.sortDCJ(i, j);
                break;
            case DCJ_RESTRICTED:            //Restricted DCJ sorting
                //check if genomes contain only linear chromosomes
                intermediateGenomes[0] = this.sortRDCJ(genomeA, genomeB, i, j);
                break;
            case HP: // HP sorting via DCJ
                // check if genomes contain only linear chromosomes
                intermediateGenomes[0] = this.sortHP(genomeA, genomeB, i, j);
                break;
            case INVERSION: // Inversion sorting via DCJ
                intermediateGenomes[0] = this.sortInv(genomeA, genomeB, i, j);
                break;
            case TRANSLOCATION: // Translocation sorting via DCJ
                intermediateGenomes[0] = this.sortTrans(genomeA, genomeB, i, j);
                break;
            case ALL: // Calculate all sorting sequences included in the framework
                // DCJ Sorting
                intermediateGenomes[0] = this.sortDCJ(i, j);
                // restricted DCJ sorting
                intermediateGenomes[1] = this.sortRDCJ(genomeA, genomeB, i, j);
                // HP Sorting
                intermediateGenomes[2] = this.sortHP(genomeA, genomeB, i, j);
                // Inversion Sorting
                intermediateGenomes[3] = this.sortInv(genomeA, genomeB, i, j);
                // Translocation Sorting
                intermediateGenomes[4] = this.sortTrans(genomeA, genomeB, i, j);
                break;
            default:
                throw new InputOutputException(MainClass.scenarioError);
        }

        return intermediateGenomes;
    }

    /**
     * Calculates the DCJ Distance.
     * @param data Data object containing the genomes.
     * @return DCJ distance
     */
    private int calcDCJDistance() {
        final DistanceDCJ calcDCJDist = new DistanceDCJ();
        int dcjDistance = calcDCJDist.calculateDistance(this.data, null);
        return dcjDistance;
    }
    
    /**
     * Calculates the restricted DCJ Distance
     * @param data Data object containing the genomes.
     * @return restricted DCJ distance
     */
    private int calcRDCJDistance() {
        final boolean linear = Utilities.onlyLinear(this.data.getGenomeA()) && Utilities.onlyLinear(this.data.getGenomeB());
        int resDCJDistance = 0;
        if (linear) {
            final DistanceResDCJ calcResDCJ = new DistanceResDCJ();
            resDCJDistance = calcResDCJ.calculateDistance(this.data, null);
        } else {
            resDCJDistance = Constants.ERROR_NUM;
        }
        return resDCJDistance;
    }

    /**
     * Calculates the HP Distance.
     * @param data Data object containing the genomes.
     * @return HP distance
     */
    private int calcHPDistance() {
        final boolean linear = Utilities.onlyLinear(this.data.getGenomeA())
                && Utilities.onlyLinear(this.data.getGenomeB());
        int hpDistance = 0;
        if (linear) {
            this.additionalData = new AdditionalDataHPDistance(this.data.getGenomeA());
            final DistanceHP calcHPDist = new DistanceHP();
            try {
                hpDistance = calcHPDist.calculateDistance(this.data, this.additionalData);
            } catch (final ClassCastException e) {
                this.handleClassCastExcep();
            }
        } else {
            hpDistance = Constants.ERROR_NUM;
        }
        return hpDistance;
    }

    /**
     * Calculates the Inversion Distance.
     * @param data Data object containing the genomes.
     * @return Inversion distance
     */
    private int calcInvDistance() {
        //check if genome contains only 1 chromosome!
        Genome genomeA = this.data.getGenomeA().clone();
        Genome genomeB = this.data.getGenomeB().clone();
        Genome genomeC = null;
        final boolean linear = Utilities.onlyLinear(genomeA) && Utilities.onlyLinear(genomeB);
        final boolean cotailed = Utilities.checkCotailed(genomeA, genomeB);
        if (!cotailed) {
            int nbGenes = genomeA.getNumberOfGenes();
            genomeA = Utilities.genomeCappingAndNumberAdaption(genomeA, nbGenes + 1, nbGenes + 2);
            genomeB = Utilities.genomeCappingAndNumberAdaption(genomeB, nbGenes + 1, nbGenes + 2);
            genomeC = Utilities.genomeCappingAndNumberAdaptionRev(this.data.getGenomeA().clone(), nbGenes + 1, nbGenes + 2);
        }
        int invDistance = 0;
        final int sumChroms = genomeA.getNumberOfChromosomes() + genomeB.getNumberOfChromosomes();
        if (linear && sumChroms == 2) {
            this.additionalData = new AdditionalDataHPDistance(genomeA);
            final DistanceInv calcInvDist = new DistanceInv();
            try {
                AdjacencyGraph cappedAdjGraph = new AdjacencyGraph(genomeA, genomeB);
                Data cappedData = new Data(genomeA, genomeB, cappedAdjGraph);
                invDistance = calcInvDist.calculateDistance(cappedData, this.additionalData);

                if (!cotailed) {//in non cotailed case check second capping distance too
                    AdditionalDataHPDistance addData2 = new AdditionalDataHPDistance(genomeC);
                    AdjacencyGraph cappedAdjGraph2 = new AdjacencyGraph(genomeC, genomeB);
                    Data cappedData2 = new Data(genomeC, genomeB, cappedAdjGraph2);
                    int invDistance2 = calcInvDist.calculateDistance(cappedData2, addData2);

                    if (invDistance2 < invDistance) {
                        invDistance = invDistance2;
                        this.useOtherScenario = true;
                    }
                }
            } catch (ClassCastException e) {
                this.handleClassCastExcep();
            }
        } else {
            invDistance = Constants.ERROR_NUM;
        }
        return invDistance;
    }

    /**
     * Calculates the Translocation Distance.
     * @param data Data object containing the genomes.
     * @return Translocation distance
     */
    private int calcTransDistance() {
        final boolean linear = Utilities.onlyLinear(this.data.getGenomeA())
                && Utilities.onlyLinear(this.data.getGenomeB());
        final boolean cotailed = Utilities.checkCotailed(this.data.getGenomeA(), this.data.getGenomeB());
        final int nbChroms = this.data.getGenomeA().getNumberOfChromosomes();
        final boolean equalNbChroms = nbChroms == this.data.getGenomeB().getNumberOfChromosomes();
        int transDistance = 0;
        if (linear && cotailed && equalNbChroms && nbChroms > 1) {
            this.additionalData = new AdditionalDataHPDistance(this.data.getGenomeA());
            final DistanceTrans calcTransDist = new DistanceTrans();
            try {
                transDistance = calcTransDist.calculateDistance(this.data, this.additionalData);
            } catch (ClassCastException e) {
                this.handleClassCastExcep();
            }
        } else {
            transDistance = Constants.ERROR_NUM;
        }
        return transDistance;
    }

    /**
     * Calculates a DCJ sorting scenario for the genomes with id i and j.
     * @param i index of genome A
     * @param j index of genome B
     * @return the intermediate genomes of the calculated sorting scenario.
     */
    private IntermediateGenomesGenerator sortDCJ(int i, int j) {
        final SortingDCJ dcjSorting = new SortingDCJ();
        OperationList result = dcjSorting.findOptSortSequence(this.data, null, this.globalData.getChromMaps().get(i));
        return new IntermediateGenomesGenerator(this.data.getGenomeA(),
                this.globalData.getBackMap(), result, this.data.getAdjGraph().getAdjacenciesGenome1(),
                this.globalData.getGenomeIDs().get(i + 1), this.globalData.getGenomeIDs().get(j + 1));
    }
    
    /**
     * Calculates a restricted DCJ sorting scenario for the genomes with id i and j.
     * @param i index of genome A
     * @param j index of genome B
     * @return the intermediate genomes of the calculated sorting scenario.
     */
    private IntermediateGenomesGenerator sortRDCJ(Genome genomeA, Genome genomeB, int i, int j) {
        boolean linear = Utilities.onlyLinear(genomeA) && Utilities.onlyLinear(genomeB);

        if (linear) {
            final SortingRestrictedDCJ resSorting = new SortingRestrictedDCJ();
            OperationList result = resSorting.findOptSortSequence(this.data, null, this.globalData.getChromMaps().get(i));
            return new IntermediateGenomesGenerator(this.data.getGenomeA(),
                    this.globalData.getBackMap(), result, this.data.getAdjGraph().getAdjacenciesGenome1(),
                    this.globalData.getGenomeIDs().get(i + 1), this.globalData.getGenomeIDs().get(j + 1));
        } else {
            this.notifications.add("- Genomes \"" + this.globalData.getGenomeIDs().get(i + 1) + "\" & \""
                    + this.globalData.getGenomeIDs().get(j + 1) + "\": HP comparison failed: Remove circular chromosomes!");
            return null;
        }
    }

    /**
     * Calculates a HP sorting scenario for the genomes A and B with id i and j.
     * @param genomeA genomeA
     * @param genomeB genomeB
     * @param i index of genome A
     * @param j index of genome B
     * @return the intermediate genomes of the calculated sorting scenario.
     */
    private IntermediateGenomesGenerator sortHP(Genome genomeA, Genome genomeB, int i, int j) {
        // check if genomes contain only linear chromosomes
        boolean linear = Utilities.onlyLinear(genomeA) && Utilities.onlyLinear(genomeB);
        if (linear) {
            // final SortingHPEasy hpSorting = new SortingHPEasy();
            final SortingHP hpSorting = new SortingHP();
            this.additionalData = new AdditionalDataHPDistance(genomeA);
            OperationList result = hpSorting.findOptSortSequence(this.data, this.additionalData, this.globalData.getChromMaps().get(i));
            return new IntermediateGenomesGenerator(genomeA,
                    this.globalData.getBackMap(), result, this.data.getAdjGraph().getAdjacenciesGenome1(),
                    this.globalData.getGenomeIDs().get(i + 1), this.globalData.getGenomeIDs().get(j + 1));
        } else {
            this.notifications.add("- Genomes \"" + this.globalData.getGenomeIDs().get(i + 1) + "\" & \""
                    + this.globalData.getGenomeIDs().get(j + 1) + "\": HP comparison failed: Remove circular chromosomes!");
            return null;
        }
    }

    /**
     * Calculates an inversion sorting scenario for the genomes A and B with id i and j.
     * @param genomeA genomeA
     * @param genomeB genomeB
     * @param i index of genome A
     * @param j index of genome B
     * @return the intermediate genomes of the calculated sorting scenario.
     */
    private IntermediateGenomesGenerator sortInv(Genome genomeA, Genome genomeB, int i, int j) {
        // check if genomes contain only 1 chromosome & are linear & co-tailed!
        boolean linear = Utilities.onlyLinear(genomeA) && Utilities.onlyLinear(genomeB);
        boolean cotailed = Utilities.checkCotailed(genomeA, genomeB);
        Genome genomeACapped = null;
        Genome genomeBCapped = null;
        Genome genomeCCapped = null;
        final int sumChroms = genomeA.getNumberOfChromosomes() + genomeB.getNumberOfChromosomes();

        if (linear && sumChroms == 2) {
            Data cappedData;

            if (!cotailed) { //then both genomes have to be capped and gene numbers adapted
                int nbGenes = genomeA.getNumberOfGenes();
                genomeACapped = Utilities.genomeCappingAndNumberAdaption(genomeA, nbGenes + 1, nbGenes + 2);
                genomeBCapped = Utilities.genomeCappingAndNumberAdaption(genomeB, nbGenes + 1, nbGenes + 2);
                genomeCCapped = Utilities.genomeCappingAndNumberAdaptionRev(genomeA, nbGenes + 1, nbGenes + 2);
                this.additionalData = new AdditionalDataHPDistance(genomeACapped);
                AdjacencyGraph cappedAdjGraph = new AdjacencyGraph(genomeACapped, genomeBCapped);
                cappedData = new Data(genomeACapped, genomeBCapped, cappedAdjGraph);
            } else {
                cappedData = this.data;
                this.additionalData = new AdditionalDataHPDistance(genomeA);
            }

            ////////////// sort genomes according to parameter stored during distance calculation /////////////
            final SortingInv invSorting = new SortingInv();
            OperationList result;
            if (!this.useOtherScenario) { //remember that chromMaps are not used here, because its unichromosomal!
                result = invSorting.findOptSortSequence(cappedData, this.additionalData, this.globalData.getChromMaps().get(i));
            } else {//in non cotailed case check second capping distance too

                AdditionalDataHPDistance addData2 = new AdditionalDataHPDistance(genomeCCapped);
                AdjacencyGraph cappedAdjGraph2 = new AdjacencyGraph(genomeCCapped, genomeBCapped);
                Data cappedData2 = new Data(genomeCCapped, genomeBCapped, cappedAdjGraph2);
                result = invSorting.findOptSortSequence(cappedData2, addData2, this.globalData.getChromMaps().get(i));
                this.useOtherScenario = false;
            }
            /////////////////////////////////////////////////////////////////////////////////////////////////////

            if (!cotailed) { //undo gene number adaption back to original gene numbers
                //also means decreasing all values in adjacency graphs and operation lists!
                result = Utilities.correctOperationList(result, genomeA.getNumberOfGenes());
            }
            return new IntermediateGenomesGenerator(genomeA,
                    this.globalData.getBackMap(), result, this.data.getAdjGraph().getAdjacenciesGenome1(),
                    this.globalData.getGenomeIDs().get(i + 1), this.globalData.getGenomeIDs().get(j + 1));
        } else {
            this.notifications.add("- Genomes \"" + this.globalData.getGenomeIDs().get(i + 1) + "\" & \""
                    + this.globalData.getGenomeIDs().get(j + 1) + "\": Inversion comparison failed. Check that: both genomes have only "
                    + "one chromosome and it is not circular.");
            return null;
        }
    }

    /**
     * Calculates a translocation sorting scenario for the genomes A and B with id i and j.
     * @param genomeA genomeA
     * @param genomeB genomeB
     * @param i index of genome A
     * @param j index of genome B
     * @return the intermediate genomes of the calculated sorting scenario.
     */
    private IntermediateGenomesGenerator sortTrans(Genome genomeA, Genome genomeB, int i, int j) {
        // check if genomes contain only linear chromosomes & are co-tailed!
        boolean linear = Utilities.onlyLinear(genomeA) && Utilities.onlyLinear(genomeB);
        boolean cotailed = Utilities.checkCotailed(genomeA, genomeB);
        int nbchroms = genomeA.getNumberOfChromosomes();
        boolean equalNbChroms = nbchroms == genomeB.getNumberOfChromosomes();
        if (linear && cotailed && equalNbChroms && nbchroms > 1) {
            final SortingTrans transSorting = new SortingTrans();
            this.additionalData = new AdditionalDataHPDistance(genomeA);
            OperationList result = transSorting.findOptSortSequence(this.data, this.additionalData, this.globalData.getChromMaps().get(i));
            return new IntermediateGenomesGenerator(genomeA,
                    this.globalData.getBackMap(), result, this.data.getAdjGraph().getAdjacenciesGenome1(),
                    this.globalData.getGenomeIDs().get(i + 1), this.globalData.getGenomeIDs().get(j + 1));
        } else {
            this.notifications.add("- Genomes \"" + this.globalData.getGenomeIDs().get(i + 1) + "\" & \""
                    + this.globalData.getGenomeIDs().get(j + 1) + "\": Translocation comparison failed. Check that: both genomes "
                    + "have an equal number of at least two cotailed chromosomes and they are not circular.");
            return null;
        }
    }

    /**
     * Method for handling a class cast exception for Additional data classes.
     */
    private void handleClassCastExcep() {
        if (!this.guiMode) {
            System.err.println("Wrong Additional Data for this scenario!");
            System.exit(1);
        } else {
            JOptionPane.showMessageDialog(this.getMainFrame(), "Wrong additional data object for this scenario!",
                    "Additonal data error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Method for handling an IOException.
     */
    private void handleIOException(String errorMsg) {
        if (!this.guiMode) {
            System.err.println(errorMsg);
            System.exit(1);
        } else {
            JOptionPane.showMessageDialog(this.getMainFrame(), errorMsg, "Input error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @return the currently selected {@link Scenario}
     */
    public Scenario getScenario() {
        return this.scenario;
    }

    /**
     * Sets the {@link Scenario}.
     * 
     * @param scenario currently selected {@link Scenario}
     */
    public void setScenario(final Scenario scenario) {
        this.scenario = scenario;
    }

    /**
     * Returns the file path.
     * @return the file path
     */
    public String getFilepath() {
        return this.filepath;
    }

    /**
     * Sets the scenario.
     * @param filepath the scenario
     */
    public void setFilepath(final String filepath) {
        this.filepath = filepath;
    }

    /**
     * Sets the data object.
     * @param data the data to set
     */
    public void setData(final Data data) {
        this.data = data;
    }

    /**
     * Returns the data object.
     * @return the data
     */
    public Data getData() {
        return this.data;
    }

    /**
     * Sets the additional data object.
     * @param additionalData the additionalData to set
     */
    public void setAdditionalData(final IAdditionalData additionalData) {
        this.additionalData = additionalData;
    }

    /**
     * Returns the additional data.
     * @return the additionalData
     */
    public IAdditionalData getAdditionalData() {
        return this.additionalData;
    }

    /**
     * Sets the option of showing the steps true or false.
     * @param showSteps
     */
    public void setShowSteps(final boolean showSteps) {
        this.showSteps = showSteps;
    }

    /**
     * Returns if show steps is true or false.
     * @return showSteps
     */
    public boolean isShowSteps() {
        return this.showSteps;
    }

    /**
     * Returns the genome inidices of the genomes which could be compared in the order
     * they were compared in. -> (gi[0] = 3 & gi[1] = 5 means comparison of genomes 3 & 5 f.e.
     * @return the genomeIndices
     */
    public ArrayList<Integer> getGenomeIndices() {
        return this.genomeIndices;
    }

    /**
     * Returns the output data after a comparison.
     * @return the outputData
     */
    public DataOutput[] getOutputData() {
        return this.outputData;
    }

    /**
     * Returns the output data after a comparison.
     * @return the outputData
     */
    public DataFramework getGlobalData() {
        return this.globalData;
    }

    /**
     * @return the list of error messages. It only contains messages, if at least
     * one genome comparison could not be carried out, because the genome formats
     * or the gene content did not match the chosen scenario.
     */
    public List<String> getNotifications() {
        return notifications;
    }

    /**
     * Clears the error message list.
     */
    public void clearNotifications() {
        this.notifications.clear();
    }

    /**
     * @return calculates the lower bound of all possible dcj sorting sequences
     * for the adjacency graph
     */
    private BigInteger calcLowerBound() {

        FactorialParallelPrimeSwing fac = new FactorialParallelPrimeSwing();
        List<Integer> compDistances = this.data.getAdjGraph().getCompDistances();
        int numeratorInt = 0;
        BigInteger numerator = new BigInteger("0");
        BigInteger denominator = new BigInteger("1");
        BigInteger product = new BigInteger("1");

        for (Integer dist : compDistances) {
            if (dist > 0) {

                numeratorInt += dist;

                Xint faculty = fac.factorial(dist);
                denominator = denominator.multiply(new BigInteger(faculty.toString()));

                int pow = (int) Math.pow((long) (dist + 1), (long) (dist - 1));
                product = product.multiply(new BigInteger(String.valueOf(pow)));
            }

        }
        numerator = new BigInteger(fac.factorial(numeratorInt).toString());

        return product.multiply(numerator.divide(denominator));
    }
}
