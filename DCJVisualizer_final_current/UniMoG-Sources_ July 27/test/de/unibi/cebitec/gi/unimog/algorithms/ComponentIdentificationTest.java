package de.unibi.cebitec.gi.unimog.algorithms;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.unibi.cebitec.gi.unimog.datastructure.AdditionalDataHPDistance;
import de.unibi.cebitec.gi.unimog.datastructure.AdjacencyGraph;
import de.unibi.cebitec.gi.unimog.datastructure.Component;
import de.unibi.cebitec.gi.unimog.datastructure.Data;
import junit.framework.Assert;
import de.unibi.cebitec.gi.unimog.GenomeExamples;

/**
 *         Tests the component identification algorithms. Use genomes C & D
 *         when comparing to dissertation.
 *
 * @author Rolf Hilker
 */
public class ComponentIdentificationTest {

    private final ComponentIdentification ci;
    private final GenomeExamples genomeExamples = new GenomeExamples();
    private AdditionalDataHPDistance addHPDataA;
    private AdjacencyGraph adjGraph = new AdjacencyGraph(this.genomeExamples.getGenomeC(),
            this.genomeExamples.getGenomeD());
    private Data data = new Data(this.genomeExamples.getGenomeC(),
            this.genomeExamples.getGenomeD(), this.adjGraph);
    // private AdditionalDataHPDistance addHPDataB;

