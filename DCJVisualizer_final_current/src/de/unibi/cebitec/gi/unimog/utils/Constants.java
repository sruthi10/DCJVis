package de.unibi.cebitec.gi.unimog.utils;

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
 * This class contains all global constants used in this framework.
 */
public final class Constants {

	/** Line break string "\n". */
	public static final String LINE_BREAK = "\n";
	
	/** Line break for output: "\r\n". */
	public static final String LINE_BREAK_OUTPUT = "\r\n";

	/** Error String. */
	public static final String ERROR = "Error: At least 2 genomes necessary";
	
	/** Number of the DCJ distance scenario. */
	public static final byte DCJ = 1;
        
        /** Number of the restricted DCJ distance scenario. */
        public static final byte RDCJ = 2;
	
	/** Number of the HP distance via DCJ scenario. */
	public static final byte HP = 3;
	
	/** Number of the Inversion distance via DCJ scenario. */
	public static final byte INV = 4;
	
	/** Number of the Translocation distance via DCJ scenario. */
	public static final byte TRANS = 5;
	
	/** Number of the all distances via DCJ scenario. */
	public static final byte ALL = 6;
	
	/** Number representing an error while calculating a distance. */
	public static final int ERROR_NUM = -1;
	
	/** Genome example for gui. */
	public static final String GENOME_EXAMPLE = ">Genome A\n1 2 -3 4 5 6 | 7 9 -10 11 -12 13 -14 15 -8 16 | 17 -18 19 )".concat(
			"\n\n>Genome B\n1 2 3 4 5 6 | 7 8 9 10 11 12 13 14 15 16 | 17 18 19 )\n".concat(
					"\n>Genome C\ngene1 gene6 gene9 -gene5 -gene4 | -gene3 -gene2 gene7 gene8 |\n")).concat(
					"\n>Genome D\ngene1 -gene2 gene6 gene7 gene3 | gene4 gene5 -gene9 gene8 |\n");

	/** Number representing the open file chooser event. */
	public static final int OPEN = 0;
	
	/** Number representing the save text event. */
	public static final int SAVE_TXT = 1;
	
	/** Number representing the save image event. */
	public static final int SAVE_IMG = 2;
	
	/** Output Style 1: Just Black & white with colored fragments. */
	public static final int OUTPUT_1 = 0;
	
	/** Output Style 2: Colored Chromosomes. */
	public static final int OUTPUT_2 = 1;
	
	/** Zoom factor for output = 0.7. */
	public static final double ZOOM_FACTOR1 = 0.7143;
	
	/** Zoom factor for output = 1. */
	public static final double ZOOM_FACTOR2 = 1;
	
	/** Zoom factor for output = 1.5. */
	public static final double ZOOM_FACTOR3 = 1.5714;
	
	/** 'DCJ' String. */
	public static final String DCJ_ST = "DCJ";
	
	/** 'HP' String. */
	public static final String HP_ST = "HP";
	
	/** 'Translocation' String. */
	public static final String TRANS_ST = "Translocation";
	
	/** 'Inversion' String. */
	public static final String INV_ST = "Inversion";
	
	/** 'Restricted DCJ' String. */
	public static final String RDCJ_ST = "Restricted DCJ";
	
	/** int for boolean 'true' = 1. */
	public static final int TRUE = 1;
	
	/** int for boolean 'false' = 0. */
	public static final int FALSE = 0;
	
	/** number of extremities of a cut = 4. */
	public static final int NB_EXTREMITIES = 4;
	
	/** Number of a PI cap = 1. */
	public static final int PI_CAP = 1;
	
	/** Number of an L cap = 2. */
	public static final int L_CAP = 2;

	/** String displaying help for the console modus. */
	public static final String CONSOLE_HELP = "The software needs two parameters in console modus:\r\n- At first the " +
			"scenario: 1 = DCJ, 2 = HP, 3 = Inversion, 4 = Translocation, 5 = All scenarios at once\r\n- Second the " +
			"path to a single file containing all genomes which should be analyzed.\r\n\r\nIf no correct parameters are " +
			"handed over the software starts with a graphical user interface.\r\nIf more help is needed, start the program " +
			"in GUI mode first and click the 'Help' button.";
	
	/**
	 * Do not instantiate.
	 */
	private Constants(){
		
	}
}
