package de.unibi.cebitec.gi.unimog.algorithms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import de.unibi.cebitec.gi.unimog.datastructure.Chromosome;
import de.unibi.cebitec.gi.unimog.datastructure.DataFramework;
import de.unibi.cebitec.gi.unimog.datastructure.Genome;
import de.unibi.cebitec.gi.unimog.exceptions.InputOutputException;
import de.unibi.cebitec.gi.unimog.utils.Constants;

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
 * Parses as many genomes from a file (call "parseInput") or a String (call "readGenomes") as it holds.
 * The parser is able to detect if the chromosome sign at the end of a genome is missing. It also
 * detects, if the space between a chromosome sign and the previous gene is missing.
 * They can be in two different formats:
>Genome B 
1 2 3 4 5 | 6 7 8 9 10 11 12 | 13 14 15 | 16 17 | 
>genome5 
1 -2 3 8 4 -5 6 | 7 9 -10 11 -12 13 14 -15 16 | 
>genome6 
1 2 3 4 5 6 | 7 8 9 10 11 12 13 14 15 16 |
>genome1 
1 -4 2 3 5 7 6 8 -16 -14 -15 -13 -11 -12 -10 9 17 |
>genome2 
1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 |
>Genome A 
2 
1 
3 
5 
4 
| 6 7 -11 -9 -10 -8 12 16 | 15 14 -13 17 |
 */
public class GenomeParser {

    /**
     * Standard constructor for a new GenomeParser which belongs to exactly one file.
     */
    public GenomeParser() {
    }

    /**
     * Reads genomes from a file.
     * @param file the file to read from
     * @return The DataFramework object containing all parsed genomes and their mappings to gene 
     * @throws IOException
     */
    //@SuppressWarnings("unchecked")
    public DataFramework parseInput(final String[] pathnames) throws IOException, InputOutputException {

        String input = "";
        String readLine = "";
        for (String pathname : pathnames) {
            File file = new File(pathname);
            // current input file
            if (!file.exists() && !file.canRead()) {
                throw new IOException("After scenario parameter, only filenames permitted, but cannot open file: ".
                        concat(file.getAbsolutePath()));
            }
            
            BufferedReader in = new BufferedReader(new FileReader(file));
            while ((readLine = in.readLine()) != null) {
                input = input.concat(readLine.concat(Constants.LINE_BREAK));
            }
        }
        
        return this.readGenomes(input);
    }

    /**
     * Returns the position of the next space in the string.
     * @param readLine The input string
     * @return The next space position
     */
    private int getNextSpace(String readLine) {
        int i = readLine.indexOf(' ');
        if (i == Constants.ERROR_NUM) {
            i = readLine.length();
        }
        return i;
    }

