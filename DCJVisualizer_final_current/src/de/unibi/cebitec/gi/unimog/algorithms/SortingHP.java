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
 * Class containing the HP sorting algorithm running in n² time by Tesler (
 * Efficient algorithms for multichromosomal genome rearrangements) and including
 * the correction by Jean & Nikolski (A correct algorithm for optimal capping) and
 * Flato & Shamir (Two notes on genome rearrangements).
 */
public class SortingHP implements ISorting {
	
	private ArrayList<Component> unorComps = new ArrayList<Component>();
	private ArrayList<Component> unorRealComps = new ArrayList<Component>();
	private ArrayList<Component> setSemiRComps = new ArrayList<Component>();
	//private Pair<int[], Integer> knots; //determines knots of unorComps
	private Pair<int[], Integer> realKnots; //determines real knots of unorRealComps
	private HashMap<Integer, ArrayList<Pair<Integer, Integer>>> lPiOwnerIndInSRComps = new HashMap<Integer, ArrayList<Pair<Integer, Integer>>>();
	private Component greatestSRKnot;
	private boolean fortressRealKnots = false;
	private ArrayList<Pair<Integer, Integer>> llPaths = new ArrayList<Pair<Integer, Integer>>(); //they contain the indices at first and the kind 
	private ArrayList<Pair<Integer, Integer>> piPiPaths = new ArrayList<Pair<Integer, Integer>>();
	private ArrayList<Pair<Integer, Integer>> lPiPaths = new ArrayList<Pair<Integer, Integer>>();
	private int nbGenes;
	private ArrayList<Pair<Integer, Integer>> telomerPosA = new ArrayList<Pair<Integer,Integer>>();
	private ArrayList<Pair<Integer, Integer>> telomerPosB = new ArrayList<Pair<Integer,Integer>>();
	private int currIndex = -1;
	private Pair<Integer, Integer> singleChromCaps = new Pair<Integer, Integer>(-1, -1);
	private boolean wholeGenOrComp = false;
	
	IntermediateGenomesGenerator genomeGenerator = new IntermediateGenomesGenerator();

