package de.unibi.cebitec.gi.unimog.algorithms;

import de.unibi.cebitec.gi.unimog.datastructure.AdjacencyGraph;
import de.unibi.cebitec.gi.unimog.datastructure.Data;
import de.unibi.cebitec.gi.unimog.datastructure.IAdditionalData;
import de.unibi.cebitec.gi.unimog.datastructure.multifurcatedTree.MultifurcatedTree;

/**
 *
 * @author Corinna Sickinger
 */
public class DistanceResDCJ implements IDistance {

    @Override
    public int calculateDistance(Data data, IAdditionalData additionalData) {
        final AdjacencyGraph adjacencyGraph = data.getAdjGraph();
        final int n = adjacencyGraph.getNbOfGenes();
        final int c = adjacencyGraph.getNumberOfCycles();
        final int pO = adjacencyGraph.getNumberOfOddPaths();

        final int resDCJDistance = n - (c + (pO / 2));
        return resDCJDistance;
    }

    @Override
    public int calculateDistance(Data data, IAdditionalData additionalData, MultifurcatedTree componentTree) {
        //not needed for restricted DCJ.
        return 0;
    }
}