    /**
     * Reads genomes from a string.
     * @param input the file to read from
     * @return The DataFramework object containing all parsed genomes and their mappings to gene 
     */
    @SuppressWarnings("unchecked")
    public DataFramework readGenomes(final String input) throws InputOutputException {

        // parse the input
        final HashMap<Integer, String> genomeIDs = new HashMap<Integer, String>();
        final ArrayList<HashMap<String, Integer>> genomesMap = new ArrayList<HashMap<String, Integer>>();
        final ArrayList<HashMap<Integer, String>> genomesMap2 = new ArrayList<HashMap<Integer, String>>();
        final ArrayList<HashMap<Integer, Integer>> chromMaps = new ArrayList<HashMap<Integer, Integer>>();
        final ArrayList<Genome> genomes = new ArrayList<Genome>();
        final ArrayList<Integer> genes = new ArrayList<Integer>();
        final HashMap<Integer, Integer> chromMap = new HashMap<Integer, Integer>();
        final char linearChrEnd = '|';
        final char circularChrEnd = ')';
        final String startSymbol = ">";
        Chromosome chrom;
        int chromNb = 0;
        Genome genome = new Genome();
        HashMap<String, Integer> geneMap = new HashMap<String, Integer>();
        HashMap<Integer, String> geneMap2 = new HashMap<Integer, String>();
        char firstChar;
        int chromIdx = 1; //for chromosome mapping
        int iGenome = 1;
        String genomeID = "";

        String[] genomeStrings = input.trim().split(startSymbol);
        if (genomeStrings.length < 2) {
            throw new InputOutputException("Genome format is wrong, either you did not enter genomes yet or you forgot to precede them by \">\"?");
        }

        for (String genomeString : genomeStrings) {
            if (!genomeString.isEmpty()) {

                String[] lines = genomeString.split(Constants.LINE_BREAK);
                String readLine;
                for (int k = 0; k < lines.length; ++k) {
                    readLine = lines[k];
                    if (k == 0) {
                        genomeID = readLine.substring(0).trim();
                        continue;
                    }
                    readLine = readLine.trim();
                    if (!readLine.startsWith("//") && !readLine.isEmpty()) { //skip comment lines
                        firstChar = readLine.charAt(0);
                        //start new genome & save finished one if it exists
                        int i = 0;
                        while (!readLine.isEmpty()) {
                            i = 0;
                            firstChar = readLine.charAt(i);
                            if (firstChar == linearChrEnd || firstChar == circularChrEnd) {
                                chrom = new Chromosome(genes, firstChar == circularChrEnd);
                                genome.addChromosome(chrom);
                                genes.clear();
                                ++chromNb;
                                readLine = readLine.substring(1).trim();
                            } else {
                                //within a chromosome, genes are saved
                                readLine = readLine.trim();
                                i = this.getNextSpace(readLine);
                                String geneName = readLine.substring(0, i);
                                char lastChar = geneName.charAt(i - 1);
                                boolean missingSpace = lastChar == linearChrEnd || lastChar == circularChrEnd;
                                if (missingSpace) {
                                    geneName = geneName.substring(0, i - 1);
                                    readLine = readLine.substring(0, i - 1).concat(" ".concat(String.valueOf(lastChar))).concat(readLine.substring(i));
                                    --i;
                                }
                                if (geneName.startsWith("-")) { //remove " " after "-"
                                    geneName = geneName.substring(1).trim();

                                    if (!geneMap.containsKey(geneName)) {
                                        geneMap.put(geneName, -chromIdx);
                                        geneMap2.put(-chromIdx, geneName);
                                        genes.add(-chromIdx);
                                        chromMap.put(chromIdx++, chromNb);
                                    }
                                } else {
                                    if (!geneMap.containsKey(geneName)) {
                                        geneMap.put(geneName, chromIdx);
                                        geneMap2.put(chromIdx, geneName);
                                        genes.add(chromIdx);
                                        chromMap.put(chromIdx++, chromNb);
                                    }
                                }
                                if (i < readLine.length()) {
                                    readLine = readLine.substring(i + 1);
                                } else {
                                    readLine = "";
                                }
                            }
                        }
                    }
                }
                if (!genes.isEmpty()) { //add chromosome to current genome
                    chrom = new Chromosome(genes, false);
                    genome.addChromosome(chrom);
                    genes.clear();
                }
                if (genome.getNumberOfChromosomes() != 0) { //store finished genome
                    genomes.add(genome);
                    genome = new Genome();
                    genomeIDs.put(iGenome, genomeID);
                    genomesMap.add(geneMap);
                    geneMap = new HashMap<String, Integer>();
                    genomesMap2.add(geneMap2);
                    geneMap2 = new HashMap<Integer, String>();
                    chromMaps.add((HashMap<Integer, Integer>) chromMap.clone());
                    chromMap.clear();
                    chromIdx = 1;
                    chromNb = 0;
                    ++iGenome;
                }
//            genomeID = readLine.substring(1).trim();
            }
        }

        if (!genes.isEmpty()) {
            chrom = new Chromosome(genes, false);
            genome.addChromosome(chrom);
            genes.clear();
        }
        if (genome.getNumberOfChromosomes() != 0) {
            genomes.add(genome);
            genomesMap.add(geneMap);
            genomesMap2.add(geneMap2);
            chromMaps.add((HashMap<Integer, Integer>) chromMap.clone());
            genomeIDs.put(iGenome, genomeID);
        }
//		if (removeDups){
//			genomes = Utilities.removeDuplicatesAdvanced(genomes);
//		}
        //Construct the global data holder which will be returned.
        final DataFramework globalData = new DataFramework();
        globalData.setChromMaps(chromMaps);
        globalData.setGenomeIDs(genomeIDs);
        globalData.setGenomes(genomes);
        globalData.setGenomesMap(genomesMap);
        globalData.setGenomesMap2(genomesMap2);
        return globalData;

    }

