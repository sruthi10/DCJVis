package de.unibi.cebitec.gi.unimog.algorithms;

import de.unibi.cebitec.gi.unimog.datastructure.DataFramework;
import de.unibi.cebitec.gi.unimog.datastructure.Genome;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author -Rolf Hilker-
 *
 * Tests the Genome Parser.
 */
public class GenomeParserTest {

    public GenomeParserTest() {
    }

private GenomeParser parser;

	/**
	 * Tests the DCJ distance calculation for these particular example.
	 *
	 * >A Genom einer bla:
	 *
	 * 2
	 * 1
	 * 3
	 * 5
	 * 4
	 * |
	 *
	 * 6
	 * 7
	 * -11
	 * -9
	 * -10
	 * -8
	 * 12
	 * 16
	 * |
	 *
	 * 15
	 * 14
	 * -13
	 * 17
	 * |
	 *
	 * >B Genom von wem anders:
	 * 1
	 * 2
	 * 3
	 * 4
	 * 5
	 * |
	 *
	 * 6
	 * 7
	 * 8
	 * 9
	 * 10
	 * 11
	 * 12
	 * |
	 *
	 * 13
	 * 14
	 * 15
	 * |
	 *
	 * 16
	 * 17
	 * |
	 */
	@Test
	public void testReadGenomes() {
		File file = new File("D:/Programmieren & Studieren/Testordner/MasterThesis/examples/GenomeExample6.txt");
		//System.out.println(file.getAbsolutePath());
		//try {
			this.parser = new GenomeParser();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//		Genome genome1 = genomes.getFirst().get(0);
//		Genome genome2 = genomes.getFirst().get(1);
//		HashMap<String, Integer> map1 = genomes.getSecond().get(0);
//		HashMap<String, Integer> map2 = genomes.getSecond().get(1);
//		ArrayList<HashMap<Integer, Integer>> chromMaps = this.parser.getChromMaps();
//		HashMap<Integer, Integer> chromMap1 = chromMaps.get(0);
//		HashMap<Integer, Integer> chromMap2 = chromMaps.get(1);
//		boolean[] circ1 = this.parser.getCircularChroms(genome1);
//		boolean[] circ2 = this.parser.getCircularChroms(genome2);
		//Pair<Genome, Genome> pair = this.parser.preprocessGenomePair(0, 1);
//
//
//		//Assert.assertTrue(genomes.size() == 2);
//		Assert.assertTrue(genome1.getNumberOfChromosomes() == 3);
//		Assert.assertTrue(genome2.getNumberOfChromosomes() == 4);
//		int[] chrom1 = genome1.getChromosome(0).getGenes();
//		Assert.assertTrue(chrom1[0] == 2);
//		Assert.assertTrue(chrom1[1] == 1);
//		Assert.assertTrue(chrom1[2] == 3);
//		Assert.assertTrue(chrom1[3] == 5);
//		Assert.assertTrue(chrom1[4] == 4);
//		Assert.assertTrue(genome1.getChromosome(0).isCircular() == false);
//
//		int[] chrom2 = genome2.getChromosome(1).getGenes();
//		Assert.assertTrue(chrom2[0] == 6);
//		Assert.assertTrue(chrom2[1] == 7);
//		Assert.assertTrue(chrom2[2] == 8);
//		Assert.assertTrue(chrom2[3] == 9);
//		Assert.assertTrue(chrom2[4] == 10);
//		Assert.assertTrue(genome2.getChromosome(1).isCircular() == false);

	}

}