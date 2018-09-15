package de.unibi.cebitec.gi.unimog.framework;

import java.util.ArrayList;
import java.util.HashMap;

import de.unibi.cebitec.gi.unimog.algorithms.IntermediateGenomesGenerator;
import de.unibi.cebitec.gi.unimog.datastructure.ChromosomeString;
import de.unibi.cebitec.gi.unimog.datastructure.DataOutput;
import de.unibi.cebitec.gi.unimog.utils.Constants;
import java.math.BigInteger;

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
 * This class provides methods for generating a simple output of 
 * sorting sequences for the console or a text field.
 */
public final class OutputPrinter {

    private static final String EQUALS = " = ";
    private static final String DIST_OF_G = " distance of the genomes \"";

    /**
     * Do not instantiate.
     */
    public OutputPrinter() {
        // Do not instantiate
    }

    /**
     * Creates a human readable string from an adjacency array.
     * The adjacency array represents one genome.
     * @param adjacencies The adjacencies to transform in a readable output
     * @param geneNameMap The mapping of the index to the original gene name
     * @return The String for output purposes
     */
    public static String adjacenciesToOutput(final ChromosomeString[] nextGenome) {
        String output = "";
        for (int i = 0; i < nextGenome.length; ++i) {
            String[] chrom = nextGenome[i].getGenes();
            if (nextGenome[i].isCircular()) {
                output = output.concat("{ ").concat(OutputPrinter.getAdjacency(chrom[chrom.length - 1], chrom[0])).concat(", ");
            } else {
                output = output.concat("{ ").concat(OutputPrinter.getAdjacency("", chrom[0])).concat(", ");
            }
            for (int j = 0; j < chrom.length - 1; ++j) {
                output = output.concat(OutputPrinter.getAdjacency(chrom[j], chrom[j + 1])).concat(", ");
            }
            if (nextGenome[i].isCircular()) {
                output = output.concat(" }   ,   ");
            } else {
                output = output.concat(OutputPrinter.getAdjacency(chrom[chrom.length - 1], "")).concat(" }   ,   ");
            }
        }
        output = output.substring(0, output.length() - 7);
        return output;
    }

    /**
     * Returns the adjacency plus a "t" for tail and a "h" for head for a one or two given
     * signed geneNames. The return value does not contain a sign anymore, as this
     * is not desired for adjacencies. If one of the genes is empty (""), it is handled as
     * a telomere. If the first gene is null it is a chromosome start telomere and if the second
     * gene is null it is a chromosome end. Handling for circular chromosomes must be done separately.
     * @param gene1 First gene whose extremity is wanted in the adjacency
     * @param gene2 Second gene belonging to this adjadcency
     * @return
     */
    private static String getAdjacency(String gene1, String gene2) {
        String geneA = gene1.toString();
        String geneB = gene2.toString();
        if (gene1.startsWith("-")) {
            geneA = gene1.substring(1).concat("_h");
        } else {
            if (!gene1.isEmpty()) {
                geneA = gene1.concat("_t");
            }
        }
        if (gene2.startsWith("-")) {
            geneB = gene2.substring(1).concat("_t");
        } else {
            if (!gene2.isEmpty()) {
                geneB = gene2.concat("_h");
            }
        }
        if (geneA.isEmpty() || geneB.isEmpty()) {
            return "(".concat(geneA).concat(geneB).concat(")");
        }
        return "(".concat(geneA).concat(", ").concat(geneB).concat(")");
    }

    /**
     * Creates a string representing the genome and its chromosomes for
     * a given Genome object.
     * @param nextGenome 	The genome to generate an output for (Gene names are already 
     * 						original & contain their sign)
     * @return The string representing the genome
     */
    public static String genomeToOutput(final ChromosomeString[] nextGenome) {
        String output = "";
        for (int i = 0; i < nextGenome.length; ++i) {
            final ChromosomeString chrom = nextGenome[i];
            final String[] genes = chrom.getGenes();
            for (int j = 0; j < genes.length; ++j) {
                output = output.concat(genes[j]).concat(" ");
            }
            if (chrom.isCircular()) {
                output = output.concat(") ");
            } else {
                output = output.concat("| ");
            }
        }
        return output;
    }

