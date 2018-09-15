/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unibi.cebitec.gi.unimog.algorithms;

import de.unibi.cebitec.gi.unimog.datastructure.Chromosome;
import de.unibi.cebitec.gi.unimog.datastructure.Component;
import de.unibi.cebitec.gi.unimog.datastructure.Genome;
import de.unibi.cebitec.gi.unimog.datastructure.Pair;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import de.unibi.cebitec.gi.unimog.GenomeExamples;
import static org.junit.Assert.*;

import de.unibi.cebitec.gi.unimog.datastructure.Genome;
import junit.framework.Assert;

/**
 * @author -Rolf Hilker-
 *
 * Tests all methods in the utilities class.
 * Only the testCheckCotailed is implemented right now.
 */
public class UtilitiesTest {

    private final GenomeExamples genomeExamples = new GenomeExamples();

    public UtilitiesTest() {
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
     * Test of genomeToIntArray method, of class Utilities.
     */
    @Test
    public void testGenomeToIntArray() {
        System.out.println("genomeToIntArray");
        ArrayList<Chromosome> genome = null;
        int nbOfGenes = 0;
        boolean addCaps = false;
        int[] expResult = null;
        int[] result = Utilities.genomeToIntArray(genome, nbOfGenes, addCaps);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of genomeSizeCalculation method, of class Utilities.
     */
    @Test
    public void testGenomeSizeCalculation() {
        System.out.println("genomeSizeCalculation");
        Genome genome = null;
        int expResult = 0;
        int result = Utilities.genomeSizeCalculation(genome);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeDuplicates method, of class Utilities.
     */
    @Test
    public void testRemoveDuplicates() {
        System.out.println("removeDuplicates");
        int[] genome = null;
        int[] expResult = null;
        int[] result = Utilities.removeDuplicates(genome);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of mergeComponents method, of class Utilities.
     */
    @Test
    public void testMergeComponents() {
        System.out.println("mergeComponents");
        ArrayList<Component> phase1Comps = null;
        ArrayList<Component> phase2Comps = null;
        ArrayList expResult = null;
        ArrayList result = Utilities.mergeComponents(phase1Comps, phase2Comps);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of genomeCapping method, of class Utilities.
     */
    @Test
    public void testGenomeCapping() {
        System.out.println("genomeCapping");
        Genome genome = null;
        int frontCap = 0;
        int backCap = 0;
        int[] expResult = null;
        int[] result = Utilities.genomeCappingToIntArray(genome, frontCap, backCap);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeDuplicatesAdvanced method, of class Utilities.
     */
    @Test
    public void testRemoveDuplicatesAdvanced() {
        System.out.println("removeDuplicatesAdvanced");
        ArrayList<Genome> genomes = null;
        ArrayList expResult = null;
        ArrayList result = Utilities.removeDuplicatesAdvanced(genomes);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of onlyLinear method, of class Utilities.
     */
    @Test
    public void testOnlyLinear() {
        System.out.println("onlyLinear");
        Genome genome = null;
        boolean expResult = false;
        boolean result = Utilities.onlyLinear(genome);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Tests the check co-tailed genomes method.
     */
    @Test
    public void testCheckCotailed() {
        final Genome genome1 = this.genomeExamples.getGenomeC();
        final Genome genome2 = this.genomeExamples.getGenomeD();
        final Genome genome3 = this.genomeExamples.getGenomeE();
        final Genome genome4 = this.genomeExamples.getGenomeG();

        Assert.assertFalse(Utilities.checkCotailed(genome1, genome2));
        Assert.assertTrue(Utilities.checkCotailed(genome3, genome4));

    }

    /**
     * Test of getGenePos method, of class Utilities.
     */
    @Test
    public void testGetGenePos() {
        System.out.println("getGenePos");
        Genome genome = null;
        ArrayList expResult = null;
        ArrayList result = Utilities.getGenePos(genome);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getExtremity method, of class Utilities.
     */
    @Test
    public void testGetExtremity() {
        System.out.println("getExtremity");
        int geneExt = 0;
        boolean fstExtremity = false;
        int expResult = 0;
        int result = Utilities.getExtremity(geneExt, fstExtremity);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of assignInvolvedExtr method, of class Utilities.
     */
    @Test
    public void testAssignInvolvedExtr() {
        System.out.println("assignInvolvedExtr");
        int[] involvExtremities = null;
        int[] adjArray1 = null;
        boolean standard = false;
        int[] expResult = null;
        int[] result = Utilities.assignInvolvedExtr(involvExtremities, adjArray1, standard);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSmallerIndexFst method, of class Utilities.
     */
    @Test
    public void testGetSmallerIndexFst() {
        System.out.println("getSmallerIndexFst");
        int startIndex = 0;
        int endIndex = 0;
        Pair expResult = null;
        Pair result = Utilities.getSmallerIndexFst(startIndex, endIndex);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNeighbourExt method, of class Utilities.
     */
    @Test
    public void testGetNeighbourExt() {
        System.out.println("getNeighbourExt");
        int ext = 0;
        int expResult = 0;
        int result = Utilities.getNeighbourExt(ext);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBelongingExt method, of class Utilities.
     */
    @Test
    public void testGetBelongingExt() {
        System.out.println("getBelongingExt");
        int ext = 0;
        int expResult = 0;
        int result = Utilities.getBelongingExt(ext);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of checkExtremities method, of class Utilities.
     */
    @Test
    public void testCheckExtremities() {
        System.out.println("checkExtremities");
        int extremity1 = 0;
        int extremity2 = 0;
        int extremity3 = 0;
        int extremity4 = 0;
        Pair expResult = null;
        Pair result = Utilities.checkExtremities(extremity1, extremity2, extremity3, extremity4);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
