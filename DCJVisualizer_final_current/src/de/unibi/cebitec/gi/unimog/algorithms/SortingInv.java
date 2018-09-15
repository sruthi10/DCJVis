package de.unibi.cebitec.gi.unimog.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


import de.unibi.cebitec.gi.unimog.datastructure.AdditionalDataHPDistance;
import de.unibi.cebitec.gi.unimog.datastructure.AdjacencyGraph;
import de.unibi.cebitec.gi.unimog.datastructure.Chromosome;
import de.unibi.cebitec.gi.unimog.datastructure.Component;
import de.unibi.cebitec.gi.unimog.datastructure.Data;
import de.unibi.cebitec.gi.unimog.datastructure.Genome;
import de.unibi.cebitec.gi.unimog.datastructure.IAdditionalData;
import de.unibi.cebitec.gi.unimog.datastructure.OperationList;
import de.unibi.cebitec.gi.unimog.datastructure.Pair;
import de.unibi.cebitec.gi.unimog.datastructure.multifurcatedTree.CountingVisitorInv;
import de.unibi.cebitec.gi.unimog.datastructure.multifurcatedTree.MultifurcatedTree;
import de.unibi.cebitec.gi.unimog.utils.Constants;

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
 * pi = permutation
 * vi = arc connecting the elements i and i+1
 * overlap: 2 arc intersect each other
 * arc is oriented if pi_i and pi_i+1 have different signs, unoriented otherwise
 * overlap graph: arcs are represented as vertices and an edge is drawn between 2 arcs if they overlap
 * isolated vertices are already adjacencies in the genomes
 * a component is a connected component of overlap graph
 * component is oriented, if one of its vertices is oriented, unoriented otherwise
 * local complementation is nothing else than application of a reversal
 * safe depicts an oriented vertex not creating an unoriented component in new graph <- we need these inversions
 * 
 * This is an implementation of the algorithm from Tannier - Advances on sorting by reversals.
 * For destruction of unoriented components Erdös, Stoye - Balanced vertices in trees... and
 * Stoye - Reversal distance without hurdles & fortresses is combined.
 * 
 */

public class SortingInv implements ISorting {

