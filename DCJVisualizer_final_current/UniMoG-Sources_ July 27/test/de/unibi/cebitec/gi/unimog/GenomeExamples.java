package de.unibi.cebitec.gi.unimog;

import java.util.ArrayList;

import de.unibi.cebitec.gi.unimog.datastructure.Chromosome;
import de.unibi.cebitec.gi.unimog.datastructure.Genome;

/**
 * @author -Rolf Hilker-
 * 
 * Class that contains some examples for Genomes.
 */
public class GenomeExamples {

	private final Chromosome chromA1 = new Chromosome(new int[] {-1, -3, 4}, false);
	private final Chromosome chromA2 = new Chromosome(new int[] {2, 5}, true);
	private final Chromosome chromA3 = new Chromosome(new int[] {-6, -7}, false);
	private final Chromosome chromB1 = new Chromosome(new int[] {1, 2}, true);
	private final Chromosome chromB2 = new Chromosome(new int[] {-3, -4}, false);
	private final Chromosome chromB3 = new Chromosome(new int[] {-5}, false);
	private final Chromosome chromB4 = new Chromosome(new int[] {6, 7}, true);
	private final Chromosome chromC1 = new Chromosome(new int[] {2, 1, 3, 5, 4}, false);
	private final Chromosome chromC2 = new Chromosome(new int[] {6, 7, -11, -9, -10, -8, 12, 16}, false);
	private final Chromosome chromC3 = new Chromosome(new int[] {15, 14, -13, 17}, false);
	private final Chromosome chromD1 = new Chromosome(new int[] {1, 2, 3, 4, 5}, false);
	private final Chromosome chromD2 = new Chromosome(new int[] {6, 7, 8, 9, 10, 11, 12}, false);
	private final Chromosome chromD3 = new Chromosome(new int[] {13, 14, 15}, false);
	private final Chromosome chromD4 = new Chromosome(new int[] {16, 17}, false);
	private final Chromosome chromE1 = new Chromosome(new int[] {-1, 2, 3, 4}, false);
	private final Chromosome chromE2 = new Chromosome(new int[] {5, 6}, false);
	private final Chromosome chromE3 = new Chromosome(new int[] {7, 8}, false);
	private final Chromosome chromE4 = new Chromosome(new int[] {9}, false);
	private final Chromosome chromF1 = new Chromosome(new int[] {3, 8, 6}, false);
	private final Chromosome chromF2 = new Chromosome(new int[] {4, 5, 9}, false);
	private final Chromosome chromF3 = new Chromosome(new int[] {1, 7, 2}, true);
	private final Chromosome chromG1 = new Chromosome(new int[] {-1, 3, 2, 4}, true);
	private final Chromosome chromG2 = new Chromosome(new int[] {5, 6}, false);
	private final Chromosome chromG3 = new Chromosome(new int[] {7, 8}, true);
	private final Chromosome chromG4 = new Chromosome(new int[] {9}, false);
	/** Chroms for duplicate genes test. */
	private final Chromosome chromH1 = new Chromosome(new int[] {5, 8, 4, 5, 7}, false);
	private final Chromosome chromH2 = new Chromosome(new int[] {2, 1, 8, 2, 3, 2}, true);
	private final ArrayList<Chromosome> genomeAList = new ArrayList<Chromosome>();
	private final ArrayList<Chromosome> genomeBList = new ArrayList<Chromosome>();
	private final ArrayList<Chromosome> genomeCList = new ArrayList<Chromosome>();
	private final ArrayList<Chromosome> genomeDList = new ArrayList<Chromosome>();
	private final ArrayList<Chromosome> genomeEList = new ArrayList<Chromosome>();
	private final ArrayList<Chromosome> genomeFList = new ArrayList<Chromosome>();
	private final ArrayList<Chromosome> genomeGList = new ArrayList<Chromosome>();
	private final ArrayList<Chromosome> genomeHList = new ArrayList<Chromosome>();
	private Genome genomeA;
	private Genome genomeB;
	private Genome genomeC;
	private Genome genomeD;
	private Genome genomeE;
	private Genome genomeF;
	private Genome genomeG;
	private Genome genomeH;

	/**
	 * Examples...
	 */
	public GenomeExamples() {
		this.genomeAList.add(this.chromA1);
		this.genomeAList.add(this.chromA2);
		this.genomeAList.add(this.chromA3);
		this.genomeAList.trimToSize();

		this.genomeBList.add(this.chromB1);
		this.genomeBList.add(this.chromB2);
		this.genomeBList.add(this.chromB3);
		this.genomeBList.add(this.chromB4);
		this.genomeBList.trimToSize();

		this.genomeCList.add(this.chromC1);
		this.genomeCList.add(this.chromC2);
		this.genomeCList.add(this.chromC3);
		this.genomeCList.trimToSize();

		this.genomeDList.add(this.chromD1);
		this.genomeDList.add(this.chromD2);
		this.genomeDList.add(this.chromD3);
		this.genomeDList.add(this.chromD4);
		this.genomeDList.trimToSize();

		this.genomeEList.add(this.chromE1);
		this.genomeEList.add(this.chromE2);
		this.genomeEList.add(this.chromE3);
		this.genomeEList.add(this.chromE4);
		this.genomeEList.trimToSize();

		this.genomeFList.add(this.chromF1);
		this.genomeFList.add(this.chromF2);
		this.genomeFList.add(this.chromF3);
		this.genomeFList.trimToSize();

		this.genomeGList.add(this.chromG1);
		this.genomeGList.add(this.chromG2);
		this.genomeGList.add(this.chromG3);
		this.genomeGList.add(this.chromG4);
		this.genomeGList.trimToSize();
		
		this.genomeHList.add(this.chromH1);
		this.genomeHList.add(this.chromH2);

		this.genomeA = new Genome(this.genomeAList);
		this.genomeB = new Genome(this.genomeBList);
		this.genomeC = new Genome(this.genomeCList);
		this.genomeD = new Genome(this.genomeDList);
		this.genomeE = new Genome(this.genomeEList);
		this.genomeF = new Genome(this.genomeFList);
		this.genomeG = new Genome(this.genomeGList);
		this.genomeH = new Genome(this.genomeHList);
	}

	public Genome getGenomeA() {
		return this.genomeA;
	}

	public Genome getGenomeB() {
		return this.genomeB;
	}

	public Genome getGenomeC() {
		return this.genomeC;
	}

	public Genome getGenomeD() {
		return this.genomeD;
	}

	public Genome getGenomeE() {
		return this.genomeE;
	}

	public Genome getGenomeF() {
		return this.genomeF;
	}

	public Genome getGenomeG() {
		return this.genomeG;
	}
	
	public Genome getGenomeH() {
		return this.genomeH;
	}
}
