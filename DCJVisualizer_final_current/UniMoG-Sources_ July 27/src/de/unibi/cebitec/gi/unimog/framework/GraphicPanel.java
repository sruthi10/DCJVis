package de.unibi.cebitec.gi.unimog.framework;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;

import de.unibi.cebitec.gi.unimog.algorithms.IntermediateGenomesGenerator;
import de.unibi.cebitec.gi.unimog.datastructure.ChromFragments;
import de.unibi.cebitec.gi.unimog.datastructure.ChromosomeString;
import de.unibi.cebitec.gi.unimog.datastructure.DataOutput;
import de.unibi.cebitec.gi.unimog.datastructure.Pair;
import de.unibi.cebitec.gi.unimog.utils.Constants;
import de.unibi.cebitec.gi.unimog.utils.Toolz;

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
 * Class creating the graphic panel with its content. It visualizes the
 * genomes after each operation.
 * 3 Möglichkeiten zu zeichnen:
 * 1. Alles auf ein Panel -> am besten für kleine Bilder
 * 2. Double buffering in offscreen image -> am besten für nicht zu große Bilder & blitzschnellen Zugriff
 * 3. Einzelne Panels für einzelne Bildbausteine -> am besten für riesige Bilder, da paint() nur von den Komponenten
 * aufgerufen wird, die auch auf dem bildschirm angezeigt werden.
 * 4. Angabe des Bereichs eines Panels, der jeweils gezeichnet werden soll -> schnellste, schönste Methode.
 * Note: um erscheinungsfehler zu beheben: Überschreibe paint & paintAll für jeweils alle genomepanels, die sichtbar sind!
 */
