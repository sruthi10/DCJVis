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
 * @author -Rolf Hilker-
 * 
 * This class represents a genome as a list of chromosomes. The
 * chromosomes consist of an array of signed integers.
 */
public class Genome implements Cloneable {

	private ArrayList<Chromosome> genome = new ArrayList<Chromosome>();
        /*Chromosome: 
        private int[] genes;
	private int length = 0;
	private boolean isCircular;
        */
	private int nbOfGenes;

	/**
	 * Constructor for creating an empty genome.
	 */
	public Genome() {
		
	}
	
	/**
	 * Constructor for creating a genome.
	 * @param genome the list of chromosomes
	 */
	public Genome(final ArrayList<Chromosome> genome) {
		genome.trimToSize();
		this.genome = genome;
		this.genome.trimToSize();
		this.nbOfGenes = Utilities.genomeSizeCalculation(this);
	}

	/**
	 * Returns the genome.
	 * @return the genome
	 */
	public ArrayList<Chromosome> getGenome() {
		return this.genome;
	}

	/**
	 * Returns the chromosome with index i.
	 * @param i the index of the chromosome
	 * @return chromosome with index i
	 */
	public Chromosome getChromosome(final int i) {
		return this.genome.get(i);
	}

	/**
	 * Returns the size of the genome.
	 * @return the size of the genome
	 */
	public int getNumberOfChromosomes() {
		return this.genome.size();
	}

	/**
	 * Returns the number of genes contained by the genome - without caps!
	 * @return the number of genes contained by the genome
	 */
	public int getNumberOfGenes() {
		return this.nbOfGenes;
	}
	
	/**
	 * Adds a chromosome to the genome.
	 * @param chrom The Chromosome to add
	 */ 
	public void addChromosome(final Chromosome chrom){
		this.genome.add(chrom);
		this.genome.trimToSize();
		this.nbOfGenes += chrom.getSize();
	}
	
//	/**
//	 * Deletes a specified gene from a genome.
//	 * @param gene The gene to remove
//	 * @return <code>true</code> if the gene was removed successfully
//	 */
//	public boolean deleteGene(final int gene){
//		for(Chromosome chrom : this.getGenome()){
//			int[] genes = chrom.getGenes();
//			int[] newGenes = new int[genes.length-1];
//			int deviation = 0;
//			for (int i=0; i<genes.length; ++i){
//				if (gene == genes[i]){
//					deviation = 1;
//				} else {
//					newGenes[i-deviation] = genes[i];
//				}
//			}
//			if (deviation == 1){
//				chrom.setGenes(newGenes);
//				this.nbOfGenes = Utilities.genomeSizeCalculation(this);
//				return true;
//			}
//		}
//		return false;
//	}
	
	/**
	 * Replaces a specified chromosome in the genome.
	 * @param chrom The chromosome to add
	 * @param index The index of the element to replace
	 */
	public void replaceChromosome(final Chromosome chrom, final int index){
		this.genome.set(index, chrom);
	}
	
	/**
	 * Clones this genome.
	 */
	public Genome clone(){
		ArrayList<Chromosome> newChroms = new ArrayList<Chromosome>();
		for (Chromosome chrom : this.genome){
			newChroms.add(chrom.clone());
		}
		newChroms.trimToSize();
		return new Genome(newChroms);
	}
	

//	/**
//	 * Checks that no values are double in the genome.
//	 */
//	private void dublicateCheck() {
//		// TODO: fill with logic if needed here
//		// throw DublicateGeneException()
//	}

}
