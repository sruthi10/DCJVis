package de.unibi.cebitec.gi.unimog.algorithms;

import java.util.ArrayList;
import java.util.HashMap;

import de.unibi.cebitec.gi.unimog.datastructure.Chromosome;
import de.unibi.cebitec.gi.unimog.datastructure.ChromosomeString;
import de.unibi.cebitec.gi.unimog.datastructure.Genome;
import de.unibi.cebitec.gi.unimog.datastructure.OperationList;
import de.unibi.cebitec.gi.unimog.datastructure.Pair;
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
 * This class is for saving a sorting sequence for a certain distance scenario
 * including the initial genome A.
 * The sorting sequence is received as a list of operations. Additionally the 
 * list of adjacencies of each intermediate genome is needed.
 * The printer allows access to the list of intermediate genomes in the form of 
 * the adjacency list or for a human readable output as a list of intermediate 
 * genomes. These genomes can be easily read and used for all kinds of nice visual output. 
 */
public class IntermediateGenomesGenerator {

	private Genome genomeA;
	private OperationList operationList;
	private int[] adjacenciesGA;
	private ArrayList<ChromosomeString[]> intermedGenomes;
	private HashMap<Integer, String> geneNameMap;
	private String genomeID1;
	private String genomeID2;
        private List<Integer> compDistances;

	/**
	 * Constructor for generating the list of intermediate genomes.
	 * @param genomeA The original old genome which should be transformed
	 * @param geneNameMap The mapping of the internal indices to the original gene names
	 * @param operationList The list of operations performed
	 * @param adjacenciesG1 The the adjacencies of the first genome
	 */
	public IntermediateGenomesGenerator(final Genome genomeA, final HashMap<Integer, String> geneNameMap, 
			final OperationList operationList, final int[] adjacenciesG1, final String genomeID1, final String genomeID2){
		this.genomeA = genomeA;
		this.geneNameMap = geneNameMap;
		this.operationList = operationList;
		this.adjacenciesGA = adjacenciesG1;
		this.intermedGenomes = this.generateIntermediateGenomes(this.genomeA , this.operationList, this.adjacenciesGA);
		this.genomeID1 = genomeID1;
		this.genomeID2 = genomeID2;
	}

	/**
	 * Constructor for only using methods of this class without using it as storage.
	 */
	public IntermediateGenomesGenerator() {
		
	}

	/**
	 * Generates the intermediate genomes for all operations contained in the
	 * given operationList. 
	 * @param genomeA The original old genome which should be transformed
	 * @param operationList List of operations to perform on the genome
	 * @param adjacenciesGA Adjacencies of genomeA
	 * @return The list of intermediate genomes according to the operation list
	 */
	public ArrayList<ChromosomeString[]> generateIntermediateGenomes(final Genome genomeA, final OperationList operationList,
			final int[] adjacenciesGA){
		
		ArrayList<int[]> adjacenciesListG1 = operationList.getAdjacencyArrayListG1();
		int size = adjacenciesListG1.size(); //has original size
		adjacenciesListG1.add(0, adjacenciesGA); //original adjacencies are added at beginning
		
		ArrayList<ChromosomeString[]> intermediateGenomes = new ArrayList<ChromosomeString[]>(); //+1 because of original genome
		//add start genome
		intermediateGenomes.add(this.convertGenome(genomeA));

		//construct a new genome for each operation done to transform the genome
		for (int i=0; i<size; ++i){  //+1 because original adjacencies are skipped
			intermediateGenomes.add(this.generateOutputGenome(adjacenciesListG1.get(i+1)));
		}
		
		return intermediateGenomes;
	}
	
