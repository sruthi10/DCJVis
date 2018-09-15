package de.unibi.cebitec.gi.unimog.datastructure;

import java.util.ArrayList;
import java.util.HashMap;

import de.unibi.cebitec.gi.unimog.utils.Constants;
import java.util.Iterator;
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
 * Data object holding the global data of the framework.
 * This includes all genomes the parser reads and all 
 * corresponding mappings, such as chromosome maps and
 * mappings for gene names to integers etc.
 * Furthermore it contains a method preprocessing a pair
 * of genomes to be compared.
 */
public class DataFramework {

    /** List of genome IDs/names. */
    private HashMap<Integer, String> genomeIDs = new HashMap<Integer, String>();
    /** List of hashmaps of original gene name to an internal integer. */
    private ArrayList<HashMap<String, Integer>> genomesMap = new ArrayList<HashMap<String, Integer>>();
    /** List of hashmaps of the internal used integer to original gene name. */
    private ArrayList<HashMap<Integer, String>> genomesMap2 = new ArrayList<HashMap<Integer, String>>();
    /** List of mappings of chromosome numbers for each gene of each genome. */
    private ArrayList<HashMap<Integer, Integer>> chromMaps = new ArrayList<HashMap<Integer, Integer>>();
    /** The main data structure: List of genomes in internal representation with genes as integers. */
    private ArrayList<Genome> genomes = new ArrayList<Genome>();
    /** Mapping of the currently compared first genome. */
    private HashMap<Integer, String> backMap = new HashMap<Integer, String>();
    /** Contains the suspended gene names of the currently compared genome pair. */
    private List<String> suspendedGenes = new ArrayList<String>();

    /**
     * Standard constructor for a new instance.
     * Doesn't need any parameter as they will be passed by the set methods.
     */
    public DataFramework() {
        
    }

    /**
     * As the parser holds all genomes of a certain file this method takes 
     * the two genomes specified by the two input numbers from the GenomeParsers genomes array
     * and preprocesses them in such a way that they are ready to be compared
     * by one of the distance methods. This includes: Genes that only occur in one
     * genome are suspended. The second genome will contain ordered gene numbers
     * from 1 to n and the first genome will contain the corresponding gene numbers
     * at the position, where they are located in the first genome. Both genomes will
     * furthermore contain the same number of genes after preprocessing.
     * @param fstIndex The index of the first genome
     * @param scndIndex The index of the second genome
     * @return The genome pair which is ready for being compared with one of the
     * 		distance methods
     */
    public Pair<Genome, Genome> preprocessGenomePair(final int fstIndex, final int scndIndex) {

        this.suspendedGenes.clear();
        
        final HashMap<String, Integer> g1Map = this.genomesMap.get(fstIndex);
        final HashMap<String, Integer> g2Map = this.genomesMap.get(scndIndex);
        final HashMap<Integer, String> g2Map2 = this.genomesMap2.get(scndIndex);
        final HashMap<Integer, Integer> g1ChromMap = this.chromMaps.get(fstIndex);
        final HashMap<Integer, Integer> g2ChromMap = this.chromMaps.get(scndIndex);
        final boolean[] g1CircularChrom = this.getCircularChroms(this.genomes.get(fstIndex));
        final boolean[] g2CircularChrom = this.getCircularChroms(this.genomes.get(scndIndex));
        final HashMap<Integer, String> backMap = new HashMap<Integer, String>();
        //final HashMap<Integer, Integer> g2BackMap = new HashMap<Integer, Integer>();

        final Genome genome1 = new Genome();
        final Genome genome2 = new Genome();
        ArrayList<Integer> chrom = new ArrayList<Integer>();
        final int[] orderList = new int[g1Map.size() + 1];
        int counter = 1;
        int lastChromNb = 0;

        //create second (ordered) genome first
        for (int i = 1; i < g2Map.size() + 1; ++i) {

            String geneName = g2Map2.get(i);
            if (geneName == null) {
                geneName = g2Map2.get(-i);
            }
            final int chromNb = g2ChromMap.get(i);
            if (g1Map.containsKey(geneName)) {
                if (chromNb != lastChromNb && chrom.size() > 0) {
                    final Chromosome chromosome = new Chromosome(chrom, g2CircularChrom[lastChromNb]);
                    genome2.addChromosome(chromosome);
                    chrom = new ArrayList<Integer>();
                    lastChromNb = chromNb;
                } else if (chromNb != lastChromNb && chrom.isEmpty()) {
                    lastChromNb = chromNb;
                }
                chrom.add(counter);
                final int number = g1Map.get(geneName);
                final int number2 = g2Map.get(geneName); //to check if it has to be *-1 in genome1
                int checker = -1;
                if (number2 > 0) {
                    checker = 1;
                    backMap.put(counter, geneName);
                } else {
                    backMap.put(counter, "-".concat(geneName));
                }
                if (number < 0) {
                    orderList[-number] = checker * (-counter++);
                } else {
                    orderList[number] = checker * counter++;
                }
            } else {
                this.suspendedGenes.add(geneName);
            }
        }
        //add last chromosome
        if (chrom.size() > 0) {
//			if (chrom.size() == 1){
//				++lastChromNb;
//			}
            Chromosome chromosome = new Chromosome(chrom, g2CircularChrom[lastChromNb]);
            genome2.addChromosome(chromosome);
            chrom = new ArrayList<Integer>();
        }

        //create first genome
        lastChromNb = 0; //if entry is 0 at chrom end & length 1 check
        int currentGene = Constants.ERROR_NUM;
        for (int i = 1; i < orderList.length; ++i) {

            //save complete chromosomes
            final int chromNb = g1ChromMap.get(i);
            if (chromNb != lastChromNb && chrom.size() > 0) {
                Chromosome chromosome = new Chromosome(chrom, g1CircularChrom[lastChromNb]);
                genome1.addChromosome(chromosome);
                chrom = new ArrayList<Integer>();
                lastChromNb = chromNb;
            } else if (chromNb != lastChromNb && chrom.isEmpty()) {
                lastChromNb = chromNb;
            }

            //add current gene to chromosome
            currentGene = orderList[i];
            if (currentGene != 0) {
                chrom.add(currentGene);
            }
        }

        //add last chromosome
        if (chrom.size() > 0) {
            final Chromosome chromosome = new Chromosome(chrom, g1CircularChrom[lastChromNb]);
            genome1.addChromosome(chromosome);
        }
        this.backMap = backMap;

        //check for genes suspended from genome 1
        Iterator<String> genes1It = g1Map.keySet().iterator();
        String currGene;
        while (genes1It.hasNext()) {
            currGene = genes1It.next();
            if (!g2Map.containsKey(currGene)) {
                this.suspendedGenes.add(currGene);
            }
        }
        
        return new Pair<Genome, Genome>(genome1, genome2);
    }

