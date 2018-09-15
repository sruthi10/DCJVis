package ca.corefacility.gview.utils.geom;
/*
 * Copyright (c) 1997, 2006, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 * 
 * 
 * This code was modified by Aaron Petkau
 */

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;


/**
 * Defines an Arc2D class which can adjust the precision of the curves used to approximate it.
 * @author aaron
 *
 */
public abstract class Arc2DPrecision extends java.awt.geom.Arc2D
{	
	private short minSegments;
	
    /**
     * This class defines an arc specified in {@code float} precision.
     * @since 1.2
     */
    public static class Float extends Arc2DPrecision implements Serializable {
        
        private static final long serialVersionUID = 1L;

        /**
         * The X coordinate of the upper-left corner of the framing
         * rectangle of the arc.
         * @since 1.2
         * @serial
         */
        public float x;

        /**
         * The Y coordinate of the upper-left corner of the framing
         * rectangle of the arc.
         * @since 1.2
         * @serial
         */
        public float y;

        /**
         * The overall width of the full ellipse of which this arc is
         * a partial section (not considering the
         * angular extents).
         * @since 1.2
         * @serial
         */
        public float width;

        /**
         * The overall height of the full ellipse of which this arc is
         * a partial section (not considering the
         * angular extents).
         * @since 1.2
         * @serial
         */
        public float height;

        /**
         * The starting angle of the arc in degrees.
         * @since 1.2
         * @serial
         */
        public float start;

        /**
         * The angular extent of the arc in degrees.
         * @since 1.2
         * @serial
         */
        public float extent;

        /**
         * Constructs a new OPEN arc, initialized to location (0, 0),
         * size (0, 0), angular extents (start = 0, extent = 0).
         * @since 1.2
         */
        public Float() {
            super(OPEN);
            
            calculateMinSegments();
        }

        /**
         * Constructs a new arc, initialized to location (0, 0),
         * size (0, 0), angular extents (start = 0, extent = 0), and
         * the specified closure type.
         *
         * @param type The closure type for the arc:
         * {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
         * @since 1.2
         */
        public Float(int type) {
            super(type);
            
            calculateMinSegments();
        }

        /**
         * Constructs a new arc, initialized to the specified location,
         * size, angular extents, and closure type.
         *
         * @param x The X coordinate of the upper-left corner of
         *          the arc's framing rectangle.
         * @param y The Y coordinate of the upper-left corner of
         *          the arc's framing rectangle.
         * @param w The overall width of the full ellipse of which
         *          this arc is a partial section.
         * @param h The overall height of the full ellipse of which this
         *          arc is a partial section.
         * @param start The starting angle of the arc in degrees.
         * @param extent The angular extent of the arc in degrees.
         * @param type The closure type for the arc:
         * {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
         * @since 1.2
         */
        public Float(float x, float y, float w, float h,
                     float start, float extent, int type) {
            super(type);
            this.x = x;
            this.y = y;
            this.width = w;
            this.height = h;
            this.start = start;
            this.extent = extent;
            
            calculateMinSegments();
        }

        /**
         * Constructs a new arc, initialized to the specified location,
         * size, angular extents, and closure type.
         *
         * @param ellipseBounds The framing rectangle that defines the
         * outer boundary of the full ellipse of which this arc is a
         * partial section.
         * @param start The starting angle of the arc in degrees.
         * @param extent The angular extent of the arc in degrees.
         * @param type The closure type for the arc:
         * {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
         * @since 1.2
         */
        public Float(Rectangle2D ellipseBounds,
                     float start, float extent, int type) {
            super(type);
            this.x = (float) ellipseBounds.getX();
            this.y = (float) ellipseBounds.getY();
            this.width = (float) ellipseBounds.getWidth();
            this.height = (float) ellipseBounds.getHeight();
            this.start = start;
            this.extent = extent;
            
            calculateMinSegments();
        }
        

        /**
         * {@inheritDoc}
         * Note that the arc
         * <a href="Arc2D.html#inscribes">partially inscribes</a>
         * the framing rectangle of this {@code RectangularShape}.
         *
         * @since 1.2
         */
        public double getX() {
            return (double) x;
        }

        /**
         * {@inheritDoc}
         * Note that the arc
         * <a href="Arc2D.html#inscribes">partially inscribes</a>
         * the framing rectangle of this {@code RectangularShape}.
         *
         * @since 1.2
         */
        public double getY() {
            return (double) y;
        }

