package de.unibi.cebitec.gi.unimog.algorithms;

import java.util.ArrayList;

import de.unibi.cebitec.gi.unimog.datastructure.Component;
import de.unibi.cebitec.gi.unimog.datastructure.multifurcatedTree.MultifurcatedTree;
import de.unibi.cebitec.gi.unimog.datastructure.multifurcatedTree.Node;
import de.unibi.cebitec.gi.unimog.datastructure.multifurcatedTree.NodeType;

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
 * A path that contains two or more white or gray components, or one white and one
 * gray component, is called a long path. A path that contains only one white or one gray
 * component, is a short path.
 * 1. The cost of a short path is 1.
 * 2. The cost of a long path with only two gray components is 1.
 * 3. The cost of all other long paths is 2.
 * 
 */
public final class CompTreeGeneration {

    private int[] compStarts;
    private int[] compEnds;
    private MultifurcatedTree componentTree;
    private int[] nodeToCompMap;

    /**
     * Default constructor.
     * @param components The components for the tree
     * @param size size of the capped genome
     */
    public CompTreeGeneration(final ArrayList<Component> components, final int size) {
        this.generateStartAndEndArray(components, size);
        this.componentTree = this.generateCompTree(components, size);
    }

    /**
     * Generates two arrays: The first one contains at each index of the capped
     * genome if a component starts at this index (indicated by value >-1) 
     * which represents the index of the component in the component array. 
     * The second array contains for each index of the capped genome if a component
     * ends at this index (indicated by a value >-1) which represents the index of the
     * component in the component array.	
     * @param components The components
     * @param size size of the capped genome
     */
    private void generateStartAndEndArray(final ArrayList<Component> components, final int size) {
        this.compStarts = new int[size];
        this.compEnds = new int[size];
        //initialize with negative values
        for (int i = 0; i < size; ++i) {
            this.compStarts[i] = -1;
            this.compEnds[i] = -1;
        }
        //put the indices in the arrays
        Component comp;
        for (int i = 0; i < components.size(); ++i) {
            comp = components.get(i);
            this.compStarts[comp.getStartIndex()] = i;
            this.compEnds[comp.getEndIndex()] = i;
        }
    }

    /**
     * Generates the component tree (grey, white, black-tree).
     * @param components The components
     * @param size size of the capped genome
     * @return Component tree
     */
    public MultifurcatedTree generateCompTree(ArrayList<Component> components, final int size) {

        Node currentNode = new Node(NodeType.BLACK, null);
        Node square = new Node(NodeType.SQUARE, null);
        MultifurcatedTree compTree = new MultifurcatedTree(currentNode);
        this.nodeToCompMap = new int[components.size()];
        int compIndex = 0;
        ArrayList<Integer> levelHasComps = new ArrayList<Integer>(); //if it has comps, go up in tree

        for (int i = 0; i < size; ++i) {
            if (this.compStarts[i] > -1 && this.compEnds[i] == -1) {
                levelHasComps.add(0); //0 is added at the end, if it remains 0 no comp was added until end of level
            }
            if (this.compStarts[i] > -1 && this.compEnds[i + 1] == -1) { //means compLength > 2			
                if (this.compEnds[i] == -1 || levelHasComps.get(levelHasComps.size() - 1) == 0) {
                    square = new Node(NodeType.SQUARE, null); //if no comps already in level, a square is added
                    currentNode.addChild(square);
                }
                final NodeType nodeType = CompTreeGeneration.getNodeType(components.get(this.compStarts[i]));
                currentNode = new Node(nodeType, null);
                square.addChild(currentNode);
                levelHasComps.set(levelHasComps.size() - 1, 1); //since comp has been added to level, it is set to 1

                //generate arrays 4 data structure
                this.nodeToCompMap[compIndex] = this.compStarts[i];
                ++compIndex;
            } else if (this.compEnds[i] > -1 && this.compStarts[i] == -1) {
                if (levelHasComps.size() > 0) {
                    if (levelHasComps.get(levelHasComps.size() - 1) == 1) {//only if current level has a comp, move up in tree
                        currentNode = square.getParent(); //TODO check if line above is correct now with size test
                        if (!currentNode.isRoot()) {
                            square = currentNode.getParent();
                        }
                    }
                    levelHasComps.remove(levelHasComps.size() - 1); //move up one level in any case
                }
            }
        }
        return compTree;
    }

    /**
     * Returns the NodeType of the Component handed over to
     * the method.
     * @param comp The Component whose type should be returned
     * @return NodeType of the Component handed over to the method.
     */
    public static NodeType getNodeType(final Component comp) {

        NodeType nodeType;
        if (comp.isOriented()) {
            nodeType = NodeType.BLACK;
        } else {
            int type = comp.getType();
            if (type == 1 || type == 5) {
                nodeType = NodeType.WHITE;
            } else {
                nodeType = NodeType.GREY;
            }
        }
        return nodeType;
    }

    /**
     * Returns the component tree belonging to this instance.
     * @return the component tree
     */
    public MultifurcatedTree getComponentTree() {
        return this.componentTree;
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
