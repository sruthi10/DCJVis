/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unibi.cebitec.gi.unimog.datastructure;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class for the representation of a splaytree.
 * @author Corinna Sickinger
 */
public class SplayTree {

    /** Array with all the nodes in the tree. 
     * The position in the array is the marker on the node at this position.
     */
    private SplayNode[] nodes;
    /** Root of the splaytree. */
    private SplayNode root;
    /** Number of chromosoms on the genome. */
    private int numberOfChromosomes;
    /** Length of the genome, the number of all genes. */
    private int genomeLength;

    /**
     * Constructor of the splaytree.
     * @param genomeA 
     *          The genome to be represented by the tree.
     */
    public SplayTree(Genome genomeA) {

        nodes = new SplayNode[2*genomeA.getNumberOfGenes() + 2*genomeA.getNumberOfChromosomes() + 2];
        numberOfChromosomes = genomeA.getNumberOfChromosomes();
        genomeLength = genomeA.getNumberOfGenes();

        ArrayList<Integer> formyNode = new ArrayList<Integer>(Arrays.asList(0));
        for (int i = 0; i < genomeA.getNumberOfChromosomes(); i++) {
            Integer[] forNode = SplayTree.intToInteger(genomeA.getChromosome(i).getGenes());
            formyNode.addAll(Arrays.asList(forNode));
            formyNode.add(0);
        }
        this.root = new SplayNode(null, formyNode);

    }

    /**
     * Initializes the SplayTree and sets all the important Informations in each Node.
     */
    public void initialize(int genomeSize) {
        this.root.initialize(this, genomeSize, 0);
        this.root.setSize();
        this.root.initializeBiggestChild();

    }

    /**
     * The Startpoint of a tree traversal.
     * @return a list with all the markers in the tree in ordered sequence.
     */
    public ArrayList<Integer> traverseTree() {
        return this.root.traverse(this.root.isReverseFlag());
    }

    /**
     * Method to find the ith node of the tree.
     * @param i
     *      The number of the node to be found.
     * @return the node on the searched position.
     */
    public SplayNode findIthNode(int i) {
        return this.root.getIthNode(i, this.root.isReverseFlag());
    }

    /**
     * To get the chromosom number of a specific node.
     * The node is splayed and the zeros (marker for a telomere) before this node
     * are counted.
     * @param node
     *      Node of which to get the chromosom number.
     * @return the chromosom number of the node in question.
     */
    public int getChromosome(SplayNode node) {
        node.splay();
        int chromNum = 0;
        if (node.isReverseFlag()) {
            if (node.getRightChild() != null) {
                chromNum = node.getRightChild().getChromosomeNumber();
            } else {
                chromNum = 1;
            }
        } else {
            if (node.getLeftChild() != null) {
                chromNum = node.getLeftChild().getChromosomeNumber();
            } else {
                chromNum = 1;
            }
        }
        return chromNum;
    }

    /**
     * Method to split one tree into two.
     * @param tosplit
     *          The root of the tree to split.
     * @param left
     *          Determines on which side the tree is split.
     *          If true, the left child of becomes the new root of a second tree.
     *          If false, the right child becomes a new root.
     * @return a pair which contains the roots of the new created trees.
     */
    public Pair<SplayNode, SplayNode> split(SplayNode tosplit, boolean left) {
        Pair<SplayNode, SplayNode> back = new Pair<SplayNode, SplayNode>(null, null);

        if (left) {
            SplayNode leftsplit = tosplit.getLeftChild();
            leftsplit.setRoot(true);
            tosplit.setLeftChild(null);
            tosplit.setRoot(true);
            back.setFirst(leftsplit);
            back.setSecond(tosplit);
        } else {
            SplayNode rightsplit = tosplit.getRightChild();
            rightsplit.setRoot(true);
            tosplit.setRightChild(null);
            tosplit.setRoot(true);
            back.setFirst(tosplit);
            back.setSecond(rightsplit);
        }

        return back;
    }

    /**
     * Merges two trees. A tree can be merged to the right or to the left of another root.
     * @param treeOne
     *          The root of the first tree.
     * @param treeTwo
     *          The root of the second tree.
     * @param left
     *          Boolean to determine on which side the trees are merged.
     *          If true, treeOne is merged to the left side of treeTwo.
     *          It becomes the new left child of treeTwo.
     *          If false, treeTwo becomes the right child of treeOne.
     * @return the root of the new tree.
     */
    public SplayNode merge(SplayNode treeOne, SplayNode treeTwo, boolean left) {

        SplayNode merged = null;
        if (left) {
            treeOne.setRoot(false);
            treeOne.setParent(treeTwo);
            treeTwo.setLeftChild(treeOne);
            merged = treeTwo;
        } else {
            treeTwo.setRoot(false);
            treeTwo.setParent(treeOne);
            treeOne.setRightChild(treeTwo);
            merged = treeOne;
        }

        return merged;
    }

    public static Integer[] intToInteger(int[] input) {
        Integer[] out = new Integer[input.length];
        for (int i = 0; i < input.length; i++) {
            out[i] = input[i];
        }
        return out;
    }

    public SplayNode getRoot() {
        return root;
    }

    public void setRoot(SplayNode root) {
        this.root = root;
    }

    public void setNode(SplayNode node, int position) {
        nodes[Math.abs(position)] = node;
    }

    public SplayNode getNode(int nodeMarker) {
        return nodes[Math.abs(nodeMarker)];
    }

    /**
     * Inserts a node into the array. All other nodes are pushed one position to the right.
     * Called if a fission is performed on the genome.
     * @param pos 
     *      Position where the node is to be inserted.
     * @param node 
     *      The node to be inserted.
     */
    public void insertNode(int pos, SplayNode node) {

        for (int i = genomeLength + numberOfChromosomes + 1; i >= genomeLength + pos; i--) {
            nodes[i + 1] = nodes[i];
        }
        nodes[genomeLength + pos] = node;
        numberOfChromosomes++;
    }

    /**
     * Deletes a node from the array. All other Nodes are pushed a position to the left.
     * Called if a fusion is performed on genome.
     * @param pos 
     *      The position of the node to be deleted.
     */
    public void deleteNode(int pos) {

        nodes[genomeLength + pos + 1] = null;
        for (int i = genomeLength + pos + 1; i <= genomeLength + numberOfChromosomes + 1; i++) {
            nodes[i] = nodes[i + 1];
        }
        numberOfChromosomes--;
    }
}
