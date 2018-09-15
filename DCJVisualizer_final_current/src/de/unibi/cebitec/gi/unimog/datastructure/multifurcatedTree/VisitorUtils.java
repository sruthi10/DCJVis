package de.unibi.cebitec.gi.unimog.datastructure.multifurcatedTree;

import java.util.Vector;

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
 * Contains some utility methods used by more than one node visitor.
 */
public class VisitorUtils {

	public VisitorUtils(){
		//Do not instantiate
	}
	
	/**
	 * Returns if a white node in the tree becomes a leaf corresponding to its
	 * children. It becomes a leaf if there is no white node below this node.
	 * @param node the node to check
	 * @return true if the node becomes a leaf, false otherwise
	 */
	public static boolean becomesALeaf(final Node node) {
		VisitorWhiteNodes whiteNodeVisitor = new VisitorWhiteNodes();
		Vector<Node> children = node.getNodeChildren();
		for (int i=0; i<children.size(); ++i){
			children.get(i).topDown(whiteNodeVisitor);
		}
		return !whiteNodeVisitor.isAWhiteChild();
	}
	
	/**
	 * Checks if the current node has a white parent at some level above in the tree.
	 * @param node the node to check
	 * @return true if it has a white parent, false, if not
	 */
	public static boolean hasWhiteParent(final Node node) {

		Node parent = node.getParent();
		while (!parent.isRoot() && parent.getNodeType() != NodeType.WHITE){
			parent = parent.getParent();
		}
		if (parent.getNodeType() == NodeType.WHITE){
			return true;
		}
		return false;
	}
	
}
