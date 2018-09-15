package de.unibi.cebitec.gi.unimog.algorithms;

import java.util.ArrayList;
import java.util.HashMap;


import de.unibi.cebitec.gi.unimog.datastructure.AdditionalDataHPDistance;
import de.unibi.cebitec.gi.unimog.datastructure.AdjacencyGraph;
import de.unibi.cebitec.gi.unimog.datastructure.Component;
import de.unibi.cebitec.gi.unimog.datastructure.Data;
import de.unibi.cebitec.gi.unimog.datastructure.Genome;
import de.unibi.cebitec.gi.unimog.datastructure.IAdditionalData;
import de.unibi.cebitec.gi.unimog.datastructure.OperationList;
import de.unibi.cebitec.gi.unimog.datastructure.Pair;
import de.unibi.cebitec.gi.unimog.datastructure.multifurcatedTree.CountLeavesVisitorExtended;
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
 * Class which implements the sorting by translocations algorithm.
 * It runs in n^3 time.
 * It is still restricted to co-tailed genomes!
 * There are only white components in the tree / black components are treated the same way!
 * TODO: test improper array with suitable genome with sequence of improper trans
 * 
 * [92] M. Ozery-Flato and R. Shamir. An algorithm for sorting by translocations <- für bessere laufzeit!
 * n^(3/2) * wurzel (log (n))
 */
public class SortingTrans implements ISorting {

    private int gene = -1;
    private boolean allGenesInComps = false;

