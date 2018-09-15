package de.unibi.cebitec.gi.unimog.algorithms;

import de.unibi.cebitec.gi.unimog.datastructure.AdditionalDataHPDistance;
import de.unibi.cebitec.gi.unimog.datastructure.Data;
import de.unibi.cebitec.gi.unimog.datastructure.IAdditionalData;
import de.unibi.cebitec.gi.unimog.datastructure.multifurcatedTree.CountLeavesVisitor;
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
 * @author -Rolf Hilker-
 * 
 * Class for calculating the Translocation distance between two given linear and
 * multichromosomal genomes under the DCJ model. 
 * Also restricted to co-tailed genomes (same tails of chromosomes in both genomes.
 * Translocation Distance: N-(C+K)+t
 * K = nb Chromosomes, L = nb leaves, T = nb trees in forest
 * For calculation the forest can be simulated and still joined in 1 tree.
 * nbChildren of the root determines the number of T while the number of leaves
 * does not change!
 */

public class DistanceTrans implements IDistance {

	@Override
	public int calculateDistance(final Data data, final IAdditionalData additionalData) throws ClassCastException{
		
		int distance = -1;
		
		//preprocessing (construction of component Tree)
		final AdditionalDataHPDistance additionalHPData = (AdditionalDataHPDistance) additionalData;
		final HPBasedDistPreprocessing hpPreprocess = new HPBasedDistPreprocessing(data, additionalHPData);
		final MultifurcatedTree componentTree = hpPreprocess.getCompTree();
		
		distance = this.innerDistCalculation(data, additionalHPData, componentTree);
		return distance;
	}

	
	@Override
	public int calculateDistance(final Data data, final IAdditionalData additionalData, final MultifurcatedTree componentTree) {
		int distance = 0;
		
		final AdditionalDataHPDistance additionalHPData = (AdditionalDataHPDistance) additionalData;		
		distance = this.innerDistCalculation(data, additionalHPData, componentTree);
		return distance;
	}
	
	
	/**
	 * Carries out the distance calculation itself.
	 * @param data The data object containing all information
	 * @param additionalData The additional data which may be needed
	 * @param componentTree the component tree
	 * @return the translocation distance
	 */
	private int innerDistCalculation(final Data data, final AdditionalDataHPDistance additionalHPData, final MultifurcatedTree componentTree) {
		int distance = 0;
		
		//Numbers of leaves etc. are calculated:
		CountLeavesVisitor countingVisitor = new CountLeavesVisitor();
		componentTree.bottomUp(countingVisitor);
		final int nbOfLeaves = countingVisitor.getNbOfLeaves();
		final int nbTrees = componentTree.getRoot().getNodeChildren().size();
		
		//Distance is calculated:
		//Can also be changed to only calculate nbOfCycles & nbOfChroms is given
		final DistanceDCJ dcjDist = new DistanceDCJ();
		final int dcjDistance = dcjDist.calculateDistance(data, additionalHPData);
		
		//Additional distance is calculated
		int additionalCost = 0;
		if (nbOfLeaves%2 == 0){
			if (nbTrees == 1){
				additionalCost = nbOfLeaves+2;
			} else {
				additionalCost = nbOfLeaves;
			}
		} else {
			additionalCost = nbOfLeaves+1;
		}
		distance = dcjDistance + additionalCost;
		
		return distance;
	}

}