	@Override
	public OperationList findOptSortSequence(final Data data, final IAdditionalData additionalData, 
			final HashMap<Integer, Integer> chromMap) {
		
		OperationList operationList = new OperationList(); 	
		int[] involvedExtremities = new int[Constants.NB_EXTREMITIES];

		////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//preprocessing creating a concatenate of A and of B & new adjacency graph = treatment as one linear chrom
		////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		int nbChromsBiggest = data.getGenomeA().getNumberOfChromosomes();
		if (data.getGenomeA().getNumberOfChromosomes() < data.getGenomeB().getNumberOfChromosomes()){
			nbChromsBiggest = data.getGenomeB().getNumberOfChromosomes();
		}
		
		this.nbGenes = data.getGenomeA().getNumberOfGenes();
		int[] cappedG1 = this.capGenome(data.getGenomeA(), nbChromsBiggest);
		Chromosome chromA = new Chromosome(cappedG1, false);
		Chromosome chromB = new Chromosome(this.capGenome(data.getGenomeB(), nbChromsBiggest), false);
		ArrayList<Chromosome> chromsA = new ArrayList<Chromosome>();
		ArrayList<Chromosome> chromsB = new ArrayList<Chromosome>();
		chromsA.add(chromA);
		chromsB.add(chromB);
		Genome concatGenomeA = new Genome(chromsA);
		Genome concatGenomeB = new Genome(chromsB);
		int[] genePos = Utilities.getGenePos(concatGenomeA).get(0);
		AdjacencyGraph concatAdjGraph = new AdjacencyGraph(concatGenomeA, concatGenomeB);
		int[] cappingIndepndntGraph = this.genCappingIndepndntGraph(concatAdjGraph.getAdjacenciesGenome1(), 
				this.nbGenes, data.getGenomeA().getNumberOfChromosomes());
		int[] capDistinction = this.genCapDistinction(data.getAdjGraph().clone(), 
				concatAdjGraph.getAdjacenciesGenome1(), data.getGenomeA().getNumberOfChromosomes());
		
		AdditionalDataHPDistance additionalHPData = (AdditionalDataHPDistance) additionalData;
		HPBasedDistPreprocessing hpPreprocess = new HPBasedDistPreprocessing(data, additionalHPData);
		int[] plusCappedGnom = additionalHPData.getGenomeCappedPlusArray().clone();
		ArrayList<Component> components = hpPreprocess.getComponents();
		AdditionalDataHPDistance additionalHPDataNew;
		HPBasedDistPreprocessing hpPreprocessNew;
		Data dataNew;

		this.identifyAllCapPaths(capDistinction, cappingIndepndntGraph, 
				concatAdjGraph.getAdjacenciesGenome1(), components); //find all l-l paths, pi-pi paths and pi-l paths
		this.classifyUnorComponents(components, capDistinction, plusCappedGnom, cappingIndepndntGraph, genePos);
		
		ArrayList<int[]> semiRKnotExtList = new ArrayList<int[]>();
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		//////////// main part of the capping algorithm ////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		HashMap<Integer, Integer> correctCaps = new HashMap<Integer, Integer>();
		for (int i=0; i<this.llPaths.size(); ++i){ //connect (and close) all l-l paths with a pi-pi path 
			//check whether there is an oriented path available
			int[] involvedExtr = this.getOrientedEtr(this.llPaths.get(i), this.piPiPaths, genePos);
			cappingIndepndntGraph[involvedExtr[0]] = involvedExtr[1];
			cappingIndepndntGraph[involvedExtr[1]] = involvedExtr[0];
			cappingIndepndntGraph[involvedExtr[2]] = involvedExtr[3];
			cappingIndepndntGraph[involvedExtr[3]] = involvedExtr[2];
			correctCaps.put(involvedExtr[0], involvedExtr[1]);
			correctCaps.put(involvedExtr[1], involvedExtr[0]);
			correctCaps.put(involvedExtr[2], involvedExtr[3]);
			correctCaps.put(involvedExtr[3], involvedExtr[2]);
		}
		
		Pair<Integer, Integer> path;
		for (int i=0; i<this.piPiPaths.size(); ++i){ //close all remaining pi-pi paths
			path = this.piPiPaths.get(i);
			cappingIndepndntGraph[path.getFirst()] = path.getSecond();
			cappingIndepndntGraph[path.getSecond()] = path.getFirst();
			correctCaps.put(path.getFirst(), path.getSecond());
			correctCaps.put(path.getSecond(), path.getFirst());
		}

		ArrayList<Pair<Integer, Integer>> lpiPaths;
		int currSemiRKnots = this.setSemiRComps.size(); //if its a fortress RK close its path or a SRK path
		if (this.fortressRealKnots && this.setSemiRComps.size() % 2 == 0 && this.setSemiRComps.size() >= 2){
			if (this.greatestSRKnot != null){ //close all l-pi paths in the greatest semi real knot
				for (int i=0; i<this.setSemiRComps.size(); ++i){
					if (this.setSemiRComps.get(i).equals(this.greatestSRKnot)){
						lpiPaths = this.lPiOwnerIndInSRComps.get(i);
						for (Pair<Integer, Integer> lpiPath : lpiPaths){
							cappingIndepndntGraph[lpiPath.getFirst()] = lpiPath.getSecond();
							cappingIndepndntGraph[lpiPath.getSecond()] = lpiPath.getFirst();
							correctCaps.put(lpiPath.getFirst(), lpiPath.getSecond());
							correctCaps.put(lpiPath.getSecond(), lpiPath.getFirst());
							this.lPiPaths.remove(lpiPath);
						}
						this.lPiOwnerIndInSRComps.remove(i); //handled already, so removed
						this.setSemiRComps.remove(i);
						break;
					}
				}
			} else { // close all pi-l path in any one semi real knot
				lpiPaths = this.lPiOwnerIndInSRComps.get(0);
				for (Pair<Integer, Integer> lpiPath : lpiPaths){
					cappingIndepndntGraph[lpiPath.getFirst()] = lpiPath.getSecond();
					cappingIndepndntGraph[lpiPath.getSecond()] = lpiPath.getFirst();
					correctCaps.put(lpiPath.getFirst(), lpiPath.getSecond());
					correctCaps.put(lpiPath.getSecond(), lpiPath.getFirst());
				}
				this.lPiOwnerIndInSRComps.remove(0); //handled already, so removed
				this.setSemiRComps.remove(0);
			}
			--currSemiRKnots;
		}
		
		while (currSemiRKnots > 1){ //connect (and close) all l-pi paths within pairs of semi real knots
			involvedExtremities = this.getOrientedEtr2(genePos);
			//check whether an unoriented component remains when connecting these two semi real knots & orient it by flipping
			semiRKnotExtList.add(involvedExtremities.clone());
			cappingIndepndntGraph[involvedExtremities[0]] = involvedExtremities[1];
			cappingIndepndntGraph[involvedExtremities[1]] = involvedExtremities[0];
			cappingIndepndntGraph[involvedExtremities[2]] = involvedExtremities[3];
			cappingIndepndntGraph[involvedExtremities[3]] = involvedExtremities[2];
			correctCaps.put(involvedExtremities[0], involvedExtremities[1]);
			correctCaps.put(involvedExtremities[1], involvedExtremities[0]);
			correctCaps.put(involvedExtremities[2], involvedExtremities[3]);
			correctCaps.put(involvedExtremities[3], involvedExtremities[2]);
			currSemiRKnots -= 2;
		}
		
		Pair<Integer, Integer> lPiPath;
		for (int i=0; i<this.lPiPaths.size(); ++i){ //close all remaining l-pi paths
			lPiPath = this.lPiPaths.get(i);
			cappingIndepndntGraph[lPiPath.getFirst()] = lPiPath.getSecond();
			cappingIndepndntGraph[lPiPath.getSecond()] = lPiPath.getFirst();
			correctCaps.put(lPiPath.getFirst(), lPiPath.getSecond());
			correctCaps.put(lPiPath.getSecond(), lPiPath.getFirst());
		}
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////// Create connected genome from obtained adj graph /////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////////////////////		

		Genome newGenome = null;
		AdjacencyGraph newAdjGraph;
		
		if (newGenome == null || data.getGenomeA().getNumberOfChromosomes() > data.getGenomeB().getNumberOfChromosomes()){ 
			//this first correction part only solves the problem, if only semi-real
			//components contribute to the failure. These chromosomes are flipped, if a real component 
			//emerges from natural capping
			// = null means there is at least one circular chromosome if we follow the natural concatenation
			ArrayList<Chromosome> genomeB = data.getGenomeB().getGenome();
			ArrayList<int[]> cappedGenomeB = new ArrayList<int[]>(); //usefull for flipping etc!
			int[] concatGenomeBNew = new int[concatAdjGraph.getAdjacenciesGenome1().length/2];
			int[] genes;
			int[] newGenes;
			int start;
			int end;
			int currInd = 0;
			for (int i=0; i<genomeB.size(); ++i){
				genes = genomeB.get(i).getGenes();
				newGenes = new int[genes.length+2];
				start = Utilities.getExtremity(genes[0]*2, false);
				end = Utilities.getExtremity(genes[genes.length-1]*2, true);
				start = cappingIndepndntGraph[start];
				end = cappingIndepndntGraph[end];
				if (start % 2 == 1){
					start = -((start+1)/2);
				} else {
					start /= 2;
				}
				if (end % 2 == 0){
					end = -((end+1)/2);
				} else {
					end = (end+1)/2;
				}
				newGenes[0] = start;
				System.arraycopy(genes, 0, newGenes, 1, genes.length);
				newGenes[newGenes.length-1] = end;
				cappedGenomeB.add(newGenes.clone());
				System.arraycopy(newGenes, 0, concatGenomeBNew, currInd, newGenes.length);
				currInd += newGenes.length;
			}
			if (data.getGenomeB().getNumberOfChromosomes() == 1){ //if only one chrom in B, both caps needed to assure they're not telomeres of concat genome
				this.singleChromCaps = new Pair<Integer, Integer>(cappedGenomeB.get(0)[0], cappedGenomeB.get(0)[cappedGenomeB.get(0).length-1]);
			}
			
			Chromosome chromNew = new Chromosome(concatGenomeBNew, false);
			ArrayList<Chromosome> newChromList = new ArrayList<Chromosome>();
			newChromList.add(chromNew);
			
			if (newGenome == null || data.getGenomeA().getNumberOfChromosomes() > data.getGenomeB().getNumberOfChromosomes()){
				/////////////////////////////////////////////////////////////////////////////////////////////
				////////////////////////////// Concatenation Algorithm by Tesler ////////////////////////////
				/////////////////////////////////////////////////////////////////////////////////////////////
				////////////////////////////// Initialize block caps //////////////////////////////////////////////
				ArrayList<Chromosome> genome = data.getGenomeA().getGenome();
				ArrayList<Pair<Integer, Integer>> blockCapsA = this.generateBlockCapsA(genome, cappedG1, genePos, nbChromsBiggest);
				genome = data.getGenomeB().getGenome();
				ArrayList<Pair<Integer, Integer>> blockCapsB = this.generateBlockCapsB(genome, correctCaps);
				concatGenomeBNew = this.generateNewGenomeB(blockCapsB, genomeB);
				
				chromB = new Chromosome(concatGenomeBNew, false);
				chromsB.clear();
				chromsB.add(chromB);
				concatGenomeB = new Genome(chromsB);
				
				/////////////////////////////////////////////////////////////////////////////////////////////
				///////////////////////// Proper flip left //////////////////////////////////////////////////
				/////////////////////////////////////////////////////////////////////////////////////////////
				Pair<Genome, Genome> currGenomes = this.renameGenomes(concatGenomeA, concatGenomeB);
				newAdjGraph = new AdjacencyGraph(currGenomes.getFirst(), currGenomes.getSecond());
				dataNew = new Data(currGenomes.getFirst(), currGenomes.getSecond());
				dataNew.setAdjGraph(newAdjGraph);
				additionalHPDataNew = new AdditionalDataHPDistance(currGenomes.getFirst());
				plusCappedGnom = additionalHPData.getGenomeCappedPlusArray().clone();
				hpPreprocessNew = new HPBasedDistPreprocessing(dataNew, additionalHPDataNew);
				components = hpPreprocessNew.getComponents();
				genes = concatGenomeA.getChromosome(0).getGenes();
				
				Pair<Integer, Integer> cutPos; 
				Pair<Integer, Integer> chromBorders;
				int fst;
				int scnd;
				
				for (int i=0; i<components.size(); ++i){
					cutPos = Utilities.getSmallerIndexFst(components.get(i).getStartIndex()-1, components.get(i).getEndIndex()-1);
					if (cutPos.getSecond()-cutPos.getFirst() > 1 && !components.get(i).isOriented()){
						for (int j=0; j<this.telomerPosA.size(); ++j){
							chromBorders = this.telomerPosA.get(j);
							if (chromBorders.getFirst() < cutPos.getFirst() && chromBorders.getSecond() > cutPos.getFirst()
							&& chromBorders.getSecond() < cutPos.getSecond()){ // ||
							//chromBorders.getFirst() > cutPos.getFirst() && chromBorders.getSecond() > cutPos.getSecond()){ <- excluded
								concatGenomeA = this.flipAChrom2(concatGenomeA, chromBorders);
								genes = concatGenomeA.getChromosome(0).getGenes();
								fst = genes[chromBorders.getSecond()];
								scnd = genes[chromBorders.getFirst()];
								for (int k=0; k<blockCapsA.size(); ++k){
									if (Math.abs(blockCapsA.get(k).getFirst()) == Math.abs(fst) || Math.abs(blockCapsA.get(k).getSecond()) == Math.abs(fst)){
										currInd = k;
										break;
									}
								}
								blockCapsA.set(currInd, new Pair<Integer, Integer>(scnd, fst));
								genePos = Utilities.getGenePos(concatGenomeA).get(0);
								break;
							}
						}
					}
					if (cutPos.getSecond()-cutPos.getFirst() >= cutPos.getSecond() && components.get(i).isOriented()){
						this.wholeGenOrComp = true;
					}
				}
				////////////////////////////////////////////////////////////////////////////////////////////////////////
				////////////////////////////////////////////////////////////////////////////////////////////////////////
				////// Special one: chrom in B and both caps are telomeres in both genomes (which is not possible) /////
				
				if (this.singleChromCaps.getFirst() == blockCapsA.get(0).getFirst() &&  
						this.singleChromCaps.getSecond() == blockCapsA.get(blockCapsA.size()-1).getSecond() ||
						this.singleChromCaps.getFirst() == -blockCapsA.get(blockCapsA.size()-1).getSecond() &&  
						this.singleChromCaps.getSecond() == -blockCapsA.get(0).getFirst()){
					Pair<Integer, Integer> capsToInvert = blockCapsA.get(blockCapsA.size()-1);
					fst = capsToInvert.getFirst();
					capsToInvert.setFirst(-capsToInvert.getSecond());
					capsToInvert.setSecond(-fst);
					blockCapsA.set(blockCapsA.size()-1, capsToInvert);
					concatGenomeA = this.flipAChrom2(concatGenomeA, new Pair<Integer, Integer>(genePos[Math.abs(capsToInvert.getSecond())], genePos[Math.abs(capsToInvert.getFirst())]));
				}
				
				////////////////////////////////////////////////////////////////////////////////////////////////////////
				////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				ArrayList<Pair<Integer, Integer>> connectedBlocksInB = new ArrayList<Pair<Integer,Integer>>();
				Pair<Integer, Integer> chromBorders2 = new Pair<Integer, Integer>(-1, -1);
				Pair<Integer, Integer> telomeresG2 = new Pair<Integer, Integer>(-1, -1);
				Pair<Integer, Integer> telomeresG2Two = new Pair<Integer, Integer>(-1, -1);
				Pair<Integer, Integer> chromBordersPrev = new Pair<Integer, Integer>(-1, -1);
				Pair<Integer, Integer> anotherPair = new Pair<Integer, Integer>(-1, -1);
				boolean whichOne1 = false;
				boolean whichOne2 = false;
				boolean whichOne3 = false;
				boolean illegal = false;
				for (int i = blockCapsA.size()-1; i>0; --i){
					chromBorders = blockCapsA.get(i);
					chromBorders2 = blockCapsA.get(i-1);
					for (int j=0; j<blockCapsB.size(); ++j){
						telomeresG2 = blockCapsB.get(j);
						if (Math.abs(chromBorders.getFirst()) == Math.abs(telomeresG2.getFirst())){
							whichOne1 = true;
							break;
						} else
						if (Math.abs(chromBorders.getFirst()) == Math.abs(telomeresG2.getSecond())){
							break;
						}
					} //we found the pair of caps containing the corresponding cap
					for (int j=0; j<connectedBlocksInB.size(); ++j){
						anotherPair = connectedBlocksInB.get(j);
						if (Math.abs(chromBorders.getFirst()) == Math.abs(anotherPair.getFirst())){
							whichOne3 = true;
							break;
						} else
						if (Math.abs(chromBorders.getFirst()) == Math.abs(anotherPair.getSecond())){
							break;
						}
						anotherPair = new Pair<Integer, Integer>(-1, -1);
					}
					if (whichOne1 && Math.abs(chromBorders2.getSecond()) == Math.abs(telomeresG2.getSecond()) ||
						!whichOne1 && Math.abs(chromBorders2.getSecond()) == Math.abs(telomeresG2.getFirst())){
						illegal = true;
					}
					if (whichOne3 && Math.abs(chromBorders2.getSecond()) == Math.abs(anotherPair.getSecond()) ||
						!whichOne3 && Math.abs(chromBorders2.getSecond()) == Math.abs(anotherPair.getFirst())){
						illegal = true;
					}
					if (illegal){ //a suggested bond is illegal, if both caps are caps of the same chromosome in the other genome!
						if (i > 1){//invert current chrom + chrom in front of current chrom to receive new caps
							chromBordersPrev = blockCapsA.get(i-2); //z.B. i-2 = (12,13) & i-1= (-14, 15) -> (-15, 14) + (-13 -12)
							concatGenomeA = this.flipAChrom2(concatGenomeA, new Pair<Integer, Integer>(genePos[Math.abs(chromBordersPrev.getFirst())], genePos[Math.abs(chromBorders2.getSecond())]));
							genePos = Utilities.getGenePos(concatGenomeA).get(0);
							this.updateTelomeres(i);
							anotherPair = chromBorders2; //be careful with anotherPair, its used in different contexts
							chromBorders2 = this.invertPair(chromBordersPrev);
							chromBordersPrev = this.invertPair(anotherPair);
							blockCapsA.set(i-1, chromBorders2);
							blockCapsA.set(i-2, chromBordersPrev);
						} else { //invert current chrom to receive new caps
							concatGenomeA = this.flipAChrom2(concatGenomeA, new Pair<Integer, Integer>(genePos[Math.abs(chromBorders2.getFirst())], genePos[Math.abs(chromBorders2.getSecond())]));
							genePos = Utilities.getGenePos(concatGenomeA).get(0);
							chromBorders2 = this.invertPair(chromBorders2);
							blockCapsA.set(i-1, chromBorders2);
						}
						/////////////////////////////////////////////////////////////////////////////////////////////
						///////////////////////// Proper flip left //////////////////////////////////////////////////
						/////////////////////////////////////////////////////////////////////////////////////////////						
					 	currGenomes = this.renameGenomes(concatGenomeA, concatGenomeB);
						newAdjGraph = new AdjacencyGraph(currGenomes.getFirst(), currGenomes.getSecond());
						dataNew = new Data(currGenomes.getFirst(), currGenomes.getSecond());
						dataNew.setAdjGraph(newAdjGraph);
						additionalHPDataNew = new AdditionalDataHPDistance(currGenomes.getFirst());
						plusCappedGnom = additionalHPData.getGenomeCappedPlusArray().clone();
						hpPreprocessNew = new HPBasedDistPreprocessing(dataNew, additionalHPDataNew);
						components = hpPreprocessNew.getComponents();
						
						for (int k=0; k<components.size(); ++k){
							cutPos = Utilities.getSmallerIndexFst(components.get(k).getStartIndex()-1, components.get(k).getEndIndex()-1);
							if (cutPos.getSecond()-cutPos.getFirst() > 1 && !components.get(k).isOriented()){
								for (int j=0; j<this.telomerPosA.size(); ++j){
									anotherPair = this.telomerPosA.get(j);
									if (anotherPair.getFirst() < cutPos.getFirst() && anotherPair.getSecond() > cutPos.getFirst()
									&& anotherPair.getSecond() < cutPos.getSecond()){ //|| cutPos.getFirst() == 0){ // ||
										//chromBorders.getFirst() > cutPos.getFirst() && chromBorders.getSecond() > cutPos.getSecond()){ <- excluded
										concatGenomeA = this.flipAChrom2(concatGenomeA, anotherPair);
										genes = concatGenomeA.getChromosome(0).getGenes();
										blockCapsA.set(j, new Pair<Integer, Integer>(genes[anotherPair.getFirst()], genes[anotherPair.getSecond()]));
										break;
									}
								}
							}
						}
						/////////////////////////////////////////////////////////////////////////////////////////////
					}
					//Find the pairs of caps containing the corresponding cap in genome B
					for (int j=0; j<blockCapsB.size(); ++j){
						telomeresG2Two = blockCapsB.get(j);
						if (Math.abs(chromBorders2.getSecond()) == Math.abs(telomeresG2Two.getFirst())){
							whichOne2 = true;
							break;
						} else
						if (Math.abs(chromBorders2.getSecond()) == Math.abs(telomeresG2Two.getSecond())){
							break;
						}
					}
					
					//then form bond from i-1 to i - no illegal bond anymore 
					if (whichOne2){
						telomeresG2Two = this.invertPair(telomeresG2Two);					
					}
					if (!whichOne1){
						telomeresG2 = this.invertPair(telomeresG2);
					}
					Pair<Integer, Integer> connectedBlock = new Pair<Integer, Integer>(-1, -1);
					int i1 = -1;
					int i2 = -1;
					for (int j=0; j<connectedBlocksInB.size(); ++j){
						connectedBlock = connectedBlocksInB.get(j);
						if (Math.abs(connectedBlock.getFirst()) == Math.abs(chromBorders.getFirst()) ||
							Math.abs(connectedBlock.getSecond()) == Math.abs(chromBorders2.getSecond()) ||
							Math.abs(connectedBlock.getSecond()) == Math.abs(chromBorders.getFirst()) ||
							Math.abs(connectedBlock.getFirst()) == Math.abs(chromBorders2.getSecond())){
							if (i1 == -1){
								i1 = j;
							} else {
								i2 = j;
								break;
							}
						}
					}
					if (i1 > -1 && i2 > -1){ //connect the two blocks ////////////////////////////////
						Pair<Integer, Integer> connectedBlock1 = connectedBlocksInB.get(i1);
						int blockStart = -1;
						int blockEnd = -1;
						if (Math.abs(connectedBlock1.getFirst()) == Math.abs(chromBorders2.getSecond()) ||
							Math.abs(connectedBlock1.getFirst()) == Math.abs(chromBorders.getFirst())){
							blockStart = -connectedBlock1.getSecond();
						} else 
						if (Math.abs(connectedBlock1.getSecond()) == Math.abs(chromBorders2.getSecond()) ||
							Math.abs(connectedBlock1.getSecond()) == Math.abs(chromBorders.getFirst())){
							blockStart = connectedBlock1.getFirst();
						}
						if (Math.abs(connectedBlock.getFirst()) == Math.abs(chromBorders.getFirst()) ||
							Math.abs(connectedBlock.getFirst()) == Math.abs(chromBorders2.getSecond())){
							blockEnd = connectedBlock.getSecond();
						} else 
						if (Math.abs(connectedBlock.getSecond()) == Math.abs(chromBorders.getFirst()) ||
							Math.abs(connectedBlock.getSecond()) == Math.abs(chromBorders2.getSecond())){
							blockEnd = -connectedBlock.getFirst();
						}
						connectedBlocksInB.set(i1, new Pair<Integer, Integer>(blockStart, blockEnd));
						connectedBlocksInB.remove(i2);
					} else
					if (i1 > -1){ //extend this block /////////////////////////////////////////////////
						connectedBlock = connectedBlocksInB.get(i1);
						if (Math.abs(connectedBlock.getFirst()) == Math.abs(chromBorders.getFirst())){
							connectedBlocksInB.set(i1, new Pair<Integer, Integer>(telomeresG2Two.getFirst(), connectedBlock.getSecond()));
						} else
						if (Math.abs(connectedBlock.getSecond()) == Math.abs(chromBorders2.getSecond())){
							connectedBlocksInB.set(i1, new Pair<Integer, Integer>(connectedBlock.getFirst(), telomeresG2.getSecond()));
						} else 
						if (Math.abs(connectedBlock.getSecond()) == Math.abs(chromBorders.getFirst())){
							connectedBlocksInB.set(i1, new Pair<Integer, Integer>(connectedBlock.getFirst(), -telomeresG2Two.getFirst()));
						} else
						if (Math.abs(connectedBlock.getFirst()) == Math.abs(chromBorders2.getSecond())){
							connectedBlocksInB.set(i1, new Pair<Integer, Integer>(-telomeresG2.getSecond(), connectedBlock.getSecond()));
						}
					} else {
						connectedBlocksInB.add(new Pair<Integer, Integer>(telomeresG2Two.getFirst(), telomeresG2.getSecond()));
					}
					
					whichOne1 = false;
					whichOne2 = false;
					whichOne3 = false;
					illegal = false;
				}
				

				 ////////// Generate correct concatenation of genome B ////////////////////////////////////
				int fstCap = blockCapsA.get(0).getFirst();
				ArrayList<Pair<Integer, Integer>> blockCapsBBackup = new ArrayList<Pair<Integer,Integer>>();
				Pair<Integer, Integer> relatedCaps = this.findCap(blockCapsB, Math.abs(fstCap));
				boolean fstB = relatedCaps.getFirst() == fstCap;
				if (!fstB){
					relatedCaps = this.invertPair(relatedCaps);
				}
				blockCapsBBackup.add(relatedCaps);
				fstCap = relatedCaps.getSecond();
				
				for (int i=0; i<blockCapsA.size()-1; ++i){
					relatedCaps = this.findCap(blockCapsA, Math.abs(fstCap));
					fstB = relatedCaps.getSecond() == fstCap;
					if (fstB){
						relatedCaps = blockCapsA.get(this.currIndex+1);
					} else {
						relatedCaps = this.invertPair(blockCapsA.get(this.currIndex-1));
					}
					fstCap = relatedCaps.getFirst();
					relatedCaps = this.findCap(blockCapsB, Math.abs(fstCap));
					fstB = relatedCaps.getFirst() == fstCap;
					if (!fstB){
						relatedCaps = this.invertPair(relatedCaps);
					}
					blockCapsBBackup.add(relatedCaps);
					fstCap = relatedCaps.getSecond();
				}

				int[] currGenes; // generate the concatenated genome B! Last step of the algo!
				genes = new int[this.nbGenes+blockCapsBBackup.size()*2];
				int pos = 0;
				for (int i=0; i<blockCapsBBackup.size(); ++i){
					relatedCaps = blockCapsBBackup.get(i);
					currGenes = this.getRelatedChrom(cappedGenomeB, relatedCaps.getFirst());
					if (currGenes != null){
						System.arraycopy(currGenes, 0, genes, pos, currGenes.length);
						pos += currGenes.length;
					} else {
						genes[pos++] = relatedCaps.getFirst();
						genes[pos++] = relatedCaps.getSecond();
					}
				}
				
				chromB = new Chromosome(genes, false);
				chromsB.clear();
				chromsB.add(chromB);
				newGenome = new Genome(chromsB);	
				
				/////////////////////////////////////////////////////////////////////////////////////////////
				///////////////////////// Proper flip left //////////////////////////////////////////////////
				/////////////////////////////////////////////////////////////////////////////////////////////			
			 	Pair<Genome, Genome> currGenomes2 = this.renameGenomes(concatGenomeA, newGenome);
				newAdjGraph = new AdjacencyGraph(currGenomes2.getFirst(), currGenomes2.getSecond());
				dataNew = new Data(currGenomes2.getFirst(), currGenomes2.getSecond());
				dataNew.setAdjGraph(newAdjGraph);
				additionalHPDataNew = new AdditionalDataHPDistance(currGenomes2.getFirst());
				plusCappedGnom = additionalHPData.getGenomeCappedPlusArray().clone();
				hpPreprocessNew = new HPBasedDistPreprocessing(dataNew, additionalHPDataNew);
				components = hpPreprocessNew.getComponents();
				Pair<Integer, Integer> anotherPair2 = null;
				
				for (int k=0; k<components.size(); ++k){
					cutPos = Utilities.getSmallerIndexFst(components.get(k).getStartIndex()-1, components.get(k).getEndIndex()-1);
					if (cutPos.getSecond()-cutPos.getFirst() > 1 && !components.get(k).isOriented() && cutPos.getSecond()-cutPos.getFirst() != genes.length-1){
						for (int j=0; j<this.telomerPosA.size(); ++j){
							anotherPair = this.telomerPosA.get(j);
							if (nbChromsBiggest > 1 && anotherPair.getFirst() <= cutPos.getFirst() && anotherPair.getSecond() >= cutPos.getFirst()
							&& anotherPair.getSecond() < cutPos.getSecond()){ // ||
								//chromBorders.getFirst() > cutPos.getFirst() && chromBorders.getSecond() > cutPos.getSecond()){ <- excluded	
								genes = concatGenomeA.getChromosome(0).getGenes().clone();
								int[] genes2 = newGenome.getChromosome(0).getGenes().clone();
								genePos = Utilities.getGenePos(newGenome).get(0);
								if (genes[anotherPair.getFirst()] == genes2[genePos[Math.abs(genes[anotherPair.getFirst()])]] && 
									genes[anotherPair.getSecond()] == genes2[genePos[Math.abs(genes[anotherPair.getSecond()])]] ||
									-genes[anotherPair.getSecond()] == genePos[Math.abs(genes[anotherPair.getFirst()])] && 
									-genes[anotherPair.getFirst()] == genePos[Math.abs(genes[anotherPair.getSecond()])]){
									
									if (genePos[Math.abs(genes[anotherPair.getSecond()])] < genePos[Math.abs(genes[anotherPair.getFirst()])]){
									//if (genes[anotherPair.getFirst()-1] != genes2[genePos[Math.abs(genes[anotherPair.getFirst()])]-1]){
										//it means the caps in "another pair" are external of the part which should be inverted
										//this means we have to invert the neighbors of "another pair", which are the other caps
										//involved in this flipping operation!
										anotherPair2 = new Pair<Integer, Integer>(anotherPair.getFirst()-1, anotherPair.getSecond()+1);										
									}
									concatGenomeA = this.flipAChrom2(concatGenomeA, anotherPair);
									if (anotherPair2 != null){
										newGenome = this.flipAChrom2(newGenome, new Pair<Integer, Integer>(
												genePos[Math.abs(genes[anotherPair2.getFirst()])], genePos[Math.abs(genes[anotherPair2.getSecond()])]));
									} else {
										newGenome = this.flipAChrom2(newGenome, new Pair<Integer, Integer>(
												genePos[Math.abs(genes[anotherPair.getFirst()])], genePos[Math.abs(genes[anotherPair.getSecond()])]));
									}
						
								} else { //if another interchromosomal comp is still there
									if (anotherPair.getFirst() == 0){
										if (this.telomerPosA.size() > 2){
											for (int l=0; l<this.telomerPosA.size(); ++l){
												if (this.telomerPosA.get(l).getFirst() > cutPos.getSecond()){
													anotherPair = this.telomerPosA.get(l-1);
													break;
												}
											}	
											genePos = Utilities.getGenePos(newGenome).get(0);
											concatGenomeA = this.flipAChrom2(concatGenomeA, anotherPair);
											fst = genePos[Math.abs(genes[anotherPair.getFirst()])];
											scnd = genePos[Math.abs(genes[anotherPair.getSecond()])];
											cutPos = Utilities.getSmallerIndexFst(fst, scnd);
											newGenome = this.flipAChrom2(newGenome, new Pair<Integer, Integer>(cutPos.getFirst(), cutPos.getSecond()));
										
										} else { //if only two chromosomes we have to exchange first and second one
											//the form the only remaining possibility to join the chromosomes with
											//the given caps. Otherwise the capping procedure would have been incorrect!
											currGenomes = this.exchangeChromosomes(concatGenomeA, newGenome);	
											concatGenomeA = currGenomes.getFirst();
											newGenome = currGenomes.getSecond();
										}
									} else {
										//as far as I know this case does not need a flip, it leaves the distance unchanged
									}
								}
								break;
							}
						}
					} else 
					if (!components.get(k).isOriented() && cutPos.getSecond()-cutPos.getFirst() == genes.length-1 && this.wholeGenOrComp){ //in case a whole genome grey comp exists which
						//was oriented by 2 even paths before
						genes = concatGenomeA.getChromosome(0).getGenes().clone();
						int[] genes2 = newGenome.getChromosome(0).getGenes().clone();
						for (int j=0; j<this.telomerPosA.size(); ++j){
							anotherPair = this.telomerPosA.get(j);
							genePos = Utilities.getGenePos(newGenome).get(0);
							if (genes[anotherPair.getFirst()] == genes2[genePos[Math.abs(genes[anotherPair.getFirst()])]] && 
									genes[anotherPair.getSecond()] == genes2[genePos[Math.abs(genes[anotherPair.getSecond()])]] ||
									-genes[anotherPair.getSecond()] == genePos[Math.abs(genes[anotherPair.getFirst()])] && 
									-genes[anotherPair.getFirst()] == genePos[Math.abs(genes[anotherPair.getSecond()])]){
								concatGenomeA = this.flipAChrom2(concatGenomeA, anotherPair);
								anotherPair = Utilities.getSmallerIndexFst(genePos[Math.abs(genes[anotherPair.getFirst()])], genePos[Math.abs(genes[anotherPair.getSecond()])]);
								newGenome = this.flipAChrom2(newGenome, anotherPair);
								break;
							}
						}
						
					}
				}
				/////////////////////////////////////////////////////////////////////////////////////////////
				
				genes = concatGenomeA.getChromosome(0).getGenes();
				int[] genes2 = newGenome.getChromosome(0).getGenes();
				boolean improper = genes[0] != genes2[0] && genes[0] != -genes2[genes2.length-1];
				boolean improper2 = genes[genes.length-1] != -genes2[0] && genes[genes.length-1] != genes2[genes2.length-1];
				int neighbor;
				if (improper){ //if there is one improper bound still or the special 1 chromosome case
					if (genes[0] != genes2[0]) {
						genePos = Utilities.getGenePos(newGenome).get(0);
						neighbor = genes2[genePos[Math.abs(genes[0])]+1];
						if (neighbor > this.nbGenes){ //invert part
							newGenome = this.flipAChrom2(newGenome, new Pair<Integer, Integer>(0, genePos[Math.abs(genes[0])]));
						} else {
							genePos = Utilities.getGenePos(concatGenomeA).get(0);
							neighbor = genes[genePos[Math.abs(genes2[0])]+1];
							if (neighbor > this.nbGenes){ //invert part
								concatGenomeA = this.flipAChrom2(concatGenomeA, new Pair<Integer, Integer>(0, genePos[Math.abs(genes2[0])]));
							} else {
								System.out.println("Sorting HP: Last improper bound cannot be assigned correctly 1.");
								//Should not occur since the capping & concat algos should omit this case!
							}
						}
					} else {
						//Since both genomes always start with the same telomere and we work on genomes (not adjacencies)
						//it is not possible that the genome is switched here!
					}
				}
				if (improper2){
					if (genes[genes.length-1] != genes2[genes2.length-1]) {
						genePos = Utilities.getGenePos(newGenome).get(0);
						neighbor = genes2[genePos[Math.abs(genes[genes.length-1])]-1];
						if (neighbor > this.nbGenes){ //invert part
							newGenome = this.flipAChrom2(newGenome, new Pair<Integer, Integer>(genePos[Math.abs(genes[genes.length-1])], genes2.length-1));
						} else {
							genePos = Utilities.getGenePos(concatGenomeA).get(0);
							neighbor = genes2[genePos[Math.abs(genes[genes.length-1])]-1];
							if (neighbor > this.nbGenes){ //invert part
								concatGenomeA = this.flipAChrom2(concatGenomeA, new Pair<Integer, Integer>(genePos[Math.abs(genes2[genes2.length-1])], genes.length-1));
							} else {
								System.out.println("Sorting HP: Last improper bound cannot be assigned correctly 2.");
								//Should not occur since the capping & concat algos should omit this case!
							}
						}
					} else {
						//Since both genomes always start with the same telomere and we work on genomes (not adjacencies)
						//it is not possible that the genome is switched here!
					}
				}
				
				/////////////////////////////////////////////////////////////////////////////////////////////
				////////////////////////////// End of concatenation Algorithm by Tesler /////////////////////
				/////////////////////////////////////////////////////////////////////////////////////////////
			}
		}
		
		Pair<Genome, Genome> currGenomes = this.renameGenomes(concatGenomeA, newGenome);
		newAdjGraph = new AdjacencyGraph(currGenomes.getFirst(), currGenomes.getSecond());
		dataNew = new Data(concatGenomeA, newGenome);
		dataNew.setAdjGraph(newAdjGraph);
		additionalHPDataNew = new AdditionalDataHPDistance(concatGenomeA);
		hpPreprocessNew = new HPBasedDistPreprocessing(dataNew, additionalHPDataNew);
		
		//from here on we have an optimal flipped and concatenated pair of genomes!
		
		HashMap<Integer, Integer> initialToCompMap = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> compToInitialMap = new HashMap<Integer, Integer>();
		int[] renamedChromB = new int[newGenome.getNumberOfGenes()];
		int[] renamedChromA = new int[newGenome.getNumberOfGenes()];
		int[] genomeA = concatGenomeA.getChromosome(0).getGenes();
		int[] genomeB = newGenome.getChromosome(0).getGenes(); //remember genome B is the sorted one!
		genePos = Utilities.getGenePos(concatGenomeA).get(0);
		for (int i=0; i<newGenome.getNumberOfGenes(); ++i){
			initialToCompMap.put(Math.abs(genomeB[i]), (i+1));
			compToInitialMap.put(i+1, genomeB[i]);
			renamedChromB[i] = i+1;
		}
		for (int i=0; i<renamedChromA.length; ++i){
			renamedChromA[i] = initialToCompMap.get(Math.abs(genomeA[i]));
			if (compToInitialMap.get(renamedChromA[i]) < 0 && genomeA[i] > 0 ||
				compToInitialMap.get(renamedChromA[i]) > 0 && genomeA[i] < 0){
				renamedChromA[i] *= -1;
			}
		}
		
		chromsA = new ArrayList<Chromosome>();
		chromsB = new ArrayList<Chromosome>();
		chromsA.add(new Chromosome(renamedChromA, false));
		chromsB.add(new Chromosome(renamedChromB, false));
		Genome renamedGenomeA = new Genome(chromsA);
		Genome renamedGenomeB = new Genome(chromsB);
		newAdjGraph = new AdjacencyGraph(renamedGenomeA, renamedGenomeB);
		dataNew = new Data(renamedGenomeA, renamedGenomeB);
		dataNew.setAdjGraph(newAdjGraph);
		additionalHPDataNew = new AdditionalDataHPDistance(renamedGenomeA);
		
		////////////// Inversion Sorting itself ////////////////////////////////////////////////////
		SortingInv sortingInv = new SortingInv();
		operationList = sortingInv.findOptSortSequence(dataNew, additionalHPDataNew, null);
		OperationList operationlistFinal = new OperationList();
		////////////////////////////////////////////////////////////////////////////////////////////
		
		//map the genes back from inversion sorting to hp sorting
		final int nbExtremities = this.nbGenes*2;
		int[] adjGraphA = newAdjGraph.getAdjacenciesGenome1().clone();
		int[] adjGraphAFinal = data.getAdjGraph().getAdjacenciesGenome1().clone();
		ArrayList<Pair<Integer, Integer>> operations = operationList.getOperationList();
		ArrayList<Pair<Integer, Integer>> operationsRemapped = new ArrayList<Pair<Integer,Integer>>();
		Pair<Integer, Integer> currPair;
		Pair<Integer, Integer> currPair2;
		int fst;
		int scnd;
		int thrd;
		int frth;
		int fst2;
		int scnd2;
		int thrd2;
		int frth2;
		for (int i=0; i<operations.size(); ++i){
			currPair = operations.get(i);
			currPair2 = operations.get(i+1);
			fst2 = compToInitialMap.get((currPair.getFirst()+1)/2);
			scnd2 = compToInitialMap.get((currPair.getSecond()+1)/2);
			thrd2 = compToInitialMap.get((currPair2.getFirst()+1)/2);
			frth2 = compToInitialMap.get((currPair2.getSecond()+1)/2);
			fst = this.getExtremity(currPair.getFirst(), fst2*2, initialToCompMap.get(Math.abs(fst2)));
			scnd = this.getExtremity(currPair.getSecond(), scnd2*2, initialToCompMap.get(Math.abs(scnd2)));
			thrd = this.getExtremity(currPair2.getFirst(), thrd2*2, initialToCompMap.get(Math.abs(thrd2)));
			frth = this.getExtremity(currPair2.getSecond(), frth2*2, initialToCompMap.get(Math.abs(frth2)));
			
			//check for telomeres in the operations and correct it
			if (fst > nbExtremities && scnd > nbExtremities){
				currPair = new Pair<Integer, Integer>(thrd, frth);
				currPair2 = new Pair<Integer, Integer>(0, 0);
			} else
			if (thrd > nbExtremities && frth > nbExtremities){
				currPair = new Pair<Integer, Integer>(fst, scnd);
				currPair2 = new Pair<Integer, Integer>(0, 0);
			} else 
			if (fst > nbExtremities && thrd > nbExtremities){
				currPair = new Pair<Integer, Integer>(scnd, scnd);
				currPair2 = new Pair<Integer, Integer>(frth, frth);
			} else 
			if (scnd > nbExtremities && frth > nbExtremities){
				currPair = new Pair<Integer, Integer>(fst, fst);
				currPair2 = new Pair<Integer, Integer>(thrd, thrd);	
			} else
			if (scnd > nbExtremities && thrd > nbExtremities){
				currPair = new Pair<Integer, Integer>(0, 0);
				currPair2 = new Pair<Integer, Integer>(0, 0);
			} else
			if (fst > nbExtremities && frth > nbExtremities){
				currPair = new Pair<Integer, Integer>(0, 0);
				currPair2 = new Pair<Integer, Integer>(0, 0);
			} else
			if (fst > nbExtremities){
				currPair = new Pair<Integer, Integer>(thrd, frth);
				currPair2 = new Pair<Integer, Integer>(scnd, scnd);
			} else
			if (frth > nbExtremities){
				currPair = new Pair<Integer, Integer>(fst, scnd);
				currPair2 = new Pair<Integer, Integer>(thrd, thrd);
			} else
			if (scnd > nbExtremities){		
				currPair = new Pair<Integer, Integer>(thrd, frth);
				currPair2 = new Pair<Integer, Integer>(fst, fst);
			} else 
			if (thrd > nbExtremities){				
				currPair = new Pair<Integer, Integer>(fst, scnd);
				currPair2 = new Pair<Integer, Integer>(frth, frth);
			} else {
				currPair = new Pair<Integer, Integer>(fst, scnd);
				currPair2 = new Pair<Integer, Integer>(thrd, frth);
			}
			if (currPair.getFirst() != 0){
				operationsRemapped.add(currPair);
				operationsRemapped.add(currPair2);
				adjGraphAFinal[currPair.getFirst()] = currPair.getSecond();
				adjGraphAFinal[currPair.getSecond()] = currPair.getFirst();
				
				if (currPair2.getFirst() != 0){
					adjGraphAFinal[currPair2.getFirst()] = currPair2.getSecond();
					adjGraphAFinal[currPair2.getSecond()] = currPair2.getFirst();
				}
				operationlistFinal.addAdjacencyArrayG1(adjGraphAFinal.clone());
				
				//also update adjacency graph belonging to inversion sorting
				currPair = operations.get(i);
				currPair2 = operations.get(i+1);
				adjGraphA[currPair.getFirst()] = currPair.getSecond();
				adjGraphA[currPair.getSecond()] = currPair.getFirst();
				adjGraphA[currPair2.getFirst()] = currPair2.getSecond();
				adjGraphA[currPair2.getSecond()] = currPair2.getFirst();
			}

			++i;
		}
		operationlistFinal.setOperationList(operationsRemapped);
		
		return operationlistFinal;
	}
	
	
	/**
	 * Exchanges the two chromosomes whose index is handed over to this method.
	 * @param fstGenome first genome
	 * @param scndGenome second genome
	 * @param fstIndex index of fst chromosome
	 * @param scndIndex index of scnd chromosome
	 * @return pair of the new fst and scnd genome
	 */
	private Pair<Genome, Genome> exchangeChromosomes(Genome fstGenome, Genome scndGenome) {
		int[] genePos = Utilities.getGenePos(scndGenome).get(0);
		int[] fstGenes = fstGenome.getChromosome(0).getGenes();
		int[] scndGenes = scndGenome.getChromosome(0).getGenes();
		int[] newGenome = new int[fstGenes.length];
		int[] newGenome2 = new int[fstGenes.length];
		
		Pair<Integer, Integer> fstBorders = this.telomerPosA.get(0);
		Pair<Integer, Integer> scndBorders = this.telomerPosA.get(1);
		System.arraycopy(fstGenes, scndBorders.getFirst(), newGenome, 0, scndBorders.getSecond()-scndBorders.getFirst()+1);
		System.arraycopy(fstGenes, fstBorders.getFirst(), newGenome, scndBorders.getSecond()-scndBorders.getFirst()+1, scndBorders.getFirst());
		int neededVal = genePos[Math.abs(fstGenes[fstBorders.getSecond()])];
		fstBorders = new Pair<Integer, Integer>(fstBorders.getFirst(), genePos[Math.abs(fstGenes[scndBorders.getFirst()])]);
		scndBorders = new Pair<Integer, Integer>(neededVal, scndBorders.getSecond());
		System.arraycopy(scndGenes, scndBorders.getFirst(), newGenome2, 0, scndBorders.getSecond()-scndBorders.getFirst()+1);
		System.arraycopy(scndGenes, fstBorders.getFirst(), newGenome2, scndBorders.getSecond()-scndBorders.getFirst()+1, scndBorders.getFirst());
		ArrayList<Chromosome> newChromslist = new ArrayList<Chromosome>();
		ArrayList<Chromosome> newChromslist2 = new ArrayList<Chromosome>();
		newChromslist.add(new Chromosome(newGenome, false));
		newChromslist2.add(new Chromosome(newGenome2, false));
		Genome newGenomeG = new Genome(newChromslist);
		Genome newGenomeG2 = new Genome(newChromslist2);
		
		return new Pair<Genome, Genome>(newGenomeG, newGenomeG2);
	}

	
	/**
	 * Renames a pair of genomes. The second genome is the ordered set of gene numbers
	 * while the first genome receives the corresponding gene numbers. Backmapping
	 * is not returned, but data structures created in the method right now.
	 */
	private Pair<Genome, Genome> renameGenomes(final Genome concatGenomeA, final Genome newGenome){
		HashMap<Integer, Integer> initialToCompMap = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> compToInitialMap = new HashMap<Integer, Integer>();
		int[] renamedChromB = new int[newGenome.getNumberOfGenes()];
		int[] renamedChromA = new int[newGenome.getNumberOfGenes()]; //both length are the same!
		int[] genomeA = concatGenomeA.getChromosome(0).getGenes();
		int[] genomeB = newGenome.getChromosome(0).getGenes(); //remember genome B is the sorted one!
		for (int i=0; i<newGenome.getNumberOfGenes(); ++i){
			initialToCompMap.put(Math.abs(genomeB[i]), (i+1));
			compToInitialMap.put(i+1, genomeB[i]);
			renamedChromB[i] = i+1;
		}
		for (int i=0; i<renamedChromA.length; ++i){
			renamedChromA[i] = initialToCompMap.get(Math.abs(genomeA[i]));
			if (compToInitialMap.get(renamedChromA[i]) < 0 && genomeA[i] > 0 ||
				compToInitialMap.get(renamedChromA[i]) > 0 && genomeA[i] < 0){
				renamedChromA[i] *= -1;
			}
		}
		
		ArrayList<Chromosome> chromsA = new ArrayList<Chromosome>();
		ArrayList<Chromosome> chromsB = new ArrayList<Chromosome>();
		chromsA.add(new Chromosome(renamedChromA, false));
		chromsB.add(new Chromosome(renamedChromB, false));
		Genome renamedGenomeA = new Genome(chromsA);
		Genome renamedGenomeB = new Genome(chromsB);
		return new Pair<Genome, Genome>(renamedGenomeA, renamedGenomeB);
	}