	@Override
	public OperationList findOptSortSequence(final Data data, final IAdditionalData additionalData,
			 final HashMap<Integer, Integer> chromMap) {
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////
		//---------------------initialize data structures------------------------------------------------------
		///////////////////////////////////////////////////////////////////////////////////////////////////////
		
		final OperationList operationList = new OperationList(); 
		int[] involvedExtremities = new int[Constants.NB_EXTREMITIES];

		//preprocessing (construction of component Tree & components)
		int[] adjG1 = data.getAdjGraph().getAdjacenciesGenome1().clone();
		AdditionalDataHPDistance additionalHPData = (AdditionalDataHPDistance) additionalData;
		HPBasedDistPreprocessing hpPreprocess = new HPBasedDistPreprocessing(data, additionalHPData);
		int[] plusCappedGnom = additionalHPData.getGenomeCappedPlusArray().clone();
		MultifurcatedTree componentTree = hpPreprocess.getCompTree();
		CountingVisitorInv countingVisitor = new CountingVisitorInv();
		componentTree.topDown(countingVisitor);
		int nbWhiteLeaves = countingVisitor.getNbWhiteLeaves(); //might be 1 to small -> check whiteRootNodeIndex
		int whiteRootNodeIndex = countingVisitor.getWhiteRootNodeIndex();
		ArrayList<Integer> whiteLeafParents = countingVisitor.getWhiteLeafParents();
		
		//data structures for replacing things
		Genome newGenome = data.getGenomeA().clone();
		Data dataWorkNew = new Data(newGenome, data.getGenomeB(), data.getAdjGraph().clone());
		AdditionalDataHPDistance addDataWorkNew = new AdditionalDataHPDistance(newGenome);
		AdjacencyGraph adjGraphNew = new AdjacencyGraph(newGenome, data.getGenomeB());
		IntermediateGenomesGenerator genomeGenerator = new IntermediateGenomesGenerator();
		int[] genePos = Utilities.getGenePos(newGenome).get(0);
		
		int[] nodeToCompMap = hpPreprocess.getNodeToCompMap();
		ArrayList<Integer> whiteLeafToIndexMap = countingVisitor.getWhiteLeafToIndexMap();
		ArrayList<Component> components = hpPreprocess.getComponents();
		Component fstComp;
		Component scndComp;
		int fstCutI = -1;
		int scndCutI = -1;
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////
		//--------------------- destroy/orient unoriented components ------------------------------------------
		///////////////////////////////////////////////////////////////////////////////////////////////////////
		while (nbWhiteLeaves > 0){
			boolean allInOne = true;
			for (int i=0; i<whiteLeafParents.size(); ++i){
				if (whiteLeafParents.get(i) == 0){
					allInOne = false;
				}
			}
			if (whiteRootNodeIndex > -1 && allInOne){
				//always CUT components by using adjacencies belonging to the same cycle!!!!!
				fstComp = components.get(nodeToCompMap[whiteRootNodeIndex]);
				involvedExtremities = this.generateInvolvedExtremities(fstComp, plusCappedGnom, adjG1, genePos);
				whiteRootNodeIndex = -1;						
			} else
			if (nbWhiteLeaves > 1){ //at first merge components across branches containing inner white nodes
				int whiteLeafIndex = this.getWhiteLeafIndex(whiteLeafParents, 0);
				int scndWhiteLeafIndex;
				if (whiteLeafIndex < 0){
					whiteLeafIndex = 0;
					scndWhiteLeafIndex = 1;
				} else { //check for a leaf with a different white parent -> there must be at least one
					scndWhiteLeafIndex = this.getWhiteLeafIndex(whiteLeafParents, whiteLeafParents.get(whiteLeafIndex)); //
				}
				
				fstComp = components.get(nodeToCompMap[whiteLeafToIndexMap.get(whiteLeafIndex)]);
				scndComp = components.get(nodeToCompMap[whiteLeafToIndexMap.get(scndWhiteLeafIndex)]);
				fstCutI = Utilities.getSmallerIndexFst(fstComp.getStartIndex(), fstComp.getEndIndex()).getFirst();
				scndCutI = Utilities.getSmallerIndexFst(scndComp.getStartIndex(), scndComp.getEndIndex()).getFirst();
				
				involvedExtremities[0] = Utilities.getExtremity(plusCappedGnom[fstCutI]*2, true);
				involvedExtremities[1] = Utilities.getExtremity(plusCappedGnom[fstCutI+1]*2, false);
				involvedExtremities[2] = Utilities.getExtremity(plusCappedGnom[scndCutI]*2, true);
				involvedExtremities[3] = Utilities.getExtremity(plusCappedGnom[scndCutI+1]*2, false);
			} else 
			if (nbWhiteLeaves == 1){
				fstComp = components.get(nodeToCompMap[whiteLeafToIndexMap.get(0)]);
				involvedExtremities = this.generateInvolvedExtremities(fstComp, plusCappedGnom, adjG1, genePos);
			}
			
			//save operation //////////////////////////////////////////////////
			adjG1 = Utilities.assignInvolvedExtr(involvedExtremities, adjG1, false);
			operationList.addOperation(new Pair<Integer, Integer>(involvedExtremities[0], involvedExtremities[2]));
			operationList.addOperation(new Pair<Integer, Integer>(involvedExtremities[1], involvedExtremities[3]));
			operationList.addAdjacencyArrayG1(adjG1.clone());
			
			/////////////// Update data structures /////////////////////////////////////////////////
			newGenome = genomeGenerator.generateGenome(adjG1);
			adjGraphNew = new AdjacencyGraph(newGenome, data.getGenomeB());
			dataWorkNew = new Data(newGenome, data.getGenomeB(), adjGraphNew);
			addDataWorkNew = new AdditionalDataHPDistance(newGenome);
			plusCappedGnom = addDataWorkNew.getGenomeCappedPlusArray();
			hpPreprocess = new HPBasedDistPreprocessing(dataWorkNew, addDataWorkNew);
			components = hpPreprocess.getComponents();
			componentTree = hpPreprocess.getCompTree();
			countingVisitor = new CountingVisitorInv();
			componentTree.topDown(countingVisitor);
			nodeToCompMap = hpPreprocess.getNodeToCompMap();
			whiteLeafToIndexMap = countingVisitor.getWhiteLeafToIndexMap();
			whiteLeafParents = countingVisitor.getWhiteLeafParents();
			nbWhiteLeaves = countingVisitor.getNbWhiteLeaves();
			whiteRootNodeIndex = countingVisitor.getWhiteRootNodeIndex();
			genePos = Utilities.getGenePos(newGenome).get(0);
			DistanceInv dist = new DistanceInv();
			dist.calculateDistance(dataWorkNew, addDataWorkNew);
			//////////////////////////////////////////////////////////////////////////////////////
		}
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////Sorting algorithm of Tannier et. al - Advances on sorting by reversals ///////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//Generate genePos array, V & boolean array for oriented arcs
		// the arcs are numbered from left to right with 0, 1, 2... for (0t,1h), (1t,2h), (2t,3h)...
		//will add 0 if its not an adj yet, then 1, 2, 3... if they dont form an adjacency, then check left to right
		genePos = Utilities.getGenePos(newGenome).get(0); //because inv dist relies on only one chrom
		ArrayList<Integer> orientedArcs = new ArrayList<Integer>(); 
		HashMap<Integer, Integer> freeArcs = new HashMap<Integer, Integer>();
		
		//scan through genome left to right & add to orientedArcs & freeArcs:
		int[] genes = newGenome.getChromosome(0).getGenes();
		int gene = 0;
		int nextGene = 0;
		int prevGene = 0;
		final OperationList opListS1 = new OperationList(); //the two lists for saving ops
		OperationList opListS2 = new OperationList(); 
		ArrayList<Pair<Integer, Integer>> operationsS1;
		ArrayList<Pair<Integer, Integer>> operationsS2;
		for (int i=0; i<newGenome.getNumberOfGenes()-1; ++i){
			gene = genes[i];
			if (gene > 0 && gene+1 != genes[i+1]){ //(1,3,2,4) -> genes[0] = 1+1 != genes[2] = 3
				nextGene = genes[genePos[gene+1]];
				if (nextGene < 0){//gene > 0 && nextGene < 0 || gene < 0 && nextGene > 0){
					orientedArcs.add(gene);
				}
				freeArcs.put(gene, gene);
			} else 
			if (gene < 0 && i !=0 && gene-1 != genes[i-1]){
				nextGene = genes[genePos[Math.abs(gene-1)]];
				if (nextGene > 0){//gene > 0 && nextGene < 0 || gene < 0 && nextGene > 0){
					orientedArcs.add(-gene);
				}
				freeArcs.put(-gene, -gene);
			}
		}
		
		int unorGene = 0;
		int gene3 = 0;
		int gene4 = 0;
		while (orientedArcs.size() > 0){
			genes = newGenome.getChromosome(0).getGenes();
			genePos = Utilities.getGenePos(newGenome).get(0);
			gene = genes[genePos[orientedArcs.get(0)]];
			unorGene = Math.abs(gene);
			involvedExtremities[0] = unorGene*2;
			involvedExtremities[1] = (unorGene+1)*2-1;
			Pair<Integer, Integer> fstPair = new Pair<Integer, Integer>(involvedExtremities[0], involvedExtremities[1]);
			opListS1.addOperation(fstPair);
			int start = genePos[unorGene]; //fst index OF the interval
			int end = 0; //fst index AFTER the interval
			if (gene > 0){
				gene3 = genes[genePos[gene]+1];
				gene4 = genes[genePos[gene+1]+1];
				involvedExtremities[2] = Utilities.getExtremity(gene3*2, false);
				involvedExtremities[3] = Utilities.getExtremity(gene4*2, false);	
				++start;
			} else {
				gene3 = genes[genePos[unorGene]-1];
				gene4 = genes[genePos[unorGene+1]-1];
				involvedExtremities[2] = Utilities.getExtremity(gene3*2, true);
				involvedExtremities[3] = Utilities.getExtremity(gene4*2, true);
				++end;
			}
			Pair<Integer, Integer> scndPair = new Pair<Integer, Integer>(involvedExtremities[2], involvedExtremities[3]);
			opListS1.addOperation(scndPair);
			end += genePos[Math.abs(gene4)];
			if (start > end){
				int intermed = start;
				start = end;
				end = intermed;
			}
			
			//check whether this operation is already contained in opListS2 and delete if that's the case
			opListS2 = this.removeOperationsIfThere(opListS2, fstPair, scndPair);
			
			//save operation
			adjG1 = this.assignOperation(adjG1, involvedExtremities);
			newGenome = this.invertInterval(newGenome, start, end);
			genePos = Utilities.getGenePos(newGenome).get(0);
			operationList.addAdjacencyArrayG1(adjG1.clone());
			
			freeArcs.remove(Integer.valueOf(unorGene)); //update remaining possible operation list
			orientedArcs.remove(Integer.valueOf(unorGene));
			if (involvedExtremities[2]+1 == involvedExtremities[3]){
				freeArcs.remove(Integer.valueOf(Math.abs(gene3)));
			} else
			if (involvedExtremities[2]-1 == involvedExtremities[3]){
				freeArcs.remove(Integer.valueOf(Math.abs(gene4)));
			}
			
			//if fst element of S2 is unoriented -> go one step back & that's what we do here, we delete them
			if (freeArcs.size() > 0){
				//check for oriented arc in updated v &  if not reapply inversions until there is one.
				//check through interval from gene to scnd gene & generate new oriented arcs!
				for (int i=start; i<end; ++i){
					gene = genes[i];
					unorGene = Math.abs(gene);
					//remove oriented arcs which do not exist anymore, remember newGenome and adjG1 are already updated, while genes array is not!
					if (orientedArcs.contains(unorGene) && (gene > 0 && genes[genePos[unorGene+1]] > 0 ||
							gene < 0 && genes[genePos[unorGene+1]] < 0)){
						orientedArcs.remove(Integer.valueOf(unorGene));
					}
					if (gene > 0 && orientedArcs.contains(gene-1) && genes[genePos[gene-1]] > 0){
						orientedArcs.remove(Integer.valueOf(gene-1));
					} else
					if (gene < 0 && orientedArcs.contains(unorGene-1) && genes[genePos[unorGene-1]] < 0){
						orientedArcs.remove(Integer.valueOf(unorGene-1));
					}
					
					//check for new oriented arc
					if (gene > 0){ 
						nextGene = genes[genePos[gene+1]];
						prevGene = genes[genePos[gene-1]];
						if (nextGene < 0 && !orientedArcs.contains(gene) && freeArcs.containsKey(gene)){
							orientedArcs.add(gene);
						}
						if (prevGene < 0 && !orientedArcs.contains(-prevGene) && freeArcs.containsKey(-prevGene)){
							orientedArcs.add(-prevGene);
						}
					} else 
					if (gene < 0){ 
						nextGene = genes[genePos[Math.abs(gene-1)]];
						prevGene = genes[genePos[Math.abs(gene+1)]]; 
						if (nextGene > 0 && !orientedArcs.contains(unorGene) && freeArcs.containsKey(unorGene)){
							orientedArcs.add(unorGene);
						}
						if (prevGene > 0 && !orientedArcs.contains(prevGene) && freeArcs.containsKey(prevGene)){
							orientedArcs.add(prevGene);
						}
					} 
					
				}

				//check if there is still an oriented arc & if not go back till there is one!
				operationsS1 = opListS1.getOperationList();
				while (orientedArcs.size() <= 0){
					fstPair = operationsS1.get(operationsS1.size()-2); //if not sure which one formed adj - check it
					scndPair = operationsS1.get(operationsS1.size()-1);
					operationsS1.remove(operationsS1.size()-2);
					operationsS1.remove(operationsS1.size()-1);
					operationList.removeAdjArray(operationList.getAdjacencyArrayListG1().size()-1);
					opListS1.setOperationList(operationsS1);
					adjG1[fstPair.getFirst()] = scndPair.getFirst();
					adjG1[scndPair.getFirst()] = fstPair.getFirst();
					adjG1[fstPair.getSecond()] = scndPair.getSecond();
					adjG1[scndPair.getSecond()] = fstPair.getSecond();
					start = genePos[(fstPair.getSecond()+1)/2];
					end = genePos[(scndPair.getSecond()+1)/2];
					if (genes[start] < 0){
						Pair<Integer, Integer> intervalBorders = this.getIntervalBorders(fstPair, scndPair, genePos);
						start = intervalBorders.getFirst();
						end = intervalBorders.getSecond();
					}
					if (end < start){
						int intermed = start;
						start = end;
						end = intermed;
					}
					newGenome = this.invertInterval(newGenome, start, end);
					genes = newGenome.getChromosome(0).getGenes();
					genePos = Utilities.getGenePos(newGenome).get(0);
					opListS2.addOperationBegin(scndPair);
					opListS2.addOperationBegin(fstPair); //move this op to scnd op list
					//check if now oriented arc is in V
					orientedArcs.clear(); //TODO: could implement interval check here -> put it in method
					Iterator<Integer> iterator = freeArcs.keySet().iterator();
					for (int i=0; i<freeArcs.size(); ++i){ //generate new oriented arcs array
						gene = genes[genePos[iterator.next()]];
						if (gene > 0){ //(1,3,2,4) -> genes[0] = 1+1 != genes[2] = 3
							nextGene = genes[genePos[gene+1]];
							prevGene = genes[genePos[gene-1]];
							if (nextGene < 0 && !orientedArcs.contains(gene) && freeArcs.containsKey(gene)){
								orientedArcs.add(gene);
							}
							if (prevGene < 0 && !orientedArcs.contains(-prevGene) && freeArcs.containsKey(-prevGene)){
								orientedArcs.add(-prevGene);
							}
						} else 
						if (gene < 0){ 
							nextGene = genes[genePos[Math.abs(gene-1)]];
							prevGene = genes[genePos[Math.abs(gene+1)]];
							if (nextGene > 0 && !orientedArcs.contains(-gene) && freeArcs.containsKey(-gene)){
								orientedArcs.add(-gene);
							}
							if (prevGene > 0 && !orientedArcs.contains(prevGene) && freeArcs.containsKey(prevGene)){
								orientedArcs.add(prevGene);
							}//TODO: melt this with other if clause up there in extra method
						} 
					}
				}
			} else {
				break;
			}
			
		}
		operationsS1 = opListS1.getOperationList();
		operationsS2 = opListS2.getOperationList();
		ArrayList<Pair<Integer, Integer>> operations = operationList.getOperationList();
		for (int i=0; i< operationsS1.size(); ++i){
			operations.add(operationsS1.get(i));
		}
		for (int i=0; i< operationsS2.size(); ++i){
			operations.add(operationsS2.get(i));
			if (i%2 == 0){
				operationList.addAdjacencyArrayG1(this.generateAdjArray(adjG1, operationsS2.get(i), operationsS2.get(i+1)));
			}
		}
		operationList.setOperationList(operations);
		
		return operationList;
	}

	
	/**
	 * Returns the index of the whiteLeafParents array which is different from the 
	 * excluded value and > 0.
	 * @param whiteLeafParents array containing at each index if the white leaf with
	 * 			this index has a white parent, and if so, which index it has
	 * @param excludedValue a value that should be excluded
	 * @return the index of the desired leaf or -1 if there is no such leaf
	 */
	private int getWhiteLeafIndex(final ArrayList<Integer> whiteLeafParents, final int excludedValue) {
		for (int i=0; i<whiteLeafParents.size(); ++i){
			if (whiteLeafParents.get(i) != excludedValue && whiteLeafParents.get(i) > 0){
				return i;
			}
		}
		for (int i=0; i<whiteLeafParents.size(); ++i){
			if (whiteLeafParents.get(i) != excludedValue && whiteLeafParents.get(i) == 0){
				return i;
			}
		}
		return -1; //shouldnt occur?
	}


