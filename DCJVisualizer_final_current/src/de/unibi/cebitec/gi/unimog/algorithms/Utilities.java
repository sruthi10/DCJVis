package de.unibi.cebitec.gi.unimog.algorithms;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import de.unibi.cebitec.gi.unimog.datastructure.Chromosome;
import de.unibi.cebitec.gi.unimog.datastructure.Component;
import de.unibi.cebitec.gi.unimog.datastructure.Genome;
import de.unibi.cebitec.gi.unimog.datastructure.OperationList;
import de.unibi.cebitec.gi.unimog.datastructure.Pair;

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
 * Class containing all utilities needed for this project.
 */
public final class Utilities {

    /**
     * Do no instantiate.
     */
    private Utilities() {
    }

    /**
     * Creates one long int array of a Genome.
     * @param genome the genome that should be transformed 
     * 			in one long array of ints.
     * @param nbOfGenes the number of genes in the genome, 
     * 			needed for faster access and genome without caps has 
     * 			a too long array then!
     * @param addCaps if caps should be added
     * @return The genome as int Array.
     */
    public static int[] genomeToIntArray(final ArrayList<Chromosome> genome,
            final int nbOfGenes, boolean addCaps) 
    {
        final int nbOfChroms = genome.size();
        final int[] genomeArray;
        if (addCaps) {
            genomeArray = new int[nbOfGenes + 2 * nbOfChroms];
        } else {
            genomeArray = new int[nbOfGenes];
        }
        int pos = 0;
        for (int i = 0; i < nbOfChroms; ++i) {
            Chromosome chrom = genome.get(i);
            int size = chrom.getSize();
            System.arraycopy(chrom.getGenes(), 0, genomeArray, pos, size);
            pos += size;
        }
        return genomeArray;
    }

    /**
     * Calculates the number of genes of a genome. Useful if a genome contains
     * more than one chromosome.
     * @param genome The genome whose size should be calculated
     * @return The number of genes of the genome
     */
    public static int genomeSizeCalculation(final Genome genome) {
        int size = 0;
        for (int i = 0; i < genome.getNumberOfChromosomes(); ++i) {
            final int[] chromosome = genome.getChromosome(i).getGenes();
            size += chromosome.length;
        }
        return size;
    }

    /**
     * Given an array, remove all duplicate entries and returns a new array with
     * only unique entries while keeping the original order.
     * @param genome Chromosome which may contain duplicate genes
     * @return Chromosome without any duplicate entries
     */
    public static int[] removeDuplicates(int[] genome) {
        // Take care of the cases where the array is null or is empty.
        if (genome == null) {
            return null;
        }
        if (genome.length == 0) {
            return new int[0];
        }

        // Use a LinkedHashSet as a mean to remove the duplicate entries.
        // The LinkedHashSet has two characteristics that fit for the job:
        // First, it retains the insertion order, which ensure the output's
        // order is the same as the input's. Secondly, by being a set, it
        // only accept each entries once; a LinkedHashSet ignores subsequent
        // insertion of the same entry.
        LinkedHashSet<Integer> set = new LinkedHashSet<Integer>();
        for (int n : genome) {
            set.add(new Integer(n));
        }

        // At this point, the LinkedHashSet contains only unique entries.
        // Since the function must return an int[], we need to copy entries
        // from the LinkedHashSet to a brand new array.
        int index = 0;
        int[] newArray = new int[set.size()];
        for (Integer currValue : set) {
            newArray[index++] = currValue.intValue();
        }
        return newArray;
    }

    /**
     * Merges two ArrayLists of Components and sorts them after the start position. 
     * Both ArrayLists have to be sorted after the start position, 
     * otherwise the sorting does not work correctly.
     * Sorting the presorted Lists is done in N steps. O(N)
     * @param phase1Comps First List of Components
     * @param phase2Comps Second List of Components
     * @return Sorted merged ArrayList of Components
     */
    public static ArrayList<Component> mergeComponents(final ArrayList<Component> phase1Comps,
        final ArrayList<Component> phase2Comps) {
        ArrayList<Component> sortedComponents = new ArrayList<Component>();
        int i = 0;
        int j = phase2Comps.size() - 1;
        while (i < phase1Comps.size()) {
            int start1 = phase1Comps.get(i).getStartIndex();
            if (j >= 0) {
                int start2 = phase2Comps.get(j).getStartIndex();
                if (start1 < start2) {
                    sortedComponents.add(phase1Comps.get(i));
                    ++i;
                } else {
                    sortedComponents.add(phase2Comps.get(j));
                    --j;
                }
            } else {
                sortedComponents.add(phase1Comps.get(i));
                ++i;
            }
        }
        while (j >= 0) {
            sortedComponents.add(phase2Comps.get(j));
            --j;
        }
        return sortedComponents;
    }

