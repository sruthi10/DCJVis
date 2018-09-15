package de.unibi.cebitec.gi.unimog.datastructure;

import java.util.ArrayList;

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
 * This class represents a chromosome as a list gene names (Strings). It can be
 * linear or circular.
 */
public class ChromosomeString {

	private static final long serialVersionUID = 1L;
	private String[] genes = new String[1];
	private int length = 0;
	private boolean isCircular;

	/**
	 * Constructor for creating a chromosome.
	 * @param genes the array of genes of this chromosome
	 * @param isCircular
	 *            <code>true</code> if chromosome is circular,
	 *            <code>false</code> otherwise.
	 */
	public ChromosomeString(String[] genes, boolean isCircular) {
		this.length = genes.length;
		this.genes = genes;
		this.isCircular = isCircular;
	}

	/**
	 * Constructor for creating a chromosome.
	 * @param genes the ArrayList of genes of this chromosome
	 * @param isCircular
	 *            <code>true</code> if chromosome is circular,
	 *            <code>false</code> otherwise.
	 */
	public ChromosomeString(ArrayList<String> genes, boolean isCircular) {
		this.length = genes.size();
		this.genes = genes.toArray(this.genes);
		this.isCircular = isCircular;
	}

	/**
	 * Returns the whole gene list of the chromosome.
	 * @return the list of genes
	 */
	public String[] getGenes() {
		return this.genes;
	}
	
	/**
	 * Returns the whole gene list of the chromosome.
	 * @param genes the new array of genes
	 */
	public void setGenes(final String[] genes) {
		this.genes = genes;
	}

	/**
	 * Returns the size of the chromosome.
	 * @return the size of the chromosome
	 */
	public int getSize() {
		return this.length;
	}

	/**
	 * Returns if the chromosome is circular.
	 * @return <code>true</code> or false.
	 */
	public boolean isCircular() {
		return this.isCircular;
	}

	/**
	 * Set if the chromosome is circular.
	 * @param isCircular
	 *            <code>true</code> if chromosome is circular,
	 *            <code>false</code> otherwise.
	 */
	public void setCircular(boolean isCircular) {
		this.isCircular = isCircular;
	}

}

