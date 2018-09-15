package de.unibi.cebitec.gi.unimog.algorithms;


import java.util.HashMap;

import de.unibi.cebitec.gi.unimog.datastructure.Data;
import de.unibi.cebitec.gi.unimog.datastructure.IAdditionalData;
import de.unibi.cebitec.gi.unimog.datastructure.OperationList;

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
 * Interface for each algorithm that sorts via DCJ operations.
 */

public interface ISorting {

	/**
	 * Method for calculating an optimal sorting sequence to transform of a pair
	 * of genomes the first one into the second one. Saves only the changed adjacencies.
	 * Afterwards only the genome object has to be adapted for printing the result.
	 * The two following operations always belong to one step as each DCJ operation
	 * always changes 2 adjacency-pairs (0,1), (2,3) ...
	 * @param data The basic data
	 * @param additionalData All additional data for the chosen scenario, <code>null</code>
	 * 			if no additional data is needed
	 * @param chromMap Mapping of chromosome numbers to genes of genome A
	 * @return The list of operations performed
	 */
	public OperationList findOptSortSequence(Data data, IAdditionalData additionalData, 
			final HashMap<Integer, Integer> chromMap);
	
}
