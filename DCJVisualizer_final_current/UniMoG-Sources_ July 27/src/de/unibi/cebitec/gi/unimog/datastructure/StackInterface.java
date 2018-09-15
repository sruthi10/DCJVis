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
 * Creates a Stack of one Object type that serves the typical stack
 * features. Functions with Last in First out principle.
 */
public interface StackInterface {

	/**
	 * Returns if the stack is empty.
	 * @return <code>true</code> if it's empty, <code>false</code> otherwise
	 */
	public boolean isEmpty();

	/**
	 * Adds one element to the top of the stack.
	 * @param insertValue the value to be inserted
	 * @return the inserted value
	 */
	public Object push(Object insertValue);

	/**
	 * Removes the top element of the stack. If the stack is empty
	 * <code>-1</code> will be returned.
	 * @return the value of the removed element
	 */
	public Object pop();

	/**
	 * Returns the top element of the stack without changing anything. If the
	 * stack is empty <code>-1</code> will be returned.
	 * @return last element of stack
	 */
	public Object peek();

	/**
	 * Returns the size of the stack.
	 * @return the size
	 */
	public int getSize();

	/**
	 * Returns the capacity of the stack.
	 * @return the capacity of the stack
	 */
	public int getCapacity();

}