	/**
	 * Returns the chromosome containing the given cap.
	 * @param cappedGenome the capped chromosome list
	 * @param cap the cap to scan for
	 * @return the chromosome in correct orientation
	 */
	private int[] getRelatedChrom(final ArrayList<int[]> cappedGenome, final int cap) {
		int[] chrom;
		for (int i=0; i<cappedGenome.size(); ++i){
			chrom = cappedGenome.get(i);
			if (Math.abs(chrom[0]) == Math.abs(cap)){
				return chrom;
			} else
			if (Math.abs(chrom[chrom.length-1]) == Math.abs(cap)){
				return this.invertChrom(chrom);
			}
		}
		return null;
	}

	
	/**
	 * Returns the pair of caps which contains the cap.
	 * @param blockCaps array of cap pairs
	 * @param cap the cap to search for, USE POSITIVE VALUES ONLY
	 * @return the pair containing the cap
	 */
	private Pair<Integer, Integer> findCap(final ArrayList<Pair<Integer, Integer>> blockCaps, final int cap) {
		Pair<Integer, Integer> relatedCaps = new Pair<Integer, Integer>(-1, -1);
		for (int i=0; i<blockCaps.size(); ++i){
			relatedCaps = blockCaps.get(i);
			if (Math.abs(relatedCaps.getFirst()) == cap || Math.abs(relatedCaps.getSecond()) == cap){
				this.currIndex = i;
				break;
			}
		}
		return relatedCaps;
	}

	
	/**
	 * Updates the telomer list of genome A after switching to neighboring chromosomes.
	 * @param i the current index in the chromosome array
	 */
	private void updateTelomeres(final int i) {

		Pair<Integer, Integer> telomeres = this.telomerPosA.get(i-1);
		Pair<Integer, Integer> telomeres2 = this.telomerPosA.get(i-2);
		int length1 = telomeres.getSecond()-telomeres.getFirst();
		int length2 = telomeres2.getSecond()-telomeres2.getFirst();
		this.telomerPosA.set(i-2, new Pair<Integer, Integer>(telomeres2.getFirst(), telomeres2.getFirst()+length1));
		this.telomerPosA.set(i-1, new Pair<Integer, Integer>(telomeres2.getFirst()+length1+1, telomeres2.getFirst()+length1+length2+1));
		
	}