    /* (non-Javadoc)
     * @see algorithms.ISorting#findOptSortSequence(datastructure.Data, datastructure.IAdditionalData, java.util.HashMap)
     */
    @Override
    public OperationList findOptSortSequence(final Data data, final IAdditionalData additionalData,
            final HashMap<Integer, Integer> chromMap) {

        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        //---------------------initialize data structures------------------------------------------------------
        ///////////////////////////////////////////////////////////////////////////////////////////////////////

        final OperationList operationList = new OperationList();
        int[] involvExtremities = new int[Constants.NB_EXTREMITIES];

        //preprocessing (construction of component Tree & components)
        AdditionalDataHPDistance additionalHPData = (AdditionalDataHPDistance) additionalData;
        HPBasedDistPreprocessing hpPreprocess = new HPBasedDistPreprocessing(data, additionalHPData);
        int[] plusCappedGnom = additionalHPData.getGenomeCappedPlusArray();
        int[] adjG1 = data.getAdjGraph().getAdjacenciesGenome1();
        final int[] adjG2 = data.getAdjGraph().getAdjacenciesGenome2();
        MultifurcatedTree componentTree = hpPreprocess.getCompTree();
        ArrayList<Component> components = hpPreprocess.getComponents();
        boolean[] geneInCompArray = this.generateGeneInCompArray(components, plusCappedGnom, data.getGenomeA().getNumberOfGenes());
        byte[] geneAtCompBorderArray = this.generateGeneAtCompBorderArray(components, plusCappedGnom, data.getGenomeA().getNumberOfGenes());
        int[] nodeToCompMap = hpPreprocess.getNodeToCompMap();
        int[] posDepthArray = this.generatePosDepthArray(nodeToCompMap, plusCappedGnom, components, data.getGenomeA().getNumberOfGenes());
        int[] newChromMap = this.genNewChromMap(data.getGenomeA());
        int[] compMap = this.generateCompMap(newChromMap, components, plusCappedGnom);
        boolean scndCase = false;
        boolean improper = false;
        Genome newGenome = data.getGenomeA();
        IntermediateGenomesGenerator genomeGenerator = new IntermediateGenomesGenerator();
        AdjacencyGraph adjGraphNew = new AdjacencyGraph(data.getGenomeA(), data.getGenomeB());
        Data dataWorkNew = new Data(data.getGenomeA(), data.getGenomeB(), data.getAdjGraph().clone());
        AdditionalDataHPDistance addDataWorkNew = new AdditionalDataHPDistance(data.getGenomeA());
        ArrayList<Pair<Integer, Integer>> improperAdjs = new ArrayList<Pair<Integer, Integer>>();
        int lastImpr = 2;
        boolean[] visited = new boolean[adjG1.length];

        int transDistance = this.calcTransDist(dataWorkNew, addDataWorkNew, componentTree);
        if (transDistance == 0) {
            return operationList;
        }
        int newTransDistance = transDistance;

        //Numbers of leaves etc. are calculated:
        CountLeavesVisitorExtended countingVisitor = new CountLeavesVisitorExtended();
        componentTree.topDown(countingVisitor);
        int nbOfLeaves = countingVisitor.getNbOfLeaves();
        ArrayList<Integer> leafToIndexMap = countingVisitor.getLeafToIndexMap();
        int nbTrees = componentTree.getRoot().getNodeChildren().size();
        ArrayList<Integer> treeIndexMap = countingVisitor.getTreeIndexMap();
        ArrayList<Integer> treeSizeMap = countingVisitor.getTreeSizeMap();

        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        //----------reduce the number of leaves/components to an even number or destroy last tree--------------
        ///////////////////////////////////////////////////////////////////////////////////////////////////////

        boolean needAnotherPreStep = this.allGenesInComps && nbOfLeaves % 2 == 0 && nbTrees == 1
                || nbOfLeaves % 2 == 0 && nbTrees == 1 || nbOfLeaves % 2 == 1;
        while (needAnotherPreStep) {
            if (nbOfLeaves % 2 == 0 && nbTrees == 1) { //also means whole genome cannot be covered by components
                //destroy any leaf of the single tree such that L' = L-1 

                final int iFirstLeaf = leafToIndexMap.get(0);
                final int iComp = nodeToCompMap[iFirstLeaf];
                int start = components.get(iComp).getStartIndex();
                involvExtremities = this.generateInvolvedExtremities(start, plusCappedGnom, newChromMap, newGenome, geneInCompArray, geneAtCompBorderArray, posDepthArray);

                //save operations. Note that algos always have to store op in this order! result will be corrupted otherwise
                Utilities.assignInvolvedExtr(involvExtremities, adjG1, true);
                operationList.addOperation(new Pair<Integer, Integer>(involvExtremities[0], involvExtremities[3]));
                operationList.addOperation(new Pair<Integer, Integer>(involvExtremities[1], involvExtremities[2]));
                operationList.addAdjacencyArrayG1(adjG1.clone());

                ///////////////////////////////////////////////////////////////////////////////////////////////////////
                //----------------------- update all data structures after operation -----------------
                ///////////////////////////////////////////////////////////////////////////////////////////////////////

                newGenome = genomeGenerator.generateGenome(adjG1);
                newChromMap = this.genNewChromMap(newGenome);
                adjGraphNew = new AdjacencyGraph(newGenome, data.getGenomeB());
                dataWorkNew = new Data(newGenome, data.getGenomeB(), adjGraphNew);
                addDataWorkNew = new AdditionalDataHPDistance(newGenome);
                hpPreprocess = new HPBasedDistPreprocessing(dataWorkNew, addDataWorkNew);
                plusCappedGnom = addDataWorkNew.getGenomeCappedPlusArray();
                componentTree = hpPreprocess.getCompTree();
                components = hpPreprocess.getComponents();
                nodeToCompMap = hpPreprocess.getNodeToCompMap();
                compMap = this.generateCompMap(newChromMap, components, plusCappedGnom);
                //Numbers of leaves etc. are calculated:
                countingVisitor = new CountLeavesVisitorExtended();
                componentTree.topDown(countingVisitor);
                nbOfLeaves = countingVisitor.getNbOfLeaves();
                leafToIndexMap = countingVisitor.getLeafToIndexMap();
                nbTrees = componentTree.getRoot().getNodeChildren().size();
                treeIndexMap = countingVisitor.getTreeIndexMap();
                treeSizeMap = countingVisitor.getTreeSizeMap();
                geneInCompArray = this.generateGeneInCompArray(components, plusCappedGnom, newGenome.getNumberOfGenes());
                geneAtCompBorderArray = this.generateGeneAtCompBorderArray(components, plusCappedGnom, newGenome.getNumberOfGenes());
                posDepthArray = this.generatePosDepthArray(nodeToCompMap, plusCappedGnom, components, newGenome.getNumberOfGenes());
                newTransDistance = this.calcTransDist(dataWorkNew, addDataWorkNew, componentTree);
                transDistance = newTransDistance;

            }
            if (nbOfLeaves % 2 == 1) {
                /* perform a bad translocation such that T'=0, or T'>1 and L' = L-1 (576 oben Proof Theorem 2)
                 * 2 Cases: T = 1: destroy middle leaf of tree
                 * T > 1: destroy a leaf in tree with > 1 leaves if one exists, otherwise any leaf */

                if (nbTrees == 1) { //again whole genome cannot be covered by components
                    final int iMiddleLeaf = leafToIndexMap.get(nbOfLeaves / 2);
                    final int iComp = nodeToCompMap[iMiddleLeaf];
                    int start = components.get(iComp).getStartIndex();
                    involvExtremities = this.generateInvolvedExtremities(start, plusCappedGnom, newChromMap, newGenome, geneInCompArray, geneAtCompBorderArray, plusCappedGnom);

                } else { //nbTrees > 1
					/* the biggest tree in the forest might also only have 1 leaf. 
                     * we want to destroy first comp of the biggest tree (with more than 1 children, if there is one.
                     * in case the whole genome is covered by components we have to merge exactly 2 components */

                    final int iBiggestTree = countingVisitor.getStartBiggestTree();
                    int i = 0;
                    int iFstLeaf = leafToIndexMap.get(i);
                    //get first leaf belonging to biggest tree
                    while (iFstLeaf < iBiggestTree) {
                        iFstLeaf = leafToIndexMap.get(++i); //never reaches next tree, because then biggest tree is without leaf
                    }

                    final int iComp = nodeToCompMap[iFstLeaf];
                    int start = components.get(iComp).getStartIndex();
                    involvExtremities = this.generateInvolvedExtremities(start, plusCappedGnom, newChromMap, newGenome, geneInCompArray, geneAtCompBorderArray, plusCappedGnom);
                }

                //save operations
                adjG1 = Utilities.assignInvolvedExtr(involvExtremities, adjG1, true);
                operationList.addOperation(new Pair<Integer, Integer>(involvExtremities[0], involvExtremities[3]));
                operationList.addOperation(new Pair<Integer, Integer>(involvExtremities[1], involvExtremities[2]));
                operationList.addAdjacencyArrayG1(adjG1.clone());

                ///////////////////////////////////////////////////////////////////////////////////////////////////////
                //----------------------- update all data structures after operation -----------------
                ///////////////////////////////////////////////////////////////////////////////////////////////////////

                newGenome = genomeGenerator.generateGenome(adjG1);
                newChromMap = this.genNewChromMap(newGenome);
                adjGraphNew = new AdjacencyGraph(newGenome, data.getGenomeB());
                dataWorkNew = new Data(newGenome, data.getGenomeB(), adjGraphNew);
                addDataWorkNew = new AdditionalDataHPDistance(newGenome);
                hpPreprocess = new HPBasedDistPreprocessing(dataWorkNew, addDataWorkNew);
                plusCappedGnom = addDataWorkNew.getGenomeCappedPlusArray();
                componentTree = hpPreprocess.getCompTree();
                components = hpPreprocess.getComponents();
                nodeToCompMap = hpPreprocess.getNodeToCompMap();
                compMap = this.generateCompMap(newChromMap, components, plusCappedGnom);
                //Numbers of leaves etc. are calculated:
                countingVisitor = new CountLeavesVisitorExtended();
                componentTree.topDown(countingVisitor);
                nbOfLeaves = countingVisitor.getNbOfLeaves();
                leafToIndexMap = countingVisitor.getLeafToIndexMap();
                nbTrees = componentTree.getRoot().getNodeChildren().size();
                treeIndexMap = countingVisitor.getTreeIndexMap();
                treeSizeMap = countingVisitor.getTreeSizeMap();
                geneInCompArray = this.generateGeneInCompArray(components, plusCappedGnom, newGenome.getNumberOfGenes());
                geneAtCompBorderArray = this.generateGeneAtCompBorderArray(components, plusCappedGnom, newGenome.getNumberOfGenes());
                posDepthArray = this.generatePosDepthArray(nodeToCompMap, plusCappedGnom, components, newGenome.getNumberOfGenes());
                newTransDistance = this.calcTransDist(dataWorkNew, addDataWorkNew, componentTree);
                transDistance = newTransDistance;
            }
            needAnotherPreStep = this.allGenesInComps;
        }

        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        //while the genome is not sorted apply cases of "lemma 4/12", first components on different chromosomes
        ///////////////////////////////////////////////////////////////////////////////////////////////////////

        boolean genomeSorted = false;
        while (!genomeSorted) {

            boolean compsOnDiffChroms = false;
            scndCase = false;
            if (compMap.length > 0) {
                compsOnDiffChroms = this.testCompsOnDiffChroms(compMap, components);
            }

            if (compsOnDiffChroms) { //there exist intrachromosomal components on different chromosomes then
                //perform a bad translocation such that T' = 0, or T' > 1 and L' is even (Lemma 4)
				/*
                 * 2 Cases: T has > 1 or = 1 leaf. If T has 1 leaf T2 also has 1 leaf & both can be destroyed
                 * In 1st case choose second leaf from the left of biggest tree and pair it with any leaf of t2.
                 * In 2nd case choose first leaf of each tree (there is only 1)
                 * if theres the possibility that the operation doesnt fit anyway then add index saying which
                 * adjacencies of the comps have already been tested when testing next operation
                 */

                final int nodeIBigTree = countingVisitor.getStartBiggestTree(); //get the two biggest trees on diff chroms
                int chromNbBiggest = compMap[nodeToCompMap[nodeIBigTree]];
                int node2ndBig = -1;
                int child2ndBiggest = -1;
                for (int i = 0; i < treeIndexMap.size(); ++i) {
                    int currTree = treeIndexMap.get(i); //is the node index of the current tree
                    int chromCurrTree = compMap[nodeToCompMap[currTree]];
                    if (currTree != nodeIBigTree && chromCurrTree != chromNbBiggest && treeSizeMap.get(i) > child2ndBiggest) {
                        node2ndBig = currTree;
                        child2ndBiggest = treeSizeMap.get(i);
                    }
                }

                int i = 0; //get second or first leaf belonging to biggest tree
                int i2ndLeaf = 0;
                while (i2ndLeaf < nodeIBigTree) {
                    i2ndLeaf = leafToIndexMap.get(++i); //never reaches next tree, because then biggest tree is without leaf
                }
                if (treeSizeMap.get(nodeIBigTree) > 1) {
                    i2ndLeaf = leafToIndexMap.get(i + 1); //+1 because must have another leaf = 2nd one
                }
                int start = components.get(nodeToCompMap[i2ndLeaf]).getStartIndex();
                involvExtremities[0] = Utilities.getExtremity(plusCappedGnom[start] * 2, true);
                involvExtremities[1] = Utilities.getExtremity(plusCappedGnom[start + 1] * 2, false);

                i = 0; //get fst leaf belonging to scnd biggest tree
                int fstLeaf = this.getFstLeafOfTree(node2ndBig, leafToIndexMap);
                int scndStart = components.get(nodeToCompMap[fstLeaf]).getStartIndex();
                involvExtremities[2] = Utilities.getExtremity(plusCappedGnom[scndStart] * 2, true);
                involvExtremities[3] = Utilities.getExtremity(plusCappedGnom[scndStart + 1] * 2, false);

                //update data structures
                int[] adjNew = adjG1.clone();
                newGenome = genomeGenerator.generateGenome(Utilities.assignInvolvedExtr(involvExtremities, adjNew, true));
                newChromMap = this.genNewChromMap(newGenome);
                adjGraphNew = new AdjacencyGraph(newGenome, data.getGenomeB());
                dataWorkNew = new Data(newGenome, data.getGenomeB(), adjGraphNew);
                addDataWorkNew = new AdditionalDataHPDistance(newGenome);
                hpPreprocess = new HPBasedDistPreprocessing(dataWorkNew, addDataWorkNew);
                plusCappedGnom = addDataWorkNew.getGenomeCappedPlusArray();
                componentTree = hpPreprocess.getCompTree();
                newTransDistance = this.calcTransDist(dataWorkNew, addDataWorkNew, componentTree);

            } else ///////////////////////////////////////////////////////////////////////////////////////////////////////
            //--------------in case all components are located on the same chromosome, separate them---------------
            ///////////////////////////////////////////////////////////////////////////////////////////////////////
            if (!compsOnDiffChroms && this.checkCompLength(components)) {
                /* perform a proper translocation such that T and L remain unchanged to separate
                 * the components on different chromosomes
                 */

                Genome saveGenome = newGenome;
                int[] savePlusCapped = plusCappedGnom.clone();
                int size = treeIndexMap.size();
                boolean chromCheck = true; //true if both genes are on same chrom, false otherwise
                int i = 0;
                int scndIndex = 0;

                while (chromCheck && i < size - 1) { //retrieve first cut. loop for walking between all comps on the chrom

                    int endFstNode = components.get(nodeToCompMap[treeIndexMap.get(i)]).getEndIndex();
                    int endSmallerScndNode = components.get(nodeToCompMap[treeIndexMap.get(i + 1) - 1]).getEndIndex();
                    int start = 0;
                    if (endFstNode < endSmallerScndNode) {
                        //last comp before start of next tree is the interesting one, therefore endindex is directly obtained
                        start = endSmallerScndNode;
                    } else {
                        start = endFstNode;
                    }
                    int scndComp = nodeToCompMap[this.getFstLeafOfTree(treeIndexMap.get(i + 1), leafToIndexMap)];
                    int end = components.get(scndComp).getStartIndex();
                    involvExtremities[0] = Utilities.getExtremity(plusCappedGnom[start] * 2, true);
                    involvExtremities[1] = Utilities.getExtremity(plusCappedGnom[start + 1] * 2, false);
                    scndIndex = start;

                    if (improper) {
                        boolean isImproper = true;
                        while (isImproper) {
                            ++scndIndex;
                            isImproper = this.checkImproper(Utilities.getExtremity(plusCappedGnom[scndIndex] * 2, true), involvExtremities[2], adjG1, improperAdjs);
                        }
                        involvExtremities[0] = Utilities.getExtremity(plusCappedGnom[scndIndex] * 2, true);
                        involvExtremities[1] = Utilities.getExtremity(plusCappedGnom[scndIndex + 1] * 2, false);
                    }

                    while (chromCheck == this.chromCheck(plusCappedGnom, newChromMap, scndIndex) && scndIndex < end) {
                        ++scndIndex;
                        involvExtremities[0] = Utilities.getExtremity(plusCappedGnom[scndIndex] * 2, true);
                        involvExtremities[1] = Utilities.getExtremity(plusCappedGnom[scndIndex + 1] * 2, false);
                    }
                    ++i;
                }

                int[] scndCut = this.genScndCut(plusCappedGnom);
                involvExtremities[2] = scndCut[0];
                involvExtremities[3] = scndCut[1];

                int[] adjArray1 = adjG1.clone();
                improper = false;
                lastImpr = involvExtremities[0];

                int scnd = 0;
                while (++scnd <= 2) { //TODO: Check unneccessary because of scnd cut generator?
                    adjArray1 = adjG1.clone();
                    if (scnd == 1) {
                        adjArray1 = Utilities.assignInvolvedExtr(involvExtremities, adjArray1, true);
                    } else {
                        adjArray1 = Utilities.assignInvolvedExtr(involvExtremities, adjArray1, false);
                    }
                    //generate new genomeA and Adjacency graph
                    newGenome = genomeGenerator.generateGenome(adjArray1);
                    adjGraphNew = new AdjacencyGraph(newGenome, data.getGenomeB());
                    dataWorkNew = new Data(newGenome, data.getGenomeB(), adjGraphNew);
                    addDataWorkNew = new AdditionalDataHPDistance(newGenome);

                    HPBasedDistPreprocessing hpPreprocessNew = new HPBasedDistPreprocessing(dataWorkNew, addDataWorkNew);
                    ArrayList<Component> componentsNew = hpPreprocessNew.getComponents();
                    if (this.checkCompLength(componentsNew)) {
                        hpPreprocess = new HPBasedDistPreprocessing(dataWorkNew, addDataWorkNew);
                        plusCappedGnom = addDataWorkNew.getGenomeCappedPlusArray();
                        componentTree = hpPreprocess.getCompTree();
                        newTransDistance = this.calcTransDist(dataWorkNew, addDataWorkNew, componentTree);
                        if (newTransDistance < transDistance) {
                            if (scnd == 2) {
                                scndCase = true;
                            }
                            improper = false;
                            ++scnd;
                            lastImpr = 2;
                        } else if (scnd == 2) {
                            improperAdjs.add(new Pair<Integer, Integer>(involvExtremities[0], involvExtremities[1]));
                            improperAdjs.add(new Pair<Integer, Integer>(involvExtremities[2], involvExtremities[3]));
                            improper = true;
                            newGenome = saveGenome;
                            plusCappedGnom = savePlusCapped;
                        }
                    } else if (scnd == 2) {
                        improperAdjs.add(new Pair<Integer, Integer>(involvExtremities[0], involvExtremities[1]));
                        improperAdjs.add(new Pair<Integer, Integer>(involvExtremities[2], involvExtremities[3]));
                        improper = true;
                        newGenome = saveGenome;
                        plusCappedGnom = savePlusCapped;
                    }
                }


                ///////////////////////////////////////////////////////////////////////////////////////////////////////
                //--------------in case all components are destroyed perform a proper translocation--------------------
                ///////////////////////////////////////////////////////////////////////////////////////////////////////

            } else {
                /*
                 * Standard case of performing a proper translocation sorting the genome.
                 * proper, if for prefix-prefix translocation there's a cycle (uv...fg..u) and for
                 * prefix-suffix trans (with gene inversion) there's a cycle (uv...gf..u) in Adjacency graph.
                 */
                int ex = adjG1[lastImpr];
                int startEx = lastImpr;
                int chromNbStart = newChromMap[(ex + 1) / 2];
                int chromNb = newChromMap[(startEx + 1) / 2];
                if (!improper) {
                    visited = new boolean[adjG1.length];
                }
                visited[startEx] = true;
                visited[ex] = true;
                boolean fstGenome = false;
                while (chromNbStart == chromNb || (chromNbStart != chromNb && !fstGenome)) {
                    if (fstGenome) {
                        ex = adjG1[ex];
                        chromNb = newChromMap[(ex + 1) / 2];
                        fstGenome = false;
                    } else {
                        ex = adjG2[ex];
                        chromNb = newChromMap[(ex + 1) / 2];
                        fstGenome = true;
                    }
                    visited[ex] = true;
                    if (startEx == ex) {
                        while (visited[startEx]) {
                            ++startEx;
                            chromNbStart = newChromMap[(startEx + 1) / 2];
                            chromNb = newChromMap[(startEx + 1) / 2];
                            ex = startEx;
                        }
                        visited[ex] = true;
                        fstGenome = false;
                    }
                    if (improper) {
                        while (this.checkImproper(ex, startEx, adjG1, improperAdjs)) {
                            int oldEx = ex;
                            boolean fst = true;
                            while (ex != oldEx || fst) {
                                if (fstGenome) {
                                    visited[ex] = false;
                                    ex = adjG1[ex];
                                    fstGenome = false;
                                } else {
                                    visited[ex] = false;
                                    ex = adjG2[ex];
                                    fstGenome = true;
                                }
                                fst = false;
                            }
                            ++startEx;
                            ex = startEx;
                            chromNbStart = newChromMap[(startEx + 1) / 2];
                            chromNb = newChromMap[(startEx + 1) / 2];

                        }
                    }
                }
                improper = false;
                involvExtremities[0] = startEx;
                involvExtremities[1] = adjG1[startEx];
                involvExtremities[2] = ex;
                involvExtremities[3] = adjG1[ex];
                lastImpr = startEx;

                int scnd = 0;
                Genome saveGenome = newGenome;
                int[] savePlusCapped = plusCappedGnom.clone();
                while (++scnd <= 2) {
                    int[] adjArray1 = adjG1.clone();
                    if (scnd == 1) {
                        adjArray1 = Utilities.assignInvolvedExtr(involvExtremities, adjArray1, true);
                    } else {
                        adjArray1 = Utilities.assignInvolvedExtr(involvExtremities, adjArray1, false);
                    }
                    //generate new genomeA and Adjacency graph
                    newGenome = genomeGenerator.generateGenome(adjArray1);
                    adjGraphNew = new AdjacencyGraph(newGenome, data.getGenomeB());
                    dataWorkNew = new Data(newGenome, data.getGenomeB(), adjGraphNew);
                    addDataWorkNew = new AdditionalDataHPDistance(newGenome);

                    HPBasedDistPreprocessing hpPreprocessNew = new HPBasedDistPreprocessing(dataWorkNew, addDataWorkNew);
                    ArrayList<Component> componentsNew = hpPreprocessNew.getComponents();
                    if (!this.checkCompLength(componentsNew)) {
                        hpPreprocess = new HPBasedDistPreprocessing(dataWorkNew, addDataWorkNew);
                        plusCappedGnom = addDataWorkNew.getGenomeCappedPlusArray();
                        componentTree = hpPreprocess.getCompTree();
                        newTransDistance = this.calcTransDist(dataWorkNew, addDataWorkNew, componentTree);
                        if (newTransDistance < transDistance) {
                            if (scnd == 2) {
                                scndCase = true;
                            }
                            improper = false;
                            ++scnd;
                            lastImpr = 2;
                        } else if (scnd == 2) {
                            improperAdjs.add(new Pair<Integer, Integer>(involvExtremities[0], involvExtremities[1]));
                            improperAdjs.add(new Pair<Integer, Integer>(involvExtremities[2], involvExtremities[3]));
                            improper = true;
                            newGenome = saveGenome;
                            plusCappedGnom = savePlusCapped;
                        }
                    } else if (scnd == 2) {
                        improperAdjs.add(new Pair<Integer, Integer>(involvExtremities[0], involvExtremities[1]));
                        improperAdjs.add(new Pair<Integer, Integer>(involvExtremities[2], involvExtremities[3]));
                        improper = true;
                        newGenome = saveGenome;
                        plusCappedGnom = savePlusCapped;
                    }
                }
            }


            //save operations
            if (!improper && newTransDistance < transDistance) {

                improperAdjs.clear();
                if (scndCase) {
                    adjG1 = Utilities.assignInvolvedExtr(involvExtremities, adjG1, false);
                    operationList.addOperation(new Pair<Integer, Integer>(involvExtremities[0], involvExtremities[2]));
                    operationList.addOperation(new Pair<Integer, Integer>(involvExtremities[1], involvExtremities[3]));
                } else {
                    adjG1 = Utilities.assignInvolvedExtr(involvExtremities, adjG1, true);
                    operationList.addOperation(new Pair<Integer, Integer>(involvExtremities[0], involvExtremities[3]));
                    operationList.addOperation(new Pair<Integer, Integer>(involvExtremities[1], involvExtremities[2]));
                }

                plusCappedGnom = addDataWorkNew.getGenomeCappedPlusArray();
                components = hpPreprocess.getComponents();
                nodeToCompMap = hpPreprocess.getNodeToCompMap();
                newChromMap = this.genNewChromMap(dataWorkNew.getGenomeA());
                compMap = this.generateCompMap(newChromMap, components, plusCappedGnom);

                //Numbers of leaves etc. are calculated:
                countingVisitor = new CountLeavesVisitorExtended();
                componentTree.topDown(countingVisitor);
                nbOfLeaves = countingVisitor.getNbOfLeaves();
                leafToIndexMap = countingVisitor.getLeafToIndexMap();
                nbTrees = componentTree.getRoot().getNodeChildren().size();
                treeIndexMap = countingVisitor.getTreeIndexMap();
                treeSizeMap = countingVisitor.getTreeSizeMap();

                operationList.addAdjacencyArrayG1(adjG1.clone());
                transDistance = newTransDistance;
                if (transDistance == 0) {
                    genomeSorted = true;
                }
            }
        }

        return operationList;
    }

