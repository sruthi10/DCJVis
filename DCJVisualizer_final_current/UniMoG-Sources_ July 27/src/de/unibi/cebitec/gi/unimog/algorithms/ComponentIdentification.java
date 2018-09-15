package de.unibi.cebitec.gi.unimog.algorithms;

import java.util.ArrayList;

import de.unibi.cebitec.gi.unimog.datastructure.AdditionalDataHPDistance;
import de.unibi.cebitec.gi.unimog.datastructure.Chromosome;
import de.unibi.cebitec.gi.unimog.datastructure.Component;
import de.unibi.cebitec.gi.unimog.datastructure.Data;
import de.unibi.cebitec.gi.unimog.datastructure.Genome;
import de.unibi.cebitec.gi.unimog.datastructure.StackInt;

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
 * @author -Rolf Hilker- This class provides the algorithms necessary for the identification of components of two
 *         multichromosomal genomes.
 */
public class ComponentIdentification {
    // comps länge 2 nicht mehr ausschließen, sondern erst nach baumerstellung ausschließen wg. chains!!!

    private ArrayList<Component> components;
    private int[]                greatestPrecursors;
    private int[]                smallestPrecursors;
    private int[]                greatestSuccessors;
    private int[]                smallestSuccessors;
    private int[]                chromosomeNbs;
    private boolean[]            belongToEvenP;

    // private int[] signs;

    /**
     * Constructor for creating an instance of the component identification algorithms.
     * 
     * @param data
     *            the data object containing the adjacency graph and both genomes
     * @param additionalHPData
     *            additional data for HP distance.
     */
    public ComponentIdentification(final Data data, final AdditionalDataHPDistance additionalHPData) {

        this.identifyComponents(data, additionalHPData);
    }

    /**
     * Runs the preprocessing and component identification algorithms and returns the result as an ArrayList of all
     * identified components.
     * 
     * @param genomeA
     *            first genome
     * @param genomeB
     *            second genome
     * @param additionalHPData
     *            additional data for HP distance.
     * @return An ArrayList of all identified components.
     */
    private void identifyComponents(final Data data, final AdditionalDataHPDistance additionalHPData) {

        final Genome genomeA = data.getGenomeA();
        final Genome genomeB = data.getGenomeB();
        this.belongToEvenP = data.getAdjGraph().getBelongToEvenPath();

        this.chromosomeNbs = this.generateChromNumbers(genomeA, genomeB);
        final int[] chromNbsGene = this.calcChromNbsGene(genomeB);
        final int nbOfGenes = genomeA.getNumberOfGenes();
        this.plusCappedPreprocessing(additionalHPData.getGenomeCappedPlusArray(), nbOfGenes);
        this.minusCappedPreprocessing(additionalHPData.getGenomeCappedMinusArray(), nbOfGenes);
        // this.generateSignsArray(additionalHPData.getGenomeCappedPlusArray());
        this.components = this.compIdentificationPhase1(additionalHPData.getGenomeCappedPlusArray(),
            this.greatestPrecursors, this.smallestPrecursors, nbOfGenes, this.chromosomeNbs, chromNbsGene);
        final ArrayList<Component> phase2Comps = this.compIdentificationPhase2(
            additionalHPData.getGenomeCappedMinusArray(), this.greatestSuccessors, this.smallestSuccessors, nbOfGenes,
            this.chromosomeNbs, chromNbsGene);
        this.components.addAll(phase2Comps);
        this.components.trimToSize();

    }

    // /**
    // * Generates the array which holds for each entry in the plus capped genome
    // * array if the two consecutive elements have the same sign of not.
    // * @param genomeCappedPlusArray the array
    // */
    // private void generateSignsArray(int[] genomeCappedPlusArray) {
    //
    // this.signs = new int[genomeCappedPlusArray.length];
    // for (int i=0; i<genomeCappedPlusArray.length-1; ++i){
    // if (genomeCappedPlusArray[i] >= 0 && genomeCappedPlusArray[i+1] >= 0){
    // this.signs[i] = ComponentIdentification.PLUS;
    // } else
    // if (genomeCappedPlusArray[i] < 0 && genomeCappedPlusArray[i+1] < 0){
    // this.signs[i] = ComponentIdentification.MINUS;
    // } else {
    // this.signs[i] = ComponentIdentification.NEUTRAL;
    // }
    // }
    // }

