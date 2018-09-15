package de.unibi.cebitec.gi.unimog.datastructure;

import de.unibi.cebitec.gi.unimog.algorithms.IntermediateGenomesGenerator;
import java.math.BigInteger;
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
 * Data holder for all data of a single comparison needed for
 * output purposes.
 * One object containing: i & j = the indexes of the associated genomes, 
 * An array with all distances for these genomes. An array with all 
 * intermediate genomes for the sorting of these genomes (if they should be sorted).
 * The lower bound of all dcj scenarios for the given genomes.
 */
public class DataOutput {

    private int i;
    private int j;
    private int[] distances;
    private BigInteger dcjLowerBound;
    private IntermediateGenomesGenerator[] intermedGenomes;

    /**
     * Constructor for an empty object.
     */
    public DataOutput() {
    }

    public DataOutput(final int i, final int j, final int[] distance) {
        this.i = i;
        this.j = j;
        this.distances = distance;
    }

    public DataOutput(final int i, final int j, final int[] distance,
            List<Integer> compDistances, final IntermediateGenomesGenerator[] intermedGenomes) {
        this.i = i;
        this.j = j;
        this.distances = distance;
        this.dcjLowerBound = null;
        this.intermedGenomes = intermedGenomes;

    }

    /**
     * Sets the incides of this genome comparison according to the genome indices
     * in the genome array.
     * @param i index of fst genome
     * @param j index of scnd genome
     */
    public void setIndices(final int i, final int j) {
        this.i = i;
        this.j = j;
    }

//	 /**
//	  * Sets the scenario of this comparison (DCJ = 0, HP = 1, inv = 2, trans = 3, all = 4).
//	  * @param scenario The scenario of the comparison
//	  */
//	 public void setScenario (final int scenario){
//		 this.scenario = scenario;
//	 }
//
//	/**
//	 * Returns the scenario of this comparison.
//	 * @return the scenario
//	 */
//	public int getScenario() {
//		return this.scenario;
//	}
    /**
     * Sets the distances of this genome comparison.
     * @param distances the distance of this comparison
     */
    public void setDistance(final int[] distances) {
        this.distances = distances;
    }

    /**
     * Returns the distance of this genome comparison.
     * @return the distance
     */
    public int[] getDistances() {
        return this.distances;
    }

    /**
     * Sets the intermediate genomes of one optimal sorting scenario 
     * of this genome comparison for all chosen scenarios.
     * @param intermedGenomes the intermediate Genomes of this comparison
     */
    public void setIntermedGenomes(final IntermediateGenomesGenerator[] intermedGenomes) {
        this.intermedGenomes = intermedGenomes;
    }

    /**
     * Returns the intermediate genomes of one optimal sorting scenario 
     * of this genome comparison for all chosen scenarios.
     * @return the intermediate genomes of this comparison
     */
    public IntermediateGenomesGenerator[] getIntermedGenomes() {
        return this.intermedGenomes;
    }

    /**
     * Returns the index of the fst genome.
     * @return the index of the fst genome.
     */
    public int getfstIndex() {
        return this.i;
    }

    /**
     * Returns the index of the scnd genome.
     * @return the index of the scnd genome.
     */
    public int getScndIndex() {
        return this.j;
    }

    /**
     * @param The lower bound of all possible dcj sorting sequences
     * for the adjacency graph.
     */
    public void setLowerBound(BigInteger dcjLowerBound) {
        this.dcjLowerBound = dcjLowerBound;
    }

    /**
     * @return The lower bound of all possible dcj sorting sequences
     * for the adjacency graph.
     */
    public BigInteger getDCJLowerBound() {
        return this.dcjLowerBound;
    }
}