        /**
         * {@inheritDoc}
         * Note that the arc
         * <a href="Arc2D.html#inscribes">partially inscribes</a>
         * the framing rectangle of this {@code RectangularShape}.
         *
         * @since 1.2
         */
        public double getWidth() {
            return (double) width;
        }

        /**
         * {@inheritDoc}
         * Note that the arc
         * <a href="Arc2D.html#inscribes">partially inscribes</a>
         * the framing rectangle of this {@code RectangularShape}.
         *
         * @since 1.2
         */
        public double getHeight() {
            return (double) height;
        }

        /**
         * {@inheritDoc}
         * @since 1.2
         */
        public double getAngleStart() {
            return (double) start;
        }

        /**
         * {@inheritDoc}
         * @since 1.2
         */
        public double getAngleExtent() {
            return (double) extent;
        }

        /**
         * {@inheritDoc}
         * @since 1.2
         */
        public boolean isEmpty() {
            return (width <= 0.0 || height <= 0.0);
        }

        /**
         * {@inheritDoc}
         * @since 1.2
         */
        public void setArc(double x, double y, double w, double h,
                           double angSt, double angExt, int closure) {
            this.setArcType(closure);
            this.x = (float) x;
            this.y = (float) y;
            this.width = (float) w;
            this.height = (float) h;
            this.start = (float) angSt;
            this.extent = (float) angExt;
            
            calculateMinSegments();
        }

        /**
         * {@inheritDoc}
         * @since 1.2
         */
        public void setAngleStart(double angSt) {
            this.start = (float) angSt;
            
            calculateMinSegments();
        }

        /**
         * {@inheritDoc}
         * @since 1.2
         */
        public void setAngleExtent(double angExt) {
            this.extent = (float) angExt;
            
            calculateMinSegments();
        }

        /**
         * {@inheritDoc}
         * @since 1.2
         */
        protected Rectangle2D makeBounds(double x, double y,
                                         double w, double h) {
            return new Rectangle2D.Float((float) x, (float) y,
                                         (float) w, (float) h);
        }

        /**
         * Writes the default serializable fields to the
         * <code>ObjectOutputStream</code> followed by a byte
         * indicating the arc type of this <code>Arc2D</code>
         * instance.
         *
         * @serialData
         * <ol>
         * <li>The default serializable fields.
         * <li>
         * followed by a <code>byte</code> indicating the arc type
         * {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
         * </ol>
         */
        private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException
        {
            s.defaultWriteObject();

            s.writeByte(getArcType());
        }

        /**
         * Reads the default serializable fields from the
         * <code>ObjectInputStream</code> followed by a byte
         * indicating the arc type of this <code>Arc2D</code>
         * instance.
         *
         * @serialData
         * <ol>
         * <li>The default serializable fields.
         * <li>
         * followed by a <code>byte</code> indicating the arc type
         * {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
         * </ol>
         */
        private void readObject(java.io.ObjectInputStream s)
            throws java.lang.ClassNotFoundException, java.io.IOException
        {
            s.defaultReadObject();

            try {
                setArcType(s.readByte());
            } catch (IllegalArgumentException iae) {
                throw new java.io.InvalidObjectException(iae.getMessage());
            }
        }
    }

    /**
     * This class defines an arc specified in {@code double} precision.
     * @since 1.2
     */
    public static class Double extends Arc2DPrecision implements Serializable {   	

        private static final long serialVersionUID = 1L;

        /**
         * The X coordinate of the upper-left corner of the framing
         * rectangle of the arc.
         * @since 1.2
         * @serial
         */
        public double x;

        /**
         * The Y coordinate of the upper-left corner of the framing
         * rectangle of the arc.
         * @since 1.2
         * @serial
         */
        public double y;

        /**
         * The overall width of the full ellipse of which this arc is
         * a partial section (not considering the angular extents).
         * @since 1.2
         * @serial
         */
        public double width;

        /**
         * The overall height of the full ellipse of which this arc is
         * a partial section (not considering the angular extents).
         * @since 1.2
         * @serial
         */
        public double height;

        /**
         * The starting angle of the arc in degrees.
         * @since 1.2
         * @serial
         */
        public double start;

        /**
         * The angular extent of the arc in degrees.
         * @since 1.2
         * @serial
         */
        public double extent;