    // /**
    // * Function page 74 in dissertation.
    // * @param sign1 first sign
    // * @param sign2 second sign of neighbour
    // * @return the sign
    // */
    // private int signFunction(final int sign1, final int sign2){
    // if (sign1 == sign2){
    // return sign1;
    // } else {
    // return ComponentIdentification.NEUTRAL;
    // }
    // }

    /**
     * Initializes the array which stores the to genome A corresponding chromosome number of each gene in genome B. 2*N
     * Calculations -> each gene is visited twice.
     * 
     * @param genomeA
     *            First genome with unsorted genes
     * @param genomeB
     *            Second genome with sorted genes (1-n)
     * @return Array of chromosome numbers
     */
    private int[] generateChromNumbers(final Genome genomeA, final Genome genomeB) {

        final AdditionalDataHPDistance additionalHPDataA = new AdditionalDataHPDistance(genomeA);
        final int[] cappedGenome = additionalHPDataA.getGenomeCappedPlusArray();
        final int nbOfGenes = genomeA.getNumberOfGenes();
        final int nbOfChromA = genomeA.getNumberOfChromosomes();
        final int cappedGenomeLength = nbOfGenes + nbOfChromA * 2;
        final int[] bufferArray = new int[nbOfGenes + 1];
        final int[] chromNumbers = new int[cappedGenomeLength];

        // For each gene in B save the corresponding chromosome number
        // at position array[gene] in the bufferArray
        for (int i = 0; i < genomeB.getNumberOfChromosomes(); i++) {
            int[] genes = genomeB.getChromosome(i).getGenes();
            for (int j = 0; j < genes.length; j++) { // caps included but not needed in this step
                bufferArray[Math.abs(genes[j])] = i + 1;
            }
        }

        // Assign for a gene in A it's chromosome number in B.
        for (int i = 0; i < additionalHPDataA.getGenomeCappedPlusArray().length; ++i) {
            if (cappedGenome[i] != 0 && cappedGenome[i] != nbOfGenes + 1) {
                chromNumbers[i] = bufferArray[Math.abs(cappedGenome[i])];
            } else {
                if (cappedGenome[i] == 0) { // front cap
                    chromNumbers[i] = bufferArray[Math.abs(cappedGenome[i + 1])];
                } else { // back cap
                    chromNumbers[i] = bufferArray[Math.abs(cappedGenome[i - 1])];
                }
            }
        }
        return chromNumbers;
    }

    /**
     * Preprocessing of the plus capped genome for component identification. The Stacks M & m are created, which contain
     * possible start positions for a component. The preprocessing procedure is finding for each gene the greatest and
     * smallest precursor in the plus capped genome.
     * 
     * @param plusCappedGenomeAArray
     *            The plus capped array of genes of genome A.
     * @param nbOfGenes
     *            the number of genes of the pre-processed genome
     * @return Returns a pair containing the greatestPrecursor array as first element and the smallestPrecursor array as
     *         second element.
     */
    private void plusCappedPreprocessing(final int[] plusCappedGenomeAArray, final int nbOfGenes) {
        // Pair<int[], int[]>
        final int cappedGenomeLength = plusCappedGenomeAArray.length;

        // preprocessing for a plus capped genome
        this.greatestPrecursors = new int[cappedGenomeLength];
        this.smallestPrecursors = new int[cappedGenomeLength];
        // l = length of genome + caps = this.cappedGenomeLength
        // for P+ string!
        for (int i = 0; i < cappedGenomeLength; i++) {
            final int nbOfGenesPlus1 = nbOfGenes + 1;
            this.greatestPrecursors[i] = nbOfGenesPlus1;
            this.smallestPrecursors[i] = 0;
            final StackInt m1 = new StackInt(nbOfGenesPlus1);
            final StackInt m2 = new StackInt(nbOfGenesPlus1);
            m1.push(nbOfGenesPlus1);
            m2.push(0);
            ++i;

            while (plusCappedGenomeAArray[i] < nbOfGenesPlus1) {
                final int prevValue = Math.abs(plusCappedGenomeAArray[i - 1]);
                final int currentValue = Math.abs(plusCappedGenomeAArray[i]);
                // Compute the M[i] aka greatestPrecursos[i]
                if (prevValue > currentValue) {
                    m1.push(prevValue);
                } else {
                    while (m1.peek() < currentValue) {
                        m1.pop();
                    }
                }
                this.greatestPrecursors[i] = m1.peek();

                // Compute the m[i] aka smallestPrecursors[i]
                if (prevValue < currentValue) {
                    m2.push(prevValue);
                } else {
                    while (m2.peek() > currentValue && m2.peek() != nbOfGenesPlus1) {
                        m2.pop();
                    }
                }
                this.smallestPrecursors[i] = m2.peek();
                ++i;
            }
            this.greatestPrecursors[i] = nbOfGenesPlus1;
            this.smallestPrecursors[i] = 0;
        }
        // return new Pair<int[], int[]>(greatestPrecursors,
        // smallestPrecursors);
    }

