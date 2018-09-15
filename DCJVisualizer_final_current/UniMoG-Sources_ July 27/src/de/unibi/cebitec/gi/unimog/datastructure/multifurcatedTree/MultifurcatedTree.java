package de.unibi.cebitec.gi.unimog.datastructure.multifurcatedTree;

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
 * Defines a <tt>traversal</tt> on a <tt>multifurcated tree</tt>. Therefore it
 * defines a constructor to create a <tt>root</tt>(which is a <tt>node</tt>) to
 * create a <tt>tree</tt>-object.
 * 
 * @author Pina Krell, Rolf Hilker
 */

public class MultifurcatedTree implements Traversable, Cloneable {

    private Node root;

    /**
     * Constructor to create a <tt>multifurcated tree</tt>.
     * @param root the root
     */
    public MultifurcatedTree(final Node root) {
        this.root = root;
    }

    /**
     * Constructor to create a <tt>multifurcated tree</tt>.
     */
    public MultifurcatedTree() {
        this.root = null;
    }

    /**
     * Checks if the tree is empty.
     * @return <code>true</code> if parentTree is empty, <code>false</code>
     *         otherwise.
     */
    public boolean isTreeEmpty() {
        return (this.root == null);
    }

    /**
     * Returns the root.
     * @return this.root the root
     */
    public Node getRoot() {
        return this.root;
    }

    /**
     * Sets the root.
     * @param root the root
     */
    public void setRoot(final Node root) {
        this.root = root;
    }

    /**
     * Inherited method of the interface <tt>Traversable</tt> Implements the
     * <tt>traversal</tt> of a Tree (means the traversal of a <tt>node</tt>,
     * e.g. the <tt>root</tt>.
     * @param nodeVisitor The visitor
     */
    public void traverse(final NodeVisitor nodeVisitor) {
        this.getRoot().topDown(nodeVisitor);
    }

    /**
     * Goes through the tree bottom-up.
     * @param nodeVisitor the visitor
     */
    public void bottomUp(final NodeVisitor nodeVisitor) {
        this.getRoot().bottomUp(nodeVisitor);
    }

    /**
     * Goes to the tree topDown.
     * @param nodeVisitor The visitor
     */
    public void topDown(final NodeVisitor nodeVisitor) {
        this.getRoot().topDown(nodeVisitor);
    }

    @Override
    public MultifurcatedTree clone() {
        return new MultifurcatedTree(this.root.clone());
    }
}