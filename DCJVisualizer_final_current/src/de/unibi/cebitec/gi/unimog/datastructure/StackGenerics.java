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
 * Creates a generic Stack that serves the typical stack features.
 * Functions with Last in First out principle.
 * @param <T> some object
 */
public class StackGenerics<T extends Object> {

	private static final int EMPTY_INDEX = -1;
	private final int capacity;
	private T[] realStack;
	private int top = StackGenerics.EMPTY_INDEX;

	/**
	 * Constructor for a new Integer Stack object. The capacity must be given.
	 * @param capacity capacity of the stack
	 */
	@SuppressWarnings("unchecked")
	public StackGenerics(int capacity) {
		this.capacity = capacity;
		final Object[] stack = new Object[this.capacity];
		this.realStack = (T[]) stack;
	}

	/**
	 * Returns if the stack is empty.
	 * @return <code>true</code> if it's empty, <code>false</code> otherwise
	 */
	public boolean isEmpty() {
		if (this.top > StackGenerics.EMPTY_INDEX) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Adds one element to the top of the stack.
	 * @param insertValue the value to be inserted
	 * @return the inserted value
	 */
	public T push(T insertValue) {
		this.realStack[++this.top] = insertValue;
		return insertValue;
	}

	/**
	 * Removes the top element of the stack. If the stack is empty
	 * <code>null</code> will be returned.
	 * @return the value of the removed element
	 */
	public T pop() {
		if (this.isEmpty()) {
			return null; // or some error message?
		} else {
			return this.realStack[this.top--];
		}
	}

	/**
	 * Returns the top element of the stack without changing anything. If the
	 * stack is empty <code>null</code> will be returned.
	 * @return last element of stack
	 */
	public T peek() {
		if (this.isEmpty()) {
			return null; // or some error message?
		} else {
			return this.realStack[this.top];
		}
	}

	/**
	 * Returns the size of the stack.
	 * @return the size
	 */
	public int getSize() {
		return this.top + 1;
	}

	/**
	 * Returns the capacity of the stack.
	 * @return the capacity of the stack
	 */
	public int getCapacity() {
		return this.capacity;
	}

}