    public ComponentIdentificationTest() {
        this.addHPDataA = new AdditionalDataHPDistance(this.genomeExamples.getGenomeC());
        // this.addHPDataB = new
        // AdditionalDataHPDistance(this.genomeExamples.getGenomeD());

        this.ci = new ComponentIdentification(this.data, this.addHPDataA);
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testInitChromNumbers() {

        int[] chromNumbers = this.ci.getChromosomeNbs();

        // Assert.assertTrue(chromNumbers.length == 15); //9+3*2
        // Assert.assertTrue(chromNumbers[0] == 1); //Test for genome F & G
        // Assert.assertTrue(chromNumbers[1] == 1);
        // Assert.assertTrue(chromNumbers[2] == 3);
        // Assert.assertTrue(chromNumbers[3] == 2);
        // Assert.assertTrue(chromNumbers[4] == 2);
        // Assert.assertTrue(chromNumbers[5] == 1);
        // Assert.assertTrue(chromNumbers[6] == 1);
        // Assert.assertTrue(chromNumbers[7] == 2);
        // Assert.assertTrue(chromNumbers[8] == 4);
        // Assert.assertTrue(chromNumbers[9] == 4);
        // Assert.assertTrue(chromNumbers[10] == 1);
        // Assert.assertTrue(chromNumbers[11] == 1);
        // Assert.assertTrue(chromNumbers[12] == 3);
        // Assert.assertTrue(chromNumbers[13] == 1);
        // Assert.assertTrue(chromNumbers[14] == 1);

        Assert.assertTrue(chromNumbers.length == 23); // 17+3*2
        Assert.assertTrue(chromNumbers[0] == 1); // Test for genome C & D
        Assert.assertTrue(chromNumbers[1] == 1);
        Assert.assertTrue(chromNumbers[2] == 1);
        Assert.assertTrue(chromNumbers[3] == 1);
        Assert.assertTrue(chromNumbers[4] == 1);
        Assert.assertTrue(chromNumbers[5] == 1);
        Assert.assertTrue(chromNumbers[6] == 1);
        Assert.assertTrue(chromNumbers[7] == 2);
        Assert.assertTrue(chromNumbers[8] == 2);
        Assert.assertTrue(chromNumbers[9] == 2);
        Assert.assertTrue(chromNumbers[10] == 2);
        Assert.assertTrue(chromNumbers[11] == 2);
        Assert.assertTrue(chromNumbers[12] == 2);
        Assert.assertTrue(chromNumbers[13] == 2);
        Assert.assertTrue(chromNumbers[14] == 2);
        Assert.assertTrue(chromNumbers[15] == 4);
        Assert.assertTrue(chromNumbers[16] == 4);
        Assert.assertTrue(chromNumbers[17] == 3);
        Assert.assertTrue(chromNumbers[18] == 3);
        Assert.assertTrue(chromNumbers[19] == 3);
        Assert.assertTrue(chromNumbers[20] == 3);
        Assert.assertTrue(chromNumbers[21] == 4);
        Assert.assertTrue(chromNumbers[22] == 4);
    }

    public void testPreprocessing() {
        final int nbOfGenes = this.genomeExamples.getGenomeC().getNumberOfGenes();
        // int[] arr = {18,2,1,3,5, 4, 0, 18, 6, 7, 11, 9, 10, 8, 12, 16, 0, 18,
        // 15, 14, 13, 17, 0};
        // Pair<int[], int[]> preprocessedPlus =
        // this.ci.plusCappedPreprocessing(this.addHPDataA.
        // getGenomeCappedPlusArray(), nbOfGenes);
        // //this.ci.seperateSignsAndValues(this.addHPDataA.getGenomeCappedMinusArray(),
        // false);
        // Pair<int[], int[]> preprocessedMinus =
        // this.ci.minusCappedPreprocessing(this.addHPDataA.
        // getGenomeCappedMinusArray(), nbOfGenes);

        final int[] greatestPrecursors = this.ci.getGreatestPrecursors();
        final int[] smallestPrecursors = this.ci.getSmallestPrecursors();
        final int[] greatestSuccessors = this.ci.getGreatestSuccessors();
        final int[] smallestSuccessors = this.ci.getSmallestSuccessors();

        // for (int i = 0; i<this.addHPDataA.getGenomeCappedPlusArray().length;
        // ++i){
        // System.out.println("GreatestPre: "+greatestPrecursors[i]);
        // System.out.println("SmallestPre: "+smallestPrecursors[i]);
        // System.out.println("GreatestSucc: "+greatestSuccessors[i]);
        // System.out.println("SmallestSucc: "+smallestSuccessors[i]);
        // }

        Assert.assertTrue(greatestPrecursors[0] == 18);
        Assert.assertTrue(greatestPrecursors[3] == 18);
        Assert.assertTrue(greatestPrecursors[4] == 18);
        Assert.assertTrue(greatestPrecursors[6] == 18);
        Assert.assertTrue(greatestPrecursors[8] == 18);
        Assert.assertTrue(greatestPrecursors[13] == 10);

        Assert.assertTrue(smallestPrecursors[0] == 0);
        Assert.assertTrue(smallestPrecursors[3] == 1);
        Assert.assertTrue(smallestPrecursors[5] == 3);
        Assert.assertTrue(smallestPrecursors[6] == 0);
        Assert.assertTrue(smallestPrecursors[8] == 0);
        Assert.assertTrue(smallestPrecursors[13] == 7);

        Assert.assertTrue(greatestSuccessors[0] == 18);
        Assert.assertTrue(greatestSuccessors[3] == 5);
        Assert.assertTrue(greatestSuccessors[1] == 3);
        Assert.assertTrue(greatestSuccessors[6] == 18);
        Assert.assertTrue(greatestSuccessors[7] == 18);
        Assert.assertTrue(greatestSuccessors[12] == 12);

        Assert.assertTrue(smallestSuccessors[0] == 0);
        Assert.assertTrue(smallestSuccessors[1] == 1);
        Assert.assertTrue(smallestSuccessors[6] == 0);
        Assert.assertTrue(smallestSuccessors[12] == 8);
        Assert.assertTrue(smallestSuccessors[14] == 0);


        // Assert.assertTrue(this.ci.getGreatestPrecursors()[0] == 10); //for
        // genome f, g
        // Assert.assertTrue(this.ci.getGreatestPrecursors()[3] == 8);
        // Assert.assertTrue(this.ci.getGreatestPrecursors()[4] == 0);
        // Assert.assertTrue(this.ci.getGreatestPrecursors()[6] == 10);
        // Assert.assertTrue(this.ci.getGreatestPrecursors()[8] == 10);
        // Assert.assertTrue(this.ci.getGreatestPrecursors()[13] == 7);
        //
        // Assert.assertTrue(this.ci.getSmallestPrecursors()[0] == 0);
        // Assert.assertTrue(this.ci.getSmallestPrecursors()[3] == 3);
        // Assert.assertTrue(this.ci.getSmallestPrecursors()[5] == 0);
        // Assert.assertTrue(this.ci.getSmallestPrecursors()[6] == 0);
        // Assert.assertTrue(this.ci.getSmallestPrecursors()[8] == 5);
        // Assert.assertTrue(this.ci.getSmallestPrecursors()[13] == 1);
        //
        // Assert.assertTrue(this.ci.getGreatestSuccessors()[0] == 10);
        // Assert.assertTrue(this.ci.getGreatestSuccessors()[3] == 10);
        // Assert.assertTrue(this.ci.getGreatestSuccessors()[1] == 8);
        // Assert.assertTrue(this.ci.getGreatestSuccessors()[6] == 5);
        // Assert.assertTrue(this.ci.getGreatestSuccessors()[7] == 9);
        // System.out.println(this.ci.getGreatestSuccessors()[12]);
        // Assert.assertTrue(this.ci.getGreatestSuccessors()[12] == 10);
        //
        // Assert.assertTrue(this.ci.getSmallestSuccessors()[0] == 0);
        // Assert.assertTrue(this.ci.getSmallestSuccessors()[1] == 0);
        // Assert.assertTrue(this.ci.getSmallestSuccessors()[6] == 0);
        // Assert.assertTrue(this.ci.getSmallestSuccessors()[12] == 2);
        // Assert.assertTrue(this.ci.getSmallestSuccessors()[14] == 0);
    }

    /**
     * Tests the componentIdentification phase 1 method.
     */
    @Test
    public void testComponentIdentificationPhase1() {

        //Component identification Test
        final ArrayList<Component> components = this.ci.getComponents();
        for (int i = 0; i < components.size(); ++i) {
            System.out.println("Start: " + components.get(i).getStartIndex()
                    + ", End: " + components.get(i).getEndIndex() + ", Type: "
                    + components.get(i).getType() + ", oriented: " + components.get(i).isOriented());
        }

        CompTreeGeneration compTreeGen = new CompTreeGeneration(components, this.addHPDataA.getGenomeCappedMinusArray().length);


        Assert.assertTrue(components.get(0).getStartIndex() == 0);
        Assert.assertTrue(components.get(0).getEndIndex() == 3);
        Assert.assertTrue(components.get(0).getType() == 3);
        Assert.assertTrue(components.get(0).isOriented() == false);

        Assert.assertTrue(components.get(1).getStartIndex() == 7);
        Assert.assertTrue(components.get(1).getEndIndex() == 8);
        Assert.assertTrue(components.get(1).getType() == 3);
        Assert.assertTrue(components.get(1).isOriented() == false);

        Assert.assertTrue(components.get(3).getStartIndex() == 9);
        Assert.assertTrue(components.get(3).getEndIndex() == 14);
        Assert.assertTrue(components.get(3).getType() == 1);
        Assert.assertTrue(components.get(3).isOriented() == true);

        Assert.assertTrue(components.get(4).getStartIndex() == 17);
        Assert.assertTrue(components.get(4).getEndIndex() == 20);
        Assert.assertTrue(components.get(4).getType() == 7);
        Assert.assertTrue(components.get(4).isOriented() == true);

        Assert.assertTrue(components.get(5).getStartIndex() == 21);
        Assert.assertTrue(components.get(5).getEndIndex() == 22);
        Assert.assertTrue(components.get(5).getType() == 2);
        Assert.assertTrue(components.get(5).isOriented() == false);
    }
}
