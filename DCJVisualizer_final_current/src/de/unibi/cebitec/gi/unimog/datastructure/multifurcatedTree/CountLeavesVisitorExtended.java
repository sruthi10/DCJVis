package de.unibi.cebitec.gi.unimog.datastructure.multifurcatedTree;

import java.util.ArrayList;

/***************************************************************************
 *   Copyright (C) 2010 by Rolf Hilker                                     *
 *   rhilker   a t  cebitec.uni-bielefeld.de                               *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 ***************************************************************************/

/**
 * Visitor created for translocation sorting.
 * Therefore, it counts the number of leaves and the number of trees in the forest.
 * It stores the index of the biggest tree, a mapping between leaf number and node index
 * in the ordered forest (actually the forest is still stored as a single tree, but
 * we have to imagine the top layer of the root as separated), the size of each tree and
 * a mapping between the number of each tree to the first index of the tree.
 * 
 * @author -Rolf Hilker-
 *
 */
public class CountLeavesVisitorExtended extends CountLeavesVisitor implements NodeVisitor {

    private int nbLeaves = 0;
    private int nbOfTree = -1;
    private int iBiggestTree = 0;
    private int children = 0;
    private int childBiggestTree = 0;
    private ArrayList<Integer> leafToIndexMap = new ArrayList<Integer>();
    private int roundNodeIndex = 0;
    private int currTreeBorder = 0;
    private ArrayList<Integer> treeSizeMap = new ArrayList<Integer>();
    private ArrayList<Integer> treeIndexMap = new ArrayList<Integer>();

    @Override
    public void visit(final Node node) {

        final Node parent = node.getParent();
        if (!node.isRoot()) {
            if (parent.isRoot()) {

                ++this.nbOfTree;
                this.currTreeBorder = this.roundNodeIndex;
                this.treeIndexMap.add(this.nbOfTree, this.currTreeBorder);
                this.treeSizeMap.add(this.nbOfTree, -1);
                this.children = 0;
            }

            if (!node.getNodeType().equals(NodeType.SQUARE)) {
                ++this.roundNodeIndex;
            }

            if (node.isLeaf()) {
                ++this.children;
                this.leafToIndexMap.add(this.nbLeaves, this.roundNodeIndex - 1);
                this.treeSizeMap.set(this.nbOfTree, this.children);

                if (this.children > this.childBiggestTree) {

                    this.iBiggestTree = this.currTreeBorder;
                    this.childBiggestTree = this.children;
                }
                ++this.nbLeaves;
            }
        }
    }

    /**
     * Returns the index of the round node forming the tree with the most children.
     * Where each node directly neighboring the root induces a 'tree'.
     * @return The biggest tree
     */
    public int getStartBiggestTree() {
        return this.iBiggestTree;
    }

    /**
     * Returns the number of leaves in the given tree.
     * @return the nbLeaves
     */
    @Override
    public int getNbOfLeaves() {
        return this.nbLeaves;
    }

    /**
     * Returns the mapping of each leaf to its index of the associated component
     * among all components of the tree. F.e. leaf 2 is the 5th round node in the tree.
     * @return the leafToIndexMap
     */
    public ArrayList<Integer> getLeafToIndexMap() {
        return this.leafToIndexMap;
    }

    /**
     * Returns the mapping of each tree index to its size.
     * @return the treeSizeMap
     */
    public ArrayList<Integer> getTreeSizeMap() {
        return this.treeSizeMap;
    }

    /**
     * Returns the index of the first round node of each tree.
     * @return the treeIndexMap
     */
    public ArrayList<Integer> getTreeIndexMap() {
        return this.treeIndexMap;
    }
}
