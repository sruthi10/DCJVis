/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unibi.cebitec.gi.unimog.datastructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Representation of a node in a splaytree.
 * @author Corinna Sickinger
 */
public class SplayNode {

    /**
     * Is node root of the tree, or not.
     */
    private boolean root;
    /**
     * The marker stored in this node.
     */
    private int marker;
    /**
     * If this flag is on, the whole subtree from this node is reversed.
     */
    private boolean reverseFlag;
    /**
     * The highest marker in all children.
     */
    private int biggestChild;
    /**
     * The chromosome number of this node.
     */
    private int chromosomeNumber;
    /**
     * The parent of this node.
     */
    private SplayNode parent;
    /**
     * Left and right child of this node. 
     */
    private SplayNode leftChild, rightChild;
    /**
     * Tells if this node is a right or a left child of its parent.
     */
    private boolean left, right;
    /**
     * The size of the subtree with this node.
     */
    private int subtreeSize;
    private SplayTree parentTree;

    /**
     * Constructor for a new splaynode. Only called if a new telomere is created.
     * Hence the marker is always 0.
     * @param newchromosomeNumber
     *          The number of the new telomere. A chromosome wiht a 0 is always the
     *          beginning of a chromosom.
     * @param nodeHolder 
     *          The splaytree, where all the nodes are stored.
     */
    public SplayNode(int newchromosomeNumber, SplayTree nodeHolder) {
        this.marker = 0;
        this.chromosomeNumber = newchromosomeNumber;
        this.parentTree = nodeHolder;
        this.reverseFlag = false;
        this.root = false;
        this.leftChild = null;
        this.rightChild = null;
    }

    /**
     * The constructor of a splaynode. With this constructor whole splaytrees are created.
     * @param parent
     *          The parent node of the splaynode to be created.
     * @param newNodes 
     *          A list with all the nodes to be inserted into the tree.
     *          The list is split in the middle. The integer of the middle node
     *          is the marker of this node. The other two halves of the list are
     *          used to create two (or more) new trees, one left and one right of
     *          this node.
     */
    public SplayNode(SplayNode parent, ArrayList<Integer> newNodes) {
        reverseFlag = false;
        
        if (parent != null) {
            this.parent = parent;
            root = false;
        } else {
            root = true;
        }
        this.parent = parent;
        if (newNodes.size() == 2) {
            this.marker = newNodes.get(0);
            this.setLeftChild(null);
            ArrayList<Integer> smallArray = new ArrayList<Integer>(Arrays.asList(newNodes.get(1)));
            this.setRightChild(new SplayNode(this, smallArray));

        } else if (newNodes.size() == 1) {
            this.marker = newNodes.get(0);
            this.setLeftChild(null);
            this.setRightChild(null);
        } else {
            int half = newNodes.size() / 2;
            this.marker = newNodes.get(half);
            List<Integer> leftList = newNodes.subList(0, half);
            ArrayList<Integer> arrayLeft = new ArrayList<Integer>(leftList);
            this.setLeftChild(new SplayNode(this, arrayLeft));
            if (newNodes.subList(half + 1, newNodes.size()).isEmpty()) {
                this.setRightChild(null);
            } else {
                List<Integer> rightList = newNodes.subList(half + 1, newNodes.size());
                ArrayList<Integer> arrayRight = new ArrayList<Integer>(rightList);
                this.setRightChild(new SplayNode(this, arrayRight));
            }
        }
    }

