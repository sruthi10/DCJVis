package de.unibi.cebitec.gi.unimog.datastructure;

import de.unibi.cebitec.gi.unimog.GraphExamples;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author -Rolf Hilker-
 *
 *         Tests the adjacency graph methods.
 */
public class AdjacencyGraphTest {

    private GraphExamples graphExamples = new GraphExamples();
    private AdjacencyGraph ag1 = this.graphExamples.getAG1();
    private AdjacencyGraph ag2 = this.graphExamples.getAG2();

    public AdjacencyGraphTest() {
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

    /**
     * Tests the parseAdjacencies method.
     */
    @Test
    public void parseAdjacenciesTest() {

        System.out.println();
        Assert.assertTrue(this.ag1.getAdjacenciesGenome1()[1] == 6);
        Assert.assertTrue(this.ag1.getAdjacenciesGenome1()[6] == 1);
        Assert.assertTrue(this.ag1.getAdjacenciesGenome1()[2] == 2);
        Assert.assertTrue(this.ag1.getAdjacenciesGenome1()[3] == 10);
        Assert.assertTrue(this.ag1.getAdjacenciesGenome1()[10] == 3);
        Assert.assertTrue(this.ag1.getAdjacenciesGenome1()[4] == 9);
        Assert.assertTrue(this.ag1.getAdjacenciesGenome1()[9] == 4);
        Assert.assertTrue(this.ag1.getAdjacenciesGenome1()[5] == 7);
        Assert.assertTrue(this.ag1.getAdjacenciesGenome1()[7] == 5);
        Assert.assertTrue(this.ag1.getAdjacenciesGenome1()[8] == 8);
        Assert.assertTrue(this.ag1.getAdjacenciesGenome1()[11] == 14);
        Assert.assertTrue(this.ag1.getAdjacenciesGenome1()[12] == 12);
        Assert.assertTrue(this.ag1.getAdjacenciesGenome1()[13] == 13);
        Assert.assertTrue(this.ag1.getAdjacenciesGenome1()[14] == 11);
    }

    /**
     * Tests the calculatePathsAndCycles method.
     */
    @Test
    public void calculatePathsAndCyclesTest() {

        Assert.assertTrue(this.ag1.getNumberOfCycles() == 1);
        Assert.assertTrue(this.ag1.getNumberOfEvenPaths() == 2);
        Assert.assertTrue(this.ag1.getNumberOfOddPaths() == 2);
        Assert.assertTrue(this.ag2.getNumberOfCycles() == 3);
        Assert.assertTrue(this.ag2.getNumberOfOddPaths() == 6);
        Assert.assertTrue(this.ag2.getNumberOfEvenPaths() == 1);
    }

    /**
     * Test of abstractGene method, of class AdjacencyGraph.
     */
    @Test
    public void testAbstractGene() {
        System.out.println("abstractGene");
        int gene = 0;
        boolean left = false;
        AdjacencyGraph instance = null;
        int expResult = 0;
        int result = instance.abstractGene(gene, left);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