    /**
     * Generates a boolean array containing at each position (which represents the gene number)
     * a 1 if the gene is located in a component and 0 if it is not in any component.
     * @param components the components
     * @param plusCappedGnom the plus capped genome
     * @param numberOfGenes number of genes
     * @return the boolean array knowing if a gene belongs to a component or not
     */
    private boolean[] generateGeneInCompArray(final ArrayList<Component> components, final int[] plusCappedGnom,
            final int numberOfGenes) {
        boolean[] geneInCompArray = new boolean[numberOfGenes + 1];
        int counter = 0;
        int start;
        int end;
        Pair<Integer, Integer> compBorders;
        for (int i = 0; i < components.size(); ++i) {
            compBorders = Utilities.getSmallerIndexFst(components.get(i).getStartIndex(), components.get(i).getEndIndex());
            start = compBorders.getFirst();
            end = compBorders.getSecond();
            if (end - start >= 2) {
                for (int j = start; j <= end; ++j) {
                    if (geneInCompArray[Math.abs(plusCappedGnom[j])] == false) {
                        ++counter;
                    }
                    geneInCompArray[Math.abs(plusCappedGnom[j])] = true;
                }
            }
        }
        if (counter == numberOfGenes) {
            this.allGenesInComps = true;
        } else {
            this.allGenesInComps = false;
        }
        return geneInCompArray;
    }

