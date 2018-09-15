package de.unibi.cebitec.gi.unimog;


import de.unibi.cebitec.gi.unimog.datastructure.AdjacencyGraph;
import de.unibi.cebitec.gi.unimog.datastructure.multifurcatedTree.MultifurcatedTree;
import de.unibi.cebitec.gi.unimog.datastructure.multifurcatedTree.Node;
import de.unibi.cebitec.gi.unimog.datastructure.multifurcatedTree.NodeType;

/**
 * @author -Rolf Hilker-
 * 
 * Class that provides some examples for Genomes and AdjacencyGraphs
 */
public class GraphExamples {

	private GenomeExamples genomeExamples = new GenomeExamples();
	private AdjacencyGraph ag1;
	private AdjacencyGraph ag2;
	private MultifurcatedTree tree1 = new MultifurcatedTree(new Node(NodeType.BLACK, null));
	private MultifurcatedTree tree2 = new MultifurcatedTree(new Node(NodeType.BLACK, null));
	
	/**
	 * Adjacency Graph examples...
	 */
	public GraphExamples() {

		this.ag1 = new AdjacencyGraph(this.genomeExamples.getGenomeA(),
				this.genomeExamples.getGenomeB());
		this.ag2 = new AdjacencyGraph(this.genomeExamples.getGenomeC(),
				this.genomeExamples.getGenomeD());
		
	}
	
	/**
	 * Generates the example tree 1 with 2 dangerous nodes.
	 *     b
	 *   / |   \
	 *  s  s    s
	 *  |  |    |
	 *  g  w(D) g
	 *     |
	 *     s
	 *     |
	 *     w(D)
	 *     |
	 *     s
	 *     |
	 *     b
	 *     |
	 *     s
	 *   /  \
	 *   w  w
	 */
	private void generateExampleTree1(){
		Node root = this.tree1.getRoot();
		Node square1 = new Node(NodeType.SQUARE, root);
		Node node1 = new Node(NodeType.GREY, square1);
		
		Node square2 = new Node(NodeType.SQUARE, root);
		Node node2 = new Node(NodeType.WHITE, square2);
		Node square5 = new Node(NodeType.SQUARE, node2);
		Node node4 = new Node(NodeType.WHITE, square5);
		Node square3 = new Node(NodeType.SQUARE, node4);
		Node node5 = new Node(NodeType.BLACK, square3);
		Node square4 = new Node(NodeType.SQUARE, node5);
		Node node6 = new Node(NodeType.WHITE, square4);
		Node node7 = new Node(NodeType.WHITE, square4);
		
		Node square6 = new Node(NodeType.SQUARE, root);
		Node node3 = new Node(NodeType.GREY, square6);
		
	}
	
	/**
	 * Generates the example tree 2 with 1 dangerous node, 1 short leaf.
	 *     b
	 *   / |
	 *  s  s
	 *  |  |  
	 *  g  w(D)
	 *     |
	 *     s
	 *   /   \
	 *  w     b
	 *  |     |
	 *  s     s
	 *  |     |
	 *  w     w
	 */
	private void generateExampleTree2(){
		Node root = this.tree2.getRoot();
		Node square1 = new Node(NodeType.SQUARE, root);
		Node node1 = new Node(NodeType.GREY, square1);
		
		Node square2 = new Node(NodeType.SQUARE, root);
		Node node2 = new Node(NodeType.WHITE, square2);
		Node square5 = new Node(NodeType.SQUARE, node2);
		
		Node node4 = new Node(NodeType.WHITE, square5);
		Node square3 = new Node(NodeType.SQUARE, node4);
		Node node5 = new Node(NodeType.WHITE, square3);
		
		Node node3 = new Node(NodeType.BLACK, square5);
		Node square4 = new Node(NodeType.SQUARE, node3);
		Node node6 = new Node(NodeType.WHITE, square4);
		
	}

	public AdjacencyGraph getAG1() {
		return this.ag1;
	}

	public AdjacencyGraph getAG2() {
		return this.ag2;
	}
	
	/**
	 * Returns the first tree example.
	 * @return tree1
	 */
	public MultifurcatedTree getTree1(){
		this.generateExampleTree1();
		return this.tree1;
	}
	
	/**
	 * Returns the second tree example.
	 * @return tree12
	 */
	public MultifurcatedTree getTree2(){
		this.generateExampleTree2();
		return this.tree2;
	}

}
