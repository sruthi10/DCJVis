package de.unibi.cebitec.gi.unimog.algorithms;

import java.util.ArrayList;

import de.unibi.cebitec.gi.unimog.datastructure.AdditionalDataHPDistance;
import de.unibi.cebitec.gi.unimog.datastructure.Component;
import de.unibi.cebitec.gi.unimog.datastructure.Data;
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
 * This class provides a method to preprocess a pair of genomes for calculation of a
 * HP based distance (HP, Inversion or Translocation distance) via the DCJ model.
 * Here the component identification is carried out and the component tree is
 * constructed and returned to calculate the desired distance afterwards.
 */
public class HPBasedDistPreprocessing {

    private ArrayList<Component> components;
    private MultifurcatedTree compTree;
    private int[] compStarts;
    private int[] nodeToCompMap;

    /**
     * Standard constructor for a new preprocessing.
     * @param data data containing genomes
     * @param additionalData additional data object
     */
    public HPBasedDistPreprocessing(final Data data, final AdditionalDataHPDistance additionalData) {
        this.hpBasedDistPreprocess(data, additionalData);
    }

    /**
     * Utility method for preprocessing a pair of genomes for calculation of a
     * HP based distance (HP, Inversion or Translocation distance) via the DCJ model.
     * Here the component identification is carried out and the component tree is
     * constructed and returned to calculate the desired distance afterwards.
     * @param data Data object containing both genomes
     * @param additionalData Additional data necessary for calculation.
     * return The component tree necessary for distance calculation of HP, Inv & Trans.
     */
    protected void hpBasedDistPreprocess(final Data data,
            final AdditionalDataHPDistance additionalData) {

        //Components are identified:
        final ComponentIdentification compIdent = new ComponentIdentification(data, additionalData);
        this.components = compIdent.getComponents();

        //Component tree is constructed:
        final CompTreeGeneration compTreeGen = new CompTreeGeneration(this.components,
                additionalData.getGenomeCappedPlusArray().length);

        this.compTree = compTreeGen.getComponentTree();
        this.compStarts = compTreeGen.getCompStarts();
        this.nodeToCompMap = compTreeGen.getNodeToCompMap();
    }

    /**
     * Returns the components.
     * @return the components
     */
    public ArrayList<Component> getComponents() {
        return this.components;
    }

    /**
     * Returns the component tree.
     * @return the compTree
     */
    public MultifurcatedTree getCompTree() {
        return this.compTree;
    }

    /**
     * Returns the array containing for each index in the capped genome
     * if a component starts there and its index in the component array.
     * @return component starts array
     */
    public int[] getCompStarts() {
        return this.compStarts;
    }

    /**
     * Returns the mapping of each node to the index of its associated
     * component in the component array.
     * @return the nodeToCompMap
     */
    public int[] getNodeToCompMap() {
        return this.nodeToCompMap;
    }
}
