package de.unibi.cebitec.gi.unimog.algorithms;

import java.util.HashMap;

import de.unibi.cebitec.gi.unimog.datastructure.AdjacencyGraph;
import de.unibi.cebitec.gi.unimog.datastructure.Data;
import de.unibi.cebitec.gi.unimog.datastructure.IAdditionalData;
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
 * Class which implements the greedy DCJ sorting algorithm. Runs in linear time.
 */
public class SortingDCJ implements ISorting {

	@Override
	public OperationList findOptSortSequence(final Data data, final IAdditionalData additionalData,
			final HashMap<Integer, Integer> chromMapA) {
		final OperationList operationList = new OperationList(); 
		final AdjacencyGraph graph = data.getAdjGraph();
		
		int[] adjacenciesG1 = graph.getAdjacenciesGenome1().clone();
		int[] adjacenciesG2 = graph.getAdjacenciesGenome2().clone();
		
		/*
		 * i is the first entry of the current adjacency while adj1 is
		 * the second entry of the first genome and adj2 the second entry
		 * of the second genome. adj3 is the entry linked with adj2 in genome 1.
		 */
		int adj1 = 0;
		int adj2 = 0;
		int adj3 = 0;
		
		for (int i=1; i<adjacenciesG2.length; ++i){
			adj1 = adjacenciesG1[i]; 
			adj2 = adjacenciesG2[i];
			if (i != adj2 && adj1 != adj2){ //i is not telomere & not already in same adjacency in g1 & 2
				//f.e. B: (4,5),   A:(4,1)   ,   (5,2)    = (4,5)   , (2,1)
				//     B: (i,adj2) A:(i,adj1),(adj2,adj3) = (i,adj2), (adj3,adj1)
				adj3 = adjacenciesG1[adj2];
				//Replace old adjacencies
				adjacenciesG1[i] = adj2;
				adjacenciesG1[adj2] = i;
				operationList.addOperation(new Pair<Integer, Integer>(i, adj2));
				//Distinguish between telomere & adjacency in genome1
				if (adj2 == adj3 && i != adj1){
					adjacenciesG1[adj1] = adj1;
					operationList.addOperation(new Pair<Integer, Integer>(adj1, adj1));
				} else //second distinction between telomere & adjacency
					if (i == adj1 && adj2 != adj3){
						adjacenciesG1[adj3] = adj3;
						operationList.addOperation(new Pair<Integer, Integer>(adj3, adj3));
					} else 
					if (i != adj1 && adj2 != adj3){
						adjacenciesG1[adj1] = adj3;
						adjacenciesG1[adj3] = adj1;
						operationList.addOperation(new Pair<Integer, Integer>(adj1, adj3));
					} else {
						operationList.addOperation(new Pair<Integer, Integer>(0, 0));
					}
				
				//save operations
				operationList.addAdjacencyArrayG1(adjacenciesG1.clone());
			} 
			if (i != adj2){
				++i; //To skip second entry of the ordered adjacencies in genome2
			}
			
		}
		for (int i=1; i<adjacenciesG2.length; ++i){
			if (i == adjacenciesG2[i] && i != adjacenciesG1[i]){ 
				adj1 = adjacenciesG1[i]; 
				//f.e. B: (1,1), A:(4,1) = (1,1), (4,4)
				//Replace old adjacencies
				adjacenciesG1[i] = i;
				adjacenciesG1[adj1] = adj1;
				operationList.addOperation(new Pair<Integer, Integer>(i, i));
				operationList.addOperation(new Pair<Integer, Integer>(adj1, adj1));
				operationList.addAdjacencyArrayG1(adjacenciesG1.clone());
			}
		}
		operationList.trimAdjArrayList();
		return operationList;
	}

}
