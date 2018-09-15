package de.unibi.cebitec.gi.unimog.algorithms;

import de.unibi.cebitec.gi.unimog.datastructure.AdditionalDataHPDistance;
import de.unibi.cebitec.gi.unimog.datastructure.Data;
import de.unibi.cebitec.gi.unimog.datastructure.IAdditionalData;
import de.unibi.cebitec.gi.unimog.datastructure.multifurcatedTree.CountingVisitor;
import de.unibi.cebitec.gi.unimog.datastructure.multifurcatedTree.MultifurcatedTree;
import de.unibi.cebitec.gi.unimog.datastructure.multifurcatedTree.Node;
import de.unibi.cebitec.gi.unimog.datastructure.multifurcatedTree.NodeType;

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
 * Class for calculating the HP distance between two given genomes under
 * the DCJ model. HP Distance: N-(C+I/2)+t
 */
public class DistanceHP implements IDistance {

	@Override
	public int calculateDistance(final Data data, final IAdditionalData additionalData) throws ClassCastException {
		int distance = 0;

		//preprocessing (construction of component Tree)
		final AdditionalDataHPDistance additionalHPData = (AdditionalDataHPDistance) additionalData;
		final HPBasedDistPreprocessing hpPreprocess = new HPBasedDistPreprocessing(data, additionalHPData);
		final MultifurcatedTree componentTree = hpPreprocess.getCompTree();
		
		//Numbers of different leaves etc. are calculated:
		CountingVisitor countingVisitor = new CountingVisitor();
		componentTree.bottomUp(countingVisitor);
		int nbWhiteLeaves = countingVisitor.getNbWhiteLeaves();
		int whiteRootNodeIndex = countingVisitor.getWhiteRootNodeIndex();
		if (whiteRootNodeIndex > -1){
			++nbWhiteLeaves;
		}
		final int nbGreyLeaves = countingVisitor.getNbGreyLeaves();
		int nbShortLeaves = countingVisitor.getNbShortLeaves();
		final boolean dangerous = countingVisitor.isDangerous();
		boolean shortDanger = false;
		if (dangerous && DistanceHP.isRootALeaf(componentTree, countingVisitor)){
			shortDanger = DistanceHP.isDangerVertexShortLeaf(countingVisitor.getDangerousNode());
			if (shortDanger){
				++nbShortLeaves;
			}
		}
	
		//Distance is calculated:
		DistanceDCJ dcjDist = new DistanceDCJ();
		final int dcjDistance = dcjDist.calculateDistance(data, additionalHPData);
	
		int additionalCost = 0;
					
		if (!dangerous){ //schwarze knoten nie dangerous!
			if (nbWhiteLeaves%2 != 0 && nbShortLeaves == 0){ // because is always 0 or 1
				additionalCost = nbWhiteLeaves+1 + (nbGreyLeaves-1)/2 + (nbGreyLeaves-1)%2;
			} else {
				additionalCost = nbWhiteLeaves + nbGreyLeaves/2 + nbGreyLeaves%2;
			}
		} else {
			if (nbWhiteLeaves>0 && (nbWhiteLeaves+1)%2 != 0 && nbShortLeaves == 0){
				additionalCost = nbWhiteLeaves+1+1 + Math.max(0, (nbGreyLeaves-2)/2 + (nbGreyLeaves-2)%2);
			} else 
			if (nbWhiteLeaves>0 && (nbWhiteLeaves+1)%2 != 0 && nbShortLeaves == 1 && shortDanger){
				additionalCost = nbWhiteLeaves+1 + nbGreyLeaves/2 + nbGreyLeaves%2; 
			} else {
			additionalCost = nbWhiteLeaves+1 + (nbGreyLeaves-1)/2 + (nbGreyLeaves-1)%2;
			}
		}
		distance = dcjDistance + additionalCost;

		return distance;
	}
	

	/**
	 * Method for finding out if the Component Tree comprising all
	 * white leaves lets the root become a leaf. CAUTION: This method only works if 
	 * there is a dangerous vertex in the tree, which means there is only one
	 * branch without a grey vertex neighboring the root.
	 * @param componentTree The component tree to check
	 * @param visitor The visitor comprehending the numbers of different leaves
	 * @return <code>true</code> if the root becomes a leaf, <code>false</code> otherwise.
	 */
	public static boolean isRootALeaf(final MultifurcatedTree componentTree, final CountingVisitor visitor){
		//visit all grey vertices & test if they have children
		for (Node child : componentTree.getRoot().getNodeChildren()){
			if (child.getNodeType() == NodeType.GREY && child.getNodeChildren().size() > 0){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method for checking in a Component Tree if the dangerous vertex 
	 * directly neighboring the root becomes a short leaf. 
	 * @param dangerVertex Dangerous vertex to test
	 * @return <code>true</code> if it is a short leaf, <code>false</code> otherwise.
	 */
	public static boolean isDangerVertexShortLeaf(final Node dangerVertex){
		
		Node child = dangerVertex;
		int nbChildren = dangerVertex.getNodeChildren().size();
		boolean stillBlack = true;
		while (nbChildren == 1){
			child = child.getNodeChildren().get(0);
			stillBlack = (child.getNodeType() == NodeType.BLACK || child.getNodeType() == NodeType.SQUARE);
			if (!stillBlack){
				break;
			}
			nbChildren = child.getNodeChildren().size();
		}
		return stillBlack;
	}


	@Override
	public int calculateDistance(Data data, IAdditionalData additionalData,
			MultifurcatedTree componentTree) {
		// Not needed here
		return 0;
	}

}