    /**
     * Preprocessing of the minus capped genome for component identification. The Stacks M & m are created, which
     * contain possible start positions for a component. The preprocessing procedure here is finding for each gene the
     * greatest and smallest successor in the minus capped genome.
     * 
     * @param minusCappedGenomeAArray
     *            The minus capped array of genes of genome A.
     * @param nbOfGenes
     *            The number of genes of the pre-processed genome
     * @return Returns a pair containing the greatestSuccessor array as first element and the smallestSuccessor array as
     *         second element.
     */
    private void minusCappedPreprocessing(final int[] minusCappedGenomeAArray, final int nbOfGenes) {

        final int cappedGenomeLength = minusCappedGenomeAArray.length;

        this.greatestSuccessors = new int[cappedGenomeLength];
        this.smallestSuccessors = new int[cappedGenomeLength];
        // l = length of genome + caps = this.cappedGenomeLength
        // for P+ string!

        for (int i = cappedGenomeLength - 1; i > 0; --i) {
            final int nbOfGenesPlus1 = nbOfGenes + 1;
            this.greatestSuccessors[i] = nbOfGenesPlus1;
            this.smallestSuccessors[i] = 0;
            final StackInt m1 = new StackInt(nbOfGenesPlus1);
            final StackInt m2 = new StackInt(nbOfGenesPlus1);
            m1.push(nbOfGenesPlus1);
            m2.push(0);
            --i;

            while (i >= 0 && minusCappedGenomeAArray[i] <= nbOfGenes) {
                final int consecValue = Math.abs(minusCappedGenomeAArray[i + 1]);
                final int currentValue = Math.abs(minusCappedGenomeAArray[i]);
                // Compute the M[i] aka greatestSuccessors[i]
                if (consecValue > currentValue) {
                    m1.push(consecValue);
                } else {
                    while (m1.peek() < currentValue && m1.peek() != nbOfGenesPlus1) {
                        m1.pop();
                    }
                }
                this.greatestSuccessors[i] = m1.peek();

                // Compute the m[i] aka smallestSuccessors[i]
                if (consecValue < currentValue && currentValue != nbOfGenesPlus1) {
                    m2.push(consecValue);
                } else {
                    while (m2.peek() > currentValue && m2.peek() != 0 || currentValue == nbOfGenesPlus1
                        && m2.peek() != 0) {
                        m2.pop();
                    }
                }
                this.smallestSuccessors[i] = m2.peek();
                --i;
            }
        }
        // return new Pair<int[], int[]>(greatestSuccessors,
        // smallestSuccessors);
    }

