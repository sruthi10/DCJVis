/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unibi.cebitec.gi.unimog.algorithms;

import de.unibi.cebitec.gi.unimog.GenomeExamples;
import junit.framework.Assert;
import de.unibi.cebitec.gi.unimog.algorithms.CompTreeGeneration;
import de.unibi.cebitec.gi.unimog.algorithms.ComponentIdentification;
import de.unibi.cebitec.gi.unimog.datastructure.AdditionalDataHPDistance;
import de.unibi.cebitec.gi.unimog.datastructure.AdjacencyGraph;
import de.unibi.cebitec.gi.unimog.datastructure.Component;
import de.unibi.cebitec.gi.unimog.datastructure.Data;
import de.unibi.cebitec.gi.unimog.datastructure.multifurcatedTree.MultifurcatedTree;
import de.unibi.cebitec.gi.unimog.datastructure.multifurcatedTree.Node;
import de.unibi.cebitec.gi.unimog.datastructure.multifurcatedTree.NodeType;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tron1c
 */
public class CompTreeGenerationTest {

    private final ComponentIdentification ci;
    private final GenomeExamples genomeExamples = new GenomeExamples();
    private AdjacencyGraph adjGraph = new AdjacencyGraph(this.genomeExamples.getGenomeC(),
            this.genomeExamples.getGenomeD());
    private Data data = new Data(this.genomeExamples.getGenomeC(),
            this.genomeExamples.getGenomeD(), this.adjGraph);
    private AdditionalDataHPDistance addHPDataA;
    // private AdditionalDataHPDistance addHPDataB;

    public CompTreeGenerationTest() {
        this.addHPDataA = new AdditionalDataHPDistance(this.genomeExamples.getGenomeC());
        // this.addHPDataB = new AdditionalDataHPDistance(this.genomeExamples.getGenomeD());

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

    /**
     * Test of generateCompTree method, of class CompTreeGeneration.
     */
    @Test
    public void testGenerateCompTree() {
        //Component identification Test
        final ArrayList<Component> components = this.ci.getComponents();
        for (int i = 0; i < components.size(); ++i) {
            System.out.println("Start: " + components.get(i).getStartIndex()
                    + ", End: " + components.get(i).getEndIndex() + ", Type: "
                    + components.get(i).getType() + ", oriented: " + components.get(i).isOriented());
        }

        CompTreeGeneration compTreeGen = new CompTreeGeneration(components, this.addHPDataA.getGenomeCappedMinusArray().length);
        MultifurcatedTree compTree = compTreeGen.getComponentTree();

        Node root = compTree.getRoot();
        Assert.assertTrue(compTree.isTreeEmpty() == false);
        Assert.assertTrue(root.getNodeType() == NodeType.BLACK);
        Assert.assertTrue(root.isRoot() == true);
        Assert.assertTrue(root.isLeaf() == false);
        Assert.assertTrue(root.getDepth() == 0);
        Assert.assertTrue(root.getParent() == null);

        Node node = root.getNodeChildren().get(0);
        Assert.assertTrue(node.getNodeType() == NodeType.SQUARE);
        Node node2 = node.getNodeChildren().firstElement();
        Node node3 = node.getNodeChildren().get(1);
        Assert.assertTrue(node2.getNodeType() == NodeType.GREY);
        Assert.assertTrue(node3.getNodeType() == NodeType.GREY);

        Node node4 = root.getNodeChildren().get(1);
        Assert.assertTrue(node4.getNodeType() == NodeType.SQUARE);
        Node node5 = node4.getNodeChildren().firstElement();
        Assert.assertTrue(node5.getNodeType() == NodeType.BLACK);
        Node node6 = node5.getNodeChildren().firstElement();
        Assert.assertTrue(node6.getNodeType() == NodeType.SQUARE);
        Node node9 = node6.getNodeChildren().firstElement();
        Assert.assertTrue(node9.getNodeType() == NodeType.WHITE);

        Node node7 = root.getNodeChildren().get(2);
        Assert.assertTrue(node7.getNodeType() == NodeType.SQUARE);
        Node node8 = node7.getNodeChildren().firstElement();
        Assert.assertTrue(node8.getNodeType() == NodeType.BLACK);
        Assert.assertTrue(node8.isLeaf() == true);
        Assert.assertTrue(node8.isRoot() == false);
        Assert.assertTrue(node8.getDepth() == 2);
    }

    /**
     * Test of getNodeType method, of class CompTreeGeneration.
     */
    @Test
    public void testGetNodeType() {
        System.out.println("getNodeType");
        Component comp = null;
        NodeType expResult = null;
        NodeType result = CompTreeGeneration.getNodeType(comp);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getComponentTree method, of class CompTreeGeneration.
     */
    @Test
    public void testGetComponentTree() {
        System.out.println("getComponentTree");
        CompTreeGeneration instance = null;
        MultifurcatedTree expResult = null;
        MultifurcatedTree result = instance.getComponentTree();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCompStarts method, of class CompTreeGeneration.
     */
    @Test
    public void testGetCompStarts() {
        System.out.println("getCompStarts");
        CompTreeGeneration instance = null;
        int[] expResult = null;
        int[] result = instance.getCompStarts();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNodeToCompMap method, of class CompTreeGeneration.
     */
    @Test
    public void testGetNodeToCompMap() {
        System.out.println("getNodeToCompMap");
        CompTreeGeneration instance = null;
        int[] expResult = null;
        int[] result = instance.getNodeToCompMap();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