    /**
     * Returns the array of names of the genomes.
     * @return the genomeIDs
     */
    public HashMap<Integer, String> getGenomeIDs() {
        return this.genomeIDs;
    }

    /**
     * Sets the array of names of the genomes.
     * @param genomeIDs the genomeIDs to set
     */
    public void setGenomeIDs(final HashMap<Integer, String> genomeIDs) {
        this.genomeIDs = genomeIDs;
    }

    /**
     * Sets the array of genome mappings of the gene name to the 
     * internal integer representation.
     * @param genomesMap the genomesMap to set
     */
    public void setGenomesMap(ArrayList<HashMap<String, Integer>> genomesMap) {
        this.genomesMap = genomesMap;
    }

    /**
     * Sets the array of genome mappings of the internal integer representation 
     * to a original gene name.
     * @param genomesMap2 the genomesMap2 to set
     */
    public void setGenomesMap2(ArrayList<HashMap<Integer, String>> genomesMap2) {
        this.genomesMap2 = genomesMap2;
    }

    /**
     * Sets the array of mappings of the chromosome numbers to each gene of each genome.
     * @param chromMaps the chromMaps to set
     */
    public void setChromMaps(ArrayList<HashMap<Integer, Integer>> chromMaps) {
        this.chromMaps = chromMaps;
    }

    /**
     * Sets the array of genomes contained by the framework.
     * @param genomes the genomes to set
     */
    public void setGenomes(ArrayList<Genome> genomes) {
        this.genomes = genomes;
    }

    /**
     * Returns for a given genome an array that saves for each
     * Chromosome if it is circular or not.
     * @param genome The genome
     * @return Array containing the information which Chromosome is circular
     */
    public boolean[] getCircularChroms(final Genome genome) {

        final int nbChroms = genome.getNumberOfChromosomes();
        final boolean[] circularChroms = new boolean[nbChroms];
        for (int i = 0; i < nbChroms; ++i) {
            circularChroms[i] = genome.getChromosome(i).isCircular();
        }

        return circularChroms;
    }

    /**
     * Returns the list of genomes read from the file.
     * @return the list of genomes
     */
    public ArrayList<Genome> getGenomes() {
        return this.genomes;
    }

    /**
     * Returns the list of mappings of each gene name to an index
     * from 1 to n. The list contains 1 mapping for each genome. 
     * @return the genomesMap
     */
    public ArrayList<HashMap<String, Integer>> getGenomesMap() {
        return this.genomesMap;
    }

    /**
     * Returns the chromosome maps.
     * @return the chromMaps
     */
    public ArrayList<HashMap<Integer, Integer>> getChromMaps() {
        return this.chromMaps;
    }

    /**
     * Returns a mapping of the gene index to the gene identifier.
     * @return the genomesMap2
     */
    public ArrayList<HashMap<Integer, String>> getGenomesMap2() {
        return this.genomesMap2;
    }

    /**
     * Returns the HashMap containing the mapping of the internal gene name
     * assigned during a comparison to the original gene name.
     * @return This mapping
     */
    public HashMap<Integer, String> getBackMap() {
        return this.backMap;
    }

    /**
     * @return The suspended gene names of the currently compared genome pair.
     */
    public List<String> getSuspendedGenes() {
        return this.suspendedGenes;
    }
    
    
}