    /**
     * Phase one of the component identification. Here directed components of type 1 (real) ,3 (semi-real) ,4
     * (semi-real) and reverse components of type 7 (semi-real) are identified.
     * 
     * @param plusCapGnom
     *            plusCappedGenome
     * @param greatPrec
     *            greatest precursors
     * @param smallPrec
     *            smallest precursors
     * @param nbGenes
     *            number of genes
     * @param chromNbs
     *            chromosome numbers in B associated to the index in capped genome
     * @param chromNbsGene
     *            chromosome numbers in B associated to the genes
     * @return An ArrayList of Components identified in this step.
     */
    private ArrayList<Component> compIdentificationPhase1(final int[] plusCapGnom, final int[] greatPrec,
        final int[] smallPrec, final int nbGenes, final int[] chromNbs, final int[] chromNbsGene) {

        /*
         * oriD & oriR save for the components of type I-III and V-VII if the elements of the component have both signs.
         * Starting with assumption that all elements are positive for direct or negative for reverse components. Then
         * in each step it is updated and if a minus is detected in direct comp or a plus reversed comp then the last
         * potential starting position is updated and if the starting pos is popped from S1/S2 the information of the
         * new start position & the popped one is put together by oriD[topS1] |= oriD[startPos] Remember that a
         * component containing other components is oriented or not according to its blocks and the associated
         * permutation. The permutation of the blocks needs to have pos and neg sign for an oriented component! For
         * components of type IV and VIII its more complicated: even path array added
         */

        final ArrayList<Component> components = new ArrayList<Component>();
        final int size = plusCapGnom.length;
        final int nPlus1 = nbGenes + 1;
        final StackInt s1 = new StackInt(size); // stacks containing the potential start positions
        final StackInt s2 = new StackInt(size);
        final int[] minDir = new int[size]; // contain the minimum or maximum between current index
        final int[] maxDir = new int[size]; // i and a potential start position
        final int[] minRev = new int[size];
        final int[] maxRev = new int[size];
        final int[] oriD = new int[size]; // saves if comp is oriented due to unequal signs in comp
        final int[] oriR = new int[size];
        final int[] chrD = new int[size]; // for checking if all genes are on same chrom in 1 comp
        final int[] chrR = new int[size];
        final int[] grey = new int[size]; // contains for a chromosome if there is a grey component on it
        int startPos = 0;
        final int falsch = 0;
        final int wahr = 1;

        for (int i = 0; i < size; ++i) {
            s1.emptyStack();
            s2.emptyStack();
            s1.push(i);
            s2.push(i);
            minDir[i] = nbGenes;
            maxDir[i] = 0;
            minRev[i] = nbGenes;
            maxRev[i] = 0;
            oriD[i] = falsch;
            oriR[i] = falsch;
            chrD[i] = chromNbs[i];
            chrR[i] = chromNbs[i];
            int currentGene = 0;
            int topS1 = i;
            int topS2 = i;
            ++i;

            // in a chromosome
            while (currentGene != nPlus1) {

                // Update minima and maxima
                topS1 = s1.peek();
                topS2 = s2.peek();
                currentGene = Math.abs(plusCapGnom[i]);
                minDir[i] = currentGene;
                minDir[topS1] = Math.min(minDir[topS1], currentGene);
                maxDir[i] = currentGene;
                maxDir[topS1] = Math.max(maxDir[topS1], currentGene);
                minRev[i] = currentGene;
                minRev[topS2] = Math.min(minDir[topS2], currentGene);
                maxRev[i] = currentGene;
                maxRev[topS2] = Math.max(maxDir[topS2], currentGene);

                // Find direct components of type I and III [i-j], [0-j]
                while (currentGene < Math.abs(plusCapGnom[startPos = topS1]) || currentGene > greatPrec[startPos]) {
                    s1.pop();
                    topS1 = s1.peek();
                    // update signs array
                    // this.signs[topS1] = this.signFunction(this.signs[topS1], this.signs[startPos]);
                    // Update minima and maxima
                    minDir[topS1] = Math.min(minDir[topS1], minDir[startPos]);
                    maxDir[topS1] = Math.max(maxDir[topS1], maxDir[startPos]);
                    oriD[topS1] |= oriD[startPos]; // for shifting information to the left, when a startpos is removed
                    if (chrD[topS1] != chrD[startPos]) {
                        chrD[topS1] = -1;
                    }
                }
                // report component
                if (plusCapGnom[i] >= 0
                    && currentGene == maxDir[startPos]
                    && chrD[startPos] == chromNbs[i]
                    && ((plusCapGnom[startPos] == 0 && maxDir[startPos] - minDir[startPos] == i - startPos - 1 && (minDir[startPos] == 1 || chromNbsGene[minDir[startPos]] != chromNbsGene[minDir[startPos] - 1])) || (plusCapGnom[startPos] != 0 && maxDir[startPos]
                        - minDir[startPos] == i - startPos))) {
                    int type = 1;
                    if (plusCapGnom[startPos] == 0) {
                        grey[chromNbs[i]] = wahr;
                        type = 3;
                    }
                    if (oriD[startPos] == falsch) {
                        components.add(new Component(startPos, i, type, false));
                        // System.out.println("Start: "+startPos+", i: "+i+", type: "+type+", oriented: "+false);
                    } else {
                        components.add(new Component(startPos, i, type, true));
                    }

                    oriD[topS1] = falsch;
                    // this.signs[startPos] = ComponentIdentification.PLUS;
                }

                // Find components of type VII [-(N+1)], [-i]
                while ((currentGene > Math.abs(plusCapGnom[startPos = topS2]) || currentGene < smallPrec[startPos])
                    && plusCapGnom[startPos] != 0) {
                    s2.pop();
                    topS2 = s2.peek();
                    // update signs array
                    // this.signs[topS2] = this.signFunction(this.signs[topS2], this.signs[startPos]);
                    // Update minima and maxima
                    minRev[topS2] = Math.min(minRev[topS2], minRev[startPos]);
                    maxRev[topS2] = Math.max(maxRev[topS2], maxRev[startPos]);
                    oriR[topS2] |= oriR[startPos];
                    if (chrR[topS2] != chrR[startPos]) {
                        chrR[topS2] = -1;
                    }
                }

                // report component
                if (plusCapGnom[i] <= 0
                    && currentGene == minRev[startPos]
                    && chrR[startPos] == chromNbs[i]
                    && ((plusCapGnom[startPos] == 0 && maxRev[startPos] - minRev[startPos] == i - startPos - 1 && (maxRev[startPos] == nbGenes || chromNbsGene[maxRev[startPos] + 1] != chromNbsGene[maxRev[startPos]])) || (plusCapGnom[startPos] != 0 && maxRev[startPos]
                        - minRev[startPos] == i - startPos))) {

                    if (plusCapGnom[startPos] == 0) {
                        if (oriR[startPos] == falsch) {
                            components.add(new Component(startPos, i, 7, false));
                        } else {
                            components.add(new Component(startPos, i, 7, true));
                        }
                        grey[chromNbs[i]] = wahr;
                    }
                    oriR[topS2] = falsch;
                    // this.signs[startPos] = ComponentIdentification.MINUS;
                }

                // Update stacks
                if (plusCapGnom[i] >= 0) {
                    s1.push(i);
                    topS1 = s1.peek();
                    chrD[topS1] = chromNbs[i];
                    if (chrR[topS2] != chromNbs[i]) { // if gene is on other chrom no comp can be here!
                        chrR[topS2] = -1;
                    }
                } else {
                    s2.push(i);
                    topS2 = s2.peek();
                    chrR[topS2] = chromNbs[i];
                    if (chrD[topS1] != chromNbs[i]) {
                        chrD[topS1] = -1;
                    }
                }

                // update marks
                oriD[topS1] = plusCapGnom[i] >= 0 ? falsch : wahr;
                oriR[topS2] = plusCapGnom[i] >= 0 ? wahr : falsch;

                ++i;
                currentGene = Math.abs(plusCapGnom[i]);
            }

            // At chromosome end test for whole chrom = grey component
            // Find components of type IV
            while (plusCapGnom[startPos = topS1] != 0) {
                s1.pop();
                topS1 = s1.peek();
                // update signs array
                // this.signs[topS1] = this.signFunction(this.signs[topS1], this.signs[startPos]);
                // Update minima and maxima
                minDir[topS1] = Math.min(minDir[topS1], minDir[startPos]);
                maxDir[topS1] = Math.max(maxDir[topS1], maxDir[startPos]);
                if (chrD[topS1] != chrD[startPos]) {
                    chrD[topS1] = -1;
                }
            }
            if (plusCapGnom[startPos + 1] > 0 // 1. vorzeichen ein +, länger als 2, bedingung für länge, gleiches chrom
                                              // alle g,
                && maxDir[startPos] - minDir[startPos] == i - startPos - 2 // linkes & rechts chrom ende
                && chrD[startPos] == chromNbs[i] // es gibt nicht schon ne comp am comp start, ist nicht sortiert
                && (minDir[startPos] == 1 || chromNbsGene[minDir[startPos]] != chromNbsGene[minDir[startPos] - 1])
                && (maxDir[startPos] == nbGenes || chromNbsGene[maxDir[startPos]] != chromNbsGene[maxDir[startPos] + 1])
                && grey[chromNbs[startPos]] == falsch) {
                int extremity1 = this.getExtremity(plusCapGnom[startPos + 1], true, true);
                int extremity2 = this.getExtremity(plusCapGnom[i - 1], false, true);
                if (oriD[startPos] == wahr || this.belongToEvenP[extremity1] && this.belongToEvenP[extremity2]) { // hier
                                                                                                                  // noch
                                                                                                                  // path
                                                                                                                  // check
                                                                                                                  // einbauen
                    components.add(new Component(startPos, i, 4, true));
                } else {
                    components.add(new Component(startPos, i, 4, false));
                }
                // this.signs[startPos] = ComponentIdentification.PLUS;
            }
        }
        components.trimToSize();
        return components;
    }

