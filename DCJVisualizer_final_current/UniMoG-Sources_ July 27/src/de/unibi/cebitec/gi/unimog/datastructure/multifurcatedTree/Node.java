package de.unibi.cebitec.gi.unimog.datastructure.multifurcatedTree;

import java.util.Vector;

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
 * Defines a <tt>node</tt> constructor to create a new <tt>node</tt> and has
 * some methods to manipulate a created <tt>node</tt>.
 * 
 * @author Pina Krell, Rolf Hilker
 */

public class Node implements Traversable, Cloneable {

    // Vector with the children of a node
    private Vector<Node> nodeChildren;
    // Parent of the Node
    private Node parent;
    private NodeType nodeType;

    /**
     * Constructs a <tt>node</tt> with a given name, parent, an incoming edge
     * length and a key-object to be stored in the <tt>node</tt> .
     * @param nodeType The node type to store within the current <tt>node</tt>
     * @param parent A <tt>node</tt> which is the parent of the <tt>node</tt> to be
     *            created
     */
    public Node(final NodeType nodeType, final Node parent) {
        this.nodeType = nodeType;
        if (parent != null) {
            parent.addChild(this);
        }
        this.nodeChildren = new Vector<Node>();
    }

    /**
     * Returns the depth of the Node.
     * @return depth the depth of the Node
     */
    public Integer getDepth() {
        Node node = this;
        Integer depth = 0;
        while (!node.isRoot()) {
            node = node.getParent();
            depth++;
        }
        return depth;
    }

    /**
     * Clones the node.
     * @return newNode the copy of the original node.
     */
    @Override
    public Node clone() {

        Node newNode = null;
        if (this.nodeType == null) {
            newNode = new Node(null, null);
        } else {
            newNode = new Node(this.nodeType, null);
        }
        if (!this.isLeaf()) {
            for (Node n : this.getNodeChildren()) {
                newNode.getNodeChildren().add(n.clone());
                newNode.getNodeChildren().lastElement().parent = newNode;
            }
        }
        return newNode;
    }

    /**
     * Bottom up through the tree.
     * @param nodeVisitor the nodeVisitor
     */
    public void bottomUp(final NodeVisitor nodeVisitor) {
        for (Node nodeChild : this.nodeChildren) {
            nodeChild.bottomUp(nodeVisitor);
        }
        nodeVisitor.visit(this);
    }

    /**
     * Top down through the tree.
     * @param nodeVisitor the nodeVisitor
     */
    public void topDown(final NodeVisitor nodeVisitor) {
        nodeVisitor.visit(this);

        for (Node nodeChild : this.nodeChildren) {
            nodeChild.topDown(nodeVisitor);
        }
    }

    /**
     * Adds a new child (also a <tt>node</tt>) to a <tt>nodes</tt> vector of
     * children.
     * @param newChild The <tt>node</tt> to be set as a new child
     */
    public void addChild(final Node newChild) {
        this.nodeChildren.add(newChild);
        newChild.parent = this;
    }

    /**
     * Deletes all children (<tt>nodes</tt>) of a <tt>node</tt>.
     */
    public void clearChildren() {
        this.nodeChildren.clear();
    }

    /**
     * Parses the object stored in a <tt>node</tt> to String.
     * @return key The key object parsed to a string
     */
    public String toString() {
        final StringBuilder out = new StringBuilder();
        if (!this.isLeaf()) {
            out.append('(');
            for (Node nodeChild : this.nodeChildren) {
                out.append(nodeChild.toString() + ",");
            }
            out.deleteCharAt(out.length() - 1);
            out.append(")");
        }
        return out.toString();
    }

    /**
     * Traverses a <tt>node</tt> with different visitors.
     * @param nodeVisitor Visitor-type with which the <tt>node</tt> should be traversed
     */
    public void traverse(final NodeVisitor nodeVisitor) {
        // visits the actual node
        nodeVisitor.visit(this);
        // calls the method traverse on all children of the actual node
        for (Node nodeChild : this.nodeChildren) {
            nodeChild.traverse(nodeVisitor);
        }
    }

    /**
     * Getter for the parent of a <tt>node</tt>.
     * @return parent The parent <tt>node</tt> of a <tt>node</tt>
     */
    public Node getParent() {
        return this.parent;
    }

    /**
     * Boolean whether a <tt>node</tt> is a <tt>leaf</tt> or not. A
     * <tt>leaf</tt> does not contain children (<tt>nodes</tt>).
     * @return true if the <tt>node</tt> is a leaf
     */
    public boolean isLeaf() {
        return (this.nodeChildren.isEmpty());
    }

    /**
     * Boolean whether a <tt>node</tt> is a <tt>root</tt> of a tree or not.
     * Means that the <tt>node</tt> has no parent.
     * @return true if the <tt>node</tt> is the <tt>root</tt>
     */
    public boolean isRoot() {
        return this.parent == null;
    }

    /**
     * Sets a specific key-object to store within the actual <tt>node</tt>.
     * @param nodeType Node type to store within the <tt>node</tt>
     */
    public void setNodeType(final NodeType nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * Getter for the key-object which is stored in the <tt>node</tt>.
     * @return key A key object which is stored in the <tt>node</tt>
     */
    public NodeType getNodeType() {
        return this.nodeType;
    }

    /**
     * Getter for the vector of children-nodes of the actual <tt>node</tt>.
     * @return nodeChildren The vector of children of a <tt>node</tt>
     */
    public Vector<Node> getNodeChildren() {
        return this.nodeChildren;
    }

    /**
     * Sets a vector of children (<tt>nodes</tt>) to a <tt>node</tt>.
     * @param children A vector of children-nodes
     */
    public void setNodeChildren(final Vector<Node> children) {
        this.nodeChildren = children;
    }
}