package de.unibi.cebitec.gi.unimog.datastructure;

import java.util.ArrayList;
import java.util.List;

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
 * This class provides the data structure for the Adjacency Graph of two
 * genomes. It contains methods to save the genomes in tables which
 * allow fast computation of the numbers of odd/even paths and cycles in
 * the graph. The computation of odd/even paths and cycles is also
 * carried out here directly.
 * 
 * 
 * DCJ distance: N-(C+I(2)
 */
public class AdjacencyGraph {

    private final int nbOfGenes;
    private int[] adjacenciesGenome1;
    private int[] adjacenciesGenome2; // needed 4 calculation & sorting
    private boolean[] belongToEvenP;
    private int numberOfCycles = 0;
    private int numberOfOddPaths = 0;
    private int numberOfEvenPaths = 0;
    private List<Integer> compDistances;

    /**
     * Constructor of a new instance. Both genomes MUST contain the same number
     * of genes! 
     * @param genome1 First genome to evaluate in the graph
     * @param genome2 Second genome to evaluate in the graph
     */
    public AdjacencyGraph(final Genome genome1, final Genome genome2) {
        this.compDistances = new ArrayList<Integer>();
        this.nbOfGenes = genome1.getNumberOfGenes();
        final int genomeLength = 2 * genome1.getNumberOfGenes() + 1; // 2N +1 
        this.adjacenciesGenome1 = this.parseAdjacencies(genome1, genomeLength);
        this.adjacenciesGenome2 = this.parseAdjacencies(genome2, genomeLength);
        this.calculatePathsAndCycles();
    }

    /**
     * Constructor for cloning.
     * @param nbOfGenes nbgenes
     * @param adjG1 adjg1
     * @param adjG2 adjg2
     * @param nbCycles nbcycles
     * @param nbOddPaths nb odd paths
     * @param nbEvenPath nb even paths
     * @param belongToEvenP belonging to even path of an extremity
     */
    private AdjacencyGraph(final int nbOfGenes, final int[] adjG1, final int[] adjG2, final int nbCycles,
            final int nbOddPaths, final int nbEvenPath, final boolean[] belongToEvenP) {
        this.nbOfGenes = nbOfGenes;
        this.adjacenciesGenome1 = adjG1;
        this.adjacenciesGenome2 = adjG2;
        this.numberOfCycles = nbCycles;
        this.numberOfOddPaths = nbOddPaths;
        this.numberOfEvenPaths = nbEvenPath;
        this.belongToEvenP = belongToEvenP;
    }

    /**
     * Parses the adjacency graph. The result contains at each index (which represents
     * a head or tail of a gene) its neighbor and if there it is a telomere it contains
     * itself.
     * @param genome Genome to parse
     * @param genomeLength Length of the genome as int value
     */
    private int[] parseAdjacencies(final Genome genome, int genomeLength) {

        // genome: -1 3 4|  2 5| -6,-7| array from 1-14 for 7 genes!
        // head = odd, tail = even, entry 0 is empty!
        final int[] genomeAdjacencies = new int[genomeLength];

        for (int i = 0; i < genome.getNumberOfChromosomes(); ++i) {
            int[] chromosome = genome.getChromosome(i).getGenes();
            int length = chromosome.length;
            int geneNumberLeft;
            int geneNumberRight;

            //boundary on list of genes
            //Part for processing telomeres, thus we choose "false" as input for "abstractGene"
            geneNumberLeft = this.abstractGene(chromosome[0], false);
            geneNumberRight = this.abstractGene(chromosome[length - 1], true); //Right telomere is left in adjacency: true

            if (genome.getChromosome(i).isCircular()) {
                genomeAdjacencies[geneNumberLeft] = geneNumberRight; // save
                genomeAdjacencies[geneNumberRight] = geneNumberLeft;
                // can already save telomere entries
            } else {
                genomeAdjacencies[geneNumberLeft] = geneNumberLeft;
                genomeAdjacencies[geneNumberRight] = geneNumberRight;
            }

            for (int j = 1; j < chromosome.length; ++j) {

                geneNumberLeft = this.abstractGene(chromosome[j - 1], true);
                geneNumberRight = this.abstractGene(chromosome[j], false);

                genomeAdjacencies[geneNumberLeft] = geneNumberRight; // save
                genomeAdjacencies[geneNumberRight] = geneNumberLeft;
            }
        }
        return genomeAdjacencies;
    }

    /**
     * Abstracts a gene to a positive value of 2*gene or 2*gene-1. If the gene is a left telomere
     * then its position is right in the adjacency and left for a right telomere. For all inner 
     * adjacencies the left gene is on the left side of the adjacency and therefore a head if
     * the gene is in reverse orientation and a tail otherwise. The same holds for the right gene
     * of an adjacency just in the other way round.
     * @param gene the gene to be abstracted
     * @param left if the gene is the left or right one in adjacency
     * @return the abstracted gene
     */
    public int abstractGene(final int gene, final boolean left) {
        /* Entry is either right, thus a tail if > 0 or left, thus a tail if < 0 */
        if (gene <= 0 && !left || gene >= 0 && left) {
            return 2 * Math.abs(gene);
        } else { /* Entry is either right, thus a tail if < 0 or left, thus a tail if > 0 */
            return 2 * Math.abs(gene) - 1;
        }
    }

    /**
     * Calculates the number of paths and cycles in the graph.
     * Additionally the array saving if an extremity belongs to an even path
     * is generated here. At first all values are false and if an extremity
     * belongs to an even path its entry turns to true.
     */
    private void calculatePathsAndCycles() {
        int edgesCount = 0;
        boolean[] visited = new boolean[this.adjacenciesGenome1.length];
        this.belongToEvenP = new boolean[this.adjacenciesGenome1.length];

        for (int i = 1; i < this.adjacenciesGenome1.length; ++i) {
            if (!visited[i]) {
                visited[i] = true; // i was now visited
                boolean choose1stGenome = false; // chooses target genome of current edge
                int extremityPath1 = i;
                int extremityPath2 = i;
                int nextExtremityPath1 = 0;
                int nextExtremityPath2 = 0;
                ++edgesCount;
                nextExtremityPath1 = this.adjacenciesGenome1[i]; // get next extremities
                nextExtremityPath2 = this.adjacenciesGenome2[i];

                while (!visited[nextExtremityPath1] && nextExtremityPath1 != extremityPath1) {
                    visited[nextExtremityPath1] = true;
                    ++edgesCount;
                    extremityPath1 = nextExtremityPath1;
                    if (choose1stGenome) { // chooses target genome of current edge
                        nextExtremityPath1 = this.adjacenciesGenome1[extremityPath1];
                        choose1stGenome = false;
                    } else {
                        nextExtremityPath1 = this.adjacenciesGenome2[extremityPath1];
                        choose1stGenome = true;
                    }
                } // path1 is finished, now walk along path2 if path1 was not a cycle
                if (extremityPath2 == nextExtremityPath1 && extremityPath1 != extremityPath2) {
                    // check if path1 is a cycle
                    this.compDistances.add((edgesCount-2)/2);
                    ++this.numberOfCycles;
                } else {
                    choose1stGenome = true;
                    while (nextExtremityPath2 != extremityPath2) {
                        visited[nextExtremityPath2] = true; // can't be circular anymore
                        ++edgesCount;
                        extremityPath2 = nextExtremityPath2;
                        if (choose1stGenome) {
                            nextExtremityPath2 = this.adjacenciesGenome1[extremityPath2];
                            choose1stGenome = false;
                        } else {
                            nextExtremityPath2 = this.adjacenciesGenome2[extremityPath2];
                            choose1stGenome = true;
                        }
                    }
                    if (edgesCount % 2 == 0) {
                        this.compDistances.add(edgesCount/2);
                        ++this.numberOfEvenPaths;

                        //update array saving true if an an extremity belongs to an even path
                        this.belongToEvenP[extremityPath2] = true;
                        if (choose1stGenome) {
                            nextExtremityPath2 = this.adjacenciesGenome1[extremityPath2];
                            choose1stGenome = false;
                        } else {
                            nextExtremityPath2 = this.adjacenciesGenome2[extremityPath2];
                            choose1stGenome = true;
                        }
                        while (nextExtremityPath2 != extremityPath2) {
                            extremityPath2 = nextExtremityPath2;
                            this.belongToEvenP[extremityPath2] = true;
                            if (choose1stGenome) {
                                nextExtremityPath2 = this.adjacenciesGenome1[extremityPath2];
                                choose1stGenome = false;
                            } else {
                                nextExtremityPath2 = this.adjacenciesGenome2[extremityPath2];
                                choose1stGenome = true;
                            }
                        }
                    } else {
                        this.compDistances.add((edgesCount-1)/2);
                        ++this.numberOfOddPaths;
                    }
                }
                edgesCount = 0;
            }
        }
    }

    /**
     * Returns the number of genes of the two genomes.
     * 
     * @return the nbOfGenes
     */
    public int getNbOfGenes() {
        return this.nbOfGenes;
    }

    /**
     * Returns the adjacency structure for genome1.
     * 
     * @return the adjacency structure for genome1.
     */
    public int[] getAdjacenciesGenome1() {
        return this.adjacenciesGenome1;
    }

    /**
     * Returns the adjacency structure for genome2.
     * 
     * @return the adjacency structure for genome2.
     */
    public int[] getAdjacenciesGenome2() {
        return this.adjacenciesGenome2;
    }

    /**
     * Returns the number of cycles in the adjacency graph defined by the two
     * genomes.
     * 
     * @return the numberOfCycles
     */
    public int getNumberOfCycles() {
        return this.numberOfCycles;
    }

    /**
     * Returns the number of odd paths in the adjacency graph defined by the two
     * genomes.
     * 
     * @return the numberOfOddPaths
     */
    public int getNumberOfOddPaths() {
        return this.numberOfOddPaths;
    }

    /**
     * Returns the number of even paths in the adjacency graph defined by the
     * two genomes.
     * 
     * @return the numberOfEvenPaths
     */
    public int getNumberOfEvenPaths() {
        return this.numberOfEvenPaths;
    }

    /**
     * Method for cloning an instance.
     * @return the cloned instance
     */
    @Override
    public AdjacencyGraph clone() {
        return new AdjacencyGraph(this.nbOfGenes, this.adjacenciesGenome1.clone(),
                this.adjacenciesGenome2.clone(), this.numberOfCycles, this.numberOfOddPaths,
                this.numberOfEvenPaths, this.belongToEvenP.clone());
    }

    /**
     * Returns the array containing if an extremity belongs to an even path.
     * 
     * @return the belonging To an Even Path
     */
    public boolean[] getBelongToEvenPath() {
        return this.belongToEvenP;
    }

    /**
     * @return The single distances of each component as list.
     */
    public List<Integer> getCompDistances() {
        return compDistances;
    }
    
    
}