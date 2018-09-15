package de.unibi.cebitec.gi.unimog.framework;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.text.JTextComponent;

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
 * Class handling the mouse event for opening the right-click menu.
 */
public class MenuEvent extends MouseAdapter {
	
	private final boolean editable;

	public MenuEvent(final boolean editable){
		this.editable = editable;
	}
	
	public void mousePressed(final MouseEvent event){
        if (event.isPopupTrigger())
            this.openMenu(event);
    }

    public void mouseReleased(final MouseEvent event){
        if (event.isPopupTrigger())
            this.openMenu(event);
    }

    /**
     * Opens the menu.
     * @param event The Mouse event which triggered this method
     */
    private void openMenu(final MouseEvent event){
    	JTextComponent parentText = (JTextComponent) event.getComponent();
        RightClickMenu menu = new RightClickMenu(parentText, this.editable);
        menu.show(parentText, event.getX(), event.getY());
    }
}
