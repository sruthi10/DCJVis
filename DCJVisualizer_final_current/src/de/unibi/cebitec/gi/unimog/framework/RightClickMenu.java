package de.unibi.cebitec.gi.unimog.framework;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
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
 * RightClickMenu displays a menu for copy and paste issues specified for two different areas.
 */
public class RightClickMenu extends JPopupMenu implements ClipboardOwner {
	
	private static final long serialVersionUID = 1L;

	private final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

	private final JTextComponent parentText;

	public RightClickMenu(final JTextComponent parentText, final boolean editableContext) {
		
		this.parentText = parentText;
		
		final JMenuItem copyItem = new JMenuItem("Copy");
		copyItem.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
		this.add(copyItem);
		final JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
		final JMenuItem selectAllItem = new JMenuItem("Select All");
		selectAllItem.setAccelerator(KeyStroke.getKeyStroke("ctrl A"));
		
		copyItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(final ActionEvent event) {
				StringSelection textToCopy = new StringSelection(RightClickMenu.this.parentText.getSelectedText());
				RightClickMenu.this.clipboard.setContents(textToCopy, RightClickMenu.this);
			}
		});
		
		selectAllItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(final ActionEvent event){
				RightClickMenu.this.parentText.selectAll();
			}
		});

		//add additional operations for editable text areas
		if (editableContext){			
			final JMenuItem pasteItem = new JMenuItem("Paste");
			final JMenuItem cutItem = new JMenuItem("Cut");
			pasteItem.setAccelerator(KeyStroke.getKeyStroke("ctrl V"));
			cutItem.setAccelerator(KeyStroke.getKeyStroke("ctrl X"));
			
			pasteItem.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(final ActionEvent event) {
					RightClickMenu.this.parentText.paste();
				}
			});
			
			cutItem.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(final ActionEvent event) {
					StringSelection textToCopy = new StringSelection(RightClickMenu.this.parentText.getSelectedText());
					RightClickMenu.this.clipboard.setContents(textToCopy, RightClickMenu.this);
					RightClickMenu.this.parentText.replaceSelection("");
				}
			});
			
		    this.add(pasteItem);
		    this.add(cutItem);
		}
	    this.add(separator);
		this.add(selectAllItem);
	}
	
	 /**
	  * Returns the String residing on the clipboard.
	  *
	  * @return any text found on the clipboard. If none is found, 
	  * an empty String is returned.
	  */
	  public String getClipboardContents() {
	    String result = "";
	    Transferable contents = clipboard.getContents(null);
	    final boolean hasTransferableText = (contents != null) &&
	      	contents.isDataFlavorSupported(DataFlavor.stringFlavor);
	    if (hasTransferableText) {
	      try {
	        result = (String)contents.getTransferData(DataFlavor.stringFlavor);
	      }
	      catch (UnsupportedFlavorException ex){
	        System.err.println("Unsupported DataFlavor for clipboard copying.");
	      }
	      catch (IOException ex) {
	        System.err.println("IOException occured during recovering of text from clipboard.");
	      }
	    }
	    return result;
	  }


	@Override
	public void lostOwnership(final Clipboard clipboard, final Transferable contents) {
		//nothing to do here		
	}
}