	/**
	 * Generates the involved extremities in order to CUT (orient) a single component by cutting 
	 * after the first gene of the component and the inner neighbour of that gene -> neighbour 
	 * of 5 is 6, 6 must be included in component, since it contains all genes of a certain interval. 
	 * @param comp The component to CUT
	 * @param plusCappedGnom the plus capped genome array
	 * @param adjArray the adjacencies of the genome
	 * @param genePos the array containing the gene position in the plus capped genome array for each gene
	 * @return the array of the 4 involved extremities in the two cuts
	 */
	private int[] generateInvolvedExtremities(final Component comp, final int[] plusCappedGnom, 
			final int[] adjArray, final int[] genePos) {
		//get 2 adjacencies of the same cycle and orient them TODO: if used more often -> extra method acessible for other algos
		int fstCutI = Utilities.getSmallerIndexFst(comp.getStartIndex(), comp.getEndIndex()).getFirst();
		int[] involvedExtremities = new int[4];
		involvedExtremities[0] = Utilities.getExtremity(plusCappedGnom[fstCutI]*2, true);
		involvedExtremities[1] = Utilities.getExtremity(plusCappedGnom[fstCutI+1]*2, false);
		if (involvedExtremities[0] % 2 == 1){
			involvedExtremities[2] = involvedExtremities[0]-1;
		} else {
			involvedExtremities[2] = involvedExtremities[0]+1;
		}
		involvedExtremities[3] = adjArray[involvedExtremities[2]];
		int scndCut = plusCappedGnom[genePos[(involvedExtremities[2]+1)/2]];
		involvedExtremities = this.getExtremity(scndCut, involvedExtremities);
		return involvedExtremities;
	}


