/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unibi.cebitec.gi.unimog.algorithms;

import de.unibi.cebitec.gi.unimog.datastructure.AdjacencyGraph;
import de.unibi.cebitec.gi.unimog.GraphExamples;
import de.unibi.cebitec.gi.unimog.datastructure.Data;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author -Rolf Hilker-
 *
 * Test class for the sorting algorithms.
 */
public class SortingTest {

    private GraphExamples agExamples = new GraphExamples();
    private AdjacencyGraph ag2 = this.agExamples.getAG2();

    public SortingTest() {
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
     * Tests the greedy DCJ sorting.
     */
    @Test
    public void testFindOptSortSequence() {
        SortingDCJ sortDCJ = new SortingDCJ();
        Data data2 = new Data();
        data2.setAdjGraph(this.ag2);
        //sortDCJ.findOptSortSequence(data2, null,Genomeparser);
    }
}
