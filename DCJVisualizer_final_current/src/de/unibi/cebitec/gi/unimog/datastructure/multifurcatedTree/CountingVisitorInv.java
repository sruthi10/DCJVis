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
 * @author -Rolf Hilker-
 *
 * Counts the number of white leaves and 
 * creates the leaf to index map for these white leaves for a MultifuractedTree. 
 */
public class CountingVisitorInv implements NodeVisitor {

	private int nbWhiteLeaves = 0;
	private int nbShortLeaves = 0;
	private ArrayList<Integer> leafToIndexMap = new ArrayList<Integer>();
	private int nodeIndex = -1;
	private ArrayList<Node> innerWhiteNodeMap = new ArrayList<Node>();
	private int whiteRootNodeIndex = -1;
	private int indexLastWN = 0;
	private ArrayList<Integer> whiteLeafParents = new ArrayList<Integer>(); /** Index of the white parent node of node with index. */
	
	@Override
	public void visit(final Node node) {
		
		if (node.getNodeType() == NodeType.WHITE){
			boolean becomesALeaf = VisitorUtils.becomesALeaf(node);
			if (node.isLeaf() || becomesALeaf){
				++this.nbWhiteLeaves;
				this.leafToIndexMap.add(this.nodeIndex);
				this.countShortLeaves(node);
				if (!VisitorUtils.hasWhiteParent(node)){
					this.whiteLeafParents.add(0);
				} else {
					this.whiteLeafParents.add(this.indexLastWN);
				}
			} else
			if (!becomesALeaf){
				if (!VisitorUtils.hasWhiteParent(node)){
					++this.indexLastWN;
					if (this.whiteRootNodeIndex > -1){
						this.whiteRootNodeIndex = -2; //if there's another inner white node without white parent WN is not root
					}
				}
				if (this.whiteRootNodeIndex == -1){
					this.whiteRootNodeIndex = this.nodeIndex;
				}
				this.innerWhiteNodeMap.add(node);
			}
		}
		
		if (!node.getNodeType().equals(NodeType.SQUARE)){
			++this.nodeIndex;
		}
	}


	/**
	 * Counts the number of short leaves. Only call for White leaves!
	 * A leaf is short if it is a white leaf adjacent to a branching vertex.
	 * @param node The current leaf to test
	 */
	private void countShortLeaves(final Node node) {

		Node parent = node.getParent();
		int nbParentChildren = parent.getNodeChildren().size();
		boolean stillBlack = true;
		while (nbParentChildren == 1){
			if (!parent.isRoot()){
				stillBlack = (!(parent.getNodeType() == NodeType.WHITE));
				if (!stillBlack){
					break;
				}
				parent = parent.getParent();
				if (!parent.isRoot()){
					nbParentChildren = parent.getNodeChildren().size();
				} else {
					break;
				}
			}
		}
		if (nbParentChildren > 1 || parent.isRoot()){
			++this.nbShortLeaves;
		}
	}

	/**
	 * Returns the number of white leaves in the given tree.
	 * @return the nbWhiteLeaves
	 */
	public int getNbWhiteLeaves() {
		return this.nbWhiteLeaves;
	}

	/**
	 * Returns the number of shorts paths in the given tree.
	 * @return the nbShortPaths
	 */
	public int getNbShortLeaves() {
		return this.nbShortLeaves;
	}


	/**
	 * Returns the mapping of each leaf to its index of the associated component
	 * among all components of the tree. F.e. leaf 2 is the 5th round node in the tree.
	 * @return the leafToIndexMap
	 */
	public ArrayList<Integer> getWhiteLeafToIndexMap() {
		return this.leafToIndexMap;
	}


//	/**
//	 * The map of all inner white nodes.
//	 * @return the innerWhiteToIndexMap
//	 */
//	public ArrayList<Node> getInnerWhiteNodes() {
//		return this.innerWhiteNodeMap;
//	}


	/**
	 * The possibly meanest white node index. Don't forget to check this 
	 * before using it!
	 * @return the possibly meanestWNodeIndex
	 */
	public int getWhiteRootNodeIndex() {
		return this.whiteRootNodeIndex;
	}


	/**
	 * Returns an array list containing at the index of each leaf the
	 * number of the parent white node, if there is a white node somewhere
	 * bottom up from this leaf to the root.
	 * @return the whiteLeafParents array
	 */
	public ArrayList<Integer> getWhiteLeafParents() {
		return this.whiteLeafParents;
	}
}