    /**
     * 
     * @param components
     * @param plusCappedGnom
     * @param numberOfGenes
     * @return a byte array holding 1 for the left gene of a component and -1 for the right gene
     * of a component.
     */
    private byte[] generateGeneAtCompBorderArray(ArrayList<Component> components,
            int[] plusCappedGnom, int numberOfGenes) {
        byte[] geneAtCompBorderArray = new byte[numberOfGenes + 1];
        Pair<Integer, Integer> compBorders;
        int start;
        int end;
        for (Component comp : components) {
            compBorders = Utilities.getSmallerIndexFst(comp.getStartIndex(), comp.getEndIndex());
            start = compBorders.getFirst();
            end = compBorders.getSecond();
            if (end - start >= 2) {
                geneAtCompBorderArray[Math.abs(plusCappedGnom[start])] = 1;
                geneAtCompBorderArray[Math.abs(plusCappedGnom[end])] = -1;
            }

        }
        return geneAtCompBorderArray;
    }

    /**
     * Generates an array holding the coverage of components for each gene in the
     * genome.
     * @param nodeToCompMap mapping of each component to 
     * @param components
     * @param numberOfGenes
     * @return 
     */
    private int[] generatePosDepthArray(int[] nodeToCompMap, int[] plusCappedGnom, ArrayList<Component> components,
            int numberOfGenes) {

        int[] posDepthArray = new int[numberOfGenes + 1];
        int iComp;
        int start;
        int stop;

        for (int i = 0; i < nodeToCompMap.length; ++i) {

            iComp = nodeToCompMap[i];
            if (iComp > 0) {
                start = components.get(iComp).getStartIndex();
                stop = components.get(iComp).getEndIndex();
                int val;
                for (int j = start; j <= stop; ++j) {
                    val = Math.abs(plusCappedGnom[j]);
                    if (val > 0 && val <= numberOfGenes) {
                        ++posDepthArray[val];
                    }
                }
            }
        }

        return posDepthArray;
    }

