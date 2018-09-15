package de.unibi.cebitec.gi.unimog.algorithms;

import de.unibi.cebitec.gi.unimog.datastructure.AdjacencyGraph;
import de.unibi.cebitec.gi.unimog.datastructure.Data;
import de.unibi.cebitec.gi.unimog.datastructure.IAdditionalData;
import de.unibi.cebitec.gi.unimog.datastructure.multifurcatedTree.MultifurcatedTree;

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
 * Class for calculating the DCJ distance between two given genomes. 
 * DCJ distance: N-(C+I/2)
 */
public class DistanceDCJ implements IDistance {

    @Override
    public int calculateDistance(final Data data, final IAdditionalData additionalData) {
                                                            //#2nd parameter mot used/ignore
        final AdjacencyGraph adjacencyGraph = data.getAdjGraph();
        final int n = adjacencyGraph.getNbOfGenes();
        final int c = adjacencyGraph.getNumberOfCycles();
        final int i = adjacencyGraph.getNumberOfOddPaths();

        final int dcjDistance = n - (c + (i / 2));
        return dcjDistance;
    }

    @Override
    public int calculateDistance(Data data, IAdditionalData additionalData,
            MultifurcatedTree componentTree) {
        // Not needed for dcj distance
        return 0;
    }
}