    /** This method initializes each node with the wright chromosome.
     * The array in the SplayTree class is filled with references to each node.
     * @param nodeHolder
     *          The parent class with all the informations about the nodes.
     * @param genomeSize
     *          The size of the genome.
     * @param chromosome
     *          The chromosome of the parent node.
     * @return The highest chromosome of this subtree.
     */
    public int initialize(SplayTree nodeHolder, int genomeSize, int chromosome) {
        //Wenn links == null -> this.chromosomnumber = chromosom
        //Wenn rechts == null -> nix
        int rightSize = 0;
        parentTree = nodeHolder;

        if (this.leftChild != null && this.rightChild != null) {
            if (this.getMarker() == 0) {
                this.chromosomeNumber = this.leftChild.initialize(nodeHolder, genomeSize, chromosome) + 1;
                rightSize = this.rightChild.initialize(nodeHolder, genomeSize, chromosomeNumber);

                nodeHolder.setNode(this, genomeSize + chromosomeNumber);

            } else {
                this.chromosomeNumber = this.leftChild.initialize(nodeHolder, genomeSize, chromosome);
                rightSize = this.rightChild.initialize(nodeHolder, genomeSize, this.chromosomeNumber);

                nodeHolder.setNode(this, marker);

            }
        } else if (this.leftChild == null && this.rightChild == null) {
            if (this.getMarker() == 0) {
                this.chromosomeNumber = chromosome + 1;
                rightSize = 0;
                nodeHolder.setNode(this, genomeSize + chromosomeNumber);
            } else {
                this.chromosomeNumber = chromosome;
                rightSize = 0;
                nodeHolder.setNode(this, marker);
            }

        } else if (this.leftChild == null) {
            if (this.getMarker() == 0) {
                this.chromosomeNumber = chromosome + 1;
                rightSize = this.rightChild.initialize(nodeHolder, genomeSize, chromosomeNumber);
                nodeHolder.setNode(this, genomeSize + chromosomeNumber);
            } else {
                this.chromosomeNumber = chromosome;
                rightSize = this.rightChild.initialize(nodeHolder, genomeSize, chromosomeNumber);
                nodeHolder.setNode(this, marker);
            }

        } else { //rightChild is null
            if (this.getMarker() == 0) {
                this.chromosomeNumber = this.leftChild.initialize(nodeHolder, genomeSize, chromosome) + 1;
                rightSize = 0;
                nodeHolder.setNode(this, genomeSize + chromosomeNumber);
            } else {
                this.chromosomeNumber = this.leftChild.initialize(nodeHolder, genomeSize, chromosome);
                rightSize = 0;
                nodeHolder.setNode(this, marker);
            }

        }
        if (rightSize > this.chromosomeNumber) {
            return rightSize;
        } else {
            return this.chromosomeNumber;
        }


    }
    
    public int getChromosomeNumber(){
        int back = 0;
        if(this.marker==0){
            back++;
        }
        if(this.leftChild!=null){
            back = back + this.leftChild.getChromosomeNumber();
        }
        if(this.rightChild!=null){
            back = back + this.rightChild.getChromosomeNumber();
        }
        return back;
    }

    public ArrayList<Integer> traverse(boolean direction) {
        ArrayList<Integer> back = new ArrayList<Integer>();
        if (direction && reverseFlag) {
            direction = false;
        } else if ((!direction) && (reverseFlag)) {
            direction = true;
        }
        if (!direction) {
            if ((leftChild == null) && (rightChild == null)) {
                back.add(this.marker);
            } else if (leftChild == null) {
                back.add(this.marker);
                back.addAll(rightChild.traverse(direction));
            } else if (rightChild == null) {
                back.addAll(leftChild.traverse(direction));
                back.add(this.marker);
            } else {
                back.addAll(leftChild.traverse(direction));
                back.add(this.marker);
                back.addAll(rightChild.traverse(direction));
            }
        } else {
            if ((leftChild == null) && (rightChild == null)) {
                back.add(-this.marker);
            } else if (leftChild == null) {
                back.addAll(rightChild.traverse(direction));
                back.add(-this.marker);
            } else if (rightChild == null) {
                back.add(-this.marker);
                back.addAll(leftChild.traverse(direction));
            } else {
                back.addAll(rightChild.traverse(direction));
                back.add(-this.marker);
                back.addAll(leftChild.traverse(direction));
            }
        }
        return back;
    }

    /**
     * The reverse flag is push down to one plain deeper in the tree.
     */
    public void pushDown() {
        SplayNode temp = this.leftChild;
        this.flipFlag();
        this.flipMarker();
        this.setLeftChild(this.rightChild);
        this.setRightChild(temp);
        if (this.leftChild != null) {
            this.leftChild.flipFlag();
        }
        if (this.rightChild != null) {
            this.rightChild.flipFlag();
        }
    }