	/**
	 * Sorts the two extremities of the second cut in the correct order.
	 * array[2] is always the left extremity of the cut and array[3] the right one.
	 * @param gene the gene with its orientation taken from the genome
	 * @param involvedExtremities the array of the 4 involved extremities in the two cuts
	 * @return the updated involvedExtremities array
	 */
	private int[] getExtremity(final int gene, final int[] involvedExtremities) {
		int fstExtremity = involvedExtremities[2];
		if (gene > 0 && fstExtremity % 2 == 0 || gene < 0 && fstExtremity % 2 == 1){
				return involvedExtremities;
		} else {
			involvedExtremities[2] = involvedExtremities[3];
			involvedExtremities[3] = fstExtremity;
		}
		return involvedExtremities;
	}

	/**
	 * Generates a new adjacency array for a given operation consisting of
	 * two pairs oder adjacencies to assign and returns a cloned instance of it.
	 * @param adjG1 old adjacency array (which is also updated since it is not a clone!)
	 * @param fstPair the first pair of the operation
	 * @param scndPair the second pair of the operation
	 * @return a clone of the new adjacency array
	 */
	private int[] generateAdjArray(int[] adjG1, Pair<Integer, Integer> fstPair,
			Pair<Integer, Integer> scndPair) {
		
		adjG1[fstPair.getFirst()] = fstPair.getSecond();
		adjG1[fstPair.getSecond()] = fstPair.getFirst();
		adjG1[scndPair.getFirst()] = scndPair.getSecond();
		adjG1[scndPair.getSecond()] = scndPair.getFirst();
		return adjG1.clone();
	}
	

