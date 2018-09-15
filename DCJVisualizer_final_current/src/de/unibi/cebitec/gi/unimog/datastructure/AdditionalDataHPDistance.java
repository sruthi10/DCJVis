package de.unibi.cebitec.gi.unimog.datastructure;

import java.util.ArrayList;

import de.unibi.cebitec.gi.unimog.algorithms.Utilities;

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
 * Stores additional data needed for the calculation of the HP distance.
 * 
 * @author -Rolf Hilker-
 * 
 */
public class AdditionalDataHPDistance implements IAdditionalData {

    private int[] genomeCappedPlusArray;
    private int[] genomeCappedMinusArray;

    /**
     * Standard constructor for saving the additional data needed for
     * calculating the HP distance.
     * @param genome The genome whose additional HP distance data is needed.
     */
    public AdditionalDataHPDistance(final Genome genome) {
        this.capping(genome);
    }

    /**
     * Adds caps at the beginning and the end of each chromosome. The front cap
     * is represented by '0' and the back cap by 'N+1' if N is the length of the
     * chromosome.
     * @param genome The genome
     * @return A pair containing the plus capped genome as first element and the
     *         minus capped genome as second elements. Both as array of
     *         integers.
     */
    private void capping(final Genome genome) {
        final int nbOfGenes = genome.getNumberOfGenes();
        final ArrayList<Chromosome> genomeCappedPlus = new ArrayList<Chromosome>();
        final ArrayList<Chromosome> genomeCappedMinus = new ArrayList<Chromosome>();
        for (int i = 0; i < genome.getNumberOfChromosomes(); i++) {
            final int[] geneArray = genome.getChromosome(i).getGenes();
            final boolean isCircular = genome.getChromosome(i).isCircular();
            final int cap = nbOfGenes + 1;

            genomeCappedPlus.add(this.cappingHelp(0, cap, geneArray, isCircular));
            genomeCappedMinus.add(this.cappingHelp(-cap, 0, geneArray, isCircular));
        }

        this.genomeCappedPlusArray = Utilities.genomeToIntArray(genomeCappedPlus, nbOfGenes, true);
        this.genomeCappedMinusArray = Utilities.genomeToIntArray(genomeCappedMinus, nbOfGenes, true);
    }

    /**
     * Does the capping and reduces lines of code...
     */
    private Chromosome cappingHelp(final int firstElement, final int lastElement,
            final int[] chromosome, final boolean isCircular) {
        final int nbOfCaps = 2;
        final int size = chromosome.length;
        final int[] cappedChrom = new int[size + nbOfCaps];
        cappedChrom[0] = firstElement;
        System.arraycopy(chromosome, 0, cappedChrom, 1, size);
        cappedChrom[size + 1] = lastElement;
        return new Chromosome(cappedChrom, isCircular);
    }

    /**
     * Returns the genes of the plus capped genome as int[].
     * @return genomeCappedPlusArray The genes of the plus capped genome as int[].
     */
    public int[] getGenomeCappedPlusArray() {
        return this.genomeCappedPlusArray;
    }

    /**
     * Returns the genes of the minus capped genome as int[].
     * @return the genomeCappedMinusArray The genes of the minus capped genome as int[].
     */
    public int[] getGenomeCappedMinusArray() {
        return this.genomeCappedMinusArray;
    }
}