    /**
     * Method for generating a String[] with the overall result ready for output.
     * The first string contains the distances, the second the adjacencies, the third the optimal sorting sequences
     * and the fourth the distance matrix in PHYLIP format.
     * @param results The results comprehended by an array of DataOutput object. One for each comparison
     * @param arrayList 
     * @return The results as a String[] readable for mankind
     */
    public static String[] printResults(final DataOutput[] results, final HashMap<Integer, String> genomeIDs, final Scenario scenario) {

        final boolean matrix = results.length != 1;
        String[] output = new String[4];
        String distances = "";
        String phylipMatrix = "";

        if (matrix) {
            String header = "";
            int rowHeaderWidth = 0;
            String genomeID = "";
            HashMap<Integer, Integer> gIDWidths = new HashMap<Integer, Integer>();
            for (int i = 1; i <= genomeIDs.size(); ++i) {
                genomeID = genomeIDs.get(i);
                header = header.concat(genomeID.concat(" | "));
                int length = genomeID.length();
                gIDWidths.put(i, length);
                if (length > rowHeaderWidth) {
                    rowHeaderWidth = length + 2;
                }
            }

            final int matrixWidth = header.length() + rowHeaderWidth + 3; // generate matrix layout & contents
            final String emptyHeader = OutputPrinter.generateEmptyHeader(rowHeaderWidth + 2);
            distances = OutputPrinter.generateLine(matrixWidth).concat(Constants.LINE_BREAK_OUTPUT);
            distances = distances.concat("|").concat(emptyHeader).concat("| ").concat(header).concat(Constants.LINE_BREAK_OUTPUT);

            int lastFstI = results[0].getfstIndex();
            boolean addSpacing = true;
            String spacingString = "";
            int[] dist;
            int fstI;
            int scndI;
            String idString;
            final String matrixHeader = " distance comparisons:".concat(Constants.LINE_BREAK_OUTPUT).concat(distances);

            if (scenario != Scenario.ALL) {
                distances = scenario.getGuiName().concat(matrixHeader);
                for (int i = 0; i < results.length; ++i) {
                    dist = results[i].getDistances();
                    fstI = results[i].getfstIndex();
                    scndI = results[i].getScndIndex();
                    idString = OutputPrinter.getIDString(genomeIDs.get(fstI), rowHeaderWidth);

                    if (lastFstI != fstI) {
                        addSpacing = true;
                        lastFstI = fstI;
                        distances = distances.concat(Constants.LINE_BREAK_OUTPUT);
                    }
                    if (addSpacing) {
                        spacingString = spacingString.concat(OutputPrinter.generateEmptyEntry(genomeIDs.get(scndI - 1).length()));
                        distances = distances.concat("| ").concat(idString).concat(" |").concat(spacingString).concat("0 |");
                        addSpacing = false;
                        spacingString = spacingString.concat("- |");
                    }
                    distances = distances.concat(OutputPrinter.generateEntry(genomeIDs.get(scndI), dist[0]));
                }
                idString = OutputPrinter.getIDString(genomeIDs.get(results[results.length - 1].getScndIndex()), rowHeaderWidth);
                spacingString = spacingString.concat(OutputPrinter.generateEmptyEntry(genomeIDs.get(results[results.length - 1].getScndIndex()).length()));
                distances = distances.concat(Constants.LINE_BREAK_OUTPUT).concat("| ").concat(idString).concat(" |").
                        concat(spacingString.concat("0 |")).concat(Constants.LINE_BREAK_OUTPUT);
            } else {
                distances = Constants.DCJ_ST.concat(matrixHeader);
                String distances2 = Constants.RDCJ_ST.concat(matrixHeader);
                String distances3 = Constants.HP_ST.concat(matrixHeader);
                String distances4 = Constants.INV_ST.concat(matrixHeader);
                String distances5 = Constants.TRANS_ST.concat(matrixHeader);
                for (int i = 0; i < results.length; ++i) {
                    dist = results[i].getDistances();
                    fstI = results[i].getfstIndex();
                    scndI = results[i].getScndIndex();
                    idString = OutputPrinter.getIDString(genomeIDs.get(fstI), rowHeaderWidth);

                    if (lastFstI != fstI) {
                        addSpacing = true;
                        lastFstI = fstI;
                        distances = distances.concat(Constants.LINE_BREAK_OUTPUT);
                        distances2 = distances2.concat(Constants.LINE_BREAK_OUTPUT);
                        distances3 = distances3.concat(Constants.LINE_BREAK_OUTPUT);
                        distances4 = distances4.concat(Constants.LINE_BREAK_OUTPUT);
                        distances5 = distances5.concat(Constants.LINE_BREAK_OUTPUT);
                    }
                    if (addSpacing) {
                        spacingString = spacingString.concat(OutputPrinter.generateEmptyEntry(genomeIDs.get(scndI - 1).length()));
                        distances = distances.concat("| ").concat(idString).concat(" |").concat(spacingString).concat("0 |");
                        distances2 = distances2.concat("| ").concat(idString).concat(" |").concat(spacingString).concat("0 |");
                        distances3 = distances3.concat("| ").concat(idString).concat(" |").concat(spacingString).concat("0 |");
                        distances4 = distances4.concat("| ").concat(idString).concat(" |").concat(spacingString).concat("0 |");
                        distances5 = distances5.concat("| ").concat(idString).concat(" |").concat(spacingString).concat("0 |");
                        addSpacing = false;
                        spacingString = spacingString.concat("- |");
                    }
                    distances = distances.concat(OutputPrinter.generateEntry(genomeIDs.get(scndI), dist[0]));
                    distances2 = distances2.concat(OutputPrinter.generateEntry(genomeIDs.get(scndI), dist[1]));
                    distances3 = distances3.concat(OutputPrinter.generateEntry(genomeIDs.get(scndI), dist[2]));
                    distances4 = distances4.concat(OutputPrinter.generateEntry(genomeIDs.get(scndI), dist[3]));
                    distances5 = distances5.concat(OutputPrinter.generateEntry(genomeIDs.get(scndI), dist[4]));
                }
                idString = OutputPrinter.getIDString(genomeIDs.get(results[results.length - 1].getScndIndex()), rowHeaderWidth);
                spacingString = spacingString.concat(OutputPrinter.generateEmptyEntry(genomeIDs.get(results[results.length - 1].getScndIndex()).length()));
                spacingString = Constants.LINE_BREAK_OUTPUT.concat("| ").concat(idString).concat(" |").
                        concat(spacingString.concat("0 |"));
                distances = distances.concat(spacingString).concat(Constants.LINE_BREAK_OUTPUT.concat(Constants.LINE_BREAK_OUTPUT)).
                        concat(distances2).concat(spacingString).concat(Constants.LINE_BREAK_OUTPUT.concat(Constants.LINE_BREAK_OUTPUT)).
                        concat(distances3).concat(spacingString).concat(Constants.LINE_BREAK_OUTPUT.concat(Constants.LINE_BREAK_OUTPUT)).
                        concat(distances4).concat(spacingString).concat(Constants.LINE_BREAK_OUTPUT.concat(Constants.LINE_BREAK_OUTPUT)).
                        concat(distances5).concat(spacingString);
            }

        } else {
            final int fstI = results[0].getfstIndex();
            final int scndI = results[0].getScndIndex();
            distances = OutputPrinter.getDistString(results[0].getDistances(), scenario, genomeIDs.get(fstI), genomeIDs.get(scndI));

        }

        output[0] = distances;


        //generate PHYLIP matrix output
        int rowHeaderWidth = 10; //PHYLIP requires exactly 10 characters for a genome identifier
        int[] dist;
        int nbFinishedLines = 1;
        String[] phylipMatrixLines = new String[genomeIDs.size()];
        for (int i = 0; i < genomeIDs.size(); ++i) {
            phylipMatrixLines[i] = OutputPrinter.getIDString(genomeIDs.get(i + 1), rowHeaderWidth);
        }
        int currentLine = 1;
        final String phylipMatrixHeader = " distance comparisons as PHYLIP matrix:".concat(Constants.LINE_BREAK_OUTPUT);

        if (scenario != Scenario.ALL) {
            phylipMatrix = scenario.getGuiName().concat(phylipMatrixHeader).concat(Constants.LINE_BREAK_OUTPUT).
                    concat(String.valueOf(genomeIDs.size()).concat(Constants.LINE_BREAK_OUTPUT));
            for (int i = 0; i < results.length; ++i) {
                dist = results[i].getDistances();
                dist[0] = dist[0] == -1 ? 10000 : dist[0];

                phylipMatrixLines[currentLine] += (dist[0] + " ");

                if (currentLine < genomeIDs.size() - 1) {
                    ++currentLine;
                } else {
                    currentLine = ++nbFinishedLines;
                }
            }
            for (String line : phylipMatrixLines) {
                phylipMatrix = phylipMatrix.concat(line).concat(Constants.LINE_BREAK_OUTPUT);
            }
        } else {

            phylipMatrix = Constants.DCJ_ST.concat(phylipMatrixHeader).concat(Constants.LINE_BREAK_OUTPUT).
                    concat(String.valueOf(genomeIDs.size()).concat(Constants.LINE_BREAK_OUTPUT));
            String phylipMatrix2 = Constants.RDCJ_ST.concat(phylipMatrixHeader).concat(Constants.LINE_BREAK_OUTPUT).
                    concat(String.valueOf(genomeIDs.size()).concat(Constants.LINE_BREAK_OUTPUT));
            String phylipMatrix3 = Constants.HP_ST.concat(phylipMatrixHeader).concat(Constants.LINE_BREAK_OUTPUT).
                    concat(String.valueOf(genomeIDs.size()).concat(Constants.LINE_BREAK_OUTPUT));
            String phylipMatrix4 = Constants.INV_ST.concat(phylipMatrixHeader).concat(Constants.LINE_BREAK_OUTPUT).
                    concat(String.valueOf(genomeIDs.size()).concat(Constants.LINE_BREAK_OUTPUT));
            String phylipMatrix5 = Constants.TRANS_ST.concat(phylipMatrixHeader).concat(Constants.LINE_BREAK_OUTPUT).
                    concat(String.valueOf(genomeIDs.size()).concat(Constants.LINE_BREAK_OUTPUT));
            String[] phylipMatrixLines2 = new String[genomeIDs.size()];
            String[] phylipMatrixLines3 = new String[genomeIDs.size()];
            String[] phylipMatrixLines4 = new String[genomeIDs.size()];
            String[] phylipMatrixLines5 = new String[genomeIDs.size()];

            for (int i = 0; i < genomeIDs.size(); ++i) {
                phylipMatrixLines2[i] = OutputPrinter.getIDString(genomeIDs.get(i + 1), rowHeaderWidth);
            }
            for (int i = 0; i < genomeIDs.size(); ++i) {
                phylipMatrixLines3[i] = OutputPrinter.getIDString(genomeIDs.get(i + 1), rowHeaderWidth);
            }
            for (int i = 0; i < genomeIDs.size(); ++i) {
                phylipMatrixLines4[i] = OutputPrinter.getIDString(genomeIDs.get(i + 1), rowHeaderWidth);
            }
            for (int i = 0; i < genomeIDs.size(); ++i) {
                phylipMatrixLines5[i] = OutputPrinter.getIDString(genomeIDs.get(i + 1), rowHeaderWidth);
            }

            for (int i = 0; i < results.length; ++i) {
                dist = results[i].getDistances();
                dist[0] = dist[0] == -1 ? 10000 : dist[0];
                dist[1] = dist[1] == -1 ? 10000 : dist[1];
                dist[2] = dist[2] == -1 ? 10000 : dist[2];
                dist[3] = dist[3] == -1 ? 10000 : dist[3];
                dist[4] = dist[4] == -1 ? 10000 : dist[4];

                phylipMatrixLines[currentLine] += (dist[0] + " ");
                phylipMatrixLines2[currentLine] += (dist[1] + " ");
                phylipMatrixLines3[currentLine] += (dist[2] + " ");
                phylipMatrixLines4[currentLine] += (dist[3] + " ");
                phylipMatrixLines5[currentLine] += (dist[4] + " ");

                if (currentLine < genomeIDs.size() - 1) {
                    ++currentLine;
                } else {
                    currentLine = ++nbFinishedLines;
                }
            }
            for (int i = 0; i < phylipMatrixLines.length; ++i) {
                phylipMatrix = phylipMatrix.concat(phylipMatrixLines[i]).concat(Constants.LINE_BREAK_OUTPUT);
                phylipMatrix2 = phylipMatrix2.concat(phylipMatrixLines2[i]).concat(Constants.LINE_BREAK_OUTPUT);
                phylipMatrix3 = phylipMatrix3.concat(phylipMatrixLines3[i]).concat(Constants.LINE_BREAK_OUTPUT);
                phylipMatrix4 = phylipMatrix4.concat(phylipMatrixLines4[i]).concat(Constants.LINE_BREAK_OUTPUT);
                phylipMatrix5 = phylipMatrix5.concat(phylipMatrixLines5[i]).concat(Constants.LINE_BREAK_OUTPUT);
            }
            phylipMatrix = phylipMatrix.concat(Constants.LINE_BREAK_OUTPUT.concat(Constants.LINE_BREAK_OUTPUT)).
                    concat(phylipMatrix2).concat(Constants.LINE_BREAK_OUTPUT.concat(Constants.LINE_BREAK_OUTPUT)).
                    concat(phylipMatrix3).concat(Constants.LINE_BREAK_OUTPUT.concat(Constants.LINE_BREAK_OUTPUT)).
                    concat(phylipMatrix4).concat(Constants.LINE_BREAK_OUTPUT).concat(Constants.LINE_BREAK_OUTPUT).
                    concat(phylipMatrix5).concat(Constants.LINE_BREAK_OUTPUT);
        }

        output[3] = phylipMatrix;

        // List of adjacencies after each operation:
        String adjacencies = "";
        String genomes = "";
        if (results[0].getIntermedGenomes() != null) {
            for (int i = 0; i < results.length; ++i) {
                if (results[i].getIntermedGenomes() != null) {
                    IntermediateGenomesGenerator[] resultArray = results[i].getIntermedGenomes();
                    for (int j = 0; j < resultArray.length; ++j) {
                        IntermediateGenomesGenerator result = resultArray[j];
                        if (result != null && !result.getGeneNameMap().isEmpty()) {
                            if (scenario == Scenario.ALL) {
                                adjacencies = adjacencies.concat(OutputPrinter.getMsg(Scenario.getScenario(j + 1), result.getGenomeID1(), result.getGenomeID2()));
                            } else {
                                adjacencies = adjacencies.concat(OutputPrinter.getMsg(scenario, result.getGenomeID1(), result.getGenomeID2()));
                            }
                            final ArrayList<ChromosomeString[]> genomesList = result.getIntermedGenomes();
                            for (int k = 0; k < genomesList.size(); ++k) {
                                ChromosomeString[] nextGenome = genomesList.get(k);
                                adjacencies = adjacencies.concat(String.valueOf(k).concat(".:  ").
                                        concat(OutputPrinter.adjacenciesToOutput(nextGenome).concat(Constants.LINE_BREAK)));
                            }
                            adjacencies = adjacencies.concat(Constants.LINE_BREAK);
                        }
                    }
                }
            }

            // Genome after each operation:
            String lowerBound;
            for (int i = 0; i < results.length; ++i) {
                if (results[i].getIntermedGenomes() != null) {
                    IntermediateGenomesGenerator[] resultArray = results[i].getIntermedGenomes();
                    BigInteger lowerBoundInt = results[i].getDCJLowerBound();

                    for (int j = 0; j < resultArray.length; ++j) {
                        IntermediateGenomesGenerator result = resultArray[j];
                        if (result != null && !result.getGeneNameMap().isEmpty()) {
                            if (scenario == Scenario.ALL) {
                                genomes = genomes.concat(OutputPrinter.getMsg(Scenario.getScenario(j + 1), result.getGenomeID1(), result.getGenomeID2()));
                            } else {
                                genomes = genomes.concat(OutputPrinter.getMsg(scenario, result.getGenomeID1(), result.getGenomeID2()));
                            }
                            final ArrayList<ChromosomeString[]> genomesList = result.getIntermedGenomes();
                            for (int k = 0; k < genomesList.size(); ++k) {
                                ChromosomeString[] nextGenome = genomesList.get(k);
                                genomes = genomes.concat(String.valueOf(k).concat(".:  ").concat(OutputPrinter.genomeToOutput(nextGenome).concat(Constants.LINE_BREAK)));
                            }

                            if (lowerBoundInt != null) {
                                lowerBound = lowerBoundInt.toString();
                                if (scenario != Scenario.DCJ && scenario != Scenario.ALL || scenario == Scenario.ALL
                                        && j > 0) {
                                    genomes = genomes.concat("This is only one of roughly ").concat(lowerBound).concat(" sorting sequences.").concat(Constants.LINE_BREAK);
                                } else {
                                    genomes = genomes.concat("This is only one of at least ").concat(lowerBound).concat(" sorting sequences.").concat(Constants.LINE_BREAK);
                                }
                            }
                            genomes = genomes.concat(Constants.LINE_BREAK);
                        }
                    }
                }
            }
        }
        output[2] = adjacencies;
        output[1] = genomes;

        return output;
    }