        /**
         * Constructs a new OPEN arc, initialized to location (0, 0),
         * size (0, 0), angular extents (start = 0, extent = 0).
         * @since 1.2
         */
        public Double() {
            super(OPEN);
            
            calculateMinSegments();
        }

        /**
         * Constructs a new arc, initialized to location (0, 0),
         * size (0, 0), angular extents (start = 0, extent = 0), and
         * the specified closure type.
         *
         * @param type The closure type for the arc:
         * {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
         * @since 1.2
         */
        public Double(int type) {
            super(type);
            
            calculateMinSegments();
        }

        /**
         * Constructs a new arc, initialized to the specified location,
         * size, angular extents, and closure type.
         *
         * @param x The X coordinate of the upper-left corner
         *          of the arc's framing rectangle.
         * @param y The Y coordinate of the upper-left corner
         *          of the arc's framing rectangle.
         * @param w The overall width of the full ellipse of which this
         *          arc is a partial section.
         * @param h The overall height of the full ellipse of which this
         *          arc is a partial section.
         * @param start The starting angle of the arc in degrees.
         * @param extent The angular extent of the arc in degrees.
         * @param type The closure type for the arc:
         * {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
         * @since 1.2
         */
        public Double(double x, double y, double w, double h,
                      double start, double extent, int type) {
            super(type);
            this.x = x;
            this.y = y;
            this.width = w;
            this.height = h;
            this.start = start;
            this.extent = extent;
            
            calculateMinSegments();
        }

        /**
         * Constructs a new arc, initialized to the specified location,
         * size, angular extents, and closure type.
         *
         * @param ellipseBounds The framing rectangle that defines the
         * outer boundary of the full ellipse of which this arc is a
         * partial section.
         * @param start The starting angle of the arc in degrees.
         * @param extent The angular extent of the arc in degrees.
         * @param type The closure type for the arc:
         * {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
         * @since 1.2
         */
        public Double(Rectangle2D ellipseBounds,
                      double start, double extent, int type) {
            super(type);
            this.x = ellipseBounds.getX();
            this.y = ellipseBounds.getY();
            this.width = ellipseBounds.getWidth();
            this.height = ellipseBounds.getHeight();
            this.start = start;
            this.extent = extent;
            
            calculateMinSegments();
        }

        /**
         * {@inheritDoc}
         * Note that the arc
         * <a href="Arc2D.html#inscribes">partially inscribes</a>
         * the framing rectangle of this {@code RectangularShape}.
         *
         * @since 1.2
         */
        public double getX() {
            return x;
        }

        /**
         * {@inheritDoc}
         * Note that the arc
         * <a href="Arc2D.html#inscribes">partially inscribes</a>
         * the framing rectangle of this {@code RectangularShape}.
         *
         * @since 1.2
         */
        public double getY() {
            return y;
        }

        /**
         * {@inheritDoc}
         * Note that the arc
         * <a href="Arc2D.html#inscribes">partially inscribes</a>
         * the framing rectangle of this {@code RectangularShape}.
         *
         * @since 1.2
         */
        public double getWidth() {
            return width;
        }

        /**
         * {@inheritDoc}
         * Note that the arc
         * <a href="Arc2D.html#inscribes">partially inscribes</a>
         * the framing rectangle of this {@code RectangularShape}.
         *
         * @since 1.2
         */
        public double getHeight() {
            return height;
        }

        /**
         * {@inheritDoc}
         * @since 1.2
         */
        public double getAngleStart() {
            return start;
        }

        /**
         * {@inheritDoc}
         * @since 1.2
         */
        public double getAngleExtent() {
            return extent;
        }

        /**
         * {@inheritDoc}
         * @since 1.2
         */
        public boolean isEmpty() {
            return (width <= 0.0 || height <= 0.0);
        }

        /**
         * {@inheritDoc}
         * @since 1.2
         */
        public void setArc(double x, double y, double w, double h,
                           double angSt, double angExt, int closure) {
            this.setArcType(closure);
            this.x = x;
            this.y = y;
            this.width = w;
            this.height = h;
            this.start = angSt;
            this.extent = angExt;
            
            calculateMinSegments();
        }

        /**
         * {@inheritDoc}
         * @since 1.2
         */
        public void setAngleStart(double angSt) {
            this.start = angSt;
            
            calculateMinSegments();
        }

