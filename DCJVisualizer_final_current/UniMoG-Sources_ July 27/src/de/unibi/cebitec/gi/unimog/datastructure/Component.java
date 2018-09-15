package de.unibi.cebitec.gi.unimog.datastructure;

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
 * Class that provides a data structure for saving components.
 */
public class Component {

	private int startIndex;
	private int endIndex;
	private int type;
	private boolean oriented;

	/**
	 * Standard constructor.
	 * @param startIndex The start index of this component
	 * @param endIndex The end index of this component
	 * @param type The type of this component
	 * @param oriented <code>true</code> if oriented and <code>false</code> otherwise.
	 */
	public Component(final int startIndex, final int endIndex, final int type, final boolean oriented) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.type = type;
		this.oriented = oriented;
	}

	/**
	 * Returns the start index of this component. If you are in a HP, Trans
	 * or Inversion distance scenario this will be the index of the gene
	 * in the capped genome!
	 * @return the startIndex
	 */
	public int getStartIndex() {
		return this.startIndex;
	}

	/**
	 * Returns the end index of this component.If you are in a HP, Trans
	 * or Inversion distance scenario this will be the index of the gene
	 * in the capped genome!
	 * @return the endIndex
	 */
	public int getEndIndex() {
		return this.endIndex;
	}

	/**
	 * Returns the type of this component. (1-8)
	 * @return the type
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * Returns if the component is oriented or not.
	 * @return <code>true</code> if oriented and <code>false</code> otherwise.
	 */
	public boolean isOriented() {
		return this.oriented;
	}

}