    /**
     * Finds gene in genome.
     * @param plusCappedGnom
     * @return index of this.geneIndex
     */
    private int[] genScndCut(int[] plusCappedGnom) {
        int extremity = -1;
        int[] adjacency = new int[2];
        for (int i = 0; i < plusCappedGnom.length; ++i) {
            if (Math.abs(plusCappedGnom[i]) == Math.abs(this.gene)) {
                if (this.gene < 0) { //get tail of this gene
                    extremity = -1 * this.gene * 2;
                } else { //get head
                    extremity = this.gene * 2 - 1;
                }
                if (plusCappedGnom[i] < 0 && this.gene < 0 || plusCappedGnom[i] > 0 && this.gene > 0) { //check orientation of gene in genome
                    adjacency[0] = Utilities.getExtremity(plusCappedGnom[i - 1] * 2, true); // cut left of gene
                    adjacency[1] = extremity;
                } else {
                    adjacency[0] = extremity;
                    adjacency[1] = Utilities.getExtremity(plusCappedGnom[i + 1] * 2, false); // cut right of gene;
                }
                return adjacency;
            }
        }
        return null;
    }

    /**
     * Checks if adjacent genes are on same chromosome or not. It is returned negative, if the tail of the
     * previous gene is wanted and positive if the head of the concurrent gene is wanted.
     * @param plusCappedGnom genome
     * @param newChromMap mapping of gene to chrom nb
     * @param start index of gene
     * @return true if both are on same chrom
     */
    private boolean chromCheck(int[] plusCappedGnom, int[] newChromMap, int start) {
        boolean chromCheck;
        if (Utilities.getExtremity(plusCappedGnom[start] * 2, true) % 2 == 1) {
            chromCheck = newChromMap[Math.abs(plusCappedGnom[start])] == newChromMap[Math.abs(plusCappedGnom[start]) - 1];
            this.gene = -1 * (Math.abs(plusCappedGnom[start]) - 1);
        } else {
            chromCheck = newChromMap[Math.abs(plusCappedGnom[start])] == newChromMap[Math.abs(plusCappedGnom[start]) + 1];
            this.gene = Math.abs(plusCappedGnom[start]) + 1;
        }
        if (chromCheck) {
            if (Utilities.getExtremity(plusCappedGnom[start + 1] * 2, false) % 2 == 1) {
                chromCheck = newChromMap[Math.abs(plusCappedGnom[start + 1])] == newChromMap[Math.abs(plusCappedGnom[start + 1]) - 1];
                this.gene = Math.abs(plusCappedGnom[start + 1]) - 1;
            } else {
                chromCheck = newChromMap[Math.abs(plusCappedGnom[start + 1])] == newChromMap[Math.abs(plusCappedGnom[start + 1]) + 1];
                this.gene = -1 * (Math.abs(plusCappedGnom[start + 1]) + 1);
            }
        }
        return chromCheck;
    }