        /**
         * {@inheritDoc}
         * @since 1.2
         */
        public void setAngleExtent(double angExt) {
            this.extent = angExt;
            
            calculateMinSegments();
        }

        /**
         * {@inheritDoc}
         * @since 1.2
         */
        protected Rectangle2D makeBounds(double x, double y,
                                         double w, double h) {
            return new Rectangle2D.Double(x, y, w, h);
        }

        /**
         * Writes the default serializable fields to the
         * <code>ObjectOutputStream</code> followed by a byte
         * indicating the arc type of this <code>Arc2D</code>
         * instance.
         *
         * @serialData
         * <ol>
         * <li>The default serializable fields.
         * <li>
         * followed by a <code>byte</code> indicating the arc type
         * {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
         * </ol>
         */
        private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException
        {
            s.defaultWriteObject();

            s.writeByte(getArcType());
        }

        /**
         * Reads the default serializable fields from the
         * <code>ObjectInputStream</code> followed by a byte
         * indicating the arc type of this <code>Arc2D</code>
         * instance.
         *
         * @serialData
         * <ol>
         * <li>The default serializable fields.
         * <li>
         * followed by a <code>byte</code> indicating the arc type
         * {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
         * </ol>
         */
        private void readObject(java.io.ObjectInputStream s)
            throws java.lang.ClassNotFoundException, java.io.IOException
        {
            s.defaultReadObject();

            try {
                setArcType(s.readByte());
            } catch (IllegalArgumentException iae) {
                throw new java.io.InvalidObjectException(iae.getMessage());
            }
        }
    }

    /**
     * This is an abstract class that cannot be instantiated directly.
     * Type-specific implementation subclasses are available for
     * instantiation and provide a number of formats for storing
     * the information necessary to satisfy the various accessor
     * methods below.
     * <p>
     * This constructor creates an object with a default closure
     * type of {@link #OPEN}.  It is provided only to enable
     * serialization of subclasses.
     *
     * @see java.awt.geom.Arc2D.Float
     * @see java.awt.geom.Arc2D.Double
     */
    Arc2DPrecision() {
        this(OPEN);
    }

    /**
     * This is an abstract class that cannot be instantiated directly.
     * Type-specific implementation subclasses are available for
     * instantiation and provide a number of formats for storing
     * the information necessary to satisfy the various accessor
     * methods below.
     *
     * @param type The closure type of this arc:
     * {@link #OPEN}, {@link #CHORD}, or {@link #PIE}.
     * @see java.awt.geom.Arc2D.Float
     * @see java.awt.geom.Arc2D.Double
     * @since 1.2
     */
    protected Arc2DPrecision(int type) {
        super(type);
    }
    
    protected void calculateMinSegments()
    {
    	double x = getX();
    	double y = -getY();
    	
    	double maxRad;
    	
    	if (x > 0)
    	{
    		// 1st quad
    		if (y > 0)
    		{
    			double x_farthest = x+getWidth();
    			maxRad = Math.sqrt(x_farthest*x_farthest + y*y);
    		}
    		else // 4th quad
    		{
    			double x_farthest = x+getWidth();
    			double y_farthest = y+getHeight();
    			
    			maxRad = Math.sqrt(x_farthest*x_farthest+y_farthest*y_farthest);
    		}
    	}
    	else
    	{
    		// 2nd quad
    		if (y > 0)
    		{
    			maxRad = Math.sqrt(x*x + y*y);
    		}
    		else // 3rd quad
    		{
    			double y_farthest = y-getHeight();
    			
    			maxRad = Math.sqrt(x*x+y_farthest*y_farthest);
    		}
    	}
        
        this.minSegments = (short)Math.max(4,  (short)Math.ceil(3.0*Math.log(maxRad) + 4.0));  
    }

    /**
     * Returns an iteration object that defines the boundary of the
     * arc.
     * This iterator is multithread safe.
     * <code>Arc2D</code> guarantees that
     * modifications to the geometry of the arc
     * do not affect any iterations of that geometry that
     * are already in process.
     *
     * @param at an optional <CODE>AffineTransform</CODE> to be applied
     * to the coordinates as they are returned in the iteration, or null
     * if the untransformed coordinates are desired.
     *
     * @return A <CODE>PathIterator</CODE> that defines the arc's boundary.
     * @since 1.2
     */
    public PathIterator getPathIterator(AffineTransform at) {
        return new ArcIteratorPrecision(this.minSegments, this, at);
    }
}