	/**
	 * Converts an internal genome with integer gene names in a genome
	 * consisting of an array list of chromosomes and string representations
	 * of the original, external gene name
	 * @param genome genome to convert
	 * @return the converted genome
	 */
	private ChromosomeString[] convertGenome(final Genome genome) {
		
		final ChromosomeString[] convertedGenome = new ChromosomeString[genome.getNumberOfChromosomes()];
		final ArrayList<Chromosome> oldGenome = genome.getGenome();
		for (int j=0; j< genome.getNumberOfChromosomes(); ++j){
			final Chromosome chrom = oldGenome.get(j);
			final String[] newGenes = new String[chrom.getSize()];
			int[] genes = chrom.getGenes();
			for (int i=0; i< chrom.getSize(); ++i){
				newGenes[i] = IntermediateGenomesGenerator.getSignedGeneName(genes[i], this.geneNameMap);
			}
			ChromosomeString newChrom = new ChromosomeString(newGenes, chrom.isCircular());
			convertedGenome[j] = newChrom;
		}
		return convertedGenome;
	}

	/**
	 * Method for generating a new genome according to an adjacency array.
	 * @param adjacencyArray the adjacencies of the genome which should be created.
	 * @return The new genome
	 */
	public ChromosomeString[] generateOutputGenome(int[] adjacencyArray){

		ArrayList<ChromosomeString> newGenome = new ArrayList<ChromosomeString>();
		ArrayList<String> currentChrom = new ArrayList<String>();
		boolean[] visited = new boolean[adjacencyArray.length];
		
		for (int j=1; j<adjacencyArray.length; ++j){
			
			if (visited[j] == false){
				//so we start with the chromosome containing gene 1 = 0,1
				int currentAdj1 = j;
				int currentAdj2 = adjacencyArray[currentAdj1]; //j is first element, currentAdj scnd
				int geneToAdd = (currentAdj1+1)/2;	// (1h, 2t) --> add 1 & in while schleife 2 before 1
				if (currentAdj1%2 == 0){ //if tail gene is negative
					geneToAdd *= -1;
				}
				currentChrom.add(IntermediateGenomesGenerator.getSignedGeneName(geneToAdd, this.geneNameMap));
				visited[currentAdj1] = true;
				
				//first direction 1t, extend to left
				while (((currentAdj2%2 == 1 && !visited[currentAdj2+1]) || 
						(currentAdj2%2 == 0 && !visited[currentAdj2-1])) 
						&& currentAdj1 != currentAdj2){ //as long as no circle or telomere
					geneToAdd = (currentAdj2+1)/2;
					if (currentAdj2%2 == 1){ //if head gene is negative
						geneToAdd *= -1;
					}
					currentChrom.add(0, IntermediateGenomesGenerator.getSignedGeneName(geneToAdd, this.geneNameMap));
					visited[currentAdj2] = true;
					
					//get values of next adjacency
					if (currentAdj2%2 == 0){ //currently tail was reviewed, f.e. (1t,3h) -> j=3t
						currentAdj1 = --currentAdj2;
					} else {
						currentAdj1 = ++currentAdj2;
					}
					currentAdj2 = adjacencyArray[currentAdj1];		// f.e. currentAdj = list[3t] = 5h
					visited[currentAdj1] = true;
				}
				
				if (currentAdj1 != currentAdj2 && (
						(currentAdj2%2 == 1 && visited[currentAdj2+1]) || 
						(currentAdj2%2 == 0 && visited[currentAdj2-1]))){ //if circle chrom is complete
					newGenome.add(new ChromosomeString(currentChrom, true));
					visited[currentAdj1] = true;
					visited[currentAdj2] = true;
				} else {
					//second direction if not circular, way to scnd telomere
					//get values of next adjacency
					if (currentAdj1 == currentAdj2){
						visited[currentAdj1] = true;
						if (j%2 == 0){ //currently tail was reviewed, f.e. (3t) -> currentAdj1=3h
							currentAdj1 = j-1;
						} else {
							currentAdj1 = j+1;
						}
					} else {
						//System.out.println("case exists IntermedGenomeGenerator");
						if (currentAdj2%2 == 0){ //currently tail was reviewed, f.e. (3t) -> currentAdj1=3h
							currentAdj1 = --currentAdj2;
						} else {
							currentAdj1 = ++currentAdj2;
							//System.out.println("Case exists: intermed genomes generator 1");
						} //TODO: does this case exist?
					}
					
					currentAdj2 = adjacencyArray[currentAdj1];		// f.e. currentAdj = list[3t] = 5h
					while (currentAdj1 != currentAdj2){ // !visited[currentAdj] cannot occur here
						geneToAdd = (currentAdj2+1)/2;
						if (currentAdj2%2 == 0){ //if tail gene is negative
							geneToAdd *= -1;
						}
						currentChrom.add(IntermediateGenomesGenerator.getSignedGeneName(geneToAdd, this.geneNameMap));
						visited[currentAdj1] = true;
						visited[currentAdj2] = true;
						
						//get values of next adjacency
						if (currentAdj2%2 == 0){ //currently tail was reviewed, f.e. (1t,3t) -> j=3h
							currentAdj1 = --currentAdj2;
						} else {
							currentAdj1 = ++currentAdj2;
						}
						currentAdj2 = adjacencyArray[currentAdj1];	// f.e. currentAdj = list[3h] = 5t
					}
					newGenome.add(new ChromosomeString(currentChrom, false));
					visited[currentAdj1] = true;
				}
				currentChrom = new ArrayList<String>();
			}
		}
		ChromosomeString[] newGenomeCorrect = new ChromosomeString[newGenome.size()];
		return newGenome.toArray(newGenomeCorrect);
	}
	