    /**
     * Utility method returning the index of the first leaf of a tree in the component array.
     * @param treeIndex index of the tree
     * @param leafToIndexMap mapping of leaves to indices in the component array
     * @return the index of the first leaf in the component array
     */
    private int getFstLeafOfTree(final Integer treeIndex, final ArrayList<Integer> leafToIndexMap) {
        int fstLeaf = 0;
        int i = 0;
        while (fstLeaf < treeIndex) {
            fstLeaf = leafToIndexMap.get(++i); //never reaches next tree, because then biggest tree is without leaf
        }
        return fstLeaf;
    }

    /**
     * Utility method for re-calculating the translocation distance after changes in genome A.
     * @param genomeA fst genome
     * @param genomeB scnd genome (ID)
     * @param adjGraph adjacency graph of genome A
     * @return translocation distance
     */
    private int calcTransDist(final Data dataWork, final AdditionalDataHPDistance addDataWork, final MultifurcatedTree compTree) {
        int transDistance = 0;
        final DistanceTrans calcTransDist = new DistanceTrans();
        try {
            transDistance = calcTransDist.calculateDistance(dataWork, addDataWork, compTree);
        } catch (final ClassCastException e) {
            System.out.println("error in sorting translocations");
        }
        return transDistance;
    }

    /**
     * Checks for two extremities if a translocation at this position was already carried out
     * and identified as improper.
     * @param ex first cut extremity
     * @param startEx second cut extremity
     * @param adjG1 adjacency graph
     * @param improperAdjs the array of improper adjacencies
     * @return <code>true</code> if the translocation would be improper
     */
    private boolean checkImproper(int ex, int startEx, int[] adjG1,
            ArrayList<Pair<Integer, Integer>> improperAdjs) {
        for (int i = 0; i < improperAdjs.size(); ++i) {
            int fst = improperAdjs.get(i).getFirst();
            int scnd = improperAdjs.get(i).getSecond();
            int thrd = improperAdjs.get(i + 1).getFirst();
            int fourth = improperAdjs.get(i + 1).getSecond();
            if (ex == fst || ex == scnd || ex == thrd || ex == fourth) {
                if (startEx == fst || startEx == scnd || startEx == thrd || startEx == fourth) {
                    int ex3 = adjG1[ex];
                    if (ex3 == fst || ex3 == scnd || ex3 == thrd || ex3 == fourth) {
                        int ex4 = adjG1[ex];
                        if (ex4 == fst || ex4 == scnd || ex4 == thrd || ex4 == fourth) {
                            return true;
                        }
                    }
                }
            }
            ++i;
        }
        return false;
    }

