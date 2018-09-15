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
 * Contains the output operation list, which contains the original gene names
 * instead of the internal integer values for one genome comparison. 
 */

public class OutOperationList {
	
	private ArrayList<Pair<String, String>> operationList = new ArrayList<Pair<String, String>>();
	private ArrayList<String[]> adjacencyArrayListG1 = new ArrayList<String[]>();

	/**
	 * An output operation list needs the list of operations in original gene name representation
	 * and the adjacencies of the initial genome for one genome comparison. 
	 * @param operationList the list of operations
	 * @param adjacencyArrayListG1 the adjacencies of the initial genome
	 */
	public OutOperationList(final ArrayList<Pair<String, String>> operationList, 
			final ArrayList<String[]> adjacencyArrayListG1){
		this.operationList = operationList;
		this.adjacencyArrayListG1 = adjacencyArrayListG1;
	}
	
	/**
	 * Returns the list of operations needed for an optimal sorting scenario.
	 * @return the operationList
	 */
	public ArrayList<Pair<String, String>> getOperationList() {
		return this.operationList;
	} 

	/**
	 * Returns the complete list of adjacency arrays for a sorting scenario.
	 * @return the adjacencyArrayList for genome 1
	 */
	public ArrayList<String[]> getAdjacencyArrayListG1() {
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
}
