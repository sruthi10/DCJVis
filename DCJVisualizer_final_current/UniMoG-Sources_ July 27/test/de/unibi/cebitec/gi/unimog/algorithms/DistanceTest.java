package de.unibi.cebitec.gi.unimog.algorithms;

import de.unibi.cebitec.gi.unimog.datastructure.AdjacencyGraph;
import de.unibi.cebitec.gi.unimog.GraphExamples;
import de.unibi.cebitec.gi.unimog.GenomeExamples;
import junit.framework.Assert;
import de.unibi.cebitec.gi.unimog.datastructure.AdditionalDataHPDistance;
import de.unibi.cebitec.gi.unimog.datastructure.Data;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author -Rolf Hilker-
 *
 * Test class for the distances.
 */
public class DistanceTest {

    private GenomeExamples genomeExamples = new GenomeExamples();
    private GraphExamples agExamples = new GraphExamples();
    private AdjacencyGraph ag2 = this.agExamples.getAG2();

    public DistanceTest() {
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
     * Tests the DCJ distance calculation.
     */
    @Test
    public void testCalculateDCJDistance() {
        DistanceDCJ distanceDCJ = new DistanceDCJ();
        Data data2 = new Data();
        data2.setAdjGraph(this.ag2);
        final int dcjDistance2 = distanceDCJ.calculateDistance(data2, null);

        Assert.assertTrue(dcjDistance2 == 11);
    }

    /**
     * Tests the HP distance calculation.
     */
    @Test
    public void testCalculateHPDistance() {
        DistanceHP distanceHP = new DistanceHP();
        Data data2 = new Data(this.genomeExamples.getGenomeC(), this.genomeExamples.getGenomeD());
        data2.setAdjGraph(this.ag2);
        AdditionalDataHPDistance additionalData2 = new AdditionalDataHPDistance(this.genomeExamples.getGenomeC());
        final int hpDistance2 = distanceHP.calculateDistance(data2, additionalData2);

        System.out.println(hpDistance2);
        Assert.assertTrue(hpDistance2 == 13);
    }
}