    /**
     * Generates a mapping for the genes of genome A to their chromosome number.
     * It has to be updated, because the former chrom map belongs to the genome
     * sorted from 1 to N and now genome A's genes are sorted according to genome B!
     * I.e. gene 9 may now be gene 6 and if 6 and 9 are on different chromosomes
     * the old mapping still returns the chrom number of the former 6 for the current gene 6!
     * @param chromMap old chromosome mapping for genome A
     * @param genomeA genome A
     * @return the new chromosome mapping for the genes of A
     */
    private int[] genNewChromMap(final Genome genomeA) { //, HashMap<Integer, Integer> chromMap){

        int chromNb = 1;
        int[] newChromMap = new int[genomeA.getNumberOfGenes() + 1];
        for (int i = 1; i < genomeA.getNumberOfChromosomes(); ++i) {
            int[] chrom = genomeA.getChromosome(i).getGenes();
            for (int j = 0; j < chrom.length; ++j) {
                int geneL = chrom[j];
                newChromMap[Math.abs(geneL)] = chromNb;
            }
            ++chromNb;
        }
        return newChromMap;
    }

    /**
     * Tests if the components are on different chromosomes or not.
     * @param compMap the mapping of components to chromosomes
     * @param component 
     * @return <code>true</code> if they are on different chroms, <code>false</code> otherwise
     */
    private boolean testCompsOnDiffChroms(final int[] compMap, final ArrayList<Component> components) {
        int fstChromNb = -1;
        int scndChromNb = -1;
        int i = 1;
        int largeComp = 0;
        while (i < compMap.length - 1 && scndChromNb == -1) {
            largeComp = Math.abs(components.get(i).getEndIndex() - components.get(i).getStartIndex());
            if (largeComp >= 2) {
                if (fstChromNb == -1) {
                    fstChromNb = compMap[i];
                } else if (fstChromNb != compMap[i]) {
                    scndChromNb = compMap[i];
                }
            }
            ++i;
        }
        if (fstChromNb != scndChromNb && fstChromNb != -1 && scndChromNb != -1 && compMap.length > 1) {
            return true;
        }
        return false;
    }

