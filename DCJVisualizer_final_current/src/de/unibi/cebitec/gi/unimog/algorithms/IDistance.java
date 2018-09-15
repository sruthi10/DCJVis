package de.unibi.cebitec.gi.unimog.algorithms;

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
 * Interface for all classes that have to calculate distances.
 * 
 */
public interface IDistance {

	/**
	 * Method for calculating the distance of two genomes according to their
	 * adjacency graph.
	 * @param data The data object containing all information
	 * @param additionalData The additional data which may be needed
	 * @return The distance between both genomes
	 */
	public int calculateDistance(final Data data, final IAdditionalData additionalData);
	
	/**
	 * Method for calculating the distance of two genomes according to their
	 * adjacency graph, if the preprocessing has already been carried out. Saves
	 * a lot of running time, because component identification is not carried out twice then.
	 * @param data The data object containing all information
	 * @param additionalData The additional data which may be needed
	 * @param componentTree the component tree
	 * @return The distance between both genomes
	 */
	public int calculateDistance(final Data data, final IAdditionalData additionalData, final MultifurcatedTree componentTree);

}