    /**
     * This method does a right rotation on the tree.
     */
    public void rightRotate() {
        if (!parent.isRoot()) {
            SplayNode grandparent = parent.getParent();
            boolean rightOfGp = parent.isRight();
            SplayNode temp = this.getParent();
            temp.setLeftChild(this.getRightChild());
            if (temp.getLeftChild() != null) {
                temp.getLeftChild().setParent(temp);
            }
            this.setRightChild(temp);
            temp.setParent(null);
            temp.setParent(this);
            this.setParent(null);
            this.setParent(grandparent);
            if (rightOfGp) {
                grandparent.setRightChild(this);
                this.setRight(true);
                this.setLeft(false);
            } else {
                grandparent.setLeftChild(this);
                this.setLeft(true);
                this.setRight(false);
            }
        } else {
            SplayNode temp = this.getParent();
            temp.setLeftChild(this.getRightChild());
            if (temp.getLeftChild() != null) {
                temp.getLeftChild().setParent(temp);
            }
            this.setRightChild(temp);
            temp.setParent(null);
            temp.setParent(this);
            this.setRoot(true);
        }

        //  this.setSize();
        //  rightChild.setBiggestChild();
        //  this.setBiggestChild();
    }

    /**
     * This method does a left rotation an the tree.
     */
    public void leftRotate() {
        if (!parent.isRoot()) {
            SplayNode grandparent = parent.getParent();
            boolean rightOfGp = parent.isRight();
            SplayNode temp = this.getParent();
            temp.setRightChild(this.getLeftChild());
            if (temp.getRightChild() != null) {
                temp.getRightChild().setParent(temp);
            }
            this.setLeftChild(temp);
            temp.setParent(null);
            temp.setParent(this);
            this.setParent(null);
            this.setParent(grandparent);
            if (rightOfGp) {
                grandparent.setRightChild(this);
                this.setRight(true);
                this.setLeft(false);
            } else {
                grandparent.setLeftChild(this);
                this.setLeft(true);
                this.setRight(false);
            }

        } else {
            SplayNode temp = this.getParent();
            temp.setRightChild(this.getLeftChild());
            if (temp.getRightChild() != null) {
                temp.getRightChild().setParent(temp);
            }
            this.setLeftChild(temp);
            temp.setParent(null);
            temp.setParent(this);
            this.setRoot(true);
        }

        //this.setSize();
        //  leftChild.setBiggestChild();
        //  this.setBiggestChild();

    }

    /**
     * The splay operation.
     * Most essential operation in a splaytree.
     */
    public void splay() {

        if (this.reverseFlag) {
            this.pushDown();
        }

        if (this.parent != null && this.parent.getParent() != null && this.parent.getParent().isReverseFlag()) {
            this.parent.getParent().pushDown();
        }
        if (this.parent != null && this.parent.isReverseFlag()) {
            this.parent.pushDown();
        }

        if (this.isReverseFlag()) {
            this.pushDown();
        }
        if (this.isRoot()) {
            parentTree.setRoot(this);
            parentTree.getRoot().setSize();
            parentTree.getRoot().initializeBiggestChild();
            return;
        } else if (parent.isRoot()) {
            // Zig Step.
            if (right) {
                leftRotate();
                parentTree.setRoot(this);
                parentTree.getRoot().setSize();
                parentTree.getRoot().initializeBiggestChild();
            } else {
                rightRotate();
                parentTree.setRoot(this);
                parentTree.getRoot().setSize();
                parentTree.getRoot().initializeBiggestChild();
            }

        } else {
            // Zig-zig step for right respectivly left children.
            if (right && parent.isRight()) {
                parent.leftRotate();
                this.leftRotate();
            } else if (left && parent.isLeft()) {
                parent.rightRotate();
                this.rightRotate();
                // Zig-zag step.
            } else if (right && parent.isLeft()) {
                this.leftRotate();
                this.rightRotate();
            } else if (left && parent.isRight()) {
                this.rightRotate();
                this.leftRotate();
            }
            splay();
        }
    }

