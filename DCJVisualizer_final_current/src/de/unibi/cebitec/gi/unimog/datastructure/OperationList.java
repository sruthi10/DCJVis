package de.unibi.cebitec.gi.unimog.datastructure;

import java.util.ArrayList;

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
 * Contains the operation list determining an optimal sorting sequence to convert one
 * genome into a second one. Also holds the list of adjacency graphs for each step
 * of the sorting. Note that the operation of a pair of operations which contains two
 * extremities always has to be the first pair of the two!
 */
public class OperationList {


	private ArrayList<Pair<Integer, Integer>> operationList = new ArrayList<Pair<Integer, Integer>>();
	private ArrayList<int[]> adjacencyArrayListG1 = new ArrayList<int[]>();
	
	/**
	 * Adds a new Pair of adjacencies to the end of the list of operations 
	 * needed for an optimal sorting scenario. Remember to always add the 
	 * pair of operations containing !two distinct extremities! first!
	 * @param operation The operation to set
	 */
	public void addOperation(final Pair<Integer, Integer> operation) {
		this.operationList.add(operation);
	}
	
	/**
	 * Adds a new Pair of adjacencies to the beginning of the list of operations 
	 * needed for an optimal sorting scenario.
	 * @param operation The operation to set
	 */
	public void addOperationBegin(final Pair<Integer, Integer> operation) {
		this.operationList.add(0, operation);
	}

	/**
	 * Returns the list of operations needed for an optimal sorting scenario.
	 * @return the operationList
	 */
	public ArrayList<Pair<Integer, Integer>> getOperationList() {
		return this.operationList;
	} 

	/**
	 * Adds a new adjacency array at the end of the adjacencyArrayList.
	 * @param adjacencyArrayG1 the adjacenciesArray for genome 1 to set
	 */
	public void addAdjacencyArrayG1(final int[] adjacencyArrayG1) {
		this.adjacencyArrayListG1.add(adjacencyArrayG1);
	}

	/**
	 * Returns the complete list of adjacency arrays for a sorting scenario.
	 * @return the adjacencyArrayList for genome 1
	 */
	public ArrayList<int[]> getAdjacencyArrayListG1() {
		return this.adjacencyArrayListG1;
	}

	/**
	 * Trims the adjacencies array list to the current size.
	 * Useful if all array lists for one distance have been
	 * added.
	 */
	public void trimAdjArrayList() {
		this.adjacencyArrayListG1.trimToSize();
		
	}

	/**
	 * Sets this list of operations to the operation list handed over here.
	 * Use with caution, since the adjacency array is not coincident with the
	 * operation list anymore unless it is updated in the correct fashion!
	 * @param operationList the new list of operations
	 */
	public void setOperationList(ArrayList<Pair<Integer, Integer>> operationList) {
		this.operationList = operationList;
		
	}

	/**
	 * Removes the adjacency array with the given index from the list.
	 * @param i the index of the adjacency array to remove
	 */
	public void removeAdjArray(final int i) {
		this.adjacencyArrayListG1.remove(i);
	}

	/**
	 * Adds an adjacency array at the beginning of the arraylist.
	 * @param adjArrayG1 the array to add
	 */
	public void addAdjacencyArrayG1Begin(final int[] adjArrayG1) {
		this.adjacencyArrayListG1.add(0, adjArrayG1);		
	}	
	
}