    /**
     * Assistance method for generating the two cut positions and their extremities before entering the while loop.
     * @param start The position to cut
     * @param plusCappedGnom the plus capped genome
     * @param chromMap The chromosome map for genome A
     * @param genomeA Genome A
     * @param components the components, if second cut is in chromosome without components assign null!
     * @param geneInCompArray 
     * @param thrdIndex 
     * @return The four extremities involved in the translocation in an array. 0, 1 were together
     * and 2,3 before. Afterwards 0,3 and 1,2 should be together.
     */
    private int[] generateInvolvedExtremities(final int start, final int[] plusCappedGnom,
            final int[] chromMap, final Genome genomeA, final boolean[] geneInCompArray,
            byte[] geneAtCompBorderArray, int[] posDepthArray) {

        final int nbExtremities = 4;
        int[] involvedExtremities = new int[nbExtremities];
        involvedExtremities[0] = Utilities.getExtremity(plusCappedGnom[start] * 2, true);
        involvedExtremities[1] = Utilities.getExtremity(plusCappedGnom[start + 1] * 2, false);
        int chromNb = chromMap[Math.abs(plusCappedGnom[start])]; //chrom number can't change during while loop

        int[] chrom;
        boolean doesntFit = true;
        int cutChromNb = 0;
        chrom = genomeA.getChromosome(cutChromNb).getGenes();
        int i = 0;
        if (this.allGenesInComps) {
            if (cutChromNb < genomeA.getNumberOfChromosomes() - 1 && chromNb == cutChromNb) {
                chrom = genomeA.getChromosome(++cutChromNb).getGenes(); //must always work as there cant be only 1 chrom
            }
            involvedExtremities[2] = Utilities.getExtremity(chrom[0] * 2, true);
            involvedExtremities[3] = Utilities.getExtremity(chrom[1] * 2, false);
        } else {
            while (doesntFit) {
                if (cutChromNb < genomeA.getNumberOfChromosomes() - 1 && (chromNb == cutChromNb || i >= chrom.length - 2)) {
                    ++cutChromNb; //must always work as there cant be only 1 chrom
                    if (cutChromNb == chromNb) {
                        ++cutChromNb;
                    }
                    chrom = genomeA.getChromosome(cutChromNb).getGenes();
                    i = 0;
                }
                int geneL = chrom[i];
                int geneAbs;
                while (geneInCompArray[geneAbs = Math.abs(geneL)] && i < chrom.length - 2
                        && ((geneAtCompBorderArray[geneAbs] == 1 && i == 0 || //these 3 lines mean that gene is at 
                        geneAtCompBorderArray[geneAbs] == -1 && i == chrom.length - 1 || //a comp border & between 
                        (geneAtCompBorderArray[geneAbs] == 1 && i == 0 ||
                        geneAtCompBorderArray[geneAbs] == -1 && i == chrom.length - 1)
                        && posDepthArray[geneAbs] > 1) || // 2 comps at top level in tree there can be a cut. 
                        geneAtCompBorderArray[geneAbs] == 0)) { //means comp is not directly below root of tree = at least in scnd layer

                    geneL = chrom[++i];
                }
                if (i == chrom.length - 1) {
                    doesntFit = true;
                } else {
                    involvedExtremities[2] = Utilities.getExtremity(geneL * 2, true);
                    involvedExtremities[3] = Utilities.getExtremity(chrom[i + 1] * 2, false);
                    doesntFit = false;
                }
            }
        }


        return involvedExtremities;
    }

    /**
     * Generates an integer array containing a mapping for each component
     * index in the component array to the chromosome number the component is 
     * located on.
     * @param chromMap the chromosome map for the genes in genome A
     * @param components the component array
     * @param plusCappedGnom the plus capped genome A
     * @return the component to chromosome mapping array
     */
    private int[] generateCompMap(int[] chromMap,
            ArrayList<Component> components, int[] plusCappedGnom) {

        int size = components.size();
        int[] compMap = new int[size];
        for (int i = 0; i < size; ++i) {
            final int geneL = plusCappedGnom[components.get(i).getStartIndex()];
            compMap[i] = chromMap[Math.abs(geneL)];
        }
        return compMap;
    }

    /**
     * Checks if there exists a component longer than 2 elements.
     * @param components component array
     * @return true if there is a component longer than 2 elements (at least 3 elements)
     */
    public boolean checkCompLength(ArrayList<Component> components) {
        for (int i = 0; i < components.size(); ++i) {
            if (Math.abs(components.get(i).getEndIndex() - components.get(i).getStartIndex()) >= 2) {
                return true;
            }
        }
        return false;
    }
}