	/**
	 * Inverts a pair and exchanges order of the pair elements.
	 * @param pair the pair to be inverted and reordered
	 * @return the new pair
	 */
	private Pair<Integer, Integer> invertPair(final Pair<Integer, Integer> pair) {
		int val1 = -pair.getFirst();
		int val2 = -pair.getSecond();
		return new Pair<Integer, Integer>(val2, val1);
	}

	
	/**
	 * Flips a chromosome by replacing the order and orientation of all genes between the
	 * chromBorders handed over to the method.
	 * @param concatGenome a concatenated genome with only one chromosome
	 * @param chromBorders the chromosome borders of the chromosome to invert = index positions, not genes!
	 * @return the new genome with the inverted chromosome within the one chromosome
	 */
	private Genome flipAChrom2(final Genome concatGenome, final Pair<Integer, Integer> chromBorders) {
		int[] genes = concatGenome.getChromosome(0).getGenes();
		Pair<Integer, Integer> chromBordersNew = Utilities.getSmallerIndexFst(chromBorders.getFirst(), chromBorders.getSecond());
		int[] intermedGenes = new int[chromBordersNew.getSecond()-chromBordersNew.getFirst()+1];
		System.arraycopy(genes, chromBordersNew.getFirst(), intermedGenes, 0, chromBordersNew.getSecond()-chromBordersNew.getFirst()+1);
		int pos = intermedGenes.length-1;
		for (int i=chromBordersNew.getFirst(); i<=chromBordersNew.getSecond(); ++i){
			genes[i] = -intermedGenes[pos--];
		}
		concatGenome.replaceChromosome(new Chromosome(genes, false), 0);
		return concatGenome;
	}

	
	/**
	 * Generates the capped genome B after the following rule:
	 * Chromosome 1 receives cap pair 1 and is concatenated with chromosome 2 etc.
	 * @param blockCapsB list of pairs of caps, each pair for one chromosome
	 * @param genomeB initial genome B
	 * @return the concatenated genome B
	 */
	private int[] generateNewGenomeB(final ArrayList<Pair<Integer, Integer>> blockCapsB,
			final ArrayList<Chromosome> genomeB) {
		int[] newGenomeB = new int[this.nbGenes+blockCapsB.size()*2];
		int pos = 0;
		int[] chrom;
		int start;
		int end;
		int index = 0;
		for (int i=0; i<genomeB.size(); ++i){
			start = pos;
			newGenomeB[pos++] = blockCapsB.get(i).getFirst();
			chrom = genomeB.get(i).getGenes();
			System.arraycopy(chrom, 0, newGenomeB, pos, chrom.length);
			pos += chrom.length;
			end = pos;
			newGenomeB[pos++] = blockCapsB.get(i).getSecond();
			this.telomerPosB.add(new Pair<Integer, Integer>(start, end)); //saves the positions of the chromosome borders
			++index;
		}
		//if genome B has less chromosomes than A
		for (int i=index; i<blockCapsB.size(); ++i){
			newGenomeB[pos++] = blockCapsB.get(i).getFirst();
			newGenomeB[pos++] = blockCapsB.get(i).getSecond();
		}
		return newGenomeB;
	}

	
	/**
	 * Generates the block caps for genome B.
	 * @param genome genome B
	 * @param correctCaps map of correct cappings
	 * @return the list of block caps of genome B
	 */
	private ArrayList<Pair<Integer, Integer>> generateBlockCapsB(final ArrayList<Chromosome> genome, 
			final HashMap<Integer, Integer> correctCaps) {
		
		ArrayList<Pair<Integer, Integer>> blockCapsB = new ArrayList<Pair<Integer, Integer>>();
		int[] genes;
		int fst;
		int scnd;		
		int lastCap = this.nbGenes+1;
		int lastCap2;
		for (int i=0; i<genome.size(); ++i){
			genes = genome.get(i).getGenes();
			fst = correctCaps.get(Utilities.getExtremity(genes[0]*2, false));
			scnd = correctCaps.get(Utilities.getExtremity(genes[genes.length-1]*2, true));
			if (fst % 2 == 1){
				fst = -((fst+1)/2);
			} else {
				fst /= 2; 
			}
			if (scnd % 2 == 0){
				scnd = -(scnd/2);
			} else {
				scnd = (scnd+1)/2;
			}
			blockCapsB.add(new Pair<Integer, Integer>(fst, scnd));
		}
		HashMap<Integer, Boolean> visited = new HashMap<Integer, Boolean>();
		for (int i=this.nbGenes+1; i<this.nbGenes+correctCaps.size(); ++i){
			visited.put(i, false);
		}
		
		//if genome B has less chroms then A find the last block caps
		Iterator<Integer> keys = correctCaps.keySet().iterator();
		while (keys.hasNext()){
			lastCap = keys.next();
			lastCap2 = correctCaps.get(lastCap);
			if (lastCap > this.nbGenes*2 && lastCap2 > this.nbGenes*2 && !visited.get((lastCap+1)/2)){
				if (lastCap % 2 == 0 && lastCap2 % 2 == 1){
					lastCap = -(lastCap/2);
					lastCap2 = -((lastCap2+1)/2);
				} else 
				if (lastCap % 2 == 0 && lastCap2 % 2 == 0){
					lastCap = -(lastCap)/2;
					lastCap2 = (lastCap2)/2;
				} else
				if (lastCap % 2 == 1 && lastCap2 % 2 == 1){
					lastCap = (lastCap+1)/2;
					lastCap2 = -((lastCap2+1)/2);
				} else {
					lastCap = (lastCap+1)/2;
					lastCap2 /= 2;
				}
				blockCapsB.add(new Pair<Integer, Integer>(-lastCap, -lastCap2));
				visited.put(Math.abs(lastCap2), true);
				visited.put(Math.abs(lastCap), true);
			}
		}
		return blockCapsB;
	}