    /**
     * Adds caps at the beginning and the end of each chromosome. In standard case the 
     * front cap is represented by '0' and the back cap by 'N+1' if N is 
     * the length of the chromosome.
     * @param genome The genome
     * @param frontCap The front cap of a chromosome
     * @param backCap The back cap of a chromosome
     * @return the capped genome
     */
    public static Genome genomeCapping(final Genome genome, final int frontCap, final int backCap) {
        ArrayList<Chromosome> cappedGenome = new ArrayList<Chromosome>();
        for (int i = 0; i < genome.getNumberOfChromosomes(); i++) {
            final int[] geneArray = genome.getChromosome(i).getGenes();
            final boolean isCircular = genome.getChromosome(i).isCircular();
            final int nbOfCaps = 2;
            final int size = geneArray.length;
            final int[] cappedChrom = new int[size + nbOfCaps];

            cappedChrom[0] = frontCap;
            System.arraycopy(geneArray, 0, cappedChrom, 1, size);
            cappedChrom[size + 1] = backCap;
            cappedGenome.add(new Chromosome(cappedChrom, isCircular));
        }

        return new Genome(cappedGenome);
    }

    /**
     * Handles both steps for unichromosomal genomes: The capping of a genome and 
     * subsequent adaption of the gene numbers. The first gene of the chromosome
     * becomes 1 and all other genes are increased by one. Only the last gene of
     * the chromosome keeps its original number (because it is already correct).
     * @param genomeA unichromosomal genome to cap and adapt
     * @param frontCap The front cap of a chromosome
     * @param backCap The back cap of a chromosome
     * @return the capped and adapted unichromosomal genome
     */
    public static Genome genomeCappingAndNumberAdaption(Genome genomeA, final int frontCap, final int backCap) {
        Genome genome = Utilities.genomeCapping(genomeA, frontCap, backCap);
        int[] genes = genome.getChromosome(0).getGenes();
        genes[0] = 1;
        for (int i = 1; i < genes.length - 1; ++i) { //-1 because last gene is already correct
            if (genes[i] > 0)   { genes[i] += 1; }
            else                { genes[i] -= 1; }
        }
        return genome;
    }
    
    /**
     * Handles both steps for unichromosomal genomes and inverts all genes: The 
     * capping of a genome and subsequent adaption of the gene numbers. The first gene 
     * of the chromosome becomes 1 and all other genes are increased by one. Only the 
     * last gene of the chromosome keeps its original number (because it is already correct).
     * @param genome unichromosomal genome to cap and adapt
     * @param frontCap The front cap of a chromosome
     * @param backCap The back cap of a chromosome
     * @return the capped, adapted and reversed unichromosomal genome
     */
    public static Genome genomeCappingAndNumberAdaptionRev(Genome genome, final int frontCap, final int backCap) {
        Genome cappedGenome = Utilities.genomeCapping(genome, frontCap, backCap);
        int[] genes = cappedGenome.getChromosome(0).getGenes();
        int[] newGenes = new int[genes.length];
        newGenes[0] = 1;
        newGenes[newGenes.length - 1] = genes[genes.length - 1];
        int index = genes.length - 2;
        for (int i = 1; i < genes.length - 1; ++i) { //-1 because last gene is already correct
            if (genes[i] > 0)   { newGenes[index] = (genes[i]+1) * -1; }
            else                { newGenes[index] = (genes[i]-1) * -1; }
            --index;
        }
        ArrayList<Chromosome> adaptedChrom = new ArrayList<Chromosome>();
        adaptedChrom.add(new Chromosome(newGenes, cappedGenome.getChromosome(0).isCircular()));
        return new Genome(adaptedChrom);
    }

    /**
     * Handles both steps in one method: First caps the genome with the given front and
     * back cap and then converts it into an integer array.
     * @param genome The genome
     * @param frontCap The front cap of a chromosome
     * @param backCap The back cap of a chromosome
     * @return the capped genome chained into an integer array
     */
    public static int[] genomeCappingToIntArray(final Genome genome, final int frontCap, final int backCap) {
        int nbOfGenes = genome.getNumberOfGenes();
        Genome genomeCapped = Utilities.genomeCapping(genome, frontCap, backCap);
        return Utilities.genomeToIntArray(genomeCapped.getGenome(), nbOfGenes, true);
    }
    