    /**
     * Phase two of the component identification. Here directed components of type 2 (semi-real) and reversed components
     * of type 5 (real), 6 (semi-real) and 8 (semi-real) are identified by going from right to left through the minus
     * capped genome.
     * 
     * @param minusCapGnom
     *            minusCappedGenome
     * @param greatSucc
     *            greatest successors
     * @param smallSucc
     *            smallest successors
     * @param nbGenes
     *            number of genes
     * @param chromNbs
     *            chromosome numbers in B
     * @param chromNbsGene
     *            chromosome numbers in B in gene order
     * @return An ArrayList of Components identified in this step.
     */
    private ArrayList<Component> compIdentificationPhase2(final int[] minusCapGnom, final int[] greatSucc,
        final int[] smallSucc, final int nbGenes, final int[] chromNbs, final int[] chromNbsGene) {

        final ArrayList<Component> components = new ArrayList<Component>();
        final int size = minusCapGnom.length;
        final int nPlus1 = nbGenes + 1;
        final StackInt s1 = new StackInt(size);
        final StackInt s2 = new StackInt(size);
        final int[] minDir = new int[size];
        final int[] maxDir = new int[size];
        final int[] minRev = new int[size];
        final int[] maxRev = new int[size];
        final int[] oriD = new int[size];
        final int[] oriR = new int[size];
        final int[] chrD = new int[size];
        final int[] chrR = new int[size];
        final int[] grey = new int[size];
        int startPos = 0;
        final int falsch = 0;
        final int wahr = 1;

        for (int i = size - 1; i > 0; --i) {
            s1.emptyStack();
            s2.emptyStack();
            s1.push(i);
            s2.push(i);
            minDir[i] = nbGenes;
            maxDir[i] = 0;
            minRev[i] = nbGenes;
            maxRev[i] = 0;
            oriD[i] = falsch;
            oriR[i] = falsch;
            chrD[i] = chromNbs[i];
            chrR[i] = chromNbs[i];
            int currentGene = 0;
            int topS1 = i;
            int topS2 = i;
            --i;

            // Within a chromosome
            while (currentGene != nPlus1) {

                // Update minima and maxima
                topS1 = s1.peek();
                topS2 = s2.peek();
                currentGene = Math.abs(minusCapGnom[i]);
                minDir[i] = currentGene;
                minDir[topS1] = Math.min(minDir[topS1], currentGene);
                maxDir[i] = currentGene;
                maxDir[topS1] = Math.max(maxDir[topS1], currentGene);
                minRev[i] = currentGene;
                minRev[topS2] = Math.min(minDir[topS2], currentGene);
                maxRev[i] = currentGene;
                maxRev[topS2] = Math.max(maxDir[topS2], currentGene);

                // Find components of type V and VI [-(i+j) till i], [-(i+j) till 0]
                while (currentGene < Math.abs(minusCapGnom[startPos = topS1]) || currentGene > greatSucc[startPos]) {
                    s1.pop();
                    topS1 = s1.peek();
                    // this.signs[topS1] = this.signFunction(this.signs[topS1], this.signs[startPos]);
                    // Update minima and maxima
                    minDir[topS1] = Math.min(minDir[topS1], minDir[startPos]);
                    maxDir[topS1] = Math.max(maxDir[topS1], maxDir[startPos]);
                    oriD[topS1] |= oriD[startPos];
                    if (chrD[topS1] != chrD[startPos]) {
                        chrD[topS1] = -1;
                    }
                }
                // report component
                if (minusCapGnom[i] <= 0
                    && currentGene == maxDir[startPos]
                    && chrD[startPos] == chromNbs[i]
                    && ((minusCapGnom[startPos] == 0 && maxDir[startPos] - minDir[startPos] == startPos - i - 1 && (minDir[startPos] == 1 || chromNbsGene[minDir[startPos]] != chromNbsGene[minDir[startPos] - 1])) || (minusCapGnom[startPos] != 0 && maxDir[startPos]
                        - minDir[startPos] == startPos - i))) {
                    int type = 5;
                    if (minusCapGnom[startPos] == 0) {
                        grey[chromNbs[i]] = wahr;
                        type = 6;
                    }

                    if (oriD[startPos] == falsch) {
                        components.add(new Component(i, startPos, type, false));
                    } else {
                        components.add(new Component(i, startPos, type, true));
                    }
                    oriD[topS1] = falsch;
                    // this.signs[i] = ComponentIdentification.MINUS;
                }

                // Find components of type I & II but only report II: [i till N+1]
                while ((currentGene > Math.abs(minusCapGnom[startPos = topS2]) || currentGene < smallSucc[startPos])
                    && minusCapGnom[startPos] != 0) {
                    s2.pop();
                    topS2 = s2.peek();
                    // update signs array
                    // this.signs[topS2] = this.signFunction(this.signs[topS2], this.signs[startPos]);
                    // Update minima and maxima
                    minRev[topS2] = Math.min(minRev[topS2], minRev[startPos]);
                    maxRev[topS2] = Math.max(maxRev[topS2], maxRev[startPos]);
                    oriR[topS2] |= oriR[startPos];
                    if (chrR[topS2] != chrR[startPos]) {
                        chrR[topS2] = -1;
                    }
                }

                // report component
                if (minusCapGnom[i] >= 0
                    && currentGene == minRev[startPos]
                    && chrR[startPos] == chromNbs[i]
                    && ((minusCapGnom[startPos] == 0 && maxRev[startPos] - minRev[startPos] == startPos - i - 1 && (maxRev[startPos] == nbGenes || chromNbsGene[maxRev[startPos] + 1] != chromNbsGene[maxRev[startPos]])) || (minusCapGnom[startPos] != 0 && maxRev[startPos]
                        - minRev[startPos] == startPos - i))) {

                    if (minusCapGnom[startPos] == 0) {
                        if (oriR[startPos] == falsch) {
                            components.add(new Component(i, startPos, 2, false));
                        } else {
                            components.add(new Component(i, startPos, 2, true));
                        }
                        grey[chromNbs[i]] = wahr;
                    }
                    oriR[topS2] = falsch;
                    // this.signs[i] = ComponentIdentification.PLUS;
                }

                // Update stacks
                if (minusCapGnom[i] <= 0) {
                    s1.push(i);
                    topS1 = s1.peek();
                    chrD[topS1] = chromNbs[i];
                    if (chrR[topS2] != chromNbs[i]) {
                        chrR[topS2] = -1;
                    }
                } else {
                    s2.push(i);
                    topS2 = s2.peek();
                    chrR[topS2] = chromNbs[i];
                    if (chrD[topS1] != chromNbs[i]) {
                        chrD[topS1] = -1;
                    }
                }

                // update marks
                oriD[topS1] = minusCapGnom[i] >= 0 ? wahr : falsch;
                oriR[topS2] = minusCapGnom[i] >= 0 ? falsch : wahr;

                --i;
                currentGene = Math.abs(minusCapGnom[i]);
            }

            // At chromosome end test for whole chrom = grey component
            // Find components of type VIII
            while (minusCapGnom[startPos = topS1] != 0) {

                s1.pop();
                topS1 = s1.peek();
                // this.signs[topS1] = this.signFunction(this.signs[topS1], this.signs[startPos]);
                // Update minima and maxima
                minDir[topS1] = Math.min(minDir[topS1], minDir[startPos]);
                maxDir[topS1] = Math.max(maxDir[topS1], maxDir[startPos]);
                if (chrD[topS1] != chrD[startPos]) {
                    chrD[topS1] = -1;
                }
            } // report component
            if (minusCapGnom[startPos - 1] < 0
                && maxDir[startPos] - minDir[startPos] == startPos - i - 2
                && chrD[startPos] == chromNbs[i]
                && (minDir[startPos] == 1 || chromNbsGene[minDir[startPos]] != chromNbsGene[minDir[startPos] - 1])
                && (maxDir[startPos] == nbGenes || chromNbsGene[maxDir[startPos]] != chromNbsGene[maxDir[startPos] + 1])
                && grey[chromNbs[startPos]] == falsch) {

                int extremity1 = this.getExtremity(minusCapGnom[i + 1], true, false);
                int extremity2 = this.getExtremity(minusCapGnom[startPos - 1], false, false);
                if (oriD[startPos] == wahr || this.belongToEvenP[extremity1] && this.belongToEvenP[extremity2]) {
                    components.add(new Component(i, startPos, 8, true));
                } else {
                    components.add(new Component(i, startPos, 8, false));
                }
                // this.signs[i] = ComponentIdentification.MINUS;
            }
        }
        components.trimToSize();
        return components;
    }