	/**
	 * Generates the list of block caps for genome A.
	 * @param genome genome A
	 * @param cappedG1 capped genome A
	 * @param genePos genepos array
	 * @param nbChromsBiggest 
	 * @return the list of block caps of genome A
	 */
	private ArrayList<Pair<Integer, Integer>> generateBlockCapsA(final ArrayList<Chromosome> genome, 
			final int[] cappedG1, final int[] genePos, final int nbChromsBiggest) {

		ArrayList<Pair<Integer, Integer>> blockCapsA = new ArrayList<Pair<Integer, Integer>>();
		int[] genes;
		int fst;
		int scnd;		
		int lastCap = this.nbGenes+1;
		int start;
		int end;
		int pos = 0;
		for (int i=0; i<genome.size(); ++i){
			genes = genome.get(i).getGenes();
			start = pos;
			pos += genes.length+1;
			end = pos++;
			this.telomerPosA.add(new Pair<Integer, Integer>(start, end));
			fst = cappedG1[genePos[Math.abs(genes[0])]-1];
			scnd = cappedG1[genePos[Math.abs(genes[genes.length-1])]+1];
			blockCapsA.add(new Pair<Integer, Integer>(fst, scnd));
			lastCap = scnd;
		}
		for (int i=0; i<nbChromsBiggest-genome.size(); ++i){ //if genome A has less chroms then B find the last block caps 
			blockCapsA.add(new Pair<Integer, Integer>(++lastCap, ++lastCap));
			this.telomerPosA.add(new Pair<Integer, Integer>(pos++, pos++));
		}
		return blockCapsA;
	}