    /**
     * @param genomeID genome id whose string representation is needed
     * @param rowHeaderWidth size of the header, determines how many spacings " " have to be
     * added at the end. Phylib format requires exactly 10 characters for a genome identifier.
     * @return the string representing the genome identifier in exactly "rowHeaderWidth" characters,
     * filled with " ", if necessary.
     */
    private static String getIDString(String genomeID, int rowHeaderWidth) {
        final int length = rowHeaderWidth - genomeID.length();
        String spacing = "";
        if (length < 0) {
            return genomeID.substring(0, genomeID.length() + length);
        }
        for (int i = 0; i < length; ++i) {
            spacing = spacing.concat(" ");
        }
        return genomeID.concat(spacing);
    }

    /**
     * Generates an entry of the distance matrix with its distance aligned to the
     * right side of the matrix entry.
     * @param genomeID The genome id on top of the row - determining the width of the
     * matrix cell
     * @param distance the distance to be displayed in the matrix cell. If it is -1
     * 		  a '-' is displayed.
     * @return the entry of this matrix cell
     */
    private static String generateEntry(final String genomeID, final int distance) {
        final String dist = distance != -1 ? String.valueOf(distance) : "-";
        final int lengthDist = dist.length();
        String emptyEntry2 = " |";
        int lengthID = genomeID.length();
        int spacing = 0;
        if (lengthID < 4) {
            lengthID = 6 - (4 - lengthID) - (lengthDist - 1);
        }
        if (lengthID == 6) {
            spacing = 6 - (lengthDist - 1);
        } else {
            spacing = genomeID.length() - (lengthDist - 1);
        }
        String emptyEntry = "";
        for (int i = 0; i < spacing; ++i) {
            emptyEntry = emptyEntry.concat(" ");
        }
        return emptyEntry.concat(dist).concat(emptyEntry2);
    }