    /**
     * Returns the extremity for a given start end endposition gene for getting the orientation for components of type
     * IV and VIII.
     * 
     * @param gene
     *            the gene whose adjacency is needed
     * @param startPos
     *            true if it is a start position of the component
     * @param direct
     *            true if it is a component of type IV = direct component
     * @return the extremity
     */
    private int getExtremity(final int gene, final boolean startPos, final boolean direct) {
        if (startPos && direct || !startPos && !direct) {
            return (Math.abs(gene)) * 2 - 1;
        }
        return (Math.abs(gene)) * 2;
    }

    // /**
    // * Checks for a given capped genome and a start and end position if
    // * the component is already sorted.
    // * @param cappedGenome array containing a capped genome
    // * @param startPos the conjectured start position of the component
    // * @param endPos the conjectured end position of the component
    // * @return true if it is a sorted component & false if not
    // */
    // private boolean checkSorted(int[] cappedGenome, int startPos, int endPos) {
    // int lastvalue = cappedGenome[startPos+1];
    // for (int i=startPos+2; i<endPos; ++i){
    // if (lastvalue+1 != cappedGenome[i]){
    // return false;
    // }
    // lastvalue = cappedGenome[i];
    // }
    // return true;
    // }

    /**
     * Returns the ci array which contains for each gene its chromosome number in genome B. The index is taken as gene
     * number and the entry its chromosome number.
     * 
     * @param genomeB
     *            Genome B
     * @return ci array
     */
    private int[] calcChromNbsGene(final Genome genomeB) {
        int[] ci = new int[genomeB.getNumberOfGenes() + 1];
        int count = 1;
        for (int i = 0; i < genomeB.getNumberOfChromosomes(); ++i) {
            Chromosome chrom = genomeB.getChromosome(i);
            for (int j = 0; j < chrom.getSize(); ++j) {
                ci[count++] = i;
            }
        }
        return ci;
    }