	/**
	 * Returns the extremity used in the original genome in comparison to the
	 * genome handed over to the inversion sorting process.
	 * @param currExtr current extremity under investigation
	 * @param fst original gene name of the extremity already * 2
	 * @param geneInComparison mapping also showing if *-1 is necessary
	 * @return the correct extremity used originally
	 */
	private int getExtremity(final int currExtr, final int fst, final int geneInComparison) {
		int result = 0;
		if (currExtr % 2 == 1){
			result = Math.abs(fst)-1;
		} else {
			result = Math.abs(fst);
		}
		if (fst < 0 || geneInComparison < 0){
			if (result % 2 == 0){
				result = (result*-1)-1;
				if (result < 0){
					result += 2;
				} 
			} else {
				result = (result*-1)+1;
				if (result <= 0){
					result -= 2;
				}
			}
		}
		return Math.abs(result);
	}

	/**
	 * Inverts the given chromosome.
	 * @param chrom the chromosome to invert
	 * @return the inverted chromosome
	 */
	private int[] invertChrom(final int[] chrom) {
		int[] newChrom = new int[chrom.length];
		for (int i=chrom.length-1; i>=0; --i){
			newChrom[chrom.length-1-i] = -1*chrom[i];
		}
		return newChrom;
	}

	/**
	 * Returns the two pairs of extremities which are connected after this step. [0] and [1]
	 * belong together and [2] and [3]. Method optimized for connecting two semi real components.
	 * @param genePos array holding the gene positions for each gene
	 * @return the extremities which form an oriented edge with each other from an l tail to a pi cap
	 * Second pair of extremities closes the path
	 */
	private int[] getOrientedEtr2(final int[] genePos) {
		int[] involvedExtr = new int[Constants.NB_EXTREMITIES];
		boolean noPath = true;
		int fst;
		int scnd;
		int thrd;
		int frth;
		int key1 = 0;
		int key2 = 1;
		int i1 = 0;
		int i2 = 1;
		int i3 = 0;
		int i4 = 0;
		ArrayList<Pair<Integer, Integer>> fstPaths = this.lPiOwnerIndInSRComps.get(key1);
		ArrayList<Pair<Integer, Integer>> scndPaths = this.lPiOwnerIndInSRComps.get(key2);
		Pair<Integer, Integer> fstPath = new Pair<Integer, Integer>(0, 0);
		Pair<Integer, Integer> scndPath = new Pair<Integer, Integer>(0, 0);
		while (noPath){
			while (!this.lPiOwnerIndInSRComps.containsKey(key1)){
				++key1;
			}
			while (!this.lPiOwnerIndInSRComps.containsKey(key2) || key1 == key2){
				++key2;
			}
			fstPaths = this.lPiOwnerIndInSRComps.get(key1);
			scndPaths = this.lPiOwnerIndInSRComps.get(key2);
			while (noPath && i4 < scndPaths.size()){
				fstPath = fstPaths.get(i3);
				scndPath = scndPaths.get(i4);
				fstPath = Utilities.getSmallerIndexFst(fstPath.getFirst(), fstPath.getSecond());
				scndPath = Utilities.getSmallerIndexFst(scndPath.getFirst(), scndPath.getSecond());
				fst = genePos[(fstPath.getFirst()+1)/2];
				scnd = genePos[(fstPath.getSecond()+1)/2];
				thrd = genePos[(scndPath.getFirst()+1)/2];
				frth = genePos[(scndPath.getSecond()+1)/2]; //join AND also close path
				if (Math.abs(fst-frth) % 2 == 0 || Math.abs(scnd-thrd) % 2 == 0){
					involvedExtr[0] = fstPath.getFirst();
					involvedExtr[1] = scndPath.getSecond();
					involvedExtr[2] = fstPath.getSecond();
					involvedExtr[3] = scndPath.getFirst();
					noPath = false;
				}
				++i4;
				if (i4 == scndPaths.size()){
					if (i3 < fstPaths.size()-1){
						++i3;
						i4 = 0;
					}
				}	
			}
			++i2;
			++key2;
			i3 = 0;
			i4 = 0;
			if (noPath && i2 == this.lPiOwnerIndInSRComps.size()){
				if (i1 < this.lPiOwnerIndInSRComps.size()-2){
					++i1;
					++key1;
					i2 = i1+1;
					key2 = key1+1;
				} else { //choose an interchromosomal edge (they all have to be interchrom)
					fstPaths = this.lPiOwnerIndInSRComps.get(0);
					scndPaths = this.lPiOwnerIndInSRComps.get(1);
					fstPath = fstPaths.get(0);
					scndPath = scndPaths.get(0);
					fstPath = Utilities.getSmallerIndexFst(fstPath.getFirst(), fstPath.getSecond());
					scndPath = Utilities.getSmallerIndexFst(scndPath.getFirst(), scndPath.getSecond());
					involvedExtr[0] = fstPath.getFirst();
					involvedExtr[1] = scndPath.getSecond();
					involvedExtr[2] = fstPath.getSecond();
					involvedExtr[3] = scndPath.getFirst();
					noPath = false;
				}
			}
		}
		this.lPiPaths.remove(fstPath);
		this.lPiPaths.remove(scndPath);
		this.lPiOwnerIndInSRComps.remove(key1);
		this.lPiOwnerIndInSRComps.remove(--key2);
		return involvedExtr;
	}

