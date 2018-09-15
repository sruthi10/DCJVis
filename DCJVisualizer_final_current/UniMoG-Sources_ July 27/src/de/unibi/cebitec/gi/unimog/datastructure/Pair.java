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
 * Class for creating a Pair of two Objects.
 * @param <T1> The first element of the Pair, has to be some object.
 * @param <T2> The second element of the Pair, can be another object.
 */
public class Pair<T1, T2> {

	private T1 o1;
	private T2 o2;

	/**
	 * Constructor for a new Pair.
	 * @param o1 The First object
	 * @param o2 The second object
	 */
	public Pair(T1 o1, T2 o2) {
		this.o1 = o1;
		this.o2 = o2;
	}

	/**
	 * Returns the first element of the Pair.
	 * @return The first element of the Pair.
	 */
	public T1 getFirst() {
		return this.o1;
	}

	/**
	 * Returns the second element of the Pair.
	 * @return The second element of the Pair.
	 */
	public T2 getSecond() {
		return this.o2;
	}

	/**
	 * Sets the first element of the Pair.
	 * @param o The first element of the Pair.
	 */
	public void setFirst(T1 o) {
		this.o1 = o;
	}

	/**
	 * Sets the second element of the Pair.
	 * @param o The second element of the Pair.
	 */
	public void setSecond(T2 o) {
		this.o2 = o;
	}

	/**
	 * Returns if two objects are the same or not.
	 * @param o1 First object
	 * @param o2 Second object
	 * @return <code>true</code> if the first object equals the second one,
	 *         <code>false</code> otherwise.
	 */
	public static boolean same(Object o1, Object o2) {
		return o1 == null ? o2 == null : o1.equals(o2);
	}

	/**
	 * Returns if a another Pair equals this Pair.
	 * @param obj The object to test if it is equal to this Pair.
	 * @return <code>true</code> if both elements of both Pairs are the same,
	 *         <code>false</code> otherwise.
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof Pair<?, ?>)) {
			return false;
		}
		Pair<?, ?> p = (Pair<?, ?>) obj;
		return Pair.same(p.o1, this.o1) && Pair.same(p.o2, this.o2);
	}

	/**
	 * Returns the Pair as String.
	 * @return The Pair as String.
	 */
	public String toString() {
		return "Pair{" + this.o1 + ", " + this.o2 + "}";
	}
}