    /**Gets the ith Node of the tree.
     * Countig starts at 0.
     * 
     * @param i number of the searched Node.
     * @param flag the flag of the parent, to decide in which direction to go.
     * @return the ith Node
     */
    public SplayNode getIthNode(int i, boolean flag) {
        boolean myflag = flag;
        if (flag && reverseFlag) {
            myflag = false;
        } else if (!flag && reverseFlag) {
            myflag = true;
        }
        if (this.leftChild != null && this.rightChild != null) {
            if (myflag) {
                int rightsize = this.rightChild.getSubtreeSize();
                if (i < rightsize) {
                    return this.rightChild.getIthNode(i, myflag);
                } else if (i > rightsize) {
                    return this.leftChild.getIthNode(i - rightsize - 1, myflag);
                } else {
                    return this;
                }
            } else {
                int leftsize = this.leftChild.getSubtreeSize();
                if (i < leftsize) {
                    return this.leftChild.getIthNode(i, myflag);
                } else if (i > leftsize) {
                    return this.rightChild.getIthNode(i - leftsize - 1, myflag);
                } else {
                    return this;
                }
            }
        } else if (this.leftChild == null && this.rightChild == null) {
            return this;
        } else if (this.leftChild == null) {
            if (myflag) {
                int rightsize = this.rightChild.getSubtreeSize();
                if (i < rightsize) {
                    return this.rightChild.getIthNode(i, myflag);
                } else {
                    return this;
                }
            } else {
                if (i > 0) {
                    return this.rightChild.getIthNode(i - 1, myflag);
                } else {
                    return this;
                }
            }
        //Case rightChild is null
        } else {
            if (myflag) {
                if(i>0){
                    return this.leftChild.getIthNode(i - 1, myflag);
                } else {
                    return this;
                }
            } else {
                int leftsize = this.leftChild.getSubtreeSize();
                if(i<leftsize){
                    return this.leftChild.getIthNode(i, myflag);
                } else {
                    return this;
                }
            }
            
        }


    }

    /**Gets the Position of this Node in the tree.
     * 
     * @return the position of a Node.
     * 
     */
    public int getPosition() {
        this.splay();
        if (this.isReverseFlag()) {
            if (this.rightChild != null) {
                return this.rightChild.getSubtreeSize() + 1;
            } else {
                return 1;
            }
        } else {
            if (this.leftChild != null) {
                return this.leftChild.getSubtreeSize() + 1;
            } else {
                return 1;
            }
        }
    }

    /**In this Method the smallest Node in this tree, the first Knode is returned.
     * 
     * @return first Knode
     */
    public SplayNode getPositionSmallestNode(boolean flag) {
        boolean myflag = flag;
        if (flag && reverseFlag) {
            myflag = false;
        } else if (!flag && reverseFlag) {
            myflag = true;
        }
        if (myflag) {
            if (rightChild == null) {
                return this;
            } else {
                return rightChild.getPositionSmallestNode(myflag);
            }
        } else {
            if (leftChild == null) {
                return this;
            } else {
                return leftChild.getPositionSmallestNode(myflag);
            }
        }

    }

    /**Here the biggest Node, the last Knode in the tree is returned.
     * 
     * @return last Knode 
     */
    public SplayNode getPositionBiggestNode(boolean flag) {
        boolean myflag = flag;
        if (flag && reverseFlag) {
            myflag = false;
        } else if (!flag && reverseFlag) {
            myflag = true;
        }
        if (myflag) {
            if (leftChild == null) {
                return this;
            } else {
                return leftChild.getPositionBiggestNode(myflag);
            }
        } else {
            if (rightChild == null) {
                return this;
            } else {
                return rightChild.getPositionBiggestNode(myflag);
            }
        }
    }

    public int setSize() {
        if ((leftChild == null) && (rightChild == null)) {
            subtreeSize = 1;
        } else if (leftChild == null) {
            subtreeSize = rightChild.setSize() + 1;
        } else if (rightChild == null) {
            subtreeSize = leftChild.setSize() + 1;
        } else {
            subtreeSize = leftChild.setSize() + rightChild.setSize() + 1;
        }
        return subtreeSize;
    }

    public int getSubtreeSize() {
        return subtreeSize;
    }