	/**
	 * Returns the two pairs of extremities which are connected after this step. [0] and [1]
	 * belong together and [2] and [3].
	 * @param llpath an l-l path
	 * @param piPiPaths array of pi-pi paths
	 * @param genePos array holding the gene positions for each gene
	 * @return the extremity which forms an oriented edge with the given extremity at "fstPos" 
	 */
	private int[] getOrientedEtr(final Pair<Integer, Integer> llpath, 
			final ArrayList<Pair<Integer, Integer>> piPiPaths, final int[] genePos) {
		int[] involvedExtr = new int[Constants.NB_EXTREMITIES];
		boolean noPath = true;
		int fst;
		int scnd;
		int thrd;
		int frth;
		int index = 0;
		while (noPath){
			fst = genePos[(llpath.getFirst()+1)/2];
			scnd = genePos[(llpath.getSecond()+1)/2];
			thrd = genePos[(piPiPaths.get(index).getFirst()+1)/2];
			frth = genePos[(piPiPaths.get(index).getSecond()+1)/2];
			if (Math.abs(fst-thrd) % 2 == 0 || Math.abs(scnd-frth) % 2 == 0){
				involvedExtr[0] = llpath.getFirst();
				involvedExtr[1] = piPiPaths.get(index).getFirst();
				involvedExtr[2] = llpath.getSecond();
				involvedExtr[3] = piPiPaths.get(index).getSecond();
				this.piPiPaths.remove(index);
				noPath = false;
			} else 
			if (Math.abs(fst-frth) % 2 == 0 || Math.abs(scnd-thrd) % 2 == 0){
				involvedExtr[0] = llpath.getFirst();
				involvedExtr[1] = piPiPaths.get(index).getSecond();
				involvedExtr[2] = llpath.getSecond();
				involvedExtr[3] = piPiPaths.get(index).getFirst();
				this.piPiPaths.remove(index);
				noPath = false;
			}
			++index;
			if (noPath && index == piPiPaths.size()){
				involvedExtr[0] = llpath.getFirst();
				involvedExtr[1] = piPiPaths.get(index-1).getFirst();
				involvedExtr[2] = llpath.getSecond();
				involvedExtr[3] = piPiPaths.get(index-1).getSecond();
				this.piPiPaths.remove(index-1);
				noPath = false;
			}
		}
		return involvedExtr;
	}

	/**
	 * Identifies and saves all l-l, pi-pi and pi-l paths in class variables.
	 * @param capDistinction array knowing which extremities are caps
	 * @param cappingIndepndntGraph adj graph to find the path between two caps
	 * @param initialAdjG1 initial adjacency graph of genome A
	 * @param components list of ALL components
	 */
	private void identifyAllCapPaths(final int[] capDistinction, final int[] cappingIndepndntGraph, 
			final int[] initialAdjG1, final ArrayList<Component> components) {
		boolean[] visited = new boolean[capDistinction.length];
		int cap;
		Pair<Integer, Integer> cap2Data;
		Pair<Integer, Integer> result;
		for (int i=1; i<capDistinction.length; ++i){
			cap = capDistinction[i];
			if (cap > Constants.FALSE && visited[i] == false){
				cap2Data = this.checkPath4LTail(i, cappingIndepndntGraph, capDistinction, initialAdjG1); 
				result = new Pair<Integer, Integer>(i, cap2Data.getFirst());
				if (cap == Constants.L_CAP && cap2Data.getSecond() == Constants.L_CAP){
					this.llPaths.add(result);
				} else
				if (cap == Constants.PI_CAP && cap2Data.getSecond() == Constants.PI_CAP){
					this.piPiPaths.add(result);
				} else {
					this.lPiPaths.add(result);
				}
				visited[i] = true;
				visited[cap2Data.getFirst()] = true;
			}
		}
	}

	/**
	 * Classifies the unoriented components in the hurdles and knots needed for the capping process.
	 * @param components all components of the current genome
	 * @param capDistinction the array classifying which extremities are pi or l caps
	 * @param plusCappedGnom the plus capped genome
	 * @param genePos array of gene positions
	 * @param adjacenciesG1 adjacencies of genome A
	 */
	private void classifyUnorComponents(final ArrayList<Component> components, final int[] capDistinction, 
			final int[] plusCappedGnom, final int[] cappingIndepndntGraph, final int[] genePos) {
		Component comp; //Remember the component array only conatains INTRAchromosomal components
		/* Identify:
		 * - A knot is a component, that doesn't separate any components
		 * --- IU -> set of unoriented intrachromosomal comps (here without comps with pi-pi or l-l paths -> interchrom for comp ident
		 * --- RU -> set of unoriented intrachromosomal REAL comps -> REAL if it has no pi or l cap in its span
		 * - semi real knots -> if intrachrom, unor comp without pi-pi & l,l path (& it becomes minimal or greatest simple (not super) real knot)
		 * - fortress of real knots -> if odd number of real knots, wich are all super
		 *    - super knot: when it protects another non-knot -> when super real knot is inside other comp, which is not a real-knot
		 * - greatest semi-real knot -> if it spans all other real knots -> cannot contain pi-pi or l-l path
		 * - simple components, brauch ich die echt? geht das nicht auch nachher?
		 */

		for (int i=0; i<components.size(); ++i){
			comp = components.get(i);
			if (!comp.isOriented() && Math.abs(comp.getStartIndex()-comp.getEndIndex()) > 1){
				this.unorComps.add(comp);
				if (this.realComp(comp, capDistinction, plusCappedGnom)){
					this.unorRealComps.add(comp);
				}
			}
		}
		
//		this.knots = this.checkHurdles(this.unorComps);
		this.realKnots = this.checkHurdles(this.unorRealComps);
		this.setSemiRComps = this.checkSemiRealComps(components, genePos); //set is complete, because comps with l-l paths are already excluded in comp ident
		//Pair<Integer, int[]> superKnots = this.checkSuperHurdles(this.unorComps, this.knots);
		Pair<Integer, int[]> superRealKnots = this.checkSuperHurdles(this.unorRealComps, this.realKnots);
		//Pair<Integer, int[]> semiRealKnots = this.checkSemiRealKnots(capDistinction, plusCappedGnom, cappingIndepndntGraph);
		//this.knots.setFirst(superKnots.getSecond()); //has to be replaced: normal (real)knots have 1, super (real) knots
		this.realKnots.setFirst(superRealKnots.getSecond()); //have a 2 !
		this.fortressRealKnots = this.realKnots.getSecond() % 2 == 1 && superRealKnots.getFirst() == this.realKnots.getSecond();
	}

	/**
	 * Calculates the set of semiRealKnots from the set of knots and real knots. All components
	 * must be semi real, because components with l-l ir pi-pi paths are already excluded.
	 * @param comonents the components
	 * @param genePos array of gene positions
	 * @return the remaining set of components
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<Component> checkSemiRealComps(ArrayList<Component> components, final int[] genePos) {
		
		ArrayList<Component> setForSemiRComps = (ArrayList<Component>) this.unorComps.clone();
		int start;
		int end;
		int counter = 0;
		Component comp;
		Component comp2;
		Pair<Integer, Integer> compBorders;
		Pair<Integer, Integer> compBorders2;
		for (int i=0; i<this.unorComps.size(); ++i){
			comp = this.unorComps.get(i);
			compBorders = Utilities.getSmallerIndexFst(comp.getStartIndex(), comp.getEndIndex());
			start = compBorders.getFirst();
			end = compBorders.getSecond();
			for (int j=0; j<this.unorRealComps.size(); ++j){
				comp2 = this.unorRealComps.get(j);
				compBorders2 = Utilities.getSmallerIndexFst(comp2.getStartIndex(), comp2.getEndIndex());
				if (start == compBorders2.getFirst() && end == compBorders2.getSecond()){
					setForSemiRComps.remove(counter); //only remove, if this unorComp is an unorRealComp (and a real knot -> Hannenhalli, veraltet)!
					--counter;
					break;
				}
			}
			if (counter == setForSemiRComps.size()-1){
				break;
			}
			++counter;
		}
		//Check for the greatest semi real knot. It exists, if all real knots are included in one of the semi-real knots
		int[] realComps = this.realKnots.getFirst();
		for (int i=0; i<setForSemiRComps.size(); ++i){
			comp = setForSemiRComps.get(i);
			compBorders = Utilities.getSmallerIndexFst(comp.getStartIndex(), comp.getEndIndex());
			for (int j=0; j<realComps.length; ++j){
				if (realComps[j] > 0){
					comp2 = this.unorRealComps.get(j);
					compBorders2 = Utilities.getSmallerIndexFst(comp2.getStartIndex(), comp2.getEndIndex());
					if (compBorders.getFirst() > compBorders2.getFirst() && compBorders.getSecond() < compBorders2.getSecond()){
						break;
					}
				}
				if (j == realComps.length-1){
					this.greatestSRKnot = comp;
				}
			}
			if (this.greatestSRKnot != null){
				break;
			}
		}
		
		//Assign all paths belonging to a semi real component to the index of the component in the setForSemiRComps
		Pair<Integer, Integer> lPiPath;
		boolean addPath = true;
		for (int i=0; i<setForSemiRComps.size(); ++i){
			comp = setForSemiRComps.get(i);
			compBorders = Utilities.getSmallerIndexFst(comp.getStartIndex(), comp.getEndIndex());
			for (int k=0; k<this.lPiPaths.size(); ++k){ //find all l-pi paths included in current semi real comp
				lPiPath = this.lPiPaths.get(k);
				lPiPath = Utilities.getSmallerIndexFst(genePos[(lPiPath.getFirst()+1)/2], genePos[(lPiPath.getSecond()+1)/2]);
				if (lPiPath.getFirst() >= compBorders.getFirst() && lPiPath.getSecond() <= compBorders.getSecond()){
					for (int j=0; j<components.size(); ++j){ //check if there is a smaller comp who's the owner of current l-pi path
						comp2 = components.get(j);
						if (Math.abs(comp2.getStartIndex()-comp2.getEndIndex()) > 1){
							compBorders2 = Utilities.getSmallerIndexFst(comp2.getStartIndex(), comp2.getEndIndex());
							if (lPiPath.getFirst() >= compBorders2.getFirst() && lPiPath.getSecond() <= compBorders2.getSecond() && comp2 != comp){
								// if at least on index of the l-pi path is beyond the extent of current comp2 the path belongs to semi real comp
								addPath = false;
							}
						}
						if (addPath == false){
							break;
						}
					}
					if (addPath){
						lPiPath = this.lPiPaths.get(k);
						if (this.lPiOwnerIndInSRComps.containsKey(i)){
							this.lPiOwnerIndInSRComps.get(i).add(lPiPath);
						} else {
							this.lPiOwnerIndInSRComps.put(i, new ArrayList<Pair<Integer, Integer>>());
							this.lPiOwnerIndInSRComps.get(i).add(lPiPath);
						}
					} else {
						addPath = true;
					}
				}
			}
		}
		
		return setForSemiRComps;
	}

	/**
	 * Checks among all hurdles, which of them are super hurdles.
	 * @param comps the array of components to check
	 * @param hurdles the corresponding array determining which of the components are hurdles/knots
	 * @return a new array which contains a 0 at the index of a non-hurdle, a 1 for a minimal hurdle
	 * 			or a 2 for a super hurdle
	 */
	private Pair<Integer, int[]> checkSuperHurdles(final ArrayList<Component> comps, final Pair<int[], Integer> hurdlePair) {
		int[] hurdles = hurdlePair.getFirst();
		int[] superHurdles = hurdles.clone();
		Component comp;
		Pair<Integer, Integer> compBorders;
		int start;
		int scndStart;
		int scndEnd;
		int nbSuperHurds = 0;
		boolean protects = false;
		ArrayList<Pair<Integer, Integer>> maximalComps;
		ArrayList<Integer> areHurdles;
		
		for (int i=0; i<superHurdles.length; ++i){
			comp = comps.get(i); //compare them to ALL other comps in this set
			start = Utilities.getSmallerIndexFst(comp.getStartIndex(), comp.getEndIndex()).getFirst();
			if (hurdles[i] == Constants.TRUE){
				maximalComps = new ArrayList<Pair<Integer, Integer>>();
				areHurdles = new ArrayList<Integer>();
				boolean isContained = false;
				for (int j=0; j<comps.size(); ++j){
					if (i != j){
						comp = comps.get(j);
						compBorders = Utilities.getSmallerIndexFst(comp.getStartIndex(), comp.getEndIndex());
						scndStart = compBorders.getFirst();
						scndEnd = compBorders.getSecond();
						
						if (hurdles[j] == Constants.FALSE && scndStart < start && scndEnd > start){
							protects = true;
							break;
						} else {
							if (maximalComps.isEmpty()){
									maximalComps.add(new Pair<Integer, Integer>(scndStart, scndEnd));
									areHurdles.add(hurdles[j]);
							} else {
								Pair<Integer, Integer> currentPair;
								for (int k=0; k<maximalComps.size(); ++k){
									currentPair = maximalComps.get(k);
									if (scndStart < currentPair.getFirst() && scndEnd > currentPair.getSecond()){
										maximalComps.remove(k);
										areHurdles.remove(k);
									} else
									if (scndStart > currentPair.getFirst() && scndEnd < currentPair.getSecond()){
										isContained = true;
									}
								}
								if (!isContained){
									maximalComps.add(new Pair<Integer, Integer>(scndStart, scndEnd));
									areHurdles.add(hurdles[j]);
								}
							}
						}
					}
				}
				if (protects || maximalComps.size() == 1 && areHurdles.get(0) == Constants.FALSE){//counter == superHurdles.length-(hurdlePair.getSecond()-1)){
					superHurdles[i] = 2;
					++nbSuperHurds;
				}
				protects = false;
			}
		}
		return new Pair<Integer, int[]>(nbSuperHurds, superHurdles);
	}