    /**
     * Corrects the operation list, in case inversion sorting was carried out
     * with genomes which are not cotailed. This means each adjacency has to be
     * decreased by 2 and adjacencies 2 and (n+2)*2 - 1 have to be replaced by
     * the other adjacency in their pair, because they were telomeres originally.
     * An a pair of operations each containing a telomere has to be deleted from
     * the list and also its adjacency graph.
     * The adjacency graph list is also corrected and for each index j int the
     * adj graph the entry of j+2 is decreased by 2 and stored in j. The cap
     * adjacencies at the end of the graph are deleted.
     * @param operationList operation list to correct
     * @param nbGenes number of genes in original genome
     * @return the corrected operation list
     */
    public static OperationList correctOperationList(OperationList operationList, int nbGenes) {
        //correct operation list
        ArrayList<Pair<Integer,Integer>> ops = operationList.getOperationList();
        int lastExtremity = (nbGenes + 2) * 2 - 1; //we need the left extremity of n + 2
        ArrayList<Integer> indexesToDelete = new ArrayList<Integer>();
        for (int i=0; i<ops.size(); ++i) {
            Pair<Integer,Integer> operation = ops.get(i);
            int fst = operation.getFirst();
            int scnd = operation.getSecond();
            if (i%2 == 0) { //remove operations flipping the whole chromosome inbetween the caps
                Pair<Integer,Integer> op2 = ops.get(i+1);
                int fst2 = op2.getFirst();
                int scnd2 = op2.getSecond();
                if ((fst == 2 || fst == lastExtremity || scnd == 2 || scnd == lastExtremity) &&
                    (fst2 == 2 || fst2 == lastExtremity || scnd2 == 2 || scnd2 == lastExtremity)) {
                ops.remove(operation);
                ops.remove(op2);
                indexesToDelete.add(i/2);
                --i;
            }
                
            }
            if (fst == 2 || fst == lastExtremity)   { fst = scnd; }
            if (scnd == 2 || scnd == lastExtremity) { scnd = fst; }
            operation.setFirst(fst-2); //directly update values in existing pairs
            operation.setSecond(scnd-2);
        }
        
        //correct adjacency graphs
        ArrayList<int[]> adjArrays = operationList.getAdjacencyArrayListG1();
        if (!indexesToDelete.isEmpty()){
            for (int i=0; i<indexesToDelete.size(); ++i) {
                adjArrays.remove((int) indexesToDelete.get(i));
                for (int j = i+1; j<indexesToDelete.size(); ++j) {
                    indexesToDelete.set(j, indexesToDelete.get(j)-1);
                }
            }
        }
        for (int i=0; i<adjArrays.size(); ++i) {
            int[] adjArray = adjArrays.get(i);
            int[] newAdjArray = new int[adjArray.length - 4];
            for (int j = 1; j<newAdjArray.length; ++j) {
                if (adjArray[j+2] == 2 || adjArray[j+2] == lastExtremity) { 
                    newAdjArray[j] = j; 
                } else { 
                    newAdjArray[j] = adjArray[j+2]-2; 
                }
            }
            adjArrays.set(i, newAdjArray);
        }
        return operationList;
    }

    /**
     * Removes duplicate genes in every genome of the list of genomes if there is one.
     * @param genomes The array list of genomes
     * @return The new array list of genomes without duplicates
     */
    public static ArrayList<Genome> removeDuplicatesAdvanced(ArrayList<Genome> genomes) {
        //TODO: implement
        return null;
    }

    /**
     * Checks if in case of HP, Inversions and Translocations only 
     * linear chromosomes are given.
     * @param genome Genome to test
     * @return <code>true</code> if it contains only linear chroms, 
     * <code>false</code> otherwise.
     */
    public static boolean onlyLinear(final Genome genome) {
        boolean isLinear = true;
        for (Chromosome chrom : genome.getGenome()) {
            isLinear = !chrom.isCircular();
            if (!isLinear) {
                return isLinear;
            }
        }
        return isLinear;
    }