public class GraphicPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private final float fontSize = 14;
    private DataOutput[] results;
    private double zoomF;
    private Scenario scenario;

    public GraphicPanel() {
        super();
        this.setOpaque(true);
    }

    /**
     * Sets the data necessary for a graphical output. When a new result is handed over, all data
     * is always set. Therefore no single setResults method is available.
     * @param results The results to print
     * @param scenario The scenario to use (DCJ, HP, ... see)
     * @param zoomFactor The zoom factor to use
     */
    public void setData(final DataOutput[] results, final Scenario scenario, final double zoomFactor) {
        this.removeAll();
        this.scenario = scenario;
        this.results = results;
        this.zoomF = zoomFactor;
        this.prepareOutput();
    }

    @SuppressWarnings("unchecked")
    public void prepareOutput() {

        this.setLayout(new GridLayout(0, 1));

        //Overall results level
        if (this.results != null && this.results[0].getIntermedGenomes() != null) {
            this.paint(this.getGraphics());

            for (int m = 0; m < this.results.length; ++m) {
                if (this.results[m].getIntermedGenomes() != null) {
                    IntermediateGenomesGenerator[] result = this.results[m].getIntermedGenomes();

                    //Result of one specific pairwise comparison level
                    for (int i = 0; i < result.length; ++i) {
                        if (result[i] != null) { //Remember that for "all" scenario result[1,2,3] are null!

                            // Create description panel for each comparison 
                            final JLabel descriptLabel = new JLabel(this.scenario.getGuiName().concat(" conversion of genome \"").concat(
                                    result[i].getGenomeID1()).concat("\" to \"").concat(
                                    result[i].getGenomeID2()).concat("\":"));
                            descriptLabel.setFont(descriptLabel.getFont().deriveFont((float) (this.fontSize * this.zoomF)));
                            final JPanel descriptPanel = new JPanel(new BorderLayout());
                            descriptPanel.add(descriptLabel, BorderLayout.CENTER);
                            this.add(descriptPanel);
                            boolean emptyComp = true;

                            //Intermediate genomes of one comparison level
                            int doubled = 0;
                            ArrayList<ChromosomeString[]> genomes = result[i].getIntermedGenomes();
                            HashMap<Integer, String> backMap = result[i].getGeneNameMap();
                            ArrayList<Pair<Integer, Integer>> operationList = result[i].getOperationList();
                            int fstExtremity = -1;
                            int scndExtremity = -1;
                            boolean[] beforeGenes;
                            String lastGene = "";
                            String neighbour = "";
                            ChromFragments chromFragments = new ChromFragments();
                            ChromFragments chromFragments2 = new ChromFragments();
                            ArrayList<Pair<String, Color>> fragmentColors;
                            HashMap<String, Color> colorMap;
                            ArrayList<Color> colorList = new ArrayList<Color>();
                            ArrayList<Boolean> chromUnalteredList = new ArrayList<Boolean>();
                            for (int j = 0; j < genomes.size(); ++j) {

                                ////////////// Single intermediate genome level ///////////////////////////////////
                                beforeGenes = new boolean[2];
                                chromFragments2 = chromFragments;
                                chromFragments = new ChromFragments();
                                fragmentColors = new ArrayList<Pair<String, Color>>();
                                colorMap = new HashMap<String, Color>();
                                neighbour = "";
                                final ArrayList<ChromosomePanel> genomePanels = new ArrayList<ChromosomePanel>();
                                if (genomes.get(j).length > 0) {
                                    final JPanel genomePanel = new JPanel();
                                    genomePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
                                    this.add(genomePanel);

                                    //Create label depicting the current step
                                    final JLabel stepLabel = new JLabel("Step " + j + ":");
                                    stepLabel.setFont(stepLabel.getFont().deriveFont((float) (this.fontSize * this.zoomF)));
                                    final JPanel stepPanel = new JPanel(new BorderLayout());
                                    stepPanel.add(stepLabel, BorderLayout.CENTER);
                                    genomePanel.add(stepPanel);
                                    genomePanel.setBackground(this.getBackground());

                                    final ChromosomeString[] currentGenome = genomes.get(j);
                                    Pair<Integer, Integer> currentOperation;
                                    if ((doubled = j * 2) < operationList.size()) {
                                        currentOperation = operationList.get(doubled); //never allowed to be 0, assure that!
                                        fstExtremity = currentOperation.getFirst();
                                        scndExtremity = currentOperation.getSecond();
                                    } else {
                                        fstExtremity = 0;
                                        scndExtremity = 0;
                                    }

                                    for (int k = 0; k < currentGenome.length; ++k) {
                                        ///////// Single chromosome level ///////////////////////////
                                        if (colorList.size() < k + 1 || !chromUnalteredList.get(k)) {
                                            colorList.add(Toolz.getBackColor());
                                        }
                                        final String[] genes = currentGenome[k].getGenes();
                                        final boolean circular = currentGenome[k].isCircular();
                                        ChromosomePanel chromPanel = new ChromosomePanel(genes, circular, fstExtremity,
                                                scndExtremity, chromFragments2, backMap, this.zoomF, colorList.get(k));
                                        genomePanel.add(chromPanel);
                                        genomePanels.add(chromPanel);
                                        chromPanel.paint(chromPanel.getGraphics());

                                        int[] befGene = chromPanel.getBeforeGenes();
                                        if (befGene[0] > -1) {
                                            beforeGenes[0] = befGene[0] == Constants.TRUE ? true : false;
                                            lastGene = chromPanel.getGene();
                                        }
                                        if (befGene[1] > -1 && neighbour.equals("")) {
                                            beforeGenes[1] = befGene[1] == Constants.TRUE ? true : false;
                                            neighbour = chromPanel.getNeighbour();
                                        }
                                        if (!chromPanel.getFragmentColors().isEmpty()) {
                                            fragmentColors.addAll(chromPanel.getFragmentColors());
                                            colorMap.putAll(chromPanel.getCurrentColorMap());
                                        }
                                        if (!chromPanel.hadACut()) {
                                            chromUnalteredList.add(k, true);
                                        } else {
                                            chromUnalteredList.add(k, false);
                                        }
                                    } ///////////////////////////////////////////////////////////////

                                    if (doubled < operationList.size()) {
                                        chromFragments.setBeforeGenes(beforeGenes.clone());
                                        chromFragments.setGene(lastGene);
                                        chromFragments.setNeighbour(neighbour);
                                        chromFragments.setFragmentColors((ArrayList<Pair<String, Color>>) fragmentColors.clone());
                                        chromFragments.setColorMap((HashMap<String, Color>) colorMap.clone());
                                    }
                                    emptyComp = false;
                                }
                            } //////////////////////////////////////////////////////////////////////////////////////
                            if (emptyComp) {
                                this.remove(descriptPanel);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Sets the zoom factor = the size of the output.
     * @param zoomFactor The zoom factor 
     */
    public void setZoomFactor(final double zoomFactor) {
        if (zoomFactor != this.zoomF) {
            final Rectangle visRect = this.getVisibleRect();
            this.zoomF = zoomFactor;
            final Component[] genomePanels = this.getComponents();
            for (Component genomePanel : genomePanels) {
                final JPanel genomePanel2 = (JPanel) genomePanel;

                final Rectangle rect = genomePanel2.getBounds();
                boolean intersects = false;  //Test whether component is in visible rect
                if ((visRect.y < rect.y && rect.y < visRect.y + visRect.height)
                        || (visRect.y < rect.y + rect.height && rect.y + rect.height < visRect.y + visRect.height)) {
                    intersects = true;
                }

                Component[] panels = genomePanel2.getComponents();
                for (Component panel : panels) {
                    try {
                        final ChromosomePanel chromPanel = (ChromosomePanel) panel;
                        chromPanel.setZoomF(zoomFactor, intersects);
                    } catch (ClassCastException ex) {
                        try { //handle description panels
                            final JPanel descriptPanel = (JPanel) panel;
                            JLabel descriptLabel = (JLabel) descriptPanel.getComponent(0);
                            descriptLabel.setFont(descriptLabel.getFont().deriveFont((float) (this.fontSize * this.zoomF)));
                        } catch (ClassCastException ex2) {
                            try {
                                final JLabel headerLabel = (JLabel) panel;
                                headerLabel.setFont(headerLabel.getFont().deriveFont((float) (this.fontSize * this.zoomF)));
                            } catch (ClassCastException ex3) {
                                //nothing to do here, other components are not handled here
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Sets the new output style. 
     */
    public void setColorMode() {
        final Rectangle visRect = this.getVisibleRect();
        final Component[] genomePanels = this.getComponents();
        for (Component genomePanel : genomePanels) {
            final JPanel genomePanel2 = (JPanel) genomePanel;

            final Rectangle rect = genomePanel2.getBounds();
            boolean intersects = false;  //Test whether component is in visible rect
            if ((visRect.y < rect.y && rect.y < visRect.y + visRect.height)
                    || (visRect.y < rect.y + rect.height && rect.y + rect.height < visRect.y + visRect.height)) {
                intersects = true;
            }

            Component[] panels = genomePanel2.getComponents();
            for (Component panel : panels) {
                try {
                    final ChromosomePanel chromPanel = (ChromosomePanel) panel;
                    //chromPanel.setChromBackgColor(Toolz.getBackColor());
                    chromPanel.setChromBackgColor(MainFrame.colorMode);
                    if (intersects) {
                        chromPanel.paint(chromPanel.getGraphics());
                    }
                } catch (ClassCastException ex) {
                    //nothing to do here, other components are not handled here
                }
            }
        }
    }
    
//	/**
//	 * Paints all components of the panel again. Necessary for saving an image.
//	 */
//	public void paintThemAll() {
//		final Component[] genomePanels = this.getComponents();
//		for (Component genomePanel : genomePanels){
//			final JPanel genomePanel2 = (JPanel) genomePanel;			
//			Component[] chromPanels = genomePanel2.getComponents();
//			for (Component chromPanel : chromPanels){
//				chromPanel.paint(chromPanel.getGraphics());
//			}
//		}
//	}
}
