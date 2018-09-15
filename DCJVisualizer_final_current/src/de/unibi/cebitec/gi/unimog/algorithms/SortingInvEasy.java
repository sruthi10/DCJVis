package de.unibi.cebitec.gi.unimog.algorithms;

import java.util.ArrayList;
import java.util.HashMap;

import de.unibi.cebitec.gi.unimog.datastructure.AdditionalDataHPDistance;
import de.unibi.cebitec.gi.unimog.datastructure.AdjacencyGraph;
import de.unibi.cebitec.gi.unimog.datastructure.Chromosome;
import de.unibi.cebitec.gi.unimog.datastructure.Data;
import de.unibi.cebitec.gi.unimog.datastructure.Genome;
import de.unibi.cebitec.gi.unimog.datastructure.IAdditionalData;
import de.unibi.cebitec.gi.unimog.datastructure.OperationList;
import de.unibi.cebitec.gi.unimog.datastructure.Pair;

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
 * Class for sorting by inversions in the trail and error fashion.
 * The running time is n^4: In the worst case n^3 operations are possible
 * and for each of these it is tested in linear time if the distance is decreased.
 * Therefore the new adjacency graph has to be calculated and afterwards the distance,
 * but this is achievable in linear time.
 */

public class SortingInvEasy implements ISorting {
	
	@Override
	public OperationList findOptSortSequence(final Data data,
			final IAdditionalData additionalData, final HashMap<Integer, Integer> chromMap) {

		OperationList operationList = new OperationList();

		Data dataWork = new Data(data.getGenomeA(), data.getGenomeB(), data.getAdjGraph().clone());
		AdditionalDataHPDistance addDataWork = new AdditionalDataHPDistance(data.getGenomeA());
		Genome lastGenome;
		
		/*
		 * Was ist eine inversion?
		 * - Unichromosomal, linear!
		 * - telomere bleiben = cotailed
		 * - von links nach rechts durchs chromosom:
		 * 		- nimm adjacency & wende inversion an
		 * 		- inversion bedeutet 1. cut left joins 2. cut left, right joins right
		 * 		- recalculate distance: if -1 then good otherwise undo & try next adjaceny
		 */
		
		int invDistance = 0;
		final DistanceInv calcInvDist = new DistanceInv();
		try {
			invDistance = calcInvDist.calculateDistance(dataWork, addDataWork);
		} catch (final ClassCastException e){
			System.out.println("error in sorting inversions easy");
		}
		int newInvDistance = invDistance;
		
		AdditionalDataHPDistance lastAddData = addDataWork;
		lastGenome = dataWork.getGenomeA();
		int length = lastGenome.getChromosome(0).getGenes().length;
		AdjacencyGraph adjGraph = dataWork.getAdjGraph();
		int gene = 0;
		int neighbour = 0;
		int extremity1 = 0;
		int extremity2 = 0;
		int extremity3 = 0;
		int extremity4 = 0;
		
		while (newInvDistance > 0){
			int i = 0;
			int k = i+1;
			lastAddData = new AdditionalDataHPDistance(dataWork.getGenomeA());
			lastGenome = dataWork.getGenomeA();
			while (invDistance <= newInvDistance && i< length-3){
				dataWork.setGenomeA(lastGenome);
				addDataWork = lastAddData;
				int[] genes = dataWork.getGenomeA().getChromosome(0).getGenes().clone();
				
				//set genes to review now
				gene = genes[i];
				neighbour = genes[i+1];
				
				//check if adjacencies have already been used for inversion
				extremity1 = adjGraph.abstractGene(gene, true);
				extremity2 = adjGraph.abstractGene(neighbour, false);
				extremity3 = adjGraph.abstractGene(genes[k], true);
				extremity4 = adjGraph.abstractGene(genes[k+1], false);
				Pair<Integer, Integer> pair1 = new Pair<Integer, Integer>(extremity1, extremity2);
				Pair<Integer, Integer> pair2 = new Pair<Integer, Integer>(extremity3, extremity4);
				
				//get second adjacency for application of inversion & test same things
				while (this.checkOperation(pair1, pair2, operationList.getOperationList()) && k<length-2){
					 //check if inv has already been done here
					++k;
					extremity3 = adjGraph.abstractGene(genes[k], true);
					extremity4 = adjGraph.abstractGene(genes[k+1], false);
					pair2 = new Pair<Integer, Integer>(extremity3, extremity4);
				}
					
				if (k < length-1){
					
					//now genes from index+1 to scndIndex have to be inverted in direction & orientation!
					int[] intermediateGenes = new int[k-i];
					int index = 0;
					for (int j=k; j>i; --j){
						intermediateGenes[index++] = -genes[j];
					}
					index = 0;
					for (int j=i+1; j<k+1; ++j){
						genes[j] = intermediateGenes[index++];
					}
					
					//check if distance decreased
					lastGenome = dataWork.getGenomeA();
					Chromosome chrom = new Chromosome(genes, false);
					Genome genomeA = new Genome();
					genomeA.addChromosome(chrom);
					dataWork.setGenomeA(genomeA);
					dataWork.setAdjGraph(new AdjacencyGraph(genomeA, dataWork.getGenomeB()));
					lastAddData = addDataWork;
					addDataWork = new AdditionalDataHPDistance(genomeA);
					
					try {
						newInvDistance = calcInvDist.calculateDistance(dataWork, addDataWork);
					} catch (final ClassCastException e){
						System.out.println("error in sorting inversions easy");
					}
				}
				if (k < length-2){
					++k;
				} else
				if (k == length-2 && i < length-3){
					++i;
					k = i+1;
				}
			}
						
			if (invDistance > newInvDistance){
				operationList.addOperation(new Pair<Integer, Integer>(extremity1, extremity3));
				operationList.addOperation(new Pair<Integer, Integer>(extremity2, extremity4));
				operationList.addAdjacencyArrayG1(dataWork.getAdjGraph().getAdjacenciesGenome1());
				invDistance = newInvDistance;
			}
		}
		
		
		return operationList;
	}
	

	
	/**
	 * Checks for 2 pairs of extremities if exactly these extremities have already been
	 * used in the same inversion.
	 * @param pair1 fst pair
	 * @param pair2 scnd pair
	 * @param operationList list of operations
	 * @return true or false..
	 */
	private boolean checkOperation(Pair<Integer, Integer> pair1,
			Pair<Integer, Integer> pair2, ArrayList<Pair<Integer, Integer>> operationList) {
		for (int i=0; i<operationList.size(); ++i){
			if ((operationList.get(i) == pair1 && operationList.get(i+1) == pair2) || 
				 (operationList.get(i) == pair2 && operationList.get(i+1) == pair1)){
				return true;
			} else {
				++i;
			}
		}
		return false;
	}
}