	/**
	 * Calculates the interval borders of an operation which are used for inverting the interval.
	 * @param fstPair fst operation
	 * @param scndPair scnd operation
	 * @param genePos array with gene positions
	 * @return the pair containing the interval borders
	 */
	private Pair<Integer, Integer> getIntervalBorders(Pair<Integer, Integer> fstPair,
			Pair<Integer, Integer> scndPair, int[] genePos) {
		int fstfst = genePos[(fstPair.getFirst()+1)/2];
		int fstscnd = genePos[(fstPair.getSecond()+1)/2];
		int scndfst = genePos[(scndPair.getFirst()+1)/2];
		int scndscnd = genePos[(scndPair.getSecond()+1)/2];
		ArrayList<Integer> order = new ArrayList<Integer>();
		if (fstfst < fstscnd){
			order.add(fstfst);
			order.add(fstscnd);
		} else {
			order.add(fstscnd);
			order.add(fstfst);
		}
		
		if (scndfst < order.get(0)){
			order.add(0, scndfst);
		} else
		if (scndfst > order.get(1)){
			order.add(scndfst);
		} //other case not interesting
		
		if (scndscnd < order.get(0)){
			order.add(0, scndscnd);
		} else
		if (scndscnd > order.get(2)){
			order.add(scndscnd);
		} //other cases not interesting
		
		return new Pair<Integer, Integer>(order.get(0)+1, order.get(order.size()-1));
	}

