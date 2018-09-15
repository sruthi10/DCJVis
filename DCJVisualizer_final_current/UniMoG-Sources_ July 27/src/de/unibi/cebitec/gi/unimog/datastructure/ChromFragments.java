package de.unibi.cebitec.gi.unimog.datastructure;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

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
 * Represents a fragment of a chromosome. Fragments contain ...
 * and can be used for coloring the background of a chromosome in different
 * colors in case it contains cut positions. Thereby the fragments of a cut
 * are highlighted.
 */
public class ChromFragments {
	
	private boolean empty;
	private boolean[] beforeGenes;
	private String fstCutGene;
	private String scndCutGene;
	private ArrayList<Pair<String, Color>> fragmentColors;
	private HashMap<String, Color> colorMap;

	/**
	 * Creates a new instance with:
	 * @param fstCutGene One gene of the first cut position in the next step
	 * @param scndCutGene One gene of the second cut position in the next step
	 * @param beforeGenes array determining if the cut was before or after the fst & scnd cut genes 
	 * @param fragmentColors the colors of the corresponding fragments
	 */
	public ChromFragments(final String fstCutGene, final String scndCutGene, 
			final boolean[] beforeGenes, final ArrayList<Color> fragmentColors) {
		this.fstCutGene = fstCutGene;
		this.scndCutGene = scndCutGene;
		this.beforeGenes = beforeGenes;
		this.empty = false;
	}
	
	/**
	 * Constructor if no fragments are present yet or at all.
	 * IsEmpty is set to true until data is added if this constructor is used.
	 */
	public ChromFragments(){
		this.empty = true;
	}

	/**
	 * Returns the boolean value indicating if the first cut extremity
	 * is cut before or after its gene.
	 * @return the boolean array
	 */
	public boolean[] getBeforeGenes(){
		return this.beforeGenes;
	}
	
	/**
	 * Returns the boolean value indicating if the first cut extremity
	 * is cut before or after its gene.
	 * @return the boolean array
	 */
	public String getNeighbour(){
		return this.scndCutGene;
	}

	/**
	 * Returns the fst cut gene if it is located in this chromosome.
	 * @return the fst cut gene
	 */
	public String getGene() {
		return this.fstCutGene;
	}
	
	/**
	 * Returns true if there are no fragments of the last step available 
	 * for the current step.
	 * @return true if no fragments available, false otherwise
	 */
	public boolean isEmpty(){
		return this.empty;
	}

	/**
	 * The array determining if a cut is located before or after the 
	 * two genes "fstCutGene" and "scndCutGene".
	 * @param beforeGenes the beforeGenes to set
	 */
	public void setBeforeGenes(final boolean[] beforeGenes) {
		this.beforeGenes = beforeGenes;
		this.empty = false;
	}

	/**
	 * The gene belonging to the first cut.
	 * @param fstCutGene the first cut gene to set
	 */
	public void setGene(final String fstCutGene) {
		this.fstCutGene = fstCutGene;
		this.empty = false;
	}

	/**
	 * Sets the the gene belonging to the second cut position
	 * after an operation has been performed.
	 * @param scndCutGene the second cut gene to set
	 */
	public void setNeighbour(final String scndCutGene) {
		this.scndCutGene = scndCutGene;
		this.empty = false;
	}

	public void setFragmentColors(ArrayList<Pair<String, Color>> fragmentColors) {
		this.fragmentColors = fragmentColors;
	}
	
	public ArrayList<Pair<String, Color>> getFragmentColors() {
		return this.fragmentColors;
	}

	public void setColorMap(HashMap<String, Color> colorMap) {
		this.colorMap = colorMap;		
	}
	
	public HashMap<String, Color> getColorMap(){
		return this.colorMap;
	}
	
}