    /**
     * Creates a mapping of the genes to their chromosomes.
     * @param genome the genome whose chromosome mapping is needed
     * @return the hashmap of chromosome mappings
     */
    public static HashMap<Integer, Integer> getChromMap(final Genome genome) {
        HashMap<Integer, Integer> chromMapping = new HashMap<Integer, Integer>();
        ArrayList<Chromosome> chroms = genome.getGenome();
        int[] chrom;
        for (int i = 0; i < chroms.size(); ++i) {
            chrom = chroms.get(i).getGenes();
            for (int j = 0; j < chrom.length; ++j) {
                chromMapping.put(Math.abs(chrom[j]), i);
            }
        }
        return chromMapping;
    }
    //Duplicate check: Easy when reading genomes save boolean[] with gene 12 at
    //index 12 -> array[12] = true -> if true then don't save scnd time!
    //
//
//	public static int removeDuplications(Vector<Genome> genomes,
//			Vector<String> mapping, AnnotationList functions, PrintStream output) {
//		
//		SortedList<Integer> c = new SortedList<Integer>();
//		
//		int n = mapping.size();
//
//
//		for (Genome g : genomes) {
//
//			// initialize array
//			boolean[] b = new boolean[n];
//			for (int i = 0; i < n; i++) {
//				b[i] = false;
//			}
//
//			for(Chromosome chr:g.getChromosomes()){
//			Vector<Integer> seq = chr.getGenes();
//
//			// mark contained, check for doubles
//			for (int i = 0; i < seq.size() - 1; i++) {
//				Integer elem = seq.get(i);
//				if (elem != 0) {
//					int e = Math.abs(elem);
//					if (b[e - 1]) {
//						// save duplicated element
//						if (!c.contains(e)) {
//							c.add(e);
//						}
//					} else {
//						b[e - 1] = true;
//					}
//				}
//			}
//			}
//		}
//
//		printVerbose(c.size()+" genes occur duplicated.");
//		output.println(c.size()+" genes occur duplicated.");
//		
//		if(c.size()==0) return 0;
//
//		//delete genes from mapping
//		int counter=0;
//		for (int j : c) {
//			mapping.removeElementAt(j-1-counter);
//			if(!functions.isEmpty())functions.removeElementAt(j-1-counter);
//			counter++;
//		}
//
//		//delete duplicated genes in all genomes and shift indices
//		int occ=0;
//		for (Genome g : genomes) {
//			for(Chromosome chr:g.getChromosomes()){
//				Vector<Integer> seq = chr.getGenes();
//				boolean lastDel=false;
//				for(int pos=0;pos<seq.size();){
//					int i=seq.get(pos);
//					int j; //shift to left by j positions
//					for(j=0;j<c.size() && c.get(j)<Math.abs(i);j++);
//					if(j<c.size() && c.get(j)==Math.abs(i)){
//						if(lastDel){
//							//avoid consecutive zeros.
//							seq.remove(pos);
//						}else{
//							seq.set(pos, 0);
//							lastDel=true;
//							pos++;
//							occ++;
//						}
//					}else if(j<=c.size()){
//						lastDel=false;
//						seq.set(pos, i-(i>0?j:-j));
//						pos++;
//					}else{
//						pos++;
//						lastDel=false;
//					}
//				}	
//			}
//		}
//		return occ;
//	}
}
