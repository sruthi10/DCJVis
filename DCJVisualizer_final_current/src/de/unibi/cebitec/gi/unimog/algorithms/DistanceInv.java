package de.unibi.cebitec.gi.unimog.algorithms;

import java.util.ArrayList;

import de.unibi.cebitec.gi.unimog.datastructure.AdditionalDataHPDistance;
import de.unibi.cebitec.gi.unimog.datastructure.Data;
import de.unibi.cebitec.gi.unimog.datastructure.IAdditionalData;
import de.unibi.cebitec.gi.unimog.datastructure.multifurcatedTree.CountingVisitorInv;
import de.unibi.cebitec.gi.unimog.datastructure.multifurcatedTree.MultifurcatedTree;

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
 * Class for calculating the Inversion distance between two given linear and
 * unichromosomal genomes under the DCJ model. Inversion Distance: N-(C+1)+t
 * 
 * A branch in a tree is called a long branch if it has two or more unoriented components.
 * A tree is called a fortress if it has an odd number of leaves, all of them on long branches.
 * w + 1 if T is a fortress
 * w otherwise.
 */
public class DistanceInv implements IDistance {

	@Override
	public int calculateDistance(Data data, IAdditionalData additionalData) throws ClassCastException {
		
		//preprocessing (construction of component Tree)
		final AdditionalDataHPDistance additionalHPData = (AdditionalDataHPDistance) additionalData;
		final HPBasedDistPreprocessing hpPreprocess = new HPBasedDistPreprocessing(data, additionalHPData);
		final MultifurcatedTree componentTree = hpPreprocess.getCompTree();

		int distance = -1;
		distance = innerDistCalculation(data, additionalHPData, componentTree);

		return distance;
	}

	@Override
	public int calculateDistance(Data data, IAdditionalData additionalData,
			MultifurcatedTree componentTree) {

		final AdditionalDataHPDistance additionalHPData = (AdditionalDataHPDistance) additionalData;
		
		int distance = -1;		
		distance = this.innerDistCalculation(data, additionalHPData, componentTree);
		return distance;
	}
	
	/**
	 * Carries out the distance calculation itself.
	 * @param data The data object containing all information
	 * @param additionalData The additional data which may be needed
	 * @param componentTree the component tree
	 * @return the inversion distance
	 */
	private int innerDistCalculation(final Data data, final AdditionalDataHPDistance additionalHPData, 
			final MultifurcatedTree componentTree){
		
		//Numbers of different leaves etc. are calculated:
		CountingVisitorInv countingVisitor = new CountingVisitorInv();
		componentTree.topDown(countingVisitor);
		int nbWhiteLeaves = countingVisitor.getNbWhiteLeaves();
		int whiteRootNodeIndex = countingVisitor.getWhiteRootNodeIndex();
		ArrayList<Integer> whiteLeafParents = countingVisitor.getWhiteLeafParents();
		boolean allInOne = true;
		for (int i=0; i<whiteLeafParents.size(); ++i){
			if (whiteLeafParents.get(i) == 0){
				allInOne = false;
			}
		}
		if (whiteRootNodeIndex > -1 && allInOne){
			++nbWhiteLeaves;
		}
		final int nbShortLeaves = countingVisitor.getNbShortLeaves();
		
		//Distance is calculated:
		DistanceDCJ dcjDist = new DistanceDCJ();
		final int dcjDistance = dcjDist.calculateDistance(data, additionalHPData);
		
		//Additional distance is calculated
		int additionalCost = 0;
		if (nbShortLeaves == 0 && nbWhiteLeaves%2 == 1){
			additionalCost = nbWhiteLeaves+1;
		} else {
			additionalCost = nbWhiteLeaves;
		}
		return dcjDistance + additionalCost;
	}

}