	/**
	 * Method for generating a new genome according to an adjacency array. Used with internal
	 * integer representation of the genome.
	 * @param adjacencyArray the adjacencies of the genome which should be created.
	 * @return The new genome
	 */
	public Genome generateGenome(int[] adjacencyArray){

		Genome newGenome = new Genome();
		ArrayList<Integer> currentChrom = new ArrayList<Integer>();
		boolean[] visited = new boolean[adjacencyArray.length];
		
		for (int j=1; j<adjacencyArray.length; ++j){
			
			if (visited[j] == false){
				//so we start with the chromosome containing gene 1 = 0,1
				int currentAdj1 = j;
				int currentAdj2 = adjacencyArray[currentAdj1]; //j is first element, currentAdj scnd
				int geneToAdd = (currentAdj1+1)/2;	// (1h, 2t) --> add 1 & in while schleife 2 before 1
				if (currentAdj1%2 == 0){ //if tail gene is negative
					geneToAdd *= -1;
				}
				currentChrom.add(geneToAdd);
				visited[currentAdj1] = true;
				
				//first direction 1t, extend to left
				while (((currentAdj2%2 == 1 && !visited[currentAdj2+1]) || 
						(currentAdj2%2 == 0 && !visited[currentAdj2-1])) 
						&& currentAdj1 != currentAdj2){ //as long as no circle or telomere
					geneToAdd = (currentAdj2+1)/2;
					if (currentAdj2%2 == 1){ //if head gene is negative
						geneToAdd *= -1;
					}
					currentChrom.add(0, geneToAdd);
					visited[currentAdj2] = true;
					
					//get values of next adjacency
					if (currentAdj2%2 == 0){ //currently tail was reviewed, f.e. (1t,3h) -> j=3t
						currentAdj1 = --currentAdj2;
					} else {
						currentAdj1 = ++currentAdj2;
					}
					currentAdj2 = adjacencyArray[currentAdj1];		// f.e. currentAdj = list[3t] = 5h
					visited[currentAdj1] = true;
				}
				
				if (currentAdj1 != currentAdj2 && (
						(currentAdj2%2 == 1 && visited[currentAdj2+1]) || 
						(currentAdj2%2 == 0 && visited[currentAdj2-1]))){ //if circle chrom is complete
					newGenome.addChromosome(new Chromosome(currentChrom, true));
					visited[currentAdj1] = true;
					visited[currentAdj2] = true;
				} else {
					//second direction if not circular, way to scnd telomere
					//get values of next adjacency
					if (currentAdj1 == currentAdj2){
						visited[currentAdj1] = true;
						if (j%2 == 0){ //currently tail was reviewed, f.e. (3t) -> currentAdj1=3h
							currentAdj1 = j-1;
						} else {
							currentAdj1 = j+1;
						}
					} else {
						//System.out.println("case exists IntermedGenomeGenerator");
						if (currentAdj2%2 == 0){ //currently tail was reviewed, f.e. (3t) -> currentAdj1=3h
							currentAdj1 = --currentAdj2;
						} else {
							currentAdj1 = ++currentAdj2;
							//System.out.println("Case exists: intermed genomes generator 2");
						} //TODO: does this case exist?
					}
					
					currentAdj2 = adjacencyArray[currentAdj1];		// f.e. currentAdj = list[3t] = 5h
					while (currentAdj1 != currentAdj2){ // !visited[currentAdj] cannot occur here
						geneToAdd = (currentAdj2+1)/2;
						if (currentAdj2%2 == 0){ //if tail gene is negative
							geneToAdd *= -1;
						}
						currentChrom.add(geneToAdd);
						visited[currentAdj1] = true;
						visited[currentAdj2] = true;
						
						//get values of next adjacency
						if (currentAdj2%2 == 0){ //currently tail was reviewed, f.e. (1t,3t) -> j=3h
							currentAdj1 = --currentAdj2;
						} else {
							currentAdj1 = ++currentAdj2;
						}
						currentAdj2 = adjacencyArray[currentAdj1];	// f.e. currentAdj = list[3h] = 5t
					}
					newGenome.addChromosome(new Chromosome(currentChrom, false));
					visited[currentAdj1] = true;
				}
				currentChrom = new ArrayList<Integer>();
			}
		}
		return newGenome;
	}
	
