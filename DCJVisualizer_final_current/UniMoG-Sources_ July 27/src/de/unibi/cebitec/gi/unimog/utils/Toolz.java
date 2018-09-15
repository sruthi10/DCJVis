package de.unibi.cebitec.gi.unimog.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.TextAttribute;
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
 * Class containing some basic tools.
 */
public class Toolz {

    public static final Color colorBackg = Color.getHSBColor(0.2f, 0.125f, 1f);
    public static int colorFactor = 0;
    public static int colorFactor2 = 90;
    public static int colorFactor3 = 0;
    public static int colorFactor4 = 90;

    /**
     * Calculates the sum of the numbers up to sumSize.
     * This last value is not included in the sum!
     * @param sumSize The size of the sum
     * @return the sum
     */
    public static int sum(final int sumSize) {
        int nbComparisons = 0;
        for (int i = 1; i < sumSize; ++i) {
            nbComparisons += i;
        }
        return nbComparisons;
    }

    /**
     * Generates a new color for the background according to the chosen color mode
     * by always increasing the r g b values in a different frequency. 
     * @return the new color
     */
//	Color getColor(int i, boolean light){
//		int add = light?127:0;
//		Color c;
//		c = new Color(add+(4&i)*32,add+(2&i)*64,add+(1&i)*128);
//		if (Settings.colorMode==0 && light) c=Color.WHITE;
//		return c;
    public static Color getBackColor() {
//		Color color = Toolz.colorBackg;
//		if (MainFrame.colorMode == Constants.OUTPUT_2){
//			color = new Color(Toolz.colorFactor, 145, Toolz.colorFactor2);
//		} else {
//			return color;
//		}
        Color color = new Color(120, Toolz.colorFactor3, Toolz.colorFactor4);
        Toolz.colorFactor3 += 38;
        if (Toolz.colorFactor3 > 255) {
            Toolz.colorFactor3 -= 255;
        }
        Toolz.colorFactor4 += 86;
        if (Toolz.colorFactor4 > 255) {
            Toolz.colorFactor4 -= 255;
        }

        return color;
    }

    /**
     * Generates a new color by always increasing the r g b values in a different frequency. 
     * @return the new color
     */
    public static Color getColor() {
        Color color = new Color(Toolz.colorFactor, 120, Toolz.colorFactor2);
        Toolz.colorFactor += 38;
        if (Toolz.colorFactor > 255) {
            Toolz.colorFactor = Toolz.colorFactor - 255;
        }
        Toolz.colorFactor2 += 86;
        if (Toolz.colorFactor2 > 255) {
            Toolz.colorFactor2 = Toolz.colorFactor2 - 255;
        }
        return color;
    }

    /**
     * Returns the font used for the output. It fst tries to assign "Lucida Console". 
     * If that font doesn't exist on the system "Courier New" is tried out. If that also
     * doesn't exist the system default "Monospaced" font is used.
     * @return the font to use: plain and size 12
     */
    public static Font getFontOutput() {
        final HashMap<TextAttribute, Object> textAttribute = new HashMap<TextAttribute, Object>();
        //textAttribute.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        Font font = new Font("Lucida Console", Font.PLAIN, 12).deriveFont(textAttribute);
        if (!font.getFontName().equals("Lucida Console")) {
            font = new Font("Courier New", Font.PLAIN, 12).deriveFont(textAttribute);
            if (!font.getFontName().equals("Courier New")) {
                font = new Font("Monospaced", Font.PLAIN, 12).deriveFont(textAttribute);
            }
        }
        return font;
    }
}
