/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.unibi.cebitec.gi.unimog.datastructure.multifurcatedTree;

import de.unibi.cebitec.gi.unimog.GenomeExamples;
import de.unibi.cebitec.gi.unimog.GraphExamples;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import de.unibi.cebitec.gi.unimog.algorithms.CompTreeGeneration;
import de.unibi.cebitec.gi.unimog.algorithms.ComponentIdentification;
import de.unibi.cebitec.gi.unimog.datastructure.AdditionalDataHPDistance;
import de.unibi.cebitec.gi.unimog.datastructure.AdjacencyGraph;
import de.unibi.cebitec.gi.unimog.datastructure.Component;
import de.unibi.cebitec.gi.unimog.datastructure.Data;
import java.util.ArrayList;
import junit.framework.Assert;

/**
 * @author -Rolf Hilker-
 *
 * Tests the CountingVisitor.
 */
public class CountingVisitorTest {

    private final ComponentIdentification ci;
	private final GenomeExamples genomeExamples = new GenomeExamples();
	private final GraphExamples graphExamples = new GraphExamples();
	private final AdjacencyGraph adjGraph = new AdjacencyGraph(this.genomeExamples.getGenomeC(), this.genomeExamples.getGenomeD());
	private final Data data = new Data(this.genomeExamples.getGenomeC(), this.genomeExamples.getGenomeD(), this.adjGraph);

	private AdditionalDataHPDistance addHPDataA;
	// private AdditionalDataHPDistance addHPDataB;

    public CountingVisitorTest() {
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

@Test
	public void testVisit() {

		//Component identification Test
		final ArrayList<Component> components = this.ci.getComponents();
		for (int i = 0; i < components.size(); ++i) {
			System.out.println("Start: " + components.get(i).getStartIndex()
					+ ", End: " + components.get(i).getEndIndex() + ", Type: "
					+ components.get(i).getType() + ", oriented: "+components.get(i).isOriented());
		}

		CompTreeGeneration compTreeGen = new CompTreeGeneration(components, this.addHPDataA.getGenomeCappedMinusArray().length);
		MultifurcatedTree compTree = compTreeGen.getComponentTree();

		final CountingVisitor countVisitor = new CountingVisitor();
		compTree.bottomUp(countVisitor);

		Assert.assertTrue(countVisitor.getNbGreyLeaves() == 2);
		Assert.assertTrue(countVisitor.getNbWhiteLeaves() == 1);
		Assert.assertTrue(countVisitor.getNbShortLeaves() == 1);
		Assert.assertTrue(countVisitor.isDangerous() == false);
		Assert.assertTrue(countVisitor.getDangerousNode() == null);


		final MultifurcatedTree tree1 = this.graphExamples.getTree1();
		final CountingVisitor countVisitor2 = new CountingVisitor();
		tree1.bottomUp(countVisitor2);

		Assert.assertTrue(countVisitor2.getNbGreyLeaves() == 2);
		Assert.assertTrue(countVisitor2.getNbWhiteLeaves() == 2);
		Assert.assertTrue(countVisitor2.getNbShortLeaves() == 2);
		Assert.assertTrue(countVisitor2.isDangerous() == true);

		final Node dangerNode = countVisitor2.getDangerousNode();
		Assert.assertTrue(dangerNode.getDepth() == 2);
		Assert.assertTrue(dangerNode.getNodeType() == NodeType.WHITE);

		final MultifurcatedTree tree2 = this.graphExamples.getTree2();
		final CountingVisitor countVisitor3 = new CountingVisitor();
		tree2.bottomUp(countVisitor3);

		Assert.assertTrue(countVisitor3.getNbGreyLeaves() == 1);
		Assert.assertTrue(countVisitor3.getNbWhiteLeaves() == 2);
		Assert.assertTrue(countVisitor3.getNbShortLeaves() == 1);
		Assert.assertTrue(countVisitor3.isDangerous() == true);

		final Node dangerNode2 = countVisitor3.getDangerousNode();
		Assert.assertTrue(dangerNode2.getDepth() == 2);
		Assert.assertTrue(dangerNode2.getNodeType() == NodeType.WHITE);
	}

  

}