    /**
     * Generates a header spacing string with the given length.
     * @param length the length of the spacing string
     * @return the spacing string
     */
    private static String generateEmptyHeader(final int length) {
        String spacingString = "";
        for (int i = 0; i < length; ++i) {
            spacingString = spacingString.concat(" ");
        }
        return spacingString;
    }

    /**
     * Generates a string representing a single empty cell in a matrix with the given width.
     * @param width the width of the cell
     */
    private static String generateEmptyEntry(final int width) {
        String emptyEntry = "";
        for (int i = 0; i < width; ++i) {
            emptyEntry = emptyEntry.concat(" ");
        }
        return emptyEntry;
    }

    /**
     * Generates a "_" line with the given length.
     * @param width
     * @return the line
     */
    private static String generateLine(final int width) {
        String line = "";
        for (int i = 0; i <= width; ++i) {

            line = line.concat("_");
        }
        return line;
    }

    /**
     * Generates the distance string in case only two genomes were compared.
     * @param dist the distances
     * @param scenario scenario index
     * @param genomeID1 id of the fst genome
     * @param genomeID2 id of the scnd genome
     * @return the distance string
     */
    private static String getDistString(final int[] dist, final Scenario scenario, final String genomeID1,
            final String genomeID2) {
        String distance = "";
        String distItself = "-";
        if (scenario != Scenario.ALL) {
            if (dist[0] != -1) {
                distItself = String.valueOf(dist[0]);
            }
            distance = scenario.getGuiName().concat(OutputPrinter.DIST_OF_G).concat(genomeID1).concat("\" & \"").concat(genomeID2).concat("\"" + OutputPrinter.EQUALS)
                    + distItself;
        } else {
            for (int j = 0; j < dist.length; ++j) {
                if (dist[j] != -1) {
                    String scenarioSt = Scenario.getScenario(j + 1).getGuiName(); // Toolz.getScenarioString(j + 1, 0);
                    distance = distance.concat(scenarioSt).concat(OutputPrinter.DIST_OF_G + genomeID1).concat("\" & \"").concat(genomeID2 + "\"").concat(OutputPrinter.EQUALS)
                            + dist[j] + Constants.LINE_BREAK_OUTPUT;
                } // else case would mean this distance is 0, no need to handle it here
            }
        }
        return distance;
    }

    /**
     * Returns the String for the currently printed sorting scenario.
     * @param scenario the scenario to show
     * @param scndIndex
     * @param fstIndex
     * @return the message to show
     */
    private static String getMsg(final Scenario scenario, final String fstIndex, final String scndIndex) {
        String msg = "";
        switch (scenario) {
            case DCJ:
                msg = "DCJ sorting scenario of the genomes \"";
                break;
            case DCJ_RESTRICTED:
                msg = "Restricted DCJ sorting scenario of the genomes \"";
                break;
            case HP:
                msg = "HP sorting scenario of the genomes \"";
                break;
            case INVERSION:
                msg = "Inversion sorting scenario of the genomes \"";
                break;
            case TRANSLOCATION:
                msg = "Translocation sorting scenario of the genomes \"";
                break;
            default:
                msg = "No scenario could be obtained.";
                break;
        }
        return msg.concat(fstIndex.concat("\" & \"".concat(scndIndex))).concat("\":".concat(Constants.LINE_BREAK));
    }
}
