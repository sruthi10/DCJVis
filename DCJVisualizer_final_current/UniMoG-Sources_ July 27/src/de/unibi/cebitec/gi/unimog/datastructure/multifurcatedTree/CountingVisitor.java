package de.unibi.cebitec.gi.unimog.datastructure.multifurcatedTree;

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
 * Counts the number of white leaves, grey leaves and 
 * short paths for a MultifuractedTree. 
 * Also knows if a dangerous vertex exists in the tree.
 */
public class CountingVisitor implements NodeVisitor {

	private int nbWhiteLeaves = 0;
	private int nbGreyLeaves = 0;
	private int nbShortLeaves = 0;
	private boolean potentiallyDangerous = false;
	private int count4Danger = 0;
	private Node dangerousNode;
	private int whiteRootNodeIndex = -1;
	private boolean branchingPoint = false;
	private boolean dangerous;
	
	@Override
	public void visit(final Node node) {
		
		//check if there is a dangerous vertex in the tree
		if (node.getDepth() == 2){
			if (node.getNodeType() == NodeType.WHITE){
				++this.count4Danger;
				this.potentiallyDangerous = (this.count4Danger == 1);
				if (this.potentiallyDangerous){
					this.dangerousNode = node;
				} else {
					this.dangerousNode = null;
				}
			}
			if (node.getNodeType() == NodeType.GREY){
				if (node.getNodeChildren().size() > 0){
					if (this.potentiallyDangerous){
						++this.count4Danger;
					} else {
						this.count4Danger = 2;
					}
					this.potentiallyDangerous = false;
				}
				this.whiteRootNodeIndex = -2;
			}
		}
		
		//count number of grey, white and short leaves
		boolean becomesALeaf = VisitorUtils.becomesALeaf(node);
		if (node.isLeaf() || becomesALeaf){
			if (node.getNodeType() == NodeType.WHITE){
				++this.nbWhiteLeaves;
				this.countShortLeaves(node);
			} else if (node.getNodeType() == NodeType.GREY){
				++this.nbGreyLeaves;
			}
		} else
		if (node.getNodeType() == NodeType.WHITE){ 
				if (becomesALeaf){
					++this.nbWhiteLeaves;
				} else {
					if (!VisitorUtils.hasWhiteParent(node)){
						if (this.whiteRootNodeIndex > -1){
							this.whiteRootNodeIndex = -2; //if there's another inner white or grey node without white parent WN is not root
						}
					}
					if (this.whiteRootNodeIndex == -1){
						this.whiteRootNodeIndex = 0; //if the index itself is needed, you can implement that
					}
				}
		}
		
		if (this.nbWhiteLeaves > 1 && this.potentiallyDangerous){
			this.branchingPoint = true; //means there must be a branching point
			this.dangerous = this.branchingPoint && this.potentiallyDangerous;
		}
	}
	
//	/**
//	 * Checks if a white node in the tree has another white child or only black children.
//	 * @param node the white node to check
//	 * @return true if it has only black children
//	 */
//	private boolean onlyBlackChildren(Node node) {
//		Vector<Node> children = new Vector<Node>();
//		children.addAll(node.getNodeChildren());
//		Node child;
//		while (children.size() > 0){
//			for (int i=0; i< children.size(); ++i){
//				child = children.get(i);
//				children.remove(i);
//				if (child.getNodeType() == NodeType.WHITE){
//					return false;
//				}
//				children.addAll(child.getNodeChildren());
//			}
//			
//		}
//		return true;
//	}



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
	 * Returns the number of grey leaves in the given tree.
	 * @return the nbGreyLeaves
	 */
	public int getNbGreyLeaves() {
		return this.nbGreyLeaves;
	}

	/**
	 * Returns the number of shorts paths in the given tree.
	 * @return the nbShortPaths
	 */
	public int getNbShortLeaves() {
		return this.nbShortLeaves;
	}

	/**
	 * Returns if there is at least one dangerous vertex
	 * in the given tree.
	 * @return the dangerous
	 */
	public boolean isDangerous() {
		return this.dangerous;
	}

	/**
	 * Returns the dangerous node.
	 * @return the dangerousNode
	 */
	public Node getDangerousNode() {
		return this.dangerousNode;
	}
	
	/**
	 * The possibly meanest white node index. Don't forget to check this 
	 * before using it!
	 * @return the possibly meanestWNodeIndex
	 */
	public int getWhiteRootNodeIndex() {
		return this.whiteRootNodeIndex;
	}


}