	/**
	 * Inverts the signs and order of a given interval in the genome handed over to the method.
	 * @param genome the genome whose first chromosome should be inverted
	 * @param start the start index of the interval to invert
	 * @param end the end index of the interval to invert
	 * @return the new genome with a replaced first chromosome by the one with the inverted interval
	 */
	private Genome invertInterval(final Genome genome, final int start, final int end) {
			
		int[] genes = genome.getChromosome(0).getGenes();
		int[] interval = new int[end-start];
		int counter = 0;
		for (int i=end-1; i>=start; --i){
			interval[counter++] = -genes[i];
		}
		for (int i=0; i<interval.length; ++i){
			genes[start+i] = interval[i];
		}
		genome.replaceChromosome(new Chromosome(genes, false), 0);
		return genome;
	}

	/**
	 * Performs an inversion by assigining the involved extremities to the
	 * current adjacency graph
	 * @param adjG1 current adjacency graph
	 * @param involvedExtremities the four involved extremities
	 * @return the updated adjacency graph
	 */
	private int[] assignOperation(int[] adjG1, int[] involvedExtremities) {
		adjG1[involvedExtremities[0]] = involvedExtremities[1];
		adjG1[involvedExtremities[1]] = involvedExtremities[0];
		adjG1[involvedExtremities[2]] = involvedExtremities[3];
		adjG1[involvedExtremities[3]] = involvedExtremities[2];
		return adjG1;
	}