    /**
     * Returns the array which contains for each gene in A it's chromosome number in B.
     * 
     * @return chromosomeNb array which contains for each gene in A it's chromosome number in B.
     */
    public int[] getChromosomeNbs() {
        return this.chromosomeNbs;
    }

    /**
     * Returns the array of the greatest precursor of each gene. If there is no bigger precursor at plus capped genes
     * array[i] the nbOfGenes+1 is found at greatest precursors array[i].
     * 
     * @return The array with the greatest precursor for each gene.
     */
    public int[] getGreatestPrecursors() {
        return this.greatestPrecursors;
    }

    /**
     * Returns the array of the smallest precursor of each gene. If there is no smaller precursor at plus capped genes
     * array[i] 0 is found at smallest precursors array[i].
     * 
     * @return The array with the smallest precursor for each gene.
     */
    public int[] getSmallestPrecursors() {
        return this.smallestPrecursors;
    }

    /**
     * Returns the array of the greatest successor of each gene. If there is no greater successor at minus capped genes
     * array[i] 0 is found at greatest successors array[i].
     * 
     * @return The array with the greatest successor for each gene.
     */
    public int[] getGreatestSuccessors() {
        return this.greatestSuccessors;
    }

    /**
     * Returns the array of the smallest successor of each gene. If there is no smaller successor at plus capped genes
     * array[i] 0 is found at smallest successor array[i].
     * 
     * @return The array with the smallest successor for each gene.
     */
    public int[] getSmallestSuccessors() {
        return this.smallestSuccessors;
    }

    /**
     * Returns the identified components.
     * 
     * @return the identified components
     */
    public ArrayList<Component> getComponents() {
        return this.components;
    }
}
