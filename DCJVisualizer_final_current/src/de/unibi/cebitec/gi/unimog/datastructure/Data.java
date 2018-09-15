package de.unibi.cebitec.gi.unimog.datastructure;

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
 * Class comprehending all basic data for a single comparison of two genomes.
 */
public class Data {

	private Genome genomeA;
	private Genome genomeB;
	private AdjacencyGraph adjGraph;

	/**
	 * Standard constructor.
	 * @param genomeA First genome.
	 * @param genomeB Second genome.
	 */
	public Data(final Genome genomeA, final Genome genomeB) {
		this.genomeA = genomeA;
		this.genomeB = genomeB;
	}
	
	/**
	 * Standard constructor.
	 * @param genomeA First genome.
	 * @param genomeB Second genome.
	 * @param adjGraph Adjacency graph belonging to the two given genomes.
	 */
	public Data(final Genome genomeA, final Genome genomeB, final AdjacencyGraph adjGraph) {
		this.genomeA = genomeA;
		this.genomeB = genomeB;
		this.adjGraph = adjGraph;
	}

	/**
	 * Constructor for creating an empty Data object.
	 */
	public Data() {

	}

	/**
	 * Returns the first genome.
	 * @return GenomeA
	 */
	public Genome getGenomeA() {
		return this.genomeA;
	}

	/**
	 * Sets the first genome.
	 * @param genomeA GenomeA to set
	 */
	public void setGenomeA(final Genome genomeA) {
		this.genomeA = genomeA;
	}

	/**
	 * Re the second genome.
	 *  @return GenomeB
	 */
	public Genome getGenomeB() {
		return this.genomeB;
	}

	/**
	 * Sets the second genome.
	 * @param genomeB GenomeB to set
	 */
	public void setGenomeB(final Genome genomeB) {
		this.genomeB = genomeB;
	}

	/**
	 * Sets the adjacency graph that should belong to the genomes!
	 * @param adjGraph the adjGraph to set
	 */
	public void setAdjGraph(AdjacencyGraph adjGraph) {
		this.adjGraph = adjGraph;
	}

	/**
	 * Returns the adjacency graph stored in this object.
	 * @return the adjacency graph
	 */
	public AdjacencyGraph getAdjGraph() {
		return this.adjGraph;
	}

}
