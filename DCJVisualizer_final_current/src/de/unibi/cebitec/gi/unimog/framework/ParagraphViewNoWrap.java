package de.unibi.cebitec.gi.unimog.framework;

import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

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
 * 
 * @author -Rolf Hilker-
 * 
 * ParagraphView without text wrapping.
 */

public class ParagraphViewNoWrap extends ParagraphView {
    
	public ParagraphViewNoWrap(Element element) {
        super(element);
    }

    @Override
    public void layout(final int width, final int height) {
        super.layout(Short.MAX_VALUE, height);
    }

    @Override
    public float getMinimumSpan(final int axis) {
        return super.getPreferredSpan(axis);
    }
}

class WrapEditorKit extends StyledEditorKit {

	private static final long serialVersionUID = 1L;
	private ViewFactory defaultFactory = new WrapColumnFactory();
    
	/**
	 * Returns the view factory.
	 */
	public ViewFactory getViewFactory() {
		return this.defaultFactory;
    }
}

class WrapColumnFactory implements ViewFactory {
    public View create(Element element) {
        String kind = element.getName();
        if (kind != null) {
            if (kind.equals(AbstractDocument.ParagraphElementName)) {
                return new ParagraphViewNoWrap(element);
            } else if (kind.equals(AbstractDocument.SectionElementName)) {
                return new BoxView(element, View.Y_AXIS);
            } else if (kind.equals(StyleConstants.ComponentElementName)) {
                return new ComponentView(element);
            } else if (kind.equals(StyleConstants.IconElementName)) {
                return new IconView(element);
            }
        }

        // default to text display
        return new LabelView(element);
    }
}