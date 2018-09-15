package de.unibi.cebitec.gi.unimog.exceptions;

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
 * Exception that should be thrown if there is an error in the 
 * input or output.
 */

public class InputOutputException extends Exception {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor with error message.
	 * 
	 * @param message
	 *            the error message
	 */
	public InputOutputException(final String message) {
		super(message);
	}

	/**
	 * Constructor with throwable.
	 * 
	 * @param cause
	 *            the cause for the error
	 */
	public InputOutputException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor with error message and throwable.
	 * 
	 * @param message
	 *            the error message
	 * @param cause
	 *            the cause for the error
	 */
	public InputOutputException(String message, Throwable cause) {
		super(message, cause);
	}

}