	/**
	 * Returns the gene name for a given gene index and the mapping of the indices
	 * to the original gene name. If the geneNb is 0 "" is returned.
	 * @param geneNb The internal number representation of the gene
	 * @param geneNameMap The mapping of the indices to the original gene name
	 * @return The original gene name
	 */
	public static String getSignedGeneName(final int geneNb, final HashMap<Integer, String> geneNameMap){
		String geneName = null;
		if (geneNb > 0){
			geneName = geneNameMap.get(geneNb);
			if (geneName == null){
				geneName = geneNameMap.get(-geneNb);
			}
		} else {
			geneName = geneNameMap.get(-geneNb);
			if (geneName == null){
				geneName = geneNameMap.get(geneNb);
			}
			if (geneName != null){
				if (geneName.startsWith("-")){
					geneName = geneName.substring(1);
				} else {
					geneName = "-"+geneName;	
				}
			}
		} 
		if (geneName == null){
			geneName = "";
		}
		return geneName;
	}

	/**
	 * Returns the list of intermediate genomes.
	 * @return the getIntermedGenomes
	 */
	public ArrayList<ChromosomeString[]> getIntermedGenomes() {
		return this.intermedGenomes;
	}
	
	/**
	 * Returns the operation list for a simple output.
	 * @return the list of operations performed
	 */
	public ArrayList<Pair<Integer, Integer>> getOperationList() {
		return this.operationList.getOperationList();
	}
	
	/**
	 * Returns the list of adjacencies of each intermediate genome
	 * for a simple output.
	 * @return the list of adjacencies
	 */
	public ArrayList<int[]> getAdjacenciesList() {
		return this.operationList.getAdjacencyArrayListG1();
	}

	/**
	 * Returns the mapping of the indices to their original gene name.
	 * @return the geneNameMap The mapping of indices to gene name.
	 */
	public HashMap<Integer, String> getGeneNameMap() {
		return this.geneNameMap;
	}
	
	/**
	 * Returns the adjacencies of the initial genome A.
	 * @return Adjacencies of initial genome A
	 */
	public int[] getAdjacenciesGA(){
		return this.adjacenciesGA;
	}

	/**
	 * Returns the genome identifier of the first genome of the pairwise comparison.
	 * @return the genomeID1
	 */
	public String getGenomeID1() {
		return this.genomeID1;
	}

	/**
	 * Returns the genome identifier of the second genome of the pairwise comparison.
	 * @return the genomeID2
	 */
	public String getGenomeID2() {
		return this.genomeID2;
	}
	
}
