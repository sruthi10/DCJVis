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
 * This class provides the easiest to implement algorithm
 * for calculating a HP sorting sequence for a comparison of two genomes. 
 * The running time is n^3: In the worst case n^2 operations are possible
 * and for each of these it is tested in linear time if the distance is decreased.
 * Therefore the new adjacency graph has to be calculated and afterwards the distance,
 * but this is achievable in linear time.
 */
public class SortingHPEasy implements ISorting {

	/*
	 * How does a HP operation look like?
	 * - multichromosomal, linear!
	 * - from left to right through genome:
	 * 		- take adjacency and try to carry out an HP operation 
	 * 		- inversion means 1. cut left joins 2. cut left, right joins right
	 * 		- translocation has different possibilities, internal & external cuts
	 * 		- recalculate distance: if -1 then good otherwise undo
	 * 		- then for some indices try second composition at same index
	 */
	
	@Override
	public OperationList findOptSortSequence(final Data data,
			final IAdditionalData additionalData, final HashMap<Integer, Integer> chromMap) {

		OperationList operationList = new OperationList();

		Data dataWork = new Data(data.getGenomeA(), data.getGenomeB(), data.getAdjGraph().clone());
		AdditionalDataHPDistance addDataWork = new AdditionalDataHPDistance(data.getGenomeA());
		Genome lastGenome;
		
		int hpDistance = 0;
		final DistanceHP calcHPDist = new DistanceHP();
		try {
			hpDistance = calcHPDist.calculateDistance(dataWork, addDataWork);
		} catch (final ClassCastException e){
			System.out.println("error in sorting hp easy");
		}
		int newHPDistance = hpDistance;
		
		AdditionalDataHPDistance lastAddData = addDataWork;
		lastGenome = dataWork.getGenomeA();
		AdjacencyGraph adjGraph = dataWork.getAdjGraph();
		int gene = 0;
		int neighbour = 0;
		int extremity1 = 0;
		int extremity2 = 0;
		int extremity3 = 0;
		int extremity4 = 0;
		
		boolean externalCut1 = false;
		boolean externalCut2 = false;
		boolean sameChromosome = false;
		boolean scndCase = false; //they are set false after each operation again
		boolean onlyFstGenes = false; //and only true if the specific case occurs
		boolean operationDone = true;
		
		while (newHPDistance > 0){
			int chrom1I = 0;
			int chrom2I = 0;
			int i = 0;
			int k = i+1;
			int length1 = dataWork.getGenomeA().getChromosome(chrom1I).getSize();
			int length2 = dataWork.getGenomeA().getChromosome(chrom2I).getSize();
			lastAddData = new AdditionalDataHPDistance(dataWork.getGenomeA());
			lastGenome = dataWork.getGenomeA();
			while (hpDistance <= newHPDistance && i<= length1 && chrom1I < lastGenome.getNumberOfChromosomes()){
				dataWork.setGenomeA(lastGenome);
				addDataWork = lastAddData;
				int[] fstGenes = dataWork.getGenomeA().getChromosome(chrom1I).getGenes().clone();
				int[] scndGenes = dataWork.getGenomeA().getChromosome(chrom2I).getGenes().clone();
				int[] fstGenes2 = new int[fstGenes.length];
				int[] scndGenes2 = new int[scndGenes.length];
				length1 = dataWork.getGenomeA().getChromosome(chrom1I).getSize();
				length2 = dataWork.getGenomeA().getChromosome(chrom2I).getSize();
				
				//set genes for first cut
				if (i == 0){
					gene = 0; //telomeres are handled by setting the neighboring extremity again
					neighbour = fstGenes[i];
					externalCut1 = true;
				} else 
				if (i < length1){
					gene = fstGenes[i-1];
					neighbour = fstGenes[i];
					externalCut1 = false;
				} else {
					gene = fstGenes[i-1];
					neighbour = 0;
					externalCut1 = true;
				}
				
				//get extremities & second cut position 
				extremity1 = adjGraph.abstractGene(gene, true);
				extremity2 = adjGraph.abstractGene(neighbour, false);
				if (k == length2){
					extremity3 = adjGraph.abstractGene(scndGenes[k-1], true);
					extremity4 = 0;
					externalCut2 = true;
				} else {
					if (k != 0){
						extremity3 = adjGraph.abstractGene(scndGenes[k-1], true);
						extremity4 = adjGraph.abstractGene(scndGenes[k], false);
						externalCut2 = false;
					} else {
						extremity3 = 0;
						extremity4 = adjGraph.abstractGene(scndGenes[k], false);
					}
				}
				Pair<Integer, Integer> pair1 = new Pair<Integer, Integer>(extremity1, extremity2);
				Pair<Integer, Integer> pair2 = new Pair<Integer, Integer>(extremity3, extremity4);
				
				//get second adj for application of operation & test if both adj have already been used for operation
				while (this.checkOperation(pair1, pair2, operationList.getOperationList()) 
						&& (k < length2 || chrom2I < dataWork.getGenomeA().getNumberOfChromosomes())){
					if (k == length2){
						++chrom2I;
						scndGenes = data.getGenomeA().getChromosome(chrom2I).getGenes();
						k = 0;
						extremity3 = adjGraph.abstractGene(0, true);
						extremity4 = adjGraph.abstractGene(scndGenes[k], false);
						externalCut2 = true;
					} else {
						++k;
						extremity3 = adjGraph.abstractGene(scndGenes[k-1], true);
						extremity4 = adjGraph.abstractGene(scndGenes[k], false);
						externalCut2 = false;
					}
					pair2 = new Pair<Integer, Integer>(extremity3, extremity4);
				}
					
				//execute operation
				int[] intermediateGenes1 = new int[fstGenes.length];
				int[] intermediateGenes2 = new int[scndGenes.length];
				sameChromosome = chrom1I == chrom2I;
				if ((!externalCut1 && !externalCut2) 
						|| (sameChromosome && externalCut1 && !externalCut2) 
						|| (sameChromosome && externalCut2 && !externalCut1)){
					if (sameChromosome && i != k){
						//inversion (1,2,3,4,5,6) = (1,2,-4,-3,5,6) = only 1 case, telomere inversion also included
						System.arraycopy(fstGenes, 0, intermediateGenes1, 0, i);
						int index = i;
						for (int j=k-1; j>i-1; --j){
							intermediateGenes1[index++] = -fstGenes[j];
						}
						index = k;
						System.arraycopy(fstGenes, k, intermediateGenes1, k, fstGenes.length-k);
						fstGenes = intermediateGenes1;
						onlyFstGenes = true;
					} else
					if (sameChromosome && i == k){
						//fission ((1,2,3,4,5,6) = (1,2,3), (4,5,6)
						System.arraycopy(fstGenes, 0, intermediateGenes1, 0, i);
						System.arraycopy(fstGenes, i, intermediateGenes2, 0, fstGenes.length-i);
						fstGenes = new int[i];
						scndGenes = new int[scndGenes.length-i];
						System.arraycopy(intermediateGenes1, 0, fstGenes, 0, i);
						System.arraycopy(intermediateGenes2, 0, scndGenes, 0, scndGenes.length);
					} else {
						//translocation
						fstGenes2 = fstGenes.clone(); //needed 4 second case
						scndGenes2 = scndGenes.clone();
						intermediateGenes1 = new int[i+scndGenes.length-k];
						intermediateGenes2 = new int[k+fstGenes.length-i];
						System.arraycopy(fstGenes, 0, intermediateGenes1, 0, i);
						System.arraycopy(scndGenes, k, intermediateGenes1, i, scndGenes.length-k);
						System.arraycopy(scndGenes, 0, intermediateGenes2, 0, k);
						System.arraycopy(fstGenes, i, intermediateGenes2, k, fstGenes.length-i);
						
						fstGenes = intermediateGenes1.clone();
						scndGenes = intermediateGenes2.clone();
						
						//save already 2nd translocation in case fst case doesn't decrease the distance
						scndCase = true;
						intermediateGenes1 = new int[i+k];
						intermediateGenes2 = new int[fstGenes.length-i+scndGenes.length-k];
						System.arraycopy(fstGenes2, 0, intermediateGenes1, 0, i);
						int index = i;
						for (int j=k-1; j>=0; --j){
							intermediateGenes1[index++] = -scndGenes2[j];
						}
						index = 0;
						for (int j=fstGenes2.length-1; j>=i; --j){
							intermediateGenes2[index++] = -fstGenes2[j];
						}
						
						System.arraycopy(scndGenes2, k, intermediateGenes2, fstGenes2.length-i, scndGenes2.length-k);
						
						fstGenes2 = intermediateGenes1;
						scndGenes2 = intermediateGenes2;
					}
				} else 
					
					
				if (externalCut1 && externalCut2 && !sameChromosome){
					//fusion ((1,2,3), (4,5,6) = (1,2,3,4,5,6))
					intermediateGenes1 = new int[fstGenes.length + scndGenes.length];
					if (i == 0 && k == 0){
						int index = 0;
						for (int j=fstGenes.length-1; j>=0; --j){
							intermediateGenes1[index++] = -fstGenes[j];
						}
						System.arraycopy(scndGenes, 0, intermediateGenes1, index, scndGenes.length);
					} else
					if (i == 0){ //k == scndGenes.length
						System.arraycopy(scndGenes, 0, intermediateGenes1, 0, scndGenes.length);
						System.arraycopy(fstGenes, 0, intermediateGenes1, scndGenes.length, fstGenes.length);
					} else
					if (k == 0){
						System.arraycopy(fstGenes, 0, intermediateGenes1, 0, fstGenes.length);
						System.arraycopy(scndGenes, 0, intermediateGenes1, fstGenes.length, scndGenes.length);
					} else {	
						System.arraycopy(scndGenes, 0, intermediateGenes1, 0, scndGenes.length);					
						int index = scndGenes.length;
						for (int j=fstGenes.length-1; j>=0; --j){
							intermediateGenes1[index++] = -fstGenes[j];
						}
					}
					fstGenes = intermediateGenes1;
					onlyFstGenes = true;
				} else
					
					
				if ((externalCut1 || externalCut2) && !sameChromosome){
					//translocation that fuses a part & detaches another part
					int backupI = i;
					int backupK = k;
					if (externalCut2) {
						intermediateGenes1 = fstGenes;
						fstGenes = scndGenes;
						scndGenes = intermediateGenes1;
						intermediateGenes1 = new int[fstGenes.length];
						backupI = k;
						backupK = i;
					}
					fstGenes2 = fstGenes.clone(); //needed 4 second case
					scndGenes2 = scndGenes.clone();
					
					intermediateGenes1 = new int[fstGenes.length+scndGenes.length-backupK];
					intermediateGenes2 = new int[backupK];
					int index = 0;
					//1. cut left & inverse chr1, add last part of chr2
					if (backupI == 0){
						for (int j=fstGenes.length-1; j>=0; --j){
							intermediateGenes1[index++] = -fstGenes[j];
						}
					} else {
						//1. cut right & copy chr1, add last part of chr2
						System.arraycopy(fstGenes, 0, intermediateGenes1, 0, backupI);
					}			
					System.arraycopy(scndGenes, backupK, intermediateGenes1, fstGenes.length, scndGenes.length-backupK);
					System.arraycopy(scndGenes, 0, intermediateGenes2, 0, backupK);	
					
					fstGenes = intermediateGenes1.clone();
					scndGenes = intermediateGenes2.clone();
					
					//save already 2nd translocation in case fst case doesn't decrease the distance
					//1. unifies cut left & right in 1. chrom & inverse part 1 of chr2
					scndCase = true;
					intermediateGenes1 = new int[fstGenes2.length+backupK];
					intermediateGenes2 = new int[scndGenes2.length-backupK]; 
					//cause fstGenes part is the same
					System.arraycopy(fstGenes, 0, intermediateGenes1, 0, fstGenes2.length);
					index = fstGenes2.length;
					for (int j=backupK-1; j>=0; --j){
						intermediateGenes1[index++] = -scndGenes2[j];
					}
					System.arraycopy(scndGenes2, backupK, intermediateGenes2, 0, scndGenes2.length-backupK);
					
					fstGenes2 = intermediateGenes1.clone();
					scndGenes2 = intermediateGenes2.clone();
				} else {
					operationDone = false;
				}
				
				if (operationDone){
					//check if distance decreased
					lastGenome = dataWork.getGenomeA();
					Chromosome chrom1 = new Chromosome(fstGenes, false);
					Chromosome chrom2 = null;
					if (!onlyFstGenes){
						chrom2 = new Chromosome(scndGenes, false);
					}
					Genome genomeA = new Genome();
					for (int m = 0; m<lastGenome.getNumberOfChromosomes(); ++m){
						if ((m != chrom1I && m != chrom2I) || genomeA.getNumberOfChromosomes() > m){
							genomeA.addChromosome(lastGenome.getChromosome(m));
						} else {
							if (m == chrom1I && m == chrom2I && chrom2 != null){
								genomeA.addChromosome(chrom1);
								genomeA.addChromosome(chrom2);
							} else
							if (m == chrom1I){
								genomeA.addChromosome(chrom1);
							} else
							if (m == chrom2I && chrom2 != null){
								genomeA.addChromosome(chrom2);
							}
						}
					}
					dataWork.setGenomeA(genomeA);
					dataWork.setAdjGraph(new AdjacencyGraph(genomeA, dataWork.getGenomeB()));
					lastAddData = addDataWork;
					addDataWork = new AdditionalDataHPDistance(genomeA);
					
					try {
						newHPDistance = calcHPDist.calculateDistance(dataWork, addDataWork);
					} catch (final ClassCastException e){
						System.out.println("error in sorting hp easy");
					}
					
					//scnd case
					if (scndCase && newHPDistance >= hpDistance){
						chrom1 = new Chromosome(fstGenes2, false);
						chrom2 = null;
						if (!onlyFstGenes){
							chrom2 = new Chromosome(scndGenes2, false);
						}
						genomeA = new Genome();
						for (int m = 0; m<lastGenome.getNumberOfChromosomes(); ++m){
							if ((m != chrom1I && m != chrom2I) || genomeA.getNumberOfChromosomes() > m){
								genomeA.addChromosome(lastGenome.getChromosome(m));
							} else {
								if (m == chrom1I){
									genomeA.addChromosome(chrom1);
								} else 
								if (m == chrom2I && chrom2 != null){
									genomeA.addChromosome(chrom2);
								}
							}
						}
						dataWork.setGenomeA(genomeA);
						dataWork.setAdjGraph(new AdjacencyGraph(genomeA, dataWork.getGenomeB()));
						lastAddData = addDataWork;
						addDataWork = new AdditionalDataHPDistance(genomeA);
						
						try {
							newHPDistance = calcHPDist.calculateDistance(dataWork, addDataWork);
						} catch (final ClassCastException e){
							System.out.println("error in sorting hp easy");
						}
					}
				}
				operationDone = true;
				onlyFstGenes = false;
				scndCase = false;
				
				// choose next indices
				if (k < length2){
					++k;
				} else
				if (k == length2 && chrom2I < lastGenome.getNumberOfChromosomes()-1){
					++chrom2I;
					k = 0;
				} else {
				//if (k == length2 && chrom2I == dataWork.getGenomeA().getNumberOfChromosomes()){
					if (i < length1){
						++i;
						k = i;
						chrom2I = chrom1I;
					} else {
						++chrom1I;
						i = 0;
						k = i+1;
						chrom2I = chrom1I;
					}
				}
			}
						
			if (hpDistance > newHPDistance){
				Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> operation = Utilities.checkExtremities(extremity1, 
						extremity2, extremity3, extremity4);
				operationList.addOperation(operation.getFirst());
				operationList.addOperation(operation.getSecond());
				operationList.addAdjacencyArrayG1(dataWork.getAdjGraph().getAdjacenciesGenome1());
				hpDistance = newHPDistance;
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