    /**
     * Checks if two genomes are co-tailed. This means that each of their
     * chromosomes has to start and end with the same gene as one chromosome in 
     * the second genome. Runs in linear time.
     * Remember that each genome can only contain unique gene numbers.
     * @param genomeA First genome
     * @param genomeB Second genome
     * @return <code>true</code> if the genomes are co-tailed, <code>false</code>
     * otherwise.
     */
    public static boolean checkCotailed(final Genome genomeA, final Genome genomeB) {
        int fstExtremity = 0;
        int lastExtremity = 0;
        int nbGenes = genomeA.getNumberOfGenes();
        final boolean[] telomers = new boolean[(nbGenes + 1) * 2];
        for (int i = 0; i < genomeA.getNumberOfChromosomes(); ++i) {
            int[] genes1 = genomeA.getChromosome(i).getGenes();
            fstExtremity = Utilities.checkSign(genes1[0] * 2, true);
            lastExtremity = Utilities.checkSign(genes1[genes1.length - 1] * 2, false);
            telomers[fstExtremity] = true;
            telomers[lastExtremity] = true;
        }
        for (int j = 0; j < genomeB.getNumberOfChromosomes(); ++j) {
            int[] genes2 = genomeB.getChromosome(j).getGenes();
            fstExtremity = Utilities.checkSign(genes2[0] * 2, true);
            lastExtremity = Utilities.checkSign(genes2[genes2.length - 1] * 2, false);
            if (telomers[fstExtremity] == false || telomers[lastExtremity] == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the telomere extremity of a gene.
     * @param geneNb
     * @return
     */
    private static int checkSign(int geneNb, boolean fstGene) {
        if (geneNb > 0 && fstGene) {
            geneNb -= 1;
        } else if (geneNb < 0 && !fstGene) {
            geneNb = -(geneNb + 1);
        } else if ((geneNb < 0 && fstGene)) {
            geneNb *= -1;
        } //else 
//		if (geneNb > 0 && !fstGene) {
//			geneNb *= -1;
//		}
        return geneNb;
    }

    /**
     * Saves the position of each gene on the chromosomes of a genome.
     * @param genome The genome whose mapping is needed
     * @return The list of mappings for each chromosome
     */
    public static ArrayList<int[]> getGenePos(final Genome genome) {
        // Array[gene] = pos
        ArrayList<int[]> mappings = new ArrayList<int[]>();
        for (int i = 0; i < genome.getNumberOfChromosomes(); ++i) {
            final Chromosome chrom = genome.getChromosome(i);
            final int size = genome.getNumberOfGenes() + 1;
            final int[] mapping = new int[size];
            int[] genes = chrom.getGenes();
            for (int j = 0; j < chrom.getSize(); ++j) {
                int as = genes[j];
                mapping[Math.abs(as)] = j;
            }
            mappings.add(mapping);
        }
        return mappings;
    }

    /**
     * Calculates the extremity number used in the adjacency array from the gene number.
     * @param geneExt gene index already *2
     * @param fstExtremity if it is the left extremity of an adjacency
     * @return the correct extremity
     */
    public static int getExtremity(int geneExt, final boolean fstExtremity) {
        if (geneExt < 0 && fstExtremity) {
            geneExt = (geneExt * -1) - 1;
        } else if (geneExt < 0 && !fstExtremity) {
            geneExt *= -1;
        } else if (geneExt > 0 && !fstExtremity) {
            --geneExt;
        }
        return geneExt;
    }

    /**
     * Assigns the four involved extremities for an operation in the adjacency graph.
     * @param involvExtremities the four involved extremities
     * @param adjArray1 the adjacency graph array 
     * @param standard true for standard assignment: (0, 3) & (1,2) together, false for (0,2) (1,3)
     * @return the modified adjacency graph array
     */
    public static int[] assignInvolvedExtr(final int[] involvExtremities, int[] adjArray1, boolean standard) {
        if (standard) {
            adjArray1[involvExtremities[0]] = involvExtremities[3];
            adjArray1[involvExtremities[3]] = involvExtremities[0];
            adjArray1[involvExtremities[1]] = involvExtremities[2];
            adjArray1[involvExtremities[2]] = involvExtremities[1];
        } else {
            adjArray1[involvExtremities[0]] = involvExtremities[2];
            adjArray1[involvExtremities[2]] = involvExtremities[0];
            adjArray1[involvExtremities[1]] = involvExtremities[3];
            adjArray1[involvExtremities[3]] = involvExtremities[1];
        }
        return adjArray1;
    }

    /**
     * Returns the start index of the component. Since we scan the
     * tree from left to right the components are always in the correct order.
     * If they weren't we would have to check which component comes first.
     * @param startIndex start index of the component
     * @param endIndex end index of the component
     * @return the index of the first cut position is the first element of the pair the other
     * one is the second
     */
    public static Pair<Integer, Integer> getSmallerIndexFst(int startIndex, int endIndex) {
        if (startIndex < endIndex) {
            return new Pair<Integer, Integer>(startIndex, endIndex);
        } else {
            return new Pair<Integer, Integer>(endIndex, startIndex);
        }
    }

    /**
     * Works for extremities in a sorted genome. Returns the
     * neighbour of the extremity in its adjacency.
     * @param ext the extremity whose neighbour in its adjacency is needed
     * @return the extremity which forms an adjacency with the input extremity 
     * 			in the sorted genome
     */
    public static int getNeighbourExt(final int ext) {
        if (ext % 2 == 0) {
            return ext + 1;
        } else {
            return ext - 1;
        }
    }

    /**
     * Works for extremities. Returns the neighbour of the extremity in its gene.
     * Meaning if we have 5 as the extremity we receive 6, since this is the
     * extremity belonging to the same gene.
     * @param ext the extremity whose neighbour in its gene is needed
     * @return the extremity which forms a gene with the input extremity
     */
    public static int getBelongingExt(final int ext) {
        if (ext % 2 == 0) {
            return ext - 1;
        } else {
            return ext + 1;
        }
    }

    /**
     * Checks if any of the extremities is zero. If this is the case 
     * the correct pairs resulting from the operation are returned.
     * Otherwise the pairs are already correct and are directly returned.
     * @param extremity1 fst extremity
     * @param extremity2 sncd extremity
     * @param extremity3 thrd extremity
     * @param extremity4 fourth extremity
     * @return the pair of the two pairs of adjacencies
     */
    public static Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> checkExtremities(
            int extremity1, int extremity2, int extremity3, int extremity4) {
        Pair<Integer, Integer> fstPair = new Pair<Integer, Integer>(extremity1, extremity3);
        Pair<Integer, Integer> scndPair = new Pair<Integer, Integer>(extremity2, extremity4);
        if (extremity1 == 0 && extremity3 == 0) {
            fstPair = scndPair;
        } else if (extremity2 == 0 && extremity4 == 0) {
            scndPair = fstPair;
        } else if (extremity2 == 0 && extremity3 == 0) {
            fstPair.setSecond(scndPair.getSecond());
            scndPair.setFirst(fstPair.getFirst());
        } else if (extremity1 == 0 && extremity4 == 0) {
            fstPair.setFirst(scndPair.getFirst());
            scndPair.setSecond(fstPair.getSecond());
        } else {
            if (extremity1 == 0) {
                fstPair.setFirst(extremity3);
            }
            if (extremity2 == 0) {
                scndPair.setFirst(extremity4);
            }
            if (extremity3 == 0) {
                fstPair.setSecond(extremity1);
            }
            if (extremity4 == 0) {
                scndPair.setSecond(extremity2);
            }
        }
        if (fstPair.getFirst() == fstPair.getSecond()) {
            int fst = fstPair.getFirst();
            fstPair.setFirst(scndPair.getFirst());
            fstPair.setSecond(scndPair.getSecond());
            scndPair.setFirst(fst);
            scndPair.setSecond(fst);
        } else if (extremity2 == 0 && extremity4 == 0) {
            scndPair = fstPair;
        }

        return new Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>(fstPair, scndPair);
    }
//	/**
//	 * Removes duplicate genes in every genome of the list of genomes if there is one.
//	 * @param genomes The array list of genomes
//	 * @return The new array list of genomes without duplicates
//	 */
//	public static ArrayList<Genome> removeDuplicatesAdvanced(ArrayList<Genome> genomes){
//		for (int i = 0; i<genomes.size(); ++i){
//			Genome genomeToCheck = genomes.get(i);
//			int[] uncheckedGenome = Utilities.genomeToIntArray(genomeToCheck.getGenome(), 
//					genomeToCheck.getNumberOfGenes(), false);
//			int[] checkedGenome  = Utilities.removeDuplicates(uncheckedGenome);
//			if (uncheckedGenome.length != checkedGenome.length){
//				Genome newGenome = new Genome();
//				int endpos = 0;
//				int startpos = 0;
//				for (int j=0; j<genomeToCheck.getNumberOfChromosomes(); ++j){
//					int countDubs = 0;
//					Chromosome oldChrom = genomeToCheck.getChromosome(j);
//					int chromLength = oldChrom.getSize();
//					endpos += chromLength-1;
//					if (endpos >= checkedGenome.length){
//						endpos = checkedGenome.length-1;
//					}
//					while (checkedGenome[endpos] != oldChrom.getGenes()[chromLength-1]){
//						--endpos;
//						++countDubs;
//					}
//					int[] newChromArray = new int[chromLength-countDubs];
//					System.arraycopy(checkedGenome, startpos, newChromArray, 0, chromLength-1);
//					Chromosome newChrom = new Chromosome(newChromArray, oldChrom.isCircular());
//					newGenome.addChromosome(newChrom);
//					startpos = endpos;
//				}
//				genomes.set(i, newGenome);
//			}
//		}
//		return genomes;
//	}
}