	/**
	 * Removes an operation from the operation list and also the corresponding
	 * adjacency graph.
	 * @param operationList the list of operations including adjacency graphs
	 * @param fstRemovePair the fst integer pair to remove
	 * @param scndRemovePair the scnd integer pair to remove
	 * @return the updated operation list
	 */
	private OperationList removeOperationsIfThere(OperationList operationList,
			Pair<Integer, Integer> fstRemovePair, Pair<Integer, Integer> scndRemovePair) {
		ArrayList<Pair<Integer, Integer>> operations = operationList.getOperationList();
		operations = this.removeOperationIfThere(operations, scndRemovePair);
		operations = this.removeOperationIfThere(operations, fstRemovePair);
		operationList.setOperationList(operations);
		return operationList;
	}
	
	/**
	 * Removes a pair from the arraylist of pairs handed over to the method if
	 * this pair is already contained in the list. Used to remove an operation
	 * if it is already present in the operation list.
	 * @param operations the list of operations as arraylist of integer pairs
	 * @param pairToRemove the integer pair to remove
	 * @return the updated arraylist of pairs
	 */
	private ArrayList<Pair<Integer, Integer>> removeOperationIfThere(ArrayList<Pair<Integer, Integer>> operations, 
			Pair<Integer, Integer> pairToRemove){
		Pair<Integer, Integer> pairSwitched = new Pair<Integer, Integer>(pairToRemove.getSecond(), pairToRemove.getFirst());
		int index = 0;
		if (operations.contains(pairToRemove)){
			index = operations.indexOf(pairToRemove);
			operations.remove(pairToRemove);
			operations.remove(index);
		} else 
		if (operations.contains(pairSwitched)){
			index = operations.indexOf(pairSwitched);
			operations.remove(pairSwitched);
			operations.remove(index);
		}

		return operations;
	}	
}