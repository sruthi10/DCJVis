package de.unibi.cebitec.gi.unimog.datastructure;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.Assert;

/**
 * @author -Rolf Hilker-
 *
 *         Tests for all data structures.
 */
public class DatastructureTest {

    private static final StackInt INTSTACK = new StackInt(10);
    private static final Stack STACK = new Stack(10);
    private final Chromosome chromB1 = new Chromosome(new int[]{1, 2, 3, 4},
            false);
    private final Chromosome chromB2 = new Chromosome(new int[]{5, 6}, false);
    private final Chromosome chromB3 = new Chromosome(new int[]{7, 8}, false);
    private final Chromosome chromB4 = new Chromosome(new int[]{9}, false);
    private final ArrayList<Chromosome> genomeBList = new ArrayList<Chromosome>();
    private Genome genomeB;
    private AdditionalDataHPDistance addHPDataB;

    public DatastructureTest() {
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
     * Tests the Integer Stack data structure.
     */
    @Test
    public void testStackInteger() {
        DatastructureTest.INTSTACK.push(3);
        DatastructureTest.INTSTACK.push(7);
        Assert.assertTrue(DatastructureTest.INTSTACK.peek() == 7);
        Assert.assertTrue(DatastructureTest.INTSTACK.pop() == 7);
        Assert.assertTrue(DatastructureTest.INTSTACK.peek() == 3);
        Assert.assertTrue(DatastructureTest.INTSTACK.pop() == 3);
        Assert.assertTrue(DatastructureTest.INTSTACK.peek() == -1);
        Assert.assertTrue(DatastructureTest.INTSTACK.pop() == -1);
    }

    /**
     * Tests the Object Stack data structure.
     */
    @Test
    public void testStack() {
        DatastructureTest.STACK.push(3);
        DatastructureTest.STACK.push(7);
        Assert.assertTrue(((Integer) DatastructureTest.STACK.peek()) == 7);
        Assert.assertTrue(((Integer) DatastructureTest.STACK.pop()) == 7);
        Assert.assertTrue(((Integer) DatastructureTest.STACK.peek()) == 3);
        Assert.assertTrue(((Integer) DatastructureTest.STACK.pop()) == 3);
        Assert.assertTrue(((Integer) DatastructureTest.STACK.peek()) == -1);
        Assert.assertTrue(((Integer) DatastructureTest.STACK.pop()) == -1);
    }

    /**
     * Tests the Chromosome data structure.
     */
    @Test
    public void testChromosome() {

        Assert.assertTrue(this.chromB1.getGenes().length == 4);
        Assert.assertTrue(this.chromB1.getSize() == 4);
        Assert.assertTrue(this.chromB1.getGenes()[0] == 1);
        Assert.assertTrue(this.chromB1.getGenes()[1] == 2);
        Assert.assertTrue(this.chromB1.getGenes()[2] == 3);
        Assert.assertTrue(this.chromB1.getGenes()[3] == 4);
    }

    /**
     * Tests the Genome data structure.
     */
    @Test
    public void testGenome() {

        this.genomeBList.add(this.chromB1);
        this.genomeBList.add(this.chromB2);
        this.genomeBList.add(this.chromB3);
        this.genomeBList.add(this.chromB4);

        this.genomeB = new Genome(this.genomeBList);
        this.addHPDataB = new AdditionalDataHPDistance(this.genomeB);

        Assert.assertTrue(this.genomeB.getGenome().size() == 4);
        Assert.assertTrue(this.genomeB.getNumberOfChromosomes() == 4);
        Assert.assertTrue(this.genomeB.getNumberOfGenes() == 9);
        Assert.assertTrue(this.genomeB.getChromosome(1).getGenes()[1] == 6);
        Assert.assertTrue(this.genomeB.getChromosome(0).getSize() == 4);
        Assert.assertTrue(this.genomeB.getGenome().get(1).getGenes()[1] == 6);

        // Assert.assertTrue(this.addHPDataB.getGenomeAsArray().length == 17);
        // Assert.assertTrue(this.addHPDataB.getGenomeAsArray()[0] == 1);
        // Assert.assertTrue(this.addHPDataB.getGenomeAsArray()[8] == 9);

        Assert.assertTrue(this.addHPDataB.getGenomeCappedPlusArray().length == 17);
        Assert.assertTrue(this.addHPDataB.getGenomeCappedPlusArray()[0] == 0);
        Assert.assertTrue(this.addHPDataB.getGenomeCappedPlusArray()[8] == 6);

        Assert.assertTrue(this.addHPDataB.getGenomeCappedMinusArray().length == 17);
        Assert.assertTrue(this.addHPDataB.getGenomeCappedMinusArray()[0] == -10);
        Assert.assertTrue(this.addHPDataB.getGenomeCappedMinusArray()[8] == 6);

    }
}