    /**
     * Sets the biggest child of this Node.
     * The biggest child is the Node with the highest marker(absolute)
     */
    public int initializeBiggestChild() {
        int absMarker = Math.abs(marker);
        if (leftChild == null && rightChild == null) {
            this.biggestChild = absMarker;
        } else if (leftChild == null) {
            int rightMarker = rightChild.initializeBiggestChild();
            if (absMarker > rightMarker) {
                this.biggestChild = absMarker;
            } else {
                this.biggestChild = rightMarker;
            }
        } else if (rightChild == null) {
            int leftMarker = leftChild.initializeBiggestChild();
            if (absMarker > leftMarker) {
                this.biggestChild = absMarker;
            } else {
                this.biggestChild = leftMarker;
            }
        } else {
            int leftM = leftChild.initializeBiggestChild();
            int rightM = rightChild.initializeBiggestChild();
            if ((absMarker > leftM) && (absMarker > rightM)) {
                this.biggestChild = absMarker;
            } else if ((leftM > absMarker) && (leftM > rightChild.biggestChild)) {
                this.biggestChild = leftM;
            } else if ((rightM > absMarker) && (rightM > leftChild.getBiggestChild())) {
                this.biggestChild = rightM;
            }
        }
        return this.biggestChild;
    }

    public void setBiggestChild() {
        int absMarker = Math.abs(marker);
        if ((leftChild == null) && (rightChild == null)) {
            this.biggestChild = absMarker;
        } else if (leftChild == null) {
            if (absMarker > rightChild.getBiggestChild()) {
                this.biggestChild = absMarker;
            } else {
                this.biggestChild = rightChild.getBiggestChild();
            }
        } else if (rightChild == null) {
            if (absMarker > leftChild.getBiggestChild()) {
                this.biggestChild = absMarker;
            } else {
                this.biggestChild = leftChild.getBiggestChild();
            }
        } else {
            if ((absMarker > leftChild.getBiggestChild()) && (absMarker > rightChild.getBiggestChild())) {
                this.biggestChild = absMarker;
            } else if ((leftChild.getBiggestChild() > absMarker) && (leftChild.getBiggestChild() > rightChild.biggestChild)) {
                this.biggestChild = leftChild.getBiggestChild();
            } else if ((rightChild.getBiggestChild() > absMarker) && (rightChild.getBiggestChild() > leftChild.getBiggestChild())) {
                this.biggestChild = rightChild.getBiggestChild();
            }
        }
    }

    public void changeChromosome(int newChromosome) {
        this.setChromosomeNumber(newChromosome);
        if (leftChild != null) {
            leftChild.changeChromosome(newChromosome);
        } else if (rightChild != null) {
            rightChild.changeChromosome(newChromosome);
        }
    }

    public int getBiggestChild() {
        return biggestChild;
    }

    // Getter and Setter.
    public SplayNode getParent() {
        return parent;
    }

    public void setParent(SplayNode parent) {
        if (parent != null) {
            this.setRoot(false);
        }
        this.parent = parent;
    }

    //public int getChromosomNumber() {
    //    return chromosomNumber;
    //}

    public void setChromosomeNumber(int chromosomeNumber) {
        this.chromosomeNumber = chromosomeNumber;
    }

    public int getMarker() {
        if (this.isReverseFlag()) {
            return this.marker * (-1);
        } else {
            return this.marker;
        }

    }

    public boolean isReverseFlag() {
        return reverseFlag;
    }

    public void flipFlag() {
        reverseFlag = !reverseFlag;
    }

    public void flipMarker() {
        marker = marker * (-1);
    }

    public void setReverseFlag(boolean reverseFlag) {
        this.reverseFlag = reverseFlag;
    }

    public SplayNode getLeftChild() {
        return leftChild;
    }

    public final void setLeftChild(SplayNode leftChild) {
        this.leftChild = leftChild;
        if (leftChild != null) {
            //this.leftChild.setParent(this);
            this.leftChild.setLeft(true);
            this.leftChild.setRight(false);
        }

    }

    public SplayNode getRightChild() {
        return rightChild;
    }

    public final void setRightChild(SplayNode rightChild) {
        this.rightChild = rightChild;
        if (rightChild != null) {
            //this.rightChild.setParent(this);
            this.rightChild.setRight(true);
            this.rightChild.setLeft(false);
        }

    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        if (root) {
            if (this.parent != null) {
                this.parent = null;
            }
            this.setLeft(false);
            this.setRight(false);
        }
        this.root = root;
    }
}