	/**
	 * Checks an array of components for hurdles (knots, real-knots, respectively).
	 * An unoriented intrachromosomal component is a hurdle/knot/real-knot in case it
	 * does not separate other components of the same set. Meaning it is minimal -> has no
	 * inner components or it is maximal and contains all other components of the set. Therefore,
	 * if the set of components consists of only one component this one is a hurdle, but not super.
	 * @param comps the components to check for hurdles
	 * @return a new array which contains a 0 at the index of a non-hurdle and a 1 at the index of a hurdle
	 */
	private Pair<int[], Integer> checkHurdles(final ArrayList<Component> comps) {
		int[] hurdles = new int[comps.size()];
		Integer nbHurdles = 0;
		Component comp;
		Pair<Integer, Integer> compBorders;
		int start;
		int end;
		int scndStart;
		boolean hasInnerComp = false;
		boolean hasOuterComp = false;
		for (int i=0; i<comps.size(); ++i){
			comp = comps.get(i);
			compBorders = Utilities.getSmallerIndexFst(comp.getStartIndex(), comp.getEndIndex());
			start = compBorders.getFirst();
			end = compBorders.getSecond(); //compare them to ALL other comps in this set
			for (int j=0; j<comps.size(); ++j){
				if (j != i){
					comp = comps.get(j);
					scndStart = Utilities.getSmallerIndexFst(comp.getStartIndex(), comp.getEndIndex()).getFirst();				
					if (start < scndStart && end > scndStart){ //scndEnd cannot be bigger then, because of comp definition
						hasInnerComp = true;
					} else {
						hasOuterComp = true;
					}
				}
				
			}
			if (hasInnerComp && !hasOuterComp || !hasInnerComp){
				hurdles[i] = Constants.TRUE;
				++nbHurdles;
			}
			hasInnerComp = false;
			hasOuterComp = false;
		}
		return new Pair<int[], Integer>(hurdles, nbHurdles);
	}

	/**
	 * Checks if a component is a real component = has no pi or l caps in its span.
	 * @param comp the component to check
	 * @param capDistinction the array classifying which extremities are pi or l caps
	 * @param plusCappedGnom the plus capped genome
	 * @return true, if the component is a real component, false otherwise
	 */
	private boolean realComp(final Component comp, final int[] capDistinction, final int[] plusCappedGnom) {
		Pair<Integer, Integer> compBorders = Utilities.getSmallerIndexFst(comp.getStartIndex(), comp.getEndIndex());
		int start = compBorders.getFirst();
		int end = compBorders.getSecond();
		int ext1 = Utilities.getExtremity(plusCappedGnom[start]*2, true);
		int ext2 = Utilities.getExtremity(plusCappedGnom[end]*2, false);
		if (capDistinction[ext1] != Constants.FALSE || capDistinction[ext2] != Constants.FALSE){
			return false;
		}
		for (int j=start; j<end; ++j){
			ext1 = Utilities.getExtremity(plusCappedGnom[j]*2, false);
			ext2 = Utilities.getExtremity(plusCappedGnom[j]*2, true);
			if (capDistinction[ext1] != Constants.FALSE || capDistinction[ext2] != Constants.FALSE){
				return false;
			}
		}
		return true;
	}

	/**
	 * Generates the array for cap distinction in so called pi and l caps (see Tesler in class comment).
	 * That means the array contains 0 for all extremities which are no caps in any genome and
	 * 1 (2) if they are pi (l) caps. Figure 3 in Jean & Nikolski explains the use of pi and l caps best.
	 * @param initialAdjacencies original adjacency graph including both genomes 
	 * @param capAdjG1 capped adjacency array of genome A (chained in one chromosome)
	 * @param nbChromsA the number of chromosomes of genome A
	 * @return the array determining the pi and l caps of genome A
	 */
	private int[] genCapDistinction(final AdjacencyGraph initialAdjacencies, final int[] capAdjG1, final int nbChromsA) {
		int[] capDistinction = new int[capAdjG1.length];
		int[] adjG1 = initialAdjacencies.getAdjacenciesGenome1();
		int[] adjG2 = initialAdjacencies.getAdjacenciesGenome2();
		int currValue;
		for (int i=0; i<adjG1.length; ++i){
			currValue = adjG1[i];
			if (currValue == i){
				capDistinction[capAdjG1[i]] = Constants.PI_CAP;
			}
			currValue = adjG2[i];
			if (currValue == i){
				capDistinction[i] = Constants.L_CAP;
			}
		}
		for (int i=adjG1.length+nbChromsA*4+1; i<capDistinction.length-2; ++i){
			capDistinction[i] = Constants.PI_CAP;
			capDistinction[++i] = Constants.PI_CAP;
			i += 2;
		}
		return capDistinction;
	}

	/**
	 * Generates the adjacency array independent of the capping of an
	 * adjacency array handed over to the method. This means that all telomere and cap
	 * extremities will be linked to theirselves.
	 * @param adjArray Array of adjacencies of a genome
	 * @param nbChromsA number of chromosomes of genome A
	 * @return the new array independent of the capping
	 */
	private int[] genCappingIndepndntGraph(final int[] adjArray, final int nbGenes, final int nbChromsA) {
		int[] cappingIndepndntArray = adjArray.clone();
		int border = nbGenes*2;
		int currValue;
		for (int i=1; i<adjArray.length; ++i){
			currValue = cappingIndepndntArray[i];
			if (i-border > nbChromsA*4+1 && (i-border-2) % 4 == 0){
				cappingIndepndntArray[i+1] = i+1;
			}
			if (currValue > border && i > border){
				cappingIndepndntArray[i] = i;
				cappingIndepndntArray[currValue] = currValue;
			} else
			if (currValue > border){
				cappingIndepndntArray[currValue] = currValue;
			}
		}
		return cappingIndepndntArray;
	}

	/**
	 * Capps a genome from n+1 to n+nbChroms*2
	 * @param genome the genome to cap
	 * @param nbChromsBiggest the number of chromosomes of the bigger genome
	 * @return a new capped genome with only 1 chromosome as integer array
	 */
	private int[] capGenome(final Genome genome, int nbChromsBiggest) {
		int[] newGenome = new int[genome.getNumberOfGenes()+nbChromsBiggest*2];
		int counter = 0;
		int cap = genome.getNumberOfGenes()+1;
		for (int i=0; i<genome.getNumberOfChromosomes(); ++i){
			newGenome[counter++] = cap++;
			int[] chrom = genome.getChromosome(i).getGenes();
			for (int j=0; j<chrom.length; ++j){
				newGenome[counter++] = chrom[j];
			}
			newGenome[counter++] = cap++;
		}
		for (int i=counter; i<newGenome.length; ++i){
			newGenome[i] = cap++;
		}
		return newGenome;
	}


	/**
	 * Walks along the path in the adjacency graph from ext1 until another cap 
	 * is found. Since this method is only for "cap-paths" there is always a second
	 * cap at the end of the path. 
	 * @param ext1 the extremity to start with
	 * @param cappingIndepndntGraph adjacency array of genome 1 (unsorted)
	 * @param capDistinction the cap distinction array
	 * @param initialAdjG1 
	 * @return a pair whose first element is the extremity and the second element the kind of cap
	 */
	private Pair<Integer, Integer> checkPath4LTail(final int ext1, final int[] cappingIndepndntGraph, 
			final int[] capDistinction, final int[] initialAdjG1) {
		boolean fst = false;
		int ext3 = cappingIndepndntGraph[ext1];
		if (ext3 == ext1){
			ext3 = initialAdjG1[ext3];
		}
		if (capDistinction[ext3] == Constants.L_CAP){
			return new Pair<Integer, Integer>(ext3, Constants.L_CAP);
		} else
		if (capDistinction[ext3] == Constants.PI_CAP){
			return new Pair<Integer, Integer>(ext3, Constants.PI_CAP);
		}
		
		while (cappingIndepndntGraph[ext3] != ext3){
			if (fst){
				ext3 = cappingIndepndntGraph[ext3];
				fst = false;
			} else {
				ext3 = Utilities.getNeighbourExt(ext3);
				fst = true;
			}
			if (capDistinction[ext3] == Constants.L_CAP){
				return new Pair<Integer, Integer>(ext3, Constants.L_CAP);
			} else
			if (capDistinction[ext3] == Constants.PI_CAP){
				return new Pair<Integer, Integer>(ext3, Constants.PI_CAP);
			}
		}
		return new Pair<Integer, Integer>(0, 0); //never reached since a cap path always ends with another cap!
	}
}
